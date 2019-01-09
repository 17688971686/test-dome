package cs.repository.repositoryImpl.monthly;

import cs.common.constants.Constant;
import cs.common.HqlBuilder;
import cs.common.ResultMsg;
import cs.common.utils.CreateTemplateUtils;
import cs.common.utils.DateUtils;
import cs.common.utils.StringUtil;
import cs.domain.monthly.MonthlyNewsletter;
import cs.domain.monthly.MonthlyNewsletter_;
import cs.model.expert.ProReviewConditionDto;
import cs.repository.AbstractRepository;
import cs.repository.repositoryImpl.expert.ExpertCostCountRepo;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.*;

/**
 * Description: 月报简报 数据操作实现类
 * author: ldm
 * Date: 2017-9-8 11:23:41
 */
@Repository
public class MonthlyNewsletterRepoImpl extends AbstractRepository<MonthlyNewsletter, String> implements MonthlyNewsletterRepo {

    @Autowired
    private ExpertCostCountRepo expertCostCountRepo;

    /**
     * 查询年度
     * @param businessId
     * @return
     */
    @Deprecated
    @Override
    public  String findYear(String businessId){
        Criteria criteria = getExecutableCriteria();
        criteria.add(Restrictions.eq(MonthlyNewsletter_.id.getName(),businessId));
        List<MonthlyNewsletter> monthlyNewsletterList=criteria.list();
        String year=monthlyNewsletterList.get(0).getReportMultiyear();
        return year;
    }

    /**
     * 更改月报简报状态
     * @param id
     * @param value
     * @return
     */
    @Override
    public ResultMsg updateMonthlyType(String id, String value) {
        HqlBuilder hqlBuilder = HqlBuilder.create();
        hqlBuilder.append(" update "+MonthlyNewsletter.class.getSimpleName()+" set "+MonthlyNewsletter_.monthlyType.getName()+"=:monthlyType ");
        hqlBuilder.setParam("monthlyType",value);
        hqlBuilder.bulidPropotyString("where",MonthlyNewsletter_.id.getName(),id);
        executeHql(hqlBuilder);
        return new ResultMsg(true, Constant.MsgCode.OK.getValue(),"操作成功！");
    }

    /**
     * 生成月报中附件数据
     * @param projectReviewConditionDto
     * @return
     */
    @Override
    public Map<String , List<ProReviewConditionDto>> proReviewConditionCount(ProReviewConditionDto projectReviewConditionDto) {
        Map<String, List<ProReviewConditionDto>> resultMap = new LinkedHashMap<>();
        Map<String, List<ProReviewConditionDto>> resultMap2 = new HashMap<>();
        HqlBuilder sqlBuilder = HqlBuilder.create();
        HqlBuilder sqlBuilder1 = HqlBuilder.create();
        sqlBuilder.append(" select   reviewstage, " +
                " count(s.projectcode) projectcode,sum(d.declarevalue) declarevalue,sum(d.authorizevalue) authorizevalue," +
                "  d.dispatchType  from cs_sign s   ");
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
        sqlBuilder.append(" group by s.reviewstage,d.dispatchType");
        sqlBuilder1.append(" select t. reviewstage , sum(t.projectcode) , sum(t.declarevalue) / 10000 declarevalue , " +
                " sum(t.authorizevalue) / 10000 authorizevalue ,  (sum(t.declarevalue) - sum(t.authorizevalue)) / 10000 ljhj ," +
                " decode(sum(t.declarevalue) , 0 , 0 ,round((sum(t.declarevalue) - sum(t.authorizevalue)) / 10000 / " +
                " (sum(t.declarevalue) / 1000),  5) * 1000) hjl ,  t.dispatchType  from  ( ")
                .append(sqlBuilder.getHqlString())
                .append(" )t   group by t.reviewstage,t.dispatchType ");
        List<Object[]> projectReviewConList = expertCostCountRepo.getObjectArray(sqlBuilder1);
//        List<ProReviewConditionDto> projectReviewConDtoList = new ArrayList<ProReviewConditionDto>();

        if (projectReviewConList.size() > 0) {
            for (int i = 0; i < projectReviewConList.size(); i++) {
                Object[] projectReviewCon = projectReviewConList.get(i);
                ProReviewConditionDto proReviewConditionDto = new ProReviewConditionDto();
                if (null != projectReviewCon[1]) {
                    proReviewConditionDto.setProCount((BigDecimal) projectReviewCon[1]);
                } else {
                    proReviewConditionDto.setProCount(BigDecimal.ZERO);
                }

                if (null != projectReviewCon[2] && ((BigDecimal) projectReviewCon[2]).compareTo(new BigDecimal("0")) > 0 ) {
                    proReviewConditionDto.setDeclareStr(CreateTemplateUtils.dealNumber(projectReviewCon[2]).toString());
                } else {
                    proReviewConditionDto.setDeclareStr("---");
                }
                if (null != projectReviewCon[3] && ((BigDecimal) projectReviewCon[3]).compareTo(new BigDecimal("0")) > 0 ) {
                    proReviewConditionDto.setAuthorizeStr(CreateTemplateUtils.dealNumber(projectReviewCon[3]).toString());
                } else {
                    proReviewConditionDto.setAuthorizeStr("---");
                }
                if (null != projectReviewCon[4]  && ((BigDecimal) projectReviewCon[4]).compareTo(new BigDecimal("0")) > 0 ) {
                    proReviewConditionDto.setLjhjStr(CreateTemplateUtils.dealNumber(projectReviewCon[4]).toString());
                } else {
                    proReviewConditionDto.setLjhjStr("---");
                }
                if (null != projectReviewCon[5] && ((BigDecimal) projectReviewCon[5]).compareTo(new BigDecimal("0")) > 0  ) {
                    proReviewConditionDto.setHjlStr(CreateTemplateUtils.dealNumber(projectReviewCon[5]) + "%");
                } else {
                    proReviewConditionDto.setHjlStr("---");
                }
                if (null != projectReviewCon[6]) {
                    switch ((String) projectReviewCon[6]){
                        case "项目发文":
                            if(null != projectReviewCon[0] && Constant.OTHERS.equals(projectReviewCon[0])){
                                proReviewConditionDto.setIndexName("审核项目");
                            }else{
                                proReviewConditionDto.setIndexName("审核投资项目");
                            }
                            break;
                        case "发文退文函":
                            proReviewConditionDto.setIndexName("建议重编项目");
                            break;
                        case "暂不实施":
                            proReviewConditionDto.setIndexName("暂不实施项目");
                            break;
                        default:break;
                    }
                } else {
                    proReviewConditionDto.setIsadvanced(null);
                }

                String reviewStageName = null ;
                if (null != projectReviewCon[0]) {
                    proReviewConditionDto.setReviewStage((String) projectReviewCon[0]);
                   reviewStageName = (String) projectReviewCon[0];
                    if(Constant.STAGE_BUDGET.equals(projectReviewCon[0])){
                        reviewStageName = "初步设计概算审核";
                    }
                } else {
                    proReviewConditionDto.setReviewStage(null);
                }
                if(null != reviewStageName ){

                    if(null != resultMap2.get(reviewStageName)){
                        resultMap2.get(reviewStageName).add(proReviewConditionDto);
                    }else{
                        List<ProReviewConditionDto> pList = new ArrayList<>();
                        pList.add(proReviewConditionDto);
                        resultMap2.put(reviewStageName , pList);
                    }
                }
            }
        }
        if(null != resultMap2.get(Constant.REGISTER_CODE)){
            resultMap.put(Constant.REGISTER_CODE , resultMap2.get(Constant.REGISTER_CODE));
        }
        if(null != resultMap2.get(Constant.STAGE_SUG)){
            resultMap.put(Constant.STAGE_SUG , resultMap2.get(Constant.STAGE_SUG));
        }
        if(null != resultMap2.get(Constant.STAGE_STUDY)){
            resultMap.put(Constant.STAGE_STUDY , resultMap2.get(Constant.STAGE_STUDY));
        }
        if(null != resultMap2.get("初步设计概算审核")){
            resultMap.put("初步设计概算审核" , resultMap2.get("初步设计概算审核"));
        }
        if(null != resultMap2.get(Constant.APPLY_REPORT)){
            resultMap.put(Constant.APPLY_REPORT , resultMap2.get(Constant.APPLY_REPORT));
        }
        if(null != resultMap2.get(Constant.OTHERS)){
            resultMap.put(Constant.OTHERS , resultMap2.get(Constant.OTHERS));
        }
        if(null != resultMap2.get(Constant.IMPORT_DEVICE)){
            resultMap.put(Constant.IMPORT_DEVICE , resultMap2.get(Constant.IMPORT_DEVICE));
        }
        if(null != resultMap2.get(Constant.DEVICE_BILL_HOMELAND)){
            resultMap.put(Constant.DEVICE_BILL_HOMELAND , resultMap2.get(Constant.DEVICE_BILL_HOMELAND));
        }
        if(null != resultMap2.get(Constant.DEVICE_BILL_IMPORT)){
            resultMap.put(Constant.DEVICE_BILL_IMPORT , resultMap2.get(Constant.DEVICE_BILL_IMPORT));
        }
        return resultMap;
    }
}