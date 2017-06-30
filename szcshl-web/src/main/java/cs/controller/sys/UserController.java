package cs.controller.sys;

import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import cs.common.Constant;
import cs.common.Constant.EnumFlowNodeGroupName;
import cs.common.ICurrentUser;
import cs.domain.sys.User;
import cs.model.PageModelDto;
import cs.model.sys.OrgDto;
import cs.model.sys.UserDto;
import cs.repository.odata.ODataObj;
import cs.service.sys.UserService;

@Controller
@RequestMapping(name = "用户", path = "user")
public class UserController {

    private String ctrlName = "user";
    @Autowired
    private UserService userService;
    @Autowired
	private ICurrentUser currentUser;

	
	@RequiresPermissions("user#fingByOData#post")
	@RequestMapping(name = "获取用户数据", path = "fingByOData",method=RequestMethod.POST)	
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
    
    @RequiresPermissions("user#findAllUsers#get")
    @RequestMapping(name = "查询所有用户信息", path = "findAllUsers", method = RequestMethod.GET)
    @ResponseBody
    public List<UserDto> findAllUsers(){
    	List<UserDto>	userDtos =userService.findAllusers();
    	return userDtos;
    }
    @RequiresPermissions("user#findUsersByOrgId#get")
    @RequestMapping(name = "根据ID获取部门信息", path = "findUsersByOrgId", method = RequestMethod.GET)
    @ResponseBody
    public List<UserDto> findUsersByOrgId(@RequestParam(required = true)String orgId){
    	List<UserDto> userDto = userService.findUserByOrgId(orgId);
    	return userDto;
    }
    
    @RequiresPermissions("user#findChargeUsers#get")
    @RequestMapping(name = "获取所在部门的用户", path = "findChargeUsers", method = RequestMethod.GET)
    @ResponseBody
    public List<UserDto> findChargeUsers(){
    	User curUser = currentUser.getLoginUser();
    	return userService.findUserByOrgId(curUser.getOrg().getId());
    }
    
    @RequiresPermissions("user##post")
    @RequestMapping(name = "创建用户", path = "", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.CREATED)
    public void post(@RequestBody UserDto userDto) {
        userService.createUser(userDto);
    }

    @RequestMapping(name = "部门编辑角色初始化", path = "initRoleUsers", method = RequestMethod.GET)
    @ResponseBody
    public Map<String,List<UserDto>> initRoleUsers() {
    	Map<String,List<UserDto>> resultMap = new HashMap<String,List<UserDto>>(3);
    	resultMap.put("DEPT_LEADER", userService.findUserByRoleName(EnumFlowNodeGroupName.DEPT_LEADER.getValue()));
    	resultMap.put("VICE_DIRECTOR", userService.findUserByRoleName(EnumFlowNodeGroupName.VICE_DIRECTOR.getValue()));
    	resultMap.put("DIRECTOR", userService.findUserByRoleName(EnumFlowNodeGroupName.DIRECTOR.getValue()));
    	
    	return resultMap;
    }
    
    @RequiresPermissions("user##delete")
    @RequestMapping(name = "删除用户", path = "", method = RequestMethod.DELETE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void delete(@RequestBody String id) {
    	userService.deleteUser(id);
    }

    @RequiresPermissions("user##put")
    @RequestMapping(name = "更新用户", path = "", method = RequestMethod.PUT)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void put(@RequestBody UserDto userDto) {
        userService.updateUser(userDto);
    }
    
    @RequestMapping(name = "获取副主任信息", path = "getViceDirector", method = RequestMethod.GET)
    @ResponseBody
    public List<UserDto> getViceDirector() {
        return userService.findUserByRoleName(Constant.EnumFlowNodeGroupName.VICE_DIRECTOR.getValue());
    }

    /**
     * 根据用户ID获取用户信息
     * @param userId
     * @return
     */
    @RequestMapping(name = "获取副主任信息", path = "findUserById", method = RequestMethod.GET)
    @ResponseBody
    public UserDto findUserById(@RequestParam String userId){
        return userService.findById(userId);
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
