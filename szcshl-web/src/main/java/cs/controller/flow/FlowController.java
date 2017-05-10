package cs.controller.flow;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.activiti.bpmn.model.BpmnModel;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.impl.bpmn.behavior.UserTaskActivityBehavior;
import org.activiti.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.impl.task.TaskDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.image.ProcessDiagramGenerator;
import org.apache.log4j.Logger;
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
import cs.common.Constant.EnumFlowNodeGroupName;
import cs.common.Constant.MsgCode;
import cs.common.ICurrentUser;
import cs.common.ResultMsg;
import cs.common.utils.Validate;
import cs.model.PageModelDto;
import cs.model.flow.FlowDto;
import cs.model.flow.FlowHistoryDto;
import cs.model.flow.Node;
import cs.model.project.SignDto;
import cs.model.sys.OrgDto;
import cs.model.sys.UserDto;
import cs.service.flow.FlowService;
import cs.service.project.SignService;
import cs.service.project.SignServiceImpl;
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
	private UserService userService;
	@Autowired
	private ICurrentUser currentUser;
	@Autowired
	private FlowService flowService;
	
	
	@RequestMapping(name = "读取流程图",path = "proccessInstance/img/{proccessInstanceId}",method = RequestMethod.GET)
    public void readProccessInstanceImg(@PathVariable("proccessInstanceId") String proccessInstanceId, HttpServletResponse response)
            throws Exception {   	
    	InputStream imageStream = getProccessInstanceImage(proccessInstanceId); 
        // 输出资源内容到相应对象
        byte[] b = new byte[1024];
        int len;
        while ((len = imageStream.read(b, 0, 1024)) != -1) {
            response.getOutputStream().write(b, 0, len);
        }
    }
	
	private InputStream getProccessInstanceImage(String proccessInstanceId) {
		ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(proccessInstanceId).singleResult();
        BpmnModel bpmnModel = repositoryService.getBpmnModel(processInstance.getProcessDefinitionId());
        List<String> activeActivityIds = runtimeService.getActiveActivityIds(proccessInstanceId);        
        // 使用spring注入引擎请使用下面的这行代码
        processEngineConfiguration = processEngine.getProcessEngineConfiguration();
        Context.setProcessEngineConfiguration((ProcessEngineConfigurationImpl) processEngineConfiguration);

        ProcessDiagramGenerator diagramGenerator = processEngineConfiguration.getProcessDiagramGenerator();
        InputStream imageStream = diagramGenerator.generateDiagram(bpmnModel, "png",activeActivityIds, activeActivityIds,
        		processEngineConfiguration.getActivityFontName(), processEngineConfiguration.getLabelFontName(),
        		processEngineConfiguration.getClassLoader(), 1.0);
        
        return imageStream;

	}
	
	@RequestMapping(name = "读取流程历史记录",path = "proccessInstance/history/{proccessInstanceId}",method = RequestMethod.POST)
	public @ResponseBody PageModelDto<FlowHistoryDto> findHisActivitiList(@PathVariable("proccessInstanceId") String proccessInstanceId){   	    
		List<FlowHistoryDto> list = flowService.convertHistory(proccessInstanceId);		
	    PageModelDto<FlowHistoryDto> pageModelDto = new PageModelDto<FlowHistoryDto>();				
		pageModelDto.setCount(list.size());
		pageModelDto.setValue(list);		
	    return pageModelDto;
	}  

	@RequestMapping(name = "获取下一环节处理信息",path = "proccessInstance/nextNodeDeal",method = RequestMethod.GET)
	public @ResponseBody FlowDto nextNodeDeal(@RequestParam(required = true) String taskId,@RequestParam(required = true) String proccessInstanceId){
		FlowDto flowDto = new FlowDto();
		flowDto.setEnd(false);
		flowDto.setSeleteNode(false);
				
		//流程实例
		ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(proccessInstanceId).singleResult();		
		flowDto.setProcessKey(processInstance.getProcessDefinitionKey());
		
		//获取当前环节信息
		ActivityImpl activityImpl = flowService.getActivityImpl(taskId,processInstance.getActivityId());		
		TaskDefinition curTaskDefinition = ((UserTaskActivityBehavior)activityImpl.getActivityBehavior()).getTaskDefinition();
		if(curTaskDefinition != null){
			Node curNode = new Node();
			curNode.setActivitiName(curTaskDefinition.getNameExpression().getExpressionText());
			curNode.setActivitiId(curTaskDefinition.getKey());			
			flowDto.setCurNode(curNode);
		}						
		String roleName = null;
		UserDto curUser = null;
		List<UserDto> nextUserList = new ArrayList<UserDto>();
		boolean isSetValue = false;	//是否已经设置值
		
		if(processInstance.getProcessDefinitionKey().equals(Constant.EnumFlow.SIGN.getValue())){
			switch(processInstance.getActivityId()){					
				case "ministerApproval":		//部长审批->中心领导
					roleName = EnumFlowNodeGroupName.DEPT_LEADER.getValue();										
					break;
				case "leaderApproval":			//中心领导->选负责人
					roleName = EnumFlowNodeGroupName.DEPT_PRINCIPAL.getValue();		
					break;
				case "selectPrincipal":			//选负责人-> 审批项目
					curUser = userService.findUserByName( currentUser.getLoginName());
					break;
				case "approval":				//审批项目-> 部长审批会议方案
					curUser = userService.findUserByName( currentUser.getLoginName());
					OrgDto org = curUser.getOrgDto();
					UserDto user = userService.findById(org.getOrgDirector());
					nextUserList.add(user);
					break;
				case "approvalPlan":			//部长审批会议方案->中心领导审批
					roleName = EnumFlowNodeGroupName.DEPT_LEADER.getValue();
					break;
				case "leaderApprovalPlan":		//中心领导审批->(项目负责人)发文申请	
					SignDto signDto = signService.findById(processInstance.getBusinessKey());
					curUser = userService.findUserByName(signDto.getMainchargeuserid());
					nextUserList.add(curUser);
					isSetValue = true;
					break;
				case "dispatch":				//发文申请->部长审批
					curUser = userService.findUserByName( currentUser.getLoginName());
					OrgDto minOrg = curUser.getOrgDto();
					if(minOrg != null){
						UserDto minUser = userService.findById(minOrg.getOrgDirector());
						nextUserList.add(minUser);
					}	
					isSetValue = true;
					break;
				case "ministerDispatches":		//部长审批->分管副主任审批
					curUser = userService.findUserByName( currentUser.getLoginName());
					OrgDto slOrg = curUser.getOrgDto();
					UserDto slUser = userService.findById(slOrg.getOrgSLeader());
					nextUserList.add(slUser);
					isSetValue = true;
					break;
				case "secDirectorDispatches":	//分管副主任审批->主任审批
					curUser = userService.findUserByName( currentUser.getLoginName());
					OrgDto mlOrg = curUser.getOrgDto();
					UserDto mlUser = userService.findById(mlOrg.getOrgMLeader());
					nextUserList.add(mlUser);
					isSetValue = true;
					break;
				case "directorDispatches":		//主任审批->归档
					roleName = EnumFlowNodeGroupName.FILER.getValue();	
					break;	
				case "doFile":					//归档->
					flowDto.setSeleteNode(true);
					Map<String,List<UserDto>> nextUserListMap = new HashMap<String,List<UserDto>>(2);
					UserDto doFileUser = userService.findById("910ec17f-c05e-4aa9-a8cf-99c59f5d2e3b");
					nextUserList.add(doFileUser);
					nextUserListMap.put("secondApproval", nextUserList);
					
					nextUserList = new ArrayList<UserDto>(); 
					doFileUser = userService.findById("f84c510e-7683-4d6f-bcb1-03edabc54daf");
					nextUserList.add(doFileUser);
					nextUserListMap.put("doConfirmFile", nextUserList);
					flowDto.setNextUserListMap(nextUserListMap);
					isSetValue = true;
					break;	
				case "secondApproval":			//第二负责人确认
					roleName = EnumFlowNodeGroupName.DEPT_LEADER.getValue();
					break;		
				case "doConfirmFile":			//归档确认结束环节
					flowDto.setEnd(true);
					break;
				case "endevent1":				//结束
					flowDto.setEnd(true);
					break;
			}	
			
			if(isSetValue == false && flowDto.isEnd() == false){
				if(Validate.isString(roleName)){
					flowDto.setNextGroup(roleName);
					nextUserList = userService.findUserByRoleName(roleName);								
				}else if(Validate.isObject(curUser)){
					flowDto.setNextGroup(curUser.getOrgDto().getName());
					nextUserList = userService.findUserByDeptId(curUser.getOrgDto().getId());						
				}
			}				
			flowDto.setNextDealUserList(nextUserList);
			
			
			if(flowDto.isEnd() == false){
				//获取下一环节信息--获取从某个节点出来的所有线路
				List<TaskDefinition> taskDefinitionList = new ArrayList<TaskDefinition>();
				flowService.nextTaskDefinition(taskDefinitionList,activityImpl,processInstance.getActivityId());
				
				if(taskDefinitionList.size()>0){
					List<Node> nextNodeList = new ArrayList<Node>(taskDefinitionList.size());    		
					taskDefinitionList.forEach(tf->{
						Node nextNode = new Node();
						nextNode.setActivitiId(tf.getKey());        		
						nextNode.setActivitiName(tf.getNameExpression().getExpressionText());
		        		nextNodeList.add(nextNode);
		        	});
		    		
					flowDto.setNextNode(nextNodeList);
		    	}
			}
		}		
		return flowDto;
	}
		
	
	@RequestMapping(name = "流程提交",path = "commit",method = RequestMethod.POST)
	public @ResponseBody ResultMsg flowCommit(@RequestBody FlowDto flowDto) throws Exception{
		ResultMsg resultMsg = null;
		ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(flowDto.getProcessInstanceId()).singleResult();	
		if(processInstance.getProcessDefinitionKey().equals(Constant.EnumFlow.SIGN.getValue())){
				resultMsg = signService.dealSignFlow(processInstance, flowDto);			
		}
		if(resultMsg == null){
			resultMsg = new ResultMsg();
			resultMsg.setReCode(MsgCode.ERROR.getValue());
			resultMsg.setReMsg("流程处理失败，失败记录已记录，请联系管理员进行处理！");
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
	
	@RequestMapping(name = "流程终止",path = "forcedend",method = RequestMethod.POST)
	public @ResponseBody ResultMsg flowForcedend(@RequestParam FlowDto glowDto){
		ResultMsg resultMsg = new ResultMsg();
		
		return resultMsg;
	}
	
	@Transactional
	@RequestMapping(name = "激活流程", path = "active/{businessKey}",method = RequestMethod.POST)  	
	@ResponseStatus( value =HttpStatus.NO_CONTENT)
    public void activeFlow(@PathVariable("businessKey") String businessKey) {  
    	ProcessInstance processInstance = flowService.findProcessInstanceByBusinessKey(businessKey);
    	runtimeService.activateProcessInstanceById(processInstance.getId()); 
    	if(processInstance.getProcessDefinitionKey().equals(Constant.EnumFlow.SIGN.getValue())){
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
    	if(processInstance.getProcessDefinitionKey().equals(Constant.EnumFlow.SIGN.getValue())){
    		signService.stopFlow(businessKey);
    	}
    	log.info("流程挂起成功！businessKey="+businessKey);
    } 
	
	@Transactional
	@RequestMapping(name = "删除流程",path = "deleteFLow",method = RequestMethod.POST)  	
	@ResponseStatus( value =HttpStatus.NO_CONTENT)
    public void deleteFlow(@RequestParam FlowDto flowDto) {  
		//流程实例
		ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(flowDto.getProcessInstanceId()).singleResult();
		runtimeService.deleteProcessInstance(processInstance.getId(), flowDto.getDealOption());
		if(processInstance.getProcessDefinitionKey().equals(Constant.EnumFlow.SIGN.getValue())){
    		signService.endFlow(processInstance.getBusinessKey());
    	}
    	log.info("流程终止成功！businessKey="+processInstance.getBusinessKey());
    }  
}
