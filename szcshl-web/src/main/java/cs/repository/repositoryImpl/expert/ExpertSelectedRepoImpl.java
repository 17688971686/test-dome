package cs.repository.repositoryImpl.expert;

import cs.common.HqlBuilder;
import cs.common.ResultMsg;
import cs.common.constants.Constant;
import cs.common.utils.BeanCopierUtils;
import cs.common.utils.DateUtils;
import cs.common.utils.StringUtil;
import cs.common.utils.Validate;
import cs.domain.expert.Expert;
import cs.domain.expert.ExpertReview;
import cs.domain.expert.ExpertSelected;
import cs.domain.expert.ExpertSelected_;
import cs.model.expert.*;
import cs.repository.AbstractRepository;
import org.hibernate.Criteria;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.hibernate.type.DoubleType;
import org.hibernate.type.StringType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.*;

/**
 * Description: 抽取专家 数据操作实现类
 * author: ldm
 * Date: 2017-6-14 14:26:49
 */
@Repository
public class ExpertSelectedRepoImpl extends AbstractRepository<ExpertSelected, String> implements ExpertSelectedRepo {
    @Autowired
    private ExpertSelectedRepo expertSelectedRepo;
    @Autowired
    private ExpertRepo expertRepo;
    @Autowired
    private ExpertReviewRepo expertReviewRepo;

    /**
     * 根据大类，小类和专家类别，综合评分确认已经抽取的专家
     *
     * @param maJorBig
     * @param maJorSmall
     * @param expeRttype
     * @param compositeScore
     * @return
     */
    @Override
    public int findConfirmSeletedEP(String reviewId, String maJorBig, String maJorSmall, String expeRttype, Integer compositeScore, Integer compositeScortEnd) {
        HqlBuilder sqlBuilder = HqlBuilder.create();
        sqlBuilder.append("select count(ID) from cs_expert_selected where expertreviewid =:reviewId ");
        sqlBuilder.setParam("reviewId", reviewId);
        sqlBuilder.append(" and " + ExpertSelected_.isConfrim.getName() + " =:isConfrim ");
        sqlBuilder.setParam("isConfrim", Constant.EnumState.YES.getValue());
        sqlBuilder.append(" and " + ExpertSelected_.maJorBig.getName() + " =:maJorBig ");
        sqlBuilder.setParam("maJorBig", maJorBig);
        sqlBuilder.append(" and " + ExpertSelected_.maJorSmall.getName() + " =:maJorSmall ");
        sqlBuilder.setParam("maJorSmall", maJorSmall);
        sqlBuilder.append(" and " + ExpertSelected_.expeRttype.getName() + " =:expeRttype ");
        sqlBuilder.setParam("expeRttype", expeRttype);
        if (compositeScore != null && compositeScore > 0) {
            sqlBuilder.append(" and " + ExpertSelected_.compositeScore.getName() + "=:compositeScore");
            sqlBuilder.setParam("compositeScore", compositeScore);
        }
        if (compositeScortEnd != null && compositeScortEnd > 0) {
            sqlBuilder.append(" and " + ExpertSelected_.compositeScoreEnd.getName() + "=:compositeScortEnd");
            sqlBuilder.setParam("compositeScortEnd", compositeScortEnd);
        }
        return returnIntBySql(sqlBuilder);
    }

    /**
     * 专家评审基本情况详细统计
     *
     * @param expertReviewCondDto
     * @return
     */
    @Override
    public ResultMsg expertReviewCondDetailCount(ExpertReviewCondDto expertReviewCondDto) {
        Map<String, Object> resultMap = new HashMap<>();
        HqlBuilder sqlBuilder = HqlBuilder.create();
        sqlBuilder.append("select t.* from (   ");
        sqlBuilder.append("select e.expertid,e.expertno,e.name,e.company,r.reviewdate,s.projectname,s.reviewstage,s.signid,a.isletterrw,a.isConfrim,a.isJoin  from cs_expert e ");
        sqlBuilder.append("left join ( select ces.*  from cs_expert_selected ces ");//判断专家抽取到和去参加
        sqlBuilder.append(" where  ces.isJoin='9' and ces.isConfrim='9' ) a  ");
     /*   sqlBuilder.append("left join cs_expert_selected a ");*/
        sqlBuilder.append("on e.expertid = a.expertid   ");
        sqlBuilder.append("left join cs_expert_review r  ");
        sqlBuilder.append("on a.expertreviewid = r.id  ");
        sqlBuilder.append("left join (select * from CS_SIGN t where signState <> '7' and signState <> '2' and (ispresign ='9' or ispresign  is null)) s  on r.businessid=s.signid) t  ");
        sqlBuilder.append("where t.expertid is not null  ");
        //添加查询条件
        if (Validate.isObject(expertReviewCondDto)) {
            if (Validate.isString(expertReviewCondDto.getName())) {
                sqlBuilder.append("and t.name like :expName ");
                sqlBuilder.setParam("expName", "%" + expertReviewCondDto.getName() + "%");
            }
            if (Validate.isString(expertReviewCondDto.getReviewtype())) {
                sqlBuilder.append("and t.isletterrw = :isletterrw ");
                sqlBuilder.setParam("isletterrw", expertReviewCondDto.getReviewtype());
            }
            if (Validate.isString(expertReviewCondDto.getBeginTime())) {
                String beginTime = expertReviewCondDto.getBeginTime() + " 00:00:00";
                sqlBuilder.append("and t.reviewdate >= to_date(:beginTime, 'yyyy-mm-dd hh24:mi:ss')  ");
                sqlBuilder.setParam("beginTime", beginTime);
            }
            if (StringUtil.isNotEmpty(expertReviewCondDto.getEndTime())) {
                String endTime = expertReviewCondDto.getEndTime() + " 23:59:59";
                sqlBuilder.append("and t.reviewdate <= to_date(:endTime, 'yyyy-mm-dd hh24:mi:ss')  ");
                sqlBuilder.setParam("endTime", endTime);
            }
        }
        sqlBuilder.append("order by t.expertno,t.isletterrw ");
        List<Object[]> expertReviewConList = expertSelectedRepo.getObjectArray(sqlBuilder);
        List<ExpertReviewCondDto> expertReviewConDtoList = new ArrayList<ExpertReviewCondDto>();
        String expertId = "";
        if (expertReviewConList.size() > 0) {
            for (int i = 0; i < expertReviewConList.size(); i++) {
                Object[] expertReviewCon = expertReviewConList.get(i);
                ExpertReviewCondDto expertReviewDto = new ExpertReviewCondDto();
                if (StringUtil.isNotEmpty(expertId) && null != expertReviewCon[0]) {
                    if (expertId.equals((String) expertReviewCon[0])) {
                        ExpertReviewCondBusDto expertReviewCondBusDto = new ExpertReviewCondBusDto();
                        if (null != expertReviewCon[4]) {
                            expertReviewCondBusDto.setReviewDate((Date) expertReviewCon[4]);
                        } else {
                            expertReviewCondBusDto.setReviewDate(null);
                        }
                        if (null != expertReviewCon[5]) {
                            expertReviewCondBusDto.setProjectName((String) expertReviewCon[5]);
                        } else {
                            expertReviewCondBusDto.setProjectName(null);
                        }
                        if (null != expertReviewCon[6]) {
                            expertReviewCondBusDto.setReviewStage((String) expertReviewCon[6]);
                        } else {
                            expertReviewCondBusDto.setReviewStage(null);
                        }
                        if (null != expertReviewCon[7]) {
                            expertReviewCondBusDto.setSignId((String) expertReviewCon[7]);
                        } else {
                            expertReviewCondBusDto.setSignId(null);
                        }
                        if (null != expertReviewCon[8]) {
                            expertReviewCondBusDto.setIsLetterRw((String) expertReviewCon[8]);
                        } else {
                            expertReviewCondBusDto.setIsLetterRw(null);
                        }
                        List<ExpertReviewCondBusDto> expertReviewCondBusDtoList = expertReviewConDtoList.get(expertReviewConDtoList.size() - 1).getExpertReviewCondBusDtoList();
                        expertReviewCondBusDtoList.add(expertReviewCondBusDto);
                        expertReviewConDtoList.get(expertReviewConDtoList.size() - 1).setExpertReviewCondBusDtoList(null);
                        expertReviewConDtoList.get(expertReviewConDtoList.size() - 1).setExpertReviewCondBusDtoList(expertReviewCondBusDtoList);
                        continue;
                    }
                }
                if (null != expertReviewCon[0]) {
                    expertReviewDto.setExpertID((String) expertReviewCon[0]);
                    expertId = expertReviewDto.getExpertID();
                } else {
                    expertReviewDto.setExpertID(null);
                }
                if (null != expertReviewCon[2]) {
                    expertReviewDto.setName((String) expertReviewCon[2]);
                } else {
                    expertReviewDto.setName(null);
                }

                if (null != expertReviewCon[3]) {
                    expertReviewDto.setComPany((String) expertReviewCon[3]);
                } else {
                    expertReviewDto.setComPany(null);
                }
                List<ExpertReviewCondBusDto> expertReviewCondBusDtoList = new ArrayList<ExpertReviewCondBusDto>();
                ExpertReviewCondBusDto expertReviewCondBusDto = new ExpertReviewCondBusDto();
                if (null != expertReviewCon[4]) {
                    expertReviewCondBusDto.setReviewDate((Date) expertReviewCon[4]);
                } else {
                    expertReviewCondBusDto.setReviewDate(null);
                }
                if (null != expertReviewCon[5]) {
                    expertReviewCondBusDto.setProjectName((String) expertReviewCon[5]);
                } else {
                    expertReviewCondBusDto.setProjectName(null);
                }
                if (null != expertReviewCon[6]) {
                    expertReviewCondBusDto.setReviewStage((String) expertReviewCon[6]);
                } else {
                    expertReviewCondBusDto.setReviewStage(null);
                }
                if (null != expertReviewCon[7]) {
                    expertReviewCondBusDto.setSignId((String) expertReviewCon[7]);
                } else {
                    expertReviewCondBusDto.setSignId(null);
                }
                if (null != expertReviewCon[8]) {
                    expertReviewCondBusDto.setIsLetterRw((String) expertReviewCon[8]);
                } else {
                    expertReviewCondBusDto.setIsLetterRw(null);
                }
                expertReviewCondBusDtoList.add(expertReviewCondBusDto);
                expertReviewDto.setExpertReviewCondBusDtoList(expertReviewCondBusDtoList);
                expertReviewConDtoList.add(expertReviewDto);
            }
        }
        resultMap.put("expertReviewConDtoList", expertReviewConDtoList);
        return new ResultMsg(true, Constant.MsgCode.OK.getValue(), "查询数据成功", resultMap);
    }

    /**
     * 专家评审基本情况综合统计
     *
     * @param expertReviewConSimpleDto
     * @return
     */
    @Override
    public ResultMsg expertReviewConSimpleCount(ExpertReviewConSimpleDto expertReviewConSimpleDto) {
        Map<String, Object> resultMap = new HashMap<>();
        //评审、函次数
        HqlBuilder sqlBuilder = HqlBuilder.create();
        sqlBuilder.append("select t.expertid,count(t.expertid)reviewCount,t.isletterrw from (   ");
        sqlBuilder.append("select  e.expertid,e.expertno,e.name,e.company,r.reviewdate,s.projectname,s.reviewstage,s.signid,a.isletterrw from cs_expert e    ");
        sqlBuilder.append("left join ( select ces.*  from cs_expert_selected ces ");//判断专家抽取到和去参加
        sqlBuilder.append(" where  ces.isJoin='9' and ces.isConfrim='9' ) a  ");
        sqlBuilder.append("on e.expertid = a.expertid   ");
        sqlBuilder.append("left join cs_expert_review r  ");
        sqlBuilder.append("on a.expertreviewid = r.id  ");
        sqlBuilder.append("left join cs_sign s  on r.businessid=s.signid) t  ");
        sqlBuilder.append("where t.expertid is not null   ");
        if (Validate.isObject(expertReviewConSimpleDto)) {
            if (Validate.isString(expertReviewConSimpleDto.getName())) {
                sqlBuilder.append("and t.name like :expName ");
                sqlBuilder.setParam("expName", "%" + expertReviewConSimpleDto.getName() + "%");
            }
            if (Validate.isString(expertReviewConSimpleDto.getReviewtype())) {
                sqlBuilder.append("and t.isletterrw = :isletterrw ");
                sqlBuilder.setParam("isletterrw", expertReviewConSimpleDto.getReviewtype());
            }
            if (StringUtil.isNotEmpty(expertReviewConSimpleDto.getBeginTime())) {
                String beginTime = expertReviewConSimpleDto.getBeginTime() + " 00:00:00";
                sqlBuilder.append("and t.reviewdate >= to_date(:beginTime, 'yyyy-mm-dd hh24:mi:ss')  ");
                sqlBuilder.setParam("beginTime", beginTime);
            }
            if (StringUtil.isNotEmpty(expertReviewConSimpleDto.getEndTime())) {
                String endTime = expertReviewConSimpleDto.getEndTime() + " 23:59:59";
                sqlBuilder.append("and t.reviewdate <= to_date(:endTime, 'yyyy-mm-dd hh24:mi:ss')  ");
                sqlBuilder.setParam("endTime", endTime);
            }
        }
        sqlBuilder.append("group by t.expertid,t.expertno,t.isletterrw   ");
        sqlBuilder.append("order by t.expertno  ");

        //评审总次数
        HqlBuilder sqlBuilder1 = HqlBuilder.create();
        sqlBuilder1.append("select t1.expertid,sum(t1.reviewCount) from (  ");
        sqlBuilder1.append("select t.expertid,t.expertno,count(t.expertid) reviewCount,t.isletterrw  from (   ");
        sqlBuilder1.append("select  e.expertid,e.expertno,e.name,e.company,r.reviewdate,s.projectname,s.reviewstage,s.signid,a.isletterrw from cs_expert e   ");
        sqlBuilder1.append("left join ( select ces.*  from cs_expert_selected ces ");//判断专家抽取到和去参加
        sqlBuilder1.append(" where  ces.isJoin='9' and ces.isConfrim='9' ) a  ");
        sqlBuilder1.append("on e.expertid = a.expertid   ");
        sqlBuilder1.append("left join cs_expert_review r  ");
        sqlBuilder1.append("on a.expertreviewid = r.id  ");
        sqlBuilder1.append("left join cs_sign s  on r.businessid=s.signid) t  ");
        sqlBuilder1.append(" where t.expertid is not null  ");
        if (Validate.isObject(expertReviewConSimpleDto)) {
            if (Validate.isString(expertReviewConSimpleDto.getName())) {
                sqlBuilder1.append("and t.name like :expName1 ");
                sqlBuilder1.setParam("expName1", "%" + expertReviewConSimpleDto.getName() + "%");
            }
            if (Validate.isString(expertReviewConSimpleDto.getReviewtype())) {
                sqlBuilder1.append("and t.isletterrw = :isletterrw1 ");
                sqlBuilder1.setParam("isletterrw1", expertReviewConSimpleDto.getReviewtype());
            }
            if (StringUtil.isNotEmpty(expertReviewConSimpleDto.getBeginTime())) {
                String beginTime = expertReviewConSimpleDto.getBeginTime() + " 00:00:00";
                sqlBuilder1.append("and t.reviewdate >= to_date(:beginTime1, 'yyyy-mm-dd hh24:mi:ss')  ");
                sqlBuilder1.setParam("beginTime1", beginTime);
            }
            if (StringUtil.isNotEmpty(expertReviewConSimpleDto.getEndTime())) {
                String endTime = expertReviewConSimpleDto.getEndTime() + " 23:59:59";
                sqlBuilder1.append("and t.reviewdate <= to_date(:endTime1, 'yyyy-mm-dd hh24:mi:ss')  ");
                sqlBuilder1.setParam("endTime1", endTime);
            }
        }
        sqlBuilder1.append("group by t.expertid,t.expertno,t.isletterrw ) t1  ");
        sqlBuilder1.append("group by t1.expertid,t1.expertno  ");
        sqlBuilder1.append("order by t1.expertno  ");

        List<Object[]> expertReviewConSimList = expertSelectedRepo.getObjectArray(sqlBuilder);
        List<Object[]> expertReviewConSimList1 = expertSelectedRepo.getObjectArray(sqlBuilder1);
        List<ExpertReviewConSimpleDto> expertRevConSimDtoList = new ArrayList<ExpertReviewConSimpleDto>();
        String expertId = "";
        if (expertReviewConSimList.size() > 0) {
            for (int i = 0; i < expertReviewConSimList.size(); i++) {
                Object[] expertReviewConSim = expertReviewConSimList.get(i);
                ExpertReviewConSimpleDto expertReviewSimDto = new ExpertReviewConSimpleDto();
                if (null != expertReviewConSim[0]) {
                    expertReviewSimDto.setExpertID((String) expertReviewConSim[0]);
                    if (expertId.equals(expertReviewSimDto.getExpertID())) {
                        ExpertReviewConSimpleDto expertReviewConSimpleDto1 = expertRevConSimDtoList.get(expertRevConSimDtoList.size() - 1);
                        if (null != expertReviewConSim[2]) {
                            String temp = (String) expertReviewConSim[2];
                            if ("0".equals(temp)) {//评审
                                if (null != expertReviewConSim[1]) {
                                    expertReviewConSimpleDto1.setReviewNum((BigDecimal) expertReviewConSim[1]);
                                } else {
                                    expertReviewConSimpleDto1.setReviewNum(null);
                                }
                            } else {//函评
                                if (null != expertReviewConSim[1]) {
                                    expertReviewConSimpleDto1.setLetterRwNum((BigDecimal) expertReviewConSim[1]);
                                } else {
                                    expertReviewConSimpleDto1.setLetterRwNum(null);
                                }
                            }
                        } else {//评审
                            if (null != expertReviewConSim[1]) {
                                expertReviewConSimpleDto1.setReviewNum((BigDecimal) expertReviewConSim[1]);
                            } else {
                                expertReviewConSimpleDto1.setReviewNum(null);
                            }
                        }
                        expertRevConSimDtoList.remove(expertRevConSimDtoList.size() - 1);
                        expertRevConSimDtoList.add(expertReviewConSimpleDto1);
                        continue;
                    }
                    expertId = expertReviewSimDto.getExpertID();
                    Expert expert = expertRepo.findById(expertReviewSimDto.getExpertID());
                    expertReviewSimDto.setName(expert.getName());
                    expertReviewSimDto.setComPany(expert.getComPany());
                    expertReviewSimDto.setTotalNum(getExpertRevTotalNum(expertReviewConSimList1, expertReviewSimDto.getExpertID()));
                } else {
                    expertReviewSimDto.setExpertID(null);
                }

                if (null != expertReviewConSim[2]) {
                    String temp = (String) expertReviewConSim[2];
                    if ("0".equals(temp)) {//评审
                        if (null != expertReviewConSim[1]) {
                            expertReviewSimDto.setReviewNum((BigDecimal) expertReviewConSim[1]);
                        } else {
                            expertReviewSimDto.setReviewNum(null);
                        }
                    } else {//函评
                        if (null != expertReviewConSim[1]) {
                            expertReviewSimDto.setLetterRwNum((BigDecimal) expertReviewConSim[1]);
                        } else {
                            expertReviewSimDto.setLetterRwNum(null);
                        }
                    }
                } else {//评审
                    if (null != expertReviewConSim[1]) {
                        expertReviewSimDto.setReviewNum((BigDecimal) expertReviewConSim[1]);
                    } else {
                        expertReviewSimDto.setReviewNum(null);
                    }
                }
                expertRevConSimDtoList.add(expertReviewSimDto);
            }

        }
        resultMap.put("expertRevConSimDtoList", expertRevConSimDtoList);
        return new ResultMsg(true, Constant.MsgCode.OK.getValue(), "查询数据成功", resultMap);
    }

    /**
     * 专家评审情况（不规则）
     *
     * @param expertReviewConSimpleDto
     * @return
     */
    @Override
    public ResultMsg expertReviewConComplicatedCount(ExpertReviewConSimpleDto expertReviewConSimpleDto) {
        Map<String, Object> resultMap = new HashMap<>();
        /*HqlBuilder sqlBuilder1 = HqlBuilder.create();
        sqlBuilder1.append("select t1.expertid,t1.name,t1.company,sum(t1.reviewCount) from (  ");
        sqlBuilder1.append("select t.expertid,t.expertno,t.name,t.company,count(t.expertid) reviewCount,t.isletterrw  from (   ");
        sqlBuilder1.append("select  e.expertid,e.expertno,e.name,e.company,r.reviewdate,s.projectname,s.reviewstage,s.signid,a.isletterrw from cs_expert e  ");
        sqlBuilder1.append("left join cs_expert_selected a ");
        sqlBuilder1.append("on e.expertid = a.expertid   ");
        sqlBuilder1.append("left join cs_expert_review r  ");
        sqlBuilder1.append("on a.expertreviewid = r.id  ");
        sqlBuilder1.append("left join cs_sign s  on r.businessid=s.signid) t  ");
        sqlBuilder1.append("where t.expertid is not null  ");
        /*     sqlBuilder1.append("left join cs_expert_review r  ");
        sqlBuilder1.append("on s.signid = r.businessid  ");
        sqlBuilder1.append("left join cs_expert_selected a  ");
        sqlBuilder1.append("on s.signid = a.businessid  ");
        sqlBuilder1.append("left join cs_expert e  ");
        sqlBuilder1.append(" on a.expertid = e.expertid) t  ");
        sqlBuilder1.append(" where t.expertid is not null  ");
        if(null != expertReviewConSimpleDto){
            if(StringUtil.isNotEmpty(expertReviewConSimpleDto.getName())){
                sqlBuilder1.append("and t.name like '%"+expertReviewConSimpleDto.getName()+"%'  ");
            }
            if(StringUtil.isNotEmpty(expertReviewConSimpleDto.getBeginTime())){
                String beginTime = expertReviewConSimpleDto.getBeginTime()+" 00:00:00";
                sqlBuilder1.append("and t.reviewdate >= to_date('"+beginTime+"', 'yyyy-mm-dd hh24:mi:ss')  ");
            }
            if(StringUtil.isNotEmpty(expertReviewConSimpleDto.getEndTime())){
                String endTime = expertReviewConSimpleDto.getEndTime()+" 23:59:59";
                sqlBuilder1.append("and t.reviewdate <= to_date('"+endTime+"', 'yyyy-mm-dd hh24:mi:ss')  ");
            }
        }
        sqlBuilder1.append("group by t.expertid,t.expertno,t.name,t.company,t.isletterrw ) t1  ");

        sqlBuilder1.append("group by t1.expertid,t1.expertno,t1.name,t1.company  ");
        sqlBuilder1.append("order by t1.expertno   ");
        List<Object[]> expertRevComplicateList = expertSelectedRepo.getObjectArray(sqlBuilder1);
        List<Object []> expertRevCompliDtoList = getExistsConComplicated(expertReviewConSimpleDto,expertRevComplicateList);
        List<ExpertReviewConSimpleDto> expertRevConCompDtoList = new ArrayList<ExpertReviewConSimpleDto>();*/
        //日期条件
        StringBuffer dateCondition = new StringBuffer();
        if (StringUtil.isNotEmpty(expertReviewConSimpleDto.getBeginTime())) {
            String beginTime = expertReviewConSimpleDto.getBeginTime();
            dateCondition.append("and cer.reviewdate >= to_date('" + beginTime + "', 'yyyy-mm-dd')  ");
        }
        if (StringUtil.isNotEmpty(expertReviewConSimpleDto.getEndTime())) {
            String endTime = expertReviewConSimpleDto.getEndTime();
            dateCondition.append("and cer.reviewdate <= to_date('" + endTime + "', 'yyyy-mm-dd')  ");
        }
        //开始查询
        HqlBuilder sqlBuilder = HqlBuilder.create();
        sqlBuilder.append(" SELECT tt.expertid,tt.name,tt.company, tce.totalCount ");
        sqlBuilder.append(" FROM (SELECT ce.expertid, ce.name, ce.company FROM cs_expert ce,");
        //查询一周内超过2次抽取和一个月内，超过4次抽取的专家
        sqlBuilder.append(" (  SELECT DISTINCT EXPERTID FROM ( SELECT TO_CHAR (NEXT_DAY (CER.REVIEWDATE + 15 / 24 - 7, 2),'YYYY-MM-DD') AS weekDate, ");
        sqlBuilder.append(" COUNT (ces.EXPERTID) AS countValue, ces.EXPERTID AS EXPERTID ");
        sqlBuilder.append(" FROM CS_EXPERT_SELECTED ces LEFT JOIN CS_EXPERT_REVIEW cer ON ces.EXPERTREVIEWID = cer.ID ");
        sqlBuilder.append(" WHERE 1=1 ").append(dateCondition.toString());
        sqlBuilder.append(" GROUP BY TO_CHAR (NEXT_DAY (CER.REVIEWDATE + 15 / 24 - 7, 2),'YYYY-MM-DD'), ces.EXPERTID ");
        sqlBuilder.append(" HAVING COUNT (ces.EXPERTID) > 2 ");
        sqlBuilder.append(" UNION SELECT TO_CHAR (CER.REVIEWDATE, 'YYYY-MM') AS montDay,");
        sqlBuilder.append(" COUNT (ces.EXPERTID) AS countValue, ces.EXPERTID AS EXPERTID ");
        sqlBuilder.append(" FROM CS_EXPERT_SELECTED ces LEFT JOIN CS_EXPERT_REVIEW cer ON ces.EXPERTREVIEWID = cer.ID ");
        sqlBuilder.append(" WHERE 1=1 ").append(dateCondition.toString());
        sqlBuilder.append(" GROUP BY TO_CHAR (CER.REVIEWDATE, 'YYYY-MM'), ces.EXPERTID ");
        sqlBuilder.append(" HAVING COUNT (ces.EXPERTID) > 4)) hse ");
        sqlBuilder.append(" WHERE hse.EXPERTID = ce.EXPERTID) tt ");
        sqlBuilder.append(" LEFT JOIN (  SELECT csc.expertid AS expertid, COUNT (csc.expertid) totalCount ");
        sqlBuilder.append(" FROM CS_EXPERT_SELECTED csc LEFT JOIN CS_EXPERT_REVIEW cer ON csc.EXPERTREVIEWID = cer.ID ");
        sqlBuilder.append(" WHERE 1=1 ").append(dateCondition.toString());
        sqlBuilder.append(" GROUP BY csc.expertid) tce ");
        sqlBuilder.append(" ON tt.expertid = tce.expertid ");
        sqlBuilder.append(" WHERE 1=1 ");
        if (Validate.isString(expertReviewConSimpleDto.getName())) {
            sqlBuilder.append(" and tt.name like '%" + expertReviewConSimpleDto.getName() + "%' ");
        }
        List<Object[]> expertRevCompliDtoList = expertSelectedRepo.getObjectArray(sqlBuilder);
        List<ExpertReviewConSimpleDto> expertRevConCompDtoList = new ArrayList<>();
        if (Validate.isList(expertRevCompliDtoList)) {
            for (int i = 0; i < expertRevCompliDtoList.size(); i++) {
                Object[] tempArr = expertRevCompliDtoList.get(i);
                ExpertReviewConSimpleDto expertReviewConSimpleDto1 = new ExpertReviewConSimpleDto();
                if (null != tempArr[0]) {
                    expertReviewConSimpleDto1.setExpertID((String) tempArr[0]);
                }
                if (null != tempArr[1]) {
                    expertReviewConSimpleDto1.setName((String) tempArr[1]);
                }
                if (null != tempArr[2]) {
                    expertReviewConSimpleDto1.setComPany((String) tempArr[2]);
                }
                if (null != tempArr[3]) {
                    expertReviewConSimpleDto1.setTotalNum((BigDecimal) tempArr[3]);
                }
                expertRevConCompDtoList.add(expertReviewConSimpleDto1);
            }
        }
        resultMap.put("expertRevConCompDtoList", expertRevConCompDtoList);
        return new ResultMsg(true, Constant.MsgCode.OK.getValue(), "查询数据成功", resultMap);
    }

    /**
     * 项目评审情况(按类别)
     *
     * @param projectReviewConditionDto
     * @return
     */
    @Override
    public ResultMsg proReviewConditionByTypeCount(ProReviewConditionDto projectReviewConditionDto) {

        Map<String, Object> resultMap = new HashMap<>();
        HqlBuilder sqlBuilder = HqlBuilder.create();
        sqlBuilder.append("select p.projecttype,count(s.projectcode) from cs_sign s   ");
        sqlBuilder.append("left join cs_work_program p  ");
        sqlBuilder.append("on s.signid = p.signid  ");
        sqlBuilder.append("left join cs_dispatch_doc d   ");
        sqlBuilder.append("on s.signid = d.signid   ");
        sqlBuilder.append("where 1 = 1 ");
//        sqlBuilder.append("and s.signstate = '9'  ");
        sqlBuilder.append("and s.signstate != '7' ");//过滤删除
        sqlBuilder.append("and s.processstate >= 6  ");//已发文
        sqlBuilder.append("and  ( s.ispresign != '0' or s.ispresign is null )  "); //排除预签收

        //添加查询条件
        if (Validate.isObject(projectReviewConditionDto)) {
            if (Validate.isString(projectReviewConditionDto.getBeginTime()) && Validate.isString(projectReviewConditionDto.getEndTime())) {
                String beginTime = projectReviewConditionDto.getBeginTime() + "-01 00:00:00";
                String[] timeArr = projectReviewConditionDto.getEndTime().split("-");
                String day = "";
                day = DateUtils.getMaxDayOfMonth(Integer.parseInt(timeArr[0]), (Integer.parseInt(timeArr[1]))) + "";
                String endTime = projectReviewConditionDto.getEndTime() + "-" + day + " 23:59:59";
                sqlBuilder.append("and D.DISPATCHDATE >= to_date('" + beginTime + "', 'yyyy-mm-dd hh24:mi:ss') ");
                sqlBuilder.append("and D.DISPATCHDATE <= to_date('" + endTime + "', 'yyyy-mm-dd hh24:mi:ss') ");
            } else if (Validate.isString(projectReviewConditionDto.getBeginTime()) && !Validate.isString(projectReviewConditionDto.getEndTime())) {
                String[] timeArr = projectReviewConditionDto.getBeginTime().split("-");
                String day = "";
                day = DateUtils.getMaxDayOfMonth(Integer.parseInt(timeArr[0]), (Integer.parseInt(timeArr[1]))) + "";
                String beginTime = projectReviewConditionDto.getBeginTime() + "-01 00:00:00";
                String endTime = projectReviewConditionDto.getBeginTime() + "-" + day + " 23:59:59";
                sqlBuilder.append("and D.DISPATCHDATE >= to_date('" + beginTime + "', 'yyyy-mm-dd hh24:mi:ss') ");
                sqlBuilder.append("and D.DISPATCHDATE <= to_date('" + endTime + "', 'yyyy-mm-dd hh24:mi:ss') ");
            } else if (Validate.isString(projectReviewConditionDto.getEndTime()) && !Validate.isString(projectReviewConditionDto.getBeginTime())) {
                String[] timeArr = projectReviewConditionDto.getEndTime().split("-");
                ;
                String day = "";
                day = DateUtils.getMaxDayOfMonth(Integer.parseInt(timeArr[0]), (Integer.parseInt(timeArr[1]))) + "";
                String beginTime = projectReviewConditionDto.getEndTime() + "-01 00:00:00";
                String endTime = projectReviewConditionDto.getEndTime() + "-" + day + " 23:59:59";
                sqlBuilder.append("and D.DISPATCHDATE >= to_date('" + beginTime + "', 'yyyy-mm-dd hh24:mi:ss') ");
                sqlBuilder.append("and D.DISPATCHDATE <= to_date('" + endTime + "', 'yyyy-mm-dd hh24:mi:ss') ");
            }
        }
        sqlBuilder.append("group by p.projecttype ");
        sqlBuilder.append("having p.projecttype is not null  ");
        List<Object[]> projectReviewConList = expertSelectedRepo.getObjectArray(sqlBuilder);
        List<ProReviewConditionDto> projectReviewConDtoList = new ArrayList<ProReviewConditionDto>();
        Integer totalNum = 0;
        if (projectReviewConList.size() > 0) {
            for (int i = 0; i < projectReviewConList.size(); i++) {
                Object[] projectReviewCon = projectReviewConList.get(i);
                ProReviewConditionDto proReviewConditionDto = new ProReviewConditionDto();
                if (null != projectReviewCon[0]) {
                    proReviewConditionDto.setProjectType((String) projectReviewCon[0]);
                } else {
                    proReviewConditionDto.setProjectType(null);
                }
                if (null != projectReviewCon[1]) {
                    proReviewConditionDto.setProjectTypeCount((BigDecimal) projectReviewCon[1]);
                    totalNum += proReviewConditionDto.getProjectTypeCount().intValue();
                }

                projectReviewConDtoList.add(proReviewConditionDto);
            }
        }
        resultMap.put("protReviewConditionList", projectReviewConDtoList);
        resultMap.put("totalNum", totalNum);
        return new ResultMsg(true, Constant.MsgCode.OK.getValue(), "查询数据成功", resultMap);
    }

    /**
     * 项目评审情况汇总
     *
     * @param projectReviewConditionDto
     * @return
     */
    @Override
    public ProReviewConditionDto proReviewConditionSum(ProReviewConditionDto projectReviewConditionDto) {
        HqlBuilder sqlBuilder = HqlBuilder.create();
        sqlBuilder.append("select sum(projectcount),sum(declarevalue),sum(authorizevalue),sum(ljhj), decode(sum(declarevalue),0,0,round(sum(ljhj) / sum(declarevalue), 4) * 100) hjl from (  ");
        sqlBuilder.append("select s.reviewstage, count(s.projectcode) projectcount,sum(d.declarevalue)/10000 declarevalue,sum(d.authorizevalue)/10000 authorizevalue,(sum(d.declarevalue) -sum(d.authorizevalue))/10000 ljhj  from cs_sign s  ");
        sqlBuilder.append("left join cs_dispatch_doc d   ");
        sqlBuilder.append("on s.signid = d.signid   ");
        sqlBuilder.append("where 1 = 1 ");
//        sqlBuilder.append("and s.signstate = '9'  ");
        sqlBuilder.append("and s.signstate != '7' ");//过滤删除
//        sqlBuilder.append("and D.DISPATCHTYPE != '项目退文' ");//过滤退文项目
//        sqlBuilder.append("and s.processstate >= 6  ");//已发文
        sqlBuilder.append("and  ( s.ispresign != '0' or s.ispresign is null )  "); //排除预签收
        //添加查询条件
        if (Validate.isObject(projectReviewConditionDto)) {
            if (Validate.isString(projectReviewConditionDto.getBeginTime()) && Validate.isString(projectReviewConditionDto.getEndTime())) {
                String beginTime = projectReviewConditionDto.getBeginTime() + "-01 00:00:00";
                String[] timeArr = projectReviewConditionDto.getEndTime().split("-");
                String day = "";
                day = DateUtils.getMaxDayOfMonth(Integer.parseInt(timeArr[0]), (Integer.parseInt(timeArr[1]))) + "";
                String endTime = projectReviewConditionDto.getEndTime() + "-" + day + " 23:59:59";
                sqlBuilder.append("and D.DISPATCHDATE >= to_date('" + beginTime + "', 'yyyy-mm-dd hh24:mi:ss') ");
                sqlBuilder.append("and D.DISPATCHDATE <= to_date('" + endTime + "', 'yyyy-mm-dd hh24:mi:ss') ");
            } else if (Validate.isString(projectReviewConditionDto.getBeginTime()) && !Validate.isString(projectReviewConditionDto.getEndTime())) {
                String[] timeArr = projectReviewConditionDto.getBeginTime().split("-");
                String day = "";
                day = DateUtils.getMaxDayOfMonth(Integer.parseInt(timeArr[0]), (Integer.parseInt(timeArr[1]))) + "";
                String beginTime = projectReviewConditionDto.getBeginTime() + "-01 00:00:00";
                String endTime = projectReviewConditionDto.getBeginTime() + "-" + day + " 23:59:59";
                sqlBuilder.append("and D.DISPATCHDATE >= to_date('" + beginTime + "', 'yyyy-mm-dd hh24:mi:ss') ");
                sqlBuilder.append("and D.DISPATCHDATE <= to_date('" + endTime + "', 'yyyy-mm-dd hh24:mi:ss') ");
            } else if (Validate.isString(projectReviewConditionDto.getEndTime()) && !Validate.isString(projectReviewConditionDto.getBeginTime())) {
                String[] timeArr = projectReviewConditionDto.getEndTime().split("-");
                ;
                String day = "";
                day = DateUtils.getMaxDayOfMonth(Integer.parseInt(timeArr[0]), (Integer.parseInt(timeArr[1]))) + "";
                String beginTime = projectReviewConditionDto.getEndTime() + "-01 00:00:00";
                String endTime = projectReviewConditionDto.getEndTime() + "-" + day + " 23:59:59";
                sqlBuilder.append("and D.DISPATCHDATE >= to_date('" + beginTime + "', 'yyyy-mm-dd hh24:mi:ss') ");
                sqlBuilder.append("and D.DISPATCHDATE <= to_date('" + endTime + "', 'yyyy-mm-dd hh24:mi:ss') ");
            }
        }
        sqlBuilder.append("group by s.reviewstage ) ");
        List<Object[]> projectReviewConList = expertSelectedRepo.getObjectArray(sqlBuilder);
        ProReviewConditionDto proReviewConditionDto = new ProReviewConditionDto();
        if (projectReviewConList.size() > 0) {
            Object[] projectReviewCon = projectReviewConList.get(0);

            if (null != projectReviewCon[0]) {
                proReviewConditionDto.setProCount((BigDecimal) projectReviewCon[0]);
            }
            if (null != projectReviewCon[1]) {
                proReviewConditionDto.setDeclareValue((BigDecimal) projectReviewCon[1]);
            } else {
                proReviewConditionDto.setDeclareValue(null);
            }
            if (null != projectReviewCon[2]) {
                proReviewConditionDto.setAuthorizeValue((BigDecimal) projectReviewCon[2]);
            } else {
                proReviewConditionDto.setAuthorizeValue(null);
            }
            if (null != projectReviewCon[3]) {
                proReviewConditionDto.setLjhj((BigDecimal) projectReviewCon[3]);
            } else {
                proReviewConditionDto.setLjhj(null);
            }
            if (null != projectReviewCon[4]) {
                proReviewConditionDto.setHjl((BigDecimal) projectReviewCon[4]);
            } else {
                proReviewConditionDto.setLjhj(null);
            }
            // projectReviewConDtoList.add(proReviewConditionDto);

        }
        return proReviewConditionDto;
    }


    /**
     * 获取退文金额汇总
     *
     * @param projectReviewConditionDto
     * @return
     */
    @Override
    public ProReviewConditionDto getBackDispatchSum(ProReviewConditionDto projectReviewConditionDto) {
        HqlBuilder sqlBuilder = HqlBuilder.create();
        sqlBuilder.append("select sum(projectcount),sum(declarevalue),sum(authorizevalue),sum(ljhj), decode(sum(declarevalue),0,0,round(sum(ljhj) / sum(declarevalue), 4) * 100) hjl from (  ");
        sqlBuilder.append("select s.reviewstage, count(s.projectcode) projectcount,sum(d.declarevalue)/10000 declarevalue,sum(d.authorizevalue)/10000 authorizevalue,(sum(d.declarevalue) -sum(d.authorizevalue))/10000 ljhj  from cs_sign s  ");
        sqlBuilder.append("left join cs_dispatch_doc d   ");
        sqlBuilder.append("on s.signid = d.signid   ");
        sqlBuilder.append("where 1 = 1 ");
//        sqlBuilder.append("and s.signstate = '9'  ");
        sqlBuilder.append("and s.signstate != '7' ");//过滤删除
        sqlBuilder.append("and D.DISPATCHTYPE = '项目退文函' ");
        sqlBuilder.append("and s.processstate >= 6  ");//已发文
        sqlBuilder.append("and  ( s.ispresign != '0' or s.ispresign is null )  "); //排除预签收
        //添加查询条件
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
                ;
                String day = "";
                day = DateUtils.getMaxDayOfMonth(Integer.parseInt(timeArr[0]), (Integer.parseInt(timeArr[1]))) + "";
                String beginTime = projectReviewConditionDto.getEndTime() + "-01 00:00:00";
                String endTime = projectReviewConditionDto.getEndTime() + "-" + day + " 23:59:59";
                sqlBuilder.append("and D.DISPATCHDATE >= to_date('" + beginTime + "', 'yyyy-mm-dd hh24:mi:ss') ");
                sqlBuilder.append("and D.DISPATCHDATE <= to_date('" + endTime + "', 'yyyy-mm-dd hh24:mi:ss') ");
            }
        }
        sqlBuilder.append("group by s.reviewstage ) ");
        List<Object[]> projectReviewConList = expertSelectedRepo.getObjectArray(sqlBuilder);
        ProReviewConditionDto proReviewConditionDto = new ProReviewConditionDto();
        if (projectReviewConList.size() > 0) {
            Object[] projectReviewCon = projectReviewConList.get(0);

            if (null != projectReviewCon[0]) {
                proReviewConditionDto.setProCount((BigDecimal) projectReviewCon[0]);
            }
            if (null != projectReviewCon[1]) {
                proReviewConditionDto.setDeclareValue((BigDecimal) projectReviewCon[1]);
            } else {
                proReviewConditionDto.setDeclareValue(null);
            }
            if (null != projectReviewCon[2]) {
                proReviewConditionDto.setAuthorizeValue((BigDecimal) projectReviewCon[2]);
            } else {
                proReviewConditionDto.setAuthorizeValue(null);
            }
            if (null != projectReviewCon[3]) {
                proReviewConditionDto.setLjhj((BigDecimal) projectReviewCon[3]);
            } else {
                proReviewConditionDto.setLjhj(null);
            }
            if (null != projectReviewCon[4]) {
                proReviewConditionDto.setHjl((BigDecimal) projectReviewCon[4]);
            } else {
                proReviewConditionDto.setLjhj(null);
            }
            // projectReviewConDtoList.add(proReviewConditionDto);

        }
        return proReviewConditionDto;
    }


    /**
     * 获取完成项目评审数
     *
     * @param projectReviewConditionDto
     * @return
     */
    @Override
    public Integer proReviewMeetingCount(ProReviewConditionDto projectReviewConditionDto) {
        Integer proCount = 0;
        HqlBuilder sqlBuilder = HqlBuilder.create();
        sqlBuilder.append("select count(s.projectcode) from cs_sign s ");
        sqlBuilder.append("left join cs_dispatch_doc d   ");
        sqlBuilder.append("on s.signid = d.signid   ");
        sqlBuilder.append("where 1 = 1 ");
        // sqlBuilder.append("and s.signstate='9'  ");
        sqlBuilder.append("and s.signstate != '7' ");//过滤删除
        sqlBuilder.append("and s.processstate >= 6  ");//已发文
        sqlBuilder.append("and  ( s.ispresign != '0' or s.ispresign is null )  "); //排除预签收
        if (null != projectReviewConditionDto) {
            if (StringUtil.isNotEmpty(projectReviewConditionDto.getBeginTime())) {
                String[] timeArr = projectReviewConditionDto.getBeginTime().split("-");
                String day = "";
                day = DateUtils.getMaxDayOfMonth(Integer.parseInt(timeArr[0]), (Integer.parseInt(timeArr[1]))) + "";
                String beginTime = projectReviewConditionDto.getBeginTime() + "-01 00:00:00";
                String endTime = projectReviewConditionDto.getBeginTime() + "-" + day + " 23:59:59";
                sqlBuilder.append("and D.DISPATCHDATE >= to_date('" + beginTime + "', 'yyyy-mm-dd hh24:mi:ss') ");
                sqlBuilder.append("and D.DISPATCHDATE <= to_date('" + endTime + "', 'yyyy-mm-dd hh24:mi:ss') ");
            }
        }
        List projectReviewConList = expertSelectedRepo.getObjectArray(sqlBuilder);
        if (projectReviewConList.size() > 0) {
            if (null != projectReviewConList.get(0)) {
                proCount = Integer.valueOf(String.valueOf(projectReviewConList.get(0)));
            }
        }
        return proCount;
    }

    /**
     * 项目签收次数
     *
     * @param projectReviewConditionDto
     * @return
     */
    @Override
    public Integer proReviewCount(ProReviewConditionDto projectReviewConditionDto) {
        Integer proCount = 0;
        HqlBuilder sqlBuilder = HqlBuilder.create();
        sqlBuilder.append("select count(s.projectcode) from cs_sign s  ");
//        sqlBuilder.append("left join cs_dispatch_doc d ");
//        sqlBuilder.append("on s.signid = d.signid ");
        sqlBuilder.append("where 1 = 1 ");
        //sqlBuilder.append("and s.signstate = '1'  ");
        sqlBuilder.append("and s.signstate != '7' ");//过滤删除
//        sqlBuilder.append("and s.processstate >= 6  ");//已发文
        sqlBuilder.append("and  ( s.ispresign != '0' or s.ispresign is null )  "); //排除预签收
        if (null != projectReviewConditionDto) {
            if (StringUtil.isNotEmpty(projectReviewConditionDto.getBeginTime())) {
                String[] timeArr = projectReviewConditionDto.getBeginTime().split("-");
                String day = "";
                day = DateUtils.getMaxDayOfMonth(Integer.parseInt(timeArr[0]), (Integer.parseInt(timeArr[1]))) + "";
                String beginTime = projectReviewConditionDto.getBeginTime() + "-01 00:00:00";
                String endTime = projectReviewConditionDto.getBeginTime() + "-" + day + " 23:59:59";
                sqlBuilder.append("and s.signdate >= to_date('" + beginTime + "', 'yyyy-mm-dd hh24:mi:ss') ");
                sqlBuilder.append("and s.signdate <= to_date('" + endTime + "', 'yyyy-mm-dd hh24:mi:ss') ");
            }
        }
        List projectReviewConList = expertSelectedRepo.getObjectArray(sqlBuilder);
        if (projectReviewConList.size() > 0) {
            if (null != projectReviewConList.get(0)) {
                proCount = Integer.valueOf(String.valueOf(projectReviewConList.get(0)));
            }
        }
        return proCount;
    }

    /**
     * 根据业务ID，更改抽取专家的评审方式
     *
     * @param businessId
     * @param propertyname
     * @param value
     */
    @Override
    public void updateExpertSelectState(String businessId, String propertyname, String value) {
        HqlBuilder hqlBuilder = HqlBuilder.create();
        hqlBuilder.append(" update " + ExpertSelected.class.getSimpleName() + " set " + propertyname + " =:pvalue ");
        hqlBuilder.setParam("pvalue", value);
        hqlBuilder.append(" where " + ExpertSelected_.businessId.getName() + " =:businessId ");
        hqlBuilder.setParam("businessId", businessId);
        executeHql(hqlBuilder);
    }

    /*/**
     * 业绩汇总
     *
     * @param achievementSumDto
     * @return
     */
    /*@Override
    public List<AchievementSumDto> findAchievementSum(AchievementSumDto achievementSumDto, Map<String, Object> levelMap, List<OrgDept> orgDeptList) {

        Integer level = 0;
        if (null != levelMap && null != levelMap.get("leaderFlag")) {
            level = (Integer) levelMap.get("leaderFlag");
        }
        HqlBuilder sqlBuilder = HqlBuilder.create();
        sqlBuilder.append(" select count(1) disSum,round(sum(t.declarevalue)/10000,2) declarevalueSum ,");
        sqlBuilder.append(" round(sum(t.authorizevalue)/10000,2) authorizevalueSum,");
        sqlBuilder.append(" round((sum(t.declarevalue)-sum(t.authorizevalue))/10000,2) extravalueSum,");
        sqlBuilder.append(" decode(sum(t.declarevalue),  0,  0,");
        sqlBuilder.append(" round(((sum(t.declarevalue) - sum(t.authorizevalue)) / 10000) /(sum(t.declarevalue) / 10000),");
        sqlBuilder.append(" 4) * 100) extraRateSum,t.ismainuser ");
        sqlBuilder.append(" from ( select d.dispatchdate, s.projectname,d.filenum,d.declarevalue,d.authorizevalue,");
        sqlBuilder.append(" d.extravalue,d.extrarate,p.ismainuser from cs_sign s ");
        sqlBuilder.append(" left join cs_dispatch_doc d on s.signid = d.signid left join CS_SIGN_PRINCIPAL2 p");

        if (Validate.isString(achievementSumDto.getUserId())) {
            sqlBuilder.append(" on s.signid = p.signid where s.processstate >= 6 and s.signstate != 7 and p.ismainuser is not null ");
            sqlBuilder.append(" and p.userid = :puserId ").setParam("puserId", achievementSumDto.getUserId());
        } else {
            if (level == 0) {
                sqlBuilder.append(" on s.signid = p.signid where s.processstate >= 6  and s.signstate != 7 ");
                sqlBuilder.append(" and p.ismainuser is not null  and p.userid = :muserid ");
                sqlBuilder.setParam("muserid", SessionUtil.getUserId());
            } else if (level == 1 || level == 2 || level == 3) {
                StringBuilder orgIds = new StringBuilder();
                if (Validate.isList(orgDeptList)) {
                    for (int i = 0; i < orgDeptList.size(); i++) {
                        if (i > 0) {
                            orgIds.append(",");
                        }
                        orgIds.append(orgDeptList.get(i).getId());
                    }
                }
                sqlBuilder.append(" on s.signid = p.signid where s.processstate >= 6  and s.signstate != 7 ");
                sqlBuilder.append(" and p.ismainuser is not null  and exists(select 1 from cs_user u ");
                sqlBuilder.bulidPropotyString("where", "u.orgid", orgIds.toString());
                sqlBuilder.append(" and u.id = p.userid ) ");
                sqlBuilder.append(" and not exists(select 1 from CS_DEPT_CS_USER u where u.userlist_id = p.userid )");
            } else if (level == 4) {
                sqlBuilder.append(" on s.signid = p.signid where s.processstate >= 6  and s.signstate != 7 ");
                sqlBuilder.append(" and p.ismainuser is not null  ");
                sqlBuilder.append(" and exists(select 1 from CS_DEPT_CS_USER u where u.userlist_id = p.userid )");
            }
        }

        if (null != achievementSumDto) {
            String yearName = achievementSumDto.getYear();
            String quartterType = achievementSumDto.getQuarter();
            bulidDateQueryCondition(sqlBuilder,yearName,quartterType);
        }
        sqlBuilder.append(" ) t  group by ismainuser ");
        List<Object[]> achievementSumObjList = expertSelectedRepo.getObjectArray(sqlBuilder);
        List<AchievementSumDto> achievementSumDtoList = new ArrayList<>();
        for (int i = 0, l = achievementSumObjList.size(); i < l; i++) {
            AchievementSumDto achievementSumDto1 = new AchievementSumDto();
            Object[] projectReviewCon = achievementSumObjList.get(i);
            if (null != projectReviewCon[0]) {
                achievementSumDto1.setDisSum(Integer.valueOf(String.valueOf(projectReviewCon[0])));
            } else {
                achievementSumDto1.setDisSum(0);
            }
            if (null != projectReviewCon[1]) {
                achievementSumDto1.setDeclarevalueSum((BigDecimal) projectReviewCon[1]);
            } else {
                achievementSumDto1.setDeclarevalueSum(BigDecimal.ZERO);
            }
            if (null != projectReviewCon[2]) {
                achievementSumDto1.setAuthorizevalueSum((BigDecimal) projectReviewCon[2]);
            } else {
                achievementSumDto1.setDeclarevalueSum(BigDecimal.ZERO);
            }
            if (null != projectReviewCon[3]) {
                achievementSumDto1.setExtravalueSum((BigDecimal) projectReviewCon[3]);
            } else {
                achievementSumDto1.setExtravalueSum(BigDecimal.ZERO);
            }
            if (null != projectReviewCon[4]) {
                achievementSumDto1.setExtraRateSum((BigDecimal) projectReviewCon[4]);
            } else {
                achievementSumDto1.setExtraRateSum(BigDecimal.ZERO);
            }

            if (null != projectReviewCon[5]) {
                achievementSumDto1.setIsmainuser((String) projectReviewCon[5]);
            } else {
                achievementSumDto1.setIsmainuser(null);
            }
            achievementSumDtoList.add(achievementSumDto1);
        }
        return achievementSumDtoList;
    }*/

    /*/**
     * 业绩明细
     *
     * @param achievementSumDto
     * @return
     */
   /* @Override
    public List<AchievementDetailDto> findAchievementDetail(AchievementSumDto achievementSumDto, Map<String, Object> levelMap, List<OrgDept> orgDeptList) {
        String beginTime = "";
        String endTime = "";
        Integer level = 0;
        if (null != levelMap && null != levelMap.get("leaderFlag")) {
            level = (Integer) levelMap.get("leaderFlag");
        }
        HqlBuilder sqlBuilder = HqlBuilder.create();
        sqlBuilder.append("select d.dispatchdate, s.projectname,d.filenum,d.declarevalue,d.authorizevalue,d.extravalue," +
                " d.extrarate,p.ismainuser from cs_sign s ");
        sqlBuilder.append(" left join cs_dispatch_doc d on s.signid = d.signid left join CS_SIGN_PRINCIPAL2 p" +
                " on s.signid = p.signid");
        StringBuilder orgIds = new StringBuilder();
        if (Validate.isObject(achievementSumDto) && Validate.isString(achievementSumDto.getUserId())) {
            sqlBuilder.append(" where s.processstate >= 6  and s.signstate != 7 and p.userid = '" + achievementSumDto.getUserId() + "' ");
        } else {
            if (level == 0) {
                sqlBuilder.append(" where s.processstate >= 6  and s.signstate != 7 and p.userid = '" + SessionUtil.getUserId() + "' ");
            } else if (level == 1 || level == 2 || level == 3) {
                List<String> tempList = StringUtil.getSplit(achievementSumDto.getDeptIds(), ",");
                if (!Validate.isList(tempList)) {
                    if (Validate.isList(orgDeptList)) {
                        for (int i = 0; i < orgDeptList.size(); i++) {
                            orgIds.append("'" + orgDeptList.get(i).getId() + "'");
                            if (i != orgDeptList.size() - 1) {
                                orgIds.append(",");
                            }
                        }
                    }
                } else {
                    for (int i = 0; i < tempList.size(); i++) {
                        orgIds.append("'" + tempList.get(i) + "'");
                        if (i != tempList.size() - 1) {
                            orgIds.append(",");
                        }
                    }
                }
                sqlBuilder.append(" where s.processstate >= 6  and s.signstate != 7 " +
                        " and exists(select 1 from cs_user u " +
                        "  where u.orgid in (" + orgIds + ") " +
                        "  and u.id = p.userid) " +
                        " and not exists(select 1 from CS_DEPT_CS_USER u where u.userlist_id = p.userid )");
            } else if (level == 4) {
                sqlBuilder.append(" where s.processstate >= 6  and s.signstate != 7 and exists(select 1 from CS_DEPT_CS_USER u where u.userlist_id = p.userid ) ");
            }
        }

        if (null != achievementSumDto) {
            if (Validate.isString(achievementSumDto.getIsmainuser())) {
                sqlBuilder.append(" and p.ismainuser =:ismainuser ").setParam("ismainuser", achievementSumDto.getIsmainuser());
            }
            if (Validate.isString(achievementSumDto.getYear()) && Validate.isString(achievementSumDto.getQuarter())) {
                if (achievementSumDto.getQuarter().equals("0")) {
                    beginTime = achievementSumDto.getYear() + "-01-01 00:00:00";
                    endTime = achievementSumDto.getYear() + "-12-31 23:59:59";
                    sqlBuilder.append(" and D.DISPATCHDATE >= to_date(:beginTime,'yyyy-mm-dd hh24:mi:ss') ").setParam("beginTime", beginTime);
                    sqlBuilder.append(" and D.DISPATCHDATE <= to_date(:endTime,'yyyy-mm-dd hh24:mi:ss') ").setParam("endTime", endTime);
                } else if (achievementSumDto.getQuarter().equals("1")) {
                    beginTime = achievementSumDto.getYear() + "-01-01 00:00:00";
                    endTime = achievementSumDto.getYear() + "-03-31 23:59:59";
                    sqlBuilder.append(" and D.DISPATCHDATE >= to_date(:beginTime,'yyyy-mm-dd hh24:mi:ss') ").setParam("beginTime", beginTime);
                    sqlBuilder.append(" and D.DISPATCHDATE <= to_date(:endTime,'yyyy-mm-dd hh24:mi:ss') ").setParam("endTime", endTime);
                } else if (achievementSumDto.getQuarter().equals("2")) {
                    beginTime = achievementSumDto.getYear() + "-04-01 00:00:00";
                    endTime = achievementSumDto.getYear() + "-06-30 23:59:59";
                    sqlBuilder.append(" and D.DISPATCHDATE >= to_date(:beginTime,'yyyy-mm-dd hh24:mi:ss') ").setParam("beginTime", beginTime);
                    sqlBuilder.append(" and D.DISPATCHDATE <= to_date(:endTime,'yyyy-mm-dd hh24:mi:ss') ").setParam("endTime", endTime);
                } else if (achievementSumDto.getQuarter().equals("3")) {
                    beginTime = achievementSumDto.getYear() + "-07-01 00:00:00";
                    endTime = achievementSumDto.getYear() + "-09-30 23:59:59";
                    sqlBuilder.append(" and D.DISPATCHDATE >= to_date(:beginTime,'yyyy-mm-dd hh24:mi:ss') ").setParam("beginTime", beginTime);
                    sqlBuilder.append(" and D.DISPATCHDATE <= to_date(:endTime,'yyyy-mm-dd hh24:mi:ss') ").setParam("endTime", endTime);
                } else if (achievementSumDto.getQuarter().equals("4")) {
                    beginTime = achievementSumDto.getYear() + "-10-01 00:00:00";
                    endTime = achievementSumDto.getYear() + "-12-31 23:59:59";
                    sqlBuilder.append(" and D.DISPATCHDATE >= to_date(:beginTime,'yyyy-mm-dd hh24:mi:ss') ").setParam("beginTime", beginTime);
                    sqlBuilder.append(" and D.DISPATCHDATE <= to_date(:endTime,'yyyy-mm-dd hh24:mi:ss') ").setParam("endTime", endTime);
                }
            }
            if ("0".equals(achievementSumDto.getIsmainuser())) {
                sqlBuilder.append(" group by d.dispatchdate,s.projectname, d.filenum,d.declarevalue, d.authorizevalue, " +
                        " d.extravalue, d.extrarate, p.ismainuser");
            }
        }

        List<Object[]> achievementDetailObjList = expertSelectedRepo.getObjectArray(sqlBuilder);
        List<AchievementDetailDto> achievementDetailDtoList = new ArrayList<AchievementDetailDto>();
        for (int i = 0; i < achievementDetailObjList.size(); i++) {
            AchievementDetailDto achievementDetailDto1 = new AchievementDetailDto();
            Object[] projectReviewCon = achievementDetailObjList.get(i);
            if (null != projectReviewCon[0]) {
                achievementDetailDto1.setDispatchDate((Date) projectReviewCon[0]);
            } else {
                achievementDetailDto1.setDispatchDate(null);
            }
            if (null != projectReviewCon[1]) {
                achievementDetailDto1.setProjectName((String) projectReviewCon[1]);
            } else {
                achievementDetailDto1.setProjectName(null);
            }
            if (null != projectReviewCon[2]) {
                achievementDetailDto1.setFileNum((String) projectReviewCon[2]);
            } else {
                achievementDetailDto1.setFileNum(null);
            }
            if (null != projectReviewCon[3]) {
                achievementDetailDto1.setDeclareValue((BigDecimal) projectReviewCon[3]);
            } else {
                achievementDetailDto1.setDeclareValue(BigDecimal.ZERO);
            }
            if (null != projectReviewCon[4]) {
                achievementDetailDto1.setAuthorizeValue((BigDecimal) projectReviewCon[4]);
            } else {
                achievementDetailDto1.setAuthorizeValue(BigDecimal.ZERO);
            }
            if (null != projectReviewCon[5]) {
                achievementDetailDto1.setExtraValue((BigDecimal) projectReviewCon[5]);
            } else {
                achievementDetailDto1.setExtraValue(BigDecimal.ZERO);
            }
            if (null != projectReviewCon[6]) {
                achievementDetailDto1.setExtraRate((BigDecimal) projectReviewCon[6]);
            } else {
                achievementDetailDto1.setExtraRate(BigDecimal.ZERO);
            }

            if (null != projectReviewCon[7]) {
                achievementDetailDto1.setIsmainuser((String) projectReviewCon[7]);
            } else {
                achievementDetailDto1.setIsmainuser(null);
            }

            achievementDetailDtoList.add(achievementDetailDto1);
        }

        return achievementDetailDtoList;
    }*/

    /*
    /**
     * 查询部门业绩明细
     * @param orgIds
     * @param yearName
     * @param quartterType
     * @param level
     * @return
     */
    /*@Override
    public List<AchievementDeptDetailDto> findDeptAchievementDetail(String orgIds,String yearName,String quartterType, int level) {
        String beginTime = "";
        String endTime = "";
        HqlBuilder sqlBuilder = HqlBuilder.create();
        sqlBuilder.append("select * from ( select d.*,'1'protype from ( WITH A AS ( select t.id,t.displayname,");
        sqlBuilder.append(" t.ismainuser,count(1) proCount from ( select u.id, u.displayname,d.dispatchdate,s.projectname,d.filenum,");
        sqlBuilder.append(" d.declarevalue,d.authorizevalue,d.extravalue, d.extrarate,p.ismainuser from cs_sign s left join cs_dispatch_doc d");
        sqlBuilder.append(" on s.signid = d.signid left join CS_SIGN_PRINCIPAL2 p on s.signid = p.signid   inner join cs_user u ");
        sqlBuilder.append(" on p.userid = u.id");

        if (4 != level) {
            sqlBuilder.append(" where s.processstate >= 6  and s.signstate != 7 ");
            sqlBuilder.append(" and exists(select 1 from cs_user u ");
            sqlBuilder.bulidPropotyString("where", "u.orgid", orgIds);
            sqlBuilder.append("  and u.id = p.userid ) ");
            sqlBuilder.append("  and p.ismainuser is not null");
            sqlBuilder.append("  and not exists(select 1 from CS_DEPT_CS_USER u where u.userlist_id = p.userid )");
            //信息化组
        } else {
            sqlBuilder.append(" where s.processstate >= 6  and s.signstate != 7 and exists(select 1 from CS_DEPT_CS_USER u where u.userlist_id = p.userid ) ");
        }
        //过滤时间
        String[] times = bulidDateQueryCondition(sqlBuilder,yearName,quartterType);
        beginTime = times[0];
        endTime = times[1];

        sqlBuilder.append(" )t group by t.id,t.displayname,t.ismainuser order by t.id,t.ismainuser desc ) select  id,displayname,");
        sqlBuilder.append(" max(CASE WHEN rnum = 1 THEN ismainuser END ) ismainuser_1,max(CASE WHEN rnum = 1 THEN proCount END ) proCount_1,");
        sqlBuilder.append(" max(CASE WHEN rnum = 2 THEN ismainuser END ) ismainuser_2,max(CASE WHEN rnum = 2 THEN proCount END ) proCount_2");
        sqlBuilder.append(" FROM ( SELECT a.*, row_number() OVER(PARTITION BY id order by id) rnum FROM a ) a1  GROUP BY id,displayname   ) d");
        sqlBuilder.append(" union all  select * from (select *  from (  select d.*,'2' protype from ( WITH A AS ( select t.id,t.displayname,t.ismainuser,count(1) proCount from ");
        sqlBuilder.append(" ( select u.id,u.displayname,d.dispatchdate,s.projectname,d.filenum,d.declarevalue,d.authorizevalue,d.extravalue,");
        sqlBuilder.append(" d.extrarate, p.ismainuser from cs_sign s left join cs_dispatch_doc d on s.signid = d.signid left join CS_SIGN_PRINCIPAL2 p");
        sqlBuilder.append(" on s.signid = p.signid inner join cs_user u on p.userid = u.id ");

        if (level != 4) {
            sqlBuilder.append(" where s.signstate >= 1 and s.signstate!=7 ");
            sqlBuilder.append(" and exists(select 1 from cs_user u ");
            sqlBuilder.bulidPropotyString("where", "u.orgid", orgIds);
            sqlBuilder.append("  and u.id = p.userid)  and p.ismainuser is not null");

        } else {
            //信息化组单独测试
            sqlBuilder.append(" where s.processstate >= 6  and s.signstate != 7 and exists(select 1 from CS_DEPT_CS_USER u where u.userlist_id = p.userid ) ");
        }
        sqlBuilder.append(" and D.DISPATCHDATE >= to_date(:beginTime,'yyyy-mm-dd hh24:mi:ss') ").setParam("beginTime", beginTime);
        sqlBuilder.append(" and D.DISPATCHDATE <= to_date(:endTime,'yyyy-mm-dd hh24:mi:ss') ").setParam("endTime", endTime);
        sqlBuilder.append(" ) t  group by t.id,t.displayname,t.ismainuser order by t.id,t.ismainuser desc  ) ");
        sqlBuilder.append(" select id,displayname, max(CASE WHEN rnum = 1 THEN ismainuser END ) ismainuser_1,max(CASE WHEN rnum = 1 THEN proCount END ) proCount_1,");
        sqlBuilder.append(" max(CASE WHEN rnum = 2 THEN ismainuser END ) ismainuser_2, max(CASE WHEN rnum = 2 THEN proCount END ) proCount_2 ");
        sqlBuilder.append(" FROM ( SELECT a.*, row_number() OVER(PARTITION BY id order by id) rnum FROM a ) a1 GROUP BY id,displayname");
        sqlBuilder.append(" ) d ) order by id,protype   ) e  where exists  (select 1  from (select t.id, t.displayname, ");
        sqlBuilder.append(" t.ismainuser, count(1) proCount  from (select u.id,u.displayname, p.ismainuser   from cs_sign s ");
        sqlBuilder.append(" left join cs_dispatch_doc d on s.signid = d.signid  left join CS_SIGN_PRINCIPAL2 p on s.signid = p.signid");
        sqlBuilder.append(" inner join cs_user u  on p.userid = u.id ");
        if (level != 4) {
            sqlBuilder.append(" where s.processstate >= 6  and s.signstate != 7 and exists(select 1 from cs_user u ");
            sqlBuilder.bulidPropotyString("where", "u.orgid", orgIds);
            sqlBuilder.append("  and u.id = p.userid )  and p.ismainuser is not null");
            sqlBuilder.append("  and not exists(select 1 from CS_DEPT_CS_USER u where u.userlist_id = p.userid )");
        } else {
            sqlBuilder.append(" where s.processstate >= 6  and s.signstate != 7 and exists(select 1 from CS_DEPT_CS_USER u where u.userlist_id = p.userid ) ");
        }

        sqlBuilder.append(" and D.DISPATCHDATE >= to_date(:beginTime,'yyyy-mm-dd hh24:mi:ss') ").setParam("beginTime", beginTime);
        sqlBuilder.append(" and D.DISPATCHDATE <= to_date(:endTime,'yyyy-mm-dd hh24:mi:ss') ").setParam("endTime", endTime);
        sqlBuilder.append(" ) t  group by t.id, t.displayname, t.ismainuser  ) g   where e.id = g.id  ))  order by id, protype");

        List<Object[]> achievementDetailObjList = expertSelectedRepo.getObjectArray(sqlBuilder);
        List<AchievementDeptDetailDto> achievementDetailDtoList = new ArrayList<AchievementDeptDetailDto>();
        try {
            for (int i = 0; i < achievementDetailObjList.size(); i += 2) {
                AchievementDeptDetailDto achievementDetailDto1 = new AchievementDeptDetailDto();
                Object[] projectReviewCon = achievementDetailObjList.get(i);
                Object[] projectReviewCon1 = achievementDetailObjList.get(i + 1);
                if (null != projectReviewCon[0]) {
                    achievementDetailDto1.setUserId((String) projectReviewCon[0]);
                } else {
                    achievementDetailDto1.setUserId(null);
                }
                if (null != projectReviewCon[1]) {
                    achievementDetailDto1.setName((String) projectReviewCon[1]);
                } else {
                    achievementDetailDto1.setName(null);
                }
                if (null != projectReviewCon[2] && null != projectReviewCon[4]) {
                    if (null != projectReviewCon[3]) {

                        achievementDetailDto1.setMainDisSum(Integer.parseInt(projectReviewCon[3].toString()));
                    } else {
                        achievementDetailDto1.setMainDisSum(0);
                    }

                    if (null != projectReviewCon[5]) {
                        achievementDetailDto1.setAssistDisSum(Integer.parseInt(projectReviewCon[5].toString()));
                    } else {
                        achievementDetailDto1.setAssistDisSum(0);
                    }

                    if (null != projectReviewCon1[3]) {
                        achievementDetailDto1.setMainProCount(Integer.parseInt(projectReviewCon1[3].toString()));
                    } else {
                        achievementDetailDto1.setMainProCount(0);
                    }

                    if (null != projectReviewCon1[5]) {
                        achievementDetailDto1.setAssistProCount(Integer.parseInt(projectReviewCon1[5].toString()));
                    } else {
                        achievementDetailDto1.setAssistProCount((Integer) projectReviewCon1[5]);
                    }
                } else {
                    if (null != projectReviewCon[3]) {
                        Integer temp = Integer.parseInt(projectReviewCon[2].toString());
                        if (temp == 9) {
                            achievementDetailDto1.setMainDisSum(Integer.parseInt(projectReviewCon[3].toString()));
                            achievementDetailDto1.setAssistDisSum(0);
                        } else {
                            achievementDetailDto1.setMainDisSum(0);
                            achievementDetailDto1.setAssistDisSum(Integer.parseInt(projectReviewCon[3].toString()));
                        }
                    }
                    if (null != projectReviewCon1[3]) {
                        Integer temp = Integer.parseInt(projectReviewCon1[2].toString());
                        if (temp == 9) {
                            achievementDetailDto1.setMainProCount(Integer.parseInt(projectReviewCon1[3].toString()));
                            achievementDetailDto1.setAssistProCount(0);
                        } else {
                            achievementDetailDto1.setMainProCount(0);
                            achievementDetailDto1.setAssistProCount(Integer.parseInt(projectReviewCon1[3].toString()));
                        }
                    }
                }
                achievementDetailDtoList.add(achievementDetailDto1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return achievementDetailDtoList;
    }

    private String[] bulidDateQueryCondition(HqlBuilder sqlBuilder, String yearName, String quartterType) {
        String beginTime ="",endTime ="";
        if (Validate.isString(yearName) && Validate.isString(quartterType)) {
            switch (quartterType) {
                case "0":
                    beginTime = yearName + "-01-01 00:00:00";
                    endTime = yearName + "-12-31 23:59:59";
                    break;
                case "1":
                    beginTime = yearName + "-01-01 00:00:00";
                    endTime = yearName + "-03-31 23:59:59";
                    break;
                case "2":
                    beginTime = yearName + "-04-01 00:00:00";
                    endTime = yearName + "-06-30 23:59:59";
                    break;
                case "3":
                    beginTime = yearName + "-07-01 00:00:00";
                    endTime = yearName + "-09-30 23:59:59";
                    break;
                case "4":
                    beginTime = yearName + "-10-01 00:00:00";
                    endTime = yearName + "-12-31 23:59:59";
                    break;
                default:
                    beginTime = yearName + "-01-01 00:00:00";
                    endTime = yearName + "-12-31 23:59:59";
                    ;
            }
            sqlBuilder.append(" and D.DISPATCHDATE >= to_date(:beginTime,'yyyy-mm-dd hh24:mi:ss') ").setParam("beginTime", beginTime);
            sqlBuilder.append(" and D.DISPATCHDATE <= to_date(:endTime,'yyyy-mm-dd hh24:mi:ss') ").setParam("endTime", endTime);
        }
        return new String[]{beginTime,endTime};
    }*/

    @Override
    public void deleteByBusinessId(String businessId) {
        HqlBuilder sqlBuilder = HqlBuilder.create();
        sqlBuilder.append(" delete from CS_EXPERT_SELECTED where BUSINESSID =:businessId ");
        sqlBuilder.setParam("businessId", businessId);
        executeSql(sqlBuilder);
    }

    @Override
    public List<ExpertSelectedDto> expertSelectedByIds(String ids) {
        List<ExpertSelectedDto> expertSelectedDtoList = new ArrayList<>();
        if(Validate.isString(ids)){
            String[] idArr = ids.split(",");
            if(idArr != null && idArr.length > 0 ){
                for(String id : idArr){
                    ExpertSelected expertSelected = expertSelectedRepo.findById(ExpertSelected_.id.getName() , id);
                    ExpertReview expertReview = expertSelected.getExpertReview();
                    Expert expert = expertSelected.getExpert();
                   ExpertSelectedDto dto = new ExpertSelectedDto();
                   BeanCopierUtils.copyPropertiesIgnoreNull(expertSelected , dto);
                   ExpertReviewDto expertReviewDto = new ExpertReviewDto();
                   BeanCopierUtils.copyPropertiesIgnoreNull(expertReview , expertReviewDto);
                   ExpertDto expertDto = new ExpertDto();
                   BeanCopierUtils.copyPropertiesIgnoreNull(expert , expertDto);
                   dto.setExpertReviewDto(expertReviewDto);
                   dto.setExpertDto(expertDto);
                   expertSelectedDtoList.add(dto);
                }
            }
        }
        return expertSelectedDtoList;
    }

    /**
     * 项目评审情况明细
     *
     * @param projectReviewConditionDto
     * @return
     */
    @Override
    public List<ProReviewConditionDto> proReviewConditionDetail(ProReviewConditionDto projectReviewConditionDto) {
        HqlBuilder sqlBuilder = HqlBuilder.create();
        sqlBuilder.append("select s.signid,case when s.isadvanced= '9' then  '提前介入'   when s.reviewstage='进口设备' then  '其它' else s.reviewstage end reviewstage, s.projectname ,s.isadvanced ,d.remark   from cs_sign s   ");
        sqlBuilder.append("left join cs_dispatch_doc d  ");
        sqlBuilder.append("on s.signid = d.signid  ");
        sqlBuilder.append("where 1 = 1 ");
        //sqlBuilder.append("and s.signstate = '9' ");
        sqlBuilder.append("and s.signstate != '7' ");//过滤删除
        sqlBuilder.append("and s.processstate >= 6  ");//已发文
        sqlBuilder.append("and D.DISPATCHTYPE != '项目退文' ");//过滤退文项目
        sqlBuilder.append("and  ( s.ispresign != '0' or s.ispresign is null )  "); //排除预签收
        List<ProReviewConditionDto> projectReviewConDtoList = new ArrayList<ProReviewConditionDto>();
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
                ;
                String day = "";
                day = DateUtils.getMaxDayOfMonth(Integer.parseInt(timeArr[0]), (Integer.parseInt(timeArr[1]))) + "";
                String beginTime = projectReviewConditionDto.getEndTime() + "-01 00:00:00";
                String endTime = projectReviewConditionDto.getEndTime() + "-" + day + " 23:59:59";
                sqlBuilder.append("and D.DISPATCHDATE >= to_date('" + beginTime + "', 'yyyy-mm-dd hh24:mi:ss') ");
                sqlBuilder.append("and D.DISPATCHDATE <= to_date('" + endTime + "', 'yyyy-mm-dd hh24:mi:ss') ");
            }
        }
        sqlBuilder.append("order by s.reviewstage ");
        List<Object[]> projectReviewConList = expertSelectedRepo.getObjectArray(sqlBuilder);
        if (projectReviewConList.size() > 0) {
            for (int i = 0; i < projectReviewConList.size(); i++) {
                Object[] projectReviewCon = projectReviewConList.get(i);
                ProReviewConditionDto proReviewConditionDto = new ProReviewConditionDto();
                if (null != projectReviewCon[1]) {
                    proReviewConditionDto.setReviewStage((String) projectReviewCon[1]);
                } else {
                    proReviewConditionDto.setReviewStage(null);
                }

                if (null != projectReviewCon[2]) {
                    if (null != projectReviewCon[4]) {
                        proReviewConditionDto.setProjectName(((String) projectReviewCon[2]) + "(" + ((String) projectReviewCon[4]) + ")");
                    } else {
                        proReviewConditionDto.setProjectName((String) projectReviewCon[2]);
                    }

                } else {
                    proReviewConditionDto.setProjectName(null);
                }
                if (null != projectReviewCon[3]) {
                    proReviewConditionDto.setIsadvanced((String) projectReviewCon[3]);
                    if ("9".equals(proReviewConditionDto.getIsadvanced())) {
                        proReviewConditionDto.setReviewStage("提前介入");
                    }
                } else {
                    proReviewConditionDto.setIsadvanced(null);
                }

                projectReviewConDtoList.add(proReviewConditionDto);
            }
        }
        return projectReviewConDtoList;
    }


    /**
     * 获取退文项目详细情况
     *
     * @param projectReviewConditionDto
     * @return
     */
    @Override
    public List<ProReviewConditionDto> getBackDispatchInfo(ProReviewConditionDto projectReviewConditionDto) {
        HqlBuilder sqlBuilder = HqlBuilder.create();
        sqlBuilder.append("select s.signid, s.reviewstage, s.projectname ,s.isadvanced ,d.remark ,s.appalyInvestment from cs_sign s   ");
        sqlBuilder.append("left join cs_dispatch_doc d  ");
        sqlBuilder.append("on s.signid = d.signid  ");
        sqlBuilder.append("where 1 = 1 ");
        //sqlBuilder.append("and s.signstate = '9' ");
        sqlBuilder.append("and s.signstate != '7' ");//过滤删除
        sqlBuilder.append("and s.processstate >= 6  ");//已发文
        sqlBuilder.append("and D.DISPATCHTYPE ='项目退文' ");
        sqlBuilder.append("and  ( s.ispresign != '0' or s.ispresign is null )  "); //排除预签收
        List<ProReviewConditionDto> projectReviewConDtoList = new ArrayList<ProReviewConditionDto>();
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
                ;
                String day = "";
                day = DateUtils.getMaxDayOfMonth(Integer.parseInt(timeArr[0]), (Integer.parseInt(timeArr[1]))) + "";
                String beginTime = projectReviewConditionDto.getEndTime() + "-01 00:00:00";
                String endTime = projectReviewConditionDto.getEndTime() + "-" + day + " 23:59:59";
                sqlBuilder.append("and D.DISPATCHDATE >= to_date('" + beginTime + "', 'yyyy-mm-dd hh24:mi:ss') ");
                sqlBuilder.append("and D.DISPATCHDATE <= to_date('" + endTime + "', 'yyyy-mm-dd hh24:mi:ss') ");
            }
        }
        sqlBuilder.append("order by s.reviewstage ");
        List<Object[]> projectReviewConList = expertSelectedRepo.getObjectArray(sqlBuilder);
        if (projectReviewConList.size() > 0) {
            for (int i = 0; i < projectReviewConList.size(); i++) {
                Object[] projectReviewCon = projectReviewConList.get(i);
                ProReviewConditionDto proReviewConditionDto = new ProReviewConditionDto();
                if (null != projectReviewCon[1]) {
                    proReviewConditionDto.setReviewStage((String) projectReviewCon[1]);
                } else {
                    proReviewConditionDto.setReviewStage(null);
                }

                if (null != projectReviewCon[2]) {
//                    if (null != projectReviewCon[4]) {
//                        proReviewConditionDto.setProjectName(((String) projectReviewCon[2]) + "(" + ((String) projectReviewCon[4]) + ")");
//                    } else {
                    if(((String) projectReviewCon[2]).indexOf("（提前介入）") > -1){
                        proReviewConditionDto.setProjectName(((String) projectReviewCon[2]).replaceAll("（提前介入）" , ""));
                    }
                        proReviewConditionDto.setProjectName((String) projectReviewCon[2]);
//                    }
                } else {
                    proReviewConditionDto.setProjectName(null);
                }
                if (null != projectReviewCon[3]) {
                    proReviewConditionDto.setIsadvanced((String) projectReviewCon[3]);
                    if ("9".equals(proReviewConditionDto.getIsadvanced())) {
                        proReviewConditionDto.setReviewStage("提前介入");
                    }
                } else {
                    proReviewConditionDto.setIsadvanced(null);
                }
                if(null != projectReviewCon[5]){
                    proReviewConditionDto.setDeclareValue(new BigDecimal(projectReviewCon[5].toString()));
                }else{
                    proReviewConditionDto.setDeclareValue(new BigDecimal(0));
                }

                projectReviewConDtoList.add(proReviewConditionDto);
            }
        }
        return projectReviewConDtoList;
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
        HqlBuilder sqlBuilder = HqlBuilder.create();
//        String beginTime = "";
//        String endTime = "";
        sqlBuilder.append("select count(s.projectcode) procount  from cs_sign s  ");
        sqlBuilder.append("left join cs_dispatch_doc d  ");
        sqlBuilder.append("on s.signid = d.signid  ");
        sqlBuilder.append("where 1 = 1 ");
        sqlBuilder.append("and s.signstate != '7' ");//过滤删除
        // sqlBuilder.append("and s.signstate = '9'  ");
//        sqlBuilder.append("and s.processstate >= 6  ");//已发文
        sqlBuilder.append("and  ( s.ispresign != '0' or s.ispresign is null )  "); //排除预签收
        //todo:添加查询条件
//        if(null != projectReviewConditionDto){
        if (StringUtil.isNotEmpty(beginTime) && StringUtil.isNotEmpty(endTime)) {
//                beginTime = projectReviewConditionDto.getBeginTime()+"-01 00:00:00";
//                String[] timeArr = projectReviewConditionDto.getEndTime().split("-");
//                String day = DateUtils.getMaxDayOfMonth(Integer.parseInt(timeArr[0]),(Integer.parseInt(timeArr[1])-1))+"";
//                endTime = projectReviewConditionDto.getEndTime()+"-"+day+" 23:59:59";
            sqlBuilder.append("and s.signdate >= to_date('" + beginTime + "', 'yyyy-mm-dd hh24:mi:ss') ");
            sqlBuilder.append("and s.signdate <= to_date('" + endTime + "', 'yyyy-mm-dd hh24:mi:ss') ");

        }
//        }
        sqlBuilder.append("and d.declarevalue < 5000  ");
        sqlBuilder.append("union all  ");
        sqlBuilder.append("select count(s.projectcode) procount  from cs_sign s  ");
        sqlBuilder.append("left join cs_dispatch_doc d  ");
        sqlBuilder.append("on s.signid = d.signid  ");
        sqlBuilder.append("where 1 = 1 ");
//        sqlBuilder.append("and s.signstate = '9'  ");
        sqlBuilder.append("and s.signstate != '7' ");//过滤删除
//        sqlBuilder.append("and s.processstate >= 6  ");//已发文
        sqlBuilder.append("and  ( s.ispresign != '0' or s.ispresign is null )  "); //排除预签收
        sqlBuilder.append("and s.signdate >= to_date('" + beginTime + "', 'yyyy-mm-dd hh24:mi:ss') ");
        sqlBuilder.append("and s.signdate <= to_date('" + endTime + "', 'yyyy-mm-dd hh24:mi:ss') ");
        sqlBuilder.append("and d.declarevalue >= 5000  and d.declarevalue < 10000   ");
        sqlBuilder.append("union all  ");
        sqlBuilder.append("select count(s.projectcode) procount  from cs_sign s  ");
        sqlBuilder.append("left join cs_dispatch_doc d  ");
        sqlBuilder.append("on s.signid = d.signid  ");
        sqlBuilder.append("where 1 = 1 ");
//        sqlBuilder.append("and s.signstate = '9'  ");
        sqlBuilder.append("and s.signstate != '7' ");//过滤删除
//        sqlBuilder.append("and s.processstate >= 6  ");//已发文
        sqlBuilder.append("and  ( s.ispresign != '0' or s.ispresign is null )  "); //排除预签收
        sqlBuilder.append("and s.signdate >= to_date('" + beginTime + "', 'yyyy-mm-dd hh24:mi:ss') ");
        sqlBuilder.append("and s.signdate <= to_date('" + endTime + "', 'yyyy-mm-dd hh24:mi:ss') ");
        sqlBuilder.append("and d.declarevalue >= 10000  and d.declarevalue < 100000   ");
        sqlBuilder.append("union all  ");
        sqlBuilder.append("select count(s.projectcode) procount  from cs_sign s  ");
        sqlBuilder.append("left join cs_dispatch_doc d  ");
        sqlBuilder.append("on s.signid = d.signid  ");
        sqlBuilder.append("where 1 = 1 ");
        sqlBuilder.append("and s.signstate != '7' ");//过滤删除
//        sqlBuilder.append("and s.processstate >= 6  ");//已发文
        sqlBuilder.append("and  ( s.ispresign != '0' or s.ispresign is null )  "); //排除预签收
        sqlBuilder.append("and s.signdate >= to_date('" + beginTime + "', 'yyyy-mm-dd hh24:mi:ss') ");
        sqlBuilder.append("and s.signdate <= to_date('" + endTime + "', 'yyyy-mm-dd hh24:mi:ss') ");
        sqlBuilder.append("and d.declarevalue >= 100000  and d.declarevalue < 500000   ");
        sqlBuilder.append("union all  ");
        sqlBuilder.append("select count(s.projectcode) procount  from cs_sign s  ");
        sqlBuilder.append("left join cs_dispatch_doc d  ");
        sqlBuilder.append("on s.signid = d.signid  ");
        sqlBuilder.append("where 1 = 1 ");
//        sqlBuilder.append("and s.signstate = '9'  ");
        sqlBuilder.append("and s.signstate != '7' ");//过滤删除
//        sqlBuilder.append("and s.processstate >= 6  ");//已发文
        sqlBuilder.append("and  ( s.ispresign != '0' or s.ispresign is null )  "); //排除预签收
        sqlBuilder.append("and s.signdate >= to_date('" + beginTime + "', 'yyyy-mm-dd hh24:mi:ss') ");
        sqlBuilder.append("and s.signdate <= to_date('" + endTime + "', 'yyyy-mm-dd hh24:mi:ss') ");
        sqlBuilder.append("and d.declarevalue >= 500000   ");

        List projectReviewConList = expertSelectedRepo.getObjectArray(sqlBuilder);
        Integer[] proCountArr = new Integer[projectReviewConList.size() + 1];
        Integer totalNum = 0;
        if (projectReviewConList.size() > 0) {
            for (int i = 0; i < projectReviewConList.size(); i++) {
                if (null != projectReviewConList.get(i)) {
                    proCountArr[i] = Integer.valueOf(String.valueOf(projectReviewConList.get(i)));
                    totalNum += proCountArr[i];
                }
            }
            proCountArr[proCountArr.length - 1] = totalNum;
        }
        return proCountArr;
    }

    /**
     * 判断是否存在不规则专家评审次数（专家一周参会超过两次或者一个季度超过12次视为不规则）
     *
     * @param expertReviewConSimpleDto
     * @return
     */
    private List<Object[]> getExistsConComplicated(ExpertReviewConSimpleDto expertReviewConSimpleDto, List<Object[]> expertRevComplicateList) {
        boolean flag = true;
        Integer revCount = 0;
        int begseason = 0;
        int begweak = 0;
        int begday = -1;
        Date begDate = null;
        Date endDate = null;
        String expertId = "";
        List<Object[]> expertCompliList = new ArrayList<Object[]>();
        for (int i = 0; i < expertRevComplicateList.size(); i++) {
            flag = true;
            revCount = 0;
            Object[] tempArr = expertRevComplicateList.get(i);
            expertId = (String) tempArr[0];
            if (StringUtil.isNotEmpty(expertReviewConSimpleDto.getBeginTime())) {
                begDate = DateUtils.converToDate(expertReviewConSimpleDto.getBeginTime(), "yyyy-MM-dd");
                begseason = DateUtils.getSeason(begDate);
                begweak = DateUtils.weekOfYear(begDate);
                begday = DateUtils.getDayOfWeek1(begDate);
            }
            if (StringUtil.isNotEmpty(expertReviewConSimpleDto.getEndTime())) {
                endDate = DateUtils.converToDate(expertReviewConSimpleDto.getEndTime(), "yyyy-MM-dd");
            }
            //todo: 不规则暂按会议起始日期不为空计算
            while (flag) {
                Date weakTemp = DateUtils.addDay(begDate, 7 - begday);
                if (weakTemp.compareTo(endDate) <= 0) {
                    if (DateUtils.getSeason(begDate) == begseason) {
                        //当前季度内参会次数大于12
                        if (revCount > 12) {
                            expertCompliList.add(tempArr);
                            revCount = 0;
                            break;
                        }
                        //按周计算  周参会次数大于2
                        int weakCount = getExpertRevTimes(begDate, weakTemp, expertId);
                        revCount += weakCount;
                        if (weakCount > 2) {
                            expertCompliList.add(tempArr);
                            revCount = 0;
                            break;
                        }
                        begDate = DateUtils.addDay(weakTemp, 1);
                        begweak = DateUtils.weekOfYear(begDate);
                        begday = DateUtils.getDayOfWeek1(begDate);
                    } else {
                        begseason = DateUtils.getSeason(begDate);
                        revCount = 0;
                        //按周计算  周参会次数大于2
                        int weakCount = getExpertRevTimes(begDate, weakTemp, expertId);
                        revCount += weakCount;
                        if (weakCount > 2) {
                            expertCompliList.add(tempArr);
                            revCount = 0;
                            break;
                        }
                        begDate = DateUtils.addDay(weakTemp, 1);
                        begday = DateUtils.getDayOfWeek1(begDate);
                    }
                } else {
                    //begDate 与 endDate 之间专家参会次数是否大于2
                    int weakCount = getExpertRevTimes(begDate, weakTemp, expertId);
                    if (weakCount > 2) {
                        expertCompliList.add(tempArr);
                        revCount = 0;
                        break;
                    }
                    flag = false;
                }
            }
        }
        return expertCompliList;
    }


    /**
     * 专家周评审次数
     *
     * @param begDate
     * @param endDate
     * @param expertId
     * @return
     */
    private int getExpertRevTimes(Date begDate, Date endDate, String expertId) {
        Integer count = 0;
        HqlBuilder sqlBuilder1 = HqlBuilder.create();
        sqlBuilder1.append("select t.expertid,count(t.expertid) from (   ");
        sqlBuilder1.append("select  e.expertid,e.expertno,e.name,e.company,r.reviewdate,s.projectname,s.reviewstage,s.signid,a.isletterrw  from cs_sign s  ");
        sqlBuilder1.append("left join cs_expert_review r  ");
        sqlBuilder1.append("on s.signid = r.businessid  ");
        sqlBuilder1.append("left join cs_expert_selected a  ");
        sqlBuilder1.append("on s.signid = a.businessid  ");
        sqlBuilder1.append("left join cs_expert e  ");
        sqlBuilder1.append("on a.expertid = e.expertid ) t   ");
        sqlBuilder1.append(" where t.expertid = '" + expertId + "'  ");
        if (null != begDate) {
            String beginTime = DateUtils.converToString(begDate, null) + " 00:00:00";
            sqlBuilder1.append("and t.reviewdate >= to_date('" + beginTime + "', 'yyyy-mm-dd hh24:mi:ss') ");
        }
        if (null != endDate) {
            String endTime = DateUtils.converToString(endDate, null) + " 23:59:59";
            sqlBuilder1.append("and t.reviewdate <= to_date('" + endTime + "', 'yyyy-mm-dd hh24:mi:ss') ");
        }
        sqlBuilder1.append(" group by t.expertid  ");
        List<Object[]> expertReviewConSimList1 = expertSelectedRepo.getObjectArray(sqlBuilder1);
        if (expertReviewConSimList1.size() > 0) {
            Object[] tempArr = expertReviewConSimList1.get(0);
            if (null != tempArr[1]) {
                BigDecimal temp = (BigDecimal) tempArr[1];
                count = temp.intValue();
            }
        }
        return count;
    }

    /**
     * 获取总参会次数
     *
     * @param expertReviewConSimList1
     * @param expertId
     * @return
     */
    private BigDecimal getExpertRevTotalNum(List<Object[]> expertReviewConSimList1, String expertId) {
        BigDecimal totalNum = null;
        if (expertReviewConSimList1.size() > 0) {
            for (int i = 0; i < expertReviewConSimList1.size(); i++) {
                Object[] expertReviewConSim = expertReviewConSimList1.get(i);
                if (null != expertReviewConSim[0]) {
                    String temp = (String) expertReviewConSim[0];
                    if (StringUtil.isNotEmpty(expertId) && StringUtil.isNotEmpty(temp) && expertId.equals(temp)) {
                        totalNum = (BigDecimal) expertReviewConSim[1];
                        break;
                    }

                }
            }
        }
        return totalNum;
    }


    /**
     * 根据业务ID统计已经确认的抽取专家
     *
     * @param businessId
     * @return
     */
    @Override
    public int getSelectEPCount(String businessId) {
        HqlBuilder sqlBuilder = HqlBuilder.create();
        sqlBuilder.append(" select count(id) from cs_expert_selected where " + ExpertSelected_.businessId.getName() + "=:businessId ");
        sqlBuilder.setParam("businessId", businessId);
        sqlBuilder.append(" and " + ExpertSelected_.isConfrim.getName() + "=:isConfrim ");
        sqlBuilder.setParam("isConfrim", Constant.EnumState.YES.getValue());
        sqlBuilder.append(" and " + ExpertSelected_.isJoin.getName() + "=:isJoin ");
        sqlBuilder.setParam("isJoin", Constant.EnumState.YES.getValue());
        sqlBuilder.append(" and " + ExpertSelected_.selectType.getName() + "=:selectType ");
        sqlBuilder.setParam("selectType", Constant.EnumExpertSelectType.AUTO.getValue());
        return returnIntBySql(sqlBuilder);
    }

    /**
     * 专家抽取统计
     *
     * @param expertSelectHis
     * @return
     */
    @Override
    public List<Object[]> getSelectHis(ExpertSelectHis expertSelectHis, boolean isScore) {
        HqlBuilder sqlBuilder = HqlBuilder.create();
        sqlBuilder.append(" SELECT EX.EXPERTID,EX.NAME,EX.COMPANY,EX.EXPERTFIELD,WP.PROJECTNAME,ES.MAJORBIG,ES.MAJORSMALL,ES.EXPERTTYPE,");
        sqlBuilder.append(" ES.SELECTTYPE, ES.ISCONFRIM,WP.REVIEWTYPE,ER.REVIEWDATE,WP.MIANCHARGEUSERNAME ,ES.SCORE,ES.DESCRIBES,WP.WORKREVIVESTAGE ");
        sqlBuilder.append(" FROM CS_EXPERT ex ");
        sqlBuilder.append(" LEFT JOIN CS_EXPERT_SELECTED es ON EX.EXPERTID = ES.EXPERTID ");
        sqlBuilder.append(" LEFT JOIN CS_EXPERT_REVIEW er ON ER.ID = ES.EXPERTREVIEWID ");
        sqlBuilder.append(" LEFT JOIN CS_WORK_PROGRAM wp ON WP.ID = ES.BUSINESSID ");
        sqlBuilder.append(" LEFT JOIN CS_SIGN s ON S.SIGNID = WP.SIGNID   ");
        sqlBuilder.append(" WHERE ex.state != '3' AND ex.state != '4' AND ES.ID IS NOT NULL ");
        //过滤预签收的
        sqlBuilder.append(" AND (S.ispresign<>'" + Constant.EnumState.NO.getValue() + "' OR S.ispresign IS NULL)");
        //如果是专家评分，则必须是已确定并且参加会议的人
        if (isScore) {
            sqlBuilder.append(" AND ES.ISCONFRIM =:isConfirm ").setParam("isConfirm", Constant.EnumState.YES.getValue());
            sqlBuilder.append(" AND ES.ISJOIN =:isJoin ").setParam("isJoin", Constant.EnumState.YES.getValue());

            if (expertSelectHis.getScore() != null) {
                //先转成string进行拆分
                String score = expertSelectHis.getScore().toString();
                //转换赋值
                double begin = Double.parseDouble(score.substring(0, 1));
                double end = Double.parseDouble(score.substring(1, 2));
                sqlBuilder.append(" AND ES.SCORE >:score ").setParam("score", begin - 1, DoubleType.INSTANCE);
                sqlBuilder.append(" AND ES.SCORE <:score1 ").setParam("score1", end + 1, DoubleType.INSTANCE);
            }

            if (Validate.isString(expertSelectHis.getReviewStage())) {
                sqlBuilder.append(" AND WP.WORKREVIVESTAGE =:reviewStage ").setParam("reviewStage", expertSelectHis.getReviewStage(), StringType.INSTANCE);
            }
        }
        if (expertSelectHis != null) {
            if (Validate.isString(expertSelectHis.getEpName())) {
                sqlBuilder.append(" AND EX.NAME like :epName ").setParam("epName", "%" + expertSelectHis.getEpName() + "%");
            }
            if (Validate.isString(expertSelectHis.getBeginTime())) {
                sqlBuilder.append(" AND ER.REVIEWDATE > to_date(:beginTime,'yyyy-mm-dd hh24:mi:ss') ").setParam("beginTime", expertSelectHis.getBeginTime().trim() + " 00:00:00");
            }
            if (Validate.isString(expertSelectHis.getEndTime())) {
                sqlBuilder.append(" AND ER.REVIEWDATE < to_date(:endTime,'yyyy-mm-dd hh24:mi:ss') ").setParam("endTime", expertSelectHis.getEndTime().trim() + " 23:59:59");
            }
            if (Validate.isString(expertSelectHis.getReviewType())) {
                sqlBuilder.append(" AND WP.REVIEWTYPE =:reviewType ").setParam("reviewType", expertSelectHis.getReviewType());
            }
            if (Validate.isString(expertSelectHis.getSelectType())) {
                sqlBuilder.append(" AND ES.SELECTTYPE =:selectType ").setParam("selectType", expertSelectHis.getSelectType());
            }

            if (Validate.isString(expertSelectHis.getMajorBig())) {
                sqlBuilder.append(" AND ES.MAJORBIG =:majorBig ").setParam("majorBig", expertSelectHis.getMajorBig());
            }
            if (Validate.isString(expertSelectHis.getMarjorSmall())) {
                sqlBuilder.append(" AND ES.MAJORSMALL =:marjorSmall ").setParam("marjorSmall", expertSelectHis.getMarjorSmall());
            }
            if (Validate.isString(expertSelectHis.getExpertType())) {
                sqlBuilder.append(" AND ES.EXPERTTYPE =:expertType ").setParam("expertType", expertSelectHis.getExpertType());
            }
            if (Validate.isString(expertSelectHis.getIsConfirm())) {
                sqlBuilder.append(" AND ES.ISCONFRIM =:isConfirm ").setParam("isConfirm", expertSelectHis.getIsConfirm());
            }
        }

        sqlBuilder.append("  ORDER BY EX.EXPERTNO ");

        return getObjectArray(sqlBuilder);
    }

    /**
     * 获取提前介入评审情况
     *
     * @param projectReviewConditionDto
     * @return
     */
    @Override
    public ProReviewConditionDto getAdvancedCon(ProReviewConditionDto projectReviewConditionDto) {
        HqlBuilder sqlBuilder = HqlBuilder.create();
        sqlBuilder.append("select  count(s.projectcode),sum(d.declarevalue) / 10000 declarevalue,sum(d.authorizevalue) / 10000 authorizevalue, " +
                " (sum(d.declarevalue) - sum(d.authorizevalue)) / 10000 ljhj, " +
                "  decode(sum(d.declarevalue),0,0,  round((sum(d.declarevalue) - sum(d.authorizevalue)) / 10000 / (sum(d.declarevalue) / 10000), 5) * 100) hjl  ");
        sqlBuilder.append("from cs_sign s  ");
        sqlBuilder.append("left join cs_dispatch_doc d  ");
        sqlBuilder.append(" on s.signid = d.signid  ");
        sqlBuilder.append("where s.processstate >= 6  and s.isadvanced = '9'  and  ( s.ispresign != '0' or s.ispresign is null )  ");
        sqlBuilder.append("and D.DISPATCHTYPE != '项目退文函' ");
        sqlBuilder.append("and s.signstate != '7' ");//过滤删除
        if (null != projectReviewConditionDto) {
            if (StringUtil.isNotEmpty(projectReviewConditionDto.getBeginTime()) && StringUtil.isNotEmpty(projectReviewConditionDto.getEndTime())) {
                String beginTime = projectReviewConditionDto.getBeginTime() + "-01 00:00:00";
                String[] timeArr = projectReviewConditionDto.getEndTime().split("-");
                String day = "";
                day = DateUtils.getMaxDayOfMonth(Integer.parseInt(timeArr[0]), (Integer.parseInt(timeArr[1]))) + "";
                String endTime = projectReviewConditionDto.getEndTime() + "-" + day + " 23:59:59";
                sqlBuilder.append("and D.DISPATCHDATE >= to_date(:beginTime, 'yyyy-mm-dd hh24:mi:ss') ").setParam("beginTime", beginTime);
                sqlBuilder.append("and D.DISPATCHDATE <= to_date(:endTime, 'yyyy-mm-dd hh24:mi:ss') ").setParam("endTime", endTime);
            } else if (StringUtil.isNotEmpty(projectReviewConditionDto.getBeginTime()) && !StringUtil.isNotEmpty(projectReviewConditionDto.getEndTime())) {
                String[] timeArr = projectReviewConditionDto.getBeginTime().split("-");
                String day = "";
                day = DateUtils.getMaxDayOfMonth(Integer.parseInt(timeArr[0]), (Integer.parseInt(timeArr[1]))) + "";
                String beginTime = projectReviewConditionDto.getBeginTime() + "-01 00:00:00";
                String endTime = projectReviewConditionDto.getBeginTime() + "-" + day + " 23:59:59";
                sqlBuilder.append("and D.DISPATCHDATE >= to_date(:beginTime, 'yyyy-mm-dd hh24:mi:ss') ").setParam("beginTime", beginTime);
                sqlBuilder.append("and D.DISPATCHDATE <= to_date(:endTime, 'yyyy-mm-dd hh24:mi:ss') ").setParam("endTime", endTime);
            } else if (StringUtil.isNotEmpty(projectReviewConditionDto.getEndTime()) && !StringUtil.isNotEmpty(projectReviewConditionDto.getBeginTime())) {
                String[] timeArr = projectReviewConditionDto.getEndTime().split("-");
                String day = "";
                day = DateUtils.getMaxDayOfMonth(Integer.parseInt(timeArr[0]), (Integer.parseInt(timeArr[1]))) + "";
                String beginTime = projectReviewConditionDto.getEndTime() + "-01 00:00:00";
                String endTime = projectReviewConditionDto.getEndTime() + "-" + day + " 23:59:59";
                sqlBuilder.append("and D.DISPATCHDATE >= to_date(:beginTime, 'yyyy-mm-dd hh24:mi:ss') ").setParam("beginTime", beginTime);
                sqlBuilder.append("and D.DISPATCHDATE <= to_date(:endTime, 'yyyy-mm-dd hh24:mi:ss') ").setParam("endTime", endTime);
            }
        }

        List<Object[]> objArr = getObjectArray(sqlBuilder);
        ProReviewConditionDto proReviewConditionDto = new ProReviewConditionDto();
        if (objArr.size() > 0) {
            Object[] projectReviewCon = objArr.get(0);
            if (null != projectReviewCon[0]) {
                proReviewConditionDto.setProCount((BigDecimal) projectReviewCon[0]);
            } else {
                proReviewConditionDto.setProCount(null);
            }

            if (null != projectReviewCon[1]) {
                proReviewConditionDto.setDeclareValue((BigDecimal) projectReviewCon[1]);
            } else {
                proReviewConditionDto.setDeclareValue(null);
            }
            if (null != projectReviewCon[2]) {
                proReviewConditionDto.setAuthorizeValue((BigDecimal) projectReviewCon[2]);
            } else {
                proReviewConditionDto.setAuthorizeValue(null);
            }

            if (null != projectReviewCon[3]) {
                proReviewConditionDto.setLjhj((BigDecimal) projectReviewCon[3]);
            } else {
                proReviewConditionDto.setLjhj(null);
            }
            if (null != projectReviewCon[4]) {
                proReviewConditionDto.setHjl((BigDecimal) projectReviewCon[4]);
            } else {
                proReviewConditionDto.setHjl(null);
            }
        }
        return proReviewConditionDto;
    }

    /**
     * 根据工作方案id抽取专家信息
     *
     * @param businessID
     * @return
     */
    @Override
    public List<ExpertSelectedDto> findByBusinessId(String businessID) {
        Criteria criteria = expertSelectedRepo.getExecutableCriteria();
        criteria.add(Restrictions.eq(ExpertSelected_.businessId.getName(), businessID));
        criteria.add(Restrictions.eq(ExpertSelected_.isConfrim.getName(), Constant.EnumState.YES.getValue()));
        criteria.add(Restrictions.eq(ExpertSelected_.isJoin.getName(), Constant.EnumState.YES.getValue()));
        criteria.addOrder(Property.forName(ExpertSelected_.expertSeq.getName()).asc());

        List<ExpertSelected> expertSelectedList = criteria.list();
        List<ExpertSelectedDto> expertSelectedDtoList = new ArrayList<>();
        if (Validate.isList(expertSelectedList)) {
            expertSelectedList.forEach(x -> {
                ExpertSelectedDto expertSelectedDto = new ExpertSelectedDto();
                BeanCopierUtils.copyProperties(x, expertSelectedDto);
                ExpertDto expertDto = new ExpertDto();
                if (Validate.isObject(x.getExpert())) {
                    BeanCopierUtils.copyProperties(x.getExpert(), expertDto);
                    expertSelectedDto.setExpertDto(expertDto);
                }
                expertSelectedDtoList.add(expertSelectedDto);
            });
        }
        return expertSelectedDtoList;
    }

    @Override
    public List<ExpertSelected> findAllByBusinessId(String businessID) {
        Criteria criteria = expertSelectedRepo.getExecutableCriteria();
        criteria.add(Restrictions.eq(ExpertSelected_.businessId.getName(), businessID));
        return criteria.list();
    }


    /**
     * 本月专家评审会情况统计
     * @param projectReviewConditionDto
     * @return
     */
    @Override
    public String[] expertReviewMeeting(ProReviewConditionDto projectReviewConditionDto) {
        HqlBuilder sqlBuilder = HqlBuilder.create();
        sqlBuilder.append("select w.workreviveStage , count(w.reviewType) from cs_work_program w  ");
        sqlBuilder.append(" left join cs_sign s  on w.signid =s.signid  ");
        sqlBuilder.append(" left join cs_dispatch_doc d  on w.signid = d.signid ");
        sqlBuilder.append("where 1 = 1 ");
        sqlBuilder.append("and s.signstate != '7' ");//过滤删除
//        sqlBuilder.append("and D.DISPATCHTYPE != '项目退文' ");//过滤退文项目
//        sqlBuilder.append("and s.processstate >= 6  ");//已发文
        sqlBuilder.append("and  ( s.ispresign != '0' or s.ispresign is null )  "); //排除预签收
        //添加查询条件
        if (Validate.isObject(projectReviewConditionDto)) {
            if (Validate.isString(projectReviewConditionDto.getBeginTime()) && Validate.isString(projectReviewConditionDto.getEndTime())) {
                String beginTime = projectReviewConditionDto.getBeginTime() + "-01 00:00:00";
                String[] timeArr = projectReviewConditionDto.getEndTime().split("-");
                String day = "";
                day = DateUtils.getMaxDayOfMonth(Integer.parseInt(timeArr[0]), (Integer.parseInt(timeArr[1]))) + "";
                String endTime = projectReviewConditionDto.getEndTime() + "-" + day + " 23:59:59";
                sqlBuilder.append("and D.DISPATCHDATE >= to_date('" + beginTime + "', 'yyyy-mm-dd hh24:mi:ss') ");
                sqlBuilder.append("and D.DISPATCHDATE <= to_date('" + endTime + "', 'yyyy-mm-dd hh24:mi:ss') ");
            } else if (Validate.isString(projectReviewConditionDto.getBeginTime()) && !Validate.isString(projectReviewConditionDto.getEndTime())) {
                String[] timeArr = projectReviewConditionDto.getBeginTime().split("-");
                String day = "";
                day = DateUtils.getMaxDayOfMonth(Integer.parseInt(timeArr[0]), (Integer.parseInt(timeArr[1]))) + "";
                String beginTime = projectReviewConditionDto.getBeginTime() + "-01 00:00:00";
                String endTime = projectReviewConditionDto.getBeginTime() + "-" + day + " 23:59:59";
                sqlBuilder.append("and D.DISPATCHDATE >= to_date('" + beginTime + "', 'yyyy-mm-dd hh24:mi:ss') ");
                sqlBuilder.append("and D.DISPATCHDATE <= to_date('" + endTime + "', 'yyyy-mm-dd hh24:mi:ss') ");
            } else if (Validate.isString(projectReviewConditionDto.getEndTime()) && !Validate.isString(projectReviewConditionDto.getBeginTime())) {
                String[] timeArr = projectReviewConditionDto.getEndTime().split("-");
                ;
                String day = "";
                day = DateUtils.getMaxDayOfMonth(Integer.parseInt(timeArr[0]), (Integer.parseInt(timeArr[1]))) + "";
                String beginTime = projectReviewConditionDto.getEndTime() + "-01 00:00:00";
                String endTime = projectReviewConditionDto.getEndTime() + "-" + day + " 23:59:59";
                sqlBuilder.append("and D.DISPATCHDATE >= to_date('" + beginTime + "', 'yyyy-mm-dd hh24:mi:ss') ");
                sqlBuilder.append("and D.DISPATCHDATE <= to_date('" + endTime + "', 'yyyy-mm-dd hh24:mi:ss') ");
            }
        }
        sqlBuilder.append(" and w.reviewType = '专家评审会' group by w.workreviveStage ");
        List<Object[]> projectReviewConList = expertSelectedRepo.getObjectArray(sqlBuilder);
        String reviewName = "";
        int count = 0;
        if (projectReviewConList.size() > 0) {
            for(int i = 0 ; i < projectReviewConList.size() ; i ++ ){
                Object[] projectReviewCon = projectReviewConList.get(i);

                if (null != projectReviewCon[0]) {
                    if(Validate.isString(reviewName)){
                        reviewName += "、";
                    }
                    if(Constant.STAGE_BUDGET.equals(projectReviewCon[0])){
                        reviewName += "初步涉及概算审核";
                    }else{
                        reviewName += projectReviewCon[0];
                    }
                }
                if (null != projectReviewCon[1]) {
                    count += Integer.parseInt(projectReviewCon[1].toString());
                }
            }

        }

        String[] result = new String[]{reviewName , String.valueOf(count)};

        return result;
    }

}