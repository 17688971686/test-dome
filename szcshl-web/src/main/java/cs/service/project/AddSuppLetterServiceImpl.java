package cs.service.project;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cs.common.Constant;
import cs.common.Constant.EnumState;
import cs.common.HqlBuilder;
import cs.common.ResultMsg;
import cs.common.utils.BeanCopierUtils;
import cs.common.utils.DateUtils;
import cs.common.utils.SessionUtil;
import cs.common.utils.Validate;
import cs.domain.project.AddSuppLetter;
import cs.domain.project.AddSuppLetter_;
import cs.domain.project.Sign;
import cs.domain.project.Sign_;
import cs.domain.project.WorkProgram;
import cs.model.PageModelDto;
import cs.model.project.AddSuppLetterDto;
import cs.repository.odata.ODataObj;
import cs.repository.repositoryImpl.project.AddSuppLetterRepo;
import cs.repository.repositoryImpl.project.FileRecordRepo;
import cs.repository.repositoryImpl.project.SignRepo;
import cs.repository.repositoryImpl.project.WorkProgramRepo;


/**
 * Description: 项目资料补充函 业务操作实现类
 * author: ldm
 * Date: 2017-8-1 18:05:57
 */
@Service
public class AddSuppLetterServiceImpl implements AddSuppLetterService {

	@Autowired
	private AddSuppLetterRepo addSuppLetterRepo;

	@Autowired
	private SignRepo signRepo;
	@Autowired
	private WorkProgramRepo workProgramRepo;
	@Autowired
	private FileRecordRepo fileRecordRepo;
	/**
	 * 保存补充资料函
	 */
	@Override
	@Transactional
	public ResultMsg addSupp(AddSuppLetterDto addSuppLetterDto) {
		if(Validate.isString(addSuppLetterDto.getBusinessId())){
			AddSuppLetter addSuppLetter = null;
			Date now = new Date();
			if (Validate.isString(addSuppLetterDto.getId())) {
				addSuppLetter = addSuppLetterRepo.findById(addSuppLetterDto.getId());
				BeanCopierUtils.copyPropertiesIgnoreNull(addSuppLetterDto,addSuppLetter);
			} else {
               /*  addSuppLetter = new AddSuppLetter();
                 BeanCopierUtils.copyProperties(addSuppLetterDto, addSuppLetter);
                 addSuppLetter.setId(UUID.randomUUID().toString());
                 addSuppLetter.setCreatedBy(SessionUtil.getDisplayName());
                 addSuppLetter.setModifiedBy(SessionUtil.getDisplayName());
                 //部长名称
                 if(SessionUtil.getUserInfo().getOrg()!=null && SessionUtil.getUserInfo().getOrg().getOrgDirectorName()!=null){
                 	addSuppLetter.setDeptMinisterName(SessionUtil.getUserInfo().getOrg().getOrgDirectorName());
                 }else{
                 	addSuppLetter.setDeptMinisterName(SessionUtil.getDisplayName());
                 }
               //分管副主任
         		if(SessionUtil.getUserInfo().getOrg()!=null && SessionUtil.getUserInfo().getOrg().getOrgSLeaderName()!=null){
         			addSuppLetter.setDeptSLeaderName(SessionUtil.getUserInfo().getOrg().getOrgSLeaderName());
         		}else{
         			addSuppLetter.setDeptSLeaderName(SessionUtil.getDisplayName());
         		}
         		//主任
         		if(SessionUtil.getUserInfo().getOrg()!=null && SessionUtil.getUserInfo().getOrg().getOrgMLeaderName()!=null){
         			addSuppLetter.setDeptDirectorName(SessionUtil.getUserInfo().getOrg().getOrgMLeaderName());
         		}else{
         			addSuppLetter.setDeptDirectorName(SessionUtil.getDisplayName());
         		}
                 //查询列表状态
                 addSuppLetter.setAddSuppStatus(Constant.EnumState.NO.getValue());
                 addSuppLetter.setAddSuppAppoveStatus(Constant.EnumState.NO.getValue());*/

             }
             WorkProgram work =  workProgramRepo.findById(addSuppLetterDto.getWorkId());
             work.setIsHaveSuppLetter(Constant.EnumState.YES.getValue());
             work.setSuppLetterDate(addSuppLetterDto.getDisapDate());
             workProgramRepo.save(work);
             addSuppLetter.setModifiedDate(now);
             addSuppLetter.setCreatedDate(now);
             addSuppLetterRepo.save(addSuppLetter);
             return new ResultMsg(true, Constant.MsgCode.OK.getValue(), "操作成功！", addSuppLetterDto);
        }else{
        	return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), "操作失败，获取项目信息失败，请联系相关人员处理！");

        }

    }

	@Override
	public AddSuppLetterDto getbyId(String id) {
		AddSuppLetter addSuppLetter = addSuppLetterRepo.findById(id);
		AddSuppLetterDto addSuppLetterDto = new AddSuppLetterDto();
		BeanCopierUtils.copyPropertiesIgnoreNull(addSuppLetter, addSuppLetterDto);
		return addSuppLetterDto;
	}


	/**
	 * 获取最大拟稿编号
	 *
	 * @param dispaDate
	 * @return
	 */
	private int findCurMaxSeq(Date dispaDate) {
		HqlBuilder sqlBuilder = HqlBuilder.create();
		sqlBuilder.append("select max(" + AddSuppLetter_.fileSeq.getName() + ") from cs_add_suppLetter where " + AddSuppLetter_.disapDate.getName() + " between ");
		sqlBuilder.append(" to_date(:beginTime,'yyyy-mm-dd hh24:mi:ss') and to_date(:endTime,'yyyy-mm-dd hh24:mi:ss' )");
		sqlBuilder.setParam("beginTime", DateUtils.converToString(dispaDate, "yyyy") + "-01-01 00:00:00");
		sqlBuilder.setParam("endTime", DateUtils.converToString(dispaDate, "yyyy") + "-12-31 23:59:59");
		return addSuppLetterRepo.returnIntBySql(sqlBuilder);
	}


	/**
	 * 根据业务ID和业务类型初始化补充资料函信息
	 * @param businessId
	 * @param businessType
	 * @return
	 */
	@Override
	public AddSuppLetterDto initSuppLetter(String businessId, String businessType,String workId) {
		AddSuppLetterDto suppletterDto = new AddSuppLetterDto();
       /* AddSuppLetter suppletter =  addSuppLetterRepo.findById("businessId",businessId);
        if(suppletter !=null && Validate.isString(suppletter.getId())){
        	BeanCopierUtils.copyPropertiesIgnoreNull(suppletter,suppletterDto);
        }else{*/
		//新增
		if(Constant.BusinessType.SIGN.getValue().equals(businessType)){
			Sign sign = signRepo.findById(Sign_.signid.getName(),businessId);
			suppletterDto.setUserName(SessionUtil.getDisplayName());
			suppletterDto.setOrgName(SessionUtil.getUserInfo().getOrg() == null ? "" : SessionUtil.getUserInfo().getOrg().getName());
			suppletterDto.setBusinessId(businessId);
			suppletterDto.setBusinessType(businessType);
			suppletterDto.setTitle("《" + sign.getProjectname() + sign.getReviewstage() + "》");
			suppletterDto.setSecretLevel(sign.getSecrectlevel());
			suppletterDto.setMergencyLevel(sign.getUrgencydegree());
		}
		 WorkProgram work =  workProgramRepo.findById(workId);
		 suppletterDto.setWorkId(work.getId());
		//  }


		return suppletterDto;
	}

	/**
	 * 生成文件字号
	 */
	@Override
	@Transactional
	public ResultMsg fileNum(String id) {
		AddSuppLetter addSuppLetter = addSuppLetterRepo.findById(AddSuppLetter_.id.getName(),id);
		if(Validate.isString(addSuppLetter.getFilenum())){
			return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(),"该补充资料函已经生成过发文字号，不能重复生成！");
		}
		//获取拟稿最大编号
		int curYearMaxSeq = findCurMaxSeq(addSuppLetter.getDisapDate());
		String filenum = Constant.DISPATCH_PREFIX + "[" + DateUtils.converToString(addSuppLetter.getDisapDate(), "yyyy") + "]" + (curYearMaxSeq + 1);
		addSuppLetter.setFilenum(filenum);
		addSuppLetter.setFileSeq((curYearMaxSeq + 1));
		addSuppLetterRepo.save(addSuppLetter);

		//如果是收文，则要更新对应的资料信息(如果生成了文件字号，工作方案的是否补充资料函则显示为是，并且显示最新的日期。如果没有，则显示为否)
		if(Constant.BusinessType.SIGN.getValue().equals(addSuppLetter.getBusinessType())){
			Date now = new Date();
			Sign sign = signRepo.findById(Sign_.signid.getName(),addSuppLetter.getBusinessId());
			if(!Validate.isString(sign.getIsHaveSuppLetter()) || Constant.EnumState.NO.getValue().equals(sign.getIsHaveSuppLetter())){
				sign.setIsHaveSuppLetter(Constant.EnumState.YES.getValue());
				sign.setSuppLetterDate(now);
				signRepo.save(sign);
			}
			List<WorkProgram> wpList = workProgramRepo.findByIds(Sign_.signid.getName(),addSuppLetter.getBusinessId(),null);
			if(Validate.isList(wpList)){
				List<WorkProgram> saveList = new ArrayList<>();
				for(WorkProgram wp : wpList){
					if(!Validate.isString(wp.getIsHaveSuppLetter()) || Constant.EnumState.NO.getValue().equals(wp.getIsHaveSuppLetter())){
						wp.setIsHaveSuppLetter(Constant.EnumState.YES.getValue());
						wp.setSuppLetterDate(now);
						saveList.add(wp);
					}
				}
				workProgramRepo.bathUpdate(saveList);
			}
		}
		return new ResultMsg(true, Constant.MsgCode.OK.getValue(),"操作成功！",addSuppLetter);
	}

	/**
	 * 根据业务ID查询拟补充资料函
	 * @param businessId
	 * @return
	 */
	@Override
	public List<AddSuppLetterDto> initSuppList(String businessId) {
		HqlBuilder hql = HqlBuilder.create();
		hql.append(" from " + AddSuppLetter.class.getSimpleName() + " where " + AddSuppLetter_.businessId.getName() + " = :businessId ");
		hql.setParam("businessId", businessId);
		List<AddSuppLetter> suppletterlist = addSuppLetterRepo.findByHql(hql);
		List<AddSuppLetterDto> addSuppLetterDtos = new ArrayList<AddSuppLetterDto>();
		if (Validate.isList(suppletterlist)) {
			suppletterlist.forEach(a -> {
				AddSuppLetterDto addDto = new AddSuppLetterDto();
				BeanCopierUtils.copyProperties(a, addDto);
				addSuppLetterDtos.add(addDto);
			});
		}

		return addSuppLetterDtos;
	}

	/**
	 * 根据业务ID判断是否有补充资料函
	 * @param businessId
	 * @return
	 */
	@Override
	public boolean isHaveSuppLetter(String businessId) {
		return addSuppLetterRepo.isHaveSuppLetter(businessId);
	}

	/**
	 * 保存（中心）文件稿纸
	 */
	@Override
	@Transactional
	public ResultMsg saveMonthlyMultiyear(AddSuppLetterDto record) {
		Date now = new Date();
		AddSuppLetter addSuppLetter = new AddSuppLetter();
		if(Validate.isString(record.getId())){
			addSuppLetter = addSuppLetterRepo.findById(record.getId());
			BeanCopierUtils.copyPropertiesIgnoreNull(record, addSuppLetter);
		}else{
			BeanCopierUtils.copyProperties(record, addSuppLetter);
//			addSuppLetter.setId(UUID.randomUUID().toString());
			addSuppLetter.setCreatedBy(SessionUtil.getUserInfo().getId());
			addSuppLetter.setModifiedBy(SessionUtil.getUserInfo().getId());
			addSuppLetter.setModifiedDate(now);
			addSuppLetter.setCreatedDate(now);

			//部长名称
			if(SessionUtil.getUserInfo().getOrg()!=null && SessionUtil.getUserInfo().getOrg().getOrgDirectorName()!=null){
				addSuppLetter.setDeptMinisterName(SessionUtil.getUserInfo().getOrg().getOrgDirectorName());
			}else{
				addSuppLetter.setDeptMinisterName(SessionUtil.getDisplayName());
			}
			//分管副主任
			if(SessionUtil.getUserInfo().getOrg()!=null && SessionUtil.getUserInfo().getOrg().getOrgSLeaderName()!=null){
				addSuppLetter.setDeptSLeaderName(SessionUtil.getUserInfo().getOrg().getOrgSLeaderName());
			}else{
				addSuppLetter.setDeptSLeaderName(SessionUtil.getDisplayName());
			}
			//主任
			if(SessionUtil.getUserInfo().getOrg()!=null && SessionUtil.getUserInfo().getOrg().getOrgMLeaderName()!=null){
				addSuppLetter.setDeptDirectorName(SessionUtil.getUserInfo().getOrg().getOrgMLeaderName());
			}else{
				addSuppLetter.setDeptDirectorName(SessionUtil.getDisplayName());
			}
			//查询列表状态
			addSuppLetter.setMonthlyStatus(Constant.EnumState.NO.getValue());
			//审批状态
			addSuppLetter.setMonthlyAppoveStatus(Constant.EnumState.NO.getValue());
		}

		addSuppLetterRepo.save(addSuppLetter);
		return new ResultMsg(true, Constant.MsgCode.OK.getValue(), "操作成功！", addSuppLetter);
	}

	/**
	 * 获取年度（中心）文件查询列表数据
	 */
	@Override
	public PageModelDto<AddSuppLetterDto> monthlyMultiyearListData(ODataObj odataObj) {
		PageModelDto<AddSuppLetterDto> pageModelDto = new PageModelDto<AddSuppLetterDto>();
		Criteria criteria = addSuppLetterRepo.getExecutableCriteria();
		criteria = odataObj.buildFilterToCriteria(criteria);
		criteria.add(Restrictions.eq(AddSuppLetter_.monthlyStatus.getName(), EnumState.NO.getValue()));
		Integer totalResult = ((Number) criteria.setProjection(Projections.rowCount()).uniqueResult()).intValue();
		pageModelDto.setCount(totalResult);
		criteria.setProjection(null);
		if(odataObj.getSkip() > 0){
			criteria.setFirstResult(odataObj.getTop());
		}
		if(odataObj.getTop() > 0){
			criteria.setMaxResults(odataObj.getTop());
		}
		List<AddSuppLetter> addlist =criteria.list();
		List<AddSuppLetterDto> addDtos = new ArrayList<AddSuppLetterDto>(addlist == null ? 0 : addlist.size());

		if(addlist != null && addlist.size() > 0){
			addlist.forEach(x->{
				AddSuppLetterDto addDto = new AddSuppLetterDto();
				BeanCopierUtils.copyProperties(x, addDto);
				addDtos.add(addDto);
			});
		}
		pageModelDto.setValue(addDtos);

		return pageModelDto;
	}

	/**
	 * 中心文件审批列表数据
	 */
	@Override
	public PageModelDto<AddSuppLetterDto> monthlyAppoveListData(ODataObj oDataObj) {
		PageModelDto<AddSuppLetterDto> pageModelDto = new PageModelDto<>();
		Criteria criteria = addSuppLetterRepo.getExecutableCriteria();
		criteria =oDataObj.buildFilterToCriteria(criteria);
		Boolean falg =false;
		//部长审批
		if(SessionUtil.hashRole(Constant.EnumFlowNodeGroupName.DEPT_LEADER.getValue())){
			falg = true;
			criteria.add(Restrictions.eq(AddSuppLetter_.deptMinisterName.getName(), SessionUtil.getDisplayName()));
			criteria.add(Restrictions.eq(AddSuppLetter_.monthlyAppoveStatus.getName(), Constant.EnumState.NO.getValue()));
		}
		//分管领导
		else if(SessionUtil.hashRole(Constant.EnumFlowNodeGroupName.VICE_DIRECTOR.getValue())){
			criteria.add(Restrictions.eq(AddSuppLetter_.deptSLeaderName.getName(), SessionUtil.getDisplayName()));
			criteria.add(Restrictions.eq(AddSuppLetter_.monthlyAppoveStatus.getName(), Constant.EnumState.PROCESS.getValue()));
			falg = true;
		}
		//主任
		else if(SessionUtil.hashRole(Constant.EnumFlowNodeGroupName.DIRECTOR.getValue())){
			criteria.add(Restrictions.eq(AddSuppLetter_.deptDirectorName.getName(), SessionUtil.getDisplayName()));
			criteria.add(Restrictions.eq(AddSuppLetter_.monthlyAppoveStatus.getName(), Constant.EnumState.STOP.getValue()));
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
			List<AddSuppLetter> addSuppList = criteria.list();
			List<AddSuppLetterDto> addSuppDtoList = new ArrayList<>();
			for(AddSuppLetter archivesLibrary : addSuppList){
				AddSuppLetterDto addDto = new AddSuppLetterDto();
				BeanCopierUtils.copyProperties(archivesLibrary,addDto);
				addSuppDtoList.add(addDto);
			}

			pageModelDto.setValue(addSuppDtoList);
		}
		return pageModelDto;
	}

	/**
	 * 初始化（中心）文件稿纸
	 */
	@Override
	public AddSuppLetterDto initMonthlyMutilyear() {
		AddSuppLetterDto suppletterDto = new AddSuppLetterDto();
		suppletterDto.setUserName(SessionUtil.getLoginName());
		suppletterDto.setOrgName(SessionUtil.getUserInfo().getOrg() == null ? "" : SessionUtil.getUserInfo().getOrg().getName());
		return suppletterDto;
	}

	/**
	 * 删除年度（中心）月报简报记录
	 */
	@Override
	public void delete(String id) {
		this.delete(id);
	}

	@Override
	public void deletes(String[] ids) {
		for(String id :ids){
			this.delete(id);
		}
	}

	/**
	 * 获取拟补充资料函查询列表
	 */
	@Override
	public PageModelDto<AddSuppLetterDto> addsuppListData(ODataObj odataObj) {
		PageModelDto<AddSuppLetterDto> pageModelDto = new PageModelDto<AddSuppLetterDto>();
		Criteria criteria = addSuppLetterRepo.getExecutableCriteria();
		criteria = odataObj.buildFilterToCriteria(criteria);
		criteria.add(Restrictions.eq(AddSuppLetter_.addSuppStatus.getName(), EnumState.NO.getValue()));
		Integer totalResult = ((Number) criteria.setProjection(Projections.rowCount()).uniqueResult()).intValue();
		pageModelDto.setCount(totalResult);
		criteria.setProjection(null);
		if(odataObj.getSkip() > 0){
			criteria.setFirstResult(odataObj.getTop());
		}
		if(odataObj.getTop() > 0){
			criteria.setMaxResults(odataObj.getTop());
		}
		List<AddSuppLetter> allist =criteria.list();
		List<AddSuppLetterDto> alDtos = new ArrayList<AddSuppLetterDto>(allist == null ? 0 : allist.size());

		if(allist != null && allist.size() > 0){
			allist.forEach(x->{
				AddSuppLetterDto alDto = new AddSuppLetterDto();
				BeanCopierUtils.copyProperties(x, alDto);
				alDtos.add(alDto);
			});
		}
		pageModelDto.setValue(alDtos);

		return pageModelDto;
	}

	/**
	 * 获取拟补充资料函审批处理列表
	 */
	@Override
	public PageModelDto<AddSuppLetterDto> addSuppApproveList(ODataObj oDataObj) {
		PageModelDto<AddSuppLetterDto> pageModelDto = new PageModelDto<>();
		Criteria criteria = addSuppLetterRepo.getExecutableCriteria();
		criteria =oDataObj.buildFilterToCriteria(criteria);
		Boolean falg =false;
		//部长审批
		if(SessionUtil.hashRole(Constant.EnumFlowNodeGroupName.DEPT_LEADER.getValue())){
			falg = true;
			criteria.add(Restrictions.eq(AddSuppLetter_.deptMinisterName.getName(), SessionUtil.getDisplayName()));
			criteria.add(Restrictions.eq(AddSuppLetter_.addSuppAppoveStatus.getName(), Constant.EnumState.NO.getValue()));
		}
		//分管领导
		else if(SessionUtil.hashRole(Constant.EnumFlowNodeGroupName.VICE_DIRECTOR.getValue())){
			criteria.add(Restrictions.eq(AddSuppLetter_.deptSLeaderName.getName(), SessionUtil.getDisplayName()));
			criteria.add(Restrictions.eq(AddSuppLetter_.addSuppAppoveStatus.getName(), Constant.EnumState.PROCESS.getValue()));
			falg = true;
		}
		//主任
		else if(SessionUtil.hashRole(Constant.EnumFlowNodeGroupName.DIRECTOR.getValue())){
			criteria.add(Restrictions.eq(AddSuppLetter_.deptDirectorName.getName(), SessionUtil.getDisplayName()));
			criteria.add(Restrictions.eq(AddSuppLetter_.addSuppAppoveStatus.getName(), Constant.EnumState.STOP.getValue()));
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
			List<AddSuppLetter> addSuppList = criteria.list();
			List<AddSuppLetterDto> addSuppDtoList = new ArrayList<>();
			for(AddSuppLetter archivesLibrary : addSuppList){
				AddSuppLetterDto addDto = new AddSuppLetterDto();
				BeanCopierUtils.copyProperties(archivesLibrary,addDto);
				addSuppDtoList.add(addDto);
			}

			pageModelDto.setValue(addSuppDtoList);
		}
		return pageModelDto;
	}

	/**
	 *领导审批资料函处理
	 * @return
	 */
	@Override
	public void updateApprove(AddSuppLetterDto addSuppLetterDto) {
		HqlBuilder sqlBuilder = HqlBuilder.create();
		sqlBuilder.append("update "+AddSuppLetter.class.getSimpleName()+" set ");
		//部长审批
		if(SessionUtil.hashRole(Constant.EnumFlowNodeGroupName.DEPT_LEADER.getValue())
				|| SessionUtil.hashRole(Constant.EnumFlowNodeGroupName.COMM_DEPT_DIRECTOR.getValue())){
			sqlBuilder.append(AddSuppLetter_.deptMinisterIdeaContent.getName()+"=:deptMinisterIdeaContent , "
					+AddSuppLetter_.addSuppAppoveStatus.getName()+"=:addSuppAppoveStatus , "
					+AddSuppLetter_.deptMinisterDate.getName()+"=:deptMinisterDate");
			sqlBuilder.setParam("deptMinisterIdeaContent", addSuppLetterDto.getDeptMinisterIdeaContent());
			sqlBuilder.setParam("addSuppAppoveStatus",Constant.EnumState.PROCESS.getValue());
			sqlBuilder.setParam("deptMinisterDate",new Date());
		}
		//分管领导审批
		else if(SessionUtil.hashRole(Constant.EnumFlowNodeGroupName.VICE_DIRECTOR.getValue())){
			sqlBuilder.append(AddSuppLetter_.deptSLeaderIdeaContent.getName()+"=:deptSLeaderIdeaContent , "
					+AddSuppLetter_.addSuppAppoveStatus.getName()+ " =:addSuppAppoveStatus , "
					+AddSuppLetter_.deptSleaderDate.getName()+"=:deptSleaderDate");
			sqlBuilder.setParam("deptSLeaderIdeaContent", addSuppLetterDto.getDeptSLeaderIdeaContent());
			sqlBuilder.setParam("addSuppAppoveStatus",Constant.EnumState.STOP.getValue());
			sqlBuilder.setParam("deptSleaderDate",new Date());
		}
		//主任审批
		else if(SessionUtil.hashRole(Constant.EnumFlowNodeGroupName.DIRECTOR.getValue())){

			sqlBuilder.append(AddSuppLetter_.deptDirectorIdeaContent.getName()+"=:deptDirectorIdeaContent , "
					+AddSuppLetter_.addSuppAppoveStatus.getName()+ "=:addSuppAppoveStatus ,"
					+AddSuppLetter_.deptDirectorDate.getName()+"=:deptDirectorDate");

			sqlBuilder.setParam("deptDirectorIdeaContent", addSuppLetterDto.getDeptDirectorIdeaContent());
			sqlBuilder.setParam("addSuppAppoveStatus",Constant.EnumState.YES.getValue());
			sqlBuilder.setParam("deptDirectorDate",new Date());
			//生成拟稿最大编号
			AddSuppLetter addSuppLetter = addSuppLetterRepo.findById(AddSuppLetter_.id.getName(),addSuppLetterDto.getId());
			int curYearMaxSeq = findCurMaxSeq(addSuppLetterDto.getDisapDate());
			String filenum = Constant.DISPATCH_PREFIX + "[" + DateUtils.converToString(addSuppLetterDto.getDisapDate(), "yyyy") + "]" + (curYearMaxSeq + 1);
			addSuppLetter.setFilenum(filenum);
			addSuppLetter.setFileSeq((curYearMaxSeq + 1));
			addSuppLetterRepo.save(addSuppLetter);
		}
		sqlBuilder.append(" where "+AddSuppLetter_.id.getName()+"=:id");
		sqlBuilder.setParam("id", addSuppLetterDto.getId());
		addSuppLetterRepo.executeHql(sqlBuilder);
	}

	/**
	 * 领导审批（中心文件）处理
	 */
	@Override
	@Transactional
	public void monthlyApproveEdit(AddSuppLetterDto addSuppLetterDto) {
		HqlBuilder sqlBuilder = HqlBuilder.create();
		sqlBuilder.append("update "+AddSuppLetter.class.getSimpleName()+" set ");
		//部长审批
		if(SessionUtil.hashRole(Constant.EnumFlowNodeGroupName.DEPT_LEADER.getValue())
				|| SessionUtil.hashRole(Constant.EnumFlowNodeGroupName.COMM_DEPT_DIRECTOR.getValue())){
			sqlBuilder.append(AddSuppLetter_.deptMinisterIdeaContent.getName()+"=:deptMinisterIdeaContent , "
					+AddSuppLetter_.monthlyAppoveStatus.getName()+"=:monthlyAppoveStatus , "
					+AddSuppLetter_.deptMinisterDate.getName()+"=:deptMinisterDate");
			sqlBuilder.setParam("deptMinisterIdeaContent", addSuppLetterDto.getDeptMinisterIdeaContent());
			sqlBuilder.setParam("monthlyAppoveStatus",Constant.EnumState.PROCESS.getValue());
			sqlBuilder.setParam("deptMinisterDate",new Date());
		}
		//分管领导审批
		else if(SessionUtil.hashRole(Constant.EnumFlowNodeGroupName.VICE_DIRECTOR.getValue())){
			sqlBuilder.append(AddSuppLetter_.deptSLeaderIdeaContent.getName()+"=:deptSLeaderIdeaContent , "
					+AddSuppLetter_.monthlyAppoveStatus.getName()+ " =:monthlyAppoveStatus , "
					+AddSuppLetter_.deptSleaderDate.getName()+"=:deptSleaderDate");
			sqlBuilder.setParam("deptSLeaderIdeaContent", addSuppLetterDto.getDeptSLeaderIdeaContent());
			sqlBuilder.setParam("monthlyAppoveStatus",Constant.EnumState.STOP.getValue());
			sqlBuilder.setParam("deptSleaderDate",new Date());
		}
		//主任审批
		else if(SessionUtil.hashRole(Constant.EnumFlowNodeGroupName.DIRECTOR.getValue())){

			sqlBuilder.append(AddSuppLetter_.deptDirectorIdeaContent.getName()+"=:deptDirectorIdeaContent , "
					+AddSuppLetter_.monthlyAppoveStatus.getName()+ "=:monthlyAppoveStatus ,"
					+AddSuppLetter_.deptDirectorDate.getName()+"=:deptDirectorDate");

			sqlBuilder.setParam("deptDirectorIdeaContent", addSuppLetterDto.getDeptDirectorIdeaContent());
			sqlBuilder.setParam("monthlyAppoveStatus",Constant.EnumState.YES.getValue());
			sqlBuilder.setParam("deptDirectorDate",new Date());
			//生成拟稿最大编号
			AddSuppLetter addSuppLetter = addSuppLetterRepo.findById(AddSuppLetter_.id.getName(),addSuppLetterDto.getId());
			int curYearMaxSeq = findCurMaxSeq(addSuppLetterDto.getSuppLetterTime());
			String filenum = Constant.DISPATCH_PREFIX + "[" + DateUtils.converToString(addSuppLetterDto.getDisapDate(), "yyyy") + "]" + (curYearMaxSeq + 1);
			addSuppLetter.setFilenum(filenum);
			addSuppLetter.setFileSeq((curYearMaxSeq + 1));
			addSuppLetterRepo.save(addSuppLetter);
		}
		sqlBuilder.append(" where "+AddSuppLetter_.id.getName()+"=:id");
		sqlBuilder.setParam("id", addSuppLetterDto.getId());
		addSuppLetterRepo.executeHql(sqlBuilder);

	}

	/**
	 * 保存拟补充资料函
	 */
	@Override
	@Transactional
	public void saveSupp(AddSuppLetterDto addSuppLetterDto) {
		AddSuppLetter addSuppLetter = null;
		Date now = new Date();
       /*  if (Validate.isString(addSuppLetterDto.getId())) {
             addSuppLetter = addSuppLetterRepo.findById(addSuppLetterDto.getId());
             BeanCopierUtils.copyPropertiesIgnoreNull(addSuppLetter,addSuppLetterDto);
         } else {*/
		addSuppLetter = new AddSuppLetter();
		BeanCopierUtils.copyProperties(addSuppLetterDto, addSuppLetter);
		// addSuppLetter.setId(UUID.randomUUID().toString());
		addSuppLetter.setCreatedBy(SessionUtil.getDisplayName());
		addSuppLetter.setModifiedBy(SessionUtil.getDisplayName());
		//部长名称
		if(SessionUtil.getUserInfo().getOrg()!=null && SessionUtil.getUserInfo().getOrg().getOrgDirectorName()!=null){
			addSuppLetter.setDeptMinisterName(SessionUtil.getUserInfo().getOrg().getOrgDirectorName());
		}else{
			addSuppLetter.setDeptMinisterName(SessionUtil.getDisplayName());
		}
		//分管副主任
		if(SessionUtil.getUserInfo().getOrg()!=null && SessionUtil.getUserInfo().getOrg().getOrgSLeaderName()!=null){
			addSuppLetter.setDeptSLeaderName(SessionUtil.getUserInfo().getOrg().getOrgSLeaderName());
		}else{
			addSuppLetter.setDeptSLeaderName(SessionUtil.getDisplayName());
		}
		//主任
		if(SessionUtil.getUserInfo().getOrg()!=null && SessionUtil.getUserInfo().getOrg().getOrgMLeaderName()!=null){
			addSuppLetter.setDeptDirectorName(SessionUtil.getUserInfo().getOrg().getOrgMLeaderName());
		}else{
			addSuppLetter.setDeptDirectorName(SessionUtil.getDisplayName());
		}
		//查询列表状态
		addSuppLetter.setAddSuppStatus(Constant.EnumState.NO.getValue());
		addSuppLetter.setAddSuppAppoveStatus(Constant.EnumState.NO.getValue());

		//  }
		if(!Validate.isString(addSuppLetter.getId())){
			addSuppLetter.setId(null);
		}
		/*Sign sign =  signRepo.findById(addSuppLetterDto.getBusinessId());
		sign.setIsHaveSuppLetter(Constant.EnumState.YES.getValue());
		sign.setSuppLetterDate(addSuppLetterDto.getDisapDate());
		signRepo.save(sign);*/
		 WorkProgram work =  workProgramRepo.findById(addSuppLetterDto.getWorkId());
         work.setIsHaveSuppLetter(Constant.EnumState.YES.getValue());
         work.setSuppLetterDate(addSuppLetterDto.getDisapDate());
         workProgramRepo.save(work);
		addSuppLetter.setModifiedDate(now);
		addSuppLetter.setCreatedDate(now);
		addSuppLetterRepo.save(addSuppLetter);
		addSuppLetterDto.setId(addSuppLetter.getId());
	}

	/**
	 * 查询主页上的拟补资料函信息
	 * @return
	 */
	@Override
	public List<AddSuppLetterDto> findHomeAddSuppLetter() {
		List<AddSuppLetterDto> addSuppLetterDtoList = new ArrayList<>();
		Criteria criteria = addSuppLetterRepo.getExecutableCriteria();
		//部长审批
		if(SessionUtil.hashRole(Constant.EnumFlowNodeGroupName.DEPT_LEADER.getValue())){
			criteria.add(Restrictions.eq(AddSuppLetter_.deptMinisterName.getName(), SessionUtil.getDisplayName()));
			criteria.add(Restrictions.eq(AddSuppLetter_.addSuppAppoveStatus.getName(), Constant.EnumState.NO.getValue()));
		}
		//分管领导
		else if(SessionUtil.hashRole(Constant.EnumFlowNodeGroupName.VICE_DIRECTOR.getValue())){
			criteria.add(Restrictions.eq(AddSuppLetter_.deptSLeaderName.getName(), SessionUtil.getDisplayName()));
			criteria.add(Restrictions.eq(AddSuppLetter_.addSuppAppoveStatus.getName(), Constant.EnumState.PROCESS.getValue()));
		}
		//主任
		else if(SessionUtil.hashRole(Constant.EnumFlowNodeGroupName.DIRECTOR.getValue())){
			criteria.add(Restrictions.eq(AddSuppLetter_.deptDirectorName.getName(), SessionUtil.getDisplayName()));
			criteria.add(Restrictions.eq(AddSuppLetter_.addSuppAppoveStatus.getName(), Constant.EnumState.STOP.getValue()));
		}else{
			return addSuppLetterDtoList;
		}
		criteria.setMaxResults(6);
		criteria.addOrder(Order.desc(AddSuppLetter_.createdDate.getName()));
		List<AddSuppLetter> addSuppLetterList = criteria.list();
		for(AddSuppLetter a : addSuppLetterList){
			AddSuppLetterDto addSuppLetterDto = new AddSuppLetterDto();
			BeanCopierUtils.copyProperties(a , addSuppLetterDto);
			addSuppLetterDtoList.add(addSuppLetterDto);
		}
		return addSuppLetterDtoList;
	}


	/**
	 * 查询主页上的月报简报信息
	 * @return
	 */
	@Override
	public List<AddSuppLetterDto> findHomeMonthly() {
		List<AddSuppLetterDto> addSuppLetterDtoList = new ArrayList<>();
		Criteria criteria = addSuppLetterRepo.getExecutableCriteria();
		//部长审批
		if(SessionUtil.hashRole(Constant.EnumFlowNodeGroupName.DEPT_LEADER.getValue())){
			criteria.add(Restrictions.eq(AddSuppLetter_.deptMinisterName.getName(), SessionUtil.getDisplayName()));
			criteria.add(Restrictions.eq(AddSuppLetter_.monthlyAppoveStatus.getName(), Constant.EnumState.NO.getValue()));
		}
		//分管领导
		else if(SessionUtil.hashRole(Constant.EnumFlowNodeGroupName.VICE_DIRECTOR.getValue())){
			criteria.add(Restrictions.eq(AddSuppLetter_.deptSLeaderName.getName(), SessionUtil.getDisplayName()));
			criteria.add(Restrictions.eq(AddSuppLetter_.monthlyAppoveStatus.getName(), Constant.EnumState.PROCESS.getValue()));
		}
		//主任
		else if(SessionUtil.hashRole(Constant.EnumFlowNodeGroupName.DIRECTOR.getValue())){
			criteria.add(Restrictions.eq(AddSuppLetter_.deptDirectorName.getName(), SessionUtil.getDisplayName()));
			criteria.add(Restrictions.eq(AddSuppLetter_.monthlyAppoveStatus.getName(), Constant.EnumState.STOP.getValue()));
		}else{
			return addSuppLetterDtoList;
		}

		criteria.setMaxResults(6);
		criteria.addOrder(Order.desc(AddSuppLetter_.createdDate.getName()));
		List<AddSuppLetter> addSuppLetterList = criteria.list();
		for(AddSuppLetter a : addSuppLetterList){
			AddSuppLetterDto addSuppLetterDto = new AddSuppLetterDto();
			BeanCopierUtils.copyProperties(a , addSuppLetterDto);
			addSuppLetterDtoList.add(addSuppLetterDto);
		}
		return addSuppLetterDtoList;
	}

	@Override
	public int countSuppLetter() {
		Criteria criteria = addSuppLetterRepo.getExecutableCriteria();
		//部长审批
		if(SessionUtil.hashRole(Constant.EnumFlowNodeGroupName.DEPT_LEADER.getValue())){
			criteria.add(Restrictions.eq(AddSuppLetter_.deptMinisterName.getName(), SessionUtil.getDisplayName()));
			criteria.add(Restrictions.eq(AddSuppLetter_.addSuppAppoveStatus.getName(), Constant.EnumState.NO.getValue()));
		}
		//分管领导
		else if(SessionUtil.hashRole(Constant.EnumFlowNodeGroupName.VICE_DIRECTOR.getValue())){
			criteria.add(Restrictions.eq(AddSuppLetter_.deptSLeaderName.getName(), SessionUtil.getDisplayName()));
			criteria.add(Restrictions.eq(AddSuppLetter_.addSuppAppoveStatus.getName(), Constant.EnumState.PROCESS.getValue()));
		}
		//主任
		else if(SessionUtil.hashRole(Constant.EnumFlowNodeGroupName.DIRECTOR.getValue())){
			criteria.add(Restrictions.eq(AddSuppLetter_.deptDirectorName.getName(), SessionUtil.getDisplayName()));
			criteria.add(Restrictions.eq(AddSuppLetter_.addSuppAppoveStatus.getName(), Constant.EnumState.STOP.getValue()));
		}else{
			return 0;
		}

		return ((Number)criteria.setProjection(Projections.rowCount()).uniqueResult()).intValue();
	}

	@Override
	public int countMonthly() {

		Criteria criteria = addSuppLetterRepo.getExecutableCriteria();
		//部长审批
		if(SessionUtil.hashRole(Constant.EnumFlowNodeGroupName.DEPT_LEADER.getValue())){
			criteria.add(Restrictions.eq(AddSuppLetter_.deptMinisterName.getName(), SessionUtil.getDisplayName()));
			criteria.add(Restrictions.eq(AddSuppLetter_.monthlyAppoveStatus.getName(), Constant.EnumState.NO.getValue()));
		}
		//分管领导
		else if(SessionUtil.hashRole(Constant.EnumFlowNodeGroupName.VICE_DIRECTOR.getValue())){
			criteria.add(Restrictions.eq(AddSuppLetter_.deptSLeaderName.getName(), SessionUtil.getDisplayName()));
			criteria.add(Restrictions.eq(AddSuppLetter_.monthlyAppoveStatus.getName(), Constant.EnumState.PROCESS.getValue()));
		}
		//主任
		else if(SessionUtil.hashRole(Constant.EnumFlowNodeGroupName.DIRECTOR.getValue())){
			criteria.add(Restrictions.eq(AddSuppLetter_.deptDirectorName.getName(), SessionUtil.getDisplayName()));
			criteria.add(Restrictions.eq(AddSuppLetter_.monthlyAppoveStatus.getName(), Constant.EnumState.STOP.getValue()));
		}else{
			return 0;
		}
		return ((Number)criteria.setProjection(Projections.rowCount()).uniqueResult()).intValue();
	}


}