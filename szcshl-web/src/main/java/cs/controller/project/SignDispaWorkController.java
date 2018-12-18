package cs.controller.project;

import com.sn.framework.jxls.JxlsUtils;
import cs.ahelper.MudoleAnnotation;
import cs.ahelper.projhelper.ProjUtil;
import cs.common.ResultMsg;
import cs.common.constants.Constant;
import cs.common.utils.*;
import cs.domain.project.SignDispaWork;
import cs.domain.project.SignDispaWork_;
import cs.domain.sys.Header;
import cs.domain.sys.OrgDept;
import cs.domain.sys.OrgDept_;
import cs.model.PageModelDto;
import cs.model.project.Achievement;
import cs.model.project.AchievementSumDto;
import cs.model.project.SignDispaWorkDto;
import cs.model.sys.HeaderDto;
import cs.model.topic.TopicMaintainDto;
import cs.repository.odata.ODataObj;
import cs.repository.repositoryImpl.project.SignDispaWorkRepo;
import cs.repository.repositoryImpl.sys.OrgDeptRepo;
import cs.repository.repositoryImpl.sys.UserRepo;
import cs.service.expert.ExpertSelectedService;
import cs.service.project.SignDispaWorkService;
import cs.service.sys.HeaderService;
import cs.service.topic.TopicMaintainService;
import cs.sql.ProjSql;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
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
import java.util.*;

import static com.sn.framework.common.StringUtil.*;
import static cs.common.constants.Constant.ERROR_MSG;

/**
 * 项目详情信息视图控制器
 * Created by ldm on 2017/9/17 0017.
 */
@Controller
@RequestMapping(name = "收文", path = "signView")
@MudoleAnnotation(name = "查询统计", value = "permission#queryStatistics")
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
    @Autowired
    private OrgDeptRepo orgDeptRepo;
    @Autowired
    private UserRepo userRepo;

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
    public List<SignDispaWork> unMergeWPSign(@RequestParam String signId) {
        List<SignDispaWork> signDispaWorkList = signDispaWorkService.unMergeWPSign(signId);
        if (!Validate.isList(signDispaWorkList)) {
            signDispaWorkList = new ArrayList<>();
        }
        return signDispaWorkList;
    }

    @RequiresAuthentication
    //@RequiresPermissions("signView#getMergeSignBySignId#post")
    @RequestMapping(name = "获取已选合并评审项目", path = "getMergeSignBySignId", method = RequestMethod.POST)
    @ResponseBody
    public List<SignDispaWork> getMergeSignBySignId(@RequestParam String signId) {
        List<SignDispaWork> signDispaWorkList = signDispaWorkService.getMergeWPSignBySignId(signId);
        if (!Validate.isList(signDispaWorkList)) {
            signDispaWorkList = new ArrayList<>();
        }
        return signDispaWorkList;
    }

    @RequiresAuthentication
    //@RequiresPermissions("signView#unMergeDISSign#post")
    @RequestMapping(name = "待选合并发文项目", path = "unMergeDISSign", method = RequestMethod.POST)
    @ResponseBody
    public List<SignDispaWork> unMergeDISSign(@RequestParam String signId) {
        List<SignDispaWork> signDispaWorkList = signDispaWorkService.unMergeDISSign(signId);
        if (!Validate.isList(signDispaWorkList)) {
            signDispaWorkList = new ArrayList<>();
        }
        return signDispaWorkList;
    }

    @RequiresAuthentication
    //@RequiresPermissions("signView#getMergeDISSign#post")
    @RequestMapping(name = "获取已选合并发文项目", path = "getMergeDISSign", method = RequestMethod.POST)
    @ResponseBody
    public List<SignDispaWork> getMergeDISSign(@RequestParam String signId) {
        List<SignDispaWork> signDispaWorkList = signDispaWorkService.getMergeDISSignBySignId(signId);
        if (!Validate.isList(signDispaWorkList)) {
            signDispaWorkList = new ArrayList<>();
        }
        return signDispaWorkList;
    }

    @RequiresAuthentication
    @RequestMapping(name = "保存合并项目", path = "mergeSign", method = RequestMethod.POST)
    @ResponseBody
    public ResultMsg mergeSign(@RequestParam String signId, @RequestParam String mergeIds, @RequestParam String mergeType) {
        ResultMsg resultMsg = signDispaWorkService.mergeSign(signId, mergeIds, mergeType);
        if (!Validate.isObject(resultMsg)) {
            resultMsg = ResultMsg.error(ERROR_MSG);
        }
        return resultMsg;
    }

    @RequiresAuthentication
    //@RequiresPermissions("signView#cancelMergeSign#post")
    @RequestMapping(name = "解除合并评审发文", path = "cancelMergeSign", method = RequestMethod.POST)
    @ResponseBody
    public ResultMsg cancelMergeSign(@RequestParam String signId, String cancelIds, @RequestParam String mergeType) {
        ResultMsg resultMsg = signDispaWorkService.cancelMergeSign(signId, cancelIds, mergeType);
        if (!Validate.isObject(resultMsg)) {
            resultMsg = ResultMsg.error(ERROR_MSG);
        }
        return resultMsg;
    }

    @RequiresAuthentication
    //@RequiresPermissions("signView#deleteAllMerge#post")
    @RequestMapping(name = "删除所有合并项目", path = "deleteAllMerge", method = RequestMethod.POST)
    @ResponseBody
    public ResultMsg deleteAllMerge(@RequestParam String signId, @RequestParam String mergeType, @RequestParam String businessId) {
        ResultMsg resultMsg = signDispaWorkService.deleteAllMerge(signId, mergeType, businessId);
        if (!Validate.isObject(resultMsg)) {
            resultMsg = ResultMsg.error(ERROR_MSG);
        }
        return resultMsg;
    }

    @RequiresAuthentication
    @RequestMapping(name = "通过条件查询进行统计分析", path = "QueryStatistics", method = RequestMethod.POST)
    @ResponseBody
    public List<SignDispaWork> queryStatistics(@RequestParam String queryData, @RequestParam int page) {
        List<SignDispaWork> signDispaWorkList = signDispaWorkService.queryStatistics(queryData, page);
        if (!Validate.isList(signDispaWorkList)) {
            signDispaWorkList = new ArrayList<>();
        }
        return signDispaWorkList;
    }

    @RequiresAuthentication
    @RequestMapping(name = "项目统计导出", path = "excelExport", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void excelExport(HttpServletResponse resp, @RequestParam(required = true) String signIds) {
        String fileName = "项目查询统计报表";
        ExcelTools excelTools = new ExcelTools();
        try {
            String title = java.net.URLDecoder.decode(fileName, "UTF-8");
            ServletOutputStream sos = resp.getOutputStream();
            List<HeaderDto> headerDtoList = headerService.findHeaderList("项目类型", Constant.EnumState.YES.getValue());//选中的表字段
            List<Header> headerList = headerService.findHeaderByType("项目类型");//所有 表字段
            List<SignDispaWork> signDispaWorkList = signDispaWorkRepo.findByIds(SignDispaWork_.signid.getName(), signIds, ProjSql.ProjCountSql());

            String[] headerPair;
            if (headerDtoList.size() > 0) {
                headerPair = new String[headerDtoList.size()];
                for (int i = 0; i < headerDtoList.size(); i++) {
                    headerPair[i] = headerDtoList.get(i).getHeaderName() + "=" + headerDtoList.get(i).getHeaderKey();
                }
            } else {
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
    public void excelExport2(HttpServletResponse resp, HttpServletRequest request, @RequestParam(required = true) String signIds) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try {
           /* String orderStr = "case reviewstage when '项目建议书' then 1 when '可行性研究报告' then 2 when '项目概算' then 3" +
                    " when '资金申请报告' then 4  when '进口设备' then 5  when '设备清单（国产）'then  6  when '设备清单（进口）'then 7" +
                    " else 8 end ";*/
            List<SignDispaWork> signDispaWorkList = signDispaWorkRepo.findByIds(SignDispaWork_.signid.getName(), signIds, ProjSql.ProjCountSql());
            resultMap.put("proCountList", signDispaWorkList);
            Map<String, Object> funcs = new HashMap<>(2);
            funcs.put("proUtils", new ExcelJxlsUtls());
            resp.setCharacterEncoding(UTF_8.name());
            resp.setContentType("application/vnd.ms-excel");
            String filename = "项目查询统计报表.xls", userAgent = request.getHeader("user-agent");
            // 判断是否 ie 浏览器
            if (userAgent.indexOf("MSIE") != -1 || userAgent.indexOf("Trident") != -1 || userAgent.indexOf("Edge") != -1) {
                filename = new String(filename.getBytes(GBK), ISO_8859_1);
            } else {
                filename = new String(filename.getBytes(UTF_8), ISO_8859_1);
            }
            resp.setHeader("Content-disposition", "attachment;filename=" + filename);
            resp.setHeader("Content-disposition", "attachment;filename=" + filename);
            JxlsUtils.exportExcel("classpath:jxls/proInfoCount.xls", resp.getOutputStream(), resultMap, funcs);

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
    @RequestMapping(name = "个人经办项目", path = "html/personMainTasks", method = RequestMethod.POST)
    @ResponseBody
    public PageModelDto<SignDispaWork> personMainTasks(HttpServletRequest request) throws ParseException {
        ODataObj odataObj = new ODataObj(request);
        PageModelDto<SignDispaWork> pageModelDto = signDispaWorkService.findMyDoProject(odataObj, true);
        if (!Validate.isObject(pageModelDto)) {
            pageModelDto = new PageModelDto();
        }
        return pageModelDto;
    }

    @RequiresAuthentication
    @RequestMapping(name = "个人协办项目", path = "html/personAssistTasks", method = RequestMethod.POST)
    @ResponseBody
    public PageModelDto<SignDispaWork> personAssistTasks(HttpServletRequest request) throws ParseException {
        ODataObj odataObj = new ODataObj(request);
        PageModelDto<SignDispaWork> pageModelDto = signDispaWorkService.findMyDoProject(odataObj, false);
        if (!Validate.isObject(pageModelDto)) {
            pageModelDto = new PageModelDto();
        }
        return pageModelDto;
    }

    @RequiresAuthentication
    @RequestMapping(name = "通过时间段获取项目信息(按评审阶段)", path = "findByTime", method = RequestMethod.POST)
    @ResponseBody
    public ResultMsg findByTime(@RequestParam String startTime, @RequestParam String endTime) {
        ResultMsg resultMsg = signDispaWorkService.findByTime(startTime, endTime);
        if (!Validate.isObject(resultMsg)) {
            resultMsg = ResultMsg.error(ERROR_MSG);
        }
        return resultMsg;
    }

    @RequiresAuthentication
    @RequestMapping(name = "通过时间段获取项目信息(按评审阶段、项目类别)", path = "findByTypeAndReview", method = RequestMethod.POST)
    @ResponseBody
    public ResultMsg findByTypeAndReview(@RequestParam String startTime, @RequestParam String endTime) {
        ResultMsg resultMsg = signDispaWorkService.findByTypeAndReview(startTime, endTime);
        if (!Validate.isObject(resultMsg)) {
            resultMsg = ResultMsg.error(ERROR_MSG);
        }
        return resultMsg;
    }

    @RequiresAuthentication
    @RequestMapping(name = "业绩统计汇总", path = "getAchievementSum", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> getAchievementSum(@RequestBody AchievementSumDto achievementSumDto) {
        /*return  expertSelectedService.findAchievementSum(achievementSumDto);*/
        return signDispaWorkService.countAchievementSum(achievementSumDto);
    }

    @RequiresAuthentication
    @RequestMapping(name = "部门业绩统计表导出", path = "exportDeptAchievementSum", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.OK)
    public void exportDeptAchievement(HttpServletResponse resp, @RequestBody AchievementSumDto achievementSumDto) {
        ServletOutputStream sos = null;
        InputStream is = null;
        try {
            Map<String, Object> deptAchievementMap = signDispaWorkService.countAchievementSum(achievementSumDto);
            String[] monthArr = ProjUtil.getQuarterMonth(achievementSumDto.getQuarter());
            String smonth = monthArr[0], emonth = monthArr[1];

            Map<String, Object> dataMap = new HashMap<>();
            dataMap.put("achievementDeptDetailList", deptAchievementMap.get("orgDeptDetailList"));
            dataMap.put("year", achievementSumDto.getYear());
            dataMap.put("smonth", smonth);
            dataMap.put("emonth", emonth);
            String[] date = DateUtils.converToString(new Date(), DateUtils.DATE_PATTERN).split("-");
            dataMap.put("cyear", date[0]);
            dataMap.put("cmonth", date[1].charAt(0) == '0' ? date[1].charAt(1) : date[1]);
            dataMap.put("cday", date[2].charAt(0) == '0' ? date[2].charAt(1) : date[2]);
            String path = SysFileUtil.getUploadPath() + File.separator + Tools.generateRandomFilename() + Constant.Template.WORD_SUFFIX.getKey();
            File file = TemplateUtil.createDoc(dataMap, Constant.Template.ACHIEVEMENT_DEPT_DETAIL.getKey(), path);
            String fileName = "部门业绩统计表.doc";
            ResponseUtils.setResponeseHead(Constant.Template.WORD_SUFFIX.getKey(), resp);
            resp.setHeader("Content-Disposition", "attachment; filename="
                    + new String(fileName.getBytes("GB2312"), "ISO8859-1"));
            int byteread = 0;
            is = new FileInputStream(file);
            sos = resp.getOutputStream();
            byte[] buffer = new byte[1024];
            while ((byteread = is.read(buffer)) != -1) {
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

    /**
     * 主办协办，项目导出一览表
     *
     * @param resp
     * @param yearName
     * @param quarter
     * @param level
     * @param isMainUser
     */
    @RequiresAuthentication
    @RequestMapping(name = "主/协办人项目导出", path = "exportProReview", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.OK)
    public void exportProReview(HttpServletResponse resp, @RequestParam String yearName, @RequestParam String quarter, @RequestParam int level, String isMainUser) {
        ServletOutputStream sos = null;
        InputStream is = null;
        try {
            Map<String, Object> resultMap = new HashMap<>();
            List<OrgDept> orgDeptList = new ArrayList<>();
            //用户ID和用户所在部门ID
            String userId = SessionUtil.getUserId(),orgId = SessionUtil.getUserInfo().getOrg().getId();
            if (level > 0) {
                Criteria criteria = orgDeptRepo.getExecutableCriteria();
                criteria.add(Restrictions.eq(OrgDept_.directorID.getName(), SessionUtil.getUserId()));
                orgDeptList = criteria.list();
                //当前查询的部门ID或者组别ID，并不是用户做在部门ID
                if(Validate.isList(orgDeptList)){
                    orgId = orgDeptList.get(0).getId();
                }
            }
            //二、查询业绩统计信息
            List<Achievement> countList = signDispaWorkRepo.countAchievement(yearName, quarter, orgId, userId, level);
            signDispaWorkService.countAchievementDetail(resultMap, level, countList, orgDeptList);
            AchievementSumDto achSumDto = null;
            if(level > 0){
                achSumDto = (AchievementSumDto) resultMap.get("orgDeptSum");
            }else {
                achSumDto = (AchievementSumDto) resultMap.get("userSum");
            }
            Map<String, Object> dataMap = new HashMap<>();
            //是否主办
            String fileName = "";
            boolean isMain = Constant.EnumState.YES.getValue().equals(isMainUser);
            if (isMain) {
                dataMap.put("titleName", "主办人评审项目一览表");
                dataMap.put("achievementList", achSumDto.getMainChildList());
                fileName = "主办人评审项目一览表.doc";
            } else {
                dataMap.put("achievementList", achSumDto.getAssistChildList());
                dataMap.put("titleName", "协办人评审项目一览表");
                fileName = "协办人评审项目一览表.doc";
            }
            String[] monthArr = ProjUtil.getQuarterMonth(quarter);
            String smonth = monthArr[0], emonth = monthArr[1];
            dataMap.put("year", yearName);
            dataMap.put("smonth", smonth);
            dataMap.put("emonth", emonth);
            String[] date = DateUtils.converToString(new Date(), "").split("-");
            dataMap.put("cyear", date[0]);
            dataMap.put("cmonth", date[1].charAt(0) == '0' ? date[1].charAt(1) : date[1]);
            dataMap.put("cday", date[2].charAt(0) == '0' ? date[2].charAt(1) : date[2]);
            File file;
            String path = SysFileUtil.getUploadPath() + File.separator + Tools.generateRandomFilename() + Constant.Template.WORD_SUFFIX.getKey();
            file = TemplateUtil.createDoc(dataMap, Constant.Template.ACHIEVEMENT_DETAIL.getKey(), path);

            ResponseUtils.setResponeseHead(Constant.Template.WORD_SUFFIX.getKey(), resp);
            resp.setHeader("Content-Disposition", "attachment; filename="
                    + new String(fileName.getBytes("GB2312"), "ISO8859-1"));
            int byteread = 0;
            is = new FileInputStream(file);
            sos = resp.getOutputStream();
            byte[] buffer = new byte[1024];
            while ((byteread = is.read(buffer)) != -1) {
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
    @RequestMapping(name = "员工业绩统计表导出", path = "exportUserAchievement", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.OK)
    public void exportUserAchievement(HttpServletResponse resp, @RequestParam String yearName, @RequestParam String quarter) {
        ServletOutputStream sos = null;
        InputStream is = null ;
        String userId = SessionUtil.getUserId(),userName = SessionUtil.getUserInfo().getDisplayName();
        try{
            Map<String,Object> resultMap = new HashMap<>();
            List<Achievement> countList = signDispaWorkRepo.countAchievement(yearName, quarter, null, userId, 0);
            signDispaWorkService.countAchievementDetail(resultMap, 0, countList, null);
            Map<String , Object> dataMap = new HashMap<>();
            //员工业绩统计信息
            dataMap.put("achievement",resultMap.get("userSum"));
            //员工课题信息
            List<TopicMaintainDto> allTopic = topicMaintainService.findTopicAll(userId,yearName,quarter);
            //遍历主课题和其它课题
            List<TopicMaintainDto> mainTopicList = new ArrayList<>(),otherTopicList = new ArrayList<>();
            if(Validate.isList(allTopic)){
                for(TopicMaintainDto topicMaintainDto :allTopic){
                    topicMaintainDto.setEndTimeStr(DateUtils.converToString(topicMaintainDto.getEndTime(),DateUtils.DATE_PATTERN));
                    if("1".equals(topicMaintainDto.getBusinessType())){
                        mainTopicList.add(topicMaintainDto);
                    }else{
                        otherTopicList.add(topicMaintainDto);
                    }
                }
            }
            dataMap.put("mainTopicList", mainTopicList);
            dataMap.put("otherTopicList", otherTopicList);

            dataMap.put("userName",userName);
            dataMap.put("deptName",SessionUtil.getUserInfo().getOrg().getName());
            dataMap.put("year", yearName);
            String[] monthArr = ProjUtil.getQuarterMonth(quarter);
            String smonth = monthArr[0], emonth = monthArr[1];
            dataMap.put("smonth", smonth);
            dataMap.put("emonth", emonth);
            String[] date = DateUtils.converToString(new Date(), "").split("-");
            dataMap.put("cyear", date[0]);
            dataMap.put("cmonth", date[1].charAt(0) == '0' ? date[1].charAt(1) : date[1]);
            dataMap.put("cday", date[2].charAt(0) == '0' ? date[2].charAt(1) : date[2]);
            String path = SysFileUtil.getUploadPath() + File.separator + Tools.generateRandomFilename() + Constant.Template.WORD_SUFFIX.getKey();
            File file = TemplateUtil.createDoc(dataMap , Constant.Template.ACHIEVEMENT_USER_DETAIL.getKey() , path);
            String fileName = "员工业绩统计表.doc" ;
            ResponseUtils.setResponeseHead(Constant.Template.WORD_SUFFIX.getKey() , resp);
            resp.setHeader("Content-Disposition", "attachment; filename="
                    + new String(fileName.getBytes("GB2312"), "ISO8859-1"));
            int byteread = 0 ;
            is = new FileInputStream(file);
            sos = resp.getOutputStream();
            byte[] buffer = new byte[1024];
            while ( (byteread = is.read(buffer)) != -1) {
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

    //课题这块暂不做导出
    /*@RequiresAuthentication
    @RequestMapping(name = "课题研究及其他业务导出", path = "exportTopicMaintainInfo", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.OK)
    public void exportTopicMaintainInfo(HttpServletResponse resp, @RequestParam(required = true) String userId) {
        ServletOutputStream sos = null;
        InputStream is = null;
        try {
            List<TopicMaintainDto> topicMaintainList = topicMaintainService.findTopicAll(userId);
            Map<String, Object> dataMap = new HashMap<>();
            dataMap.put("topicMaintainList", topicMaintainList);
            dataMap.put("userName", SessionUtil.getDisplayName());
            String[] date = DateUtils.converToString(new Date(), "").split("-");
            dataMap.put("year", date[0]);
            dataMap.put("currentYear", date[0]);
            dataMap.put("currentMonth", date[1].charAt(0) == '0' ? date[1].charAt(1) : date[1]);
            dataMap.put("currentDay", date[2].charAt(0) == '0' ? date[2].charAt(1) : date[2]);
            String path = SysFileUtil.getUploadPath() + File.separator + Tools.generateRandomFilename() + Constant.Template.WORD_SUFFIX.getKey();
            File file = TemplateUtil.createDoc(dataMap, Constant.Template.ACHIEVEMENT_TOPIC_MAINTAIN.getKey(), path);
            String fileName = "课题研究及其他业务一览表.doc";
            ResponseUtils.setResponeseHead(Constant.Template.WORD_SUFFIX.getKey(), resp);
            resp.setHeader("Content-Disposition", "attachment; filename="
                    + new String(fileName.getBytes("GB2312"), "ISO8859-1"));
            int byteread = 0;
            is = new FileInputStream(file);
            sos = resp.getOutputStream();
            byte[] buffer = new byte[1024];
            while ((byteread = is.read(buffer)) != -1) {
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
    }*/

    @RequiresAuthentication
    @RequestMapping(name = "获取业绩明细", path = "findAchievementDetail", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> findAchievementDetail(@RequestParam String yearName, @RequestParam String quarter, @RequestParam String id, @RequestParam int level) {
        Map<String, Object> resultMap = new HashMap<>();
        List<OrgDept> orgDeptList = new ArrayList<>();
        if (level > 0) {
            OrgDept orgDept = orgDeptRepo.findOrgDeptById(id);
            orgDeptList.add(orgDept);
            resultMap.put("showObj", orgDept);
        }
        //二、查询业绩统计信息
        List<Achievement> countList = signDispaWorkRepo.countAchievement(yearName, quarter, id, id, level);
        signDispaWorkService.countAchievementDetail(resultMap, level, countList, orgDeptList);

        return resultMap;
    }

    @RequiresAuthentication
    @RequestMapping(name = "项目评审情况汇总(按照申报投资金额)", path = "pieDate", method = RequestMethod.POST)
    @ResponseBody
    public ResultMsg pieDate(@RequestParam String startTime, @RequestParam String endTime) {
        Date start = DateUtils.converToDate(startTime, "yyyy-MM-dd");
        Date end = DateUtils.converToDate(endTime, "yyyy-MM-dd");
        Integer[][] result = null;
        Boolean b = false;
        if (DateUtils.daysBetween(start, end) > 0) {
            Integer[] integers = expertSelectedService.proReviewCondByDeclare(startTime, endTime);
            if (integers.length > 0) {
                //存项目数目
                Integer[] result2 = new Integer[integers.length - 1];
                //对项目数目的百分比
                Integer[] result3 = new Integer[integers.length - 1];

                for (int i = 0; i < integers.length - 1; i++) {
                    if (integers[integers.length - 1] > 0) {
                        b = true;
                        result2[i] = integers[i];
                        double temp = (double) integers[i] / (double) integers[integers.length - 1] * 100;
                        String str = String.format("%.0f", temp);
                        result3[i] = Integer.valueOf(str);
                    }
                }
                result = new Integer[][]{result2, result3, {integers[integers.length - 1]}};
            }
            if (b) {
                return new ResultMsg(true, Constant.MsgCode.OK.getValue(), "查询数据成功", result);
            } else {
                return new ResultMsg(true, Constant.MsgCode.OK.getValue(), "查询数据失败", null);
            }

        } else {
            return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), "结束日期必须大于开始日期！", null);
        }
    }

    @RequiresAuthentication
    @RequestMapping(name = "获取半年前的日期", path = "getDate", method = RequestMethod.POST)
    @ResponseBody
    public Date getDate() {
        Date now = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(now);
        calendar.add(Calendar.MONTH, -6);
        return calendar.getTime();
    }

    @RequiresAuthentication
    @RequestMapping(name = "对秘密项目进行权限限制", path = "findSecretProPermission", method = RequestMethod.POST)
    @ResponseBody
    public ResultMsg findSecretProPermission(@RequestParam String signId) {
        ResultMsg resultMsg = signDispaWorkService.findSecretProPermission(signId);
        if (!Validate.isObject(resultMsg)) {
            resultMsg = ResultMsg.error(ERROR_MSG);
        }
        return resultMsg;
    }

    @RequiresAuthentication
    @RequestMapping(name = "计算剩余工作日", path = "admin/countWeekDays", method = RequestMethod.POST)
    @ResponseBody
    public ResultMsg countWeekDays(@RequestParam String oldSignDate, @RequestParam String signDate) {
        ResultMsg resultMsg = signDispaWorkService.countWeekDays(DateUtils.converToDate1(oldSignDate, DateUtils.DATE_PATTERN), DateUtils.converToDate1(signDate, DateUtils.DATE_PATTERN));
        if (!Validate.isObject(resultMsg)) {
            resultMsg = ResultMsg.error(ERROR_MSG);
        }
        return resultMsg;
    }

    @RequiresPermissions("signView#html/signChart#get")
    @RequestMapping(name = "项目统计分析", path = "html/signChart", method = RequestMethod.GET)
    public String signChart() {
        return "sign/signChart";
    }

    @RequiresPermissions("signView#html/list#get")
    @RequestMapping(name = "优秀评审报告查询", path = "html/list", method = RequestMethod.GET)
    public String list() {
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
        return "financial/proReviewConCount";
    }

    @RequiresPermissions("signView#html/achievement#get")
    @RequestMapping(name = "业绩统计", path = "html/achievement", method = RequestMethod.GET)
    public String achievementSum() {
        return "achievement/achievementSum";
    }


}
