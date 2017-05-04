package cs.controller;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.activiti.bpmn.model.BpmnModel;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.bpmn.behavior.UserTaskActivityBehavior;
import org.activiti.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.PvmActivity;
import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.impl.task.TaskDefinition;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.image.ProcessDiagramGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cs.common.Constant;
import cs.common.Constant.EnumFlowNodeGroupName;
import cs.common.Constant.MsgCode;
import cs.common.ICurrentUser;
import cs.common.ResultMsg;
import cs.common.utils.BeanCopierUtils;
import cs.common.utils.Validate;
import cs.domain.Org;
import cs.domain.User;
import cs.model.FlowDto;
import cs.model.FlowHistoryDto;
import cs.model.Node;
import cs.model.OrgDto;
import cs.model.PageModelDto;
import cs.model.UserDto;
import cs.service.FlowService;
import cs.service.SignService;
import cs.service.UserService;

@Controller
@RequestMapping(name = "流程", path = "flow")
public class FlowController {	
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
	
	@RequestMapping(name = "读取流程历史记录",path = "proccessInstance/history/{proccessInstanceId}",method = RequestMethod.GET)
	public @ResponseBody PageModelDto<FlowHistoryDto> findHisActivitiList(@PathVariable("proccessInstanceId") String proccessInstanceId){   	    
		List<FlowHistoryDto> list = flowService.convertHistory(proccessInstanceId);		
	    PageModelDto<FlowHistoryDto> pageModelDto = new PageModelDto<FlowHistoryDto>();				
		pageModelDto.setCount(list.size());
		pageModelDto.setValue(list);		
	    return pageModelDto;
	}  

	@RequestMapping(name = "获取下一环节处理信息",path = "proccessInstance/nextNodeDeal",method = RequestMethod.GET)
	public @ResponseBody FlowDto nextNodeDeal(@RequestParam(required = true) String proccessInstanceId){
		FlowDto flowDto = new FlowDto();
		flowDto.setEnd(false);
				
		//流程实例
		ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(proccessInstanceId).singleResult();		
		//获取当前环节信息
		ActivityImpl activityImpl = getCurNode(processInstance.getProcessDefinitionKey(),processInstance.getActivityId());		
		TaskDefinition curTaskDefinition = ((UserTaskActivityBehavior)activityImpl.getActivityBehavior()).getTaskDefinition();
		if(curTaskDefinition != null){
			Node curNode = new Node();
			curNode.setActivitiName(curTaskDefinition.getNameExpression().getExpressionText());
			curNode.setActivitiId(curTaskDefinition.getKey());			
			flowDto.setCurNode(curNode);
		}	
		//获取下一环节信息--获取从某个节点出来的所有线路
		List<PvmTransition> outTransitions = activityImpl.getOutgoingTransitions();
		if(outTransitions != null){
			List<Node> nextNodeList = new ArrayList<Node>(outTransitions.size());
			for(PvmTransition tr:outTransitions){
				PvmActivity ac = tr.getDestination(); //获取线路的终点节点
				Node nextNode = new Node();
				nextNode.setActivitiName(ac.getProperty("name").toString());
				nextNode.setActivitiId(tr.getId());	
				nextNodeList.add(nextNode);
			}
			flowDto.setNextNode(nextNodeList);
		}
				
		String roleName = null;
		UserDto curUser = null;
		List<UserDto> nextUserList = new ArrayList<UserDto>();
		
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
				case "leaderApprovalPlan":		//中心领导审批->发文申请
					break;
				case "dispatch":				//发文申请->部长审批
					break;
				case "ministerDispatches":		//部长审批->分管副主任审批
					break;
				case "secDirectorDispatches":	//分管副主任审批->主任审批
					break;
				case "directorDispatches":		//主任审批->归档
					break;	
				case "doFile":					//归档->
					break;	
				case "endevent1":				//结束
					flowDto.setEnd(true);
					break;
			}	
			
			if(nextUserList.size() == 0){
				if(Validate.isString(roleName)){
					flowDto.setNextGroup(roleName);
					nextUserList = userService.findUserByRoleName(roleName);								
				}else if(Validate.isObject(curUser)){
					flowDto.setNextGroup(curUser.getOrgDto().getName());
					nextUserList = userService.findUserByDeptId(curUser.getOrgDto().getId());						
				}
			}
			
						
			if(nextUserList != null && nextUserList.size() > 0){
				List<UserDto> nextUserDtoList = new ArrayList<UserDto>(nextUserList.size());
				nextUserList.forEach(u ->{
					UserDto ud = new UserDto();
					BeanCopierUtils.copyProperties(u, ud);
					nextUserDtoList.add(ud);
				});
				flowDto.setNextDealUserList(nextUserDtoList);
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

	@RequestMapping(name = "流程回退",path = "back",method = RequestMethod.POST)
	public @ResponseBody ResultMsg flowBack(@RequestParam FlowDto glowDto){
		ResultMsg resultMsg = new ResultMsg();
		
		return resultMsg;
	}
	
	@RequestMapping(name = "流程终止",path = "forcedend",method = RequestMethod.POST)
	public @ResponseBody ResultMsg flowForcedend(@RequestParam FlowDto glowDto){
		ResultMsg resultMsg = new ResultMsg();
		
		return resultMsg;
	}
	
	/**
	* 根据key获得一个最新的流程定义
	* @param key
	* @return
	*/
	private ProcessDefinition getNewProcessDefinition(String key) {
		//根据key查询已经激活的流程定义，并且按照版本进行降序。那么第一个就是将要得到的最新流程定义对象
		List<ProcessDefinition> processDefinitionList = repositoryService.createProcessDefinitionQuery().processDefinitionKey(key).orderByProcessDefinitionVersion().desc().list();
		if (processDefinitionList.size() > 0) {
			return processDefinitionList.get(0);
		}
		return null;
	}
	
	private ActivityImpl getCurNode(String flowKey,String activityId) {
		ProcessDefinition processDefinition = getNewProcessDefinition(flowKey);
		ProcessDefinitionEntity processDefinitionEntity = (ProcessDefinitionEntity)((RepositoryServiceImpl)repositoryService).getDeployedProcessDefinition(processDefinition.getId());  
		return  processDefinitionEntity.findActivity(activityId);//当前节点				  
	}
	 
}
