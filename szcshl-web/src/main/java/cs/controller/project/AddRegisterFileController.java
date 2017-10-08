package cs.controller.project;

import cs.ahelper.IgnoreAnnotation;
import cs.common.ResultMsg;
import cs.model.PageModelDto;
import cs.model.project.AddRegisterFileDto;
import cs.repository.odata.ODataFilterItem;
import cs.repository.odata.ODataObj;
import cs.service.project.AddRegisterFileService;

import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.WebUtils;

import com.alibaba.druid.filter.Filter;
import com.alibaba.fastjson.JSON;

import javax.management.modelmbean.ModelMBeanInfoSupport;
import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Description: 登记补充资料 控制层 author: ldm Date: 2017-8-3 15:26:51
 */
@Controller
@RequestMapping(name = "登记补充资料", path = "addRegisterFile")
@IgnoreAnnotation
public class AddRegisterFileController {

	String ctrlName = "addRegisterFile";
	@Autowired
	private AddRegisterFileService addRegisterFileService;

    @RequiresAuthentication
    //@RequiresPermissions("addRegisterFile#findByOData#post")
	@RequestMapping(name = "获取数据", path = "findByOData", method = RequestMethod.POST)
	@ResponseBody
	public PageModelDto<AddRegisterFileDto> get(HttpServletRequest request) throws ParseException {
		ODataObj odataObj = new ODataObj(request);
		PageModelDto<AddRegisterFileDto> addRegisterFileDtos = addRegisterFileService.get(odataObj);
		return addRegisterFileDtos;
	}

    @RequiresAuthentication
    //@RequiresPermissions("addRegisterFile#initFindByOData#get")
	@RequestMapping(name = "初始化登记表数据", path = "initFindByOData", method = RequestMethod.GET)
	@ResponseBody
	public List<AddRegisterFileDto> initFindByOData(HttpServletRequest request) throws ParseException {
		ODataObj odataObj = new ODataObj(request);
		List<AddRegisterFileDto> addRegisterFileList = addRegisterFileService.initRegisterFileData(odataObj);
		return addRegisterFileList;
	}

    @RequiresAuthentication
    //@RequiresPermissions("addRegisterFile#save#post")
	@RequestMapping(name = "创建记录", path = "save", method = RequestMethod.POST)
	@ResponseBody
	public ResultMsg create(@RequestBody AddRegisterFileDto[] addRegisterFileDtos) throws ParseException {
		return addRegisterFileService.bathSave(addRegisterFileDtos);
	}

    @RequiresAuthentication
    //@RequiresPermissions("addRegisterFile#initprintdata#post")
	@RequestMapping(name = "初始化打印資料頁面", path = "initprintdata", method = RequestMethod.POST)
	public @ResponseBody Map<String,Object> initpint(@RequestParam String signid) throws ParseException {
		 Map<String,Object> map = addRegisterFileService.initprint(signid);
		return map;
	}

    @RequiresAuthentication
    //@RequiresPermissions("addRegisterFile#findByBusinessId#post")
	@RequestMapping(name = "根据业务ID查询补充资料信息", path = "findByBusinessId", method = RequestMethod.POST)
	public @ResponseBody List<AddRegisterFileDto> findByBusinessId(@RequestParam String businessId){
		return addRegisterFileService.findByBusinessId(businessId);
	}

    @RequiresAuthentication
	@RequestMapping(name = "主键查询", path = "html/findById", method = RequestMethod.GET)
	public @ResponseBody AddRegisterFileDto findById(@RequestParam(required = true) String id) {
		return addRegisterFileService.findById(id);
	}

    @RequiresAuthentication
    //@RequiresPermissions("addRegisterFile#deleteFile#delete")
	@RequestMapping(name = "删除记录", path = "deleteFile", method = RequestMethod.DELETE)
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	public void delete(@RequestParam String ids) {
		addRegisterFileService.deleteRegisterFile(ids);
	}

    @RequiresAuthentication
    //@RequiresPermissions("addRegisterFile#findbySuppdate#post")
	@RequestMapping(name = "根据日期查找资料", path = "findbySuppdate", method = RequestMethod.POST)
	public @ResponseBody List<AddRegisterFileDto> findbySuppdate(@RequestParam String suppDate) throws ParseException {
		List<AddRegisterFileDto> list = addRegisterFileService.findbySuppdate(suppDate);
		return list;
	}

	// begin#html
    @RequiresAuthentication
    //@RequiresPermissions("addRegisterFile#html/list#get")
	@RequestMapping(name = "列表页面", path = "list", method = RequestMethod.GET)
	public String list() {
		return ctrlName + "/list";
	}

    @RequiresAuthentication
	//@RequiresPermissions("addRegisterFile#addRegisterFile/edit#get")
	@RequestMapping(name = "编辑页面", path = "edit", method = RequestMethod.GET)
	public String edit() {
		return ctrlName + "/edit";
	}
	// end#html

}