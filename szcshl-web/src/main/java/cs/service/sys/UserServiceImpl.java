package cs.service.sys;

import cs.common.Constant;
import cs.common.Constant.EnumFlowNodeGroupName;
import cs.common.HqlBuilder;
import cs.common.ResultMsg;
import cs.common.utils.BeanCopierUtils;
import cs.common.utils.SessionUtil;
import cs.common.utils.Validate;
import cs.domain.sys.*;
import cs.model.PageModelDto;
import cs.model.sys.OrgDto;
import cs.model.sys.RoleDto;
import cs.model.sys.UserDto;
import cs.repository.odata.ODataObj;
import cs.repository.repositoryImpl.sys.OrgRepo;
import cs.repository.repositoryImpl.sys.RoleRepo;
import cs.repository.repositoryImpl.sys.UserRepo;
import org.activiti.engine.IdentityService;
import org.activiti.engine.identity.Group;
import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

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
    @Autowired
    private LogService logService;
    @Autowired
    private OrgDeptRepo orgDeptRepo;

    @Override
    @Transactional
    public PageModelDto<UserDto> get(ODataObj odataObj) {
        PageModelDto<UserDto> pageModelDto = new PageModelDto<>();
        Criteria criteria = userRepo.getExecutableCriteria();
        odataObj.buildFilterToCriteria(criteria);
        criteria.addOrder(Order.desc(User_.jobState.getName())).addOrder(Order.asc(User_.userSort.getName()));
        //排除掉超级管理员
        /*Integer totalResult = ((Number) criteria.setProjection(Projections.rowCount()).uniqueResult()).intValue();
        pageModelDto.setCount(totalResult);
        criteria.setProjection(null);
        criteria.addOrder(Order.desc(User_.jobState.getName())).addOrder(Order.asc(User_.userSort.getName()));
        criteria.setFirstResult(odataObj.getSkip());
        criteria.setMaxResults(odataObj.getTop());*/

        List<User> listUser = criteria.list();
        List<UserDto> userDtoList = new ArrayList<>();
        if(Validate.isList(listUser)){
            int totalCount = listUser.size();
            pageModelDto.setCount(totalCount);
            for (int i=0,l=totalCount;i<l;i++) {
                User item = listUser.get(i);
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
        }

        pageModelDto.setValue(userDtoList);

        logger.info("查询用户数据");
        return pageModelDto;
    }


    @Override
    @Transactional
    public ResultMsg createUser(UserDto userDto) {
        User findUser = userRepo.findUserByName(userDto.getLoginName());
        // 用户不存在
        if (findUser == null) {
            boolean isSLeader = false;  //是否是分管领导
            User user = new User();
            BeanCopierUtils.copyProperties(userDto, user);

            user.setId(UUID.randomUUID().toString());
            if (!Validate.isString(user.getUserNo())) {
                user.setUserNo(String.format("%03d", Integer.valueOf(findMaxUserNo()) + 1));
            }
            user.setCreatedBy(SessionUtil.getLoginName());
            user.setCreatedDate(new Date());
            user.setModifiedDate(new Date());
            user.setModifiedBy(SessionUtil.getLoginName());
            user.setPassword(Constant.PASSWORD);        //设置系统默认登录密码

            List<String> roleNames = new ArrayList<String>();
            // 加入角色
            for (RoleDto roleDto : userDto.getRoleDtoList()) {
                Role role = roleRepo.findById(Role_.id.getName(), roleDto.getId());
                if (role != null) {
                    if (EnumFlowNodeGroupName.VICE_DIRECTOR.getValue().equals(role.getRoleName())) {
                        isSLeader = true;
                    }
                    user.getRoles().add(role);
                    roleNames.add(role.getRoleName());
                }
            }
            //添加部门
            if (Validate.isString(userDto.getOrgId())) {
                Org o = orgRepo.findById(Org_.id.getName(), userDto.getOrgId());
                user.setOrg(o);
                //如果是分管领导，则设置默认分管部门类型
                if (isSLeader) {
                    user.setMngOrgType(Constant.OrgType.getValue(o.getName()));
                }
            }
            userRepo.save(user);
            fleshPostUserCache();
            //createActivitiUser(user.getId(), user.getLoginName(), user.getPassword(), roleNames);
            return new ResultMsg(true, Constant.MsgCode.OK.getValue(), user.getId(), "创建成功", null);
        } else {
            return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), String.format("用户：%s 已经存在,请重新输入！", userDto.getLoginName()));
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
    public ResultMsg deleteUser(String id) {
        try {
            userRepo.deleteById(User_.id.getName(), id);
            fleshPostUserCache();
            return new ResultMsg(true, Constant.MsgCode.OK.getValue(),"删除成功！");
        }catch (Exception e){
            logger.info("删除用户异常："+e.getMessage());
            return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(),"删除异常："+e.getMessage());
        }
    }

    @Override
    @Transactional
    public ResultMsg updateUser(UserDto userDto) {
        boolean isSLeader = false;
        User user = userRepo.findById(userDto.getId());
        BeanCopierUtils.copyPropertiesIgnoreNull(userDto, user);

        // 清除已有role
        user.getRoles().clear();
        List<String> roleNames = new ArrayList<String>();
        // 加入角色
        for (RoleDto roleDto : userDto.getRoleDtoList()) {
            Role role = roleRepo.findById(roleDto.getId());
            roleNames.add(roleDto.getRoleName());
            if (role != null) {
                user.getRoles().add(role);
                if (EnumFlowNodeGroupName.VICE_DIRECTOR.getValue().equals(role.getRoleName())) {
                    isSLeader = true;
                }
            }
        }
        //添加部门
        if (Validate.isString(userDto.getOrgId())) {
            Org o = orgRepo.findById(userDto.getOrgId());
            user.setOrg(o);
            //如果是分管领导，则设置默认分管部门类型
            if (isSLeader) {
                user.setMngOrgType(Constant.OrgType.getValue(o.getName()));
            }
        }
        userRepo.save(user);
        fleshPostUserCache();
        //this.updateActivitiUser(user.getId(), user.getLoginName(), user.getPassword(), roleNames);
        return new ResultMsg(true, Constant.MsgCode.OK.getValue(), "修改成功！");
    }

    @Override
    public Set<String> findPermissions(String userName) {
        return userRepo.getUserPermission(userName);
    }

    @Override
    public Set<String> findRoles(String userName) {
        return userRepo.getUserRoles(userName);
    }

    @Override
    public void changePwd(String password) {
        User user = userRepo.getCacheUserById(SessionUtil.getUserId());
        if (user != null) {
            user.setPassword(password);
            userRepo.save(user);
            fleshPostUserCache();
            logger.info(String.format("修改密码,用户名:%s", user.getDisplayName()));
        }
    }

    @Override
    public UserDto findUserByName(String userName) {
        User user = userRepo.findUserByName(userName);
        UserDto userDto = new UserDto();
        BeanCopierUtils.copyProperties(user, userDto);

        List<Role> roleList = user.getRoles();
        if (roleList != null && roleList.size() > 0) {
            List<RoleDto> roleDtoList = new ArrayList<RoleDto>(roleList.size());
            roleList.forEach(r -> {
                RoleDto roleDto = new RoleDto();
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
     *
     * @param id
     * @param inclueOrg
     * @return
     */
    @Override
    public UserDto findById(String id, boolean inclueOrg) {
        UserDto userDto = new UserDto();
        if (inclueOrg) {
            User user = userRepo.findById(id);
            BeanCopierUtils.copyProperties(user, userDto);
            if (user.getOrg() != null) {
                OrgDto orgDto = new OrgDto();
                BeanCopierUtils.copyProperties(user.getOrg(), orgDto);
                userDto.setOrgDto(orgDto);
            }
            if (user.getRoles() != null) {
                List<RoleDto> roleDtoList = new ArrayList<>();
                for (Role role : user.getRoles()) {
                    RoleDto roleDto = new RoleDto();
                    BeanCopierUtils.copyProperties(role, roleDto);
                    roleDtoList.add(roleDto);
                }
                userDto.setRoleDtoList(roleDtoList);
            }
        } else {
            User user = userRepo.findById(User_.id.getName(), id);
            BeanCopierUtils.copyProperties(user, userDto);
        }

        return userDto;
    }

    /**
     * 获取当前用户的部门领导
     */
    @Override
    public UserDto getOrgDirector(String userId) {
        User user = userRepo.findOrgDirector(userId);
        if (user == null) {
            return null;
        }
        UserDto userDto = new UserDto();
        BeanCopierUtils.copyProperties(user, userDto);
        return userDto;
    }


    /**
     * 获取当前用户的分管主任
     */
    @Override
    public UserDto getOrgSLeader(String userId) {
        User user = userRepo.findOrgSLeader(userId);
        if (user == null) {
            return null;
        }
        UserDto userDto = new UserDto();
        BeanCopierUtils.copyProperties(user, userDto);
        return userDto;
    }

    /**
     * 获取部门副主任
     *
     * @param userList
     * @param org
     * @return
     */
    @Override
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
        List<User> users = findAllPostUser();
        List<UserDto> userDtos = new ArrayList<>();
        if (Validate.isList(users)) {
            users.forEach(x -> {
                UserDto userDto = new UserDto();
                BeanCopierUtils.copyProperties(x, userDto);
                userDtos.add(userDto);
            });
        }
        return userDtos;
    }


    @Override
    public int findMaxUserNo() {
        HqlBuilder sql = HqlBuilder.create();
        sql.append("select max(to_number(userNo)) from cs_user");
        return userRepo.returnIntBySql(sql);
    }


    @Override
    public List<UserDto> findByOrgUserName(String orgId) {

        HqlBuilder sqlBuilder = HqlBuilder.create();
        sqlBuilder.append(" select loginName from cs_user where orgId = ");
        sqlBuilder.setParam("orgId", orgId);
        List<User> users = userRepo.findByHql(sqlBuilder);
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


    /**
     * 查询所有用户显示名和id
     */
    @Override
    public List<UserDto> getAllUserDisplayName() {
//       User u =  userRepo.findById(SessionUtil.getUserId());

        HqlBuilder hqlBuilder = HqlBuilder.create();
        hqlBuilder.append("select " + User_.id.getName() + "," + User_.displayName.getName() + " from cs_user where " + User_.loginName.getName() + "<>:loginName");

        hqlBuilder.setParam("loginName", SessionUtil.getLoginName());

        List<UserDto> userDtoList = new ArrayList<>();

        if (SessionUtil.hashRole(EnumFlowNodeGroupName.DIRECTOR.getValue()) ||
                SessionUtil.hashRole(EnumFlowNodeGroupName.VICE_DIRECTOR.getValue()) ||
                SessionUtil.hashRole(EnumFlowNodeGroupName.DEPT_LEADER.getValue())
                ) {//如果是领导，则根据所属角色选择代办代理人
            hqlBuilder.append(" and " + User_.id.getName() + " in(select users_id from CS_USER_CS_ROLE where roles_id =(");
            hqlBuilder.append("select " + Role_.id.getName() + " from cs_role where ");
            hqlBuilder.append(Role_.roleName.getName() + "=:roleName))");
            if (SessionUtil.hashRole(EnumFlowNodeGroupName.DIRECTOR.getValue())) {//主任
                hqlBuilder.setParam("roleName", EnumFlowNodeGroupName.DIRECTOR.getValue());
            }

            if (SessionUtil.hashRole(EnumFlowNodeGroupName.VICE_DIRECTOR.getValue())) {//副主任
                hqlBuilder.setParam("roleName", EnumFlowNodeGroupName.VICE_DIRECTOR.getValue());
            }
            if (SessionUtil.hashRole(EnumFlowNodeGroupName.DEPT_LEADER.getValue())) {//部门负责人
                hqlBuilder.setParam("roleName", EnumFlowNodeGroupName.DEPT_LEADER.getValue());
            }
        } else {//其他，则只能选择本部门工作人员作为代办代理
            hqlBuilder.append(" and orgId=(");
            hqlBuilder.append("select orgId from cs_user where " + User_.id.getName() + "=:userId)");
            hqlBuilder.setParam("userId", SessionUtil.getUserId());
        }
        List<Object[]> list = userRepo.getObjectArray(hqlBuilder);
        if (!list.isEmpty()) {
            for (int i = 0; i < list.size(); i++) {
                Object[] userNames = list.get(i);
                UserDto userDto = new UserDto();
                userDto.setId((String) userNames[0]);
                userDto.setDisplayName((String) userNames[1]);
                userDtoList.add(userDto);
            }
        }
        return userDtoList;
    }


    /**
     * 保存代办人
     */
    @Override
    public void saveTakeUser(String takeUserId) {
        HqlBuilder hqlBuilder = HqlBuilder.create();
        hqlBuilder.append("update " + User.class.getSimpleName() + " set " + User_.takeUserId.getName() + "=:takeUserId where " + User_.id.getName() + "=:userId ");
        hqlBuilder.setParam("takeUserId", takeUserId);
        hqlBuilder.setParam("userId", SessionUtil.getUserId());
        userRepo.executeHql(hqlBuilder);
        fleshPostUserCache();
    }
/*
    @Override
    public UserDto getTakeUserByLoginName() {
        HqlBuilder hqlBuilder = HqlBuilder.create();
        hqlBuilder.append("select " + User_.displayName.getName() + " from cs_user where id=(");
        hqlBuilder.append("select " + User_.takeUserId.getName() + " from cs_user where " + User_.loginName.getName() + "=:loginName)");
        hqlBuilder.setParam("loginName", SessionUtil.getLoginName());
        List<Object[]> list = userRepo.getObjectArray(hqlBuilder);
        UserDto userDto = new UserDto();
        if (!list.isEmpty()) {
            Object[] obj = list.get(0);
            userDto.setDisplayName(obj[0].toString());
        }
        return userDto;
    }*/

    /**
     * 取消代办人
     */
    @Override
    @Transactional
    public void cancelTakeUser() {
        HqlBuilder hqlBuilder = HqlBuilder.create();
        hqlBuilder.append("update " + User.class.getSimpleName() + " set " + User_.takeUserId.getName() + "=:takeUserId where " + User_.id.getName() + "=:userId");
        hqlBuilder.setParam("takeUserId", "");
        hqlBuilder.setParam("userId", SessionUtil.getUserId());
        userRepo.executeHql(hqlBuilder);
        fleshPostUserCache();
    }

    @Override
    public User getCacheUserById(String userId) {
        return userRepo.getCacheUserById(userId);
    }

    @Override
    public List<User> getCacheUserListById(String userIds) {
        return userRepo.getCacheUserListById(userIds);
    }

    @Override
    public List<User> findAllPostUser() {
        return userRepo.findAllPostUser();
    }

    @Override
    public void fleshPostUserCache() {
        userRepo.fleshPostUserCache();
    }

    @Override
    public void resetPwd(String ids) {
        List<User> userList = userRepo.getCacheUserListById(ids);
        for (User u : userList) {
            u.setPassword(Constant.PASSWORD);
            u.setModifiedBy(SessionUtil.getDisplayName());
            u.setModifiedDate(new Date());
        }
        userRepo.bathUpdate(userList);
        userRepo.fleshPostUserCache();
    }

    @Override
    public void saveUser(User user) {
        userRepo.save(user);
        userRepo.fleshPostUserCache();
    }

    @Override
    @Transactional
    public ResultMsg Login(HttpServletRequest request, HttpServletResponse res, String userName, String password){
        User user=userRepo.findUserByName(userName);
        ResultMsg resultMsg =new ResultMsg();
        if(user!=null){
            List<OrgDept> orgDeptList=orgDeptRepo.findAll();
            for(OrgDept orgDept :orgDeptList){
                //判断。登录的只能是部门负责人、副主任、主任、信息组
           if(orgDept.getDirectorID().equals(user.getId()) || orgDept.getsLeaderID().equals(user.getId()) || orgDept.getmLeaderID().equals(user.getId()) ){
            Date date = new Date();
            HttpServletRequest req = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            if(user.getLoginFailCount()>5&&user.getLastLoginDate().getDay()==(new Date()).getDay()){
                resultMsg.setReMsg("登录失败次数过多,请明天再试!");
                logger.warn(String.format("登录失败次数过多,用户名:%s", userName));
            }
            else if(password!=null&&password.equals(user.getPassword())){
                //setCookiesByJsessionid(request,res);
                user.setLoginFailCount(0);
                user.setLastLoginDate(new Date());
                //shiro
                UsernamePasswordToken token = new UsernamePasswordToken(user.getLoginName(), user.getPassword());
                Subject subject = SecurityUtils.getSubject();
                subject.login(token);

                resultMsg.setFlag(true);
                resultMsg.setReMsg(user.getDisplayName());
                logger.debug(String.format("登录成功,用户名:%s", userName));
                //登陆日志
                Log log = new Log();
                log.setUserName(user.getDisplayName());
                log.setResult("9");
                log.setCreatedDate(date);
                log.setMessage("登陆成功");
                logService.save(log);

            }else{
                user.setLoginFailCount(user.getLoginFailCount()+1);
                user.setLastLoginDate(new Date());
                resultMsg.setReMsg("用户名或密码错误!");
                //登陆日志
                Log log = new Log();
                log.setUserName(user.getDisplayName());
                log.setResult("0");
                log.setCreatedDate(date);
                log.setMessage("用户名或密码错误");
                logService.save(log);
                break;//当密码错误时就停止循环。防止提示信息被覆盖
            }
            userRepo.save(user);
        }else{
                resultMsg.setReMsg("没有权限登录!");
             }

        }
      }else{
            Date date = new Date();
            resultMsg.setReMsg("用户不存在!");
            //登陆日志
            HttpServletRequest req = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
            /*loginLog.setDisplayName("用户不存在");*/
            Log log = new Log();
            log.setResult("0");
            log.setCreatedDate(date);
            log.setMessage("用户不存在");
            logService.save(log);
        }

        return resultMsg;
    }
}



	

	
