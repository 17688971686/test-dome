package cs.repository.repositoryImpl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import cs.domain.User;
import cs.domain.User_;
import cs.repository.AbstractRepository;
import cs.repository.odata.ODataObj;

@Repository
public class UserRepoImpl extends AbstractRepository<User, String> implements UserRepo {
	@Override
	public User findUserByName(String userName) {
		Criteria criteria = this.getSession().createCriteria(User.class);
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
		Criteria crit = this.getSession().createCriteria(User.class);
		userIds.forEach(x -> {
			crit.add(Restrictions.ne(User_.id.getName(), x));
		});

		List<User> list = oDataObj.buildQuery(crit).list();
		return list;
	}
 
	@Override
	public Set<String> getUserPermission(String userName) {
		Criteria crit = this.getSession().createCriteria(User.class);
		crit.add(Restrictions.eq(User_.loginName.getName(), userName));
		List<User> list = crit.list();		
		Set<String> permissions = new HashSet<>();
		if (list.size()>0) {
			User user = list.get(0);
			//如果超级管理员，则默认给所有权限，开发阶段暂时这么使用
			user.getRoles().forEach(x -> {	
				if("超级管理员".equals(x.getRoleName())){
					permissions.clear();
					permissions.add("*");
					return ;
				}else{
					x.getResources().forEach(y -> {
						permissions.add(y.getPath());
					});
				}
			});
		}
		return permissions;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<User> findUserByRoleName(String roleName) {
		Criteria criteria = getExecutableCriteria();		 
		List<User> list = criteria.createAlias("roles","roles")
        .add(Restrictions.eq("roles.roleName",roleName)).list();
		 
		 return list;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<User> findUserByDeptId(String deptId) {
		Criteria criteria = getExecutableCriteria();		 
		List<User> list = criteria.createAlias("org","org")
        .add(Restrictions.eq("org.id",deptId)).list();
		
		return list;
	}
}
