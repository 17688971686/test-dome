package cs.controller.monthly;

import cs.ahelper.MudoleAnnotation;
import cs.common.ResultMsg;
import cs.common.constants.Constant;
import cs.common.utils.DateUtils;
import cs.common.utils.SessionUtil;
import cs.common.utils.StringUtil;
import cs.common.utils.Validate;
import cs.model.PageModelDto;
import cs.model.monthly.MonthlyNewsletterDto;
import cs.model.project.AddSuppLetterDto;
import cs.repository.odata.ODataFilterItem;
import cs.repository.odata.ODataObj;
import cs.service.monthly.MonthlyNewsletterService;
import cs.service.project.AddSuppLetterService;
import cs.service.rtx.RTXService;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.List;

import static cs.common.constants.Constant.ERROR_MSG;

/**
 * Description: 月报简报 控制层
 * author: sjy
 * Date: 2017-9-8 11:23:41
 */
@Controller
@RequestMapping(name = "月报简报", path = "monthlyNewsletter")
@MudoleAnnotation(name = "月报简报管理",value = "permission#monthlyNewsletter")
public class MonthlyNewsletterController {

	String ctrlName = "monthlyNewsletter";
    @Autowired
    private MonthlyNewsletterService monthlyNewsletterService;

    @Autowired
    private AddSuppLetterService addSuppLetterService;
    @Autowired
    private RTXService rtxService;

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
    @RequestMapping(name = "保存月报简报", path = "saveMonthlyMultiyear", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.CREATED)
    public @ResponseBody ResultMsg monthlyMultiyearAdd(@RequestBody AddSuppLetterDto record) {
       return addSuppLetterService.saveMonthlyMultiyear(record);
    }

    @RequiresAuthentication
    //@RequiresPermissions("monthlyNewsletter#initMonthlyMultiyear#post")
    @RequestMapping(name = "初始化月报简报", path = "initMonthlyMultiyear", method = RequestMethod.POST)
    @ResponseBody
    public AddSuppLetterDto initMutilyear(){
    	return addSuppLetterService.initMonthlyMutilyear();
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
    //@RequiresPermissions("monthlyNewsletter#getMonthlyList#post")
    @RequestMapping(name = "年度月报简报列表", path = "getMonthlyList", method = RequestMethod.POST)
    @ResponseBody
    public PageModelDto<MonthlyNewsletterDto> getMonthlyList(HttpServletRequest request) throws ParseException {
        ODataObj odataObj = new ODataObj(request);
        PageModelDto<MonthlyNewsletterDto> monthlyNewsletterDtos = monthlyNewsletterService.getMonthlyList(odataObj);
        return monthlyNewsletterDtos;
    }

   /* @RequiresAuthentication
    //@RequiresPermissions("monthlyNewsletter#deleteMonthlyList#post")
    @RequestMapping(name = "删除月报简报管理数据列表", path = "deleteMonthlyList", method = RequestMethod.POST)
    @ResponseBody
    public PageModelDto<MonthlyNewsletterDto> deleteMonthlyList(HttpServletRequest request) throws ParseException {
        ODataObj odataObj = new ODataObj(request);
        PageModelDto<MonthlyNewsletterDto> monthlyNewsletterDtos = monthlyNewsletterService.deleteMonthlyList(odataObj);	
        return monthlyNewsletterDtos;
    }*/

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
    @ResponseBody
    public ResultMsg deleteMonthlyData(@RequestParam String id) {
        return monthlyNewsletterService.deleteMonthlyData(id);
    }

    @RequiresAuthentication
    @RequestMapping(name = "恢复月报简报记录", path = "restoreMonthlyData", method = RequestMethod.GET)
    @ResponseBody
    public ResultMsg restoreMonthlyData(@RequestParam String id) {
        return monthlyNewsletterService.restoreMonthlyData(id);
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
    @RequestMapping(name="发起流程" , path="startFlow" , method = RequestMethod.POST)
    @ResponseBody
    public ResultMsg startFlow(@RequestParam String id){
        ResultMsg resultMsg = monthlyNewsletterService.startFlow(id);
        if(Validate.isObject(resultMsg)){
            if(resultMsg.isFlag()){
                String procInstName = Validate.isObject(resultMsg.getReObj())?resultMsg.getReObj().toString():"";
                rtxService.dealPoolRTXMsg(resultMsg.getIdCode(),resultMsg,procInstName, Constant.MsgType.task_type.name());
                resultMsg.setIdCode(null);
                resultMsg.setReObj(null);
            }
        }else{
            resultMsg = ResultMsg.error(ERROR_MSG);
        }
        return resultMsg;
    }


    @RequiresAuthentication
    //@RequiresPermissions("monthlyNewsletter##put")
    @RequestMapping(name = "更新记录", path = "", method = RequestMethod.PUT)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void put(@RequestBody MonthlyNewsletterDto record) {
        monthlyNewsletterService.update(record);
    }

    @RequiresAuthentication
    @RequestMapping(name = "生成月报简报", path = "createMonthReport", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void createMonthReport(HttpServletResponse resp , @RequestParam String reportMultiyear, @RequestParam String theMonths, @RequestParam String startMoultiyear, @RequestParam String endMoultiyear, @RequestParam String staerTheMonths, @RequestParam String endTheMonths) {
        InputStream is = null;
        ServletOutputStream out = null;
        MonthlyNewsletterDto  monthlyNewsletterDto = new MonthlyNewsletterDto();
        monthlyNewsletterDto.setReportMultiyear(reportMultiyear);
        monthlyNewsletterDto.setTheMonths(theMonths);
        monthlyNewsletterDto.setStartMoultiyear(startMoultiyear);
        monthlyNewsletterDto.setEndMoultiyear(endMoultiyear);
        monthlyNewsletterDto.setStaerTheMonths(staerTheMonths);
        monthlyNewsletterDto.setEndTheMonths(endTheMonths);
        try {
            if(!StringUtil.isNotEmpty(monthlyNewsletterDto.getReportMultiyear())){
                monthlyNewsletterDto.setReportMultiyear(String.valueOf(DateUtils.getCurYear()));
            }
            if(!StringUtil.isNotEmpty(monthlyNewsletterDto.getTheMonths())){
                monthlyNewsletterDto.setTheMonths(String.valueOf(DateUtils.getCurMonth()+1));
            }
            if(!StringUtil.isNotEmpty(monthlyNewsletterDto.getStartMoultiyear())){
                monthlyNewsletterDto.setStartMoultiyear(monthlyNewsletterDto.getReportMultiyear());
            }
            if(!StringUtil.isNotEmpty(monthlyNewsletterDto.getEndMoultiyear())){
                monthlyNewsletterDto.setEndMoultiyear(monthlyNewsletterDto.getReportMultiyear());
            }
            if(!StringUtil.isNotEmpty(monthlyNewsletterDto.getStaerTheMonths())){
                monthlyNewsletterDto.setStaerTheMonths("1");
            }
            if(!StringUtil.isNotEmpty(monthlyNewsletterDto.getEndTheMonths())){
                monthlyNewsletterDto.setEndTheMonths(monthlyNewsletterDto.getTheMonths());
            }

            File docFile = monthlyNewsletterService.createMonthTemplate(monthlyNewsletterDto);
            is = new FileInputStream(docFile);
            resp.setCharacterEncoding("utf-8");
            resp.setContentType("application/msword");
            // 设置浏览器以下载的方式处理该文件默认
            String fileName2 = new String((monthlyNewsletterDto.getTheMonths()+"月月报" + ".doc").getBytes("GB2312"), "ISO-8859-1");
            resp.addHeader("Content-Disposition", "attachment;filename=" + fileName2);
            out = resp.getOutputStream();
            byte[] buffer = new byte[512];  // 缓冲区
            int bytesToRead = -1;
            // 通过循环将读入的Word文件的内容输出到浏览器中
            while((bytesToRead = is.read(buffer)) != -1) {
                out.write(buffer, 0, bytesToRead);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if(Validate.isObject(is)){
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // begin#html
    @RequiresPermissions("monthlyNewsletter#html/monthlyNewsletterList#get")
    @RequestMapping(name = "月报简报管理", path = "html/monthlyNewsletterList", method = RequestMethod.GET)
    public String monthlyNewsletterList() {
        return ctrlName+"/monthlyNewsletterList"; 
    }

    @RequiresPermissions("monthlyNewsletter#html/theMonthsList#get")
    @RequestMapping(name = "年度月报简报", path = "html/theMonthsList", method = RequestMethod.GET)
    public String theMonthsList() {
        return ctrlName+"/theMonthsList"; 
    } 

    @RequiresAuthentication
//    @RequiresPermissions("monthlyNewsletter#html/monthlyHistoryList#get")
    @RequestMapping(name = "月报简报历史数据列表", path = "html/monthlyHistoryList", method = RequestMethod.GET)
    public String monthsHistoryList() {
        return ctrlName+"/monthlyHistoryList"; 
    }

    @RequiresAuthentication
//    @RequiresPermissions("monthlyNewsletter#html/monthlyHistoryAdd#get")
    @RequestMapping(name = "月报简报历史数据新建", path = "html/monthlyHistoryAdd", method = RequestMethod.GET)
    public String monthsHistoryAdd() {
        return ctrlName+"/monthlyHistoryAdd"; 
    } 

    @RequiresAuthentication
//    @RequiresPermissions("monthlyNewsletter#html/monthlyMultiyearList#get")
    @RequestMapping(name = "年度月报简报列表", path = "html/monthlyMultiyearList", method = RequestMethod.GET)
    public String monthlyMultiyear() {
        System.out.println(SessionUtil.hashRole(Constant.EnumFlowNodeGroupName.REPORT_USER.getValue()));
        SessionUtil.getSession().setAttribute("REPORT_USER",SessionUtil.hashRole(Constant.EnumFlowNodeGroupName.REPORT_USER.getValue())==true
                ?"1":"2");
        return ctrlName+"/monthlyMultiyearList"; 
    } 

    @RequiresAuthentication
//    @RequiresPermissions("monthlyNewsletter#html/monthlyMultiAppoveEdit#get")
    @RequestMapping(name = "月报简报审批处理", path = "html/monthlyMultiAppoveEdit", method = RequestMethod.GET)
    public String monthlyMultiAppoveEdit() {
        return ctrlName+"/monthlyMultiAppoveEdit"; 
    } 
    
    @RequiresPermissions("monthlyNewsletter#html/monthlyMultiyFileList#get")
    @RequestMapping(name = "月报简报查询", path = "html/monthlyMultiyFileList", method = RequestMethod.GET)
    public String monthlyMultiyFileList() {
        return ctrlName+"/monthlyMultiyFileList"; 
    } 
    
    /*@RequiresPermissions("monthlyNewsletter#html/monthlyMultiyAppoveList#get")
    @RequestMapping(name = "月报简报审批列表", path = "html/monthlyMultiyAppoveList", method = RequestMethod.GET)
    public String monthlyMultiyAppoveList() {
        return ctrlName+"/monthlyMultiyAppoveList";
    }

    @RequiresPermissions("monthlyNewsletter#html/monthlyMultiyAppoveEdit#get")
    @RequestMapping(name = "月报简报审批处理", path = "html/monthlyMultiyAppoveEdit", method = RequestMethod.GET)
    public String monthlyMultiyAppoveEdit() {
        return ctrlName+"/monthlyMultiyAppoveEdit";
    }
*/

    @RequiresAuthentication
//    @RequiresPermissions("monthlyNewsletter#html/monthlyMultiyearAdd#get")
    @RequestMapping(name = "编辑月报简报年度", path = "html/monthlyMultiyearAdd", method = RequestMethod.GET)
    public String monthlyMultiyearAdd() {
        return ctrlName+"/monthlyMultiyearAdd"; 
    }

    @RequiresAuthentication
//    @RequiresPermissions("monthlyNewsletter#html/monthlyYearAdd#get")
    @RequestMapping(name = "新建月报简报年度", path = "html/monthlyYearAdd", method = RequestMethod.GET)
    public String monthlyYearAdd() {
        return ctrlName+"/monthlyYearAdd"; 
    }

    @RequiresAuthentication
    //@RequiresPermissions("monthlyNewsletter#html/monthlyMultiyearDetail#get")
    @RequestMapping(name = "月报简报页面详细", path = "html/monthlyMultiyView", method = RequestMethod.GET)
    public String monthlyMultiyView() {
        return ctrlName+"/monthlyMultiyView";
    }

    @RequiresAuthentication
    //@RequiresPermissions("monthlyNewsletter#html/monthlyNewsletterEdit#get")
    @RequestMapping(name = "编辑页面", path = "html/monthlyNewsletterEdit", method = RequestMethod.GET)
    public String edit() {
        return ctrlName+"/monthlyNewsletterEdit";
    }

    @RequiresAuthentication
//    @RequiresPermissions("monthlyNewsletter#html/monthlyExcellentList#get")
    @RequestMapping(name = "优秀评审报告", path = "html/monthlyExcellentList", method = RequestMethod.GET)
    public String monthlyExcellentList() {
        return ctrlName+"/monthlyExcellentList";
    }

}