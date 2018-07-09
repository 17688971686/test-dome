package cs.controller.flow;

import cs.ahelper.LogMsg;
import cs.ahelper.MudoleAnnotation;
import cs.common.constants.Constant.MsgType;
import cs.common.constants.Constant.MsgCode;
import cs.common.constants.FlowConstant;
import cs.common.ResultMsg;
import cs.common.utils.Validate;
import cs.domain.flow.HiProcessTask;
import cs.domain.flow.RuProcessTask;
import cs.domain.flow.RuTask;
import cs.domain.project.SignDispaWork;
import cs.model.PageModelDto;
import cs.model.flow.FlowDto;
import cs.model.flow.Node;
import cs.model.flow.TaskDto;
import cs.repository.odata.ODataObj;
import cs.service.archives.ArchivesLibraryService;
import cs.service.asserts.assertStorageBusiness.AssertStorageBusinessService;
import cs.service.book.BookBuyBusinessService;
import cs.service.flow.FlowNextNodeFilter;
import cs.service.flow.FlowService;
import cs.service.flow.IFlow;
import cs.service.monthly.MonthlyNewsletterService;
import cs.service.project.AddSuppLetterService;
import cs.service.project.ProjectStopService;
import cs.service.project.SignService;
import cs.service.project.WorkProgramService;
import cs.service.reviewProjectAppraise.AppraiseService;
import cs.service.rtx.RTXService;
import cs.service.sys.AnnountmentService;
import cs.service.sys.UserService;
import cs.service.topic.TopicInfoService;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.log4j.Logger;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.text.ParseException;
import java.util.*;

@Controller
@RequestMapping(name = "流程", path = "flow")
@MudoleAnnotation(name = "我的工作台",value = "permission#workbench")
public class FlowController {
    private static Logger log = Logger.getLogger(FlowController.class);

    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    private HistoryService historyService;
    @Autowired
    private SignService signService;
    @Autowired
    private FlowService flowService;
    @Autowired
    private TaskService taskService;

    @Autowired
    @Qualifier("signFlowImpl")
    private IFlow signFlowImpl;
    @Autowired
    @Qualifier("assertStorageFlowImpl")
    private IFlow assertStorageFlowImpl;
    @Autowired
    @Qualifier("booksBuyBusFlowImpl")
    private IFlow booksBuyBusFlowImpl;
    @Autowired
    @Qualifier("topicFlowImpl")
    private IFlow topicFlowImpl;
    @Autowired
    @Qualifier("projectStopFlowImpl")
    private IFlow projectStopFlowImpl;
    @Autowired
    @Qualifier("archivesFlowImpl")
    private IFlow archivesFlowImpl;
    @Autowired
    @Qualifier("appraiseFlowImpl")
    private IFlow appraiseFlowImpl;
    @Autowired
    @Qualifier("suppLetterFlowImpl")
    private IFlow suppLetterFlowImpl;
    @Autowired
    @Qualifier("annountMentFlowImpl")
    private IFlow annountMentFlowImpl;
    @Autowired
    @Qualifier("monthFlowImpl")
    private IFlow monthFlowImpl;

    @Autowired
    private WorkProgramService workProgramService;

    @Autowired
    private TopicInfoService topicInfoService;
    @Autowired
    private BookBuyBusinessService bookBuyBusinessService;
    @Autowired
    private AssertStorageBusinessService assertStorageBusinessService;
    @Autowired
    private ProjectStopService projectStopService;
    @Autowired
    private ArchivesLibraryService archivesLibraryService;
    @Autowired
    private AppraiseService appraiseService;
    @Autowired
    private AddSuppLetterService addSuppLetterService;
    @Autowired
    private MonthlyNewsletterService monthlyNewsletterService;
    @Autowired
    private AnnountmentService annountmentService;
    @Autowired
    private UserService userService;
    @Autowired
    private RTXService rtxService;

    //@RequiresPermissions("flow#html/tasks#post")
    @RequiresAuthentication
    @RequestMapping(name = "待办项目", path = "html/tasks", method = RequestMethod.POST)
    public @ResponseBody PageModelDto<RuProcessTask> tasks(HttpServletRequest request) throws ParseException {
        ODataObj odataObj = new ODataObj(request);
        List<RuProcessTask> resultList = flowService.queryRunProcessTasks(odataObj,true,null,null);
        PageModelDto<RuProcessTask> pageModelDto = new PageModelDto<RuProcessTask>();
        pageModelDto.setCount(Validate.isList(resultList)?resultList.size():0);
        pageModelDto.setValue(resultList);
        return pageModelDto;
    }

    /**
     * 查询我的待办任务（除项目流程外）
     * @param request
     * @return
     * @throws ParseException
     */
    //@RequiresPermissions("flow#queryMyAgendaTask#post")
    @RequiresAuthentication
    @RequestMapping(name = "我的待办任务", path = "queryMyAgendaTask", method = RequestMethod.POST)
    @ResponseBody
    public PageModelDto<RuTask> queryMyAgendaTask(HttpServletRequest request) throws ParseException {
        ODataObj odataObj = new ODataObj(request);
        PageModelDto<RuTask> pageModelDto = flowService.queryMyAgendaTask(odataObj);
        return pageModelDto;
    }

    //@RequiresPermissions("flow#queryAgendaTask#post")
    @RequiresAuthentication
    @RequestMapping(name = "在办任务", path = "queryAgendaTask", method = RequestMethod.POST)
    @ResponseBody
    public PageModelDto<RuTask> queryAgendaTask(HttpServletRequest request) throws ParseException {
        ODataObj odataObj = new ODataObj(request);
        PageModelDto<RuTask> pageModelDto = flowService.queryAgendaTask(odataObj);
        return pageModelDto;
    }

    //@RequiresPermissions("flow#html/doingtasks#post")
    @RequiresAuthentication
    @RequestMapping(name = "在办项目", path = "html/doingtasks", method = RequestMethod.POST)
    @ResponseBody
    public PageModelDto<RuProcessTask> doingtasks(HttpServletRequest request) throws ParseException {
        ODataObj odataObj = new ODataObj(request);
        Map<String,Object> authMap = userService.getUserSignAuth();
        Integer authFlag = new Integer(authMap.get("leaderFlag").toString());
        List<String> orgIdList = (List<String>) authMap.get("orgIdList");
        List<RuProcessTask> resultList = flowService.queryRunProcessTasks(odataObj,false,authFlag,orgIdList);
        //已项目为单位，要过滤掉重复的项目
        List<RuProcessTask> finalList = new ArrayList<>();
        List<String> existList = new ArrayList<>();
        for (int i = 0,l=resultList.size(); i < l; i++) {
            RuProcessTask rpt = resultList.get(i);
            if (existList.contains(rpt.getBusinessKey())) {
                //判断是否是主分支，是则替换为主分支
                if(workProgramService.mainBranch(rpt.getBusinessKey())){
                    for(int j = 0 ; j < finalList.size() ; j++){
                        RuProcessTask r = finalList.get(j);
                        if(rpt.getBusinessKey().equals(r.getBusinessKey())){
                            finalList.set(j , r);
                            break;
                        }
                    }
                }else{

                    continue;
                }
            } else {
                existList.add(rpt.getBusinessKey());
                finalList.add(rpt);
            }
        }
        PageModelDto<RuProcessTask> pageModelDto = new PageModelDto<RuProcessTask>();
        pageModelDto.setCount(Validate.isList(finalList)?finalList.size():0);
        pageModelDto.setValue(finalList);
        return pageModelDto;
    }

    //@RequiresPermissions("flow#html/endTasks#post")
    @RequiresAuthentication
    @RequestMapping(name = "办结项目", path = "html/endTasks", method = RequestMethod.POST)
    public @ResponseBody
    PageModelDto<SignDispaWork> endTasks(HttpServletRequest request) throws ParseException {
        ODataObj odataObj = new ODataObj(request);
        PageModelDto<SignDispaWork> pageModelDto = flowService.queryETasks(odataObj);
        return pageModelDto;
    }

    //@RequiresPermissions("flow#getMyHomeTasks#post")//停用
    @RequiresAuthentication
    @RequestMapping(name = "查询主页我的待办任务", path = "getMyHomeTasks", method = RequestMethod.POST)
    @ResponseBody
    public  List<RuProcessTask> getMyHomeTasks() {
        return flowService.queryMyRunProcessTasks(6);
    }

    //@RequiresPermissions("flow#getMyHomeEndTask#post")
    @RequiresAuthentication
    @RequestMapping(name = "获取主页上的办结任务", path = "getMyHomeEndTask", method = RequestMethod.POST)
    @ResponseBody
    public List<TaskDto> getMyHomeEndTask() {
        return flowService.queryMyEndTasks();
    }

    @RequiresAuthentication
    @RequestMapping(name = "读取流程图", path = "processInstance/img/{processInstanceId}", method = RequestMethod.GET)
    public void readProccessInstanceImg(@PathVariable("processInstanceId") String processInstanceId, HttpServletResponse response)
            throws Exception {
        InputStream imageStream = getProcessInstanceImage(processInstanceId);
        if(imageStream == null){
            String resultMsg = "已找不到流程信息";
            response.getOutputStream().write(resultMsg.getBytes());
        }else{
            // 输出资源内容到相应对象
            byte[] b = new byte[1024];
            int len;
            while ((len = imageStream.read(b, 0, 1024)) != -1) {
                response.getOutputStream().write(b, 0, len);
            }
        }

    }

    private InputStream getProcessInstanceImage(String processInstanceId) {
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
        if(processInstance == null){
            return null;
        }
        BpmnModel bpmnModel = repositoryService.getBpmnModel(processInstance.getProcessDefinitionId());
        /**********************************   第一版本  ******************************/
       /* List<String> activeActivityIds = runtimeService.getActiveActivityIds(processInstanceId);
        // 使用spring注入引擎请使用下面的这行代码
        processEngineConfiguration = processEngine.getProcessEngineConfiguration();
        Context.setProcessEngineConfiguration((ProcessEngineConfigurationImpl) processEngineConfiguration);

        ProcessDiagramGenerator diagramGenerator = processEngineConfiguration.getProcessDiagramGenerator();
        InputStream imageStream = diagramGenerator.generateDiagram(bpmnModel, "png", activeActivityIds, activeActivityIds,
                processEngineConfiguration.getActivityFontName(), processEngineConfiguration.getLabelFontName(),
                processEngineConfiguration.getClassLoader(), 1.0);*/

        /**********************************   第二版本  ******************************/
        /*// 经过的节点
        List<String> activeActivityIds = new ArrayList<>();
        List<String> finishedActiveActivityIds = new ArrayList<>();

        // 已执行完的任务节点
        List<HistoricActivityInstance> finishedInstances = historyService.createHistoricActivityInstanceQuery().processInstanceId(processInstance.getId()).finished().list();
        for (HistoricActivityInstance hai : finishedInstances) {
            finishedActiveActivityIds.add(hai.getActivityId());
        }

        // 已完成的节点+当前节点
        activeActivityIds.addAll(finishedActiveActivityIds);
        activeActivityIds.addAll(runtimeService.getActiveActivityIds(processInstance.getId()));

        // 经过的流
        ProcessDefinitionEntity processDefinitionEntity = (ProcessDefinitionEntity) repositoryService.getProcessDefinition(processInstance.getProcessDefinitionId());
        List<String> highLightedFlows = new ArrayList<>();
        getHighLightedFlows(processDefinitionEntity.getActivities(), highLightedFlows, activeActivityIds);

        ProcessDiagramGenerator pdg = processEngineConfiguration.getProcessDiagramGenerator();
        InputStream inputStream = pdg.generateDiagram(bpmnModel, "PNG", activeActivityIds, highLightedFlows,
                processEngineConfiguration.getProcessEngineConfiguration().getActivityFontName(),
                processEngineConfiguration.getProcessEngineConfiguration().getLabelFontName(),
                processEngineConfiguration.getProcessEngineConfiguration().getProcessEngineConfiguration().getClassLoader(), 2.0);
       */
        /**********************************   第三版本  ******************************/
        // 经过的节点
        List<String> activeActivityIds = new ArrayList<>();

        // 已执行完的任务节点
        List<HistoricActivityInstance> finishedInstances = historyService.createHistoricActivityInstanceQuery().processInstanceId(processInstance.getId()).finished().list();
        for (HistoricActivityInstance hai : finishedInstances) {
            activeActivityIds.add(hai.getActivityId());
        }
        // 已完成的节点+当前节点
        activeActivityIds.addAll(runtimeService.getActiveActivityIds(processInstance.getId()));
        ProcessDefinitionEntity processDefinitionEntity = (ProcessDefinitionEntity) repositoryService.getProcessDefinition(processInstance.getProcessDefinitionId());
        InputStream inputStream = cs.activiti.ProcessDiagramGenerator.generateDiagram(processDefinitionEntity, "PNG", activeActivityIds);
        return inputStream;

    }
    /*
    * 递归查询经过的流
    */
    private void getHighLightedFlows(List<ActivityImpl> activityList, List<String> highLightedFlows, List<String> historicActivityInstanceList) {
        for (ActivityImpl activity : activityList) {
            if (activity.getProperty("type").equals("subProcess")) {
                getHighLightedFlows(activity.getActivities(), highLightedFlows, historicActivityInstanceList);
            }

            if (historicActivityInstanceList.contains(activity.getId())) {
                List<PvmTransition> pvmTransitionList = activity.getOutgoingTransitions();
                for (PvmTransition pvmTransition : pvmTransitionList) {
                    String destinationFlowId = pvmTransition.getDestination().getId();
                    if (historicActivityInstanceList.contains(destinationFlowId)) {
                        highLightedFlows.add(pvmTransition.getId());
                    }
                }
            }
        }
    }

    @RequestMapping(name = "读取流程历史记录", path = "processInstance/history/{processInstanceId}", method = RequestMethod.POST)
    @RequiresAuthentication
    public @ResponseBody
    PageModelDto<HiProcessTask> findHisActivitiList(@PathVariable("processInstanceId") String processInstanceId) {
        List<HiProcessTask> list = flowService.getProcessHistory(processInstanceId);
        PageModelDto<HiProcessTask> pageModelDto = new PageModelDto<HiProcessTask>();
        pageModelDto.setCount(list.size());
        pageModelDto.setValue(list);
        return pageModelDto;
    }

    @RequestMapping(name = "获取流程处理信息", path = "processInstance/flowNodeInfo", method = RequestMethod.POST)
    @RequiresAuthentication
    public @ResponseBody
    FlowDto flowNodeInfo(@RequestParam(required = true) String taskId, @RequestParam(required = true) String processInstanceId) {
        FlowDto flowDto = new FlowDto();
        flowDto.setProcessInstanceId(processInstanceId);
        flowDto.setEnd(false);

        //获取当前任务数据
        Task task = taskService.createTaskQuery().taskId(taskId).active().singleResult();
        if (Validate.isObject(task)) {
            //任务不为空，说明任务还未办理
            flowDto.setTaskId(taskId);
            //流程实例
            ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
            flowDto.setProcessKey(processInstance.getProcessDefinitionKey());
            flowDto.setEnd(processInstance.isEnded());

            Node curNode = new Node();
            curNode.setActivitiName(task.getName());
            curNode.setActivitiId(task.getTaskDefinitionKey());
            flowDto.setCurNode(curNode);
            flowDto.setSuspended(task.isSuspended());
            flowDto.setProcessKey(processInstance.getProcessDefinitionKey());

            /**
             * 流程的一些参数处理
             */
            switch (processInstance.getProcessDefinitionKey()){
                case FlowConstant.SIGN_FLOW:
                    flowDto.setBusinessMap(signFlowImpl.getFlowBusinessMap(processInstance.getBusinessKey(),task.getTaskDefinitionKey()));
                    break;
                case FlowConstant.TOPIC_FLOW:
                    flowDto.setBusinessMap(topicFlowImpl.getFlowBusinessMap(processInstance.getBusinessKey(),task.getTaskDefinitionKey()));
                    break;
                case FlowConstant.BOOKS_BUY_FLOW:
                    flowDto.setBusinessMap(booksBuyBusFlowImpl.getFlowBusinessMap(processInstance.getBusinessKey(),task.getTaskDefinitionKey()));
                    break;
                case FlowConstant.ASSERT_STORAGE_FLOW:
                    flowDto.setBusinessMap(assertStorageFlowImpl.getFlowBusinessMap(processInstance.getBusinessKey(),task.getTaskDefinitionKey()));
                    break;
                case FlowConstant.PROJECT_STOP_FLOW:
                    flowDto.setBusinessMap(projectStopFlowImpl.getFlowBusinessMap(processInstance.getBusinessKey(),task.getTaskDefinitionKey()));
                    break;
                case FlowConstant.FLOW_ARCHIVES:
                    flowDto.setBusinessMap(archivesFlowImpl.getFlowBusinessMap(processInstance.getBusinessKey(),task.getTaskDefinitionKey()));
                    break;
                case FlowConstant.FLOW_APPRAISE_REPORT:
                    flowDto.setBusinessMap(appraiseFlowImpl.getFlowBusinessMap(processInstance.getBusinessKey(),task.getTaskDefinitionKey()));
                    break;
                case FlowConstant.FLOW_SUPP_LETTER:
                    flowDto.setBusinessMap(suppLetterFlowImpl.getFlowBusinessMap(processInstance.getBusinessKey(),task.getTaskDefinitionKey()));
                    break;
                case FlowConstant.MONTHLY_BULLETIN_FLOW:
                    flowDto.setBusinessMap(monthFlowImpl.getFlowBusinessMap(processInstance.getBusinessKey(),task.getTaskDefinitionKey()));
                    break;
                case FlowConstant.ANNOUNT_MENT_FLOW:
                    flowDto.setBusinessMap(annountMentFlowImpl.getFlowBusinessMap(processInstance.getBusinessKey(),task.getTaskDefinitionKey()));
                    break;
                default:
                    ;
            }

            //获取下一环节信息
            if(flowDto.isEnd() == false){
                //获取下一环节信息--获取从某个节点出来的所有线路
                ProcessDefinitionEntity def = (ProcessDefinitionEntity) ((RepositoryServiceImpl) repositoryService)
                        .getDeployedProcessDefinition(task.getProcessDefinitionId());
                ActivityImpl activityImpl = def.findActivity(task.getTaskDefinitionKey());
                List<Node> nextNodeList = new ArrayList<>();
                flowService.nextTaskDefinition(nextNodeList,activityImpl,task.getTaskDefinitionKey());
                //如果有环节需要过滤，则过滤
                FlowNextNodeFilter flowNextNodeFilter = FlowNextNodeFilter.getInstance(task.getTaskDefinitionKey());
                if(flowNextNodeFilter != null){
                    nextNodeList = flowNextNodeFilter.filterNextNode(flowDto.getBusinessMap(),nextNodeList);
                }
                flowDto.setNextNode(nextNodeList);
            }

        }else{
            //如果任务已处理，则任务ID为空
            flowDto.setTaskId(null);
        }

        return flowDto;
    }


    @RequiresAuthentication
    @RequestMapping(name = "流程提交", path = "commit", method = RequestMethod.POST)
    @LogMsg(module = "流程提交",logLevel = "1")
    @ResponseBody
    @Transactional(rollbackFor = {Exception.class})
    public ResultMsg flowCommit(@RequestBody FlowDto flowDto){
        ResultMsg resultMsg = null;
        String errorMsg = "";
        String module="";
        ProcessInstance processInstance = null;
        boolean isProj = false;
        try{
            processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(flowDto.getProcessInstanceId()).singleResult();
            Task task = null;
            if (Validate.isString(flowDto.getTaskId())) {
                task = taskService.createTaskQuery().taskId(flowDto.getTaskId()).active().singleResult();
            } else {
                task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).active().singleResult();
            }
            if (task == null) {
                return new ResultMsg(false, MsgCode.ERROR.getValue(), "该流程已被处理！");
            }
            if (task.isSuspended()) {
                return new ResultMsg(false, MsgCode.ERROR.getValue(), "项目已暂停，不能进行操作！");
            }
            module = processInstance.getProcessDefinitionKey();

            switch (module){
                case FlowConstant.SIGN_FLOW:
                    isProj = true;
                    resultMsg = signService.dealFlow(processInstance, task,flowDto);
                    break;
                case FlowConstant.ROLL_BACK_SEND_FLOW:
                    resultMsg = signService.dealFlow(processInstance, task,flowDto);
                    break;
                case FlowConstant.TOPIC_FLOW:
                    resultMsg = topicInfoService.dealFlow(processInstance, task,flowDto);
                    break;
                //图书流程，已给委里处理
                case FlowConstant.BOOKS_BUY_FLOW:
                    resultMsg = bookBuyBusinessService.dealFlow(processInstance, task,flowDto);
                    break;
                //固定资产，已给委里处理
                case FlowConstant.ASSERT_STORAGE_FLOW:
                    resultMsg = assertStorageBusinessService.dealFlow(processInstance,task,flowDto);
                    break;
                case FlowConstant.PROJECT_STOP_FLOW:
                    resultMsg = projectStopService.dealFlow(processInstance, task,flowDto);
                    break;
                case FlowConstant.FLOW_ARCHIVES:
                    resultMsg = archivesLibraryService.dealFlow(processInstance, task,flowDto);
                    break;
                case FlowConstant.FLOW_APPRAISE_REPORT:
                    resultMsg = appraiseService.dealFlow(processInstance, task,flowDto);
                    break;
                case FlowConstant.FLOW_SUPP_LETTER:
                    resultMsg = addSuppLetterService.dealSignSupperFlow(processInstance, task,flowDto);
                    break;
                case FlowConstant.MONTHLY_BULLETIN_FLOW:
                    resultMsg = monthlyNewsletterService.dealSignSupperFlow(processInstance, task,flowDto);
                    break;
                case FlowConstant.ANNOUNT_MENT_FLOW:
                    resultMsg = annountmentService.dealSignSupperFlow(processInstance, task,flowDto);
                    break;
                default:
                    resultMsg = new ResultMsg(false,MsgCode.ERROR.getValue(),"操作失败，没有对应的流程！");
            }
        }catch (Exception e){
            errorMsg = e.getMessage();
            log.info("流程提交异常："+errorMsg);
            resultMsg = new ResultMsg(false,MsgCode.ERROR.getValue(),"操作异常，错误信息已记录，请联系管理员查看！异常信息："+errorMsg);
        }
        //腾讯通消息处理
        rtxService.dealPoolRTXMsg(flowDto.getTaskId(),resultMsg,processInstance.getName(),isProj? MsgType.project_type.name(): MsgType.task_type.name());
        return resultMsg;
    }

    @RequiresAuthentication
    @RequestMapping(name = "获取重写工作方案分支", path = "getBranchInfo", method = RequestMethod.GET)
    public @ResponseBody ResultMsg getBranchInfo(@RequestBody FlowDto flowDto) {

        ResultMsg resultMsg = flowService.getBranchInfo(flowDto);
        return resultMsg;
    }

    @RequiresAuthentication
    @RequestMapping(name = "流程回退", path = "rollbacklast", method = RequestMethod.POST)
    @ResponseBody
    @Transactional
    public ResultMsg rollBackLast(@RequestBody FlowDto flowDto) {
        ResultMsg resultMsg = flowService.rollBackLastNode(flowDto);
        //成功，发送短信通知
        if(resultMsg.isFlag()){
            //flowDto.getTaskId()
        }
        return resultMsg;
    }


    @RequiresAuthentication
    @Transactional
    @RequestMapping(name = "激活流程", path = "active/{businessKey}", method = RequestMethod.POST)
    @ResponseBody
    public ResultMsg activeFlow(@PathVariable("businessKey") String businessKey) {
        log.info("流程激活成功！businessKey=" + businessKey);
        return flowService.restartFlow(businessKey);
    }

    @Deprecated
    /**
     * 流程挂起
     * @param businessKey
     * @return
     */
    @RequiresAuthentication
    @Transactional
    @RequestMapping(name = "挂起流程", path = "suspend/{businessKey}", method = RequestMethod.POST)
    @ResponseBody
    public ResultMsg suspendFlow(@PathVariable("businessKey") String businessKey) {
        log.info("流程挂起成功！businessKey=" + businessKey);
        return flowService.stopFlow(businessKey);
    }

    @RequiresAuthentication
    @Transactional
    @RequestMapping(name = "终止流程", path = "deleteFLow", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public ResultMsg deleteFlow(@RequestParam FlowDto flowDto) {
        ResultMsg resultMsg = null;
        //流程实例
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(flowDto.getProcessInstanceId()).singleResult();
        switch (processInstance.getProcessDefinitionKey()){
            case FlowConstant.SIGN_FLOW:
                resultMsg = signService.endFlow(processInstance.getBusinessKey());
                break;
            default:
                resultMsg = new ResultMsg(false,MsgCode.ERROR.getValue(),"操作失败，没有对应的流程！");
        }
        //成功再删除流程
        if(resultMsg.isFlag()){
            runtimeService.deleteProcessInstance(processInstance.getId(), flowDto.getDealOption());
        }
        log.info("流程终止成功！businessKey=" + processInstance.getBusinessKey());
        return resultMsg;
    }

    @RequiresAuthentication
    @RequestMapping(name = "任务转办", path = "taskTransferAssignee", method ={RequestMethod.POST,RequestMethod.GET} )
    @ResponseBody
    public ResultMsg taskTransferAssignee(@RequestParam String taskId, @RequestParam String oldUserId, @RequestParam String newUserId){
        return flowService.taskTransferAssignee(taskId,oldUserId,newUserId);
    }
    /******************************   以下是页面处理  ******************************/
    //@RequiresPermissions("flow#flowDeal/processKey#get")
    @RequiresAuthentication
    @RequestMapping(name = "待办任务页面", path = "flowDeal/{processKey}", method = RequestMethod.GET)
    public String ruProcessTask(@PathVariable("processKey") String processKey) {
        String resultPage = "";
        switch (processKey){
            case FlowConstant.TOPIC_FLOW:
                resultPage = "topicInfo/flowDeal";
                break;
            case FlowConstant.PROJECT_STOP_FLOW:
                resultPage = "projectStop/flowDeal";
                break;
            case FlowConstant.BOOKS_BUY_FLOW:
                resultPage = "bookBuyBusiness/flowDeal";
                break;
            case FlowConstant.ASSERT_STORAGE_FLOW:
                resultPage = "asserts/assertStorageBusiness/flowDeal";
                break;
            case FlowConstant.FLOW_ARCHIVES:
                resultPage = "archives/flowDeal";
                break;
            case FlowConstant.FLOW_APPRAISE_REPORT:
                resultPage = "reviewProjectAppraise/flowDeal";
                break;
            case FlowConstant.FLOW_SUPP_LETTER:
                resultPage = "addSuppLetter/letterFlowDeal";
                break;
            case FlowConstant.MONTHLY_BULLETIN_FLOW:
                resultPage = "monthlyNewsletter/flow/flowDeal";
                break;
            case FlowConstant.ANNOUNT_MENT_FLOW:
                resultPage = "annountMent/flow/flowDeal";
                break;
            default:
                ;
        }
        return resultPage;
    }

    //@RequiresPermissions("flow#flowDetail/processKey#get")
    @RequiresAuthentication
    @RequestMapping(name = "在办任务", path = "flowDetail/{processKey}", method = RequestMethod.GET)
    public String flowDetail(@PathVariable("processKey") String processKey) {
        String resultPage = "";
        switch (processKey){
            case FlowConstant.TOPIC_FLOW:
                resultPage = "topicInfo/flowDetail";
                break;
            case FlowConstant.BOOKS_BUY_FLOW:
                resultPage = "bookBuyBusiness/flowDetail";
                break;
            case FlowConstant.ASSERT_STORAGE_FLOW:
                resultPage = "asserts/assertStorageBusiness/flowDetail";
                break;
            case FlowConstant.PROJECT_STOP_FLOW:
                resultPage = "projectStop/flowDetail";
                break;
            case FlowConstant.FLOW_ARCHIVES:
                resultPage = "archives/flowDetail";
                break;
            case FlowConstant.FLOW_APPRAISE_REPORT:
                resultPage = "reviewProjectAppraise/flowDetail";
                break;
            case FlowConstant.FLOW_SUPP_LETTER:
                resultPage = "addSuppLetter/letterFlowDetail";
                break;
            case FlowConstant.MONTHLY_BULLETIN_FLOW:
                resultPage = "monthlyNewsletter/flow/flowDetail";
                break;
            case FlowConstant.ANNOUNT_MENT_FLOW:
                resultPage = "annountMent/flow/flowDetail";
                break;
            default:
                ;
        }
        return resultPage;
    }

    //@RequiresPermissions("flow#flowEnd/processKey#get")
    @RequiresAuthentication
    @RequestMapping(name = "办结任务", path = "flowEnd/{processKey}", method = RequestMethod.GET)
    public String flowEnd(@PathVariable("processKey") String processKey) {
        String resultPage = "";
        switch (processKey){
            case FlowConstant.TOPIC_FLOW:
                resultPage = "topicInfo/flowEnd";
                break;
            case FlowConstant.ASSERT_STORAGE_FLOW:
                 resultPage = "asserts/assertStorageBusiness/flowEnd";
                 break;
            case FlowConstant.PROJECT_STOP_FLOW:
                resultPage = "projectStop/flowEnd";
                break;
            case FlowConstant.FLOW_ARCHIVES:
                resultPage = "archives/flowEnd";
                break;
            case FlowConstant.FLOW_APPRAISE_REPORT:
                resultPage = "reviewProjectAppraise/flowEnd";
                break;
            case FlowConstant.FLOW_SUPP_LETTER:
                resultPage = "" +
                        "";
                break;
            case FlowConstant.MONTHLY_BULLETIN_FLOW:
                resultPage = "monthlyNewsletter/flow/flowDeal";
                break;
            case FlowConstant.ANNOUNT_MENT_FLOW:
                resultPage = "annountMent/flow/flowDeal";
                break;
            case FlowConstant.BOOKS_BUY_FLOW:
                resultPage = "bookBuyBusiness/flowEnd";
                break;
            default:
                ;
        }
        return resultPage;
    }

    @RequestMapping(name = "获取流程列表",path = "proc",method = RequestMethod.GET)
    @ResponseBody
    public List<Map<String, Object>> getProc(){
        return  flowService.getProc();
    }

    @RequiresAuthentication
    @RequestMapping(name = "重写工作方案", path = "rollbackSend/{processKey}", method = RequestMethod.GET)
    public String rollbackSend(@PathVariable("processKey") String processKey) {
        String resultPage = "";
        switch (processKey){
            case FlowConstant.ROLL_BACK_SEND_FLOW:
                resultPage = "rollbackSend/rollbackSend";
                break;
            case FlowConstant.QUERY_BRANCH_INFO:
                resultPage = "rollbackSend/getBranchInfo";
                break;
            default:
                ;
        }
        return resultPage;
    }
}