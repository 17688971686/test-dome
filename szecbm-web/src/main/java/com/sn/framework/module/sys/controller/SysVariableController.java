package com.sn.framework.module.sys.controller;

import com.sn.framework.core.web.PageModelDto;
import com.sn.framework.module.sys.domain.SysVariableType;
import com.sn.framework.module.sys.model.SysVariableDto;
import com.sn.framework.module.sys.service.ISysVariableService;
import com.sn.framework.odata.impl.jpa.OdataJPA;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static com.sn.framework.common.StringUtil.SEPARATE_COMMA;


/**
 * Description: 系统变量信息   控制器
 *
 * @author: tzg
 * @date: 2018/1/24 15:12
 */
@Controller
@RequestMapping(name = "系统变量信息", path = "sys/variable")
public class SysVariableController {

    @Autowired
    private ISysVariableService sysVariableService;

    @RequiresPermissions("sys:variable:get")
    @RequestMapping(name = "获取系统变量数据", path = "", method = RequestMethod.GET)
    @ResponseBody
    public PageModelDto<SysVariableDto> get(OdataJPA odata) {
        return sysVariableService.findPageByOdata(odata);
    }

    @RequiresPermissions("sys:variable:get")
    @RequestMapping(name = "根据id获取系统变量", path = "{sysVariableId}", method = RequestMethod.GET)
    @ResponseBody
    public SysVariableDto findById(@PathVariable("sysVariableId") String SysVariableId) {
        SysVariableDto SysVariableDto = sysVariableService.getById(SysVariableId);
        return SysVariableDto;
    }

    @RequiresPermissions("sys:variable:post")
    @RequestMapping(name = "创建系统变量", path = "", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.CREATED)
    public void post(@RequestBody SysVariableDto sysVariableDto) {
        sysVariableService.create(sysVariableDto);
    }

    @RequiresPermissions("sys:variable:put")
    @RequestMapping(name = "更新系统变量", path = "", method = RequestMethod.PUT)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void put(@RequestBody SysVariableDto SysVariableDto) {
        sysVariableService.update(SysVariableDto);
    }

    @RequiresPermissions("sys:variable:delete")
    @RequestMapping(name = "删除系统变量", path = "", method = RequestMethod.DELETE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void delete(@RequestBody String sysVariableId) {
        if (sysVariableId.contains(SEPARATE_COMMA)) {
            sysVariableService.deleteByIds(sysVariableId.split(SEPARATE_COMMA));
        } else {
            sysVariableService.deleteById(sysVariableId);
        }
    }

    // begin#html
    @RequiresPermissions("sys:variable:get")
    @RequestMapping(name = "系统变量列表页面", path = "html/list", method = RequestMethod.GET)
    public String list() {
        return "sys/sysVariable/sysVariableList";
    }

    @RequiresPermissions(value = {"sys:variable:post", "sys:variable:put"}, logical = Logical.OR)
    @RequestMapping(name = "编辑系统变量页面", path = "html/edit", method = RequestMethod.GET)
    public String edit(Map<String, Object> params) {
        params.put("varTypes", SysVariableType.values());
        return "sys/sysVariable/sysVariableEdit";
    }
    // end#html

}