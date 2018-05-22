package cs.repository.repositoryImpl.sys;

import cs.common.constants.Constant;
import cs.common.HqlBuilder;
import cs.common.cache.CacheConstant;
import cs.common.cache.CacheManager;
import cs.common.cache.ICache;
import cs.common.utils.BeanCopierUtils;
import cs.common.utils.StringUtil;
import cs.common.utils.Validate;
import cs.domain.sys.*;
import cs.model.sys.OrgDto;
import cs.model.sys.UserDto;
import cs.repository.AbstractRepository;
import cs.repository.odata.ODataObj;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class UserRepoImpl extends AbstractRepository<User, String> implements UserRepo {
    @Autowired
    private OrgDeptRepo orgDeptRepo;
    @Override
    public User findUserByName(String userName) {
        Criteria criteria = getExecutableCriteria();
        criteria.add(Restrictions.eq(User_.loginName.getName(), userName));
        List<User> users = criteria.list();
        if (users.size() > 0) {
            return users.get(0);
        } else {
            return null;
        }
    }

    @Override
    public List<User> getUsersNotIn(List<String> userIds, ODataObj oDataObj) {
        Criteria crit = getExecutableCriteria();
        userIds.forEach(x -> {
            crit.add(Restrictions.ne(User_.id.getName(), x));
        });

        List<User> list = oDataObj.buildQuery(crit).list();
        return list;
    }

    @Override
    public Set<String> getUserPermission(String userName) {
        User user = findUserByName(userName);
        if (user == null || user.getRoles() == null) {
            return Collections.EMPTY_SET;
        }
        Set<String> permissions = new HashSet<>();
        //如果超级管理员，则默认给所有权限，开发阶段暂时这么使用
        user.getRoles().forEach(x -> {
            if (Constant.SUPER_ROLE.equals(x.getRoleName())) {
                permissions.clear();
                permissions.add("*");
                return;
            } else {
                x.getResources().forEach(y -> {
                    permissions.add(y.getPath());
                });
            }
        });
        return permissions;
    }

    @Override
    public Set<String> getUserRoles(String userName) {
        User user = findUserByName(userName);
        if (user == null || user.getRoles() == null) {
            return Collections.EMPTY_SET;
        }
        Set<String> roles = new HashSet<String>();
        //如果超级管理员，则默认给所有权限，开发阶段暂时这么使用
        user.getRoles().forEach(x -> {
            roles.add(x.getRoleName());
        });
        return roles;
    }

    @Override
    public Set<String> getUserRole(String userId) {
        User user = findById(userId);
        if (user == null || user.getRoles() == null) {
            return Collections.EMPTY_SET;
        }
        Set<String> roles = new HashSet<String>();
        //如果超级管理员，则默认给所有权限，开发阶段暂时这么使用
        user.getRoles().forEach(x -> {
            roles.add(x.getRoleName());
        });
        return roles;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<User> findUserByRoleName(String roleName) {
        Criteria criteria = getExecutableCriteria();
        List<User> list = criteria.createAlias(User_.roles.getName(), User_.roles.getName())
                .add(Restrictions.eq(User_.roles.getName() + "." + Role_.roleName.getName(), roleName)).list();

        return list;
    }

    /**
     * 根据部门ID查询在职的人员
     *
     * @param orgId
     * @return
     */
    @Override
    @SuppressWarnings("unchecked")
    public List<User> findUserByOrgId(String orgId) {
        Criteria criteria = getExecutableCriteria();
        criteria.add(Restrictions.eq(User_.jobState.getName(), "t"));
        List<User> list = criteria.createAlias(User_.org.getName(), User_.org.getName())
                .add(Restrictions.eq(User_.org.getName() + "." + Org_.id.getName(), orgId)).list();
        return list;
    }

    /**
     * 根据用户ID查询部门领导
     *
     * @param userId
     * @return
     */
    @Override
    public User findOrgDirector(String userId) {
        HqlBuilder sqlBuilder = HqlBuilder.create();
        sqlBuilder.append("select u.* from cs_user u where u." + User_.id.getName() + " = ");
        sqlBuilder.append(" (select o." + Org_.orgDirector.getName() + " from cs_org o ");
        sqlBuilder.append(" where o." + Org_.id.getName() + " = ");
        sqlBuilder.append(" (select cu.orgid from cs_user cu where cu." + User_.id.getName() + " =:userId ))");
        sqlBuilder.setParam("userId", userId);
        List<User> list = findBySql(sqlBuilder);
        if (Validate.isList(list)) {
            return list.get(0);
        }
        return null;
    }

    /**
     * 根据用户ID查询分管领导
     *
     * @param userId
     * @return
     */
    @Override
    public User findOrgSLeader(String userId) {
        HqlBuilder sqlBuilder = HqlBuilder.create();
        sqlBuilder.append("select u.* from cs_user u where u." + User_.id.getName() + " = ");
        sqlBuilder.append(" (select o." + Org_.orgSLeader.getName() + " from cs_org o ");
        sqlBuilder.append(" where o." + Org_.id.getName() + " = ");
        sqlBuilder.append(" (select cu.orgid from cs_user cu where cu." + User_.id.getName() + " =:userId ))");
        sqlBuilder.setParam("userId", userId);
        List<User> list = findBySql(sqlBuilder);
        if (Validate.isList(list)) {
            return list.get(0);
        }
        return null;
    }

    /**
     * 验证待办人是否是当前分办部门的人员
     *
     * @param orgId
     * @param userIdList
     * @return
     */
    @Override
    public boolean checkIsSignOrgDeptUser(String signId,String orgId, String userIdList) {
        boolean result = false;
        List<String> userIds = StringUtil.getSplit(userIdList, ",");
        for (String userId : userIds) {
            result = checkUser(signId,orgId,userId);
            if (result) {
                break;
            }
        }
        return result;
    }

    /**
     * 验证用户是否是部长下的管理人员
     * @param orgType
     * @param orgId
     * @param mainUserId
     * @return
     */
    @Override
    public boolean checkIsMainSigUser(String orgType, String orgId, String mainUserId) {
        //判断是部门还是组
        boolean isOrg = "org".equals(orgType.toLowerCase());
        HqlBuilder sqlBuilder = HqlBuilder.create();
        if(isOrg){
            sqlBuilder.append("SELECT COUNT (UF.ID) FROM CS_USER UF WHERE UF.ID=:userId AND UF.ORGID =:orgId ");
        }else{
            sqlBuilder.append("SELECT COUNT (UF.SYSDEPTLIST_ID) FROM CS_DEPT_CS_USER UF WHERE UF.USERLIST_ID = :userId AND UF.SYSDEPTLIST_ID =:orgId ");
        }
        sqlBuilder.setParam("userId",mainUserId).setParam("orgId",orgId);
        int result = returnIntBySql(sqlBuilder);
        return (result > 0);
    }

    /**
     * 查询在职的部门用户
     * @return
     */
    @Override
    public List<UserDto> findUserAndOrg() {
        HqlBuilder hqlBuilder = HqlBuilder.create();
        hqlBuilder.append("select * from cs_user where " + User_.jobState.getName() + " = 't'  and (orgid is not null or orgid <> '') order by orgid desc");
        List<User> userList = this.findBySql(hqlBuilder);
        List<UserDto> userDtoList = new ArrayList<>();
        if(userList != null && userList.size() >0 ){
            for(User user : userList){
                UserDto userDto = new UserDto();
                BeanCopierUtils.copyPropertiesIgnoreNull(user , userDto);
                if(user.getOrg()!= null){
                    Org org = user.getOrg();
                    OrgDto orgDto = new OrgDto();
                    BeanCopierUtils.copyPropertiesIgnoreNull(org , orgDto);
                    userDto.setOrgDto(orgDto);
                }
                userDtoList.add(userDto);
            }
        }
        return userDtoList;
    }

    private boolean checkUser(String signId,String orgId,String userId){
        OrgDept orgDept = orgDeptRepo.findOrgDeptById(orgId);
        //判断是部门还是组
        boolean isOrg = "org".equals(orgDept.getType().toLowerCase());
        HqlBuilder sqlBuilder = HqlBuilder.create();
        if(isOrg){
            sqlBuilder.append(" SELECT COUNT (UF.ID) FROM CS_USER UF, ");
        }else{
            sqlBuilder.append(" SELECT COUNT (UF.SYSDEPTLIST_ID) FROM CS_DEPT_CS_USER UF, ");
        }
        sqlBuilder.append(" (SELECT CS.ORGID, CS.SIGNID, CS.BRANCHID FROM CS_SIGN_BRANCH cs ");
        sqlBuilder.append(" WHERE CS.SIGNID = :rosignid AND CS.ORGID = :roorgid) RO, ");
        sqlBuilder.setParam("rosignid",signId).setParam("roorgid",orgId);
        sqlBuilder.append(" (SELECT CP.SIGNID, CP.USERID, CP.FLOWBRANCH FROM CS_SIGN_PRINCIPAL2 cp ");
        sqlBuilder.append(" WHERE CP.SIGNID = :rusignid AND CP.USERID = :ruuserid) RU ");
        sqlBuilder.setParam("rusignid",signId).setParam("ruuserid",userId);
        if(isOrg){
            sqlBuilder.append("  WHERE  UF.ID = RU.USERID AND RO.ORGID = UF.ORGID AND RO.BRANCHID = RU.FLOWBRANCH AND RO.SIGNID = RU.SIGNID ");
        }else{
            sqlBuilder.append(" WHERE UF.SYSDEPTLIST_ID = RO.ORGID AND UF.USERLIST_ID = RU.USERID AND RO.BRANCHID = RU.FLOWBRANCH AND RO.SIGNID = RU.SIGNID ");
        }
        int result = returnIntBySql(sqlBuilder);
        return (result > 0);
    }
    /**
     * 从缓存获取用户信息（主要用户流程处理，用到用户ID和用户登录名等信息）
     *
     * @param userId
     * @return
     */
    @Override
    public User getCacheUserById(String userId) {
        User cacheUser = null;
        ICache cache = CacheManager.getCache();
        List<User> userList = (List<User>) cache.get(CacheConstant.USER_CACHE);
        //查询所有在职用户
        if (!Validate.isList(userList)) {
            userList = findAllPostUser();
            cache.put(CacheConstant.USER_CACHE, userList);
        }
        for (User user : userList) {
            if (user.getId().equals(userId)) {
                cacheUser = user;
                break;
            }
        }
        if (cacheUser == null) {
            cacheUser = findById(User_.id.getName(), userId);
            userList.add(cacheUser);
            cache.put(CacheConstant.USER_CACHE, userList);
        }
        return cacheUser;
    }

    /**
     * 查询所有在职员工
     *
     * @return
     */
    @Override
    public List<User> findAllPostUser() {
        ICache cache = CacheManager.getCache();
        List<User> resultList = (List<User>) cache.get(CacheConstant.USER_CACHE);
        if (!Validate.isList(resultList)) {
            HqlBuilder hqlBuilder = HqlBuilder.create();
            hqlBuilder.append(" from " + User.class.getSimpleName() + " where " + User_.jobState.getName() + " = :jobState ");
            hqlBuilder.setParam("jobState", "t");
            resultList = findByHql(hqlBuilder);
            cache.put(CacheConstant.USER_CACHE, resultList);
        }
        return resultList;
    }

    /**
     * 刷新所有在职用户细腻系
     */
    @Override
    public void fleshPostUserCache() {
        ICache cache = CacheManager.getCache();
        HqlBuilder hqlBuilder = HqlBuilder.create();
        hqlBuilder.append(" from " + User.class.getSimpleName() + " where " + User_.jobState.getName() + " = :jobState ");
        hqlBuilder.setParam("jobState", "t");
        List<User> resultList = findByHql(hqlBuilder);
        cache.put(CacheConstant.USER_CACHE, resultList);
    }

    /**
     * 根据多个ID获取用户列表信息
     *
     * @param userIds
     * @return
     */
    @Override
    public List<User> getCacheUserListById(String userIds) {
        List<User> userList = new ArrayList<>();
        List<String> ids = StringUtil.getSplit(userIds, ",");
        for (String id : ids) {
            userList.add(getCacheUserById(id));
        }
        return userList;
    }

}
