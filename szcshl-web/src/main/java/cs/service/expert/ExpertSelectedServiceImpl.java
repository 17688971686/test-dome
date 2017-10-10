package cs.service.expert;

import cs.common.Constant;
import cs.common.HqlBuilder;
import cs.common.ResultMsg;
import cs.common.utils.BeanCopierUtils;
import cs.common.utils.DateUtils;
import cs.common.utils.Validate;
import cs.domain.expert.ExpertReview;
import cs.domain.expert.ExpertSelected;
import cs.domain.expert.ExpertSelected_;
import cs.domain.financial.FinancialManager;
import cs.domain.financial.FinancialManager_;
import cs.domain.sys.User;
import cs.model.PageModelDto;
import cs.model.expert.*;
import cs.model.financial.FinancialManagerDto;
import cs.repository.odata.ODataFilterItem;
import cs.repository.odata.ODataObj;
import cs.repository.odata.ODataObjFilterStrategy;
import cs.repository.repositoryImpl.expert.ExpertCostCountRepo;
import cs.repository.repositoryImpl.expert.ExpertReviewRepo;
import cs.repository.repositoryImpl.expert.ExpertSelectedRepo;
import cs.repository.repositoryImpl.financial.FinancialManagerRepo;
import cs.service.financial.FinancialManagerService;
import cs.service.project.SignPrincipalService;
import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

/**
 * Description: 抽取专家 业务操作实现类
 * author: ldm
 * Date: 2017-6-14 14:26:49
 */
@Service
public class ExpertSelectedServiceImpl  implements ExpertSelectedService {

	@Autowired
	private ExpertSelectedRepo expertSelectedRepo;
	@Autowired
	private ExpertReviewRepo expertReviewRepo;
	@Autowired
	private ExpertCostCountRepo expertCostCountRepo;
	@Autowired
	private SignPrincipalService signPrincipalService;
	@Autowired
	private FinancialManagerRepo financialManagerRepo;
	@Autowired
	private FinancialManagerService financialManagerService;


	@Override
	@Transactional
	public void save(ExpertSelectedDto record) {
		ExpertSelected domain = new ExpertSelected();
		BeanCopierUtils.copyProperties(record, domain);

		expertSelectedRepo.save(domain);
	}

	@Override
	@Transactional
	public void update(ExpertSelectedDto record) {
		ExpertSelected domain = expertSelectedRepo.findById(record.getId());
		BeanCopierUtils.copyPropertiesIgnoreNull(record, domain);

		expertSelectedRepo.save(domain);
	}

	@Override
	public ExpertSelectedDto findById(String id) {
		ExpertSelectedDto modelDto = new ExpertSelectedDto();
		if (Validate.isString(id)) {
			ExpertSelected domain = expertSelectedRepo.findById(id);
			modelDto.setExpertDto(new ExpertDto());
			BeanCopierUtils.copyProperties(domain, modelDto);
			BeanCopierUtils.copyProperties(domain.getExpert(), modelDto.getExpertDto());
		}
		return modelDto;
	}

	/**
	 * 删除已经抽取的专家（主要针对自选和境外专家）
	 *
	 * @param reviewId
	 * @param ids
	 */
	@Override
	@Transactional
	public ResultMsg delete(String reviewId, String ids, boolean deleteAll) {
		ExpertReview expertReview = expertReviewRepo.findById(reviewId);
		if (deleteAll) {
			expertReviewRepo.delete(expertReview);
		} else {
			expertSelectedRepo.deleteById(ExpertSelected_.id.getName(), ids);
		}
		return new ResultMsg(true, Constant.MsgCode.OK.getValue(), "删除成功！");
	}

	@Override
	public PageModelDto<ExpertSelectedDto> get(ODataObj odataObj) {
		PageModelDto<ExpertSelectedDto> pageModelDto = new PageModelDto<ExpertSelectedDto>();
		List<ExpertSelectedDto> expertSelectedDtoList = new ArrayList<>();
		String beginTime = "" ,endTime = "";
		Date bTime = null, eTime = null;
		//Criteria 查询
		Criteria criteria = expertSelectedRepo.getExecutableCriteria();
		if (Validate.isList(odataObj.getFilter())) {
			Object value;
			for (ODataFilterItem item : odataObj.getFilter()) {
				value = item.getValue();
				if (null == value) {
					continue;
				}
				if("beginTime".equals(item.getField())){
					beginTime = item.getValue().toString();
                    bTime = DateUtils.converToDate1(beginTime,"EEE MMM dd HH:mm:ss Z yyyy");
                    beginTime = DateUtils.converToString(bTime,"yyyy-MM-dd");
					continue;
				}
				if("endTime".equals(item.getField())){
					endTime = item.getValue().toString();
                    eTime = DateUtils.converToDate1(endTime,"EEE MMM dd HH:mm:ss Z yyyy");
                    endTime = DateUtils.converToString(eTime,"yyyy-MM-dd");
					continue;
				}
				criteria.add(ODataObjFilterStrategy.getStrategy(item.getOperator()).getCriterion(item.getField(),value));
			}

		}
		//根据评审费发放时间查询
		if(beginTime != null || endTime != null){
            StringBuffer sqlSB = new StringBuffer();
            sqlSB.append(" (select count(t.id) from cs_expert_review t  where t.id = "+criteria.getAlias()+"_.EXPERTREVIEWID ");
            if (Validate.isString(beginTime)) {
                sqlSB.append(" and t.paydate >= to_date('"+beginTime+"', 'yyyy-mm-dd')");
            }
            if (Validate.isString(endTime)) {
                sqlSB.append(" and t.paydate <= to_date('"+endTime+"', 'yyyy-mm-dd')");
            }
            sqlSB.append(" and "+criteria.getAlias()+"_.ISCONFRIM='9'");
            sqlSB.append(" and "+criteria.getAlias()+"_.ISJOIN='9'");
            sqlSB.append(" ) > 0 ");
            criteria.add(Restrictions.sqlRestriction(sqlSB.toString()));
        }
		Integer totalResult = ((Number) criteria.setProjection(Projections.rowCount()).uniqueResult()).intValue();
		criteria.setProjection(null);
		// 处理分页
		if (odataObj.getSkip() > 0) {
			criteria.setFirstResult(odataObj.getSkip());
		}
		if (odataObj.getTop() > 0) {
			criteria.setMaxResults(odataObj.getTop());
		}
		List<ExpertSelected> resultList = criteria.list();
		List<ExpertSelectedDto> resultDtoList = new ArrayList<ExpertSelectedDto>(resultList.size());
		if (resultList != null && resultList.size() > 0) {
			resultList.forEach(x -> {
				ExpertSelectedDto modelDto = new ExpertSelectedDto();
				BeanCopierUtils.copyProperties(x, modelDto);
				modelDto.setExpertDto(new ExpertDto());
				modelDto.setExpertReviewDto(new ExpertReviewDto());
				BeanCopierUtils.copyProperties(x.getExpert(), modelDto.getExpertDto());
				BeanCopierUtils.copyProperties(x.getExpertReview(), modelDto.getExpertReviewDto());
				User u = signPrincipalService.getMainPriUser(x.getExpertReview().getBusinessId());
				if(null != u){
					modelDto.setPrincipal(u.getDisplayName());
				}
				resultDtoList.add(modelDto);
			});
		}
		pageModelDto.setCount(totalResult);
		pageModelDto.setValue(resultDtoList);
		return pageModelDto;
	}

	/**
	 * 专家评审费用统计
	 *
	 * @param
	 * @return
	 */
	@Override
	public ResultMsg expertCostTotal(ExpertCostCountDto expertCostDto) {
		//odataObj.getFilter().get(0);
		Map<String, Object> resultMap = new HashMap<>();
		PageModelDto<ExpertCostCountDto> pageModelDto = new PageModelDto<ExpertCostCountDto>();
		HqlBuilder sqlBuilder = HqlBuilder.create();
		sqlBuilder.append("select t.name,t.idcard,t.userphone,sum(t.reviewcost)reviewcost,sum(t.reviewtaxes)reviewtaxes,sum(t.yreviewcost)yreviewcost,sum(t.yreviewtaxes)yreviewtaxes from( ");
		sqlBuilder.append("select e.name,e.idcard,e.userphone,sum(s.reviewcost) reviewcost,sum(s.reviewtaxes)reviewtaxes,null yreviewcost,null yreviewtaxes from cs_expert_selected s ");
		sqlBuilder.append("left join cs_expert e  on s.expertid = e.expertid ");
		sqlBuilder.append("left join cs_expert_review r on s.expertreviewid = r.id ");
		sqlBuilder.append("where 1=1 ");
		String[] timeArr = expertCostDto.getBeginTime().split("-");;
		if(null != expertCostDto && null != expertCostDto.getBeginTime()){
			String day = DateUtils.getMaxDayOfMonth(Integer.parseInt(timeArr[0]),(Integer.parseInt(timeArr[1])-1))+"";
			String bTime = expertCostDto.getBeginTime()+"-01 00:00:00";
			String eTime = expertCostDto.getBeginTime()+"-"+day+" 23:59:59";
            sqlBuilder.append("and r.paydate is not null  ");
			sqlBuilder.append("and r.paydate >= to_date('"+bTime+"', 'yyyy-mm-dd hh24:mi:ss') ");
			sqlBuilder.append(" and r.paydate <= to_date('"+eTime+"', 'yyyy-mm-dd hh24:mi:ss') ");
			sqlBuilder.append("and s.isconfrim = '9' ");
			sqlBuilder.append("and s.isjoin = '9' ");
		}
		sqlBuilder.append("group by e.expertid,e.name,e.idcard,e.userphone ");
		sqlBuilder.append("having sum(s.reviewcost)>0");
		sqlBuilder.append("union  ");
		sqlBuilder.append("select e.name,e.idcard,e.userphone,null reviewcost,null reviewtaxes,sum(s.reviewcost) yreviewcost,sum(s.reviewtaxes)yreviewtaxes from cs_expert_selected s  ");
		sqlBuilder.append("left join cs_expert e  on s.expertid = e.expertid  ");
		sqlBuilder.append("left join cs_expert_review r on s.expertreviewid = r.id ");
		sqlBuilder.append("where r.paydate is not null and  r.paydate >= to_date('"+timeArr[0]+"-01-01 00:00:00','yyyy-mm-dd hh24:mi:ss') ");
		sqlBuilder.append("and  r.paydate <= to_date('"+timeArr[0]+"-12-31 23:59:59','yyyy-mm-dd hh24:mi:ss') ");
        sqlBuilder.append("and s.isconfrim = '9' ");
        sqlBuilder.append("and s.isjoin = '9' ");
		sqlBuilder.append("group by e.expertid,e.name,e.idcard,e.userphone ");
		sqlBuilder.append("having sum(s.reviewcost)>0 ) t ");
		sqlBuilder.append("group by t.name,t.idcard,t.userphone ");
		//List<ExpertCostCountDto> expertCostCountDtoList = expertCostCountRepo.findBySql(sqlBuilder);
		List<Map> expertCostCountDtoList = expertCostCountRepo.findMapListBySql(sqlBuilder);
		List<ExpertCostCountDto> expertCostCountDtoList1 = new ArrayList<ExpertCostCountDto>();
		if (expertCostCountDtoList.size() > 0) {
			for (int i = 0; i < expertCostCountDtoList.size(); i++) {
				Object obj = expertCostCountDtoList.get(i);
				Object[] expertCostCounts = (Object[]) obj;
				ExpertCostCountDto expertCostCountDto = new ExpertCostCountDto();
				if (null != expertCostCounts[0]) {
					expertCostCountDto.setName((String) expertCostCounts[0]);
				}
				if (null != expertCostCounts[1]) {
					expertCostCountDto.setIdCard((String) expertCostCounts[1]);
				}
				if (null != expertCostCounts[2]) {
					expertCostCountDto.setUserPhone((String) expertCostCounts[2]);
				}
				if (null != expertCostCounts[3]) {
					expertCostCountDto.setReviewcost((BigDecimal) expertCostCounts[3]);
				}
				if (null != expertCostCounts[4]) {
					expertCostCountDto.setReviewtaxes((BigDecimal) expertCostCounts[4]);
				}
				if (null != expertCostCounts[5]) {
					expertCostCountDto.setYreviewcost((BigDecimal) expertCostCounts[5]);
				}
				if (null != expertCostCounts[6]) {
					expertCostCountDto.setYreviewtaxes((BigDecimal) expertCostCounts[6]);
				}

				expertCostCountDtoList1.add(expertCostCountDto);
			}
		}
		resultMap.put("expertCostTotalInfo", expertCostCountDtoList1);
		return new ResultMsg(true, Constant.MsgCode.OK.getValue(), "查询数据成功", resultMap);
	}

    /**
     * 专家评审费明细汇总
     * @param expertCostDetailCountDto
     * @return
     */
	public ResultMsg expertCostDetailTotal(ExpertCostDetailCountDto expertCostDetailCountDto) {
		Map<String, Object> resultMap = new HashMap<>();
		PageModelDto<ExpertCostCountDto> pageModelDto = new PageModelDto<ExpertCostCountDto>();
		String beginTime = "";
		String endTime = "";
		HqlBuilder sqlBuilder = HqlBuilder.create();
		sqlBuilder.append("select e.expertid,e.name,e.expertno,nvl(sum(s.reviewcost),0)reviewcost,nvl(sum(s.reviewtaxes),0)reviewtaxes,nvl(sum(s.totalcost),0)total ");
		sqlBuilder.append(" from cs_expert_selected s ");
		sqlBuilder.append("left join cs_expert e ");
		sqlBuilder.append("on s.expertid = e.expertid ");
		sqlBuilder.append(" left join cs_expert_review r ");
		sqlBuilder.append("on s.expertreviewid = r.id ");
		sqlBuilder.append("where 1=1 ");
		if(null != expertCostDetailCountDto && null != expertCostDetailCountDto.getBeginTime()){
			String[] timeArr = expertCostDetailCountDto.getBeginTime().split("-");
			String day = DateUtils.getMaxDayOfMonth(Integer.parseInt(timeArr[0]),(Integer.parseInt(timeArr[1])-1))+"";
			 beginTime = expertCostDetailCountDto.getBeginTime()+"-01 00:00:00";
			 endTime = expertCostDetailCountDto.getBeginTime()+"-"+day+" 23:59:59";
            sqlBuilder.append(" and r.paydate is not null  ");
			sqlBuilder.append("and r.paydate >= to_date('"+beginTime+"', 'yyyy-mm-dd hh24:mi:ss') ");
			sqlBuilder.append(" and r.paydate <= to_date('"+endTime+"', 'yyyy-mm-dd hh24:mi:ss') ");
		}
		sqlBuilder.append("and s.isconfrim ='9' ");
		sqlBuilder.append("and s.isjoin ='9' ");
		sqlBuilder.append("group by e.expertid,e.name, e.expertno ");
		sqlBuilder.append("having sum(s.reviewcost) > 0 ");
		sqlBuilder.append("order by e.expertno  ");
		List<Map> expertCostCountDtoList = expertCostCountRepo.findMapListBySql(sqlBuilder);
		List<ExpertCostCountDto> expertCostCountDtoList1 = new ArrayList<ExpertCostCountDto>();
		List<ExpertCostDetailCountDto> expertCostDetailCountDtoList = new ArrayList<>();
		if (expertCostCountDtoList.size() > 0) {
			for (int i = 0; i < expertCostCountDtoList.size(); i++) {
				Object obj = expertCostCountDtoList.get(i);
				Object[] expertCostCounts = (Object[]) obj;
				ExpertCostCountDto expertCostCountDto = new ExpertCostCountDto();
				if (null != expertCostCounts[0]) {
					expertCostCountDto.setExpertId((String) expertCostCounts[0]);
				}else{
                    expertCostCountDto.setExpertId(null);
                }
				if (null != expertCostCounts[1]) {
					expertCostCountDto.setName((String) expertCostCounts[1]);
				}else{
                    expertCostCountDto.setName(null);
                }
				if (null != expertCostCounts[2]) {
					expertCostCountDto.setExpertNo((String) expertCostCounts[2]);
				}else{
                    expertCostCountDto.setExpertNo(null);
                }
				if (null != expertCostCounts[3]) {
					expertCostCountDto.setReviewcost((BigDecimal) expertCostCounts[3]);
				}
				if (null != expertCostCounts[4]) {
					expertCostCountDto.setReviewtaxes((BigDecimal) expertCostCounts[4]);
				}
				if (null != expertCostCounts[5]) {
					expertCostCountDto.setMonthTotal((BigDecimal) expertCostCounts[5]);
				}

				if (null != expertCostCountDto.getExpertId()) {
					expertCostDetailCountDtoList = getExpertCostDetailById(expertCostCountDto.getExpertId(),beginTime,endTime);
				}
				if (expertCostDetailCountDtoList.size() > 0) {
					expertCostCountDto.setExpertCostDetailCountDtoList(expertCostDetailCountDtoList);
				}
				expertCostCountDtoList1.add(expertCostCountDto);
			}
		}
		resultMap.put("expertCostTotalInfo", expertCostCountDtoList1);
		return new ResultMsg(true, Constant.MsgCode.OK.getValue(), "查询数据成功", resultMap);
	}

	@Override
	public List<ExpertCostDetailCountDto> getExpertCostDetailById(String expertId,String beginTime,String endTime) {
		Map<String, Object> resultMap = new HashMap<>();
		HqlBuilder sqlBuilder = HqlBuilder.create();
		sqlBuilder.append("select e.expertid,e.name,e.expertno,r.reviewtitle,p.reviewtype,nvl(r.reviewdate,'')reviewdate,nvl(s.reviewcost,0)reviewcost,nvl(s.reviewtaxes,0)reviewtaxes  ");
		sqlBuilder.append("from cs_expert_selected s ");
		sqlBuilder.append("left join cs_expert e ");
		sqlBuilder.append("on s.expertid = e.expertid ");
		sqlBuilder.append("left join cs_expert_review r  ");
		sqlBuilder.append("on s.expertreviewid = r.id ");
		sqlBuilder.append("left join cs_work_program p ");
		sqlBuilder.append("on  s.businessid = p.id ");
		sqlBuilder.append("where 1 = 1 ");
		sqlBuilder.append("and e.expertid = '" + expertId + "' ");
        sqlBuilder.append(" and r.paydate is not null ");
		sqlBuilder.append("and r.paydate >= to_date('"+beginTime+"', 'yyyy-mm-dd hh24:mi:ss') ");
		sqlBuilder.append(" and r.paydate <= to_date('"+endTime+"', 'yyyy-mm-dd hh24:mi:ss') ");
		sqlBuilder.append("and s.isconfrim ='9' ");
		sqlBuilder.append("and s.isjoin ='9' ");
		sqlBuilder.append("and s.reviewcost > 0  ");
		sqlBuilder.append("order by e.expertno  ");
		List<Map> expertCostCountDetailDtoList = expertCostCountRepo.findMapListBySql(sqlBuilder);
		List<ExpertCostDetailCountDto> expertCostCountDetailDtoList1 = new ArrayList<ExpertCostDetailCountDto>();
		if (expertCostCountDetailDtoList.size() > 0) {
			for (int i = 0; i < expertCostCountDetailDtoList.size(); i++) {
				Object obj = expertCostCountDetailDtoList.get(i);
				Object[] expertCostDetailCounts = (Object[]) obj;
				ExpertCostDetailCountDto expertCostDetailCountDto = new ExpertCostDetailCountDto();
				if (null != expertCostDetailCounts[0]) {
					expertCostDetailCountDto.setExpertId((String) expertCostDetailCounts[0]);
				}else{
                    expertCostDetailCountDto.setExpertId(null);
                }
				if (null != expertCostDetailCounts[1]) {
					expertCostDetailCountDto.setName((String) expertCostDetailCounts[1]);
				}else{
                    expertCostDetailCountDto.setName(null);
                }
				if (null != expertCostDetailCounts[2]) {
					expertCostDetailCountDto.setExpertNo((String) expertCostDetailCounts[2]);
				}else{
                    expertCostDetailCountDto.setExpertNo(null);
                }
				if (null != expertCostDetailCounts[3]) {
					expertCostDetailCountDto.setReviewTitle((String) expertCostDetailCounts[3]);
				}else{
                    expertCostDetailCountDto.setReviewTitle(null);
                }
				if (null != expertCostDetailCounts[4]) {
					expertCostDetailCountDto.setReviewType((String) expertCostDetailCounts[4]);
				}else{
                    expertCostDetailCountDto.setReviewType(null);
                }
				if (null != expertCostDetailCounts[5]) {
					expertCostDetailCountDto.setReviewDate((Date) expertCostDetailCounts[5]);
				}else{
                    expertCostDetailCountDto.setReviewDate(null);
                }
				if (null != expertCostDetailCounts[6]) {
					expertCostDetailCountDto.setReviewcost((BigDecimal) expertCostDetailCounts[6]);
				}
				if (null != expertCostDetailCounts[7]) {
					expertCostDetailCountDto.setReviewtaxes((BigDecimal) expertCostDetailCounts[7]);
				}
				expertCostCountDetailDtoList1.add(expertCostDetailCountDto);
			}

		}
		return expertCostCountDetailDtoList1;
	}

	@Override
	public ResultMsg projectReviewCost(ProjectReviewCostDto projectReviewCostDto) {
		Map<String, Object> resultMap = new HashMap<>();
		PageModelDto<ProjectReviewCostDto> pageModelDto = new PageModelDto<ProjectReviewCostDto>();
		HqlBuilder sqlBuilder = HqlBuilder.create();
		sqlBuilder.append("select s.projectcode,s.projectname,s.builtcompanyname,s.reviewstage,r.totalcost,r.paydate,d.declarevalue,d.authorizevalue,s.signdate,r.businessid from cs_sign s  ");
		sqlBuilder.append(" left join cs_expert_review r  ");
		sqlBuilder.append("on s.signid = r.businessid  ");
		sqlBuilder.append("left join cs_dispatch_doc d  ");
		sqlBuilder.append("on s.signid = d.signid  ");
		//sqlBuilder.append("where r.totalcost is not null ");
		//todo:添加查询条件
		if(null != projectReviewCostDto){

		}
		List<Map> projectReviewCostList = expertCostCountRepo.findMapListBySql(sqlBuilder);
		List<ProjectReviewCostDto> projectReviewCostDtoList = new ArrayList<ProjectReviewCostDto>();
		List<FinancialManagerDto> financialManagerDtoList = new ArrayList<FinancialManagerDto>();
		if (projectReviewCostList.size() > 0) {
			for (int i = 0; i < projectReviewCostList.size(); i++) {
				Object obj = projectReviewCostList.get(i);
				Object[] projectReviewCost = (Object[]) obj;
				ProjectReviewCostDto projectReviewCostDto1 = new ProjectReviewCostDto();
				if (null != projectReviewCost[0]) {
					projectReviewCostDto1.setProjectcode((String) projectReviewCost[0]);
				}

				if (null != projectReviewCost[1]) {
					projectReviewCostDto1.setProjectname((String) projectReviewCost[1]);
				}else{
					projectReviewCostDto1.setProjectname(null);
				}
				if (null != projectReviewCost[2]) {
					projectReviewCostDto1.setBuiltcompanyname((String) projectReviewCost[2]);
				}else{
					projectReviewCostDto1.setBuiltcompanyname(null);
				}
				if (null != projectReviewCost[3]) {
					projectReviewCostDto1.setReviewstage((String) projectReviewCost[3]);
				}
				if (null != projectReviewCost[4]) {
					projectReviewCostDto1.setTotalCost((BigDecimal) projectReviewCost[4]);
				}
				if (null != projectReviewCost[5]) {
					projectReviewCostDto1.setPayDate((Date) projectReviewCost[5]);
				}
				if (null != projectReviewCost[6]) {
					projectReviewCostDto1.setDeclareValue((BigDecimal) projectReviewCost[6]);
				}
				if (null != projectReviewCost[7]) {
					projectReviewCostDto1.setAuthorizeValue((BigDecimal) projectReviewCost[7]);
				}
				if (null != projectReviewCost[8]) {
					projectReviewCostDto1.setSigndate((Date) projectReviewCost[8]);
				}
				if (null != projectReviewCost[9]) {
					projectReviewCostDto1.setBusinessId((String) projectReviewCost[9]);
				}

				if (null != projectReviewCostDto1.getBusinessId()) {
					financialManagerDtoList = getFinancialManagerByBusid(projectReviewCostDto1.getBusinessId());
					Integer totalCost = financialManagerService.sunCount(projectReviewCostDto1.getBusinessId());
					if (null != totalCost){
						projectReviewCostDto1.setTotalCost(BigDecimal.valueOf(totalCost));
					}
				}

				if (financialManagerDtoList.size() > 0) {
					projectReviewCostDto1.setFinancialManagerDtoList(financialManagerDtoList);
				}
				projectReviewCostDtoList.add(projectReviewCostDto1);
			}
		}
		resultMap.put("projectReviewCostDtoList", projectReviewCostDtoList);
		return new ResultMsg(true, Constant.MsgCode.OK.getValue(), "查询数据成功", resultMap);
	}

	@Override
	public List<FinancialManagerDto> getFinancialManagerByBusid(String businessId) {
		HqlBuilder hqlBuilder = HqlBuilder.create();
		hqlBuilder.append(" from "+FinancialManager.class.getSimpleName() + " where "+ FinancialManager_.businessId.getName()+ " =:businessId");
		hqlBuilder.setParam("businessId", businessId);
		List<FinancialManager> financialManagerlist= financialManagerRepo.findByHql(hqlBuilder);
		List<FinancialManagerDto> financialManagerDtoList = new ArrayList<FinancialManagerDto>();
		if(financialManagerlist.size()>0){
			for (FinancialManager financialManager:financialManagerlist){
				FinancialManagerDto financialManagerDto = new FinancialManagerDto();
				BeanCopierUtils.copyProperties(financialManager,financialManagerDto);
				financialManagerDtoList.add(financialManagerDto);
			}
		}
		return financialManagerDtoList;
	}

}