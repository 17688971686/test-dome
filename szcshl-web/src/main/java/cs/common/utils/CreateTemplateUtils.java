package cs.common.utils;

import cs.common.Constant;
import cs.domain.expert.Expert;
import cs.domain.expert.ExpertType;
import cs.domain.meeting.RoomBooking;
import cs.domain.project.*;
import cs.domain.sys.Org;
import cs.domain.sys.SysFile;
import cs.domain.sys.User;
import cs.model.expert.ProReviewConditionDto;
import cs.model.monthly.MonthlyNewsletterDto;

import java.io.File;
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
    public static SysFile createStudyTemplateOpinion(SignDispaWork signDispaWork) {
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("projectName", signDispaWork.getProjectname());
        dataMap.put("docNum", signDispaWork.getSignnum());
        dataMap.put("explain", "xxxxx");
        dataMap.put("title1", "xxxxx");
        dataMap.put("content1", "xxxxx");
        dataMap.put("title2", "xxxxx");
        dataMap.put("content2", "xxxxx");
        dataMap.put("dateStr", DateUtils.converToString(new Date(), "yyyy年MM月dd日"));

        SysFile sysFile = TemplateUtil.createTemplate(signDispaWork.getSignid(),  Constant.SysFileType.SIGN.getValue(),
                Constant.SysFileType.TEMOLATE.getValue(), Constant.ProjectStage.STAGE_STUDY.getValue(),
                Constant.Template.STUDY_OPINION.getKey(), Constant.Template.STUDY_OPINION.getValue().split("_")[1],
                Constant.Template.OUTPUT_SUFFIX.getKey(), dataMap);

        return sysFile;
    }


    /**
     * 评审组名单
     *
     * @param signDispaWork
     * @return
     */
    public static SysFile createStudyTemplateRoster(SignDispaWork signDispaWork, List<Expert> expertList) {
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


        SysFile sysFile = TemplateUtil.createTemplate(signDispaWork.getSignid(),  Constant.SysFileType.SIGN.getValue(),
                Constant.SysFileType.TEMOLATE.getValue(), Constant.ProjectStage.STAGE_STUDY.getValue(),
                Constant.Template.STUDY_ROSTER.getKey(), Constant.Template.STUDY_ROSTER.getValue().split("_")[1],
                Constant.Template.OUTPUT_SUFFIX.getKey(), dataMap);

        return sysFile;
    }


    /**
     * 投资估算表
     *
     * @param signDispaWork
     * @return
     */
    public static SysFile createStudyTemplateEstimate(SignDispaWork signDispaWork) {
        Map<String, Object> dataMap = new HashMap<>();

        dataMap.put("gsnum", "2");
        dataMap.put("projectName", signDispaWork.getProjectname());

        SysFile sysFile = TemplateUtil.createTemplate(signDispaWork.getSignid(),  Constant.SysFileType.SIGN.getValue(),
                Constant.SysFileType.TEMOLATE.getValue(), Constant.ProjectStage.STAGE_STUDY.getValue(),
                Constant.Template.STUDY_ESTIMATE.getKey(), Constant.Template.STUDY_ESTIMATE.getValue().split("_")[1],
                Constant.Template.OUTPUT_SUFFIX_XLS.getKey(), dataMap);
        return sysFile;
    }


    /***********************dubget项目概算模板区*******************/

    /**
     * 审核意见
     *
     * @param signDispaWork
     * @return
     */
    public static SysFile createBudgetTemplateOpinion(SignDispaWork signDispaWork) {

        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("projectName", signDispaWork.getProjectname());
        dataMap.put("docNum", signDispaWork.getSignnum());
        dataMap.put("explain", "xxxxx");
        dataMap.put("title1", "xxxxx");
        dataMap.put("content1", "xxxxx");
        dataMap.put("title2", "xxxxx");
        dataMap.put("content2", "xxxxx");
        dataMap.put("dateStr", DateUtils.converToString(new Date(), "yyyy年MM月dd日"));

        SysFile sysFile = TemplateUtil.createTemplate(signDispaWork.getSignid(),  Constant.SysFileType.SIGN.getValue(),
                Constant.SysFileType.TEMOLATE.getValue(), Constant.ProjectStage.STAGE_BUDGET.getValue(),
                Constant.Template.BUDGET_OPINION.getKey(), Constant.Template.BUDGET_OPINION.getValue().split("_")[1],
                Constant.Template.OUTPUT_SUFFIX.getKey(), dataMap);
        return sysFile;
    }


    /**
     * 建安工程费用
     *
     * @param signDispaWork
     * @return
     */
    public static SysFile createBudgetTemplateProjectCost(SignDispaWork signDispaWork) {
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("jagcfynum", "4");
        dataMap.put("projectName", signDispaWork.getProjectname());

        SysFile sysFile = TemplateUtil.createTemplate(signDispaWork.getSignid(),  Constant.SysFileType.SIGN.getValue(),
                Constant.SysFileType.TEMOLATE.getValue(), Constant.ProjectStage.STAGE_BUDGET.getValue(),
                Constant.Template.BUDGET_PROJECTCOST.getKey(), Constant.Template.BUDGET_PROJECTCOST.getValue().split("_")[1],
                Constant.Template.OUTPUT_SUFFIX.getKey(), dataMap);
        return sysFile;
    }

    /**
     * 概算汇总表和审核对照表
     *
     * @param signDispaWork
     * @return
     */
    public static SysFile createBudgetTemplateEstimate(SignDispaWork signDispaWork) {
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("gsnum1", "1");
        dataMap.put("projectName", signDispaWork.getProjectname());
        dataMap.put("gsnum2", "2");

        SysFile sysFile = TemplateUtil.createTemplate(signDispaWork.getSignid(),  Constant.SysFileType.SIGN.getValue(),
                Constant.SysFileType.TEMOLATE.getValue(), Constant.ProjectStage.STAGE_BUDGET.getValue(),
                Constant.Template.BUDGET_ESTIMATE.getKey(), Constant.Template.BUDGET_ESTIMATE.getValue().split("_")[1],
                Constant.Template.OUTPUT_SUFFIX_XLS.getKey(), dataMap);
        return sysFile;
    }

    /**
     * 审核组名单
     *
     * @param signDispaWork
     * @return
     */
    public static SysFile createBudgetTemplateRoster(SignDispaWork signDispaWork,  List<Expert> expertList) {
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

        SysFile sysFile = TemplateUtil.createTemplate(signDispaWork.getSignid(),  Constant.SysFileType.SIGN.getValue(),
                Constant.SysFileType.TEMOLATE.getValue(), Constant.ProjectStage.STAGE_BUDGET.getValue(),
                Constant.Template.BUDGET_ROSTER.getKey(), Constant.Template.BUDGET_ROSTER.getValue().split("_")[1],
                Constant.Template.OUTPUT_SUFFIX.getKey(), dataMap);
        return sysFile;
    }


    /***************************sug项目建议书****************************/

    /**
     * 评审意见
     *
     * @param signDispaWork
     * @return
     */
    public static SysFile createSugTemplateOpinion(SignDispaWork signDispaWork) {
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

        SysFile sysFile = TemplateUtil.createTemplate(signDispaWork.getSignid(),  Constant.SysFileType.SIGN.getValue(),
                Constant.SysFileType.TEMOLATE.getValue(), Constant.ProjectStage.STAGE_SUG.getValue(),
                Constant.Template.SUG_OPINION.getKey(), Constant.Template.SUG_OPINION.getValue().split("_")[1],
                Constant.Template.OUTPUT_SUFFIX.getKey(), dataMap);
        return sysFile;
    }

    /**
     * 投资匡算表
     *
     * @param signDispaWork
     * @return
     */
    public static SysFile createSugTemplateEstime(SignDispaWork signDispaWork) {

        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("ksnum", "2");
        dataMap.put("projectName", signDispaWork.getProjectname());
        SysFile sysFile = TemplateUtil.createTemplate(signDispaWork.getSignid(),  Constant.SysFileType.SIGN.getValue(),
                Constant.SysFileType.TEMOLATE.getValue(), Constant.ProjectStage.STAGE_SUG.getValue(),
                Constant.Template.SUG_ESTIMATE.getKey(), Constant.Template.SUG_ESTIMATE.getValue().split("_")[1],
                Constant.Template.OUTPUT_SUFFIX_XLS.getKey(), dataMap);
        return sysFile;
    }

    /**
     * 评审组名单
     *
     * @param signDispaWork
     * @return
     */
    public static SysFile createSugTemplateRoster(SignDispaWork signDispaWork,  List<Expert> expertList) {
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

        SysFile sysFile = TemplateUtil.createTemplate(signDispaWork.getSignid(), Constant.SysFileType.SIGN.getValue(),
                Constant.SysFileType.TEMOLATE.getValue(), Constant.ProjectStage.STAGE_SUG.getValue(),
                Constant.Template.BUDGET_ROSTER.getKey(), Constant.Template.SUG_ROSTER.getValue().split("_")[1],
                Constant.Template.OUTPUT_SUFFIX.getKey(), dataMap);
        return sysFile;
    }


    /********************************资金申请报告**********************************/

    /**
     * 评审意见
     *
     * @param signDispaWork
     * @return
     */
    public static SysFile createReportTemplateOpinion(SignDispaWork signDispaWork) {
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("projectName", signDispaWork.getProjectname());
        dataMap.put("docNum", signDispaWork.getSignnum());
        dataMap.put("explain", "xxxxx");
        dataMap.put("title1", "xxxxx");
        dataMap.put("content1", "xxxxx");
        dataMap.put("title2", "xxxxx");
        dataMap.put("content2", "xxxxx");
        dataMap.put("dateStr", DateUtils.converToString(new Date(), "yyyy年MM月dd日"));

        SysFile sysFile = TemplateUtil.createTemplate(signDispaWork.getSignid(),  Constant.SysFileType.SIGN.getValue(),
                Constant.SysFileType.TEMOLATE.getValue(), Constant.ProjectStage.APPLY_REPORT.getValue(),
                Constant.Template.REPORT_OPINION.getKey(), Constant.Template.REPORT_OPINION.getValue().split("_")[1],
                Constant.Template.OUTPUT_SUFFIX.getKey(), dataMap);
        return sysFile;
    }

    /**
     * 投资估算表
     *
     * @param signDispaWork
     * @return
     */
    public static SysFile createReportTemplateEstimate(SignDispaWork signDispaWork) {
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("gsnum", "2");
        dataMap.put("projectName", signDispaWork.getProjectname());
        SysFile sysFile = TemplateUtil.createTemplate(signDispaWork.getSignid(),  Constant.SysFileType.SIGN.getValue(),
                Constant.SysFileType.TEMOLATE.getValue(), Constant.ProjectStage.APPLY_REPORT.getValue(),
                Constant.Template.REPORT_ESTIMATE.getKey(), Constant.Template.REPORT_ESTIMATE.getValue().split("_")[1],
                Constant.Template.OUTPUT_SUFFIX_XLS.getKey(), dataMap);
        return sysFile;
    }

    /**
     * 评审组名单
     *
     * @param signDispaWork
     * @return
     */
    public static SysFile createReportTemplateRoster(SignDispaWork signDispaWork,  List<Expert> expertList) {
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

        SysFile sysFile = TemplateUtil.createTemplate(signDispaWork.getSignid(),  Constant.SysFileType.SIGN.getValue(),
                Constant.SysFileType.TEMOLATE.getValue(), Constant.ProjectStage.APPLY_REPORT.getValue(),
                Constant.Template.REPORT_ROSTER.getKey(), Constant.Template.REPORT_ROSTER.getValue().split("_")[1],
                Constant.Template.OUTPUT_SUFFIX.getKey(), dataMap);
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
    public static SysFile createtTemplateSignIn(Sign sign, WorkProgram workProgram) {
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("projectName", sign.getProjectname());
        dataMap.put("dateStr", DateUtils.converToString(new Date(), "yyyy年MM月dd日"));
        dataMap.put("reviewStage", sign.getReviewstage());

        SysFile sysFile = TemplateUtil.createTemplate(sign.getSignid(),  Constant.SysFileType.SIGN.getValue(),
                Constant.SysFileType.MEETING.getValue(), Constant.SysFileType.WORKPROGRAM.getValue(),
                Constant.Template.SIGN_IN.getKey(), Constant.Template.SIGN_IN.getValue(),
                Constant.Template.OUTPUT_SUFFIX.getKey(), dataMap);

        return sysFile;
    }


    /**
     * 会议通知
     *
     * @param sign
     * @param workProgram
     * @return
     */
    public static SysFile createTemplateNotice(Sign sign, WorkProgram workProgram, User user,List<RoomBooking> rbList) {

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

            sysFile = TemplateUtil.createTemplate(sign.getSignid(), Constant.SysFileType.SIGN.getValue(),
                    Constant.SysFileType.MEETING.getValue(), Constant.SysFileType.WORKPROGRAM.getValue(),
                    Constant.Template.UNIT_NOTICE.getKey(), Constant.Template.UNIT_NOTICE.getValue(),
                    Constant.Template.OUTPUT_SUFFIX.getKey(), dataMap);
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
    public static List<SysFile> createTemplateMeeting(Sign sign, WorkProgram workProgram,List<RoomBooking> rbList) {
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
                    sysFile = TemplateUtil.createTemplate(sign.getSignid(),  Constant.SysFileType.SIGN.getValue(),
                            Constant.SysFileType.MEETING.getValue(), Constant.SysFileType.WORKPROGRAM.getValue(),
                            Constant.Template.MEETING_AM.getKey(), Constant.Template.MEETING_AM.getValue(),
                            Constant.Template.OUTPUT_SUFFIX.getKey(), dataMap);
                    sysFileList.add(sysFile);

                } else {

                    sysFile = TemplateUtil.createTemplate(sign.getSignid(),  Constant.SysFileType.SIGN.getValue(),
                            Constant.SysFileType.MEETING.getValue(), Constant.SysFileType.WORKPROGRAM.getValue(),
                            Constant.Template.MEETING_PM.getKey(), Constant.Template.MEETING_PM.getValue(),
                            Constant.Template.OUTPUT_SUFFIX.getKey(), dataMap);
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
    public static SysFile createTemplateInvitation(Sign sign, WorkProgram workProgram, Expert expert, User user,List<RoomBooking> rbList) {
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
            sysFile = TemplateUtil.createTemplate(sign.getSignid(), Constant.SysFileType.SIGN.getValue(),
                    Constant.SysFileType.MEETING.getValue(), Constant.SysFileType.WORKPROGRAM.getValue(),
                    Constant.Template.INVITATION.getKey(), Constant.Template.INVITATION.getValue(),
                    Constant.Template.OUTPUT_SUFFIX.getKey(), dataMap);
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
    public static SysFile createTemplateCompere(Sign sign, WorkProgram workProgram, List<Expert> expertList) {
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

        SysFile sysFile = TemplateUtil.createTemplate(sign.getSignid(),  Constant.SysFileType.SIGN.getValue(),
                Constant.SysFileType.MEETING.getValue(), Constant.SysFileType.WORKPROGRAM.getValue(),
                Constant.Template.COMPERE.getKey(), Constant.Template.COMPERE.getValue(),
                Constant.Template.OUTPUT_SUFFIX.getKey(), dataMap);
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
    public static SysFile createTemplateAssist(Sign sign, WorkProgram workProgram, List<AssistPlanSign> apsList, AssistUnit assistUnit, Org org) {
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

        SysFile sysFile = TemplateUtil.createTemplate(sign.getSignid(),  Constant.SysFileType.SIGN.getValue(),
                Constant.SysFileType.MEETING.getValue(), Constant.SysFileType.WORKPROGRAM.getValue(),
                Constant.Template.ASSIST.getKey(), Constant.Template.ASSIST.getValue(),
                Constant.Template.OUTPUT_SUFFIX.getKey(), dataMap);
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
    public static SysFile subjectStudyNovice(Sign sign, WorkProgram workProgram, User user,List<RoomBooking> rbList) {
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
            sysFile = TemplateUtil.createTemplate(sign.getSignid(),  Constant.SysFileType.SIGN.getValue(),
                    Constant.SysFileType.SUBJECT_STUDY.getValue(), Constant.SysFileType.SUBJECT_STUDY.getValue(),
                    Constant.Template.SUBJECT_STUDY_NOVICE.getKey(), Constant.Template.SUBJECT_STUDY_NOVICE.getValue(),
                    Constant.Template.OUTPUT_SUFFIX.getKey(), dataMap);
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
    public static SysFile subjectStudySignIn(Sign sign, WorkProgram workProgram) {
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("dateStr", DateUtils.converToString(new Date(), "yyyy年MM月dd日"));
        SysFile sysFile = TemplateUtil.createTemplate(sign.getSignid(),  Constant.SysFileType.SIGN.getValue(),
                Constant.SysFileType.SUBJECT_STUDY.getValue(), Constant.SysFileType.SUBJECT_STUDY.getValue(),
                Constant.Template.SUBJECT_STUDY_SIGNIN.getKey(), Constant.Template.SUBJECT_STUDY_SIGNIN.getValue(),
                Constant.Template.OUTPUT_SUFFIX.getKey(), dataMap);
        return sysFile;
    }


    /**
     * 课题研究-专家签名表
     *
     * @param sign
     * @param workProgram
     * @return
     */
    public static SysFile subjectStudySignature(Sign sign, WorkProgram workProgram) {
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("projectName", sign.getProjectname());
        SysFile sysFile = TemplateUtil.createTemplate(sign.getSignid(),  Constant.SysFileType.SIGN.getValue(),
                Constant.SysFileType.SUBJECT_STUDY.getValue(), Constant.SysFileType.SUBJECT_STUDY.getValue(),
                Constant.Template.SUBJECT_STUDY_EXPERTAIGNATURE.getKey(), Constant.Template.SUBJECT_STUDY_EXPERTAIGNATURE.getValue(),
                Constant.Template.OUTPUT_SUFFIX.getKey(), dataMap);
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
    public static SysFile subjectStudyInvitation(Sign sign, WorkProgram workProgram, Expert expert, User user,List<RoomBooking> rbList) {
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
            sysFile = TemplateUtil.createTemplate(sign.getSignid(),  Constant.SysFileType.SIGN.getValue(),
                    Constant.SysFileType.SUBJECT_STUDY.getValue(), Constant.SysFileType.SUBJECT_STUDY.getValue(),
                    Constant.Template.SUBJECT_STUDY_INVITATION.getKey(), Constant.Template.SUBJECT_STUDY_INVITATION.getValue(),
                    Constant.Template.OUTPUT_SUFFIX.getKey(), dataMap);
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
    public static List<SysFile> subjectStudyMeeting(Sign sign, WorkProgram workProgram,List<RoomBooking> rbList) {
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
                    sysFile = TemplateUtil.createTemplate(sign.getSignid(),  Constant.SysFileType.SIGN.getValue(),
                            Constant.SysFileType.SUBJECT_STUDY.getValue(), Constant.SysFileType.SUBJECT_STUDY.getValue(),
                            Constant.Template.SUBJECT_STUDY_MEETINGAM.getKey(), Constant.Template.SUBJECT_STUDY_MEETINGAM.getValue(),
                            Constant.Template.OUTPUT_SUFFIX.getKey(), dataMap);
                    sysFileList.add(sysFile);

                } else {

                    sysFile = TemplateUtil.createTemplate(sign.getSignid(),  Constant.SysFileType.SIGN.getValue(),
                            Constant.SysFileType.SUBJECT_STUDY.getValue(), Constant.SysFileType.SUBJECT_STUDY.getValue(),
                            Constant.Template.SUBJECT_STUDY_MEETINGPM.getKey(), Constant.Template.SUBJECT_STUDY_MEETINGPM.getValue(),
                            Constant.Template.OUTPUT_SUFFIX.getKey(), dataMap);
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
        //todo:初始化参数
        dataMap.put("signTotal", signCount);
        dataMap.put("proTotal", proReviewConditionCur.getProCount());
        dataMap.put("declareTotal", proReviewConditionCur.getDeclareValue()+"");
        dataMap.put("authorizeTotal",proReviewConditionCur.getAuthorizeValue()+"");
        dataMap.put("ljhjTotal", proReviewConditionCur.getLjhj()+"");
        dataMap.put("hjlTotal", proReviewConditionCur.getHjl()+"%");

        dataMap.put("proAllTotal", proReviewConditionSum.getProCount());
        dataMap.put("declareAllTotal", proReviewConditionSum.getDeclareValue()+"");
        dataMap.put("authorizeAllTotal", proReviewConditionSum.getAuthorizeValue()+"");
        dataMap.put("ljhjAllTotal", proReviewConditionSum.getLjhj()+"");
        dataMap.put("hjlAllTotal", proReviewConditionSum.getHjl()+"%");
        dataMap.put("reviewCount",reviewCount);//评审会次数
        List<ProReviewConditionDto> proReviewCondition = proReviewConditionDtoList;
        List<ProReviewConditionDto> proReviewConditionAll = proReviewConditionDtoAllList;
        dataMap.put("proReviewConditionList",proReviewCondition);
        dataMap.put("proReviewConditionAllList",proReviewConditionAll);
        dataMap.put("proReviewCondDetailMap",proReviewCondDetailMap);
        String dayStr =  DateUtils.converToString(new Date(),null).split("-")[2];
        dataMap.put("curDay",dayStr);

        String[] reviewStage = {"xmjys-项目建议书","kxxyj-可行性研究报告","xmgs-项目概算","tqjr-提前介入","zjsq-资金申请报告","qt-其它","jksb-进口设备","sbqdgc-设备清单(国产)","sbqdjk-设备清单(进口)"};
        String[] reviewStageTotal = {"xmjysTotal-项目建议书","kxxyjTotal-可行性研究报告","xmgsTotal-项目概算","tqjrTotal-提前介入","zjsqTotal-资金申请报告","qtTotal-其它","jksbTotal-进口设备","sbqdgcTotal-设备清单(国产)","sbqdjkTotal-设备清单(进口)"};
        String[] projectType = {"projectTypeA-市政工程","projectTypeHouse-房建工程","projectTypeInfo-信息工程","projectTypeBuy-设备采购","projectTypeOther-其他"};
        boolean flag = true;
        //当月月报
        if (proReviewConditionDtoList.size()>0){
            for(int i=0;i<reviewStage.length;i++){
                flag = true;
                String [] tempArr = reviewStage[i].split("-");
                for(int j=0;j<proReviewConditionDtoList.size();j++){
                    if(tempArr[1].equals(proReviewConditionDtoList.get(j).getReviewStage())){
                        flag = false;
                        dataMap.put(tempArr[0], "完成"+tempArr[1]+"评审"+proReviewConditionDtoList.get(j).getProCount()+"项，申报总投资"+proReviewConditionDtoList.get(j).getDeclareValue()
                                +"亿元，审核后总投资"+proReviewConditionDtoList.get(j).getAuthorizeValue()+"亿元，累计净核减投资"+proReviewConditionDtoList.get(j).getLjhj()+"亿元,核减率"+proReviewConditionDtoList.get(j).getHjl()+"%");
                        break;
                    }
                }
                if(flag){
                    dataMap.put(tempArr[0],"");
                }
            }
        }
        //截止至当前月月报
        int reviewTotal = 0;
        String proCent = "";
        if (proReviewConditionDtoAllList.size()>0){
            for(int k=0;k<proReviewConditionDtoAllList.size();k++){
                if("提前介入".equals(proReviewConditionDtoAllList.get(k).getReviewStage())){
                    reviewTotal = proReviewConditionSum.getProCount().intValue() -  proReviewConditionDtoAllList.get(k).getProCount().intValue();
                    break;
                }else{
                    reviewTotal = proReviewConditionSum.getProCount().intValue();
                }
            }
            for(int i=0;i<reviewStageTotal.length;i++){
                flag = true;
                String [] tempArr = reviewStageTotal[i].split("-");
                for(int j=0;j<proReviewConditionDtoAllList.size();j++){
                    if(tempArr[1].equals(proReviewConditionDtoAllList.get(j).getReviewStage())){
                        if(!tempArr[1].equals("提前介入")){
                            flag = false;
                            if(reviewTotal !=0 ){
                                proCent = String.format("%.2f",(proReviewConditionDtoAllList.get(j).getProCount().floatValue()/(float) reviewTotal)*100)+"%";
                            }

                            dataMap.put(tempArr[0], "完成"+tempArr[1]+"评审"+proReviewConditionDtoAllList.get(j).getProCount()+"项，占评审项目数的"+proCent+"申报总投资"+proReviewConditionDtoAllList.get(j).getDeclareValue()
                                    +"亿元，审核后总投资"+proReviewConditionDtoAllList.get(j).getAuthorizeValue()+"亿元，累计净核减投资"+proReviewConditionDtoAllList.get(j).getLjhj()+"亿元,核减率"+proReviewConditionDtoAllList.get(j).getHjl()+"%");
                            break;
                        }else{
                            dataMap.put(tempArr[0], "另，完成"+tempArr[1]+"项目评审"+proReviewConditionDtoAllList.get(j).getProCount()+"项，申报总投资"+proReviewConditionDtoAllList.get(j).getDeclareValue()
                                    +"亿元，审核后总投资"+proReviewConditionDtoAllList.get(j).getAuthorizeValue()+"亿元，累计净核减投资"+proReviewConditionDtoAllList.get(j).getLjhj()+"亿元,核减率"+proReviewConditionDtoAllList.get(j).getHjl()+"%");
                            break;
                        }

                    }
                }
                if(flag){
                    dataMap.put(tempArr[0],"");
                }
            }
        }
        //项目类别
        String projectTypeItem = "1至"+monthlyNewsletterDto.getTheMonths()+"月评审的项目中,";
        if(proReviewConditionByTypeList.size()>0){
            for(int i=0;i<proReviewConditionByTypeList.size();i++){
              for(int j=0;j<projectType.length;j++){
                    String [] tempArr = projectType[j].split("-");
                    if(tempArr[1].equals(proReviewConditionByTypeList.get(i).getProjectType())){
                        proCent = String.format("%.2f",(proReviewConditionByTypeList.get(i).getProjectTypeCount().floatValue()/totalNum.floatValue()*100))+"%";

                        if(i!=(proReviewConditionByTypeList.size()-1)){
                            projectTypeItem += tempArr[1]+"类项目"+proReviewConditionByTypeList.get(i).getProjectTypeCount()+"项，占项目总数的"+proCent+";";
                        }else{
                            projectTypeItem += tempArr[1]+"类项目"+proReviewConditionByTypeList.get(i).getProjectTypeCount()+"项，占项目总数的"+proCent+"。";
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
        String  showName = Constant.Template.MONTH_REPORT.getValue() +  Constant.Template.OUTPUT_SUFFIX.getKey();
        String path = SysFileUtil.getUploadPath();
        String relativeFileUrl = SysFileUtil.generatRelativeUrl(path ,  Constant.Template.MONTH_REPORT.getValue() ,null , null , showName);

        File docFile = TemplateUtil.createDoc(dataMap , Constant.Template.MONTH_REPORT.getKey() , path + File.separator + relativeFileUrl);
        return docFile;
    }
}