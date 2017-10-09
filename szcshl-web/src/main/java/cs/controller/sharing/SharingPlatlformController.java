 package cs.controller.sharing;

import java.text.ParseException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import cs.ahelper.MudoleAnnotation;
import cs.model.expert.ExpertSelConditionDto;
import cs.model.sharing.SharingPrivilegeDto;
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
import cs.model.sharing.SharingPlatlformDto;
import cs.model.sys.UserDto;
import cs.repository.odata.ODataObj;
import cs.service.sharing.SharingPlatlformService;

/**
 * Description: 共享平台 控制层
 * author: sjy
 * Date: 2017-7-11 10:40:17
 */
@Controller
@RequestMapping(name = "共享平台", path = "sharingPlatlform")
@MudoleAnnotation(name = "公告资料管理",value = "permission#annountment")
public class SharingPlatlformController {

    String ctrlName = "sharing";
    @Autowired
    private SharingPlatlformService sharingPlatlformService;

    @RequiresAuthentication
    //@RequiresPermissions("sharingPlatlform#findByOData#post")
    @RequestMapping(name = "获取数据", path = "findByOData", method = RequestMethod.POST)
    @ResponseBody
    public PageModelDto<SharingPlatlformDto> get(HttpServletRequest request) throws ParseException {
        ODataObj odataObj = new ODataObj(request);
        PageModelDto<SharingPlatlformDto> sharingPlatlformDtos = sharingPlatlformService.get(odataObj);
        return sharingPlatlformDtos;
    }

    @RequiresAuthentication
    //@RequiresPermissions("sharingPlatlform#findByCurUser#post")
    @RequestMapping(name = "获取当前用户发布的数据", path = "findByCurUser", method = RequestMethod.POST)
    @ResponseBody
    public PageModelDto<SharingPlatlformDto> findByCurUser(HttpServletRequest request) throws ParseException {
        ODataObj odataObj = new ODataObj(request);
        PageModelDto<SharingPlatlformDto> sharingPlatlformDtos = sharingPlatlformService.findByCurUser(odataObj);
        return sharingPlatlformDtos;
    }

    @RequiresAuthentication
    //@RequiresPermissions("sharingPlatlform#findByReception#post")
    @RequestMapping(name = "获取接收到的共享数据", path = "findByReception", method = RequestMethod.POST)
    @ResponseBody
    public PageModelDto<SharingPlatlformDto> findByReception(HttpServletRequest req) throws ParseException {
        ODataObj odataObj = new ODataObj(req);
        PageModelDto<SharingPlatlformDto> sharingPlatlformDtos = sharingPlatlformService.findByReception(odataObj);
        return sharingPlatlformDtos;
    }

    @RequiresAuthentication
    //@RequiresPermissions("sharingPlatlform#initOrgAndUser#post")
    @RequestMapping(name = "获取部门和用户数据", path = "initOrgAndUser", method = RequestMethod.POST)
    public @ResponseBody
    Map<String, Object> initOrgAndUser() {
        return sharingPlatlformService.initOrgAndUser();
    }

    @RequiresAuthentication
    //@RequiresPermissions("sharingPlatlform#updatePublish#post")
    @RequestMapping(name = "修改发布状态", path = "updatePublish", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void updatePublish(@RequestParam String ids,@RequestParam String status) {
        sharingPlatlformService.updatePublishStatus(ids,status);
    }

    @RequiresAuthentication
    //@RequiresPermissions("sharingPlatlform#findUser#post")
    @RequestMapping(name = "个人发布", path = "findUser", method = RequestMethod.POST)
    @ResponseBody
    public UserDto findUser(@RequestBody UserDto user) {
        UserDto userDto = sharingPlatlformService.findUser(user.getLoginName());
        return userDto;
    }

    @RequiresAuthentication
    //@RequiresPermissions("sharingPlatlform#saveSharing#post")
    @RequestMapping(name = "创建记录", path = "saveSharing", method = RequestMethod.POST)
    @ResponseBody
    public SharingPlatlformDto post(@RequestBody SharingPlatlformDto record) {
        sharingPlatlformService.save(record);
        return record;
    }

    @RequiresAuthentication
    //@RequiresPermissions("sharingPlatlform#html/findById#get")
    @RequestMapping(name = "主键查询", path = "html/findById", method = RequestMethod.GET)
    public @ResponseBody
    SharingPlatlformDto findById(@RequestParam(required = true) String id) {
        return sharingPlatlformService.findById(id);
    }

    @RequiresAuthentication
    //@RequiresPermissions("sharingPlatlform#html/sharingDeatilById#get")
    @RequestMapping(name = "根据ID查询详情信息", path = "html/sharingDeatilById", method = RequestMethod.GET)
    public @ResponseBody
    SharingPlatlformDto detailById(@RequestParam(required = true) String id) {
        return sharingPlatlformService.findById(id);
    }

    @RequiresAuthentication
    //@RequiresPermissions("sharingPlatlform##delete")
    @RequestMapping(name = "删除记录", path = "sharingDelete", method = RequestMethod.DELETE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void delete(@RequestBody String id) {
        sharingPlatlformService.delete(id);
    }

    //@RequiresPermissions("sharingPlatlform##put")
    @RequiresAuthentication
    @RequestMapping(name = "更新记录", path = "", method = RequestMethod.PUT)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void put(@RequestBody SharingPlatlformDto record) {
        sharingPlatlformService.update(record);
    }

    // begin#html
    @RequiresPermissions("sharingPlatlform#html/list#get")
    @RequestMapping(name = "共享资料管理", path = "html/list", method = RequestMethod.GET)
    public String list() {
        return ctrlName + "/list";
    }

    @RequiresAuthentication
    //@RequiresPermissions("sharingPlatlform#html/edit#get")
    @RequestMapping(name = "编辑页面", path = "html/edit", method = RequestMethod.GET)
    public String edit() {
        return ctrlName + "/edit";
    }

    @RequiresAuthentication
    //@RequiresPermissions("sharingPlatlform#html/detail#get")
    @RequestMapping(name = "资料共享详情页", path = "html/detail", method = RequestMethod.GET)
    public String sharingDetail() {
        return ctrlName + "/detail";
    }

    @RequiresPermissions("sharingPlatlform#html/yetList#get")
    @RequestMapping(name = "共享资料列表", path = "html/yetList", method = RequestMethod.GET)
    public String sharingYet() {
        return ctrlName + "/yetList";
    }

    // end#html

}