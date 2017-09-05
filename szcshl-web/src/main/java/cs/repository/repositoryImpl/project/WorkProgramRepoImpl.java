package cs.repository.repositoryImpl.project;

import cs.common.HqlBuilder;
import cs.common.utils.SessionUtil;
import cs.common.utils.Validate;
import cs.domain.project.*;
import cs.domain.sys.Org;
import cs.domain.sys.Org_;
import cs.repository.AbstractRepository;
import cs.repository.repositoryImpl.sys.OrgRepo;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class WorkProgramRepoImpl extends AbstractRepository<WorkProgram,String> implements WorkProgramRepo {

    @Autowired
    private OrgRepo orgRepo;
    /**
     * 根据收文编号，查询对应工作方案
     * @param signId
     * @return
     */
    @Override
    public WorkProgram findByPrincipalUser(String signId) {
        Criteria criteria = getExecutableCriteria();
        criteria.createAlias(WorkProgram_.sign.getName(), WorkProgram_.sign.getName());
        criteria.add(Restrictions.eq(WorkProgram_.sign.getName()+ "." + Sign_.signid.getName(), signId));
        criteria.add(Restrictions.sqlRestriction(" branchId = (select flowBranch from cs_sign_principal2 where signId ='"+signId+"' and userId ='"+ SessionUtil.getUserInfo().getId()+"' )"));
        return (WorkProgram)criteria.uniqueResult();
    }

    /**
     * 获取评审部门
     * @param signId
     * @return
     */
    @Override
    public List<Org> getReviewOrg(String signId) {
        HqlBuilder sqlBuilder = HqlBuilder.create();
        sqlBuilder.append("SELECT co.* FROM cs_org co, CS_SIGN_BRANCH csb WHERE CO.ID = csb.ORGID AND csb.signid = :signid ORDER BY TO_NUMBER (csb.ISMAINBRABCH) DESC");
        sqlBuilder.setParam("signid",signId);
        return orgRepo.findBySql(sqlBuilder);
    }

    /**
     * 根据收文ID和分支ID查询对应的工作方案
     * @param signId
     * @param branchId
     * @return
     */
    @Override
    public WorkProgram findBySignIdAndBranchId(String signId, String branchId) {
        HqlBuilder hqlBuilder = HqlBuilder.create();
        hqlBuilder.append(" from "+WorkProgram.class.getSimpleName()+" where "+WorkProgram_.sign.getName()+"."+Sign_.signid.getName()+" =:signId ");
        hqlBuilder.setParam("signId",signId);
        hqlBuilder.append(" and "+WorkProgram_.branchId.getName()+" =:branchId ").setParam("branchId",branchId);
        List<WorkProgram> wpList = findByHql(hqlBuilder);
        if(Validate.isList(wpList)){
            return wpList.get(0);
        }
        return null;
    }

    /**
     * 更新工作方案的专家评审方案信息
     * @param wpId
     * @param reviewId
     */
    @Override
    public void updateReviewId(String wpId, String reviewId) {
        HqlBuilder hqlBuilder = HqlBuilder.create();
        hqlBuilder.append(" update "+WorkProgram.class.getSimpleName()+" set "+WorkProgram_.expertReviewId.getName()+" =:expertReviewId ");
        hqlBuilder.setParam("expertReviewId",reviewId);
        hqlBuilder.append(" where "+WorkProgram_.id.getName()+" =:wpId ");
        hqlBuilder.setParam("wpId",wpId);
        executeHql(hqlBuilder);
    }

    /**
     * 初始化专家评审费，默认每个专家1000元
     * @param id
     */
    @Override
    public void initExpertCost(String id) {
        //1、统计已经确认的专家个数
        HqlBuilder sqlBuilder = HqlBuilder.create();
        sqlBuilder.append(" UPDATE CS_WORK_PROGRAM wp SET wp.expertCost = 1000 * (SELECT COUNT (ID) FROM CS_EXPERT_SELECTED es WHERE es.EXPERTREVIEWID = wp.EXPERTREVIEWID AND es.ISCONFRIM = '9') " );
        sqlBuilder.append(" WHERE wp.id = :id");
        sqlBuilder.setParam("id",id);
        executeSql(sqlBuilder);
    }
}
