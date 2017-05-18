package cs.controller.project;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import cs.model.project.WorkProgramDto;
import cs.service.project.WorkProgramService;

@Controller
@RequestMapping(name = "工作方案", path = "workprogram")
public class WorkProgramController {

	private String ctrlName = "workprogram";
	
	@Autowired
	private WorkProgramService workProgramService;
	

	@RequiresPermissions("workprogram#addWork#post")
	@RequestMapping(name = "工作方案提交", path = "addWork",method=RequestMethod.POST)	
	@ResponseStatus(value = HttpStatus.CREATED)
	@ResponseBody
	public WorkProgramDto post(@RequestBody  WorkProgramDto workProgramDto) throws Exception  {
		workProgramService.save(workProgramDto);
		return workProgramDto;
	}
	
	@RequiresPermissions("workprogram#initWorkBySignId#get")
	@RequestMapping(name = "工作方案编辑", path = "html/initWorkBySignId",method=RequestMethod.GET)	
	public @ResponseBody WorkProgramDto initWorkBySignId(@RequestParam(required = true) String signId,String isMain){
		WorkProgramDto workDto =workProgramService.initWorkBySignId(signId,isMain);
		return workDto;
	}
	
	@RequiresPermissions("workprogram#html/edit#get")
	@RequestMapping(name = "工作方案编辑", path = "html/edit" ,method = RequestMethod.GET)
	public String edit(){
			
		return ctrlName + "/edit";
	}
			
}
