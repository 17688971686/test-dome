package cs.controller.sys;

import java.text.ParseException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import cs.ahelper.MudoleAnnotation;
import cs.common.ResultMsg;
import cs.domain.sys.OrgDept;
import cs.service.sys.OrgDeptService;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import cs.domain.sys.User;
import cs.model.PageModelDto;
import cs.model.sys.CompanyDto;
import cs.model.sys.OrgDto;
import cs.model.sys.UserDto;
import cs.repository.odata.ODataObj;
import cs.service.sys.OrgService;
import cs.service.sys.UserService;

@Controller
@RequestMapping(name = "部门", path = "org")
@MudoleAnnotation(name = "系统管理",value = "permission#system")
public class OrgController {
    private String ctrlName = "org";
    @Autowired
    private OrgService orgService;
    @Autowired
    private OrgDeptService orgDeptService;

    //@RequiresPermissions("org#fingByOData#post")
    @RequiresAuthentication
    @RequestMapping(name = "获取部门数据", path = "fingByOData", method = RequestMethod.POST)
    public @ResponseBody
    PageModelDto<OrgDto> get(HttpServletRequest request) throws ParseException {
        ODataObj odataObj = new ODataObj(request);
        PageModelDto<OrgDto> orgDtos = orgService.get(odataObj);

        return orgDtos;
    }

    //@RequiresPermissions("org#getOrgById#post")
    @RequiresAuthentication
    @RequestMapping(name = "根据ID获取部门数据", path = "getOrgById", method = RequestMethod.POST)
    public @ResponseBody OrgDto get(@RequestParam(required = true) String id) throws ParseException {
        OrgDto orgDto = orgService.findById(id);
        return orgDto;
    }

    //@RequiresPermissions("org#getCompany#get")
    @RequiresAuthentication
    @RequestMapping(name = "获取所有单位", path = "getCompany", method = RequestMethod.GET)
    @ResponseBody
    public List<CompanyDto> getCompany(HttpServletRequest request) throws ParseException {
        ODataObj oDataObj = new ODataObj(request);
        List<CompanyDto> comDto = orgService.getCompany(oDataObj);
        return comDto;
    }

    //@RequiresPermissions("org##post")
    @RequiresAuthentication
    @RequestMapping(name = "创建部门", path = "", method = RequestMethod.POST)
    @ResponseBody
    public ResultMsg post(@RequestBody OrgDto orgDto) {
        return orgService.createOrg(orgDto);
    }

    //@RequiresPermissions("org##put")
    @RequiresAuthentication
    @RequestMapping(name = "更新部门", path = "updateOrg", method = RequestMethod.POST)
    @ResponseBody
    public ResultMsg update(@RequestBody OrgDto orgDto) {
        return orgService.updateOrg(orgDto);
    }

    //@RequiresPermissions("org##delete")
    @RequiresAuthentication
    @RequestMapping(name = "删除部门", path = "", method = RequestMethod.DELETE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void delete(@RequestBody String id) {
        orgService.deleteOrg(id);
    }

    //@RequiresPermissions("org#users#post")
    @RequiresAuthentication
    @RequestMapping(name = "部门用户", path = "users", method = RequestMethod.POST)
    public @ResponseBody
    PageModelDto<UserDto> orgUsers(@RequestParam String orgId,HttpServletRequest request)throws ParseException {
        ODataObj odataObj = new ODataObj(request);
        return orgService.getOrgUsers(orgId,odataObj);
    }

    //@RequiresPermissions("org#userNotIn#post")
    @RequiresAuthentication
    @RequestMapping(name = "非部门用户", path = "userNotIn", method = RequestMethod.POST)
    public @ResponseBody
    PageModelDto<UserDto> userNotIn(@RequestParam String orgId, HttpServletRequest request) throws ParseException {

        ODataObj odataObj = new ODataObj(request);
        return orgService.getUsersNotInOrg(orgId, odataObj);
    }

    //@RequiresPermissions("org#addUsers#post")
    @RequiresAuthentication
    @RequestMapping(name = "添加用户到部门", path = "addUsers", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.CREATED)
    public void postUserToOrg(@RequestParam String orgId, @RequestParam String userId) {
        orgService.addUserToOrg(userId, orgId);
    }

    //@RequiresPermissions("org#deleteUsers#delete")
    @RequiresAuthentication
    @RequestMapping(name = "从部门移除用户", path = "deleteUsers", method = RequestMethod.DELETE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteUserFromOrg(@RequestParam String orgId, @RequestParam String userId) {
        String[] ids = userId.split(",");
        if (ids.length > 1) {
            orgService.removeOrgUsers(ids, orgId);
        } else {
            orgService.removeOrgUser(userId, orgId);
        }
    }

    //@RequiresPermissions("org#listAll#post")
    @RequiresAuthentication
    @RequestMapping(name = "所有部门查询", path = "listAll", method = RequestMethod.POST)
    public @ResponseBody  List<OrgDto> listAll() {
        return orgService.listAll();
    }
    //根据部门视图进行查询
    @RequiresAuthentication
    @RequestMapping(name = "部门列表查询", path = "queryOrgList", method = RequestMethod.POST)
    public @ResponseBody  List<OrgDept> queryOrgList() {
        return orgDeptService.queryAll();
    }

    // begin#html

    @RequiresPermissions("org#html/list#get")
    @RequestMapping(name = "部门管理", path = "html/list", method = RequestMethod.GET)
    public String list() {
        return ctrlName + "/list";
    }

    //@RequiresPermissions("org#html/edit#get")
    @RequiresAuthentication
    @RequestMapping(name = "编辑部门页面", path = "html/edit", method = RequestMethod.GET)
    public String edit() {
        return ctrlName + "/edit";
    }

    //@RequiresPermissions("org#html/orgUser#get")
    @RequiresAuthentication
    @RequestMapping(name = "部门用户列表页面", path = "html/orgUser", method = RequestMethod.GET)
    public String listUser() {
        return ctrlName + "/list_user";
    }
    // end#html
}
