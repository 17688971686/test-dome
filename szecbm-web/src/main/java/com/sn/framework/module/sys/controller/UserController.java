package com.sn.framework.module.sys.controller;

import com.sn.framework.common.StringUtil;
import com.sn.framework.core.common.SessionUtil;
import com.sn.framework.core.web.PageModelDto;
import com.sn.framework.module.sys.domain.Resource;
import com.sn.framework.module.sys.domain.User;
import com.sn.framework.module.sys.domain.User_;
import com.sn.framework.module.sys.model.UserDto;
import com.sn.framework.module.sys.service.IUserService;
import com.sn.framework.odata.impl.jpa.OdataJPA;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.sn.framework.common.StringUtil.SEPARATE_COMMA;
import static com.sn.framework.core.Constants.USER_KEY_ADMIN;

/**
 * 用户Controller
 *
 * @author lqs
 * @date 2017/7/19
 */
@Controller
@RequestMapping(name = "用户管理", path = "sys/user")
public class UserController {


    @Autowired
    private IUserService userService;

    @RequestMapping(name = "获取用户数据", path = "", method = RequestMethod.GET)
    @ResponseBody
    public PageModelDto<UserDto> get(OdataJPA odata) {
        odata.addNEFilter(User_.username, USER_KEY_ADMIN);
        return userService.findPageByOdata(odata);
    }

    @RequiresPermissions("sys:user:post")
    @RequestMapping(name = "创建用户", path = "", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.CREATED)
    public void post(@RequestBody UserDto userDto) {
        userDto.setUserDataType(1);
        userService.create(userDto);
    }

    @RequiresPermissions("sys:user:delete")
    @RequestMapping(name = "删除用户", path = "", method = RequestMethod.DELETE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void delete(@RequestParam String userId) {
        if (userId.contains(SEPARATE_COMMA)) {
            userService.deleteByIds(StringUtil.split(userId, SEPARATE_COMMA));
        } else {
            userService.deleteById(userId);
        }
    }


    @RequestMapping(name = "根据ID获取部门信息", path = "findUsersByOrgId", method = RequestMethod.GET)
    @ResponseBody
    public List<UserDto> findUsersByOrgId() {
        User curUser = SessionUtil.getUserInfo();
        if(curUser.getOrgan() != null){
            return userService.findUserByOrgId(curUser.getOrgan().getOrganId());
        }
        return null;
    }


    @RequestMapping(name = "根据id获取用户", path = "{userId}", method = RequestMethod.GET)
    @ResponseBody
    public UserDto findById(@PathVariable String userId) {
        return userService.getById(userId);
    }

    @RequestMapping(name = "获取类型为市长的用户", path = "findMajor", method = RequestMethod.GET)
    @ResponseBody
    public List<UserDto> findMajorList() {
         return userService.findMajorList();
    }

    @RequiresPermissions("sys:user:put")
    @RequestMapping(name = "更新用户", path = "", method = RequestMethod.PUT)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void put(@RequestBody UserDto userDto) {
        userService.update(userDto);
    }

    @RequiresPermissions("sys:user:isEnable")
    @RequestMapping(name = "用户状态启动", path = "enable", method = RequestMethod.PUT)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void enable(@RequestParam String id) {
        userService.changeUserState(id, true);
    }

    @RequiresPermissions("sys:user:isEnable")
    @RequestMapping(name = "用户状态停用", path = "disable", method = RequestMethod.PUT)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void disable(@RequestParam String id) {
        userService.changeUserState(id, false);
    }

    @RequiresPermissions("sys:user:resetPwd")
    @RequestMapping(name = "重置用户密码", path = "resetPwd", method = RequestMethod.PUT)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void resetPwd(@RequestBody String userId) {
        userService.resetPwd(userId);
    }

    @RequestMapping(name = "获取用户资源数据", path = "resources", method = RequestMethod.GET)
    @ResponseBody
    public List<Resource> getUserResources(String status) {
        return userService.getUserResources(SessionUtil.getUsername(), status);
    }

    @RequiresPermissions("sys:user:setRoles")
    @RequestMapping(name = "设置用户角色", path = "setRoles", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void setRoles(@RequestParam String userId, String roleIds) {
        userService.setRoles(userId, roleIds);
    }

    // begin#html
    //@RequiresPermissions(ModuleDefine.SYS + "#user#html/list#get")
    @RequestMapping(name = "用户列表页面", path = "html/list", method = RequestMethod.GET)
    public String list() {
        return "sys/user/userList";
    }

    //@RequiresPermissions(ModuleDefine.SYS + "#user#html/edit#get")
    @RequestMapping(name = "编辑用户页面", path = "html/edit", method = RequestMethod.GET)
    public String edit() {
        return "sys/user/userEdit";
    }
    // end#html
}
