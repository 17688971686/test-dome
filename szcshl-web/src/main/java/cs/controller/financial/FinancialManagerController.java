package cs.controller.financial;

import cs.ahelper.MudoleAnnotation;
import cs.common.utils.BeanCopierUtils;
import cs.common.utils.ExcelTools;
import cs.domain.expert.Expert;
import cs.domain.expert.ExpertSelected;
import cs.model.PageModelDto;
import cs.model.expert.ExpertDto;
import cs.model.expert.ExpertReviewDto;
import cs.model.expert.ExpertSelectedDto;
import cs.model.financial.FinancialManagerDto;
import cs.model.project.SignDto;
import cs.repository.odata.ODataObj;
import cs.service.expert.ExpertReviewService;
import cs.service.financial.FinancialManagerService;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description: 财务管理 控制层
 * author: sjy
 * Date: 2017-8-7 11:32:03
 */
@Controller
@RequestMapping(name = "财务管理", path = "financialManager")
@MudoleAnnotation(name = "财务管理",value = "permission#financial")
public class FinancialManagerController {

    String ctrlName = "financial";
    @Autowired
    private FinancialManagerService financialManagerService;

    @Autowired
    private ExpertReviewService expertReviewService;

    @RequiresAuthentication
    //@RequiresPermissions("financialManager#findByOData#post")
    @RequestMapping(name = "获取评审费统用计列表数据", path = "findByOData", method = RequestMethod.POST)
    @ResponseBody
    public PageModelDto<SignDto> get(HttpServletRequest request) throws ParseException {
        ODataObj odataObj = new ODataObj(request);
        PageModelDto<SignDto> financialManagerDtos = financialManagerService.get(odataObj);
        return financialManagerDtos;
    }

    @RequiresAuthentication
    //@RequiresPermissions("financialManager#assistCostCountList#post")
    @RequestMapping(name = "获取协审费用统计列表数据", path = "assistCostCountList", method = RequestMethod.POST)
    @ResponseBody
    public PageModelDto<SignDto> assistCostCountList(HttpServletRequest request) throws ParseException {
        ODataObj odataObj = new ODataObj(request);
        PageModelDto<SignDto> financialManagerDtos = financialManagerService.assistCostCountGet(odataObj);
        return financialManagerDtos;
    }

    @RequiresAuthentication
    //@RequiresPermissions("financialManager#initfinancial#get")
    @RequestMapping(name = "初始化财务页面", path = "initfinancial", method = RequestMethod.GET)
    public @ResponseBody
    Map<String, Object> initfinancial(@RequestParam String signid) throws Exception {
        Map<String, Object> map = financialManagerService.initfinancialData(signid);
        return map;
    }

    @RequiresAuthentication
    //@RequiresPermissions("financialManager#html/sumfinancial#get")
    @RequestMapping(name = "统计评审总费用", path = "html/sumfinancial", method = RequestMethod.GET)
    @ResponseBody
    public Integer sunFinancial(@RequestParam String businessId){
        Integer intsumcount = financialManagerService.sunCount(businessId);
        return intsumcount;
    }

    @RequiresAuthentication
    //@RequiresPermissions("financialManager##post")
    @RequestMapping(name = "创建记录", path = "", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.CREATED)
    public void post(@RequestBody FinancialManagerDto []record) {
        for(FinancialManagerDto financialDto : record){
            financialManagerService.save(financialDto);
        }
    }

	@RequiresAuthentication
    @RequestMapping(name = "主键查询", path = "html/findById",method=RequestMethod.GET)
    public @ResponseBody FinancialManagerDto findById(@RequestParam(required = true)String id){
        return financialManagerService.findById(id);
    }

    @RequiresAuthentication
    //@RequiresPermissions("financialManager##delete")
    @RequestMapping(name = "删除记录", path = "", method = RequestMethod.DELETE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void delete(@RequestBody String id) {
        financialManagerService.delete(id);
    }

    @RequiresAuthentication
    //@RequiresPermissions("financialManager##put")
    @RequestMapping(name = "更新记录", path = "", method = RequestMethod.PUT)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void put(@RequestBody FinancialManagerDto record) {
        financialManagerService.update(record);
    }

 	@RequiresAuthentication
    //@RequiresPermissions("financialManager#exportExcel#post")
    @RequestMapping(name="评审费用统计表导出" , path="exportExcel" , method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void exportExcel(HttpServletResponse response , @RequestParam String fileName , @RequestParam String businessId){

//        String title = fileName;
        ExcelTools excelTools = new ExcelTools();
//        List<ExpertSelected> expertSelectedList = new ArrayList<>();
        List<Map> exportMap = new ArrayList<>();
       ExpertReviewDto expertReviewDto = expertReviewService.initBybusinessId(businessId,"");

        for(ExpertSelectedDto expertSelectedDto : expertReviewDto.getExpertSelectedDtoList()){
            ExpertSelected expertSelected = new ExpertSelected();
            BeanCopierUtils.copyProperties(expertSelectedDto , expertSelected);
//            expertSelectedList.add(expertSelected);
            ExpertDto expertDto = expertSelectedDto.getExpertDto();
            Expert expert = new Expert();
            BeanCopierUtils.copyProperties(expertDto , expert);
            Map<String , Object> dataMap = new HashMap<>();
            dataMap.put("NAME" , expert.getName());
            dataMap.put("IDCARD" , expert.getIdCard() == null ? "" : expert.getIdCard());
            dataMap.put("OPENINGBANK" , (expert.getOpeningBank() == null ? "" : expert.getOpeningBank()) + "/" + (expert.getBankAccount() == null ? "" : expert.getBankAccount()));
            dataMap.put("REVIEWCOST" , expertSelected.getReviewCost());
            dataMap.put("REVIEWTAXES" , expertSelected.getReviewTaxes());
            dataMap.put("TOTALCOST" , expertSelected.getTotalCost() );
            dataMap.put("ISLETTERRW" , "9".equals(expertSelected.getIsLetterRw()) ? "是" : "否");
            exportMap.add(dataMap);
        }

        try{
            String title = java.net.URLDecoder.decode(fileName,"UTF-8");
            ServletOutputStream sos = response.getOutputStream();
            String[] headerArr= new String[7];
            headerArr[0] = "姓名" + "=" + "NAME";
            headerArr[1] = "身份证号码" + "=" + "IDCARD";
            headerArr[2] = "开户行/银行账号" + "=" + "OPENINGBANK";
            headerArr[3] = "评审费" + "=" + "REVIEWCOST";
            headerArr[4] = "应纳税额" + "=" + "REVIEWTAXES";
            headerArr[5] = "合计（元）" + "=" + "TOTALCOST";
            headerArr[6] = "是否函评" + "=" + "ISLETTERRW";

            HSSFWorkbook wb = excelTools.createExcelBook(title , headerArr , exportMap , HashMap.class);
            response.setContentType("application/vnd.ms-excel;charset=GBK");
            response.setHeader("Content-type", "application/x-msexcel");
            response.setHeader("Content_Length", String.valueOf(wb.getBytes().length));
            String fileName2 = new String((title + ".xls").getBytes("GB2312"), "ISO-8859-1");

            response.setHeader("Content-Disposition", "attachment;filename=" + fileName2);
            wb.write(sos);
            sos.flush();
            sos.close();
        }catch (Exception e) {
            e.printStackTrace();
        }


    }

    // begin#html
    @RequiresPermissions("financialManager#html/list#get")
    @RequestMapping(name = "评审费统计", path = "html/list", method = RequestMethod.GET)
    public String list() {
        return ctrlName+"/list"; 
    }

    @RequiresPermissions("financialManager#html/expertReviewCondCount#get")
    @RequestMapping(name = "专家评审情况统计", path = "html/expertReviewCondCount", method = RequestMethod.GET)
    public String expertReviewCondCount() {
        return ctrlName+"/expertReviewCondCount";
    }

    @RequiresPermissions("financialManager#html/proReviewConCount#get")
    @RequestMapping(name = "项目评审情况统计", path = "html/proReviewConCount", method = RequestMethod.GET)
    public String proReviewConditionCount() {
        return ctrlName+"/proReviewConCount";
    }

    @RequiresPermissions("financialManager#html/projectCostCount#get")
    @RequestMapping(name = "评审费统计", path = "html/projectCostCount", method = RequestMethod.GET)
    public String proCostCount() {
        return ctrlName+"/projectCostCount";
    }

    @RequiresAuthentication
    //@RequiresPermissions("financialManager#html/projectCostCount#get")
    @RequestMapping(name = "评审费分类统计", path = "html/proCostClassifyCount", method = RequestMethod.GET)
    public String proCostClassifyCount() {
        return ctrlName+"/proCostClassifyCount";
    }

    @RequiresPermissions("financialManager#html/add#get")
    @RequestMapping(name = "评审费用录入", path = "html/add", method = RequestMethod.GET)
    public String edit() {
        return ctrlName+"/add";
    }

    @RequiresPermissions("financialManager#html/assistCostAdd#get")
    @RequestMapping(name = "协审费用录入", path = "html/assistCostAdd", method = RequestMethod.GET)
    public String assistCostAdd() {
        return ctrlName+"/assistCostAdd";
    }
    
    @RequiresPermissions("financialManager#html/assistCostList#get")
    @RequestMapping(name = "协审费用录入列表页面", path = "html/assistCostList", method = RequestMethod.GET)
    public String assistCostList() {
        return ctrlName+"/assistCostList";
    }

    @RequiresPermissions("financialManager#html/expertCount#get")
    @RequestMapping(name = "专家费统计", path = "html/expertCount", method = RequestMethod.GET)
    public String expertCount() {
        return ctrlName+"/expertCount";
    }
    
    @RequiresPermissions("financialManager#html/expertPaymentCount#get")
    @RequestMapping(name = "专家缴税统计", path = "html/expertPaymentCount", method = RequestMethod.GET)
    public String expertPaymentCount() {
        return ctrlName+"/expertPaymentCount";
    }

    @RequiresPermissions("financialManager#html/expertPaymentDetailCount#get")
    @RequestMapping(name = "专家缴税统计明细", path = "html/expertPaymentDetailCount", method = RequestMethod.GET)
    public String expertPaymentDetailCount() {
        return ctrlName+"/expertPaymentDetailCount";
    }

    @RequiresPermissions("financialManager#html/assistCostCount#get")
    @RequestMapping(name = "协审费用统计", path = "html/assistCostCount", method = RequestMethod.GET)
    public String assistCostCount() {
        return ctrlName+"/assistCostCount";
    }

    @RequiresPermissions("financialManager#html/stageCostTable#get")
    @RequestMapping(name = "评审费发放表", path = "html/stageCostTable", method = RequestMethod.GET)
    public String stageCostTable() {
        return ctrlName+"/stageCostTable";
    }


}