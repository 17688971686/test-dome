package cs.controller.flow;

import java.io.InputStream;
import java.text.ParseException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.activiti.bpmn.model.BpmnModel;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.activiti.engine.impl.context.Context;
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
import cs.common.ICurrentUser;
import cs.common.Constant.MsgCode;
import cs.common.ResultMsg;
import cs.common.utils.Validate;
import cs.model.PageModelDto;
import cs.model.flow.FlowDto;
import cs.model.flow.FlowHistoryDto;
import cs.model.flow.Node;
import cs.model.flow.TaskDto;
import cs.repository.odata.ODataObj;
import cs.service.flow.FlowService;
import cs.service.project.SignService;
import cs.service.project.SignServiceImpl;

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
	private ICurrentUser currentUser;
	
	@RequiresPermissions("flow#html/tasks#post")
	@RequestMapping(name = "个人待办流程", path = "html/tasks",method=RequestMethod.POST)
	public @ResponseBody PageModelDto<TaskDto> tasks(HttpServletRequest request) throws ParseException  {	
		ODataObj odataObj = new ODataObj(request);				
		PageModelDto<TaskDto> pageModelDto = flowService.queryGTasks(odataObj);				
		return pageModelDto;
	}
	
	@RequiresPermissions("flow#html/tasksCount#get")
	@RequestMapping(name = "待办流程", path = "html/tasksCount",method=RequestMethod.GET)
	public @ResponseBody long tasksCount(HttpServletRequest request) throws ParseException  {	
		TaskQuery taskQuery = taskService.createTaskQuery();
		taskQuery.taskCandidateOrAssigned(currentUser.getLoginUser().getLoginName());			
		return taskQuery.count();
	}

	@RequiresPermissions("flow#html/endTasks#post")
	@RequestMapping(name = "已办结流程", path = "html/endTasks",method=RequestMethod.POST)
	public @ResponseBody PageModelDto<TaskDto> endTasks(HttpServletRequest request) throws ParseException  {
		ODataObj odataObj = new ODataObj(request);
		PageModelDto<TaskDto> pageModelDto = flowService.queryETasks(odataObj);
		return pageModelDto;
	}

	@RequestMapping(name = "读取流程图",path = "processInstance/img/{processInstanceId}",method = RequestMethod.GET)
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
        InputStream imageStream = diagramGenerator.generateDiagram(bpmnModel, "png",activeActivityIds, activeActivityIds,
        		processEngineConfiguration.getActivityFontName(), processEngineConfiguration.getLabelFontName(),
        		processEngineConfiguration.getClassLoader(), 1.0);
        
        return imageStream;

	}
	
	@RequestMapping(name = "读取流程历史记录",path = "processInstance/history/{processInstanceId}",method = RequestMethod.POST)
	public @ResponseBody PageModelDto<FlowHistoryDto> findHisActivitiList(@PathVariable("processInstanceId") String processInstanceId){   	    
		List<FlowHistoryDto> list = flowService.convertHistory(processInstanceId);		
	    PageModelDto<FlowHistoryDto> pageModelDto = new PageModelDto<FlowHistoryDto>();				
		pageModelDto.setCount(list.size());
		pageModelDto.setValue(list);		
	    return pageModelDto;
	}  

	@RequestMapping(name = "获取流程处理信息",path = "processInstance/flowNodeInfo",method = RequestMethod.GET)
	public @ResponseBody FlowDto flowNodeInfo(@RequestParam(required = true) String taskId,@RequestParam(required = true) String processInstanceId){
		FlowDto flowDto = new FlowDto();
		flowDto.setTaskId(taskId);
		flowDto.setProcessInstanceId(processInstanceId);
		flowDto.setEnd(false);
		flowDto.setSeleteNode(false);
		    		
		//流程实例
		ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();		
		flowDto.setProcessKey(processInstance.getProcessDefinitionKey());
	
		Task task = taskService.createTaskQuery().taskId(taskId).active().singleResult();   
		if(task != null){
			Node curNode = new Node();
			curNode.setActivitiName(task.getName());
			curNode.setActivitiId(task.getTaskDefinitionKey());			
			flowDto.setCurNode(curNode);
		}
		if(processInstance.getProcessDefinitionKey().equals(Constant.EnumFlow.FINAL_SIGN.getValue())){
			if(Constant.FLOW_BMLD_QR_GD.equals(task.getTaskDefinitionKey())){
				flowDto.setEnd(true);
			}
		}			
				
		return flowDto;
	}
		
	
	@RequestMapping(name = "流程提交",path = "commit",method = RequestMethod.POST)
	public @ResponseBody ResultMsg flowCommit(@RequestBody FlowDto flowDto) throws Exception{
		ResultMsg resultMsg = null;
		ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(flowDto.getProcessInstanceId()).singleResult();	
		if(processInstance.getProcessDefinitionKey().equals(Constant.EnumFlow.FINAL_SIGN.getValue())){
			resultMsg = signService.dealFlow(processInstance, flowDto);			
		}		
		return resultMsg;
	}

	@RequestMapping(name = "回退到上一个环节",path = "rollbacklast",method = RequestMethod.POST)
	public @ResponseBody ResultMsg rollBackLast(@RequestBody FlowDto flowDto){
		ResultMsg resultMsg = new ResultMsg();
		if(Validate.isString(flowDto.getTaskId())){
			flowService.RollBackLastNode(flowDto);
			resultMsg.setReCode(MsgCode.OK.getValue());
			resultMsg.setReMsg("回退成功！");
		}else{
			resultMsg.setReCode(MsgCode.ERROR.getValue());
			resultMsg.setReMsg(Constant.ERROR_MSG);
			log.info("流程回退到上一步异常：无法获取任务ID(TaskId)");
		}
		return resultMsg;
	}
	
	@RequestMapping(name = "回退到指定环节",path = "rollback",method = RequestMethod.POST)
	public @ResponseBody ResultMsg rollback(@RequestBody FlowDto flowDto){
		ResultMsg resultMsg = new ResultMsg();
		if(Validate.isString(flowDto.getTaskId())){
			flowService.RollBackByActiviti(flowDto);
			resultMsg.setReCode(MsgCode.OK.getValue());
			resultMsg.setReMsg("回退成功！");
		}else{
			resultMsg.setReCode(MsgCode.ERROR.getValue());
			resultMsg.setReMsg(Constant.ERROR_MSG);
			log.info("流程回退到上一步异常：无法获取任务ID(TaskId)");
		}
		return resultMsg;
	}	
	
	@Transactional
	@RequestMapping(name = "激活流程", path = "active/{businessKey}",method = RequestMethod.POST)  	
	@ResponseStatus( value =HttpStatus.NO_CONTENT)
    public void activeFlow(@PathVariable("businessKey") String businessKey) {  
    	ProcessInstance processInstance = flowService.findProcessInstanceByBusinessKey(businessKey);
    	runtimeService.activateProcessInstanceById(processInstance.getId()); 
    	if(processInstance.getProcessDefinitionKey().equals(Constant.EnumFlow.FINAL_SIGN.getValue())){
    		signService.restartFlow(businessKey);
    	}
    	log.info("流程激活成功！businessKey="+businessKey);
    }  
	
	@Transactional
	@RequestMapping(name = "挂起流程", path = "suspend/{businessKey}",method = RequestMethod.POST)  	
	@ResponseStatus( value =HttpStatus.NO_CONTENT)
    public void suspendFlow(@PathVariable("businessKey") String businessKey) {  
    	ProcessInstance processInstance = flowService.findProcessInstanceByBusinessKey(businessKey);
    	runtimeService.suspendProcessInstanceById(processInstance.getId()); 
    	if(processInstance.getProcessDefinitionKey().equals(Constant.EnumFlow.FINAL_SIGN.getValue())){
    		signService.stopFlow(businessKey);
    	}
    	log.info("流程挂起成功！businessKey="+businessKey);
    } 
	
	@Transactional
	@RequestMapping(name = "终止流程",path = "deleteFLow",method = RequestMethod.POST)  	
	@ResponseStatus( value =HttpStatus.NO_CONTENT)
    public void deleteFlow(@RequestParam FlowDto flowDto) {  
		//流程实例
		ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(flowDto.getProcessInstanceId()).singleResult();
		runtimeService.deleteProcessInstance(processInstance.getId(), flowDto.getDealOption());
		if(processInstance.getProcessDefinitionKey().equals(Constant.EnumFlow.FINAL_SIGN.getValue())){
    		signService.endFlow(processInstance.getBusinessKey());
    	}
    	log.info("流程终止成功！businessKey="+processInstance.getBusinessKey());
    }  
}