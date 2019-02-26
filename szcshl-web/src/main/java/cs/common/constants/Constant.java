package cs.common.constants;

/**
 * 系统常亮
 *
 * @author ldm
 * @author MCL
 * @date 2017年7月28日 下午2:12:53
 */
public class Constant {
    /**
     * 超级管理员账号
     */
    public static final String SUPER_ROLE = "超级管理员";
    public static final String EMPTY_STRING = " ";

    public static final String DEFAULT_BUILD_COMPNAME = "建设单位";

    public static final String DEFAULT_DESIGN_COMPNAME = "编制单位";
    //专家默认费用
    public static final int EXPERT_REVIEW_COST = 1000;
    /**
     * 默认需要上传的文件
     */
    //public static final String DEFAULT_CHECK_FILE = "投资估算表,投资匡算表;评审意见,审核意见";
    /**
     * 企业名称
     */
    public static final String COMPANY_NAME = "深圳市政府投资项目评审中心";
    /**
     * 默认错误信息
     */
    public static final String ERROR_MSG = "操作异常，错误信息已记录，请联系相关人员处理！";
    /**
     * 默认错误信息
     */
    public static final String ERROR_XSS_MSG = "操作异常，参数存在攻击信息，请联系相关人员处理！";
    /**
     * 系统业务属性文件名
     */
    public static final String businessPropertiesName = "business.properties";
    /**
     * 默认来文单位
     */
    public static final String SEND_FILE_UNIT = "深圳市发展和改革委员会";
    /**
     * 停用
     */
    public static final String USER = "在用";
    /**
     * 停用
     */
    public static final String UNUSER = "停用";

    /**
     * 发文范围默认值
     */
    //public static final String DIS_SCOPE_XSY = "艾传荣副巡视员";

    /**
     * 发文范围默认值
     */
    public static final String DIS_SCOPE_ZXLD = "中心领导";

    /**
     * 项目评审登记表评估类标识
     */
    public static String SIGN_PX_PREFIX = "PX";
    /**
     * 项目评审登记表概算类标识
     */
    public static String SIGN_GX_PREFIX = "GX";
    /**
     * 发文编号前缀
     */
    public static String DISPATCH_PREFIX = "深投审";

    /**
     * 设备清单(国产、进口设备)阶段的前缀
     */
    public static String DISPATCH_EQUIPMENT_PREFIX = "深投审设";
    /**
     * 拟补充资料函文件字号
     */
    public static String ADDSUPPER_PREFIX = "深投审函";

    /**
     * 插件默认地址
     */
    public static final String plugin_file_path = "contents/plugins";
    /**
     * 工作方案表抬头
     */
    public static final String WORKPROGRAM_NAME = "评审工作方案";
    public static final String DRAW_ASSIST_UNITNAME = "系统协审单位抽签最大序号值";

    /**
     * session中用户的key
     */
    public final static String USER_SESSION_KEY = "CURRENT_USER";
    /**
     * session中用户的key
     */
    public final static String NOTICE_KEY = "NOTICE_KEY";

    /**
     * 会议预定类型
     */
    public enum MEET_BOOK_ENUM{
        /**
         * 评审会
         */
        REVIEW("0","评审会");
        private String bookCode;
        private String bookCodeName;

        public String getBookCode() {
            return bookCode;
        }

        public void setBookCode(String bookCode) {
            this.bookCode = bookCode;
        }

        public String getBookCodeName() {
            return bookCodeName;
        }

        public void setBookCodeName(String bookCodeName) {
            this.bookCodeName = bookCodeName;
        }

        MEET_BOOK_ENUM(String bookCode, String bookCodeName) {
            this.bookCode = bookCode;
            this.bookCodeName = bookCodeName;
        }
    }

    /**
     * 项目流程信息状态
     * (1:已发起，2:正在做工作方案，3:已完成工作方案，4:正在做发文 5:已完成发文填报 6:已完成发文编号 7:已发送报销，8:已发送存档，9:已确认归档)
     */
    public enum SignProcessState {
        IS_START(1),
        DO_WP(2),
        END_WP(3),
        DO_DIS(4),
        END_DIS(5),
        END_DIS_NUM(6),
        SEND_CW(7),
        SEND_FILE(8),
        FINISH(9);

        private Integer value;

        SignProcessState(Integer value) {
            this.value = value;
        }

        public Integer getValue() {
            return value;
        }
    }

    /**
     * 以下是缓存参数
     */
    public static enum EnumConfigKey {
        CONFIG_LIST("CONFIG_LIST"),                 //所有数据参数缓存
        SYSINIT("SYSINIT"),                         //系统初始化
        ;

        private String value;

        EnumConfigKey(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    /**
     * 定义一个业务状态
     * "0" 表示否或者草稿
     * "1" 表示进行中
     * "2" 表示暂停
     * "5" 表示正常
     * "7" 表示已删除
     * "8" 表示强制结束(流程结束)
     * "9" 表示是,或者已完成
     *
     * @author ldm
     */
    public static enum EnumState {
        NO("0"),
        PROCESS("1"),
        STOP("2"),
        NORMAL("5"),
        DELETE("7"),
        FORCE("8"),
        YES("9");

        private String value;

        EnumState(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    /**
     * 业务类型：
     * 1为报审文件，2为归档图纸，3为补充材料，4其他资料，
     * 5为归档中的报审登记表中的其它资料，
     * 6为归档项目审核中的补充资料，
     * 7为归档其它特殊文件
     */

    public static enum AddRegisterFileType {
        BSWJ("1"),
        GDTZ("2"),
        BCZL("3"),
        QTZL("4"),
        GBQT("5"),
        GXBZ("6"),
        GQTW("7");

        private String value;

        AddRegisterFileType(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    /**
     * 专家状态
     *
     * @author Administrator
     */
    public static enum EnumExpertState {
        AUDITTING("1"),             //审核中
        OFFICIAL("2"),              //正式
        ALTERNATIVE("3"),           //备选
        STOP("4"),                  //停用
        REMOVE("0");                //已删除

        private String value;

        EnumExpertState(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    //专家选择类型
    public static enum EnumExpertSelectType {
        AUTO("1"),            //随机选
        SELF("2"),            //自选
        OUTSIDE("3");        //境外、市外

        private String value;

        EnumExpertSelectType(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public static String getName(String key) {
            switch (key) {
                case "1":
                    return "随机抽取";
                case "2":
                    return "自选";
                case "3":
                    return "境外、市外专家";
                default:
                    return "";
            }
        }
    }

    /**
     * 流程环节 处理组名称
     *
     * @author ldm
     */
    public static enum EnumFlowNodeGroupName {
        SIGN_USER("签收员"),
        COMM_DEPT_DIRECTOR("综合部部长"),
        APPRAISE_REVIEWER("优秀评审报告审批员"),
        VICE_DIRECTOR("副主任"),
        DIRECTOR("主任"),
        FILER("归档员"),
        DEPT_LEADER("部门负责人"),
        FINANCIAL("财务人员"),
        SUPER_LEADER("上级领导"),
        REPORT_USER("报表人员");
        private String value;

        EnumFlowNodeGroupName(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    public static enum EnumPostdoctoralName {
        POSTDOCTORAL_ADMIN("博士后基地管理员"),
        POSTDOCTORAL_PERSON("博士后人员");
        private String value;

        EnumPostdoctoralName(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    /**
     * 消息返回码
     *
     * @author ldm
     */
    public static enum MsgCode {
        OK("ok"),
        ERROR("error"),
        TIMEOUT("timeout"),
        //流程处理状态
        FLOW_INSTANCE_NULL("01"),       //流程实例为空
        FLOW_TASK_NULL("02"),           //任务为空
        FLOW_ACTIVI_NEQ("03"),          //当前环节名称不对
        FLOW_NOT_MATCH("04");           //没有匹配

        private String value;

        MsgCode(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    /**
     * 模板枚举
     */
    public enum Template {
        EXPORTROOM("exportRoom", "导出会议室安排"),
        EXCEL_SUFFIX(".xls", "xls表格"),
        WORD_SUFFIX(".doc", "work文档"),
        PDF_SUFFIX(".pdf", "pdf文档"),
        COMPERE("meetBefore/compere", "主持人手稿"),
        MEETING_AM("meetingAM", "会议议程（上午）"),
        MEETING_PM("meetingPM", "会议议程（下午）"),
        EXPERTREVIEWIDEA("meetBefore/expertReviewIdea", "专家评审意见书"),
        ASSIST("assist", "协审协议书"),
        SIGN_IN("signIn", "签到表"),
        INVITATION("meetBefore/invitation", "邀请函"),
        PROJECR_NOTICE("meetBefore/notice", "项目单位会议通知"),
        THIS_STAGE_MEETING("thisStageMeeing", "本周评审会议安排"),
        NEXT_STAGE_MEETING("nextStageMeeting", "下周评审会议安排"),
        UNIT_NOTICE("meetBefore/unitNotice", "相关单位会议通知"),


        //可行性研究报告
        STUDY_OPINION("study/opinion", "可行性研究报告_评审意见"),
        STUDY_ESTIMATE("study/estimate", "可行性研究报告_投资估算表"),
        STUDY_ROSTER("study/roster", "可行性研究报告_评审组名单"),

        //资金申请报告
        REPORT_OPINION("report/opinion", "资金申请报告_评审意见"),
        REPORT_ESTIMATE("report/estimate", "资金申请报告_投资估算表"),
        REPORT_ROSTER("report/roster", "资金申请报告_评审组名单"),

        //项目建议书
        SUG_OPINION("sug/opinion", "项目建议书_评审意见"),
        SUG_ESTIMATE("sug/estimate", "项目建议书_投资匡算表"),
        SUG_ROSTER("sug/roster", "项目建议书_评审组名单"),

        //项目概算
        BUDGET_OPINION("budget/opinion", "项目概算_审核意见"),
        BUDGET_ESTIMATE("budget/estimate", "项目概算_概算汇总表和审核对照表"),
        BUDGET_PROJECTCOST("budget/projectCost", "项目概算_建安工程费用"),
        BUDGET_ROSTER("budget/roster", "项目概算_审核组名单"),

        //课题研究
        SUBJECT_STUDY_NOVICE("subjectStudy/subjectNovice", "课题研究_会议通知"),
        SUBJECT_STUDY_SIGNIN("subjectStudy/subjectSignIn", "课题研究_签到表"),
        SUBJECT_STUDY_EXPERTAIGNATURE("subjectStudy/expertSignature", "课题研究_专家签名"),
        SUBJECT_STUDY_INVITATION("subjectStudy/subjectInvitation", "课题研究_邀请函"),
        SUBJECT_STUDY_MEETINGAM("subjectStudy/subjectMeetingAM", "课题研究_会议议程（上午）"),
        SUBJECT_STUDY_MEETINGPM("subjectStudy/subjectMeetingPM", "课题研究_会议议程（下午）"),
        //月报简报
        MONTH_REPORT("monthReport/monthReport", "月报简报"),

        /*************以下是ISO模板*************/
        //项目建议书
        STAGE_SUG_SIGN("print/sign/xmjys", "项目建议书_报审登记表"),
        STAGE_SUG_WORKPROGRAM("print/workProgram/xmjys", "项目建议书_工作方案"),
        STAGE_SUG_FILERECORD("print/filerecord/fileXmjys", "项目建议书_归档"),
        STAGE_SUG_DISPATCHDOC("print/dispatchDoc/dispatchXmjys", "项目建议书_发文"),
        STAGE_STUDY_DISPATCHDOC("print/dispatchDoc/dispatchKxxyj", "可行性研究报告_发文"),
        STAGE_BUDGET_DISPATCHDOC("print/dispatchDoc/dispatchXmgs", "项目概算_发文"),

        STAGE_REPORT_WORKPROGRAM("print/workProgram/zjsq", "资金申请_工作方案"),
        STAGE_DEVICE_WORKPROGRAM("print/workProgram/jksb", "进口设备_工作方案"),
        STAGE_HOMELAND_WORKPROGRAM("print/workProgram/sbqd", "设备清单_工作方案"),

        INFORMATION("print/expert_information", "专家信息"),

        //可行性研究
        STAGE_STUDY_FILERECORD("print/filerecord/fileKxxyj", "可行性研究_归档"),

        //项目概算
        STAGE_BUDGET_SIGN("print/sign/signXmgs", "项目概算_报审登记表"),
        STAGE_BUDGET_FILERECORD("print/filerecord/fileXmgs", "项目概算_归档"),
        STAGE_BUDGET_XS_FILERECORD("print/filerecord/fileXmgsXS", "项目概算协审_归档"),

        //进口设备
        IMPORT_DEVICE_SIGN("print/sign/signJksb", "进口设备_报审登记表"),
        IMPORT_DEVICE_FILERECORD("print/filerecord/fileJksb", "进口设备_归档"),
        IMPORT_DEVICE_DISPATCHDOC("print/dispatchDoc/dispatchJksb", "进口设备_发文"),

        //设备清单
        DEVICE_BILL_SIGN("print/sign/signSbqd", "设备清单_报审登记表"),
        DEVICE_BILL_FILERECORD("print/filerecord/fileSbqd2", "设备清单_归档"),
        DEVICE_BILL_DISPATCHDOC("print/dispatchDoc/dispatchSbqd", "设备清单_发文"),

        //资金申请报告
        APPLY_REPORT_FILERECORD("print/filerecord/fileZjsq", "资金申请_归档"),
        APPLY_REPORT_SIGN("print/sign/signZjsq", "资金申请_报审登记表"),
        APPLY_REPORT_DISPATCHDOC("print/dispatchDoc/dispatchZjsq", "资金申请单_发文"),

        //其它
        OTHERS_DISPATCHDOC("print/dispatchDoc/dispatchOther", "其它阶段_发文"),
        OTHERS_FILERECORD("print/filerecord/fileOther", "其它阶段_归档"),

        //登记赋码
        DJFM_DISPATCHDOC("print/dispatchDoc/dispatchDJFM", "登记赋码_发文"),
        DJFM_FILERECORD("print/filerecord/fileDJFM", "登记赋码_归档"),

        //其它资料
        OTHER_FILE("print/other", "其它资料"),

        //拟补充资料函
        ADDSUPPLETER("print/common", "拟补充资料函"),

        //专家申请表
        EXPERT("print/expert", "专家申请表"),

        //专家聘书
        EXPERTOFFER("print/expertoffer", "专家聘书"),

        //专家评审费
        EXPERT_PAYMENT("print/expert_payment", "专家评审费"),
        EXPERT_PAYMENT_one("print/expert_payment_one", "专家评审费"),

        //专家评分
        EXPERT_SCORD("print/expert_score", "专家评分"),

        //单位评分
        UNIT_SCORE("print/unit_score", "单位评分"),

        //月报简报
        MONTHLY("print/monthly/monthly", "月报简报"),

        //借阅档案
        ARCHIVES_DETAIL("print/archives/archivesDetail", "借阅档案"),
        //课题研究
        TOPICINFO_WORKPROGRAM("print/topicInfo/workProgramTopic", "课题研究_工作方案"),
        TOPICINFO_FILERECORD("print/topicInfo/fileTopic", "课题研究_归档"),

        //专家缴费
        EXPERT_PAYTAXES("print/expert_paytaxes", "专家缴费"),
        EXPERT_PAYTAXESDETAIL("print/expert_paytaxesDetail", "专家缴费详细页"),

        ADD_REGISTER_FILE("print/registerFile", "拟补充资料清单"),

        PARTYDETAIL("party/partyDetail" , "党员基本信息采集表"),
        SIGN_IN_SHEET("party/signInSheet" , "党员签到表"),

        ACHIEVEMENT_DETAIL("achievement/achievementDetail","员工业绩统计表"),
        ACHIEVEMENT_DEPT_DETAIL("achievement/achievementDeptDetail","部门业绩统计表"),
        //ACHIEVEMENT_TOPIC_MAINTAIN("achievement/topicMaintain","课题研究及其他业务一览表"),
        ACHIEVEMENT_USER_DETAIL("achievement/achievementUserDetail","员工业绩统计一栏表"),
        //ACHIEVEMENT_ASSIST_PROREVIEW("achievement/assistProReview","协办人评审项目一览表"),
        PROJECT_STOP("print/projectStop" , "项目暂停申请表"),
        PROJECT_VPROJECT("print/vProject" , "委项目处理表")
        ;

        private String key;
        private String value;

        Template(String key, String value) {
            this.key = key;
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public String getKey() {
            return key;
        }
    }

    /**
     * 系统文件类型（跟sysfile.svc.js mainTypeValue同步）
     */
    public static enum SysFileType {
        SIGN("项目附件"),
        FILLSIGN("审批登记"),
        TOPIC("课题附件"),
        HUMAN("人事附件"),
        BOOKS("图书附件"),
        NOTICE("通知公告"),
        SHARE("资料共享"),
        MEETTINGROOM("会议室预定"),
        WORKPROGRAM("工作方案"),
        DISPATCH("发文"),
        DOFILE("归档"),
        MEETING("会前准备材料"),
        SUPPLEMENT("补充函"),
        STAGEMEETING("评审会会议"),
        FILELIBRARY("质量管理文件库"),
        POLICYLIBRARY("政策标准库"),
        SUBJECT_STUDY("课题研究"),
        TEMOLATE("评审报告"),
        FGW_FILE("委附件"),
        DOCTOR_KTYJDG("课题研究大纲"),
        DOCTOR_KTWWHT("课题外委合同");

        private String value;

        SysFileType(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }



    /**
     * 合并类型
     */
    public static enum MergeType {
        WORK_PROGRAM("1"),          //工作方案
        DISPATCH("2"),              //发文
        DIS_SINGLE("1"),            //单个
        DIS_MERGE("2"),             //合并
        REVIEW_SELF("自评"),        //自评
        REVIEW_MEETING("专家评审会"),
        REVIEW_LEETER("专家函评"),
        REVIEW_MERGE("合并评审"),   //合并评审
        REVIEW_SIGNLE("单个评审"),  //单个评审
        ASSIST_SIGNLE("独立项目"),  //协审独立项目
        ASSIST_MERGE("合并项目");   //协审合并项目
        private String value;

        MergeType(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    /**
     * 部门对应的类型
     */
    public static enum OrgType {
        ORGZHB("综合部", ""),
        ORGPGYB("评估一部", "PX"),
        ORGPGEB("评估二部", "PX"),
        ORGGSYB("概算一部", "GX"),
        ORGGSEB("概算二部", "GX"),
        ORXXHZ("信息技术组", "PX");

        private String key;
        private String value;

        OrgType(String key, String value) {
            this.key = key;
            this.value = value;
        }

        public static String getValue(String key) {
            OrgType[] enums = OrgType.values();
            for (int i = 0; i < enums.length; i++) {
                if (enums[i].getKey().equals(key)) {
                    return enums[i].getValue();
                }
            }
            return "";
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }

    /**
     * 文件库-文件类型
     */
    public static enum libraryType {
        FOLDER_TYPE("FOLDER"),      //文件夹
        FILE_TYPE("FILE"),          //文件
        QUALITY_LIBRARY("QUALITY"), //质量管理文件库
        POLICY_LIBRARY("POLICY");   //政策标准文件库

        private String value;

        libraryType(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }


    /**
     * 菜单类型
     */
    /*public static enum menu {
        EXPERT("expert", "专家库");
        private String key;
        private String value;

        menu(String key, String value) {
            this.key = key;
            this.value = value;
        }

        public static menu getByKey(String key) {
            menu[] enums = menu.values();
            for (int i = 0; i < enums.length; i++) {
                if (enums[i].getKey().equals(key)) {
                    return enums[i];
                }
            }
            return null;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }*/

    /**
     * 日记模块
     */
    public static enum LOG_MODULE {
        INTERFACE("系统接口"),
        FLOWCOMMIT("流程提交"),
        FLOWBACK("流程回退"),
        FLOWACTIVE("流程激活"),
        FLOWSTOP("流程暂停"),
        QUARTZ("定时器"),
        FILEUPDATE("附件修改");
        private String value;

        LOG_MODULE(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    /**
     * 专家抽取类型
     */
    public enum ExpertSelectType {
        自选,
        正选,
        备选,
        境外专家,
        市外专家,
        新专家
    }

    /**
     * 短信类型
     */
    public enum MsgType {
        task_type,          //任务短信
        project_type,       //项目短信
        incoming_type,      //项目签收
        sendfgw_type        //发送发改委类型
    }

    /**
     * 系统人员级别
     */
    public static enum SYS_USER_LEVEL {
        /**
         * 主任
         */
        ZR(1),
        /**
         * 副主任
         */
        FZR(2),
        /**
         * 部长
         */
        BZ(3),
        /**
         * 组长
         */
        ZZ(4),
        /**
         * 普通用户
         */
        PT(0);
        private int value;

        SYS_USER_LEVEL(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }
}
