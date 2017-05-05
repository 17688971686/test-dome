package cs.service.sys;

import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import cs.common.Response;
import cs.domain.sys.User;
import cs.model.PageModelDto;
import cs.model.sys.OrgDto;
import cs.model.sys.UserDto;
import cs.repository.odata.ODataObj;

public interface UserService {

	PageModelDto<UserDto> get(ODataObj odataObj);

	void createUser(UserDto userDto);

	void deleteUser(String id);

	void deleteUsers(String[] ids);
	
	void updateUser(UserDto userDto);
	
	Response Login(String userName, String password, HttpServletRequest request);
	
	Set<String> getCurrentUserPermissions();
	
	void logout();
	
	void changePwd(String password);
	
	UserDto findUserByName(String userName);

	List<UserDto> getUser();

	List<OrgDto> getOrg(ODataObj odataObj );
	
	List<UserDto> findUserByRoleName(String roleName);

	List<UserDto> findUserByDeptId(String deptId);

	UserDto findById(String id);
}