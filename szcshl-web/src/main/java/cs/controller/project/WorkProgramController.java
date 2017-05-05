package cs.controller.project;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;

import cs.model.project.WorkProgramDto;
import cs.service.project.WorkProgramService;

@Controller
@RequestMapping(name = "工作方案", path = "workprogram")
public class WorkProgramController {

	private String ctrlName = "workprogram";
	
	@Autowired
	private WorkProgramService workProgramService;
	

	@RequiresPermissions("workprogram##post")
	@RequestMapping(name = "工作方案提交", path = "",method=RequestMethod.POST)	
	@ResponseStatus(value = HttpStatus.CREATED)
	public void post(@RequestBody WorkProgramDto workProgramDto) throws Exception  {
		workProgramService.save(workProgramDto);
	}
	
	@RequiresPermissions("workprogram#html/edit#get")
	@RequestMapping(name = "工作方案编辑", path = "html/edit" ,method = RequestMethod.GET)
	public String edit(){
			
		return ctrlName + "/edit";
	}
			
}
