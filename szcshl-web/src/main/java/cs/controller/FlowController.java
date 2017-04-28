package cs.controller;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.activiti.bpmn.model.BpmnModel;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.activiti.engine.impl.context.Context;
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
import cs.common.ResultMsg;
import cs.common.utils.Validate;
import cs.model.FlowDto;
import cs.model.FlowHistoryDto;
import cs.model.PageModelDto;
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
	public @ResponseBody Map<String,Object> nextNodeDeal(@RequestParam(required = true) String proccessInstanceId){
		Map<String,Object> result  = new HashMap<String,Object>();
		result.put("isEnd", false);
		
		ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(proccessInstanceId).singleResult();		
		String roleName = "";
		if(processInstance.getProcessDefinitionKey().equals(Constant.EnumFlow.SIGN.getValue())){
			switch(processInstance.getActivityId()){					
				case "ministerApproval":		//部长审批->中心领导
					roleName = EnumFlowNodeGroupName.DEPT_LEADER.getValue();										
					break;
				case "leaderApproval":			//中心领导->选负责人
					roleName = EnumFlowNodeGroupName.DEPT_PRINCIPAL.getValue();		
					break;
				case "selectPrincipal":			//负责人-> 审批项目
					break;
				case "approval":				//审批项目->部长审批会议方案
					break;
				case "approvalPlan":			//部长审批会议方案->中心领导审批
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
					result.put("isEnd", true);
					break;
			}	
			if(Validate.isString(roleName)){
				result.put("nextGroup", roleName);	
				result.put("nextDealUserList",userService.findUserByRoleName(roleName) );	
			}			
		}		
		return result;
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
}
