package cs.controller;

import java.text.ParseException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import cs.model.ProjectExpeDto;
import cs.repository.odata.ODataObj;
import cs.repository.repositoryImpl.ProjectExpeRepoImpl;
import cs.service.ExpertService;
import cs.service.ProjectExpeService;

@Controller
@RequestMapping(name = "项目经验", path = "projectExpe")
public class ProjectExpeController {
	private String ctrlName = "expert";
	@Autowired
	private ProjectExpeService projectExpeService;

	@RequiresPermissions("expert##getProject")	
	@RequestMapping(name = "查找项目经验", path = "getProject", method = RequestMethod.GET)
	public @ResponseBody List<ProjectExpeDto> getProject(HttpServletRequest request) throws ParseException {
		ODataObj odataObj = new ODataObj(request);
		List<ProjectExpeDto> projectDtos = projectExpeService.getProject(odataObj);
		return projectDtos;
	}	
	@RequiresPermissions("expert##post")
	@RequestMapping(name = "添加项目经验", path = "projectExpe",method=RequestMethod.POST)	
	@ResponseStatus(value = HttpStatus.CREATED)
	public void  createProject(@RequestBody ProjectExpeDto project)  {
		projectExpeService.createProject(project);		
	}
	@RequiresPermissions("expert##delete")
	@RequestMapping(name = "删除项目经验", path = "deleteProject",method=RequestMethod.DELETE)	
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	public void  deleteProject(@RequestBody String id)  {		
		String[] ids=id.split(",");
		if(ids.length>1){
			projectExpeService.deleteProject(ids);	
		}else{
			projectExpeService.deleteProject(ids[0]);	
		}		
	}
	@RequiresPermissions("expert##put")
	@RequestMapping(name = "更新项目经验", path = "updateProject",method=RequestMethod.PUT)	
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	public void  updateProject(@RequestBody ProjectExpeDto projectExpeDto){		
		projectExpeService.updateProject(projectExpeDto);	
	}
}
