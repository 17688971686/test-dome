package cs.repository.repositoryImpl.sys;

import cs.common.HqlBuilder;
import cs.common.cache.CacheConstant;
import cs.common.cache.CacheManager;
import cs.common.cache.ICache;
import cs.common.utils.Validate;
import cs.domain.sys.OrgDept;
import cs.domain.sys.OrgDept_;
import cs.domain.sys.User;
import cs.domain.sys.User_;
import cs.repository.AbstractRepository;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
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
        sqlBuilder.append(" order by "+User_.userSort.getName()+" nulls last");
        return userRepo.findBySql(sqlBuilder);
    }

    /**
     * 缓存查询所有部门信息
     * @return
     */
    @Override
    public List<OrgDept> findAllByCache() {
        ICache cache = CacheManager.getCache();
        List<OrgDept> list = (List<OrgDept>) cache.get(CacheConstant.ORG_DEPT_CACHE);
        if(!Validate.isList(list)){
            list = findAllByOrder();
        }
        return list;
    }

    /**
     * 缓存所有的部门信息
     */
    @Override
    public void fleshOrgDeptCache() {
        ICache cache = CacheManager.getCache();
        List<OrgDept> allList = findAllByOrder();
        cache.put(CacheConstant.ORG_DEPT_CACHE,allList);
    }

   private List<OrgDept> findAllByOrder(){
       Criteria criteria = getExecutableCriteria();
       criteria.addOrder(Order.asc(OrgDept_.sort.getName()));
       return criteria.list();
   }

    @Override
    public OrgDept findOrgDeptById(String id) {
        OrgDept result = null;
        ICache cache = CacheManager.getCache();
        List<OrgDept> list = (List<OrgDept>) cache.get(CacheConstant.ORG_DEPT_CACHE);
        for(OrgDept l : list){
            if(id.equals(l.getId())){
                result = l;
                break;
            }
        }
        if(result == null){
            result = findById(id);
            list.add(result);
            cache.put(CacheConstant.ORG_DEPT_CACHE,list);
        }

        return result;
    }
}
