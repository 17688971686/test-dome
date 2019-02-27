package cs.controller.sys;

import cs.ahelper.IgnoreAnnotation;
import cs.common.ResultMsg;
import cs.common.constants.Constant;
import cs.common.utils.Tools;
import cs.common.utils.Validate;
import cs.service.sys.UserService;
import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

import static org.apache.shiro.web.filter.authc.FormAuthenticationFilter.DEFAULT_ERROR_KEY_ATTRIBUTE_NAME;

/**
 * @author
 */
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
     *
     * @param request
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping(name = "登录", path = "login")
    public String login(HttpServletRequest request, Model model) throws Exception {
        Subject subject = SecurityUtils.getSubject();
        if (subject != null && subject.isAuthenticated()) {
            return "forward:/admin/index";
        }
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
        String browerVerson = Tools.getBrowserName(request.getHeader("User-Agent").toLowerCase());
        if (Validate.isString(browerVerson) && ("ie8".equals(browerVerson) || ("ie7".equals(browerVerson)))) {
            return "home/loginLow";
        }
        return "home/login";
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
    @ResponseBody
    public ResultMsg password(@RequestParam String password) {
        userService.changePwd(password);
        return new ResultMsg(true, Constant.MsgCode.OK.getValue(), "操作成功！");
    }


    // begin#html
    @RequestMapping(name = "修改密码页面", path = "html/changePwd", method = RequestMethod.GET)
    public String list() {
        return ctrlName + "/changePwd";
    }
    // end#html

}
