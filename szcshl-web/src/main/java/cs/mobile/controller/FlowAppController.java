package cs.mobile.controller;

import com.alibaba.fastjson.JSONObject;
import cs.common.ResultMsg;
import cs.common.constants.Constant;
import cs.common.constants.FlowConstant;
import cs.common.utils.Validate;
import cs.domain.flow.HiProcessTask;
import cs.domain.project.SignBranch;
import cs.domain.sys.Log;
import cs.domain.sys.OrgDept;
import cs.domain.sys.OrgDept_;
import cs.domain.sys.User;
import cs.mobile.service.FlowAppService;
import cs.mobile.service.IFlowApp;
import cs.mobile.service.WorkDynamicService;
import cs.model.PageModelDto;
import cs.model.flow.FlowDto;
import cs.model.flow.Node;
import cs.model.sys.UserDto;
import cs.repository.repositoryImpl.sys.OrgDeptRepo;
import cs.service.archives.ArchivesLibraryService;
import cs.service.asserts.assertStorageBusiness.AssertStorageBusinessService;
import cs.service.book.BookBuyBusinessService;
import cs.service.flow.FlowNextNodeFilter;
import cs.service.flow.FlowService;
import cs.service.flow.IFlow;
import cs.service.monthly.MonthlyNewsletterService;
import cs.service.project.AddSuppLetterService;
import cs.service.project.ProjectStopService;
import cs.service.project.SignBranchService;
import cs.service.project.SignService;
import cs.service.reviewProjectAppraise.AppraiseService;
import cs.service.rtx.RTXService;
import cs.service.sys.AnnountmentService;
import cs.service.sys.LogService;
import cs.service.sys.UserService;
import cs.service.topic.TopicInfoService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Description: 移动端 控制层
 * author: zsl
 * Date: 2018-3-1 15:33:41
 */
@Controller
@RequestMapping(name = "流程控制", path = "api/flowApp")
public class FlowAppController {

    private static Logger log = Logger.getLogger(cs.controller.flow.FlowController.class);

    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    private WorkDynamicService workDynamicService;
    @Autowired
    private SignService signService;
    @Autowired
    private FlowService flowService;
    @Autowired
    private FlowAppService flowAppService;
    @Autowired
    private TaskService taskService;
    @Autowired
    @Qualifier("SignFlowAppImpl")
    private IFlowApp signFlowAppImpl;
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
    private TopicInfoService topicInfoService;
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
    private LogService logService;
    @Autowired
    private RTXService rtxService;
    @Autowired
    private UserService userService;
    @Autowired
    private OrgDeptRepo orgDeptRepo;
    @Autowired
    private SignBranchService signBranchService;

    @RequestMapping(name = "流程提交", path = "commit", method = RequestMethod.POST)
    @ResponseBody
    public  ResultMsg flowCommit(String flowObj,String username){
        //处理移动端传的对象
        String decodeData = "";
        try {
             decodeData = java.net.URLDecoder.decode(flowObj,"utf-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        FlowDto flowDto = JSONObject.parseObject(decodeData, FlowDto.class);
        if(Validate.isObject(flowDto)){

            String  dealOption = flowDto.getDealOption();
         /*   try {
                username = new String(username.getBytes("ISO-8859-1"),"UTF-8");

                if(Validate.isString(flowDto.getDealOption())){
                    dealOption = new String(dealOption.getBytes("ISO-8859-1"),"UTF-8");
                    flowDto.setDealOption(dealOption);
                }
                if(Validate.isObject(flowDto.getCurNode()) && Validate.isString(flowDto.getCurNode().getActivitiName())){
                    flowDto.getCurNode().setActivitiName(new String(flowDto.getCurNode().getActivitiName().getBytes("ISO-8859-1"),"UTF-8"));
                }
                if(Validate.isObject(flowDto.getNextNode()) && Validate.isString(flowDto.getNextNode().get(0).getActivitiName())){
                    flowDto.getNextNode().get(0).setActivitiName(new String(flowDto.getNextNode().get(0).getActivitiName().getBytes("ISO-8859-1"),"UTF-8"));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }*/

        }
        UserDto userDto=userService.findUserByName(username);
        ResultMsg resultMsg = null;
        String errorMsg = "";
        String module="";
        String businessKey = "";
        ProcessInstance processInstance = null;
        boolean isProj = false;
        //判断是任务还是项目
        String projectOrTask = "";
        Task task = null;
        try{
            processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(flowDto.getProcessInstanceId()).singleResult();
            if (Validate.isString(flowDto.getTaskId())) {
                task = taskService.createTaskQuery().taskId(flowDto.getTaskId()).active().singleResult();
            } else {
                task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).active().singleResult();
            }
            if (task == null) {
                return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), "该流程已被处理！");
            }
            if (task.isSuspended()) {
                return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), "项目已暂停，不能进行操作！");
            }
            module = processInstance.getProcessDefinitionKey();
            businessKey = processInstance.getBusinessKey();
            switch (module){
                case FlowConstant.SIGN_FLOW:
                    resultMsg = flowAppService.dealFlow(processInstance, task,flowDto,userDto);
                    isProj = true;
                    break;
                case FlowConstant.TOPIC_FLOW:
                    resultMsg = topicInfoService.dealFlow(processInstance, task,flowDto);
                    break;

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
                    resultMsg = flowAppService.monthlyDealFlow(processInstance, task,flowDto,userDto);
                    break;
                case FlowConstant.ANNOUNT_MENT_FLOW:
                    resultMsg = flowAppService.annountDealFlow(processInstance, task,flowDto,userDto);
                    break;
                default:
                    resultMsg = new ResultMsg(false, Constant.MsgCode.ERROR.getValue(),"操作失败，没有对应的流程！");
            }
        }catch (Exception e){
            errorMsg = e.getMessage();
            log.info("流程提交异常："+errorMsg);
            resultMsg = new ResultMsg(false, Constant.MsgCode.ERROR.getValue(),"操作异常，错误信息已记录，请联系管理员查看！");
        }
        //添加日记记录
        Log log = new Log();
        log.setCreatedDate(new Date());
        log.setUserName(userDto.getDisplayName());
        log.setLogCode(resultMsg.getReCode());
        log.setBuninessId(businessKey);
        log.setMessage(resultMsg.getReMsg()+errorMsg);
        log.setModule(Constant.LOG_MODULE.FLOWCOMMIT.getValue()+FlowConstant.getFLowNameByFlowKey(module) );
        log.setResult(resultMsg.isFlag()? Constant.EnumState.YES.getValue(): Constant.EnumState.NO.getValue());
        log.setLogger(this.getClass().getName()+".flowCommit");
        //优先级别高
        log.setLogLevel(Constant.EnumState.PROCESS.getValue());
        logService.save(log);
        //腾讯通消息处理
        rtxService.dealPoolRTXMsg(task.getTaskDefinitionKey(),flowDto.getTaskId(),resultMsg,processInstance.getName(),isProj? Constant.MsgType.project_type.name(): Constant.MsgType.task_type.name());
        return resultMsg;
    }


    @RequestMapping(name = "获取流程处理信息", path = "flowNodeInfo", method = RequestMethod.POST)
    public @ResponseBody
    FlowDto flowNodeInfo(@RequestParam(required = true) String taskId,  String processInstanceId,String username) {
        FlowDto flowDto = new FlowDto();
        flowDto.setProcessInstanceId(processInstanceId);
        flowDto.setEnd(false);
   /*     try {
            username = new String(username.getBytes("ISO-8859-1"),"UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }*/
        User u = userService.findByName(username);
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
                    flowDto.setBusinessMap(signFlowAppImpl.getFlowBusinessMap(processInstance.getBusinessKey(),task.getTaskDefinitionKey(),u.getId()));
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


    @RequestMapping(name = "处理记录", path = "historyInfo", method = RequestMethod.GET)
    public @ResponseBody
    PageModelDto<HiProcessTask> findHisActivitiList(@RequestParam String processInstanceId) {
        List<HiProcessTask> list = flowService.getProcessHistory(processInstanceId);
        PageModelDto<HiProcessTask> pageModelDto = new PageModelDto<HiProcessTask>();
        pageModelDto.setCount(list.size());
        pageModelDto.setValue(list);
        return pageModelDto;
    }



    @RequestMapping(name = "项目重新分办", path = "getBack", method = RequestMethod.POST)
    @ResponseBody
    @Transactional
    public ResultMsg getBack(@RequestParam(required = true) String taskId, String businessKey,String username) {
        String backActivityId = "", branch = "";
        UserDto userDto = userService.findUserByName(username);
        User user = userService.findByName(username);
        String level = userService.getUserLevel(user);
        if (Validate.isString(level) && ("1".equals(level) || "2".equals(level))) {
            backActivityId = FlowConstant.FLOW_SIGN_FGLD_FB;
        } else {
            OrgDept orgDept = orgDeptRepo.findById(OrgDept_.directorID.getName(),user.getId());
            if ((Validate.isString(level) && "3".equals(level)) || null != orgDept) {
                //根据当前用户所在部门ID，查询是哪个分支的取回
                SignBranch signBranch = signBranchService.findByOrgDirector(businessKey, orgDept.getId());
                if (signBranch != null) {
                    branch = signBranch.getBranchId();
                    if (FlowConstant.SignFlowParams.BRANCH_INDEX1.getValue().equals(branch)) {
                        backActivityId = FlowConstant.FLOW_SIGN_BMFB1;
                    } else if (FlowConstant.SignFlowParams.BRANCH_INDEX2.getValue().equals(branch)) {
                        backActivityId = FlowConstant.FLOW_SIGN_BMFB2;
                    } else if (FlowConstant.SignFlowParams.BRANCH_INDEX3.getValue().equals(branch)) {
                        backActivityId = FlowConstant.FLOW_SIGN_BMFB3;
                    } else if (FlowConstant.SignFlowParams.BRANCH_INDEX4.getValue().equals(branch)) {
                        backActivityId = FlowConstant.FLOW_SIGN_BMFB4;
                    }
                } else {
                    return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), "操作失败，你没有权限进行此操作！");
                }
            }
        }
        if (Validate.isString(backActivityId)) {
            ResultMsg result = null;
            try {
                result = flowAppService.callBackProcess(taskId, backActivityId, businessKey, Validate.isString(branch) ? false : true,userDto);
                //取回成功,则删除相应的分支信息
                if (result.isFlag()) {
                    signService.deleteBranchInfo(businessKey, Validate.isString(branch) ? branch : null);
                }
                return result;
            } catch (Exception e) {
                return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), "流程回退失败，请联系系统管理员查看！");
            }
        } else {
            return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), "操作失败，你没有权限进行此操作！");
        }
    }


    @RequestMapping(name = "流程回退", path = "rollbacklast", method = RequestMethod.POST)
    @ResponseBody
    @Transactional
    public ResultMsg rollBackLast(String flowObj) {
        //处理移动端传的对象
        String decodeData = "";
        try {
            decodeData = java.net.URLDecoder.decode(flowObj,"utf-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        FlowDto flowDto = JSONObject.parseObject(decodeData, FlowDto.class);
        ResultMsg resultMsg = flowService.rollBackLastNode(flowDto);
        return resultMsg;
    }


}
