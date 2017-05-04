package cs.controller;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.google.gson.JsonObject;

import cs.domain.Dict;
import cs.domain.Expert;
import cs.model.DictDto;
import cs.model.ExpertDto;
import cs.model.PageModelDto;
import cs.model.ProjectExpeDto;
import cs.model.WorkExpeDto;
import cs.repository.odata.ODataObj;
import cs.repository.repositoryImpl.DictRepo;
import cs.repository.repositoryImpl.WorkExpeRepo;
import cs.service.ExpertService;

@Controller
@RequestMapping(name = "专家", path = "expert")
public class ExpertController {
	private String ctrlName = "expert";
	@Autowired
	private ExpertService expertService;

	
	@RequiresPermissions("expert##get")	
	@RequestMapping(name = "获取专家数据", path = "", method = RequestMethod.GET)
	public @ResponseBody PageModelDto<ExpertDto> get(HttpServletRequest request) throws ParseException {
		ODataObj odataObj = new ODataObj(request);
		PageModelDto<ExpertDto> expertDtos = expertService.get(odataObj);
		return expertDtos;
	}	
	@RequiresPermissions("expert##post")
	@RequestMapping(name = "创建专家", path = "",method=RequestMethod.POST)	
	@ResponseStatus(value = HttpStatus.CREATED)
	public void  post(@RequestBody ExpertDto expert,HttpServletResponse response)  {
		  JsonObject jsonObject = new JsonObject();
		String expertID=expertService.createExpert(expert);	
		jsonObject.addProperty("expertID",expertID);
		try {
			response.getWriter().print(jsonObject.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	@RequiresPermissions("expert##delete")
	@RequestMapping(name = "删除专家", path = "",method=RequestMethod.DELETE)	
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	public void  delete(@RequestBody String id)  {		
		String[] ids=id.split(",");
		if(ids.length>1){
			expertService.deleteExpert(ids);	
		}else{
			expertService.deleteExpert(id);	
		}		
	}
	@RequiresPermissions("expert##updateAudit")
	@RequestMapping(name = "评审专家", path = "updateAudit",method=RequestMethod.POST)	
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	public void  updateAudit(@RequestParam String id,String flag)  {
		String[] ids=id.split(",");
		expertService.updateAudit(ids,flag);	
	}
	@RequiresPermissions("expert##put")
	@RequestMapping(name = "更新专家", path = "",method=RequestMethod.PUT)	
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	public void  put(@RequestBody ExpertDto expertDto){		
		expertService.updateExpert(expertDto);	
	}
	// begin#html
	@RequiresPermissions("expert#html/queryReList#get")
	@RequestMapping(name = "专家重复查询列表页面", path = "html/queryReList", method = RequestMethod.GET)
	public String list() {
		return ctrlName + "/queryReList";
	}
	@RequiresPermissions("expert#html/edit#get")
	@RequestMapping(name = "编辑专家页面", path = "html/edit", method = RequestMethod.GET)
	public String edit() {
		//userService.createUser(userDto);
		return ctrlName + "/edit";
	}
	@RequiresPermissions("expert#html/queryAllList#get")
	@RequestMapping(name = "专家综合查询页面", path = "html/queryAllList", method = RequestMethod.GET)
	public String query() {
		//userService.createUser(userDto);
		return ctrlName + "/queryAllList";
	}
	@RequiresPermissions("expert#html/audit#get")
	@RequestMapping(name = "专家审核页面", path = "html/audit", method = RequestMethod.GET)
	public String audit() {
		//userService.createUser(userDto);
		return ctrlName + "/audit";
	}
	@RequiresPermissions("expert#html/workExpe#get")
	@RequestMapping(name = "工作经验", path = "html/workExpe", method = RequestMethod.GET)
	public String workExpe() {
		//userService.createUser(userDto);
		return ctrlName + "/workExpe";
	}
	@RequiresPermissions("expert#html/projectExpe#get")
	@RequestMapping(name = "项目经验", path = "html/projectExpe", method = RequestMethod.GET)
	public String projectExpe() {
		//userService.createUser(userDto);
		return ctrlName + "/projectExpe";
	}
}
