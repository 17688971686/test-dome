package cs.common.utils;

import cs.common.Constant;
import cs.domain.expert.Expert;
import cs.domain.expert.ExpertType;
import cs.domain.meeting.RoomBooking;
import cs.domain.project.*;
import cs.domain.sys.Ftp;
import cs.domain.sys.Org;
import cs.domain.sys.SysFile;
import cs.domain.sys.User;
import cs.model.expert.ProReviewConditionDto;
import cs.model.monthly.MonthlyNewsletterDto;

import java.io.File;
import java.math.BigDecimal;
import java.util.*;

/**
 * Description: 生成模板并生成附件类
 * Author: mcl
 * Date: 2017/9/4 9:05
 */
public class CreateTemplateUtils {

/********************begin发文模板**************************************/

/***********8*****study可行性研究报告模板区********************/

    /**
     * 评审意见
     *
     * @param signDispaWork
     */
    public static SysFile createStudyTemplateOpinion(Ftp f,SignDispaWork signDispaWork) {
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("projectName", signDispaWork.getProjectname());
        dataMap.put("docNum", signDispaWork.getSignnum());
        dataMap.put("explain", "xxxxx");
        dataMap.put("title1", "xxxxx");
        dataMap.put("content1", "xxxxx");
        dataMap.put("title2", "xxxxx");
        dataMap.put("content2", "xxxxx");
        dataMap.put("dateStr", DateUtils.converToString(new Date(), "yyyy年MM月dd日"));

        SysFile sysFile = TemplateUtil.createTemplate(f,signDispaWork.getSignid(),  Constant.SysFileType.SIGN.getValue(),
                Constant.SysFileType.TEMOLATE.getValue(), Constant.STAGE_STUDY,
                Constant.Template.STUDY_OPINION.getKey(), Constant.Template.STUDY_OPINION.getValue().split("_")[1],
                Constant.Template.WORD_SUFFIX.getKey(), dataMap);

        return sysFile;
    }


    /**
     * 评审组名单
     *
     * @param signDispaWork
     * @return
     */
    public static SysFile createStudyTemplateRoster(Ftp f,SignDispaWork signDispaWork, List<Expert> expertList) {
        Map<String, Object> dataMap = new HashMap<>();

        dataMap.put("mdnum", "3");
        dataMap.put("projectName", signDispaWork.getProjectname());
        dataMap.put("generalCounsel", "市发改委分管副主任");
        dataMap.put("counselor", "市发改委主办处室处长");
        dataMap.put("director", "主任");
        String leaderName = signDispaWork.getLeaderName() == null ? "" :signDispaWork.getLeaderName();
        dataMap.put("viceDirector", leaderName);
        String ministerName =  signDispaWork.getMinisterName() == null ? "" : signDispaWork.getMinisterName();
        dataMap.put("minister", ministerName);
        String projectDirector = (signDispaWork.getmUserName() == null ? "" : signDispaWork.getmUserName())
                                    + (signDispaWork.getAUserName() == null ? "" : ","+signDispaWork.getAUserName());
        dataMap.put("proojectDirector", projectDirector);

        String expertName = "";
        for (Expert expert : expertList) {
            if (!StringUtil.isBlank(expertName) && expert.getName() != null) {
                expertName += ",";
            }
            List<ExpertType> expertTypeList = expert.getExpertType();
            expertName += expert.getName();
        }

        String reviewGroup = "主任," + leaderName + "," + ministerName + "," + projectDirector;

        if(!"".equals(expertName) && expertName.length() >0){
            reviewGroup += "," + expertName;
        }

        dataMap.put("reviewGroup", reviewGroup);


        SysFile sysFile = TemplateUtil.createTemplate(f,signDispaWork.getSignid(),  Constant.SysFileType.SIGN.getValue(),
                Constant.SysFileType.TEMOLATE.getValue(), Constant.STAGE_STUDY,
                Constant.Template.STUDY_ROSTER.getKey(), Constant.Template.STUDY_ROSTER.getValue().split("_")[1],
                Constant.Template.WORD_SUFFIX.getKey(), dataMap);

        return sysFile;
    }


    /**
     * 投资估算表
     *
     * @param signDispaWork
     * @return
     */
    public static SysFile createStudyTemplateEstimate(Ftp f,SignDispaWork signDispaWork) {
        Map<String, Object> dataMap = new HashMap<>();

        dataMap.put("gsnum", "2");
        dataMap.put("projectName", signDispaWork.getProjectname());

        SysFile sysFile = TemplateUtil.createTemplate(f,signDispaWork.getSignid(),  Constant.SysFileType.SIGN.getValue(),
                Constant.SysFileType.TEMOLATE.getValue(), Constant.STAGE_STUDY,
                Constant.Template.STUDY_ESTIMATE.getKey(), Constant.Template.STUDY_ESTIMATE.getValue().split("_")[1],
                Constant.Template.WORD_SUFFIX.getKey(), dataMap);
        return sysFile;
    }


    /***********************dubget项目概算模板区*******************/

    /**
     * 审核意见
     *
     * @param signDispaWork
     * @return
     */
    public static SysFile createBudgetTemplateOpinion(Ftp f,SignDispaWork signDispaWork) {

        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("projectName", signDispaWork.getProjectname());
        dataMap.put("docNum", signDispaWork.getSignnum());
        dataMap.put("explain", "xxxxx");
        dataMap.put("title1", "xxxxx");
        dataMap.put("content1", "xxxxx");
        dataMap.put("title2", "xxxxx");
        dataMap.put("content2", "xxxxx");
        dataMap.put("dateStr", DateUtils.converToString(new Date(), "yyyy年MM月dd日"));

        SysFile sysFile = TemplateUtil.createTemplate(f,signDispaWork.getSignid(),  Constant.SysFileType.SIGN.getValue(),
                Constant.SysFileType.TEMOLATE.getValue(), Constant.STAGE_BUDGET,
                Constant.Template.BUDGET_OPINION.getKey(), Constant.Template.BUDGET_OPINION.getValue().split("_")[1],
                Constant.Template.WORD_SUFFIX.getKey(), dataMap);
        return sysFile;
    }


    /**
     * 建安工程费用
     *
     * @param signDispaWork
     * @return
     */
    public static SysFile createBudgetTemplateProjectCost(Ftp f,SignDispaWork signDispaWork) {
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("jagcfynum", "4");
        dataMap.put("projectName", signDispaWork.getProjectname());

        SysFile sysFile = TemplateUtil.createTemplate(f,signDispaWork.getSignid(),  Constant.SysFileType.SIGN.getValue(),
                Constant.SysFileType.TEMOLATE.getValue(), Constant.STAGE_BUDGET,
                Constant.Template.BUDGET_PROJECTCOST.getKey(), Constant.Template.BUDGET_PROJECTCOST.getValue().split("_")[1],
                Constant.Template.WORD_SUFFIX.getKey(), dataMap);
        return sysFile;
    }

    /**
     * 概算汇总表和审核对照表
     *
     * @param signDispaWork
     * @return
     */
    public static SysFile createBudgetTemplateEstimate(Ftp f,SignDispaWork signDispaWork) {
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("gsnum1", "1");
        dataMap.put("projectName", signDispaWork.getProjectname());
        dataMap.put("gsnum2", "2");

        SysFile sysFile = TemplateUtil.createTemplate(f,signDispaWork.getSignid(),  Constant.SysFileType.SIGN.getValue(),
                Constant.SysFileType.TEMOLATE.getValue(), Constant.STAGE_BUDGET,
                Constant.Template.BUDGET_ESTIMATE.getKey(), Constant.Template.BUDGET_ESTIMATE.getValue().split("_")[1],
                Constant.Template.EXCEL_SUFFIX.getKey(), dataMap);
        return sysFile;
    }

    /**
     * 审核组名单
     *
     * @param signDispaWork
     * @return
     */
    public static SysFile createBudgetTemplateRoster(Ftp f,SignDispaWork signDispaWork,  List<Expert> expertList) {
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("mdnum", "3");
        dataMap.put("projectName", signDispaWork.getProjectname());
        dataMap.put("generalCounsel", "市发改委分管副主任");
        dataMap.put("counselor", "市发改委主办处室处长");
        dataMap.put("director", "主任");
        String leanderName = signDispaWork.getLeaderName() == null ? "" : signDispaWork.getLeaderName();
        dataMap.put("viceDirector", leanderName);
        String ministerName = signDispaWork.getMinisterName() == null ? "" : signDispaWork.getMinisterName();
        dataMap.put("minister", ministerName);
        String projectDirector = (signDispaWork.getmUserName() == null ? "" : signDispaWork.getmUserName())
                                + (signDispaWork.getAUserName() == null ? "" : ","+signDispaWork.getAUserName());
        dataMap.put("proojectDirector", projectDirector);

        String expertName = "";
        for (Expert expert : expertList) {
            if (!StringUtil.isBlank(expertName) && expert.getName() != null) {
                expertName += ",";
            }
            List<ExpertType> expertTypeList = expert.getExpertType();
            expertName += expert.getName();
        }

        String reviewGroup = "主任," + leanderName + "," + ministerName + "," + projectDirector ;

        if(!"".equals(expertName) && expertName.length() >0){
            reviewGroup += "," + expertName;
        }
        dataMap.put("reviewGroup", reviewGroup);

        SysFile sysFile = TemplateUtil.createTemplate(f,signDispaWork.getSignid(),  Constant.SysFileType.SIGN.getValue(),
                Constant.SysFileType.TEMOLATE.getValue(), Constant.STAGE_BUDGET,
                Constant.Template.BUDGET_ROSTER.getKey(), Constant.Template.BUDGET_ROSTER.getValue().split("_")[1],
                Constant.Template.WORD_SUFFIX.getKey(), dataMap);
        return sysFile;
    }


    /***************************sug项目建议书****************************/

    /**
     * 评审意见
     *
     * @param signDispaWork
     * @return
     */
    public static SysFile createSugTemplateOpinion(Ftp f,SignDispaWork signDispaWork) {
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("projectName", signDispaWork.getProjectname());
        dataMap.put("docNum", signDispaWork.getSignnum());
        dataMap.put("explain", "xxxxx");
        dataMap.put("title1", "xxxxx");
        dataMap.put("content1", "xxxxx");
        dataMap.put("title2", "xxxxx");
        dataMap.put("content2", "xxxxx");
        System.out.print(DateUtils.converToString(new Date(), "yyyy年MM月dd日"));
        dataMap.put("dateStr", DateUtils.converToString(new Date(), "yyyy年MM月dd日"));

        SysFile sysFile = TemplateUtil.createTemplate(f,signDispaWork.getSignid(),  Constant.SysFileType.SIGN.getValue(),
                Constant.SysFileType.TEMOLATE.getValue(), Constant.STAGE_SUG,
                Constant.Template.SUG_OPINION.getKey(), Constant.Template.SUG_OPINION.getValue().split("_")[1],
                Constant.Template.WORD_SUFFIX.getKey(), dataMap);
        return sysFile;
    }

    /**
     * 投资匡算表
     *
     * @param signDispaWork
     * @return
     */
    public static SysFile createSugTemplateEstime(Ftp f,SignDispaWork signDispaWork) {

        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("ksnum", "2");
        dataMap.put("projectName", signDispaWork.getProjectname());
        SysFile sysFile = TemplateUtil.createTemplate(f,signDispaWork.getSignid(),  Constant.SysFileType.SIGN.getValue(),
                Constant.SysFileType.TEMOLATE.getValue(), Constant.STAGE_SUG,
                Constant.Template.SUG_ESTIMATE.getKey(), Constant.Template.SUG_ESTIMATE.getValue().split("_")[1],
                Constant.Template.EXCEL_SUFFIX.getKey(), dataMap);
        return sysFile;
    }

    /**
     * 评审组名单
     *
     * @param signDispaWork
     * @return
     */
    public static SysFile createSugTemplateRoster(Ftp f,SignDispaWork signDispaWork,  List<Expert> expertList) {
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("psnum", "3");
        dataMap.put("projectName", signDispaWork.getProjectname());
        dataMap.put("generalCounsel", "市发改委分管副主任");
        dataMap.put("counselor", "市发改委主办处室处长");
        dataMap.put("director", "主任");
        String leanderName = signDispaWork.getLeaderName() == null ? "" : signDispaWork.getLeaderName();
        dataMap.put("viceDirector", leanderName);
        String ministerName = signDispaWork.getMinisterName() == null ? "" : signDispaWork.getMinisterName();
        dataMap.put("minister", ministerName);
        String projectDirector = (signDispaWork.getmUserName() == null ? "" : signDispaWork.getmUserName())
                + (signDispaWork.getAUserName() == null ? "" : ","+signDispaWork.getAUserName());
        dataMap.put("proojectDirector", projectDirector);

        String expertName = "";
        for (Expert expert : expertList) {
            if (!StringUtil.isBlank(expertName) && expert.getName() != null) {
                expertName += ",";
            }
            List<ExpertType> expertTypeList = expert.getExpertType();
            expertName += expert.getName();
        }

        String reviewGroup = "主任," + leanderName + "," + ministerName + "," + projectDirector ;

        if(!"".equals(expertName) && expertName.length() >0){
            reviewGroup += "," + expertName;
        }

        dataMap.put("reviewGroup", reviewGroup);

        SysFile sysFile = TemplateUtil.createTemplate(f,signDispaWork.getSignid(), Constant.SysFileType.SIGN.getValue(),
                Constant.SysFileType.TEMOLATE.getValue(), Constant.STAGE_SUG,
                Constant.Template.BUDGET_ROSTER.getKey(), Constant.Template.SUG_ROSTER.getValue().split("_")[1],
                Constant.Template.WORD_SUFFIX.getKey(), dataMap);
        return sysFile;
    }


    /********************************资金申请报告**********************************/

    /**
     * 评审意见
     *
     * @param signDispaWork
     * @return
     */
    public static SysFile createReportTemplateOpinion(Ftp f,SignDispaWork signDispaWork) {
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("projectName", signDispaWork.getProjectname());
        dataMap.put("docNum", signDispaWork.getSignnum());
        dataMap.put("explain", "xxxxx");
        dataMap.put("title1", "xxxxx");
        dataMap.put("content1", "xxxxx");
        dataMap.put("title2", "xxxxx");
        dataMap.put("content2", "xxxxx");
        dataMap.put("dateStr", DateUtils.converToString(new Date(), "yyyy年MM月dd日"));

        SysFile sysFile = TemplateUtil.createTemplate(f,signDispaWork.getSignid(),  Constant.SysFileType.SIGN.getValue(),
                Constant.SysFileType.TEMOLATE.getValue(), Constant.APPLY_REPORT,
                Constant.Template.REPORT_OPINION.getKey(), Constant.Template.REPORT_OPINION.getValue().split("_")[1],
                Constant.Template.WORD_SUFFIX.getKey(), dataMap);
        return sysFile;
    }

    /**
     * 投资估算表
     *
     * @param signDispaWork
     * @return
     */
    public static SysFile createReportTemplateEstimate(Ftp f,SignDispaWork signDispaWork) {
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("gsnum", "2");
        dataMap.put("projectName", signDispaWork.getProjectname());
        SysFile sysFile = TemplateUtil.createTemplate(f,signDispaWork.getSignid(),  Constant.SysFileType.SIGN.getValue(),
                Constant.SysFileType.TEMOLATE.getValue(), Constant.APPLY_REPORT,
                Constant.Template.REPORT_ESTIMATE.getKey(), Constant.Template.REPORT_ESTIMATE.getValue().split("_")[1],
                Constant.Template.EXCEL_SUFFIX.getKey(), dataMap);
        return sysFile;
    }

    /**
     * 评审组名单
     *
     * @param signDispaWork
     * @return
     */
    public static SysFile createReportTemplateRoster(Ftp f,SignDispaWork signDispaWork,  List<Expert> expertList) {
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("psnum", "3");
        dataMap.put("projectName", signDispaWork.getProjectname());
        dataMap.put("generalCounsel", "市发改委分管副主任");
        dataMap.put("counselor", "市发改委主办处室处长");
        dataMap.put("director", "主任");
        String leanderName = signDispaWork.getLeaderName() == null ? "" : signDispaWork.getLeaderName();
        dataMap.put("viceDirector", leanderName);
        String ministeName = signDispaWork.getMinisterName() == null ? "" : signDispaWork.getMinisterName();
        dataMap.put("minister", ministeName);
        String projectDirector = (signDispaWork.getmUserName() == null ? "" : signDispaWork.getmUserName())
                + (signDispaWork.getAUserName() == null ? "" : ","+signDispaWork.getAUserName());
        dataMap.put("proojectDirector", projectDirector);

        String expertName = "";
        for (Expert expert : expertList) {
            if (!StringUtil.isBlank(expertName) && expert.getName() != null) {
                expertName += ",";
            }
            List<ExpertType> expertTypeList = expert.getExpertType();
            expertName += expert.getName();
        }

        String reviewGroup = "主任," + leanderName + "," + ministeName + "," + projectDirector ;

        if(!"".equals(expertName) && expertName.length() >0){
            reviewGroup += "," + expertName;
        }

        dataMap.put("reviewGroup", reviewGroup);

        SysFile sysFile = TemplateUtil.createTemplate(f,signDispaWork.getSignid(),  Constant.SysFileType.SIGN.getValue(),
                Constant.SysFileType.TEMOLATE.getValue(), Constant.APPLY_REPORT,
                Constant.Template.REPORT_ROSTER.getKey(), Constant.Template.REPORT_ROSTER.getValue().split("_")[1],
                Constant.Template.WORD_SUFFIX.getKey(), dataMap);
        return sysFile;
    }


    /********************end发文模板**************************************/


    /************************begin会前准备材料******************************************/

    /**
     * 生成签到表
     *
     * @param sign
     * @param workProgram
     * @return
     */
    public static SysFile createtTemplateSignIn(Ftp f,Sign sign, WorkProgram workProgram) {
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("projectName", sign.getProjectname());
        dataMap.put("dateStr", DateUtils.converToString(new Date(), "yyyy年MM月dd日"));
        dataMap.put("reviewStage", sign.getReviewstage());

        SysFile sysFile = TemplateUtil.createTemplate(f,sign.getSignid(),  Constant.SysFileType.SIGN.getValue(),
                Constant.SysFileType.MEETING.getValue(), Constant.SysFileType.WORKPROGRAM.getValue(),
                Constant.Template.SIGN_IN.getKey(), Constant.Template.SIGN_IN.getValue(),
                Constant.Template.WORD_SUFFIX.getKey(), dataMap);

        return sysFile;
    }


    /**
     * 会议通知
     *
     * @param sign
     * @param workProgram
     * @return
     */
    public static SysFile createTemplateNotice(Ftp f,Sign sign, WorkProgram workProgram, User user,List<RoomBooking> rbList) {

        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("projectName", sign.getProjectname());
        dataMap.put("reviewStage", sign.getReviewstage());
        dataMap.put("sendFileUnit", "深圳市人民检察院");
        dataMap.put("studyBeginTime", DateUtils.converToString(workProgram.getStudyBeginTime(), "yyyy年MM月dd日"));
        dataMap.put("contactPerson", user.getDisplayName());//联系人
        dataMap.put("contactPersonTel", user.getUserPhone());//联系电话
        dataMap.put("contactPersonFax", "83642081");//传真
        dataMap.put("contactPersonAddress", "深圳市政府投资项目评审中心");
        dataMap.put("dateStr", DateUtils.converToString(new Date(), "yyyy年MM月dd日"));
        //获得会议信息
        SysFile sysFile = null;
        if (rbList != null && rbList.size() > 0) {
            Date compareDate = DateUtils.converToDate("12:00", "HH:MM");
            for (RoomBooking roomBooking : rbList) {
                String[] str = roomBooking.getRbDate().split("-");
                String[] str2 = str[2].split("\\(");
                dataMap.put("rbDate", str[0] + "年" + str[1] + "月" + str2[0] + "日(" + str2[1]);
                dataMap.put("beginTime", DateUtils.converToString(roomBooking.getBeginTime(), "HH:mm"));
                dataMap.put("endTime", DateUtils.converToString(roomBooking.getEndTime(), "HH:mm"));
                dataMap.put("addressName", roomBooking.getAddressName()); //会议地点

                if (DateUtils.compareIgnoreSecond(roomBooking.getBeginTime(), compareDate) == 1 &&
                        DateUtils.compareIgnoreSecond(roomBooking.getEndTime(), compareDate) == -1) {
                    dataMap.put("lastTime", "全");//天数
                } else {
                    dataMap.put("lastTime", "半");//天数
                }
            }

            sysFile = TemplateUtil.createTemplate(f,sign.getSignid(), Constant.SysFileType.SIGN.getValue(),
                    Constant.SysFileType.MEETING.getValue(), Constant.SysFileType.WORKPROGRAM.getValue(),
                    Constant.Template.UNIT_NOTICE.getKey(), Constant.Template.UNIT_NOTICE.getValue(),
                    Constant.Template.WORD_SUFFIX.getKey(), dataMap);
        }
        return sysFile;
    }


    /**
     * 评审会议议程
     *
     * @param sign
     * @param workProgram
     * @return
     */
    public static List<SysFile> createTemplateMeeting(Ftp f,Sign sign, WorkProgram workProgram,List<RoomBooking> rbList) {
        List<SysFile> sysFileList = new ArrayList<>();
        SysFile sysFile = null;

        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("projectName", sign.getProjectname());
        dataMap.put("reviewStage", sign.getReviewstage());

        Date compareDate = DateUtils.converToDate("12:00", "HH:mm");
        if (!rbList.isEmpty()) {
            for (RoomBooking roomBooking : rbList) {

                dataMap.put("rbDate", roomBooking.getRbDate());//会议日期显示星期
                dataMap.put("beginTime", DateUtils.converToString(roomBooking.getBeginTime(), "HH:mm"));//会议开始时间
                dataMap.put("endTime", DateUtils.converToString(roomBooking.getEndTime(), "HH:mm"));//会议结束时间
                dataMap.put("meetingAddress", roomBooking.getAddressName());
                if (DateUtils.compareIgnoreSecond(roomBooking.getBeginTime(), compareDate) == 1) {
                    sysFile = TemplateUtil.createTemplate(f,sign.getSignid(),  Constant.SysFileType.SIGN.getValue(),
                            Constant.SysFileType.MEETING.getValue(), Constant.SysFileType.WORKPROGRAM.getValue(),
                            Constant.Template.MEETING_AM.getKey(), Constant.Template.MEETING_AM.getValue(),
                            Constant.Template.WORD_SUFFIX.getKey(), dataMap);
                    sysFileList.add(sysFile);

                } else {

                    sysFile = TemplateUtil.createTemplate(f,sign.getSignid(),  Constant.SysFileType.SIGN.getValue(),
                            Constant.SysFileType.MEETING.getValue(), Constant.SysFileType.WORKPROGRAM.getValue(),
                            Constant.Template.MEETING_PM.getKey(), Constant.Template.MEETING_PM.getValue(),
                            Constant.Template.WORD_SUFFIX.getKey(), dataMap);
                    sysFileList.add(sysFile);
                }
            }
        }
        return sysFileList;
    }

    /**
     * 邀请函
     *
     * @param sign
     * @param workProgram
     * @param expert
     * @return
     */
    public static SysFile createTemplateInvitation(Ftp f,Sign sign, WorkProgram workProgram, Expert expert, User user,List<RoomBooking> rbList) {
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("expertName", expert.getName());
        dataMap.put("projectName", sign.getProjectname());
        dataMap.put("studyBeginTime", DateUtils.converToString(workProgram.getStudyBeginTime(), "yyyy年MM月dd日"));
        dataMap.put("contactPerson", user.getDisplayName());//联系人
        dataMap.put("contactPersonTel", user.getUserPhone());//联系电话
        dataMap.put("contactPersonFax", "83642081");//传真
        dataMap.put("contactPersonAddress", "深圳市政府投资项目评审中心");
        dataMap.put("dateStr", DateUtils.converToString(new Date(), "yyyy年MM月dd日"));
        //获得会议信息
        SysFile sysFile = null;
        if (rbList != null && rbList.size() > 0) {
            Date compareDate = DateUtils.converToDate("12:00", "HH:mm");
            for (RoomBooking roomBooking : rbList) {
                String[] str = roomBooking.getRbDate().split("-");
                String[] str2 = str[2].split("\\(");
                dataMap.put("rbDate", str[0] + "年" + str[1] + "月" + str2[0] + "日(" + str2[1]);
//                dataMap.put("beginTime", DateUtils.converToString(roomBooking.getBeginTime(), "HH:mm"));
                dataMap.put("addressName", roomBooking.getAddressName()); //会议地点
//                dataMap.put("endTime", DateUtils.converToString(roomBooking.getEndTime(), "HH:mm"));

                if (DateUtils.compareIgnoreSecond(roomBooking.getBeginTime(), compareDate) == 1 &&
                        DateUtils.compareIgnoreSecond(roomBooking.getEndTime(), compareDate) == -1) {
                    dataMap.put("lastTime", "全");//天数
                } else {
                    dataMap.put("lastTime", "半");//天数
                }
            }
            sysFile = TemplateUtil.createTemplate(f,sign.getSignid(), Constant.SysFileType.SIGN.getValue(),
                    Constant.SysFileType.MEETING.getValue(), Constant.SysFileType.WORKPROGRAM.getValue(),
                    Constant.Template.INVITATION.getKey(), Constant.Template.INVITATION.getValue(),
                    Constant.Template.WORD_SUFFIX.getKey(), dataMap);
        }

        return sysFile;
    }


    /**
     * 主持人稿
     *
     * @param sign
     * @param workProgram
     * @param expertList
     * @return
     */
    public static SysFile createTemplateCompere(Ftp f,Sign sign, WorkProgram workProgram, List<Expert> expertList) {
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("projectName", sign.getProjectname());
        String expertName = "";
        String expertType = "";
        for (Expert expert : expertList) {
            if (!StringUtil.isBlank(expertName) && expert.getName() != null) {
                expertName += "、";
            }
            List<ExpertType> expertTypeList = expert.getExpertType();
            for (ExpertType et : expertTypeList) {
                if (!StringUtil.isBlank(expertType) && !StringUtil.isBlank(et.getExpertType()) && expertType.indexOf(et.getExpertType()) == -1) {
                    expertType += "、";
                }
                if (expertType.indexOf(et.getExpertType()) == -1) {

                    expertType += et.getExpertType();
                }
            }
            expertName += expert.getName();
        }
        dataMap.put("expertName", expertName);
        dataMap.put("expertType", expertType);
        dataMap.put("maindeptName", sign.getMaindeptName());//主办事处名称
        dataMap.put("assistdeptName", sign.getAssistdeptName());//协办事处名称
        dataMap.put("mainDeptName", sign.getMaindeptName());//主管部门名称
        dataMap.put("builtcompanyName", sign.getBuiltcompanyName());//建设单位
        dataMap.put("designcompanyName", sign.getDesigncompanyName());//编制单位

        String reviewGroupmembers = "";
        reviewGroupmembers += sign.getLeaderName() == null ? "" : sign.getLeaderName()
                + workProgram.getMinisterName() == null ? "" : "," + workProgram.getMinisterName()
                +workProgram.getMianChargeUserName() == null ? "" : "," + workProgram.getMianChargeUserName()
                +workProgram.getSecondChargeUserName() == null ? "" : "," + workProgram.getSecondChargeUserName();
        dataMap.put("reviewGroupmembers" , reviewGroupmembers);

//        dataMap.put("leaderName", sign.getLeaderName() == null ? "" : sign.getLeaderName());//中心领导名
//        dataMap.put("ministerName", workProgram.getMinisterName() == null ? "" : workProgram.getMinisterName());//部长名
//        dataMap.put("mianChargeUserName", workProgram.getMianChargeUserName() == null ? "" : workProgram.getMianChargeUserName()); //第一负责人
//        dataMap.put("secondChargeUserName", workProgram.getSecondChargeUserName() == null ? "" : workProgram.getSecondChargeUserName()); //第二负责人
        dataMap.put("projectBackGround", workProgram.getProjectBackGround());
        dataMap.put("buildSize", workProgram.getBuildSize());
        dataMap.put("buildContent", workProgram.getBuildContent());
        dataMap.put("appalyInvestment", workProgram.getAppalyInvestment());//申报金额
        dataMap.put("mainPoint", workProgram.getMainPoint());//拟评审重点问题

        SysFile sysFile = TemplateUtil.createTemplate(f,sign.getSignid(),  Constant.SysFileType.SIGN.getValue(),
                Constant.SysFileType.MEETING.getValue(), Constant.SysFileType.WORKPROGRAM.getValue(),
                Constant.Template.COMPERE.getKey(), Constant.Template.COMPERE.getValue(),
                Constant.Template.WORD_SUFFIX.getKey(), dataMap);
        return sysFile;
    }


    /**
     * 协审协议书
     *
     * @param sign
     * @param workProgram
     * @param apsList
     * @param assistUnit
     * @param org
     * @return
     */
    public static SysFile createTemplateAssist(Ftp f,Sign sign, WorkProgram workProgram, List<AssistPlanSign> apsList, AssistUnit assistUnit, Org org) {
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("workreviveStage", "");
        if (org != null) {
            dataMap.put("orgName", org.getName());
            dataMap.put("orgAddress", org.getOrgAddress());
            dataMap.put("orgMLeader", org.getOrgMLeader());
            dataMap.put("orgPhone", org.getOrgPhone());
            dataMap.put("orgFax", org.getOrgFax());
        }
        if (assistUnit != null) {
            dataMap.put("unitName", assistUnit.getUnitName());
            dataMap.put("address", assistUnit.getAddress());
            dataMap.put("phoneNum", assistUnit.getPhoneNum());
            dataMap.put("principalName", assistUnit.getPrincipalName());
            dataMap.put("contactFax", assistUnit.getContactFax());
        }
        dataMap.put("projectName", sign.getProjectname());
        if (!apsList.isEmpty()) {
            AssistPlanSign assistPlanSign = apsList.get(0);
            AssistPlan assistPlan = assistPlanSign.getAssistPlan();
            float assistDays = assistPlanSign.getAssistDays();
            Date reportTime = assistPlan.getReportTime();
            Date finishTime = DateUtils.addDay(reportTime, (int) assistDays);
            dataMap.put("finishTime", DateUtils.converToString(finishTime, "yyyy年MM月dd日")); //建设规模
            dataMap.put("estimateCost", assistPlanSign.getEstimateCost()); //建设规模
            dataMap.put("assistCost", assistPlanSign.getAssistCost()); //协审费用
        }
        dataMap.put("assistDeptUserName", sign.getAssistdeptName()); //协办事处联系人
        dataMap.put("mainDeptUserName", sign.getMainDeptUserName()); //主办事处联系人

        SysFile sysFile = TemplateUtil.createTemplate(f,sign.getSignid(),  Constant.SysFileType.SIGN.getValue(),
                Constant.SysFileType.MEETING.getValue(), Constant.SysFileType.WORKPROGRAM.getValue(),
                Constant.Template.ASSIST.getKey(), Constant.Template.ASSIST.getValue(),
                Constant.Template.WORD_SUFFIX.getKey(), dataMap);
        return sysFile;

    }

    /************************end会前准备材料******************************************/


    /***********************begin课题研究模板****************************************/

    /**
     * 课题研究—会议通知
     *
     * @param sign
     * @param workProgram
     * @return
     */
    public static SysFile subjectStudyNovice(Ftp f,Sign sign, WorkProgram workProgram, User user,List<RoomBooking> rbList) {
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("projectName", sign.getProjectname());
        dataMap.put("reviewStage", sign.getReviewstage());
        dataMap.put("sendFileUnit", "深圳市人民检察院");
        dataMap.put("studyBeginTime", DateUtils.converToString(workProgram.getStudyBeginTime(), "yyyy年MM月dd日"));
        dataMap.put("contactPerson", user.getDisplayName());//联系人
        dataMap.put("contactPersonTel", user.getUserPhone());//联系电话
        dataMap.put("contactPersonFax", "83642081");//传真
        dataMap.put("contactPersonAddress", "深圳市政府投资项目评审中心");
        dataMap.put("dateStr", DateUtils.converToString(new Date(), "yyyy年MM月dd日"));
        //获得会议信息
        SysFile sysFile = null;
        if (rbList != null && rbList.size() > 0) {
            Date compareDate = DateUtils.converToDate("12:00", "HH:MM");
            for (RoomBooking roomBooking : rbList) {
                String[] str = roomBooking.getRbDate().split("-");
                String[] str2 = str[2].split("\\(");
                dataMap.put("rbDate", str[0] + "年" + str[1] + "月" + str2[0] + "日(" + str2[1]);
                dataMap.put("beginTime", DateUtils.converToString(roomBooking.getBeginTime(), "HH:mm"));
                dataMap.put("addressName", roomBooking.getAddressName()); //会议地点
                dataMap.put("endTime", DateUtils.converToString(roomBooking.getEndTime(), "HH:mm"));

                if (DateUtils.compareIgnoreSecond(roomBooking.getBeginTime(), compareDate) == 1 &&
                        DateUtils.compareIgnoreSecond(roomBooking.getEndTime(), compareDate) == -1) {
                    dataMap.put("lastTime", "全");//天数
                } else {
                    dataMap.put("lastTime", "半");//天数
                }
            }
            sysFile = TemplateUtil.createTemplate(f,sign.getSignid(),  Constant.SysFileType.SIGN.getValue(),
                    Constant.SysFileType.SUBJECT_STUDY.getValue(), Constant.SysFileType.SUBJECT_STUDY.getValue(),
                    Constant.Template.SUBJECT_STUDY_NOVICE.getKey(), Constant.Template.SUBJECT_STUDY_NOVICE.getValue(),
                    Constant.Template.WORD_SUFFIX.getKey(), dataMap);
        }
        return sysFile;

    }


    /**
     * 课题研究-签到表
     *
     * @param sign
     * @param workProgram
     * @return
     */
    public static SysFile subjectStudySignIn(Ftp f,Sign sign, WorkProgram workProgram) {
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("dateStr", DateUtils.converToString(new Date(), "yyyy年MM月dd日"));
        SysFile sysFile = TemplateUtil.createTemplate(f,sign.getSignid(),  Constant.SysFileType.SIGN.getValue(),
                Constant.SysFileType.SUBJECT_STUDY.getValue(), Constant.SysFileType.SUBJECT_STUDY.getValue(),
                Constant.Template.SUBJECT_STUDY_SIGNIN.getKey(), Constant.Template.SUBJECT_STUDY_SIGNIN.getValue(),
                Constant.Template.WORD_SUFFIX.getKey(), dataMap);
        return sysFile;
    }


    /**
     * 课题研究-专家签名表
     *
     * @param sign
     * @param workProgram
     * @return
     */
    public static SysFile subjectStudySignature(Ftp f,Sign sign, WorkProgram workProgram) {
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("projectName", sign.getProjectname());
        SysFile sysFile = TemplateUtil.createTemplate(f,sign.getSignid(),  Constant.SysFileType.SIGN.getValue(),
                Constant.SysFileType.SUBJECT_STUDY.getValue(), Constant.SysFileType.SUBJECT_STUDY.getValue(),
                Constant.Template.SUBJECT_STUDY_EXPERTAIGNATURE.getKey(), Constant.Template.SUBJECT_STUDY_EXPERTAIGNATURE.getValue(),
                Constant.Template.WORD_SUFFIX.getKey(), dataMap);
        return sysFile;

    }


    /**
     * 课题研究-邀请函
     *
     * @param sign
     * @param workProgram
     * @param expert
     * @param user
     * @return
     */
    public static SysFile subjectStudyInvitation(Ftp f,Sign sign, WorkProgram workProgram, Expert expert, User user,List<RoomBooking> rbList) {
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("expertName", expert.getName());
        dataMap.put("projectName", sign.getProjectname());
        dataMap.put("studyBeginTime", DateUtils.converToString(workProgram.getStudyBeginTime(), "yyyy年MM月dd日"));
        dataMap.put("contactPerson", user.getDisplayName());//联系人
        dataMap.put("contactPersonTel", user.getUserPhone());//联系电话
        dataMap.put("contactPersonFax", "83642081");//传真
        dataMap.put("contactPersonAddress", "深圳市政府投资项目评审中心");
        dataMap.put("dateStr", DateUtils.converToString(new Date(), "yyyy年MM月dd日"));
        //获得会议信息
        SysFile sysFile = null;
        if (rbList != null && rbList.size() > 0) {
            Date compareDate = DateUtils.converToDate("12:00", "HH:mm");
            for (RoomBooking roomBooking : rbList) {
                String[] str = roomBooking.getRbDate().split("-");
                String[] str2 = str[2].split("\\(");
                dataMap.put("rbDate", str[0] + "年" + str[1] + "月" + str2[0] + "日(" + str2[1]);
                dataMap.put("addressName", roomBooking.getAddressName()); //会议地点

                if (DateUtils.compareIgnoreSecond(roomBooking.getBeginTime(), compareDate) == 1 &&
                        DateUtils.compareIgnoreSecond(roomBooking.getEndTime(), compareDate) == -1) {
                    dataMap.put("lastTime", "全");//天数
                } else {
                    dataMap.put("lastTime", "半");//天数
                }
            }
            sysFile = TemplateUtil.createTemplate(f,sign.getSignid(),  Constant.SysFileType.SIGN.getValue(),
                    Constant.SysFileType.SUBJECT_STUDY.getValue(), Constant.SysFileType.SUBJECT_STUDY.getValue(),
                    Constant.Template.SUBJECT_STUDY_INVITATION.getKey(), Constant.Template.SUBJECT_STUDY_INVITATION.getValue(),
                    Constant.Template.WORD_SUFFIX.getKey(), dataMap);
        }
        return sysFile;
    }


    /**
     * 课题研究-会议议程
     *
     * @param sign
     * @param workProgram
     * @return
     */
    public static List<SysFile> subjectStudyMeeting(Ftp f,Sign sign, WorkProgram workProgram,List<RoomBooking> rbList) {
        List<SysFile> sysFileList = new ArrayList<>();
        SysFile sysFile = null;
        Map<String, Object> dataMap = new HashMap<>();
        Date compareDate = DateUtils.converToDate("12:00", "HH:mm");
        if (!rbList.isEmpty()) {
            for (RoomBooking roomBooking : rbList) {
                dataMap.put("rbDate", roomBooking.getRbDate());//会议日期显示星期
                dataMap.put("beginTime", DateUtils.converToString(roomBooking.getBeginTime(), "HH:mm"));//会议开始时间
                dataMap.put("endTime", DateUtils.converToString(roomBooking.getEndTime(), "HH:mm"));//会议结束时间
                dataMap.put("addressName", roomBooking.getAddressName());
                if (DateUtils.compareIgnoreSecond(roomBooking.getBeginTime(), compareDate) == 1) {
                    sysFile = TemplateUtil.createTemplate(f,sign.getSignid(),  Constant.SysFileType.SIGN.getValue(),
                            Constant.SysFileType.SUBJECT_STUDY.getValue(), Constant.SysFileType.SUBJECT_STUDY.getValue(),
                            Constant.Template.SUBJECT_STUDY_MEETINGAM.getKey(), Constant.Template.SUBJECT_STUDY_MEETINGAM.getValue(),
                            Constant.Template.WORD_SUFFIX.getKey(), dataMap);
                    sysFileList.add(sysFile);

                } else {

                    sysFile = TemplateUtil.createTemplate(f,sign.getSignid(),  Constant.SysFileType.SIGN.getValue(),
                            Constant.SysFileType.SUBJECT_STUDY.getValue(), Constant.SysFileType.SUBJECT_STUDY.getValue(),
                            Constant.Template.SUBJECT_STUDY_MEETINGPM.getKey(), Constant.Template.SUBJECT_STUDY_MEETINGPM.getValue(),
                            Constant.Template.WORD_SUFFIX.getKey(), dataMap);
                    sysFileList.add(sysFile);
                }
            }
        }
        return sysFileList;
    }


    /***********************end课题研究模板****************************************/


    /**
     * 生成月报简报模板
     * @param monthlyNewsletterDto
     * @param proReviewConditionDtoList
     * @return
     */
    public static File createMonthTemplate(MonthlyNewsletterDto monthlyNewsletterDto,Integer signCount,Integer reviewCount,List<ProReviewConditionDto> proReviewConditionDtoList,List<ProReviewConditionDto> proReviewConditionDtoAllList,List<ProReviewConditionDto> proReviewConditionByTypeList,Integer totalNum,ProReviewConditionDto proReviewConditionCur,ProReviewConditionDto proReviewConditionSum,Map<String,List<ProReviewConditionDto> > proReviewCondDetailMap,Integer[] proCountArr) {
        Map<String, Object> dataMap = new HashMap<>();
        //报告年度
        dataMap.put("reportMultiyear", monthlyNewsletterDto.getReportMultiyear());
        //报告月份
        dataMap.put("theMonths", monthlyNewsletterDto.getTheMonths());
        if(Integer.parseInt(monthlyNewsletterDto.getTheMonths())<10){
            dataMap.put("theNthMonths",NumUtils.NumberToChn(Integer.parseInt(monthlyNewsletterDto.getTheMonths())));
        }else{
          String temp = NumUtils.NumberToChn(Integer.parseInt(monthlyNewsletterDto.getTheMonths()));
          temp = temp.substring(1,temp.length());
            dataMap.put("theNthMonths",temp);
        }
        //todo:初始化参数
        dataMap.put("signTotal", signCount!=null?signCount:0);
        dataMap.put("proTotal", proReviewConditionCur.getProCount()!=null?proReviewConditionCur.getProCount():0);
        dataMap.put("declareTotal", proReviewConditionCur.getDeclareValue()!=null?proReviewConditionCur.getDeclareValue()+"":0);
        dataMap.put("authorizeTotal",proReviewConditionCur.getAuthorizeValue()!=null?proReviewConditionCur.getAuthorizeValue()+"":0);
        dataMap.put("ljhjTotal", proReviewConditionCur.getLjhj()!=null?proReviewConditionCur.getLjhj()+"":0);
        dataMap.put("hjlTotal", proReviewConditionCur.getHjl()!=null?proReviewConditionCur.getHjl()+"%":0+"%");

        dataMap.put("proAllTotal", proReviewConditionSum.getProCount()!=null?proReviewConditionSum.getProCount():0);
        dataMap.put("declareAllTotal", proReviewConditionSum.getDeclareValue()!=null?proReviewConditionSum.getDeclareValue()+"":0);
        dataMap.put("authorizeAllTotal", proReviewConditionSum.getAuthorizeValue()!=null?proReviewConditionSum.getAuthorizeValue()+"":0);
        dataMap.put("ljhjAllTotal",proReviewConditionSum.getLjhj()!=null?proReviewConditionSum.getLjhj()+"":0);
        dataMap.put("hjlAllTotal", proReviewConditionSum.getHjl()!=null?proReviewConditionSum.getHjl()+"%":0+"%");
        dataMap.put("reviewCount",reviewCount!=null?reviewCount:0);//评审会次数
        List<ProReviewConditionDto> proReviewCondition = proReviewConditionDtoList;
        List<ProReviewConditionDto> proReviewConditionAll = proReviewConditionDtoAllList;
        dataMap.put("proReviewConditionList",proReviewCondition);
        dataMap.put("proReviewConditionAllList",proReviewConditionAll);
        dataMap.put("proReviewCondDetailMap",proReviewCondDetailMap);
        String curYear =  DateUtils.converToString(new Date(),null).split("-")[0];
        String curMonth =  DateUtils.converToString(new Date(),null).split("-")[1];
        String dayStr =  DateUtils.converToString(new Date(),null).split("-")[2];
        dataMap.put("curDay",dayStr);
        dataMap.put("curMonth",curMonth);
        dataMap.put("beginMonth",monthlyNewsletterDto.getStaerTheMonths());
        dataMap.put("curYear",curYear);
        String[] reviewStage = {"xmjys-项目建议书","kxxyj-可行性研究报告","xmgs-项目概算","zjsq-资金申请报告","qt-其它","jksb-进口设备","sbqdgc-设备清单(国产)","sbqdjk-设备清单(进口)"};
        String[] reviewStageTemp = {"xmjys-项 目 建 议 书 ","kxxyj-可 行 性 研 究 报 告 ","xmgs-项 目 概 算 ","zjsq-资 金 申 请 报 告 ","qt-其 它 ","jksb-进 口 设 备 ","sbqdgc-设 备 清 单 ( 国 产 ) ","sbqdjk-设 备 清 单 ( 进 口 ) "};
        String[] reviewStageTotal = {"xmjysTotal-项目建议书","kxxyjTotal-可行性研究报告","xmgsTotal-项目概算","zjsqTotal-资金申请报告","qtTotal-其它","jksbTotal-进口设备","sbqdgcTotal-设备清单(国产)","sbqdjkTotal-设备清单(进口)"};
        String[] reviewStageTotalTemp = {"xmjysTotal-项 目 建 议 书 ","kxxyjTotal-可 行 性 研 究 报 告 ","xmgsTotal-项 目 概 算 ","zjsqTotal-资 金 申 请 报 告 ","qtTotal-其 它 ","jksbTotal-进 口 设 备 ","sbqdgcTotal-设 备 清 单 ( 国 产 ) ","sbqdjkTotal-设 备 清 单 ( 进 口 ) "};
        String[] projectType = {"projectTypeA-市政工程","projectTypeHouse-房建工程","projectTypeInfo-信息工程","projectTypeBuy-设备采购","projectTypeOther-其他"};
        String[] projectTypeTemp = {"projectTypeA-市 政 工 程 ","projectTypeHouse-房 建 工 程 ","projectTypeInfo-信 息 工 程 ","projectTypeBuy-设 备 采 购 ","projectTypeOther-其 他 "};
        boolean flag = true;
        //当月月报
        if (proReviewConditionDtoList.size()>0){
            for(int j=0;j<proReviewConditionDtoList.size();j++){
              for(int i=0;i<reviewStage.length;i++){
                flag = true;
                String [] tempArr = reviewStage[i].split("-");
                String [] tempArrTemp = reviewStageTemp[i].split("-");
                    if(tempArr[1].contains(proReviewConditionDtoList.get(j).getReviewStage()) || proReviewConditionDtoList.get(j).getReviewStage().contains("提前介入")){
                        flag = false;
                        if(!proReviewConditionDtoList.get(j).getIsadvanced().equals("9")){
                            dataMap.put(tempArrTemp[0], " 完 成 "+tempArrTemp[1]+" 评 审 "+proReviewConditionDtoList.get(j).getProCount()+" 项 ， 申 报 总 投 资 "+proReviewConditionDtoList.get(j).getDeclareValue()
                                    +" 亿 元 ， 审 核 后 总 投 资 "+proReviewConditionDtoList.get(j).getAuthorizeValue()+" 亿 元 ， 累 计 净 核 减 投 资 "+proReviewConditionDtoList.get(j).getLjhj()+" 亿 元 ,核 减 率 "+proReviewConditionDtoList.get(j).getHjl()+" % ");
                        }else{
                            dataMap.put(tempArrTemp[0], " 完 成 "+tempArrTemp[1]+" （ 提 前 介 入 ）评 审 "+proReviewConditionDtoList.get(j).getProCount()+" 项 ， 申 报 总 投 资 "+proReviewConditionDtoList.get(j).getDeclareValue()
                                    +" 亿 元 ， 审 核 后 总 投 资 "+proReviewConditionDtoList.get(j).getAuthorizeValue()+" 亿 元 ， 累 计 净 核 减 投 资 "+proReviewConditionDtoList.get(j).getLjhj()+" 亿 元 , 核 减 率 "+proReviewConditionDtoList.get(j).getHjl()+" % ");
                        }

                        break;
                    }
                  if((j+1) == proReviewConditionDtoList.size()){
                      if(flag){
                          dataMap.put(tempArr[0],"");
                      }
                  }
                }

            }
        }
        //截止至当前月月报
        int reviewTotal = 0;
        String proCent = "";
        BigDecimal advancedPoCount = BigDecimal.ZERO;//提前介入项目数
        BigDecimal advanceDecVal = BigDecimal.ZERO;//提前介入申报总额
        BigDecimal advanceAuthorizeVal = BigDecimal.ZERO;//提前介入审定总额
        BigDecimal advanceljhjTotalVal = BigDecimal.ZERO;//提前介入累计净核减投资
        boolean isAdvanced = false; //是否提前介入
        if (proReviewConditionDtoAllList.size()>0){
            for(int k=0;k<proReviewConditionDtoAllList.size();k++){
                if("9".equals(proReviewConditionDtoAllList.get(k).getIsadvanced())){  //提前介入
                    isAdvanced = true;
                    if(reviewTotal==0){
                        reviewTotal = proReviewConditionSum.getProCount().intValue() -  proReviewConditionDtoAllList.get(k).getProCount().intValue();
                    }else{
                        reviewTotal -= proReviewConditionDtoAllList.get(k).getProCount().intValue();
                    }
                    break;
                }
                if((k+1)==proReviewConditionDtoAllList.size()){
                    if(!isAdvanced){
                        reviewTotal = proReviewConditionSum.getProCount().intValue();
                    }
                }
            }

                for(int j=0;j<proReviewConditionDtoAllList.size();j++){
                    flag = true;
                    for(int i=0;i<reviewStageTotal.length;i++){
                        String [] tempArr = reviewStageTotal[i].split("-");
                        String [] tempArrTemp = reviewStageTotalTemp[i].split("-");
                    if(tempArr[1].contains(proReviewConditionDtoAllList.get(j).getReviewStage()) || proReviewConditionDtoAllList.get(j).getReviewStage().contains("提前介入")){
                        flag = false;
                        if(!proReviewConditionDtoAllList.get(j).getIsadvanced().equals("9")){

                            if(reviewTotal !=0 ){
                                proCent = String.format("%.2f",(proReviewConditionDtoAllList.get(j).getProCount().floatValue()/(float) reviewTotal)*100)+"%";
                            }

                            dataMap.put(tempArrTemp[0], " 完 成 "+tempArrTemp[1]+" 评 审 "+proReviewConditionDtoAllList.get(j).getProCount()+" 项，占 评 审 项 目 数 的 "+proCent+" 申 报 总 投 资 "+proReviewConditionDtoAllList.get(j).getDeclareValue()
                                    +" 亿 元 ，审 核 后 总 投 资 "+proReviewConditionDtoAllList.get(j).getAuthorizeValue()+" 亿 元， 累 计 净 核 减 投 资 "+proReviewConditionDtoAllList.get(j).getLjhj()+" 亿 元 ,核 减 率 "+proReviewConditionDtoAllList.get(j).getHjl()+" %");
                            break;
                        }else{//提前介入
                            advancedPoCount = advancedPoCount.add(proReviewConditionDtoAllList.get(j).getProCount());
                            advanceDecVal = advanceDecVal.add(BigDecimal.valueOf(proReviewConditionDtoAllList.get(j).getDeclareValue().doubleValue()));
                            advanceAuthorizeVal = advanceAuthorizeVal.add(BigDecimal.valueOf(proReviewConditionDtoAllList.get(j).getAuthorizeValue().doubleValue()));
                            advanceljhjTotalVal = advanceljhjTotalVal.add(BigDecimal.valueOf(proReviewConditionDtoAllList.get(j).getLjhj().doubleValue()));
                            break;
                        }
                    }
                        if((j+1) == proReviewConditionDtoAllList.size()){
                            if(flag){
                                dataMap.put(tempArr[0],"");
                            }
                        }
                }
            }
            if (isAdvanced){//提前介入
                BigDecimal diffNum = new BigDecimal (advanceDecVal.subtract(advanceAuthorizeVal).toString());
                double temp = diffNum.divide(advanceDecVal,3,BigDecimal.ROUND_HALF_UP).doubleValue()*100;
                dataMap.put("tqjrTotal", "另 ， 完 成 提 前 介 入 项 目 评 审 "+advancedPoCount+" 项，申 报 总 投 资 "+advanceDecVal
                        +" 亿 元 ， 审 核 后 总 投 资 "+advanceAuthorizeVal+" 亿 元 ， 累 计 净 核 减 投 资 "+advanceljhjTotalVal+" 亿 元 ,核 减 率 "+temp+"%");
            }
        }
        //项目类别
        String projectTypeItem = monthlyNewsletterDto.getStaerTheMonths()+"至"+monthlyNewsletterDto.getTheMonths()+" 月 评 审 的 项 目 中,";
        if(proReviewConditionByTypeList.size()>0){
            for(int i=0;i<proReviewConditionByTypeList.size();i++){
              for(int j=0;j<projectType.length;j++){
                    String [] tempArr = projectType[j].split("-");
                    String [] tempArrTemp = projectTypeTemp[j].split("-");
                    if(tempArr[1].equals(proReviewConditionByTypeList.get(i).getProjectType())){
                        proCent = String.format("%.2f",(proReviewConditionByTypeList.get(i).getProjectTypeCount().floatValue()/totalNum.floatValue()*100))+"%";

                        if(i!=(proReviewConditionByTypeList.size()-1)){
                            projectTypeItem += tempArrTemp[1]+"类 项 目 "+proReviewConditionByTypeList.get(i).getProjectTypeCount()+"项，占 项 目 总 数 的 "+proCent+";";
                        }else{
                            projectTypeItem += tempArrTemp[1]+"类 项 目 "+proReviewConditionByTypeList.get(i).getProjectTypeCount()+"项 ，占 项 目 总 数 的 "+proCent+"。";
                        }
                        break;
                    }
                }
            }
        }
        dataMap.put("projectTypeItem",projectTypeItem);
        //投资金额
        for(int i=0;i<proCountArr.length-1;i++){
            dataMap.put("proCount"+(i+1),proCountArr[i]);
            if(proCountArr[i]!=0){
                double temp = (double)proCountArr[i]/(double)proCountArr[proCountArr.length-1]*100;
                String result = String.format("%.2f",temp)+"%";
                dataMap.put("proCountCent"+(i+1),result);
            }else{
                dataMap.put("proCountCent"+(i+1),0+"%");
            }
        }
        String  showName = Constant.Template.MONTH_REPORT.getValue() +  Constant.Template.WORD_SUFFIX.getKey();
        String path = SysFileUtil.getUploadPath();
        String relativeFileUrl = SysFileUtil.generatRelativeUrl(path ,  Constant.Template.MONTH_REPORT.getValue() ,null , null , showName);

        File docFile = TemplateUtil.createDoc(dataMap , Constant.Template.MONTH_REPORT.getKey() , path + File.separator + relativeFileUrl);
        return docFile;
    }
}