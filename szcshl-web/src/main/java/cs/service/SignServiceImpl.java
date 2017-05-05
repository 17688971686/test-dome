package cs.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cs.common.Constant;
import cs.common.Constant.EnumState;
import cs.common.Constant.MsgCode;
import cs.common.ICurrentUser;
import cs.common.ResultMsg;
import cs.common.utils.ActivitiUtil;
import cs.common.utils.BeanCopierUtils;
import cs.common.utils.Validate;
import cs.domain.Org;
import cs.domain.Sign;
import cs.domain.WorkProgram;
import cs.model.FlowDto;
import cs.model.OrgDto;
import cs.model.PageModelDto;
import cs.model.RoleDto;
import cs.model.SignDto;
import cs.model.UserDto;
import cs.model.WorkProgramDto;
import cs.repository.odata.ODataObj;
import cs.repository.repositoryImpl.DispatchDocRepo;
import cs.repository.repositoryImpl.OrgRepo;
import cs.repository.repositoryImpl.SignRepo;
import cs.repository.repositoryImpl.WorkProgramRepo;

@Service
public class SignServiceImpl implements SignService {
	private static Logger log = Logger.getLogger(SignServiceImpl.class);
	
	@Autowired
	private SignRepo signRepo;
	@Autowired
	private ICurrentUser currentUser;
	@Autowired
	private UserService userService;
	@Autowired
	private OrgRepo orgRepo;
	@Autowired
	private WorkProgramRepo workProgramRepo;
	@Autowired
	private DispatchDocRepo dispatchDocRepo;
	
	//flow service
	@Autowired
	private TaskService taskService;
	@Autowired
	private RuntimeService runtimeService;
	
	@Override
	@Transactional
	public void createSign(SignDto signDto) { 
		Sign sign = new Sign(); 
		SignDtoToSign(signDto,sign);     
		sign.setSignState(EnumState.NORMAL.getValue());
        signRepo.save(sign);
	}			
	
	@Override
	public PageModelDto<SignDto> get(ODataObj odataObj) {
		PageModelDto<SignDto> pageModelDto = new PageModelDto<SignDto>();
		List<Sign> signs = signRepo.findByOdata(odataObj);
		List<SignDto> signDtos = new ArrayList<SignDto>();
		
		if(signs != null && signs.size() > 0){
			signs.forEach(x->{
				SignDto signDto = new SignDto();
				BeanCopierUtils.copyProperties(x, signDto);
				//cannot copy 
				signDto.setCreatedDate(x.getCreatedDate());
				signDto.setModifiedDate(x.getModifiedDate());
				
				signDtos.add(signDto);
			});						
		}		
		pageModelDto.setCount(signDtos.size());
		pageModelDto.setValue(signDtos);		
		return pageModelDto;
	}
	
	@Override
	@Transactional
	public void updateSign(SignDto signDto) throws Exception {		
		Sign sign = signRepo.findById(signDto.getSignid());
		BeanCopierUtils.copyPropertiesIgnoreNull(signDto, sign);
		Date now = new Date();
	    sign.setCreatedBy(currentUser.getLoginName());
	    sign.setCreatedDate(now);
	    sign.setModifiedBy(currentUser.getLoginName());
	    sign.setModifiedDate(now);
	    
		sign.setModifiedBy(currentUser.getLoginName());
	    sign.setModifiedDate(new Date());
		signRepo.save(sign);		
	}
	
	@Override
	@Transactional
	public void startFlow(String signid) throws Exception{		
		if(!Validate.isString(signid)){
			throw new Exception( "操作异常，错误信息已记录，请联系管理员尽快处理！");
		}				
		try{
			Sign sign = signRepo.findById(signid);
			sign.setFolwState(EnumState.PROCESS.getValue());
			signRepo.save(sign);	
			
			//创建流程 并 跳过第一第二环节
			ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(Constant.EnumFlow.SIGN.getValue(),signid);
			//设置第三环节参数，为综合部部长
			Map<String,Object> flowParamMap = ActivitiUtil.flowArguments(null,currentUser.getLoginName(),currentUser.getLoginName(),false);
			Task task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).active().singleResult();		
			taskService.complete(task.getId(),flowParamMap);
			
			//设置第三环节参数，为综合部部长
			ActivitiUtil.flowArguments(flowParamMap,null,Constant.EnumFlowNodeGroupName.COMM_DEPT_DIRECTOR.getValue(),true);		
			task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).active().singleResult();
			taskService.complete(task.getId(),flowParamMap);
			
			log.info("项目签收流程创建成功,流程实例ID为"+processInstance.getId()+"，并成功跳过前2个环节！");
		}catch(Exception e){
			log.info("项目签收流程创建失败：" + e.getMessage());
			throw new Exception( "保存失败，错误信息已记录，请联系管理员尽快处理！");
		}				
	}

	

	@Override
	public Map<String, Object> initFillPageData(String signId) {
		Map<String,Object> map = new HashMap<String,Object>();
		
		//1收文对象
		Sign sign = signRepo.findById(signId);
		SignDto signDto = new SignDto();
		SignToSignDto(sign,signDto);			
		if(sign.getWorkProgram() != null){
			WorkProgram wp = sign.getWorkProgram();
			WorkProgramDto wpDto = new WorkProgramDto();
			BeanCopierUtils.copyProperties(wp, wpDto);
			signDto.setWorkProgramDto(wpDto);
		}		
		map.put("sign", signDto);
		
		List<OrgDto> orgDtoList = new ArrayList<OrgDto>();
		List<Org> orgList = orgRepo.findAll();
		if(orgList != null){
			orgList.forEach( o ->{
				OrgDto orgDto = new OrgDto();
				BeanCopierUtils.copyProperties(o, orgDto);
				orgDtoList.add(orgDto);
			});
			map.put("orgList", orgDtoList);
		}
				
		if(Validate.isString(sign.getMaindepetid())){
			List<UserDto> userList = userService.findUserByDeptId(sign.getMaindepetid());
			map.put("mainUserList", userList);
		}
		if(Validate.isString(sign.getAssistdeptid())){
			List<UserDto> userList = userService.findUserByDeptId(sign.getAssistdeptid());
			map.put("assistUserList", userList);
		}
		
		return map;
	}

	@Override
	public PageModelDto<SignDto> getFlow(ODataObj odataObj) {
		//获取当前用户信息
		UserDto curUser = userService.findUserByName(currentUser.getLoginName());
		TaskQuery taskQuery = taskService.createTaskQuery().processDefinitionKey(Constant.EnumFlow.SIGN.getValue());
		taskQuery.or().taskCandidateUser(curUser.getLoginName()).taskAssignee(curUser.getLoginName());
		
		List<RoleDto> roles = curUser.getRoles();
		if(roles != null){
			List<String> roleList = new ArrayList<String>(roles.size());
			roles.forEach(role ->{
				roleList.add(role.getRoleName());
			});
			if(roleList != null && roleList.size() > 0){
				taskQuery.taskCandidateGroupIn(roleList);
			}
		}	
						
		long total = taskQuery.count();		
		List<Task> tasks = taskQuery.orderByTaskCreateTime().desc().listPage(odataObj.getSkip(), odataObj.getTop());
		
		PageModelDto<SignDto> pageModelDto = new PageModelDto<SignDto>();
		List<SignDto> signDtos = new ArrayList<SignDto>();
		if(tasks != null){
			tasks.forEach(t->{
				// 通过任务对象获取流程实例
				ProcessInstance pi = runtimeService.createProcessInstanceQuery().processInstanceId(t.getProcessInstanceId()).singleResult();
				// 通过流程实例获取“业务键”
				String signId = pi.getBusinessKey();
				
				SignDto signDto = new SignDto();
				Sign sign = signRepo.findById(signId);	
				if(sign != null && Validate.isString(sign.getSignid())){
					SignToSignDto(sign,signDto);
					// 添加流程参数
					signDto.setTaskId(t.getId());
					signDto.setProcessInstanceId(t.getProcessInstanceId());
					
					signDtos.add(signDto);
				}			
			});		
		}				
		pageModelDto.setCount(Integer.valueOf(String.valueOf(total)));
		pageModelDto.setValue(signDtos);		
		return pageModelDto;
	}

	private void SignToSignDto(Sign sign,SignDto signDto){
		BeanCopierUtils.copyProperties(sign, signDto);
		signDto.setCreatedDate(sign.getCreatedDate());
		signDto.setModifiedDate(sign.getModifiedDate());
	}
	
	private void SignDtoToSign(SignDto signDto,Sign sign){
		BeanCopierUtils.copyProperties(signDto, sign);
        Date now = new Date();
        sign.setCreatedBy(currentUser.getLoginName());
        sign.setCreatedDate(now);
        sign.setModifiedBy(currentUser.getLoginName());
        sign.setModifiedDate(now);
	}

	@Override
	public void claimSignFlow(String taskId) {
		 taskService.claim(taskId, currentUser.getLoginName());
	}

	/************************************** 以下是流程处理业务  ******************************************/
	@Override
	@Transactional
	public ResultMsg dealSignFlow(ProcessInstance processInstance, FlowDto flowDto) throws Exception{
		ResultMsg resultMsg = new ResultMsg();
		
		if(!Validate.isString(flowDto.getNextGroup()) && !Validate.isString(flowDto.getNextDealUser())){
			log.info("项目签收流程处理失败：获取不到下一环节处理组和处理人信息！");
			throw new Exception( "保存失败，错误信息已记录，请联系管理员尽快处理！");			
		}
		
		Task task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).active().singleResult();
		if(task == null){
			log.info("项目签收流程处理失败：获取不到activiti任务！");
			throw new Exception( "保存失败，错误信息已记录，请联系管理员尽快处理！");
		}
			
		String signid = processInstance.getBusinessKey();
		Sign sign = null;
		boolean saveSignFlag = false;
		switch(processInstance.getActivityId()){	
			//综合部部长审批
			case "ministerApproval":	
				sign = signRepo.findById(signid);
				sign.setComprehensivehandlesug(flowDto.getDealOption());	
				saveSignFlag = true;
				break;
			//中心领导审批	
			case "leaderApproval":
				sign = signRepo.findById(signid);
				sign.setLeaderhandlesug(flowDto.getDealOption());	
				saveSignFlag = true;
				break;
			//选负责人	
			case "selectPrincipal":
				break;
			//项目负责人审批项目	
			case "approval":
				break;	
			//部长审批方案	
			case "approvalPlan":
				sign = signRepo.findById(signid);
				WorkProgram workProgram = sign.getWorkProgram();
				workProgram.setMinisterSuggesttion(flowDto.getDealOption());
				workProgramRepo.save(workProgram);
				break;	
			//中心领导审批	
			case "leaderApprovalPlan":
				sign = signRepo.findById(signid);
				WorkProgram wk = sign.getWorkProgram();
				wk.setLeaderSuggesttion(flowDto.getDealOption());
				workProgramRepo.save(wk);
				break;
			//发文申请	
			case "dispatch":	
				break;
			//部长审核	
			case "ministerDispatches":
				/*sign = signRepo.findById(signid);
				DispatchDoc dispatchDoc = sign.getDispatchDoc();
				dispatchDoc.setMinisterSuggesttion(flowDto.getDealOption());
				dispatchDoc.setMinisterDate(new Date());
				dispatchDocRepo.save(dispatchDoc);*/
				break;
			//分管副主任审核
			case "secDirectorDispatches":
				/*sign = signRepo.findById(signid);
				DispatchDoc dpd = sign.getDispatchDoc();
				dpd.setViceDirectorSuggesttion(flowDto.getDealOption());
				dpd.setViceDirectorDate(new Date());
				dispatchDocRepo.save(dpd);*/
				break;
			//主任审核
			case "directorDispatches":
				/*sign = signRepo.findById(signid);
				DispatchDoc dpdoc = sign.getDispatchDoc();
				dpdoc.setDirectorSuggesttion(flowDto.getDealOption());
				dpdoc.setDirectorDate(new Date());
				dispatchDocRepo.save(dpdoc);*/
				break;			
			case "endevent1":				
				break;
			default:
				;
		}	
		if(sign != null && saveSignFlag){
			signRepo.save(sign);
		}
		
		taskService.addComment(task.getId(),processInstance.getId(),flowDto.getDealOption());	//添加处理信息
		Map<String,Object> nextProcessVariables = ActivitiUtil.flowArguments(null,flowDto.getNextDealUser(),flowDto.getNextGroup(),false);
		taskService.complete(task.getId(),nextProcessVariables);
						
		resultMsg.setReCode(MsgCode.OK.getValue());
		resultMsg.setReMsg("操作成功！");		
		return resultMsg;
	}

	@Override
	@Transactional
	public List<OrgDto> selectSign(ODataObj odataObj) {		
		List<Org> org = orgRepo.findByOdata(odataObj);
		List<OrgDto> orgDto = new ArrayList<OrgDto>();
		
		if(org != null && org.size() > 0){
			org.forEach(x->{
				OrgDto orgDtos = new OrgDto();
				BeanCopierUtils.copyProperties(x, orgDtos);
				
				orgDtos.setCreatedDate(x.getCreatedDate());
				orgDtos.setModifiedDate(x.getModifiedDate());
				
				orgDto.add(orgDtos);
			});						
		}		
		return orgDto;
	}

	@Override
	@Transactional
	public void deleteSign(String signid) {		
		Sign sign =	signRepo.findById(signid);
		sign.setSignState(EnumState.DELETE.getValue());
		signRepo.save(sign);
		log.info(String.format("删除收文, 逻辑删除成功！", sign.getSignid()));		
	}

	@Override
	@Transactional
	public void deleteSigns(String[] signids) {		
		for(String signid : signids){			
			this.deleteSign(signid);	
		}
		log.info("批量删除收文");
	}

	@Override
	public void stopFlow(String signid) {
		Sign sign = signRepo.findById(signid);
		sign.setFolwState(EnumState.STOP.getValue());
		signRepo.save(sign);
	}

	@Override
	public void restartFlow(String signid) {
		Sign sign = signRepo.findById(signid);
		sign.setFolwState(EnumState.PROCESS.getValue());
		signRepo.save(sign);		
	}
	
	@Override
	public SignDto findById(String signid) {
		Sign sign = signRepo.findById(signid);
		SignDto signDto = new SignDto();
		SignToSignDto(sign,signDto);	
		
		return signDto;
	}
}
