package cs.repository.repositoryImpl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import cs.domain.Role;
import cs.domain.Role_;
import cs.repository.AbstractRepository;

@Repository
public class RoleRepoImpl extends AbstractRepository<Role, String> implements RoleRepo {

	@Override
	public boolean isRoleExist(String roleName) {
		Criteria criteria=this.getSession().createCriteria(Role.class); 
		criteria.add(Restrictions.eq(Role_.roleName.getName(), roleName) );
		List<Role> roles=criteria.list();
		return !roles.isEmpty();
	}

}
