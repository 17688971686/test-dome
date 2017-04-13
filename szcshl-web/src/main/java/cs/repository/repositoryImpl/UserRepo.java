package cs.repository.repositoryImpl;

import java.util.List;
import java.util.Set;

import cs.domain.User;
import cs.repository.IRepository;
import cs.repository.odata.ODataObj;

public interface UserRepo extends IRepository<User, String> {
	User findUserByName(String userName);
	List<User> getUsersNotIn(List<String> userIds, ODataObj oDataObj);
	Set<String> getUserPermission(String userName);
}
