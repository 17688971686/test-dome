package cs.controller.sys;


import cs.common.Response;
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

@Controller
@RequestMapping(name = "账户", path = "account")
public class AccountController {
	private static Logger logger = Logger.getLogger(AccountController.class);

	private String ctrlName = "account";
	@Autowired
	private UserService userService;

	@RequestMapping(name = "登录", path = "login")
	public String login(HttpServletRequest request, Model model) throws Exception {
		logger.debug("AccountController.login()");
		// 登录失败从request中获取shiro处理的异常信息。
		// shiroLoginFailure:就是shiro异常类的全类名.
		String exception = (String) request.getAttribute("shiroLoginFailure");
		logger.debug("exception=" + exception);
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
			} else if(DisabledAccountException.class.getName().equals(exception)){
				logger.info("账号已停用");
				msg = "账号已停用";
			} else {
				logger.info(exception);
				msg = exception;
			}
		}
		if(request.getParameter("forceLogout") != null) {
			msg = "您已经被管理员强制退出，请重新登录";
		}
		model.addAttribute("msg", msg);
		// 此方法不处理登录成功,由shiro进行处理
		return "home/login";
	}

	@RequestMapping(name = "退出", path = "logout", method = RequestMethod.GET)
	@ResponseStatus(value = HttpStatus.OK)
	public  String logout() {
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
	public  @ResponseBody Response password(@RequestBody String password) {
		userService.changePwd(password);
		Response response=new Response();
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
