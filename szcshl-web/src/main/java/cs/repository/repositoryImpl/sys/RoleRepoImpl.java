package cs.repository.repositoryImpl.sys;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import cs.domain.sys.Role;
import cs.domain.sys.Role_;
import cs.repository.AbstractRepository;

@Repository
public class RoleRepoImpl extends AbstractRepository<Role, String> implements RoleRepo {

    @Override
    public boolean isRoleExist(String roleName) {
        Criteria criteria = getExecutableCriteria();
        criteria.add(Restrictions.eq(Role_.roleName.getName(), roleName));
        List<Role> roles = criteria.list();
        return !roles.isEmpty();
    }

    @Override
    public Role findById(String id) {
        return super.findById(id);
    }


}
