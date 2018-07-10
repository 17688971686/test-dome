package com.sn.framework.core.shiro;

import com.octo.captcha.service.CaptchaServiceException;
import com.sn.framework.common.RSAKey;
import com.sn.framework.common.RSAUtils;
import com.sn.framework.core.boot.properties.ShiroProperties;
import com.sn.framework.core.common.SNKit;
import com.sn.framework.core.web.Result;
import com.sn.framework.module.sys.domain.User;
import com.sn.framework.module.sys.service.IUserInfoService;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.GeneralSecurityException;

/**
 * Description: 自定义登录表单过滤器
 * @Author: tzg
 * @Date: 2017/6/16 10:02
 */
public class SNFormAuthenticationFilter extends FormAuthenticationFilter {
    private static final Logger logger = LoggerFactory.getLogger(SNFormAuthenticationFilter.class);

    protected static IUserInfoService userService;
    /**
     * shiro基础配置
     */
    protected static ShiroProperties shiroProperties;

    @Component
    public static class UserUtils {

        @Autowired
        private IUserInfoService userInfoService;
        @Autowired
        private ShiroProperties properties;

        @PostConstruct
        public void init() {
            userService = this.userInfoService;
            shiroProperties = this.properties;
        }
    }

    /**
     * 验证码的参数名
     */
    private String captchaParam = "captcha";

    public String getCaptchaParam() {
        return captchaParam;
    }

    public void setCaptchaParam(String captchaParam) {
        this.captchaParam = captchaParam;
    }

    /**
     * 创建用户登录凭证
     *
     * @param request
     * @param response
     * @return
     */
    @Override
    protected AuthenticationToken createToken(ServletRequest request, ServletResponse response) {
        String username = getUsername(request);
        String password = getPassword(request);
        boolean rememberMe = isRememberMe(request);
        String host = getHost(request);
        /** 校验码 */
        String captcha = WebUtils.getCleanParam(request, getCaptchaParam());
        return new SNWebToken(username, password, rememberMe, host, captcha == null ? null : captcha.toUpperCase());
    }

    /**
     * 执行PC端登录操作
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @Override
    protected boolean executeLogin(ServletRequest request, ServletResponse response) throws Exception {
        SNWebToken token = (SNWebToken) this.createToken(request, response);
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        if (shiroProperties.isLoginCaptcha()) {
            Boolean isCaptchaCorrect = false;
            try {
                isCaptchaCorrect = JCaptchaUtil.getInstance().validateResponseForID(httpServletRequest.getSession().getId(), token.getCaptcha());
            } catch (CaptchaServiceException e) {
                e.printStackTrace();
            }
            if (!isCaptchaCorrect) {
                // 拒绝访问，不再校验账号和密码
                return this.onLoginFailure(token, new IllegalCaptchaException("验证码错误"), request, response);
            }
        }

        // 密码解密（RSA 加密算法）
        RSAKey resKey = (RSAKey) ((HttpServletRequest) request).getSession().getAttribute("resKey");
        if (null != resKey) {
            try {
                String pwd = new String(RSAUtils.decryptByPrivateKey(String.valueOf(token.getPassword()), resKey.getPrivateKey()));
                token.setPassword(pwd.toCharArray());
            } catch (GeneralSecurityException e) {
                return this.onLoginFailure(token, new UnknownAccountException("密码解密失败", e.getCause()), request, response);
            }
        } else {
            return this.onLoginFailure(token, new UnknownAccountException("无法获取秘钥"), request, response);
        }
//        if (!SNKit.checkPwd(String.valueOf(token.getPassword()))) {
//            return this.onLoginFailure(token, new UnknownAccountException("不符合规则的密码"), request, response);
//        }

        return this.executeLogin(token, request, response);
    }

    /**
     * 执行登录操作
     *
     * @param token
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    protected boolean executeLogin(SNToken token, ServletRequest request, ServletResponse response) throws Exception {
        if (token == null) {
            logger.warn("无法获取用户凭证");
            return this.onLoginFailure(null, new UnknownAccountException("无法获取用户凭证"), request, response);
        } else {
            String username = token.getUsername();
            User user;
            if (token.getUserInfo() != null) {
                user = token.getUserInfo();
            } else {
                user = userService.findByUsername(username);
                if (null == user) {
                    return this.onLoginFailure(token, new UnknownAccountException("账号或密码不正确"), request, response);
                }
                token.setUserInfo(user);
            }

            try {
                Subject subject = this.getSubject(request, response);
                subject.login(token);
                SNKit.addSessionAttribute(subject, user);
                userService.loginSuccess(username);

                return this.onLoginSuccess(token, subject, request, response);
            } catch (AuthenticationException var5) {
                logger.warn(String.format("用户【%s】登录失败！", username));

                if (!(var5 instanceof DisabledAccountException)) {
                    userService.loginFailure(username);
                }

                return this.onLoginFailure(token, var5, request, response);
            }
        }
    }

    /**
     * 重写用户登录成功的处理（增加ajax的实现）
     *
     * @param token
     * @param subject
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @Override
    protected boolean onLoginSuccess(AuthenticationToken token, Subject subject, ServletRequest
            request, ServletResponse response) throws Exception {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        // 监测是否是ajax请求
        if (SNKit.isJsonContent(httpServletRequest)) {
            SNKit.printJsonMsg(httpServletResponse, HttpStatus.OK, new Result("登录成功"));
        } else {
            String redirectUrl = getSuccessUrl();
            // 清除记录的前一个请求
            WebUtils.getAndClearSavedRequest(request);
            WebUtils.redirectToSavedRequest(request, response, redirectUrl);
        }
        //we handled the success redirect directly, prevent the chain from continuing:
        return false;
    }

    /**
     * 重写用户失败的处理（增加ajax的实现）
     *
     * @param token
     * @param e
     * @param request
     * @param response
     * @return
     */
    @Override
    protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException e, ServletRequest
            request, ServletResponse response) {
        if (logger.isDebugEnabled()) {
            logger.debug("用户身份异常", e);
        }
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        // 监测是否是ajax请求
        if (SNKit.isJsonContent(httpServletRequest)) {
            String msg = SNKit.extractErrorMsg(e.getClass().getName());
            SNKit.printJsonMsg(httpServletResponse, HttpStatus.BAD_REQUEST, new Result(Result.Status.ERROR, msg));
        } else {
            setFailureAttribute(request, e);
        }
        //login failed, let request continue back to the login page:
        return true;
    }

    /**
     * 检查请求是否要拒绝访问
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        // 判断是否登录请求
        if (isLoginRequest(request, response)) {
            // 判断是否提交登录的请求
            if (isLoginSubmission(request, response)) {
                if (logger.isTraceEnabled()) {
                    logger.trace("发现登录提交请求，尝试执行登录操作。");
                }
                return executeLogin(request, response);
            } else {
                if (logger.isTraceEnabled()) {
                    logger.trace("进入登录页。");
                }
                //allow them to see the login page ;)
                return true;
            }
        } else {
            if (logger.isTraceEnabled()) {
                logger.trace("没有权限访问，系统将跳转到【{}】", getLoginUrl());
            }

            // 监测是否是ajax请求
            if (SNKit.isJsonContent(httpServletRequest)) {
                SNKit.printJsonMsg((HttpServletResponse) response, HttpStatus.REQUEST_TIMEOUT, new Result(Result.Status.TIMEOUT, "登录超时"));
            } else {
                saveRequestAndRedirectToLogin(request, response);
            }
            return false;
        }
    }

}