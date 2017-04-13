package cs.controller;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {
	private String ctrlName = "home";
	private static Logger logger = Logger.getLogger(HomeController.class.getName());

	@RequestMapping(name = "登录", path = "/")
	public String login() {

		return this.ctrlName + "/login";
	}

}
