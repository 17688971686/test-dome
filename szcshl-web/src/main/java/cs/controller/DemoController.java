package cs.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(name = "Demo", path = "demo")
public class DemoController {
	private String ctrlName = "demo";
	
	//@RequiresPermissions("demo#save#post")
	@RequestMapping(name = "上传文件", path = "save", method = RequestMethod.POST)
	public @ResponseBody String Save(HttpServletRequest request){
		return "true";
	}
	@RequestMapping(name = "删除上传文件", path = "remove", method = RequestMethod.POST)
	public @ResponseBody String remove(HttpServletRequest request){
		return "true";
	}
	// begin#html
	//@RequiresPermissions("demo#html/list#get")
	@RequestMapping(name = "DemoList页面", path = "html/list", method = RequestMethod.GET)
	public String list() {
		return ctrlName + "/list";
	}

	// end#html
}
