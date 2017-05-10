package cs.controller.sys;

import java.text.ParseException;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cs.model.PageModelDto;
import cs.model.sys.LogDto;
import cs.repository.odata.ODataObj;
import cs.service.sys.LogService;

@Controller
@RequestMapping(name = "日志", path = "log")
public class LogController {
	private String ctrlName = "log";
	@Autowired
	private LogService logService;

	@RequiresPermissions("log#fingByOData#post")	
	@RequestMapping(name = "获取日志数据", path = "fingByOData", method = RequestMethod.POST)
	public @ResponseBody PageModelDto<LogDto> get(HttpServletRequest request) throws ParseException {
		ODataObj odataObj = new ODataObj(request);
		PageModelDto<LogDto> logDtos = logService.get(odataObj);

		return logDtos;
	}

	// begin#html
	@RequiresPermissions("log#html/list#get")	
	@RequestMapping(name = "日志列表页面", path = "html/list", method = RequestMethod.GET)
	public String list() {
		return ctrlName + "/list";
	}
	
	//end#html

}
