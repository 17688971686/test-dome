package cs.service.flow;

import cs.common.Constant;
import cs.common.ResultMsg;
import cs.common.utils.ActivitiUtil;
import cs.common.utils.SessionUtil;
import cs.common.utils.Validate;
import cs.domain.flow.*;
import cs.domain.project.*;
import cs.domain.sys.User;
import cs.model.PageModelDto;
import cs.model.flow.FlowDto;
import cs.model.flow.Node;
import cs.model.flow.TaskDto;
import cs.repository.odata.ODataObj;
import cs.repository.repositoryImpl.flow.HiProcessTaskRepo;
import cs.repository.repositoryImpl.flow.RuProcessTaskRepo;
import cs.repository.repositoryImpl.flow.RuTaskRepo;
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
import org.activiti.engine.task.Task;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
    private RuProcessTaskRepo ruProcessTaskRepo;
    @Autowired
    private RuTaskRepo ruTaskRepo;
    @Autowired
    private HiProcessTaskRepo hiProcessTaskRepo;

    @Autowired
    @Qualifier("signFlowBackImpl")
    private IFlowBack signFlowBackImpl;
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
        HistoricTaskInstance currTask = historyService.createHistoricTaskInstanceQuery().taskId(flowDto.getTaskId()).singleResult();
        if (currTask == null) {
            return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), "操作失败，无法获取任务信息！");
        }
        // 取得流程实例
        ProcessInstance instance = runtimeService.createProcessInstanceQuery().processInstanceId(currTask.getProcessInstanceId()).singleResult();
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

        String backActivitiId = "";
        // 建立新出口
        List<TransitionImpl> newTransitions = new ArrayList<TransitionImpl>();
        switch (instance.getProcessDefinitionKey()){
            case Constant.SIGN_FLOW:
                backActivitiId = signFlowBackImpl.backActivitiId(instance.getBusinessKey(),task.getTaskDefinitionKey());
                break;
            default:
                backActivitiId = "";
        }
        //如果有指定回退环节，则回退到指定环节
        if(Validate.isString(backActivitiId)){
            ActivityImpl nextActivityImpl = ((ProcessDefinitionImpl) definition).findActivity(backActivitiId);
            TransitionImpl newTransition = currActivity.createOutgoingTransition();
            newTransition.setDestination(nextActivityImpl);
            newTransitions.add(newTransition);
        //没有指定环节，则回退到上一个环节
        }else{
            List<PvmTransition> nextTransitionList = currActivity.getIncomingTransitions();
            for (PvmTransition nextTransition : nextTransitionList) {
                PvmActivity nextActivity = nextTransition.getSource();
                ActivityImpl nextActivityImpl = ((ProcessDefinitionImpl) definition).findActivity(nextActivity.getId());
                TransitionImpl newTransition = currActivity.createOutgoingTransition();
                newTransition.setDestination(nextActivityImpl);
                newTransitions.add(newTransition);
            }
        }

        //取得之前定义的环节处理人信息，不用重新赋值，用之前的就行了（前提是 用户参数是唯一的）
        Map<String,Object> valiables = taskService.getVariables(task.getId());
        taskService.addComment(task.getId(), instance.getId(), flowDto.getDealOption());    //添加处理信息
        taskService.complete(task.getId(),valiables );

        // 恢复方向
        for (TransitionImpl transitionImpl : newTransitions) {
            currActivity.getOutgoingTransitions().remove(transitionImpl);
        }
        for (PvmTransition pvmTransition : oriPvmTransitionList) {
            pvmTransitionList.add(pvmTransition);
        }

        return new ResultMsg(true, Constant.MsgCode.OK.getValue(), "操作成功！");
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
     * @param isUserDeal 是否为个人待办
     * @return
     */
    @Override
    public PageModelDto<RuProcessTask> queryRunProcessTasks(ODataObj odataObj, boolean isUserDeal) {
        PageModelDto<RuProcessTask> pageModelDto = new PageModelDto<RuProcessTask>();
        Criteria criteria = ruProcessTaskRepo.getExecutableCriteria();
        criteria = odataObj.buildFilterToCriteria(criteria);
        if (isUserDeal) {
            Disjunction dis = Restrictions.disjunction();
            dis.add(Restrictions.eq(RuProcessTask_.assignee.getName(), SessionUtil.getUserId()));
            dis.add(Restrictions.like(RuProcessTask_.assigneeList.getName(), "%" + SessionUtil.getUserId() + "%"));
            criteria.add(dis);
        }

        Integer totalResult = ((Number) criteria.setProjection(Projections.rowCount()).uniqueResult()).intValue();
        criteria.setProjection(null);
        // 处理分页
        if (odataObj.getSkip() > 0) {
            criteria.setFirstResult(odataObj.getSkip());
        }
        if (odataObj.getTop() > 0) {
            criteria.setMaxResults(odataObj.getTop());
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
        dis.add(Restrictions.eq(RuProcessTask_.assignee.getName(), SessionUtil.getUserId()));
        dis.add(Restrictions.like(RuProcessTask_.assigneeList.getName(), "%" + SessionUtil.getUserId() + "%"));
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

    @Override
    public PageModelDto<RuProcessTask> queryPersonTasks(ODataObj oDataObj) {
        PageModelDto<RuProcessTask> pageModelDto = new PageModelDto<RuProcessTask>();
        Criteria criteria = ruProcessTaskRepo.getExecutableCriteria();
        criteria = oDataObj.buildFilterToCriteria(criteria);
        Disjunction dis = Restrictions.disjunction();
        dis.add(Restrictions.eq(RuProcessTask_.mainUserId.getName(), SessionUtil.getUserId()));
        criteria.add(dis);
        Integer totalResult = ((Number) criteria.setProjection(Projections.rowCount()).uniqueResult()).intValue();
        criteria.setProjection(null);
        // 处理分页
        if (oDataObj.getSkip() > 0) {
            criteria.setFirstResult(oDataObj.getSkip());
        }
        if (oDataObj.getTop() > 0) {
            criteria.setMaxResults(oDataObj.getTop());
        }
        List<RuProcessTask> runProcessList = criteria.list();
        pageModelDto.setCount(totalResult);
        pageModelDto.setValue(runProcessList);
        return pageModelDto;
    }

    /**
     * 查询我的待办任务（除项目签收流程外）
     * @param oDataObj
     * @return
     */
    @Override
    public PageModelDto<RuTask> queryMyAgendaTask(ODataObj oDataObj) {
        PageModelDto<RuTask> pageModelDto = new PageModelDto<RuTask>();
        Criteria criteria = ruTaskRepo.getExecutableCriteria();
        criteria = oDataObj.buildFilterToCriteria(criteria);
        Disjunction dis = Restrictions.disjunction();
        dis.add(Restrictions.eq(RuTask_.assignee.getName(), SessionUtil.getUserId()));
        dis.add(Restrictions.like(RuTask_.assigneeList.getName(), "%" + SessionUtil.getUserId() + "%"));
        criteria.add(dis);
        Integer totalResult = ((Number) criteria.setProjection(Projections.rowCount()).uniqueResult()).intValue();
        criteria.setProjection(null);
        // 处理分页
        if (oDataObj.getSkip() > 0) {
            criteria.setFirstResult(oDataObj.getSkip());
        }
        if (oDataObj.getTop() > 0) {
            criteria.setMaxResults(oDataObj.getTop());
        }
        List<RuTask> runProcessList = criteria.list();
        pageModelDto.setCount(totalResult);
        pageModelDto.setValue(runProcessList);
        return pageModelDto;
    }

}