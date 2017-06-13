package cs.service.project;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cs.common.Constant;
import cs.common.Constant.EnumState;
import cs.common.HqlBuilder;
import cs.common.ICurrentUser;
import cs.common.utils.BeanCopierUtils;
import cs.common.utils.DateUtils;
import cs.common.utils.Validate;
import cs.domain.meeting.RoomBooking;
import cs.domain.project.MergeDispa;
import cs.domain.project.Sign;
import cs.domain.project.Sign_;
import cs.domain.project.WorkProgram;
import cs.domain.project.WorkProgram_;
import cs.domain.sys.SysFile;
import cs.domain.sys.User;
import cs.model.meeting.RoomBookingDto;
import cs.model.project.SignDto;
import cs.model.project.WorkProgramDto;
import cs.model.sys.UserDto;
import cs.repository.repositoryImpl.project.MergeDispaRepo;
import cs.repository.repositoryImpl.project.SignRepo;
import cs.repository.repositoryImpl.project.WorkProgramRepo;
import cs.repository.repositoryImpl.sys.SysFileRepo;
import cs.service.sys.UserService;

@Service
public class WorkProgramServiceImpl implements WorkProgramService {
	private static Logger log = Logger.getLogger(WorkProgramServiceImpl.class);
	@Autowired
	private WorkProgramRepo workProgramRepo;
	@Autowired
	private ICurrentUser currentUser;
	@Autowired
	private SignRepo signRepo;
	@Autowired
	private UserService userService;
	@Autowired
	private MergeDispaRepo mergeDispaRepo;
	@Autowired
	private SysFileRepo sysFileRepo;
	
	@Override
	@Transactional
	public void save(WorkProgramDto workProgramDto) throws Exception {				
		if(Validate.isString(workProgramDto.getSignId())){
			WorkProgram workProgram = null;						
			Date now = new Date();		
							
			if(!Validate.isString(workProgramDto.getId())){
				workProgram = new WorkProgram(); 
				BeanCopierUtils.copyProperties(workProgramDto, workProgram);		
				workProgram.setId(UUID.randomUUID().toString());
				workProgram.setCreatedBy(currentUser.getLoginUser().getId());
				workProgram.setCreatedDate(now);
			}else{
				workProgram = workProgramRepo.findById(workProgramDto.getId());
				BeanCopierUtils.copyPropertiesIgnoreNull(workProgramDto, workProgram);	
				//判断是否是单个评审和次项目,删除关联项目
				if(workProgramDto.getIsSigle().equals(Constant.EnumState.PROCESS.getValue()) ||workProgramDto.getIsMainProject().equals(Constant.EnumState.NO.getValue()) ){
					this.deleteMegreWokr(workProgram.getId());
				}
			}

			workProgram.setModifiedBy(currentUser.getLoginUser().getId());
			workProgram.setModifiedDate(now);	
			
			Sign sign = signRepo.findById(workProgramDto.getSignId());
			if(!Validate.isString(workProgram.getTitleName())){
				workProgram.setTitleName(sign.getReviewstage()+Constant.WORKPROGRAM_NAME); 	//默认名称
			}
			//判断是否是主流程
			boolean isMainFlow = false;
			if((Validate.isString(workProgramDto.getIsMain()) && workProgramDto.getIsMain().equals(EnumState.YES.getValue()))
					|| isMainSignChange(sign)){		
				isMainFlow = true;				
			}			
			if(isMainFlow){
				workProgram.setIsMain(EnumState.YES.getValue());
				sign.setIsreviewCompleted(EnumState.YES.getValue());
			}else{
				workProgram.setIsMain(Constant.EnumState.NO.getValue());
				sign.setIsreviewACompleted(EnumState.YES.getValue());
			}

			//判断是否是协审流程
            if(Validate.isString(sign.getIsassistflow()) && EnumState.YES.getValue().equals(sign.getIsassistflow())){
                sign.setIsNeedWrokPrograml(EnumState.YES.getValue());
            }
			workProgram.setSign(sign);			
			workProgramRepo.save(workProgram);
						
			sign.getWorkProgramList().add(workProgram);
			signRepo.save(sign);
			//用于返回页面
			workProgramDto.setId(workProgram.getId());
		}else{
			log.info("工作方案添加操作：无法获取收文ID（SignId）信息");
			throw new Exception(Constant.ERROR_MSG);
		}
	}
	
	/**
	 * 删除项目关联
	 */
	@Transactional
	public void deleteMegreWokr(String workId){
		MergeDispa merge = mergeDispaRepo.getById(workId);
		if(merge !=null){
			mergeDispaRepo.delete(merge);
		}
		
	}

	/**
	 * 根据收文ID初始化用户待处理的工作方案
	 */
	@Override
	public WorkProgramDto initWorkBySignId(String signId,String isMain) {
		boolean isMainUser = false,isAssistUser = false;
		Sign sign = signRepo.findById(signId);
		WorkProgramDto workProgramDto = new WorkProgramDto();	
		 		              
        HqlBuilder hqlBuilder = HqlBuilder.create();
        hqlBuilder.append(" from "+WorkProgram.class.getSimpleName()+" where "+WorkProgram_.sign.getName()+"."+Sign_.signid.getName()+" = :signId ");
        hqlBuilder.setParam("signId", signId);
        
        //如果有传入的参数，则优先按传入的参数查询，没有则根据当前用户判断
        if(Validate.isString(isMain)){
        	hqlBuilder.append(" and "+WorkProgram_.isMain.getName()+" = :isMain ").setParam("isMain", isMain);
        }else{
        	isMainUser = isMainSignChange(sign);
        	if(isMainUser) {
        		isMain = EnumState.YES.getValue();
        	}else{
        		isAssistUser = isAssistSignChange(sign);
        		if(isAssistUser){
        			isMain = EnumState.NO.getValue();
        		}
        	}
        }
        
        if(Validate.isString(isMain)){
        	hqlBuilder.append(" and "+WorkProgram_.isMain.getName()+" = :isMain ").setParam("isMain", isMain);
        }
        List<WorkProgram> list = workProgramRepo.findByHql(hqlBuilder);
		              
		if(list != null && list.size() > 0){
			//如果是创建人的上级领导，则显示
			WorkProgram workProgram = null;
			for(int i=0,l= list.size();i<l;i++){
				workProgram = list.get(i);
				UserDto checkUser = userService.findById(workProgram.getCreatedBy());
				if(userService.curUserIsSuperLeader(checkUser) || isMainUser || isAssistUser){
					BeanCopierUtils.copyProperties(workProgram,workProgramDto);
					//初始化会议室预定情况
					List<RoomBooking> roomBookings = workProgram.getRoomBookings();
					if(roomBookings != null && roomBookings.size() > 0){
						List<RoomBookingDto> roomBookingDtos = new ArrayList<RoomBookingDto>(roomBookings.size());
						roomBookings.forEach(r ->{
							RoomBookingDto rbDto = new RoomBookingDto();
							BeanCopierUtils.copyProperties(r,rbDto);
                            rbDto.setBeginTimeStr(DateUtils.converToString(rbDto.getBeginTime(),"HH:mm"));
                            rbDto.setEndTimeStr(DateUtils.converToString(rbDto.getEndTime(),"HH:mm"));
							roomBookingDtos.add(rbDto);
						});
						workProgramDto.setRoomBookingDtos(roomBookingDtos);
					}
					break;
				}				
			}			
		}
		
		if(!Validate.isString(workProgramDto.getId())){
			workProgramDto.setProjectName(sign.getProjectname());
			workProgramDto.setBuildCompany(sign.getBuiltcompanyName());
			workProgramDto.setDesignCompany(sign.getDesigncompanyName());
			workProgramDto.setTitleName(sign.getReviewstage()+Constant.WORKPROGRAM_NAME); 	//默认名称
			workProgramDto.setTitleDate(new Date());
			//来文单位默认全部是：深圳市发展和改革委员会，可改...
			//联系人，就是默认签收表的那个主办处室联系人，默认读取过来但是这边可以给他修改，和主办处室联系人都是独立的两个字段
			workProgramDto.setSendFileUnit(Constant.SEND_FILE_UNIT);
			workProgramDto.setSendFileUser(sign.getMainDeptUserName());
			
			//主流程
			if(isMainUser){
				UserDto dealUser = userService.findById(sign.getmFlowMainUserId());
				workProgramDto.setMianChargeUserId(dealUser.getId());
				workProgramDto.setMianChargeUserName(dealUser.getDisplayName());
				workProgramDto.setReviewOrgId(dealUser.getOrgDto().getId());
				workProgramDto.setReviewOrgName(dealUser.getOrgDto().getName());
				workProgramDto.setIsMain(Constant.EnumState.YES.getValue());
				
				if(Validate.isString(sign.getmFlowAssistUserId())){
					dealUser = userService.findById(sign.getmFlowAssistUserId());
					workProgramDto.setSecondChargeUserId(dealUser.getId());
					workProgramDto.setSecondChargeUserName(dealUser.getDisplayName());
				}				
			//协办流程
			}else{
				UserDto dealUser = userService.findById(sign.getaFlowMainUserId());
				workProgramDto.setMianChargeUserId(dealUser.getId());
				workProgramDto.setMianChargeUserName(dealUser.getDisplayName());
				workProgramDto.setReviewOrgId(dealUser.getOrgDto().getId());
				workProgramDto.setReviewOrgName(dealUser.getOrgDto().getName());
				workProgramDto.setIsMain(Constant.EnumState.NO.getValue());
				
				if(Validate.isString(sign.getaFlowAssistUserId())){
					dealUser = userService.findById(sign.getmFlowAssistUserId());
					workProgramDto.setSecondChargeUserId(dealUser.getId());
					workProgramDto.setSecondChargeUserName(dealUser.getDisplayName());
				}												
			}			
		}
		
		return workProgramDto;
	}

	//判断当前用户是否是主流程的处理人
	protected boolean isMainSignChange(Sign sign){
		User curUser = currentUser.getLoginUser();
		return (curUser.getId()).equals(sign.getmFlowAssistUserId()) || (curUser.getId()).equals(sign.getmFlowMainUserId());
	}
	
	//判断当前用户是否是协流程处理人
	protected boolean isAssistSignChange(Sign sign){
		User curUser = currentUser.getLoginUser();
		return (curUser.getId()).equals(sign.getaFlowAssistUserId()) || (curUser.getId()).equals(sign.getaFlowMainUserId());
	}

	//待选项目列表
	@Override
	public List<SignDto> waitProjects(SignDto signDto) {
		
		HqlBuilder hqlBuilder = HqlBuilder.create(" from " + Sign.class.getSimpleName()).append(" where ");
		if (StringUtils.isNotBlank(signDto.getSignid())) {
			String[] linkSids = signDto.getSignid().split(",");
			hqlBuilder.append(Sign_.signid.getName()).append(" not in( ");
			for (int i = 0, j = 0; i < linkSids.length; i++) {
				if (StringUtils.isNotBlank(linkSids[i])) {
					if (j != 0) {
						hqlBuilder.append(",");
					}
					hqlBuilder.append(":linkSids" + i).setParam("linkSids" + i, linkSids[i]);
					j++;
				}
			}
			hqlBuilder.append(")");
			hqlBuilder.append(" and ");
			
		}
		//是否完成发文
		hqlBuilder.append(Sign_.isDispatchCompleted.getName())
			.append(" not in(:isDispatchCompleted,:isDispatchCompleted1)").setParam("isDispatchCompleted", Constant.EnumState.NO.getValue())
			.setParam("isDispatchCompleted1", "null");
		//是否发起流程
		hqlBuilder.append(" and " + Sign_.folwState.getName()).append(" not in(:folwState,:folwState1,:folwState2)")
			.setParam("folwState", Constant.EnumState.STOP.getValue()).setParam("folwState1", Constant.EnumState.DELETE.getValue()).setParam("folwState2", Constant.EnumState.NO.getValue());
		//收文状态
		hqlBuilder.append(" and " + Sign_.signState.getName()).append(" not in(:signState,:signState1,:signState2)")
			.setParam("signState", Constant.EnumState.STOP.getValue()).setParam("signState1", Constant.EnumState.DELETE.getValue()).setParam("signState2", Constant.EnumState.NO.getValue());
		
		if(StringUtils.isNotBlank(signDto.getProjectname())){
			hqlBuilder.append(" and "+Sign_.projectname.getName()).append("=:projectname").setParam("projectname", signDto.getProjectname());
		}
		if(StringUtils.isNoneBlank(signDto.getBuiltcompanyName())){
			hqlBuilder.append(" and "+Sign_.builtcompanyName.getName()).append("=:builtcompanyName").setParam("builtcompanyName", signDto.getBuiltcompanyName());
		}
		if(StringUtils.isNotBlank(signDto.getReviewstage())){
			hqlBuilder.append(" and "+Sign_.reviewstage.getName()).append("=:reviewstage").setParam("reviewstage", signDto.getReviewstage());	
		}
		if(signDto.getStartTime()!=null&&signDto.getEndTime()!=null){
			hqlBuilder.append(" and " + Sign_.signdate.getName()).append(" between ").append(":startTime") .append(" and ").append(":endTime").setParam("startTime", signDto.getStartTime()).setParam("endTime", signDto.getEndTime());
		}
		List<Sign> list = signRepo.findByHql(hqlBuilder);
		List<SignDto> signDtoList = new ArrayList<>();
		if (list != null && list.size() >0) {
			list.forEach(x ->{
				SignDto sDto = new SignDto();
				BeanCopierUtils.copyProperties(x, sDto);
				signDtoList.add(sDto);
			});
		}
		
		return signDtoList;
	}

	@Override
	public List<SignDto> selectedProject(String[] ids) {
		List<SignDto> signDtos = new ArrayList<>();
		for(String id : ids){
			if(Validate.isString(id)){
				SignDto signDto = new SignDto();
				Sign s=signRepo.findById(id);
				BeanCopierUtils.copyProperties(s, signDto);
				signDtos.add(signDto);
			}
		}
		return signDtos;
	}

	@Override
	@Transactional
	public void mergeAddWork(String signId, String linkSignId) {
		Date now = new Date();
		Sign sign = signRepo.findById(signId);
		MergeDispa  merge = new MergeDispa();
		List<WorkProgram>works=sign.getWorkProgramList();
		for(WorkProgram work :works){
			merge.setBusinessId(work.getId());
			merge.setType(work.getReviewType());
		}
		merge.setSignId(signId);
		merge.setLinkSignId(linkSignId);
		merge.setCreatedBy(currentUser.getLoginName());
		merge.setModifiedBy(currentUser.getLoginName());
		merge.setCreatedDate(now);
		merge.setModifiedDate(now);
		mergeDispaRepo.save(merge);
	}

	@Override
	public Map<String, Object> getInitSeleSignByIds(String bussnessId) {
		Map<String,Object> map = new HashMap<>();
		MergeDispa merge=mergeDispaRepo.getById(bussnessId);
		List<SignDto> signDtos = null;
		String linkSignId = "";
		if(merge!=null && Validate.isString(merge.getBusinessId())){
			linkSignId= merge.getLinkSignId();
			signDtos = new ArrayList<>();
			String [] ids =linkSignId.split(",");
			if(ids!=null){
				for(String id :ids){
					if(Validate.isString(id)){
						SignDto signDto = new SignDto();
						Sign sign = signRepo.findById(id);
						BeanCopierUtils.copyProperties(sign, signDto);
						signDto.setCreatedDate(sign.getCreatedDate());
						signDto.setModifiedDate(sign.getModifiedDate());
						signDtos.add(signDto);
					}
				}
			}
		}
		map.put("signDtoList", signDtos);
		map.put("linkSignId", linkSignId);
		return map;
	}

	@Override
	public Map<String, Object> getInitRelateData(String signId) {
		Map<String, Object> map =new HashMap<String,Object>();
		 String linkSignId ="";
		Sign sign=	signRepo.findById(signId);
		List<WorkProgram> work=	sign.getWorkProgramList();
		for(WorkProgram wp :work){
			String workId = wp.getId();
			MergeDispa merge = mergeDispaRepo.getById(workId);
			if(merge!=null&& Validate.isString(merge.getBusinessId())){
				linkSignId=merge.getLinkSignId();
			}
		}
		map.put("linkSignId", linkSignId);
		//查询系统上传文件
		Criteria file = sysFileRepo.getSession().createCriteria(SysFile.class);
		file.add(Restrictions.eq("businessId", sign.getSignid()));
		List<SysFile> sysFilelist = file.list();
		if(sysFilelist !=null){
			map.put("sysFilelist", sysFilelist);
		}
		return map;
	}

	/**
	 * 根据项目ID删除工作方案
	 * @param signId
	 */
	@Override
	public void deleteBySignId(String signId) {
        HqlBuilder sqlBuilder = HqlBuilder.create();
        sqlBuilder.append(" delete from cs_work_program where signid =:signid");
        sqlBuilder.setParam("signid",signId);
        workProgramRepo.executeSql(sqlBuilder);
	}


}
