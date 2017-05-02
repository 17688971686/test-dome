package cs.controller;

import java.text.ParseException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import cs.domain.User;
import cs.model.CompanyDto;
import cs.model.OrgDto;
import cs.model.PageModelDto;
import cs.model.UserDto;
import cs.repository.odata.ODataObj;
import cs.service.OrgService;
import cs.service.UserService;

@Controller
@RequestMapping(name = "部门", path = "org")
public class OrgController {
	private String ctrlName = "org";
	@Autowired
	private OrgService orgService;
	
	@RequiresPermissions("org##get")	
	@RequestMapping(name = "获取部门数据", path = "", method = RequestMethod.GET)
	public @ResponseBody PageModelDto<OrgDto> get(HttpServletRequest request) throws ParseException {
		ODataObj odataObj = new ODataObj(request);
		PageModelDto<OrgDto> orgDtos = orgService.get(odataObj);

		return orgDtos;
	}
	
	@RequiresPermissions("org#html/getOrgById#get")	
	@RequestMapping(name = "根据ID获取部门数据", path = "html/getOrgById", method = RequestMethod.GET)
	public @ResponseBody OrgDto get(@RequestParam(required = true)String id) throws ParseException {
		OrgDto orgDto = orgService.findById(id);

		return orgDto;
	}
	
	@RequiresPermissions("org#getCompany#get")	
	@RequestMapping(name = "获取所有单位", path = "getCompany", method = RequestMethod.GET)
	@ResponseBody
	public List<CompanyDto> getCompany(HttpServletRequest request) throws ParseException{
		
		ODataObj oDataObj = new ODataObj(request);
		List<CompanyDto> comDto = 	orgService.getCompany(oDataObj);
		return comDto;
	}

	@RequiresPermissions("org##post")	
	@RequestMapping(name = "创建部门", path = "", method = RequestMethod.POST)
	@ResponseStatus(value = HttpStatus.CREATED)
	public void post(@RequestBody OrgDto orgDto) {
		orgService.createOrg(orgDto);
	}

	@RequiresPermissions("org##put")	
	@RequestMapping(name = "更新部门", path = "", method = RequestMethod.PUT)
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	public void update(@RequestBody OrgDto orgDto) {
		orgService.updateOrg(orgDto);
	}

	@RequiresPermissions("org##delete")	
	@RequestMapping(name = "删除部门", path = "", method = RequestMethod.DELETE)
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	public void delete(@RequestBody String id) {
		String[] ids = id.split(",");
		if (ids.length > 1) {
			orgService.deleteOrgs(ids);
		} else {
			orgService.deleteOrg(id);
		}
	}

	@RequiresPermissions("org#{orgId}/users#get")	
	@RequestMapping(name = "部门用户", path = "{orgId}/users", method = RequestMethod.GET)
	public @ResponseBody PageModelDto<UserDto> orgUsers(@PathVariable String orgId) {

		return orgService.getOrgUsers(orgId);
	}
	
	@RequiresPermissions("org#{orgId}/userNotIn#get")	
	@RequestMapping(name = "非部门用户", path = "{orgId}/userNotIn", method = RequestMethod.GET)
	public @ResponseBody PageModelDto<UserDto> userNotIn(@PathVariable String orgId,HttpServletRequest request) throws ParseException {

		ODataObj odataObj = new ODataObj(request);
		return orgService.getUsersNotInOrg(orgId, odataObj);
	}
	
	@RequiresPermissions("org#{orgId}/users#post")	
	@RequestMapping(name = "添加用户到部门", path = "{orgId}/users", method = RequestMethod.POST)
	@ResponseStatus(value = HttpStatus.CREATED)
	public void postUserToOrg(@PathVariable String orgId,@RequestBody String userId) {
		orgService.addUserToOrg(userId, orgId);
	}
	
	@RequiresPermissions("org#{orgId}/users#delete")
	@RequestMapping(name = "从部门移除用户", path = "{orgId}/users", method = RequestMethod.DELETE)
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	public void deleteUserFromOrg(@PathVariable String orgId,@RequestBody String userId) {
		String[] ids = userId.split(",");
		if (ids.length > 1) {
			orgService.removeOrgUsers(ids,orgId);
		} else {
			orgService.removeOrgUser(userId, orgId);
		}
	}
	
	
	
	// begin#html
	
	@RequiresPermissions("org#html/list#get")
	@RequestMapping(name = "部门列表页面", path = "html/list", method = RequestMethod.GET)
	public String list() {
		return ctrlName + "/list";
	}

	@RequiresPermissions("org#html/edit#get")
	@RequestMapping(name = "编辑部门页面", path = "html/edit", method = RequestMethod.GET)
	public String edit() {
		return ctrlName + "/edit";
	}

	@RequiresPermissions("org#html/orgUser#get")
	@RequestMapping(name = "部门用户列表页面", path = "html/orgUser", method = RequestMethod.GET)
	public String listUser() {
		return ctrlName + "/list_user";
	}
	// end#html
}
