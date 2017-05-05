package cs.repository.repositoryImpl.sys;

import cs.domain.sys.Role;
import cs.repository.IRepository;

public interface RoleRepo extends IRepository<Role, String>{
	boolean isRoleExist(String roleName);
}
