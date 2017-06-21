package cs.common;

/**
 * 系统常亮
 *
 * @author ldm
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
     * 工作方案表抬头
     */
    public static final String WORKPROGRAM_NAME = "评审工作方案";
    public static final String DRAW_ASSIST_UNITNAME = "系统协审单位抽签最大序号值";
    public static final String FLOW_LINK_SYMBOL = "$";    //流程业务ID和名称关联符号

    /**
     * 项目签收流程环节名称
     *
     * @author Administrator
     */
    public static final String FLOW_ZR_TB = "ZR_TB";//填报
    public static final String FLOW_QS = "QS";//签收
    public static final String FLOW_ZHB_SP_SW = "ZHB_SP_SW";//综合部审批
    public static final String FLOW_FGLD_SP_SW = "FGLD_SP_SW";//分管副主任审批
    public static final String FLOW_BM_FB1 = "BM_FB1";//部门分办-主流程
    public static final String FLOW_BM_FB2 = "BM_FB2";//部门分办
    public static final String FLOW_XMFZR_SP_GZFA1 = "XMFZR_SP_GZFA1";//项目负责人承办	-主流程
    public static final String FLOW_XMFZR_SP_GZFA2 = "XMFZR_SP_GZFA2";//项目负责人承办
    public static final String FLOW_BZ_SP_GZAN1 = "BZ_SP_GZAN1";//部长审批-主流程
    public static final String FLOW_BZ_SP_GZAN2 = "BZ_SP_GZAN2";//部长审批
    public static final String FLOW_FGLD_SP_GZFA1 = "FGLD_SP_GZFA1";//分管副主任审批-主流程
    public static final String FLOW_FGLD_SP_GZFA2 = "FGLD_SP_GZFA2";//分管副主任审批
    public static final String FLOW_FW_SQ = "FW_SQ";//发文申请
    public static final String FLOW_BZ_SP_FW = "BZ_SP_FW";//部长审批发文
    public static final String FLOW_FGLD_SP_FW = "FGLD_SP_FW";//分管领导审批发文
    public static final String FLOW_ZR_SP_FW = "ZR_SP_FW";//主任审批发文
    public static final String FLOW_MFZR_GD = "MFZR_GD";//第一负责人归档
    public static final String FLOW_AZFR_SP_GD = "AZFR_SP_GD";//第二负责人审批归档
    public static final String FLOW_BMLD_QR_GD = "BMLD_QR_GD";//确认归档

    //项目协审流程环节名称
    public static final String FLOW_XS_ZR = "XS_ZR";// 主任
    public static final String FLOW_XS_XMQS = "XS_XMQS";// 项目签收
    public static final String FLOW_XS_ZHBBL = "XS_ZHBBL";// 综合部审批
    public static final String FLOW_XS_FGLD_SP = "XS_FGLD_SP";// 分管副主任审批
    public static final String FLOW_XS_BMFB = "XS_BMFB";// 部门分办
    public static final String FLOW_XS_XMFZR_GZFA = "XS_XMFZR_GZFA";// 项目负责人承办
    public static final String FLOW_XS_BZSP_GZFA = "XS_BZSP_GZFA";// 部长审批
    public static final String FLOW_XS_FGLDSP_GZFA = "XS_FGLDSP_GZFA";// 分管副主任审批
    public static final String FLOW_XS_FW = "XS_FW"; //发文申请
    public static final String FLOW_XS_BZSP_FW = "XS_BZSP_FW";// 部长审批发文
    public static final String FLOW_XS_FGLDSP_FW = "XS_FGLDSP_FW";// 分管领导审批发文
    public static final String FLOW_XS_ZRSP_FW = "XS_ZRSP_FW";// 主任审批发文
    public static final String FLOW_XS_FZR_GD = "XS_FZR_GD";// 第一负责人归档
    public static final String FLOW_XS_FZR_SP = "XS_FZR_SP";// 第二负责人审批归档
    public static final String FLOW_XS_QRGD = "XS_QRGD";// 确认归档

    /**
     * 以下是缓存参数
     */
    public static enum EnumConfigKey{
        CONFIG_LIST("CONFIG_LIST"),                 //所有数据参数缓存
        LAST_UNIT_MAXSORT("LAST_UNIT_MAXSORT"),     //上一个抽签单位的最大序号
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
     * "8" 表示强制结束
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
     * 业务流程名称
     *
     * @author ldm
     */
    public static enum EnumFlow {
        FINAL_SIGN("FINAL_SIGN_FLOW"),        //项目签收流程
        SIGN_XS_FLOW("SIGN_XS_FLOW"),        //项目协审流程
        SIGN("newsignflow");

        private String value;

        EnumFlow(String value) {
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
        DEPT_LEADER("部门负责人");

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
        OK("ok"), ERROR("error");

        private String value;

        MsgCode(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    /**
     * 存档编号类型
     * （GD表示概率，PD表示评估）
     *
     * @author ldm
     */
    public static enum FileNumType {
        GD("GD"), PD("PD");

        private String value;

        FileNumType(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }
    
    /**
     * 系统文件类型
     */
    public static enum SysFileType{
        SIGN("收文"),
        WORKPROGRAM("工作方案"),
        DISPATCH("发文"),
        DOFILE("归档"),
        MEETING("会前准备材料"),
        SUPPLEMENT("补充函");

        private String value;

        SysFileType(String value){
            this.value = value;
        }
        public String getValue() {
            return value;
        }
    }

}
