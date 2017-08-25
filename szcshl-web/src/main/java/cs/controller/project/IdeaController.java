package cs.controller.project;

import java.text.ParseException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import cs.common.ResultMsg;
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

import cs.model.project.IdeaDto;
import cs.repository.odata.ODataObj;
import cs.service.project.IdeaService;

@Controller
@RequestMapping(name="意见", path = "idea")
public class IdeaController {
	
	@Autowired
	private IdeaService ideaService;
	
	@RequiresPermissions("idea#findMyIdea#post")
	@RequestMapping(name="获取常用意见" ,path="findMyIdea",method=RequestMethod.POST)
	public @ResponseBody List<IdeaDto> findMyIdea(HttpServletRequest  request){
		return ideaService.findMyIdea();
	}
	
	
	@RequiresPermissions("idea##post")
	@RequestMapping(name="添加意见",path="",method=RequestMethod.POST)
	@ResponseBody
	public ResultMsg post(@RequestBody IdeaDto[] ideaDtos){
		return ideaService.bathSave(ideaDtos);
	}
	
	@RequiresPermissions("idea##delete")
	@RequestMapping(name="删除意见", path="", method=RequestMethod.DELETE)
	@ResponseStatus(value=HttpStatus.NO_CONTENT)
	public void delete(@RequestParam(required=true) String ideas){
			ideaService.deleteIdea(ideas);
	}

}
