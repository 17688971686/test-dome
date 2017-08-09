package cs.shiro.filter;

import cs.common.Constant;
import cs.common.utils.Validate;
import cs.domain.sys.User;
import cs.service.sys.UserService;
import org.apache.log4j.Logger;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * Description: 自定义表单过滤器
 * Author: tzg
 * Date: 2017/6/28 14:08
 */
public class MyFormAuthenticationFilter extends FormAuthenticationFilter {
    private static Logger logger = Logger.getLogger(MyFormAuthenticationFilter.class);

    @Autowired
    private UserService userService;

    /**
     * 登录URL
     */
    private String loginUrl;
    /**
     * 登录成功跳转URL
     */
    private String successUrl;

    protected boolean executeLogin(ServletRequest request,ServletResponse response) throws Exception {
        AuthenticationToken token = createToken(request, response);
        if (token == null) {
            String msg = "create AuthenticationToken error";
            throw new IllegalStateException(msg);
        }
        String username = (String) token.getPrincipal();
        User user = userService.findByName(username);
        if(user != null){
            //用户停用，未激活等判断
            /*if(isDisabled(user)){
                return onLoginFailure(token,failureUrl,adminLogin,new DisabledException(),request, response);
            }
            if(!isActive(user)){
                return onLoginFailure(token,failureUrl,adminLogin,new InactiveException(),request, response);
            }*/
        }
        try {
            Subject subject = getSubject(request, response);
            subject.login(token);
            return onLoginSuccess(token,subject, request, response);
        } catch (AuthenticationException e) {
            return onLoginFailure(token, e, request, response);
        }
    }

    protected void issueSuccessRedirect(ServletRequest request, ServletResponse response)
            throws Exception {
        // 清除记录的前一个请求
        WebUtils.getAndClearSavedRequest(request);
        WebUtils.issueRedirect(request, response, successUrl, null, true);
    }

    @Transactional
    @Override
    protected boolean onLoginSuccess(AuthenticationToken token, Subject subject, ServletRequest request, ServletResponse response) throws Exception {
        String username = (String) subject.getPrincipal();
        User user = userService.findByName(username);
        Date now = new Date();
        String loginIP = request.getRemoteAddr();
        user.setUserIP(loginIP);
        user.setLastLogin(now);
        user.setLastLoginDate(now);
        user.setLoginFailCount(0);
        userService.saveUser(user);

        //保存session
        user.setPassword(null);
        user.setUserSalt(null);
        subject.getSession().setAttribute(Constant.USER_SESSION_KEY, user); // 在session中设置用户信息
        logger.info("用户【" + username + "】登录成功！");

        return super.onLoginSuccess(token, subject, request, response);
    }

    @Override
    protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException e, ServletRequest request, ServletResponse response) {
        String username = (String) token.getPrincipal();
        User user = userService.findByName(username);
        if (null != user) {
            user.setLoginFailCount(user.getLoginFailCount() + 1);
            user.setLastLoginDate(new Date());
            userService.saveUser(user);
        }
        //自己处理用户登录错误跳转
        logger.warn("用户【" + username + "】登录失败！");
        String className = e.getClass().getName();
        request.setAttribute(getFailureKeyAttribute(), className);
        if(Validate.isString(loginUrl)){
            try {
                request.getRequestDispatcher(loginUrl).forward(request, response);
            }  catch (Exception e1) {

            }
        }
        return true;
    }

    @Override
    protected boolean onAccessDenied(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        if (!"XMLHttpRequest".equalsIgnoreCase(request.getHeader("X-Requested-With"))) {// 不是ajax请求
            //如果被踢出了，直接退出，重定向到踢出后的地址
            return super.onAccessDenied(servletRequest, servletResponse);
        }
        logger.warn("用户登录超时！");
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        return false;
    }

    @Override
    public String getLoginUrl() {
        return loginUrl;
    }

    @Override
    public void setLoginUrl(String loginUrl) {
        this.loginUrl = loginUrl;
    }

    @Override
    public String getSuccessUrl() {
        return successUrl;
    }

    @Override
    public void setSuccessUrl(String successUrl) {
        this.successUrl = successUrl;
    }
}