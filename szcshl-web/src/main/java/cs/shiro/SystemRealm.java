package cs.shiro;


import cs.domain.sys.User;
import cs.service.sys.UserService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.apache.log4j.Logger;

public class SystemRealm extends AuthorizingRealm {
    private static Logger logger = Logger.getLogger(SystemRealm.class);
    private static final String ALGORITHM = "MD5";

    @Autowired
    private UserService userService;

    /**
     * 登录认证
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
     * 授权,只有成功通过doGetAuthenticationInfo方法的认证后才会执行。
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
        // 得到用户名
        String userName = (String) token.getPrincipal();
        SimplePrincipalCollection principals = new SimplePrincipalCollection(userName, getName());
        super.doClearCache(principals);

        User user = userService.findByName(userName);
        if (null == user) {
            throw new UnknownAccountException();
        }
       /* if("停用".equals(user.getUseState())||user.getUseState()==null){
            throw new DisabledAccountException();
        }*/
        // 如果身份认证验证成功，返回一个AuthenticationInfo实现；
        return new SimpleAuthenticationInfo(userName, user.getPassword(), getName());
    }

}
