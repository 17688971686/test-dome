package cs.service.sys;

import cs.common.ResultMsg;
import cs.domain.sys.Org;
import cs.domain.sys.User;
import cs.model.PageModelDto;
import cs.model.sys.OrgDto;
import cs.model.sys.UserDto;
import cs.repository.odata.ODataObj;

import java.util.List;
import java.util.Set;

public interface UserService {

    PageModelDto<UserDto> get(ODataObj odataObj);

    ResultMsg createUser(UserDto userDto);

    void deleteUser(String id);

    ResultMsg updateUser(UserDto userDto);

    Set<String> findPermissions(String username);

    Set<String> findRoles(String username);

    void changePwd(String password);

    UserDto findUserByName(String userName);

    List<UserDto> getUser();

    List<OrgDto> getOrg(ODataObj odataObj);

    List<UserDto> findUserByRoleName(String roleName);

    List<UserDto> findUserByOrgId(String orgId);

    UserDto findById(String id,boolean inclueOrg);

    UserDto filterOrgSLeader(List<UserDto> userList, Org org);

    UserDto getOrgDirector(String userId);

    UserDto getOrgSLeader(String userId);

    /**
     * 查询所有在职用户
     * @return
     */
    List<UserDto> findAllusers();

	int findMaxUserNo();

	List<UserDto> findByOrgUserName(String orgId);

    void saveUser(User user);

    User findByName(String userName);

    List<UserDto> getAllUserDisplayName();

    void saveTakeUser(String takeUserId);

    UserDto getTakeUserByLoginName();

    void cancelTakeUser();

    User getCacheUserById(String userId);

    List<User> getCacheUserListById(String userIds);

    List<User> findAllPostUser();

    void fleshPostUserCache();

    /**
     * 重置密码
     * @param ids
     */
    void resetPwd(String ids);
}