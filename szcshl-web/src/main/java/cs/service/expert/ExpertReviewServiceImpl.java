package cs.service.expert;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import cs.domain.expert.*;
import cs.domain.meeting.RoomBooking;
import cs.domain.meeting.RoomBooking_;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Session;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cs.common.Constant;
import cs.common.HqlBuilder;
import cs.common.ICurrentUser;
import cs.common.Constant.EnumExpertSelectType;
import cs.common.Constant.EnumState;
import cs.common.utils.BeanCopierUtils;
import cs.common.utils.StringUtil;
import cs.common.utils.Validate;
import cs.domain.project.Sign;
import cs.domain.project.Sign_;
import cs.domain.project.WorkProgram;
import cs.domain.project.WorkProgram_;
import cs.domain.sys.User;
import cs.model.PageModelDto;
import cs.model.expert.ExpertDto;
import cs.model.expert.ExpertReviewDto;
import cs.model.sys.UserDto;
import cs.repository.odata.ODataObj;
import cs.repository.repositoryImpl.expert.ExpertRepo;
import cs.repository.repositoryImpl.expert.ExpertReviewRepo;
import cs.repository.repositoryImpl.project.SignRepo;
import cs.repository.repositoryImpl.project.WorkProgramRepo;

/**
 * Description: 专家评审 业务操作实现类 author: ldm Date: 2017-5-17 14:02:25
 */
@Service
public class ExpertReviewServiceImpl implements ExpertReviewService {
	private static Logger log = Logger.getLogger(ExpertReviewServiceImpl.class);
	@Autowired
	private ExpertReviewRepo expertReviewRepo;
	@Autowired
	private ICurrentUser currentUser;
	@Autowired
	private ExpertRepo expertRepo;
	@Autowired
	private WorkProgramRepo workProgramRepo;
	@Autowired
	private SignRepo signRepo;

	@Override
	public PageModelDto<ExpertReviewDto> get(ODataObj odataObj) {
		PageModelDto<ExpertReviewDto> pageModelDto = new PageModelDto<ExpertReviewDto>();
		List<ExpertReview> resultList = expertReviewRepo.findByOdata(odataObj);
		List<ExpertReviewDto> resultDtoList = new ArrayList<ExpertReviewDto>(resultList.size());

		if (resultList != null && resultList.size() > 0) {
			resultList.forEach(x -> {
				ExpertReviewDto modelDto = new ExpertReviewDto();
				BeanCopierUtils.copyProperties(x, modelDto);
				modelDto.setCreatedDate(x.getCreatedDate());
				modelDto.setModifiedDate(x.getModifiedDate());

				resultDtoList.add(modelDto);
			});
		}
		pageModelDto.setCount(odataObj.getCount());
		pageModelDto.setValue(resultDtoList);
		return pageModelDto;
	}

	@Override
	@Transactional
	public void save(ExpertReviewDto record) throws Exception {
		ExpertReview domain = new ExpertReview();
		BeanCopierUtils.copyProperties(record, domain);
		if (Validate.isString(record.getExpertId()) && Validate.isString(record.getWorkProgramId())) {
			domain.setExpert(expertRepo.findById(record.getExpertId()));
			domain.setWorkProgram(workProgramRepo.findById(record.getWorkProgramId()));
		} else {
			log.info("评审专家保存失败：无法获取专家ID和工作方案ID");
			throw new Exception(Constant.ERROR_MSG);
		}

		Date now = new Date();
		domain.setCreatedBy(currentUser.getLoginName());
		domain.setModifiedBy(currentUser.getLoginName());
		domain.setCreatedDate(now);
		domain.setModifiedDate(now);
		expertReviewRepo.save(domain);
	}

	@Override
	@Transactional
	public void update(ExpertReviewDto record) {
		ExpertReview domain = expertReviewRepo.findById(record.getId());
		BeanCopierUtils.copyPropertiesIgnoreNull(record, domain);
		domain.setModifiedBy(currentUser.getLoginName());
		domain.setModifiedDate(new Date());

		expertReviewRepo.save(domain);
	}

	@Override
	public ExpertReviewDto findById(String id) {
		ExpertReviewDto modelDto = new ExpertReviewDto();
		if (Validate.isString(id)) {
			ExpertReview domain = expertReviewRepo.findById(id);
			BeanCopierUtils.copyProperties(domain, modelDto);
		}
		return modelDto;
	}

	@Override
	@Transactional
	public void delete(String id) {
		expertReviewRepo.deleteById(ExpertReview_.id.getName(), id);
	}

	@Override
	public List<ExpertReviewDto> initByWorkProgramId(String workProgramId) {
		HqlBuilder hqlBuilder = HqlBuilder.create();
		hqlBuilder.append(" from " + ExpertReview.class.getSimpleName() + " where "
				+ ExpertReview_.workProgram.getName() + "." + WorkProgram_.id.getName() + " = :workProgramId ");
		hqlBuilder.setParam("workProgramId", workProgramId);
		List<ExpertReview> list = expertReviewRepo.findByHql(hqlBuilder);

		if (list != null && list.size() > 0) {
			List<ExpertReviewDto> resultList = new ArrayList<ExpertReviewDto>(list.size());
			list.forEach(l -> {
				ExpertReviewDto epDto = new ExpertReviewDto();
				BeanCopierUtils.copyProperties(l, epDto);
				if (l.getExpert() != null) {
					ExpertDto expertDto = new ExpertDto();
					BeanCopierUtils.copyProperties(l.getExpert(), expertDto);
					epDto.setExpertDto(expertDto);
				}
				resultList.add(epDto);
			});

			return resultList;
		}
		return null;
	}

	@Override
	public Map<String, Object> getReviewList(String orgName, String year, String quarter) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
		Date startDate = null, endDate = null;
		
		try {
			if(quarter=="1"){
				startDate = format.parse(year+"-01");
				endDate = format.parse(year+"-07");
			}else{
				startDate = format.parse(year+"-07");
				endDate = format.parse(year+"-12");
			}
		} catch (ParseException e) {
			e.printStackTrace(); 
		}
		//查询全部信息
		HqlBuilder hqlBuilder = HqlBuilder.create();
		hqlBuilder.append(" from " + ExpertReview.class.getSimpleName()+" where ");
		hqlBuilder.append( ExpertReview_.workProgram.getName() + "." + WorkProgram_.reviewOrgName.getName() + " = :reviewOrgName ");
		hqlBuilder.append( " and "+ExpertReview_.reviewDate.getName() +" between :startDate and :endDate ");
		//hqlBuilder.append(" group by "+ ExpertReview_.expert.getName()+"."+Expert_.name.getName());
		hqlBuilder.setParam("reviewOrgName", orgName);
		hqlBuilder.setParam("startDate", startDate);
		hqlBuilder.setParam("endDate", endDate);
		List<ExpertReview> list = expertReviewRepo.findByHql(hqlBuilder);
		if(Validate.isList(list)){
			list.forEach(o->{
				ExpertReviewDto epDto = new ExpertReviewDto();
				BeanCopierUtils.copyProperties(o, epDto);
			});
		}
		
		//统计合计
		HqlBuilder hqlBuilder2 = HqlBuilder.create();
		//专家数量统计
		hqlBuilder2.append("select count(*) from " + ExpertReview.class.getSimpleName());
		//会议次数统计
		HqlBuilder hqlBuilder3= HqlBuilder.create();
		hqlBuilder3.append("select count(*) from " + RoomBooking.class.getSimpleName()+" where ");
		hqlBuilder.append( " and "+RoomBooking_.rbDay.getName() +" between :startDate and :endDate ");
		hqlBuilder.append( ExpertReview_.workProgram.getName() + "." + WorkProgram_.reviewOrgName.getName() + " = :reviewOrgName ");
		//hqlBuilder2.append("select count(*) from " + ExpertReview.class.getSimpleName());
		//hqlBuilder2.append( ExpertReview_.workProgram.getName() + "." + WorkProgram_.reviewOrgName.getName() + " = :reviewOrgName ");
		//hqlBuilder2.append( " and "+ExpertReview_.reviewDate.getName() +" between :startDate and :endDate ");
		//hqlBuilder2.append(" group by "+ ExpertReview_.expert.getName()+"."+Expert_.name.getName());
		  hqlBuilder2.setParam("reviewOrgName", orgName);
		  hqlBuilder3.setParam("startDate", startDate);
		  hqlBuilder3.setParam("endDate", endDate);
		List<ExpertReview> totallist = expertReviewRepo.findByHql(hqlBuilder2);
		if(Validate.isList(totallist)){
			totallist.forEach(o->{
				ExpertReviewDto epDto2 = new ExpertReviewDto();
				BeanCopierUtils.copyProperties(o, epDto2);
			});
		}
	/*	Criteria criteria = expertReviewRepo.getSession().createCriteria(ExpertReview.class);
		// 查询全部
		// criteria.addOrder(Order.asc("e.name"));
		// criteria.add(Restrictions.eq("w.reviewOrgName", orgName));
		//criteria.add(Restrictions.between(ExpertReview_.reviewDate.getName(), startDate, endDate));
		ProjectionList projectionList = Projections.projectionList();
		criteria.createAlias("expert", "e",CriteriaSpecification.LEFT_JOIN).setFetchMode("expert", FetchMode.JOIN);
		//每个人参会次数
		projectionList.add(Projections.count("e.name"));
		// 综合评分
		projectionList.add(Projections.sum(ExpertReview_.score.getName()));
		criteria.setProjection(projectionList);
		projectionList.add(Projections.groupProperty("e.name"));
		List reviewList = criteria.list();
		
		//合计
		Criteria criteria2 = expertReviewRepo.getSession().createCriteria(ExpertReview.class);
		criteria2.createAlias("workProgram", "w").setFetchMode("workProgram", FetchMode.JOIN);
		ProjectionList projectionList2 = Projections.projectionList();
		
		projectionList2.add(Projections.rowCount());
		//int totalCount = ((Integer) criteria2.setProjection(Projections.rowCount()).uniqueResult()).intValue();
		criteria2.setProjection(projectionList);
		List totalList = criteria.list();
		*/
		return null;
	}
	
	//todo
	//查詢評分和費用（待修改）
	@Override
	public List<ExpertDto> getSelectExpert(String signId) {
		Sign sign=signRepo.getById(signId);
		List<ExpertDto> expertDtoList=new ArrayList<ExpertDto>();
		if(sign!=null){
			List<WorkProgram> workList=sign.getWorkProgramList();
			if(Validate.isList(workList)){
				for (WorkProgram workProgram : workList) {
					List<ExpertReview> expertReviewList=workProgram.getExpertReviews();
				if(Validate.isList(expertReviewList)){					
					for (ExpertReview expertReview : expertReviewList) {
							ExpertDto expertDto=new ExpertDto();
							ExpertReviewDto expertReviewDto=new ExpertReviewDto();
							BeanCopierUtils.copyProperties(expertReview.getExpert(), expertDto);
							BeanCopierUtils.copyProperties(expertReview, expertReviewDto);
							expertDto.setExpertReviewDto(expertReviewDto);
							expertDtoList.add(expertDto);
					}
				}
				}
			}
		}
		return expertDtoList;
	}
	
	@Override
	public ExpertReviewDto getSelectExpertById(String expertId) {
		Date now=new Date();
		Expert expert=expertRepo.getById(expertId);
		ExpertDto expertDto=new ExpertDto();
		ExpertReviewDto expertReviewDto=new ExpertReviewDto();
		if(expert!=null){
			BeanCopierUtils.copyPropertiesIgnoreNull(expert, expertDto);
			ExpertReview expertReview=expert.getExpertReview();
			if(expertReview!=null){
					BeanCopierUtils.copyPropertiesIgnoreNull(expertReview, expertReviewDto);
					expertReviewDto.setExpertDto(expertDto);
					expertReviewDto.setModifiedBy(currentUser.getLoginName());
					expertReviewDto.setModifiedDate(now);
				}
		}
		return expertReviewDto;
	}
	//更新数据,保存分数
	@Override
	@Transactional
	public void expertMark(ExpertReviewDto expertReviewDto ) {
		Date now=new Date();
		Expert expert=expertRepo.getById(expertReviewDto.getExpertId());
		ExpertReview expertReview=expert.getExpertReview();
		if(expertReview != null){
			expertReview.setScore(expertReviewDto.getScore()==null?0:expertReviewDto.getScore());
			expertReview.setDescribes(expertReviewDto.getDescribes());
			expertReview.setReviewDate(now);
			expert.setExpertReview(expertReview);
			expertRepo.save(expert);
		}
	}
	
	//更新数据,保存费用
	@Override
	@Transactional
	public void savePayment(ExpertReviewDto expertReviewDto)throws Exception {
		if(Validate.isString(expertReviewDto.getExpertId())){
			Date now=new Date();
			Expert expert=expertRepo.getById(expertReviewDto.getExpertId());
			if(expert!=null && !Validate.isBlank(expert.getExpertID())){
				ExpertReview expertReview=expert.getExpertReview();
				if(expertReview != null){
					BeanCopierUtils.copyPropertiesIgnoreNull(expertReviewDto,expertReview);
					expertReview.setModifiedBy(currentUser.getLoginName());
					expertReview.setModifiedDate(now);
					expert.setExpertReview(expertReview);
				}
				expertRepo.save(expert);
			}
		} else {
			log.info("提交收文信息异常：无法获取收文ID（SignId）信息");
			throw new Exception(Constant.ERROR_MSG);
		}
	}

	@Override
	@Transactional
	public void save(String workProgramId, String expertIds, String selectType) {
		WorkProgram workProgram = workProgramRepo.findById(workProgramId);
		Date now = new Date();
		List<String> expertIdArr = StringUtil.getSplit(expertIds, ",");
		for (int i = 0, l = expertIdArr.size(); i < l; i++) {
			ExpertReview domain = new ExpertReview();
			domain.setId(UUID.randomUUID().toString());

			// 由于自选只能选一个，所以要先删除之前选的专家
			if (EnumExpertSelectType.SELF.getValue().equals(selectType)) {
				deleteExpert(workProgramId, null, EnumExpertSelectType.SELF.getValue(), null);
			}
			// 评审会时间
			domain.setReviewDate(workProgram.getStageTime());
			domain.setSelectType(selectType);
			domain.setExpert(expertRepo.findById(expertIdArr.get(i)));
			domain.setWorkProgram(workProgram);
			domain.setState(EnumState.NO.getValue());

			domain.setCreatedBy(currentUser.getLoginName());
			domain.setModifiedBy(currentUser.getLoginName());
			domain.setCreatedDate(now);
			domain.setModifiedDate(now);
			expertReviewRepo.save(domain);
		}
	}

	@Override
	public List<ExpertDto> refleshExpert(String workProgramId, String selectType) {
		List<ExpertDto> resultList = new ArrayList<ExpertDto>();

		HqlBuilder hqlBuilder = HqlBuilder.create();
		hqlBuilder.append(" from " + ExpertReview.class.getSimpleName() + " where "
				+ ExpertReview_.workProgram.getName() + "." + WorkProgram_.id.getName() + " = :workProgramId ");
		hqlBuilder.setParam("workProgramId", workProgramId);
		hqlBuilder.append(" and " + ExpertReview_.selectType.getName() + " = :selectType ");
		hqlBuilder.setParam("selectType", selectType);

		List<ExpertReview> list = expertReviewRepo.findByHql(hqlBuilder);
		if (list != null && list.size() > 0) {
			list.forEach(l -> {
				ExpertDto expertDto = new ExpertDto();
				Expert expert = l.getExpert();
				if (expert != null) {
					BeanCopierUtils.copyProperties(expert, expertDto);
					resultList.add(expertDto);
				}
			});
		}
		return resultList;
	}

	@Override
	@Transactional
	public void updateExpertState(String workProgramId, String expertIds, String state) {
		HqlBuilder hqlBuilder = HqlBuilder.create();
		hqlBuilder.append(" update " + ExpertReview.class.getSimpleName() + " set " + ExpertReview_.state.getName()
				+ " = :state ");
		hqlBuilder.setParam("state", state);
		hqlBuilder.append(" where " + ExpertReview_.workProgram.getName() + "." + WorkProgram_.id.getName()
				+ " = :workProgramId ");
		hqlBuilder.setParam("workProgramId", workProgramId);

		String[] idArr = expertIds.split(",");
		if (idArr.length > 1) {
			hqlBuilder.append(" and " + ExpertReview_.expert.getName() + "." + Expert_.expertID.getName() + " in ( ");
			int totalL = idArr.length;
			for (int i = 0; i < totalL; i++) {
				if (i == totalL - 1) {
					hqlBuilder.append(" :id" + i).setParam("id" + i, idArr[i]);
				} else {
					hqlBuilder.append(" :id" + i + ",").setParam("id" + i, idArr[i]);
				}
			}
			hqlBuilder.append(" ) ");
		} else {
			hqlBuilder.append(
					" and " + ExpertReview_.expert.getName() + "." + Expert_.expertID.getName() + " = :expertId ");
			hqlBuilder.setParam("expertId", expertIds);
		}
		expertReviewRepo.executeHql(hqlBuilder);
	}

	@Override
	@Transactional
	public void deleteExpert(String workProgramId, String expertIds, String seleType, String expertSelConditionId) {
		HqlBuilder hqlBuilder = HqlBuilder.create();
		hqlBuilder.append(" delete from " + ExpertReview.class.getSimpleName());
		hqlBuilder.append(" where " + ExpertReview_.workProgram.getName() + "." + WorkProgram_.id.getName()
				+ " = :workProgramId ");
		hqlBuilder.setParam("workProgramId", workProgramId);

		if (Validate.isString(expertIds)) {
			String[] idArr = expertIds.split(",");
			if (idArr.length > 1) {
				hqlBuilder
						.append(" and " + ExpertReview_.expert.getName() + "." + Expert_.expertID.getName() + " in ( ");
				int totalL = idArr.length;
				for (int i = 0; i < totalL; i++) {
					if (i == totalL - 1) {
						hqlBuilder.append(" :id" + i).setParam("id" + i, idArr[i]);
					} else {
						hqlBuilder.append(" :id" + i + ",").setParam("id" + i, idArr[i]);
					}
				}
				hqlBuilder.append(" ) ");
			} else {
				hqlBuilder.append(
						" and " + ExpertReview_.expert.getName() + "." + Expert_.expertID.getName() + " = :expertId ");
				hqlBuilder.setParam("expertId", expertIds);
			}
		}

		if (Validate.isString(seleType)) {
			hqlBuilder.append(" and " + ExpertReview_.selectType.getName() + " =:seleType").setParam("seleType",
					seleType);
		}

		if (Validate.isString(expertSelConditionId)) {
			hqlBuilder.append(" and " + ExpertReview_.epSelCondition.getName() + "." + ExpertSelCondition_.id.getName()
					+ " =:expertSelConditionId").setParam("expertSelConditionId", expertSelConditionId);
		}
		expertReviewRepo.executeHql(hqlBuilder);
	}

}