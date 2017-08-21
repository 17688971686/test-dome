package cs.repository.repositoryImpl.sys;

import cs.common.HqlBuilder;
import cs.domain.sys.OrgDept;
import cs.domain.sys.User;
import cs.domain.sys.User_;
import cs.repository.AbstractRepository;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class OrgDeptRepoImpl extends AbstractRepository<OrgDept, String> implements OrgDeptRepo {

    @Autowired
    private UserRepo userRepo;
    /**
     * 根据项目签收流程分支查询
     * @param branchId
     * @return
     */
    @Override
    public OrgDept queryBySignBranchId(String signId,String branchId) {
        Criteria criteria = getExecutableCriteria();
        criteria.add(Restrictions.sqlRestriction(" id = (select orgid from CS_SIGN_BRANCH where signid='"+signId+"' and branchid = '"+branchId+"')"));
        return (OrgDept) criteria.uniqueResult();
    }

    /**
     * 根据ID 查询部门下的用户
     * @param id
     * @return
     */
    @Override
    public List<User> queryOrgDeptUser(String id) {
        HqlBuilder sqlBuilder = HqlBuilder.create();
        sqlBuilder.append(" select u.* from cs_user u where u."+User_.jobState.getName()+" =:jobState ").setParam("jobState","t");
        sqlBuilder.append(" and u.orgId = :orgId ").setParam("orgId",id);
        sqlBuilder.append(" or u.id IN (SELECT USERLIST_ID FROM CS_DEPT_CS_USER WHERE SYSDEPTLIST_ID =:orgId2) ");
        sqlBuilder.setParam("orgId2",id);
        return userRepo.findBySql(sqlBuilder);
    }

    /**
     * 根据项目ID和分支查询对应的用户信息
     * @param signId
     * @param branchId
     * @return
     */
    @Override
    public List<User> queryOrgDeptUser(String signId, String branchId) {
        HqlBuilder sqlBuilder = HqlBuilder.create();
        sqlBuilder.append(" select u.* from cs_user u where u."+User_.jobState.getName()+" =:jobState ").setParam("jobState","t");
        sqlBuilder.append(" and u.orgId = (SELECT orgid FROM CS_SIGN_BRANCH WHERE signid =:signid AND branchid =:branchid) ");
        sqlBuilder.setParam("signid",signId).setParam("branchid",branchId);
        sqlBuilder.append(" OR (u.id IN (SELECT USERLIST_ID FROM CS_DEPT_CS_USER WHERE SYSDEPTLIST_ID = (SELECT orgid FROM CS_SIGN_BRANCH WHERE signid =:signid2 AND branchid =:branchid2))) ");
        sqlBuilder.setParam("signid2",signId).setParam("branchid2",branchId);
        return userRepo.findBySql(sqlBuilder);
    }
}
