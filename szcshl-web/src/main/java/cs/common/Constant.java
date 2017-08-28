package cs.common;

/**
 * 系统常亮
 *
 * @author ldm
 * @author MCL
 * @date 2017年7月28日 下午2:12:53
 */
/**
 * @author MCL
 *@date 2017年7月28日 下午2:12:53 
 */

import java.util.Date;

/**
 * @author MCL
 *@date 2017年7月28日 下午2:12:55 
 */
public class Constant {
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
     * 默认送件人
     */
    public static final String SEND_SIGN_NAME = "魏俊辉";

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
     * 工作方案表抬头
     */
    public static final String WORKPROGRAM_NAME = "评审工作方案";
    public static final String DRAW_ASSIST_UNITNAME = "系统协审单位抽签最大序号值";

    /**
     * session中用户的key
     */
    public final static String USER_SESSION_KEY = "CURRENT_USER";

    /**
     * 流程名称
     */
    public final static String SIGN_FLOW = "FINAL_SIGN_FLOW";           //项目签收流程

    /**
     * 项目签收流程环节名称
     */
    public static final String FLOW_SIGN_ZR = "SIGN_ZR";                //填报
    public static final String FLOW_SIGN_QS = "SIGN_QS";                 //签收
    public static final String FLOW_SIGN_ZHB = "SIGN_ZHB";              //综合部审批
    public static final String FLOW_SIGN_FGLD_FB = "SIGN_FGLD_FB";      //分管副主任审批
    public static final String FLOW_SIGN_BMFB1 = "SIGN_BMFB1";          //部门分办1
    public static final String FLOW_SIGN_BMFB2 = "SIGN_BMFB2";          //部门分办2
    public static final String FLOW_SIGN_BMFB3 = "SIGN_BMFB3";          //部门分办3
    public static final String FLOW_SIGN_BMFB4 = "SIGN_BMFB4";          //部门分办4
    public static final String FLOW_SIGN_XMFZR1 = "SIGN_XMFZR1";        //项目负责人办理1
    public static final String FLOW_SIGN_XMFZR2 = "SIGN_XMFZR2";        //项目负责人办理2
    public static final String FLOW_SIGN_XMFZR3 = "SIGN_XMFZR3";        //项目负责人办理3
    public static final String FLOW_SIGN_XMFZR4 = "SIGN_XMFZR4";        //项目负责人办理4
    public static final String FLOW_SIGN_BMLD_SPW1 = "SIGN_BMLD_SPW1";  //部长审批1
    public static final String FLOW_SIGN_BMLD_SPW2 = "SIGN_BMLD_SPW2";  //部长审批2
    public static final String FLOW_SIGN_BMLD_SPW3 = "SIGN_BMLD_SPW3";  //部长审批3
    public static final String FLOW_SIGN_BMLD_SPW4 = "SIGN_BMLD_SPW4";  //部长审批4
    public static final String FLOW_SIGN_FGLD_SPW1 = "SIGN_FGLD_SPW1";  //分管副主任审批1
    public static final String FLOW_SIGN_FGLD_SPW2 = "SIGN_FGLD_SPW2";  //分管副主任审批2
    public static final String FLOW_SIGN_FGLD_SPW3 = "SIGN_FGLD_SPW3";  //分管副主任审批3
    public static final String FLOW_SIGN_FGLD_SPW4 = "SIGN_FGLD_SPW4";  //分管副主任审批4
    public static final String FLOW_SIGN_FW = "SIGN_FW";                //发文申请
    public static final String FLOW_SIGN_QRFW = "SIGN_QRFW";            //项目负责人确认发文
    public static final String FLOW_SIGN_BMLD_QRFW = "SIGN_BMLD_QRFW";  //部长审批发文
    public static final String FLOW_SIGN_FGLD_QRFW = "SIGN_FGLD_QRFW";  //分管领导审批发文
    public static final String FLOW_SIGN_ZR_QRFW = "SIGN_ZR_QRFW";      //主任审批发文
    public static final String FLOW_SIGN_FWBH = "SIGN_FWBH";            //生成发文编号
    public static final String FLOW_SIGN_CWBL = "SIGN_CWBL";            //财务办理
    public static final String FLOW_SIGN_GD = "SIGN_GD";                //归档
    public static final String FLOW_SIGN_DSFZR_QRGD = "SIGN_DSFZR_QRGD";//第二负责人确认
    public static final String FLOW_SIGN_QRGD = "SIGN_QRGD";            //最终归档

    /**
     * 项目签收流程参数
     */
    public static enum SignFlowParams {
        GO_DISPATCH("godispatch"),              //直接发文
        PRIN_USER("principaluser"),             //项目负责人确认
        SECOND_USER("seconduser"),              //第二负责人确认
        UNPASS("unpass"),                       //项目负责人确认不通过
        BRANCH1("branch1"),                     //项目分支1
        BRANCH2("branch2"),                     //项目分支2
        BRANCH3("branch3"),                     //项目分支3
        BRANCH4("branch4"),                     //项目分支4
        BRANCH_INDEX1("1"),                     //分支序号1（主分支）
        BRANCH_INDEX2("2"),                     //分支序号2
        BRANCH_INDEX3("3"),                     //分支序号3
        BRANCH_INDEX4("4");                     //分支序号4

        private String value;

        SignFlowParams(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    /**
     * 流程用户处理定义名称
     */
    public static enum FlowUserName{
        USER_ZR("user_zr"),              //主任
        USER_QS("user_qs"),              //项目签收人
        USER_ZHB("user_zhb"),            //综合部部长
        USER_FGLD("user_fgld"),          //分管领导
        USER_BZ1("user_bz1"),            //部门领导1（主分支）
        USER_BZ2("user_bz2"),            //部门领导2
        USER_BZ3("user_bz3"),            //部门领导3
        USER_BZ4("user_bz4"),            //部门领导4
        USER_FZR1("users1"),             //项目负责人1（主分支）
        USER_FZR2("users2"),             //项目负责人
        USER_FZR3("users3"),             //项目负责人
        USER_FZR4("users4"),             //项目负责人
        USER_FGLD1("user_fgld1"),        //分管领导1（主分支）
        USER_FGLD2("user_fgld2"),        //分管领导2
        USER_FGLD3("user_fgld3"),        //分管领导3
        USER_FGLD4("user_fgld4"),        //分管领导4
        USER_HQ("user_hq"),              //项目负责人确认
        USER_HQ_LIST("user_hq_list"),    //项目负责人确认列表
        USER_M("user_m"),                //项目第一负责人
        USER_A("user_a"),                //项目第二负责人
        USER_CW("users_cw"),             //财务
        USER_QRGD("user_qrgd");          //确认归档人员

        private String value;

        FlowUserName(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

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
        AUDITTING("1"),            //审核中
        OFFICIAL("2"),            //正式
        ALTERNATIVE("3"),        //备选
        STOP("4"),                //停用
        REMOVE("5");            //已删除

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
    }

    /**
     * 流程环节 处理组名称
     *
     * @author ldm
     */
    public static enum EnumFlowNodeGroupName {
        SIGN_USER("签收员"),
        COMM_DEPT_DIRECTOR("综合部部长"),
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
    public static enum SignBusinessType {
        GD("GD"), PD("PD");

        private String value;

        SignBusinessType(String value) {
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
        NEXT_STAGE_MEETING("nextStageMeeting", "下周评审会议安排");

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
        POLICYLIBRARY("政策标准库");

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
        DISPATCH("2");              //发文
        private String value;

        MergeType(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    /**
     * 发文方式，评审方式
     */
    public static enum MergeWay {
        SINGLE("1"),          //单个
        MERGE("2");           //合并
        private String value;

        MergeWay(String value) {
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
        ORGPGYB("评估一部","PD"),
        ORGPGEB("评估二部","PD"),
        ORGGSYB("概算一部","GD"),
        ORGGSEB("概算二部","GD");

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
}
