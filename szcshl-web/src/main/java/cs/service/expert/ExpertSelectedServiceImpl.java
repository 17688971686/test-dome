package cs.service.expert;

import cs.common.Constant;
import cs.common.HqlBuilder;
import cs.common.ResultMsg;
import cs.common.utils.*;
import cs.domain.expert.*;
import cs.domain.financial.FinancialManager;
import cs.domain.financial.FinancialManager_;
import cs.domain.sys.User;
import cs.model.PageModelDto;
import cs.model.expert.*;
import cs.model.financial.FinancialManagerDto;
import cs.repository.odata.ODataFilterItem;
import cs.repository.odata.ODataObj;
import cs.repository.odata.ODataObjFilterStrategy;
import cs.repository.repositoryImpl.expert.*;
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
public class ExpertSelectedServiceImpl implements ExpertSelectedService {

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

    @Autowired
    private ExpertRepo expertRepo;



    @Override
    @Transactional
    public void save(ExpertSelectedDto record) {
        ExpertSelected domain = new ExpertSelected();
        BeanCopierUtils.copyProperties(record, domain);

        expertSelectedRepo.save(domain);
    }

    @Override
    @Transactional
    public ResultMsg update(ExpertSelectedDto record) {
        boolean isUpdateScore = false;
        if (!Validate.isObject(record.getScore()) || record.getScore() <= 0) {
            return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), "操作失败，你还没对专家进行评分！");
        }
        ExpertSelected domain = expertSelectedRepo.findById(record.getId());
        if (!record.getScore().equals(domain.getScore())) {
            isUpdateScore = true;
        }
        BeanCopierUtils.copyPropertiesIgnoreNull(record, domain);
        expertSelectedRepo.save(domain);

        if (isUpdateScore) {
            //计算综合评分（根据有评分总数，除以评分次数，没有评分的次数不算）
            expertRepo.updateExpertCompositeScore(domain.getExpert().getExpertID());
        }
        return new ResultMsg(true, Constant.MsgCode.OK.getValue(), "操作成功！");
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
        String beginTime = "", endTime = "", expertName = "";
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
                if ("name".equals(item.getField())) {
                    expertName = item.getValue().toString();
                    continue;
                }
                if ("beginTime".equals(item.getField())) {
                    beginTime = item.getValue().toString();
                    bTime = DateUtils.converToDate1(beginTime, "EEE MMM dd HH:mm:ss Z yyyy");
                    beginTime = DateUtils.converToString(bTime, "yyyy-MM-dd");
                    continue;
                }
                if ("endTime".equals(item.getField())) {
                    endTime = item.getValue().toString();
                    eTime = DateUtils.converToDate1(endTime, "EEE MMM dd HH:mm:ss Z yyyy");
                    endTime = DateUtils.converToString(eTime, "yyyy-MM-dd");
                    continue;
                }
                criteria.add(ODataObjFilterStrategy.getStrategy(item.getOperator()).getCriterion(item.getField(), value));
            }

        }
        if (beginTime != null || endTime != null || expertName != null) {
            StringBuffer sqlSB = new StringBuffer();
            sqlSB.append(" (select count(t.id) from cs_expert_review t  where t.id = " + criteria.getAlias() + "_.EXPERTREVIEWID ");
            if (Validate.isString(beginTime)) {
                sqlSB.append(" and t.paydate >= to_date('" + beginTime + "', 'yyyy-mm-dd')");
            }
            if (Validate.isString(endTime)) {
                sqlSB.append(" and t.paydate <= to_date('" + endTime + "', 'yyyy-mm-dd')");
            }

            sqlSB.append(" and " + criteria.getAlias() + "_.ISCONFRIM='9'");
            sqlSB.append(" and " + criteria.getAlias() + "_.ISJOIN='9'");

            sqlSB.append(" ) > 0 ");
            if (Validate.isString(expertName)) {
                sqlSB.append(" and (select count(e.expertID) from cs_expert e where e.expertID = " + criteria.getAlias() + "_.expertId and e.name  like '%" + expertName + "%' )>0");
            }
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
        if (Validate.isList(resultList)) {
            resultList.forEach(x -> {
                ExpertSelectedDto modelDto = new ExpertSelectedDto();
                BeanCopierUtils.copyProperties(x, modelDto);
                modelDto.setExpertDto(new ExpertDto());
                modelDto.setExpertReviewDto(new ExpertReviewDto());
                if (null != x.getExpert()) {
                    BeanCopierUtils.copyProperties(x.getExpert(), modelDto.getExpertDto());
                }
                if (null != x.getExpertReview()) {
                    BeanCopierUtils.copyProperties(x.getExpertReview(), modelDto.getExpertReviewDto());
                }
                User u = signPrincipalService.getMainPriUser(x.getExpertReview().getBusinessId());
                if (null != u) {
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
     * @author ldm 修改月2018-03
     SELECT e.name, e.idcard, e.userphone, me.reviewcost AS reviewcost, me.reviewtaxes AS reviewtaxes, ye.reviewcost AS yreviewcost, ye.reviewtaxes AS yreviewtaxes
     FROM (  SELECT s.EXPERTID, SUM (s.reviewcost) reviewcost, SUM (s.reviewtaxes) reviewtaxes
     FROM cs_expert_selected s, cs_expert_review r
     WHERE s.expertreviewid = r.id AND s.isconfrim = '9' AND s.isjoin = '9' AND r.paydate IS NOT NULL
     AND TO_CHAR (r.paydate, 'yyyy-mm') = '2017-03' GROUP BY s.expertid) me,
     cs_expert e,
     (  SELECT s.EXPERTID, SUM (s.reviewcost) reviewcost, SUM (s.reviewtaxes) reviewtaxes
     FROM cs_expert_selected s, cs_expert_review r
     WHERE s.expertreviewid = r.id AND s.isconfrim = '9' AND s.isjoin = '9' AND r.paydate IS NOT NULL
     AND TO_CHAR (r.paydate, 'yyyy') = '2017' GROUP BY s.expertid) ye
     where me.EXPERTID = e.EXPERTID and me.EXPERTID = ye.EXPERTID
     * @param
     * @return
     */
    @Override
    public ResultMsg expertCostTotal(ExpertCostCountDto expertCostDto) {
        Map<String, Object> resultMap = new HashMap<>();
        if(expertCostDto == null){
            expertCostDto = new ExpertCostCountDto();
        }
        if(!Validate.isString(expertCostDto.getYear())){
            expertCostDto.setYear(DateUtils.getNowYear());
        }
        if(!Validate.isString(expertCostDto.getMonth())){
            expertCostDto.setMonth(String.valueOf(DateUtils.getCurMonth()));
        }
        String monthValue = String.format("%02d", Integer.parseInt(expertCostDto.getMonth()) );
        HqlBuilder sqlBuilder = HqlBuilder.create();
        sqlBuilder.append(" SELECT e.name, e.idcard, e.userphone, me.reviewcost AS reviewcost, me.reviewtaxes AS reviewtaxes, ye.reviewcost AS yreviewcost, ye.reviewtaxes AS yreviewtaxes ");
        sqlBuilder.append(" FROM (  SELECT s.EXPERTID, SUM (s.reviewcost) reviewcost, SUM (s.reviewtaxes) reviewtaxes ");
        sqlBuilder.append(" FROM cs_expert_selected s, cs_expert_review r ");
        sqlBuilder.append(" WHERE s.expertreviewid = r.id AND s.isconfrim = '9' AND s.isjoin = '9' AND r.paydate IS NOT NULL ");
        sqlBuilder.append(" AND TO_CHAR (r.paydate, 'yyyy-mm') = :monthValue GROUP BY s.expertid) me, ");
        sqlBuilder.setParam("monthValue",expertCostDto.getYear()+"-"+monthValue);
        sqlBuilder.append(" cs_expert e, ");
        sqlBuilder.append(" (  SELECT s.EXPERTID, SUM (s.reviewcost) reviewcost, SUM (s.reviewtaxes) reviewtaxes ");
        sqlBuilder.append(" FROM cs_expert_selected s, cs_expert_review r ");
        sqlBuilder.append(" WHERE s.expertreviewid = r.id AND s.isconfrim = '9' AND s.isjoin = '9' AND r.paydate IS NOT NULL ");
        sqlBuilder.append(" AND TO_CHAR (r.paydate, 'yyyy') = :yearValue GROUP BY s.expertid) ye ");
        sqlBuilder.setParam("yearValue",expertCostDto.getYear());
        sqlBuilder.append(" where me.EXPERTID = e.EXPERTID and me.EXPERTID = ye.EXPERTID order by E.EXPERTNO ");

        List<Object[]> expertCostCountDtoList = expertCostCountRepo.getObjectArray(sqlBuilder);
        BigDecimal tMonthReviewcost = BigDecimal.ZERO,tMonthReviewtaxes = BigDecimal.ZERO,
                tYearReviewcost = BigDecimal.ZERO,tYearReviewtaxes = BigDecimal.ZERO;
        List<ExpertCostCountDto> expertCostCountDtoList1 = new ArrayList<ExpertCostCountDto>();
        if (expertCostCountDtoList.size() > 0) {
            for (int i = 0; i < expertCostCountDtoList.size(); i++) {
                Object[] expertCostCounts = expertCostCountDtoList.get(i);
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
                    tMonthReviewcost = Arith.safeAdd(tMonthReviewcost,expertCostCountDto.getReviewcost());
                }
                if (null != expertCostCounts[4]) {
                    expertCostCountDto.setReviewtaxes((BigDecimal) expertCostCounts[4]);
                    tMonthReviewtaxes = Arith.safeAdd(tMonthReviewtaxes,expertCostCountDto.getReviewtaxes());
                }
                if (null != expertCostCounts[5]) {
                    expertCostCountDto.setYreviewcost((BigDecimal) expertCostCounts[5]);
                    tYearReviewcost = Arith.safeAdd(tYearReviewcost,expertCostCountDto.getYreviewcost());
                }
                if (null != expertCostCounts[6]) {
                    expertCostCountDto.setYreviewtaxes((BigDecimal) expertCostCounts[6]);
                    tYearReviewtaxes = Arith.safeAdd(tYearReviewtaxes,expertCostCountDto.getYreviewtaxes());
                }
                expertCostCountDtoList1.add(expertCostCountDto);
            }
        }
        resultMap.put("expertCostTotalInfo", expertCostCountDtoList1);
        resultMap.put("tMonthReviewcost", tMonthReviewcost);
        resultMap.put("tMonthReviewtaxes", tMonthReviewtaxes);
        resultMap.put("tYearReviewcost", tYearReviewcost);
        resultMap.put("tYearReviewtaxes", tYearReviewtaxes);

        return new ResultMsg(true, Constant.MsgCode.OK.getValue(), "查询数据成功", resultMap);
    }

    /**
     * 专家评审费明细汇总
     *
     * @param expertCostDetailCountDto
     * @return
     */
    @Override
    public ResultMsg expertCostDetailTotal(ExpertCostDetailCountDto expertCostDetailCountDto) {
        Map<String, Object> resultMap = new HashMap<>();
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

        if(expertCostDetailCountDto == null){
            expertCostDetailCountDto = new ExpertCostDetailCountDto();
        }
        if(!Validate.isString(expertCostDetailCountDto.getYear())){
            expertCostDetailCountDto.setYear(DateUtils.getNowYear());
        }
        if(!Validate.isString(expertCostDetailCountDto.getMonth())){
            expertCostDetailCountDto.setMonth(String.valueOf(DateUtils.getCurMonth()));
        }
        String day = DateUtils.getMaxDayOfMonth(Integer.parseInt(expertCostDetailCountDto.getYear()),Integer.parseInt(expertCostDetailCountDto.getMonth())) + "";
        String bTime = expertCostDetailCountDto.getYear()+"-"+expertCostDetailCountDto.getMonth()+ "-01 00:00:00";
        String eTime = expertCostDetailCountDto.getYear()+"-"+expertCostDetailCountDto.getMonth()+ "-" + day + " 23:59:59";

        sqlBuilder.append(" and r.paydate is not null  ");
        sqlBuilder.append("and r.paydate >= to_date('" + bTime + "', 'yyyy-mm-dd hh24:mi:ss') ");
        sqlBuilder.append(" and r.paydate <= to_date('" + eTime + "', 'yyyy-mm-dd hh24:mi:ss') ");

        sqlBuilder.append("and s.isconfrim ='9' ");
        sqlBuilder.append("and s.isjoin ='9' ");
        sqlBuilder.append("group by e.expertid,e.name, e.expertno ");
        sqlBuilder.append("having sum(s.reviewcost) > 0 ");
        sqlBuilder.append("order by e.expertno  ");
        List<Object[]> expertCostCountDtoList = expertCostCountRepo.getObjectArray(sqlBuilder);
        List<ExpertCostCountDto> expertCostCountDtoList1 = new ArrayList<ExpertCostCountDto>();
        List<ExpertCostDetailCountDto> expertCostDetailCountDtoList = new ArrayList<>();
        if (expertCostCountDtoList.size() > 0) {
            for (int i = 0; i < expertCostCountDtoList.size(); i++) {
                Object[] expertCostCounts = expertCostCountDtoList.get(i);
                ExpertCostCountDto expertCostCountDto = new ExpertCostCountDto();
                if (null != expertCostCounts[0]) {
                    expertCostCountDto.setExpertId((String) expertCostCounts[0]);
                } else {
                    expertCostCountDto.setExpertId(null);
                }
                if (null != expertCostCounts[1]) {
                    expertCostCountDto.setName((String) expertCostCounts[1]);
                } else {
                    expertCostCountDto.setName(null);
                }
                if (null != expertCostCounts[2]) {
                    expertCostCountDto.setExpertNo((String) expertCostCounts[2]);
                } else {
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
                    expertCostDetailCountDtoList = getExpertCostDetailById(expertCostCountDto.getExpertId(), beginTime, endTime);
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
    public List<ExpertCostDetailCountDto> getExpertCostDetailById(String expertId, String beginTime, String endTime) {
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
        sqlBuilder.append("and r.paydate >= to_date('" + beginTime + "', 'yyyy-mm-dd hh24:mi:ss') ");
        sqlBuilder.append(" and r.paydate <= to_date('" + endTime + "', 'yyyy-mm-dd hh24:mi:ss') ");
        sqlBuilder.append("and s.isconfrim ='9' ");
        sqlBuilder.append("and s.isjoin ='9' ");
        sqlBuilder.append("and s.reviewcost > 0  ");
        sqlBuilder.append("order by e.expertno  ");
        List<Object[]> expertCostCountDetailDtoList = expertCostCountRepo.getObjectArray(sqlBuilder);
        List<ExpertCostDetailCountDto> expertCostCountDetailDtoList1 = new ArrayList<ExpertCostDetailCountDto>();
        if (expertCostCountDetailDtoList.size() > 0) {
            for (int i = 0; i < expertCostCountDetailDtoList.size(); i++) {
                Object[] expertCostDetailCounts = expertCostCountDetailDtoList.get(i);
                ExpertCostDetailCountDto expertCostDetailCountDto = new ExpertCostDetailCountDto();
                if (null != expertCostDetailCounts[0]) {
                    expertCostDetailCountDto.setExpertId((String) expertCostDetailCounts[0]);
                } else {
                    expertCostDetailCountDto.setExpertId(null);
                }
                if (null != expertCostDetailCounts[1]) {
                    expertCostDetailCountDto.setName((String) expertCostDetailCounts[1]);
                } else {
                    expertCostDetailCountDto.setName(null);
                }
                if (null != expertCostDetailCounts[2]) {
                    expertCostDetailCountDto.setExpertNo((String) expertCostDetailCounts[2]);
                } else {
                    expertCostDetailCountDto.setExpertNo(null);
                }
                if (null != expertCostDetailCounts[3]) {
                    expertCostDetailCountDto.setReviewTitle((String) expertCostDetailCounts[3]);
                } else {
                    expertCostDetailCountDto.setReviewTitle(null);
                }
                if (null != expertCostDetailCounts[4]) {
                    expertCostDetailCountDto.setReviewType((String) expertCostDetailCounts[4]);
                } else {
                    expertCostDetailCountDto.setReviewType(null);
                }
                if (null != expertCostDetailCounts[5]) {
                    expertCostDetailCountDto.setReviewDate((Date) expertCostDetailCounts[5]);
                } else {
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

    /**
     * 项目评审费用统计
     */
    @Override
    public ResultMsg projectReviewCost(ProjectReviewCostDto projectReviewCostDto) {
        Map<String, Object> resultMap = new HashMap<>();
        HqlBuilder sqlBuilder = HqlBuilder.create();
        sqlBuilder.append("select s.projectcode,s.projectname,s.builtcompanyname,s.reviewstage,r.totalcost,r.paydate,d.declarevalue,d.authorizevalue,s.signdate,r.businessid from cs_sign s  ");
        sqlBuilder.append(" left join cs_expert_review r  ");
        sqlBuilder.append("on s.signid = r.businessid  ");
        sqlBuilder.append("left join cs_dispatch_doc d  ");
        sqlBuilder.append("on s.signid = d.signid  ");
        sqlBuilder.append("LEFT JOIN ( SELECT o.id oid, o.name oname, B.SIGNID bsignid FROM V_ORG_DEPT o, CS_SIGN_BRANCH b  WHERE O.ID = B.ORGID AND B.ISMAINBRABCH = '9') mo  ");
        sqlBuilder.append("ON s.signid = mo.bsignid  ");
        sqlBuilder.append("where r.paydate is not null ");
        //添加过滤条件。过滤删除
        sqlBuilder.append("and s.signState <> '" + Constant.EnumState.DELETE.getValue() + "' ");
        //todo:添加查询条件
        if (null != projectReviewCostDto) {
            if (StringUtil.isNotEmpty(projectReviewCostDto.getProjectname())) {
                sqlBuilder.append("and s.projectname like '%" + projectReviewCostDto.getProjectname() + "%' ");
            }

            if (StringUtil.isNotEmpty(projectReviewCostDto.getBuiltcompanyname())) {
                sqlBuilder.append("and s.builtcompanyname like '%" + projectReviewCostDto.getBuiltcompanyname() + "%' ");
            }

            if (StringUtil.isNotEmpty(projectReviewCostDto.getReviewstage())) {
                sqlBuilder.append("and s.reviewstage = '" + projectReviewCostDto.getReviewstage() + "' ");
            }


            if (StringUtil.isNotEmpty(projectReviewCostDto.getBeginTime())) {
                sqlBuilder.append("and r.paydate >= to_date('" + projectReviewCostDto.getBeginTime() + "', 'yyyy-mm-dd hh24:mi:ss') ");
            }

            if (StringUtil.isNotEmpty(projectReviewCostDto.getEndTime())) {
                sqlBuilder.append("and r.paydate <= to_date('" + projectReviewCostDto.getEndTime() + "', 'yyyy-mm-dd hh24:mi:ss') ");
            }

            if (StringUtil.isNotEmpty(projectReviewCostDto.getDeptName())) {
                sqlBuilder.append("and mo.oname = '" + projectReviewCostDto.getDeptName() + "' ");
            }
        }
        List<Object[]> projectReviewCostList = expertCostCountRepo.getObjectArray(sqlBuilder);
        List<ProjectReviewCostDto> projectReviewCostDtoList = new ArrayList<ProjectReviewCostDto>();
        List<FinancialManagerDto> financialManagerDtoList = new ArrayList<FinancialManagerDto>();
        if (projectReviewCostList.size() > 0) {
            for (int i = 0; i < projectReviewCostList.size(); i++) {
                Object[] projectReviewCost = projectReviewCostList.get(i);
                ProjectReviewCostDto projectReviewCostDto1 = new ProjectReviewCostDto();
                if (null != projectReviewCost[0]) {
                    projectReviewCostDto1.setProjectcode((String) projectReviewCost[0]);
                }

                if (null != projectReviewCost[1]) {
                    projectReviewCostDto1.setProjectname((String) projectReviewCost[1]);
                } else {
                    projectReviewCostDto1.setProjectname(null);
                }
                if (null != projectReviewCost[2]) {
                    projectReviewCostDto1.setBuiltcompanyname((String) projectReviewCost[2]);
                } else {
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
                    BigDecimal totalCost = financialManagerService.sunCount(projectReviewCostDto1.getBusinessId());
                    if (null != totalCost) {
                        projectReviewCostDto1.setTotalCost(totalCost);
                    }
                }

                if (financialManagerDtoList.size() > 0) {
                    projectReviewCostDto1.setFinancialManagerDtoList(financialManagerDtoList);
                }
                User u = signPrincipalService.getMainPriUser(projectReviewCostDto1.getBusinessId());
                if (null != u) {
                    projectReviewCostDto1.setPrincipal(u.getDisplayName());
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
        hqlBuilder.append(" from " + FinancialManager.class.getSimpleName() + " where " + FinancialManager_.businessId.getName() + " =:businessId");
        hqlBuilder.setParam("businessId", businessId);
        List<FinancialManager> financialManagerlist = financialManagerRepo.findByHql(hqlBuilder);
        List<FinancialManagerDto> financialManagerDtoList = new ArrayList<FinancialManagerDto>();
        if (financialManagerlist.size() > 0) {
            for (FinancialManager financialManager : financialManagerlist) {
                FinancialManagerDto financialManagerDto = new FinancialManagerDto();
                BeanCopierUtils.copyProperties(financialManager, financialManagerDto);
                financialManagerDtoList.add(financialManagerDto);
            }
        }
        return financialManagerDtoList;
    }

    /**
     * 项目评审费分类统计
     *
     * @param projectReviewCostDto
     * @return
     */
    @Override
    public ResultMsg proReviewClassifyCount(ProjectReviewCostDto projectReviewCostDto, int page) {
        Map<String, Object> resultMap = new HashMap<>();
        HqlBuilder sqlBuilder = HqlBuilder.create();
        HqlBuilder sqlBuilder1 = HqlBuilder.create();
        sqlBuilder.append("select * from (select a.* , rownum rn from (");
        sqlBuilder.append("select s.projectcode,s.projectname,s.builtcompanyname,s.reviewstage,r.totalcost,r.paydate,d.declarevalue,d.authorizevalue,s.signdate,r.businessid,f.chargename,f.charge from cs_sign s  ");
        sqlBuilder.append(" left join cs_expert_review r  ");
        sqlBuilder.append("on s.signid = r.businessid  ");
        sqlBuilder.append("left join cs_dispatch_doc d  ");
        sqlBuilder.append("on s.signid = d.signid  ");
        sqlBuilder.append("left join cs_financial_manager f  ");
        sqlBuilder.append("on s.signid = f.businessid  ");
        sqlBuilder.append("LEFT JOIN ( SELECT o.id oid, o.name oname, B.SIGNID bsignid FROM V_ORG_DEPT o, CS_SIGN_BRANCH b  WHERE O.ID = B.ORGID AND B.ISMAINBRABCH = '9') mo  ");
        sqlBuilder.append("ON s.signid = mo.bsignid  ");
        sqlBuilder.append("where r.paydate is not null  ");
        sqlBuilder.append("and f.chargename is not null ");

        sqlBuilder1.append("select f.chargename,sum(f.charge) from cs_sign s  ");
        sqlBuilder1.append("left join cs_expert_review r  ");
        sqlBuilder1.append("on s.signid = r.businessid  ");
        sqlBuilder1.append("left join cs_dispatch_doc d  ");
        sqlBuilder1.append("on s.signid = d.signid  ");
        sqlBuilder1.append("left join cs_financial_manager f  ");
        sqlBuilder1.append("on s.signid = f.businessid  ");
        sqlBuilder1.append("LEFT JOIN ( SELECT o.id oid, o.name oname, B.SIGNID bsignid FROM V_ORG_DEPT o, CS_SIGN_BRANCH b  WHERE O.ID = B.ORGID AND B.ISMAINBRABCH = '9') mo  ");
        sqlBuilder1.append("ON s.signid = mo.bsignid  ");
        sqlBuilder1.append("where r.paydate is not null  ");
        sqlBuilder1.append("and f.chargename is not null  ");
        if (null != projectReviewCostDto) {

            if (StringUtil.isNotEmpty(projectReviewCostDto.getChargeName())) {
                //sqlBuilder.append("and f.chargename = '"+projectReviewCostDto.getChargeName()+"' ");
                sqlBuilder1.append("and f.chargename = '" + projectReviewCostDto.getChargeName() + "' ");
            }

            if (StringUtil.isNotEmpty(projectReviewCostDto.getProjectname())) {
                sqlBuilder.append("and s.projectname like '%" + projectReviewCostDto.getProjectname() + "%' ");
                sqlBuilder1.append("and s.projectname like '%" + projectReviewCostDto.getProjectname() + "%' ");
            }

            if (StringUtil.isNotEmpty(projectReviewCostDto.getBuiltcompanyname())) {
                sqlBuilder.append("and s.builtcompanyname like '%" + projectReviewCostDto.getBuiltcompanyname() + "%' ");
                sqlBuilder1.append("and s.builtcompanyname like '%" + projectReviewCostDto.getBuiltcompanyname() + "%' ");
            }

            if (StringUtil.isNotEmpty(projectReviewCostDto.getReviewstage())) {
                sqlBuilder.append("and s.reviewstage = '" + projectReviewCostDto.getReviewstage() + "' ");
                sqlBuilder1.append("and s.reviewstage = '" + projectReviewCostDto.getReviewstage() + "' ");
            }

            if (StringUtil.isNotEmpty(projectReviewCostDto.getBeginTime())) {
                sqlBuilder.append("and r.paydate >= to_date('" + projectReviewCostDto.getBeginTime() + "', 'yyyy-mm-dd hh24:mi:ss') ");
                sqlBuilder1.append("and r.paydate >= to_date('" + projectReviewCostDto.getBeginTime() + "', 'yyyy-mm-dd hh24:mi:ss') ");
            }

            if (StringUtil.isNotEmpty(projectReviewCostDto.getEndTime())) {
                sqlBuilder.append("and r.paydate <= to_date('" + projectReviewCostDto.getEndTime() + "', 'yyyy-mm-dd hh24:mi:ss') ");
                sqlBuilder1.append("and r.paydate <= to_date('" + projectReviewCostDto.getEndTime() + "', 'yyyy-mm-dd hh24:mi:ss') ");
            }

            if (StringUtil.isNotEmpty(projectReviewCostDto.getDeptName())) {
                sqlBuilder.append("and mo.oname = '" + projectReviewCostDto.getDeptName() + "' ");
                sqlBuilder1.append("and mo.oname = '" + projectReviewCostDto.getDeptName() + "' ");
            }

        }
        sqlBuilder.append(" ) a ) where rn >" + (page * 200) + " and rn <" + ((page + 1) * 200 + 1));
        sqlBuilder1.append("group by f.chargename  ");
        List<Object[]> projectReviewCostList = expertCostCountRepo.getObjectArray(sqlBuilder);
        List<Object[]> projectClassifytList = expertCostCountRepo.getObjectArray(sqlBuilder1);
        List<ProjectReviewCostDto> projectReviewCostDtoList = new ArrayList<ProjectReviewCostDto>();
        List<ProReviewClassifyCountDto> proReviewClassifyCountDtoList = new ArrayList<>();
        if (Validate.isList(projectReviewCostList)) {
            for (int i = 0; i < projectReviewCostList.size(); i++) {
                Object[] projectReviewCost = projectReviewCostList.get(i);
                ProjectReviewCostDto projectReviewCostDto1 = new ProjectReviewCostDto();
                if (null != projectReviewCost[0]) {
                    projectReviewCostDto1.setProjectcode((String) projectReviewCost[0]);
                }
                if (null != projectReviewCost[1]) {
                    projectReviewCostDto1.setProjectname((String) projectReviewCost[1]);
                } else {
                    projectReviewCostDto1.setProjectname(null);
                }
                if (null != projectReviewCost[2]) {
                    projectReviewCostDto1.setBuiltcompanyname((String) projectReviewCost[2]);
                } else {
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
                if (null != projectReviewCost[10]) {
                    projectReviewCostDto1.setChargeName((String) projectReviewCost[10]);
                }
                if (null != projectReviewCost[11]) {
                    projectReviewCostDto1.setCharge((BigDecimal) projectReviewCost[11]);
                }
            /*	if (null != projectReviewCostDto1.getBusinessId()) {
                    financialManagerDtoList = getFinancialManagerByBusid(projectReviewCostDto1.getBusinessId());

				}
				if (financialManagerDtoList.size() > 0) {
					projectReviewCostDto1.setFinancialManagerDtoList(financialManagerDtoList);
				}*/
                User u = signPrincipalService.getMainPriUser(projectReviewCostDto1.getBusinessId());
                if (null != u) {
                    projectReviewCostDto1.setPrincipal(u.getDisplayName());
                }
                projectReviewCostDtoList.add(projectReviewCostDto1);
            }
        }

        if (projectClassifytList.size() > 0) {
            for (int i = 0; i < projectClassifytList.size(); i++) {
                Object obj = projectClassifytList.get(i);
                Object[] projectClassifyCost = (Object[]) obj;
                ProReviewClassifyCountDto proReviewClassifyCountDto = new ProReviewClassifyCountDto();
                if (null != projectClassifyCost[0]) {
                    proReviewClassifyCountDto.setChargeName((String) projectClassifyCost[0]);
                } else {
                    proReviewClassifyCountDto.setChargeName(null);
                }

                if (null != projectClassifyCost[1]) {
                    proReviewClassifyCountDto.setTotalCharge((BigDecimal) projectClassifyCost[1]);
                } else {
                    proReviewClassifyCountDto.setTotalCharge(null);
                }
                proReviewClassifyCountDtoList.add(proReviewClassifyCountDto);
            }

        }
        resultMap.put("proReviewClassifyDetailDtoList", projectReviewCostDtoList);
        resultMap.put("proReviewClassifyCountDtoList", proReviewClassifyCountDtoList);
        return new ResultMsg(true, Constant.MsgCode.OK.getValue(), "查询数据成功", resultMap);
    }

    /**
     * 项目评审情况统计
     *
     * @param projectReviewConditionDto
     * @return
     */
    @Override
    public ResultMsg proReviewConditionCount(ProReviewConditionDto projectReviewConditionDto) {
        Map<String, Object> resultMap = new HashMap<>();
        HqlBuilder sqlBuilder = HqlBuilder.create();
        sqlBuilder.append("select s.reviewstage, count(s.projectcode),sum(d.declarevalue)/10000 declarevalue,sum(d.authorizevalue)/10000 authorizevalue,(sum(d.declarevalue) -sum(d.authorizevalue))/10000 ljhj, decode(sum(d.declarevalue),0,0,  round((sum(d.declarevalue) - sum(d.authorizevalue)) / 10000 / (sum(d.declarevalue) / 1000), 5) * 1000) hjl,s.isadvanced   from cs_sign s   ");
        sqlBuilder.append("left join cs_dispatch_doc d  ");
        sqlBuilder.append("on s.signid = d.signid  ");
        sqlBuilder.append("where 1 = 1 ");
        sqlBuilder.append("and s.signstate != '7' ");//过滤删除
        sqlBuilder.append("and (s.ispresign != '0' or s.ispresign is null) ");//过滤预签收的
        sqlBuilder.append("and s.processstate >= 6  ");//已发文
        sqlBuilder.append("and D.DISPATCHTYPE != '项目退文函' ");//过滤退文项目

        //todo:添加查询条件
        if (null != projectReviewConditionDto) {
            if (StringUtil.isNotEmpty(projectReviewConditionDto.getBeginTime()) && StringUtil.isNotEmpty(projectReviewConditionDto.getEndTime())) {
                String beginTime = projectReviewConditionDto.getBeginTime() + "-01 00:00:00";
                String[] timeArr = projectReviewConditionDto.getEndTime().split("-");
                String day = "";
                day = DateUtils.getMaxDayOfMonth(Integer.parseInt(timeArr[0]), (Integer.parseInt(timeArr[1]))) + "";
                String endTime = projectReviewConditionDto.getEndTime() + "-" + day + " 23:59:59";
                sqlBuilder.append("and D.DISPATCHDATE >= to_date('" + beginTime + "', 'yyyy-mm-dd hh24:mi:ss') ");
                sqlBuilder.append("and D.DISPATCHDATE <= to_date('" + endTime + "', 'yyyy-mm-dd hh24:mi:ss') ");
            } else if (StringUtil.isNotEmpty(projectReviewConditionDto.getBeginTime()) && !StringUtil.isNotEmpty(projectReviewConditionDto.getEndTime())) {
                String[] timeArr = projectReviewConditionDto.getBeginTime().split("-");
                String day = "";
                day = DateUtils.getMaxDayOfMonth(Integer.parseInt(timeArr[0]), (Integer.parseInt(timeArr[1]))) + "";
                String beginTime = projectReviewConditionDto.getBeginTime() + "-01 00:00:00";
                String endTime = projectReviewConditionDto.getBeginTime() + "-" + day + " 23:59:59";
                sqlBuilder.append("and D.DISPATCHDATE >= to_date('" + beginTime + "', 'yyyy-mm-dd hh24:mi:ss') ");
                sqlBuilder.append("and D.DISPATCHDATE <= to_date('" + endTime + "', 'yyyy-mm-dd hh24:mi:ss') ");
            } else if (StringUtil.isNotEmpty(projectReviewConditionDto.getEndTime()) && !StringUtil.isNotEmpty(projectReviewConditionDto.getBeginTime())) {
                String[] timeArr = projectReviewConditionDto.getEndTime().split("-");
                String day = "";
                day = DateUtils.getMaxDayOfMonth(Integer.parseInt(timeArr[0]), (Integer.parseInt(timeArr[1]))) + "";
                String beginTime = projectReviewConditionDto.getEndTime() + "-01 00:00:00";
                String endTime = projectReviewConditionDto.getEndTime() + "-" + day + " 23:59:59";
                sqlBuilder.append("and D.DISPATCHDATE >= to_date('" + beginTime + "', 'yyyy-mm-dd hh24:mi:ss') ");
                sqlBuilder.append("and D.DISPATCHDATE <= to_date('" + endTime + "', 'yyyy-mm-dd hh24:mi:ss') ");
            }
        }
        sqlBuilder.append("group by s.reviewstage,s.isadvanced");
        List<Object[]> projectReviewConList = expertCostCountRepo.getObjectArray(sqlBuilder);
        List<ProReviewConditionDto> projectReviewConDtoList = new ArrayList<ProReviewConditionDto>();

        if (projectReviewConList.size() > 0) {
            for (int i = 0; i < projectReviewConList.size(); i++) {
                Object[] projectReviewCon = projectReviewConList.get(i);
                ProReviewConditionDto proReviewConditionDto = new ProReviewConditionDto();
                if (null != projectReviewCon[0]) {
                    proReviewConditionDto.setReviewStage((String) projectReviewCon[0]);
                } else {
                    proReviewConditionDto.setReviewStage(null);
                }
                if (null != projectReviewCon[1]) {
                    proReviewConditionDto.setProCount((BigDecimal) projectReviewCon[1]);
                } else {
                    proReviewConditionDto.setProCount(BigDecimal.ZERO);
                }
                if (null != projectReviewCon[2]) {
                    proReviewConditionDto.setDeclareValue((BigDecimal) projectReviewCon[2]);
                    proReviewConditionDto.setDeclareStr(String.valueOf(projectReviewCon[2]));
                } else {
                    proReviewConditionDto.setDeclareValue(null);
                    proReviewConditionDto.setDeclareStr("0");
                }
                if (null != projectReviewCon[3]) {
                    proReviewConditionDto.setAuthorizeValue((BigDecimal) projectReviewCon[3]);
                    proReviewConditionDto.setAuthorizeStr(String.valueOf(projectReviewCon[3]));
                } else {
                    proReviewConditionDto.setAuthorizeStr("0");
                }
                if (null != projectReviewCon[4]) {
                    proReviewConditionDto.setLjhj((BigDecimal) projectReviewCon[4]);
                    proReviewConditionDto.setLjhjStr(String.valueOf(projectReviewCon[4]));
                } else {
                    proReviewConditionDto.setLjhj(null);
                    proReviewConditionDto.setLjhjStr("0");
                }
                if (null != projectReviewCon[5]) {
                    proReviewConditionDto.setHjl((BigDecimal) projectReviewCon[5]);
                    proReviewConditionDto.setHjlStr(String.valueOf(projectReviewCon[5]) + "%");
                } else {
                    proReviewConditionDto.setLjhj(null);
                    proReviewConditionDto.setHjlStr("0");
                }
                if (null != projectReviewCon[6]) {
                    proReviewConditionDto.setIsadvanced((String) projectReviewCon[6]);
                    if (proReviewConditionDto.getIsadvanced().equals("9")) {
                        proReviewConditionDto.setReviewStage(proReviewConditionDto.getReviewStage() + "（提前介入）");
                    }
                } else {
                    proReviewConditionDto.setIsadvanced(null);
                }
                projectReviewConDtoList.add(proReviewConditionDto);
            }
        }
        resultMap.put("protReviewConditionList", projectReviewConDtoList);
        return new ResultMsg(true, Constant.MsgCode.OK.getValue(), "查询数据成功", resultMap);
    }

    @Override
    public List<ProjectReviewCostDto> findProjectRevireCost(ProjectReviewCostDto projectReviewCost) {
//		PageModelDto<ProjectReviewCostDto> pageModelDto = new PageModelDto<ProjectReviewCostDto>();
        HqlBuilder sqlBuilder = HqlBuilder.create();
        sqlBuilder.append("select s.projectcode,s.projectname,s.builtcompanyname,s.reviewstage,r.totalcost,r.paydate,d.declarevalue,d.authorizevalue,s.signdate,r.businessid , s.processstate , s.islightup from cs_sign s  ");
        sqlBuilder.append(" left join cs_expert_review r  ");
        sqlBuilder.append("on s.signid = r.businessid  ");
        sqlBuilder.append("left join cs_dispatch_doc d  ");
        sqlBuilder.append("on s.signid = d.signid  ");
        sqlBuilder.append("LEFT JOIN ( SELECT o.id oid, o.name oname, B.SIGNID bsignid FROM V_ORG_DEPT o, CS_SIGN_BRANCH b  WHERE O.ID = B.ORGID AND B.ISMAINBRABCH = '9') mo  ");
        sqlBuilder.append("ON s.signid = mo.bsignid  ");
        sqlBuilder.append("where r.paydate is not null ");
        //添加过滤条件。过滤删除和暂停的
        sqlBuilder.append("and s.signState <> '" + Constant.EnumState.DELETE.getValue() + "' ");
        sqlBuilder.append("and s.signState <> '" + Constant.EnumState.STOP.getValue() + "' ");
        //todo:添加查询条件
        if (projectReviewCost != null) {
            if (StringUtil.isNotEmpty(projectReviewCost.getProjectname())) {
                sqlBuilder.append("and s.projectname like '%" + projectReviewCost.getProjectname() + "%' ");
            }

            if (StringUtil.isNotEmpty(projectReviewCost.getBuiltcompanyname())) {
                sqlBuilder.append("and s.builtcompanyname like '%" + projectReviewCost.getBuiltcompanyname() + "%' ");
            }

            if (StringUtil.isNotEmpty(projectReviewCost.getReviewstage())) {
                sqlBuilder.append("and s.reviewstage = '" + projectReviewCost.getReviewstage() + "' ");
            }


            if (StringUtil.isNotEmpty(projectReviewCost.getBeginTime())) {
                sqlBuilder.append("and r.paydate >= to_date('" + projectReviewCost.getBeginTime() + "', 'yyyy-mm-dd hh24:mi:ss') ");
            }

            if (StringUtil.isNotEmpty(projectReviewCost.getEndTime())) {
                sqlBuilder.append("and r.paydate <= to_date('" + projectReviewCost.getEndTime() + "', 'yyyy-mm-dd hh24:mi:ss') ");
            }

            if (StringUtil.isNotEmpty(projectReviewCost.getDeptName())) {
                sqlBuilder.append("and mo.oname = '" + projectReviewCost.getDeptName() + "' ");
            }
        }
        List<Object[]> projectReviewCostList = expertSelectedRepo.getObjectArray(sqlBuilder);

        List<ProjectReviewCostDto> projectReviewCostDtoList = new ArrayList<>();
        if (projectReviewCostList.size() > 0) {
            for (int i = 0; i < projectReviewCostList.size(); i++) {
                Object obj = projectReviewCostList.get(i);
                Object[] result = (Object[]) obj;
                ProjectReviewCostDto projectReviewCostDto = new ProjectReviewCostDto();
                projectReviewCostDto.setProjectcode((String) result[0]);
                projectReviewCostDto.setProjectname((String) result[1]);
                projectReviewCostDto.setBuiltcompanyname((String) result[2]);
                projectReviewCostDto.setReviewstage((String) result[3]);
                projectReviewCostDto.setTotalCost((BigDecimal) result[4]);
                projectReviewCostDto.setPayDate((Date) result[5]);
                projectReviewCostDto.setDeclareValue((BigDecimal) result[6]);
                projectReviewCostDto.setAuthorizeValue((BigDecimal) result[7]);
                projectReviewCostDto.setSigndate((Date) result[8]);
                projectReviewCostDto.setBusinessId((String) result[9]);
                projectReviewCostDto.setIsLightUp((String) result[11]);
                projectReviewCostDto.setProcessState(new Integer(result[10].toString()));

                projectReviewCostDtoList.add(projectReviewCostDto);

                BigDecimal totalCost = financialManagerService.sunCount(projectReviewCostDto.getBusinessId());
                if (null != totalCost) {
                    projectReviewCostDto.setTotalCost(totalCost);
                }
                User u = signPrincipalService.getMainPriUser(projectReviewCostDto.getBusinessId());
                projectReviewCostDto.setPrincipal(u == null ? "" : u.getDisplayName());
            }
        }
//		pageModelDto.setCount(projectReviewCostDtoList.size());
//		pageModelDto.setValue(projectReviewCostDtoList);


        return projectReviewCostDtoList;
    }


    /**
     * 根据业务ID统计已经确认的抽取专家
     *
     * @param businessId
     * @return
     */
    @Override
    public int getSelectEPCount(String businessId) {
        return expertSelectedRepo.getSelectEPCount(businessId);
    }


    /**
     * 专家评审基本情况详细统计
     *
     * @param expertReviewCondDto
     * @return
     */
    @Override
    public ResultMsg expertReviewCondDetailCount(ExpertReviewCondDto expertReviewCondDto) {
        return expertSelectedRepo.expertReviewCondDetailCount(expertReviewCondDto);
    }

    /**
     * 专家评审基本情况综合统计
     *
     * @param expertReviewConSimpleDto
     * @return
     */
    @Override
    public ResultMsg expertReviewConSimpleCount(ExpertReviewConSimpleDto expertReviewConSimpleDto) {
        return expertSelectedRepo.expertReviewConSimpleCount(expertReviewConSimpleDto);
    }

    /**
     * 专家评审基本情况不规则统计
     *
     * @param expertReviewConSimpleDto
     * @return
     */
    @Override
    public ResultMsg expertReviewConComplicatedCount(ExpertReviewConSimpleDto expertReviewConSimpleDto) {
        return expertSelectedRepo.expertReviewConComplicatedCount(expertReviewConSimpleDto);
    }

    /**
     * 项目评审情况统计(按类别)
     *
     * @param projectReviewConditionDto
     * @return
     */
    @Override
    public ResultMsg proReviewConditionByTypeCount(ProReviewConditionDto projectReviewConditionDto) {
        return expertSelectedRepo.proReviewConditionByTypeCount(projectReviewConditionDto);
    }

    /**
     * 项目情况统计汇总
     *
     * @param projectReviewConditionDto
     * @return
     */
    @Override
    public ProReviewConditionDto proReviewConditionSum(ProReviewConditionDto projectReviewConditionDto) {
        return expertSelectedRepo.proReviewConditionSum(projectReviewConditionDto);
    }

    /**
     * 项目退文汇总
     *
     * @param projectReviewConditionDto
     * @return
     */
    @Override
    public ProReviewConditionDto getBackDispatchSum(ProReviewConditionDto projectReviewConditionDto) {
        return expertSelectedRepo.getBackDispatchSum(projectReviewConditionDto);
    }

    /**
     * 项目退文明细
     *
     * @param projectReviewConditionDto
     * @return
     */
    @Override
    public List<ProReviewConditionDto> getBackDispatchInfo(ProReviewConditionDto projectReviewConditionDto) {
        return expertSelectedRepo.getBackDispatchInfo(projectReviewConditionDto);
    }

    /**
     * 项目评审情况明细
     *
     * @param projectReviewConditionDto
     * @return
     */
    @Override
    public List<ProReviewConditionDto> proReviewConditionDetail(ProReviewConditionDto projectReviewConditionDto) {
        return expertSelectedRepo.proReviewConditionDetail(projectReviewConditionDto);
    }

    /**
     * 项目评审情况汇总(按照申报投资金额)
     *
     * @param beginTime
     * @param endTime
     * @return
     */
    @Override
    public Integer[] proReviewCondByDeclare(String beginTime, String endTime) {

        return expertSelectedRepo.proReviewCondByDeclare(beginTime, endTime);
    }

    /**
     * 专家评审会次数
     *
     * @param projectReviewConditionDto
     * @return
     */
    @Override
    public Integer proReviewMeetingCount(ProReviewConditionDto projectReviewConditionDto) {

        return expertSelectedRepo.proReviewMeetingCount(projectReviewConditionDto);
    }

    /**
     * 项目评审次数
     *
     * @param projectReviewConditionDto
     * @return
     */
    @Override
    public Integer proReviewCount(ProReviewConditionDto projectReviewConditionDto) {
        return expertSelectedRepo.proReviewCount(projectReviewConditionDto);
    }

    /**
     * 获取提前介入评审情况
     *
     * @param projectReviewConditionDto
     * @return
     */
    @Override
    public ProReviewConditionDto getAdvancedCon(ProReviewConditionDto projectReviewConditionDto) {
        return expertSelectedRepo.getAdvancedCon(projectReviewConditionDto);
    }

    /**
     * 业绩汇总
     * @param achievementSumDto
     * @return
     */
    @Override
    public ResultMsg findAchievementSum(AchievementSumDto achievementSumDto) {
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("achievementSumList", expertSelectedRepo.findAchievementSum(achievementSumDto));
        achievementSumDto.setIsmainuser("9");
        resultMap.put("achievementMainList",expertSelectedRepo.findAchievementDetail(achievementSumDto));
        achievementSumDto.setIsmainuser("0");
        resultMap.put("achievementAssistList",expertSelectedRepo.findAchievementDetail(achievementSumDto));
        return new ResultMsg(true, Constant.MsgCode.OK.getValue(), "查询数据成功", resultMap);

    }

    /**
     * 业绩明细
     * @param achievementSumDto
     * @return
     */
    @Override
    public ResultMsg findAchievementDetail(AchievementSumDto achievementSumDto) {
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("achievementDetailList", expertSelectedRepo.findAchievementDetail(achievementSumDto));
        return new ResultMsg(true, Constant.MsgCode.OK.getValue(), "查询数据成功", resultMap);
    }

}