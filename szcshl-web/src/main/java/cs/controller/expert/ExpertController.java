package cs.controller.expert;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cs.model.expert.ExpertReviewDto;
import cs.model.expert.ExpertSelConditionDto;
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

import cs.model.PageModelDto;
import cs.model.expert.ExpertDto;
import cs.repository.odata.ODataObj;
import cs.service.expert.ExpertService;

@Controller
@RequestMapping(name = "专家", path = "expert")
public class ExpertController {
	private String ctrlName = "expert";
	@Autowired
	private ExpertService expertService;
	
	@RequiresPermissions("expert#findByOData#post")	
	@RequestMapping(name = "获取专家数据", path = "findByOData", method = RequestMethod.POST)
	public @ResponseBody PageModelDto<ExpertDto> findPageByOData(HttpServletRequest request) throws ParseException {	
		ODataObj odataObj = new ODataObj(request);
		PageModelDto<ExpertDto> expertDtos = expertService.get(odataObj);
		return expertDtos;
	}	
	
	@RequiresPermissions("expert#findRepeatByOData#post")	
	@RequestMapping(name = "查询重复专家", path = "findRepeatByOData", method = RequestMethod.POST)
	public @ResponseBody PageModelDto<ExpertDto> findRepeatByOData(){
		List<ExpertDto> list = expertService.findAllRepeat();
		PageModelDto<ExpertDto> pageModelDto = new PageModelDto<ExpertDto>();				
		pageModelDto.setCount(list.size());
		pageModelDto.setValue(list);
		return pageModelDto;
	}

    @RequiresPermissions("expertReview#findReviewExpert#post")
    @RequestMapping(name = "专家抽取", path = "findReviewExpert", method = RequestMethod.POST)
    @ResponseBody
    public List<ExpertDto> findReviewExpert(@RequestBody ExpertSelConditionDto epSelCondition) {
		List<ExpertDto> pageModelDto = expertService.findExpert(epSelCondition);
        return pageModelDto;
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
	
	@RequiresPermissions("expert#findById#post")
	@RequestMapping(name = "初始化详情页面", path = "findById",method=RequestMethod.GET)	
	public @ResponseBody ExpertDto findById(@RequestParam(required = true)String id){
		return expertService.findById(id);
	}
	
	@RequiresPermissions("expert#updateAudit#post")
	@RequestMapping(name = "评审专家", path = "updateAudit",method=RequestMethod.POST)	
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	public void  updateAudit(@RequestParam String ids,String flag)  {
		expertService.updateAudit(ids,flag);	
	}
	
	@RequiresPermissions("expert##put")
	@RequestMapping(name = "更新专家", path = "",method=RequestMethod.PUT)	
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	public void  put(@RequestBody ExpertDto expertDto){		
		expertService.updateExpert(expertDto);	
	}
	
	// begin#html
	@RequiresPermissions("expert#html/repeat#get")
	@RequestMapping(name = "专家重复查询列表页面", path = "html/repeat", method = RequestMethod.GET)
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
