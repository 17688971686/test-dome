package cs.service.sys;

import cs.common.ResultMsg;
import cs.domain.sys.Org;
import cs.domain.sys.User;
import cs.model.PageModelDto;
import cs.model.sys.OrgDto;
import cs.model.sys.UserDto;
import cs.repository.odata.ODataObj;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface UserService {

    PageModelDto<UserDto> get(ODataObj odataObj);

    ResultMsg createUser(UserDto userDto);

    /**
     * 根据ID删除用户信息
     * @param id
     * @return
     */
    ResultMsg deleteUser(String id);

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

    /**
     * 保存代办人
     * @param takeUserId
     */
    void saveTakeUser(String takeUserId);

    /*UserDto getTakeUserByLoginName();*/

    /**
     * 取消代办
     */
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

    /**
     * 移动端的登录
     */
    ResultMsg Login(HttpServletRequest request, HttpServletResponse response, String userName, String password);

    /**
     * 获取用户查看待办任务
     * @return
     */
    Map<String,Object> getUserSignAuth();

    /**
     * 验证用户是否是部长下的管理人员
     * @param orgType
     * @param orgId
     * @param mainUserId
     * @return
     */
    boolean checkIsMainSigUser(String orgType, String orgId, String mainUserId);

    /***
     * 获取当前用户级别
     * 0表示普通用户，1表示主任，2表示分管领导，3表示部长 4.表示组长）
     * @return
     */
    Map<String, Object>  getUserLevel();
}