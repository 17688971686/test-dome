package cs.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.activiti.engine.IdentityService;
import org.activiti.engine.identity.Group;
import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.subject.Subject;
import org.hibernate.Criteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cs.common.ICurrentUser;
import cs.common.Response;
import cs.common.utils.BeanCopierUtils;
import cs.domain.Org;
import cs.domain.Role;
import cs.domain.User;
import cs.model.OrgDto;
import cs.model.PageModelDto;
import cs.model.RoleDto;
import cs.model.UserDto;
import cs.repository.odata.ODataObj;
import cs.repository.repositoryImpl.OrgRepo;
import cs.repository.repositoryImpl.RoleRepo;
import cs.repository.repositoryImpl.UserRepo;
import cs.utils.Cryptography;

@Service
public class UserServiceImpl implements UserService {
	private static Logger logger = Logger.getLogger(UserServiceImpl.class);
	@Autowired
	private UserRepo userRepo;
	@Autowired
	private RoleRepo roleRepo;
	@Autowired
	private ICurrentUser currentUser;

	@Autowired
	private OrgRepo orgRepo;
	@Autowired
	private IdentityService identityService;
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
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
			
			userDto.setUserSex(item.getUserSex());		
			userDto.setUserPhone(item.getUserPhone());
			userDto.setUserMPhone(item.getUserMPhone());
			userDto.setEmail(item.getEmail());
			userDto.setJobState(item.getJobState());
			userDto.setUseState(item.getUseState());
			userDto.setPwdState(item.getPwdState());
			userDto.setUserOrder(item.getUserOrder());
			userDto.setLastLogin(item.getLastLogin());
			userDto.setUserIP(item.getUserIP());
			userDto.setLastLoginDate(item.getLastLoginDate());
	            
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
			
			OrgDto orgDto = new OrgDto();
            if(item.getOrg() !=null){            	
            	orgDto.setId(item.getOrg().getId());
            	orgDto.setName(item.getOrg().getName());            	
            }
            userDto.setOrgDto(orgDto);
			
			
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
			//MD5加密密码
			String salt1 = new SecureRandomNumberGenerator().nextBytes().toHex();
			String salt2 = Cryptography.md5(salt1, userDto.getLoginName());
			String password = Cryptography.md5(userDto.getPassword(), userDto.getPassword()+salt2,2);
			user.setUserSalt(salt1);
			user.setPassword(userDto.getPassword());
			
			
			user.setModifiedBy(currentUser.getLoginName());
			user.setUserPhone(userDto.getUserPhone());
			user.setUserMPhone(userDto.getUserMPhone());
			user.setEmail(userDto.getEmail());
			user.setJobState(userDto.getJobState());
			user.setUseState(userDto.getUseState());
			user.setPwdState(userDto.getPwdState());
			user.setUserOrder(userDto.getUserOrder());
			
			List<String> orgs = new ArrayList<String>();
			List<String> roleNames = new ArrayList<String>();
			
			// 加入角色
			for (RoleDto roleDto : userDto.getRoles()) {
				Role role = roleRepo.findById(roleDto.getId());
				if (role != null) {
					user.getRoles().add(role);
					roleNames.add(role.getRoleName());
					//System.out.println(role.getRoleName());
				}

			}
			//添加部门
			OrgDto oDto =  userDto.getOrgDto();
			Org o = orgRepo.findById(oDto.getId());
			//o.setId(o.getId());
			user.setOrg(o);
			userRepo.save(user);
			//System.out.println(userDto.getId()+userDto.getLoginName()+userDto.getPassword());
			createActivitiUser(user.getId(), user.getLoginName(), user.getPassword(), roleNames);
			logger.info(String.format("创建用户,登录名:%s", userDto.getLoginName()));
		} else {
			throw new IllegalArgumentException(String.format("用户：%s 已经存在,请重新输入！", userDto.getLoginName()));
		}

	}
	@Override
	@Transactional
	public List<OrgDto> getOrg(ODataObj odataObj ) {
		List<Org> org= orgRepo.findByOdata(odataObj);
		List<OrgDto> orgDto = new ArrayList<>();
		for(Org item : org){
			
			OrgDto orgDtos = new OrgDto();
			orgDtos.setId(item.getId());
			orgDtos.setName(item.getName());
			orgDtos.setRemark(item.getRemark());
			
			orgDtos.setOrgPhone(item.getOrgPhone());
			orgDtos.setOrgFax(item.getOrgFax());
			orgDtos.setOrgAddress(item.getOrgAddress());
			orgDtos.setOrgFunction(item.getOrgFunction());
			
			orgDtos.setOrgDirector(item.getOrgDirector());//科长
			orgDtos.setOrgAssistant(item.getOrgAssistant());//副科长
			orgDtos.setOrgCompany(item.getOrgCompany());//单位名称
			orgDtos.setCreatedBy(currentUser.getLoginName());
			orgDtos.setOrgIdentity(item.getOrgIdentity());
			orgDtos.setModifiedBy(currentUser.getLoginName());
			
			orgDto.add(orgDtos);
		}
		return orgDto;
	}

	@Override
	@Transactional
	public void deleteUser(String id) {
		User user = userRepo.findById(id);
		if (user != null) {
			if(!user.getLoginName().equals("admin")){
				userRepo.delete(user);
				this.deleteActivitiUser(user.getId());
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
		
		user.setUserSex(userDto.getUserSex());		
		user.setUserPhone(userDto.getUserPhone());
		user.setUserMPhone(userDto.getUserMPhone());
		user.setEmail(userDto.getEmail());
		user.setJobState(userDto.getJobState());
		user.setUseState(userDto.getUseState());
		user.setPwdState(userDto.getPwdState());
		user.setUserOrder(userDto.getUserOrder());
		
		OrgDto oDto =  userDto.getOrgDto();
		Org o = orgRepo.findById(oDto.getId());
		user.setOrg(o);
		
		// 清除已有role
		user.getRoles().clear();
			List<String> roleNames=new ArrayList<String>();
		// 加入角色
		for (RoleDto roleDto : userDto.getRoles()) {
			Role role = roleRepo.findById(roleDto.getId());
			roleNames.add(roleDto.getRoleName());
			if (role != null) {
				user.getRoles().add(role);
			}
		}
		userRepo.save(user);
		this.updateActivitiUser(user.getId(), user.getLoginName(), user.getPassword(), roleNames);
		logger.info(String.format("更新用户,用户名:%s", userDto.getLoginName()));
	}
	@Override
	@Transactional
	public Response Login(String userName, String password,HttpServletRequest request){
		User user=userRepo.findUserByName(userName);
		Response response =new Response();
		 
		if(user!=null){
//			if(user.getLoginFailCount()>5&&user.getLastLoginDate().getDay()==(new Date()).getDay()){	
//				response.setMessage("登录失败次数过多,请明天再试!");
//				logger.warn(String.format("登录失败次数过多,用户名:%s", userName));
//			}
			if(password!=null&&password.equals(user.getPassword())){
				currentUser.setLoginName(user.getLoginName());
				currentUser.setDisplayName(user.getDisplayName());
				user.setLoginFailCount(0);
				String loginIP=	request.getRemoteAddr();
				System.out.println(loginIP);
				user.setUserIP(loginIP);
				 String lastloign = sdf.format(new Date());//获取当前时间
				 user.setLastLogin(lastloign);
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
	public UserDto findUserByName(String userName){
		User user = userRepo.findUserByName(userName);
		UserDto userDto = new UserDto();
		BeanCopierUtils.copyProperties(user, userDto);
		List<Role> roleList = user.getRoles();
		if(roleList != null && roleList.size() >0){
			List<RoleDto> roleDtoList = new ArrayList<RoleDto>(roleList.size());
			RoleDto roleDto = new RoleDto();
			roleList.forEach(r ->{
				BeanCopierUtils.copyProperties(r, roleDto);
				roleDtoList.add(roleDto);
			});
			userDto.setRoles(roleDtoList);
		}		
		return userDto;
	}
	
	  protected void createActivitiUser(String userId, String userName, String userPwd, List<String> groups){
		  if (identityService.createUserQuery().userId(userId).count() == 0) {
			   // Following data can already be set by demo setup script
			   org.activiti.engine.identity.User user = identityService.newUser(userId);
	           user.setFirstName(userName);
	           user.setPassword(userPwd);
	           identityService.saveUser(user);
		            
	           if (groups != null) {
	              for (String group : groups) {
	                  identityService.createMembership(userId, group);
	              }
	           }
		  }
	}
	 protected void deleteActivitiUser(String userId){
		 if (identityService.createUserQuery().userId(userId).count() != 0) {

	            // Following data can already be set by demo setup script
	            List<Group> oldGroups = identityService.createGroupQuery().groupMember(userId).list();
	            for(Group group:oldGroups){
	            	identityService.deleteMembership(userId, group.getId());
	            }
	            identityService.deleteUser(userId);
		 }
	 }
	 
	 
	 protected void updateActivitiUser(String userId, String userName, String userPwd, 
	          List<String> groups){
		 if (identityService.createUserQuery().userId(userId).count() != 0) {
			 List<Group> oldGroups = identityService.createGroupQuery().groupMember(userId).list();
	            for(Group oldgroup:oldGroups){
	            	identityService.deleteMembership(userId, oldgroup.getName());
	            }
	            identityService.deleteUser(userId);
	            // Following data can already be set by demo setup script

	            org.activiti.engine.identity.User user = identityService.newUser(userId);
	            user.setFirstName(userName);
	            user.setPassword(userPwd);
	            identityService.saveUser(user);
	            if (groups != null) {
	                for (String group : groups) {
	                    identityService.createMembership(userId, group);
	                }
	            }
		 }
	 }

	@Override
	@Transactional
	public List<UserDto> getUser() {
		List<User>  users=userRepo.findAll();
		List<UserDto> userDtolist = new  ArrayList<>();
		if(users != null && users.size() > 0){
			users.forEach(x->{
				UserDto userDto = new UserDto();
				BeanCopierUtils.copyProperties(x, userDto);
				userDto.setCreatedDate(x.getCreatedDate());
				userDto.setModifiedDate(x.getModifiedDate());
				userDtolist.add(userDto);
			});						
		}	
		return userDtolist;
		
	}
	 
	/**
	 * 根据角色名称获取用户信息
	 * @author ldm
	 */
	@Override
	public List<UserDto> findUserByRoleName(String roleName) {
		List<User> users=userRepo.findUserByRoleName(roleName);
		List<UserDto> userDtolist = new  ArrayList<>();

		if(users != null && users.size() > 0){
			users.forEach(x->{
				UserDto userDto = new UserDto();
				BeanCopierUtils.copyProperties(x, userDto);
				userDto.setCreatedDate(x.getCreatedDate());
				userDto.setModifiedDate(x.getModifiedDate());
				userDtolist.add(userDto);
			});						
		}	
		return userDtolist;		
	}


	/**
	 * 根据部门ID查询对应的用户信息
	 */
	@Override
	public List<UserDto> findUserByDeptId(String deptId) {
		List<User> userList = userRepo.findUserByDeptId(deptId);
		List<UserDto> userDtolist = new ArrayList<>();
		
		if(userList != null && userList.size() > 0){
			userList.forEach(x->{
				UserDto userDto = new UserDto();
				BeanCopierUtils.copyProperties(x, userDto);
				userDto.setCreatedDate(x.getCreatedDate());
				userDto.setModifiedDate(x.getModifiedDate());
				userDtolist.add(userDto);
			});						
		}		

		return userDtolist;
	}


	@Override
	public UserDto findById(String id) {
		
		User  user=	userRepo.findById(id);
		UserDto userDto = new UserDto();
		BeanCopierUtils.copyProperties(user, userDto);
		return userDto;
	}	
}



	

	
