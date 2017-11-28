package cs.repository.repositoryImpl.sys;

import java.util.List;
import java.util.Set;

import cs.domain.sys.User;
import cs.model.sys.UserDto;
import cs.repository.IRepository;
import cs.repository.odata.ODataObj;

public interface UserRepo extends IRepository<User, String> {
    User findUserByName(String userName);

    List<User> getUsersNotIn(List<String> userIds, ODataObj oDataObj);

    Set<String> getUserPermission(String userName);

    Set<String> getUserRoles(String userName);

    Set<String> getUserRole(String userId);

    List<User> findUserByRoleName(String roleName);

    List<User> findUserByOrgId(String orgId);

    User getCacheUserById(String userId);

    List<User> getCacheUserListById(String userIds);

    List<User> findAllPostUser();

    void fleshPostUserCache();

    User findOrgDirector(String userId);

    User findOrgSLeader(String userId);
}
