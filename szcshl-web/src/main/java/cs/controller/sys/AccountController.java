package cs.controller.sys;


import cs.ahelper.IgnoreAnnotation;
import cs.common.Constant;
import cs.common.Response;
import cs.domain.sys.User;
import cs.service.sys.UserService;
import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

import java.util.Date;

import static org.apache.shiro.web.filter.authc.FormAuthenticationFilter.DEFAULT_ERROR_KEY_ATTRIBUTE_NAME;

@Controller
@RequestMapping(name = "账户", path = "account")
@IgnoreAnnotation
public class AccountController {
    private static Logger logger = Logger.getLogger(AccountController.class);

    private String ctrlName = "account";
    @Autowired
    private UserService userService;

    /**
     * 用户登录
     * @param request
     * @param model
     * @param username
     * @param password
     * @param rememberMe
     * @return
     * @throws Exception
     */
    @RequestMapping(name = "登录", path = "login")
    public String login(HttpServletRequest request, Model model, String username, String password,boolean rememberMe) throws Exception {
        System.out.println("rememberMe:"+rememberMe);
        //将form中的用户名密码传入Realm 的doGetAuthenticationInfo
        UsernamePasswordToken token = new UsernamePasswordToken(username, password.toCharArray());
        token.setRememberMe(rememberMe);
        Subject currentUser = SecurityUtils.getSubject();
        String error = "";
        try {
            currentUser.login(token);
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
            SecurityUtils.getSubject().getSession().setAttribute(Constant.USER_SESSION_KEY, user); // 在session中设置用户信息
            logger.info("用户【" + username + "】登录成功！");
        } catch (UnknownAccountException ex) {// 用户名没有找到
            error = "您输入的用户名不存在！";
        } catch (IncorrectCredentialsException ex) {// 用户名密码不匹配
            error = "用户名密码不匹配 ！";
        }
        catch(ExcessiveAttemptsException e){
            error="密码错误次数已超五次，账号锁定1小时！";
        }
        catch (AuthenticationException ex) {// 其他的登录错误
            ex.printStackTrace();
            error = "其他的登录错误  ！";
        }
        // 验证是否成功登录的方法
        if (currentUser.isAuthenticated()) {
            return "redirect:/admin/index";
        } else {
            model.addAttribute("message", error);
            currentUser.logout();
            return "forward:/";
        }

        /*
        // 如果登陆失败从request中获取认证异常信息，shiroLoginFailure就是shiro异常类的全限定名
        String exception = (String) request.getAttribute(DEFAULT_ERROR_KEY_ATTRIBUTE_NAME);
        String msg = "";
        if (exception != null) {
            if (UnknownAccountException.class.getName().equals(exception)) {
                logger.info("账号不存在");
                msg = "账号或密码不正确";
            } else if (IncorrectCredentialsException.class.getName().equals(exception)) {
                logger.info("密码不正确");
                msg = "账号或密码不正确";
            } else if (AuthenticationException.class.getName().equals(exception)) {
                logger.info("账号或密码不正确");
                msg = "账号或密码不正确";
            } else if (DisabledAccountException.class.getName().equals(exception)) {
                logger.info("账号已停用");
                msg = "账号已停用";
            } else {
                logger.info(exception);
                msg = exception;
            }
        }
        model.addAttribute("msg", msg);

        return "home/login";
        */
    }

    @RequestMapping(name = "退出", path = "logout", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    public String logout() {
        Subject subject = SecurityUtils.getSubject();
        String username = (String) subject.getPrincipal();
        if (subject.isAuthenticated()) {
            subject.logout(); // session 会销毁，在SessionListener监听session销毁，清理权限缓存
            if (logger.isDebugEnabled()) {
                logger.info(String.format("退出登录,用户名:%s", username));
            }
        }
        return "forward:/";
    }

    @RequestMapping(name = "修改密码", path = "password", method = RequestMethod.PUT)
    @ResponseStatus(value = HttpStatus.OK)
    public @ResponseBody
    Response password(@RequestBody String password) {
        userService.changePwd(password);
        Response response = new Response();
        response.setIsSuccess(true);
        return response;
    }


    // begin#html
    @RequestMapping(name = "修改密码页面", path = "html/changePwd", method = RequestMethod.GET)
    public String list() {
        return ctrlName + "/changePwd";
    }

    // end#html
}
