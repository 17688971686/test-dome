package cs.service.project;

import cs.common.Constant;
import cs.common.HqlBuilder;
import cs.common.utils.BeanCopierUtils;
import cs.common.utils.SessionUtil;
import cs.common.utils.Validate;
import cs.domain.flow.RuProcessTask;
import cs.domain.flow.RuProcessTask_;
import cs.domain.project.*;
import cs.domain.sys.Role;
import cs.model.PageModelDto;
import cs.model.project.ProjectStopDto;
import cs.quartz.unit.QuartzUnit;
import cs.repository.odata.ODataObj;
import cs.repository.repositoryImpl.flow.RuProcessTaskRepo;
import cs.repository.repositoryImpl.project.ProjectStopRepo;
import cs.repository.repositoryImpl.project.SignDispaWorkRepo;
import cs.repository.repositoryImpl.project.SignRepo;
import cs.service.flow.FlowService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.runtime.ProcessInstance;
import org.hibernate.Criteria;
import org.hibernate.criterion.*;
import org.hibernate.hql.internal.ast.HqlParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class ProjectStopServiceImp implements ProjectStopService {
	@Autowired
	private ProjectStopRepo projectStopRepo;

	@Autowired
	private SignRepo signRepo;

	@Autowired
	private SignDispaWorkRepo signDispaWorkRepo;

	@Autowired
	private FlowService flowService;

	@Autowired
	private RuntimeService runtimeService;

	@Override
	@Transactional
	public List<ProjectStop> findProjectStopBySign(String signId) {

		HqlBuilder hqlBuilder=HqlBuilder.create();

		hqlBuilder.append("select ps from "+ProjectStop.class.getSimpleName()+" ps where ps."+ProjectStop_.sign.getName()+"."+Sign_.signid.getName()+"=:signId");
		hqlBuilder.setParam("signId", signId);
		List<ProjectStop> psList=projectStopRepo.findByHql(hqlBuilder);
		return psList;
	}

	@Override
	public SignDispaWork findSignBySignId(String signId) {
		HqlBuilder sqlBuilder = HqlBuilder.create();
		sqlBuilder.append("select signid,projectname,builtcompanyname,mUserName,receivedate,mOrgName from V_SIGN_DISP_WORK where "+SignDispaWork_.signid.getName()+"=:signid");
		sqlBuilder.setParam("signid",signId);
		List<Object[]> signList=signDispaWorkRepo.getObjectArray(sqlBuilder);
		SignDispaWork  signDispaWork=new SignDispaWork();
		if(Validate.isList(signList)){
			Object[] str = signList.get(0);
			signDispaWork.setSignid((String) str[0]);
			signDispaWork.setProjectname((String)str[1]);
			signDispaWork.setBuiltcompanyname( (String)str[2]);
			signDispaWork.setmUserName( (String)str[3]);
			signDispaWork.setReceivedate((Date) str[4]);
			signDispaWork.setmOrgName((String)str[5]);
		}
		return signDispaWork;
	}

	@Override
	public int countUsedWorkday(String signId) {
		HqlBuilder sqlBuilder=HqlBuilder.create();
		sqlBuilder.append("select signdate from cs_sign where signid =:signId");
		sqlBuilder.setParam("signId",signId);
		List<Object[]> signList= signRepo.getObjectArray(sqlBuilder);
		int count=0;
		if(Validate.isList(signList)){
			Date signDate = (Date)signList.get(0)[0];
			QuartzUnit unit = new QuartzUnit();
			count=unit.countWorkday( signDate );
		}
		return count;
	}

	@Override
	@Transactional
	public void savePauseProject(ProjectStopDto projectStopDto) {
		ProjectStop projectStop = new ProjectStop();
		Sign sign =new Sign();
		sign.setSignid(projectStopDto.getSignid());
		BeanCopierUtils.copyProperties(projectStopDto,projectStop);
		projectStop.setCreatedBy(SessionUtil.getLoginName());
		projectStop.setCreatedDate(new Date());
		projectStop.setModifiedBy(SessionUtil.getLoginName());
		projectStop.setModifiedDate(new Date());
		projectStop.setStopid(UUID.randomUUID().toString());
		projectStop.setSign(sign);
		if(SessionUtil.getUserInfo().getOrg()!=null && SessionUtil.getUserInfo().getOrg().getOrgDirectorName()!= null){
			projectStop.setDirectorName(SessionUtil.getUserInfo().getOrg().getOrgDirectorName());//部长
		}else{
			projectStop.setDirectorName(SessionUtil.getLoginName());
		}
		if(SessionUtil.getUserInfo().getOrg()!=null && SessionUtil.getUserInfo().getOrg().getOrgSLeaderName()!=null){
			projectStop.setLeaderName(SessionUtil.getUserInfo().getOrg().getOrgSLeaderName());//分管副主任
		}else{
			projectStop.setLeaderName(SessionUtil.getLoginName());
		}
		projectStop.setIsactive(Constant.EnumState.NO.getValue());
		projectStop.setApproveStatus(Constant.EnumState.NO.getValue());//默认处于：未处理环节
		if(projectStopDto.getMaterial() != null){
			String[] materials= projectStopDto.getMaterial().split(",");
			for(int i=0; i<materials.length; i++){
				if("中心发补充材料函".equals(materials[i])){
					projectStop.setIsSupplementMaterial(Constant.EnumState.YES.getValue());
				}
				if("申报单位要求暂停审核函".equals(materials[i])){
					projectStop.setIsPuaseApprove(Constant.EnumState.YES.getValue());
				}
			}
		}
		projectStopRepo.save(projectStop);

	}

	@Override
	public PageModelDto<ProjectStopDto> findProjectStopByStopId(ODataObj oDataObj) {
		PageModelDto<ProjectStopDto> pageModelDto = new PageModelDto<>();
		Criteria criteria = projectStopRepo.getExecutableCriteria();
		criteria = oDataObj.buildFilterToCriteria(criteria);
		Boolean b=false;
		//部长审核
		if(SessionUtil.hashRole(Constant.EnumFlowNodeGroupName.DEPT_LEADER.getValue())){
			b=true;
			criteria.add(Restrictions.eq(ProjectStop_.directorName.getName(),SessionUtil.getLoginName()));
			criteria.add(Restrictions.eq(ProjectStop_.approveStatus.getName(),Constant.EnumState.NO.getValue()));
		}
		//分管副主任办理
		else if(SessionUtil.hashRole(Constant.EnumFlowNodeGroupName.VICE_DIRECTOR.getValue())){
			criteria.add(Restrictions.eq(ProjectStop_.leaderName.getName(),SessionUtil.getLoginName()));
			criteria.add(Restrictions.eq(ProjectStop_.approveStatus.getName(),Constant.EnumState.PROCESS.getValue()));
			b=true;
		}
		if(b){
			Integer totalResult = ((Number)criteria.setProjection(Projections.rowCount()).uniqueResult()).intValue();

			pageModelDto.setCount(totalResult);

			criteria.setProjection(null);
			if(oDataObj.getSkip()>0){
				criteria.setFirstResult(oDataObj.getSkip());
			}
			if(oDataObj.getTop() >0 ){
				criteria.setMaxResults(oDataObj.getTop());
			}

			if(Validate.isString(oDataObj.getOrderby())){
				if(oDataObj.isOrderbyDesc()){
					criteria.addOrder(Property.forName(oDataObj.getOrderby()).desc());
				}else{
					criteria.addOrder(Property.forName(oDataObj.getOrderby()).asc());
				}
			}
			List<ProjectStop> projectStopList = criteria.list();
			List<ProjectStopDto> projectStopDtoList = new ArrayList<>();
			for(ProjectStop projectStop : projectStopList){
				ProjectStopDto projectStopDto = new ProjectStopDto();
				BeanCopierUtils.copyProperties(projectStop,projectStopDto);
				projectStopDtoList.add(projectStopDto);
			}

			pageModelDto.setValue(projectStopDtoList);
		}
		return pageModelDto;
	}

	@Override
	public ProjectStopDto getProjectStopByStopId(String stopId) {
		ProjectStop projectStop =projectStopRepo.findById(stopId);
		ProjectStopDto projectStopDto = new ProjectStopDto();
		BeanCopierUtils.copyProperties(projectStop,projectStopDto);
		return projectStopDto;
	}

	@Override
	@Transactional
	public void updateProjectStop(ProjectStopDto projectStopDto) {

		HqlBuilder sqlBuilder = HqlBuilder.create();
		sqlBuilder.append("update "+ProjectStop.class.getSimpleName()+" set ");
		//部长审核
		if(SessionUtil.hashRole(Constant.EnumFlowNodeGroupName.DEPT_LEADER.getValue())
				|| SessionUtil.hashRole(Constant.EnumFlowNodeGroupName.COMM_DEPT_DIRECTOR.getValue())){
			sqlBuilder.append(ProjectStop_.directorIdeaContent.getName()+"=:directorIdeaContent , "+ProjectStop_.approveStatus.getName()+"=:approveStatus");
			sqlBuilder.setParam("directorIdeaContent",projectStopDto.getDirectorIdeaContent());
			sqlBuilder.setParam("approveStatus",Constant.EnumState.PROCESS.getValue());
		}
		//分管副主任办理
		else if(SessionUtil.hashRole(Constant.EnumFlowNodeGroupName.VICE_DIRECTOR.getValue())){
			sqlBuilder.append(ProjectStop_.leaderIdeaContent.getName()+"=:leaderIdeaContent ,"+ProjectStop_.approveStatus.getName()+"=:approveStatus ,"+ ProjectStop_.isactive.getName()+"=:isactive");
			sqlBuilder.setParam("leaderIdeaContent",projectStopDto.getLeaderIdeaContent());
			sqlBuilder.setParam("approveStatus",Constant.EnumState.YES.getValue());
			sqlBuilder.setParam("isactive",Constant.EnumState.YES.getValue());

			//修改项目状态
			HqlBuilder hql=HqlBuilder.create();
			hql.append("update "+ Sign.class.getSimpleName()+" set "+Sign_.signState.getName()+" =:signState ,"+Sign_.isProjectState.getName()+ " =:isProjectState ," + Sign_.isLightUp.getName()+"=:isLightUp");
			hql.append(" where " + Sign_.signid.getName()+" =:signId");
			hql.setParam("isLightUp" , Constant.signEnumState.PAUSE.getValue());
			hql.setParam("signState",Constant.EnumState.STOP.getValue());
			hql.setParam("isProjectState", Constant.EnumState.YES.getValue());
			hql.setParam("signId",projectStopDto.getSign().getSignid());
			signRepo.executeHql(hql);

			//暂停流程
			ProcessInstance processInstance = flowService.findProcessInstanceByBusinessKey(projectStopDto.getSign().getSignid());
			runtimeService.suspendProcessInstanceById(processInstance.getId());
		}
		sqlBuilder.append(" where "+ProjectStop_.stopid.getName()+"=:stopId");
		sqlBuilder.setParam("stopId",projectStopDto.getStopid());
		projectStopRepo.executeHql(sqlBuilder);

	}


	@Override
	public List<ProjectStop> findPauseProjectSuccess() {
		HqlBuilder sqlBuilder = HqlBuilder.create();
		sqlBuilder.append("select stopid,pausetime,pausedays from cs_projectStop where isactive=:isactive");
		sqlBuilder.setParam("isactive",Constant.EnumState.YES.getValue());
		List<Object[]> map = projectStopRepo.getObjectArray(sqlBuilder);
		List<ProjectStop> projectStopList = new ArrayList<>();
		if(Validate.isList(map)){
			for(int i=0;i<map.size();i++){
				Object[] objs= map.get(i);
				ProjectStop projectStop = new ProjectStop();
				projectStop.setStopid((String)objs[0]);
				projectStop.setPausetime((Date) objs[1]);
				projectStop.setPausedays((float)objs[2]);
				projectStopList.add(projectStop);
			}
		}
		return projectStopList;
	}

	@Override
	public void updateProjectStopStatus(List<ProjectStop> projectStopList) {
		projectStopRepo.bathUpdate(projectStopList);
	}

    /**
     * 查询我的暂停项目待办总数
     * @return
     */
	@Override
	public int findMyPauseCount() {
		Criteria criteria = projectStopRepo.getExecutableCriteria();
		//部长审核
		if(SessionUtil.hashRole(Constant.EnumFlowNodeGroupName.DEPT_LEADER.getValue())){
			criteria.add(Restrictions.eq(ProjectStop_.directorName.getName(),SessionUtil.getLoginName()));
			criteria.add(Restrictions.eq(ProjectStop_.approveStatus.getName(),Constant.EnumState.NO.getValue()));
		}
		//分管副主任办理
		else if(SessionUtil.hashRole(Constant.EnumFlowNodeGroupName.VICE_DIRECTOR.getValue())){
			criteria.add(Restrictions.eq(ProjectStop_.leaderName.getName(),SessionUtil.getLoginName()));
			criteria.add(Restrictions.eq(ProjectStop_.approveStatus.getName(),Constant.EnumState.PROCESS.getValue()));
		}else{
		    return 0;
        }
		return ((Number)criteria.setProjection(Projections.rowCount()).uniqueResult()).intValue();
	}

    @Override
    public List<ProjectStopDto> findHomeProjectStop() {

		List<ProjectStopDto> projectStopDtoList = new ArrayList<>();
		Criteria criteria = projectStopRepo.getExecutableCriteria();
		//部长审核
		if(SessionUtil.hashRole(Constant.EnumFlowNodeGroupName.DEPT_LEADER.getValue())){
			criteria.add(Restrictions.eq(ProjectStop_.directorName.getName(),SessionUtil.getLoginName()));
			criteria.add(Restrictions.eq(ProjectStop_.approveStatus.getName(),Constant.EnumState.NO.getValue()));
		}
		//分管副主任办理
		else if(SessionUtil.hashRole(Constant.EnumFlowNodeGroupName.VICE_DIRECTOR.getValue())){
			criteria.add(Restrictions.eq(ProjectStop_.leaderName.getName(),SessionUtil.getLoginName()));
			criteria.add(Restrictions.eq(ProjectStop_.approveStatus.getName(),Constant.EnumState.PROCESS.getValue()));
		}else{
			return projectStopDtoList;
		}
		criteria.setMaxResults(6);
		criteria.addOrder(Order.desc(ProjectStop_.createdDate.getName()));
		List<ProjectStop> projectStopList = criteria.list();

		for(ProjectStop p : projectStopList){
			ProjectStopDto projectStopDto = new ProjectStopDto();
			BeanCopierUtils.copyProperties( p , projectStopDto);
			projectStopDtoList.add(projectStopDto);
		}
        return projectStopDtoList;
    }

}
