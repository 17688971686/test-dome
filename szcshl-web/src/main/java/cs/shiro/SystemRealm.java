package cs.shiro;


import cs.domain.sys.User;
import cs.service.sys.UserService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.apache.log4j.Logger;

public class SystemRealm extends AuthorizingRealm {
    private static Logger logger = Logger.getLogger(SystemRealm.class);
    private static final String ALGORITHM = "MD5";

    @Autowired
    private UserService userService;

    /**
     * 提供用户信息返回权限信息
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        // TODO Auto-generated method stub
        String userName = (String) principals.getPrimaryPrincipal();
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        authorizationInfo.addStringPermissions(userService.findPermissions(userName));
        authorizationInfo.setRoles(userService.findRoles(userName));
        return authorizationInfo;
    }

    /**
     * 提供账户信息返回认证信息
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
        // 得到用户名
        String userName = (String) token.getPrincipal();
        User user = userService.findByName(userName);
        if (null == user) {
            throw new UnknownAccountException();
        }
        //停用或者离职的，不能登录系统
       if("停用".equals(user.getUseState()) || "f".equals(user.getJobState())){
            throw new DisabledAccountException();
        }
       /* 加密的情况下使用
       SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(userName,
                user.getPassword(), ByteSource.Util.bytes(user.getUserSalt()), getName());*/
        // 如果身份认证验证成功，返回一个AuthenticationInfo实现；
        return new SimpleAuthenticationInfo(userName, user.getPassword(), getName());
    }

}
