package cs.service.sys;

import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import cs.common.Response;
import cs.domain.sys.Org;
import cs.model.PageModelDto;
import cs.model.sys.OrgDto;
import cs.model.sys.UserDto;
import cs.repository.odata.ODataObj;

public interface UserService {

    PageModelDto<UserDto> get(ODataObj odataObj);

    void createUser(UserDto userDto);

    void deleteUser(String id);

    void updateUser(UserDto userDto);

    Response Login(String userName, String password, HttpServletRequest request);

    Set<String> getCurrentUserPermissions();

    void logout();

    void changePwd(String password);

    UserDto findUserByName(String userName);

    List<UserDto> getUser();

    List<OrgDto> getOrg(ODataObj odataObj);

    List<UserDto> findUserByRoleName(String roleName);

    List<UserDto> findUserByOrgId(String orgId);

    UserDto findById(String id,boolean inclueOrg);

    UserDto filterOrgDirector(List<UserDto> userList, Org org);

    UserDto filterOrgSLeader(List<UserDto> userList, Org org);

    UserDto getOrgDirector();

    UserDto getOrgSLeader();

    List<UserDto> findAllusers();
    
    int findMaxUserNo();
}