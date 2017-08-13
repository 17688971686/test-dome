package cs.common.utils;

import cs.common.Constant;
import cs.domain.sys.User;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.SessionKey;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.subject.support.DefaultSubjectContext;
import org.apache.shiro.web.session.mgt.WebSessionKey;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Description: session  工具类
 * Author: tzg
 * Date: 2017/6/29 11:48
 */
public class SessionUtil {

    public static Session getSession() {
        Subject subject = SecurityUtils.getSubject();
        return subject != null ? subject.getSession() : null;
    }

    public static User getUserInfo() {
        Session session = getSession();
        if (null == session) return null;
            return (User) session.getAttribute(Constant.USER_SESSION_KEY);
    }

    public static String getUserId() {
        User user = getUserInfo();
        if (null == user) return null;
        return user.getId();
    }

    public static String getLoginName() {
        User user = getUserInfo();
        if (null == user) return null;
        return user.getLoginName();
    }

    public static String getDisplayName() {
        User user = getUserInfo();
        if (null == user) return null;
        return user.getDisplayName();
    }

    public static boolean checkPermissions(String... permissions ){
        Subject subject = SecurityUtils.getSubject();
        try{
            subject.checkPermissions(permissions);
            return true;
        } catch (AuthorizationException e){
            return false;
        }
    }

    public static boolean hashRole(String role){
        Subject subject = SecurityUtils.getSubject();
        return  subject.hasRole(role);
    }

    public static boolean hashAllRole(List<String> roles){
        Subject subject = SecurityUtils.getSubject();
        return  subject.hasAllRoles(roles);
    }
}