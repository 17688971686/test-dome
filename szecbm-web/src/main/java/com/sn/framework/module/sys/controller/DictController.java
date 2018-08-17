package com.sn.framework.module.sys.controller;

import com.sn.framework.core.syslog.OperatorType;
import com.sn.framework.core.syslog.SysLog;
import com.sn.framework.module.sys.model.DictDto;
import com.sn.framework.module.sys.service.IDictService;
import com.sn.framework.odata.impl.jpa.OdataJPA;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.sn.framework.core.Constants.ROLE_KEY_ADMIN;
import static com.sn.framework.common.StringUtil.SEPARATE_COMMA;

/**
 * Description:
 * @Author: qbl
 * @Date: 2017/9/11 17:11
 */
@Controller
@RequestMapping(name = "字典管理", path = "sys/dict")
public class DictController {

    @Autowired
    private IDictService dictService;

    @RequestMapping(name = "获取字典数据", path = "", method = RequestMethod.GET)
    @ResponseBody
    public List<DictDto> get(OdataJPA odata) {
        return dictService.findByOdata(odata);
    }

    @RequestMapping(name = "获取所有可用字典数据", path = "all", method = RequestMethod.GET)
    @ResponseBody
    public List<DictDto> getAll() {
        return dictService.findAll();
    }

    @RequestMapping(name = "根据id获取字典数据", path = "{dictId}", method = RequestMethod.GET)
    @ResponseBody
    public DictDto findById(@PathVariable String dictId) {
        return dictService.getById(dictId);
    }

    @RequiresPermissions("sys:dict:post")
    @RequestMapping(name = "创建字典", path = "", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.CREATED)
    @SysLog(businessType = "数据字典",operatorType = OperatorType.ADD,serviceclass = IDictService.class,idName = "dictId")
    public void post(@RequestBody DictDto dictDto) {
        dictService.create(dictDto);
    }

    @RequiresPermissions("sys:dict:put")
    @RequestMapping(name = "更新字典", path = "", method = RequestMethod.PUT)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @SysLog(businessType = "数据字典",operatorType = OperatorType.UPDATE,serviceclass = IDictService.class,idName = "dictId")
    public void put(@RequestBody DictDto dictDto) {
        dictService.update(dictDto);
    }

    @RequiresPermissions("sys:dict:delete")
    @RequestMapping(name = "删除字典", path = "", method = RequestMethod.DELETE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @SysLog(businessType = "数据字典",operatorType = OperatorType.DELETE,serviceclass = IDictService.class,idName = "dictId")
    public void delete(String dictId) {
        if (dictId.contains(SEPARATE_COMMA)) {
            dictService.deleteByIds(dictId.split(SEPARATE_COMMA));
        } else {
            dictService.deleteById(dictId);
        }
    }

    @RequiresRoles(ROLE_KEY_ADMIN)
    @RequestMapping(name = "重置字典", path = "reset", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void resetDict() throws Exception {
        dictService.initDictData();
    }

    //    @RequiresPermissions(ModuleDefine.SYS +"#dict#html/list#get")
    @RequestMapping(name = "数据字典列表页面", path = "html/list", method = RequestMethod.GET)
    public String list() {
        return "sys/dict/dictList";
    }

    //    @RequiresPermissions(ModuleDefine.SYS +"#dict#html/edit#get")
    @RequestMapping(name = "数据字典编辑页面", path = "html/edit", method = RequestMethod.GET)
    public String edit() {
        return "sys/dict/dictEdit";
    }
}