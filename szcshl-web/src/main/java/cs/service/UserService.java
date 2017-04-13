package cs.service;

import java.util.Set;

import cs.common.Response;
import cs.domain.User;
import cs.model.PageModelDto;
import cs.model.UserDto;
import cs.repository.odata.ODataObj;

public interface UserService {

	PageModelDto<UserDto> get(ODataObj odataObj);

	void createUser(UserDto userDto);

	void deleteUser(String id);

	void deleteUsers(String[] ids);
	
	void updateUser(UserDto userDto);
	
	Response Login(String userName, String password);
	Set<String> getCurrentUserPermissions();
	void logout();
	void changePwd(String password);
	
	User findUserByName(String userName);

}