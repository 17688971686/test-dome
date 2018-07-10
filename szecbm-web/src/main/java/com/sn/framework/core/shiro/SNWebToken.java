package com.sn.framework.core.shiro;

import org.apache.shiro.authc.UsernamePasswordToken;

/**
 * Description: 用户校验令牌（用于pc端访问的处理）
 *
 * @author: tzg
 * @date: 2017/10/23 14:47
 */
public class SNWebToken extends SNToken {

    /** 校验码 */
    private String captcha;

    public SNWebToken() {
    }

    public SNWebToken(String username, String password) {
        super(username, password);
    }

    public SNWebToken(String username, String password, String captcha) {
        super(username, password);
        this.captcha = captcha;
    }

    public SNWebToken(String username, String password, String host, String captcha) {
        super(username, password, host);
        this.captcha = captcha;
    }

    public SNWebToken(String username, String password, boolean rememberMe, String captcha) {
        super(username, password, rememberMe);
        this.captcha = captcha;
    }

    public SNWebToken(String username, String password, boolean rememberMe, String host, String captcha) {
        super(username, password, rememberMe, host);
        this.captcha = captcha;
    }

    public String getCaptcha() {
        return captcha;
    }

    public void setCaptcha(String captcha) {
        this.captcha = captcha;
    }

}