package cs.repository.repositoryImpl.project;

import cs.common.Constant;
import cs.common.HqlBuilder;
import cs.common.utils.Validate;
import cs.domain.expert.ExpertReview_;
import cs.domain.expert.ExpertSelected_;
import cs.domain.project.Sign;
import cs.domain.project.Sign_;
import cs.repository.AbstractRepository;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class SignRepoImpl extends AbstractRepository<Sign, String> implements SignRepo {

    /**
     * 更改流程状态
     *
     * @param state
     * @return
     */
    @Override
    public boolean updateSignState(String signId, String state) {
        HqlBuilder hqlBuilder = HqlBuilder.create();
        hqlBuilder.append(" update " + Sign.class.getSimpleName() + " set " + Sign_.signState.getName() + " =:state ");
        hqlBuilder.setParam("state", state);
        hqlBuilder.append(" where " + Sign_.signid.getName() + " =:signid ");
        hqlBuilder.setParam("signid", signId);

        return executeHql(hqlBuilder) >= 0 ? true : false;
    }

    /**
     * 更改项目流程状态
     * @param signId
     * @param processState
     * @return
     */
    @Override
    public boolean updateSignProcessState(String signId, Integer processState) {
        HqlBuilder hqlBuilder = HqlBuilder.create();
        hqlBuilder.append(" update " + Sign.class.getSimpleName() + " set " + Sign_.processState.getName() + " =:processState ");
        hqlBuilder.setParam("processState", processState);
        hqlBuilder.append(" where " + Sign_.signid.getName() + " =:signid ");
        hqlBuilder.setParam("signid", signId);
        return executeHql(hqlBuilder) >= 0 ? true : false;
    }

    /**
     * 根据委里收文编号，获取项目信息
     * @param filecode
     * @return
     */
    @Override
    public Sign findByFilecode(String filecode) {
        HqlBuilder hqlBuilder = HqlBuilder.create();
        hqlBuilder.append(" from  " + Sign.class.getSimpleName());
        hqlBuilder.append(" where " + Sign_.filecode.getName() + " = :filecode ");
        hqlBuilder.setParam("filecode", filecode);

        List<Sign> signList = findByHql(hqlBuilder);
        if(Validate.isList(signList)){
            return signList.get(0);
        }else{
            return null;
        }
    }

    /**
     * 是否完成专家评分
     * @param signId
     * @return
     */
    @Override
    public boolean isFinishEPGrade(String signId) {
        /**
         * SELECT count(eps.id)
         FROM CS_EXPERT_SELECTED eps
         WHERE     eps.ISCONFRIM = '9'
         AND EPS.ISJOIN = '9'
         AND (EPS.SCORE IS NULL OR EPS.SCORE = 0)
         AND EPS.EXPERTREVIEWID IN (SELECT epr.ID
         FROM CS_EXPERT_REVIEW epr
         WHERE epr.id IN (SELECT EXPERTREVIEWID
         FROM CS_WORK_PROGRAM
         WHERE signid =
         '0626e2a3-457a-49b6-b336-02068dc4c5cc'))
         */
        //有评审专家，才要判断
        if(isHaveEPReviewCost(signId)){
            HqlBuilder sqlBuilder = HqlBuilder.create();
            sqlBuilder.append("SELECT count(eps.id) FROM cs_expert_selected eps WHERE eps."+ ExpertSelected_.isConfrim.getName()+" =:isConfrim ");
            sqlBuilder.setParam("isConfrim", Constant.EnumState.YES.getValue());
            sqlBuilder.append(" and eps."+ExpertSelected_.isJoin.getName()+" =:isJoin ");
            sqlBuilder.setParam("isJoin", Constant.EnumState.YES.getValue());
            sqlBuilder.append(" and (eps."+ExpertSelected_.score.getName()+" is null or eps."+ExpertSelected_.score.getName()+" = 0 ) ");
            sqlBuilder.append(" and eps.expertReviewId in ( SELECT epr.id FROM cs_expert_review epr WHERE epr."+ ExpertReview_.id.getName());
            sqlBuilder.append(" in (SELECT wp.expertReviewId FROM cs_work_program wp where wp.signid =:signid ))");
            sqlBuilder.setParam("signid",signId);

            int resultInt = returnIntBySql(sqlBuilder);
            return (resultInt > 0)?false:true;
        }
        return true;
    }

    /**
     * 是否有专家评审费
     * @param signId
     * @return
     */
    @Override
    public boolean isHaveEPReviewCost(String signId) {
        HqlBuilder sqlBuilder = HqlBuilder.create();
        sqlBuilder.append("SELECT count(eps.id) FROM cs_expert_selected eps WHERE eps."+ ExpertSelected_.isConfrim.getName()+" =:isConfrim ");
        sqlBuilder.setParam("isConfrim", Constant.EnumState.YES.getValue());
        sqlBuilder.append(" and eps."+ExpertSelected_.isJoin.getName()+" =:isJoin ");
        sqlBuilder.setParam("isJoin", Constant.EnumState.YES.getValue());
        sqlBuilder.append(" and eps.expertReviewId in ( SELECT epr.id FROM cs_expert_review epr WHERE epr."+ ExpertReview_.id.getName());
        sqlBuilder.append(" in (SELECT wp.expertReviewId FROM cs_work_program wp where wp.signid =:signid ))");
        sqlBuilder.setParam("signid",signId);

        int resultInt = returnIntBySql(sqlBuilder);
        return (resultInt > 0)?true:false;
    }

}
