package cs.controller.sharing;

import java.text.ParseException;
import java.util.Map;

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
import cs.model.sharing.SharingPlatlformDto;
import cs.model.sys.UserDto;
import cs.repository.odata.ODataObj;
import cs.service.sharing.SharingPlatlformService;

/**
 * Description: 共享平台 控制层
 * author: sjy
 * Date: 2017-7-11 10:40:17
 */
@Controller
@RequestMapping(name = "共享平台", path = "sharingPlatlform")
public class SharingPlatlformController {

	String ctrlName = "sharing";
    @Autowired
    private SharingPlatlformService sharingPlatlformService;

    @RequiresPermissions("sharingPlatlform#findByOData#post")
    @RequestMapping(name = "获取数据", path = "findByOData", method = RequestMethod.POST)
    @ResponseBody
    public PageModelDto<SharingPlatlformDto> get(HttpServletRequest request) throws ParseException {
        ODataObj odataObj = new ODataObj(request);
        PageModelDto<SharingPlatlformDto> sharingPlatlformDtos = sharingPlatlformService.get(odataObj);	
        return sharingPlatlformDtos;
    }
    @RequiresPermissions("sharingPlatlform#getOrg#post")
    @RequestMapping(name = "获取部门数据", path = "getOrg", method = RequestMethod.POST)
    public  @ResponseBody Map<String,Object> getOrg(){
    	Map<String,Object> map =sharingPlatlformService.getOrg();
    	return map;
    }
    @RequiresPermissions("sharingPlatlform#findByODataYet#post")
    @RequestMapping(name = "获取已发布共享数据", path = "findByODataYet", method = RequestMethod.POST)
    @ResponseBody
    public PageModelDto<SharingPlatlformDto> getYetData(HttpServletRequest req) throws ParseException{
    	 ODataObj odataObj = new ODataObj(req);
         PageModelDto<SharingPlatlformDto> sharingPlatlformDtos = sharingPlatlformService.get(odataObj);	
         return sharingPlatlformDtos;
    }
    
    @RequiresPermissions("sharingPlatlform#editPubStatus#put")
   	@RequestMapping(name = "修改发布状态", path = "editPubStatus",method=RequestMethod.PUT)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void  updatePublish(@RequestBody SharingPlatlformDto record){
    	sharingPlatlformService.updatePublishStatus(record);
    }
    
    @RequiresPermissions("sharingPlatlform#findUser#post")
    @RequestMapping(name = "个人发布", path = "findUser", method = RequestMethod.POST)
    @ResponseBody
    public UserDto findUser(@RequestBody UserDto user){
    	UserDto userDto = sharingPlatlformService.findUser(user.getLoginName());
    	return userDto ;
    }

    @RequiresPermissions("sharingPlatlform#addSharing#post")
    @RequestMapping(name = "创建记录", path = "addSharing", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.CREATED)
    @ResponseBody
    public SharingPlatlformDto post(@RequestBody SharingPlatlformDto record) {
        sharingPlatlformService.save(record);
        return record;
    }

    @RequiresPermissions("sharingPlatlform#html/findById#get")
	@RequestMapping(name = "主键查询", path = "html/findById",method=RequestMethod.GET)
	public @ResponseBody SharingPlatlformDto findById(@RequestParam(required = true)String id){		
		return sharingPlatlformService.findById(id);
	}
	
    @RequiresPermissions("sharingPlatlform#html/sharingDeatilById#get")
   	@RequestMapping(name = "根据ID查询详情信息", path = "html/sharingDeatilById",method=RequestMethod.GET)
    public @ResponseBody SharingPlatlformDto detailById(@RequestParam(required = true)String id){
    	return sharingPlatlformService.findById(id);
    }
    
    @RequiresPermissions("sharingPlatlform#postSharingAritle#get")
   	@RequestMapping(name = "获取上一篇信息", path = "postSharingAritle",method=RequestMethod.GET)
    public  @ResponseBody SharingPlatlformDto postSharingAritle(@RequestParam(required = true)String id){
    	return sharingPlatlformService.postSharingAritle(id);
    }
   
    @RequiresPermissions("sharingPlatlform#nextSharingArticle#get")
   	@RequestMapping(name = "获取下一篇信息", path = "nextSharingArticle",method=RequestMethod.GET)
    public @ResponseBody SharingPlatlformDto nextSharingArticle(@RequestParam(required = true)String id){
    	return sharingPlatlformService.nextSharingArticle(id);
    }
    
    @RequiresPermissions("sharingPlatlform##delete")
    @RequestMapping(name = "删除记录", path = "sharingDelete", method = RequestMethod.DELETE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void delete(@RequestBody String id) {
    	String [] ids = id.split(",");
    	if(ids.length >1){
    		sharingPlatlformService.deletes(ids);
    	}else{
    		sharingPlatlformService.delete(id);      
    	}
    }

   //@RequiresPermissions("sharingPlatlform##put")
    @RequestMapping(name = "更新记录", path = "", method = RequestMethod.PUT)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void put(@RequestBody SharingPlatlformDto record) {
        sharingPlatlformService.update(record);
    }

    // begin#html
    @RequiresPermissions("sharingPlatlform#html/list#get")
    @RequestMapping(name = "列表页面", path = "html/list", method = RequestMethod.GET)
    public String list() {
        return ctrlName+"/list"; 
    }

    @RequiresPermissions("sharingPlatlform#html/edit#get")
    @RequestMapping(name = "编辑页面", path = "html/edit", method = RequestMethod.GET)
    public String edit() {
        return ctrlName+"/edit";
    }
    
    @RequiresPermissions("sharingPlatlform#html/detail#get")
    @RequestMapping(name = "资料共享详情页", path = "html/detail", method = RequestMethod.GET)
    public String sharingDetail(){
    	return ctrlName +"/detail";
    }
    
    @RequiresPermissions("sharingPlatlform#html/yetList#get")
    @RequestMapping(name = "已发布共享数据", path = "html/yetList", method = RequestMethod.GET)
    public String sharingYet(){
    	return ctrlName +"/yetList";
    }
    
    // end#html

}