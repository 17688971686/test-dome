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

import cs.domain.WorkExpe;
import cs.model.WorkExpeDto;
import cs.repository.odata.ODataObj;
import cs.service.ExpertService;
import cs.service.WorkExpeService;

@Controller
@RequestMapping(name = "工作经验", path = "workExpe")
public class WorkExpeController {
	private String ctrlName = "expert";
	@Autowired
	private WorkExpeService  workExpeService;
	@RequiresPermissions("expert##getWork")	
	@RequestMapping(name = "查找工作经验", path = "getWork", method = RequestMethod.GET)
	public @ResponseBody List<WorkExpeDto> getWork(HttpServletRequest request) throws ParseException {
		ODataObj odataObj = new ODataObj(request);
		List<WorkExpeDto> workDtos = workExpeService.getWork(odataObj);
		return workDtos;
	}	
	@RequiresPermissions("expert##post")
	@RequestMapping(name = "添加工作经验", path = "workExpe",method=RequestMethod.POST)	
	@ResponseStatus(value = HttpStatus.CREATED)
	public void  createWork(@RequestBody WorkExpeDto work)  {
		workExpeService.createWork(work);		
	}
	@RequiresPermissions("expert##delete")
	@RequestMapping(name = "删除工作经验", path = "deleteWork",method=RequestMethod.DELETE)	
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	public void  deleteWork(@RequestBody String id)  {		
		String[] ids=id.split(",");
		if(ids.length>1){
			workExpeService.deleteWork(ids);	
		}else{
			workExpeService.deleteWork(ids[0]);	
		}		
	}
	@RequiresPermissions("expert##put")
	@RequestMapping(name = "更新工作经验", path = "updateWork",method=RequestMethod.PUT)	
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	public void  updateWork(@RequestBody WorkExpeDto workExpeDto){		
		workExpeService.updateWork(workExpeDto);	
	}
	@RequiresPermissions("expert#html/workExpe#get")
	@RequestMapping(name = "工作页面", path = "html/workExpe", method = RequestMethod.GET)
	public String workExpe() {
		//userService.createUser(userDto);
		return ctrlName + "/workExpe";
	}
}
