package cs.controller;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import cs.common.Response;
import cs.model.UserDto;
import cs.service.UserService;

@Controller
@RequestMapping(name = "账户", path = "account")
public class AccountController {
	private String ctrlName = "account";
	@Autowired
	private UserService userService;
	
	@RequestMapping(name = "登录", path = "login", method = RequestMethod.POST)
	@ResponseStatus(value = HttpStatus.CREATED)
	public @ResponseBody Response post(@RequestBody UserDto userDto) {
		Response loginResult= userService.Login(userDto.getLoginName(), userDto.getPassword());
		
		return loginResult;
	}
	@RequestMapping(name = "退出", path = "logout", method = RequestMethod.GET)
	@ResponseStatus(value = HttpStatus.OK)
	public   String logout() {
		userService.logout();
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
