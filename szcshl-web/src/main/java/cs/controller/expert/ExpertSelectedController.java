package cs.controller.expert;

import cs.ahelper.IgnoreAnnotation;
import cs.common.ResultMsg;
import cs.common.utils.BeanCopierUtils;
import cs.common.utils.DateUtils;
import cs.common.utils.ExcelTools;
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
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

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
    public ResultMsg delete(@RequestParam(required = true)String reviewId, @RequestParam(required = true) String id, boolean deleteAll) {
        return expertSelectedService.delete(reviewId,id,deleteAll);
    }

    @RequiresAuthentication
    //@RequiresPermissions("expertSelected##put")
    @RequestMapping(name = "更新记录", path = "", method = RequestMethod.PUT)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void put(@RequestBody ExpertSelectedDto record) {
        expertSelectedService.update(record);
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
        return  expertSelectedService.expertCostTotal(expertCostCountDto);
    }

    @RequiresAuthentication
    //@RequiresPermissions("expertSelected#expertCostTotal#post")
    @RequestMapping(name = "项目评审费统计", path = "projectCostTotal", method = RequestMethod.POST)
    @ResponseBody
    public ResultMsg projectCostTotal(@RequestBody ProjectReviewCostDto projectReviewCostDto){
        return  expertSelectedService.projectReviewCost(projectReviewCostDto);
    }

    @RequiresAuthentication
    //@RequiresPermissions("expertSelected#expertCostTotal#post")
    @RequestMapping(name = "项目评审费分类统计", path = "proCostClassifyTotal", method = RequestMethod.POST)
    @ResponseBody
    public ResultMsg proCostClassifyTotal(@RequestBody ProjectReviewCostDto projectReviewCostDto){
        return  expertSelectedService.proReviewClassifyCount(projectReviewCostDto);
    }

    @RequiresAuthentication
    //@RequiresPermissions("expertSelected#expertCostDetailTotal#post")
    @RequestMapping(name = "专家评审费明细汇总", path = "expertCostDetailTotal", method = RequestMethod.POST)
    @ResponseBody
    public ResultMsg expertCostDetailTotal(@RequestBody ExpertCostDetailCountDto expertCostDetailCountDto) throws ParseException {
        return  expertSelectedService.expertCostDetailTotal(expertCostDetailCountDto);
    }

    @RequiresAuthentication
    @RequestMapping(name="专家明细导出" , path ="expertDetailExport" , method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void expertDetailExport(HttpServletRequest request,HttpServletResponse resp){
        try {
            PageModelDto<ExpertSelectedDto> expertSelectedDtos = findByOData(request);
            List<ExpertSelectedDto> expertSelectedDtoList = expertSelectedDtos.getValue();
            String title = request.getParameter("fileName");
            title = java.net.URLDecoder.decode(java.net.URLDecoder.decode(title,"UTF-8"),"UTF-8");
            ExcelTools excelTools = new ExcelTools();
            List<ExpertCostDetailDto> expertCostDetailDtoList = new ArrayList<>();
            for(ExpertSelectedDto esd : expertSelectedDtoList){
                ExpertCostDetailDto expertCostDetailDto = new ExpertCostDetailDto();
                BeanCopierUtils.copyProperties(esd, expertCostDetailDto);
                BeanCopierUtils.copyProperties(esd.getExpertDto(), expertCostDetailDto);
                BeanCopierUtils.copyProperties(esd.getExpertReviewDto(), expertCostDetailDto);
                expertCostDetailDto.setReviewCost(esd.getReviewCost());
                expertCostDetailDto.setReviewTaxes(esd.getReviewTaxes());
                if(null != esd.getExpertReviewDto() && null != esd.getExpertReviewDto().getReviewDate()){
                    expertCostDetailDto.setReviewDate(DateUtils.converToString(esd.getExpertReviewDto().getReviewDate(),"yyyy-MM-dd"));
                }
                expertCostDetailDtoList.add(expertCostDetailDto);
            }
                //String title = new String((fileName).getBytes("GB2312") , "ISO-8859-1");
                ServletOutputStream sos = resp.getOutputStream();
                String [] headerPair =new String[]{"姓名=name","身份证号=idCard","开户行=openingBank","银行账号=bankAccount","评审费=reviewCost","应缴税=reviewTaxes","项目名称=reviewTitle","评审时间=reviewDate","负责人=principal"};
                HSSFWorkbook wb = excelTools.createExcelBook(title , headerPair , expertCostDetailDtoList , ExpertCostDetailDto.class);
                resp.setContentType("application/vnd.ms-excel;charset=GBK");
                resp.setHeader("Content-type" , "application/x-msexcel");
                resp.setHeader("Content_Length" , String.valueOf(wb.getBytes().length));
                String fileName2 = new String((title +".xls").getBytes("GB2312") , "ISO-8859-1");
                resp.setHeader("Content-Disposition" , "attachment;filename="+fileName2);
                wb.write(sos);
                sos.flush();
                sos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequiresAuthentication
    @RequestMapping(name="专家评审费统计导出" , path ="excelExport" , method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void excelExport(HttpServletResponse resp ,@RequestBody ExpertCostCountDto[] expertCostCountDtoArr ,@RequestParam String fileName){
        ExcelTools excelTools = new ExcelTools();
        List<ExpertCostCountDto> expertCostCountDtoList = new ArrayList<>();
        for(ExpertCostCountDto eccd : expertCostCountDtoArr){
            expertCostCountDtoList.add(eccd);
        }
        try {
            fileName = java.net.URLDecoder.decode(java.net.URLDecoder.decode(fileName,"UTF-8"),"UTF-8");
            ServletOutputStream sos = resp.getOutputStream();
            String [] headerPair =new String[]{"姓名=name","身份证号码=idCard","手机号码=userPhone","应缴所得税额(本月)=reviewcost","应缴税额(本月)=reviewtaxes","应缴所得税额(本年)=yreviewcost","应缴税额(本年)=yreviewtaxes"};
            HSSFWorkbook wb = excelTools.createExcelBook(fileName , headerPair , expertCostCountDtoList , ExpertCostCountDto.class);
            resp.setContentType("application/vnd.ms-excel;charset=GBK");
            resp.setHeader("Content-type" , "application/x-msexcel");
            resp.setHeader("Content_Length" , String.valueOf(wb.getBytes().length));
            String fileName2 = fileName +".xls";
            resp.setHeader("Content-Disposition" , "attachment;filename="+fileName2);
            wb.write(sos);
            sos.flush();
            sos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}