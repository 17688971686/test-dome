package cs.controller.monthly;

import cs.model.PageModelDto;
import cs.model.monthly.MonthlyNewsletterDto;
import cs.repository.odata.ODataObj;
import cs.service.monthly.MonthlyNewsletterService;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.Date;

/**
 * Description: 月报简报 控制层
 * author: sjy
 * Date: 2017-9-8 11:23:41
 */
@Controller
@RequestMapping(name = "月报简报", path = "monthlyNewsletter")
public class MonthlyNewsletterController {

	String ctrlName = "monthlyNewsletter";
    @Autowired
    private MonthlyNewsletterService monthlyNewsletterService;

    @RequiresPermissions("monthlyNewsletter#findByOData#post")
    @RequestMapping(name = "获取数据", path = "findByOData", method = RequestMethod.POST)
    @ResponseBody
    public PageModelDto<MonthlyNewsletterDto> get(HttpServletRequest request) throws ParseException {
        ODataObj odataObj = new ODataObj(request);
        PageModelDto<MonthlyNewsletterDto> monthlyNewsletterDtos = monthlyNewsletterService.get(odataObj);	
        return monthlyNewsletterDtos;
    }
    
    @RequiresPermissions("monthlyNewsletter#getMonthlyList#post")
    @RequestMapping(name = "获取月报简报管理数据列表", path = "getMonthlyList", method = RequestMethod.POST)
    @ResponseBody
    public PageModelDto<MonthlyNewsletterDto> getMonthlyList(HttpServletRequest request) throws ParseException {
        ODataObj odataObj = new ODataObj(request);
        PageModelDto<MonthlyNewsletterDto> monthlyNewsletterDtos = monthlyNewsletterService.getMonthlyList(odataObj);	
        return monthlyNewsletterDtos;
    }
    
    @RequiresPermissions("monthlyNewsletter#deleteMonthlyList#post")
    @RequestMapping(name = "删除月报简报管理数据列表", path = "deleteMonthlyList", method = RequestMethod.POST)
    @ResponseBody
    public PageModelDto<MonthlyNewsletterDto> deleteMonthlyList(HttpServletRequest request) throws ParseException {
        ODataObj odataObj = new ODataObj(request);
        PageModelDto<MonthlyNewsletterDto> monthlyNewsletterDtos = monthlyNewsletterService.deleteMonthlyList(odataObj);	
        return monthlyNewsletterDtos;
    }

    @RequiresPermissions("monthlyNewsletter#savaMonthlyNewsletter#post")
    @RequestMapping(name = "保存月报简报", path = "savaMonthlyNewsletter", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.CREATED)
    public void post(@RequestBody MonthlyNewsletterDto record) {
        monthlyNewsletterService.saveTheMonthly(record);
    }

    @RequiresPermissions("monthlyNewsletter#deleteMonthlyData#delete")
	@RequestMapping(name = "删除月报简报记录", path = "deleteMonthlyData", method = RequestMethod.DELETE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteMonthlyData(@RequestBody String id) {
    	String [] ids = id.split(",");
    	if(ids.length > 1){
    		monthlyNewsletterService.deleteMonthlyDatas(ids);
    	}else{
    		monthlyNewsletterService.deleteMonthlyData(id);      
    	}
    }
    
    
	@RequestMapping(name = "主键查询", path = "html/findById",method=RequestMethod.GET)
	public @ResponseBody MonthlyNewsletterDto findById(@RequestParam(required = true)String id){		
		return monthlyNewsletterService.findById(id);
	}
	
	@RequiresPermissions("monthlyNewsletter#savaHistory#post")
    @RequestMapping(name = "保存月报简报历史数据", path = "savaHistory", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.CREATED)
    public void savaHistory(@RequestBody MonthlyNewsletterDto record) {
        monthlyNewsletterService.save(record);
    }
	
	@RequiresPermissions("monthlyNewsletter#deleteHistoryList#post")
	@RequestMapping(name = "删除月报简报历史列表", path = "deleteHistoryList", method = RequestMethod.POST)
	@ResponseBody
	public PageModelDto<MonthlyNewsletterDto> deleteHistoryList(HttpServletRequest request) throws ParseException {
		ODataObj odataObj = new ODataObj(request);
		PageModelDto<MonthlyNewsletterDto> monthlyNewsletterDtos = monthlyNewsletterService.deleteHistoryList(odataObj);	
		return monthlyNewsletterDtos;
	}
	
	@RequiresPermissions("monthlyNewsletter#mothlyHistoryList#post")
	@RequestMapping(name = "月报简报历史数据列表", path = "mothlyHistoryList", method = RequestMethod.POST)
	@ResponseBody
	public PageModelDto<MonthlyNewsletterDto> mothlyHistoryList(HttpServletRequest request) throws ParseException {
		ODataObj odataObj = new ODataObj(request);
		PageModelDto<MonthlyNewsletterDto> monthlyNewsletterDtos = monthlyNewsletterService.mothlyHistoryList(odataObj);	
		return monthlyNewsletterDtos;
	}
	
	@RequiresPermissions("monthlyNewsletter#deleteHistory#delete")
	@RequestMapping(name = "删除月报简报历史记录", path = "deleteHistory", method = RequestMethod.DELETE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void delete(@RequestBody String id) {
    	String [] ids = id.split(",");
    	if(ids.length > 1){
    		monthlyNewsletterService.deletes(ids);
    	}else{
    		monthlyNewsletterService.delete(id);      
    	}
    }

    @RequiresPermissions("monthlyNewsletter##put")
    @RequestMapping(name = "更新记录", path = "", method = RequestMethod.PUT)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void put(@RequestBody MonthlyNewsletterDto record) {
        monthlyNewsletterService.update(record);
    }

    // begin#html
    @RequiresPermissions("monthlyNewsletter#html/monthlyNewsletterList#get")
    @RequestMapping(name = "月报简报列表页面", path = "html/monthlyNewsletterList", method = RequestMethod.GET)
    public String monthlyNewsletterList() {
        return ctrlName+"/monthlyNewsletterList"; 
    }

    @RequiresPermissions("monthlyNewsletter#html/theMonthsList#get")
    @RequestMapping(name = "月报列表页面", path = "html/theMonthsList", method = RequestMethod.GET)
    public String theMonthsList() {
        return ctrlName+"/theMonthsList"; 
    } 
    
    @RequiresPermissions("monthlyNewsletter#html/monthlyHistoryList#get")
    @RequestMapping(name = "月报简报历史数据列表页面", path = "html/monthlyHistoryList", method = RequestMethod.GET)
    public String monthsHistoryList() {
        return ctrlName+"/monthlyHistoryList"; 
    } 
    
    @RequiresPermissions("monthlyNewsletter#html/monthlyHistoryAdd#get")
    @RequestMapping(name = "月报简报历史数据新建页面", path = "html/monthlyHistoryAdd", method = RequestMethod.GET)
    public String monthsHistoryAdd() {
        return ctrlName+"/monthlyHistoryAdd"; 
    } 
    
    @RequiresPermissions("monthlyNewsletter#html/monthlyMultiyearList#get")
    @RequestMapping(name = "月报简报年度列表页面", path = "html/monthlyMultiyearList", method = RequestMethod.GET)
    public String monthlyMultiyear() {
        return ctrlName+"/monthlyMultiyearList"; 
    } 
    
    @RequiresPermissions("monthlyNewsletter#html/monthlyMultiyearAdd#get")
    @RequestMapping(name = "新建月报简报年度页面", path = "html/monthlyMultiyearAdd", method = RequestMethod.GET)
    public String monthlyMultiyearAdd() {
        return ctrlName+"/monthlyMultiyearAdd"; 
    } 
    
    @RequiresPermissions("monthlyNewsletter#html/monthlyNewsletterEdit#get")
    @RequestMapping(name = "编辑页面", path = "html/monthlyNewsletterEdit", method = RequestMethod.GET)
    public String edit() {
        return ctrlName+"/monthlyNewsletterEdit";
    }
    // end#html

}