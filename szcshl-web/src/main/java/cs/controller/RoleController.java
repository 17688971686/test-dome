package cs.controller;

import java.text.ParseException;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import cs.model.PageModelDto;
import cs.model.RoleDto;
import cs.repository.odata.ODataObj;
import cs.service.RoleService;

@Controller
@RequestMapping(name = "角色", path = "role")
public class RoleController {
	private String ctrlName="role";
	@Autowired
	private RoleService roleService;
	
	@RequiresPermissions("role##get")
	@RequestMapping(name = "获取角色数据", path = "",method=RequestMethod.GET)	
	public @ResponseBody PageModelDto<RoleDto> get(HttpServletRequest request) throws ParseException {
		ODataObj odataObj=new ODataObj(request);
		PageModelDto<RoleDto> roleDtos=roleService.get(odataObj);
		
		return roleDtos;		
	}
	
	
	@RequiresPermissions("role##post")
	@RequestMapping(name = "创建角色", path = "",method=RequestMethod.POST)	
	@ResponseStatus(value = HttpStatus.CREATED)
	public void  post(@RequestBody RoleDto roleDto)  {		
		roleService.createRole(roleDto);		
	}
	
	@RequiresPermissions("role##put")
	@RequestMapping(name = "更新角色", path = "",method=RequestMethod.PUT)	
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	public void  update(@RequestBody RoleDto roleDto) throws Exception  {	
		roleService.updateRole(roleDto);		
	}
	
	@RequiresPermissions("role##delete")
	@RequestMapping(name = "删除角色", path = "",method=RequestMethod.DELETE)	
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	public void  delete(@RequestBody String id)  {
		String[] ids=id.split(",");
		if(ids.length>1){
			roleService.deleteRoles(ids);	
		}else{
			roleService.deleteRole(id);	
		}	
	}
	
	
	//begin#html
	
	@RequiresPermissions("role#html/list#get")
	@RequestMapping(name="角色列表页面",path="html/list",method=RequestMethod.GET)
	public String list(){
		return ctrlName + "/list";
	}
	
	@RequiresPermissions("role#html/edit#get")
	@RequestMapping(name="编辑角色页面",path="html/edit",method=RequestMethod.GET)
	public String edit(){
		return ctrlName + "/edit";
	}
	//end#html
}
