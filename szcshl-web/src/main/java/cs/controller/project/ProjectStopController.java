package cs.controller.project;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cs.model.project.ProjectStopDto;
import cs.service.project.ProjectStopService;

@Controller
@RequestMapping(name="项目暂停",path="projectStop")
public class ProjectStopController {
	
	private String ctrlName="projectStop";
	@Autowired
	private ProjectStopService projectStopService;
	
    @RequiresPermissions("ProjectStop#createpStop#post")
    @RequestMapping(name="项目暂停",path="projectStop",method=RequestMethod.POST)
    @ResponseBody
    public void projectStop(@RequestParam String signid){
    	projectStopService.addProjectStop(signid);
    	
    }
    
    @RequiresPermissions("ProjectStop#projectStart#post")
    @RequestMapping(name="项目启动",path="projectStart",method=RequestMethod.POST)
    @ResponseBody
    public void projectStart(@RequestParam String signid){
    	projectStopService.projectStart(signid);
    }
	
}
