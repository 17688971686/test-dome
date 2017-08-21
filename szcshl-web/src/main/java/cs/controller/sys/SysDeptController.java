package cs.controller.sys;

import cs.common.ResultMsg;
import cs.domain.sys.SysDept;
import cs.model.PageModelDto;
import cs.model.sys.CompanyDto;
import cs.model.sys.OrgDto;
import cs.model.sys.SysDeptDto;
import cs.model.sys.UserDto;
import cs.repository.odata.ODataObj;
import cs.repository.repositoryImpl.sys.SysDeptRepo;
import cs.service.sys.OrgService;
import cs.service.sys.SysDeptService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.List;

@Controller
@RequestMapping(name = "部门", path = "sysdept")
public class SysDeptController {
    private String ctrlName = "sysdept";

    @Autowired
    private SysDeptService sysDeptService;

    @RequiresPermissions("sysdept#fingByOData#post")
    @RequestMapping(name = "获取部门小组数据", path = "fingByOData", method = RequestMethod.POST)
    @ResponseBody
    public PageModelDto<SysDeptDto> get(HttpServletRequest request) throws ParseException {
        ODataObj odataObj = new ODataObj(request);
        PageModelDto<SysDeptDto> sysDeptDto = sysDeptService.get(odataObj);

        return sysDeptDto;
    }

    @RequiresPermissions("sysdept#getSysDeptById#post")
    @RequestMapping(name = "根据ID获取部门小组数据", path = "getSysDeptById", method = RequestMethod.POST)
    @ResponseBody
    public SysDeptDto get(@RequestParam(required = true) String id) throws ParseException {
      return sysDeptService.findById(id);
    }

    @RequiresPermissions("sysdept##post")
    @RequestMapping(name = "新增部门小组", path = "", method = RequestMethod.POST)
    @ResponseBody
    public ResultMsg post(@RequestBody SysDeptDto sysDeptDto) {
        return sysDeptService.save(sysDeptDto);
    }

    @RequiresPermissions("sysdept##put")
    @RequestMapping(name = "更新部门小组", path = "", method = RequestMethod.PUT)
    @ResponseBody
    public ResultMsg update(@RequestBody SysDeptDto sysDeptDto) {
        return sysDeptService.save(sysDeptDto);
    }

    @RequiresPermissions("sysdept##delete")
    @RequestMapping(name = "删除部门小组", path = "", method = RequestMethod.DELETE)
    @ResponseBody
    public ResultMsg delete(@RequestBody String id) {
        return sysDeptService.deleteSysDept(id);
    }

    @RequiresPermissions("sysdept#users#post")
    @RequestMapping(name = "部门小组用户", path = "users", method = RequestMethod.POST)
    @ResponseBody
    public  PageModelDto<UserDto> sysDeptUsers(@RequestParam String id) {
        return sysDeptService.getSysDeptUsers(id);
    }

    @RequiresPermissions("sysdept#userNotIn#post")
    @RequestMapping(name = "非部门小组用户", path = "userNotIn", method = RequestMethod.POST)
    @ResponseBody
    public PageModelDto<UserDto> userNotInSysDept(@RequestParam String id){
        return sysDeptService.getUserNotInSysDept(id);
    }

    @RequiresPermissions("sysdept#addUsers#post")
    @RequestMapping(name = "添加用户到部门小组", path = "addUsers", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.CREATED)
    public void postUserToOrg(@RequestParam String id, @RequestParam String userId) {
        sysDeptService.addUserToSysDept(id, userId);
    }

    @RequiresPermissions("sysdept#deleteUsers#delete")
    @RequestMapping(name = "从部门小组移除用户", path = "deleteUsers", method = RequestMethod.DELETE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteUserFromOrg(@RequestParam String id, @RequestParam String userId) {
        sysDeptService.removeSysDeptUser(id, userId);
    }

    // begin#html
    @RequiresPermissions("sysdept#html/list#get")
    @RequestMapping(name = "部门小组列表页面", path = "html/list", method = RequestMethod.GET)
    public String list() {
        return ctrlName + "/list";
    }

    @RequiresPermissions("sysdept#html/edit#get")
    @RequestMapping(name = "编辑部门小组页面", path = "html/edit", method = RequestMethod.GET)
    public String edit() {
        return ctrlName + "/edit";
    }

    @RequiresPermissions("sysdept#html/orgUser#get")
    @RequestMapping(name = "部门小组用户列表页面", path = "html/orgUser", method = RequestMethod.GET)
    public String listUser() {
        return ctrlName + "/list_user";
    }
    // end#html
}
