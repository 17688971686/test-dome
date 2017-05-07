<#if controllerPackage??>package ${controllerPackage};</#if>

import cs.model.PageModelDto;
import cs.model.demo.${beanName}Dto;
import cs.repository.odata.ODataObj;
import cs.service.demo.${beanName}Service;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.Date;

/**
* Description: ${comment!''} 控制层
* author: ${author!''}
* Date: ${.now}
*/
@Controller
@RequestMapping(name = "${comment!''}", path = "${beanName?uncap_first}")
public class ${beanName}Controller {

    @Autowired
    private ${beanName}Service ${beanName?uncap_first}Service;

    @RequiresPermissions("${beanName?uncap_first}##get")
    @RequestMapping(name = "获取数据", path = "", method = RequestMethod.GET)
    @ResponseBody
    public PageModelDto<${beanName}Dto> get(HttpServletRequest request) throws ParseException {
        ODataObj odataObj = new ODataObj(request);
        return ${beanName?uncap_first}Service.getDto(odataObj);
    }

    @RequiresPermissions("${beanName?uncap_first}##post")
    @RequestMapping(name = "创建记录", path = "", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.CREATED)
    public void post(@RequestBody ${beanName}Dto record) {
        ${beanName?uncap_first}Service.create(record);
    }

    @RequiresPermissions("${beanName?uncap_first}##delete")
    @RequestMapping(name = "删除记录", path = "", method = RequestMethod.DELETE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void delete(@RequestBody String id) {
        String[] ids = id.split(",");
        if (ids.length > 1) {
            ${beanName?uncap_first}Service.delete(ids);
        } else {
            ${beanName?uncap_first}Service.delete(id);
        }
    }

    @RequiresPermissions("${beanName?uncap_first}##put")
    @RequestMapping(name = "更新记录", path = "", method = RequestMethod.PUT)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void put(@RequestBody ${beanName}Dto record) {
        ${beanName?uncap_first}Service.update(record);
    }

    // begin#html
    @RequiresPermissions("${beanName?uncap_first}#html/list#get")
    @RequestMapping(name = "列表页面", path = "html/list", method = RequestMethod.GET)
    public String list() {
        return "${beanName?uncap_first}/list";
    }

    @RequiresPermissions("${beanName?uncap_first}#html/edit#get")
    @RequestMapping(name = "编辑页面", path = "html/edit", method = RequestMethod.GET)
    public String edit() {
        return "${beanName?uncap_first}/edit";
    }
    // end#html

}