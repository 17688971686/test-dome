package cs.controller.expert;

import cs.ahelper.IgnoreAnnotation;
import cs.common.constants.Constant;
import cs.common.ResultMsg;
import cs.common.utils.*;
import cs.model.PageModelDto;
import cs.model.expert.*;
import cs.repository.odata.ODataObj;
import cs.service.expert.ExpertSelectedService;
import cs.service.sys.HeaderService;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static cs.common.constants.Constant.COMPANY_NAME;

/**
 * Description: 抽取专家 控制层
 * author: ldm
 * Date: 2017-6-14 14:26:49
 */
@Controller
@IgnoreAnnotation
@RequestMapping(name = "抽取专家", path = "expertSelected")
public class ExpertSelectedController {
    @Autowired
    private ExpertSelectedService expertSelectedService;

    @Autowired
    private HeaderService headerService;

    @RequiresAuthentication
    //@RequiresPermissions("expertSelected##post")
    @RequestMapping(name = "创建记录", path = "", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.CREATED)
    public void post(@RequestBody ExpertSelectedDto record) {
        expertSelectedService.save(record);
    }

    @RequiresAuthentication
    @RequestMapping(name = "主键查询", path = "html/findById", method = RequestMethod.GET)
    public @ResponseBody
    ExpertSelectedDto findById(@RequestParam(required = true) String id) {
        return expertSelectedService.findById(id);
    }

    @RequiresAuthentication
    //@RequiresPermissions("expertSelected##delete")
    @RequestMapping(name = "删除记录", path = "", method = RequestMethod.DELETE)
    @ResponseBody
    public ResultMsg delete(@RequestParam(required = true) String reviewId, @RequestParam(required = true) String id, boolean deleteAll) {
        return expertSelectedService.delete(reviewId, id, deleteAll);
    }

    @RequiresAuthentication
    //@RequiresPermissions("expertSelected##put")
    @RequestMapping(name = "更新记录", path = "", method = RequestMethod.PUT)
    @ResponseBody
    public ResultMsg put(@RequestBody ExpertSelectedDto record) {

        return expertSelectedService.update(record);
    }

    @RequiresAuthentication
    //@RequiresPermissions("expertSelected#findByOData#post")
    @RequestMapping(name = "获取数据", path = "findByOData", method = RequestMethod.POST)
    @ResponseBody
    public PageModelDto<ExpertSelectedDto> findByOData(HttpServletRequest request) throws ParseException {
        ODataObj odataObj = new ODataObj(request);
        PageModelDto<ExpertSelectedDto> expertSelectedDtos = expertSelectedService.get(odataObj);
        return expertSelectedDtos;
    }

    @RequiresAuthentication
    //@RequiresPermissions("expertSelected#expertCostTotal#post")
    @RequestMapping(name = "专家评审费汇总", path = "expertCostTotal", method = RequestMethod.POST)
    @ResponseBody
    public ResultMsg expertCostTotal(@RequestBody ExpertCostCountDto expertCostCountDto) throws ParseException {
        return expertSelectedService.expertCostTotal(expertCostCountDto);
    }

    @RequiresAuthentication
    //@RequiresPermissions("expertSelected#projectCostTotal#post")
    @RequestMapping(name = "项目评审费统计", path = "projectCostTotal", method = RequestMethod.POST)
    @ResponseBody
    public ResultMsg projectCostTotal(@RequestBody ProjectReviewCostDto projectReviewCostDto) {
        return expertSelectedService.projectReviewCost(projectReviewCostDto);
    }

    @RequiresAuthentication
    //@RequiresPermissions("expertSelected#expertCostTotal#post")
    @RequestMapping(name = "项目评审费录入", path = "findProjectReviewCost", method = RequestMethod.POST)
    @ResponseBody
    public PageModelDto<ProjectReviewCostDto> findProjectReviewCost(@RequestBody ProjectReviewCostDto projectReviewCostDto,String skip,String size) {
        return expertSelectedService.findProjectRevireCostBak(projectReviewCostDto,skip,size);
    }

    @RequiresAuthentication
    //@RequiresPermissions("expertSelected#expertCostTotal#post")
    @RequestMapping(name = "项目评审费分类统计", path = "proCostClassifyTotal", method = RequestMethod.POST)
    @ResponseBody
    public ResultMsg proCostClassifyTotal(@RequestBody ProjectReviewCostDto projectReviewCostDto, int page) {
        return expertSelectedService.proReviewClassifyCount(projectReviewCostDto, page);
    }

    @RequiresAuthentication
    //@RequiresPermissions("expertSelected#proReviewConditionCount#post")
    @RequestMapping(name = "项目评审情况统计", path = "proReviewConditionCount", method = RequestMethod.POST)
    @ResponseBody
    public ResultMsg proCostClassifyTotal(@RequestBody ProReviewConditionDto proReviewConditionDto) {
        return expertSelectedService.proReviewConditionCount(proReviewConditionDto);
    }

    @RequiresAuthentication
    //@RequiresPermissions("expertSelected#expertReviewCondCount#post")
    @RequestMapping(name = "专家评审情况综合统计", path = "expertReviewCondCount", method = RequestMethod.POST)
    @ResponseBody
    public ResultMsg expertReviewCondTotal(@RequestBody ExpertReviewConSimpleDto expertReviewConSimpleDto) {
        return expertSelectedService.expertReviewConSimpleCount(expertReviewConSimpleDto);
    }

    @RequiresAuthentication
    //@RequiresPermissions("expertSelected#expertReviewCompliCount#post")
    @RequestMapping(name = "专家评审情况不规则统计", path = "expertReviewCompliCount", method = RequestMethod.POST)
    @ResponseBody
    public ResultMsg expertReviewCompliCount(@RequestBody ExpertReviewConSimpleDto expertReviewConSimpleDto) {
        return expertSelectedService.expertReviewConComplicatedCount(expertReviewConSimpleDto);
    }


    @RequiresAuthentication
    //@RequiresPermissions("expertSelected#expertReviewCondDetailCount#post")
    @RequestMapping(name = "专家评审情况详细统计", path = "expertReviewCondDetailCount", method = RequestMethod.POST)
    @ResponseBody
    public ResultMsg expertReviewCondDetailTotal(@RequestBody ExpertReviewCondDto expertReviewCondDto) {
        return expertSelectedService.expertReviewCondDetailCount(expertReviewCondDto);
    }

    @RequiresAuthentication
    //@RequiresPermissions("expertSelected#expertCostDetailTotal#post")
    @RequestMapping(name = "专家评审费明细汇总", path = "expertCostDetailTotal", method = RequestMethod.POST)
    @ResponseBody
    public ResultMsg expertCostDetailTotal(@RequestBody ExpertCostDetailCountDto expertCostDetailCountDto) throws ParseException {
        return expertSelectedService.expertCostDetailTotal(expertCostDetailCountDto);
    }

    @RequiresAuthentication
    @RequestMapping(name = "专家明细导出", path = "expertDetailExport", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void expertDetailExport(HttpServletRequest request, HttpServletResponse resp) {
        try {
            PageModelDto<ExpertSelectedDto> expertSelectedDtos = findByOData(request);
            List<ExpertSelectedDto> expertSelectedDtoList = expertSelectedDtos.getValue();
            String title = request.getParameter("fileName");
            title = java.net.URLDecoder.decode(java.net.URLDecoder.decode(title, "UTF-8"), "UTF-8");
            ExcelTools excelTools = new ExcelTools();
            List<ExpertCostDetailDto> expertCostDetailDtoList = new ArrayList<>();
            for (ExpertSelectedDto esd : expertSelectedDtoList) {
                ExpertCostDetailDto expertCostDetailDto = new ExpertCostDetailDto();
                BeanCopierUtils.copyProperties(esd, expertCostDetailDto);
                BeanCopierUtils.copyProperties(esd.getExpertDto(), expertCostDetailDto);
                BeanCopierUtils.copyProperties(esd.getExpertReviewDto(), expertCostDetailDto);
                expertCostDetailDto.setReviewCost(esd.getReviewCost() == null ? new BigDecimal(0) : esd.getReviewCost());
                expertCostDetailDto.setReviewTaxes(esd.getReviewTaxes() == null ? new BigDecimal(0) : esd.getReviewTaxes());
                if (null != esd.getExpertReviewDto() && null != esd.getExpertReviewDto().getReviewDate()) {
                    expertCostDetailDto.setReviewDate(DateUtils.converToString(esd.getExpertReviewDto().getReviewDate(), "yyyy-MM-dd"));
                }
                expertCostDetailDtoList.add(expertCostDetailDto);
            }
            //String title = new String((fileName).getBytes("GB2312") , "ISO-8859-1");
            ServletOutputStream sos = resp.getOutputStream();
            String[] headerPair = new String[]{"姓名=name", "身份证号=idCard",  "银行账号=bankAccount","开户行=openingBank", "评审费=reviewCost", "应缴税=reviewTaxes", "项目名称=reviewTitle", "评审/函评日期=reviewDate", "负责人=principal"};
            HSSFWorkbook wb = excelTools.createExcelBook(title, headerPair, expertCostDetailDtoList, ExpertCostDetailDto.class);
            resp.setContentType("application/vnd.ms-excel;charset=GBK");
            resp.setHeader("Content-type", "application/x-msexcel");
            resp.setHeader("Content_Length", String.valueOf(wb.getBytes().length));
            String fileName2 = new String((title + ".xls").getBytes("GB2312"), "ISO-8859-1");
            resp.setHeader("Content-Disposition", "attachment;filename=" + fileName2);
            wb.write(sos);
            sos.flush();
            sos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequiresAuthentication
    @RequestMapping(name = "专家评审费统计导出", path = "excelExport/{year}/{month}", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void excelExport(@PathVariable String year, @PathVariable String month, @RequestBody ExpertCostCountDto[] expertCostCountDtoArr,HttpServletResponse resp) {
        ExcelTools excelTools = new ExcelTools();
        List<ExpertCostCountDto> expertCostCountDtoList = new ArrayList<>();
        for (ExpertCostCountDto eccd : expertCostCountDtoArr) {
            expertCostCountDtoList.add(eccd);
        }
        ServletOutputStream sos = null;
        try {
            String titleName = COMPANY_NAME+year + "年" + month + "月缴税统计汇总表";
            sos = resp.getOutputStream();
            HSSFWorkbook wb = excelTools.createExpertTaxes("专家缴税统计", expertCostCountDtoList);
            resp.setContentType("application/vnd.ms-excel;charset=GBK");
            resp.setHeader("Content-type", "application/x-msexcel");
            resp.setHeader("Content_Length", String.valueOf(wb.getBytes().length));
            String fileName2 = new String((titleName + ".xls").getBytes("GB2312"), "ISO-8859-1");
            resp.setHeader("Content-Disposition", "attachment;filename=" + fileName2);
            wb.write(sos);
            sos.flush();
            sos.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                sos.close();
            } catch (Exception e) {
                e.printStackTrace();

            }
        }
    }

    /**
     * 打印预览
     * @param month
     * @param year
     * @param response
     */
    @RequiresAuthentication
    @RequestMapping(name = "打印预览", path = "printPreview/{year}/{month}", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    public void printPreview(@PathVariable String year, @PathVariable String month, HttpServletResponse response) {
        InputStream inputStream = null;
        File file = null;
        File printFile = null;
        try {
            String path = SysFileUtil.getUploadPath() + File.separator + Tools.generateRandomFilename() + Constant.Template.WORD_SUFFIX.getKey();
            String filePath = path.substring(0, path.lastIndexOf(".")) + Constant.Template.PDF_SUFFIX.getKey();
            String fileName = "";
            ExpertCostCountDto expertCostCountDto = new ExpertCostCountDto();

            expertCostCountDto.setYear(year);
            expertCostCountDto.setMonth(month);
            ResultMsg resultMsg = expertSelectedService.expertCostTotal(expertCostCountDto);
            Map<String, Object> expertFileDatas = new HashMap<>();

            String times = year + "年" + month + "月";
            expertFileDatas.put("time", times);
            Map<String, Object> resultMap = (Map<String, Object>) resultMsg.getReObj();
            for (String key : resultMap.keySet()) {
                expertFileDatas.put(key, resultMap.get(key));
            }
            file = TemplateUtil.createDoc(expertFileDatas, Constant.Template.EXPERT_PAYTAXES.getKey(), path);

            if (file != null) {
                OfficeConverterUtil.convert2PDF(file.getAbsolutePath(), filePath);
            }
            printFile = new File(filePath);
            inputStream = new BufferedInputStream(new FileInputStream(printFile));

            byte[] buffer = new byte[inputStream.available()];
            inputStream.read(buffer);  //读取文件流
            inputStream.close();

            response.reset();  //重置结果集
            response.setContentType("application/pdf");
            response.addHeader("Content-Length", "" + printFile.length());  //返回头 文件大小
            response.setHeader("Content-Disposition", "inline;filename=" + new String(fileName.getBytes(), "ISO-8859-1"));

            //获取返回体输出权
            OutputStream os = new BufferedOutputStream(response.getOutputStream());
            os.write(buffer); // 输出文件
            os.flush();
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (file != null) {
                    Tools.deleteFile(file);
                }
                if (printFile != null) {
                    Tools.deleteFile(printFile);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 查看明细的打印预览
     *
     * @param time
     * @param response
     */
    @RequiresAuthentication
    @RequestMapping(name = "打印预览", path = "printPreviewDetail/{time}", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    public void printPreviewDetail(@PathVariable String time, HttpServletResponse response) {
        InputStream inputStream = null;
        File file = null;
        File printFile = null;
        try {
            String path = SysFileUtil.getUploadPath() + File.separator + Tools.generateRandomFilename() + Constant.Template.WORD_SUFFIX.getKey();
            String filePath = path.substring(0, path.lastIndexOf(".")) + Constant.Template.PDF_SUFFIX.getKey();
            String fileName = "";
            String year = time.substring(0, 4);
            String month = time.substring(4);
            ExpertCostDetailCountDto expertCostDetailCountDto = new ExpertCostDetailCountDto();
            expertCostDetailCountDto.setYear(year);
            expertCostDetailCountDto.setMonth(month);
            ResultMsg resultMsg = expertSelectedService.expertCostDetailTotal(expertCostDetailCountDto);
            Map<String, Object> expertFileDatas = new HashMap<>();
            String times = year + "年" + month + "月";
            expertFileDatas.put("time", times);
            Map<String, ArrayList> map = (Map) resultMsg.getReObj();
            for (String key : map.keySet()) {
                expertFileDatas.put("expertCostTotalInfo", map.get(key));
            }
            file = TemplateUtil.createDoc(expertFileDatas, Constant.Template.EXPERT_PAYTAXESDETAIL.getKey(), path);

            if (file != null) {
                OfficeConverterUtil.convert2PDF(file.getAbsolutePath(), filePath);
            }
            printFile = new File(filePath);
            inputStream = new BufferedInputStream(new FileInputStream(printFile));

            byte[] buffer = new byte[inputStream.available()];
            inputStream.read(buffer);  //读取文件流
            inputStream.close();

            response.reset();  //重置结果集
            response.setContentType("application/pdf");
            response.addHeader("Content-Length", "" + printFile.length());  //返回头 文件大小
            response.setHeader("Content-Disposition", "inline;filename=" + new String(fileName.getBytes(), "ISO-8859-1"));

            //获取返回体输出权
            OutputStream os = new BufferedOutputStream(response.getOutputStream());
            os.write(buffer); // 输出文件
            os.flush();
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (file != null) {
                    Tools.deleteFile(file);
                }
                if (printFile != null) {
                    Tools.deleteFile(printFile);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}