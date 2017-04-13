package cs.repository.repositoryImpl;

import cs.domain.Role;
import cs.repository.IRepository;

public interface RoleRepo extends IRepository<Role, String>{
	boolean isRoleExist(String roleName);
}
