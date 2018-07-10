package com.sn.framework.core.shiro;

import com.sn.framework.module.sys.domain.User;
import org.apache.shiro.authc.UsernamePasswordToken;

/**
 * Description: 用户校验令牌（公共，密码加密）
 *
 * @author: tzg
 * @date: 2017/10/23 14:47
 */
public class SNToken extends UsernamePasswordToken {

    /** 用户信息 */
    private User userInfo;

    public SNToken() { }

    public SNToken(String username, String password) {
        super(username, password);
    }

    public SNToken(String username, String password, String host) {
        super(username, password, host);
    }

    public SNToken(String username, String password, boolean rememberMe) {
        super(username, password, rememberMe);
    }

    public SNToken(String username, String password, boolean rememberMe, String host) {
        super(username, password, rememberMe, host);
    }

    public User getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(User userInfo) {
        this.userInfo = userInfo;
    }
}