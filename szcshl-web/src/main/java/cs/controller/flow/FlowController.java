package cs.controller.flow;

import cs.common.Constant;
import cs.common.Constant.MsgCode;
import cs.common.FlowConstant;
import cs.common.ResultMsg;
import cs.common.utils.Validate;
import cs.domain.flow.HiProcessTask;
import cs.domain.flow.RuProcessTask;
import cs.domain.flow.RuTask;
import cs.model.PageModelDto;
import cs.model.flow.FlowDto;
import cs.model.flow.Node;
import cs.model.flow.TaskDto;
import cs.model.project.ProjectStopDto;
import cs.repository.odata.ODataObj;
import cs.service.book.BookBuyBusinessService;
import cs.service.flow.FlowNextNodeFilter;
import cs.service.flow.FlowService;
import cs.service.flow.IFlow;
import cs.service.project.SignService;
import cs.service.project.SignServiceImpl;
import cs.service.topic.TopicInfoService;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.engine.*;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.image.ProcessDiagramGenerator;
import org.apache.log4j.Logger;
import org.apache.shiro.authz.annotation.RequiresPermissions;
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
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping(name = "流程", path = "flow")
public class FlowController {
    private static Logger log = Logger.getLogger(SignServiceImpl.class);

    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    private ProcessEngineConfiguration processEngineConfiguration;
    @Autowired
    private ProcessEngine processEngine;
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
    @Qualifier("booksBuyBusFlowImpl")
    private IFlow booksBuyBusFlowImpl;
    @Autowired
    @Qualifier("topicFlowImpl")
    private IFlow topicFlowImpl;
    @Autowired
    private TopicInfoService topicInfoService;
    @Autowired
    private BookBuyBusinessService bookBuyBusinessService;

    @RequiresPermissions("flow#html/tasks#post")
    @RequestMapping(name = "待办项目", path = "html/tasks", method = RequestMethod.POST)
    public @ResponseBody
    PageModelDto<RuProcessTask> tasks(HttpServletRequest request) throws ParseException {
        ODataObj odataObj = new ODataObj(request);
        PageModelDto<RuProcessTask> pageModelDto = flowService.queryRunProcessTasks(odataObj,true);
        return pageModelDto;
    }

    /**
     * 查询我的待办任务（除项目流程外）
     * @param request
     * @return
     * @throws ParseException
     */
    @RequiresPermissions("flow#queryMyAgendaTask#post")
    @RequestMapping(name = "我的待办任务", path = "queryMyAgendaTask", method = RequestMethod.POST)
    public @ResponseBody
    PageModelDto<RuTask> queryMyAgendaTask(HttpServletRequest request) throws ParseException {
        ODataObj odataObj = new ODataObj(request);
        PageModelDto<RuTask> pageModelDto = flowService.queryMyAgendaTask(odataObj);
        return pageModelDto;
    }

    @RequiresPermissions("flow#queryAgendaTask#post")
    @RequestMapping(name = "所有待办任务", path = "queryAgendaTask", method = RequestMethod.POST)
    public @ResponseBody
    PageModelDto<RuTask> queryAgendaTask(HttpServletRequest request) throws ParseException {
        ODataObj odataObj = new ODataObj(request);
        PageModelDto<RuTask> pageModelDto = flowService.queryAgendaTask(odataObj);
        return pageModelDto;
    }

    @RequiresPermissions("flow#html/personDtasks#post")
    @RequestMapping(name="个人在办项目",path="html/personDtasks",method=RequestMethod.POST)
    @ResponseBody
    public PageModelDto<RuProcessTask> personDtasks(HttpServletRequest request) throws ParseException {
        ODataObj odataObj = new ODataObj(request);
        PageModelDto<RuProcessTask> pageModelDto = flowService.queryPersonTasks(odataObj);
        return pageModelDto;
    }

    @RequiresPermissions("flow#html/doingtasks#post")
    @RequestMapping(name = "在办项目", path = "html/doingtasks", method = RequestMethod.POST)
    public @ResponseBody
    PageModelDto<RuProcessTask> doingtasks(HttpServletRequest request) throws ParseException {
        ODataObj odataObj = new ODataObj(request);
        PageModelDto<RuProcessTask> pageModelDto = flowService.queryRunProcessTasks(odataObj,false);
        return pageModelDto;
    }

    @RequiresPermissions("flow#html/endTasks#post")
    @RequestMapping(name = "办结项目", path = "html/endTasks", method = RequestMethod.POST)
    public @ResponseBody
    PageModelDto<TaskDto> endTasks(HttpServletRequest request) throws ParseException {
        ODataObj odataObj = new ODataObj(request);
        PageModelDto<TaskDto> pageModelDto = flowService.queryETasks(odataObj);
        return pageModelDto;
    }

    @RequiresPermissions("flow#getMyHomeTasks#post")
    @RequestMapping(name = "查询主页我的待办任务", path = "getMyHomeTasks", method = RequestMethod.POST)
    @ResponseBody
    public  List<RuProcessTask> getMyHomeTasks() {
        return flowService.queryMyRunProcessTasks();
    }

    @RequiresPermissions("flow#getMyHomeEndTask#postt")
    @RequestMapping(name = "获取主页上的办结任务", path = "getMyHomeEndTask", method = RequestMethod.POST)
    @ResponseBody
    public List<TaskDto> getMyHomeEndTask() {
        return flowService.queryMyEndTasks();
    }

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
        List<String> activeActivityIds = runtimeService.getActiveActivityIds(processInstanceId);
        // 使用spring注入引擎请使用下面的这行代码
        processEngineConfiguration = processEngine.getProcessEngineConfiguration();
        Context.setProcessEngineConfiguration((ProcessEngineConfigurationImpl) processEngineConfiguration);

        ProcessDiagramGenerator diagramGenerator = processEngineConfiguration.getProcessDiagramGenerator();
        InputStream imageStream = diagramGenerator.generateDiagram(bpmnModel, "png", activeActivityIds, activeActivityIds,
                processEngineConfiguration.getActivityFontName(), processEngineConfiguration.getLabelFontName(),
                processEngineConfiguration.getClassLoader(), 1.0);

        return imageStream;
    }

    @RequestMapping(name = "读取流程历史记录", path = "processInstance/history/{processInstanceId}", method = RequestMethod.POST)
    public @ResponseBody
    PageModelDto<HiProcessTask> findHisActivitiList(@PathVariable("processInstanceId") String processInstanceId) {
        List<HiProcessTask> list = flowService.getProcessHistory(processInstanceId);
        PageModelDto<HiProcessTask> pageModelDto = new PageModelDto<HiProcessTask>();
        pageModelDto.setCount(list.size());
        pageModelDto.setValue(list);
        return pageModelDto;
    }

    @RequestMapping(name = "获取流程处理信息", path = "processInstance/flowNodeInfo", method = RequestMethod.POST)
    public @ResponseBody
    FlowDto flowNodeInfo(@RequestParam(required = true) String taskId, @RequestParam(required = true) String processInstanceId) {
        FlowDto flowDto = new FlowDto();
        flowDto.setTaskId(taskId);
        flowDto.setProcessInstanceId(processInstanceId);
        flowDto.setEnd(false);

        //流程实例
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
        flowDto.setProcessKey(processInstance.getProcessDefinitionKey());
        flowDto.setEnd(processInstance.isEnded());

        //获取当前任务数据
        Task task = taskService.createTaskQuery().taskId(taskId).active().singleResult();
        if (task != null) {
            Node curNode = new Node();
            curNode.setActivitiName(task.getName());
            curNode.setActivitiId(task.getTaskDefinitionKey());
            flowDto.setCurNode(curNode);
            flowDto.setSuspended(task.isSuspended());
        }
        flowDto.setProcessKey(processInstance.getProcessDefinitionKey());

        /**
         * 流程的一些参数处理
         */
        switch (processInstance.getProcessDefinitionKey()){
            case Constant.SIGN_FLOW:
                flowDto.setBusinessMap(signFlowImpl.getFlowBusinessMap(processInstance.getBusinessKey(),task.getTaskDefinitionKey()));
                break;
            case FlowConstant.TOPIC_BFGW:
                flowDto.setBusinessMap(topicFlowImpl.getFlowBusinessMap(processInstance.getBusinessKey(),task.getTaskDefinitionKey()));
                break;
            case FlowConstant.BOOKS_BUY_FLOW:
                flowDto.setBusinessMap(booksBuyBusFlowImpl.getFlowBusinessMap(processInstance.getBusinessKey(),task.getTaskDefinitionKey()));
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

        return flowDto;
    }


    @RequestMapping(name = "流程提交", path = "commit", method = RequestMethod.POST)
    public @ResponseBody
    ResultMsg flowCommit(@RequestBody FlowDto flowDto) throws Exception {
        ResultMsg resultMsg = null;
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(flowDto.getProcessInstanceId()).singleResult();
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
        switch (processInstance.getProcessDefinitionKey()){
            case Constant.SIGN_FLOW:
                resultMsg = signService.dealFlow(processInstance, task,flowDto);
                break;
            case FlowConstant.TOPIC_BFGW:
                resultMsg = topicInfoService.dealFlow(processInstance, task,flowDto);
                break;
            case FlowConstant.BOOKS_BUY_FLOW:
                resultMsg = bookBuyBusinessService.dealFlow(processInstance, task,flowDto);
                break;
            default:
                resultMsg = new ResultMsg(false,MsgCode.ERROR.getValue(),"操作失败，没有对应的流程！");
        }
        return resultMsg;
    }

    @RequestMapping(name = "流程回退", path = "rollbacklast", method = RequestMethod.POST)
    public @ResponseBody ResultMsg rollBackLast(@RequestBody FlowDto flowDto) {
        ResultMsg resultMsg = flowService.rollBackLastNode(flowDto);
        return resultMsg;
    }

    @RequestMapping(name = "回退到指定环节", path = "rollback", method = RequestMethod.POST)
    public @ResponseBody
    ResultMsg rollback(@RequestBody FlowDto flowDto) {
        ResultMsg resultMsg = new ResultMsg();
        if (Validate.isString(flowDto.getTaskId())) {
            flowService.rollBackByActiviti(flowDto);
            resultMsg.setReCode(MsgCode.OK.getValue());
            resultMsg.setReMsg("回退成功！");
        } else {
            resultMsg.setReCode(MsgCode.ERROR.getValue());
            resultMsg.setReMsg(Constant.ERROR_MSG);
            log.info("流程回退到上一步异常：无法获取任务ID(TaskId)");
        }
        return resultMsg;
    }

    @Transactional
    @RequestMapping(name = "激活流程", path = "active/{businessKey}", method = RequestMethod.POST)
    public @ResponseBody ResultMsg activeFlow(@PathVariable("businessKey") String businessKey) {
        log.info("流程激活成功！businessKey=" + businessKey);
        return signService.restartFlow(businessKey);
    }

    /**
     * 流程挂起
     * @param businessKey
     * @return
     */
    @Transactional
    @RequestMapping(name = "挂起流程", path = "suspend/{businessKey}", method = RequestMethod.POST)
    @ResponseBody
    public ResultMsg suspendFlow(@PathVariable("businessKey") String businessKey,@RequestBody ProjectStopDto projectStopDto) {
        log.info("流程挂起成功！businessKey=" + businessKey);
        return signService.stopFlow(businessKey,projectStopDto);

    }

    @Transactional
    @RequestMapping(name = "终止流程", path = "deleteFLow", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public ResultMsg deleteFlow(@RequestParam FlowDto flowDto) {
        ResultMsg resultMsg = null;
        //流程实例
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(flowDto.getProcessInstanceId()).singleResult();
        switch (processInstance.getProcessDefinitionKey()){
            case Constant.SIGN_FLOW:
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

    /******************************   以下是页面处理  ******************************/
    @RequestMapping(name = "待办任务页面", path = "flowDeal/{processKey}", method = RequestMethod.GET)
    public String ruProcessTask(@PathVariable("processKey") String processKey) {
        String resultPage = "";
        switch (processKey){
            case FlowConstant.TOPIC_FLOW:
                resultPage = "topicInfo/flowDeal";
                break;
            case FlowConstant.BOOKS_BUY_FLOW:
                resultPage = "bookBuyBusiness/flowDeal";
                break;
            default:
                ;
        }
        return resultPage;
    }
}