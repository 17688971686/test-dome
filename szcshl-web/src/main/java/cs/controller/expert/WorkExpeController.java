package cs.controller.expert;

import java.text.ParseException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import cs.common.ResultMsg;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import cs.model.expert.WorkExpeDto;
import cs.repository.odata.ODataObj;
import cs.service.expert.WorkExpeService;

@Controller
@RequestMapping(name = "工作经验", path = "workExpe")
public class WorkExpeController {
	private String ctrlName = "expert";

	@Autowired
	private WorkExpeService  workExpeService;

	@RequiresPermissions("workExpe#findByOdata#post")
	@RequestMapping(name = "查找工作经验", path = "findByOdata", method = RequestMethod.POST)
    @ResponseBody
	public List<WorkExpeDto> getWork(HttpServletRequest request) throws ParseException {
		ODataObj odataObj = new ODataObj(request);
		return workExpeService.getWork(odataObj);
	}

	@RequiresPermissions("workExpe#saveWorkExpe#post")
	@RequestMapping(name = "保存工作经验", path = "saveWorkExpe",method=RequestMethod.POST)
	@ResponseBody
	public ResultMsg saveWorkExpe(@RequestBody WorkExpeDto work)  {
	    return workExpeService.saveWorkExpe(work);
	}

	@RequiresPermissions("workExpe#deleteWork#delete")
	@RequestMapping(name = "删除工作经验", path = "deleteWork",method=RequestMethod.DELETE)	
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	public void  deleteWork(@RequestParam String ids)  {
        workExpeService.deleteWork(ids);
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
