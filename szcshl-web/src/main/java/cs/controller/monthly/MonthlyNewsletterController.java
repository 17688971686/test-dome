package cs.controller.monthly;

import cs.ahelper.MudoleAnnotation;
import cs.common.ResultMsg;
import cs.model.PageModelDto;
import cs.model.monthly.MonthlyNewsletterDto;
import cs.model.project.AddSuppLetterDto;
import cs.repository.odata.ODataObj;
import cs.service.monthly.MonthlyNewsletterService;
import cs.service.project.AddSuppLetterService;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.List;

/**
 * Description: 月报简报 控制层
 * author: sjy
 * Date: 2017-9-8 11:23:41
 */
@Controller
@RequestMapping(name = "月报简报", path = "monthlyNewsletter")
@MudoleAnnotation(name = "项目管理",value = "permission#sign")
public class MonthlyNewsletterController {

	String ctrlName = "monthlyNewsletter";
    @Autowired
    private MonthlyNewsletterService monthlyNewsletterService;

    @Autowired
    private AddSuppLetterService addSuppLetterService;

    @RequiresAuthentication
    //@RequiresPermissions("monthlyNewsletter#findByOData#post")
    @RequestMapping(name = "获取数据", path = "findByOData", method = RequestMethod.POST)
    @ResponseBody
    public PageModelDto<MonthlyNewsletterDto> get(HttpServletRequest request) throws ParseException {
        ODataObj odataObj = new ODataObj(request);
        PageModelDto<MonthlyNewsletterDto> monthlyNewsletterDtos = monthlyNewsletterService.get(odataObj);	
        return monthlyNewsletterDtos;
    }

    @RequiresAuthentication
    //@RequiresPermissions("monthlyNewsletter#saveMonthlyMultiyear#post")
    @RequestMapping(name = "保存（中心）文件稿纸", path = "saveMonthlyMultiyear", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.CREATED)
    public @ResponseBody ResultMsg monthlyMultiyearAdd(@RequestBody AddSuppLetterDto record) {
       return addSuppLetterService.saveMonthlyMultiyear(record);
    }

    @RequiresAuthentication
    //@RequiresPermissions("monthlyNewsletter#initMonthlyMultiyear#post")
    @RequestMapping(name = "初始化（中心）文件稿纸", path = "initMonthlyMultiyear", method = RequestMethod.POST)
    @ResponseBody
    public AddSuppLetterDto initMutilyear(){
    	return addSuppLetterService.initMonthlyMutilyear();
    }


    @RequiresAuthentication
    //@RequiresPermissions("monthlyNewsletter#monthlyMultiyearList#post")
    @RequestMapping(name = "获取（中心）文件查询列表数据", path = "monthlyMultiyearList", method = RequestMethod.POST)
    @ResponseBody
    public PageModelDto<AddSuppLetterDto> monthlyMultiyearList(HttpServletRequest request) throws ParseException {
        ODataObj odataObj = new ODataObj(request);
        PageModelDto<AddSuppLetterDto> addSuppLetterDtos = addSuppLetterService.monthlyMultiyearListData(odataObj);	
        return addSuppLetterDtos;
    }

    @RequiresAuthentication
    //@RequiresPermissions("monthlyNewsletter#monthlyAppoveList#post")
    @RequestMapping(name = "获取（中心）文件审批列表数据", path = "monthlyAppoveList", method = RequestMethod.POST)
    @ResponseBody
    public PageModelDto<AddSuppLetterDto> monthlyAppoveList(HttpServletRequest request) throws ParseException {
        ODataObj odataObj = new ODataObj(request);
        PageModelDto<AddSuppLetterDto> addSuppLetterDtos = addSuppLetterService.monthlyAppoveListData(odataObj);	
        return addSuppLetterDtos;
    }

    @RequiresAuthentication
    //@RequiresPermissions("monthlyNewsletter#updateApprove#post")
    @RequestMapping(name = "领导审批（中心文件）处理", path = "updateApprove", method = RequestMethod.POST)
    @ResponseBody
    public void updateApprove(@RequestBody AddSuppLetterDto addSuppLetterDto){
		addSuppLetterService.monthlyApproveEdit(addSuppLetterDto);
    }

    @RequiresAuthentication
    //@RequiresPermissions("monthlyNewsletter#deleteMutiyear#delete")
 	@RequestMapping(name = "删除年度（中心）月报简报记录", path = "deleteMutiyear", method = RequestMethod.DELETE)
     @ResponseStatus(value = HttpStatus.NO_CONTENT)
     public void deleteMutiyear(@RequestBody String id) {
     	String [] ids = id.split(",");
     	if(ids.length > 1){
     		addSuppLetterService.deletes(ids);
     	}else{
     		addSuppLetterService.delete(id);      
     	}
     }

     @RequiresAuthentication
     @RequestMapping(name="查询主页上的月报简报审批处理信息" , path = "findHomeMonthly" , method = RequestMethod.GET)
     @ResponseBody
     public List<AddSuppLetterDto> findHomeMonthly(){
        return addSuppLetterService.findHomeMonthly();
     }

    @RequiresAuthentication
    //@RequiresPermissions("monthlyNewsletter#getMonthlyList#post")
    @RequestMapping(name = "获取月报简报管理数据列表", path = "getMonthlyList", method = RequestMethod.POST)
    @ResponseBody
    public PageModelDto<MonthlyNewsletterDto> getMonthlyList(HttpServletRequest request) throws ParseException {
        ODataObj odataObj = new ODataObj(request);
        PageModelDto<MonthlyNewsletterDto> monthlyNewsletterDtos = monthlyNewsletterService.getMonthlyList(odataObj);	
        return monthlyNewsletterDtos;
    }

    @RequiresAuthentication
    //@RequiresPermissions("monthlyNewsletter#deleteMonthlyList#post")
    @RequestMapping(name = "删除月报简报管理数据列表", path = "deleteMonthlyList", method = RequestMethod.POST)
    @ResponseBody
    public PageModelDto<MonthlyNewsletterDto> deleteMonthlyList(HttpServletRequest request) throws ParseException {
        ODataObj odataObj = new ODataObj(request);
        PageModelDto<MonthlyNewsletterDto> monthlyNewsletterDtos = monthlyNewsletterService.deleteMonthlyList(odataObj);	
        return monthlyNewsletterDtos;
    }

    @RequiresAuthentication
    //@RequiresPermissions("monthlyNewsletter#savaMonthlyNewsletter#post")
    @RequestMapping(name = "保存月报简报", path = "savaMonthlyNewsletter", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.CREATED)
    public @ResponseBody ResultMsg post(@RequestBody MonthlyNewsletterDto record) {
       return monthlyNewsletterService.saveTheMonthly(record);
    }

    @RequiresAuthentication
    //@RequiresPermissions("monthlyNewsletter#savaMonthlyNewsletter#post")
    @RequestMapping(name = "编辑月报简报", path = "monthlyEdit", method = RequestMethod.PUT)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public  void monthlyEdit(@RequestBody MonthlyNewsletterDto record) {
       monthlyNewsletterService.editTheMonthly(record);
    }

    @RequiresAuthentication
    //@RequiresPermissions("monthlyNewsletter#deleteMonthlyData#delete")
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

    @RequiresAuthentication
	@RequestMapping(name = "主键查询", path = "html/findById",method=RequestMethod.GET)
	public @ResponseBody MonthlyNewsletterDto findById(@RequestParam(required = true)String id){		
		return monthlyNewsletterService.findById(id);
	}

    @RequiresAuthentication
    //@RequiresPermissions("monthlyNewsletter#savaHistory#post")
    @RequestMapping(name = "保存月报简报历史数据", path = "savaHistory", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.CREATED)
    public @ResponseBody ResultMsg savaHistory(@RequestBody MonthlyNewsletterDto record) {
       return monthlyNewsletterService.save(record);
    }

    @RequiresAuthentication
    //@RequiresPermissions("monthlyNewsletter#deleteHistoryList#post")
	@RequestMapping(name = "删除月报简报历史列表", path = "deleteHistoryList", method = RequestMethod.POST)
	@ResponseBody
	public PageModelDto<MonthlyNewsletterDto> deleteHistoryList(HttpServletRequest request) throws ParseException {
		ODataObj odataObj = new ODataObj(request);
		PageModelDto<MonthlyNewsletterDto> monthlyNewsletterDtos = monthlyNewsletterService.deleteHistoryList(odataObj);	
		return monthlyNewsletterDtos;
	}

    @RequiresAuthentication
    //@RequiresPermissions("monthlyNewsletter#mothlyHistoryList#post")
	@RequestMapping(name = "月报简报历史数据列表", path = "mothlyHistoryList", method = RequestMethod.POST)
	@ResponseBody
	public PageModelDto<MonthlyNewsletterDto> mothlyHistoryList(HttpServletRequest request) throws ParseException {
		ODataObj odataObj = new ODataObj(request);
		PageModelDto<MonthlyNewsletterDto> monthlyNewsletterDtos = monthlyNewsletterService.mothlyHistoryList(odataObj);	
		return monthlyNewsletterDtos;
	}

    @RequiresAuthentication
    //@RequiresPermissions("monthlyNewsletter#deleteHistory#delete")
	@RequestMapping(name = "删除月报简报历史数据记录", path = "deleteHistory", method = RequestMethod.DELETE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void delete(@RequestBody String id) {
    	String [] ids = id.split(",");
    	if(ids.length > 1){
    		monthlyNewsletterService.deletes(ids);
    	}else{
    		monthlyNewsletterService.delete(id);      
    	}
    }

    @RequiresAuthentication
    //@RequiresPermissions("monthlyNewsletter##put")
    @RequestMapping(name = "更新记录", path = "", method = RequestMethod.PUT)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void put(@RequestBody MonthlyNewsletterDto record) {
        monthlyNewsletterService.update(record);
    }

    @RequiresAuthentication
    @RequestMapping(name = "生成月报简报", path = "createMonthReport", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.CREATED)
    public @ResponseBody ResultMsg createMonthReport(@RequestBody MonthlyNewsletterDto monthlyNewsletterDto) {
        return monthlyNewsletterService.createMonthTemplate(monthlyNewsletterDto);
    }

    // begin#html
    @RequiresPermissions("monthlyNewsletter#html/monthlyNewsletterList#get")
    @RequestMapping(name = "月报简报列表", path = "html/monthlyNewsletterList", method = RequestMethod.GET)
    public String monthlyNewsletterList() {
        return ctrlName+"/monthlyNewsletterList"; 
    }

    @RequiresPermissions("monthlyNewsletter#html/theMonthsList#get")
    @RequestMapping(name = "月报列表", path = "html/theMonthsList", method = RequestMethod.GET)
    public String theMonthsList() {
        return ctrlName+"/theMonthsList"; 
    } 
    
    @RequiresPermissions("monthlyNewsletter#html/monthlyHistoryList#get")
    @RequestMapping(name = "月报简报历史数据列表", path = "html/monthlyHistoryList", method = RequestMethod.GET)
    public String monthsHistoryList() {
        return ctrlName+"/monthlyHistoryList"; 
    }

    @RequiresAuthentication
    @RequiresPermissions("monthlyNewsletter#html/monthlyHistoryAdd#get")
    @RequestMapping(name = "月报简报历史数据新建", path = "html/monthlyHistoryAdd", method = RequestMethod.GET)
    public String monthsHistoryAdd() {
        return ctrlName+"/monthlyHistoryAdd"; 
    } 
    
    @RequiresPermissions("monthlyNewsletter#html/monthlyMultiyearList#get")
    @RequestMapping(name = "年度月报简报列表", path = "html/monthlyMultiyearList", method = RequestMethod.GET)
    public String monthlyMultiyear() {
        return ctrlName+"/monthlyMultiyearList"; 
    } 
    
    @RequiresPermissions("monthlyNewsletter#html/monthlyMultiAppoveEdit#get")
    @RequestMapping(name = "月报简报审批处理", path = "html/monthlyMultiAppoveEdit", method = RequestMethod.GET)
    public String monthlyMultiAppoveEdit() {
        return ctrlName+"/monthlyMultiAppoveEdit"; 
    } 
    
    @RequiresPermissions("monthlyNewsletter#html/monthlyMultiyFileList#get")
    @RequestMapping(name = "月报简报查询列表", path = "html/monthlyMultiyFileList", method = RequestMethod.GET)
    public String monthlyMultiyFileList() {
        return ctrlName+"/monthlyMultiyFileList"; 
    } 
    
    @RequiresPermissions("monthlyNewsletter#html/monthlyMultiyAppoveList#get")
    @RequestMapping(name = "月报简报审批列表", path = "html/monthlyMultiyAppoveList", method = RequestMethod.GET)
    public String monthlyMultiyAppoveList() {
        return ctrlName+"/monthlyMultiyAppoveList";
    }
    
    @RequiresPermissions("monthlyNewsletter#html/monthlyMultiyAppoveEdit#get")
    @RequestMapping(name = "月报简报审批处理", path = "html/monthlyMultiyAppoveEdit", method = RequestMethod.GET)
    public String monthlyMultiyAppoveEdit() {
        return ctrlName+"/monthlyMultiyAppoveEdit";
    }


    @RequiresPermissions("monthlyNewsletter#html/monthlyMultiyearAdd#get")
    @RequestMapping(name = "编辑月报简报年度", path = "html/monthlyMultiyearAdd", method = RequestMethod.GET)
    public String monthlyMultiyearAdd() {
        return ctrlName+"/monthlyMultiyearAdd"; 
    }
    
    @RequiresPermissions("monthlyNewsletter#html/monthlyYearAdd#get")
    @RequestMapping(name = "新建月报简报年度", path = "html/monthlyYearAdd", method = RequestMethod.GET)
    public String monthlyYearAdd() {
        return ctrlName+"/monthlyYearAdd"; 
    }

    @RequiresAuthentication
    //@RequiresPermissions("monthlyNewsletter#html/monthlyMultiyearDetail#get")
    @RequestMapping(name = "月报简报页面详细", path = "html/monthlyMultiyearDetail", method = RequestMethod.GET)
    public String monthlyMultiyearDetail() {
        return ctrlName+"/monthlyMultiyearDetail"; 
    }

    @RequiresAuthentication
    //@RequiresPermissions("monthlyNewsletter#html/monthlyNewsletterEdit#get")
    @RequestMapping(name = "编辑页面", path = "html/monthlyNewsletterEdit", method = RequestMethod.GET)
    public String edit() {
        return ctrlName+"/monthlyNewsletterEdit";
    }
    
    @RequiresPermissions("monthlyNewsletter#html/monthlyExcellentList#get")
    @RequestMapping(name = "优秀评审报告", path = "html/monthlyExcellentList", method = RequestMethod.GET)
    public String monthlyExcellentList() {
        return ctrlName+"/monthlyExcellentList";
    }

    @RequiresAuthentication
    //@RequiresPermissions("monthlyNewsletter#html/monthlyUpload#get")
    @RequestMapping(name = "上传附件页面", path = "html/monthlyUpload", method = RequestMethod.GET)
    public String monthlyUpload() {
        return ctrlName+"/monthlyUpload";
    }
    // end#html

}