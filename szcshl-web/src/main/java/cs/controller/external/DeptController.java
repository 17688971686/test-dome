package cs.controller.external;

import java.text.ParseException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import cs.model.PageModelDto;
import cs.model.external.DeptDto;
import cs.repository.odata.ODataObj;
import cs.service.external.DeptService;

@Controller
@RequestMapping(name = "部门", path = "dept")
public class DeptController {

	String ctrlName = "dept";
	
    @Autowired
    private DeptService deptService;

    @RequiresPermissions("dept##get")
    @RequestMapping(name = "获取数据", path = "", method = RequestMethod.GET)
    @ResponseBody
    public PageModelDto<DeptDto> get(HttpServletRequest request) throws ParseException {
        ODataObj odataObj = new ODataObj(request);
        PageModelDto<DeptDto> deptDtos = deptService.get(odataObj);	
        return deptDtos;
    }

    @RequiresPermissions("dept##post")
    @RequestMapping(name = "创建记录", path = "", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.CREATED)
    public void post(@RequestBody DeptDto record) {
        deptService.save(record);
    }

    @RequiresPermissions("dept##put")
    @RequestMapping(name = "更新记录", path = "", method = RequestMethod.PUT)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void put(@RequestBody DeptDto record) {
        deptService.update(record);
    }
    
    @RequestMapping(name = "主键查询", path = "html/findById",method=RequestMethod.GET)	
	@Transactional
	public @ResponseBody DeptDto findById(@RequestParam(required = true)String deptId){		
		return deptService.findById(deptId);
	}

    @RequiresPermissions("dept##delete")
    @RequestMapping(name = "删除记录", path = "", method = RequestMethod.DELETE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void delete(@RequestBody String id) {
    	deptService.delete(id);
    }
    
    @RequiresPermissions("dept#html/list#get")
    @RequestMapping(name = "列表页面", path = "html/list", method = RequestMethod.GET)
    public String list() {
        return ctrlName+"/list";
    }

    @RequiresPermissions("dept#html/edit#get")
    @RequestMapping(name = "编辑页面", path = "html/edit", method = RequestMethod.GET)
    public String edit() {
        return ctrlName+"/edit";
    }

}