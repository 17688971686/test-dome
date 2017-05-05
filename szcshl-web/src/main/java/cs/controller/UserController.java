package cs.controller;

import java.text.ParseException;
import java.util.List;

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

import cs.domain.User;
import cs.model.OrgDto;
import cs.model.PageModelDto;
import cs.model.UserDto;
import cs.repository.odata.ODataObj;
import cs.service.UserService;

@Controller
@RequestMapping(name = "用户", path = "user")
public class UserController {

    private String ctrlName = "user";
    @Autowired
    private UserService userService;

	
	@RequiresPermissions("user##get")
	@RequestMapping(name = "获取用户数据", path = "",method=RequestMethod.GET)	
    @ResponseBody
    public PageModelDto<UserDto> get(HttpServletRequest request) throws ParseException {
        ODataObj odataObj = new ODataObj(request);
        PageModelDto<UserDto> userDtos = userService.get(odataObj);
        return userDtos;
    }

    @RequiresPermissions("user#getOrg#get")
    @RequestMapping(name = "获取机构数据", path = "getOrg", method = RequestMethod.GET)
    @ResponseBody
    public List<OrgDto> getOrg(HttpServletRequest request) throws ParseException {
        ODataObj odataObj = new ODataObj(request);
        List<OrgDto> orgDto = userService.getOrg(odataObj);
        return orgDto;
    }
    
    @RequiresPermissions("user#findUsersByOrgId#get")
    @RequestMapping(name = "根据ID获取部门信息", path = "findUsersByOrgId", method = RequestMethod.GET)
    @ResponseBody
    public List<UserDto> findUsersByOrgId(@RequestParam(required = true)String orgId){
    	List<UserDto> userDto = userService.findUserByDeptId(orgId);
    	return userDto;
    }
    
    @RequiresPermissions("user##post")
    @RequestMapping(name = "创建用户", path = "", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.CREATED)
    public void post(@RequestBody UserDto userDto) {
        userService.createUser(userDto);
    }

    @RequiresPermissions("user##delete")
    @RequestMapping(name = "删除用户", path = "", method = RequestMethod.DELETE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void delete(@RequestBody String id) {
        String[] ids = id.split(",");
        if (ids.length > 1) {
            userService.deleteUsers(ids);
        } else {
            userService.deleteUser(id);
        }
    }

    @RequiresPermissions("user##put")
    @RequestMapping(name = "更新用户", path = "", method = RequestMethod.PUT)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void put(@RequestBody UserDto userDto) {
        userService.updateUser(userDto);
    }

    // begin#html
    @RequiresPermissions("user#html/list#get")
    @RequestMapping(name = "用户列表页面", path = "html/list", method = RequestMethod.GET)
    public String list() {
        return ctrlName + "/list";
    }

    @RequiresPermissions("user#html/edit#get")
    @RequestMapping(name = "编辑用户页面", path = "html/edit", method = RequestMethod.GET)
    public String edit() {
        //userService.createUser(userDto);
        return ctrlName + "/edit";
    }
    // end#html
}
