package cs.common;

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
    public static final String SUPER_USER = "admin";
    /**
     * 企业名称
     */
    public static final String COMPANY_NAME = "深圳市政府投资项目评审中心";
    /**
     * 默认错误信息
     */
    public static final String ERROR_MSG = "操作异常，错误信息已记录，请联系相关人员处理！";
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
    public static final String UNUSER = "停用";

    /**
     * 发文范围默认值
     */
    public static final String DIS_SCOPE_XSY = "艾传荣副巡视员" ;

    /**
     * 发文范围默认值
     */
    public static final String DIS_SCOPE_ZXLD = "中心领导" ;

    /**
     * 发文编号前缀
     */
    public static String DISPATCH_PREFIX = "深投审";

    /**
     * 默认收文工作日
     */
    public static Float WORK_DAY_15 = 15f;

    /**
     * 默认工作日
     */
    public static Float WORK_DAY_12 = 12f;

    /**
     * 初始化用户密码
     */
    public static final String PASSWORD = "123456";

    /**
     * 评审报告评优 综合部负责人
     */
    //public static final String GENERALCONDUTOR = "王纯";


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
     * 项目流程信息状态
     * (1:已发起，2:正在做工作方案，3:已完成工作方案，4:正在做发文 5:已完成发文 6:已完成发文编号 7:正在归档，8:已完成归档，9:已确认归档)
     */
    public static enum SignProcessState{
        IS_START(1),
        DO_WP(2),
        END_WP(3),
        DO_DIS(4),
        END_DIS(5),
        END_DIS_NUM(6),
        DO_FILE(7),
        END_FILE(8),
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
        LAST_UNIT_MAXSORT("LAST_UNIT_MAXSORT"),     //上一个抽签单位的最大序号
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
        NO("0"), PROCESS("1"), STOP("2"), NORMAL("5"),
        DELETE("7"), FORCE("8"), YES("9");

        private String value;

        EnumState(String value) {
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
        REMOVE("5");                //已删除

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
            switch (key){
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
        FINANCIAL("财务人员");

        private String value;

        EnumFlowNodeGroupName(String value) {
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
        FLOW_NOT_MATCH("04"),           //没有匹配
        //以下主要用于接口对接
        SUCCESS("01"),                  //成功
        OBJ_NULL("02"),                 //空对象
        MAIN_VALUE_NULL("03"),          //关键属性为空
        SAVE_ERROR("04");               //保存异常

        private String value;

        MsgCode(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    /**
     *  项目业务类型
     *
     * @author ldm
     */
    public static enum BusinessType {
        GX("GX"),                   //表示项目概算
        PX("PX"),                   //表示项目评估
        SIGN("SIGN"),               //表示项目签收
        TOPIC("TOPIC"),             //表示课题研究
        SIGN_WP("SIGN_WP"),         //项目签收工作方案
        TOPIC_WP("TOPIC_WP");       //课题研究工作方案

        private String value;

        BusinessType(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    /**
     * 模板枚举
     */
    public static enum Template {
        EXPORTROOM("exportRoom", "导出会议室安排"),
        OUTPUT_SUFFIX(".doc", "work文档"),        //生成work文档
        COMPERE("compere", "主持人"),
        MEETING_AM("meetingAM", "会议议程（上午）"),
        MEETING_PM("meetingPM", "会议议程（下午）"),
        ASSIST("assist", "协审协议书"),
        SIGN_IN("signIn", "签到表"),
        INVITATION("invitation", "邀请函"),
        UNIT_NOTICE("notice", "项目单位会议通知"),
        THIS_STAGE_MEETING("thisStageMeeing", "本周评审会议安排"),
        NEXT_STAGE_MEETING("nextStageMeeting", "下周评审会议安排"),
        OUTPUT_SUFFIX_XLS(".xls","xls表格"),//生成xls表格

        //可行性研究报告
        STUDY_OPINION("study/opinion","可行性研究报告_评审意见"),
        STUDY_ESTIMATE("study/estimate","可行性研究报告_投资估算表"),
        STUDY_ROSTER("study/roster","可行性研究报告_评审组名单"),

        //资金申请报告
        REPORT_OPINION("report/opinion","资金申请报告_评审意见"),
        REPORT_ESTIMATE("report/estimate","资金申请报告_投资估算表"),
        REPORT_ROSTER("report/roster","资金申请报告_评审组名单"),

        //项目建议书
        SUG_OPINION("sug/opinion","项目建议书_评审意见"),
        SUG_ESTIMATE("sug/estimate","项目建议书_投资匡算表"),
        SUG_ROSTER("sug/roster","项目建议书_评审组名单"),

        //项目概算
        BUDGET_OPINION("budget/opinion","项目概算_审核意见"),
        BUDGET_ESTIMATE("budget/estimate","项目概算_概算汇总表和审核对照表"),
        BUDGET_PROJECTCOST("budget/projectCost","项目概算_建安工程费用"),
        BUDGET_ROSTER("budget/roster", "项目概算_审核组名单"),

        //课题研究
        SUBJECT_STUDY_NOVICE("subjectStudy/subjectNovice" , "课题研究_会议通知"),
        SUBJECT_STUDY_SIGNIN("subjectStudy/subjectSignIn","课题研究_签到表"),
        SUBJECT_STUDY_EXPERTAIGNATURE("subjectStudy/expertSignature" , "课题研究_专家签名"),
        SUBJECT_STUDY_INVITATION("subjectStudy/subjectInvitation" , "课题研究_邀请函"),
        SUBJECT_STUDY_MEETINGAM("subjectStudy/subjectMeetingAM" ,"课题研究_会议议程（上午）"),
        SUBJECT_STUDY_MEETINGPM("subjectStudy/subjectMeetingPM" , "课题研究_会议议程（下午）"),
        //月报简报
        MONTH_REPORT("monthReport/monthReport","月报简报");

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
        TEMOLATE("发文模板");

        private String value;

        SysFileType(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    /**
     * 警示灯状态
     * @author MCL
     *@date 2017年6月29日 下午12:03:19 
     */
    public static enum signEnumState {
        NOLIGHT("0"),                   //不显示
        PROCESS("1"),                   //在办
        DISPA("2"),                     //已发文
        ARCHIVE("3"),                   //已发送存档
        PAUSE("4"),                     //暂停
        UNDER3WORKDAY("5"),             //少于三个工作日
        DISPAOVER("6"),                 //发文超期
        OVER25WORKDAYARCHIVE("7"),      //超过25个工作日未存档
        ARCHIVEOVER("8");               //存档超期

        private String value;

        signEnumState(String value) {
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
        REVIEW_SIGNLE("单个评审");  //单个评审
        private String value;

        MergeType(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    /**
     * 部门名称
     * @author sjy
     *
     */
    public static enum OrgName {
        ORGZHB("综合部"),
        ORGPGYB("评估一部"),
        ORGPGEB("评估二部"),
        ORGGSYB("概算一部"),
        ORGGSEB("概算二部");

        private String value;

        OrgName(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    /**
     * 部门对应的类型
     */
    public static enum OrgType{
        ORGZHB("综合部",""),
        ORGPGYB("评估一部","PX"),
        ORGPGEB("评估二部","PX"),
        ORGGSYB("概算一部","GX"),
        ORGGSEB("概算二部","GX");

        private String key;
        private String value;

        OrgType(String key,String value) {
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
     * 文件夹类型
     */
    public static enum folderType{

        FILE_LIBRARY("1"),//文件库

        POLICY_LIBRARY("2");//政策库

        private String value;

        folderType(String value){
            this.value=value;
        }

        public String getValue(){
            return value;
        }

    }

    /**
     * 文件类型
     */
    public static enum fileNatrue{

        FOLDER_TYPE("1"),//文件夹

        FILE_TYPE("2");//文件

        private String value;

        fileNatrue(String value){
            this.value=value;
        }

        public String getValue(){
            return value;
        }

    }


    /**
     * 项目阶段
     */
    public static enum ProjectStage{
        STAGE_SUG("项目建议书"),
        STAGE_STUDY("可行性研究报告"),
        STAGE_BUDGET("项目概算"),
        APPLY_REPORT ("资金申请报告"),
        OTHERS("其它"),
        DEVICE_BILL_HOMELAND("设备清单（国产）"),
        DEVICE_BILL_IMPORT("设备清单（进口）"),
        IMPORT_DEVICE("进口设备");

        private String value;

        ProjectStage(String value) {
            this.value = value;
        }
        public String getValue() {
            return value;
        }
    }

    /**
     * 表头类型
     */
    public static enum HeaderType{
        TYPE_PROJECT("项目类型"),
        TYPE_TOPIC("课题类型");
        private String value;
        HeaderType(String value){
            this.value = value ;
        }
        public String getValue(){
            return value;
        }
    }

    /**
     * 菜单类型
     */
    public static enum menu {
        EXPERT("expert","专家库");
        private String key;
        private String value;

        menu(String key,String value) {
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
    }
}
