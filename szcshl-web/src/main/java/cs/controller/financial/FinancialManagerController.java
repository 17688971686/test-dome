package cs.controller.financial;

import cs.ahelper.MudoleAnnotation;
import cs.common.constants.Constant;
import cs.common.ResultMsg;
import cs.common.utils.BeanCopierUtils;
import cs.common.utils.ExcelTools;
import cs.common.utils.Validate;
import cs.domain.expert.Expert;
import cs.domain.expert.ExpertSelected;
import cs.model.expert.ExpertDto;
import cs.model.expert.ExpertReviewDto;
import cs.model.expert.ExpertSelectedDto;
import cs.model.financial.FinancialManagerDto;
import cs.model.project.SignAssistCostDto;
import cs.service.expert.ExpertReviewService;
import cs.service.financial.FinancialManagerService;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Description: 财务管理 控制层
 * author: sjy
 * Date: 2017-8-7 11:32:03
 */
@Controller
@RequestMapping(name = "财务管理", path = "financialManager")
@MudoleAnnotation(name = "财务管理", value = "permission#financial")
public class FinancialManagerController {

    String ctrlName = "financial";
    @Autowired
    private FinancialManagerService financialManagerService;

    @Autowired
    private ExpertReviewService expertReviewService;

    @RequiresAuthentication
    @RequestMapping(name = "协审费录入页面", path = "findSingAssistCostList", method = RequestMethod.POST)
    @ResponseBody
    public List<SignAssistCostDto> findSingAssistCostList(@RequestBody SignAssistCostDto signAssistCost) {
        List<SignAssistCostDto> resultList = financialManagerService.signAssistCostList(signAssistCost, false);
        if(!Validate.isList(resultList)){
            resultList = new ArrayList<>();
        }
        return resultList;
    }

    @RequiresAuthentication
    @RequestMapping(name = "根据项目ID查询出对应的协审费用", path = "findSignCostBySignId", method = RequestMethod.POST)
    @ResponseBody
    public List<SignAssistCostDto> findSignCostBySignId(@RequestParam String signId) {
        List<SignAssistCostDto> resultList = financialManagerService.findSignCostBySignId(signId);
        if(!Validate.isList(resultList)){
            resultList = new ArrayList<>();
        }
        return resultList;
    }

    @RequiresAuthentication
    @RequestMapping(name = "协审费统计", path = "findSingAssistCostCount", method = RequestMethod.POST)
    @ResponseBody
    public List<SignAssistCostDto> findSingAssistCostCount(@RequestBody SignAssistCostDto signAssistCost) {
        List<SignAssistCostDto> resultList = financialManagerService.signAssistCostList(signAssistCost, true);
        if(!Validate.isList(resultList)){
            resultList = new ArrayList<>();
        }
        return resultList;
    }

    @RequiresAuthentication
    //@RequiresPermissions("financialManager#initfinancial#post")
    @RequestMapping(name = "初始化财务费用录入", path = "initfinancial", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> initfinancial(@RequestParam String businessId,@RequestParam String chargeType, String businessType) throws Exception {
        return financialManagerService.initfinancialData(businessId, chargeType,businessType);
    }

    @RequiresAuthentication
    @RequestMapping(name = "协审费录入", path = "assistFinancial", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> assistFinancial(@RequestParam String businessId, String businessType) throws Exception {
        Map<String, Object> map = financialManagerService.initfinancialData(businessId, Constant.EnumState.STOP.getValue(), businessType);
        return map;
    }

    @RequiresAuthentication
    //@RequiresPermissions("financialManager#html/sumfinancial#get")
    @RequestMapping(name = "统计评审总费用", path = "html/sumfinancial", method = RequestMethod.GET)
    @ResponseBody
    public BigDecimal sunFinancial(@RequestParam String businessId) {
        BigDecimal intsumcount = financialManagerService.sunCount(businessId);
        return intsumcount;
    }

    @RequiresAuthentication
    //@RequiresPermissions("financialManager##post")
    @RequestMapping(name = "保存评审费", path = "", method = RequestMethod.POST)
    @ResponseBody
    public ResultMsg post(@RequestBody FinancialManagerDto[] record) {
        return financialManagerService.save(record);
    }

    @RequiresAuthentication
    @RequestMapping(name = "主键查询", path = "html/findById", method = RequestMethod.GET)
    public @ResponseBody
    FinancialManagerDto findById(@RequestParam(required = true) String id) {
        return financialManagerService.findById(id);
    }

    @RequiresAuthentication
    //@RequiresPermissions("financialManager##delete")
    @RequestMapping(name = "删除记录", path = "", method = RequestMethod.DELETE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void delete(@RequestBody String id) {
        if(Validate.isString(id)){
            financialManagerService.delete(id);
        }
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
    @RequestMapping(name = "评审费用统计表导出", path = "exportExcel", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void exportExcel(HttpServletResponse response, @RequestParam String fileName, @RequestParam String businessId) {

//        String title = fileName;
        ExcelTools excelTools = new ExcelTools();
//        List<ExpertSelected> expertSelectedList = new ArrayList<>();
        List<Map> exportMap = new ArrayList<>();
        ExpertReviewDto expertReviewDto = expertReviewService.initBybusinessId(businessId, "");

        for (ExpertSelectedDto expertSelectedDto : expertReviewDto.getExpertSelectedDtoList()) {
            ExpertSelected expertSelected = new ExpertSelected();
            if (Constant.EnumState.YES.getValue().equals(expertSelectedDto.getIsConfrim())
                    && Constant.EnumState.YES.getValue().equals(expertSelectedDto.getIsJoin())) {
                BeanCopierUtils.copyProperties(expertSelectedDto, expertSelected);
                ExpertDto expertDto = expertSelectedDto.getExpertDto();
                Expert expert = new Expert();
                BeanCopierUtils.copyProperties(expertDto, expert);
                Map<String, Object> dataMap = new HashMap<>();
                dataMap.put("NAME", expert.getName());
                dataMap.put("IDCARD", expert.getIdCard() == null ? "" : expert.getIdCard());
                dataMap.put("OPENINGBANK", (expert.getOpeningBank() == null ? "" : expert.getOpeningBank()) + "/" + (expert.getBankAccount() == null ? "" : expert.getBankAccount()));
                dataMap.put("REVIEWCOST", expertSelected.getReviewCost());
                dataMap.put("REVIEWTAXES", expertSelected.getReviewTaxes());
                dataMap.put("TOTALCOST", expertSelected.getTotalCost());
                dataMap.put("ISLETTERRW", (Constant.EnumState.YES.getValue()).equals(expertSelected.getIsLetterRw()) ? "是" : "否");
                exportMap.add(dataMap);
            }
        }

        try {
            String title = java.net.URLDecoder.decode(fileName, "UTF-8");
            ServletOutputStream sos = response.getOutputStream();
            String[] headerArr = new String[7];
            headerArr[0] = "姓名" + "=" + "NAME";
            headerArr[1] = "身份证号码" + "=" + "IDCARD";
            headerArr[2] = "开户行/银行账号" + "=" + "OPENINGBANK";
            headerArr[3] = "评审费" + "=" + "REVIEWCOST";
            headerArr[4] = "应纳税额" + "=" + "REVIEWTAXES";
            headerArr[5] = "合计（元）" + "=" + "TOTALCOST";
            headerArr[6] = "是否函评" + "=" + "ISLETTERRW";

            HSSFWorkbook wb = excelTools.createExcelBook(title, headerArr, exportMap, HashMap.class);
            response.setContentType("application/vnd.ms-excel;charset=GBK");
            response.setHeader("Content-type", "application/x-msexcel");
            response.setHeader("Content_Length", String.valueOf(wb.getBytes().length));
            String fileName2 = new String((title + ".xls").getBytes("GB2312"), "ISO-8859-1");

            response.setHeader("Content-Disposition", "attachment;filename=" + fileName2);
            wb.write(sos);
            sos.flush();
            sos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequiresAuthentication
    //@RequiresPermissions("financialManager#exportExcel#post")
    @RequestMapping(name = "专家评审费用统计表导出", path = "costExportExcel", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void costExportExcel(HttpServletResponse response, @RequestParam String fileName, @RequestParam String businessId) {
        ExpertReviewDto expertReviewDto = expertReviewService.initBybusinessId(businessId, "");
        ServletOutputStream sos = null;
        try {

            String title = java.net.URLDecoder.decode(fileName, "UTF-8");
            // 第一步，创建一个webbook，对应一个Excel文件
            HSSFWorkbook wb = new HSSFWorkbook();
            // 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
            HSSFSheet sheet = wb.createSheet(title);
            //设置列的宽度
            sheet.setColumnWidth(0, 3766);
            sheet.setColumnWidth(1, 3766);
            sheet.setColumnWidth(2, 6766);
            sheet.setColumnWidth(3, 6766);
            sheet.setColumnWidth(4, 3766);
            sheet.setColumnWidth(5, 3766);
            sheet.setColumnWidth(6, 3766);
            // 第三步，在sheet中添加表头第2行,注意老版本poi对Excel的行数列数有限制short
            HSSFRow row = sheet.createRow((int) 3);
            // 设置值表头 设置表头居中
            HSSFCellStyle style = wb.createCellStyle();
            style.setAlignment(HorizontalAlignment.CENTER); // 创建一个居中格式
            //设置红字字体
            HSSFCellStyle styless = wb.createCellStyle();
            Font font = wb.createFont();
            font.setColor(HSSFColor.RED.index);    //红字
            styless.setFont(font);
            styless.setAlignment(HorizontalAlignment.CENTER); // 创建一个居中格式
            //合并单元格
            CellRangeAddress region1 = new CellRangeAddress(0, 2, 0,6); //参数1：起始行 参数2：终止行 参数3：起始列 参数4：终止列
            sheet.addMergedRegion(region1);
            //设置标题
            HSSFRow titleRow = sheet.createRow((int) 0);
            HSSFCell titleCell = titleRow.createCell((short) 0);
            titleCell.setCellValue(title);
            HSSFCellStyle titleStyle = wb.createCellStyle();
            HSSFFont font2 = wb.createFont();
            font2.setFontName("仿宋_GB2312");
            //粗体显示
            font2.setBold(true);
            //字体大小
            font2.setFontHeightInPoints((short) 12);
            titleStyle.setFont(font2);
            //垂直
            titleStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            // 创建一个居中格式
            titleStyle.setAlignment(HorizontalAlignment.CENTER);
            titleCell.setCellStyle(titleStyle);
            //设置第一行
            HSSFCell cell = row.createCell( 0);
            cell.setCellValue("姓名");
            cell.setCellStyle(style);
            cell = row.createCell(1);
            cell.setCellValue("是否外地");
            cell.setCellStyle(style);
            cell = row.createCell(2);
            cell.setCellValue("银行账号");
            cell.setCellStyle(style);
            cell = row.createCell(3);
            cell.setCellValue("开户行");
            cell.setCellStyle(style);
            cell = row.createCell(4);
            cell.setCellValue("评审费");
            cell.setCellStyle(style);
            cell = row.createCell(5);
            cell.setCellValue("应纳税额");
            cell.setCellStyle(style);
            cell = row.createCell(6);
            cell.setCellValue("合计（元）");
            cell.setCellStyle(style);
            //要先进行过滤。取出需要的数据。
            //如果没有先进行过滤时。直接循环赋值。会造成有一些行会空白的。（也就是说有多少条数据就有多少行,过滤的那行会变成空。所以先进行过滤）
            List<ExpertSelectedDto> expertSelectedDtoList =  expertReviewDto.getExpertSelectedDtoList();
            //把未确认和未参加会议的专家过滤掉
            if(Validate.isList(expertSelectedDtoList)){
                expertSelectedDtoList = expertSelectedDtoList.stream().filter(es ->(Constant.EnumState.YES.getValue().equals(es.getIsJoin()) && Constant.EnumState.YES.getValue().equals(es.getIsConfrim()))).collect(Collectors.toList());
                if(Validate.isList(expertSelectedDtoList)){
                    //然后再进行赋值
                    for (int i = 0,l=expertSelectedDtoList.size(); i <l; i++) {
                        ExpertSelectedDto expertSelectedDto = expertSelectedDtoList.get(i);
                        row = sheet.createRow((int) i + 4);//用来控制数据在第几行开始
                        //定义实体类
                        ExpertDto expertDto = expertSelectedDto.getExpertDto();
                        // 第四步，创建单元格，并设置值
                        //先定义单元格。方便写入样式
                        HSSFCell NameCells = row.createCell(0);//名字
                        HSSFCell cells = row.createCell(1);//是否外境
                        HSSFCell bankCells = row.createCell( 2);//定义银行账号
                        HSSFCell OpeningCells = row.createCell( 3);//定义开户银行
                        HSSFCell costCells = row.createCell(4);//定义评审费
                        HSSFCell ReviewCells = row.createCell(5);//应纳税额
                        HSSFCell TotalCells = row.createCell(6);//合计
                        //写入数据和样式
                        //名字
                        NameCells.setCellValue(expertDto.getName() == null ? "" :expertDto.getName());
                        NameCells.setCellStyle(style);

                        //是否外境
                        if ((Constant.EnumState.STOP.getValue()).equals(expertDto.getExpertField())) {//判断是否是外境。就是红字字体
                            cells.setCellValue("是");
                            cells.setCellStyle(styless);//加样式
                        } else {
                            cells.setCellValue("否");
                            cells.setCellStyle(style);//加样式

                        }
                        //定义银行账号
                        bankCells.setCellValue(expertDto.getBankAccount()==null?"":expertDto.getBankAccount());
                        //定义开户银行
                        OpeningCells.setCellValue((expertDto.getOpeningBank() == null ? "" : expertDto.getOpeningBank()));
                        OpeningCells.setCellStyle(style);//居中
                        //定义评审费
                        BigDecimal b = new BigDecimal("2000");//定义评审费的额度
                        if(expertSelectedDto.getReviewCost()==null){
                            costCells.setCellValue("");
                            costCells.setCellStyle(style);
                        }else{
                            if (expertSelectedDto.getReviewCost().compareTo(b) >= 0) {//判断，当评审费的额度大于或等于2000时，字体红色
                                costCells.setCellValue(expertSelectedDto.getReviewCost().toString());
                                costCells.setCellStyle(styless);
                            } else {
                                costCells.setCellValue(expertSelectedDto.getReviewCost().toString());
                                costCells.setCellStyle(style);
                            }
                        }

                        //应纳税额
                        ReviewCells.setCellValue(expertSelectedDto.getReviewTaxes()== null ? "":expertSelectedDto.getReviewTaxes().toString());
                        ReviewCells.setCellStyle(style);
                        //合计
                        TotalCells.setCellValue(expertSelectedDto.getTotalCost()== null ? "" :expertSelectedDto.getTotalCost().toString());
                        TotalCells.setCellStyle(style);
                    }
                }
            }

            // 第六步，将文件保存
            //FileOutputStream fout = new FileOutputStream("E:/students.xls");
            sos = response.getOutputStream();
            response.setContentType("application/vnd.ms-excel;charset=GBK");
            response.setHeader("Content-type", "application/x-msexcel");
            response.setHeader("Content_Length", String.valueOf(wb.getBytes().length));
            String fileName2 = new String((title + ".xls").getBytes("GB2312"), "ISO-8859-1");
            response.setHeader("Content-Disposition", "attachment;filename=" + fileName2);
            wb.write(sos);
            sos.flush();

        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if(Validate.isObject(sos)){
                try {
                    sos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


    }


    // begin#html
    @RequiresPermissions("financialManager#html/list#get")
    @RequestMapping(name = "评审费录入", path = "html/list", method = RequestMethod.GET)
    public String list() {
        return ctrlName + "/list";
    }


    @RequiresPermissions("financialManager#html/projectCostCount#get")
    @RequestMapping(name = "评审费用统计", path = "html/projectCostCount", method = RequestMethod.GET)
    public String proCostCount() {
        return ctrlName + "/projectCostCount";
    }

    @RequiresPermissions("financialManager#html/proCostClassifyCount#get")
    @RequestMapping(name = "评审费分类统计", path = "html/proCostClassifyCount", method = RequestMethod.GET)
    public String proCostClassifyCount() {
        return ctrlName + "/proCostClassifyCount";
    }

    @RequiresAuthentication
//    @RequiresPermissions("financialManager#html/add#get")
    @RequestMapping(name = "评审费录入", path = "html/add", method = RequestMethod.GET)
    public String edit() {
        return ctrlName + "/add";
    }

    @RequiresAuthentication
//    @RequiresPermissions("financialManager#html/assistCostAdd#get")
    @RequestMapping(name = "协审费录入", path = "html/assistCostAdd", method = RequestMethod.GET)
    public String assistCostAdd() {
        return ctrlName + "/assistCostAdd";
    }

    @RequiresPermissions("financialManager#html/assistCostList#get")
    @RequestMapping(name = "协审费用录入", path = "html/assistCostList", method = RequestMethod.GET)
    public String assistCostList() {
        return ctrlName + "/assistCostList";
    }

    @RequiresPermissions("financialManager#html/expertCount#get")
    @RequestMapping(name = "专家费用统计", path = "html/expertCount", method = RequestMethod.GET)
    public String expertCount() {
        return ctrlName + "/expertCount";
    }

    @RequiresPermissions("financialManager#html/expertPaymentCount#get")
    @RequestMapping(name = "专家缴税统计", path = "html/expertPaymentCount", method = RequestMethod.GET)
    public String expertPaymentCount() {
        return ctrlName + "/expertPaymentCount";
    }

    @RequiresAuthentication
//    @RequiresPermissions("financialManager#html/expertPaymentDetailCount#get")
    @RequestMapping(name = "专家缴税统计明细", path = "html/expertPaymentDetailCount", method = RequestMethod.GET)
    public String expertPaymentDetailCount() {
        return ctrlName + "/expertPaymentDetailCount";
    }

    @RequiresPermissions("financialManager#html/assistCostCount#get")
    @RequestMapping(name = "协审费用统计", path = "html/assistCostCount", method = RequestMethod.GET)
    public String assistCostCount() {
        return ctrlName + "/assistCostCount";
    }

    @RequiresAuthentication
//    @RequiresPermissions("financialManager#html/stageCostTable#get")
    @RequestMapping(name = "评审费发放表", path = "html/stageCostTable", method = RequestMethod.GET)
    public String stageCostTable() {
        return ctrlName + "/stageCostTable";
    }


}