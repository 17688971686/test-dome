package com.sn.framework.module.sys.controller;

import com.sn.framework.odata.impl.jpa.OdataJPA;
import com.sn.framework.core.web.PageModelDto;
import com.sn.framework.module.sys.domain.Resource;
import com.sn.framework.module.sys.service.IResourceService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import static com.sn.framework.common.StringUtil.SEPARATE_COMMA;

/**
 * Description: 系统资源  控制器
 * @Author tzg
 * @Date 2017/9/14 10:01
 */
@Controller
@RequestMapping(name = "系统资源", path = "sys/resource")
public class ResourceController {

    @Autowired
    private IResourceService resourceService;

//    @RequiresPermissions("sys:resource:get")
    @RequestMapping(name = "获取系统资源数据", path = "", method = RequestMethod.GET)
    @ResponseBody
    public PageModelDto<Resource> get(OdataJPA odata) {
        return resourceService.findPageByOdata(odata);
    }

//    @RequiresPermissions("sys:resource:get")
    @RequestMapping(name = "获取系统资源数据", path = "{resId}", method = RequestMethod.GET)
    @ResponseBody
    public Resource get(@PathVariable String resId) {
        return resourceService.getById(resId);
    }

//    @RequiresPermissions("sys:resource:post")
    @RequestMapping(name = "新增系统资源数据", path = "", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestBody Resource recored) {
        resourceService.create(recored);
    }

//    @RequiresPermissions("sys:resource:put")
    @RequestMapping(name = "更新系统资源数据", path = "", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@RequestBody Resource recored) {
        resourceService.update(recored);
    }

    @RequiresPermissions("sys:resource:delete")
    @RequestMapping(name = "删除系统资源数据", path = "", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@RequestBody String ids) {
        if (ids.contains(SEPARATE_COMMA)) {
            resourceService.deleteByIds(ids.split(SEPARATE_COMMA));
        } else {
            resourceService.deleteById(ids);
        }
    }

//    @RequiresPermissions("sys:resource:reset")
    @RequestMapping(name = "重置系统资源数据", path = "reset", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void reset() throws Exception {
        resourceService.resetResource();
    }

    @RequestMapping(name = "系统资源列表页面", path = "html/list", method = RequestMethod.GET)
    public String list() {
        return "sys/resource/resourceList";
    }

}