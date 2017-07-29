package cs.repository.repositoryImpl.sys;

import java.util.List;
import java.util.Set;

import cs.domain.sys.User;
import cs.repository.IRepository;
import cs.repository.odata.ODataObj;

public interface UserRepo extends IRepository<User, String> {
	User findUserByName(String userName);
	List<User> getUsersNotIn(List<String> userIds, ODataObj oDataObj);
	Set<String> getUserPermission(String userName);
	Set<String> getUserRoles(String userName);
	List<User> findUserByRoleName(String roleName);
	List<User> findUserByOrgId(String orgId);
}
