package com.sn.framework.core.shiro;

import com.sn.framework.common.ObjectUtils;
import com.sn.framework.core.boot.properties.ShiroProperties;
import com.sn.framework.module.sys.domain.User;
import com.sn.framework.module.sys.repo.IUserRepo;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.Set;

/**
 * 自定义shiro权限校验
 *
 * @author tzg
 * @date 2016/12/22 23:39
 */
public class SNShiroRealm extends AuthorizingRealm {

    private static final Logger logger = LoggerFactory.getLogger(SNShiroRealm.class);

    @Autowired
    private IUserRepo userRepo;
    @Autowired
    private ShiroProperties shiroProperties;

    /**
     * 权限资源角色
     * @param principals
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        if (logger.isDebugEnabled()) {
            logger.debug("获取用户权限与角色");
        }
        String username = (String) principals.getPrimaryPrincipal();
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        //add Permission Resources
        Set<String> perms = userRepo.getUserPermission(username);
        info.setStringPermissions(perms);
        //add Roles String[Set<String> roles]
        Set<String> roles = userRepo.getUserRoles(username);
        info.setRoles(roles);
        return info;
    }

    /**
     * 登录验证
     * @param token
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        if (logger.isDebugEnabled()) {
            logger.debug("开始用户身份校验");
        }
        SNToken snToken;
        if (token instanceof SNToken) {
            snToken = (SNToken) token;
        } else {
            snToken = ObjectUtils.convert(SNToken.class, token);
            snToken.setUserInfo(userRepo.findByUsername(snToken.getUsername()));
        }

        String username = snToken.getUsername();
        User user = snToken.getUserInfo();

        if (user == null) {
            logger.warn(String.format("未找到该用户：%s", username));
            throw new UnknownAccountException("用户名或密码错误");
        }
        if (user.getLoginFailCount() >= shiroProperties.getLoginFailCount()
                && ObjectUtils.isAfterDay(user.getLastLoginDate(), new Date())) {
            logger.warn(String.format("登录失败次数过多,用户名：%s", username));
            throw new ExcessiveAttemptsException();
        }
        if ("0".equals(user.getUseState()) || user.getUseState() == null) {
            logger.warn(String.format("该用户已被禁用：%s", username));
            throw new DisabledAccountException("该用户已被禁用，请联系管理员");
        }

        return new SimpleAuthenticationInfo(username, user.getPassword(), getName());
    }

}
