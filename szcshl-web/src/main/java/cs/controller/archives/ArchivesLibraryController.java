package cs.controller.archives;

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
import cs.model.PageModelDto;
import cs.model.archives.ArchivesLibraryDto;
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
    //@RequiresPermissions("archivesLibrary#savaLibrary#post")
    @RequestMapping(name = "保存记录", path = "savaLibrary", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.CREATED)
    public @ResponseBody ResultMsg post(@RequestBody ArchivesLibraryDto record) {
      return  archivesLibraryService.save(record);
    }

    @RequiresAuthentication
    @RequestMapping(name = "发起流程", path = "startFlow", method = RequestMethod.POST)
    @ResponseBody
    public ResultMsg startFlow(@RequestParam(required = true) String id) {
        return archivesLibraryService.startFlow(id);
    }


    @RequiresAuthentication
	@RequestMapping(name = "主键查询", path = "findById",method=RequestMethod.POST)
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
    @RequestMapping(name = "项目档案借阅", path = "html/archivesLibraryAdd", method = RequestMethod.GET)
    public String centerAdd() {
        return ctrlName+"/archivesLibraryAdd";
    }

    @RequiresPermissions("archivesLibrary#html/archivesLibraryList#get")
    @RequestMapping(name = "档案借阅查询", path = "html/archivesLibraryList", method = RequestMethod.GET)
    public String archivesCenterList() {
        return ctrlName+"/archivesLibraryList"; 
    } 

    // end#html

}