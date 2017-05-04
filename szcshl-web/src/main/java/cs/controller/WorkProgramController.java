package cs.controller;

import java.util.UUID;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import cs.common.Constant;
import cs.common.utils.Validate;
import cs.model.SignDto;
import cs.model.WorkProgramDto;
import cs.service.WorkProgramService;

@Controller
@RequestMapping(name = "评审方案", path = "workprogram")
public class WorkProgramController {

	private String ctrlName = "workprogram";
	
	@Autowired
	private WorkProgramService workProgramService;
	
	
	@RequiresPermissions("workprogram#html/edit#get")
	@RequestMapping(name = "单位编辑页面", path = "html/edit" ,method = RequestMethod.GET)
	public String edit(){
			
		return ctrlName + "/edit";
	}
	
	@RequiresPermissions("workprogram##post")
	@RequestMapping(name = "新增工作方案", path = "",method=RequestMethod.POST)	
	@ResponseStatus(value = HttpStatus.CREATED)
	public void post(@RequestBody WorkProgramDto workProgramDto) throws Exception  {
		workProgramService.createWork(workProgramDto);
	}		
}
