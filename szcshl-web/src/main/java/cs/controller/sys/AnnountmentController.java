package cs.controller.sys;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import cs.model.PageModelDto;
import cs.model.sys.AnnountmentDto;
import cs.repository.odata.ODataObj;
import cs.service.sys.AnnountmentService;

@Controller
@RequestMapping(name="通知公告",path="annountment")
public class AnnountmentController {
	
	private String  ctrlName="annountment";
	
	@Autowired
	private AnnountmentService annService;
	
	@RequiresPermissions("annountment#fingByOData#post")
	@RequestMapping(name="获取所有数据",path="fingByOData",method=RequestMethod.POST)
	@ResponseBody
	public PageModelDto<AnnountmentDto>  get(HttpServletRequest request) throws ParseException{
		ODataObj odataObj=new ODataObj(request);
		PageModelDto<AnnountmentDto> pageModelDto=annService.get(odataObj);
		return pageModelDto;
	}
	
	@RequiresPermissions("annountment##post")
	@RequestMapping(name="新增通知公告",path="",method=RequestMethod.POST)
	 @ResponseBody
	public AnnountmentDto create(@RequestBody AnnountmentDto annountmentDto){
		annService.createAnnountment(annountmentDto);
		return annountmentDto;
	}
	
	@RequiresPermissions("annountment#initAnOrg#get")
	@RequestMapping(name="初始化发布单位",path="initAnOrg",method=RequestMethod.GET)
	@ResponseBody
	public String inintAnOrg(){
		return annService.findAnOrg();
	}
	
	@RequiresPermissions("annountment#findAnnountmentById#get")
	@RequestMapping(name="通过ID获取通知公告",path="findAnnountmentById",method=RequestMethod.GET)
	@ResponseBody
	public AnnountmentDto findAnnoungmentById(@RequestParam String anId){
		return annService.findAnnountmentById(anId);
	}
	
	@RequiresPermissions("annountment##put")
	@RequestMapping(name="更新通知公告",path="",method=RequestMethod.PUT)
	@ResponseStatus(value=HttpStatus.NO_CONTENT)
	public void update(@RequestBody AnnountmentDto annountmentDto){
		annService.updateAnnountment(annountmentDto);
		
	}
	
	@RequiresPermissions("annountment#getAnnountment#get")
	@RequestMapping(name="获取主页上的通知公告",path="getAnnountment",method=RequestMethod.GET)
	@ResponseBody
	public List<AnnountmentDto> getAnnountment(){
		return annService.getAnnountment();
	}
	
	@RequiresPermissions("annountment#postArticle#get")
	@RequestMapping(name="获取上一篇",path="postArticle",method=RequestMethod.GET)
	@ResponseBody
	public AnnountmentDto postpostArticle(@RequestParam String anId){
		return annService.postAritle(anId);
	}
	
	@RequiresPermissions("annountment#nextArticle#get")
	@RequestMapping(name="获取下一篇",path="nextArticle",method=RequestMethod.GET)
	@ResponseBody
	public AnnountmentDto nextArticle(@RequestParam String anId){
		return annService.nextArticle(anId);
	}
	
	@RequiresPermissions("annountment##delete")
	@RequestMapping(name="更新通知公告",path="",method=RequestMethod.DELETE)
	@ResponseStatus(value=HttpStatus.NO_CONTENT)
	public void delete(@RequestBody String anId){
		annService.deleteAnnountment(anId);
		
	}
	
	
	//begin  html
	@RequiresPermissions("annountment#html/list#get")
	@RequestMapping(name="通知公告列表",path="html/list",method=RequestMethod.GET)
	public String list(){
		return ctrlName+"/list";
	}
	
	@RequiresPermissions("annountment#html/Edit#get")
	@RequestMapping(name="通知公告列表",path="html/Edit",method=RequestMethod.GET)
	public String edit(){
		return ctrlName+"/Edit";
	}

}
