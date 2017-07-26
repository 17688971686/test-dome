package cs.controller.sharing;

import cs.model.PageModelDto;
import cs.model.sharing.SharingPrivilegeDto;
import cs.repository.odata.ODataObj;
import cs.service.sharing.SharingPrivilegeService;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.Date;

/**
 * Description: 共享平台 控制层
 * author: sjy
 * Date: 2017-7-20 18:23:08
 */
@Controller
@RequestMapping(name = "共享平台", path = "sharingPrivilege")
public class SharingPrivilegeController {

	String ctrlName = "sharingPrivilege";
    @Autowired
    private SharingPrivilegeService sharingPrivilegeService;

    @RequiresPermissions("sharingPrivilege#findByOData#post")
    @RequestMapping(name = "获取数据", path = "findByOData", method = RequestMethod.POST)
    @ResponseBody
    public PageModelDto<SharingPrivilegeDto> get(HttpServletRequest request) throws ParseException {
        ODataObj odataObj = new ODataObj(request);
        PageModelDto<SharingPrivilegeDto> sharingPrivilegeDtos = sharingPrivilegeService.get(odataObj);	
        return sharingPrivilegeDtos;
    }

    @RequiresPermissions("sharingPrivilege##post")
    @RequestMapping(name = "创建记录", path = "", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.CREATED)
    public void post(@RequestBody SharingPrivilegeDto record) {
        sharingPrivilegeService.save(record);
    }

	@RequestMapping(name = "主键查询", path = "html/findById",method=RequestMethod.GET)
	public @ResponseBody SharingPrivilegeDto findById(@RequestParam(required = true)String id){		
		return sharingPrivilegeService.findById(id);
	}
	
    @RequiresPermissions("sharingPrivilege##delete")
    @RequestMapping(name = "删除记录", path = "", method = RequestMethod.DELETE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void delete(@RequestBody String id) {
    	sharingPrivilegeService.delete(id);      
    }

    @RequiresPermissions("sharingPrivilege##put")
    @RequestMapping(name = "更新记录", path = "", method = RequestMethod.PUT)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void put(@RequestBody SharingPrivilegeDto record) {
        sharingPrivilegeService.update(record);
    }

    // begin#html
    @RequiresPermissions("sharingPrivilege#html/list#get")
    @RequestMapping(name = "列表页面", path = "html/list", method = RequestMethod.GET)
    public String list() {
        return ctrlName+"/list"; 
    }

    @RequiresPermissions("sharingPrivilege#html/edit#get")
    @RequestMapping(name = "编辑页面", path = "html/edit", method = RequestMethod.GET)
    public String edit() {
        return ctrlName+"/edit";
    }
    // end#html

}