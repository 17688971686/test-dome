package cs.controller.expert;

import java.text.ParseException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import cs.ahelper.IgnoreAnnotation;
import cs.ahelper.MudoleAnnotation;
import cs.common.ResultMsg;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
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

import cs.model.expert.ExpertTypeDto;
import cs.repository.odata.ODataObj;
import cs.service.expert.ExpertTypeService;

@Controller
@IgnoreAnnotation
@RequestMapping(name="专家类型",path="expertType")
public class ExpertTypeController {
	
	@Autowired
	private ExpertTypeService expertTypeService;

    @RequiresAuthentication
	//@RequiresPermissions("expertType#getExpertType#get")
	@RequestMapping(name = "获取专家类型", path = "getExpertType",method=RequestMethod.GET)	
	public @ResponseBody List<ExpertTypeDto> getExpertTypeByExpertId(HttpServletRequest request) throws ParseException{
		ODataObj odataObj=new ODataObj(request);
		return expertTypeService.getExpertType(odataObj);
		
	}

    @RequiresAuthentication
	//@RequiresPermissions("expertType##post")
	@RequestMapping(name = "添加专家类型", path = "",method=RequestMethod.POST)	
	@ResponseBody
	public ResultMsg saveType(@RequestBody ExpertTypeDto expertTypeDto)  {
		return expertTypeService.saveExpertType(expertTypeDto);
	}

    @RequiresAuthentication
	//@RequiresPermissions("expertType#getExpertTypeById#get")
	@RequestMapping(name="通过专家类型id查找专家类型",path="getExpertTypeById",method=RequestMethod.GET)
	public @ResponseBody ExpertTypeDto getExpertTypeById(@RequestParam String expertTypeId){
		
		return expertTypeService.getExpertTypeById(expertTypeId);
	}

    @RequiresAuthentication
	//@RequiresPermissions("expertType##put")
	@RequestMapping(name="更新专家类型",path="",method=RequestMethod.PUT)
	@ResponseStatus(value=HttpStatus.NO_CONTENT)
	public void update(@RequestBody ExpertTypeDto expertTypeDto){
		expertTypeService.updateExpertType(expertTypeDto);
	}

    @RequiresAuthentication
	//@RequiresPermissions("expertType##delete")
	@RequestMapping(name="刪除专家类型",path="",method=RequestMethod.DELETE)
	@ResponseStatus(value=HttpStatus.NO_CONTENT)
	public void delete(@RequestParam String ids){
		expertTypeService.deleteExpertType(ids);
		
	}

}
