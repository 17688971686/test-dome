package cs.service.project;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cs.common.Constant;
import cs.common.Constant.EnumState;
import cs.common.HqlBuilder;
import cs.common.ICurrentUser;
import cs.common.utils.BeanCopierUtils;
import cs.common.utils.Validate;
import cs.domain.project.Sign;
import cs.domain.project.Sign_;
import cs.domain.project.WorkProgram;
import cs.domain.project.WorkProgram_;
import cs.domain.sys.User;
import cs.model.project.WorkProgramDto;
import cs.model.sys.UserDto;
import cs.repository.repositoryImpl.project.SignRepo;
import cs.repository.repositoryImpl.project.WorkProgramRepo;
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
	
	@Override
	@Transactional
	public void save(WorkProgramDto workProgramDto) throws Exception {				
		if(Validate.isString(workProgramDto.getSignId())){
			WorkProgram workProgram = new WorkProgram(); 		
			BeanCopierUtils.copyProperties(workProgramDto, workProgram);
			
			Date now = new Date();		
			workProgram.setModifiedBy(currentUser.getLoginUser().getId());
			workProgram.setModifiedDate(now);			
			
			if(!Validate.isString(workProgramDto.getId())){
				workProgram.setId(UUID.randomUUID().toString());
				workProgram.setCreatedBy(currentUser.getLoginUser().getId());
				workProgram.setCreatedDate(now);
			}	
			
			Sign sign = signRepo.findById(workProgramDto.getSignId());
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
					break;
				}				
			}			
		}
		
		if(!Validate.isString(workProgramDto.getId())){
			workProgramDto.setProjectName(sign.getProjectname());
			workProgramDto.setBuildCompany(sign.getBuiltcompanyName());
			workProgramDto.setDesignCompany(sign.getDesigncompanyName());
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

}
