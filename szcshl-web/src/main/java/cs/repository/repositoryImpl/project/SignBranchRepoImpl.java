package cs.repository.repositoryImpl.project;

import cs.common.Constant;
import cs.common.HqlBuilder;
import cs.common.utils.SessionUtil;
import cs.domain.flow.RuProcessTask_;
import cs.domain.project.SignBranch;
import cs.domain.project.SignBranch_;
import cs.repository.AbstractRepository;
import org.hibernate.Criteria;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;


@Repository
public class SignBranchRepoImpl extends AbstractRepository<SignBranch, String> implements SignBranchRepo {
    /**
     * 完成分支工作方案
     * @param signId
     * @param branchId
     */
    @Override
    public void finishWP(String signId, String branchId) {
        HqlBuilder hqlBuilder = HqlBuilder.create();
        hqlBuilder.append(" update "+SignBranch.class.getSimpleName()+" set "+ SignBranch_.isEndWP.getName()+" =:isEndWP ");
        hqlBuilder.setParam("isEndWP", Constant.EnumState.YES.getValue());
        hqlBuilder.append(" ,"+SignBranch_.isNeedWP.getName()+" =:isNeedWP ");
        hqlBuilder.setParam("isNeedWP",Constant.EnumState.YES.getValue());
        hqlBuilder.append(" where "+SignBranch_.signId.getName()+" =:signId and "+SignBranch_.branchId.getName()+" =:branchId ");
        hqlBuilder.setParam("signId",signId).setParam("branchId",branchId);
        executeHql(hqlBuilder);
    }

    /**
     * 验证是否完成了工作方案
     * @param signId
     * @param branchId
     * @return
     */
    @Override
    public boolean checkFinishWP(String signId, String branchId) {
        Criteria criteria = getExecutableCriteria();
        criteria.add(Restrictions.eq(SignBranch_.signId.getName(),signId));
        criteria.add(Restrictions.eq(SignBranch_.branchId.getName(),branchId));
        criteria.add(Restrictions.eq(SignBranch_.isEndWP.getName(), Constant.EnumState.YES.getValue()));
        Integer totalResult = ((Number) criteria.setProjection(Projections.rowCount()).uniqueResult()).intValue();
        return (totalResult == null || totalResult== 0)?false:true;
    }

    /**
     * 完成分支操作
     * @param signId
     * @param branchId
     */
    @Override
    public void finishBranch(String signId, String branchId) {
        HqlBuilder hqlBuilder = HqlBuilder.create();
        hqlBuilder.append(" update "+SignBranch.class.getSimpleName()+" set "+ SignBranch_.isFinished.getName()+" =:isFinished ");
        hqlBuilder.setParam("isFinished", Constant.EnumState.YES.getValue());
        hqlBuilder.append(" where "+SignBranch_.signId.getName()+" =:signId and "+SignBranch_.branchId.getName()+" =:branchId ");
        hqlBuilder.setParam("signId",signId).setParam("branchId",branchId);
        executeHql(hqlBuilder);
    }

    /**
     * 分支工作方案状态
     * @param signId
     * @param branchId
     * @param state
     */
    @Override
    public void isNeedWP(String signId, String branchId,String state) {
        HqlBuilder hqlBuilder = HqlBuilder.create();
        hqlBuilder.append(" update "+SignBranch.class.getSimpleName()+" set "+ SignBranch_.isNeedWP.getName()+" =:state ");
        hqlBuilder.setParam("state",state);
        //如果不需要，则没有完成工作方案
        if(Constant.EnumState.NO.getValue().equals(state)){
            hqlBuilder.append(" ,"+SignBranch_.isEndWP.getName()+" =:isEndWP ");
            hqlBuilder.setParam("isEndWP",Constant.EnumState.NO.getValue());
        }
        hqlBuilder.append(" where "+SignBranch_.signId.getName()+" =:signId and "+SignBranch_.branchId.getName()+" =:branchId ");
        hqlBuilder.setParam("signId",signId).setParam("branchId",branchId);
        executeHql(hqlBuilder);
    }

    /**
     * 验证是否需要工作方案
     * @param signId
     * @param branchId
     * @return
     */
    @Override
    public boolean checkIsNeedWP(String signId, String branchId) {
        Criteria criteria = getExecutableCriteria();
        criteria.add(Restrictions.eq(SignBranch_.signId.getName(),signId));
        criteria.add(Restrictions.eq(SignBranch_.branchId.getName(),branchId));
        criteria.add(Restrictions.eq(SignBranch_.isNeedWP.getName(), Constant.EnumState.YES.getValue()));
        Integer totalResult = ((Number) criteria.setProjection(Projections.rowCount()).uniqueResult()).intValue();
        return (totalResult>0)?true:false;
    }

    /**
     * 协审分支是否全部完成
     * @param signId
     * @return
     */
    @Override
    public boolean assistFlowFinish(String signId) {
        Criteria criteria = getExecutableCriteria();
        criteria.add(Restrictions.eq(SignBranch_.signId.getName(),signId));
        criteria.add(Restrictions.eq(SignBranch_.isMainBrabch.getName(), Constant.EnumState.NO.getValue()));
        Disjunction dis = Restrictions.disjunction();
        dis.add(Restrictions.eq(SignBranch_.isFinished.getName(), Constant.EnumState.NO.getValue()));
        dis.add(Restrictions.isNull(SignBranch_.isFinished.getName()));
        Integer totalResult = ((Number) criteria.setProjection(Projections.rowCount()).uniqueResult()).intValue();

        return (totalResult > 0)?false:true;
    }

    /**
     * 所有协审分支（除了主分支）
     * @param signId
     * @return
     */
    @Override
    public int allAssistCount(String signId) {
        Criteria criteria = getExecutableCriteria();
        criteria.add(Restrictions.eq(SignBranch_.signId.getName(),signId));
        criteria.add(Restrictions.eq(SignBranch_.isMainBrabch.getName(), Constant.EnumState.NO.getValue()));
        Integer totalResult = ((Number) criteria.setProjection(Projections.rowCount()).uniqueResult()).intValue();
        return totalResult == null?0:totalResult;
    }

    /**
     * 完成的协审分支
     * @param signId
     * @return
     */
    @Override
    public int finishAssistCount(String signId) {
        Criteria criteria = getExecutableCriteria();
        criteria.add(Restrictions.eq(SignBranch_.signId.getName(),signId));
        criteria.add(Restrictions.eq(SignBranch_.isMainBrabch.getName(), Constant.EnumState.NO.getValue()));
        criteria.add(Restrictions.eq(SignBranch_.isFinished.getName(), Constant.EnumState.YES.getValue()));
        Integer totalResult = ((Number) criteria.setProjection(Projections.rowCount()).uniqueResult()).intValue();
        return totalResult == null?0:totalResult;
    }

    /**
     * 验证是否所有流程都已完成
     * @return
     */
    @Override
    public boolean allBranchFinish(String signId) {
        Criteria criteria = getExecutableCriteria();
        criteria.add(Restrictions.eq(SignBranch_.signId.getName(),signId));
        Disjunction dis = Restrictions.disjunction();
        dis.add(Restrictions.eq(SignBranch_.isFinished.getName(), Constant.EnumState.NO.getValue()));
        dis.add(Restrictions.isNull(SignBranch_.isFinished.getName()));
        criteria.add(dis);
        Integer totalResult = ((Number) criteria.setProjection(Projections.rowCount()).uniqueResult()).intValue();
        return (totalResult>0)?false:true;
    }

    /**
     * 是否已完成所有的工作方案
     * @param signId
     * @return
     */
    @Override
    public boolean allWPFinish(String signId) {
        Criteria criteria = getExecutableCriteria();
        criteria.add(Restrictions.eq(SignBranch_.signId.getName(),signId));
        criteria.add(Restrictions.eq(SignBranch_.isNeedWP.getName(), Constant.EnumState.YES.getValue()));
        Disjunction dis = Restrictions.disjunction();
        dis.add(Restrictions.eq(SignBranch_.isEndWP.getName(), Constant.EnumState.NO.getValue()));
        dis.add(Restrictions.isNull(SignBranch_.isEndWP.getName()));
        criteria.add(dis);
        Integer totalResult = ((Number) criteria.setProjection(Projections.rowCount()).uniqueResult()).intValue();
        return (totalResult>0)?false:true;
    }

}