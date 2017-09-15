package cs.common;

/**
 * 流程参数
 * Created by ldm on 2017/9/4 0004.
 */
public class FlowConstant {

    /***************  流程名称  ****************/
    /**
     *课题研究流程
     */
    public final static String TOPIC_FLOW = "TOPIC_FLOW";
    /**
     *图书采购流程
     */
    public final static String BOOKS_BUY_FLOW = "BOOKS_BUY_FLOW";

    /**
     *资产入库流程
     */
    public final static String ASSERT_STORAGE_FLOW = "ASSERT_STORAGE_FLOW";

    /*************** S 课题研究流程 环节名称  ****************/
    public static final String TOPIC_JHTC = "TOPIC_JHTC";                //计划提出
    public static final String TOPIC_BZSH_JH = "TOPIC_BZSH_JH";          //部长审核
    public static final String TOPIC_FGLD_JH = "TOPIC_FGLD_JH";          //副主任审核
    public static final String TOPIC_ZRSH_JH = "TOPIC_ZRSH_JH";          //主任审定
    public static final String TOPIC_BFGW = "TOPIC_BFGW";                //报发改委审批
    public static final String TOPIC_LXDW = "TOPIC_LXDW";                //联系合作单位
    public static final String TOPIC_QDHT = "TOPIC_QDHT";                //签订合同
    public static final String TOPIC_YJSS = "TOPIC_YJSS";                //课题研究实施
    public static final String TOPIC_NBCS = "TOPIC_NBCS";                //内部初审
    public static final String TOPIC_GZFA = "TOPIC_GZFA";                //提出成果鉴定会（或论证会）方案
    public static final String TOPIC_BZSH_FA = "TOPIC_BZSH_FA";          //部长审核
    public static final String TOPIC_FGLD_FA = "TOPIC_FGLD_FA";          //副主任审核
    public static final String TOPIC_ZRSH_FA = "TOPIC_ZRSH_FA";          //主任审定
    public static final String TOPIC_CGJD = "TOPIC_CGJD";                //召开成果鉴定会
    public static final String TOPIC_KTBG = "TOPIC_KTBG";                //完成课题报告
    public static final String TOPIC_BZSH_BG = "TOPIC_BZSH_BG";          //部长审核
    public static final String TOPIC_FGLD_BG = "TOPIC_FGLD_BG";          //副主任审核
    public static final String TOPIC_ZRSH_BG = "TOPIC_ZRSH_BG";          //主任审定
    public static final String TOPIC_KTJT = "TOPIC_KTJT";                //课题结题
    public static final String TOPIC_BZSH_JT = "TOPIC_BZSH_JT";          //部长审核
    public static final String TOPIC_FGLD_JT = "TOPIC_FGLD_JT";          //副主任审核
    public static final String TOPIC_ZRSH_JT = "TOPIC_ZRSH_JT";          //主任审定
    public static final String TOPIC_YFZL = "TOPIC_YFZL";                //印发资料
    public static final String TOPIC_ZLGD = "TOPIC_ZLGD";                //资料归档
    public static final String TOPIC_BZSH_GD = "TOPIC_BZSH_GD";          //部长审核归档
    public static final String TOPIC_GDY_QR = "TOPIC_GDY_QR";            //归档员确认


    /*************** E 课题研究流程 环节名称  ****************/

    /*************** S 资产入库流程环节名称  ****************/
    public static final String ASSERT_STORAGE_APPLY = "ASSERT_STORAGE_APPLY";         //资产入库申请
    public static final String ASSERT_STORAGE_BZSH = "ASSERT_STORAGE_BZSH";          //部长审批
    public static final String ASSERT_STORAGE_ZHBSH = "ASSERT_STORAGE_ZHBSH";        //综合部意见
    public static final String ASSERT_STORAGE_ZXLDSH = "ASSERT_STORAGE_ZXLDSH";      //中心领导审批

    /*************** E 资产入库流程 环节名称  ****************/


    /*************** S 图书采购流程 环节名称  ****************/
    public static final String BOOK_LEADER_CGQQ = "BOOK_LEADER_CGQQ";                //各项目负责人/部门提出购买图书请求
    public static final String BOOK_BZSP = "BOOK_BZSP";                              //部长审批
    public static final String BOOK_FGFZRSP = "BOOK_FGFZRSP";                       //分管副主任审批
    public static final String BOOK_ZXZRSP = "BOOK_ZXZRSP";                         //中心主任审批
    public static final String BOOK_YSRK = "BOOK_YSRK";                            //购买后档案员验收并入库

    /*************** E 图书采购流程 环节名称  ****************/



    /**
     * 流程参数
     */
    public static enum FlowParams{
        USER("user"),                    //单个用户
        USERS("users"),                  //多个用户
        USER_BZ("user_bz"),              //部长
        USER_FGLD("user_fgld"),          //分管领导
        USER_ZR("user_zr"),              //主任
        USER_GDY("user_gdy"),            //归档员
        SEND_FGW("send_fgw"),            //送发改委
        USER_ADMIN("admin");             //系统管理员

        private String value;

        FlowParams(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    /**
     * 图书采购流程参数
     */
    public static enum BooksBuyFlowParams{
        USER_APPLY("user_apply"),         //用户申请
        USER_BZ("user_bz"),              //部长
        USER_FGLD("user_fgfzr"),          //分管领导
        USER_ZR("user_zxzr"),              //主任
        USER_DAY("admin");             //档案员

        private String value;

        BooksBuyFlowParams(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    /**
     * 资产入库流程参数
     */
    public static enum AssertStorageFlowParams{
        USER("user"),         //用户申请
        USER_BZ("user_bz"),    //部长
        USER_ZHB("user_zhb"),  //分管领导
        USER_ZXLD("user_zxld"); //中心领导

        private String value;

        AssertStorageFlowParams(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    /**
     * 流程参数
     */
    public static enum CodeKey{
        TOPIC_INFO_CODE("KT"),                    //课题编号
        TOPIC_FILING_CODE("KD");                  //课题归档编号

        private String value;

        CodeKey(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }
}
