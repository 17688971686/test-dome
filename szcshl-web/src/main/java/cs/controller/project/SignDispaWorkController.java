package cs.controller.project;

import com.sn.framework.jxls.JxlsUtils;
import cs.ahelper.MudoleAnnotation;
import cs.common.constants.Constant;
import cs.common.ResultMsg;
import cs.common.constants.SysConstants;
import cs.common.utils.*;
import cs.domain.project.SignDispaWork;
import cs.domain.project.SignDispaWork_;
import cs.domain.sys.Header;
import cs.model.PageModelDto;
import cs.model.expert.AchievementDeptDetailDto;
import cs.model.expert.AchievementDetailDto;
import cs.model.expert.AchievementSumDto;
import cs.model.project.SignDispaWorkDto;
import cs.model.sys.HeaderDto;
import cs.model.topic.TopicMaintainDto;
import cs.repository.odata.ODataObj;
import cs.repository.repositoryImpl.project.SignDispaWorkRepo;
import cs.service.expert.ExpertSelectedService;
import cs.service.project.SignDispaWorkService;
import cs.service.sys.HeaderService;
import cs.service.topic.TopicInfoService;
import cs.service.topic.TopicMaintainService;
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
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.*;
import static com.sn.framework.common.StringUtil.GBK;
import static com.sn.framework.common.StringUtil.ISO_8859_1;
import static com.sn.framework.common.StringUtil.UTF_8;

/**
 * 项目详情信息视图控制器
 * Created by ldm on 2017/9/17 0017.
 */
@Controller
@RequestMapping(name = "收文", path = "signView")
@MudoleAnnotation(name = "查询统计",value = "permission#queryStatistics")
public class SignDispaWorkController {

    @Autowired
    private SignDispaWorkService signDispaWorkService;

    @Autowired
    private HeaderService headerService;

    @Autowired
    private ExpertSelectedService expertSelectedService;

    @Autowired
    private SignDispaWorkRepo signDispaWorkRepo;

    @Autowired
    private TopicMaintainService topicMaintainService;


    //@RequiresPermissions("signView#getSignList#post")
    @RequiresAuthentication
    @RequestMapping(name = "项目查询统计", path = "getSignList", method = RequestMethod.POST)
    @ResponseBody
    public PageModelDto<SignDispaWorkDto> getSignList(HttpServletRequest request) throws ParseException {
        ODataObj odataObj = new ODataObj(request);
        PageModelDto<SignDispaWorkDto> pageModelDto = signDispaWorkService.getCommQurySign(odataObj);
        return pageModelDto;
    }

    @RequiresAuthentication
    //@RequiresPermissions("signView#unMergeWPSign#post")
    @RequestMapping(name = "待选合并评审项目", path = "unMergeWPSign", method = RequestMethod.POST)
    @ResponseBody
    public List<SignDispaWork> unMergeWPSign(@RequestParam(required = true) String signId) {
        return signDispaWorkService.unMergeWPSign(signId);
    }

    @RequiresAuthentication
    //@RequiresPermissions("signView#getMergeSignBySignId#post")
    @RequestMapping(name = "获取已选合并评审项目", path = "getMergeSignBySignId", method = RequestMethod.POST)
    public @ResponseBody
    List<SignDispaWork> getMergeSignBySignId(@RequestParam(required = true) String signId) {
        return signDispaWorkService.getMergeWPSignBySignId(signId);
    }

    @RequiresAuthentication
    //@RequiresPermissions("signView#unMergeDISSign#post")
    @RequestMapping(name = "待选合并发文项目", path = "unMergeDISSign", method = RequestMethod.POST)
    @ResponseBody
    public List<SignDispaWork> unMergeDISSign(@RequestParam(required = true) String signId) {
        return signDispaWorkService.unMergeDISSign(signId);
    }

    @RequiresAuthentication
    //@RequiresPermissions("signView#getMergeDISSign#post")
    @RequestMapping(name = "获取已选合并发文项目", path = "getMergeDISSign", method = RequestMethod.POST)
    @ResponseBody
    public List<SignDispaWork> getMergeDISSign(@RequestParam(required = true) String signId) {
        return signDispaWorkService.getMergeDISSignBySignId(signId);
    }

    @RequiresAuthentication
    @RequestMapping(name = "保存合并项目", path = "mergeSign", method = RequestMethod.POST)
    @ResponseBody
    public ResultMsg mergeSign(@RequestParam(required = true) String signId,
                               @RequestParam(required = true) String mergeIds, @RequestParam(required = true) String mergeType) {
        return signDispaWorkService.mergeSign(signId, mergeIds, mergeType);
    }

    @RequiresAuthentication
    //@RequiresPermissions("signView#cancelMergeSign#post")
    @RequestMapping(name = "解除合并评审发文", path = "cancelMergeSign", method = RequestMethod.POST)
    @ResponseBody
    public ResultMsg cancelMergeSign(@RequestParam(required = true) String signId, String cancelIds,
                                     @RequestParam(required = true) String mergeType) {
        return signDispaWorkService.cancelMergeSign(signId, cancelIds, mergeType);
    }

    @RequiresAuthentication
    //@RequiresPermissions("signView#deleteAllMerge#post")
    @RequestMapping(name = "删除所有合并项目", path = "deleteAllMerge", method = RequestMethod.POST)
    @ResponseBody
    public ResultMsg deleteAllMerge(@RequestParam(required = true) String signId,
                                    @RequestParam(required = true) String mergeType,
                                    @RequestParam(required = true) String businessId) {
        return signDispaWorkService.deleteAllMerge(signId, mergeType,businessId);
    }

    @RequiresAuthentication
    @RequestMapping(name = "通过条件查询进行统计分析" , path = "QueryStatistics" , method = RequestMethod.POST)
    @ResponseBody
    public List<SignDispaWork> queryStatistics(@RequestParam String queryData , @RequestParam int page){
        return signDispaWorkService.queryStatistics(queryData , page);
    }

    @RequiresAuthentication
    @RequestMapping(name = "项目统计导出", path = "excelExport", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void excelExport(HttpServletResponse resp, @RequestParam(required = true) String signIds) {
        String fileName = "项目查询统计报表";
        ExcelTools excelTools = new ExcelTools();
        try {
            String title = java.net.URLDecoder.decode(fileName,"UTF-8");
            ServletOutputStream sos = resp.getOutputStream();
            List<HeaderDto> headerDtoList = headerService.findHeaderList("项目类型", Constant.EnumState.YES.getValue());//选中的表字段
            List<Header> headerList = headerService.findHeaderByType("项目类型");//所有 表字段

            String orderStr = "case reviewstage when '项目建议书' then 1 when '可行性研究报告' then 2 when '项目概算' then 3" +
                    " when '资金申请报告' then 4  when '进口设备' then 5  when '设备清单（国产）'then  6  when '设备清单（进口）'then 7" +
                    " else 8 end ";
            List<SignDispaWork> signDispaWorkList =signDispaWorkRepo.findByIds(SignDispaWork_.signid.getName() , signIds,orderStr);

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
            HSSFWorkbook wb = excelTools.createExcelBook(title, headerPair, signDispaWorkList, SignDispaWork.class);
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
    @RequestMapping(name = "项目统计导出", path = "excelExport2", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void excelExport2(HttpServletResponse resp,HttpServletRequest request, @RequestParam(required = true) String signIds) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try {
            String orderStr = "case reviewstage when '项目建议书' then 1 when '可行性研究报告' then 2 when '项目概算' then 3" +
                    " when '资金申请报告' then 4  when '进口设备' then 5  when '设备清单（国产）'then  6  when '设备清单（进口）'then 7" +
                    " else 8 end ";
            List<SignDispaWork> signDispaWorkList =signDispaWorkRepo.findByIds(SignDispaWork_.signid.getName() , signIds,orderStr);
            resultMap.put("proCountList", signDispaWorkList);
            Map<String, Object> funcs = new HashMap<>(2);
            funcs.put("proUtils", new ExcelJxlsUtls());
            resp.setCharacterEncoding(UTF_8.name());
            resp.setContentType("application/vnd.ms-excel");
            String filename = new String("项目查询统计报表.xls"), userAgent = request.getHeader("user-agent");
            // 判断是否 ie 浏览器
            if (userAgent.indexOf("MSIE") != -1 || userAgent.indexOf("Trident") != -1 || userAgent.indexOf("Edge") != -1) {
                filename = new String(filename.getBytes(GBK), ISO_8859_1);
            } else {
                filename = new String(filename.getBytes(UTF_8), ISO_8859_1);
            }
            resp.setHeader("Content-disposition", "attachment;filename=" + filename);
            resp.setHeader("Content-disposition", "attachment;filename=" + filename);
            JxlsUtils.exportExcel("classpath:jxls/proInfoCount.xls", resp.getOutputStream(), resultMap,funcs);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @RequiresPermissions("signView#html/signList#get")
    @RequestMapping(name = "项目查询统计", path = "html/signList", method = RequestMethod.GET)
    public String signList() {
        return "sign/signList";
    }


    @RequiresAuthentication
    @RequestMapping(name="个人经办项目",path="html/personMainTasks",method=RequestMethod.POST)
    @ResponseBody
    public PageModelDto<SignDispaWork> personMainTasks(HttpServletRequest request) throws ParseException {
        ODataObj odataObj = new ODataObj(request);
        PageModelDto<SignDispaWork> pageModelDto = signDispaWorkService.findMyDoProject(odataObj,true);
        return pageModelDto;
    }

    @RequiresAuthentication
    @RequestMapping(name="个人协办项目",path="html/personAssistTasks",method=RequestMethod.POST)
    @ResponseBody
    public PageModelDto<SignDispaWork> personAssistTasks(HttpServletRequest request) throws ParseException {
        ODataObj odataObj = new ODataObj(request);
        PageModelDto<SignDispaWork> pageModelDto = signDispaWorkService.findMyDoProject(odataObj,false);
        return pageModelDto;
    }

    @RequiresAuthentication
    @RequestMapping(name = "通过时间段获取项目信息(按评审阶段)" , path = "findByTime" , method = RequestMethod.POST)
    @ResponseBody
    public ResultMsg findByTime(@RequestParam String startTime , @RequestParam String endTime){
        return signDispaWorkService.findByTime(startTime , endTime);
    }
    @RequiresAuthentication
    @RequestMapping(name = "通过时间段获取项目信息(按评审阶段、项目类别)" , path = "findByTypeAndReview" , method = RequestMethod.POST)
    @ResponseBody
    public ResultMsg findByTypeAndReview(@RequestParam String startTime , @RequestParam String endTime){
        return signDispaWorkService.findByTypeAndReview(startTime , endTime);
    }

    @RequiresAuthentication
    @RequestMapping(name = "获取员工业绩汇总" , path = "getAchievementSum" , method = RequestMethod.POST)
    @ResponseBody
    public ResultMsg getAchievementSum(@RequestBody AchievementSumDto achievementSumDto){
        return expertSelectedService.findAchievementSum(achievementSumDto);

       // return null;
    }

    @RequiresAuthentication
    @RequestMapping(name = "员工业绩统计表导出" , path = "exportAchievementSum" , method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.OK)
    public void exportPersonalAchievement(HttpServletResponse resp, @RequestBody AchievementSumDto achievementSumDto){
        ServletOutputStream sos = null;
        InputStream is = null ;
        try{
            ResultMsg resultMsg = expertSelectedService.findAchievementSum(achievementSumDto);
            Map<String , Object> dataMap = new HashMap<>();
            Map<String,Object> resultMap = (Map<String,Object>) resultMsg.getReObj();
           if(Validate.isObject(resultMap.get("level"))){
               Integer level = (Integer) resultMap.get("level");
               if(level == 0){
                   dataMap.put("topicDetailList", topicMaintainService.findTopicAll(SessionUtil.getUserId()));
               }
           }
           List<AchievementSumDto> achievementSumDtoList = (List<AchievementSumDto> )resultMap.get("achievementSumList");
           if(achievementSumDtoList.size() > 0){
               if(achievementSumDtoList.size() == 2){
                   dataMap.put("assistDoc",achievementSumDtoList.get(0));
                   dataMap.put("mainDoc",achievementSumDtoList.get(1));
               }else {
                   AchievementSumDto temp = new AchievementSumDto();
                   temp.setDisSum(0);
                   temp.setExtraRateSum(BigDecimal.ZERO);
                   temp.setIsmainuser("0");
                   temp.setExtravalueSum(BigDecimal.ZERO);
                   temp.setAuthorizevalueSum(BigDecimal.ZERO);
                   temp.setDeclarevalueSum(BigDecimal.ZERO);
                  if("9".equals(achievementSumDtoList.get(0).getIsmainuser())){
                      dataMap.put("assistDoc",temp);
                      dataMap.put("mainDoc",achievementSumDtoList.get(0));
                   }else if("0".equals(achievementSumDtoList.get(0).getIsmainuser())){
                      dataMap.put("mainDoc",temp);
                      dataMap.put("assistDoc",achievementSumDtoList.get(0));
                  }
               }

           }
            dataMap.put("userName",SessionUtil.getDisplayName());
            dataMap.put("dept",SessionUtil.getUserInfo().getOrg().getName());
            dataMap.put("year",achievementSumDto.getYear());
            String[] quarterArr = getMonthByQuarterFlag(achievementSumDto.getQuarter());
            dataMap.put("beginMonth",quarterArr[0]);
            dataMap.put("endMonth",quarterArr[1]);
            String[] date = DateUtils.converToString(new Date(),"").split("-");
            dataMap.put("currentYear",date[0]);
            dataMap.put("currentMonth",date[1].charAt(0)=='0'?date[1].charAt(1):date[1]);
            dataMap.put("currentDay",date[2].charAt(0)=='0'?date[2].charAt(1):date[2]);
            String path = SysFileUtil.getUploadPath() + File.separator + Tools.generateRandomFilename() + Constant.Template.WORD_SUFFIX.getKey();
            File file = TemplateUtil.createDoc(dataMap , Constant.Template.ACHIEVEMENT_DETAIL.getKey() , path);
            String fileName = "员工业绩统计表.doc" ;
            ResponseUtils.setResponeseHead(Constant.Template.WORD_SUFFIX.getKey() , resp);
            resp.setHeader("Content-Disposition", "attachment; filename="
                    + new String(fileName.getBytes("GB2312"), "ISO8859-1"));
            int bytesum = 0 , byteread = 0 ;
            is = new FileInputStream(file);
            sos = resp.getOutputStream();
            byte[] buffer = new byte[1024];
            while ( (byteread = is.read(buffer)) != -1) {
                bytesum += byteread; //字节数 文件大小
                sos.write(buffer, 0, byteread);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (sos != null) {
                try {
                    sos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    @RequiresAuthentication
    @RequestMapping(name = "部门业绩统计表导出" , path = "exportDeptAchievementSum" , method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.OK)
    public void exportDeptAchievement(HttpServletResponse resp, @RequestBody AchievementDeptDetailDto achievementSumDto){
        ServletOutputStream sos = null;
        InputStream is = null ;
        try{
            ResultMsg resultMsg = expertSelectedService.findDeptAchievementDetail(achievementSumDto);
            Map<String , Object> dataMap = new HashMap<>();
            Map<String,Object> resultMap = (Map<String,Object>) resultMsg.getReObj();
           List<AchievementDeptDetailDto> achievementDeptDetailList = new ArrayList<AchievementDeptDetailDto>();
           if(Validate.isObject(resultMap.get("achievementDeptDetailList"))){
               achievementDeptDetailList = (List<AchievementDeptDetailDto> )resultMap.get("achievementDeptDetailList");
           }
            dataMap.put("achievementDeptDetailList",achievementDeptDetailList);
            dataMap.put("year",achievementSumDto.getYear());
            dataMap.put("dept",achievementSumDto.getDeptNames());
            String[] date = DateUtils.converToString(new Date(),"").split("-");
            dataMap.put("currentYear",date[0]);
            dataMap.put("currentMonth",date[1].charAt(0)=='0'?date[1].charAt(1):date[1]);
            dataMap.put("currentDay",date[2].charAt(0)=='0'?date[2].charAt(1):date[2]);
            String path = SysFileUtil.getUploadPath() + File.separator + Tools.generateRandomFilename() + Constant.Template.WORD_SUFFIX.getKey();
            File file = TemplateUtil.createDoc(dataMap , Constant.Template.ACHIEVEMENT_DEPT_DETAIL.getKey() , path);
            String fileName = "部门业绩统计表.doc" ;
            ResponseUtils.setResponeseHead(Constant.Template.WORD_SUFFIX.getKey() , resp);
            resp.setHeader("Content-Disposition", "attachment; filename="
                    + new String(fileName.getBytes("GB2312"), "ISO8859-1"));
            int bytesum = 0 , byteread = 0 ;
            is = new FileInputStream(file);
            sos = resp.getOutputStream();
            byte[] buffer = new byte[1024];
            while ( (byteread = is.read(buffer)) != -1) {
                bytesum += byteread; //字节数 文件大小
                sos.write(buffer, 0, byteread);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (sos != null) {
                try {
                    sos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    @RequiresAuthentication
    @RequestMapping(name = "课题研究及其他业务导出" , path = "exportTopicMaintainInfo" , method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.OK)
    public void exportTopicMaintainInfo(HttpServletResponse resp,@RequestParam(required = true) String userId){
        ServletOutputStream sos = null;
        InputStream is = null ;
        try{
            List<TopicMaintainDto> topicMaintainList = topicMaintainService.findTopicAll(userId);
            Map<String , Object> dataMap = new HashMap<>();
            dataMap.put("topicMaintainList",topicMaintainList);
            dataMap.put("userName",SessionUtil.getDisplayName());
            String[] date = DateUtils.converToString(new Date(),"").split("-");
            dataMap.put("year",date[0]);
            dataMap.put("currentYear",date[0]);
            dataMap.put("currentMonth",date[1].charAt(0)=='0'?date[1].charAt(1):date[1]);
            dataMap.put("currentDay",date[2].charAt(0)=='0'?date[2].charAt(1):date[2]);
            String path = SysFileUtil.getUploadPath() + File.separator + Tools.generateRandomFilename() + Constant.Template.WORD_SUFFIX.getKey();
            File file = TemplateUtil.createDoc(dataMap , Constant.Template.ACHIEVEMENT_TOPIC_MAINTAIN.getKey() , path);
            String fileName = "课题研究及其他业务一览表.doc" ;
            ResponseUtils.setResponeseHead(Constant.Template.WORD_SUFFIX.getKey() , resp);
            resp.setHeader("Content-Disposition", "attachment; filename="
                    + new String(fileName.getBytes("GB2312"), "ISO8859-1"));
            int bytesum = 0 , byteread = 0 ;
            is = new FileInputStream(file);
            sos = resp.getOutputStream();
            byte[] buffer = new byte[1024];
            while ( (byteread = is.read(buffer)) != -1) {
                bytesum += byteread; //字节数 文件大小
                sos.write(buffer, 0, byteread);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (sos != null) {
                try {
                    sos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    @RequiresAuthentication
    @RequestMapping(name = "主/协办人项目导出" , path = "exportProReview" , method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.OK)
    public void exportProReview(HttpServletResponse resp, @RequestBody AchievementSumDto achievementSumDto){
        ServletOutputStream sos = null;
        InputStream is = null ;
        try{
            ResultMsg resultMsg = expertSelectedService.findAchievementSum(achievementSumDto);
            Map<String , Object> dataMap = new HashMap<>();
            Map<String,Object> resultMap = (Map<String,Object>) resultMsg.getReObj();
            List<AchievementDetailDto> achievementMainList = new ArrayList<AchievementDetailDto>();
            List<AchievementDetailDto> achievementAssistList = new ArrayList<AchievementDetailDto>();
            if(Validate.isObject(resultMap.get("achievementMainList"))){
                achievementMainList = (List<AchievementDetailDto>)resultMap.get("achievementMainList");
                achievementAssistList = (List<AchievementDetailDto>)resultMap.get("achievementAssistList");
            }
            dataMap.put("achievementMainList",achievementMainList);
            dataMap.put("achievementAssistList",achievementAssistList);
            dataMap.put("name",SessionUtil.getDisplayName());
            dataMap.put("year",achievementSumDto.getYear());
            String[] quarterArr = getMonthByQuarterFlag(achievementSumDto.getQuarter());
            dataMap.put("beginMonth",quarterArr[0]);
            dataMap.put("endMonth",quarterArr[1]);
            String[] date = DateUtils.converToString(new Date(),"").split("-");
            dataMap.put("currentYear",date[0]);
            dataMap.put("currentMonth",date[1].charAt(0)=='0'?date[1].charAt(1):date[1]);
            dataMap.put("currentDay",date[2].charAt(0)=='0'?date[2].charAt(1):date[2]);
            File file;
            String fileName = "";
            String path = SysFileUtil.getUploadPath() + File.separator + Tools.generateRandomFilename() + Constant.Template.WORD_SUFFIX.getKey();
            if("9".equals(achievementSumDto.getIsMainPro())){
                file = TemplateUtil.createDoc(dataMap , Constant.Template.ACHIEVEMENT_MAIN_PROREVIEW.getKey() , path);
                fileName = "主办人评审项目一览表.doc" ;
            }else{
                file = TemplateUtil.createDoc(dataMap , Constant.Template.ACHIEVEMENT_ASSIST_PROREVIEW.getKey() , path);
                fileName = "协办人评审项目一览表.doc" ;
            }

            ResponseUtils.setResponeseHead(Constant.Template.WORD_SUFFIX.getKey() , resp);
            resp.setHeader("Content-Disposition", "attachment; filename="
                    + new String(fileName.getBytes("GB2312"), "ISO8859-1"));
            int bytesum = 0 , byteread = 0 ;
            is = new FileInputStream(file);
            sos = resp.getOutputStream();
            byte[] buffer = new byte[1024];
            while ( (byteread = is.read(buffer)) != -1) {
                bytesum += byteread; //字节数 文件大小
                sos.write(buffer, 0, byteread);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (sos != null) {
                try {
                    sos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    private String[] getMonthByQuarterFlag(String quarter){
        String[] quarterArr = new String[2];
        if(Validate.isString(quarter)){
            if("0".equals(quarter)){
                quarterArr[0]="1";
                quarterArr[1]="12";
            }else if("1".equals(quarter)){
                quarterArr[0]="1";
                quarterArr[1]="3";
            }else if("2".equals(quarter)){
                quarterArr[0]="4";
                quarterArr[1]="6";
            }else if("3".equals(quarter)){
                quarterArr[0]="7";
                quarterArr[1]="9";
            }else if("4".equals(quarter)){
                quarterArr[0]="10";
                quarterArr[1]="12";
            }
        }else{
            quarterArr[0]="1";
            quarterArr[1]="12";
        }
        return  quarterArr;
    }

    @RequiresAuthentication
    @RequestMapping(name = "获取部门业绩明细" , path = "getAchievementDeptDetail" , method = RequestMethod.POST)
    @ResponseBody
    public ResultMsg getAchievementDeptDetail(@RequestBody AchievementDeptDetailDto achievementSumDto){
        return expertSelectedService.findDeptAchievementDetail(achievementSumDto);
    }

    @RequiresAuthentication
    @RequestMapping(name="项目评审情况汇总(按照申报投资金额)" , path="pieDate" , method = RequestMethod.POST)
    @ResponseBody
    public ResultMsg pieDate(@RequestParam String startTime ,@RequestParam  String endTime){

        Date start = DateUtils.converToDate(startTime , "yyyy-MM-dd");
        Date end = DateUtils.converToDate(endTime , "yyyy-MM-dd");
        Integer[][] result = null;
        Boolean b = false;
        if(DateUtils.daysBetween(start , end) >0){
            Integer[] integers = expertSelectedService.proReviewCondByDeclare(startTime , endTime);
            if(integers.length>0){
                //存项目数目
                Integer[] result2= new Integer[integers.length - 1];
                //对项目数目的百分比
                Integer[] result3 = new Integer[integers.length - 1];

                for(int i=0 ; i<integers.length-1 ; i++){
                    if(integers[integers.length-1] > 0 ){
                        b = true;
                        result2[i] = integers[i];
                        double temp = (double)integers[i]/(double)integers[integers.length-1]*100;
                        String str = String.format("%.0f",temp);
                        result3[i] =Integer.valueOf(str);
                    }
                }
                result = new Integer[][]{result2 , result3 ,{integers[ integers.length -1 ]}};
            }
            if(b){
                return new ResultMsg(true , Constant.MsgCode.OK.getValue() , "查询数据成功" , result);
            }else{
                return new ResultMsg(true , Constant.MsgCode.OK.getValue() , "查询数据失败" , null);
            }

        }else{
            return new ResultMsg(false , Constant.MsgCode.ERROR.getValue() , "结束日期必须大于开始日期！" , null);
        }
    }

    @RequiresAuthentication
    @RequestMapping(name="获取半年前的日期" , path="getDate" , method = RequestMethod.POST)
    @ResponseBody
    public Date getDate(){
        Date now = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(now);
        calendar.add(Calendar.MONTH , -6);
        return calendar.getTime();

    }

    @RequiresAuthentication
    @RequestMapping(name="对秘密项目进行权限限制" , path="findSecretProPermission" , method = RequestMethod.POST)
    @ResponseBody
    public ResultMsg findSecretProPermission(@RequestParam String signId){
        return signDispaWorkService.findSecretProPermission(signId);
    }

    @RequiresAuthentication
    @RequestMapping(name = "计算剩余工作日" , path = "admin/countWeekDays" , method = RequestMethod.POST )
    @ResponseBody
    public ResultMsg countWeekDays(@RequestParam String oldSignDate ,  @RequestParam String signDate){
        return signDispaWorkService.countWeekDays(DateUtils.converToDate1(oldSignDate , "yyyy-MM-dd") , DateUtils.converToDate1(signDate , "yyyy-MM-dd"));
    }



    @RequiresPermissions("signView#html/signChart#get")
    @RequestMapping(name="项目统计分析" , path = "html/signChart" , method = RequestMethod.GET)
    public String signChart(){
        return "sign/signChart";
    }

    @RequiresPermissions("signView#html/list#get")
    @RequestMapping(name="优秀评审报告查询" , path="html/list" , method = RequestMethod.GET)
    public String list(){
        return "reviewProjectAppraise/list";
    }

    @RequiresPermissions("signView#html/expertReviewCondCount#get")
    @RequestMapping(name = "专家评审情况统计", path = "html/expertReviewCondCount", method = RequestMethod.GET)
    public String expertReviewCondCount() {
        return "financial/expertReviewCondCount";
    }

    @RequiresPermissions("signView#html/proReviewConCount#get")
    @RequestMapping(name = "项目评审情况统计", path = "html/proReviewConCount", method = RequestMethod.GET)
    public String proReviewConditionCount() {
        return  "financial/proReviewConCount";
    }

    @RequiresPermissions("signView#html/achievement#get")
    @RequestMapping(name = "业绩统计", path = "html/achievement", method = RequestMethod.GET)
    public String achievementSum() {
        return  "achievement/achievementSum";
    }


}
