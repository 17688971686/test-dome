package cs.common.utils;

import cs.common.Constant;
import cs.domain.expert.Expert;
import cs.domain.expert.ExpertType;
import cs.domain.meeting.RoomBooking;
import cs.domain.project.*;
import cs.domain.sys.Org;
import cs.domain.sys.SysFile;
import cs.domain.sys.User;

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
     * @param sign
     * @param workProgram
     */
    public static  SysFile createStudyTemplateOpinion(Sign sign , WorkProgram workProgram){
        Map<String , Object> dataMap = new HashMap<>();
        dataMap.put("projectName",sign.getProjectname());
        dataMap.put("docNum",sign.getDocnum());
        dataMap.put("explain","xxxxx");
        dataMap.put("title1","xxxxx");
        dataMap.put("content1","xxxxx");
        dataMap.put("title2","xxxxx");
        dataMap.put("content2","xxxxx");
        dataMap.put("dateStr",DateUtils.converToString(new Date(), "yyyy年MM月dd日"));

        SysFile sysFile = TemplateUtil.createTemplate(sign.getSignid() , workProgram.getId() , Constant.SysFileType.SIGN.getValue() ,
                Constant.SysFileType.TEMOLATE.getValue() , Constant.ProjectStage.STAGE_STUDY.getValue() ,
                Constant.Template.STUDY_OPINION.getKey() , Constant.Template.STUDY_OPINION.getValue().split("_")[1] ,
                Constant.Template.OUTPUT_SUFFIX.getKey() , dataMap);

        return sysFile;
    }


    /**
     * 评审组名单
     * @param sign
     * @param workProgram
     * @return
     */
    public static  SysFile createStudyTemplateRoster(Sign sign , WorkProgram workProgram , List<Expert> expertList){
        Map<String , Object> dataMap = new HashMap<>();

        dataMap.put("mdnum","xx");
        dataMap.put("projectName",sign.getProjectname());
        dataMap.put("generalCounsel","市发改委分管副主任");
        dataMap.put("counselor","市发改委主办处室处长");
        dataMap.put("director","主任");
        dataMap.put("viceDirector",workProgram.getLeaderName());
        dataMap.put("minister",workProgram.getMinisterName());
        String projectDirector = workProgram.getMianChargeUserName()+"," + workProgram.getSecondChargeUserName();
        dataMap.put("proojectDirector",projectDirector);

        String expertName="";
        for (Expert expert : expertList) {
            if(!StringUtil.isBlank(expertName) && expert.getName()!=null){
                expertName+=",";
            }
            List<ExpertType> expertTypeList = expert.getExpertType();
            expertName+=expert.getName();
        }

        String reviewGroup = "主任," + workProgram.getLeaderName()+"," + workProgram.getMinisterName() +
                "," + projectDirector + "," + expertName;
        dataMap.put("reviewGroup",reviewGroup);


        SysFile sysFile = TemplateUtil.createTemplate(sign.getSignid() , workProgram.getId() , Constant.SysFileType.SIGN.getValue() ,
                Constant.SysFileType.TEMOLATE.getValue() , Constant.ProjectStage.STAGE_STUDY.getValue() ,
                Constant.Template.STUDY_ROSTER.getKey() , Constant.Template.STUDY_ROSTER.getValue().split("_")[1] ,
                Constant.Template.OUTPUT_SUFFIX.getKey() , dataMap);

        return sysFile;
    }


    /**
     * 投资估算表
     * @param sign
     * @param workProgram
     * @return
     */
    public static SysFile createStudyTemplateEstimate(Sign sign , WorkProgram workProgram){
        Map<String , Object> dataMap = new HashMap<>();

        dataMap.put("gsnum","");
        dataMap.put("projectName",sign.getProjectname());

        SysFile sysFile = TemplateUtil.createTemplate(sign.getSignid() , workProgram.getId() , Constant.SysFileType.SIGN.getValue() ,
                Constant.SysFileType.TEMOLATE.getValue() , Constant.ProjectStage.STAGE_STUDY.getValue() ,
                Constant.Template.STUDY_ESTIMATE.getKey() , Constant.Template.STUDY_ESTIMATE.getValue().split("_")[1] ,
                Constant.Template.OUTPUT_SUFFIX_XLS.getKey() , dataMap);
        return sysFile;
    }


    /***********************dubget项目概算模板区*******************/

    /**
     * 审核意见
     * @param sign
     * @param workProgram
     * @return
     */
    public static SysFile createBudgetTemplateOpinion(Sign sign , WorkProgram workProgram){

        Map<String , Object> dataMap = new HashMap<>();
        dataMap.put("projectName",sign.getProjectname());
        dataMap.put("docNum",sign.getDocnum());
        dataMap.put("explain","xxxxx");
        dataMap.put("title1","xxxxx");
        dataMap.put("content1","xxxxx");
        dataMap.put("title2","xxxxx");
        dataMap.put("content2","xxxxx");
        dataMap.put("dateStr",DateUtils.converToString(new Date(), "yyyy年MM月dd日"));

        SysFile sysFile = TemplateUtil.createTemplate(sign.getSignid() , workProgram.getId() , Constant.SysFileType.SIGN.getValue() ,
                Constant.SysFileType.TEMOLATE.getValue() , Constant.ProjectStage.STAGE_BUDGET.getValue() ,
                Constant.Template.BUDGET_OPINION.getKey() , Constant.Template.BUDGET_OPINION.getValue().split("_")[1] ,
                Constant.Template.OUTPUT_SUFFIX.getKey() , dataMap);
        return sysFile;
    }


    /**
     * 建安工程费用
     * @param sign
     * @param workProgram
     * @return
     */
    public static SysFile createBudgetTemplateProjectCost(Sign sign ,WorkProgram workProgram){
        Map<String , Object> dataMap = new HashMap<>();
        dataMap.put("jagcfynum" ,"");
        dataMap.put("projectName" , sign.getProjectname());

        SysFile sysFile = TemplateUtil.createTemplate(sign.getSignid() , workProgram.getId() , Constant.SysFileType.SIGN.getValue() ,
                Constant.SysFileType.TEMOLATE.getValue() , Constant.ProjectStage.STAGE_BUDGET.getValue() ,
                Constant.Template.BUDGET_PROJECTCOST.getKey() , Constant.Template.BUDGET_PROJECTCOST.getValue().split("_")[1] ,
                Constant.Template.OUTPUT_SUFFIX.getKey() , dataMap);
        return sysFile;
    }

    /**
     * 概算汇总表和审核对照表
     * @param sign
     * @param workProgram
     * @return
     */
    public static SysFile createBudgetTemplateEstimate(Sign sign , WorkProgram workProgram){
        Map<String , Object> dataMap = new HashMap<>();
        dataMap.put("gsnum1" ,"项目总概算汇总表附件编号");
        dataMap.put("projectName" , sign.getProjectname());
        dataMap.put("gsnum2" ,"项目总概算审核对照表编号");

        SysFile sysFile = TemplateUtil.createTemplate(sign.getSignid() , workProgram.getId() , Constant.SysFileType.SIGN.getValue() ,
                Constant.SysFileType.TEMOLATE.getValue() , Constant.ProjectStage.STAGE_BUDGET.getValue() ,
                Constant.Template.BUDGET_ESTIMATE.getKey() , Constant.Template.BUDGET_ESTIMATE.getValue().split("_")[1] ,
                Constant.Template.OUTPUT_SUFFIX_XLS.getKey() , dataMap);
        return sysFile;
    }

    /**
     * 审核组名单
     * @param sign
     * @param workProgram
     * @return
     */
    public static SysFile createBudgetTemplateRoster(Sign sign , WorkProgram workProgram , List<Expert> expertList){
        Map<String , Object> dataMap = new HashMap<>();
        dataMap.put("mdnum","xx");
        dataMap.put("projectName",sign.getProjectname());
        dataMap.put("generalCounsel","市发改委分管副主任");
        dataMap.put("counselor","市发改委主办处室处长");
        dataMap.put("director","主任");
        dataMap.put("viceDirector",workProgram.getLeaderName());
        dataMap.put("minister",workProgram.getMinisterName());
        String projectDirector = workProgram.getMianChargeUserName()+"," + workProgram.getSecondChargeUserName();
        dataMap.put("proojectDirector",projectDirector);

        String expertName="";
        for (Expert expert : expertList) {
            if(!StringUtil.isBlank(expertName) && expert.getName()!=null){
                expertName+=",";
            }
            List<ExpertType> expertTypeList = expert.getExpertType();
            expertName+=expert.getName();
        }

        String reviewGroup = "主任," + workProgram.getLeaderName()+"," + workProgram.getMinisterName() +
                "," + projectDirector + "," + expertName;
        dataMap.put("reviewGroup",reviewGroup);

        SysFile sysFile = TemplateUtil.createTemplate(sign.getSignid() , workProgram.getId() , Constant.SysFileType.SIGN.getValue() ,
                Constant.SysFileType.TEMOLATE.getValue() , Constant.ProjectStage.STAGE_BUDGET.getValue() ,
                Constant.Template.BUDGET_ROSTER.getKey() , Constant.Template.BUDGET_ROSTER.getValue().split("_")[1],
                Constant.Template.OUTPUT_SUFFIX.getKey() , dataMap);
        return sysFile;
    }


    /***************************sug项目建议书****************************/

    /**
     * 评审意见
     * @param sign
     * @param workProgram
     * @return
     */
    public static SysFile createSugTemplateOpinion(Sign sign , WorkProgram workProgram){
        Map<String , Object> dataMap = new HashMap<>();
        dataMap.put("projectName",sign.getProjectname());
        dataMap.put("docNum",sign.getDocnum());
        dataMap.put("explain","xxxxx");
        dataMap.put("title1","xxxxx");
        dataMap.put("content1","xxxxx");
        dataMap.put("title2","xxxxx");
        dataMap.put("content2","xxxxx");
        System.out.print(DateUtils.converToString(new Date(), "yyyy年MM月dd日"));
        dataMap.put("dateStr",DateUtils.converToString(new Date(), "yyyy年MM月dd日"));

        SysFile sysFile = TemplateUtil.createTemplate(sign.getSignid(), workProgram.getId() , Constant.SysFileType.SIGN.getValue() ,
                Constant.SysFileType.TEMOLATE.getValue() , Constant.ProjectStage.STAGE_SUG.getValue() ,
                Constant.Template.SUG_OPINION.getKey() , Constant.Template.SUG_OPINION.getValue().split("_")[1],
                Constant.Template.OUTPUT_SUFFIX.getKey() , dataMap);
        return sysFile;
    }

    /**
     * 投资匡算表
     * @param sign
     * @param workProgram
     * @return
     */
    public static SysFile createSugTemplateEstime(Sign sign , WorkProgram workProgram){

        Map<String ,Object> dataMap = new HashMap<>();
        dataMap.put("ksnum" , "");
        dataMap.put("projectName" , sign.getProjectname());
        SysFile sysFile = TemplateUtil.createTemplate(sign.getSignid() , workProgram.getId() , Constant.SysFileType.SIGN.getValue() ,
                Constant.SysFileType.TEMOLATE.getValue() , Constant.ProjectStage.STAGE_SUG.getValue() ,
                Constant.Template.SUG_ESTIMATE.getKey() , Constant.Template.SUG_ESTIMATE.getValue().split("_")[1] ,
                Constant.Template.OUTPUT_SUFFIX_XLS.getKey() , dataMap);
        return sysFile;
    }

    /**
     * 评审组名单
     * @param sign
     * @param workProgram
     * @return
     */
    public static  SysFile createSugTemplateRoster(Sign sign ,WorkProgram workProgram , List<Expert> expertList){
        Map<String , Object> dataMap = new HashMap<>();
        dataMap.put("psnum","xx");
        dataMap.put("projectName",sign.getProjectname());
        dataMap.put("generalCounsel","市发改委分管副主任");
        dataMap.put("counselor","市发改委主办处室处长");
        dataMap.put("director","主任");
        dataMap.put("viceDirector",workProgram.getLeaderName());
        dataMap.put("minister",workProgram.getMinisterName());
        String projectDirector = workProgram.getMianChargeUserName()+"," + workProgram.getSecondChargeUserName();
        dataMap.put("proojectDirector",projectDirector);

        String expertName="";
        for (Expert expert : expertList) {
            if(!StringUtil.isBlank(expertName) && expert.getName()!=null){
                expertName+=",";
            }
            List<ExpertType> expertTypeList = expert.getExpertType();
            expertName+=expert.getName();
        }

        String reviewGroup = "主任," + workProgram.getLeaderName()+"," + workProgram.getMinisterName() +
                "," + projectDirector + "," + expertName;
        dataMap.put("reviewGroup",reviewGroup);

        SysFile sysFile = TemplateUtil.createTemplate(sign.getSignid() , workProgram.getId(), Constant.SysFileType.SIGN.getValue(),
                Constant.SysFileType.TEMOLATE.getValue() , Constant.ProjectStage.STAGE_SUG.getValue() ,
                Constant.Template.BUDGET_ROSTER.getKey() , Constant.Template.SUG_ROSTER.getValue().split("_")[1] ,
                Constant.Template.OUTPUT_SUFFIX.getKey() , dataMap);
        return sysFile;
    }


    /********************************资金申请报告**********************************/

    /**
     * 评审意见
     * @param sign
     * @param workProgram
     * @return
     */
    public static SysFile createReportTemplateOpinion(Sign sign , WorkProgram workProgram){
        Map<String , Object> dataMap = new HashMap<>();
        dataMap.put("projectName",sign.getProjectname());
        dataMap.put("docNum",sign.getDocnum());
        dataMap.put("explain","xxxxx");
        dataMap.put("title1","xxxxx");
        dataMap.put("content1","xxxxx");
        dataMap.put("title2","xxxxx");
        dataMap.put("content2","xxxxx");
        dataMap.put("dateStr",DateUtils.converToString(new Date(), "yyyy年MM月dd日"));

        SysFile sysFile = TemplateUtil.createTemplate(sign.getSignid() , workProgram.getId() , Constant.SysFileType.SIGN.getValue() ,
                Constant.SysFileType.TEMOLATE.getValue() , Constant.ProjectStage.APPLY_REPORT.getValue() ,
                Constant.Template.REPORT_OPINION.getKey() , Constant.Template.REPORT_OPINION.getValue().split("_")[1] ,
                Constant.Template.OUTPUT_SUFFIX.getKey() ,dataMap);
        return sysFile;
    }

    /**
     * 投资估算表
     * @param sign
     * @param workProgram
     * @return
     */
    public static SysFile createReportTemplateEstimate(Sign sign , WorkProgram workProgram){
        Map<String , Object> dataMap = new HashMap<>();
        dataMap.put("gsnum", "");
        dataMap.put("projectName" , sign.getProjectname());
        SysFile sysFile = TemplateUtil.createTemplate(sign.getSignid() , workProgram.getId() , Constant.SysFileType.SIGN.getValue() ,
                Constant.SysFileType.TEMOLATE.getValue() , Constant.ProjectStage.APPLY_REPORT.getValue() ,
                Constant.Template.REPORT_ESTIMATE.getKey() , Constant.Template.REPORT_ESTIMATE.getValue().split("_")[1] ,
                Constant.Template.OUTPUT_SUFFIX_XLS.getKey() ,dataMap);
        return sysFile;
    }

    /**
     * 评审组名单
     * @param sign
     * @param workProgram
     * @return
     */
    public static SysFile createReportTemplateRoster(Sign sign , WorkProgram workProgram , List<Expert> expertList){
        Map<String , Object> dataMap = new HashMap<>();
        dataMap.put("psnum","xx");
        dataMap.put("projectName",sign.getProjectname());
        dataMap.put("generalCounsel","市发改委分管副主任");
        dataMap.put("counselor","市发改委主办处室处长");
        dataMap.put("director","主任");
        dataMap.put("viceDirector",workProgram.getLeaderName());
        dataMap.put("minister",workProgram.getMinisterName());
        String projectDirector = workProgram.getMianChargeUserName()+"," + workProgram.getSecondChargeUserName();
        dataMap.put("proojectDirector",projectDirector);

        String expertName="";
        for (Expert expert : expertList) {
            if(!StringUtil.isBlank(expertName) && expert.getName()!=null){
                expertName+=",";
            }
            List<ExpertType> expertTypeList = expert.getExpertType();
            expertName+=expert.getName();
        }

        String reviewGroup = "主任," + workProgram.getLeaderName()+"," + workProgram.getMinisterName() +
                "," + projectDirector + "," + expertName;
        dataMap.put("reviewGroup",reviewGroup);

        SysFile sysFile = TemplateUtil.createTemplate(sign.getSignid() , workProgram.getId() , Constant.SysFileType.SIGN.getValue() ,
                Constant.SysFileType.TEMOLATE.getValue() , Constant.ProjectStage.APPLY_REPORT.getValue() ,
                Constant.Template.REPORT_ROSTER.getKey() , Constant.Template.REPORT_ROSTER.getValue().split("_")[1] ,
                Constant.Template.OUTPUT_SUFFIX.getKey() , dataMap);
        return sysFile;
    }


    /********************end发文模板**************************************/




    /************************begin会前准备材料******************************************/

    /**
     * 生成签到表
     * @param sign
     * @param workProgram
     * @return
     */
    public static SysFile createtTemplateSignIn(Sign sign ,WorkProgram workProgram){
        Map<String , Object> dataMap = new HashMap<>();
        dataMap.put("projectName",sign.getProjectname());
        dataMap.put("dateStr",DateUtils.converToString(new Date(), "yyyy年MM月dd日"));
        dataMap.put("reviewStage",sign.getReviewstage());

        SysFile sysFile = TemplateUtil.createTemplate(sign.getSignid() , workProgram.getId() , Constant.SysFileType.SIGN.getValue() ,
                Constant.SysFileType.MEETING.getValue() , Constant.SysFileType.WORKPROGRAM.getValue() ,
                Constant.Template.SIGN_IN.getKey() , Constant.Template.SIGN_IN.getValue() ,
                Constant.Template.OUTPUT_SUFFIX.getKey() , dataMap);

        return sysFile;
    }


    /**
     * 会议通知
     * @param sign
     * @param workProgram
     * @return
     */
    public static SysFile createTemplateNotice(Sign sign , WorkProgram workProgram , User user){

        Map<String , Object> dataMap = new HashMap<>();
        dataMap.put("projectName" , sign.getProjectname());
        dataMap.put("reviewStage" , sign.getReviewstage());
        dataMap.put("sendFileUnit","深圳市人民检察院");
        dataMap.put("studyBeginTime",DateUtils.converToString(workProgram.getStudyBeginTime(), "yyyy年MM月dd日"));
        dataMap.put("contactPerson",user.getDisplayName());//联系人
        dataMap.put("contactPersonTel",user.getUserPhone());//联系电话
        dataMap.put("contactPersonFax","83642081");//传真
        dataMap.put("contactPersonAddress", "深圳市政府投资项目评审中心");
        dataMap.put("dateStr",DateUtils.converToString(new Date(), "yyyy年MM月dd日"));
        //获得会议信息
        List<RoomBooking> rbList=workProgram.getRoomBookings();
        SysFile sysFile=null;
        if(rbList!=null && rbList.size()>0) {
            Date compareDate = DateUtils.converToDate("12:00", "HH:MM");
            for (RoomBooking roomBooking : rbList) {
                String[] str= roomBooking.getRbDate().split("-");
                String[] str2=str[2].split("\\(");
                dataMap.put("rbDate", str[0]+"年"+str[1]+"月" + str2[0] + "日(" + str2[1]);
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

            sysFile = TemplateUtil.createTemplate(sign.getSignid(), workProgram.getId(), Constant.SysFileType.SIGN.getValue(),
                    Constant.SysFileType.MEETING.getValue(), Constant.SysFileType.WORKPROGRAM.getValue(),
                    Constant.Template.UNIT_NOTICE.getKey(), Constant.Template.UNIT_NOTICE.getValue(),
                    Constant.Template.OUTPUT_SUFFIX.getKey(), dataMap);
        }
        return sysFile;
    }


    /**
     * 评审会议议程
     * @param sign
     * @param workProgram
     * @return
     */
    public static List<SysFile> createTemplateMeeting(Sign sign , WorkProgram workProgram) {
        List<SysFile> sysFileList = new ArrayList<>();
        SysFile sysFile = null;

        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("projectName", sign.getProjectname());
        dataMap.put("reviewStage", sign.getReviewstage());

        List<RoomBooking> rbList = workProgram.getRoomBookings();
        Date compareDate = DateUtils.converToDate("12:00", "HH:mm");
        if (!rbList.isEmpty()) {
            for (RoomBooking roomBooking : rbList) {

                dataMap.put("rbDate", roomBooking.getRbDate());//会议日期显示星期
                dataMap.put("beginTime", DateUtils.converToString(roomBooking.getBeginTime(), "HH:mm"));//会议开始时间
                dataMap.put("endTime", DateUtils.converToString(roomBooking.getEndTime(), "HH:mm"));//会议结束时间
                dataMap.put("meetingAddress", roomBooking.getAddressName());
                if (DateUtils.compareIgnoreSecond(roomBooking.getBeginTime(), compareDate) == 1) {
                    sysFile = TemplateUtil.createTemplate(sign.getSignid() , workProgram.getId() , Constant.SysFileType.SIGN.getValue() ,
                            Constant.SysFileType.MEETING.getValue() , Constant.SysFileType.WORKPROGRAM.getValue() ,
                            Constant.Template.MEETING_AM.getKey() , Constant.Template.MEETING_AM.getValue() ,
                            Constant.Template.OUTPUT_SUFFIX.getKey() , dataMap);
                    sysFileList.add(sysFile);

                }else{

                    sysFile = TemplateUtil.createTemplate(sign.getSignid() , workProgram.getId() , Constant.SysFileType.SIGN.getValue() ,
                            Constant.SysFileType.MEETING.getValue() , Constant.SysFileType.WORKPROGRAM.getValue() ,
                            Constant.Template.MEETING_PM.getKey() , Constant.Template.MEETING_PM.getValue() ,
                            Constant.Template.OUTPUT_SUFFIX.getKey() , dataMap);
                    sysFileList.add(sysFile);
                }
            }
        }
        return sysFileList;
    }

    /**
     * 邀请函
     * @param sign
     * @param workProgram
     * @param expert
     * @return
     */
    public static SysFile createTemplateInvitation(Sign sign , WorkProgram workProgram , Expert expert,User user){
        Map<String ,Object> dataMap = new HashMap<>();
        dataMap.put("expertName",expert.getName());
        dataMap.put("projectName" , sign.getProjectname());
        dataMap.put("studyBeginTime",DateUtils.converToString(workProgram.getStudyBeginTime(), "yyyy年MM月dd日"));
        dataMap.put("contactPerson",user.getDisplayName());//联系人
        dataMap.put("contactPersonTel",user.getUserPhone());//联系电话
        dataMap.put("contactPersonFax","83642081");//传真
        dataMap.put("contactPersonAddress", "深圳市政府投资项目评审中心");
        dataMap.put("dateStr",DateUtils.converToString(new Date(), "yyyy年MM月dd日"));
        //获得会议信息
        List<RoomBooking> rbList=workProgram.getRoomBookings();
        SysFile sysFile=null;
        if(rbList!=null && rbList.size()>0) {
            Date compareDate = DateUtils.converToDate("12:00", "HH:mm");
            for (RoomBooking roomBooking : rbList) {
//                String[] str = roomBooking.getRbDate().split("-");
//                String[] str2 = str[2].split("\\(");
//                dataMap.put("rbDate", str[0] + "年" + str[1] + "月" + str2[0] + "日(" + str2[1]);
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
                sysFile = TemplateUtil.createTemplate(sign.getSignid() , workProgram.getId() , Constant.SysFileType.SIGN.getValue() ,
                        Constant.SysFileType.MEETING.getValue() , Constant.SysFileType.WORKPROGRAM.getValue() ,
                        Constant.Template.INVITATION.getKey() , Constant.Template.INVITATION.getValue() ,
                        Constant.Template.OUTPUT_SUFFIX.getKey() , dataMap);
            }

        return sysFile;
    }


    /**
     * 主持人稿
     * @param sign
     * @param workProgram
     * @param expertList
     * @return
     */
    public static SysFile createTemplateCompere(Sign sign, WorkProgram workProgram , List<Expert> expertList){
        Map<String ,Object> dataMap = new HashMap<>();
        dataMap.put("projectName" , sign.getProjectname());
        String expertName="";
        String expertType = "";
        for (Expert expert : expertList) {
            if(!StringUtil.isBlank(expertName) && expert.getName()!=null){
                expertName+="、";
            }
            List<ExpertType> expertTypeList = expert.getExpertType();
            for(ExpertType et : expertTypeList){
                if(!StringUtil.isBlank(expertType) && !StringUtil.isBlank(et.getExpertType()) && expertType.indexOf(et.getExpertType())==-1){
                    expertType+="、";
                }
                if(expertType.indexOf(et.getExpertType())==-1){

                    expertType += et.getExpertType();
                }
            }
            expertName+=expert.getName();
        }
        dataMap.put("expertName", expertName);
        dataMap.put("expertType" , expertType);
        dataMap.put("maindeptName", sign.getMaindeptName());//主办事处名称
        dataMap.put("assistdeptName", sign.getAssistdeptName());//协办事处名称
        dataMap.put("mainDeptName", sign.getMaindeptName());//主管部门名称
        dataMap.put("builtcompanyName", sign.getBuiltcompanyName());//建设单位
        dataMap.put("designcompanyName", sign.getDesigncompanyName());//编制单位
        dataMap.put("leaderName",sign.getLeaderName() );//中心领导名
        dataMap.put("ministerName",workProgram.getMinisterName() );//部长名
        dataMap.put("mianChargeUserName",workProgram.getMianChargeUserName() ); //第一负责人
        dataMap.put("secondChargeUserName",workProgram.getSecondChargeUserName() ); //第二负责人
        dataMap.put("projectBackGround", workProgram.getProjectBackGround());
        dataMap.put("buildSize", workProgram.getBuildSize());
        dataMap.put("buildContent", workProgram.getBuildContent());
        dataMap.put("appalyInvestment", workProgram.getAppalyInvestment());//申报金额
        dataMap.put("mainPoint",workProgram.getMainPoint());//拟评审重点问题

        SysFile sysFile = TemplateUtil.createTemplate(sign.getSignid() , workProgram.getId() , Constant.SysFileType.SIGN.getValue() ,
                Constant.SysFileType.MEETING.getValue() , Constant.SysFileType.WORKPROGRAM.getValue() ,
                Constant.Template.COMPERE.getKey() , Constant.Template.COMPERE.getValue() ,
                Constant.Template.OUTPUT_SUFFIX.getKey() , dataMap);
        return sysFile;
    }


    /**
     * 协审协议书
     * @param sign
     * @param workProgram
     * @param apsList
     * @param assistUnit
     * @param org
     * @return
     */
    public static SysFile createTemplateAssist(Sign sign ,WorkProgram workProgram, List<AssistPlanSign> apsList , AssistUnit assistUnit , Org org){
        Map<String , Object> dataMap = new HashMap<>();
        dataMap.put("workreviveStage" , "");
       if(org != null){
           dataMap.put("orgName",org.getName());
           dataMap.put("orgAddress",org.getOrgAddress());
           dataMap.put("orgMLeader",org.getOrgMLeader());
           dataMap.put("orgPhone",org.getOrgPhone());
           dataMap.put("orgFax",org.getOrgFax());
       }
       if(assistUnit !=null){
           dataMap.put("unitName",assistUnit.getUnitName());
           dataMap.put("address",assistUnit.getAddress());
           dataMap.put("phoneNum",assistUnit.getPhoneNum());
           dataMap.put("principalName",assistUnit.getPrincipalName());
           dataMap.put("contactFax",assistUnit.getContactFax());
       }
       dataMap.put("projectName" , sign.getProjectname());
       if(!apsList.isEmpty()){
           AssistPlanSign assistPlanSign=apsList.get(0);
           AssistPlan assistPlan=assistPlanSign.getAssistPlan();
           float assistDays=assistPlanSign.getAssistDays();
           Date reportTime=assistPlan.getReportTime();
           Date finishTime=DateUtils.addDay(reportTime, (int)assistDays);
           dataMap.put("finishTime", DateUtils.converToString(finishTime, "yyyy年MM月dd日")); //建设规模
           dataMap.put("estimateCost", assistPlanSign.getEstimateCost()); //建设规模
           dataMap.put("assistCost",assistPlanSign.getAssistCost()); //协审费用
       }
        dataMap.put("assistDeptUserName", sign.getAssistdeptName()); //协办事处联系人
        dataMap.put("mainDeptUserName", sign.getMainDeptUserName()); //主办事处联系人

        SysFile sysFile = TemplateUtil.createTemplate(sign.getSignid() , workProgram.getId() , Constant.SysFileType.SIGN.getValue() ,
                Constant.SysFileType.MEETING.getValue() , Constant.SysFileType.WORKPROGRAM.getValue() ,
                Constant.Template.ASSIST.getKey() , Constant.Template.ASSIST.getValue() ,
                Constant.Template.OUTPUT_SUFFIX.getKey() , dataMap);
        return sysFile;

    }

    /************************end会前准备材料******************************************/
}