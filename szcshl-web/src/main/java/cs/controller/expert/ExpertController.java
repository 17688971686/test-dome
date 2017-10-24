package cs.controller.expert;

import cs.ahelper.MudoleAnnotation;
import cs.common.ResultMsg;
import cs.common.utils.BeanCopierUtils;
import cs.common.utils.DateUtils;
import cs.common.utils.ExcelTools;
import cs.domain.expert.Expert;
import cs.domain.project.SignDispaWork;
import cs.domain.sys.Header;
import cs.model.PageModelDto;
import cs.model.expert.*;
import cs.model.sys.HeaderDto;
import cs.repository.odata.ODataObj;
import cs.service.expert.ExpertService;
import cs.service.project.SignDispaWorkService;
import cs.service.sys.HeaderService;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping(name = "专家信息", path = "expert")
@MudoleAnnotation(name = "专家库管理",value = "permission#expert")
public class ExpertController {
    private String ctrlName = "expert";
    @Autowired
    private ExpertService expertService;

    @Autowired
    private SignDispaWorkService signDispaWorkService;

    @Autowired
    private HeaderService headerService;

    @RequiresAuthentication
    //@RequiresPermissions("expert#findByOData#post")
    @RequestMapping(name = "获取专家数据", path = "findByOData", method = RequestMethod.POST)
    public @ResponseBody
    PageModelDto<ExpertDto> findPageByOData(HttpServletRequest request) throws ParseException {
        ODataObj odataObj = new ODataObj(request);
        PageModelDto<ExpertDto> expertDtos = expertService.get(odataObj);
        return expertDtos;
    }

    @RequiresAuthentication
    @RequestMapping(name="专家信息导出Excel" , path ="exportToExcel" , method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void expertDetailExport(HttpServletRequest request,HttpServletResponse resp ,@RequestParam String filterData ,@RequestParam String fileName){
        try {
//            ODataObj odataObj = new ODataObj(request);
//            odataObj.setTop(0);
//            odataObj.setCount(false);
//            PageModelDto<ExpertDto> expertDtoList = expertService.get(odataObj);
            String title = java.net.URLDecoder.decode(fileName,"UTF-8");
            String filters = java.net.URLDecoder.decode(filterData,"UTF-8");
            List<HeaderDto> headerDtoList = headerService.findHeaderListSelected("专家类型");//选中的表字段
            List<Header> headerList = headerService.findHeaderByType("专家类型");//所有 表字段
            List<ExpertDto> expertDtoList = expertService.exportData(filters);
            ExcelTools excelTools = new ExcelTools();
            String[] headerPair ;
            if(headerDtoList.size()>0) {
                headerPair = new String[headerDtoList.size()];
                for (int i = 0; i < headerDtoList.size(); i++) {
                    headerPair[i] = headerDtoList.get(i).getHeaderName() + "=" + headerDtoList.get(i).getHeaderKey();
                }
            }else{
                headerPair = new String[headerList.size()];
                for (int i = 0; i < headerList.size(); i++) {
                    headerPair[i] = headerList.get(i).getHeaderName() + "=" + headerList.get(i).getHeaderKey();
                }
            }
            HSSFWorkbook wb = excelTools.createExcelBook("专家信息" , headerPair , expertDtoList , ExpertDto.class);
            resp.setContentType("application/vnd.ms-excel;charset=GBK");
            resp.setHeader("Content-type", "application/x-msexcel");
            resp.setHeader("Content_Length", String.valueOf(wb.getBytes().length));
            String fileName2 = new String((title + ".xls").getBytes("GB2312"), "ISO-8859-1");

            resp.setHeader("Content-Disposition", "attachment;filename=" + fileName2);

            ServletOutputStream sos = resp.getOutputStream();
            wb.write(sos);
            sos.flush();
            sos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @RequiresAuthentication
    //@RequiresPermissions("expert#findRepeatByOData#post")
    @RequestMapping(name = "查询重复专家", path = "findRepeatByOData", method = RequestMethod.POST)
    public @ResponseBody
    PageModelDto<ExpertDto> findRepeatByOData() {
        List<ExpertDto> list = expertService.findAllRepeat();
        PageModelDto<ExpertDto> pageModelDto = new PageModelDto<ExpertDto>();
        pageModelDto.setCount(list.size());
        pageModelDto.setValue(list);
        return pageModelDto;
    }

    @RequiresAuthentication
    @RequestMapping(name = "统计符合条件的专家", path = "countReviewExpert", method = RequestMethod.POST)
    @ResponseBody
    public List<ExpertDto> countReviewExpert(@RequestParam String minBusinessId, @RequestParam String reviewId, @RequestBody ExpertSelConditionDto epSelCondition) {
        return expertService.countExpert(minBusinessId, reviewId, epSelCondition);
    }

    @RequiresAuthentication
    @RequestMapping(name = "专家抽取", path = "autoExpertReview", method = RequestMethod.POST)
    @ResponseBody
    public ResultMsg findReviewExpert(@RequestParam String minBusinessId, @RequestParam String reviewId, @RequestBody ExpertSelConditionDto[] paramArrary) {
        return expertService.autoExpertReview(minBusinessId, reviewId, paramArrary);
    }

    @RequiresAuthentication
    //@RequiresPermissions("expert##post")
    @RequestMapping(name = "保存专家", path = "", method = RequestMethod.POST)
    @ResponseBody
    public ResultMsg post(@RequestBody ExpertDto expert) {
        return expertService.saveExpert(expert);
    }

    @RequiresAuthentication
    //@RequiresPermissions("expert##delete")
    @RequestMapping(name = "删除专家", path = "", method = RequestMethod.DELETE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void delete(@RequestBody String id) {
        String[] ids = id.split(",");
        if (ids.length > 1) {
            expertService.deleteExpert(ids);
        } else {
            expertService.deleteExpert(id);
        }
    }

    @RequiresAuthentication
    @RequestMapping(name = "头像上传", path = "uploadPhoto", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void uploadPhoto(HttpServletRequest request, @RequestParam("file") MultipartFile multipartFile,
                            @RequestParam(required = true) String expertId) throws IOException {
        expertService.savePhone(multipartFile.getBytes(), expertId);
    }

    @RequiresAuthentication
    @RequestMapping(name = "显示头像", path = "transportImg", method = RequestMethod.GET)
    public void transportImg(@RequestParam(required = true) String expertId, HttpServletRequest request, HttpServletResponse response) throws IOException {
        byte[] photos = expertService.findExpertPhoto(expertId);
        if (photos != null) {
            OutputStream outputStream = response.getOutputStream();
            InputStream in = new ByteArrayInputStream(photos);
            response.setContentType("img/jpeg");
            response.setCharacterEncoding("utf-8");
            try {
                int len = 0;
                byte[] buf = new byte[1024];
                while ((len = in.read(buf, 0, 1024)) != -1) {
                    outputStream.write(buf, 0, len);
                }
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @RequiresAuthentication
    //@RequiresPermissions("expert#findById#post")
    @RequestMapping(name = "专家详情", path = "findById", method = RequestMethod.POST)
    public @ResponseBody
    ExpertDto findById(@RequestParam(required = true) String id) {
        return expertService.findById(id);
    }

    @RequiresAuthentication
    //@RequiresPermissions("expert#updateAudit#post")
    @RequestMapping(name = "专家审核", path = "updateAudit", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void updateAudit(@RequestParam String ids, String flag) {
        expertService.updateAudit(ids, flag);
    }

    @RequiresAuthentication
    @RequestMapping(name = "专家抽取统计", path = "expertSelectHis", method = RequestMethod.POST)
    @ResponseBody
    public List<ExpertSelectHis> expertSelectHis(@RequestBody ExpertSelectHis expertSelectHis) {
        return expertService.expertSelectHis(expertSelectHis,false);
    }

    @RequiresAuthentication
    @RequestMapping(name = "专家评分统计", path = "expertScoreHis", method = RequestMethod.POST)
    @ResponseBody
    public List<ExpertSelectHis> expertScoreHis(@RequestBody ExpertSelectHis expertSelectHis) {
        return expertService.expertSelectHis(expertSelectHis,true);
    }


    @RequiresAuthentication
    @RequestMapping(name="查询专家评审的项目信息" , path = "reviewProject" , method =  RequestMethod.POST)
    @ResponseBody
    public PageModelDto<SignDispaWork> reviewProject(@RequestParam String expertId){

        PageModelDto<SignDispaWork> pageModelDto = new PageModelDto<>();
        if(!"" .equals(expertId)){

            pageModelDto = signDispaWorkService.reviewProject(expertId.substring( 1, expertId.length()));
        }
        return pageModelDto;
    }

    // begin#html
    @RequiresPermissions("expert#html/repeat#get")
    @RequestMapping(name = "重复专家查询", path = "html/repeat", method = RequestMethod.GET)
    public String list() {
        return ctrlName + "/queryReList";
    }

    @RequiresPermissions("expert#html/edit#get")
    @RequestMapping(name = "专家信息录入", path = "html/edit", method = RequestMethod.GET)
    public String edit() {
        return ctrlName + "/edit";
    }

    @RequiresPermissions("expert#html/queryAllList#get")
    @RequestMapping(name = "综合查询", path = "html/queryAllList", method = RequestMethod.GET)
    public String query() {
        return ctrlName + "/queryAllList";
    }

    @RequiresPermissions("expert#html/audit#get")
    @RequestMapping(name = "专家信息审核", path = "html/audit", method = RequestMethod.GET)
    public String audit() {
        return ctrlName + "/audit";
    }

    //@RequiresPermissions("expert#html/workExpe#get")
    @RequiresAuthentication
    @RequestMapping(name = "工作经验", path = "html/workExpe", method = RequestMethod.GET)
    public String workExpe() {
        return ctrlName + "/workExpe";
    }

    //@RequiresPermissions("expert#html/projectExpe#get")
    @RequiresAuthentication
    @RequestMapping(name = "项目经验", path = "html/projectExpe", method = RequestMethod.GET)
    public String projectExpe() {

        return ctrlName + "/projectExpe";
    }

    @RequiresPermissions("expert#html/scoreList#get")
    @RequestMapping(name = "专家评分一览表", path = "html/scoreList", method = RequestMethod.GET)
    public String reviewList() {

        return ctrlName + "/scoreList";
    }

    @RequiresPermissions("expert#html/selectHisList#get")
    @RequestMapping(name = "专家抽取统计", path = "html/selectHisList", method = RequestMethod.GET)
    public String selectHisList() {

        return ctrlName + "/selectHisList";
    }
}
