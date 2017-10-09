package cs.controller.external;

import java.text.ParseException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import cs.ahelper.IgnoreAnnotation;
import cs.ahelper.MudoleAnnotation;
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

import cs.common.utils.Validate;
import cs.model.PageModelDto;
import cs.model.external.DeptDto;
import cs.model.external.OfficeUserDto;
import cs.model.project.SignDto;
import cs.repository.odata.ODataObj;
import cs.service.external.OfficeUserService;
 
@Controller
@RequestMapping(name = "处室人员", path = "officeUser")
@MudoleAnnotation(name = "系统管理",value = "permission#system")
public class OfficeUserController {

	String ctrlName ="officeUser";
	
	@Autowired
	private OfficeUserService officeUserService;

    @RequiresAuthentication
    //@RequiresPermissions("officeUser#fingByOData#post")
	@RequestMapping(name = "获取数据", path = "fingByOData", method = RequestMethod.POST)
	@ResponseBody
    public PageModelDto<OfficeUserDto> get(HttpServletRequest request) throws ParseException {
		 ODataObj odataObj = new ODataObj(request);
		 PageModelDto<OfficeUserDto> officeUserDtos = officeUserService.get(odataObj);	
		 return officeUserDtos;
	}

    @RequiresAuthentication
    //@RequiresPermissions("officeUser#getDepts#get")
 	@RequestMapping(name = "获取办事处信息", path = "getDepts", method = RequestMethod.GET)
 	@ResponseBody
    public List<DeptDto> getDepts(){
    	List<DeptDto> deptDto=	officeUserService.getDepts();
    	return deptDto;
    }

    @RequiresAuthentication
    //@RequiresPermissions("officeUser##post")
    @RequestMapping(name = "创建记录", path = "", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.CREATED)
    public void post(@RequestBody OfficeUserDto record) {
        officeUserService.save(record);
    }

    @RequiresAuthentication
	@RequestMapping(name = "主键查询", path = "html/findById",method=RequestMethod.GET)	
	@Transactional
	public @ResponseBody OfficeUserDto findById(@RequestParam(required = true)String officeID){		
		return officeUserService.findById(officeID);
	}

    @RequiresAuthentication
    //@RequiresPermissions("officeUser#findOfficeUsersByDeptName#post")
	@RequestMapping(name = "根据部门名称获取处室信息", path = "findOfficeUsersByDeptName", method = RequestMethod.POST)
    @ResponseBody
	public List<OfficeUserDto> findOfficeUserDeptId(@RequestBody SignDto signDto ){
		String deptName = Validate.isString(signDto.getMaindeptName())?signDto.getMaindeptName():signDto.getAssistdeptName();
		List<OfficeUserDto> officeDto =officeUserService.findOfficeUserByDeptId(deptName);
		return officeDto;
	}

    @RequiresAuthentication
    //@RequiresPermissions("officeUser##delete")
    @RequestMapping(name = "删除记录", path = "", method = RequestMethod.DELETE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void delete(@RequestBody String id) {
    	String [] ids = id.split(",");
		if (ids.length > 1) {
			officeUserService.deletes(ids);
		} else {
			officeUserService.delete(id);
		}     
    }

    @RequiresAuthentication
    //@RequiresPermissions("officeUser##put")
    @RequestMapping(name = "更新记录", path = "", method = RequestMethod.PUT)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void put(@RequestBody OfficeUserDto record) {
        officeUserService.update(record);
    }

    // begin#html
    @RequiresPermissions("officeUser#html/list#get")
    @RequestMapping(name = "处室人员管理", path = "html/list", method = RequestMethod.GET)
    public String list() {
        return ctrlName+"/list"; 
    }

    @RequiresAuthentication
    //@RequiresPermissions("officeUser#html/edit#get")
    @RequestMapping(name = "编辑页面", path = "html/edit", method = RequestMethod.GET)
    public String edit() {
        return ctrlName+"/edit";
    }
    // end#html
	    
	
}
