package cs.controller.flow;

import java.io.InputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cs.common.utils.SessionUtil;
import cs.repository.repositoryImpl.project.SignBranchRepo;
import cs.repository.repositoryImpl.project.SignMergeRepo;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.impl.task.TaskDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.activiti.image.ProcessDiagramGenerator;
import org.apache.log4j.Logger;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import cs.common.Constant;
import cs.common.Constant.MsgCode;
import cs.common.ResultMsg;
import cs.common.utils.BeanCopierUtils;
import cs.common.utils.Validate;
import cs.domain.flow.HiProcessTask;
import cs.domain.flow.RuProcessTask;
import cs.domain.sys.User;
import cs.model.PageModelDto;
import cs.model.flow.FlowDto;
import cs.model.flow.Node;
import cs.model.flow.TaskDto;
import cs.model.project.ProjectStopDto;
import cs.model.sys.UserDto;
import cs.repository.odata.ODataObj;
import cs.service.flow.FlowService;
import cs.service.project.SignPrincipalService;
import cs.service.project.SignService;
import cs.service.project.SignServiceImpl;
import cs.service.sys.OrgService;
import cs.service.sys.UserService;

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
    private UserService userService;
    @Autowired
    private OrgService orgService;
    @Autowired
    private SignPrincipalService signPrincipalService;
    @Autowired
    private SignBranchRepo signBranchRepo;
    @Autowired
    private SignMergeRepo signMergeRepo;

    @RequiresPermissions("flow#html/tasks#post")
    @RequestMapping(name = "待办任务", path = "html/tasks", method = RequestMethod.POST)
    public @ResponseBody
    PageModelDto<RuProcessTask> tasks(HttpServletRequest request) throws ParseException {
        ODataObj odataObj = new ODataObj(request);
        PageModelDto<RuProcessTask> pageModelDto = flowService.queryRunProcessTasks(odataObj,true);
        return pageModelDto;
    }

    @RequiresPermissions("flow#html/doingtasks#post")
    @RequestMapping(name = "在办任务", path = "html/doingtasks", method = RequestMethod.POST)
    public @ResponseBody
    PageModelDto<RuProcessTask> doingtasks(HttpServletRequest request) throws ParseException {
        ODataObj odataObj = new ODataObj(request);
        PageModelDto<RuProcessTask> pageModelDto = flowService.queryRunProcessTasks(odataObj,false);
        return pageModelDto;
    }

    @RequiresPermissions("flow#html/tasksCount#get")
    @RequestMapping(name = "待办流程", path = "html/tasksCount", method = RequestMethod.GET)
    public @ResponseBody
    long tasksCount(HttpServletRequest request) throws ParseException {
        TaskQuery taskQuery = taskService.createTaskQuery();
        taskQuery.taskCandidateOrAssigned(SessionUtil.getUserId());
        return taskQuery.count();
    }

    @RequiresPermissions("flow#html/endTasks#post")
    @RequestMapping(name = "办结任务", path = "html/endTasks", method = RequestMethod.POST)
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
        // 输出资源内容到相应对象
        byte[] b = new byte[1024];
        int len;
        while ((len = imageStream.read(b, 0, 1024)) != -1) {
            response.getOutputStream().write(b, 0, len);
        }
    }

    private InputStream getProcessInstanceImage(String processInstanceId) {
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
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

    @RequestMapping(name = "获取流程处理信息", path = "processInstance/flowNodeInfo", method = RequestMethod.GET)
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

        //加载环节业务数据
        if ((Constant.SIGN_FLOW.equals(flowDto.getProcessKey()))
                && Validate.isString(flowDto.getCurNode().getActivitiId())) {

            Map<String, Object> businessMap = new HashMap<>();
            String branchIndex = "";
            switch (flowDto.getCurNode().getActivitiId()) {
                //综合部拟办
                case Constant.FLOW_SIGN_ZHB:
                    businessMap.put("viceDirectors", userService.findUserByRoleName(Constant.EnumFlowNodeGroupName.VICE_DIRECTOR.getValue()));
                    break;
                //分管领导审核
                case Constant.FLOW_SIGN_FGLD_FB:
                    businessMap.put("orgs", orgService.listAll());
                    break;
                //部门分办（选择项目负责人）
                case Constant.FLOW_SIGN_BMFB1:
                case Constant.FLOW_SIGN_BMFB2:
                case Constant.FLOW_SIGN_BMFB3:
                case Constant.FLOW_SIGN_BMFB4:
                    List<UserDto> userList = userService.findUserByOrgId(SessionUtil.getUserInfo().getOrg().getId());
                    //排除项目负责人（这里是用户本身）
                    for(UserDto userDto:userList){
                        if(userDto.getId().equals(SessionUtil.getUserInfo().getId())){
                            userList.remove(userDto);
                            break;
                        }
                    }
                    businessMap.put("users", userList);
                    break;
                //项目负责人办理
                case Constant.FLOW_SIGN_XMFZR1:
                    branchIndex =  Constant.SignFlowParams.BRANCH_INDEX1.getValue();
                case Constant.FLOW_SIGN_XMFZR2:
                    if(!Validate.isString(branchIndex)){
                        branchIndex =  Constant.SignFlowParams.BRANCH_INDEX2.getValue();
                    }
                case Constant.FLOW_SIGN_XMFZR3:
                    if(!Validate.isString(branchIndex)){
                        branchIndex =  Constant.SignFlowParams.BRANCH_INDEX3.getValue();
                    }
                case Constant.FLOW_SIGN_XMFZR4:
                    if(!Validate.isString(branchIndex)){
                        branchIndex =  Constant.SignFlowParams.BRANCH_INDEX4.getValue();
                    }
                    businessMap.put("isFinishWP", signBranchRepo.checkFinishWP(processInstance.getBusinessKey(),branchIndex));
                    break;
                //发文申请
                case Constant.FLOW_SIGN_FW:
                    List<User> prilUserList = signPrincipalService.getAllSecondPriUser(processInstance.getBusinessKey());
                    if(Validate.isList(prilUserList)){
                        List<UserDto> userDtoList = new ArrayList<>(prilUserList.size());
                        prilUserList.forEach(pul ->{
                            UserDto userDto = new UserDto();
                            BeanCopierUtils.copyProperties(pul,userDto);
                            userDtoList.add(userDto);
                        });
                        businessMap.put("prilUserList", userDtoList);
                    }
                    break;
                //生成发文编号，如果是合并发文次项目，则不需要关联
                case Constant.FLOW_SIGN_FWBH:
                    boolean isMerge =signMergeRepo.checkIsMerege(processInstance.getBusinessKey(), Constant.MergeType.DISPATCH.getValue());
                    businessMap.put("needDISNum", !isMerge);
                    break;
                //项目归档
                case Constant.FLOW_SIGN_GD:
                    //项目负责人获取第一个作为处理人
                    List<User> checkUserList = signPrincipalService.getAllSecondPriUser(processInstance.getBusinessKey());
                    if(Validate.isList(checkUserList)){
                        businessMap.put("checkFileUser", checkUserList.get(0));
                    }
                    break;
                default:
            }
            flowDto.setBusinessMap(businessMap);
        }

        //获取下一环节信息
        if(flowDto.isEnd() == false){
            //获取下一环节信息--获取从某个节点出来的所有线路
            ProcessDefinitionEntity def = (ProcessDefinitionEntity) ((RepositoryServiceImpl) repositoryService)
                    .getDeployedProcessDefinition(task.getProcessDefinitionId());
            ActivityImpl activityImpl = def.findActivity(task.getTaskDefinitionKey());
            List<Node> nextNodeList = new ArrayList<>();
            flowService.nextTaskDefinition(nextNodeList,activityImpl,task.getTaskDefinitionKey());
            //排除掉一些环节
            switch (flowDto.getCurNode().getActivitiId()) {
                case Constant.FLOW_SIGN_FW:
                    //有项目负责人，则隐藏部长审核环节
                    if(flowDto.getBusinessMap().get("prilUserList") != null){
                        for(int i=0;i<nextNodeList.size();i++){
                            if((nextNodeList.get(i).getActivitiId()).equals(Constant.FLOW_SIGN_BMLD_QRFW))
                                nextNodeList.remove(i);
                        }
                    }
                case Constant.FLOW_SIGN_GD:
                    if(flowDto.getBusinessMap().get("checkFileUser") != null){
                        for(int i=0;i<nextNodeList.size();i++){
                            if((nextNodeList.get(i).getActivitiId()).equals(Constant.FLOW_SIGN_QRGD))
                                nextNodeList.remove(i);
                        }
                    }
                    break;
                default:
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
        switch (processInstance.getProcessDefinitionKey()){
            case Constant.SIGN_FLOW:
                resultMsg = signService.dealFlow(processInstance, flowDto);
                break;
            default:
                resultMsg = new ResultMsg(false,MsgCode.ERROR.getValue(),"操作失败，没有对应的流程！");
        }
        return resultMsg;
    }

    @RequestMapping(name = "回退到上一个环节", path = "rollbacklast", method = RequestMethod.POST)
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
}