package cs.controller.project;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;

import cs.model.project.DispatchDocDto;
import cs.service.project.DispatchDocService;

@Controller
@RequestMapping(name = "发文", path = "dispatch")
public class DispatchDocController {
	
	private  String ctrlName = "dispatch";
	
	@Autowired
	private DispatchDocService dispatchDocService;
	
	@RequiresPermissions("dispatch##post")
	@RequestMapping(name = "发文提交", path = "",method=RequestMethod.POST)	
	@ResponseStatus(value = HttpStatus.CREATED)
	public void post(@RequestBody DispatchDocDto dispatchDocDto) throws Exception  {
		dispatchDocService.save(dispatchDocDto);
	}
	
	@RequiresPermissions("dispatch#html/edit#get")
	@RequestMapping(name = "发文编辑", path = "html/edit", method = RequestMethod.GET)
	public String edit() {		
				return ctrlName + "/edit";
	}
}
