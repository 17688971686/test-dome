package com.sn.framework.module.sys.service.impl;

import com.sn.framework.common.*;
import com.sn.framework.core.boot.properties.ShiroProperties;
import com.sn.framework.core.common.*;
import com.sn.framework.core.service.impl.SServiceImpl;
import com.sn.framework.module.sys.domain.Organ;
import com.sn.framework.module.sys.domain.Resource;
import com.sn.framework.module.sys.domain.Role;
import com.sn.framework.module.sys.domain.User;
import com.sn.framework.module.sys.model.UserDto;
import com.sn.framework.module.sys.model.UserInfoDto;
import com.sn.framework.module.sys.repo.IOrganRepo;
import com.sn.framework.module.sys.repo.IResourceRepo;
import com.sn.framework.module.sys.repo.IUserRepo;
import com.sn.framework.module.sys.service.IUserService;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.security.GeneralSecurityException;
import java.util.*;

import static com.sn.framework.common.StringUtil.SEPARATE_COMMA;
import static com.sn.framework.core.Constants.CACHE_KEY_SYS_RESOURCE;
import static com.sn.framework.core.Constants.USER_KEY_ADMIN;

/**
 * 用户信息  业务实现类
 *
 * @author lqs
 * @date 2017/7/19
 */
@Service
public class UserServiceImpl extends SServiceImpl<IUserRepo, User, UserDto> implements IUserService {

    private final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private ShiroProperties shiroProperties;
    @Autowired
    private IResourceRepo resourceRepo;
    @Autowired
    private IOrganRepo organRepo;
    /**
     * 默认密码：默认值为123456
     */
    @Value("${sn.default-password:123456}")
    private String DEFAULT_PASSWORD;

    /**
     * 用户集合转换
     *
     * @param domainList       实际用户集合
     * @param ignoreProperties 忽略字段
     * @return
     */
    @Override
    protected List<UserDto> convertDtos(List<User> domainList, String... ignoreProperties) {
        if (ObjectUtils.isEmpty(ignoreProperties)) {
            ignoreProperties = new String[]{"password", "userSalt", "roles", "organ"};
        }
        final String[] _ignoreProperties = ignoreProperties;
        List<UserDto> dtoList = new ArrayList<>(domainList.size());
        domainList.forEach(x -> {
            UserDto dto = x.convert(modelCls, _ignoreProperties);
            if (null != x.getOrgan()) {
                dto.setOrgan(new Organ(x.getOrgan()));
            }
            dtoList.add(dto);
        });
        return dtoList;
    }

    @Override
    public UserDto findByUsername(String username, String... ignoreProperties) {
        User domain = baseRepo.findByUsername(username);
        return convertDto(domain, ignoreProperties);
    }

    @Override
    public UserDto getById(String userId, String... ignoreProperties) {
        User domain = baseRepo.getById(userId);
        return convertDto(domain, ignoreProperties);
    }

    /**
     * 把用户数据映射实体转换为用户展示实体
     *
     * @param domain
     * @param ignoreProperties
     * @return
     */
    protected static UserDto convertDto(User domain, String... ignoreProperties) {
        if (null == domain) {
            return null;
        }
        if (ObjectUtils.isEmpty(ignoreProperties)) {
            ignoreProperties = new String[]{"password", "userSalt", "roles", "organ"};
        }
        UserDto dto = domain.convert(UserDto.class, ignoreProperties);
        if (null != domain.getOrgan()) {
            dto.setOrgan(domain.getOrgan().convert(Organ.class, "users", "resources"));
        }
        return dto;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create(UserDto dto, String... ignoreProperties) {
        Assert.hasText(dto.getUsername(), "用户名不能为空");
        User user = baseRepo.findByUsername(dto.getUsername());
        if (null == user) {
            if (ObjectUtils.isEmpty(ignoreProperties)) {
                ignoreProperties = new String[]{"createdDate", "modifiedDate", "roles", "password"};
            }
            user = dto.convert(domainCls, ignoreProperties);
            if (StringUtil.isBlank(user.getUserId())) {
                user.setUserId(IdWorker.get32UUID());
            }
            String pwd = dto.getPassword(), vPwd = dto.getVerifyPassword();
            if (StringUtil.isNotBlank(pwd)) {
                if (null != SessionUtil.getSession()) {
                    RSAKey resKey = (RSAKey) SessionUtil.getSession().getAttribute("resKey");
                    Assert.notNull(resKey, "密码校验异常");
                    try {
                        pwd = new String(RSAUtils.decryptByPrivateKey(String.valueOf(pwd), resKey.getPrivateKey()));
                        vPwd = new String(RSAUtils.decryptByPrivateKey(String.valueOf(vPwd), resKey.getPrivateKey()));
                    } catch (GeneralSecurityException e) {
                        throw new SnRuntimeException("密码验证失败", e.getCause());
                    }
                }
                Assert.isTrue(pwd.equals(vPwd), "两次输入的密码不一致");
                user.setPassword(pwd);
            } else {
                user.setPassword(DEFAULT_PASSWORD);
            }

            SNKit.decodePwd(user, shiroProperties.isEncryption());

            if (StringUtil.isBlank(user.getUseState())) {
                user.setUseState("0");
            }

            String createdBy = SessionUtil.getUsername();
            if (StringUtil.isBlank(createdBy)) {
                createdBy = "root";
            }
            user.setCreatedBy(createdBy);
            user.setModifiedBy(createdBy);
            //如果有部门，则新增部门
            if(Validate.isObject(dto.getOrgan()) && Validate.isString(dto.getOrgan().getOrganId())){
                user.setOrgan(organRepo.getById(dto.getOrgan().getOrganId()));
            }
            baseRepo.save(user);
        } else {
            logger.warn(String.format("用户名称:%s 已经存在，请重新输入！", dto.getUsername()));
            throw new IllegalArgumentException(String.format("用户名称:%s 已经存在，请重新输入！", dto.getUsername()));
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(UserDto dto, String... ignoreProperties) {
        User user = baseRepo.getById(dto.getUserId());
        if (null != user) {
            //修改时过滤掉createdDate", "modifiedDate", "createdBy", "password", "userSalt
            if (ObjectUtils.isEmpty(ignoreProperties)) {
                ignoreProperties = new String[]{"createdDate", "modifiedDate", "createdBy", "password", "userSalt", "userDataType"};
            }
            BeanUtils.copyProperties(dto, user, ignoreProperties);
            if(Validate.isObject(dto.getOrgan()) && Validate.isString(dto.getOrgan().getOrganId())){
                boolean isnew = true;
                if(Validate.isObject(user.getOrgan()) && Validate.isString(user.getOrgan().getOrganId())){
                    if(dto.getOrgan().getOrganId().equals(user.getOrgan().getOrganId())){
                        isnew = false;
                    }
                }
                if(isnew){
                    user.setOrgan(organRepo.getById(dto.getOrgan().getOrganId()));
                }
            }else{
                user.setOrgan(null);
            }
            user.setModifiedBy(SessionUtil.getUsername());
            user.setModifiedDate(new Date());
            baseRepo.save(user);
        } else {
            logger.warn("数据不存在");
            throw new IllegalArgumentException("数据不存在");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void changeUserState(String userId, boolean isEnable) {
        User entity = baseRepo.getById(userId);
        Assert.notNull(entity, "数据不存在");
        entity.setUseState(isEnable ? "1" : "0");
        baseRepo.save(entity);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteById(String userId) {
        User entity = baseRepo.getById(userId);
        if (null != entity) {
            if (entity.getUserDataType() == 0) {
                throw new IllegalArgumentException("系统基础数据无法删除");
            }
            baseRepo.delete(entity);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @Cacheable(cacheNames = CACHE_KEY_SYS_RESOURCE, key = "#username + '_' + #status")
    public List<Resource> getUserResources(String username, String status) {
        if(!"admin".equals(username)){
            User user = baseRepo.findByUsername(username);
            return resourceRepo.findMenus(user, status);
        }else{
            return resourceRepo.findAll();
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveCurrentUserInfo(UserInfoDto userDto) {
        User user = baseRepo.findByUsername(SessionUtil.getUsername());
        BeanUtils.copyProperties(userDto, user);
        String oldPassword = userDto.getOldPassword();
        Date currentDate = new Date();
        if (StringUtil.isNotBlank(oldPassword)) {
            RSAKey resKey = (RSAKey) SessionUtil.getSession().getAttribute("resKey");
            Assert.notNull(resKey, "密码校验异常");
            try {
                oldPassword = new String(RSAUtils.decryptByPrivateKey(String.valueOf(oldPassword), resKey.getPrivateKey()));
            } catch (GeneralSecurityException e) {
                throw new SnRuntimeException("原密码错误", e.getCause());
            }
            if (shiroProperties.isEncryption()) {
                oldPassword = SNKit.decodePwd(user.getUsername(), oldPassword, user.getUserSalt());
            }
            if (!oldPassword.equals(user.getPassword())) {
                throw new SnRuntimeException("原密码错误");
            }
            String newPassword = userDto.getNewPassword(),
                    verifyPassword = userDto.getVerifyPassword();
            Assert.hasText(newPassword, "未设置新的密码");
            if (!SNKit.checkPwd(newPassword)) {
                throw new SnRuntimeException("密码过于简单，必须同时包含数字和字母，并且不得少于 8 个字符");
            }
            Assert.hasText(verifyPassword, "未设置新的确认密码");
            try {
                newPassword = new String(RSAUtils.decryptByPrivateKey(String.valueOf(newPassword), resKey.getPrivateKey()));
                verifyPassword = new String(RSAUtils.decryptByPrivateKey(String.valueOf(verifyPassword), resKey.getPrivateKey()));
            } catch (GeneralSecurityException e) {
                throw new SnRuntimeException("新密码错误", e.getCause());
            }
            if (!newPassword.equals(verifyPassword)) {
                throw new SnRuntimeException("两次输入的新密码不相同");
            }
            user.setPassword(newPassword);
            SNKit.decodePwd(user, shiroProperties.isEncryption());
            user.setChangePasswordDate(currentDate);
        }
        user.setModifiedBy(SessionUtil.getUsername());
        user.setModifiedDate(currentDate);
        baseRepo.save(user);
        // 更新用户的session信息
        SNKit.addSessionAttribute(SecurityUtils.getSubject(), user);
    }

    @Override
    public List<UserDto> findMajorList() {
        List<User> users = baseRepo.findMajorList();
        return convertDtos(users);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void resetPwd(String userId) {
        User user = baseRepo.getById(userId);
        user.setPassword(DEFAULT_PASSWORD);
        SNKit.decodePwd(user, shiroProperties.isEncryption());
        baseRepo.save(user);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = CACHE_KEY_SYS_RESOURCE, allEntries = true)
    public void setRoles(String userId, String roleIds) {
        Assert.hasText(userId, "未指定用户");
        User user = baseRepo.getById(userId);
        Assert.notNull(user, "未找到用户数据");
        user.getRoles().clear();
        if (StringUtil.isNotBlank(roleIds)) {
            String[] ids = StringUtil.split(roleIds, SEPARATE_COMMA);
            Set<Role> roleSet = new HashSet<>();
            for (int i = 0, len = ids.length; i < len; i++) {
                Role role = commonRepo.findById(Role.class, ids[i]);
                if (null != role) {
                    roleSet.add(role);
                }
            }
            user.setRoles(roleSet);
        }
        user.setModifiedDate(new Date());
        user.setModifiedBy(SessionUtil.getUsername());
        baseRepo.update(user);
    }

    @Override
    public List<UserDto> findUserByOrgId() {
        List<User> userList =  new ArrayList<>();
        if(USER_KEY_ADMIN.equals(SessionUtil.getUserInfo().getUsername())){
           userList = baseRepo.getUserByOrganId(null,true);
        }else{
          userList = baseRepo.getUserByOrganId(SessionUtil.getUserInfo().getOrgan().getOrganId(),true);
        }
       if(Validate.isList(userList)){
           return convertDtos(userList);
       }else{
           return null;
       }

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void resetAllUserPwd() {
        List<User> userList = baseRepo.findAll();
        if(Validate.isList(userList)){
            for(User user:userList){
                if(!USER_KEY_ADMIN.equals(user.getUsername())){
                    user.setPassword(DEFAULT_PASSWORD);
                    SNKit.decodePwd(user, shiroProperties.isEncryption());
                    baseRepo.save(user);
                }
            }
        }

    }

    @Override
    public List<UserDto> getOrganUser() {
        List<User> orgUserList = baseRepo.getOrganUser();
        if(Validate.isList(orgUserList)){
            return convertDtos(orgUserList);
        }else{
            return null;
        }
    }
}
