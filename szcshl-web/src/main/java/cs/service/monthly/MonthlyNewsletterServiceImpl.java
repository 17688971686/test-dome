package cs.service.monthly;

import cs.common.Constant;
import cs.common.Constant.EnumState;
import cs.common.Constant.MsgCode;
import cs.common.FlowConstant;
import cs.common.ResultMsg;
import cs.common.utils.*;
import cs.domain.monthly.MonthlyNewsletter;
import cs.domain.monthly.MonthlyNewsletter_;
import cs.domain.project.AddSuppLetter;
import cs.domain.project.AddSuppLetter_;
import cs.domain.sys.Org;
import cs.domain.sys.OrgDept;
import cs.domain.sys.SysFile;
import cs.domain.sys.User;
import cs.model.PageModelDto;
import cs.model.expert.ProReviewConditionDto;
import cs.model.flow.FlowDto;
import cs.model.monthly.MonthlyNewsletterDto;
import cs.repository.odata.ODataObj;
import cs.repository.repositoryImpl.monthly.MonthlyNewsletterRepo;
import cs.repository.repositoryImpl.project.AddSuppLetterRepo;
import cs.repository.repositoryImpl.sys.OrgDeptRepo;
import cs.repository.repositoryImpl.sys.SysFileRepo;
import cs.repository.repositoryImpl.sys.UserRepo;
import cs.service.expert.ExpertSelectedService;
import cs.service.rtx.RTXSendMsgPool;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

/**
 * Description: 月报简报 业务操作实现类
 * author: sjy
 * Date: 2017-9-8 11:23:41
 */
@Service
public class MonthlyNewsletterServiceImpl  implements MonthlyNewsletterService {

	@Autowired
	private MonthlyNewsletterRepo monthlyNewsletterRepo;

	@Autowired
	private ExpertSelectedService expertSelectedService;

	@Autowired
	private SysFileRepo sysFileRepo;

	@Autowired
	private AddSuppLetterRepo addSuppLetterRepo;

	@Autowired
	private RuntimeService runtimeService;

	@Autowired
	private ProcessEngine processEngine;

	@Autowired
	private TaskService taskService;

	@Autowired
	private UserRepo userRepo;
	@Autowired
	private OrgDeptRepo orgDeptRepo;

	@Override
	public PageModelDto<MonthlyNewsletterDto> get(ODataObj odataObj) {
		PageModelDto<MonthlyNewsletterDto> pageModelDto = new PageModelDto<MonthlyNewsletterDto>();
		List<MonthlyNewsletter> resultList = monthlyNewsletterRepo.findByOdata(odataObj);
		List<MonthlyNewsletterDto> resultDtoList = new ArrayList<MonthlyNewsletterDto>(resultList.size());
		
		if(resultList != null && resultList.size() > 0){
            resultList.forEach(x->{
				MonthlyNewsletterDto modelDto = new MonthlyNewsletterDto();
				BeanCopierUtils.copyProperties(x, modelDto);
				resultDtoList.add(modelDto);
			});						
		}		
		pageModelDto.setCount(odataObj.getCount());
		pageModelDto.setValue(resultDtoList);
		return pageModelDto;
	}

	/**
	 * 保存月报简报历史数据
	 * @return 
	 */
	@Override
	@Transactional
	public ResultMsg save(MonthlyNewsletterDto record) {
		MonthlyNewsletter domain = new MonthlyNewsletter(); 
		BeanCopierUtils.copyProperties(record, domain); 
		Date now = new Date();
		domain.setId(UUID.randomUUID().toString());
		domain.setCreatedBy(SessionUtil.getDisplayName());
		domain.setModifiedBy(SessionUtil.getDisplayName());
		domain.setAddTime(now);
		domain.setAuthorizedUser(SessionUtil.getDisplayName());
		domain.setAuthorizedTime(now);
		domain.setMonthlyType(EnumState.NORMAL.getValue());
		domain.setCreatedDate(now);
		domain.setModifiedDate(now);
		monthlyNewsletterRepo.save(domain);
		return new ResultMsg(true, MsgCode.OK.getValue(), "操作成功！", domain);
	}

	@Override
	@Transactional
	public void update(MonthlyNewsletterDto record) {
		MonthlyNewsletter domain = monthlyNewsletterRepo.findById(record.getId());
		BeanCopierUtils.copyPropertiesIgnoreNull(record, domain);
		domain.setModifiedBy(SessionUtil.getDisplayName());
		domain.setModifiedDate(new Date());
		
		monthlyNewsletterRepo.save(domain);
	}

	@Override
	public MonthlyNewsletterDto findById(String id) {		
		MonthlyNewsletterDto modelDto = new MonthlyNewsletterDto();
		if(Validate.isString(id)){
			MonthlyNewsletter domain = monthlyNewsletterRepo.findById(id);
			BeanCopierUtils.copyProperties(domain, modelDto);
		}		
		return modelDto;
	}

	/**
	 * 删除月报简报历史记录
	 */
	@Override
	@Transactional
	public void delete(String id) {
		MonthlyNewsletterDto monthlyDto = new MonthlyNewsletterDto();
		MonthlyNewsletter domain = monthlyNewsletterRepo.findById(id);
		domain.setMonthlyType(Constant.EnumState.DELETE.getValue());
		BeanCopierUtils.copyProperties(domain, monthlyDto);
		monthlyNewsletterRepo.save(domain);
	}

	@Override
	public void deletes(String[] ids) {
		for(String id :ids){
			this.delete(id);
		}
	}

	/**
	 * 删除月报简报历史列表
	 */
	@Override
	public PageModelDto<MonthlyNewsletterDto> deleteHistoryList(ODataObj odataObj) {
		PageModelDto<MonthlyNewsletterDto> pageModelDto = new PageModelDto<MonthlyNewsletterDto>();
		Criteria criteria = monthlyNewsletterRepo.getExecutableCriteria();
		criteria = odataObj.buildFilterToCriteria(criteria);
		criteria.add(Restrictions.eq(MonthlyNewsletter_.monthlyType.getName(), EnumState.DELETE.getValue()));
		Integer totalResult = ((Number) criteria.setProjection(Projections.rowCount()).uniqueResult()).intValue();
	    pageModelDto.setCount(totalResult);
	    criteria.setProjection(null);
	    if(odataObj.getSkip() > 0){
	    	criteria.setFirstResult(odataObj.getTop());
	    }
	    if(odataObj.getTop() > 0){
	    	criteria.setMaxResults(odataObj.getTop());
	    }
	    List<MonthlyNewsletter> monthlist =criteria.list();
		List<MonthlyNewsletterDto> monthDtos = new ArrayList<MonthlyNewsletterDto>(monthlist == null ? 0 : monthlist.size());
		
		if(monthlist != null && monthlist.size() > 0){
			monthlist.forEach(x->{
				MonthlyNewsletterDto monthDto = new MonthlyNewsletterDto();
				BeanCopierUtils.copyProperties(x, monthDto);
				monthDtos.add(monthDto);
			});						
		}		
		pageModelDto.setValue(monthDtos);	
		
		return pageModelDto;
	}

	/**
	 * 月报简报历史数据列表
	 */
	@Override
	public PageModelDto<MonthlyNewsletterDto> mothlyHistoryList(ODataObj odataObj) {
		PageModelDto<MonthlyNewsletterDto> pageModelDto = new PageModelDto<MonthlyNewsletterDto>();
		Criteria criteria = monthlyNewsletterRepo.getExecutableCriteria();
		criteria = odataObj.buildFilterToCriteria(criteria);
		criteria.add(Restrictions.eq(MonthlyNewsletter_.monthlyType.getName(), EnumState.NORMAL.getValue()));
		Integer totalResult = ((Number) criteria.setProjection(Projections.rowCount()).uniqueResult()).intValue();
	    pageModelDto.setCount(totalResult);
	    criteria.setProjection(null);
	    if(odataObj.getSkip() > 0){
	    	criteria.setFirstResult(odataObj.getTop());
	    }
	    if(odataObj.getTop() > 0){
	    	criteria.setMaxResults(odataObj.getTop());
	    }
	    List<MonthlyNewsletter> monthlist =criteria.list();
		List<MonthlyNewsletterDto> monthDtos = new ArrayList<MonthlyNewsletterDto>(monthlist == null ? 0 : monthlist.size());
		
		if(monthlist != null && monthlist.size() > 0){
			monthlist.forEach(x->{
				MonthlyNewsletterDto monthDto = new MonthlyNewsletterDto();
				BeanCopierUtils.copyProperties(x, monthDto);
				monthDtos.add(monthDto);
			});						
		}		
		pageModelDto.setValue(monthDtos);	
		
		return pageModelDto;
	}

	/**
	 * 保存月报简报
	 * @return 
	 */
	@Override
	public ResultMsg saveTheMonthly(MonthlyNewsletterDto record) {
		   MonthlyNewsletter domain = new MonthlyNewsletter();
		   if(Validate.isString(record.getId())){
			   MonthlyNewsletter monthly =  monthlyNewsletterRepo.findById(record.getId());
			   BeanCopierUtils.copyPropertiesIgnoreNull(record,monthly);
		   }else{
			   BeanCopierUtils.copyProperties(record, domain); 
				Date now = new Date();
				domain.setId(UUID.randomUUID().toString());
				domain.setCreatedBy(SessionUtil.getDisplayName());
				domain.setModifiedBy(SessionUtil.getDisplayName());
				domain.setAddTime(now);
				domain.setAuthorizedUser(SessionUtil.getDisplayName());
				domain.setAuthorizedTime(now);
				domain.setMonthlyNewsletterName(record.getReportMultiyear()+"月报简报数据");
				domain.setMonthlyType(EnumState.PROCESS.getValue());
				domain.setCreatedDate(now);
				domain.setModifiedDate(now);
		   }
			
			monthlyNewsletterRepo.save(domain);
		return new ResultMsg(true, MsgCode.OK.getValue(), "操作成功！", domain);
	}

	/**
	 * 获取月报简报管理数据列表
	 */
	@Override
	public PageModelDto<MonthlyNewsletterDto> getMonthlyList(ODataObj odataObj) {
		PageModelDto<MonthlyNewsletterDto> pageModelDto = new PageModelDto<MonthlyNewsletterDto>();
		Criteria criteria = monthlyNewsletterRepo.getExecutableCriteria();
		criteria = odataObj.buildFilterToCriteria(criteria);
		criteria.add(Restrictions.eq(MonthlyNewsletter_.monthlyType.getName(), EnumState.PROCESS.getValue()));
		Integer totalResult = ((Number) criteria.setProjection(Projections.rowCount()).uniqueResult()).intValue();
	    pageModelDto.setCount(totalResult);
	    criteria.setProjection(null);
	    if(odataObj.getSkip() > 0){
	    	criteria.setFirstResult(odataObj.getTop());
	    }
	    if(odataObj.getTop() > 0){
	    	criteria.setMaxResults(odataObj.getTop());
	    }
	    List<MonthlyNewsletter> monthlist =criteria.list();
		List<MonthlyNewsletterDto> monthDtos = new ArrayList<MonthlyNewsletterDto>(monthlist == null ? 0 : monthlist.size());
		
		if(monthlist != null && monthlist.size() > 0){
			monthlist.forEach(x->{
				MonthlyNewsletterDto monthDto = new MonthlyNewsletterDto();
				BeanCopierUtils.copyProperties(x, monthDto);
				monthDtos.add(monthDto);
			});						
		}		
		pageModelDto.setValue(monthDtos);	
		
		return pageModelDto;
	}

	/**
	 * 删除月报简报管理数据列表
	 */
	@Override
	public PageModelDto<MonthlyNewsletterDto> deleteMonthlyList(ODataObj odataObj) {
		PageModelDto<MonthlyNewsletterDto> pageModelDto = new PageModelDto<MonthlyNewsletterDto>();
		Criteria criteria = monthlyNewsletterRepo.getExecutableCriteria();
		criteria = odataObj.buildFilterToCriteria(criteria);
		criteria.add(Restrictions.eq(MonthlyNewsletter_.monthlyType.getName(), EnumState.STOP.getValue()));
		Integer totalResult = ((Number) criteria.setProjection(Projections.rowCount()).uniqueResult()).intValue();
	    pageModelDto.setCount(totalResult);
	    criteria.setProjection(null);
	    if(odataObj.getSkip() > 0){
	    	criteria.setFirstResult(odataObj.getTop());
	    }
	    if(odataObj.getTop() > 0){
	    	criteria.setMaxResults(odataObj.getTop());
	    }
	    List<MonthlyNewsletter> monthlist =criteria.list();
		List<MonthlyNewsletterDto> monthDtos = new ArrayList<MonthlyNewsletterDto>(monthlist == null ? 0 : monthlist.size());
		
		if(monthlist != null && monthlist.size() > 0){
			monthlist.forEach(x->{
				MonthlyNewsletterDto monthDto = new MonthlyNewsletterDto();
				BeanCopierUtils.copyProperties(x, monthDto);
				monthDtos.add(monthDto);
			});						
		}		
		pageModelDto.setValue(monthDtos);	
		
		return pageModelDto;
	}

	@Override
	public void deleteMonthlyDatas(String[] ids) {
		for(String id :ids){
			this.delete(id);
		}
		
	}
	/**
	 * 删除月报简报记录
	 */
	@Override
	public void deleteMonthlyData(String id) {
		MonthlyNewsletterDto monthlyDto = new MonthlyNewsletterDto();
		MonthlyNewsletter domain = monthlyNewsletterRepo.findById(id);
		domain.setMonthlyType(Constant.EnumState.STOP.getValue());
		BeanCopierUtils.copyProperties(domain, monthlyDto);
		monthlyNewsletterRepo.save(domain);
		
	}

	@Override
	@Transactional
	public void editTheMonthly(MonthlyNewsletterDto record) {
		MonthlyNewsletter domain = monthlyNewsletterRepo.findById(record.getId());
		BeanCopierUtils.copyPropertiesIgnoreNull(record,domain);
		domain.setModifiedBy(SessionUtil.getDisplayName());
	    domain.setModifiedDate(new Date());
		monthlyNewsletterRepo.save(domain);
	}

	/**
	 * 生成月报简报模板
	 * @param monthlyNewsletterDto
	 */
	@Override
	public ResultMsg createMonthTemplate(MonthlyNewsletterDto monthlyNewsletterDto) {
		//todo:测试初始化数据
/*		monthlyNewsletterDto.setReportMultiyear("2017");
		monthlyNewsletterDto.setTheMonths("09");
		monthlyNewsletterDto.setStartMoultiyear("2017");
		monthlyNewsletterDto.setStaerTheMonths("01");
		monthlyNewsletterDto.setEndTheMonths("09");*/
		ProReviewConditionDto proReviewConditionCur = new ProReviewConditionDto();//汇总当前月
		ProReviewConditionDto proReviewConditionSum = new ProReviewConditionDto();//累计至当前月
		Integer reviewCount = 0;//当月专家评审会次数
		Integer signCount = 0;//当月签收项目数
		//当月月报
		if(StringUtil.isNotEmpty(monthlyNewsletterDto.getReportMultiyear()) && StringUtil.isNotEmpty(monthlyNewsletterDto.getTheMonths())){
			ProReviewConditionDto proReviewConditionDto = new ProReviewConditionDto();
			proReviewConditionDto.setBeginTime(monthlyNewsletterDto.getReportMultiyear()+"-"+monthlyNewsletterDto.getTheMonths());
			ResultMsg resultMsg = expertSelectedService.proReviewConditionCount(proReviewConditionDto);
			Map<String, Object> resultMap = (Map<String, Object>)resultMsg.getReObj();
			List<ProReviewConditionDto> proReviewConditionDtoList =  (List<ProReviewConditionDto>)resultMap.get("protReviewConditionList");
			//当前月汇总
			proReviewConditionCur = expertSelectedService.proReviewConditionSum(proReviewConditionDto);
			//专家评审明细
			List<ProReviewConditionDto> proReviewCondDetailList =  expertSelectedService.proReviewConditionDetail(proReviewConditionDto);
			Map<String,List<ProReviewConditionDto> > proReviewCondDetailMap = new HashMap<String,List<ProReviewConditionDto>>();
			for(int i=0;i<proReviewConditionDtoList.size();i++){
				List<ProReviewConditionDto> proReviewConditionDetailList = new ArrayList<ProReviewConditionDto>();
				String key = "";
				 for(int j=0;j<proReviewCondDetailList.size();j++){
				 	if(StringUtil.isNotEmpty(proReviewConditionDtoList.get(i).getReviewStage()) && StringUtil.isNotEmpty(proReviewCondDetailList.get(j).getReviewStage())&&proReviewConditionDtoList.get(i).getReviewStage().equals(proReviewCondDetailList.get(j).getReviewStage())){
						proReviewConditionDetailList.add(proReviewCondDetailList.get(j));
						key = NumUtils.NumberToChn(i+1)+"、"+ proReviewConditionDtoList.get(i).getReviewStage();
					}
					 if(j==(proReviewCondDetailList.size()-1)){
						 proReviewCondDetailMap.put(key,proReviewConditionDetailList);
						 break;
					 }
				 }
			}
			signCount = expertSelectedService.proReviewCount(proReviewConditionDto);
			reviewCount = expertSelectedService.proReviewMeetingCount(proReviewConditionDto);//本月专家评审会次数
			//至当前月月报
			resultMsg = null;
			resultMap.clear();
			List<ProReviewConditionDto> proReviewConditionDtoAllList = new ArrayList<ProReviewConditionDto>();
			List<ProReviewConditionDto> proReviewConditionByTypeAllList = new ArrayList<ProReviewConditionDto>();
			Integer[] proCountArr = null; //按投资金额的项目数
			Integer totalNum = 0; //项目数
			if(StringUtil.isNotEmpty(monthlyNewsletterDto.getStartMoultiyear()) && StringUtil.isNotEmpty(monthlyNewsletterDto.getStaerTheMonths()) && StringUtil.isNotEmpty(monthlyNewsletterDto.getEndTheMonths())){
				proReviewConditionDto.setBeginTime(monthlyNewsletterDto.getStartMoultiyear()+"-"+monthlyNewsletterDto.getStaerTheMonths());
				proReviewConditionDto.setEndTime(monthlyNewsletterDto.getStartMoultiyear()+"-"+monthlyNewsletterDto.getEndTheMonths());
				resultMsg = expertSelectedService.proReviewConditionCount(proReviewConditionDto);
				resultMap = (Map<String, Object>)resultMsg.getReObj();
				proReviewConditionDtoAllList =  (List<ProReviewConditionDto>)resultMap.get("protReviewConditionList");
				proReviewConditionSum = expertSelectedService.proReviewConditionSum(proReviewConditionDto);
				//项目类别
				resultMsg = null;
				resultMap.clear();
				resultMsg = expertSelectedService.proReviewConditionByTypeCount(proReviewConditionDto);
				resultMap = (Map<String, Object>)resultMsg.getReObj();
				 proReviewConditionByTypeAllList =  (List<ProReviewConditionDto>)resultMap.get("protReviewConditionList");
				 totalNum = (Integer) resultMap.get("totalNum");
				 //投资金额
				String beginTime = proReviewConditionDto.getBeginTime()+"-01 00:00:00";
				String[] timeArr = proReviewConditionDto.getEndTime().split("-");
				String day = DateUtils.getMaxDayOfMonth(Integer.parseInt(timeArr[0]),(Integer.parseInt(timeArr[1])-1))+"";
				String endTime = proReviewConditionDto.getEndTime()+"-"+day+" 23:59:59";
				  proCountArr = expertSelectedService.proReviewCondByDeclare(beginTime , endTime);
				}


			SysFile sysFile = CreateTemplateUtils.createMonthTemplate(monthlyNewsletterDto,signCount,reviewCount,proReviewConditionDtoList,proReviewConditionDtoAllList,proReviewConditionByTypeAllList,totalNum,proReviewConditionCur,proReviewConditionSum,proReviewCondDetailMap,proCountArr);
			//sysFileRepo.save(sysFile);
		}
		return new ResultMsg(true, Constant.MsgCode.OK.getValue(), "操作成功");
}

	/**
	 * 月报简报发起流程
	 *
	 * @param id
	 * @return
	 */
	@Override
	public ResultMsg startFlow(String id) {
		AddSuppLetter addSuppLetter = addSuppLetterRepo.findById(AddSuppLetter_.id.getName(), id);
		if (addSuppLetter == null) {
			return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), "发起流程失败，该项目已不存在！");
		}
		if (Validate.isString(addSuppLetter.getProcessInstanceId())) {
			return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), "该项目已发起流程！");
		}
		if (SessionUtil.getUserInfo().getOrg() == null || !Validate.isString(SessionUtil.getUserInfo().getOrg().getOrgDirector())) {
			return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), "您所在部门还没设置部长，请联系管理员进行设置！");
		}

		//查询部门领导
		User user=userRepo.findOrgDirector(addSuppLetter.getCreatedBy());//查询用户的所在部门领导
		if (!Validate.isString(user.getDisplayName())) {//判断是否有部门领导
			return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), "【" + user.getOrg().getName() + "】的部长未设置，请先设置！");
		}
		//1、启动流程
		ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(FlowConstant.MONTHLY_BULLETIN_FLOW, id,
				ActivitiUtil.setAssigneeValue(FlowConstant.MonthlyNewsletterFlowParams.USER.getValue(), SessionUtil.getUserId()));

		//2、设置流程实例名称
		processEngine.getRuntimeService().setProcessInstanceName(processInstance.getId(), addSuppLetter.getTitle());

		//3、更改项目状态
		addSuppLetter.setProcessInstanceId(processInstance.getId());
		addSuppLetter.setAppoveStatus(Constant.EnumState.NO.getValue());
		addSuppLetterRepo.save(addSuppLetter);

		//4、跳过第一环节（填报）
		Task task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).active().singleResult();
		taskService.addComment(task.getId(), processInstance.getId(), "");    //添加处理信息

		User leadUser = userRepo.getCacheUserById(user.getId());
		String assigneeValue = Validate.isString(leadUser.getTakeUserId()) ? leadUser.getTakeUserId() : leadUser.getId();
		taskService.complete(task.getId(), ActivitiUtil.setAssigneeValue(FlowConstant.MonthlyNewsletterFlowParams.USER_BZ.getValue(), assigneeValue));

		//放入腾讯通消息缓冲池
		RTXSendMsgPool.getInstance().sendReceiverIdPool(task.getId(), assigneeValue);
		return new ResultMsg(true, Constant.MsgCode.OK.getValue(), "操作成功！");
	}

	/**
	 * 月报简报流程处理
	 *
	 * @param processInstance
	 * @param task
	 * @param flowDto
	 * @return
	 */
	@Override
	public ResultMsg dealSignSupperFlow(ProcessInstance processInstance, Task task, FlowDto flowDto) {
		String businessId = processInstance.getBusinessKey(),
				assigneeValue = "";                            //流程处理人
		Map<String, Object> variables = new HashMap<>();       //流程参数
		User dealUser = null;                                  //用户
		List<User> dealUserList = null;                        //用户列表
		AddSuppLetter addSuppLetter = addSuppLetterRepo.findById(businessId);
		task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).active().singleResult();
		//环节处理人设定
		switch (task.getTaskDefinitionKey()) {
			//项目负责人填报
			case FlowConstant.MONTH_YB:
				flowDto.setDealOption("");//默认意见为空
				variables = ActivitiUtil.setAssigneeValue(FlowConstant.MonthlyNewsletterFlowParams.USER_BZ.getValue(), SessionUtil.getUserInfo().getOrg().getOrgDirector());
				break;
			//部长审批
			case FlowConstant.MONTH_BZ:
				addSuppLetter.setDeptMinisterId(SessionUtil.getUserId());
				addSuppLetter.setDeptMinisterName(SessionUtil.getDisplayName());
				addSuppLetter.setDeptMinisterDate(new Date());
				addSuppLetter.setDeptMinisterIdeaContent(flowDto.getDealOption());
				addSuppLetter.setAppoveStatus(EnumState.PROCESS.getValue());
				addSuppLetterRepo.save(addSuppLetter);
				variables = ActivitiUtil.setAssigneeValue(FlowConstant.MonthlyNewsletterFlowParams.USER_FGLD.getValue(), SessionUtil.getUserInfo().getOrg().getOrgSLeader());
				break;
			//分管领导审批
			case FlowConstant.MONTH_FG:
				addSuppLetter.setDeptSLeaderId(SessionUtil.getUserId());
				addSuppLetter.setDeptSLeaderName(SessionUtil.getDisplayName());
				addSuppLetter.setDeptSleaderDate(new Date());
				addSuppLetter.setDeptSLeaderIdeaContent(flowDto.getDealOption());
				addSuppLetter.setAppoveStatus(EnumState.STOP.getValue());
				addSuppLetterRepo.save(addSuppLetter);
				//查询部门领导
				User user=userRepo.findOrgDirector(addSuppLetter.getCreatedBy());//查询用户的所在部门
				if (!Validate.isString(user.getOrg().getOrgMLeader())) {//判断是否有部门主任
					return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), "【" + user.getOrg().getName() + "】的主任未设置，请先设置！");
				}
				variables = ActivitiUtil.setAssigneeValue(FlowConstant.MonthlyNewsletterFlowParams.USER_ZR.getValue(), user.getOrg().getOrgMLeader());
				break;

			//主任审批
			case FlowConstant.MONTH_ZR:
				addSuppLetter.setDeptDirectorId(SessionUtil.getUserId());
				addSuppLetter.setDeptDirectorName(SessionUtil.getDisplayName());
				addSuppLetter.setDeptDirectorDate(new Date());
				addSuppLetter.setDeptDirectorIdeaContent(flowDto.getDealOption());
				addSuppLetter.setAppoveStatus(EnumState.YES.getValue());
				addSuppLetterRepo.save(addSuppLetter);
				break;
			default:
			    	break;
		}
		taskService.addComment(task.getId(), processInstance.getId(), (flowDto == null) ? "" : flowDto.getDealOption());    //添加处理信息
		if (flowDto.isEnd()) {
			taskService.complete(task.getId());
		} else {
			taskService.complete(task.getId(), variables);
		}
		//放入腾讯通消息缓冲池
		RTXSendMsgPool.getInstance().sendReceiverIdPool(task.getId(), assigneeValue);
		return new ResultMsg(true, Constant.MsgCode.OK.getValue(), "操作成功！");
	}

}