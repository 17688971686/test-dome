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

    /**
     * 验证待办人是否是当前分办部门的人员
     * @param orgId
     * @param userIdList
     * @return
     */
    boolean checkIsSignOrgDeptUser(String signId,String orgId, String userIdList);

    /**
     * 验证用户是否是部长下的管理人员
     * @param orgType
     * @param orgId
     * @param mainUserId
     * @return
     */
    boolean checkIsMainSigUser(String orgType, String orgId, String mainUserId);

    /**
     * 查询在职的部门用户
     * @return
     */
    List<UserDto> findUserAndOrg();

    /**
     * 检验用户是否已经转为代办
     * @param takeUserId
     * @return
     */
    boolean checkTakeExist(String takeUserId);

    /**
     * 判断设定的用户是否为请假人（已经设定代办人的人）
     * @param takeUserId
     * @return
     */
    boolean checkUserSetTask(String takeUserId);
}
