package cs.controller.expert;

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

import cs.domain.expert.ExpertGlory;
import cs.model.expert.ExpertGloryDto;
import cs.model.expert.WorkExpeDto;
import cs.repository.odata.ODataObj;
import cs.service.expert.ExpertGloryService;
import cs.service.expert.WorkExpeService;

@Controller
@RequestMapping(name = "工作经验", path = "expertGlory")
public class ExpertGloryController {
	private String ctrlName = "expert";
	@Autowired
	private ExpertGloryService  expertGloryService;
	@RequiresPermissions("expert##getWork")	
	@RequestMapping(name = "查找专家聘书", path = "", method = RequestMethod.GET)
	public @ResponseBody List<ExpertGloryDto> getGlory(HttpServletRequest request) throws ParseException {
		ODataObj odataObj = new ODataObj(request);
		List<ExpertGloryDto> expertGloryDto = expertGloryService.getGlory(odataObj);
		return expertGloryDto;
	}	
	@RequiresPermissions("expert##put")
	@RequestMapping(name = "添加专家聘书", path = "",method=RequestMethod.PUT)	
	@ResponseStatus(value = HttpStatus.CREATED)
	public void  createGlory(@RequestBody ExpertGloryDto expertGloryDto)  {
		expertGloryService.createGlory(expertGloryDto);		
	}
	@RequiresPermissions("expert##delete")
	@RequestMapping(name = "删除专家聘书", path = "deleteGlory",method=RequestMethod.DELETE)	
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	public void  deleteWork(@RequestBody String id)  {		
		String[] ids=id.split(",");
		if(ids.length>1){
			expertGloryService.deleteGlory(ids);	
		}else{
			expertGloryService.deleteGlory(ids[0]);	
		}		
	}
	@RequiresPermissions("expert##put")
	@RequestMapping(name = "更新专家聘书", path = "updateGlory",method=RequestMethod.PUT)	
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	public void  updateGlory(@RequestBody ExpertGloryDto expertGloryDto){		
		expertGloryService.updateGlory(expertGloryDto);	
	}
	@RequiresPermissions("expert#html/expertGlory#get")
	@RequestMapping(name = "专家聘书页面", path = "html/expertGlory", method = RequestMethod.GET)
	public String workExpe() {
		//userService.createUser(userDto);
		return ctrlName + "/expertGlory";
	}
}
