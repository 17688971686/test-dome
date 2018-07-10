package com.sn.framework.core.shiro;

import org.apache.shiro.authc.AuthenticationException;

/**
 * Description: 验证码错误的异常
 *
 * @author: tzg
 * @date: 2017/12/25 21:11
 */
public class IllegalCaptchaException extends AuthenticationException {

    public IllegalCaptchaException() {
    }

    public IllegalCaptchaException(String message) {
        super(message);
    }

    public IllegalCaptchaException(Throwable cause) {
        super(cause);
    }

    public IllegalCaptchaException(String message, Throwable cause) {
        super(message, cause);
    }
}