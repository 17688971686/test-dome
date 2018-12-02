package cs.controller.sys;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import cs.ahelper.MudoleAnnotation;
import cs.common.utils.Validate;
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

import cs.model.PageModelDto;
import cs.model.sys.RoleDto;
import cs.repository.odata.ODataObj;
import cs.service.sys.RoleService;

@Controller
@RequestMapping(name = "角色", path = "role")
@MudoleAnnotation(name = "系统管理",value = "permission#system")
public class RoleController {
    private String ctrlName = "role";
    @Autowired
    private RoleService roleService;

    //@RequiresPermissions("role#fingByOData#post")
    @RequiresAuthentication
    @RequestMapping(name = "获取角色数据", path = "fingByOData", method = RequestMethod.POST)
    @ResponseBody
    public PageModelDto<RoleDto> get(HttpServletRequest request) throws ParseException {
        ODataObj odataObj = new ODataObj(request);
        PageModelDto<RoleDto> roleDtos = roleService.get(odataObj);
        if(!Validate.isObject(roleDtos)){
            roleDtos = new PageModelDto();
        }
        return roleDtos;
    }

    @RequiresAuthentication
    @RequestMapping(name = "获取所有角色", path = "findAllRoles", method = RequestMethod.POST)
    @ResponseBody
    public List<RoleDto> findAllRoles() {
        List<RoleDto> roleDtoList = roleService.findAllRoles();
        if(!Validate.isList(roleDtoList)){
            roleDtoList = new ArrayList<>();
        }
        return roleDtoList;
    }

    //@RequiresPermissions("role#findById#post")
    @RequiresAuthentication
    @RequestMapping(name = "获取角色数据", path = "findById", method = RequestMethod.POST)
    @ResponseBody
    public RoleDto get(@RequestParam String roleId) {
        RoleDto roleDto = roleService.findById(roleId);
        if(!Validate.isObject(roleDto)){
            roleDto = new RoleDto();
        }
        return roleDto;
    }

    //@RequiresPermissions("role##post")
    @RequiresAuthentication
    @RequestMapping(name = "创建角色", path = "", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.CREATED)
    public void post(@RequestBody RoleDto roleDto) {
        roleService.createRole(roleDto);
    }

    //@RequiresPermissions("role##put")
    @RequiresAuthentication
    @RequestMapping(name = "更新角色", path = "", method = RequestMethod.PUT)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void update(@RequestBody RoleDto roleDto) throws Exception {
        roleService.updateRole(roleDto);
    }

    //@RequiresPermissions("role##delete")
    @RequiresAuthentication
    @RequestMapping(name = "删除角色", path = "", method = RequestMethod.DELETE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void delete(@RequestBody String id) {
        String[] ids = id.split(",");
        if (ids.length > 1) {
            roleService.deleteRoles(ids);
        } else {
            roleService.deleteRole(id);
        }
    }

    //begin#html
    @RequiresPermissions("role#html/list#get")
    @RequestMapping(name = "角色管理", path = "html/list", method = RequestMethod.GET)
    public String list() {
        return ctrlName + "/list";
    }

    //@RequiresPermissions("role#html/edit#get")
    @RequiresAuthentication
    @RequestMapping(name = "编辑角色页面", path = "html/edit", method = RequestMethod.GET)
    public String edit() {
        return ctrlName + "/edit";
    }
    //end#html
}
