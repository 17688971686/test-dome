package cs.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import cs.common.ICurrentUser;
import cs.common.Response;
import cs.domain.Role;
import cs.domain.User;
import cs.model.PageModelDto;
import cs.model.RoleDto;
import cs.model.UserDto;
import cs.repository.odata.ODataObj;
import cs.repository.repositoryImpl.RoleRepo;
import cs.repository.repositoryImpl.UserRepo;

@Service
public class UserServiceImpl implements UserService {
	private static Logger logger = Logger.getLogger(UserServiceImpl.class);
	@Autowired
	private UserRepo userRepo;
	@Autowired
	private RoleRepo roleRepo;
	@Autowired
	private ICurrentUser currentUser;

	/*
	 * (non-Javadoc)
	 * 
	 * @see cs.service.UserService#get(cs.repository.odata.ODataObj)
	 */
	@Override
	@Transactional
	public PageModelDto<UserDto> get(ODataObj odataObj) {
		
		List<User> listUser = userRepo.findByOdata(odataObj);
		List<UserDto> userDtoList = new ArrayList<>();
		for (User item : listUser) {
			UserDto userDto = new UserDto();
			userDto.setId(item.getId());
			userDto.setLoginName(item.getLoginName());
			userDto.setDisplayName(item.getDisplayName());
			userDto.setPassword(item.getPassword());
			userDto.setRemark(item.getRemark());
			userDto.setCreatedDate(item.getCreatedDate());

			// 查询相关角色
			List<RoleDto> roleDtoList = new ArrayList<>();
			for (Role role : item.getRoles()) {
				RoleDto roleDto = new RoleDto();
				roleDto.setRemark(role.getRemark());
				roleDto.setRoleName(role.getRoleName());
				roleDto.setCreatedDate(role.getCreatedDate());
				roleDto.setId(role.getId());

				roleDtoList.add(roleDto);
			}
			userDto.setRoles(roleDtoList);

			userDtoList.add(userDto);
		}
		PageModelDto<UserDto> pageModelDto = new PageModelDto<>();
		pageModelDto.setCount(odataObj.getCount());
		pageModelDto.setValue(userDtoList);

		logger.info("查询用户数据");
		return pageModelDto;
	}

	@Override
	@Transactional
	public void createUser(UserDto userDto) {
		User findUser=userRepo.findUserByName(userDto.getLoginName());
		if (findUser==null) {// 用户不存在
			User user = new User();
			user.setRemark(userDto.getRemark());
			user.setLoginName(userDto.getLoginName());
			user.setDisplayName(userDto.getDisplayName());
			user.setId(UUID.randomUUID().toString());
			user.setCreatedBy(currentUser.getLoginName());
			user.setPassword(userDto.getPassword());
			user.setModifiedBy(currentUser.getLoginName());
			// 加入角色
			for (RoleDto roleDto : userDto.getRoles()) {
				Role role = roleRepo.findById(roleDto.getId());
				if (role != null) {
					user.getRoles().add(role);
				}

			}

			userRepo.save(user);
			logger.info(String.format("创建用户,登录名:%s", userDto.getLoginName()));
		} else {
			throw new IllegalArgumentException(String.format("用户：%s 已经存在,请重新输入！", userDto.getLoginName()));
		}

	}

	@Override
	@Transactional
	public void deleteUser(String id) {
		User user = userRepo.findById(id);
		if (user != null) {
			if(!user.getLoginName().equals("admin")){
				userRepo.delete(user);
				logger.info(String.format("删除用户,用户名:%s", user.getLoginName()));
			}
			
		}
	}

	@Override
	@Transactional
	public void deleteUsers(String[] ids) {
		for (String id : ids) {
			this.deleteUser(id);
		}
		logger.info("批量删除用户");
	}

	@Override
	@Transactional
	public void updateUser(UserDto userDto) {
		User user = userRepo.findById(userDto.getId());
		user.setRemark(userDto.getRemark());
		user.setDisplayName(userDto.getDisplayName());
		user.setModifiedBy(currentUser.getLoginName());

		// 清除已有role
		user.getRoles().clear();

		// 加入角色
		for (RoleDto roleDto : userDto.getRoles()) {
			Role role = roleRepo.findById(roleDto.getId());
			if (role != null) {
				user.getRoles().add(role);
			}
		}

		userRepo.save(user);
		logger.info(String.format("更新用户,用户名:%s", userDto.getLoginName()));
	}
	@Override
	@Transactional
	public Response Login(String userName, String password){
		User user=userRepo.findUserByName(userName);
		Response response =new Response();
		if(user!=null){
			if(user.getLoginFailCount()>5&&user.getLastLoginDate().getDay()==(new Date()).getDay()){	
				response.setMessage("登录失败次数过多,请明天再试!");
				logger.warn(String.format("登录失败次数过多,用户名:%s", userName));
			}
			else if(password!=null&&password.equals(user.getPassword())){
				currentUser.setLoginName(user.getLoginName());
				currentUser.setDisplayName(user.getDisplayName());
				user.setLoginFailCount(0);
				user.setLastLoginDate(new Date());
				//shiro
				UsernamePasswordToken token = new UsernamePasswordToken(user.getLoginName(), user.getPassword());
				Subject currentUser = SecurityUtils.getSubject();
				currentUser.login(token);
				
				response.setIsSuccess(true);
				logger.info(String.format("登录成功,用户名:%s", userName));
			}else{
				user.setLoginFailCount(user.getLoginFailCount()+1);	
				user.setLastLoginDate(new Date());
				response.setMessage("用户名或密码错误!");
			}
			userRepo.save(user);
		}else{
			response.setMessage("用户名或密码错误!");
		}
		
		return response;
	}
	@Override
	@Transactional
	public Set<String> getCurrentUserPermissions(){
		//logger.info(String.format("查询当前用户权限,用户名:%s", currentUser.getLoginName()));
		return  userRepo.getUserPermission(currentUser.getLoginName());
		
	}
	@Transactional
	public void logout(){
		Subject currentUser = SecurityUtils.getSubject();
		if(currentUser!=null){
			currentUser.logout();
			logger.info(String.format("退出登录,用户名:%s", currentUser.getPrincipal()));
		}
		
	}
	@Transactional
	public void changePwd(String password){
		String userName=currentUser.getLoginName();
		User user=userRepo.findUserByName(userName);
		
		if(user!=null){
			user.setPassword(password);
			userRepo.save(user);
			logger.info(String.format("修改密码,用户名:%s", userName));
		}
	}
	@Transactional
	public User findUserByName(String userName){
		return userRepo.findUserByName(userName);
	}
}
