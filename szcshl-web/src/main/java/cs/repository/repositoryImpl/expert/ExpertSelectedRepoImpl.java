package cs.repository.repositoryImpl.expert;

import cs.common.Constant;
import cs.common.HqlBuilder;
import cs.common.utils.Validate;
import cs.domain.expert.ExpertSelected;
import cs.domain.expert.ExpertSelected_;
import cs.model.expert.ExpertSelectHis;
import cs.repository.AbstractRepository;
import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Description: 抽取专家 数据操作实现类
 * author: ldm
 * Date: 2017-6-14 14:26:49
 */
@Repository
public class ExpertSelectedRepoImpl extends AbstractRepository<ExpertSelected, String> implements ExpertSelectedRepo {

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