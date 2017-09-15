package cs.service.archives;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.transaction.Transactional;

import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cs.common.Constant;
import cs.common.Constant.EnumState;
import cs.common.Constant.MsgCode;
import cs.common.HqlBuilder;
import cs.common.ResultMsg;
import cs.common.utils.BeanCopierUtils;
import cs.common.utils.SessionUtil;
import cs.common.utils.Validate;
import cs.domain.Archives.ArchivesLibrary;
import cs.domain.Archives.ArchivesLibrary_;
import cs.domain.monthly.MonthlyNewsletter;
import cs.domain.monthly.MonthlyNewsletter_;
import cs.domain.project.ProjectStop;
import cs.model.PageModelDto;
import cs.model.Archives.ArchivesLibraryDto;
import cs.model.monthly.MonthlyNewsletterDto;
import cs.model.project.ProjectStopDto;
import cs.repository.odata.ODataObj;
import cs.repository.repositoryImpl.archives.ArchivesLibraryRepo;

/**
 * Description: 档案借阅管理 业务操作实现类
 * author: sjy
 * Date: 2017-9-12 17:34:30
 */
@Service
public class ArchivesLibraryServiceImpl  implements ArchivesLibraryService {

	@Autowired
	private ArchivesLibraryRepo archivesLibraryRepo;
	
	@Override
	public PageModelDto<ArchivesLibraryDto> get(ODataObj odataObj) {
		PageModelDto<ArchivesLibraryDto> pageModelDto = new PageModelDto<ArchivesLibraryDto>();
		List<ArchivesLibrary> resultList = archivesLibraryRepo.findByOdata(odataObj);
		List<ArchivesLibraryDto> resultDtoList = new ArrayList<ArchivesLibraryDto>(resultList.size());
		
		if(resultList != null && resultList.size() > 0){
            resultList.forEach(x->{
				ArchivesLibraryDto modelDto = new ArchivesLibraryDto();
				BeanCopierUtils.copyProperties(x, modelDto);
				resultDtoList.add(modelDto);
			});						
		}		
		pageModelDto.setCount(odataObj.getCount());
		pageModelDto.setValue(resultDtoList);
		return pageModelDto;
	}

	/**
	 * 创建（中心）借阅保存
	 */
	@Override
	@Transactional
	public ResultMsg save(ArchivesLibraryDto record) {
		ArchivesLibrary domain = new ArchivesLibrary(); 
		BeanCopierUtils.copyProperties(record, domain); 
		Date now = new Date();
		domain.setId(UUID.randomUUID().toString());
		domain.setCreatedBy(SessionUtil.getDisplayName());
		domain.setModifiedBy(SessionUtil.getDisplayName());
		domain.setCreatedDate(now);
		domain.setModifiedDate(now);
		//部长名称
		if(SessionUtil.getUserInfo().getOrg()!=null && SessionUtil.getUserInfo().getOrg().getOrgDirectorName()!=null){
			domain.setDeptMinister(SessionUtil.getUserInfo().getOrg().getOrgDirectorName());
		}else{
			domain.setDeptMinister(SessionUtil.getDisplayName());
		}
		//分管副主任
		if(SessionUtil.getUserInfo().getOrg()!=null && SessionUtil.getUserInfo().getOrg().getOrgSLeaderName()!=null){
			domain.setDeptSLeader(SessionUtil.getUserInfo().getOrg().getOrgSLeaderName());
		}else{
			domain.setDeptSLeader(SessionUtil.getDisplayName());
		}
		//主任
		/*if(SessionUtil.getUserInfo().getOrg()!=null && SessionUtil.getUserInfo().getOrg().getOrgMLeaderName()!=null){
			domain.setDeptDirector(SessionUtil.getUserInfo().getOrg().getOrgMLeaderName());
		}else{
			domain.setDeptDirector(SessionUtil.getDisplayName());
		}*/
		//隐藏表单状态
		domain.setHideStatus(Constant.EnumState.NO.getValue());
		//默认未处理状态0
		domain.setArchivesStatus(Constant.EnumState.NO.getValue());
		archivesLibraryRepo.save(domain);
		return new ResultMsg(true, MsgCode.OK.getValue(), "操作成功！", domain);
	}
	
	/**
	 * 创建（市）借阅记录
	 */
	@Override
	@Transactional
	public ResultMsg saveCity(ArchivesLibraryDto record) {
		ArchivesLibrary domain = new ArchivesLibrary(); 
		BeanCopierUtils.copyProperties(record, domain); 
		Date now = new Date();
		domain.setId(UUID.randomUUID().toString());
		domain.setCreatedBy(SessionUtil.getDisplayName());
		domain.setModifiedBy(SessionUtil.getDisplayName());
		domain.setCreatedDate(now);
		domain.setModifiedDate(now);
		//部长名称
		if(SessionUtil.getUserInfo().getOrg()!=null && SessionUtil.getUserInfo().getOrg().getOrgDirectorName()!=null){
			domain.setDeptMinister(SessionUtil.getUserInfo().getOrg().getOrgDirectorName());
		}else{
			domain.setDeptMinister(SessionUtil.getDisplayName());
		}
		//分管副主任
		if(SessionUtil.getUserInfo().getOrg()!=null && SessionUtil.getUserInfo().getOrg().getOrgSLeaderName()!=null){
			domain.setDeptSLeader(SessionUtil.getUserInfo().getOrg().getOrgSLeaderName());
		}else{
			domain.setDeptSLeader(SessionUtil.getDisplayName());
		}
		//主任
		if(SessionUtil.getUserInfo().getOrg()!=null && SessionUtil.getUserInfo().getOrg().getOrgMLeaderName()!=null){
			domain.setDeptDirector(SessionUtil.getUserInfo().getOrg().getOrgMLeaderName());
		}else{
			domain.setDeptDirector(SessionUtil.getDisplayName());
		}
		//隐藏表单状态
		domain.setHideStatus(Constant.EnumState.NO.getValue());
		//默认未处理状态0
		domain.setArchivesStatus(Constant.EnumState.NO.getValue());
		archivesLibraryRepo.save(domain);
		return new ResultMsg(true, MsgCode.OK.getValue(), "操作成功！", domain);
	}

	@Override
	@Transactional
	public void update(ArchivesLibraryDto record) {
		ArchivesLibrary domain = archivesLibraryRepo.findById(record.getId());
		BeanCopierUtils.copyPropertiesIgnoreNull(record, domain);
		domain.setModifiedBy(SessionUtil.getDisplayName());
		domain.setModifiedDate(new Date());
		
		archivesLibraryRepo.save(domain);
	}

	@Override
	public ArchivesLibraryDto findById(String id) {		
		ArchivesLibraryDto modelDto = new ArchivesLibraryDto();
		if(Validate.isString(id)){
			ArchivesLibrary domain = archivesLibraryRepo.findById(id);
			BeanCopierUtils.copyProperties(domain, modelDto);
		}		
		return modelDto;
	}

	@Override
	@Transactional
	public void delete(String id) {

	}

	/**
	 * 审批项目查询列表
	 */
	@Override
	public PageModelDto<ArchivesLibraryDto> findByProjectList(ODataObj oDataObj) {
		PageModelDto<ArchivesLibraryDto> pageModelDto = new PageModelDto<>();
		Criteria criteria = archivesLibraryRepo.getExecutableCriteria();
		criteria =oDataObj.buildFilterToCriteria(criteria);
		Boolean falg =false;
		//部长审批
		if(SessionUtil.hashRole(Constant.EnumFlowNodeGroupName.DEPT_LEADER.getValue())){
			falg = true;
			criteria.add(Restrictions.eq(ArchivesLibrary_.deptMinister.getName(), SessionUtil.getDisplayName()));
			criteria.add(Restrictions.eq(ArchivesLibrary_.archivesStatus.getName(), Constant.EnumState.NO.getValue()));
		}
		//分管领导
		else if(SessionUtil.hashRole(Constant.EnumFlowNodeGroupName.VICE_DIRECTOR.getValue())){
			criteria.add(Restrictions.eq(ArchivesLibrary_.deptSLeader.getName(), SessionUtil.getDisplayName()));
			criteria.add(Restrictions.eq(ArchivesLibrary_.archivesStatus.getName(), Constant.EnumState.PROCESS.getValue()));
			falg = true;
		}
		//主任
		else if(SessionUtil.hashRole(Constant.EnumFlowNodeGroupName.DIRECTOR.getValue())){
			criteria.add(Restrictions.eq(ArchivesLibrary_.deptDirector.getName(), SessionUtil.getDisplayName()));
			criteria.add(Restrictions.eq(ArchivesLibrary_.archivesStatus.getName(), Constant.EnumState.STOP.getValue()));
			falg = true;
		}
		if(falg){
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
			List<ArchivesLibrary> archivesLibraryList = criteria.list();
			List<ArchivesLibraryDto> archivesLibraryDtoList = new ArrayList<>();
			for(ArchivesLibrary archivesLibrary : archivesLibraryList){
				ArchivesLibraryDto archivesLibraryDto = new ArchivesLibraryDto();
				BeanCopierUtils.copyProperties(archivesLibrary,archivesLibraryDto);
				archivesLibraryDtoList.add(archivesLibraryDto);
			}

			pageModelDto.setValue(archivesLibraryDtoList);
		}
		return pageModelDto;
	}

	/**
	 * 项目审批处理
	 * @return 
	 */
	@Override
	@Transactional
	public void updateArchivesLibrary(ArchivesLibraryDto record) {
		HqlBuilder sqlBuilder = HqlBuilder.create();
		sqlBuilder.append("update "+ArchivesLibrary.class.getSimpleName()+" set ");
		//部长审批
		if(SessionUtil.hashRole(Constant.EnumFlowNodeGroupName.DEPT_LEADER.getValue())
		|| SessionUtil.hashRole(Constant.EnumFlowNodeGroupName.COMM_DEPT_DIRECTOR.getValue())){
			sqlBuilder.append(ArchivesLibrary_.deptMinisterIdeaContent.getName()+"=:deptMinisterIdeaContent , "
			+ArchivesLibrary_.archivesStatus.getName()+"=:archivesStatus , "+ArchivesLibrary_.deptMinisterDate.getName()+"=:deptMinisterDate , "
			+ArchivesLibrary_.archivesUserName.getName()+"=:archivesUserName ");
			sqlBuilder.setParam("deptMinisterIdeaContent", record.getDeptMinisterIdeaContent());
			sqlBuilder.setParam("archivesStatus",Constant.EnumState.PROCESS.getValue());
			sqlBuilder.setParam("deptMinisterDate",new Date());
			//归档员
			sqlBuilder.setParam("archivesUserName",record.getArchivesUserName());
		}
		//分管领导审批
		else if(SessionUtil.hashRole(Constant.EnumFlowNodeGroupName.VICE_DIRECTOR.getValue())){
			sqlBuilder.append(ArchivesLibrary_.deptSLeaderIdeaContent.getName()+"=:deptSLeaderIdeaContent , "+ArchivesLibrary_.archivesStatus.getName()+ "=:archivesStatus , "
			+ArchivesLibrary_.archivesType.getName()+ " =:archivesType , "+ArchivesLibrary_.deptSleaderDate.getName()+"=:deptSleaderDate");
			sqlBuilder.setParam("deptSLeaderIdeaContent", record.getDeptSLeaderIdeaContent());
			sqlBuilder.setParam("archivesStatus",Constant.EnumState.STOP.getValue());
			sqlBuilder.setParam("deptSleaderDate",new Date());
			//中心档案查询：0
			sqlBuilder.setParam("archivesType",Constant.EnumState.NO.getValue());
		}
		//主任审批
		else if(SessionUtil.hashRole(Constant.EnumFlowNodeGroupName.DIRECTOR.getValue())){
			sqlBuilder.append(ArchivesLibrary_.deptDirectorIdeaContent.getName()+"=:deptDirectorIdeaContent , "+ArchivesLibrary_.archivesStatus.getName()+ "=:archivesStatus"
					+ArchivesLibrary_.archivesType.getName()+ "=:archivesType , "+ArchivesLibrary_.deptDirectorDate.getName()+"=:deptDirectorDate");
			sqlBuilder.setParam("deptDirectorIdeaContent", record.getDeptDirectorIdeaContent());
			sqlBuilder.setParam("archivesStatus",Constant.EnumState.YES.getValue());
			sqlBuilder.setParam("deptDirectorDate",new Date());
			//市档案查询：9
			sqlBuilder.setParam("archivesType",Constant.EnumState.YES.getValue());
		}
		sqlBuilder.append(" where "+ArchivesLibrary_.id.getName()+"=:id");
		sqlBuilder.setParam("id", record.getId());
		archivesLibraryRepo.executeHql(sqlBuilder);
	}
	

	/**
	 * 中心档案查询列表
	 */
	@Override
	public PageModelDto<ArchivesLibraryDto> findByCenterData(ODataObj odataObj) {
		PageModelDto<ArchivesLibraryDto> pageModelDto = new PageModelDto<ArchivesLibraryDto>();
		Criteria criteria = archivesLibraryRepo.getExecutableCriteria();
		criteria = odataObj.buildFilterToCriteria(criteria);
		criteria.add(Restrictions.eq(ArchivesLibrary_.archivesType.getName(), EnumState.NO.getValue()));
		Integer totalResult = ((Number) criteria.setProjection(Projections.rowCount()).uniqueResult()).intValue();
	    pageModelDto.setCount(totalResult);
	    criteria.setProjection(null);
	    if(odataObj.getSkip() > 0){
	    	criteria.setFirstResult(odataObj.getTop());
	    }
	    if(odataObj.getTop() > 0){
	    	criteria.setMaxResults(odataObj.getTop());
	    }
	    List<ArchivesLibrary> allist =criteria.list();
		List<ArchivesLibraryDto> alDtos = new ArrayList<ArchivesLibraryDto>(allist == null ? 0 : allist.size());
		
		if(allist != null && allist.size() > 0){
			allist.forEach(x->{
				ArchivesLibraryDto alDto = new ArchivesLibraryDto();
				BeanCopierUtils.copyProperties(x, alDto);
				alDtos.add(alDto);
			});						
		}		
		pageModelDto.setValue(alDtos);	
		
		return pageModelDto;
	}

	/**
	 * 市档案查询列表
	 */
	@Override
	public PageModelDto<ArchivesLibraryDto> findByCityList(ODataObj odataObj) {
		PageModelDto<ArchivesLibraryDto> pageModelDto = new PageModelDto<ArchivesLibraryDto>();
		Criteria criteria = archivesLibraryRepo.getExecutableCriteria();
		criteria = odataObj.buildFilterToCriteria(criteria);
		criteria.add(Restrictions.eq(ArchivesLibrary_.archivesType.getName(), EnumState.YES.getValue()));
		Integer totalResult = ((Number) criteria.setProjection(Projections.rowCount()).uniqueResult()).intValue();
	    pageModelDto.setCount(totalResult);
	    criteria.setProjection(null);
	    if(odataObj.getSkip() > 0){
	    	criteria.setFirstResult(odataObj.getTop());
	    }
	    if(odataObj.getTop() > 0){
	    	criteria.setMaxResults(odataObj.getTop());
	    }
	    List<ArchivesLibrary> allist =criteria.list();
		List<ArchivesLibraryDto> alDtos = new ArrayList<ArchivesLibraryDto>(allist == null ? 0 : allist.size());
		
		if(allist != null && allist.size() > 0){
			allist.forEach(x->{
				ArchivesLibraryDto alDto = new ArchivesLibraryDto();
				BeanCopierUtils.copyProperties(x, alDto);
				alDtos.add(alDto);
			});						
		}		
		pageModelDto.setValue(alDtos);	
		
		return pageModelDto;
	}

	
	
}