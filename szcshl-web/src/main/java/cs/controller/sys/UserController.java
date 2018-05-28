package cs.controller.sys;

import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import cs.ahelper.MudoleAnnotation;
import cs.common.ResultMsg;
import cs.common.utils.SessionUtil;
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

import cs.common.constants.Constant;
import cs.common.constants.Constant.EnumFlowNodeGroupName;
import cs.domain.sys.User;
import cs.model.PageModelDto;
import cs.model.sys.OrgDto;
import cs.model.sys.UserDto;
import cs.repository.odata.ODataObj;
import cs.service.sys.UserService;

@Controller
@RequestMapping(name = "用户", path = "user")
@MudoleAnnotation(name = "系统管理", value = "permission#system")
public class UserController {

    private String ctrlName = "user";
    @Autowired
    private UserService userService;


    //@RequiresPermissions("user#fingByOData#post")

    /**
     * 因排序有问题，直接在页面排序
     * @param request
     * @return
     * @throws ParseException
     */
    @RequiresAuthentication
    @RequestMapping(name = "获取用户数据", path = "fingByOData", method = RequestMethod.POST)
    @ResponseBody
    public PageModelDto<UserDto> get(HttpServletRequest request) throws ParseException {
        ODataObj odataObj = new ODataObj(request);
        PageModelDto<UserDto> userDtos = userService.get(odataObj);
        return userDtos;
    }

    //@RequiresPermissions("user#getOrg#get")
    @RequiresAuthentication
    @RequestMapping(name = "获取机构数据", path = "getOrg", method = RequestMethod.GET)
    @ResponseBody
    public List<OrgDto> getOrg(HttpServletRequest request) throws ParseException {
        ODataObj odataObj = new ODataObj(request);
        List<OrgDto> orgDto = userService.getOrg(odataObj);
        return orgDto;
    }

    //@RequiresPermissions("user#findAllUsers#get")
    @RequiresAuthentication
    @RequestMapping(name = "查询所有用户信息", path = "findAllUsers", method = RequestMethod.GET)
    @ResponseBody
    public List<UserDto> findAllUsers() {
        List<UserDto> userDtos = userService.findAllusers();
        return userDtos;
    }

    //@RequiresPermissions("user#findUsersByOrgId#get")
    @RequiresAuthentication
    @RequestMapping(name = "根据ID获取部门信息", path = "findUsersByOrgId", method = RequestMethod.GET)
    @ResponseBody
    public List<UserDto> findUsersByOrgId(@RequestParam(required = true) String orgId) {
        List<UserDto> userDto = userService.findUserByOrgId(orgId);
        return userDto;
    }


    //@RequiresPermissions("user#findChargeUsers#get")
    @RequiresAuthentication
    @RequestMapping(name = "获取所在部门的用户", path = "findChargeUsers", method = RequestMethod.GET)
    @ResponseBody
    public List<UserDto> findChargeUsers() {
        User curUser = SessionUtil.getUserInfo();
        if(curUser.getOrg() != null){
            return userService.findUserByOrgId(curUser.getOrg().getId());
        }
        return null;
    }

    //@RequiresPermissions("user#findByOrgUserName#get")
    @RequiresAuthentication
    @RequestMapping(name = "根据部门ID获取用户所在部门", path = "findByOrgUserName", method = RequestMethod.GET)
    @ResponseBody
    public List<UserDto> findByOrgUserNames(@RequestParam(required = true) String orgId) {
        List<UserDto> userlist = userService.findByOrgUserName(orgId);
        return userlist;
    }

    //@RequiresPermissions("user##post")
    @RequiresAuthentication
    @RequestMapping(name = "创建用户", path = "", method = RequestMethod.POST)
    @ResponseBody
    public ResultMsg post(@RequestBody UserDto userDto) {
        return userService.createUser(userDto);
    }

    @RequiresAuthentication
    @RequestMapping(name = "部门编辑角色初始化", path = "initRoleUsers", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, List<UserDto>> initRoleUsers() {
        Map<String, List<UserDto>> resultMap = new HashMap<String, List<UserDto>>(3);
        resultMap.put("DEPT_LEADER", userService.findUserByRoleName(EnumFlowNodeGroupName.DEPT_LEADER.getValue()));
        resultMap.put("VICE_DIRECTOR", userService.findUserByRoleName(EnumFlowNodeGroupName.VICE_DIRECTOR.getValue()));
        resultMap.put("DIRECTOR", userService.findUserByRoleName(EnumFlowNodeGroupName.DIRECTOR.getValue()));

        return resultMap;
    }

    //@RequiresPermissions("user##delete")
    @RequiresAuthentication
    @RequestMapping(name = "删除用户", path = "", method = RequestMethod.DELETE)
    @ResponseBody
    public ResultMsg delete(@RequestBody String id) {
        return userService.deleteUser(id);
    }

    //@RequiresPermissions("user##put")
    @RequiresAuthentication
    @RequestMapping(name = "更新用户", path = "", method = RequestMethod.PUT)
    @ResponseBody
    public ResultMsg put(@RequestBody UserDto userDto) {
        return userService.updateUser(userDto);
    }

    @RequiresAuthentication
    @RequestMapping(name = "获取副主任信息", path = "getViceDirector", method = RequestMethod.GET)
    @ResponseBody
    public List<UserDto> getViceDirector() {
        return userService.findUserByRoleName(Constant.EnumFlowNodeGroupName.VICE_DIRECTOR.getValue());
    }


    //@RequiresPermissions("user#createUserNo#get")
    @RequiresAuthentication
    @RequestMapping(name = "生成员工工号", path = "createUserNo", method = RequestMethod.GET)
    @ResponseBody
    public String createUserNo() {
        String userNo = String.format("%03d", Integer.valueOf(userService.findMaxUserNo()) + 1);
        return userNo;
    }

    /**
     * 根据用户ID获取用户信息
     *
     * @param userId
     * @return
     */
    @RequiresAuthentication
    @RequestMapping(name = "根据用户ID查询用户信息", path = "findUserById", method = RequestMethod.POST)
    @ResponseBody
    public UserDto findUserById(@RequestParam String userId) {
        return userService.findById(userId, true);
    }

    /**
     * 以下代办人处理
     */
    //@RequiresPermissions("user#getAllUserDisplayName#get")
    @RequiresAuthentication
    @RequestMapping(name = "获取所有用户显示名和id", path = "getAllUserDisplayName", method = RequestMethod.POST)
    @ResponseBody
    public List<UserDto> getAllUserDisPlayName() {
        return userService.getAllUserDisplayName();
    }

    @RequiresAuthentication
    @RequestMapping(name = "根据ID获取所有待办任务信息", path = "findAllTaskList", method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> findAllTaskList(@RequestParam(defaultValue = "") String userId){
        if(!Validate.isString(userId)){
            userId = SessionUtil.getUserId();
        }
        Map<String,Object> taskMap = userService.findAllTaskList(userId);
        return taskMap;
    }

    @RequiresAuthentication
    @RequestMapping(name = "获取当前用户信息", path = "findCurrentUser", method = RequestMethod.POST)
    @ResponseBody
    public UserDto findCurrentUser() {
        return userService.findById(SessionUtil.getUserId(),true);
    }

    //@RequiresPermissions("user#saveTakeUser#post")
    @RequiresAuthentication
    @RequestMapping(name = "保存代办人", path = "saveTakeUser", method = RequestMethod.POST)
    @ResponseBody
    public ResultMsg saveTkeUser(@RequestParam String takeUserId) {
        return userService.saveTakeUser(takeUserId);
    }

    //@RequiresPermissions("user#cancelTakeUser#get")
    @RequiresAuthentication
    @RequestMapping(name = "取消代办人", path = "cancelTakeUser", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void cancelTakeUser() {
        userService.cancelTakeUser();
    }

    @RequiresAuthentication
    @RequestMapping(name = "重置密码", path = "resetPwd", method = RequestMethod.PUT)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void resetPwd(@RequestParam String ids) {
        userService.resetPwd(ids);
    }

    @RequiresAuthentication
    @RequestMapping(name = "查询所有部门下的用户" , path = "findUserAndOrg" , method = RequestMethod.POST)
    @ResponseBody
    public List<UserDto> findUserAndOrg(){
        return userService.findUserAndOrg();
    }


    // begin#html
    @RequiresPermissions("user#html/list#get")
    @RequestMapping(name = "用户管理", path = "html/list", method = RequestMethod.GET)
    public String list() {
        return ctrlName + "/list";
    }

    @RequiresAuthentication
    //@RequiresPermissions("user#html/edit#get")
    @RequestMapping(name = "编辑用户页面", path = "html/edit", method = RequestMethod.GET)
    public String edit() {
        //userService.createUser(userDto);
        return ctrlName + "/edit";
    }
    // end#html
}
