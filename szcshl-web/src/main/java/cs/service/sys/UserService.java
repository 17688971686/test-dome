package cs.service.sys;

import cs.common.ResultMsg;
import cs.domain.project.AgentTask;
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


    List<UserDto> getSMSManyUser();

    /**
     * 保存代办人
     * @param takeUserId
     */
    ResultMsg saveTakeUser(String takeUserId);

    /*UserDto getTakeUserByLoginName();*/

    /**
     * 取消代办
     */
    void cancelTakeUser();

    User getCacheUserById(String userId);

    List<User> getCacheUserListById(String userIds);

    List<User> findAllPostUser();

    List<User> findAllPostUserByCriteria();

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

    /**
     * 查询在职的部门用户
     * @return
     */
    List<UserDto> findUserAndOrg();

    /**
     * 用户用户所有的待办任务（待办项目和待办任务）
     * @param userId
     * @return
     */
    Map<String,Object> findAllTaskList(String userId);

    /**
     * 获取任务环节处理人信息
     * @param userId        原用户ID
     * @param agentTaskList 代办人列表，不能为空
     */
    String getTaskDealId(String userId,List<AgentTask> agentTaskList,String nodeKey);

    /**
     * 获取任务环节处理人信息
     * @param user          用户对象
     * @param agentTaskList 代办人列表，不能为空
     */
    String getTaskDealId(User user,List<AgentTask> agentTaskList,String nodeKey);

    /**
     * 获取用户等级
     * @param u
     * @return
     */
    String getUserLevel(User u);

    /**
     * 用户验证
     * @param u
     * @return
     */
    Map<String, Object>  getUserAuthForApp(User u);


    /**
     * 密码加密
     */
    void encodePwd();

}