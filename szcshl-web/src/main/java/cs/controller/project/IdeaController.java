package cs.controller.project;

import cs.ahelper.IgnoreAnnotation;
import cs.common.ResultMsg;
import cs.model.project.IdeaDto;
import cs.service.project.IdeaService;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping(name="意见", path = "idea")
@IgnoreAnnotation
public class IdeaController {
	
	@Autowired
	private IdeaService ideaService;

	@RequiresAuthentication
	//@RequiresPermissions("idea#findMyIdea#post")
	@RequestMapping(name="获取常用意见" ,path="findMyIdea",method=RequestMethod.POST)
	public @ResponseBody List<IdeaDto> findMyIdea(HttpServletRequest  request){
		return ideaService.findMyIdea();
	}


	@RequiresAuthentication
	//@RequiresPermissions("idea##post")
	@RequestMapping(name="添加意见",path="",method=RequestMethod.POST)
	@ResponseBody
	public ResultMsg post(@RequestBody IdeaDto[] ideaDtos){
		return ideaService.bathSave(ideaDtos);
	}

	@RequiresAuthentication
	//@RequiresPermissions("idea##delete")
	@RequestMapping(name="删除意见", path="", method=RequestMethod.DELETE)
	@ResponseStatus(value=HttpStatus.NO_CONTENT)
	public void delete(@RequestParam(required=true) String ideas){
			ideaService.deleteIdea(ideas);
	}

}
