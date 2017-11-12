package cs.repository.repositoryImpl.project;

import cs.common.Constant;
import cs.common.HqlBuilder;
import cs.common.utils.SessionUtil;
import cs.common.utils.Validate;
import cs.domain.flow.RuProcessTask_;
import cs.domain.project.SignBranch;
import cs.domain.project.SignBranch_;
import cs.domain.project.SignPrincipal;
import cs.domain.project.SignPrincipal_;
import cs.domain.sys.*;
import cs.repository.AbstractRepository;
import cs.repository.repositoryImpl.sys.OrgDeptRepo;
import cs.repository.repositoryImpl.sys.UserRepo;
import org.hibernate.Criteria;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public class SignBranchRepoImpl extends AbstractRepository<SignBranch, String> implements SignBranchRepo {
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private OrgDeptRepo orgDeptRepo;
    @Autowired
    private SignBranchRepo signBranchRepo;
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
     * 完成分支流程
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
     * 分支是否需要工作方案
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

    /**
     * 判断分支是否有工作方案
     * @param signId
     * @return
     */
    @Override
    public boolean isHaveWP(String signId) {
        Criteria criteria = getExecutableCriteria();
        criteria.add(Restrictions.eq(SignBranch_.signId.getName(),signId));
        criteria.add(Restrictions.eq(SignBranch_.isNeedWP.getName(), Constant.EnumState.YES.getValue()));
        criteria.add(Restrictions.eq(SignBranch_.isEndWP.getName(), Constant.EnumState.YES.getValue()));
        Integer totalResult = ((Number) criteria.setProjection(Projections.rowCount()).uniqueResult()).intValue();
        return (totalResult>0)?true:false;
    }

    /**
     * 查询分支的部门领导
     * @param signId
     * @return
     */
    @Override
    public List<User> findAssistOrgDirector(String signId) {
        HqlBuilder hqlBuilder = HqlBuilder.create();
        hqlBuilder.append(" select u from " + User.class.getSimpleName() + " u where u." + User_.id.getName() + " in ( ");
        hqlBuilder.append(" select og." + OrgDept_.directorID.getName() + " from " + OrgDept.class.getSimpleName() + " og ");
        hqlBuilder.append(" where og." + OrgDept_.id.getName() + " in (");
        hqlBuilder.append(" select bh." + SignBranch_.orgId.getName() + " from " + SignBranch.class.getSimpleName() + " bh ");
        hqlBuilder.append(" where bh."+SignBranch_.signId.getName()+" =:signId ").setParam("signId",signId);
        hqlBuilder.append(" and bh."+SignBranch_.isMainBrabch.getName()+" =:isMain ").setParam("isMain", Constant.EnumState.NO.getValue());
        hqlBuilder.append(" ) )");
        return userRepo.findByHql(hqlBuilder);
    }

    /**
     * 查询主办分支的领导
     * @param signId
     * @return
     */
    @Override
    public User findMainOrgDirector(String signId) {
        HqlBuilder hqlBuilder = HqlBuilder.create();
        hqlBuilder.append(" select u from " + User.class.getSimpleName() + " u where u." + User_.id.getName() + " = ( ");
        hqlBuilder.append(" select og." + OrgDept_.directorID.getName() + " from " + OrgDept.class.getSimpleName() + " og ");
        hqlBuilder.append(" where og." + OrgDept_.id.getName() + " = (");
        hqlBuilder.append(" select bh." + SignBranch_.orgId.getName() + " from " + SignBranch.class.getSimpleName() + " bh ");
        hqlBuilder.append(" where bh."+SignBranch_.signId.getName()+" =:signId ").setParam("signId",signId);
        hqlBuilder.append(" and bh."+SignBranch_.isMainBrabch.getName()+" =:isMain ").setParam("isMain", Constant.EnumState.YES.getValue());
        hqlBuilder.append(" ) )");
        List<User> list = userRepo.findByHql(hqlBuilder);
        if(Validate.isList(list)){
            return list.get(0);
        }
        return null;
    }

    /**
     * 查询分支的分管副主任
     * @param signId
     * @return
     */
    @Override
    public List<User> findAssistSLeader(String signId) {
        HqlBuilder hqlBuilder = HqlBuilder.create();
        hqlBuilder.append(" select u from " + User.class.getSimpleName() + " u where u." + User_.id.getName() + " in ( ");
        hqlBuilder.append(" select og." + OrgDept_.sLeaderID.getName() + " from " + OrgDept.class.getSimpleName() + " og ");
        hqlBuilder.append(" where og." + OrgDept_.id.getName() + " in (");
        hqlBuilder.append(" select bh." + SignBranch_.orgId.getName() + " from " + SignBranch.class.getSimpleName() + " bh ");
        hqlBuilder.append(" where bh."+SignBranch_.signId.getName()+" =:signId ").setParam("signId",signId);
        hqlBuilder.append(" and bh."+SignBranch_.isMainBrabch.getName()+" =:isMain ").setParam("isMain", Constant.EnumState.NO.getValue());
        hqlBuilder.append(" ) )");
        return userRepo.findByHql(hqlBuilder);
    }

    /**
     * 查询主办分支的分管领导
     * @param signId
     * @return
     */
    @Override
    public User findMainSLeader(String signId) {
        HqlBuilder hqlBuilder = HqlBuilder.create();
        hqlBuilder.append(" select u from " + User.class.getSimpleName() + " u where u." + User_.id.getName() + " = ( ");
        hqlBuilder.append(" select og." + OrgDept_.sLeaderID.getName() + " from " + OrgDept.class.getSimpleName() + " og ");
        hqlBuilder.append(" where og." + OrgDept_.id.getName() + " = (");
        hqlBuilder.append(" select bh." + SignBranch_.orgId.getName() + " from " + SignBranch.class.getSimpleName() + " bh ");
        hqlBuilder.append(" where bh."+SignBranch_.signId.getName()+" =:signId ").setParam("signId",signId);
        hqlBuilder.append(" and bh."+SignBranch_.isMainBrabch.getName()+" =:isMain ").setParam("isMain", Constant.EnumState.YES.getValue());
        hqlBuilder.append(" ) )");
        List<User> list = userRepo.findByHql(hqlBuilder);
        if(Validate.isList(list)){
            return list.get(0);
        }
        return null;
    }

    /**
     * 获取项目的评审单位,按主评审排序
     * @param signId
     * @return
     */
    @Override
    public List<OrgDept> getOrgDeptBySignId(String signId) {
        HqlBuilder hqlBuilder = HqlBuilder.create();
        hqlBuilder.append("select o from "+OrgDept.class.getSimpleName()+" o left join ");
        hqlBuilder.append(SignBranch.class.getSimpleName()+" s on o."+OrgDept_.id.getName()+" = s."+SignBranch_.orgId.getName());
        hqlBuilder.append(" and s."+SignBranch_.signId.getName() +" =:signId ");
        hqlBuilder.setParam("signId",signId);
        hqlBuilder.append(" where s."+SignBranch_.signId.getName()+" is not null ");
        hqlBuilder.append(" order by s."+SignBranch_.branchId.getName());

        /*Criteria criteria = orgDeptRepo.getExecutableCriteria();
        StringBuffer sb = new StringBuffer();
        sb.append(" id in ( select "+SignBranch_.orgId.getName()+" from cs_sign_branch ");
        sb.append(" where "+SignBranch_.signId.getName()+" = '"+signId+"' )");
        criteria.add(Restrictions.sqlRestriction(sb.toString()));
        return criteria.list();
        */

        return orgDeptRepo.findByHql(hqlBuilder);
    }

    /**
     * 根据部门ID和项目ID，查询所在分支信息
     * @param signId
     * @param orgId
     * @return
     */
    @Override
    public SignBranch findByOrgDirector(String signId, String orgId) {
        Criteria criteria = signBranchRepo.getExecutableCriteria();
        criteria.add(Restrictions.eq(SignBranch_.signId.getName(),signId));
        criteria.add(Restrictions.eq(SignBranch_.orgId.getName(),orgId));
        List<SignBranch> resuList = criteria.list();
        if(Validate.isList(resuList)){
            return resuList.get(0);
        }
        return null;
    }


}