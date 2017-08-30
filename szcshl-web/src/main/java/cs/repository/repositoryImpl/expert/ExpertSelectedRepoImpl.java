package cs.repository.repositoryImpl.expert;

import cs.common.Constant;
import cs.common.HqlBuilder;
import cs.domain.expert.ExpertSelected;
import cs.domain.expert.ExpertSelected_;
import cs.repository.AbstractRepository;
import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

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
}