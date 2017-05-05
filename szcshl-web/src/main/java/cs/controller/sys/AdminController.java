package cs.controller.sys;

import org.apache.log4j.Logger;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import cs.common.ICurrentUser;
import cs.common.Util;
import cs.model.sys.UserDto;
import cs.service.sys.UserService;

@Controller
@RequestMapping(name = "管理界面", path = "admin")
public class AdminController {
	private String ctrlName = "admin";
	private static Logger logger = Logger.getLogger(AdminController.class.getName());
	@Autowired
	private ICurrentUser currentUser;
	@Autowired
	private UserService userService;
	

	@RequiresPermissions("admin#index#get")
	@RequestMapping(name = "首页", path = "index")
	public String index(Model model) {

		model.addAttribute("user", currentUser.getLoginName());
		return ctrlName + "/index";
	}

	@RequiresPermissions("admin#welcome#get")
	@RequestMapping(name = "欢迎页", path = "welcome")
	public String welcome(Model model) {
		UserDto user=userService.findUserByName( currentUser.getLoginName());
		if(user!=null){
			model.addAttribute("user", user.getLoginName());
			model.addAttribute("lastLoginDate", Util.formatDate( user.getLastLoginDate()));
		}
		
		return ctrlName + "/welcome";
	}
}
