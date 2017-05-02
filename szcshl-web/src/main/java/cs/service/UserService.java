package cs.service;

import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import cs.common.Response;
import cs.domain.User;
import cs.model.OrgDto;
import cs.model.PageModelDto;
import cs.model.UserDto;
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
	
	User findUserByName(String userName);

	List<User> getUser();

	List<OrgDto> getOrg(ODataObj odataObj );
	
	List<User> findUserByRoleName(String roleName);

	List<User> findUserByDeptId(String deptId);
}