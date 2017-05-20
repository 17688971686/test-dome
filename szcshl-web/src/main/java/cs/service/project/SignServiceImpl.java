package cs.service.project;

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
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cs.common.Constant;
import cs.common.Constant.EnumFlowNodeGroupName;
import cs.common.Constant.EnumState;
import cs.common.Constant.MsgCode;
import cs.common.ICurrentUser;
import cs.common.ResultMsg;
import cs.common.utils.ActivitiUtil;
import cs.common.utils.BeanCopierUtils;
import cs.common.utils.Validate;
import cs.domain.external.Dept;
import cs.domain.project.DispatchDoc;
import cs.domain.project.FileRecord;
import cs.domain.project.Sign;
import cs.domain.project.Sign_;
import cs.domain.project.WorkProgram;
import cs.domain.sys.Company;
import cs.domain.sys.Org;
import cs.model.PageModelDto;
import cs.model.external.DeptDto;
import cs.model.external.OfficeUserDto;
import cs.model.flow.FlowDto;
import cs.model.project.SignDto;
import cs.model.sys.OrgDto;
import cs.model.sys.UserDto;
import cs.repository.odata.ODataObj;
import cs.repository.repositoryImpl.external.DeptRepo;
import cs.repository.repositoryImpl.project.DispatchDocRepo;
import cs.repository.repositoryImpl.project.FileRecordRepo;
import cs.repository.repositoryImpl.project.SignRepo;
import cs.repository.repositoryImpl.project.WorkProgramRepo;
import cs.repository.repositoryImpl.sys.CompanyRepo;
import cs.repository.repositoryImpl.sys.OrgRepo;
import cs.service.external.OfficeUserService;
import cs.service.sys.UserService;

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
	@Autowired
	private FileRecordRepo fileRecordRepo;
	//flow service
	@Autowired
	private TaskService taskService;
	@Autowired
	private RuntimeService runtimeService;
	@Autowired
	private DeptRepo deptRepo;
	
	@Autowired
	private OfficeUserService officeUserService;

	@Autowired
	private CompanyRepo companyRepo;
	@Override
	@Transactional
	public void createSign(SignDto signDto) { 
		Sign sign = new Sign(); 
		BeanCopierUtils.copyProperties(signDto, sign);   
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
		pageModelDto.setCount(odataObj.getCount());
		pageModelDto.setValue(signDtos);		
		return pageModelDto;
	}
	
	@Override
	@Transactional
	public void updateSign(SignDto signDto) {		
		Sign sign = signRepo.findById(signDto.getSignid());		
		BeanCopierUtils.copyPropertiesIgnoreNull(signDto, sign);
	   	    
		sign.setModifiedBy(currentUser.getLoginName());
	    sign.setModifiedDate(new Date());
		signRepo.save(sign);
		
		log.info("更新sign 成功！signid="+signDto.getSignid());
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
			
			//创建流程 并 
			ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(Constant.EnumFlow.SIGN.getValue(),signid);
			//跳过第一第二环节
			Map<String,Object> flowParamMap = ActivitiUtil.flowArguments(null,currentUser.getLoginName(),currentUser.getLoginName(),false);
			Task task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).active().singleResult();	
			taskService.addComment(task.getId(),processInstance.getId(),"系统自动处理");	//添加处理信息
			taskService.complete(task.getId(),flowParamMap);
			
			//设置第三环节参数，为综合部部长
			ActivitiUtil.flowArguments(flowParamMap,null,Constant.EnumFlowNodeGroupName.COMM_DEPT_DIRECTOR.getValue(),true);		
			task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).active().singleResult();
			taskService.addComment(task.getId(),processInstance.getId(),"系统自动处理");	//添加处理信息
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
		BeanCopierUtils.copyProperties(sign, signDto);   					
		map.put("sign", signDto);
		
		
		//获取办事处所有信息
		List<DeptDto> deptDtolist = new ArrayList<DeptDto>();
		List<Dept> deptlist =deptRepo.findAll();
		if(deptlist !=null){
			deptlist.forEach(o ->{
				DeptDto deptDto = new DeptDto();
				BeanCopierUtils.copyProperties(o, deptDto);
				deptDtolist.add(deptDto);
			});
			map.put("deptlist", deptDtolist);
		}
		//主办事处
		if(Validate.isString(sign.getMaindepetid())){
			List<OfficeUserDto> officeList =officeUserService.findOfficeUserByDeptId(sign.getMaindepetid());
			map.put("mainOfficeList", officeList);
		}
		//协办事处
		if(Validate.isString(sign.getAssistdeptid())){
			List<OfficeUserDto> officeList =officeUserService.findOfficeUserByDeptId(sign.getAssistdeptid());
			map.put("assistOfficeList", officeList);
		}
		//编制单位查询
		Criteria criteria =	companyRepo.getSession().createCriteria(Company.class);
		criteria.add(Restrictions.eq("coType", "编制单位"));
		List<Company> designcomlist = criteria.list();
		if(designcomlist!=null){
			map.put("designcomlist", designcomlist);
		}
		//建设单位查询
		Criteria c =companyRepo.getSession().createCriteria(Company.class);
		c.add(Restrictions.eq("coType", "建设单位"));
		List<Company> builtcomlist = c.list();
		if(builtcomlist!=null){
			map.put("builtcomlist", builtcomlist);
		}
		return map;
	}
	
	@Override
	public void claimSignFlow(String taskId) {
		 taskService.claim(taskId, currentUser.getLoginName());
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
		BeanCopierUtils.copyProperties(sign, signDto);			
		return signDto;
	}

	@Override
	public void endFlow(String signid) {
		Sign sign = signRepo.findById(signid);
		sign.setFolwState(EnumState.FORCE.getValue());
		signRepo.save(sign);	
	}	
	
	/************************************** S  新流程项目处理   *********************************************/
	@Override
	@Transactional
	public void startNewFlow(String signid) throws Exception{		
		if(!Validate.isString(signid)){
			log.info("发起流程失败，无法获取收文ID！");
			throw new Exception( Constant.ERROR_MSG);
		}				
		try{
			//更改流程状态
			Sign sign = signRepo.findById(signid);
			sign.setFolwState(EnumState.PROCESS.getValue());
			signRepo.save(sign);	
			
			//创建流程 (业务数据：ID+符号+名称)
			ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(Constant.EnumFlow.FINAL_SIGN.getValue(),signid+Constant.FLOW_LINK_SYMBOL+sign.getProjectname());
			
			List<UserDto> userList = userService.findUserByRoleName(EnumFlowNodeGroupName.SIGN_USER.getValue());
			if(userList == null || userList.size() == 0){				
				throw new Exception("发起流程失败，请先设置【签收员】角色用户！");
			}
			UserDto dealUser = userList.get(0);
			//跳过第一环节
			Task task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).active().singleResult();	
			taskService.addComment(task.getId(),processInstance.getId(),"系统自动处理");	//添加处理信息
			taskService.complete(task.getId(),ActivitiUtil.setAssigneeValue(dealUser.getLoginName()));
									
			log.info("项目签收流程创建成功,流程实例ID为"+processInstance.getId()+"，任务ID为"+task.getId());
		}catch(Exception e){
			log.info("项目签收流程创建失败：" + e.getMessage());
			throw new Exception( "保存失败，错误信息已记录，请联系管理员尽快处理！");
		}				
	}
	
	/**
	 * 查询待办任务
	 */
	@Override
	public PageModelDto<SignDto> getPendingSign(ODataObj odataObj) {
		TaskQuery taskQuery = taskService.createTaskQuery().processDefinitionKey(Constant.EnumFlow.FINAL_SIGN.getValue());
		taskQuery.taskCandidateOrAssigned(currentUser.getLoginUser().getLoginName());		
						 
		int total = Integer.valueOf(String.valueOf(taskQuery.count()));		
		List<Task> tasks = taskQuery.orderByTaskCreateTime().desc().listPage(odataObj.getSkip(), odataObj.getTop());
		
		PageModelDto<SignDto> pageModelDto = new PageModelDto<SignDto>();
		List<SignDto> signDtos = new ArrayList<SignDto>();				
		if(tasks != null){
			int countValue = tasks.size();
			List<String> idList = new ArrayList<String>(countValue);
			for(int i=0;i<countValue;i++){
				// 通过任务对象获取流程实例
				Task t = tasks.get(i);
				ProcessInstance pi = runtimeService.createProcessInstanceQuery().processInstanceId(t.getProcessInstanceId()).singleResult();
				idList.add(pi.getBusinessKey());		
			}
							
			List<Sign> signList = signRepo.findByIds(Sign_.signid.getName(),StringUtils.join(idList.toArray(),","),Sign_.createdDate.getName()+" desc ");
			for(int i=0;i<countValue;i++){
				Sign s = signList.get(i);						
				SignDto signDto = new SignDto();					
				BeanCopierUtils.copyProperties(s,signDto);												
				signDtos.add(signDto);								
			}																
		}	
		
		pageModelDto.setCount(total);
		pageModelDto.setValue(signDtos);		
		return pageModelDto;
	}
	
	@Override
	@Transactional
	public ResultMsg dealFlow(ProcessInstance processInstance, FlowDto flowDto) throws Exception{		
				
		Task task = null;
		if(Validate.isString(flowDto.getTaskId())){
			task = taskService.createTaskQuery().taskId(flowDto.getTaskId()).active().singleResult();  
		}else{
			task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).active().singleResult();
		}				
		if(task == null){
			log.info("项目签收流程处理失败：获取不到流程任务！");
			throw new Exception(Constant.ERROR_MSG);
		}			
		//参数定义
		
		String signid = ActivitiUtil.getProcessBusinessKey(processInstance.getBusinessKey()),businessId = "",assigneeValue = "";		
		Sign sign = null;
		WorkProgram wk = null;
		DispatchDoc dp = null;
		List<UserDto> userList = null;
		UserDto dealUser = null ;
		boolean saveSignFlag = false;			
		Map<String,Object> variables = processInstance.getProcessVariables();

		//流程处理
		switch(flowDto.getCurNode().getActivitiId()){				
			case Constant.FLOW_ZR_TB:				//填报	
				break;
				
			case Constant.FLOW_QS:					//签收	
				sign = signRepo.findById(signid);
				sign.setSigndate(new Date());
				sign.setIssign(EnumState.YES.getValue());
				saveSignFlag = true;
				
				userList = userService.findUserByRoleName(EnumFlowNodeGroupName.COMM_DEPT_DIRECTOR.getValue());
				if(userList == null || userList.size() == 0){	
					return new ResultMsg(false,MsgCode.ERROR.getValue(),"请先设置【"+EnumFlowNodeGroupName.COMM_DEPT_DIRECTOR.getValue()+"】角色用户！");
				}
				variables.put("user",userList.get(0).getLoginName());
				break;
				
			case Constant.FLOW_ZHB_SP_SW:			//综合部审批
				sign = signRepo.findById(signid);
				sign.setComprehensivehandlesug(flowDto.getDealOption());
				saveSignFlag = true;
				//获取分管领导		
				dealUser = userService.getOrgSLeader();
				if(dealUser == null){
					return new ResultMsg(false,MsgCode.ERROR.getValue(),"请先设置该部门的【"+EnumFlowNodeGroupName.VICE_DIRECTOR.getValue()+"】角色用户！");
				}
				variables.put("user",dealUser.getLoginName());
				break;
				
			case Constant.FLOW_FGLD_SP_SW:			//分管副主任
				sign = signRepo.findById(signid);
				sign.setLeaderhandlesug(flowDto.getDealOption());
				
				userList = userService.findUserByRoleName(EnumFlowNodeGroupName.DEPT_LEADER.getValue());
				if(userList == null || userList.size() == 0){	
					return new ResultMsg(false,"","请先设置【"+EnumFlowNodeGroupName.DEPT_LEADER.getValue()+"】角色用户！");
				}
				
				//判断是否分为两个部门处理				
				if(flowDto.getBusinessMap().get("hostdept") != null){
					Org hostOrg = orgRepo.findById(flowDto.getBusinessMap().get("hostdept").toString());
					dealUser = userService.filterOrgDirector(userList,hostOrg);
					if(dealUser == null || !Validate.isString(dealUser.getLoginName())){
						return new ResultMsg(false,MsgCode.ERROR.getValue(),"请先设置【"+hostOrg.getName()+"】的"+EnumFlowNodeGroupName.DEPT_LEADER.getValue()
						+"，设置的用户角色必须为【"+EnumFlowNodeGroupName.DEPT_LEADER.getValue()+"】！");
					}
					sign.setmOrgId(hostOrg.getId());	//设置主办部门
					assigneeValue = dealUser.getLoginName();
					variables.put("hostdept", Integer.valueOf(EnumState.YES.getValue()));
					variables.put("muser",assigneeValue);
				}else{
					return new ResultMsg(false,MsgCode.ERROR.getValue(),"必须要设置主办部门！");
				}
				
				if(flowDto.getBusinessMap().get("assistdept") != null){
					Org assistOrg = orgRepo.findById(flowDto.getBusinessMap().get("assistdept").toString());
					dealUser = userService.filterOrgDirector(userList,assistOrg);
					if(dealUser == null || !Validate.isString(dealUser.getLoginName())){
						return new ResultMsg(false,MsgCode.ERROR.getValue(),"请先设置【"+assistOrg.getName()+"】的"+EnumFlowNodeGroupName.DEPT_LEADER.getValue()
						+"，设置的用户角色必须为【"+EnumFlowNodeGroupName.DEPT_LEADER.getValue()+"】！");
					}
					sign.setaOrgId(assistOrg.getId());	//设置协办部门
					variables.put("assistdept", Integer.valueOf(EnumState.YES.getValue()));
					variables.put("auser",dealUser.getLoginName());
				}else{
					variables.put("assistdept", Integer.valueOf(EnumState.NO.getValue()));
				}
				
				saveSignFlag = true;
				break;	
			case Constant.FLOW_BM_FB1:				//部门分办-主流程
				sign = signRepo.findById(signid);
				
				String dealUserLoginName = "";
				if(flowDto.getBusinessMap().get("M_USER_ID") == null){
					return new ResultMsg(false,MsgCode.ERROR.getValue(),"请先设置选择主要负责人！");
				}
				dealUser = userService.findById(flowDto.getBusinessMap().get("M_USER_ID").toString());
				sign.setmFlowMainUserId(dealUser.getId());
				dealUserLoginName = dealUser.getLoginName();
				
				if(flowDto.getBusinessMap().get("A_USER_ID") != null && Validate.isString(flowDto.getBusinessMap().get("A_USER_ID").toString())){
					dealUser = userService.findById(flowDto.getBusinessMap().get("A_USER_ID").toString());
					sign.setmFlowAssistUserId(dealUser.getId());
					dealUserLoginName += ","+dealUser.getLoginName();
				}
				variables.put("users", dealUserLoginName);					

				saveSignFlag = true;
				break;
			case Constant.FLOW_BM_FB2:				//部门分办	
				sign = signRepo.findById(signid);
				
				String dealUserLoginName2 = "";
				if(flowDto.getBusinessMap().get("M_USER_ID") == null){
					return new ResultMsg(false,MsgCode.ERROR.getValue(),"请先设置选择主要负责人！");
				}
				dealUser = userService.findById(flowDto.getBusinessMap().get("M_USER_ID").toString());
				sign.setaFlowMainUserId(dealUser.getId());
				dealUserLoginName2 = dealUser.getLoginName();
				
				if(flowDto.getBusinessMap().get("A_USER_ID") != null && Validate.isString(flowDto.getBusinessMap().get("A_USER_ID").toString())){
					dealUser = userService.findById(flowDto.getBusinessMap().get("A_USER_ID").toString());
					sign.setaFlowAssistUserId(dealUser.getId());
					dealUserLoginName2 += ","+dealUser.getLoginName();
				}
				variables.put("users", dealUserLoginName2);					

				saveSignFlag = true;	
				break;	
			case Constant.FLOW_XMFZR_SP_GZFA1:		//项目负责人承办-主流程
				sign = signRepo.findById(signid);
				if(!currentUser.getLoginUser().getId().equals(sign.getmFlowMainUserId())){
					return new ResultMsg(false,MsgCode.ERROR.getValue(),"您不是第一负责人，不能进行下一步操作！");
				}
				//如果是直接发文，则直接跳转
				if(flowDto.getBusinessMap().get("ZJFW") != null){
					variables.put("zjfw", EnumState.YES.getValue());
					dealUser = userService.findById(sign.getmFlowMainUserId());					
					assigneeValue = dealUser.getLoginName();
					if(Validate.isString(sign.getmFlowAssistUserId())){
						dealUser = userService.findById(sign.getmFlowAssistUserId());	
						assigneeValue += ","+dealUser.getLoginName();
					}
					variables.put("users", assigneeValue);
				}else{
					variables.put("zjfw", EnumState.NO.getValue());
					dealUser = userService.getOrgDirector();
					if(dealUser == null){
						return new ResultMsg(false,MsgCode.ERROR.getValue(),"请先设置"+EnumFlowNodeGroupName.DEPT_LEADER.getValue()+"，然后重新登录处理即可！");
					}	
					variables.put("user", dealUser.getLoginName());
				}				
				break;	
			case Constant.FLOW_XMFZR_SP_GZFA2:		//项目负责人承办	
				sign = signRepo.findById(signid);
				if(!currentUser.getLoginUser().getId().equals(sign.getaFlowMainUserId())){
					return new ResultMsg(false,MsgCode.ERROR.getValue(),"您不是第一负责人，不能进行下一步操作！");
				}
				
				dealUser = userService.getOrgDirector();
				if(dealUser == null){
					return new ResultMsg(false,MsgCode.ERROR.getValue(),"请先设置"+EnumFlowNodeGroupName.DEPT_LEADER.getValue()+"，然后重新登录处理即可！");					
				}else{				
					variables.put("user", dealUser.getLoginName());
				}
				break;	
			case Constant.FLOW_BZ_SP_GZAN1:			//部长审批-主流程	
				businessId = flowDto.getBusinessMap().get("M_WP_ID").toString();
				wk = workProgramRepo.findById(businessId);
				wk.setMinisterSuggesttion(flowDto.getDealOption());
				wk.setMinisterDate(new Date());
				workProgramRepo.save(wk);
				
				//获取分管领导		
				dealUser = userService.getOrgSLeader();
				if(dealUser == null){
					return new ResultMsg(false,MsgCode.ERROR.getValue(),"请先设置该部门的【"+EnumFlowNodeGroupName.VICE_DIRECTOR.getValue()+"】角色用户！");
				}
				variables.put("user", dealUser.getLoginName());			
				break;	
			case Constant.FLOW_BZ_SP_GZAN2:			//部长审批	
				businessId = flowDto.getBusinessMap().get("A_WP_ID").toString();
				wk = workProgramRepo.findById(businessId);
				wk.setMinisterSuggesttion(flowDto.getDealOption());
				wk.setMinisterDate(new Date());
				workProgramRepo.save(wk);
				
				//获取分管领导		
				dealUser = userService.getOrgSLeader();
				if(dealUser == null){
					return new ResultMsg(false,MsgCode.ERROR.getValue(),"请先设置该部门的【"+EnumFlowNodeGroupName.VICE_DIRECTOR.getValue()+"】角色用户！");
				}
				variables.put("user", dealUser.getLoginName());		
				
				break;	
			case Constant.FLOW_FGLD_SP_GZFA1:		//分管副主任审批-主流程
				businessId = flowDto.getBusinessMap().get("M_WP_ID").toString();
				wk = workProgramRepo.findById(businessId);
				wk.setLeaderSuggesttion(flowDto.getDealOption());
				wk.setLeaderDate(new Date());
				workProgramRepo.save(wk);
				
				//获取主流程负责人
				sign = signRepo.findById(signid);
				dealUser = userService.findById(sign.getmFlowMainUserId());
				assigneeValue = dealUser.getLoginName();
				if(Validate.isString(sign.getmFlowAssistUserId())){
					dealUser = userService.findById(sign.getmFlowAssistUserId());
					assigneeValue += ","+dealUser.getLoginName();
				}
				variables.put("users", assigneeValue);
				break;	
			case Constant.FLOW_FGLD_SP_GZFA2:		//分管副主任审批	
				businessId = flowDto.getBusinessMap().get("A_WP_ID").toString();
				wk = workProgramRepo.findById(businessId);
				wk.setMinisterSuggesttion(flowDto.getDealOption());
				wk.setLeaderDate(new Date());
				workProgramRepo.save(wk);
				
				//获取主流程负责人
				sign = signRepo.findById(signid);
				dealUser = userService.findById(sign.getmFlowMainUserId());
				assigneeValue = dealUser.getLoginName();
				if(Validate.isString(sign.getmFlowAssistUserId())){
					dealUser = userService.findById(sign.getmFlowAssistUserId());
					assigneeValue += ","+dealUser.getLoginName();
				}
				variables.put("users", assigneeValue);
				
				break;	
			case Constant.FLOW_FW_SQ:				//发文申请	
				sign = signRepo.findById(signid);
				if(!currentUser.getLoginUser().getId().equals(sign.getmFlowMainUserId())){
					return new ResultMsg(false,MsgCode.ERROR.getValue(),"您不是第一负责人，不能进行下一步操作！");
				}
				dealUser = userService.getOrgDirector();
				if(dealUser == null){
					return new ResultMsg(false,MsgCode.ERROR.getValue(),"请先设置该部门的部门领导！");					
				}
				variables.put("user", dealUser.getLoginName());
				break;	
			case Constant.FLOW_BZ_SP_FW:			//部长审批发文
				businessId = flowDto.getBusinessMap().get("DIS_ID").toString();
				dp = dispatchDocRepo.findById(businessId);
				dp.setMinisterSuggesttion(flowDto.getDealOption());
				dp.setMinisterDate(new Date());
				dispatchDocRepo.save(dp);
				
				dealUser = userService.getOrgSLeader();
				if(dealUser == null){
					return new ResultMsg(false,MsgCode.ERROR.getValue(),"请先设置该部门的分管领导！");			
				}
				variables.put("user", dealUser.getLoginName());
				break;	
			case Constant.FLOW_FGLD_SP_FW:			//分管领导审批发文	
				businessId = flowDto.getBusinessMap().get("DIS_ID").toString();
				dp = dispatchDocRepo.findById(businessId);
				dp.setViceDirectorSuggesttion(flowDto.getDealOption());
				dp.setViceDirectorDate(new Date());
				dispatchDocRepo.save(dp);	
				
				userList = userService.findUserByRoleName(EnumFlowNodeGroupName.DIRECTOR.getValue());
				if(userList == null || userList.size() == 0){
					return new ResultMsg(false,MsgCode.ERROR.getValue(),"请先设置【"+EnumFlowNodeGroupName.DIRECTOR.getValue()+"】角色用户！");
				}
				variables.put("user", userList.get(0).getLoginName());
				
				break;	
			case Constant.FLOW_ZR_SP_FW:			//主任审批发文	
				businessId = flowDto.getBusinessMap().get("DIS_ID").toString();
				dp = dispatchDocRepo.findById(businessId);
				dp.setDirectorSuggesttion(flowDto.getDealOption());
				dp.setDirectorDate(new Date());
				dispatchDocRepo.save(dp);
				
				//第一负责人归档
				sign = signRepo.findById(signid);
				dealUser = userService.findById(sign.getmFlowMainUserId());
				variables.put("user", dealUser.getLoginName());
				break;	
			case Constant.FLOW_MFZR_GD:				//第一负责人归档	
				sign = signRepo.findById(signid);
				//有第二负责人，则跳转到第二负责人审核
				if(Validate.isString(sign.getmFlowAssistUserId())){	
					variables.put("assistuser", Integer.valueOf(EnumState.YES.getValue()));
					dealUser =  userService.findById(sign.getmFlowAssistUserId());
					variables.put("user", dealUser.getLoginName());
				}else{
					variables.put("assistuser", Integer.valueOf(EnumState.NO.getValue()));
					userList = userService.findUserByRoleName(EnumFlowNodeGroupName.FILER.getValue());
					if(userList == null || userList.size() == 0){
						return new ResultMsg(false,MsgCode.ERROR.getValue(),"请先设置【"+EnumFlowNodeGroupName.FILER.getValue()+"】角色用户！");
					}
					dealUser = userList.get(0);
					variables.put("user", dealUser.getLoginName());
				}
				
				break;	
			case Constant.FLOW_AZFR_SP_GD:			//第二负责人审批归档	
				userList = userService.findUserByRoleName(EnumFlowNodeGroupName.FILER.getValue());
				if(userList == null || userList.size() == 0){
					return new ResultMsg(false,MsgCode.ERROR.getValue(),"请先设置【"+EnumFlowNodeGroupName.FILER.getValue()+"】角色用户！");
				}
				dealUser = userList.get(0);
				variables.put("user", dealUser.getLoginName());
				break;	
			case Constant.FLOW_BMLD_QR_GD:			//确认归档	
				sign = signRepo.findById(signid);
				
				FileRecord fileRecord = sign.getFileRecord();
				fileRecord.setFileDate(new Date());
				fileRecord.setSignUserid(currentUser.getLoginUser().getId());
				fileRecordRepo.save(fileRecord);
				
				sign.setSignState(EnumState.YES.getValue());	//更改状态
				saveSignFlag = true;
				break;														
			default:
				;
		}	
		if(sign != null && saveSignFlag){
			signRepo.save(sign);
		}
		
		taskService.addComment(task.getId(),processInstance.getId(),flowDto.getDealOption());	//添加处理信息
		if(flowDto.isEnd()){
			taskService.complete(task.getId());
		}else{
			taskService.complete(task.getId(),variables);
		}	
		
		return new ResultMsg(true,MsgCode.OK.getValue(),"操作成功！");
	}
		
	/************************************** E  新流程项目处理   *********************************************/
}
