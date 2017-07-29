package cs.service.sys;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.activiti.engine.IdentityService;
import org.activiti.engine.identity.Group;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cs.common.Constant;
import cs.common.Constant.EnumFlowNodeGroupName;
import cs.common.HqlBuilder;
import cs.common.utils.BeanCopierUtils;
import cs.common.utils.SessionUtil;
import cs.common.utils.Validate;
import cs.domain.sys.Org;
import cs.domain.sys.Org_;
import cs.domain.sys.Role;
import cs.domain.sys.Role_;
import cs.domain.sys.User;
import cs.domain.sys.User_;
import cs.model.PageModelDto;
import cs.model.sys.OrgDto;
import cs.model.sys.RoleDto;
import cs.model.sys.UserDto;
import cs.repository.odata.ODataObj;
import cs.repository.repositoryImpl.sys.OrgRepo;
import cs.repository.repositoryImpl.sys.RoleRepo;
import cs.repository.repositoryImpl.sys.UserRepo;

@Service
public class UserServiceImpl implements UserService {
    private static Logger logger = Logger.getLogger(UserServiceImpl.class);
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private RoleRepo roleRepo;
    @Autowired
    private OrgRepo orgRepo;
    @Autowired
    private IdentityService identityService;

    @Override
    @Transactional
    public PageModelDto<UserDto> get(ODataObj odataObj) {
        List<User> listUser = userRepo.findByOdata(odataObj);
        List<UserDto> userDtoList = new ArrayList<>();

        for (User item : listUser) {
            UserDto userDto = new UserDto();
            BeanCopierUtils.copyProperties(item, userDto);

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
            userDto.setRoleDtoList(roleDtoList);

            OrgDto orgDto = new OrgDto();
            if (item.getOrg() != null) {
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
        User findUser = userRepo.findUserByName(userDto.getLoginName());
        // 用户不存在
        if (findUser == null) {
            User user = new User();
            BeanCopierUtils.copyProperties(userDto, user);

            user.setId(UUID.randomUUID().toString());
            user.setCreatedBy(SessionUtil.getLoginName());
            user.setCreatedDate(new Date());
            user.setModifiedDate(new Date());
            user.setModifiedBy(SessionUtil.getLoginName());
            user.setPassword(Constant.PASSWORD);		//设置系统默认登录密码

            List<String> roleNames = new ArrayList<String>();
            // 加入角色
            for (RoleDto roleDto : userDto.getRoleDtoList()) {
                Role role = roleRepo.findById(Role_.id.getName(),roleDto.getId());
                if (role != null) {
                    user.getRoles().add(role);
                    roleNames.add(role.getRoleName());
                }
            }
            //添加部门
            if (Validate.isString(userDto.getOrgId())) {
                Org o = orgRepo.findById(Org_.id.getName(),userDto.getOrgId());
                user.setOrg(o);
            }
            userRepo.save(user);

            createActivitiUser(user.getId(), user.getLoginName(), user.getPassword(), roleNames);
            logger.info(String.format("创建用户,登录名:%s", userDto.getLoginName()));
        } else {
            throw new IllegalArgumentException(String.format("用户：%s 已经存在,请重新输入！", userDto.getLoginName()));
        }

    }

    @Override
    @Transactional
    public List<OrgDto> getOrg(ODataObj odataObj) {
        List<Org> org = orgRepo.findByOdata(odataObj);
        List<OrgDto> orgDto = new ArrayList<>();

        for (Org item : org) {
            OrgDto orgDtos = new OrgDto();
            BeanCopierUtils.copyProperties(item, orgDtos);
            orgDto.add(orgDtos);
        }
        return orgDto;
    }

    @Override
    @Transactional
    public void deleteUser(String id) {
        userRepo.deleteById(User_.id.getName(), id);
    }

    @Override
    @Transactional
    public void updateUser(UserDto userDto) {
        User user = userRepo.findById(userDto.getId());
        user.setRemark(userDto.getRemark());
        user.setDisplayName(userDto.getDisplayName());
        user.setModifiedBy(SessionUtil.getLoginName());

        user.setUserSex(userDto.getUserSex());
        user.setUserPhone(userDto.getUserPhone());
        user.setUserMPhone(userDto.getUserMPhone());
        user.setEmail(userDto.getEmail());
        user.setJobState(userDto.getJobState());
        user.setUseState(userDto.getUseState());
        user.setPwdState(userDto.getPwdState());

        //添加部门
        if (Validate.isString(userDto.getOrgId())) {
            Org o = orgRepo.findById(userDto.getOrgId());
            user.setOrg(o);
        }

        // 清除已有role
        user.getRoles().clear();
        List<String> roleNames = new ArrayList<String>();
        // 加入角色
        for (RoleDto roleDto : userDto.getRoleDtoList()) {
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
    public Set<String> findPermissions(String userName) {
        return userRepo.getUserPermission(userName);

    }

    @Override
    public Set<String> findRoles(String userName) {
        return userRepo.getUserRoles(userName);
    }

    @Transactional
    public void changePwd(String password) {
        String userName = SessionUtil.getLoginName();
        User user = userRepo.findUserByName(userName);
        if (user != null) {
            user.setPassword(password);
            userRepo.save(user);
            logger.info(String.format("修改密码,用户名:%s", userName));
        }
    }

    @Transactional
    public UserDto findUserByName(String userName) {
        User user = userRepo.findUserByName(userName);
        UserDto userDto = new UserDto();
        BeanCopierUtils.copyProperties(user, userDto);

        List<Role> roleList = user.getRoles();
        if (roleList != null && roleList.size() > 0) {
            List<RoleDto> roleDtoList = new ArrayList<RoleDto>(roleList.size());
            RoleDto roleDto = new RoleDto();
            roleList.forEach(r -> {
                BeanCopierUtils.copyProperties(r, roleDto);
                roleDtoList.add(roleDto);
            });
            userDto.setRoleDtoList(roleDtoList);
        }

        if (user.getOrg() != null) {
            OrgDto orgDto = new OrgDto();
            BeanCopierUtils.copyProperties(user.getOrg(), orgDto);
            userDto.setOrgDto(orgDto);
        }
        return userDto;
    }

    protected void createActivitiUser(String userId, String userName, String userPwd, List<String> groups) {
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

    protected void deleteActivitiUser(String userId) {
        if (identityService.createUserQuery().userId(userId).count() != 0) {
            // Following data can already be set by demo setup script
            List<Group> oldGroups = identityService.createGroupQuery().groupMember(userId).list();
            for (Group group : oldGroups) {
                identityService.deleteMembership(userId, group.getId());
            }
            identityService.deleteUser(userId);
        }
    }


    protected void updateActivitiUser(String userId, String userName, String userPwd, List<String> groups) {
        if (identityService.createUserQuery().userId(userId).count() != 0) {
            List<Group> oldGroups = identityService.createGroupQuery().groupMember(userId).list();
            for (Group oldgroup : oldGroups) {
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
        List<User> users = userRepo.findAll();
        List<UserDto> userDtolist = new ArrayList<>();
        if (users != null && users.size() > 0) {
            users.forEach(x -> {
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
     *
     * @author ldm
     */
    @Override
    public List<UserDto> findUserByRoleName(String roleName) {
        List<User> users = userRepo.findUserByRoleName(roleName);
        List<UserDto> userDtolist = new ArrayList<>();

        if (users != null && users.size() > 0) {
            users.forEach(x -> {
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
     * 根据部门ID查询对应的用户信息(不包含角色和部门信息)
     */
    @Override
    public List<UserDto> findUserByOrgId(String orgId) {
        List<User> userList = userRepo.findUserByOrgId(orgId);
        List<UserDto> userDtolist = new ArrayList<>();
        if (userList != null && userList.size() > 0) {
            userList.forEach(x -> {
                UserDto userDto = new UserDto();
                BeanCopierUtils.copyProperties(x, userDto);
                userDtolist.add(userDto);
            });
        }
        return userDtolist;
    }


    /**
     * 根据ID查询用户
     * @param id
     * @param inclueOrg
     * @return
     */
    @Override
    public UserDto findById(String id,boolean inclueOrg) {
        UserDto userDto = new UserDto();
        if(inclueOrg){
            User user = userRepo.findById(id);
            BeanCopierUtils.copyProperties(user, userDto);
            if (user.getOrg() != null) {
                OrgDto orgDto = new OrgDto();
                BeanCopierUtils.copyProperties(user.getOrg(), orgDto);
                userDto.setOrgDto(orgDto);
            }
            if(user.getRoles()!=null){
            	List<RoleDto> roleDtoList=new ArrayList<>();
            	for(Role role : user.getRoles()){
            		RoleDto roleDto=new RoleDto();
            		BeanCopierUtils.copyProperties(role, roleDto);
            		roleDtoList.add(roleDto);
            	}
            	userDto.setRoleDtoList(roleDtoList);
            }
        }else{
            User user = userRepo.findById(User_.id.getName(),id);
            BeanCopierUtils.copyProperties(user, userDto);
        }

        return userDto;
    }


    /**
     * 获取当前用户的部门领导
     */
    @Override
    public UserDto getOrgDirector() {
        UserDto user = findById(SessionUtil.getUserInfo().getOrg().getOrgDirector(),false);
        if (user != null && Validate.isString(user.getId())) {
            return user;
        }
        return null;
    }


    /**
     * 获取当前用户的分管主任
     */
    @Override
    public UserDto getOrgSLeader() {
        Org org = orgRepo.findById(SessionUtil.getUserInfo().getOrg().getId());
        List<UserDto> userList = findUserByRoleName(EnumFlowNodeGroupName.VICE_DIRECTOR.getValue());
        if (userList == null || userList.size() == 0) {
            return null;
        }
        UserDto user = filterOrgSLeader(userList, org);
        if (user != null && Validate.isString(user.getLoginName())) {
            return user;
        }
        return null;
    }

    /**
     * 获取部门部长
     *
     * @param userList
     * @param org
     * @return
     */
    public UserDto filterOrgDirector(List<UserDto> userList, Org org) {
        for (int i = 0, l = userList.size(); i < l; i++) {
            UserDto delUser = userList.get(i);
            if (delUser.getId().equals(org.getOrgDirector())) {
                return delUser;
            }
        }
        return null;
    }

    /**
     * 获取部门副主任
     *
     * @param userList
     * @param org
     * @return
     */
    public UserDto filterOrgSLeader(List<UserDto> userList, Org org) {
        for (int i = 0, l = userList.size(); i < l; i++) {
            UserDto delUser = userList.get(i);
            if (delUser.getId().equals(org.getOrgSLeader())) {
                return delUser;
            }
        }
        return null;
    }


    @Override
    public List<UserDto> findAllusers() {
        List<User> users = userRepo.findAll();
        List<UserDto> userDtos = new ArrayList<>();
        if (users != null && users.size() > 0) {
            users.forEach(x -> {
                UserDto userDto = new UserDto();
                BeanCopierUtils.copyProperties(x, userDto);
                userDto.setCreatedDate(x.getCreatedDate());
                userDto.setModifiedDate(x.getModifiedDate());
                userDtos.add(userDto);
            });
        }
        return userDtos;
    }


	@Override
	public int findMaxUserNo() {
		
		HqlBuilder sql=HqlBuilder.create();
		
		sql.append("select max(to_number(userNo)) from cs_user");
		
		return userRepo.returnIntBySql(sql);
	}


	@Override
	public List<UserDto> findByOrgUserName(String orgId) {
		
	    HqlBuilder sqlBuilder = HqlBuilder.create();
        sqlBuilder.append(" select loginName from cs_user where orgId = ");
        sqlBuilder.setParam("orgId", orgId);
        List<User> users=  userRepo.findByHql(sqlBuilder);
        List<UserDto> userDto = new ArrayList<UserDto>();
        
        if (users != null && users.size() > 0) {
            users.forEach(x -> {
                UserDto userDtos = new UserDto();
                BeanCopierUtils.copyProperties(x, userDto);
                userDtos.setCreatedDate(x.getCreatedDate());
                userDtos.setModifiedDate(x.getModifiedDate());
                userDto.add(userDtos);
            });
        }
		return userDto;
	}

	/********************   以下方法主要是登录用  **********************/
    @Override
    public User findByName(String userName) {
        return userRepo.findUserByName(userName);
    }

    @Override
    public void saveUser(User user) {
        userRepo.save(user);
    }
}



	

	
