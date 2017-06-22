<#if layer??>package ${layer};</#if>

import cs.model.PageModelDto;
<#if info.Dto??>
import ${info.DtoLayer}.${info.Dto};
</#if>
import cs.repository.odata.ODataObj;
<#if info.Service??>
import ${info.ServiceLayer}.${info.Service};
</#if>

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.Date;

/**
 * Description: ${info.comment!''} 控制层
 * author: ${info.author!''}
 * Date: ${.now}
 */
@Controller
@RequestMapping(name = "${info.comment!''}", path = "${info.beanName?uncap_first}")
public class ${fileName!''} {

	String ctrlName = "${info.beanName?uncap_first}";
    @Autowired
    private ${info.beanName!''}Service ${info.beanName?uncap_first}Service;

    @RequiresPermissions("${info.beanName?uncap_first}#findByOData#post")
    @RequestMapping(name = "获取数据", path = "findByOData", method = RequestMethod.POST)
    @ResponseBody
    public PageModelDto<${info.beanName}Dto> get(HttpServletRequest request) throws ParseException {
        ODataObj odataObj = new ODataObj(request);
        PageModelDto<${info.beanName}Dto> ${info.beanName?uncap_first}Dtos = ${info.beanName?uncap_first}Service.get(odataObj);	
        return ${info.beanName?uncap_first}Dtos;
    }

    @RequiresPermissions("${info.beanName?uncap_first}##post")
    @RequestMapping(name = "创建记录", path = "", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.CREATED)
    public void post(@RequestBody ${info.beanName}Dto record) {
        ${info.beanName?uncap_first}Service.save(record);
    }

	@RequestMapping(name = "主键查询", path = "html/findById",method=RequestMethod.GET)
	public @ResponseBody ${info.beanName}Dto findById(@RequestParam(required = true)String id){		
		return ${info.beanName?uncap_first}Service.findById(id);
	}
	
    @RequiresPermissions("${info.beanName?uncap_first}##delete")
    @RequestMapping(name = "删除记录", path = "", method = RequestMethod.DELETE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void delete(@RequestBody String id) {
    	${info.beanName?uncap_first}Service.delete(id);      
    }

    @RequiresPermissions("${info.beanName?uncap_first}##put")
    @RequestMapping(name = "更新记录", path = "", method = RequestMethod.PUT)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void put(@RequestBody ${info.beanName}Dto record) {
        ${info.beanName?uncap_first}Service.update(record);
    }

    // begin#html
    @RequiresPermissions("${info.beanName?uncap_first}#html/list#get")
    @RequestMapping(name = "列表页面", path = "html/list", method = RequestMethod.GET)
    public String list() {
        return ctrlName+"/list"; 
    }

    @RequiresPermissions("${info.beanName?uncap_first}#html/edit#get")
    @RequestMapping(name = "编辑页面", path = "html/edit", method = RequestMethod.GET)
    public String edit() {
        return ctrlName+"/edit";
    }
    // end#html

}