package cs.controller.financial;

import java.text.ParseException;
import java.util.Map;

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
import cs.model.financial.FinancialManagerDto;
import cs.model.project.SignDto;
import cs.repository.odata.ODataObj;
import cs.service.financial.FinancialManagerService;

/**
 * Description: 财务管理 控制层
 * author: sjy
 * Date: 2017-8-7 11:32:03
 */
@Controller
@RequestMapping(name = "财务管理", path = "financialManager")
public class FinancialManagerController {

	String ctrlName = "financial";
    @Autowired
    private FinancialManagerService financialManagerService;

    @RequiresPermissions("financialManager#findByOData#post")
    @RequestMapping(name = "获取已办理项目评审费数据", path = "findByOData", method = RequestMethod.POST)
    @ResponseBody
    public PageModelDto<SignDto> get(HttpServletRequest request) throws ParseException {
        ODataObj odataObj = new ODataObj(request);
        PageModelDto<SignDto> financialManagerDtos = financialManagerService.get(odataObj);	
        return financialManagerDtos;
    }
    
    @RequiresPermissions("financialManager#initfinancial#get")
    @RequestMapping(name = "初始化财务页面", path = "initfinancial", method = RequestMethod.GET)
    public @ResponseBody
    Map<String, Object> initfinancial(@RequestParam String signid) throws Exception {
        Map<String, Object> map = financialManagerService.initfinancialData(signid);
        return map;
    }
    @RequiresPermissions("financialManager#html/sumfinancial#get")
    @RequestMapping(name = "统计评审总费用", path = "html/sumfinancial", method = RequestMethod.GET)
    @ResponseBody
    public Integer sunFinancial(@RequestParam String signId){
    	Integer intsumcount = financialManagerService.sunCount(signId);
    	return intsumcount;
    }

    @RequiresPermissions("financialManager##post")
    @RequestMapping(name = "创建记录", path = "", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.CREATED)
    public void post(@RequestBody FinancialManagerDto record[]) {
    	for(FinancialManagerDto financialDto : record){
    		financialManagerService.save(financialDto);
    	}
    }

	@RequestMapping(name = "主键查询", path = "html/findById",method=RequestMethod.GET)
	public @ResponseBody FinancialManagerDto findById(@RequestParam(required = true)String id){		
		return financialManagerService.findById(id);
	}
	
    @RequiresPermissions("financialManager##delete")
    @RequestMapping(name = "删除记录", path = "", method = RequestMethod.DELETE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void delete(@RequestBody String id) {
    	financialManagerService.delete(id);      
    }

    @RequiresPermissions("financialManager##put")
    @RequestMapping(name = "更新记录", path = "", method = RequestMethod.PUT)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void put(@RequestBody FinancialManagerDto record) {
        financialManagerService.update(record);
    }

    // begin#html
    @RequiresPermissions("financialManager#html/list#get")
    @RequestMapping(name = "列表页面", path = "html/list", method = RequestMethod.GET)
    public String list() {
        return ctrlName+"/list"; 
    }

    @RequiresPermissions("financialManager#html/add#get")
    @RequestMapping(name = "评审费用报销", path = "html/add", method = RequestMethod.GET)
    public String edit() {
        return ctrlName+"/add";
    }
    
   
}