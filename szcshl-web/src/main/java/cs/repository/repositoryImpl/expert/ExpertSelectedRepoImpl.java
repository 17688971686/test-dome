package cs.repository.repositoryImpl.expert;

import cs.common.Constant;
import cs.common.HqlBuilder;
import cs.common.utils.Validate;
import cs.common.ResultMsg;
import cs.common.utils.StringUtil;
import cs.domain.expert.Expert;
import cs.domain.expert.ExpertSelected;
import cs.domain.expert.ExpertSelected_;
import cs.model.expert.ExpertSelectHis;
import cs.model.PageModelDto;
import cs.model.expert.ExpertReviewConSimpleDto;
import cs.model.expert.ExpertReviewCondBusDto;
import cs.model.expert.ExpertReviewCondDto;
import cs.repository.AbstractRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

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
    private  ExpertRepo expertRepo;
    /**
     * 根据大类，小类和专家类别确认已经抽取的专家
     * @param maJorBig
     * @param maJorSmall
     * @param expeRttype
     * @return
     */
    @Override
    public int findConfirmSeletedEP(String reviewId,String maJorBig,String maJorSmall,String expeRttype) {
        HqlBuilder sqlBuilder = HqlBuilder.create();
        sqlBuilder.append("select count(ID) from cs_expert_selected where expertreviewid =:reviewId ");
        sqlBuilder.setParam("reviewId",reviewId);
        sqlBuilder.append(" and "+ExpertSelected_.isConfrim.getName()+" =:isConfrim ");
        sqlBuilder.setParam("isConfrim", Constant.EnumState.YES.getValue());
        sqlBuilder.append(" and "+ExpertSelected_.maJorBig.getName()+" =:maJorBig ");
        sqlBuilder.setParam("maJorBig",maJorBig);
        sqlBuilder.append(" and "+ExpertSelected_.maJorSmall.getName()+" =:maJorSmall ");
        sqlBuilder.setParam("maJorSmall",maJorSmall);
        sqlBuilder.append(" and "+ExpertSelected_.expeRttype.getName()+" =:expeRttype ");
        sqlBuilder.setParam("expeRttype",expeRttype);
        return returnIntBySql(sqlBuilder);

        /*Criteria criteria = getExecutableCriteria();
        criteria.add(Restrictions.sqlRestriction(" re"));
        criteria.add(Restrictions.eq(ExpertSelected_.maJorBig.getName(),maJorBig));
        criteria.add(Restrictions.eq(ExpertSelected_.maJorSmall.getName(),maJorSmall));
        criteria.add(Restrictions.eq(ExpertSelected_.expeRttype.getName(), expeRttype));
        return ((Number) criteria.setProjection(Projections.rowCount()).uniqueResult()).intValue();*/
    }

    /**
     *专家评审基本情况详细统计
     * @param expertReviewCondDto
     * @return
     */
    @Override
    public ResultMsg expertReviewCondDetailCount(ExpertReviewCondDto expertReviewCondDto) {
        Map<String, Object> resultMap = new HashMap<>();
        PageModelDto<ExpertReviewCondDto> pageModelDto = new PageModelDto<ExpertReviewCondDto>();
        HqlBuilder sqlBuilder = HqlBuilder.create();
        sqlBuilder.append("select t.* from (   ");
        sqlBuilder.append("select e.expertid,e.expertno,e.name,e.company,r.reviewdate,s.projectname,s.reviewstage,s.signid,a.isletterrw  from cs_sign s  ");
        sqlBuilder.append("left join cs_expert_review r  ");
        sqlBuilder.append("on s.signid = r.businessid  ");
        sqlBuilder.append("left join cs_expert_selected a  ");
        sqlBuilder.append("on s.signid = a.businessid  ");
        sqlBuilder.append("left join cs_expert e  on a.expertid = e.expertid) t  ");
        sqlBuilder.append("where t.expertid is not null  ");
        sqlBuilder.append("order by t.expertno,t.isletterrw ");
        //todo:添加查询条件
        if(null != expertReviewCondDto){
        }
        List<Map> expertReviewConList = expertSelectedRepo.findMapListBySql(sqlBuilder);
        List<ExpertReviewCondDto> expertReviewConDtoList = new ArrayList<ExpertReviewCondDto>();
        String expertId = "";
        if (expertReviewConList.size() > 0) {
            for (int i = 0; i < expertReviewConList.size(); i++) {
                Object obj = expertReviewConList.get(i);
                Object[] expertReviewCon = (Object[]) obj;
                ExpertReviewCondDto expertReviewDto = new ExpertReviewCondDto();
                if(StringUtil.isNotEmpty(expertId) && null != expertReviewCon[0]){
                    if(expertId.equals((String)expertReviewCon[0])){
                        ExpertReviewCondBusDto expertReviewCondBusDto = new ExpertReviewCondBusDto();
                        if (null != expertReviewCon[4]) {
                            expertReviewCondBusDto.setReviewDate((Date) expertReviewCon[4]);
                        }else{
                            expertReviewCondBusDto.setReviewDate(null);
                        }
                        if (null != expertReviewCon[5]) {
                            expertReviewCondBusDto.setProjectName((String) expertReviewCon[5]);
                        }else{
                            expertReviewCondBusDto.setProjectName(null);
                        }
                        if (null != expertReviewCon[6]) {
                            expertReviewCondBusDto.setReviewStage((String) expertReviewCon[6]);
                        }else{
                            expertReviewCondBusDto.setReviewStage(null);
                        }
                        if (null != expertReviewCon[7]) {
                            expertReviewCondBusDto.setSignId((String) expertReviewCon[7]);
                        }else{
                            expertReviewCondBusDto.setSignId(null);
                        }
                        if (null != expertReviewCon[8]) {
                            expertReviewCondBusDto.setIsLetterRw((String) expertReviewCon[8]);
                        }else{
                            expertReviewCondBusDto.setIsLetterRw(null);
                        }
                        List<ExpertReviewCondBusDto> expertReviewCondBusDtoList =expertReviewConDtoList.get(expertReviewConDtoList.size()-1).getExpertReviewCondBusDtoList();
                        expertReviewCondBusDtoList .add(expertReviewCondBusDto);
                        expertReviewConDtoList.get(expertReviewConDtoList.size()-1).setExpertReviewCondBusDtoList(null);
                        expertReviewConDtoList.get(expertReviewConDtoList.size()-1).setExpertReviewCondBusDtoList(expertReviewCondBusDtoList);
                        continue;
                    }
                }
                if (null != expertReviewCon[0]) {
                    expertReviewDto.setExpertID((String) expertReviewCon[0]);
                    expertId = expertReviewDto.getExpertID();
                }else{
                    expertReviewDto.setExpertID(null);
                }
                if (null != expertReviewCon[2]) {
                    expertReviewDto.setName((String) expertReviewCon[2]);
                }else{
                    expertReviewDto.setName(null);
                }

                if (null != expertReviewCon[3]) {
                    expertReviewDto.setComPany((String) expertReviewCon[3]);
                }else{
                    expertReviewDto.setComPany(null);
                }
                List<ExpertReviewCondBusDto> expertReviewCondBusDtoList = new ArrayList<ExpertReviewCondBusDto>();
                ExpertReviewCondBusDto expertReviewCondBusDto = new ExpertReviewCondBusDto();
                if (null != expertReviewCon[4]) {
                    expertReviewCondBusDto.setReviewDate((Date) expertReviewCon[4]);
                }else{
                    expertReviewCondBusDto.setReviewDate(null);
                }
                if (null != expertReviewCon[5]) {
                    expertReviewCondBusDto.setProjectName((String) expertReviewCon[5]);
                }else{
                    expertReviewCondBusDto.setProjectName(null);
                }
                if (null != expertReviewCon[6]) {
                    expertReviewCondBusDto.setReviewStage((String) expertReviewCon[6]);
                }else{
                    expertReviewCondBusDto.setReviewStage(null);
                }
                if (null != expertReviewCon[7]) {
                    expertReviewCondBusDto.setSignId((String) expertReviewCon[7]);
                }else{
                    expertReviewCondBusDto.setSignId(null);
                }
                if (null != expertReviewCon[8]) {
                    expertReviewCondBusDto.setIsLetterRw((String) expertReviewCon[8]);
                }else{
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
     * @param expertReviewConSimpleDto
     * @return
     */
    @Override
    public ResultMsg expertReviewConSimpleCount(ExpertReviewConSimpleDto expertReviewConSimpleDto) {
        Map<String, Object> resultMap = new HashMap<>();
        PageModelDto<ExpertReviewConSimpleDto> pageModelDto = new PageModelDto<ExpertReviewConSimpleDto>();
        //评审、函次数sql
        HqlBuilder sqlBuilder = HqlBuilder.create();
        sqlBuilder.append("select t.expertid,count(t.expertid)reviewCount,t.isletterrw from (   ");
        sqlBuilder.append("select  e.expertid,e.expertno,e.name,e.company,r.reviewdate,s.projectname,s.reviewstage,s.signid,a.isletterrw from cs_sign s   ");
        sqlBuilder.append("left join cs_expert_review r  ");
        sqlBuilder.append("on s.signid = r.businessid  ");
        sqlBuilder.append("left join cs_expert_selected a  ");
        sqlBuilder.append("on s.signid = a.businessid  ");
        sqlBuilder.append("left join cs_expert e  ");
        sqlBuilder.append("on a.expertid = e.expertid) t  ");
        sqlBuilder.append("where t.expertid is not null   ");
        sqlBuilder.append("group by t.expertid,t.expertno,t.isletterrw   ");
        sqlBuilder.append("order by t.expertno  ");

        //评审总次数
        HqlBuilder sqlBuilder1 = HqlBuilder.create();
        sqlBuilder1.append("select t1.expertid,sum(t1.reviewCount) from (  ");
        sqlBuilder1.append("select t.expertid,t.expertno,count(t.expertid) reviewCount,t.isletterrw   ");
        sqlBuilder1.append("select t.expertid,t.expertno,count(t.expertid) reviewCount,t.isletterrw  from (   ");
        sqlBuilder1.append("select  e.expertid,e.expertno,e.name,e.company,r.reviewdate,s.projectname,s.reviewstage,s.signid,a.isletterrw from cs_sign s  ");
        sqlBuilder1.append("left join cs_expert_review r  ");
        sqlBuilder1.append("on s.signid = r.businessid  ");
        sqlBuilder1.append("left join cs_expert_selected a  ");
        sqlBuilder1.append("on s.signid = a.businessid  ");
        sqlBuilder1.append("left join cs_expert e  ");
        sqlBuilder1.append(" on a.expertid = e.expertid) t  ");
        sqlBuilder1.append(" where t.expertid is not null  ");
        sqlBuilder1.append("group by t.expertid,t.expertno,t.isletterrw ) t1  ");
        sqlBuilder1.append("group by t1.expertid,t1.expertno  ");
        sqlBuilder1.append("order by t1.expertno  ");

        //todo:添加查询条件
        if(null != expertReviewConSimpleDto){
        }
        List<Map> expertReviewConSimList = expertSelectedRepo.findMapListBySql(sqlBuilder);
        List<Map> expertReviewConSimList1 = expertSelectedRepo.findMapListBySql(sqlBuilder1);
        List<ExpertReviewConSimpleDto> expertRevConSimDtoList = new ArrayList<ExpertReviewConSimpleDto>();
        if (expertReviewConSimList.size() > 0) {
            for(int i=0;i<expertReviewConSimList.size();i++){
                Object obj = expertReviewConSimList.get(i);
                Object[] expertReviewConSim = (Object[]) obj;
                ExpertReviewConSimpleDto expertReviewSimDto = new ExpertReviewConSimpleDto();
                if (null != expertReviewConSim[0]) {
                    expertReviewSimDto.setExpertID((String) expertReviewConSim[0]);
                    Expert expert =  expertRepo.findById(expertReviewSimDto.getExpertID());
                    expertReviewSimDto.setName(expert.getName());
                    expertReviewSimDto.setComPany(expert.getComPany());
                    expertReviewSimDto.setTotalNum(getExpertRevTotalNum(expertReviewConSimList1,expertReviewSimDto.getExpertID()));
                }else{
                    expertReviewSimDto.setExpertID(null);
                }

                if (null != expertReviewConSim[2]) {
                    BigDecimal temp = (BigDecimal)expertReviewConSim[2];
                    if (temp.equals(0)){//评审
                        if (null != expertReviewConSim[1]) {
                            expertReviewSimDto.setReviewNum((BigDecimal) expertReviewConSim[1]);
                        }else{
                            expertReviewSimDto.setReviewNum(null);
                        }
                    }else{//函评
                        if (null != expertReviewConSim[1]) {
                            expertReviewSimDto.setLetterRwNum((BigDecimal) expertReviewConSim[1]);
                        }else{
                            expertReviewSimDto.setLetterRwNum(null);
                        }
                    }
                }else{//评审
                    if (null != expertReviewConSim[1]) {
                        expertReviewSimDto.setReviewNum((BigDecimal) expertReviewConSim[1]);
                    }else{
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
     * 获取总参会次数
     * @param expertReviewConSimList1
     * @param expertId
     * @return
     */
    private BigDecimal getExpertRevTotalNum(List<Map>expertReviewConSimList1,String expertId){
        BigDecimal totalNum = null  ;
        if(expertReviewConSimList1.size()>0){
            for(int i=0;i<expertReviewConSimList1.size();i++){
                Object obj = expertReviewConSimList1.get(i);
                Object[] expertReviewConSim = (Object[]) obj;
                if (null != expertReviewConSim[0]) {
                    String temp = (String) expertReviewConSim[0];
                    if (StringUtil.isNotEmpty(expertId) && StringUtil.isNotEmpty(temp) && expertId.equals(temp)){
                        totalNum = (BigDecimal) expertReviewConSim[1];
                        break;
                    }

                }
            }
        }
        return  totalNum;
    }



    /**
     * 根据业务ID统计已经确认的抽取专家
     * @param businessId
     * @return
     */
    @Override
    public int getSelectEPCount(String businessId) {
        HqlBuilder sqlBuilder = HqlBuilder.create();
        sqlBuilder.append(" select count(id) from cs_expert_selected where "+ExpertSelected_.businessId.getName()+"=:businessId ");
        sqlBuilder.setParam("businessId",businessId);
        sqlBuilder.append(" and "+ExpertSelected_.isConfrim.getName()+"=:isConfrim ");
        sqlBuilder.setParam("isConfrim", Constant.EnumState.YES.getValue());
        sqlBuilder.append(" and "+ExpertSelected_.isJoin.getName()+"=:isJoin ");
        sqlBuilder.setParam("isJoin", Constant.EnumState.YES.getValue());
        sqlBuilder.append(" and "+ExpertSelected_.selectType.getName()+"=:selectType ");
        sqlBuilder.setParam("selectType", Constant.EnumExpertSelectType.AUTO.getValue());
        return returnIntBySql(sqlBuilder);
    }

    /**
     * 专家抽取统计
     * @param expertSelectHis
     * @return
     */
    @Override
    public List<Object[]> getSelectHis(ExpertSelectHis expertSelectHis) {
        HqlBuilder sqlBuilder = HqlBuilder.create();
        sqlBuilder.append(" SELECT EX.EXPERTID,EX.NAME,EX.COMPANY,EX.EXPERTFIELD,WP.PROJECTNAME,ES.MAJORBIG,ES.MAJORSMALL,");
        sqlBuilder.append(" ES.EXPERTTYPE, ES.SELECTTYPE, ES.ISCONFRIM,WP.REVIEWTYPE,ER.REVIEWDATE,WP.MIANCHARGEUSERNAME ");
        sqlBuilder.append("  FROM CS_EXPERT ex ");
        sqlBuilder.append("  LEFT JOIN CS_EXPERT_SELECTED es ON EX.EXPERTID = ES.EXPERTID ");
        sqlBuilder.append("  LEFT JOIN CS_EXPERT_REVIEW er ON ER.ID = ES.EXPERTREVIEWID ");
        sqlBuilder.append("  LEFT JOIN CS_WORK_PROGRAM wp ON WP.ID = ES.BUSINESSID ");
        sqlBuilder.append("  WHERE ex.state != '3' AND ex.state != '4' AND ES.ID IS NOT NULL ");
        if(expertSelectHis != null){
            if(Validate.isString(expertSelectHis.getEpName())){
                sqlBuilder.append(" AND EX.NAME like :epName ").setParam("epName","%"+expertSelectHis.getEpName()+"%");
            }
            if(Validate.isString(expertSelectHis.getBeginTime())){
                sqlBuilder.append(" AND ER.REVIEWDATE > to_date(:beginTime,'yyyy-mm-dd hh24:mi:ss') ").setParam("beginTime",expertSelectHis.getBeginTime().trim() + " 00:00:00");
            }
            if(Validate.isString(expertSelectHis.getEndTime())){
                sqlBuilder.append(" AND ER.REVIEWDATE < to_date(:endTime,'yyyy-mm-dd hh24:mi:ss') ").setParam("endTime",expertSelectHis.getEndTime().trim() + " 23:59:59");
            }
            if(Validate.isString(expertSelectHis.getReviewType())){
                sqlBuilder.append(" AND WP.REVIEWTYPE =:reviewType ").setParam("reviewType",expertSelectHis.getReviewType());
            }
            if(Validate.isString(expertSelectHis.getSelectType())){
                sqlBuilder.append(" AND ES.SELECTTYPE =:selectType ").setParam("selectType",expertSelectHis.getSelectType());
            }

            if(Validate.isString(expertSelectHis.getMajorBig())){
                sqlBuilder.append(" AND ES.MAJORSMALL =:majorBig ").setParam("majorBig",expertSelectHis.getMajorBig());
            }
            if(Validate.isString(expertSelectHis.getMarjorSmall())){
                sqlBuilder.append(" AND ES.MAJORSMALL =:marjorSmall ").setParam("marjorSmall",expertSelectHis.getMarjorSmall());
            }
            if(Validate.isString(expertSelectHis.getExpertType())){
                sqlBuilder.append(" AND ES.EXPERTTYPE =:expertType ").setParam("expertType",expertSelectHis.getExpertType());
            }
            if(Validate.isString(expertSelectHis.getIsConfirm())){
                sqlBuilder.append(" AND ES.ISCONFRIM =:isConfirm ").setParam("isConfirm",expertSelectHis.getIsConfirm());
            }
        }

        sqlBuilder.append("  ORDER BY EX.EXPERTNO ");

        return getObjectArray(sqlBuilder);
    }
}