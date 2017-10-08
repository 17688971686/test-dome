package cs.controller.sys;

import cs.ahelper.MudoleAnnotation;
import cs.model.PageModelDto;
import cs.model.sys.SysConfigDto;
import cs.repository.odata.ODataObj;
import cs.service.sys.SysConfigService;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.List;

/**
 * Description: 系统参数 控制层
 * author: ldm
 * Date: 2017-6-13 14:28:16
 */
@Controller
@RequestMapping(name = "系统参数", path = "sysConfig")
@MudoleAnnotation(name = "系统管理",value = "permission#system")
public class SysConfigController {

    String ctrlName = "sysconfig";
    @Autowired
    private SysConfigService sysConfigService;

    //@RequiresPermissions("sysConfig#findByOData#post")
    @RequiresAuthentication
    @RequestMapping(name = "获取数据", path = "findByOData", method = RequestMethod.POST)
    @ResponseBody
    public PageModelDto<SysConfigDto> get(HttpServletRequest request) throws ParseException {
        ODataObj odataObj = new ODataObj(request);
        PageModelDto<SysConfigDto> sysConfigDtos = sysConfigService.get(odataObj);
        return sysConfigDtos;
    }

    //@RequiresPermissions("sysConfig#queryList#get")
    @RequiresAuthentication
    @RequestMapping(name = "获取数据", path = "queryList", method = RequestMethod.GET)
    @ResponseBody
    public List<SysConfigDto> queryList() {
        return sysConfigService.queryAll();
    }

    //@RequiresPermissions("sysConfig##post")
    @RequiresAuthentication
    @RequestMapping(name = "创建记录", path = "", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.CREATED)
    public void post(@RequestBody SysConfigDto record) {
        sysConfigService.save(record);
    }

    @RequiresAuthentication
    @RequestMapping(name = "主键查询", path = "html/findById", method = RequestMethod.GET)
    public @ResponseBody
    SysConfigDto findById(@RequestParam(required = true) String id) {
        return sysConfigService.findById(id);
    }

    //@RequiresPermissions("sysConfig##delete")
    @RequiresAuthentication
    @RequestMapping(name = "删除记录", path = "", method = RequestMethod.DELETE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void delete(@RequestParam String id) {
        sysConfigService.delete(id);
    }

    // begin#html
    @RequiresPermissions("sysConfig#html/list#get")
    @RequestMapping(name = "系统配置", path = "html/list", method = RequestMethod.GET)
    public String list() {
        return ctrlName + "/list";
    }

    //@RequiresPermissions("sysConfig#html/edit#get")
    @RequiresAuthentication
    @RequestMapping(name = "编辑页面", path = "html/edit", method = RequestMethod.GET)
    public String edit() {
        return ctrlName + "/edit";
    }
    // end#html

}