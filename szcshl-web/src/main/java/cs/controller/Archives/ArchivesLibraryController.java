package cs.controller.Archives;

import java.text.ParseException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

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

import cs.common.Constant.EnumFlowNodeGroupName;
import cs.ahelper.MudoleAnnotation;
import cs.common.ResultMsg;
import cs.domain.sys.User;
import cs.model.PageModelDto;
import cs.model.Archives.ArchivesLibraryDto;
import cs.model.sys.UserDto;
import cs.repository.odata.ODataObj;
import cs.service.archives.ArchivesLibraryService;
import cs.service.sys.UserService;

/**
 * Description: 档案借阅管理 控制层
 * author: sjy
 * Date: 2017-9-12 17:34:30
 */
@Controller
@RequestMapping(name = "档案借阅管理", path = "archivesLibrary")
@MudoleAnnotation(name = "档案借阅管理",value = "permission#archives")
public class ArchivesLibraryController {

	String ctrlName = "archives";
    @Autowired
    private ArchivesLibraryService archivesLibraryService;

    @Autowired
	private UserService userService;
    
    @RequiresAuthentication
    //@RequiresPermissions("archivesLibrary#findByOData#post")
    @RequestMapping(name = "获取数据", path = "findByOData", method = RequestMethod.POST)
    @ResponseBody
    public PageModelDto<ArchivesLibraryDto> get(HttpServletRequest request) throws ParseException {
        ODataObj odataObj = new ODataObj(request);
        PageModelDto<ArchivesLibraryDto> archivesLibraryDtos = archivesLibraryService.get(odataObj);	
        return archivesLibraryDtos;
    }
    
    @RequiresAuthentication
   // @RequiresPermissions("archivesLibrary#findByCenterList#post")
    @RequestMapping(name = "中心档案查询列表", path = "findByCenterList", method = RequestMethod.POST)
    @ResponseBody
    public PageModelDto<ArchivesLibraryDto> findByCenter(HttpServletRequest request) throws ParseException {
        ODataObj odataObj = new ODataObj(request);
        PageModelDto<ArchivesLibraryDto> archivesLibraryDtos = archivesLibraryService.findByCenterData(odataObj);	
        return archivesLibraryDtos;
    }
    
    @RequiresAuthentication
    //@RequiresPermissions("archivesLibrary#findByCityList#post")
    @RequestMapping(name = "市档案查询列表", path = "findByCityList", method = RequestMethod.POST)
    @ResponseBody
    public PageModelDto<ArchivesLibraryDto> findByCityList(HttpServletRequest request) throws ParseException {
        ODataObj odataObj = new ODataObj(request);
        PageModelDto<ArchivesLibraryDto> archivesLibraryDtos = archivesLibraryService.findByCityList(odataObj);	
        return archivesLibraryDtos;
    }
    
    @RequiresAuthentication
   // @RequiresPermissions("archivesLibrary#findByProjectList#post")
    @RequestMapping(name = "查询审批项目列表", path = "findByProjectList", method = RequestMethod.POST)
    @ResponseBody
    public PageModelDto<ArchivesLibraryDto> getProject(HttpServletRequest request) throws ParseException {
        ODataObj odataObj = new ODataObj(request);
        PageModelDto<ArchivesLibraryDto> archivesLibraryDtos = archivesLibraryService.findByProjectList(odataObj);	
        return archivesLibraryDtos;
    }
    
    @RequiresAuthentication
    //@RequiresPermissions("archivesLibrary#updateLibrary#put")
    @RequestMapping(name = "项目审批处理", path = "updateLibrary", method = RequestMethod.PUT)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void  updateLibrary(@RequestBody ArchivesLibraryDto record) {
        archivesLibraryService.updateArchivesLibrary(record);
    }
    

    @RequiresAuthentication
    //@RequiresPermissions("archivesLibrary#savaLibrary#post")
    @RequestMapping(name = "创建中心借阅记录", path = "savaLibrary", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.CREATED)
    public @ResponseBody ResultMsg post(@RequestBody ArchivesLibraryDto record) {
      return  archivesLibraryService.save(record);
    }
    
    @RequiresAuthentication
   // @RequiresPermissions("archivesLibrary#saveCity#post")
    @RequestMapping(name = "创建市借阅记录", path = "saveCity", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.CREATED)
    public @ResponseBody ResultMsg cityAdd(@RequestBody ArchivesLibraryDto record) {
      return  archivesLibraryService.saveCity(record);
    }

    @RequiresAuthentication
	@RequestMapping(name = "主键查询", path = "html/findById",method=RequestMethod.GET)
	public @ResponseBody ArchivesLibraryDto findById(@RequestParam(required = true)String id){		
		return archivesLibraryService.findById(id);
	}
	
    @RequiresAuthentication
	@RequestMapping(name = "获取归档员", path = "findByArchivesUser",method=RequestMethod.GET)
	public @ResponseBody List<UserDto>  findByArchivesUser(){		
		 List<UserDto> 	users= userService.findUserByRoleName(EnumFlowNodeGroupName.FILER.getValue());
		return users;
	}
	
    @RequiresAuthentication
    //@RequiresPermissions("archivesLibrary##delete")
    @RequestMapping(name = "删除记录", path = "", method = RequestMethod.DELETE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void delete(@RequestBody String id) {
    	archivesLibraryService.delete(id);      
    }
    
    @RequiresAuthentication
   // @RequiresPermissions("archivesLibrary##put")
    @RequestMapping(name = "更新记录", path = "", method = RequestMethod.PUT)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void put(@RequestBody ArchivesLibraryDto record) {
        archivesLibraryService.update(record);
    }
   
    // begin#html
   
    @RequiresPermissions("archivesLibrary#html/archivesLibraryAdd#get")
    @RequestMapping(name = "中心档案借阅添加页面", path = "html/archivesLibraryAdd", method = RequestMethod.GET)
    public String centerAdd() {
        return ctrlName+"/archivesLibraryAdd";
    }
    
    @RequiresPermissions("archivesLibrary#html/archivesLibraryEdit#get")
    @RequestMapping(name = "中心档案借阅编辑页面", path = "html/archivesLibraryEdit", method = RequestMethod.GET)
    public String centerEdit() {
        return ctrlName+"/archivesLibraryEdit";
    }
    
    @RequiresPermissions("archivesLibrary#html/archivesCityEdit#get")
    @RequestMapping(name = "市档案借阅添加页面", path = "html/archivesCityEdit", method = RequestMethod.GET)
    public String edit() {
        return ctrlName+"/archivesCityEdit";
    }
    
    @RequiresPermissions("archivesLibrary#html/archivesProjectList#get")
    @RequestMapping(name = "项目借阅审批列表页面", path = "html/archivesProjectList", method = RequestMethod.GET)
    public String archivesProjectRead() {
        return ctrlName+"/archivesProjectList";
    }
    
    @RequiresPermissions("archivesLibrary#html/archivesLibraryList#get")
    @RequestMapping(name = "中心档案查询列表页面", path = "html/archivesLibraryList", method = RequestMethod.GET)
    public String archivesCenterList() {
        return ctrlName+"/archivesLibraryList"; 
    } 
    
    @RequiresPermissions("archivesLibrary#html/archivesCityList#get")
    @RequestMapping(name = "市档案查询列表页面", path = "html/archivesCityList", method = RequestMethod.GET)
    public String archivesCityList() {
        return ctrlName+"/archivesCityList"; 
    }
    
    @RequiresPermissions("archivesLibrary#html/archivesLibraryDetail#get")
    @RequestMapping(name = "查看档案详细页面", path = "html/archivesLibraryDetail", method = RequestMethod.GET)
    public String archivesDetail() {
        return ctrlName+"/archivesLibraryDetail"; 
    }
    // end#html

}