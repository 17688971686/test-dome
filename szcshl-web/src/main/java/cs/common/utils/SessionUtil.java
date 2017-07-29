package cs.common.utils;

import cs.common.Constant;
import cs.domain.sys.User;
import cs.model.sys.UserDto;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;

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
}