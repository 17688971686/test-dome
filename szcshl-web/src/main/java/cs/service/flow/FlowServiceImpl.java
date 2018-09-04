package cs.service.flow;

import cs.ahelper.projhelper.DisUtil;
import cs.ahelper.projhelper.WorkPGUtil;
import cs.common.RandomGUID;
import cs.common.constants.Constant;
import cs.common.constants.Constant.MergeType;
import cs.common.constants.Constant.MsgCode;
import cs.common.constants.FlowConstant;
import cs.common.HqlBuilder;
import cs.common.ResultMsg;
import cs.common.utils.*;
import cs.domain.flow.*;
import cs.domain.project.*;
import cs.domain.sys.Log;
import cs.domain.sys.User;
import cs.domain.topic.WorkPlan;
import cs.model.PageModelDto;
import cs.model.flow.FlowDto;
import cs.model.flow.Node;
import cs.model.flow.TaskDto;
import cs.model.project.CommentDto;
import cs.repository.odata.ODataFilterItem;
import cs.repository.odata.ODataObj;
import cs.repository.odata.ODataObjFilterStrategy;
import cs.repository.repositoryImpl.flow.HiProcessTaskRepo;
import cs.repository.repositoryImpl.flow.RuProcessTaskRepo;
import cs.repository.repositoryImpl.flow.RuTaskRepo;
import cs.repository.repositoryImpl.meeting.RoomBookingRepo;
import cs.repository.repositoryImpl.project.*;
import cs.repository.repositoryImpl.sys.UserRepo;
import cs.repository.repositoryImpl.topic.WorkPlanRepo;
import cs.service.project.AgentTaskService;
import cs.service.project.SignPrincipalService;
import cs.service.project.SignService;
import cs.service.rtx.RTXSendMsgPool;
import cs.service.rtx.RTXService;
import cs.service.sys.LogService;
import cs.service.sys.OrgService;
import cs.service.sys.UserService;
import cs.sql.WorkSql;
import org.activiti.engine.*;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricProcessInstanceQuery;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.impl.pvm.PvmActivity;
import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.impl.pvm.process.ProcessDefinitionImpl;
import org.activiti.engine.impl.pvm.process.TransitionImpl;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.query.NativeQuery;
import org.hibernate.type.StringType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;
import java.util.zip.ZipInputStream;

import static cs.common.constants.FlowConstant.*;
import static cs.common.constants.SysConstants.SEPARATE_COMMA;
import static cs.common.constants.SysConstants.SUPER_ACCOUNT;


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
    private SignService signService;
    @Autowired
    private WorkProgramRepo workProgramRepo;
    @Autowired
    private SignMergeRepo signMergeRepo;
    @Autowired
    private SignDispaWorkRepo signDispaWorkRepo;
    @Autowired
    private SessionFactory sessionFactory;
    @Autowired
    private RoomBookingRepo roomBookingRepo;
    @Autowired
    private WorkPlanRepo workPlanRepo;
    @Autowired
    private SignBranchRepo signBranchRepo;
    @Autowired
    private UserService userService;
    @Autowired
    private OrgService orgService;
    @Autowired
    private RTXService rtxService;
    @Autowired
    @Qualifier("signFlowBackImpl")
    private IFlowBack signFlowBackImpl;
    @Autowired
    @Qualifier("topicFlowBackImpl")
    private IFlowBack topicFlowBackImpl;
    @Autowired
    @Qualifier("appraiseFlowBackImpl")
    private IFlowBack appraiseFlowBackImpl;
    @Autowired
    @Qualifier("archivesFlowBackImpl")
    private IFlowBack archivesFlowBackImpl;
    @Autowired
    @Qualifier("suppLetterFlowBackImpl")
    private IFlowBack suppLetterFlowBackImpl;
    @Autowired
    @Qualifier("annountmentFlowBackImpl")
    private IFlowBack annountmentFlowBackImpl;
    @Autowired
    @Qualifier("monthFlowBackImpl")
    private IFlowBack monthFlowBackImpl;
    @Autowired
    @Qualifier("projectStopFlowBackImpl")
    private IFlowBack projectStopFlowBackImpl;
    @Autowired
    @Qualifier("workHisFlowBackImpl")
    private IFlowBack workHisFlowBackImpl;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private LogService logService;
    @Autowired
    private DispatchDocRepo dispatchDocRepo;
    @Autowired
    private AgentTaskRepo agentTaskRepo;
    @Autowired
    private SignPrincipalService signPrincipalService;
    @Autowired
    private SignRepo signRepo;
    @Autowired
    private AgentTaskService agentTaskService;
    @Autowired
    private UserRepo userRepo;

    /**
     * 回退到上一环节或者指定环节
     *
     * @param flowDto
     * @return
     */
    @Override
    @Transactional
    public ResultMsg rollBackLastNode(FlowDto flowDto) {
        ResultMsg resultMsg = null;
        String errorMsg = "",module = "",businessKey = "",userParam = "",assigneeValue = "";
        DisUtil disUtil = null;
        WorkPGUtil workPGUtil = null;
        Map<String,Object> resultMap = null;
        boolean isProj = false;
        try {
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
            ProcessDefinitionEntity definition = (ProcessDefinitionEntity) (processEngine.getRepositoryService().getProcessDefinition(currTask.getProcessDefinitionId()));
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
            if (task == null) {
                return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), "操作失败，无法获取任务信息！");
            }

            String backActivitiId = "";
            // 建立新出口
            List<TransitionImpl> newTransitions = new ArrayList<TransitionImpl>();
            module = instance.getProcessDefinitionKey();
            businessKey = instance.getBusinessKey();
            switch (module) {
                case FlowConstant.SIGN_FLOW:
                    isProj = true;
                    resultMap = signFlowBackImpl.backActivitiId(instance.getBusinessKey(), task.getTaskDefinitionKey());
                    backActivitiId = resultMap.get(FLOW_BACK_NODEKEY).toString();
                    //如果是回退到工作方案环节，还要修改预定会议室状态和重置分支工作方案状态
                    if (FlowConstant.FLOW_SIGN_XMFZR1.equals(backActivitiId) || FlowConstant.FLOW_SIGN_XMFZR2.equals(backActivitiId)
                            || FlowConstant.FLOW_SIGN_XMFZR3.equals(backActivitiId) || FlowConstant.FLOW_SIGN_XMFZR4.equals(backActivitiId)) {
                        WorkProgram wk = workProgramRepo.findBySignIdAndBranchId(businessKey, backActivitiId.substring(backActivitiId.length() - 1, backActivitiId.length()), false);
                        if (Validate.isObject(wk) && Validate.isString(wk.getId())) {
                            roomBookingRepo.updateStateByBusinessId(wk.getId(), Constant.EnumState.NO.getValue());
                            signService.updateSignProcessState(businessKey, Constant.SignProcessState.DO_WP.getValue());
                            signBranchRepo.resetBranchState(businessKey, wk.getBranchId());
                        }
                        workPGUtil = WorkPGUtil.create(wk);
                        if (FlowConstant.FLOW_SIGN_XMFZR1.equals(backActivitiId)) {
                            //如果是合并评审环节，还要合并回退
                            if (workPGUtil.isMergeWP() && workPGUtil.isMainWP()) {
                               resultMsg = mergerProjBack(businessKey,MergeType.WORK_PROGRAM.getValue(),flowDto.getDealOption());
                               if(Validate.isObject(resultMsg) && (!resultMsg.isFlag() || Constant.MsgCode.ERROR.getValue().equals(resultMsg.getReCode()))){
                                   return resultMsg;
                               }
                            }
                        }
                        //清空审批意见
                        workPGUtil.resetMinisterOption().resetLeaderOption();
                        workProgramRepo.save(workPGUtil.getWorkProgram());

                    }else if (FlowConstant.FLOW_SIGN_FGLD_QRFW.equals(backActivitiId)) {
                        //回退到分管副主任审批环节
                        DispatchDoc dispatchDoc = dispatchDocRepo.findById("signid", businessKey);
                        if (Validate.isObject(dispatchDoc) && Validate.isString(dispatchDoc.getId())) {
                            disUtil = DisUtil.create(dispatchDoc);
                            if(disUtil.isMergeDis() && disUtil.isMainProj()){
                                resultMsg = mergerProjBack(businessKey,MergeType.DIS_MERGE.getValue(),flowDto.getDealOption());
                                if(Validate.isObject(resultMsg) && (!resultMsg.isFlag() || Constant.MsgCode.ERROR.getValue().equals(resultMsg.getReCode()))){
                                    return resultMsg;
                                }
                            }
                            disUtil.resetViceDirectorOption().resetDirectorOption();
                            dispatchDocRepo.save(disUtil.getDispatchDoc());
                        }
                    }else if (FlowConstant.FLOW_SIGN_BMLD_QRFW.equals(backActivitiId)) {
                        //回退到部长审批环节
                        DispatchDoc dispatchDoc = dispatchDocRepo.findById("signid", businessKey);
                        if (Validate.isObject(dispatchDoc) && Validate.isString(dispatchDoc.getId())) {
                            disUtil = DisUtil.create(dispatchDoc);
                            if(disUtil.isMergeDis() && disUtil.isMainProj()){
                                resultMsg = mergerProjBack(businessKey,MergeType.DIS_MERGE.getValue(),flowDto.getDealOption());
                                if(Validate.isObject(resultMsg) && (!resultMsg.isFlag() || Constant.MsgCode.ERROR.getValue().equals(resultMsg.getReCode()))){
                                    return resultMsg;
                                }
                            }
                            disUtil.resetMinisterOption().resetViceDirectorOption().resetDirectorOption();
                            dispatchDocRepo.save(disUtil.getDispatchDoc());
                        }

                    } else if (FlowConstant.FLOW_SIGN_FW.equals(backActivitiId)) {
                        //回退到发文环节
                        DispatchDoc dispatchDoc = dispatchDocRepo.findById("signid", businessKey);
                        if (Validate.isObject(dispatchDoc) && Validate.isString(dispatchDoc.getId())) {
                            disUtil = DisUtil.create(dispatchDoc);
                            if(disUtil.isMergeDis() && disUtil.isMainProj()){
                                resultMsg = mergerProjBack(businessKey,MergeType.DIS_MERGE.getValue(),flowDto.getDealOption());
                                if(Validate.isObject(resultMsg) && (!resultMsg.isFlag() || Constant.MsgCode.ERROR.getValue().equals(resultMsg.getReCode()))){
                                    return resultMsg;
                                }
                            }
                            disUtil.resetMainUserOption().resetSecondUserOption().resetMinisterOption().resetViceDirectorOption().resetDirectorOption();
                            dispatchDocRepo.save(disUtil.getDispatchDoc());
                        }
                    }
                    break;
                case FlowConstant.TOPIC_FLOW:
                    resultMap = topicFlowBackImpl.backActivitiId(instance.getBusinessKey(), task.getTaskDefinitionKey());
                    backActivitiId = resultMap.get(FLOW_BACK_NODEKEY).toString();
                    //如果是回退到工作方案环节，还要修改预定会议室状态
                    if (FlowConstant.TOPIC_GZFA.equals(backActivitiId)) {
                        WorkPlan workPlan = workPlanRepo.findById("topId", instance.getBusinessKey());
                        if (workPlan != null) {
                            roomBookingRepo.updateStateByBusinessId(workPlan.getId(), Constant.EnumState.NO.getValue());
                        }
                    }
                    break;
                case FlowConstant.PROJECT_STOP_FLOW:
                    resultMap = projectStopFlowBackImpl.backActivitiId(instance.getBusinessKey(), task.getTaskDefinitionKey());
                    backActivitiId = resultMap.get(FLOW_BACK_NODEKEY).toString();
                    break;
                case FlowConstant.FLOW_APPRAISE_REPORT:
                    resultMap = appraiseFlowBackImpl.backActivitiId(instance.getBusinessKey(), task.getTaskDefinitionKey());
                    backActivitiId = resultMap.get(FLOW_BACK_NODEKEY).toString();
                    break;
                case FlowConstant.FLOW_ARCHIVES:
                    resultMap = archivesFlowBackImpl.backActivitiId(instance.getBusinessKey(), task.getTaskDefinitionKey());
                    backActivitiId = resultMap.get(FLOW_BACK_NODEKEY).toString();
                    break;
                case FlowConstant.FLOW_SUPP_LETTER:
                    resultMap = suppLetterFlowBackImpl.backActivitiId(instance.getBusinessKey(), task.getTaskDefinitionKey());
                    backActivitiId = resultMap.get(FLOW_BACK_NODEKEY).toString();
                    break;
                case FlowConstant.ANNOUNT_MENT_FLOW:
                    resultMap = annountmentFlowBackImpl.backActivitiId(instance.getBusinessKey(), task.getTaskDefinitionKey());
                    backActivitiId = resultMap.get(FLOW_BACK_NODEKEY).toString();
                    break;
                case FlowConstant.MONTHLY_BULLETIN_FLOW:
                    resultMap = monthFlowBackImpl.backActivitiId(instance.getBusinessKey(), task.getTaskDefinitionKey());
                    backActivitiId = resultMap.get(FLOW_BACK_NODEKEY).toString();
                    break;
                case FlowConstant.WORK_HIS_FLOW:
                    resultMap = workHisFlowBackImpl.backActivitiId(instance.getBusinessKey(), task.getTaskDefinitionKey());
                    backActivitiId = resultMap.get(FLOW_BACK_NODEKEY).toString();
                    break;
                default:
                    backActivitiId = "";
            }
            //如果有指定回退环节，则回退到指定环节
            if (Validate.isString(backActivitiId)) {
                ActivityImpl nextActivityImpl = ((ProcessDefinitionImpl) definition).findActivity(backActivitiId);
                TransitionImpl newTransition = currActivity.createOutgoingTransition();
                newTransition.setDestination(nextActivityImpl);
                newTransitions.add(newTransition);
                //没有指定环节，则回退到上一个环节
            } else {
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
            Map<String, Object> valiables = taskService.getVariables(task.getId());
            taskService.addComment(task.getId(), instance.getId(), flowDto.getDealOption() + "【流程回退】");    //添加处理信息
            taskService.complete(task.getId(), valiables);
            if(Validate.isMap(resultMap)){
                userParam = resultMap.get(FLOW_BACK_USER).toString();
                Object uObj = valiables.get(userParam);
                if(Validate.isObject(uObj)){
                    assigneeValue = uObj.toString();
                }
            }
            // 恢复方向
            for (TransitionImpl transitionImpl : newTransitions) {
                currActivity.getOutgoingTransitions().remove(transitionImpl);
            }
            for (PvmTransition pvmTransition : oriPvmTransitionList) {
                pvmTransitionList.add(pvmTransition);
            }
            resultMsg = new ResultMsg(true, Constant.MsgCode.OK.getValue(), "操作成功！");
            //发送腾讯通消息
            if(Validate.isString(assigneeValue)){
                RTXSendMsgPool.getInstance().sendReceiverIdPool(flowDto.getTaskId(), assigneeValue);
                rtxService.dealPoolRTXMsg(flowDto.getTaskId(),resultMsg,instance.getName(),isProj? Constant.MsgType.project_type.name(): Constant.MsgType.task_type.name());
            }
        } catch (Exception e) {
            errorMsg = e.getMessage();
            log.error("流程回退异常：" + errorMsg);
            resultMsg = new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), "操作失败，错误问题已记录，请联系管理员检查确认！");
        }
        //添加日记记录
        Log log = new Log();
        log.setCreatedDate(new Date());
        log.setUserName(SessionUtil.getDisplayName());
        log.setLogCode(resultMsg.getReCode());
        log.setBuninessId(businessKey);
        log.setMessage(resultMsg.getReMsg() + errorMsg);
        log.setModule(Constant.LOG_MODULE.FLOWBACK.getValue() + FlowConstant.getFLowNameByFlowKey(module));
        log.setResult(resultMsg.isFlag() ? Constant.EnumState.YES.getValue() : Constant.EnumState.NO.getValue());
        log.setLogger(this.getClass().getName() + ".rollBackLastNode");
        //优先级别高
        log.setLogLevel(Constant.EnumState.PROCESS.getValue());
        logService.save(log);

        return resultMsg;
    }

    /**
     * 合并项目回退
     * @param businessKey
     * @param mergeType
     */
    public ResultMsg mergerProjBack(String businessKey,String mergeType,String dealOption){
        ResultMsg resultMsg = null;
        List<SignMerge> mergeList = signMergeRepo.findByType(businessKey,mergeType);
        if (Validate.isList(mergeList)) {
            FlowDto flowDto2 = new FlowDto();
            flowDto2.setDealOption(dealOption);
            for (SignMerge s : mergeList) {
                Task task2 = taskService.createTaskQuery().processInstanceBusinessKey(s.getMergeId()).active().singleResult();
                flowDto2.setTaskId(task2.getId());
                resultMsg = rollBackLastNode(flowDto2);
            }
        }
        return resultMsg;
    }

    @Override
    public void nextTaskDefinition(List<Node> nextNodeList, ActivityImpl activityImpl, String activityId) {
        //TaskDefinition taskDefinition = null;
        //如果遍历节点为用户任务并且节点不是当前节点信息   
        if ("userTask".equals(activityImpl.getProperty("type")) && !activityId.equals(activityImpl.getId())) {
            //获取该节点下一个节点信息
            // = ((UserTaskActivityBehavior) activityImpl.getActivityBehavior()).getTaskDefinition();
            Node nextNode = new Node();
            nextNode.setActivitiId(activityImpl.getId());
            nextNode.setActivitiName(activityImpl.getProperty("name").toString());
            nextNodeList.add(nextNode);

        } else {
            //获取节点所有流向线路信息
            List<PvmTransition> outTransitions = activityImpl.getOutgoingTransitions();
            List<PvmTransition> outTransitionsTemp = null;
            if (Validate.isList(outTransitions)) {
                for (PvmTransition tr : outTransitions) {
                    PvmActivity ac = tr.getDestination(); //获取线路的终点节点
                    //如果流向线路为排他网关
                    if ("exclusiveGateway".equals(ac.getProperty("type")) || "inclusiveGateway".equals(ac.getProperty("type")) || "parallelGateway".equals(ac.getProperty("type"))) {
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
                    } else {
                        String taskType = (String) ac.getProperty("type");
                        //System.out.print(taskType);
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
    public PageModelDto<SignDispaWork> queryETasks(ODataObj odataObj) {
        PageModelDto<SignDispaWork> pageModelDto = new PageModelDto<SignDispaWork>();
        Criteria criteria = signDispaWorkRepo.getExecutableCriteria();
        criteria = odataObj.buildFilterToCriteria(criteria);
        criteria.add(Restrictions.eq(SignDispaWork_.signState.getName(), Constant.EnumState.YES.getValue()));
        if (SUPER_ACCOUNT.equals(SessionUtil.getLoginName()) || SessionUtil.hashRole(Constant.EnumFlowNodeGroupName.DIRECTOR.getValue())) {

        } else {
            if (SessionUtil.hashRole(Constant.EnumFlowNodeGroupName.DEPT_LEADER.getValue())) { //是部门负责人
                criteria.add(Restrictions.eq(SignDispaWork_.ministerName.getName(), SessionUtil.getDisplayName()));
            } else if (SessionUtil.hashRole(Constant.EnumFlowNodeGroupName.VICE_DIRECTOR.getValue())) {//是副主任
                criteria.add(Restrictions.eq(SignDispaWork_.leaderName.getName(), SessionUtil.getDisplayName()));
            } else {//不是主任
                criteria.add(Restrictions.or(Restrictions.like(SignDispaWork_.aUserID.getName(), SessionUtil.getUserId()), Restrictions.like(SignDispaWork_.mUserId.getName(), SessionUtil.getUserId())));
            }
        }

        //统计总数
        Integer totalResult = ((Number) criteria.setProjection(Projections.rowCount()).uniqueResult()).intValue();
        pageModelDto.setCount(totalResult);
        //处理分页
        criteria.setProjection(null);
        if (odataObj.getSkip() > 0) {
            criteria.setFirstResult(odataObj.getSkip());
        }
        if (odataObj.getTop() > 0) {
            criteria.setMaxResults(odataObj.getTop());
        }

        List<SignDispaWork> signDispaWorkList = criteria.list();
        pageModelDto.setValue(signDispaWorkList);
        return pageModelDto;
    }

    /**
     * 在办任务查询(改成前端分页,后端分页效率太低)
     * （个人只能查询个人的，部长（组长）可以查询部门下人员办理的，分管领导可以查询分管部门办理的）
     *
     * @param odataObj
     * @param isUserDeal 是否为个人待办
     * @return
     */
    @Override
    public List<RuProcessTask> queryRunProcessTasks(ODataObj odataObj, boolean isUserDeal, Integer leaderFlag, List<String> orgIdList) {
        Criteria criteria = ruProcessTaskRepo.getExecutableCriteria();
        if (odataObj != null) {
            criteria = odataObj.buildFilterToCriteria(criteria);
        }
        criteria.addOrder(Order.desc(RuProcessTask_.signDate.getName()));
        List<RuProcessTask> runProcessList = null;
        String curUserId = SessionUtil.getUserId();
        //在办任务也包含待办任务，所以要加上这个条件
        if (isUserDeal) {
            runProcessList = findRuProcessTaskByUserId(curUserId);
        } else {
            if (leaderFlag != 1) {
                Disjunction dis2 = Restrictions.disjunction();
                dis2.add(Restrictions.eq(RuProcessTask_.assignee.getName(), curUserId));
                dis2.add(Restrictions.like(RuProcessTask_.assigneeList.getName(), "%" + curUserId + "%"));
                //只要是经办人就能查看
                String querySql = " (SELECT COUNT (ID_) FROM ACT_HI_IDENTITYLINK act WHERE act.USER_ID_ = ? AND act.PROC_INST_ID_ = {alias}.PROCESSINSTANCEID) > 0 ";
                dis2.add(Restrictions.sqlRestriction(querySql, curUserId, StringType.INSTANCE));

                if (leaderFlag == 2 && Validate.isList(orgIdList)) {
                    //分管领导，查询所管辖的部门
                    for (String orgId : orgIdList) {
                        dis2.add(Restrictions.or(Restrictions.eq(RuProcessTask_.mOrgId.getName(), orgId), Restrictions.like(RuProcessTask_.aOrgId.getName(), "%" + orgId + "%")));
                    }
                } else if (leaderFlag == 3 && Validate.isList(orgIdList)) {
                    //部长
                    String orgId = orgIdList.get(0);
                    dis2.add(Restrictions.eq(RuProcessTask_.mOrgId.getName(), orgId));
                    dis2.add(Restrictions.like(RuProcessTask_.aOrgId.getName(), "%" + orgId + "%"));
                    //如果是部长或组长，并且是项目负责人时，还需用户ID查询
                    dis2.add(Restrictions.eq(RuProcessTask_.mainUserId.getName(), curUserId));
                    dis2.add(Restrictions.like(RuProcessTask_.aUserId.getName(), "%" + curUserId + "%"));
                }
                criteria.add(dis2);
                criteria.addOrder(Order.desc(RuProcessTask_.createTime.getName()));
            }
            runProcessList = criteria.list();
        }
        //过滤掉已删除的项目
        List<RuProcessTask> resultList = runProcessList.stream().filter((RuProcessTask rb) -> !(Constant.EnumState.DELETE.getValue()).equals(rb.getSignState()))
                .collect(Collectors.toList());
        if (isUserDeal) {
            //合并评审项目处理
            resultList.forEach(rl -> {
                //如果是合并评审主项目，则查询次项目，如果是合并评审次项目，则查询主项目
                if (Constant.EnumState.YES.getValue().equals(rl.getReviewType())) {
                    rl.setReviewSignDtoList(signService.findReviewSign(rl.getBusinessKey()));
                } else if (Constant.EnumState.NO.getValue().equals(rl.getReviewType())) {
                    rl.setReviewSignDtoList(signService.findMainReviewSign(rl.getBusinessKey()));
                }
            });
        }
        return resultList;
    }



    /**
     * 在办任务查询(改成前端分页,后端分页效率太低)
     * （个人只能查询个人的，部长（组长）可以查询部门下人员办理的，分管领导可以查询分管部门办理的）
     *
     * @param odataObj
     * @param isUserDeal 是否为个人待办
     * @return
     */
    @Override
    public List<RuProcessTask> queryRunTasksForApp(ODataObj odataObj, boolean isUserDeal, Integer leaderFlag, List<String> orgIdList,String curUserId) {
        Criteria criteria = ruProcessTaskRepo.getExecutableCriteria();
        if (odataObj != null) {
            criteria = odataObj.buildFilterToCriteria(criteria);
        }
        criteria.addOrder(Order.desc(RuProcessTask_.signDate.getName()));
        List<RuProcessTask> runProcessList = null;
        //在办任务也包含待办任务，所以要加上这个条件
        if (isUserDeal) {
            runProcessList = findRuProcessTaskByUserId(curUserId);
        } else {
            if (leaderFlag != 1) {
                Disjunction dis2 = Restrictions.disjunction();
                dis2.add(Restrictions.eq(RuProcessTask_.assignee.getName(), curUserId));
                dis2.add(Restrictions.like(RuProcessTask_.assigneeList.getName(), "%" + curUserId + "%"));
                //只要是经办人就能查看
                String querySql = " (SELECT COUNT (ID_) FROM ACT_HI_IDENTITYLINK act WHERE act.USER_ID_ = ? AND act.PROC_INST_ID_ = {alias}.PROCESSINSTANCEID) > 0 ";
                dis2.add(Restrictions.sqlRestriction(querySql, curUserId, StringType.INSTANCE));

                if (leaderFlag == 2 && Validate.isList(orgIdList)) {
                    //分管领导，查询所管辖的部门
                    for (String orgId : orgIdList) {
                        dis2.add(Restrictions.or(Restrictions.eq(RuProcessTask_.mOrgId.getName(), orgId), Restrictions.like(RuProcessTask_.aOrgId.getName(), "%" + orgId + "%")));
                    }
                } else if (leaderFlag == 3 && Validate.isList(orgIdList)) {
                    //部长
                    String orgId = orgIdList.get(0);
                    dis2.add(Restrictions.eq(RuProcessTask_.mOrgId.getName(), orgId));
                    dis2.add(Restrictions.like(RuProcessTask_.aOrgId.getName(), "%" + orgId + "%"));
                    //如果是部长或组长，并且是项目负责人时，还需用户ID查询
                    dis2.add(Restrictions.eq(RuProcessTask_.mainUserId.getName(), curUserId));
                    dis2.add(Restrictions.like(RuProcessTask_.aUserId.getName(), "%" + curUserId + "%"));
                }
                criteria.add(dis2);
                criteria.addOrder(Order.desc(RuProcessTask_.createTime.getName()));
            }
            runProcessList = criteria.list();
        }
        //过滤掉已删除的项目
        List<RuProcessTask> resultList = runProcessList.stream().filter((RuProcessTask rb) -> !(Constant.EnumState.DELETE.getValue()).equals(rb.getSignState()))
                .collect(Collectors.toList());
        if (isUserDeal) {
            //合并评审项目处理
            resultList.forEach(rl -> {
                //如果是合并评审主项目，则查询次项目，如果是合并评审次项目，则查询主项目
                if (Constant.EnumState.YES.getValue().equals(rl.getReviewType())) {
                    rl.setReviewSignDtoList(signService.findReviewSign(rl.getBusinessKey()));
                } else if (Constant.EnumState.NO.getValue().equals(rl.getReviewType())) {
                    rl.setReviewSignDtoList(signService.findMainReviewSign(rl.getBusinessKey()));
                }
            });
        }
        return resultList;
    }



    /**
     * app个人待办项目
     * @param odataObj
     * @param
     * @return
     */
    @Override
    public List<RuProcessTask> queryAgendaTaskForApp(ODataObj odataObj,String curUserId) {
        Criteria criteria = ruProcessTaskRepo.getExecutableCriteria();
        if (odataObj != null) {
            criteria = odataObj.buildFilterToCriteria(criteria);
        }
        criteria.addOrder(Order.desc(RuProcessTask_.signDate.getName()));
         List<RuProcessTask> runProcessList = null;
        runProcessList = findRuProcessTaskByUserId(curUserId);
        //过滤掉已删除的项目
        List<RuProcessTask> resultList = runProcessList.stream().filter((RuProcessTask rb) -> !(Constant.EnumState.DELETE.getValue()).equals(rb.getSignState()))
                .collect(Collectors.toList());
            //合并评审项目处理
            resultList.forEach(rl -> {
                //如果是合并评审主项目，则查询次项目，如果是合并评审次项目，则查询主项目
                if (Constant.EnumState.YES.getValue().equals(rl.getReviewType())) {
                    rl.setReviewSignDtoList(signService.findReviewSign(rl.getBusinessKey()));
                } else if (Constant.EnumState.NO.getValue().equals(rl.getReviewType())) {
                    rl.setReviewSignDtoList(signService.findMainReviewSign(rl.getBusinessKey()));
                }
            });
        return resultList;
    }

    /**
     * 根据用户ID查询待办任务
     *
     * @param curUserId
     * @return
     */
    @Override
    public List<RuProcessTask> findRuProcessTaskByUserId(String curUserId) {
        if(!Validate.isString(curUserId)) {
            curUserId = SessionUtil.getUserId();
        }
        Criteria criteria = ruProcessTaskRepo.getExecutableCriteria();
        //排除合并评审阶段的次项目数据
        Disjunction disj = Restrictions.disjunction();
        disj.add(Restrictions.isNull(RuProcessTask_.reviewType.getName()));
        disj.add(Restrictions.eq(RuProcessTask_.reviewType.getName(), ""));
        disj.add(Restrictions.eq(RuProcessTask_.reviewType.getName(), Constant.EnumState.YES.getValue()));
        disj.add(Restrictions.sqlRestriction(" ( {alias}." + RuProcessTask_.reviewType.getName() + "= '" + Constant.EnumState.NO.getValue()
                + "' and {alias}." + RuProcessTask_.nodeDefineKey.getName() + " != '" + FlowConstant.FLOW_SIGN_BMLD_SPW1
                + "' and {alias}." + RuProcessTask_.nodeDefineKey.getName() + " != '" + FlowConstant.FLOW_SIGN_FGLD_SPW1 + "' )"));
        criteria.add(disj);

        Disjunction dis = Restrictions.disjunction();
        dis.add(Restrictions.eq(RuProcessTask_.assignee.getName(), curUserId));
        dis.add(Restrictions.like(RuProcessTask_.assigneeList.getName(), "%" + curUserId + "%"));
        criteria.add(dis);
        criteria.addOrder(Order.desc(RuProcessTask_.createTime.getName()));

        return criteria.list();
    }

    /**
     * 根据用户ID查询待办任务
     *
     * @param userId
     * @return
     */
    @Override
    public List<RuTask> findRuTaskByUserId(String userId) {
        Criteria criteria = ruTaskRepo.getExecutableCriteria();
        Disjunction dis = Restrictions.disjunction();
        dis.add(Restrictions.eq(RuTask_.assignee.getName(), SessionUtil.getUserId()));
        dis.add(Restrictions.like(RuTask_.assigneeList.getName(), "%" + SessionUtil.getUserId() + "%"));
        criteria.add(dis);
        return criteria.list();
    }

    /**
     * 根据环节实例ID获取流程的处理记录信息
     *
     * @param processInstanceId
     * @return
     */
    @Override
    public List<HiProcessTask> getProcessHistory(String processInstanceId) {
        Criteria criteria = hiProcessTaskRepo.getExecutableCriteria();
        criteria.add(Restrictions.eq(HiProcessTask_.procInstId.getName(), processInstanceId));
        //先根据办结时间排序，再根据开始时间排序
        criteria.addOrder(Order.asc(HiProcessTask_.startTime.getName()));
        criteria.addOrder(Order.asc(HiProcessTask_.endTime.getName()));
        List<HiProcessTask> resultList = criteria.list();
        return resultList;
    }

    /**
     * 查询首页的个人待办任务
     *
     * @return
     */
    @Override
    public List<RuProcessTask> queryMyRunProcessTasks(Integer max) {
        Criteria criteria = ruProcessTaskRepo.getExecutableCriteria();
        Disjunction dis = Restrictions.disjunction();
        dis.add(Restrictions.eq(RuProcessTask_.assignee.getName(), SessionUtil.getUserId()));
        dis.add(Restrictions.like(RuProcessTask_.assigneeList.getName(), "%" + SessionUtil.getUserId() + "%"));
        criteria.add(Restrictions.ne(RuProcessTask_.signState.getName(), Constant.EnumState.DELETE.getValue()));
        criteria.add(dis);
        //排除合并评审阶段的次项目数据
        Disjunction dis2 = Restrictions.disjunction();
        dis2.add(Restrictions.isNull(RuProcessTask_.reviewType.getName()));
        dis2.add(Restrictions.eq(RuProcessTask_.reviewType.getName(), ""));
        dis2.add(Restrictions.eq(RuProcessTask_.reviewType.getName(), Constant.EnumState.YES.getValue()));
        dis2.add(Restrictions.sqlRestriction(" ( {alias}." + RuProcessTask_.reviewType.getName() + "= '" + Constant.EnumState.NO.getValue()
                + "' and {alias}." + RuProcessTask_.nodeDefineKey.getName() + " != '" + FlowConstant.FLOW_SIGN_BMLD_SPW1
                + "' and {alias}." + RuProcessTask_.nodeDefineKey.getName() + " != '" + FlowConstant.FLOW_SIGN_FGLD_SPW1 + "' )"));
        criteria.add(dis2);
        criteria.addOrder(Order.desc(RuProcessTask_.createTime.getName()));
        if (max != null && max > 0) {
            criteria.setMaxResults(max);
        }
        return criteria.list();
    }

    /**
     * 查询首页我的办结任务
     *
     * @return
     */
    /*@Override
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
                taskDto.setFlowKey(h.getProcessDefinitionId().substring(0, h.getProcessDefinitionId().indexOf(":")));
                list.add(taskDto);
            });
        }
        return list;
    }*/

    @Override
    public List<RuTask> queryMyHomeAgendaTask() {
        Criteria criteria = ruTaskRepo.getExecutableCriteria();
        Disjunction dis = Restrictions.disjunction();
        dis.add(Restrictions.eq(RuTask_.assignee.getName(), SessionUtil.getUserId()));
        dis.add(Restrictions.like(RuTask_.assigneeList.getName(), "%" + SessionUtil.getUserId() + "%"));
        criteria.add(dis);
        criteria.addOrder(Order.desc(RuTask_.createTime.getName()));
        criteria.setMaxResults(6);
        return criteria.list();
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
     *
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

    /**
     * 所有在办任务
     *
     * @param odataObj
     * @return
     */
    @Override
    public PageModelDto<RuTask> queryAgendaTask(ODataObj odataObj) {
        PageModelDto<RuTask> pageModelDto = new PageModelDto<>();
        Criteria criteria = ruTaskRepo.getExecutableCriteria();
        if (odataObj != null) {
            criteria = odataObj.buildFilterToCriteria(criteria);
        }
        criteria.addOrder(Order.desc(RuTask_.createTime.getName()));
        List<RuTask> runProcessList = criteria.list();
        boolean isHaveAllPermission = SessionUtil.hashRole(Constant.EnumFlowNodeGroupName.DIRECTOR.getValue())
                || SUPER_ACCOUNT.equals(SessionUtil.getLoginName());
        if (isHaveAllPermission) {
            pageModelDto.setCount(runProcessList.size());
            pageModelDto.setValue(runProcessList);
        } else {
            List<RuTask> resultList = new ArrayList<>();
            //查找用户权限
            Map<String, Object> authMap = userService.getUserSignAuth();
            Integer authFlag = new Integer(authMap.get("leaderFlag").toString());
            List<String> orgIdList = (List<String>) authMap.get("orgIdList");
            if (authFlag != 1) {
                String curUserId = SessionUtil.getUserId();
                for (RuTask ruTask : runProcessList) {
                    List<String> userIdList = findUserIdByProcessInstanceId(ruTask.getInstanceId());
                    //如果是经办人，则可以查看
                    if (userIdList.contains(curUserId)) {
                        resultList.add(ruTask);
                        continue;
                    }
                    //如果当前代办人，可以查看
                    if (curUserId.equals(ruTask.getAssignee()) || (ruTask.getAssigneeList() != null && ruTask.getAssigneeList().contains(curUserId))) {
                        resultList.add(ruTask);
                        continue;
                    }
                    //分管领导或者部长
                    if (authFlag != 0) {
                        for (String orgId : orgIdList) {
                            boolean isOrgUser = orgService.checkIsOrgUer(orgId, Validate.isString(ruTask.getAssignee()) ? ruTask.getAssignee() : ruTask.getAssigneeList());
                            if (isOrgUser) {
                                resultList.add(ruTask);
                                break;
                            }
                        }
                    }
                }
            }

            pageModelDto.setCount(resultList.size());
            pageModelDto.setValue(resultList);
        }
        return pageModelDto;

    }

    /**
     * 流程处理，只有一个分支的情况
     *
     * @param businessKey 业务ID
     * @param activiName  环节名称
     * @return
     */
    @Override
    public ResultMsg dealFlowByBusinessKey(String businessKey, String activiName, FlowDto flowDto, String definitionKey) throws Exception {
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceBusinessKey(businessKey).singleResult();
        if (processInstance == null) {
            return new ResultMsg(false, Constant.MsgCode.FLOW_INSTANCE_NULL.getValue(), "流程已处理完毕！");
        }
        Task task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).active().singleResult();
        if (task == null) {
            return new ResultMsg(false, Constant.MsgCode.FLOW_TASK_NULL.getValue(), "流程已处理完毕！");
        }
        if (!task.getTaskDefinitionKey().equals(activiName)) {
            return new ResultMsg(false, Constant.MsgCode.FLOW_ACTIVI_NEQ.getValue(), "环节名称不一致！");
        }

        ResultMsg resultMsg = null;
        switch (processInstance.getProcessDefinitionKey()) {
            case FlowConstant.SIGN_FLOW:
                resultMsg = signService.dealFlow(processInstance, task, flowDto);
                break;
        }
        if (resultMsg == null) {
            resultMsg = new ResultMsg(false, Constant.MsgCode.FLOW_NOT_MATCH.getValue(), "没有匹配的流程！");
        }
        return resultMsg;
    }

    /**
     * 取回流程
     *
     * @param taskId      当前任务ID
     * @param activityId  取回节点ID
     * @param businessKey 删除工作方案的singId
     * @return ResultMsg
     */
    @Override
    public ResultMsg callBackProcess(String taskId, String activityId, String businessKey, boolean allBranch) throws Exception {
        if (!Validate.isString(activityId)) {
            throw new Exception("目标节点ID为空！");
        }
        ProcessInstance ProcessInstance = findProcessInstanceByTaskId(taskId);
        taskService.addComment(taskId, ProcessInstance.getId(), "【" + SessionUtil.getDisplayName() + "】重新分办");    //添加处理信息
        if (allBranch) {
            // 如果是删除所有分支，查找所有并行任务节点，同时取回
            List<Task> taskList = findTaskListByKey(ProcessInstance.getId());
            for (Task task : taskList) {
                if (task.getId().equals(taskId)) {
                    //取回项目流程
                    commitProcess(task.getId(), null, activityId);
                } else {
                    //删除流程实例
                    deleteTask(task.getId(), task.getExecutionId());
                }
            }
        } else {
            //如果只是取回当前任务
            Task task = taskService.createTaskQuery().taskId(taskId).active().singleResult();
            if (task != null) {
                commitProcess(task.getId(), null, activityId);
            } else {
                return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), "操作失败，该任务已提交！请重新刷新再试!");
            }
        }
        return new ResultMsg(true, Constant.MsgCode.OK.getValue(), "操作成功！");
    }

    /**
     * 根据任务ID获取对应的流程实例
     *
     * @param taskId 任务ID
     * @return
     * @throws Exception
     */
    private ProcessInstance findProcessInstanceByTaskId(String taskId)
            throws Exception {
        // 找到流程实例
        ProcessInstance processInstance = runtimeService
                .createProcessInstanceQuery().processInstanceId(findTaskById(taskId).getProcessInstanceId()).singleResult();
        if (processInstance == null) {
            throw new Exception("流程实例未找到!");
        }
        return processInstance;
    }

    /**
     * 根据流程实例ID查询所有任务集合
     *
     * @param processInstanceId
     * @return
     */
    @Override
    public List<Task> findTaskListByKey(String processInstanceId) {
        return taskService.createTaskQuery().processInstanceId(processInstanceId).list();
    }

    /**
     * 根据业务ID，暂停流程
     *
     * @param businessKey
     * @return
     */
    @Override
    public ResultMsg stopFlow(String businessKey) {
        ResultMsg resultMsg = null;
        String errorMsg = "";
        String module = "";
        try {
            ProcessInstance processInstance = findProcessInstanceByBusinessKey(businessKey);
            runtimeService.suspendProcessInstanceById(processInstance.getId());
            module = processInstance.getProcessDefinitionKey();
            resultMsg = new ResultMsg(true, Constant.MsgCode.OK.getValue(), "操作成功！");
        } catch (Exception e) {
            errorMsg = e.getMessage();
            log.error("流程回退异常：" + errorMsg);
            resultMsg = new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), "操作失败，错误问题已记录，请联系管理员检查确认！");
        }

        //添加日记记录
        Log log = new Log();
        log.setCreatedDate(new Date());
        log.setBuninessId(businessKey);
        log.setUserName(SessionUtil.getDisplayName());
        log.setLogCode(resultMsg.getReCode());
        log.setMessage(resultMsg.getReMsg() + errorMsg);
        log.setModule(Constant.LOG_MODULE.FLOWSTOP.getValue() + FlowConstant.getFLowNameByFlowKey(module));
        log.setResult(resultMsg.isFlag() ? Constant.EnumState.YES.getValue() : Constant.EnumState.NO.getValue());
        log.setLogger(this.getClass().getName() + ".stopFlow");
        //优先级别高
        log.setLogLevel(Constant.EnumState.PROCESS.getValue());
        logService.save(log);

        return resultMsg;
    }

    /**
     * 根据业务ID，激活流程
     *
     * @param businessKey
     * @return
     */
    @Override
    @Transactional
    public ResultMsg restartFlow(String businessKey) {
        ResultMsg resultMsg = null;
        String errorMsg = "";
        String module = "";
        //激活流程
        try {
            ProcessInstance processInstance = findProcessInstanceByBusinessKey(businessKey);
            if (processInstance != null) {
                module = processInstance.getProcessDefinitionKey();
                switch (module) {
                    case FlowConstant.SIGN_FLOW:
                        signService.activateFlow(businessKey);
                        break;
                    default:
                        ;
                }
                //如果是暂停，则重新启动
                if (processInstance.isSuspended()) {
                    runtimeService.activateProcessInstanceById(processInstance.getId());
                }
                resultMsg = new ResultMsg(true, Constant.MsgCode.OK.getValue(), "操作成功！");
            } else {
                resultMsg = new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), "流程实例已被删除！");
            }

        } catch (Exception e) {
            errorMsg = e.getMessage();
            log.info("流程重新激活异常：" + errorMsg);
            resultMsg = new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), "操作异常：" + e.getMessage());
        }
        //添加日记记录
        Log log = new Log();
        log.setCreatedDate(new Date());
        log.setUserName(SessionUtil.getDisplayName());
        log.setLogCode(resultMsg.getReCode());
        log.setBuninessId(businessKey);
        log.setMessage(resultMsg.getReMsg() + errorMsg);
        log.setModule(Constant.LOG_MODULE.FLOWACTIVE.getValue() + FlowConstant.getFLowNameByFlowKey(module));
        log.setResult(resultMsg.isFlag() ? Constant.EnumState.YES.getValue() : Constant.EnumState.NO.getValue());
        log.setLogger(this.getClass().getName() + ".restartFlow");
        //优先级别高
        log.setLogLevel(Constant.EnumState.PROCESS.getValue());
        logService.save(log);
        return resultMsg;
    }

    /**
     * @param taskId     当前任务ID
     * @param variables  流程变量
     * @param activityId 流程转向执行任务节点ID<br>
     *                   此参数为空，默认为提交操作
     * @throws Exception
     */
    private void commitProcess(String taskId, Map<String, Object> variables, String activityId) throws Exception {
        if (variables == null) {
            variables = new HashMap<String, Object>();
        }
        // 跳转节点为空，默认提交操作
        if (!Validate.isString(activityId)) {
            taskService.complete(taskId, variables);
        } else {// 流程转向操作
            turnTransition(taskId, activityId, variables);
        }
    }

    /**
     * 流程转向操作
     *
     * @param taskId     当前任务ID
     * @param activityId 目标节点任务ID
     * @param variables  流程变量
     * @throws Exception
     */
    private void turnTransition(String taskId, String activityId,
                                Map<String, Object> variables) throws Exception {
        // 当前节点
        ActivityImpl currActivity = findActivitiImpl(taskId, null);
        // 清空当前流向
        List<PvmTransition> oriPvmTransitionList = clearTransition(currActivity);

        // 创建新流向
        TransitionImpl newTransition = currActivity.createOutgoingTransition();
        // 目标节点
        ActivityImpl pointActivity = findActivitiImpl(taskId, activityId);
        // 设置新流向的目标节点
        newTransition.setDestination(pointActivity);

        // 执行转向任务
        taskService.complete(taskId, variables);
        // 删除目标节点新流入
        pointActivity.getIncomingTransitions().remove(newTransition);

        // 还原以前流向
        restoreTransition(currActivity, oriPvmTransitionList);
    }

    /**
     * 清空指定活动节点流向
     *
     * @param activityImpl 活动节点
     * @return 节点流向集合
     */
    private List<PvmTransition> clearTransition(ActivityImpl activityImpl) {
        // 存储当前节点所有流向临时变量
        List<PvmTransition> oriPvmTransitionList = new ArrayList<PvmTransition>();
        // 获取当前节点所有流向，存储到临时变量，然后清空
        List<PvmTransition> pvmTransitionList = activityImpl
                .getOutgoingTransitions();
        for (PvmTransition pvmTransition : pvmTransitionList) {
            oriPvmTransitionList.add(pvmTransition);
        }
        pvmTransitionList.clear();

        return oriPvmTransitionList;
    }

    /**
     * 根据任务ID和节点ID获取活动节点 <br>
     *
     * @param taskId     任务ID
     * @param activityId 活动节点ID <br>
     *                   如果为null或""，则默认查询当前活动节点 <br>
     *                   如果为"end"，则查询结束节点 <br>
     * @return
     * @throws Exception
     */
    private ActivityImpl findActivitiImpl(String taskId, String activityId)
            throws Exception {
        // 取得流程定义
        ProcessDefinitionEntity processDefinition = findProcessDefinitionEntityByTaskId(taskId);

        // 获取当前活动节点ID
        if (!Validate.isString(activityId)) {
            activityId = findTaskById(taskId).getTaskDefinitionKey();
        }

        // 根据流程定义，获取该流程实例的结束节点
        if (activityId.toUpperCase().equals("END")) {
            for (ActivityImpl activityImpl : processDefinition.getActivities()) {
                List<PvmTransition> pvmTransitionList = activityImpl
                        .getOutgoingTransitions();
                if (pvmTransitionList.isEmpty()) {
                    return activityImpl;
                }
            }
        }

        // 根据节点ID，获取对应的活动节点
        ActivityImpl activityImpl = ((ProcessDefinitionImpl) processDefinition).findActivity(activityId);

        return activityImpl;
    }

    /**
     * 根据任务ID获得任务实例
     *
     * @param taskId 任务ID
     * @return
     * @throws Exception
     */
    private TaskEntity findTaskById(String taskId) throws Exception {
        TaskEntity task = (TaskEntity) taskService.createTaskQuery().taskId(taskId).singleResult();
        if (task == null) {
            throw new Exception("任务实例未找到!");
        }
        return task;
    }

    /**
     * 根据任务ID获取流程定义
     *
     * @param taskId 任务ID
     * @return
     * @throws Exception
     */
    private ProcessDefinitionEntity findProcessDefinitionEntityByTaskId(String taskId) throws Exception {
        // 取得流程定义
        ProcessDefinitionEntity processDefinition = (ProcessDefinitionEntity) ((RepositoryServiceImpl) repositoryService)
                .getDeployedProcessDefinition(findTaskById(taskId).getProcessDefinitionId());

        if (processDefinition == null) {
            throw new Exception("流程定义未找到!");
        }

        return processDefinition;
    }

    /**
     * 还原指定活动节点流向
     *
     * @param activityImpl         活动节点
     * @param oriPvmTransitionList 原有节点流向集合
     */
    private void restoreTransition(ActivityImpl activityImpl,
                                   List<PvmTransition> oriPvmTransitionList) {
        // 清空现有流向
        List<PvmTransition> pvmTransitionList = activityImpl
                .getOutgoingTransitions();
        pvmTransitionList.clear();
        // 还原以前流向
        for (PvmTransition pvmTransition : oriPvmTransitionList) {
            pvmTransitionList.add(pvmTransition);
        }
    }

    /**
     * 删除流程任务实例
     *
     * @param taskId      任务节点ID
     * @param executionId 流程实例节点ID
     */
    @Override
    public void deleteTask(String taskId, String executionId) {
        //直接执行SQL
        //DELETE FROM act_ru_identitylink WHERE TASK_ID_='135068'(act_ru_task主键)
        //流程实例,通过EXECUTION_ID_字段和act_ru_execution关联
        //DELETE FROM act_ru_task WHERE ID_='135068'
        //任务节点表
        //DELETE FROM act_ru_execution WHERE ID_='135065'
        Session session = sessionFactory.getCurrentSession();
        StringBuffer stringBuffer = new StringBuffer();
        NativeQuery nativeQuery = null;

        stringBuffer.append("DELETE FROM act_ru_identitylink WHERE TASK_ID_=:taskId");
        nativeQuery = session.createNativeQuery(stringBuffer.toString());
        nativeQuery.setParameter("taskId", taskId).executeUpdate();

        stringBuffer.setLength(0);
        stringBuffer.append("DELETE FROM act_ru_task WHERE ID_=:taskId");
        nativeQuery = session.createNativeQuery(stringBuffer.toString());
        nativeQuery.setParameter("taskId", taskId).executeUpdate();

        stringBuffer.setLength(0);
        stringBuffer.append("DELETE FROM act_ru_execution WHERE ID_=:executionId");
        nativeQuery = session.createNativeQuery(stringBuffer.toString());
        nativeQuery.setParameter("executionId", executionId).executeUpdate();

    }

    /**
     * 获取流程列表
     */
    @Override
    public List<Map<String, Object>> getProc() {
        List<Map<String, Object>> list = jdbcTemplate.queryForList("SELECT arp.NAME_,arp.KEY_ FROM act_re_procdef arp where arp.KEY_ != 'FINAL_SIGN_FLOW' GROUP BY arp.NAME_,arp.KEY_");
        return list;
    }

    /**
     * 根据流程实例，获取经办人ID
     *
     * @param processInstanceId
     * @return
     */
    @Override
    public List<String> findUserIdByProcessInstanceId(String processInstanceId) {
        /*HqlBuilder sqlBuilder = HqlBuilder.create();
        sqlBuilder.append("select distinct ACT.USER_ID_ from ACT_HI_IDENTITYLINK act where act.PROC_INST_ID_ = :processInstanceId");
        sqlBuilder.setParam("processInstanceId", processInstanceId);*/
        List<Map<String, Object>> resultMapList = jdbcTemplate.queryForList("select distinct ACT.USER_ID_ USER_ID from ACT_HI_IDENTITYLINK act where act.PROC_INST_ID_ = '" + processInstanceId + "'");
        List<String> resultList = new ArrayList<>();
        for (Map<String, Object> map : resultMapList) {
            resultList.add(map.get("USER_ID") == null ? "" : map.get("USER_ID").toString());
        }
        return resultList;
    }

    /**
     * 根据流程实例获取办理意见信息
     *
     * @param procInstId
     * @param nodeKeys
     * @return
     */
    @Override
    public List<CommentDto> findCommentByProcInstId(String procInstId, List<String> nodeKeys) {
        /**
         SELECT hm.*, CU.DISPLAYNAME
         FROM (  SELECT HC.PROC_INST_ID_,
         HC.TIME_,
         HC.MESSAGE_,
         HA.ACT_ID_,
         HA.ASSIGNEE_
         FROM ACT_HI_COMMENT hc, ACT_HI_ACTINST ha
         WHERE     HC.PROC_INST_ID_ = HA.PROC_INST_ID_
         AND HC.TASK_ID_ = HA.TASK_ID_
         AND hc.PROC_INST_ID_ = '1037501'
         --and HA.ACT_ID_ = 'SIGN_FGLD_FB'
         AND ha.ACT_TYPE_ = 'userTask'
         ORDER BY HC.TIME_) hm,
         cs_user cu
         WHERE hm.ASSIGNEE_ = CU.ID
         */
        HqlBuilder sqlBuilder = HqlBuilder.create();
        sqlBuilder.append(" SELECT hm.*, CU.DISPLAYNAME AS DISPLAYNAME_ FROM ( ");
        sqlBuilder.append(" SELECT HC.PROC_INST_ID_,HC.TIME_,HC.MESSAGE_,HA.ACT_ID_,HA.ASSIGNEE_ ");
        sqlBuilder.append(" FROM ACT_HI_COMMENT hc, ACT_HI_ACTINST ha WHERE HC.PROC_INST_ID_ = HA.PROC_INST_ID_ ");
        sqlBuilder.append(" AND HC.TASK_ID_ = HA.TASK_ID_ AND hc.PROC_INST_ID_ = :procInstId ").setParam("procInstId", procInstId);
        if (Validate.isList(nodeKeys)) {
            sqlBuilder.bulidPropotyString("AND", "HA.ACT_ID_", StringUtil.listToString(nodeKeys));
        }
        sqlBuilder.append(" AND ha.ACT_TYPE_ = 'userTask' ) hm,cs_user cu WHERE hm.ASSIGNEE_ = CU.ID ORDER BY hm.TIME_");
        List<Object[]> commentList = signBranchRepo.getObjectArray(sqlBuilder);
        List<CommentDto> resultList = new ArrayList<>();
        for (Object[] objArr : commentList) {
            CommentDto commentDto = new CommentDto();
            mapToComment(objArr, commentDto);
            resultList.add(commentDto);
        }
        return resultList;
    }

    private void mapToComment(Object[] objArr, CommentDto commentDto) {
        commentDto.setProcInstId(objArr[0].toString());
        commentDto.setCommentDate((Date) objArr[1]);
        commentDto.setComments(objArr[2] == null ? "" : objArr[2].toString());
        commentDto.setNodeKeyValue(objArr[3] == null ? "" : objArr[3].toString());
        commentDto.setUserName(objArr[5] == null ? "" : objArr[5].toString());

    }

    /**
     * 只要最新的评审意见
     *
     * @param commentList
     * @param nodeKeys
     * @return
     */
    @Override
    public Map<String, CommentDto> filterComents(List<CommentDto> commentList, List<String> nodeKeys) {
        Map<String, CommentDto> resultMap = new HashMap<>();
        if (Validate.isList(commentList)) {
            for (CommentDto commentDto : commentList) {
                resultMap.put(commentDto.getNodeKeyValue(), commentDto);
            }
        }
        return resultMap;
    }

    /**
     * 流程转办
     *
     * @param taskId    任务ID
     * @param oldUserId 原先处理人
     * @param newUserId 新处理人
     * @return
     */
    @Override
    public ResultMsg taskTransferAssignee(String taskId, String oldUserId, String newUserId) {
        ResultMsg resultMsg = new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), "");
        Task task = taskService.createTaskQuery().taskId(taskId).active().singleResult();
        if (!Validate.isObject(task)) {
            resultMsg.setReMsg("该任务已处理！");
        } else {
            //如果是当前待办人,直接转办
            if (Validate.isString(task.getAssignee())) {
                taskService.setAssignee(taskId, newUserId);
            } else {
                //如果当前任务有多个待办人，则要修改对应的待办人
                HqlBuilder sqlBuilder = HqlBuilder.create();
                sqlBuilder.append(" UPDATE ACT_RU_IDENTITYLINK SET USER_ID_ = :newUserId WHERE TASK_ID_ = :taskId AND USER_ID_ = :oldUserId AND TYPE_ = 'candidate' ");
                sqlBuilder.setParam("newUserId", newUserId).setParam("taskId", taskId).setParam("oldUserId", oldUserId);
                ruTaskRepo.executeSql(sqlBuilder);
            }
            //添加转办记
            ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(task.getProcessInstanceId()).singleResult();
            AgentTask agentTask = new AgentTask();
            agentTask.setAgentId((new RandomGUID()).valueAfterMD5);
            agentTask.setTransDate(new Date());
            agentTask.setTaskId(taskId);

            User user = userService.getCacheUserById(oldUserId);
            agentTask.setUserId(oldUserId);
            agentTask.setUserName(user.getDisplayName());
            user = userService.getCacheUserById(newUserId);
            agentTask.setAgentId(newUserId);
            agentTask.setAgentUserName(user.getDisplayName());

            agentTask.setNodeKey(task.getTaskDefinitionKey());
            agentTask.setNodeNameValue(task.getName());
            agentTask.setFlowName(processInstance.getName());

            agentTaskRepo.save(agentTask);
        }
        resultMsg.setFlag(true);
        resultMsg.setReCode(Constant.MsgCode.OK.getValue());
        resultMsg.setReMsg("操作成功！");
        return resultMsg;
    }

    /**
     * @param signId
     * @return List<Map<String,Object>>
     */

    @Override
    public List<Map<String,Object>> getBranchInfo(String signId) {
        return jdbcTemplate.queryForList(WorkSql.getReWorkSql(),signId);
    }

    /**
     * 合并发文流程处理
     *
     * @param processInstance 流程实例
     * @param task            任务
     * @param nodeName        环节名称
     * @param flowDto         办理信息
     * @return
     */
    @Override
    public ResultMsg dealMerDisFlow(ProcessInstance processInstance, Task task, DispatchDoc dp, String nodeName, FlowDto flowDto, boolean isAgentTask) throws Exception {
        String assigneeValue = "",                             //环节处理人
                nextNodeKey = "",                               //环节名称
                signid = processInstance.getBusinessKey();      //项目ID

        boolean isNextUser = false;
        List<AgentTask> agentTaskList = new ArrayList<>();      //代办任务
        Map<String, Object> variables = new HashMap<>();        //环节参数
        List<User> userList = null;                             //处理人列表
        User dealUser = null;                                   //处理人
        Sign sign = null;                                       //项目信息
        List<SignMerge> mergeList = signMergeRepo.findByType(signid, MergeType.DISPATCH.getValue());
        ResultMsg returnMsg = new ResultMsg(true, MsgCode.OK.getValue(), "操作成功！");

        switch (nodeName) {
            //发文申请
            case FLOW_SIGN_FW:
                if (Validate.isList(mergeList)) {
                    //1、先检验是否发放评审费和通过校验
                    for (SignMerge s : mergeList) {
                        if (!signService.checkReviewCost(s.getMergeId())) {
                            return new ResultMsg(false, MsgCode.ERROR.getValue(), "合并发文次项目,还没完成专家评审费发放，不能进行下一步操作！");
                        }
                        if (!signService.checkFileUpload(s.getMergeId())) {
                            return new ResultMsg(false, MsgCode.ERROR.getValue(), "合并发文次项目,还没上传[评审意见]或者[审核意见]附件信息！");
                        }
                    }
                }
                //校验本身是否已经发送评审费和上传附件
                if (!signService.checkReviewCost(signid)) {
                    return new ResultMsg(false, MsgCode.ERROR.getValue(), "您还没完成专家评审费发放，不能进行下一步操作！");
                }
                if (!signService.checkFileUpload(signid)) {
                    return new ResultMsg(false, MsgCode.ERROR.getValue(), "您还没上传[评审意见]或者[审核意见]附件信息！");
                }
                //取环节处理人
                userList = signPrincipalService.getAllSecondPriUser(signid);
                if (Validate.isList(userList)) {
                    //有项目负责人，则项目负责人审核
                    variables.put(FlowConstant.SignFlowParams.HAVE_XMFZR.getValue(), true);
                    for (User user : userList) {
                        String userId = userService.getTaskDealId(user.getId(), agentTaskList, FLOW_SIGN_QRFW);
                        assigneeValue = StringUtil.joinString(assigneeValue, SEPARATE_COMMA, userId);
                    }
                    variables.put(FlowConstant.SignFlowParams.USER_HQ_LIST.getValue(), StringUtil.getSplit(assigneeValue, SEPARATE_COMMA));
                } else {
                    //没有项目负责人，则主办部长审核
                    variables.put(FlowConstant.SignFlowParams.HAVE_XMFZR.getValue(), false);
                    assigneeValue = signService.getMainDirecotr(signid, agentTaskList, FlowConstant.FLOW_SIGN_BMLD_QRFW);
                    variables.put(FlowConstant.SignFlowParams.USER_BZ1.getValue(), assigneeValue);
                }

                //开始处理流程
                commitFWSQNode(variables, signid, task.getId(), processInstance.getId(), flowDto.getDealOption(), dp, isAgentTask);
                //处理合并发文的发文和项目信息
                for (SignMerge s : mergeList) {
                    sign = signRepo.findById(s.getMergeId());
                    ResultMsg resultMsg = checkMerDisNode(sign, FLOW_SIGN_FW);
                    if (resultMsg.isFlag()) {
                        Map<String, Object> resultObj = (Map<String, Object>) resultMsg.getReObj();
                        task = (Task) resultObj.get("task");
                        processInstance = (ProcessInstance) resultObj.get("processInstance");
                        commitFWSQNode(variables, sign.getSignid(), task.getId(), processInstance.getId(), flowDto.getDealOption(), sign.getDispatchDoc(), isAgentTask);
                    } else {
                        throw new Exception(resultMsg.getReMsg());
                    }
                }
                break;
            //项目负责人办理
            case FLOW_SIGN_QRFW:
                boolean isAgree = Constant.EnumState.YES.getValue().equals(flowDto.getBusinessMap().get("AGREE").toString());
                if (isAgree) {
                    variables.put(FlowConstant.SignFlowParams.XMFZR_SP.getValue(), true);
                    variables.put(FlowConstant.SignFlowParams.HAVE_XB.getValue(), false);
                    //获取主分支的部门领导
                    assigneeValue = signService.getMainDirecotr(signid, agentTaskList, FLOW_SIGN_BMLD_QRFW);
                    variables.put(FlowConstant.SignFlowParams.USER_BZ1.getValue(), assigneeValue);
                } else {
                    variables.put(FlowConstant.SignFlowParams.HAVE_XB.getValue(), null);
                    variables.put(FlowConstant.SignFlowParams.XMFZR_SP.getValue(), false);
                    //选择第一负责人
                    assigneeValue = signService.getMainPriUserId(signid, agentTaskList,FlowConstant.FLOW_SIGN_QRFW);
                    variables.put(FlowConstant.SignFlowParams.USER_FZR1.getValue(), assigneeValue);

                }
                commitFZENode(variables, task.getId(), processInstance.getId(), flowDto.getDealOption(), dp, isAgentTask, isAgree);
                //处理合并发文的发文和项目信息
                for (SignMerge s : mergeList) {
                    sign = signRepo.findById(s.getMergeId());
                    ResultMsg resultMsg = checkMerDisNode(sign, FLOW_SIGN_QRFW);
                    if (resultMsg.isFlag()) {
                        Map<String, Object> resultObj = (Map<String, Object>) resultMsg.getReObj();
                        task = (Task) resultObj.get("task");
                        processInstance = (ProcessInstance) resultObj.get("processInstance");
                        //以主项目的为主
                        /*assigneeValue = signService.getMainPriUserId(signid, agentTaskList,FlowConstant.FLOW_SIGN_QRFW);
                        variables.put(FlowConstant.SignFlowParams.USER_FZR1.getValue(), assigneeValue);*/

                        commitFZENode(variables, task.getId(), processInstance.getId(), flowDto.getDealOption(), sign.getDispatchDoc(), isAgentTask, isAgree);
                    } else {
                        throw new Exception(resultMsg.getReMsg());
                    }
                }
                break;
            //主办部长审批发文
            case FLOW_SIGN_BMLD_QRFW:
                sign = signRepo.findById(Sign_.signid.getName(), signid);
                User mainLead = userRepo.getCacheUserById(sign.getLeaderId());
                assigneeValue = Validate.isString(mainLead.getTakeUserId()) ? mainLead.getTakeUserId() : mainLead.getId();
                //没有协办
                variables.put(FlowConstant.SignFlowParams.HAVE_XB.getValue(), false);
                variables.put(FlowConstant.SignFlowParams.USER_FGLD1.getValue(), assigneeValue);

                commitZBNode(variables, task.getId(), processInstance.getId(), flowDto.getDealOption(), dp, isAgentTask);
                for (SignMerge s : mergeList) {
                    sign = signRepo.findById(s.getMergeId());
                    ResultMsg resultMsg = checkMerDisNode(sign, FLOW_SIGN_BMLD_QRFW);
                    if (resultMsg.isFlag()) {
                        Map<String, Object> resultObj = (Map<String, Object>) resultMsg.getReObj();
                        task = (Task) resultObj.get("task");
                        processInstance = (ProcessInstance) resultObj.get("processInstance");
                        commitZBNode(variables, task.getId(), processInstance.getId(), flowDto.getDealOption(), sign.getDispatchDoc(), isAgentTask);
                    } else {
                        throw new Exception(resultMsg.getReMsg());
                    }
                }
                break;
            //主办分管副主任审批发文
            case FLOW_SIGN_FGLD_QRFW:
                dealUser = userRepo.findUserByRoleName(Constant.EnumFlowNodeGroupName.DIRECTOR.getValue()).get(0);
                assigneeValue = userService.getTaskDealId(dealUser, agentTaskList, FLOW_SIGN_ZR_QRFW);
                variables.put(FlowConstant.SignFlowParams.USER_ZR.getValue(), assigneeValue);
                //下一环节还是自己处理
                isNextUser = assigneeValue.equals(SessionUtil.getUserId());
                nextNodeKey = FLOW_SIGN_ZR_QRFW;

                commitFGZRNode(variables, task.getId(), processInstance.getId(), flowDto.getDealOption(), dp, isAgentTask);
                for (SignMerge s : mergeList) {
                    sign = signRepo.findById(s.getMergeId());
                    ResultMsg resultMsg = checkMerDisNode(sign, FLOW_SIGN_FGLD_QRFW);
                    if (resultMsg.isFlag()) {
                        Map<String, Object> resultObj = (Map<String, Object>) resultMsg.getReObj();
                        task = (Task) resultObj.get("task");
                        processInstance = (ProcessInstance) resultObj.get("processInstance");
                        commitFGZRNode(variables, task.getId(), processInstance.getId(), flowDto.getDealOption(), sign.getDispatchDoc(), isAgentTask);
                    } else {
                        throw new Exception(resultMsg.getReMsg());
                    }
                }

                break;
            //主任审批发文
            case FLOW_SIGN_ZR_QRFW:
                //项目负责人生成发文编号
                assigneeValue = signService.getMainPriUserId(signid, agentTaskList,FlowConstant.FLOW_SIGN_FWBH);
                variables.put(FlowConstant.SignFlowParams.USER_FZR1.getValue(), assigneeValue);
                commitZRNode(variables, task.getId(), processInstance.getId(), flowDto.getDealOption(), dp, isAgentTask);
                for (SignMerge s : mergeList) {
                    sign = signRepo.findById(s.getMergeId());
                    ResultMsg resultMsg = checkMerDisNode(sign, FLOW_SIGN_ZR_QRFW);
                    if (resultMsg.isFlag()) {
                        Map<String, Object> resultObj = (Map<String, Object>) resultMsg.getReObj();
                        task = (Task) resultObj.get("task");
                        processInstance = (ProcessInstance) resultObj.get("processInstance");
                        //主任审批之后，由各个的项目负责人处理
                        assigneeValue = signService.getMainPriUserId(s.getMergeId(), agentTaskList,FlowConstant.FLOW_SIGN_FWBH);
                        variables.put(FlowConstant.SignFlowParams.USER_FZR1.getValue(), assigneeValue);
                        commitZRNode(variables, task.getId(), processInstance.getId(), flowDto.getDealOption(), sign.getDispatchDoc(), isAgentTask);
                    } else {
                        throw new Exception(resultMsg.getReMsg());
                    }
                }
                break;
        }

        //放入腾讯通消息缓冲池
        if (isNextUser == false) {
            RTXSendMsgPool.getInstance().sendReceiverIdPool(task.getId(), assigneeValue);
        } else {
            processInstance = runtimeService.createProcessInstanceQuery().processInstanceBusinessKey(signid).singleResult();
            List<Task> nextTaskList = taskService.createTaskQuery().processInstanceId(processInstance.getId()).taskAssignee(assigneeValue).list();
            for (Task t : nextTaskList) {
                if (nextNodeKey.equals(t.getTaskDefinitionKey())) {
                    ResultMsg resultMsg = signService.dealFlow(processInstance, t, flowDto);
                    if (!returnMsg.isFlag()) {
                        return resultMsg;
                    }
                }
            }
        }
        //如果是代办，还要更新环节名称和任务ID
        if (Validate.isList(agentTaskList)) {
            agentTaskService.updateAgentInfo(agentTaskList, processInstance.getId(), processInstance.getName());
        }
        return returnMsg;
    }

    @Override
    public PageModelDto<TaskDto> queryEndTasks(ODataObj odataObj) {
        PageModelDto<TaskDto> resultPage = new PageModelDto<>();

        HistoryService historyService = processEngine.getHistoryService();
        HistoricProcessInstanceQuery hq = historyService.createHistoricProcessInstanceQuery().finished();
        if(!SUPER_ACCOUNT.equals(SessionUtil.getLoginName())){
            hq = hq.involvedUser(SessionUtil.getUserId());
        }
        hq = hq.processDefinitionKeyNotIn(Arrays.asList("FINAL_SIGN_FLOW"));
        if (Validate.isList(odataObj.getFilter())) {
            Object value;
            for (ODataFilterItem item : odataObj.getFilter()) {
                value = item.getValue();
                if (null == value) {
                    continue;
                }
                switch (item.getField()){
                    case "flowKey":
                        hq = hq.processDefinitionKey(value.toString());
                        break;
                    case "flowName":
                        hq = hq.processInstanceNameLike("%"+value.toString()+"%");
                        break;
                }
            }
        }
        resultPage.setCount(Integer.parseInt(String.valueOf(hq.count())));
        List<HistoricProcessInstance> historicList = hq.orderByProcessInstanceEndTime().desc().listPage(odataObj.getSkip(), odataObj.getTop());
        List<TaskDto> list = new ArrayList<TaskDto>(historicList == null ? 0 : historicList.size());
        if (historicList != null && historicList.size() > 0) {
            historicList.forEach(h -> {
                TaskDto taskDto = new TaskDto();
                taskDto.setBusinessKey(h.getBusinessKey());
                taskDto.setFlowName(h.getName());
                taskDto.setProcessInstanceId(h.getId());
                taskDto.setProcessVariables(h.getProcessVariables());
                taskDto.setProcessDefinitionId(h.getProcessDefinitionId());
                taskDto.setCreateDate(h.getStartTime());
                taskDto.setEndDate(h.getEndTime());
                taskDto.setDurationInMillis(h.getDurationInMillis());
                taskDto.setDurationTime(ActivitiUtil.formatTime(h.getDurationInMillis()));
                taskDto.setFlowKey(h.getProcessDefinitionId().substring(0, h.getProcessDefinitionId().indexOf(":")));
                list.add(taskDto);
            });
        }
        resultPage.setValue(list);
        return resultPage;
    }

    /**
     * 发文申请环节处理
     *
     * @param signId
     * @param taskId
     * @param procInstId
     * @param dealOption
     * @param dp
     * @param isAgentTask
     */
    private void commitFWSQNode(Map<String, Object> variables, String signId, String taskId, String procInstId, String dealOption, DispatchDoc dp, boolean isAgentTask) {
        taskService.addComment(taskId, procInstId, dealOption);
        taskService.complete(taskId, variables);
        //修改第一负责人信息
        dp.setMianChargeSuggest(dealOption + "  签名：" + ActivitiUtil.getSignName(SessionUtil.getDisplayName(), isAgentTask) + "  日期：" + DateUtils.converToString(new Date(), "yyyy年MM月dd日"));
        signService.resetDisReviewOption(dp);
        dispatchDocRepo.save(dp);
        signRepo.updateSignProcessState(signId, Constant.SignProcessState.END_DIS.getValue());
    }

    /**
     * 合并发文，项目负责人处理环节
     *
     * @param variables
     * @param taskId
     * @param procInstId
     * @param dealOption
     * @param dp
     * @param isAgentTask
     * @param isAgree
     */
    private void commitFZENode(Map<String, Object> variables, String taskId, String procInstId, String dealOption, DispatchDoc dp, boolean isAgentTask, boolean isAgree) {
        String newDealOption = dealOption;
        if (isAgree) {
            newDealOption += "【审批结果：核稿无误】";
        } else {
            newDealOption += "【审批结果：核稿有误】";
        }
        taskService.addComment(taskId, procInstId, newDealOption);
        taskService.complete(taskId, variables);
        //修改第二负责人意见
        String optionString = Validate.isString(dp.getSecondChargeSuggest()) ? (dp.getSecondChargeSuggest() + "<br>") : "";
        dp.setSecondChargeSuggest(optionString + dealOption + " 签名：" + ActivitiUtil.getSignName(SessionUtil.getDisplayName(), isAgentTask) + " 日期：" + DateUtils.converToString(new Date(), "yyyy年MM月dd日"));
        dispatchDocRepo.save(dp);
    }

    /**
     * 部长审批环节意见
     *
     * @param variables
     * @param taskId
     * @param procInstId
     * @param dealOption
     * @param dp
     * @param isAgentTask
     */
    private void commitZBNode(Map<String, Object> variables, String taskId, String procInstId, String dealOption, DispatchDoc dp, boolean isAgentTask) {
        taskService.addComment(taskId, procInstId, dealOption);
        taskService.complete(taskId, variables);
        dp.setMinisterSuggesttion(dealOption);
        dp.setMinisterDate(new Date());
        dp.setMinisterName(ActivitiUtil.getSignName(SessionUtil.getDisplayName(), isAgentTask));
        dispatchDocRepo.save(dp);
    }

    /**
     * 分管副主任审批环节
     *
     * @param variables
     * @param taskId
     * @param procInstId
     * @param dealOption
     * @param dp
     * @param isAgentTask
     */
    private void commitFGZRNode(Map<String, Object> variables, String taskId, String procInstId, String dealOption, DispatchDoc dp, boolean isAgentTask) {
        taskService.addComment(taskId, procInstId, dealOption);
        taskService.complete(taskId, variables);
        dp.setViceDirectorDate(new Date());
        dp.setViceDirectorSuggesttion(dealOption);
        dp.setViceDirectorName(ActivitiUtil.getSignName(SessionUtil.getDisplayName(), isAgentTask));
        dispatchDocRepo.save(dp);
    }

    /**
     * 主任环节提交
     *
     * @param variables
     * @param taskId
     * @param procInstId
     * @param dealOption
     * @param dp
     * @param isAgentTask
     */
    private void commitZRNode(Map<String, Object> variables, String taskId, String procInstId, String dealOption, DispatchDoc dp, boolean isAgentTask) {
        taskService.addComment(taskId, procInstId, dealOption);
        taskService.complete(taskId, variables);
        dp.setDirectorSuggesttion(dealOption);
        dp.setDirectorDate(new Date());
        dp.setDirectorName(ActivitiUtil.getSignName(SessionUtil.getDisplayName(), isAgentTask));
        dispatchDocRepo.save(dp);
    }

    private ResultMsg checkMerDisNode(Sign sign, String nodeName) {
        ResultMsg resultMsg = new ResultMsg(false, MsgCode.ERROR.getValue(), "");
        if (!Validate.isObject(sign.getDispatchDoc())) {
            resultMsg.setReMsg("项目【" + sign.getProjectname() + "】还没填报发文！");
            return resultMsg;
        }
        Map<String, Object> resultObj = new HashMap<>();
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceBusinessKey(sign.getSignid()).singleResult();
        if (processInstance == null) {
            resultMsg.setReMsg("项目【" + sign.getProjectname() + "】流程已办结！");
            return resultMsg;
        } else {
            resultObj.put("processInstance", processInstance);
        }
        Task task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).active().singleResult();
        if (task == null) {
            resultMsg.setReMsg("项目【" + sign.getProjectname() + "】流程已办结！");
            return resultMsg;
        } else {
            resultObj.put("task", task);
        }
        if (!task.getTaskDefinitionKey().equals(nodeName)) {
            resultMsg.setReMsg("项目【" + sign.getProjectname() + "】办理环节与主项目不一致！");
            return resultMsg;
        }
        resultMsg.setFlag(true);
        resultMsg.setReCode(MsgCode.OK.getValue());
        resultMsg.setReObj(resultObj);
        return resultMsg;
    }
}