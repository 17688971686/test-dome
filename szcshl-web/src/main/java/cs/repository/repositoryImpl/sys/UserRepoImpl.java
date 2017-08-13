package cs.repository.repositoryImpl.sys;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import cs.common.HqlBuilder;
import cs.common.utils.SessionUtil;
import cs.domain.sys.*;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import cs.repository.AbstractRepository;
import cs.repository.odata.ODataObj;

@Repository
public class UserRepoImpl extends AbstractRepository<User, String> implements UserRepo {
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
        if(user == null || user.getRoles() == null) {
            return Collections.EMPTY_SET;
        }
        Set<String> permissions = new HashSet<>();
        //如果超级管理员，则默认给所有权限，开发阶段暂时这么使用
        user.getRoles().forEach(x -> {
            if ("超级管理员".equals(x.getRoleName())) {
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
        if(user == null || user.getRoles() == null) {
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
     * @param orgId
     * @return
     */
    @Override
    @SuppressWarnings("unchecked")
    public List<User> findUserByOrgId(String orgId) {
        Criteria criteria = getExecutableCriteria();
        criteria.add(Restrictions.eq(User_.jobState.getName(),"t"));
        List<User> list = criteria.createAlias(User_.org.getName(), User_.org.getName())
                .add(Restrictions.eq(User_.org.getName() + "." + Org_.id.getName(), orgId)).list();
        return list;
    }

}
