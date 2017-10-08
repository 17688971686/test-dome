package cs.controller.external;

import java.text.ParseException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import cs.ahelper.IgnoreAnnotation;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import cs.domain.external.OfficeUser;
import cs.model.PageModelDto;
import cs.model.external.DeptDto;
import cs.model.external.OfficeUserDto;
import cs.repository.odata.ODataObj;
import cs.repository.repositoryImpl.external.DeptRepo;
import cs.service.external.DeptService;

@Controller
@RequestMapping(name = "办事处", path = "dept")
@IgnoreAnnotation
public class DeptController {

	String ctrlName = "dept";
	
    @Autowired
    private DeptService deptService;

    @RequiresAuthentication
    //@RequiresPermissions("dept#fingByOData#post")
    @RequestMapping(name = "获取数据", path = "fingByOData", method = RequestMethod.POST)
    @ResponseBody
    public PageModelDto<DeptDto> get(HttpServletRequest request) throws ParseException {
        ODataObj odataObj = new ODataObj(request);
        PageModelDto<DeptDto> deptDtos = deptService.get(odataObj);	
        return deptDtos;
    }

    @RequiresAuthentication
    //@RequiresPermissions("dept#getDeptOfficeUsers#post")
   	@RequestMapping(name = "获取用户所在办事处", path = "getDeptOfficeUsers", method = RequestMethod.POST)
    public @ResponseBody PageModelDto<OfficeUserDto> getDeptOfficeUser(@RequestParam String deptId){
    	return deptService.getDeptOfficeUsers(deptId);
    }

    @RequiresAuthentication
    //@RequiresPermissions("org#addOfficeUser#post")
	@RequestMapping(name = "添加人员到办事处", path = "addOfficeUser", method = RequestMethod.POST)
	@ResponseStatus(value = HttpStatus.CREATED)
    public void  postOfficeUserToDept(@RequestParam String deptId,@RequestParam String officeId){
		deptService.addOfficeUserToDept(deptId,officeId);
    }

    @RequiresAuthentication
    //@RequiresPermissions("dept#deleteOfficeUsers#delete")
	@RequestMapping(name = "从办事处移除用户", path = "deleteOfficeUsers", method = RequestMethod.DELETE)
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	public void deleteOfficeUserFromDept(@RequestParam String deptId,@RequestParam String officeId){
		String [] ids =officeId.split(",");
		if(ids.length >1){
			deptService.removeOfficeUserDepts(ids,deptId);
		}else{
			deptService.removeOfficeUserDept(officeId,deptId);
		}
	}

    @RequiresAuthentication
    //@RequiresPermissions("dept#NotInoDeptfficeUsers#post")
	@RequestMapping(name = "获取非用户所在办事处", path = "NotInoDeptfficeUsers", method = RequestMethod.POST)
    public @ResponseBody PageModelDto<OfficeUserDto> getNotInofficeUsers(@RequestParam String deptId,HttpServletRequest request) throws ParseException{
    	ODataObj odataObj = new ODataObj(request);
    	PageModelDto<OfficeUserDto> officeDto= deptService.getOfficeUsersNotInDept(deptId, odataObj);
    	return officeDto;
    }

    @RequiresAuthentication
    //@RequiresPermissions("dept##post")
    @RequestMapping(name = "创建记录", path = "", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.CREATED)
    public void post(@RequestBody DeptDto record) {
        deptService.save(record);
    }

    @RequiresAuthentication
    //@RequiresPermissions("dept##put")
    @RequestMapping(name = "更新记录", path = "", method = RequestMethod.PUT)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void put(@RequestBody DeptDto record) {
        deptService.update(record);
    }

    @RequiresAuthentication
    @RequestMapping(name = "主键查询", path = "html/findById",method=RequestMethod.GET)	
	@Transactional
	public @ResponseBody DeptDto findById(@RequestParam(required = true)String deptId){		
		return deptService.findById(deptId);
	}

    @RequiresAuthentication
    //@RequiresPermissions("dept##delete")
    @RequestMapping(name = "删除记录", path = "", method = RequestMethod.DELETE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void delete(@RequestBody String id) {
    	deptService.delete(id);
    }

    @RequiresAuthentication
    //@RequiresPermissions("dept#html/list#get")
    @RequestMapping(name = "列表页面", path = "html/list", method = RequestMethod.GET)
    public String list() {
        return ctrlName+"/list";
    }

    @RequiresAuthentication
    //@RequiresPermissions("dept#html/edit#get")
    @RequestMapping(name = "编辑页面", path = "html/edit", method = RequestMethod.GET)
    public String edit() {
        return ctrlName+"/edit";
    }

    @RequiresAuthentication
    //@RequiresPermissions("dept#html/listOfficeUser#get")
	@RequestMapping(name = "办事处人员列表", path = "html/listOfficeUser", method = RequestMethod.GET)
	public String listUser() {
		return ctrlName + "/listOfficeUser";
	}

}