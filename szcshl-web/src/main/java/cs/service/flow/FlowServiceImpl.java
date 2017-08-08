package cs.service.flow;

import cs.common.Constant;
import cs.common.ResultMsg;
import cs.common.utils.ActivitiUtil;
import cs.common.utils.SessionUtil;
import cs.common.utils.Validate;
import cs.domain.flow.HiProcessTask;
import cs.domain.flow.HiProcessTask_;
import cs.domain.flow.RuProcessTask;
import cs.domain.flow.RuProcessTask_;
import cs.domain.project.*;
import cs.domain.sys.User;
import cs.model.PageModelDto;
import cs.model.flow.FlowDto;
import cs.model.flow.FlowHistoryDto;
import cs.model.flow.Node;
import cs.model.flow.TaskDto;
import cs.repository.odata.ODataFilterItem;
import cs.repository.odata.ODataObj;
import cs.repository.repositoryImpl.flow.HiProcessTaskRepo;
import cs.repository.repositoryImpl.flow.RuProcessTaskRepo;
import cs.repository.repositoryImpl.project.DispatchDocRepo;
import cs.repository.repositoryImpl.project.SignRepo;
import cs.repository.repositoryImpl.project.WorkProgramRepo;
import cs.service.project.SignPrincipalService;
import org.activiti.engine.*;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricProcessInstanceQuery;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.bpmn.behavior.UserTaskActivityBehavior;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.PvmActivity;
import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.impl.pvm.process.ProcessDefinitionImpl;
import org.activiti.engine.impl.pvm.process.TransitionImpl;
import org.activiti.engine.impl.task.TaskDefinition;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Comment;
import org.activiti.engine.task.Task;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipInputStream;

@Service
public class FlowServiceImpl implements FlowService {
    private static Logger log = Logger.getLogger(FlowServiceImpl.class);
    @Autowired
    private TaskService taskService;
    @Autowired
    private ProcessEngine processEngine;
    @Autowired
    private HistoryService historyService;
    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    private SignRepo signRepo;
    @Autowired
    private SignPrincipalService signPrincipalService;
    @Autowired
    private RuProcessTaskRepo ruProcessTaskRepo;
    @Autowired
    private HiProcessTaskRepo hiProcessTaskRepo;
    @Autowired
    private WorkProgramRepo workProgramRepo;
    @Autowired
    private DispatchDocRepo dispatchDocRepo;

    /**
     * 回退到上一环节
     *
     * @param flowDto
     * @return
     */
    @Override
    @Transactional
    public ResultMsg rollBackLastNode(FlowDto flowDto) {
        // 取得当前任务
        HistoricTaskInstance currTask = historyService
                .createHistoricTaskInstanceQuery().taskId(flowDto.getTaskId())
                .singleResult();
        if (currTask == null) {
            return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), "操作失败，无法获取任务信息！");
        }
        // 取得流程实例
        ProcessInstance instance = runtimeService
                .createProcessInstanceQuery()
                .processInstanceId(currTask.getProcessInstanceId())
                .singleResult();
        //流程结束
        if (instance == null) {
            return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), "操作失败，流程已结束！");
        }
        // 取得流程定义
        ProcessDefinitionEntity definition = (ProcessDefinitionEntity) (processEngine.getRepositoryService().getProcessDefinition(currTask
                .getProcessDefinitionId()));
        if (definition == null) {
            return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), "操作失败，流程定义数据为空！");
        }
        // 取得上一步活动
        ActivityImpl currActivity = ((ProcessDefinitionImpl) definition).findActivity(currTask.getTaskDefinitionKey());

        // 清除当前活动的出口
        List<PvmTransition> oriPvmTransitionList = new ArrayList<PvmTransition>();
        List<PvmTransition> pvmTransitionList = currActivity.getOutgoingTransitions();
        for (PvmTransition pvmTransition : pvmTransitionList) {
            oriPvmTransitionList.add(pvmTransition);
        }
        pvmTransitionList.clear();

        // 完成任务
        Task task = taskService.createTaskQuery().taskId(flowDto.getTaskId()).active().singleResult();
        if(task == null){
            return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), "操作失败，无法获取任务信息！");
        }

        // 建立新出口
        List<TransitionImpl> newTransitions = new ArrayList<TransitionImpl>();
        Map<String,Object> valiables = new HashMap<>();
        String backActivitiId = "";
        //如果是排他网关，则只能回退到前一个处理人环节，而不是多人
        /*if(flowDto.getBusinessMap().get("ExclusiveGateWay") != null && Boolean.parseBoolean(flowDto.getBusinessMap().get("ExclusiveGateWay").toString())){
            switch (task.getTaskDefinitionKey()){
                case Constant.FLOW_BMLD_QR_GD://确认归档 -》第二负责人确认归档 或者 第一负责人归档
                    Sign sign = signRepo.findById(Sign_.signid.getName(),instance.getBusinessKey());
                    //如果存在第二负责人，则交给第二负责人处理，否则交给第一负责人处理
                    if(Validate.isString(sign.getSecondPriUser())){
                        backActivitiId = Constant.FLOW_AZFR_SP_GD;
                        valiables.put("user",sign.getSecondPriUser());
                    }else{
                        backActivitiId = Constant.FLOW_MFZR_GD;
                        User priUser = signPrincipalService.getMainPriUser(instance.getBusinessKey(), Constant.EnumState.YES.getValue());
                        valiables.put("user",priUser.getLoginName());
                    }
                    break;
                //协审确认归档环节
                case Constant.FLOW_XS_QRGD://确认归档 -》第二负责人确认归档 或者 第一负责人归档
                    Sign xsSign = signRepo.findById(Sign_.signid.getName(),instance.getBusinessKey());
                    //如果存在第二负责人，则交给第二负责人处理，否则交给第一负责人处理
                    if(Validate.isString(xsSign.getSecondPriUser())){
                        backActivitiId = Constant.FLOW_XS_FZR_SP;
                        valiables.put("user",xsSign.getSecondPriUser());
                    }else{
                        backActivitiId = Constant.FLOW_XS_FZR_GD;
                        User priUser = signPrincipalService.getMainPriUser(instance.getBusinessKey(), Constant.EnumState.YES.getValue());
                        valiables.put("user",priUser.getLoginName());
                    }
                    break;
            }
            ActivityImpl nextActivityImpl = ((ProcessDefinitionImpl) definition).findActivity(backActivitiId);
            TransitionImpl newTransition = currActivity.createOutgoingTransition();
            newTransition.setDestination(nextActivityImpl);
            newTransitions.add(newTransition);
        }else{
            List<PvmTransition> nextTransitionList = currActivity.getIncomingTransitions();
            for (PvmTransition nextTransition : nextTransitionList) {
                PvmActivity nextActivity = nextTransition.getSource();
                ActivityImpl nextActivityImpl = ((ProcessDefinitionImpl) definition).findActivity(nextActivity.getId());
                TransitionImpl newTransition = currActivity.createOutgoingTransition();
                newTransition.setDestination(nextActivityImpl);
                newTransitions.add(newTransition);
            }
        }*/

        if(!Validate.isMap(valiables)) {
            valiables = getLastNodeUser(task.getTaskDefinitionKey(), instance.getProcessDefinitionKey(), instance.getBusinessKey(), flowDto.getBusinessMap());
        }
        if(!Validate.isMap(valiables)){
            return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), "操作失败，无法获取环节处理人！");
        }
        taskService.addComment(task.getId(), instance.getId(), flowDto.getDealOption());    //添加处理信息
        taskService.complete(task.getId(),valiables );
        //historyService.deleteHistoricTaskInstance(task.getId());  保留任务的历史数据信息（备注、附件等！）

        // 恢复方向
        for (TransitionImpl transitionImpl : newTransitions) {
            currActivity.getOutgoingTransitions().remove(transitionImpl);
        }
        for (PvmTransition pvmTransition : oriPvmTransitionList) {
            pvmTransitionList.add(pvmTransition);
        }

        return new ResultMsg(true, Constant.MsgCode.OK.getValue(), "操作成功！");
    }

    /**
     * 根据环节定义获取上一环节处理人
     * @param taskDefinitionKey
     * @return
     */
    private Map<String,Object> getLastNodeUser(String taskDefinitionKey,String processKey,String businessKey,Map<String,Object> valiables) {
        if(Validate.isEmpty(taskDefinitionKey)){
            return null;
        }
        Map<String,Object> resultMap = new HashMap<>();
        String assigneeValue = "",otherKey = "";
        List<User> priUserList = null;
        User priUser = null;

        Sign sign = null;
        WorkProgram wk = null;
        DispatchDoc dp = null;
        /*
        switch (taskDefinitionKey){
            case Constant.FLOW_ZHB_SP_SW: //综合部部长-》签收
                sign = signRepo.findById(Sign_.signid.getName(),businessKey);
                resultMap.put("user",sign.getCreatedBy());
                break;

            case Constant.FLOW_FGLD_SP_SW: //分管副主任审批-》综合部部长
                sign = signRepo.findById(Sign_.signid.getName(),businessKey);
                resultMap.put("user",sign.getComprehensiveName());
                break;

            case Constant.FLOW_BZ_SP_GZAN1: //部长审批工作方案（主）-》项目负责人承办（主）
                //查找项目负责人
                priUserList = signPrincipalService.getSignPriUser(businessKey, Constant.EnumState.YES.getValue());
                for (int i = 0, l = priUserList.size(); i < l; i++) {
                    if (i == 0) {
                        assigneeValue = priUserList.get(i).getLoginName();
                    } else {
                        assigneeValue += "," + priUserList.get(i).getLoginName();
                    }
                }
                resultMap.put("users",assigneeValue);
                break;
            case Constant.FLOW_BZ_SP_GZAN2: //部长审批工作方案（协）-》项目负责人承办（协）
                //查找项目负责人
                priUserList = signPrincipalService.getSignPriUser(businessKey, Constant.EnumState.NO.getValue());
                for (int i = 0, l = priUserList.size(); i < l; i++) {
                    if (i == 0) {
                        assigneeValue = priUserList.get(i).getLoginName();
                    } else {
                        assigneeValue += "," + priUserList.get(i).getLoginName();
                    }
                }
                resultMap.put("users",assigneeValue);
                break;

            case Constant.FLOW_FGLD_SP_GZFA1: //分管副主任审批工作方案（主）-》部长审批工作方案（主）
                otherKey = valiables.get("M_WP_ID").toString();
                wk = workProgramRepo.findById(WorkProgram_.id.getName(),otherKey);
                resultMap.put("user",wk.getMinisterName());
                break;

            case Constant.FLOW_FGLD_SP_GZFA2: //分管副主任审批工作方案（协）-》部长审批工作方案（协）
                otherKey = valiables.get("A_WP_ID").toString();
                wk = workProgramRepo.findById(WorkProgram_.id.getName(),otherKey);
                resultMap.put("user",wk.getMinisterName());
                break;

            case Constant.FLOW_BZ_SP_FW://部长审批发文 -》发文填写
                priUserList = signPrincipalService.getSignPriUser(businessKey, Constant.EnumState.YES.getValue());
                for (int i = 0, l = priUserList.size(); i < l; i++) {
                    if (i == 0) {
                        assigneeValue = priUserList.get(i).getLoginName();
                    } else {
                        assigneeValue += "," + priUserList.get(i).getLoginName();
                    }
                }
                resultMap.put("users",assigneeValue);
                break ;

            case Constant.FLOW_FGLD_SP_FW:  //分管领导审批发文 -》部长审批工作方案（发文）
                otherKey = valiables.get("DIS_ID").toString();
                dp = dispatchDocRepo.findById(DispatchDoc_.id.getName(),otherKey);
                resultMap.put("user",dp.getMinisterName());
                break;

            case Constant.FLOW_ZR_SP_FW: //主任审批发文 -》 分管领导审批发文
                otherKey = valiables.get("DIS_ID").toString();
                dp = dispatchDocRepo.findById(DispatchDoc_.id.getName(),otherKey);
                resultMap.put("user",dp.getViceDirectorName());
                break;

            case Constant.FLOW_AZFR_SP_GD: //第二负责人审批归档 -》第一负责人归档
                priUser = signPrincipalService.getMainPriUser(businessKey, Constant.EnumState.YES.getValue());
                resultMap.put("user",priUser.getLoginName());
                break;
            *//**********************************   项目签收流程 end  ****************************************//*

            *//**********************************   项目概算流程 begin  ****************************************//*
            case Constant.FLOW_XS_ZHBBL: //综合部审批 -》 项目签收
                sign = signRepo.findById(Sign_.signid.getName(),businessKey);
                resultMap.put("user",sign.getCreatedBy());
                break;
            case Constant.FLOW_XS_FGLD_SP://分管副主任 -》 综合部审批
                sign = signRepo.findById(Sign_.signid.getName(),businessKey);
                resultMap.put("user",sign.getComprehensiveName());
                break;
            case Constant.FLOW_XS_BZSP_GZFA://部长审批工作方案 -》 填报工作方案
                priUserList = signPrincipalService.getSignPriUser(businessKey, Constant.EnumState.YES.getValue());
                for (int i = 0, l = priUserList.size(); i < l; i++) {
                    if (i == 0) {
                        assigneeValue = priUserList.get(i).getLoginName();
                    } else {
                        assigneeValue += "," + priUserList.get(i).getLoginName();
                    }
                }
                resultMap.put("users",assigneeValue);
                break;
            case Constant.FLOW_XS_FGLDSP_GZFA://分管领导审批工作方案 -》 部长审批工作方案
                otherKey = valiables.get("WP_ID").toString();
                wk = workProgramRepo.findById(WorkProgram_.id.getName(),otherKey);
                resultMap.put("user",wk.getMinisterName());
                break;
            case Constant.FLOW_XS_BZSP_FW://部长审批发文 -》 负责人填报发文
                priUserList = signPrincipalService.getSignPriUser(businessKey, Constant.EnumState.YES.getValue());
                for (int i = 0, l = priUserList.size(); i < l; i++) {
                    if (i == 0) {
                        assigneeValue = priUserList.get(i).getLoginName();
                    } else {
                        assigneeValue += "," + priUserList.get(i).getLoginName();
                    }
                }
                resultMap.put("users",assigneeValue);
                break;
            case Constant.FLOW_XS_FGLDSP_FW://分管领导审批发文 -》 部长审批发文
                otherKey = valiables.get("DIS_ID").toString();
                dp = dispatchDocRepo.findById(DispatchDoc_.id.getName(),otherKey);
                resultMap.put("user",dp.getMinisterName());
                break;
            case Constant.FLOW_XS_ZRSP_FW://主任审批发文 -》 分管领导审批发文
                otherKey = valiables.get("DIS_ID").toString();
                dp = dispatchDocRepo.findById(DispatchDoc_.id.getName(),otherKey);
                resultMap.put("user",dp.getViceDirectorName());
                break;
            case Constant.FLOW_XS_FZR_SP://第二负责人审批归档 -》 第一负责人归档
                priUser = signPrincipalService.getMainPriUser(businessKey, Constant.EnumState.YES.getValue());
                resultMap.put("user",priUser.getLoginName());
                break;
            *//**********************************   项目概算流程 end  ****************************************//*
            default:
                resultMap = null;
        }*/
        return resultMap;
    }


    @Override
    public void rollBackByActiviti(FlowDto flowDto) {
        // 取得当前任务.当前任务节点
        HistoricTaskInstance currTask = historyService.createHistoricTaskInstanceQuery()
                .taskId(flowDto.getTaskId()).singleResult();
        // 取得流程实例，流程实例
        ProcessInstance instance = runtimeService.createProcessInstanceQuery()
                .processInstanceId(currTask.getProcessInstanceId()).singleResult();
        if (instance == null) {
            log.error("流程回退异常，流程已经结束了！");
            return;
        }

        // 取得流程定义
        ProcessDefinitionEntity definition = (ProcessDefinitionEntity) ((RepositoryServiceImpl) repositoryService)
                .getDeployedProcessDefinition(currTask.getProcessDefinitionId());
        if (definition == null) {
            log.error("流程回退异常，流程定义未找到");
            return;
        }
        //取得当前活动节点
        ActivityImpl currActivity = ((ProcessDefinitionImpl) definition).findActivity(currTask.getTaskDefinitionKey());

        //也就是节点间的连线
        //获取来源节点的关系
        List<PvmTransition> nextTransitionList = currActivity.getIncomingTransitions();
        // 清除当前活动的出口
        List<PvmTransition> oriPvmTransitionList = new ArrayList<PvmTransition>();
        //新建一个节点连线关系集合
        //获取出口节点的关系
        List<PvmTransition> pvmTransitionList = currActivity.getOutgoingTransitions();
        //
        for (PvmTransition pvmTransition : pvmTransitionList) {
            oriPvmTransitionList.add(pvmTransition);
        }
        pvmTransitionList.clear();

        // 建立新出口
        List<TransitionImpl> newTransitions = new ArrayList<TransitionImpl>();
        for (PvmTransition nextTransition : nextTransitionList) {
            ActivityImpl nextActivityImpl = ((ProcessDefinitionImpl) definition).findActivity(flowDto.getRollBackActiviti());
            TransitionImpl newTransition = currActivity.createOutgoingTransition();
            newTransition.setDestination(nextActivityImpl);
            newTransitions.add(newTransition);
        }

        // 完成任务
        List<Task> tasks = taskService.createTaskQuery().processInstanceId(instance.getId())
                .taskDefinitionKey(currTask.getTaskDefinitionKey()).list();
        for (Task task : tasks) {
            taskService.addComment(task.getId(), instance.getId(), flowDto.getDealOption());    //添加处理信息
            taskService.complete(task.getId(), ActivitiUtil.flowArguments(null, flowDto.getBackNodeDealUser(), "", false));
            historyService.deleteHistoricTaskInstance(task.getId());
        }
        // 恢复方向
        for (TransitionImpl transitionImpl : newTransitions) {
            currActivity.getOutgoingTransitions().remove(transitionImpl);
        }
        for (PvmTransition pvmTransition : oriPvmTransitionList) {
            pvmTransitionList.add(pvmTransition);
        }
    }

    @Override
    public void nextTaskDefinition(List<Node> nextNodeList, ActivityImpl activityImpl, String activityId) {
        TaskDefinition taskDefinition = null;
        //如果遍历节点为用户任务并且节点不是当前节点信息   
        if ("userTask".equals(activityImpl.getProperty("type")) && !activityId.equals(activityImpl.getId())) {
            //获取该节点下一个节点信息   
            taskDefinition = ((UserTaskActivityBehavior) activityImpl.getActivityBehavior()).getTaskDefinition();
            Node nextNode = new Node();
            nextNode.setActivitiId(taskDefinition.getKey());
            nextNode.setActivitiName(taskDefinition.getNameExpression().getExpressionText());
            nextNodeList.add(nextNode);
        } else {
            //获取节点所有流向线路信息
            List<PvmTransition> outTransitions = activityImpl.getOutgoingTransitions();
            List<PvmTransition> outTransitionsTemp = null;
            if (Validate.isList(outTransitions)) {
                for (PvmTransition tr : outTransitions) {
                    PvmActivity ac = tr.getDestination(); //获取线路的终点节点
                    //如果流向线路为排他网关
                    if ("exclusiveGateway".equals(ac.getProperty("type")) || "inclusiveGateway".equals(ac.getProperty("type"))||"parallelGateway".equals(ac.getProperty("type"))) {
                        outTransitionsTemp = ac.getOutgoingTransitions();
                        if (outTransitionsTemp.size() == 1) {
                            nextTaskDefinition(nextNodeList, (ActivityImpl) outTransitionsTemp.get(0).getDestination(), activityId);
                        } else if (outTransitionsTemp.size() > 1) {
                            for (PvmTransition tr1 : outTransitionsTemp) {
                                nextTaskDefinition(nextNodeList, (ActivityImpl) tr1.getDestination(), activityId);
                            }
                        }
                    } else if ("userTask".equals(ac.getProperty("type"))) {
                        Node nextNode = new Node();
                        nextNode.setActivitiId(ac.getId());
                        nextNode.setActivitiName((String) ac.getProperty("name"));
                        nextNodeList.add(nextNode);
                    }else{
                        String taskType = (String) ac.getProperty("type");
                        System.out.print(taskType);
                    }
                }
            }
        }
    }

    @Override
    public void deployementProcessByName(String path, String sourceName, String flowName) {
        String bpmnName = sourceName + ".bpmn", pngName = sourceName + ".png";
        if (path != null && path.trim().length() > 0) {
            bpmnName = path + File.separatorChar + bpmnName;
            pngName = path + File.separatorChar + pngName;
        }

        InputStream inputStreamBpmn = this.getClass().getResourceAsStream(path + File.pathSeparator + bpmnName);
        InputStream inputStreampng = this.getClass().getResourceAsStream(path + File.pathSeparator + pngName);
        //部署流程定义
        Deployment deployment = processEngine.getRepositoryService()
                .createDeployment()//创建部署对象
                .name(flowName)
                .addInputStream(bpmnName, inputStreamBpmn)//部署加载资源文件
                .addInputStream(pngName, inputStreampng)
                .deploy();

        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().deploymentId(deployment.getId()).singleResult();
        log.info("流程部署成功,流程名称-" + processDefinition.getName() + ",流程ID-" + processDefinition.getId());
    }

    @Override
    public void deployementProcessByZip(String zipPath, String flowName) {
        InputStream in = this.getClass().getClassLoader().getResourceAsStream(zipPath);
        ZipInputStream zipIn = new ZipInputStream(in);
        Deployment deployment = repositoryService.createDeployment().addZipInputStream(zipIn).name(flowName).deploy();
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().deploymentId(deployment.getId()).singleResult();
        log.info("流程部署成功,流程名称-" + processDefinition.getName() + ",流程ID-" + processDefinition.getId());
    }

    @Override
    public ProcessInstance findProcessInstanceByBusinessKey(String businessKey) {
        return runtimeService.createProcessInstanceQuery().processInstanceBusinessKey(businessKey).singleResult();
    }

    @Override
    public PageModelDto<TaskDto> queryETasks(ODataObj odataObj) {
        PageModelDto<TaskDto> pageModelDto = new PageModelDto<TaskDto>();

        HistoryService historyService = processEngine.getHistoryService();
        HistoricProcessInstanceQuery hq = historyService.createHistoricProcessInstanceQuery().finished();

        int total = Integer.valueOf(String.valueOf(hq.count()));
        pageModelDto.setCount(total);

        List<HistoricProcessInstance> historicList = hq.orderByProcessInstanceEndTime().desc().listPage(odataObj.getSkip(), odataObj.getTop());
        if (historicList != null && historicList.size() > 0) {
            List<TaskDto> list = new ArrayList<TaskDto>(historicList.size());
            historicList.forEach(h -> {
                TaskDto taskDto = new TaskDto();
                taskDto.setBusinessKey(h.getBusinessKey());
                taskDto.setBusinessName(h.getName());
                taskDto.setProcessInstanceId(h.getId());
                taskDto.setProcessVariables(h.getProcessVariables());
                taskDto.setProcessDefinitionId(h.getProcessDefinitionId());
                taskDto.setCreateDate(h.getStartTime());
                taskDto.setEndDate(h.getEndTime());
                taskDto.setDurationInMillis(h.getDurationInMillis());
                taskDto.setDurationTime(ActivitiUtil.formatTime(h.getDurationInMillis()));

                list.add(taskDto);
            });
            pageModelDto.setValue(list);
        }

        return pageModelDto;
    }
    /**
     * 在办任务查询
     *
     * @param odataObj
     * @param skip
     * @param top
     * @param isUserDeal 是否为个人待办
     * @return
     */
    @Override
    public PageModelDto<RuProcessTask> queryRunProcessTasks(ODataObj odataObj, String skip, String top, boolean isUserDeal) {
        PageModelDto<RuProcessTask> pageModelDto = new PageModelDto<RuProcessTask>();
        Criteria criteria = ruProcessTaskRepo.getExecutableCriteria();
        // 处理filter
        if (odataObj.getFilter() != null) {
            for (ODataFilterItem oDataFilterItem : odataObj.getFilter()) {
                String field = oDataFilterItem.getField();
                String operator = oDataFilterItem.getOperator();
                Object value = oDataFilterItem.getValue(); 
                switch (operator) {
                    case "like":
                        criteria.add(Restrictions.like(field, "%" + value + "%"));
                        break;
                    case "eq":
                        criteria.add(Restrictions.eq(field, value));
                        break;
                    case "ne":
                        criteria.add(Restrictions.ne(field, value));
                        break;
                    default:
                        break;
                }
            }
        }
        if (isUserDeal) {
            Disjunction dis = Restrictions.disjunction();
            dis.add(Restrictions.eq(RuProcessTask_.assignee.getName(), SessionUtil.getLoginName()));
            dis.add(Restrictions.like(RuProcessTask_.userName.getName(), "%" + SessionUtil.getLoginName() + "%"));
            criteria.add(dis);
        }

        Integer totalResult = ((Number) criteria.setProjection(Projections.rowCount()).uniqueResult()).intValue();
        criteria.setProjection(null);
        // 处理分页
        if (Validate.isString(skip)) {
            criteria.setFirstResult(Integer.valueOf(skip));
        }
        if (Validate.isString(top)) {
            criteria.setMaxResults(Integer.valueOf(top));
        }
        List<RuProcessTask> runProcessList = criteria.list();
        pageModelDto.setCount(totalResult);
        pageModelDto.setValue(runProcessList);
        return pageModelDto;
    }

    /**
     * 根据环节实例ID获取流程的处理记录信息
     * @param processInstanceId
     * @return
     */
    @Override
    public List<HiProcessTask> getProcessHistory(String processInstanceId) {
        Criteria criteria = hiProcessTaskRepo.getExecutableCriteria();
        criteria.add(Restrictions.eq(HiProcessTask_.procInstId.getName(),processInstanceId));
        criteria.addOrder(Order.asc(HiProcessTask_.startTime.getName()));
        List<HiProcessTask> resultList = criteria.list();
        return resultList;
    }

    /**
     * 查询首页的个人待办任务
     * @return
     */
    @Override
    public List<RuProcessTask> queryMyRunProcessTasks() {
        Criteria criteria = ruProcessTaskRepo.getExecutableCriteria();
        Disjunction dis = Restrictions.disjunction();
        dis.add(Restrictions.eq(RuProcessTask_.assignee.getName(), SessionUtil.getLoginName()));
        dis.add(Restrictions.like(RuProcessTask_.userName.getName(), "%" + SessionUtil.getLoginName() + "%"));
        criteria.add(dis);
        criteria.addOrder(Order.desc(RuProcessTask_.createTime.getName()));
        criteria.setMaxResults(6);

        return criteria.list();
    }

    /**
     * 查询首页我的办结任务
     * @return
     */
    @Override
    public List<TaskDto> queryMyEndTasks() {
        HistoryService historyService = processEngine.getHistoryService();
        HistoricProcessInstanceQuery hq = historyService.createHistoricProcessInstanceQuery().finished();
        List<HistoricProcessInstance> historicList = hq.orderByProcessInstanceEndTime().desc().listPage(0, 6);
        List<TaskDto> list = new ArrayList<TaskDto>(historicList == null ? 0 : historicList.size());
        if (historicList != null && historicList.size() > 0) {
            historicList.forEach(h -> {
                TaskDto taskDto = new TaskDto();
                taskDto.setBusinessKey(h.getBusinessKey());
                taskDto.setBusinessName(h.getName());
                taskDto.setProcessInstanceId(h.getId());
                taskDto.setProcessVariables(h.getProcessVariables());
                taskDto.setProcessDefinitionId(h.getProcessDefinitionId());
                taskDto.setCreateDate(h.getStartTime());
                taskDto.setEndDate(h.getEndTime());
                taskDto.setDurationInMillis(h.getDurationInMillis());
                taskDto.setDurationTime(ActivitiUtil.formatTime(h.getDurationInMillis()));
                list.add(taskDto);
            });
        }
        return list;
    }

}