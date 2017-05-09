package cs.controller.sys;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

import cs.model.PageModelDto;
import cs.model.sys.DictDto;
import cs.model.sys.OrgDto;
import cs.model.sys.UserDto;
import cs.repository.odata.ODataObj;
import cs.service.sys.DictService;

@Controller
@RequestMapping(name = "数据字典", path = "dict")
public class DictController {

	private String ctrlName = "dict";
	
	@Autowired
	private DictService dictService;
	
	@RequiresPermissions("dict##get")	
	@RequestMapping(name = "获取字典数据", path = "", method = RequestMethod.GET)
	public @ResponseBody PageModelDto<DictDto> get(HttpServletRequest request) throws ParseException {
		ODataObj odataObj = new ODataObj(request);
		PageModelDto<DictDto> dictDtos = dictService.get(odataObj);
		List<DictDto> dtos = dictService.getDictItemByCode("DICT_SEX");		
		return dictDtos;
	}
	
	
	@RequiresPermissions("dict#dictItems#get")	
	@RequestMapping(name = "获取字典数据", path = "dictItems", method = RequestMethod.GET)
	public @ResponseBody List<DictDto> getDictItems(String dictCode) throws ParseException {		
		return dictService.getDictItemByCode(dictCode);		
	}
	
	@RequiresPermissions("dict#dictNameData#get")	
	@RequestMapping(name = "获取字典数据字面值", path = "dictNameData", method = RequestMethod.GET)
	public @ResponseBody Map<String, String> dictNameData(@RequestParam(required = true) String dictCode) throws ParseException {				
		return dictService.getDictNameByCode(dictCode);				
	}
	
	@RequiresPermissions("dict##post")
	@RequestMapping(name = "创建字典", path = "",method=RequestMethod.POST)	
	@ResponseStatus(value = HttpStatus.CREATED)
	public void  post(@RequestBody DictDto dictDto)  {		
		dictService.createDict(dictDto);	
	}
	
	@RequiresPermissions("dict##put")
	@RequestMapping(name = "更新字典", path = "",method=RequestMethod.PUT)	
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	public void  put(@RequestBody DictDto dictDto)  {		
		dictService.updateDict(dictDto);		
	}
	
	@RequiresPermissions("dict##delete")
	@RequestMapping(name = "删除字典", path = "",method=RequestMethod.DELETE)	
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	public void  delete(@RequestBody String id)  {		
		String[] ids=id.split(",");
		if(ids.length>1){
			dictService.deleteDicts(ids);
		}else{
			dictService.deleteDict(ids[0]);
		}		
	}
	
	@RequiresPermissions("dict#html/list#get")
	@RequestMapping(name = "数据字典列表页面", path = "html/list", method = RequestMethod.GET)
	public String list() {
		return ctrlName + "/list";
	}
	
	@RequiresPermissions("dict#html/edit#get")
	@RequestMapping(name = "编辑页面", path = "html/edit", method = RequestMethod.GET)
	public String edit() {
		
	
		return ctrlName + "/edit";
	}
	
}
