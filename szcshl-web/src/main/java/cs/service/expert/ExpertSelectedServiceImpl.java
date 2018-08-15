package cs.service.expert;

import cs.common.constants.Constant;
import cs.common.HqlBuilder;
import cs.common.ResultMsg;
import cs.common.utils.*;
import cs.domain.expert.*;
import cs.domain.financial.FinancialManager;
import cs.domain.financial.FinancialManager_;
import cs.domain.sys.OrgDept;
import cs.domain.sys.OrgDept_;
import cs.domain.sys.User;
import cs.model.PageModelDto;
import cs.model.expert.*;
import cs.model.financial.FinancialManagerDto;
import cs.repository.odata.ODataFilterItem;
import cs.repository.odata.ODataObj;
import cs.repository.odata.ODataObjFilterStrategy;
import cs.repository.repositoryImpl.expert.*;
import cs.repository.repositoryImpl.financial.FinancialManagerRepo;
import cs.repository.repositoryImpl.sys.OrgDeptRepo;
import cs.repository.repositoryImpl.sys.UserRepo;
import cs.service.project.SignPrincipalService;
import cs.service.sys.UserService;
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
    private UserService userService;
    @Autowired
    private ExpertRepo expertRepo;
    @Autowired
    private OrgDeptRepo orgDeptRepo;
    @Autowired
    private UserRepo userRepo;


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
     *
     * @param
     * @return
     * @author ldm 修改月2018-03
     * SELECT e.name, e.idcard, e.userphone, me.reviewcost AS reviewcost, me.reviewtaxes AS reviewtaxes, ye.reviewcost AS yreviewcost, ye.reviewtaxes AS yreviewtaxes
     * FROM (  SELECT s.EXPERTID, SUM (s.reviewcost) reviewcost, SUM (s.reviewtaxes) reviewtaxes
     * FROM cs_expert_selected s, cs_expert_review r
     * WHERE s.expertreviewid = r.id AND s.isconfrim = '9' AND s.isjoin = '9' AND r.paydate IS NOT NULL
     * AND TO_CHAR (r.paydate, 'yyyy-mm') = '2017-03' GROUP BY s.expertid) me,
     * cs_expert e,
     * (  SELECT s.EXPERTID, SUM (s.reviewcost) reviewcost, SUM (s.reviewtaxes) reviewtaxes
     * FROM cs_expert_selected s, cs_expert_review r
     * WHERE s.expertreviewid = r.id AND s.isconfrim = '9' AND s.isjoin = '9' AND r.paydate IS NOT NULL
     * AND TO_CHAR (r.paydate, 'yyyy') = '2017' GROUP BY s.expertid) ye
     * where me.EXPERTID = e.EXPERTID and me.EXPERTID = ye.EXPERTID
     */
    @Override
    public ResultMsg expertCostTotal(ExpertCostCountDto expertCostDto) {
        Map<String, Object> resultMap = new HashMap<>();
        if (expertCostDto == null) {
            expertCostDto = new ExpertCostCountDto();
        }
        if (!Validate.isString(expertCostDto.getYear())) {
            expertCostDto.setYear(DateUtils.getNowYear());
        }
        if (!Validate.isString(expertCostDto.getMonth())) {
            expertCostDto.setMonth(String.valueOf(DateUtils.getCurMonth()));
        }
        int year = Integer.parseInt(expertCostDto.getYear());
        int month = Integer.parseInt(expertCostDto.getMonth());
        String monthValue = String.format("%02d", month);
        int maxDate = DateUtils.getMaxDayOfMonth(year, month);
        HqlBuilder sqlBuilder = HqlBuilder.create();
        sqlBuilder.append(" SELECT e.name, e.idcard, e.userphone, me.reviewcost AS reviewcost, me.reviewtaxes AS reviewtaxes, ye.reviewcost AS yreviewcost, ye.reviewtaxes AS yreviewtaxes ");
        sqlBuilder.append(" FROM (  SELECT s.EXPERTID, SUM (s.reviewcost) reviewcost, SUM (s.reviewtaxes) reviewtaxes ");
        sqlBuilder.append(" FROM cs_expert_selected s, cs_expert_review r ");
        sqlBuilder.append(" WHERE s.expertreviewid = r.id AND s.isconfrim = '9' AND s.isjoin = '9' AND r.paydate IS NOT NULL ");
        sqlBuilder.append(" AND TO_CHAR (r.reviewDate, 'yyyy-mm') = :monthValue GROUP BY s.expertid) me, ");
        sqlBuilder.setParam("monthValue", expertCostDto.getYear() + "-" + monthValue);
        sqlBuilder.append(" cs_expert e, ");
        sqlBuilder.append(" (  SELECT s.EXPERTID, SUM (s.reviewcost) reviewcost, SUM (s.reviewtaxes) reviewtaxes ");
        sqlBuilder.append(" FROM cs_expert_selected s, cs_expert_review r ");
        sqlBuilder.append(" WHERE s.expertreviewid = r.id AND s.isconfrim = '9' AND s.isjoin = '9' AND r.paydate IS NOT NULL ");
        sqlBuilder.append(" AND TO_CHAR (r.reviewDate, 'yyyy') = :yearValue AND r.reviewDate <= to_date(:yearDate,'yyyy-mm-dd') GROUP BY s.expertid) ye ");
        sqlBuilder.setParam("yearValue", expertCostDto.getYear()).setParam("yearDate", expertCostDto.getYear() + "-" + monthValue + "-" + maxDate);
        sqlBuilder.append(" where me.EXPERTID = e.EXPERTID and me.EXPERTID = ye.EXPERTID order by E.EXPERTNO ");

        List<Object[]> expertCostCountDtoList = expertCostCountRepo.getObjectArray(sqlBuilder);
        BigDecimal tMonthReviewcost = BigDecimal.ZERO, tMonthReviewtaxes = BigDecimal.ZERO,
                tYearReviewcost = BigDecimal.ZERO, tYearReviewtaxes = BigDecimal.ZERO;
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
                    tMonthReviewcost = Arith.safeAdd(tMonthReviewcost, expertCostCountDto.getReviewcost());
                }
                if (null != expertCostCounts[4]) {
                    expertCostCountDto.setReviewtaxes((BigDecimal) expertCostCounts[4]);
                    tMonthReviewtaxes = Arith.safeAdd(tMonthReviewtaxes, expertCostCountDto.getReviewtaxes());
                }
                if (null != expertCostCounts[5]) {
                    expertCostCountDto.setYreviewcost((BigDecimal) expertCostCounts[5]);
                    tYearReviewcost = Arith.safeAdd(tYearReviewcost, expertCostCountDto.getYreviewcost());
                }
                if (null != expertCostCounts[6]) {
                    expertCostCountDto.setYreviewtaxes((BigDecimal) expertCostCounts[6]);
                    tYearReviewtaxes = Arith.safeAdd(tYearReviewtaxes, expertCostCountDto.getYreviewtaxes());
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

        if (expertCostDetailCountDto == null) {
            expertCostDetailCountDto = new ExpertCostDetailCountDto();
        }
        if (!Validate.isString(expertCostDetailCountDto.getYear())) {
            expertCostDetailCountDto.setYear(DateUtils.getNowYear());
        }
        if (!Validate.isString(expertCostDetailCountDto.getMonth())) {
            expertCostDetailCountDto.setMonth(String.valueOf(DateUtils.getCurMonth()));
        }
        String day = DateUtils.getMaxDayOfMonth(Integer.parseInt(expertCostDetailCountDto.getYear()), Integer.parseInt(expertCostDetailCountDto.getMonth())) + "";
        String bTime = expertCostDetailCountDto.getYear() + "-" + expertCostDetailCountDto.getMonth() + "-01 00:00:00";
        String eTime = expertCostDetailCountDto.getYear() + "-" + expertCostDetailCountDto.getMonth() + "-" + day + " 23:59:59";

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
        sqlBuilder.append(" SELECT sdk.signid,sdk.projectcode, sdk.projectname, sdk.builtcompanyname, sdk.reviewstage, ");
        sqlBuilder.append(" sdk.appalyInvestment, sdk.authorizevalue, sdk.signdate, sdk.MORGNAME,sdk.ALLPRIUSER, ");
        sqlBuilder.append(" CFM.CHARGENAME,CFM.CHARGE,CFM.STAGECOUNT,CFM.PAYMENTDATA ");
        sqlBuilder.append(" FROM SIGN_DISP_WORK sdk, CS_FINANCIAL_MANAGER cfm,CS_SIGN sg ");
        sqlBuilder.append(" WHERE sdk.signid = sg.signid  ");
        /*sqlBuilder.append("AND sg.isSendFileRecord =:fileState ");
        sqlBuilder.setParam("fileState", Constant.EnumState.YES.getValue());*/
        sqlBuilder.append(" AND CFM.BUSINESSID = SDK.SIGNID AND  sdk.signState <> :signState AND CFM.CHARGETYPE = :chargeType AND CFM.PAYMENTDATA is not null  ");
        sqlBuilder.setParam("signState", Constant.EnumState.DELETE.getValue()).setParam("chargeType", Constant.EnumState.PROCESS.getValue());

        //查询条件
        if (null != projectReviewCostDto) {
            if (StringUtil.isNotEmpty(projectReviewCostDto.getProjectname())) {
                sqlBuilder.append("and sdk.projectname like :projectName ");
                sqlBuilder.setParam("projectName", "%" + projectReviewCostDto.getProjectname() + "%");
            }

            if (StringUtil.isNotEmpty(projectReviewCostDto.getBuiltcompanyname())) {
                sqlBuilder.append("and sdk.builtcompanyname like :projectCode ");
                sqlBuilder.setParam("projectCode", "%" + projectReviewCostDto.getBuiltcompanyname() + "%");
            }

            if (StringUtil.isNotEmpty(projectReviewCostDto.getReviewstage())) {
                sqlBuilder.append("and sdk.reviewstage = :stage ");
                sqlBuilder.setParam("stage", projectReviewCostDto.getReviewstage());
            }

            if (StringUtil.isNotEmpty(projectReviewCostDto.getBeginTime())) {
                sqlBuilder.append("and CFM.PAYMENTDATA >= to_date(:beginTime, 'yyyy-mm-dd hh24:mi:ss') ");
                sqlBuilder.setParam("beginTime", projectReviewCostDto.getBeginTime());
            }

            if (StringUtil.isNotEmpty(projectReviewCostDto.getEndTime())) {
                sqlBuilder.append("and CFM.PAYMENTDATA <= to_date(:endTime, 'yyyy-mm-dd hh24:mi:ss') ");
                sqlBuilder.setParam("endTime", projectReviewCostDto.getEndTime());
            }

            if (StringUtil.isNotEmpty(projectReviewCostDto.getDeptName())) {
                sqlBuilder.append("and SDK.MORGNAME = :deptName ");
                sqlBuilder.setParam("deptName", projectReviewCostDto.getDeptName());
            }
        }
        sqlBuilder.append(" ORDER BY CFM.PAYMENTDATA DESC,SDK.SIGNDATE DESC,SDK.SIGNID ");
        List<Object[]> projectReviewCostList = expertCostCountRepo.getObjectArray(sqlBuilder);
        List<ProjectReviewCostDto> projectReviewCostDtoList = new ArrayList<ProjectReviewCostDto>();
        List<FinancialManagerDto> financialManagerDtoList = null;
        String oldSignid = "";
        ProjectReviewCostDto reviewCostDto = null;
        BigDecimal totalCost = BigDecimal.ZERO;
        boolean isNew = false;
        if (Validate.isList(projectReviewCostList)) {
            for (int i = 0, l = projectReviewCostList.size(); i < l; i++) {
                //遍历查询结果，如果是新项目，则创建一个评审对象
                Object[] projectReviewCost = projectReviewCostList.get(i);
                String signId = projectReviewCost[0].toString();
                if (!oldSignid.equals(signId)) {
                    if (i > 0) {
                        reviewCostDto.setFinancialManagerDtoList(financialManagerDtoList);
                        reviewCostDto.setTotalCost(totalCost);
                        totalCost = BigDecimal.ZERO;
                        projectReviewCostDtoList.add(reviewCostDto);
                    }
                    reviewCostDto = new ProjectReviewCostDto();
                    financialManagerDtoList = new ArrayList<>();
                    oldSignid = signId;
                    isNew = true;
                } else {
                    isNew = false;
                }

                if (isNew) {
                    reviewCostDto.setBusinessId(signId);
                    reviewCostDto.setProjectcode(projectReviewCost[1] == null ? "" : projectReviewCost[1].toString());
                    reviewCostDto.setProjectname(projectReviewCost[2] == null ? "" : projectReviewCost[2].toString());
                    reviewCostDto.setBuiltcompanyname(projectReviewCost[3] == null ? "" : projectReviewCost[3].toString());
                    reviewCostDto.setReviewstage(projectReviewCost[4] == null ? "" : projectReviewCost[4].toString());
                    reviewCostDto.setDeclareValue(projectReviewCost[5] == null ? null : (BigDecimal) projectReviewCost[5]);
                    reviewCostDto.setAuthorizeValue(projectReviewCost[6] == null ? null : (BigDecimal) projectReviewCost[6]);
                    reviewCostDto.setSigndate(projectReviewCost[7] == null ? null : (Date) projectReviewCost[7]);
                    reviewCostDto.setDeptName(projectReviewCost[8] == null ? "" : projectReviewCost[8].toString());
                    reviewCostDto.setPrincipal(projectReviewCost[9] == null ? "" : projectReviewCost[9].toString());
                    //reviewCostDto.setTotalCost(projectReviewCost[12] == null ? null : (BigDecimal) projectReviewCost[12]);
                    reviewCostDto.setPayDate(projectReviewCost[13] == null ? null : (Date) projectReviewCost[13]);
                }
                FinancialManagerDto financialManagerDto = new FinancialManagerDto();
                financialManagerDto.setChargeName(projectReviewCost[10] == null ? null : projectReviewCost[10].toString());
                financialManagerDto.setCharge(projectReviewCost[11] == null ? null : (BigDecimal) projectReviewCost[11]);
                financialManagerDtoList.add(financialManagerDto);
                //计算总数
                totalCost = Arith.safeAdd(totalCost, projectReviewCost[11] == null ? BigDecimal.ZERO : (BigDecimal) projectReviewCost[11]);
                //最后一个
                if (i == l - 1) {
                    reviewCostDto.setFinancialManagerDtoList(financialManagerDtoList);
                    reviewCostDto.setTotalCost(totalCost);
                    totalCost = BigDecimal.ZERO;
                    projectReviewCostDtoList.add(reviewCostDto);
                }

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
        sqlBuilder.append("where f.paymentdata is not null  ");
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
        sqlBuilder1.append("where s.isSendFileRecord =:fileState ");
        sqlBuilder1.setParam("fileState", Constant.EnumState.YES.getValue());
        sqlBuilder1.append("and  f.paymentdata is not null and f.chargename is not null  ");

        if (null != projectReviewCostDto) {
            if (StringUtil.isNotEmpty(projectReviewCostDto.getChargeName())) {
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
                sqlBuilder.append("and  f.paymentdata >= to_date('" + projectReviewCostDto.getBeginTime() + "', 'yyyy-mm-dd hh24:mi:ss') ");
                sqlBuilder1.append("and  f.paymentdata >= to_date('" + projectReviewCostDto.getBeginTime() + "', 'yyyy-mm-dd hh24:mi:ss') ");
            }

            if (StringUtil.isNotEmpty(projectReviewCostDto.getEndTime())) {
                sqlBuilder.append("and  f.paymentdata <= to_date('" + projectReviewCostDto.getEndTime() + "', 'yyyy-mm-dd hh24:mi:ss') ");
                sqlBuilder1.append("and  f.paymentdata <= to_date('" + projectReviewCostDto.getEndTime() + "', 'yyyy-mm-dd hh24:mi:ss') ");
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

    /**
     * 评审费发送
     *
     * @param projectReviewCostDto
     * @return
     */
    @Override
    public List<ProjectReviewCostDto> findProjectRevireCost(ProjectReviewCostDto projectReviewCostDto) {
        HqlBuilder sqlBuilder = HqlBuilder.create();
        sqlBuilder.append(" SELECT sdk.signid,sdk.projectcode,sdk.projectname,sdk.builtcompanyname,sdk.reviewstage, ");
        sqlBuilder.append(" sdk.appalyInvestment,sdk.authorizevalue,sdk.signdate,sdk.MORGNAME,sdk.ALLPRIUSER, ");
        sqlBuilder.append(" CFM.CHARGENAME,CFM.CHARGE,CFM.STAGECOUNT,CFM.PAYMENTDATA ");
        sqlBuilder.append(" FROM SIGN_DISP_WORK sdk LEFT JOIN CS_FINANCIAL_MANAGER cfm on CFM.BUSINESSID = SDK.SIGNID ");
        sqlBuilder.append(" WHERE sdk.signState !=:signState1 and sdk.signState !=:signState2 ");
        sqlBuilder.setParam("signState1", Constant.EnumState.STOP.getValue());
        sqlBuilder.setParam("signState2", Constant.EnumState.DELETE.getValue());
        sqlBuilder.append(" AND (sdk.isassistproc is null or sdk.isassistproc = :assistproc) ");
        sqlBuilder.setParam("assistproc", Constant.EnumState.NO.getValue());
        sqlBuilder.append(" AND CFM.CHARGETYPE = :chageType AND CFM.CHARGENAME = :chageName AND CFM.PAYMENTDATA IS NOT NULL ");
        sqlBuilder.setParam("chageType", Constant.EnumState.PROCESS.getValue());
        sqlBuilder.setParam("chageName", "专家评审费");

        //查询条件
        if (Validate.isObject(projectReviewCostDto)) {
            if (StringUtil.isNotEmpty(projectReviewCostDto.getProjectname())) {
                sqlBuilder.append("and sdk.projectname like :projectName ");
                sqlBuilder.setParam("projectName", "%" + projectReviewCostDto.getProjectname() + "%");
            }

            if (StringUtil.isNotEmpty(projectReviewCostDto.getBuiltcompanyname())) {
                sqlBuilder.append("and sdk.builtcompanyname like :projectCode ");
                sqlBuilder.setParam("projectCode", "%" + projectReviewCostDto.getBuiltcompanyname() + "%");
            }

            if (StringUtil.isNotEmpty(projectReviewCostDto.getReviewstage())) {
                sqlBuilder.append("and sdk.reviewstage = :stage ");
                sqlBuilder.setParam("stage", projectReviewCostDto.getReviewstage());
            }

            if (StringUtil.isNotEmpty(projectReviewCostDto.getBeginTime())) {
                sqlBuilder.append("and CFM.PAYMENTDATA >= to_date(:beginTime, 'yyyy-mm-dd hh24:mi:ss') ");
                sqlBuilder.setParam("beginTime", projectReviewCostDto.getBeginTime());
            }

            if (StringUtil.isNotEmpty(projectReviewCostDto.getEndTime())) {
                sqlBuilder.append("and CFM.PAYMENTDATA <= to_date(:endTime, 'yyyy-mm-dd hh24:mi:ss') ");
                sqlBuilder.setParam("endTime", projectReviewCostDto.getEndTime());
            }

            if (StringUtil.isNotEmpty(projectReviewCostDto.getDeptName())) {
                sqlBuilder.append("and SDK.MORGNAME = :deptName ");
                sqlBuilder.setParam("deptName", projectReviewCostDto.getDeptName());
            }
        }
        sqlBuilder.append(" ORDER BY CFM.PAYMENTDATA DESC nulls first,SDK.SIGNDATE DESC ");
        List<Object[]> projectReviewCostList = expertSelectedRepo.getObjectArray(sqlBuilder);

        List<ProjectReviewCostDto> projectReviewCostDtoList = new ArrayList<>();
        if (Validate.isList(projectReviewCostList)) {
            for (int i = 0; i < projectReviewCostList.size(); i++) {
                Object[] reObj = projectReviewCostList.get(i);
                ProjectReviewCostDto projCostDto = new ProjectReviewCostDto();
                projCostDto.setBusinessId(reObj[0].toString());
                projCostDto.setProjectcode(reObj[1] == null ? "" : reObj[1].toString());
                projCostDto.setProjectname(reObj[2] == null ? "" : reObj[2].toString());
                projCostDto.setBuiltcompanyname(reObj[3] == null ? "" : reObj[3].toString());
                projCostDto.setReviewstage(reObj[4] == null ? "" : reObj[4].toString());
                projCostDto.setDeclareValue(reObj[5] == null ? null : (BigDecimal) reObj[5]);
                projCostDto.setAuthorizeValue(reObj[6] == null ? null : (BigDecimal) reObj[6]);
                projCostDto.setSigndate(reObj[7] == null ? null : (Date) reObj[7]);
                projCostDto.setDeptName(reObj[8] == null ? "" : reObj[8].toString());
                projCostDto.setPrincipal(reObj[9] == null ? "" : reObj[9].toString());
                //费用只取专家评审费
                projCostDto.setTotalCost(reObj[11] == null ? null : (BigDecimal) reObj[11]);
                projCostDto.setPayDate(reObj[13] == null ? null : (Date) reObj[13]);

                projectReviewCostDtoList.add(projCostDto);
            }
        }

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
     *
     * @param achievementSumDto
     * @return
     */
    @Override
    public ResultMsg findAchievementSum(AchievementSumDto achievementSumDto) {
        Map<String, Object> resultMap = new HashMap<>();
        Map<String, Object> levelMap = userService.getUserLevel();
        Integer level = 0;
        List<OrgDept> orgDeptList = new ArrayList<OrgDept>();
        if(null != levelMap && null!= levelMap.get("leaderFlag")){
            level = (Integer) levelMap.get("leaderFlag");
            if(level == 0){

            }else if(level == 1){
                orgDeptList = orgDeptRepo.findAll();

            }else if(level == 2){
                Criteria criteria = orgDeptRepo.getExecutableCriteria();
                criteria.add(Restrictions.eq(OrgDept_.sLeaderID.getName(),SessionUtil.getUserId()));
                orgDeptList = criteria.list();
            }else if(level == 3){
                Criteria criteria = orgDeptRepo.getExecutableCriteria();
                criteria.add(Restrictions.eq(OrgDept_.directorID.getName(),SessionUtil.getUserId()));
                orgDeptList = criteria.list();
            }else if(level == 4){
                Criteria criteria = orgDeptRepo.getExecutableCriteria();
                criteria.add(Restrictions.eq(OrgDept_.directorID.getName(),SessionUtil.getUserId()));
                orgDeptList = criteria.list();
            }
        }
        resultMap.put("achievementSumList", expertSelectedRepo.findAchievementSum(achievementSumDto, levelMap,orgDeptList));
        achievementSumDto.setIsmainuser("9");
        resultMap.put("achievementMainList", expertSelectedRepo.findAchievementDetail(achievementSumDto, levelMap,orgDeptList));
        achievementSumDto.setIsmainuser("0");
        resultMap.put("achievementAssistList", expertSelectedRepo.findAchievementDetail(achievementSumDto, levelMap,orgDeptList));

        resultMap.put("orgDeptList",orgDeptList);
        resultMap.put("level", level);
        if(Validate.isString(achievementSumDto.getUserId())){
            resultMap.put("userId", achievementSumDto.getUserId());
            User user = userRepo.findById(achievementSumDto.getUserId());
            resultMap.put("deptName",user.getOrg().getName());
        }else{
            resultMap.put("userId", SessionUtil.getUserId());
        }
        return new ResultMsg(true, Constant.MsgCode.OK.getValue(), "查询数据成功", resultMap);

    }

    @Override
    public ResultMsg findAchievementDetail(AchievementSumDto achievementSumDto, Map<String, Object> levelMap) {
        return null;
    }

/*    *//**
     * 业绩明细
     *
     * @param achievementSumDto
     * @return
     *//*
    @Override
    public ResultMsg findAchievementDetail(AchievementSumDto achievementSumDto, Map<String, Object> levelMap) {
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("achievementDetailList", expertSelectedRepo.findAchievementDetail(achievementSumDto, levelMap));
        return new ResultMsg(true, Constant.MsgCode.OK.getValue(), "查询数据成功", resultMap);
    }*/

    /**
     * 部门业绩明细
     *
     * @param achievementSumDto
     * @return
     */
    @Override
    public ResultMsg findDeptAchievementDetail(AchievementDeptDetailDto achievementSumDto) {
        Map<String, Object> resultMap = new HashMap<>();
        Map<String, Object> levelMap = userService.getUserLevel();
        resultMap.put("achievementDeptDetailList", expertSelectedRepo.findDeptAchievementDetail(achievementSumDto, levelMap));
        return new ResultMsg(true, Constant.MsgCode.OK.getValue(), "查询数据成功", resultMap);
    }

}