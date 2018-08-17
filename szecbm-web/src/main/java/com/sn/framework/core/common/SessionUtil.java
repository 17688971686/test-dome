package com.sn.framework.core.common;

import com.sn.framework.core.Constants;
import com.sn.framework.module.sys.domain.Organ;
import com.sn.framework.module.sys.model.UserDto;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;

/**
 * Description: session  工具类
 * @Author: tzg
 * @Date: 2017/6/29 11:48
 */
public class SessionUtil {

    /**
     * 获取当前session
     *
     * @return
     */
    public static Session getSession() {
        try {
            Subject subject = SecurityUtils.getSubject();
            return subject != null ? subject.getSession() : null;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 获取当前用户用户信息
     *
     * @return
     */
    public static UserDto getUserInfo() {
        Session session = getSession();
        if (null == session) {
            return null;
        }
        return (UserDto) session.getAttribute(Constants.USER_SESSION_KEY);
    }

    /**
     * 获取当前用户账号名
     *
     * @return
     */
    public static String getUsername() {
        try {
            Subject subject = SecurityUtils.getSubject();
            if (null == subject) {
                return null;
            }
            return (String) subject.getPrincipal();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 获取当前用户姓名
     *
     * @return
     */
    public static String getDisplayName() {
        UserDto userDto = getUserInfo();
        if (null == userDto) {
            return null;
        }
        return userDto.getDisplayName();
    }

    /**
     * 获取当前用户所属机构
     *
     * @return
     */
    public static Organ getOrgan() {
        UserDto userDto = getUserInfo();
        if (null == userDto) {
            return null;
        }
        return userDto.getOrgan();
    }

    /**
     * 获取当前用户所属机构
     *
     * @return
     */
    public static String getOrganId() {
        Organ organ = getOrgan();
        if (null == organ) {
            return null;
        }
        return organ.getOrganId();
    }

//    /**
//     * 获取系统配置信息
//     *
//     * @return
//     */
//    public static ConfigParam getConfigParam() {
//        Session session = getSession();
//        if (null == session) {
//            return null;
//        }
//        return (ConfigParam) session.getAttribute(Constants.CONFIG_SESSION_KEY);
//    }
//
//    /**
//     * 系统是否锁定（主要针对项目申报、月报、问题协调，以及业主申请项目入库的锁定）
//     * @return
//     */
//    public static boolean isLock() {
//        ConfigParam param = getConfigParam();
//        if (null == param) {
//            return true;
//        } else {
//            return param.getIsLock();
//        }
//    }

//    /**
//     * 获取当前申报年份
//     *
//     * @return
//     */
//    public static String getApplyYear() {
//        ConfigParam configParam = getConfigParam();
//        if (null == configParam) {
//            return null;
//        }
//        return configParam.getApplyYear();
//    }
}