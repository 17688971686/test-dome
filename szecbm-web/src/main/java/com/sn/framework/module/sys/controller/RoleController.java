package com.sn.framework.module.sys.controller;

import com.sn.framework.common.StringUtil;
import com.sn.framework.core.web.PageModelDto;
import com.sn.framework.module.sys.domain.Resource;
import com.sn.framework.module.sys.domain.Role_;
import com.sn.framework.module.sys.model.RoleDto;
import com.sn.framework.module.sys.service.IRoleService;
import com.sn.framework.odata.impl.jpa.OdataJPA;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

import static com.sn.framework.common.StringUtil.SEPARATE_COMMA;
import static com.sn.framework.core.Constants.ROLE_KEY_ADMIN;

/**
 * 用户Controller
 *
 * @author lqs
 * @date 2017/7/19
 */
@Controller
@RequestMapping(name = "角色管理", path = "sys/role")
public class RoleController {

    @Autowired
    private IRoleService roleService;

    @RequestMapping(name = "获取角色数据", path = "", method = RequestMethod.GET)
    @ResponseBody
    public PageModelDto<RoleDto> get(OdataJPA odata) {
//        if (!SecurityUtils.getSubject().hasRole(ROLE_KEY_ADMIN)) {
            odata.addNEFilter(Role_.roleName, ROLE_KEY_ADMIN);
//        }
        return roleService.findPageByOdata(odata);
    }

    @RequestMapping(name = "获取用户角色角色数据", path = "userRoles", method = RequestMethod.GET)
    @ResponseBody
    public List<RoleDto> findUserRoles(@RequestParam String userId) {
        return roleService.findUserRoles(userId);
    }

    @RequiresPermissions("sys:role:post")
    @RequestMapping(name = "创建角色", path = "", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.CREATED)
    public void post(@RequestBody RoleDto roleDto) {
        roleService.create(roleDto);
    }

    @RequiresPermissions("sys:role:delete")
    @RequestMapping(name = "删除角色", path = "", method = RequestMethod.DELETE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void delete(@RequestParam String roleId) {
        if (roleId.contains(SEPARATE_COMMA)) {
            roleService.deleteByIds(StringUtil.split(roleId, SEPARATE_COMMA));
        } else {
            roleService.deleteById(roleId);
        }
    }

    @RequestMapping(name = "根据id获取角色", path = "{roleId}", method = RequestMethod.GET)
    @ResponseBody
    public RoleDto findById(@PathVariable("roleId") String roleId) {
        RoleDto roleDto = roleService.getById(roleId);
        return roleDto;
    }

    @RequiresPermissions("sys:role:put")
    @RequestMapping(name = "更新角色", path = "", method = RequestMethod.PUT)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void put(@RequestBody RoleDto roleDto) {
        roleService.update(roleDto);
    }

    @RequiresPermissions("sys:role:authorization")
    @RequiresRoles(ROLE_KEY_ADMIN)
    @RequestMapping(name = "为角色授权", path = "authorization", method = RequestMethod.PUT)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void authorization(String roleId, @RequestBody Set<Resource> resources) {
        roleService.authorization(roleId, resources);
    }

    @RequiresPermissions("sys:role:isEnable")
    @RequestMapping(name = "角色状态启动", path = "enable", method = RequestMethod.PUT)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void enable(String id) {
        roleService.enable(id);
    }

    @RequiresPermissions("sys:role:isEnable")
    @RequestMapping(name = "角色状态停用", path = "disable", method = RequestMethod.PUT)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void disable(String id) {
        roleService.disable(id);
    }

    // begin#html
    @RequestMapping(name = "角色列表页面", path = "html/list", method = RequestMethod.GET)
    public String list() {
        return "sys/role/roleList";
    }

    @RequestMapping(name = "编辑角色页面", path = "html/edit", method = RequestMethod.GET)
    public String edit() {
        return "sys/role/roleEdit";
    }
    // end#html
}
