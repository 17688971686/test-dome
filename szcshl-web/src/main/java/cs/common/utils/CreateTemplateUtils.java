package cs.common.utils;

import cs.common.constants.Constant;
import cs.common.constants.ProjectConstant;
import cs.domain.expert.Expert;
import cs.domain.expert.ExpertSelected;
import cs.domain.meeting.RoomBooking;
import cs.domain.project.Sign;
import cs.domain.project.WorkProgram;
import cs.domain.sys.Ftp;
import cs.domain.sys.SysFile;
import cs.domain.sys.User;
import cs.model.expert.ProReviewConditionDto;
import cs.model.monthly.MonthlyNewsletterDto;

import java.io.File;
import java.math.BigDecimal;
import java.util.*;

import static cs.common.constants.Constant.COMPANY_NAME;

/**
 * Description: 生成模板并生成附件类
 * Author: mcl
 * Date: 2017/9/4 9:05
 */
public class CreateTemplateUtils {

    /**
     * 获取确认参与的专家名称进行拼接
     *
     * @param expertSelectedList
     * @return
     */
    private static String getExpertName(List<ExpertSelected> expertSelectedList) {
        String expertName = "";
        if(Validate.isList(expertSelectedList)){
            for (ExpertSelected expertSelected : expertSelectedList) {
                if (Constant.EnumState.YES.getValue().equals(expertSelected.getIsConfrim())
                        && Constant.EnumState.YES.getValue().equals(expertSelected.getIsJoin())) {
                    Expert expert = expertSelected.getExpert();
                    if (!StringUtil.isBlank(expertName)
                            && expert.getName() != null) {
                        expertName += ",";
                    }
                    expertName += expert.getName();
                }
            }
        }

        return expertName;
    }

/********************begin发文模板**************************************/

/************** begin**study可行性研究报告模板区********************/
    /**
     * 评审意见
     *
     * @param sign
     */
    public static SysFile createStudyTemplateOpinion(Ftp f, Sign sign) {
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("projectName", sign.getProjectname());

        dataMap.put("docNum", "深投审〔" + DateUtils.converToString(new Date(), DateUtils.DATE_YEAR) + "〕");
        dataMap.put("explain", "xxxxx");
        dataMap.put("title1", "xxxxx");
        dataMap.put("content1", "xxxxx");
        dataMap.put("title2", "xxxxx");
        dataMap.put("content2", "xxxxx");
        dataMap.put("dateStr", DateUtils.converToString(new Date(), "yyyy年MM月dd日"));

        SysFile sysFile = TemplateUtil.createTemplate(
                f, sign.getSignid(), Constant.SysFileType.SIGN.getValue(),
                sign.getSignid(), Constant.SysFileType.TEMOLATE.getValue(),
                ProjectConstant.REVIEW_STATE_ENUM.STAGESUG.getZhCode(),
                Constant.Template.STUDY_OPINION.getKey(),
                Constant.Template.STUDY_OPINION.getValue().split("_")[1],
                Constant.Template.WORD_SUFFIX.getKey(),
                dataMap);

        return sysFile;
    }


    /**
     * 评审组名单
     *
     * @param sign
     * @return
     */
    public static SysFile createStudyTemplateRoster(Ftp f, Sign sign, List<ExpertSelected> expertSelectedList, WorkProgram workProgram, String generalCounsel, String counselor) {
        Map<String, Object> dataMap = new HashMap<>();

        dataMap.put("mdnum", "3");
        dataMap.put("projectName", sign.getProjectname());
        dataMap.put("generalCounsel",  generalCounsel );
        dataMap.put("counselor",  counselor );
        dataMap.put("director", sign.getLeaderName());
        String leaderName = sign.getLeaderName() == null ? "" : sign.getLeaderName();
        dataMap.put("viceDirector", leaderName);
        String ministerName  = "";
        String projectDirector = "";
        if(workProgram != null){
            ministerName = workProgram.getMinisterName() == null ? "" : workProgram.getMinisterName();

            projectDirector = (workProgram.getMianChargeUserName() == null ? "" : workProgram.getMianChargeUserName())
                    + (workProgram.getSecondChargeUserName() == null ? "" : "   " + workProgram.getSecondChargeUserName().replaceAll(",", "   "));
        }

        dataMap.put("minister", ministerName);
        dataMap.put("proojectDirector", projectDirector);

        String expertName = getExpertName(expertSelectedList);

        //排序为：总负责人、专业负责人、审核人、专家组 、项目负责人（人名还三个空格隔开）
        String reviewGroup = leaderName + "   " + ministerName;

        if (!"".equals(expertName) && expertName.length() > 0) {

            reviewGroup += "   " + expertName.replaceAll(",", "   ");
        }

        reviewGroup += "   " + projectDirector;

        dataMap.put("reviewGroup", reviewGroup);


        SysFile sysFile = TemplateUtil.createTemplate(
                f, sign.getSignid(), Constant.SysFileType.SIGN.getValue(),
                sign.getSignid(), Constant.SysFileType.TEMOLATE.getValue(),
                ProjectConstant.REVIEW_STATE_ENUM.STAGESTUDY.getZhCode(),
                Constant.Template.STUDY_ROSTER.getKey(),
                Constant.Template.STUDY_ROSTER.getValue().split("_")[1],
                Constant.Template.WORD_SUFFIX.getKey(),
                dataMap);

        return sysFile;
    }


    /**
     * 投资估算表
     *
     * @param sign
     * @return
     */
    public static SysFile createStudyTemplateEstimate(Ftp f, Sign sign) {
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("gsnum", "2");
        dataMap.put("projectName", sign.getProjectname());

        SysFile sysFile = TemplateUtil.createTemplate(
                f, sign.getSignid(), Constant.SysFileType.SIGN.getValue(),
                sign.getSignid(), Constant.SysFileType.TEMOLATE.getValue(),
                ProjectConstant.REVIEW_STATE_ENUM.STAGESTUDY.getZhCode(),
                Constant.Template.STUDY_ESTIMATE.getKey(),
                Constant.Template.STUDY_ESTIMATE.getValue().split("_")[1],
                Constant.Template.EXCEL_SUFFIX.getKey(),
                dataMap);
        return sysFile;
    }

    /************** end**study可行性研究报告模板区********************/


    /**************begin*********dubget项目概算模板区*******************/

    /**
     * 审核意见
     *
     * @param sign
     * @return
     */
    public static SysFile createBudgetTemplateOpinion(Ftp f, Sign sign) {

        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("projectName", sign.getProjectname());
        dataMap.put("docNum", "深投审〔" + DateUtils.converToString(new Date(), DateUtils.DATE_YEAR) + "〕");
        dataMap.put("explain", "xxxxx");
        dataMap.put("title1", "xxxxx");
        dataMap.put("content1", "xxxxx");
        dataMap.put("title2", "xxxxx");
        dataMap.put("content2", "xxxxx");
        dataMap.put("dateStr", DateUtils.converToString(new Date(), "yyyy年MM月dd日"));

        SysFile sysFile = TemplateUtil.createTemplate(
                f, sign.getSignid(), Constant.SysFileType.SIGN.getValue(),
                sign.getSignid(), Constant.SysFileType.TEMOLATE.getValue(),
                ProjectConstant.REVIEW_STATE_ENUM.STAGEBUDGET.getZhCode(),
                Constant.Template.BUDGET_OPINION.getKey(),
                Constant.Template.BUDGET_OPINION.getValue().split("_")[1],
                Constant.Template.WORD_SUFFIX.getKey(),
                dataMap);
        return sysFile;
    }


    /**
     * 建安工程费用
     *
     * @param sign
     * @return
     */
    public static SysFile createBudgetTemplateProjectCost(Ftp f, Sign sign) {
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("jagcfynum", "4");
        dataMap.put("projectName", sign.getProjectname());

        SysFile sysFile = TemplateUtil.createTemplate(
                f, sign.getSignid(), Constant.SysFileType.SIGN.getValue(),
                sign.getSignid(), Constant.SysFileType.TEMOLATE.getValue(),
                ProjectConstant.REVIEW_STATE_ENUM.STAGEBUDGET.getZhCode(),
                Constant.Template.BUDGET_PROJECTCOST.getKey(),
                Constant.Template.BUDGET_PROJECTCOST.getValue().split("_")[1],
                Constant.Template.WORD_SUFFIX.getKey(), dataMap);
        return sysFile;
    }

    /**
     * 概算汇总表和审核对照表
     *
     * @param sign
     * @return
     */
    public static SysFile createBudgetTemplateEstimate(Ftp f, Sign sign) {
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("gsnum1", "1");
        dataMap.put("projectName", sign.getProjectname());
        dataMap.put("gsnum2", "2");

        SysFile sysFile = TemplateUtil.createTemplate(
                f, sign.getSignid(), Constant.SysFileType.SIGN.getValue(),
                sign.getSignid(), Constant.SysFileType.TEMOLATE.getValue(),
                ProjectConstant.REVIEW_STATE_ENUM.STAGEBUDGET.getZhCode(),
                Constant.Template.BUDGET_ESTIMATE.getKey(),
                Constant.Template.BUDGET_ESTIMATE.getValue().split("_")[1],
                Constant.Template.EXCEL_SUFFIX.getKey(),
                dataMap);
        return sysFile;
    }

    /**
     * 审核组名单
     *
     * @return
     */
    public static SysFile createBudgetTemplateRoster(Ftp f, Sign sign, List<ExpertSelected> expertSelectedList, WorkProgram workProgram, String generalCounsel, String counselor) {
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("mdnum", "3");
        dataMap.put("projectName", sign.getProjectname());
        dataMap.put("generalCounsel", generalCounsel );
        dataMap.put("counselor",  counselor );
        dataMap.put("director", sign.getLeaderName());
        String leanderName = sign.getLeaderName() == null ? "" : sign.getLeaderName();
        dataMap.put("viceDirector", leanderName);
        String ministerName  = "";
        String projectDirector = "";
        if(workProgram != null){
            ministerName = workProgram.getMinisterName() == null ? "" : workProgram.getMinisterName();

            projectDirector = (workProgram.getMianChargeUserName() == null ? "" : workProgram.getMianChargeUserName())
                    + (workProgram.getSecondChargeUserName() == null ? "" : "   " + workProgram.getSecondChargeUserName().replaceAll(",", "   "));
        }

        dataMap.put("minister", ministerName);

        dataMap.put("proojectDirector", projectDirector);

        String expertName = getExpertName(expertSelectedList);

        String reviewGroup = leanderName + "   " + ministerName;

        if (!"".equals(expertName) && expertName.length() > 0) {
            reviewGroup += "   " + expertName.replaceAll(",", "   ");
        }
        reviewGroup += "   " + projectDirector;

        dataMap.put("reviewGroup", reviewGroup);

        SysFile sysFile = TemplateUtil.createTemplate(
                f, sign.getSignid(), Constant.SysFileType.SIGN.getValue(),
                sign.getSignid(), Constant.SysFileType.TEMOLATE.getValue(),
                ProjectConstant.REVIEW_STATE_ENUM.STAGEBUDGET.getZhCode(),
                Constant.Template.BUDGET_ROSTER.getKey(),
                Constant.Template.BUDGET_ROSTER.getValue().split("_")[1],
                Constant.Template.WORD_SUFFIX.getKey(),
                dataMap);
        return sysFile;
    }
/**************end*********dubget项目概算模板区*******************/

    /**************begin*************sug项目建议书****************************/

    /**
     * 评审意见
     *
     * @param sign
     * @return
     */
    public static SysFile createSugTemplateOpinion(Ftp f, Sign sign) {
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("projectName", sign.getProjectname());
        dataMap.put("docNum", "深投审〔" + DateUtils.converToString(new Date(), DateUtils.DATE_YEAR) + "〕");
        dataMap.put("explain", "xxxxx");
        dataMap.put("title1", "xxxxx");
        dataMap.put("content1", "xxxxx");
        dataMap.put("title2", "xxxxx");
        dataMap.put("content2", "xxxxx");
        System.out.print(DateUtils.converToString(new Date(), "yyyy年MM月dd日"));
        dataMap.put("dateStr", DateUtils.converToString(new Date(), "yyyy年MM月dd日"));

        SysFile sysFile = TemplateUtil.createTemplate(
                f, sign.getSignid(), Constant.SysFileType.SIGN.getValue(),
                sign.getSignid(), Constant.SysFileType.TEMOLATE.getValue(),
                ProjectConstant.REVIEW_STATE_ENUM.STAGESUG.getZhCode(),
                Constant.Template.SUG_OPINION.getKey(),
                Constant.Template.SUG_OPINION.getValue().split("_")[1],
                Constant.Template.WORD_SUFFIX.getKey(),
                dataMap);
        return sysFile;
    }

    /**
     * 投资匡算表
     *
     * @param sign
     * @return
     */
    public static SysFile createSugTemplateEstime(Ftp f, Sign sign) {

        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("ksnum", "2");
        dataMap.put("projectName", sign.getProjectname());
        SysFile sysFile = TemplateUtil.createTemplate(
                f, sign.getSignid(), Constant.SysFileType.SIGN.getValue(),
                sign.getSignid(), Constant.SysFileType.TEMOLATE.getValue(),
                ProjectConstant.REVIEW_STATE_ENUM.STAGESUG.getZhCode(),
                Constant.Template.SUG_ESTIMATE.getKey(),
                Constant.Template.SUG_ESTIMATE.getValue().split("_")[1],
                Constant.Template.EXCEL_SUFFIX.getKey(),
                dataMap);
        return sysFile;
    }

    /**
     * 评审组名单
     *
     * @param sign
     * @param expertSelectedList
     * @return
     */
    public static SysFile createSugTemplateRoster(Ftp f, Sign sign, List<ExpertSelected> expertSelectedList, WorkProgram workProgram, String generalCounsel, String counselor) {
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("psnum", "3");
        dataMap.put("projectName", sign.getProjectname());
        dataMap.put("generalCounsel",  generalCounsel );
        dataMap.put("counselor",  counselor );
        dataMap.put("director", sign.getLeaderName());
        String leanderName = sign.getLeaderName() == null ? "" : sign.getLeaderName();
        dataMap.put("viceDirector", leanderName);
        String ministerName  = "";
        String projectDirector = "";
        if(workProgram != null){
            ministerName = workProgram.getMinisterName() == null ? "" : workProgram.getMinisterName();

            projectDirector = (workProgram.getMianChargeUserName() == null ? "" : workProgram.getMianChargeUserName())
                    + (workProgram.getSecondChargeUserName() == null ? "" : "   " + workProgram.getSecondChargeUserName().replaceAll(",", "   "));
        }

        dataMap.put("minister", ministerName);
        dataMap.put("projectDirector", projectDirector);

        String expertName = getExpertName(expertSelectedList);

        String reviewGroup = leanderName + "   " + ministerName;

        if (!"".equals(expertName) && expertName.length() > 0) {
            reviewGroup += "   " + expertName.replaceAll(",", "   ");
        }
        reviewGroup += "   " + projectDirector;
        dataMap.put("reviewGroup", reviewGroup);

        SysFile sysFile = TemplateUtil.createTemplate(
                f, sign.getSignid(), Constant.SysFileType.SIGN.getValue(),
                sign.getSignid(), Constant.SysFileType.TEMOLATE.getValue(),
                ProjectConstant.REVIEW_STATE_ENUM.STAGESUG.getZhCode(),
                Constant.Template.SUG_ROSTER.getKey(),
                Constant.Template.SUG_ROSTER.getValue().split("_")[1],
                Constant.Template.WORD_SUFFIX.getKey(),
                dataMap);
        return sysFile;
    }
    /**************end*************sug项目建议书****************************/

    /******************begin**************资金申请报告**********************************/
    /**
     * 评审意见
     *
     * @param sign
     * @return
     */
    public static SysFile createReportTemplateOpinion(Ftp f, Sign sign) {
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("projectName", sign.getProjectname());
        dataMap.put("docNum", "深投审〔" + DateUtils.converToString(new Date(), DateUtils.DATE_YEAR) + "〕");
        dataMap.put("explain", "xxxxx");
        dataMap.put("title1", "xxxxx");
        dataMap.put("content1", "xxxxx");
        dataMap.put("title2", "xxxxx");
        dataMap.put("content2", "xxxxx");
        dataMap.put("dateStr", DateUtils.converToString(new Date(), "yyyy年MM月dd日"));

        SysFile sysFile = TemplateUtil.createTemplate(
                f, sign.getSignid(), Constant.SysFileType.SIGN.getValue(),
                sign.getSignid(), Constant.SysFileType.TEMOLATE.getValue(),
                ProjectConstant.REVIEW_STATE_ENUM.STAGEREPORT.getZhCode(),
                Constant.Template.REPORT_OPINION.getKey(),
                Constant.Template.REPORT_OPINION.getValue().split("_")[1],
                Constant.Template.WORD_SUFFIX.getKey(),
                dataMap);
        return sysFile;
    }

    /**
     * 投资估算表
     *
     * @param sign
     * @return
     */
    public static SysFile createReportTemplateEstimate(Ftp f, Sign sign) {
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("gsnum", "2");
        dataMap.put("projectName", sign.getProjectname());
        SysFile sysFile = TemplateUtil.createTemplate(
                f, sign.getSignid(), Constant.SysFileType.SIGN.getValue(),
                sign.getSignid(), Constant.SysFileType.TEMOLATE.getValue(),
                ProjectConstant.REVIEW_STATE_ENUM.STAGEREPORT.getZhCode(),
                Constant.Template.REPORT_ESTIMATE.getKey(),
                Constant.Template.REPORT_ESTIMATE.getValue().split("_")[1],
                Constant.Template.EXCEL_SUFFIX.getKey(),
                dataMap);
        return sysFile;
    }

    /**
     * 评审组名单
     *
     * @param sign
     * @return
     */
    public static SysFile createReportTemplateRoster(Ftp f, Sign sign, List<ExpertSelected> expertSelectedList, WorkProgram workProgram, String generalCounsel, String counselor) {
        Map<String, Object> dataMap = new HashMap<>();

        dataMap.put("psnum", "3");
        dataMap.put("projectName", sign.getProjectname());
        dataMap.put("generalCounsel", generalCounsel );
        dataMap.put("counselor",  counselor );
        dataMap.put("director", sign.getLeaderName());
        String leanderName = sign.getLeaderName() == null ? "" : sign.getLeaderName();
        dataMap.put("viceDirector", leanderName);
        String ministerName  = "";
        String projectDirector = "";
        if(workProgram != null){
            ministerName = workProgram.getMinisterName() == null ? "" : workProgram.getMinisterName();

            projectDirector = (workProgram.getMianChargeUserName() == null ? "" : workProgram.getMianChargeUserName())
                    + (workProgram.getSecondChargeUserName() == null ? "" : "   " + workProgram.getSecondChargeUserName().replaceAll(",", "   "));
        }

        dataMap.put("minister", ministerName);
        dataMap.put("proojectDirector", projectDirector);

        String expertName = getExpertName(expertSelectedList);

        String reviewGroup = leanderName + "   " + ministerName;

        if (!"".equals(expertName) && expertName.length() > 0) {
            reviewGroup += "   " + expertName.replaceAll(",", "   ");
        }
        reviewGroup += "   " + projectDirector;
        dataMap.put("reviewGroup", reviewGroup);

        SysFile sysFile = TemplateUtil.createTemplate(
                f, sign.getSignid(), Constant.SysFileType.SIGN.getValue(),
                sign.getSignid(), Constant.SysFileType.TEMOLATE.getValue(),
                ProjectConstant.REVIEW_STATE_ENUM.STAGEREPORT.getZhCode(),
                Constant.Template.REPORT_ROSTER.getKey(),
                Constant.Template.REPORT_ROSTER.getValue().split("_")[1],
                Constant.Template.WORD_SUFFIX.getKey(), dataMap);
        return sysFile;
    }
/******************end**************资金申请报告**********************************/

    /********************end发文模板**************************************/


    /************************begin会前准备材料******************************************/

    /**
     * 生成签到表
     *
     * @param sign
     * @param workProgram
     * @return
     */
    public static SysFile createtTemplateSignIn(Ftp f, Sign sign, WorkProgram workProgram) {
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("projectName", sign.getProjectname());
        dataMap.put("dateStr", DateUtils.converToString(new Date(), "yyyy年MM月dd日"));
        dataMap.put("reviewStage", sign.getReviewstage());

        SysFile sysFile = TemplateUtil.createTemplate(
                f, sign.getSignid(), Constant.SysFileType.SIGN.getValue(),
                workProgram.getId(), Constant.SysFileType.MEETING.getValue() + "(" + workProgram.getBranchId() + ")", Constant.SysFileType.WORKPROGRAM.getValue(),
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
    public static SysFile createTemplateNotice(Ftp f, Sign sign, WorkProgram workProgram, User user, List<RoomBooking> rbList, List<User> secondUserList) {

        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("projectName", sign.getProjectname());
        dataMap.put("reviewStage", sign.getReviewstage());
        dataMap.put("builtcompanyName", sign.getBuiltcompanyName());
        //第一负责人
        String contactPerson = user.getDisplayName() == null ? "" : user.getDisplayName();
        String contactPersonTel = user.getUserPhone() == null ? "" : user.getUserPhone();
        //第二负责人 , 负责人总共三个，第一负责人排第一，联系电话显示两个
        if (secondUserList != null && secondUserList.size() > 0) {
            for (int i = 0; i < secondUserList.size() && i < 2; i++) {
                User secondUser = secondUserList.get(i);
                if (secondUser != null) {

                    contactPerson += secondUser.getDisplayName() == null ? "" : "," + secondUser.getDisplayName();

                }
            }
            if (secondUserList.get(0) != null) {
                contactPersonTel += secondUserList.get(0).getUserPhone() == null ? "" : "," + secondUserList.get(0).getUserPhone();
            }
        }
        dataMap.put("contactPerson", contactPerson);//联系人
        dataMap.put("contactPersonTel", contactPersonTel);//联系电话

        dataMap.put("dateStr", DateUtils.converToString(new Date(), "yyyy年MM月dd日"));

        //获得会议信息
        SysFile sysFile = new SysFile();
        if (rbList != null && rbList.size() > 0) {
            RoomBooking roomBooking = rbList.get(0);
            String[] str = roomBooking.getRbDate().split("-");
            String[] str2 = str[2].split("\\(");

            String meetTime = str[0] + "年" + str[1] + "月" + str2[0] + "日(" + str2[1]
                    + DateUtils.converToString(roomBooking.getBeginTime(), "HH:mm")
                    + "至"
                    + DateUtils.converToString(roomBooking.getEndTime(), "HH:mm");
            dataMap.put("meetTime", meetTime);

            dataMap.put("addressName", roomBooking.getAddressName()); //会议地点

            dataMap.put("contactPersonAddress", "福田区莲花支路公交大厦" + roomBooking.getAddressName().substring(0, 3));

            Date compareDate = DateUtils.converToDate("12:00", "HH:mm");

            //如果开始时间大于12点或结束时间小于12点默认是半天，其它情况默认是全天
            if (DateUtils.compareIgnoreSecond(DateUtils.converToDate(
                    DateUtils.converToString(roomBooking.getBeginTime(), "HH:mm"), "HH:mm"), compareDate) > 0
                    || DateUtils.compareIgnoreSecond(
                    DateUtils.converToDate(DateUtils.converToString(roomBooking.getEndTime(), "HH:mm"), "HH:mm"), compareDate) < 0) {
                dataMap.put("duration", "半天");

            } else {
                dataMap.put("duration", "全天");
            }

            sysFile = TemplateUtil.createTemplate(
                    f, sign.getSignid(), Constant.SysFileType.SIGN.getValue(),
                    workProgram.getId(), Constant.SysFileType.MEETING.getValue() + "(" + workProgram.getBranchId() + ")", Constant.SysFileType.WORKPROGRAM.getValue(),
                    Constant.Template.PROJECR_NOTICE.getKey(), Constant.Template.PROJECR_NOTICE.getValue(),
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
    public static List<SysFile> createTemplateMeeting(Ftp f, Sign sign, WorkProgram workProgram, List<RoomBooking> rbList) {
        List<SysFile> sysFileList = new ArrayList<>();
        SysFile sysFile = new SysFile();

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
                    sysFile = TemplateUtil.createTemplate(
                            f, sign.getSignid(), Constant.SysFileType.SIGN.getValue(),
                            workProgram.getId(), Constant.SysFileType.MEETING.getValue() + "(" + workProgram.getBranchId() + ")", Constant.SysFileType.WORKPROGRAM.getValue(),
                            Constant.Template.MEETING_AM.getKey(), Constant.Template.MEETING_AM.getValue(),
                            Constant.Template.WORD_SUFFIX.getKey(), dataMap);
                    sysFileList.add(sysFile);

                } else {

                    sysFile = TemplateUtil.createTemplate(
                            f, sign.getSignid(), Constant.SysFileType.SIGN.getValue(),
                            workProgram.getId(), Constant.SysFileType.MEETING.getValue() + "(" + workProgram.getBranchId() + ")", Constant.SysFileType.WORKPROGRAM.getValue(),
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
     * @param expertList
     * @return
     */
    public static SysFile createTemplateInvitation(Ftp f, Sign sign, WorkProgram workProgram, List<Expert> expertList, User user, List<RoomBooking> rbList, List<User> secondUserList) {
        Map<String, Object> dataMap = new HashMap<>();

        //第一负责人
        String contactPerson = user.getDisplayName() == null ? "" : user.getDisplayName();
        String contactPersonTel = user.getUserPhone() == null ? "" : user.getUserPhone();
        //第二负责人 , 负责人总共三个，第一负责人排第一，联系电话显示两个
        if (secondUserList != null && secondUserList.size() > 0) {
            for (int i = 0; i < secondUserList.size() && i < 2; i++) {
                User secondUser = secondUserList.get(i);
                if (secondUser != null) {

                    contactPerson += secondUser.getDisplayName() == null ? "" : "," + secondUser.getDisplayName();

                }
            }
            if (secondUserList.get(0) != null) {
                contactPersonTel += secondUserList.get(0).getUserPhone() == null ? "" : "," + secondUserList.get(0).getUserPhone();
            }
        }

        Date compareDate = DateUtils.converToDate("12:00", "HH:mm");

        List<String[]> resultList = new ArrayList<>();
        for (Expert expert : expertList) {
            String[] resultArr = new String[10];
            resultArr[0] = expert.getName();
            resultArr[1] = "男".equals(expert.getSex()) ? "先生" : "女士";
            resultArr[2] = sign.getProjectname();
            resultArr[3] = sign.getReviewstage();

            String meetTime = ""; //会议时间
            String meetRessadd = "";//会议地点
            if (rbList != null && rbList.size() > 0) {
                RoomBooking roomBooking = rbList.get(0);
                String[] str = roomBooking.getRbDate().split("-");
                String[] str2 = str[2].split("\\(");

                meetTime = str[0] + "年" + str[1] + "月" + str2[0] + "日(" + str2[1]
                        + DateUtils.converToString(roomBooking.getBeginTime(), "HH:mm")
                        + "至"
                        + DateUtils.converToString(roomBooking.getEndTime(), "HH:mm");

                meetRessadd = roomBooking.getAddressName();

                //如果开始时间大于12点或结束时间小于12点默认是半天，其它情况默认是全天
                if (DateUtils.compareIgnoreSecond(DateUtils.converToDate(
                        DateUtils.converToString(roomBooking.getBeginTime(), "HH:mm"), "HH:mm"), compareDate) > 0
                        || DateUtils.compareIgnoreSecond(
                        DateUtils.converToDate(DateUtils.converToString(roomBooking.getEndTime(), "HH:mm"), "HH:mm"), compareDate) < 0) {
                    dataMap.put("duration", "半天");

                } else {
                    dataMap.put("duration", "全天");
                }
            }
            resultArr[4] = meetTime;
            resultArr[5] = meetRessadd;
            resultArr[6] = contactPerson;
            resultArr[7] = contactPersonTel;
            resultArr[8] = "福田区莲花支路公交大厦" + meetRessadd.substring(0, 3);
            resultArr[9] = DateUtils.converToString(new Date(), "yyyy年MM月dd日");
            resultList.add(resultArr);
        }

        dataMap.put("resultList", resultList);
        dataMap.put("resultListSize", resultList == null ? 0 : resultList.size());
        SysFile sysFile = new SysFile();

        sysFile = TemplateUtil.createTemplate(
                f, sign.getSignid(), Constant.SysFileType.SIGN.getValue(),
                workProgram.getId(), Constant.SysFileType.MEETING.getValue() + "(" + workProgram.getBranchId() + ")", Constant.SysFileType.WORKPROGRAM.getValue(),
                Constant.Template.INVITATION.getKey(), Constant.Template.INVITATION.getValue(),
                Constant.Template.WORD_SUFFIX.getKey(), dataMap);

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
    public static SysFile createTemplateCompere(Ftp f, Sign sign, WorkProgram workProgram, List<Expert> expertList, String expertGl) {
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("projectName", sign.getProjectname()); //项目名称
        dataMap.put("reviewStage", sign.getReviewstage());//评审阶段
        dataMap.put("expertList", expertList);//专家列表
//        dataMap.put("mOrgName" , sign.getMaindeptName() == null ? "" : sign.getMaindeptName() );//主办处室
//        dataMap.put("aOrgName" , sign.getAssistdeptName() == null ? "" : sign.getAssistdeptName() );//协办处室
        dataMap.put("inviteUnitLeader", workProgram.getInviteUnitLeader() == null ? "" : workProgram.getInviteUnitLeader());//拟邀请单位及领导
        dataMap.put("builtcompanyName", sign.getBuiltcompanyName() == null ? "" : sign.getBuiltcompanyName());//建设单位
        dataMap.put("designcompanyName", sign.getDesigncompanyName() == null ? "" : sign.getDesigncompanyName());//编制单位
        dataMap.put("projectBackGround", workProgram.getProjectBackGround() == null ? "" : workProgram.getProjectBackGround()); //项目背景
        dataMap.put("buildSize", workProgram.getBuildSize() == null ? "" : workProgram.getBuildSize());//申报规模
        dataMap.put("buildContent", workProgram.getBuildContent() == null ? "" : workProgram.getBuildContent());//建设内容
        dataMap.put("appalyInvestment", workProgram.getAppalyInvestment() == null ? 0 : workProgram.getAppalyInvestment());//申报投资
        dataMap.put("mainPoint", workProgram.getMainPoint() == null ? "" : workProgram.getMainPoint());//重点问题
        dataMap.put("expertGL", expertGl);//专家组长
        //中心领导、部长、项目负责人、第二负责人
        String leaderName = workProgram.getLeaderName() == null ? "" : workProgram.getLeaderName();
        String ministerName = workProgram.getMinisterName() == null ? "" : "," + workProgram.getMinisterName();
        String mianChargeUserName = workProgram.getMianChargeUserName() == null ? "" : "," + workProgram.getMianChargeUserName();
        String secondChargeUserName = workProgram.getSecondChargeUserName() == null ? "" : "," + workProgram.getSecondChargeUserName();
        String reviewGroupmembers = leaderName + ministerName + mianChargeUserName + secondChargeUserName;
        dataMap.put("projectGroupMeber", reviewGroupmembers);

        SysFile sysFile = TemplateUtil.createTemplate(
                f, sign.getSignid(), Constant.SysFileType.SIGN.getValue(),
                workProgram.getId(), Constant.SysFileType.MEETING.getValue() + "(" + workProgram.getBranchId() + ")", Constant.SysFileType.WORKPROGRAM.getValue(),
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
    /*public static SysFile createTemplateAssist(Ftp f,Sign sign, WorkProgram workProgram, List<AssistPlanSign> apsList, AssistUnit assistUnit, Org org) {
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("workreviveStage", "");
        if (org != null) {
            dataMap.put("orgName", org.getName() == null ? "" : org.getName());
            dataMap.put("orgAddress", org.getOrgAddress() == null ? "" : org.getOrgAddress());
            dataMap.put("orgMLeader", org.getOrgMLeader() == null ? "" : org.getOrgMLeader());
            dataMap.put("orgPhone", org.getOrgPhone() == null ? "" : org.getOrgPhone());
            dataMap.put("orgFax", org.getOrgFax() == null ? "" : org.getOrgFax());
        }
        if (assistUnit != null) {
            dataMap.put("unitName", assistUnit.getUnitName() == null ? "" : assistUnit.getUnitName());
            dataMap.put("address", assistUnit.getAddress() == null ? "" : assistUnit.getAddress());
            dataMap.put("phoneNum", assistUnit.getPhoneNum() == null ? "" : assistUnit.getPhoneNum());
            dataMap.put("principalName", assistUnit.getPrincipalName() == null ? "" : assistUnit.getPrincipalName());
            dataMap.put("contactFax", assistUnit.getContactFax() == null ? "" : assistUnit.getContactFax());
        }
        dataMap.put("projectName", sign.getProjectname() == null ? "" : sign.getProjectname());
        if (!apsList.isEmpty()) {
            AssistPlanSign assistPlanSign = apsList.get(0);
            AssistPlan assistPlan = assistPlanSign.getAssistPlan();
            float assistDays = assistPlanSign.getAssistDays();
            Date reportTime = assistPlan.getReportTime();
            Date finishTime = DateUtils.addDay(reportTime, (int) assistDays);
            dataMap.put("finishTime", DateUtils.converToString(finishTime, "yyyy年MM月dd日")); //建设规模
            dataMap.put("estimateCost", assistPlanSign.getEstimateCost() == null ? "" : assistPlanSign.getEstimateCost()); //建设规模
            dataMap.put("assistCost", assistPlanSign.getAssistCost() == null ? 0 : assistPlanSign.getAssistCost()); //协审费用
        }
        dataMap.put("assistDeptUserName", sign.getAssistdeptName() == null ? "" : sign.getAssistdeptName()); //协办事处联系人
        dataMap.put("mainDeptUserName", sign.getMainDeptUserName() == null ? "" : sign.getMainDeptUserName()); //主办事处联系人

        SysFile sysFile = TemplateUtil.createTemplate(
                f,sign.getSignid(),  Constant.SysFileType.SIGN.getValue(),
                workProgram.getId(),Constant.SysFileType.MEETING.getValue(), Constant.SysFileType.WORKPROGRAM.getValue(),
                Constant.Template.ASSIST.getKey(), Constant.Template.ASSIST.getValue(),
                Constant.Template.WORD_SUFFIX.getKey(), dataMap);
        return sysFile;

    }*/

    /**
     * 专家评审意见书
     *
     * @param f
     * @param sign
     * @param workProgram
     * @return
     */
    public static SysFile createTemplateExpertReviewIdea(Ftp f, Sign sign, WorkProgram workProgram) {
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("projectName", sign.getProjectname());
        dataMap.put("reviewStage", sign.getReviewstage());
        SysFile sysFile = TemplateUtil.createTemplate(
                f, sign.getSignid(), Constant.SysFileType.SIGN.getValue(),
                workProgram.getId(), Constant.SysFileType.MEETING.getValue() + "(" + workProgram.getBranchId() + ")", Constant.SysFileType.WORKPROGRAM.getValue(),
                Constant.Template.EXPERTREVIEWIDEA.getKey(), Constant.Template.EXPERTREVIEWIDEA.getValue(),
                Constant.Template.WORD_SUFFIX.getKey(), dataMap);
        return sysFile;
    }

    /**
     * 相关单位会议通知
     * @param f
     * @param sign
     * @param workProgram
     * @param user
     * @param rbList
     * @param secondUserList
     * @return
     */
    public static SysFile createTemplateUnitNotice(Ftp f, Sign sign, WorkProgram workProgram, User user, List<RoomBooking> rbList, List<User> secondUserList) {

        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("projectName", sign.getProjectname());
        dataMap.put("reviewStage", sign.getReviewstage());
        String inviteUnitLeader = workProgram.getInviteUnitLeader() == null ? "" : workProgram.getInviteUnitLeader();

        String[] inviteUnitLeaderArr = inviteUnitLeader.split("[;,；，、]");

        dataMap.put("inviteUnitLeaderArr", inviteUnitLeaderArr );
        dataMap.put("inviteUnitLeaderArrSize" , inviteUnitLeaderArr == null ? 0 : inviteUnitLeaderArr.length);
        //第一负责人
        String contactPerson = user.getDisplayName() == null ? "" : user.getDisplayName();
        String contactPersonTel = user.getUserPhone() == null ? "" : user.getUserPhone();
        //第二负责人 , 负责人总共三个，第一负责人排第一，联系电话显示两个
        if (secondUserList != null && secondUserList.size() > 0) {
            for (int i = 0; i < secondUserList.size() && i < 2; i++) {
                User secondUser = secondUserList.get(i);
                if (secondUser != null) {

                    contactPerson += secondUser.getDisplayName() == null ? "" : "," + secondUser.getDisplayName();

                }
            }
            if (secondUserList.get(0) != null) {
                contactPersonTel += secondUserList.get(0).getUserPhone() == null ? "" : "," + secondUserList.get(0).getUserPhone();
            }
        }
        dataMap.put("contactPerson", contactPerson);//联系人
        dataMap.put("contactPersonTel", contactPersonTel);//联系电话

        dataMap.put("dateStr", DateUtils.converToString(new Date(), "yyyy年MM月dd日"));

        //获得会议信息
        SysFile sysFile = new SysFile();
        if (rbList != null && rbList.size() > 0) {
            RoomBooking roomBooking = rbList.get(0);
            String[] str = roomBooking.getRbDate().split("-");
            String[] str2 = str[2].split("\\(");

            String meetTime = str[0] + "年" + str[1] + "月" + str2[0] + "日(" + str2[1]
                    + DateUtils.converToString(roomBooking.getBeginTime(), "HH:mm")
                    + "至"
                    + DateUtils.converToString(roomBooking.getEndTime(), "HH:mm");
            dataMap.put("meetTime", meetTime);

            dataMap.put("addressName", roomBooking.getAddressName()); //会议地点

            dataMap.put("contactPersonAddress", "福田区莲花支路公交大厦" + roomBooking.getAddressName().substring(0, 3));

            Date compareDate = DateUtils.converToDate("12:00", "HH:mm");

            //如果开始时间大于12点或结束时间小于12点默认是半天，其它情况默认是全天
            if (DateUtils.compareIgnoreSecond(DateUtils.converToDate(
                    DateUtils.converToString(roomBooking.getBeginTime(), "HH:mm"), "HH:mm"), compareDate) > 0
                    || DateUtils.compareIgnoreSecond(
                    DateUtils.converToDate(DateUtils.converToString(roomBooking.getEndTime(), "HH:mm"), "HH:mm"), compareDate) < 0) {
                dataMap.put("duration", "半天");

            } else {
                dataMap.put("duration", "全天");
            }

            sysFile = TemplateUtil.createTemplate(
                    f, sign.getSignid(), Constant.SysFileType.SIGN.getValue(),
                    workProgram.getId(), Constant.SysFileType.MEETING.getValue() + "(" + workProgram.getBranchId() + ")", Constant.SysFileType.WORKPROGRAM.getValue(),
                    Constant.Template.UNIT_NOTICE.getKey(), Constant.Template.UNIT_NOTICE.getValue(),
                    Constant.Template.WORD_SUFFIX.getKey(), dataMap);
        }
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
    public static SysFile subjectStudyNovice(Ftp f, Sign sign, WorkProgram workProgram, User user, List<RoomBooking> rbList) {
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("projectName", sign.getProjectname());
        dataMap.put("reviewStage", sign.getReviewstage());
        dataMap.put("sendFileUnit", "深圳市人民检察院");
        dataMap.put("studyBeginTime", DateUtils.converToString(workProgram.getStudyBeginTime(), "yyyy年MM月dd日"));
        dataMap.put("contactPerson", user.getDisplayName());//联系人
        dataMap.put("contactPersonTel", user.getUserPhone());//联系电话
        dataMap.put("contactPersonFax", "83642081");//传真
        dataMap.put("contactPersonAddress", COMPANY_NAME);
        dataMap.put("dateStr", DateUtils.converToString(new Date(), "yyyy年MM月dd日"));
        //获得会议信息
        SysFile sysFile = new SysFile();
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
            sysFile = TemplateUtil.createTemplate(
                    f, sign.getSignid(), Constant.SysFileType.SIGN.getValue(),
                    workProgram.getId(), Constant.SysFileType.SUBJECT_STUDY.getValue(), Constant.SysFileType.SUBJECT_STUDY.getValue(),
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
    public static SysFile subjectStudySignIn(Ftp f, Sign sign, WorkProgram workProgram) {
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("dateStr", DateUtils.converToString(new Date(), "yyyy年MM月dd日"));
        SysFile sysFile = TemplateUtil.createTemplate(
                f, sign.getSignid(), Constant.SysFileType.SIGN.getValue(),
                workProgram.getId(), Constant.SysFileType.SUBJECT_STUDY.getValue(), Constant.SysFileType.SUBJECT_STUDY.getValue(),
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
    public static SysFile subjectStudySignature(Ftp f, Sign sign, WorkProgram workProgram) {
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("projectName", sign.getProjectname());
        SysFile sysFile = TemplateUtil.createTemplate(
                f, sign.getSignid(), Constant.SysFileType.SIGN.getValue(),
                workProgram.getId(), Constant.SysFileType.SUBJECT_STUDY.getValue(), Constant.SysFileType.SUBJECT_STUDY.getValue(),
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
    public static SysFile subjectStudyInvitation(Ftp f, Sign sign, WorkProgram workProgram, Expert expert, User user, List<RoomBooking> rbList) {
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
        SysFile sysFile = new SysFile();
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
            sysFile = TemplateUtil.createTemplate(
                    f, sign.getSignid(), Constant.SysFileType.SIGN.getValue(),
                    workProgram.getId(), Constant.SysFileType.SUBJECT_STUDY.getValue(), Constant.SysFileType.SUBJECT_STUDY.getValue(),
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
    public static List<SysFile> subjectStudyMeeting(Ftp f, Sign sign, WorkProgram workProgram, List<RoomBooking> rbList) {
        List<SysFile> sysFileList = new ArrayList<>();
        SysFile sysFile = new SysFile();
        Map<String, Object> dataMap = new HashMap<>();
        Date compareDate = DateUtils.converToDate("12:00", "HH:mm");
        if (!rbList.isEmpty()) {
            for (RoomBooking roomBooking : rbList) {
                dataMap.put("rbDate", roomBooking.getRbDate());//会议日期显示星期
                dataMap.put("beginTime", DateUtils.converToString(roomBooking.getBeginTime(), "HH:mm"));//会议开始时间
                dataMap.put("endTime", DateUtils.converToString(roomBooking.getEndTime(), "HH:mm"));//会议结束时间
                dataMap.put("addressName", roomBooking.getAddressName());
                if (DateUtils.compareIgnoreSecond(roomBooking.getBeginTime(), compareDate) == 1) {
                    sysFile = TemplateUtil.createTemplate(
                            f, sign.getSignid(), Constant.SysFileType.SIGN.getValue(),
                            workProgram.getId(), Constant.SysFileType.SUBJECT_STUDY.getValue(), Constant.SysFileType.SUBJECT_STUDY.getValue(),
                            Constant.Template.SUBJECT_STUDY_MEETINGAM.getKey(), Constant.Template.SUBJECT_STUDY_MEETINGAM.getValue(),
                            Constant.Template.WORD_SUFFIX.getKey(), dataMap);
                    sysFileList.add(sysFile);

                } else {

                    sysFile = TemplateUtil.createTemplate(
                            f, sign.getSignid(), Constant.SysFileType.SIGN.getValue(),
                            workProgram.getId(), Constant.SysFileType.SUBJECT_STUDY.getValue(), Constant.SysFileType.SUBJECT_STUDY.getValue(),
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
     * 处理数值，保留两位小数
     * @param obj
     * @return
     */
    public static Object dealNumber(Object obj){

        Object result = null ;
        if(Validate.isObject(obj)) {
            double dValue = Double.valueOf(obj.toString());
            double d1 = Double.parseDouble(String.format("%.2f", dValue));
            if (d1 > 0) {
                result = d1;
            } else {
                if (dValue > 0) {
                    int i = 0, j = 0;
                    int cs = 10;
                    double d = dValue;
                    while (i < 2) {
                        j++;
                        if ((int) (d) == 0) {
                            cs *= 10;
                            d *= 10;
                        } else {
                            i++;
                        }
                    }
                    result = Double.parseDouble(String.format("%." + j + "f", dValue));
                } else {
                    result = 0;
                }
            }
        }else{
            result = 0 ;
        }
        return result;
    }


    /**
     * 生成月报简报模板
     *
     * @return
     */
    public static File createMonthTemplate(Map<String , Object> paramsMap ) {
        Map<String, Object> dataMap = new HashMap<>();

        MonthlyNewsletterDto monthlyNewsletterDto = (MonthlyNewsletterDto) paramsMap.get("monthlyNewsletterDto");
        Integer signCount = (Integer) paramsMap.get("signCount");
        Integer reviewCount = (Integer) paramsMap.get("reviewCount");
        List<ProReviewConditionDto> proReviewConditionDtoList = (List<ProReviewConditionDto>) paramsMap.get("proReviewConditionDtoList");
        List<ProReviewConditionDto> proReviewConditionDtoAllList = (List<ProReviewConditionDto>) paramsMap.get("proReviewConditionDtoAllList");
        List<ProReviewConditionDto> proReviewConditionByTypeList = (List<ProReviewConditionDto>) paramsMap.get("proReviewConditionByTypeAllList");
        Integer totalNum = (Integer) paramsMap.get("totalNum");
        ProReviewConditionDto proReviewConditionCur = (ProReviewConditionDto) paramsMap.get("proReviewConditionCur");
        ProReviewConditionDto proReviewConditionSum = (ProReviewConditionDto) paramsMap.get("proReviewConditionSum");

        ProReviewConditionDto proReviewConditionSumLast = (ProReviewConditionDto) paramsMap.get("proReviewConditionSumLast");

        Map<String, List<ProReviewConditionDto>> proReviewCondDetailMap = (Map<String, List<ProReviewConditionDto>>) paramsMap.get("proReviewCondDetailMap");
        Integer[] proCountArr = (Integer[]) paramsMap.get("proCountArr");
        ProReviewConditionDto acvanceCurDto = (ProReviewConditionDto) paramsMap.get("acvanceCurDto");
        ProReviewConditionDto acvanceTotalDto = (ProReviewConditionDto) paramsMap.get("acvanceTotalDto");
        ProReviewConditionDto backDispatchTotalCur = (ProReviewConditionDto) paramsMap.get("backDispatchTotalCur");
        List<ProReviewConditionDto> backDispatchList = (List<ProReviewConditionDto>) paramsMap.get("backDispatchList");
        Map<String , List<ProReviewConditionDto>> attachmentContentMap = (Map<String, List<ProReviewConditionDto>>) paramsMap.get("attachmentContentList");
        Map<String , List<ProReviewConditionDto>> attachmentContentAllMap = (Map<String, List<ProReviewConditionDto>>) paramsMap.get("attachmentContentAllList");
        List<ProReviewConditionDto> backDispatchListAll = (List<ProReviewConditionDto>) paramsMap.get("backDispatchListAll");
        //附件一和附件二的内容
        dataMap.put("attachmentContentMap" , attachmentContentMap);
        dataMap.put("attachmentContentAllMap" , attachmentContentAllMap);

        //// 本月专家评审会情况
        String[] expertReviewMeeting = (String[]) paramsMap.get("expertReviewMeeting");

        //报告年度
        dataMap.put("reportMultiyear", monthlyNewsletterDto.getReportMultiyear());
        //报告月份
        dataMap.put("theMonths", monthlyNewsletterDto.getTheMonths());
        if (Integer.parseInt(monthlyNewsletterDto.getTheMonths()) < 10) {
            dataMap.put("theNthMonths", NumUtils.NumberToChn(Integer.parseInt(monthlyNewsletterDto.getTheMonths())));
        } else {
            String temp = NumUtils.NumberToChn(Integer.parseInt(monthlyNewsletterDto.getTheMonths()));
            temp = temp.substring(1, temp.length());
            dataMap.put("theNthMonths", temp);
        }

        //统计退文金额
        if(null != backDispatchList && backDispatchList.size() > 0 ){
            BigDecimal twTotal = new BigDecimal(0);
            for(ProReviewConditionDto p : backDispatchList){
                twTotal =  twTotal.add(p.getDeclareValue());
            }
            dataMap.put("twTotal" , twTotal);
        }

        //todo:初始化参数
        dataMap.put("signTotal", signCount != null ? signCount : 0);
        dataMap.put("proTotal", reviewCount != null ? reviewCount : 0);
        dataMap.put("declareTotal", dealNumber(proReviewConditionCur.getDeclareValue()));
        dataMap.put("authorizeTotal", dealNumber(proReviewConditionCur.getAuthorizeValue()));
        dataMap.put("ljhjTotal", dealNumber(proReviewConditionCur.getLjhj()));
        dataMap.put("hjlTotal",dealNumber(proReviewConditionCur.getHjl()));
        dataMap.put("backTotal", dealNumber(backDispatchTotalCur.getDeclareValue()));
        dataMap.put("backDispatchList", backDispatchList);
        dataMap.put("excludeBackPro", proReviewConditionCur.getProCount() != null ? proReviewConditionCur.getProCount() + "" : 0);
        dataMap.put("proAllTotal", proReviewConditionSum.getProCount() != null ? proReviewConditionSum.getProCount() : 0);
        dataMap.put("declareAllTotal", dealNumber(proReviewConditionSum.getDeclareValue()));
        dataMap.put("authorizeAllTotal", dealNumber(proReviewConditionSum.getAuthorizeValue()));
        dataMap.put("ljhjAllTotal", dealNumber(proReviewConditionSum.getLjhj()));
        dataMap.put("hjlAllTotal",  dealNumber(proReviewConditionSum.getHjl()) + "%");
        //dataMap.put("reviewCount",reviewCount!=null?reviewCount:0);//评审会次数
//        List<ProReviewConditionDto> proReviewCondition = proReviewConditionDtoList;
        List<ProReviewConditionDto> proReviewConditionAll = proReviewConditionDtoAllList;
        dataMap.put("proReviewConditionList", proReviewConditionDtoList);
        dataMap.put("proReviewConditionAllList", proReviewConditionAll);
        dataMap.put("proReviewCondDetailMap", proReviewCondDetailMap);
        String curYear = DateUtils.converToString(new Date(), null).split("-")[0];
        String curMonth = DateUtils.converToString(new Date(), null).split("-")[1];
        String dayStr = DateUtils.converToString(new Date(), null).split("-")[2];
        dataMap.put("curDay", dayStr);
        dataMap.put("curMonth", curMonth);
        dataMap.put("beginMonth", monthlyNewsletterDto.getStaerTheMonths());
        dataMap.put("curYear", curYear);

        if(null != backDispatchListAll && backDispatchListAll.size() > 0  ){
            int twProjectAllCount = 0 ;
            double twzjAll = 0 ;
            for(ProReviewConditionDto p : backDispatchListAll){
                twzjAll += p.getDeclareValue() == null ? 0 : new Double( p.getDeclareValue().toString());
            }

            dataMap.put("twProjectAllCount" , backDispatchListAll.size());
            dataMap.put("twzjAll" , dealNumber(twzjAll/10000));
        }

        double thisTotal = proReviewConditionSum.getProCount() != null ? new Double( proReviewConditionSum.getProCount().toString()) : 0;
        double lastTotal = proReviewConditionSumLast.getProCount() != null ? new Double( proReviewConditionSumLast.getProCount().toString()) : 0;
        double thisDeclare = new Double( dealNumber(proReviewConditionSum.getDeclareValue()).toString());
        double lastDeclare = new Double( dealNumber(proReviewConditionSumLast.getDeclareValue()).toString());

        double t = (thisTotal - lastTotal)/lastTotal;
        dataMap.put("proAllTotalLast", lastTotal == 0 ? 100 : dealNumber(t));
        dataMap.put("declareAllTotalLast", lastDeclare == 0 ? 100 : new Double( dealNumber((thisDeclare - lastDeclare)/lastDeclare).toString()) * 100 );

        dataMap.put("last1", (thisTotal - lastTotal));
        dataMap.put("last2", (thisDeclare - lastDeclare ));

        String[] reviewStage = {"xmjys-项目建议书", "kxxyj-可行性研究报告", "xmgs-项目概算", "zjsq-资金申请报告", "qt-其它", "jksb-进口设备", "sbqdgc-设备清单（国产）", "sbqdjk-设备清单（进口）" , "djfm-登记赋码"};
        String[] reviewStageTotal = { "djfmTotal-登记赋码","xmjysTotal-项目建议书", "kxxyjTotal-可行性研究报告", "xmgsTotal-项目概算", "zjsqTotal-资金申请报告", "qtTotal-其它", "jksbTotal-进口设备", "sbqdgcTotal-设备清单（国产）", "sbqdjkTotal-设备清单（进口）" , "tqjrTotal-提前介入" };
        String[] projectType = {"projectTypeA-市政工程", "projectTypeHouse-房建工程", "projectTypeInfo-信息工程", "projectTypeBuy-设备采购", "projectTypeOther-其它"};
        boolean flag = true;

        Object o = dealNumber(proReviewConditionCur.getAuthorizeValue());
        dataMap.put("declareTotalStr", new Double(dealNumber(proReviewConditionCur.getDeclareValue()).toString())== 0 ? "---" : dealNumber(proReviewConditionCur.getDeclareValue()));
        dataMap.put("authorizeTotalStr", new Double(dealNumber(proReviewConditionCur.getAuthorizeValue()).toString()) == 0 ? "---" : dealNumber(proReviewConditionCur.getAuthorizeValue()));
        dataMap.put("ljhjTotalStr", new Double(dealNumber(proReviewConditionCur.getLjhj()).toString()) == 0 ? "---" : dealNumber(proReviewConditionCur.getLjhj()));
        dataMap.put("hjlTotalStr", new Double(dealNumber(proReviewConditionCur.getHjl()).toString()) == 0 ? "---" : dealNumber(proReviewConditionCur.getHjl()) + "%");
        dataMap.put("declareAllTotalStr", new Double(dealNumber(proReviewConditionSum.getDeclareValue()).toString()) == 0 ? "---" : dealNumber(proReviewConditionSum.getDeclareValue()));
        dataMap.put("authorizeAllTotalStr",new Double(dealNumber(proReviewConditionSum.getAuthorizeValue()).toString()) == 0 ? "---" : dealNumber(proReviewConditionSum.getAuthorizeValue()));
        dataMap.put("ljhjAllTotalStr", new Double(dealNumber(proReviewConditionSum.getLjhj()).toString()) == 0 ? "---" : dealNumber(proReviewConditionSum.getLjhj()));
        dataMap.put("hjlAllTotalStr",  new Double(dealNumber(proReviewConditionSum.getHjl()).toString()) == 0 ? "---" : dealNumber(proReviewConditionSum.getHjl()) + "%");
        //当月月报
        if (proReviewConditionDtoList.size() > 0) {
            int xmzs = 0;
            double sbztz = 0;
            double shztz = 0;
            double tz = 0 , tzhjl = 0;

            for (int j = 0; j < proReviewConditionDtoList.size(); j++) {
                for (int i = 0; i < reviewStage.length; i++) {
                    String[] tempArr = reviewStage[i].split("-");
                    if (tempArr[1].equals(proReviewConditionDtoList.get(j).getReviewStage())) {
                        if (null == proReviewConditionDtoList.get(j).getIsadvanced() || "0".equals(proReviewConditionDtoList.get(j).getIsadvanced())) {
                            String temp = tempArr[1];
                            if(tempArr[1] != null && (ProjectConstant.REVIEW_STATE_ENUM.STAGEBUDGET.getZhCode()).equals(tempArr[1])){
                                temp = "完成初步涉及概算审核";
                            }else{
                                temp = "完成" + temp + "评审";
                            }
                            double ljhj = new Double(dealNumber(proReviewConditionDtoList.get(j).getLjhj()).toString());
                            double hjl = new Double(dealNumber(proReviewConditionDtoList.get(j).getHjl()).toString());
                            // "qtTotal-其它", "jksbTotal-进口设备",
                            boolean flag2 = false;
                            if("qt".equals(tempArr[0]) || "jksb".equals(tempArr[0])){
                                flag2 = true;
                                xmzs +=  (proReviewConditionDtoList.get(j).getProCount() != null ? new Integer( (proReviewConditionDtoList.get(j).getProCount()).toString() ): 0);
                                sbztz += new Double( dealNumber(proReviewConditionDtoList.get(j).getDeclareValue()).toString());
                                shztz += new Double( dealNumber(proReviewConditionDtoList.get(j).getAuthorizeValue()).toString());
                                tz += ljhj;
                                tzhjl += hjl;

                            }else{
                                dataMap.put(tempArr[0], temp + (proReviewConditionDtoList.get(j).getProCount() != null ? proReviewConditionDtoList.get(j).getProCount() : 0) + "项，" +
                                        "申报总投资" + ( dealNumber(proReviewConditionDtoList.get(j).getDeclareValue()))
                                        + "亿元，审核后总投资 " + ( dealNumber(proReviewConditionDtoList.get(j).getAuthorizeValue())) + "亿元，" +
                                        "累计净核" + (ljhj > 0 ? "减" : "增") + "投资 " + (ljhj)
                                        + "亿元，核" + (hjl > 0 ? "减" : "增") + "率" + (hjl) + " %。");
                            }
                            if(flag2){
                                tzhjl = new Double(dealNumber(tz / sbztz  * 100).toString());

                                dataMap.put("qt", "完成其他评审" + xmzs + "项（包含进口设备），申报总投资" + sbztz
                                        + "亿元，审核后总投资 " + shztz + "亿元，" +
                                        "累计净核" + (tz > 0 ? "减" : "增") + "投资 " + (tz)
                                        + "亿元，核" + (tzhjl > 0 ? "减" : "增") + "率" + (tzhjl) + " %。");

                            }
                            break;
                        }
                    }
                }

            }
        }
        if (null != acvanceCurDto && acvanceCurDto.getProCount().intValue() > 0) {
            double ljhj = new Double(dealNumber(acvanceCurDto.getLjhj()).toString());
            double hjl = new Double(dealNumber(acvanceCurDto.getHjl()).toString());
            dataMap.put("tqjr", "完成提前介入项目评审" + (acvanceCurDto.getProCount() != null ? acvanceCurDto.getProCount() : 0) + "项," +
                    "申报总投资" + ( dealNumber(acvanceCurDto.getDeclareValue())) +
                    "亿元,审核后总投资" + ( dealNumber(acvanceCurDto.getAuthorizeValue()))
                    + "累计净核" + (ljhj > 0 ? "减" : "增") + "投资 " + (ljhj)
                    + "亿元，核" + (hjl > 0 ? "减" : "增") + "率" + (hjl) + " %。");
        }
        //截止至当前月月报
        int reviewTotal = 0;
        String proCent = "";
        boolean isAdvanced = false; //是否提前介入
        if (proReviewConditionDtoAllList.size() > 0) {
            int xmzsAll = 0;
            double sbztzAll = 0;
            double shztzAll = 0;
            double tzAll = 0 , tzhjlAll = 0;
            for (int j = 0; j < proReviewConditionDtoAllList.size(); j++) {
                for (int i = 0; i < reviewStageTotal.length; i++) {
                    String[] tempArr = reviewStageTotal[i].split("-");
                    //String [] tempArrTemp = reviewStageTotalTemp[i].split("-");
                    if (tempArr[1].equals(proReviewConditionDtoAllList.get(j).getReviewStage())) {
                        if (null == proReviewConditionDtoAllList.get(j).getIsadvanced() || "0".equals(proReviewConditionDtoAllList.get(j).getIsadvanced())) {
                            if (reviewTotal != 0) {
                                proCent = String.format("%.2f", (proReviewConditionDtoAllList.get(j).getProCount().floatValue() / (float) reviewTotal) * 100) + "%";
                            }
                            String temp = tempArr[1];
                            if(tempArr[1] != null && (ProjectConstant.REVIEW_STATE_ENUM.STAGEBUDGET.getZhCode()).equals(tempArr[1])){
                                temp = "完成初步涉及概算审核";
                            }else{
                                temp = "完成" + temp + "评审";
                            }
                            double ljhj = new Double(dealNumber(proReviewConditionDtoAllList.get(j).getLjhj()).toString());
                            double hjl = new Double(dealNumber(proReviewConditionDtoAllList.get(j).getHjl()).toString());
                          // "qtTotal-其它", "jksbTotal-进口设备",
                          boolean flag3 = false;
                           if("qtTotal".equals(tempArr[0]) || "jksbTotal".equals(tempArr[0])){
                               flag3 = true;
                               xmzsAll +=  (proReviewConditionDtoAllList.get(j).getProCount() != null ? new Integer( (proReviewConditionDtoAllList.get(j).getProCount()).toString() ): 0);
                               sbztzAll += new Double( dealNumber(proReviewConditionDtoAllList.get(j).getDeclareValue()).toString());
                               shztzAll += new Double( dealNumber(proReviewConditionDtoAllList.get(j).getAuthorizeValue()).toString());
                               tzAll += ljhj;
                               tzhjlAll += hjl;


                           }else{
                               dataMap.put(tempArr[0], temp + (proReviewConditionDtoAllList.get(j).getProCount() != null ? proReviewConditionDtoAllList.get(j).getProCount() : 0) + "项，" +
                                       "申报总投资" + ( dealNumber(proReviewConditionDtoAllList.get(j).getDeclareValue()))
                                       + "亿元，审核后总投资 " + ( dealNumber(proReviewConditionDtoAllList.get(j).getAuthorizeValue())) + "亿元，" +
                                       "累计净核" + (ljhj > 0 ? "减" : "增") + "投资 " + (ljhj)
                                       + "亿元，核" + (hjl > 0 ? "减" : "增") + "率" + (hjl) + " %。");
                           }
                           if(flag3){
                               tzhjlAll = new Double(dealNumber(tzAll / sbztzAll  * 100).toString());
                               dataMap.put("qtTotal",  "完成其它项目审核" + xmzsAll  + "项（含进口设备），" +
                                       "申报总投资" + sbztzAll
                                       + "亿元，审核后总投资 " + shztzAll + "亿元，" +
                                       "累计净核" + (tzAll > 0 ? "减" : "增") + "投资 " + (tzAll)
                                       + "亿元，核" + (tzhjlAll > 0 ? "减" : "增") + "率" + (tzhjlAll) + " %。");
                           }


                            break;
                        }
                    }
                }
            }
            if (isAdvanced) {//提前介入
                if (null != acvanceTotalDto && acvanceTotalDto.getProCount().intValue() > 0) {
                    double ljhj = new Double(dealNumber(acvanceCurDto.getLjhj()).toString());
                    double hjl = new Double(dealNumber(acvanceCurDto.getHjl()).toString());
                    dataMap.put("tqjrTotal", "另，完成提前介入项目评审 " + (acvanceTotalDto.getProCount() != null ? acvanceTotalDto.getProCount() : 0) + "项，" +
                            "申报总投资" + ( dealNumber(acvanceTotalDto.getDeclareValue()))
                            + "亿元，审核后总投资" + ( dealNumber(acvanceTotalDto.getAuthorizeValue())) + "亿元，"
                            + "累计净核" + (ljhj > 0 ? "减" : "增") + "投资 " + (ljhj)
                            + "亿元，核" + (hjl > 0 ? "减" : "增") + "率" + (hjl) + " %。");
                }
            }
        }

        //项目阶段
        String projectStage = "";
        if(null != proReviewConditionDtoAllList && proReviewConditionDtoAllList.size() > 0 ){
            int count = 0 ;
            double precent = 0;
            boolean f = false;

            for (int j = 0; j < reviewStageTotal.length; j++) {
                String[] tempArr = reviewStageTotal[j].split("-");
                String temp = tempArr[1];
                if(tempArr[1] != null && (ProjectConstant.REVIEW_STATE_ENUM.STAGEBUDGET.getZhCode()).equals(tempArr[1])){
                    temp = "完成初步涉及概算审核";
                }else{
                    temp = "完成" + temp + "评审";
                }
                for (int i = 0; i < proReviewConditionDtoAllList.size(); i++) {
                    if (tempArr[1].equals(proReviewConditionDtoAllList.get(i).getReviewStage())) {
                        if (totalNum != 0) {
                            proCent = String.format("%.2f", (proReviewConditionDtoAllList.get(i).getProCount().floatValue() / totalNum.floatValue() * 100)) ;
                        } else {
                            proCent = "0";
                        }

                        //进口设备和其它归为一类
                        if("qtTotal".equals(tempArr[0]) || "jksbTotal".equals(tempArr[0])){
                            count += new Integer(proReviewConditionDtoAllList.get(i).getProCount().toString());
                            precent += new Double(proCent);
                            f = true;
                        }else{
                            projectStage += temp + "类项目" + proReviewConditionDtoAllList.get(i).getProCount() + "项，占项目总数的" + proCent + "%;";
                        }
//
//                        if (i != (proReviewConditionDtoAllList.size() - 1)) {
//
//                        } else {
//                            projectStage += tempArr[1] + "类项目" + proReviewConditionDtoAllList.get(i).getProCount() + "项，占项目总数的" + proCent + "。";
//                        }
                        break;
                    }
                }
            }
            if(f){
                projectStage += "其它类项目" + count + "项，占项目总数的" + dealNumber(proCent) + "%。";
            }else{
                projectStage = projectStage.substring( 0 , projectStage.length() - 1) + "。";
            }
        }
        dataMap.put("projectStage" , projectStage);
        //项目类别
        String projectTypeItem = "";
        if (proReviewConditionByTypeList.size() > 0) {
            for (int i = 0; i < proReviewConditionByTypeList.size(); i++) {
                for (int j = 0; j < projectType.length; j++) {
                    String[] tempArr = projectType[j].split("-");
                    if (tempArr[1].equals(proReviewConditionByTypeList.get(i).getProjectType())) {
                        if (totalNum != 0) {
                            proCent = String.format("%.2f", (proReviewConditionByTypeList.get(i).getProjectTypeCount().floatValue() / totalNum.floatValue() * 100)) + "%";
                        } else {
                            proCent = "0%";
                        }

                        if (i != (proReviewConditionByTypeList.size() - 1)) {
                            projectTypeItem += tempArr[1] + "类项目" + proReviewConditionByTypeList.get(i).getProjectTypeCount() + "项，占项目总数的" + proCent + ";";
                        } else {
                            projectTypeItem += tempArr[1] + "类项目" + proReviewConditionByTypeList.get(i).getProjectTypeCount() + "项，占项目总数的" + proCent + "。";
                        }
                        break;
                    }
                }
            }
        }
        dataMap.put("projectTypeItem", projectTypeItem);
        //投资金额
      /*  proCentf = 100f;
        temp1 = 0f;*/
        for (int i = 0; i < proCountArr.length - 1; i++) {
            dataMap.put("proCount" + (i + 1), proCountArr[i]);
            if (proCountArr[i] != 0 && proCountArr[proCountArr.length - 1] != 0) {
                double temp = (double) proCountArr[i] / (double) proCountArr[proCountArr.length - 1] * 100;
                String result = String.format("%.2f", temp) + "%";
                dataMap.put("proCountCent" + (i + 1), result);
            } else {
                dataMap.put("proCountCent" + (i + 1), 0 + "%");
            }
        }
        String showName = Constant.Template.MONTH_REPORT.getValue() + Constant.Template.WORD_SUFFIX.getKey();
        String path = SysFileUtil.getUploadPath();
        String relativeFileUrl = SysFileUtil.generatRelativeUrl(path, Constant.Template.MONTH_REPORT.getValue(), null, null, showName);

        //设置被退文项目
        if (backDispatchList != null && backDispatchList.size() > 0 ) {
            String twProjects = "";
            for (ProReviewConditionDto p : backDispatchList) {
                if(Validate.isString(twProjects)){
                    twProjects += "、";
                }
                twProjects += p.getProjectName();
            }

            dataMap.put("twProjectList" , twProjects == null ? "" : twProjects);
        }

        //记录专家评审场次
        if(expertReviewMeeting != null){

            String expertReviewMeetingMsg = "本月共组织召开" + expertReviewMeeting[0] + "等专家评审会" + expertReviewMeeting[1] + "场。";

            dataMap.put("expertReviewMeeting" , expertReviewMeetingMsg);
        }

        File docFile = TemplateUtil.createDoc(dataMap, Constant.Template.MONTH_REPORT.getKey(), path + File.separator + relativeFileUrl);
       /* String filePath = docFile.getAbsolutePath();
        filePath = filePath.substring(0, filePath.lastIndexOf("."))+"monthRep" + Constant.Template.WORD_SUFFIX.getKey();
        OfficeConverterUtil.wordSaveAsWord(docFile.getAbsolutePath(),filePath); //另存为doc
        File realFile = new File(filePath);*/
        return docFile;
    }
}