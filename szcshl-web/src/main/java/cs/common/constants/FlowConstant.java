package cs.common.constants;

/**
 * 流程参数
 * Created by ldm on 2017/9/4 0004.
 */
public class FlowConstant {

    /***************  流程名称  ****************/
    /**
     * 重写工作方案流程
     */
    public final static String WORK_HIS_FLOW = "WORK_HIS_FLOW";
    /**
     * 重写工作方案流程环节名称
     */
    public static final String WPHIS_XMFZR = "WPHIS_XMFZR";                            //负责人填报
    public static final String WPHIS_BMLD_SPW = "ROLL_BACK_SEND_BZ_SP";                //部长审批
    public static final String WPHIS_FGLD_SPW = "ROLL_BACK_SEND_FGLD_SP";              //分管领导审批


    /**
     * 项目签收流程
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
    public static final String FLOW_SIGN_BMLD_QRFW_XB = "SIGN_BMLD_QRFW_XB"; //协办部长审批发文
    public static final String FLOW_SIGN_BMLD_QRFW = "SIGN_BMLD_QRFW";       //部长审批发文
    public static final String FLOW_SIGN_FGLD_QRFW_XB = "SIGN_FGLD_QRFW_XB"; //协办分管领导审批发文
    public static final String FLOW_SIGN_FGLD_QRFW = "SIGN_FGLD_QRFW";  //分管领导审批发文
    public static final String FLOW_SIGN_ZR_QRFW = "SIGN_ZR_QRFW";      //主任审批发文
    public static final String FLOW_SIGN_FWBH = "SIGN_FWBH";            //生成发文编号
    public static final String FLOW_SIGN_CWBL = "SIGN_CWBL";            //财务办理
    public static final String FLOW_SIGN_GD = "SIGN_GD";                //归档
    public static final String FLOW_SIGN_DSFZR_QRGD = "SIGN_DSFZR_QRGD";//第二负责人确认
    public static final String FLOW_SIGN_QRGD = "SIGN_QRGD";            //最终归档


    /**
     * 项目暂停审批流程
     */
    public final static String PROJECT_STOP_FLOW = "PROJECT_STOP_FLOW";           //项目签收流程
    /**
     * 项目暂停环节
     */
    public final static String FLOW_STOP_FZR = "STOP_FZR";           //项目负责人填报
    public static final String FLOW_STOP_BZ_SP = "STOP_BZ_SP";       //部长审批
    public static final String FLOW_STOP_FGLD_SP = "STOP_FGLD_SP";   //分管领导审批


    /**
     * 档案借阅流程
     */
    public final static String FLOW_ARCHIVES = "FLOW_ARCHIVES";
    /**
     * 档案借阅流程环节
     */
    public final static String FLOW_ARC_SQ = "ARC_SQ";              //填报
    public static final String FLOW_ARC_BZ_SP = "ARC_BZ_SP";        //部长审批
    public static final String FLOW_ARC_FGLD_SP = "ARC_FGLD_SP";    //分管领导审批
    public static final String FLOW_ARC_GDY = "ARC_GDY";            //归档员
    public static final String FLOW_ARC_ZR_SP = "ARC_ZR_SP";        //主任审批


    /**
     * 优秀评审报告流程
     */
    public final static String FLOW_APPRAISE_REPORT = "FLOW_APPRAISE_REPORT";
    /**
     * 优秀评审报告流程环节
     */
    public final static String FLOW_ARP_FZR = "ARP_FZR";              //负责填报
    public static final String FLOW_ARP_BZ_SP = "ARP_BZ_SP";          //部长审批
    public static final String FLOW_ARP_ZHB_SP = "ARP_ZHB_SP";        //综合部审批

    /**
     * 拟补充资料函流程
     */
    public final static String FLOW_SUPP_LETTER = "FLOW_SUPP_LETTER";

    /**
     * 拟补充资料函流程环节
     */
    public final static String FLOW_SPL_FZR = "SPL_FZR";              //负责填报
    public static final String FLOW_SPL_BZ_SP = "SPL_BZ_SP";          //主办部长审核
    public static final String FLOW_SPL_LD_HQ = "SPL_LD_HQ";          //领导会签
    public static final String FLOW_SPL_FGLD_SP = "SPL_FGLD_SP";      //分管领导审批

    /**
     * 项目签收流程参数
     */
    public static enum SignFlowParams {
        BRANCH1("branch1"),                     //项目分支1
        BRANCH2("branch2"),                     //项目分支2
        BRANCH3("branch3"),                     //项目分支3
        BRANCH4("branch4"),                     //项目分支4
        BRANCH_INDEX1("1"),                     //分支序号1（主分支）
        BRANCH_INDEX2("2"),                     //分支序号2
        BRANCH_INDEX3("3"),                     //分支序号3
        BRANCH_INDEX4("4"),                     //分支序号4
        WORK_PLAN1("workplan1"),                //是否直接发文
        WORK_PLAN2("workplan2"),                //是否直接发文
        WORK_PLAN3("workplan3"),                //是否直接发文
        WORK_PLAN4("workplan4"),                //是否直接发文
        HAVE_XMFZR("xmfzr"),                    //是否有项目负责人
        HAVE_XB("xb"),                          //是否有协办
        XMFZR_SP("fzrsp"),                      //项目负责人审批
        XBBZ_SP("xbbzsp"),                      //协办部长审批
        XBFZR_SP("xbfzrsp"),                    //协办副主任审批
        HAVE_ZJPSF("zjpsf"),                    //是否有专家评审费
        /*一下是用户参数*/
        USER("user"),                    //用户
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
        USER_HQ_LIST("user_hq_list"),    //项目负责人确认列表
        USER_HQ("user_hq"),              //项目负责人确认
        USER_XBBZ_LIST("user_xbbz_list"),//协办部长列表
        USER_XBBZ("user_xbbz"),          //协办部长
        USER_XBFGLD_LIST("user_xbfgld_list"),//协办分管领导列表
        USER_XBFGLD("user_xbbz"),         //协办分管领导
        USER_M("user_m"),                //项目第一负责人
        USER_A("user_a"),                //项目第二负责人
        USER_CW("users_cw"),             //财务
        USER_QRGD("user_qrgd");          //确认归档人员

        private String value;

        SignFlowParams(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

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
    public static final String TOPIC_KTFZR = "TOPIC_KTFZR";              //课题负责人
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
    public static final String TOPIC_KTFZR_QR = "TOPIC_KTFZR_QR";        //课题负责人确认
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
     * 月报简报流程
     */
    public final static String MONTHLY_BULLETIN_FLOW = "MONTHLY_BULLETIN_FLOW";
    /**
     * 月报简报流程环节
     */
    public final static String MONTH_YB = "MONTH_YB";              //月报填报
    public static final String MONTH_BZ = "MONTH_BZ";          //部长审批
    public static final String MONTH_FG = "MONTH_FG";        //分管领导审批
    public static final String MONTH_ZR = "MONTH_ZR";        //主任审批

    /**
     * 通知公告流程
     */
    public final static String ANNOUNT_MENT_FLOW = "ANNOUNT_MENT_FLOW";
    /**
     * 通知公告流程环节
     */
    public final static String ANNOUNT_TZ = "ANNOUNT_TZ";              //通知公告填报
    public static final String ANNOUNT_BZ = "ANNOUNT_BZ";          //部长审批
    public static final String ANNOUNT_FZ = "ANNOUNT_FZ";        //副主任审批
    public static final String ANNOUNT_ZR = "ANNOUNT_ZR";        //主任审批


    /**
     * 流程参数
     */
    public static enum FlowParams{
        ISAGREE("agree"),                //是否同意
        TB_FZ("tbfz"),                   //填报分支，用来判断是否是部长发起
        ZR_FZ("zrfz"),                   //主任分支
        FGLD_FZ("fgldfz"),               //分管领导分支
        BZ_FZ("bzfz"),                    //部长分支
        USER("user"),                    //单个用户
        USERS("users"),                  //多个用户
        USER_BZ("user_bz"),              //部长
        USER_ZHB("user_zhb"),            //综合部人员
        USER_FGLD("user_fgld"),          //分管领导
        USER_ZR("user_zr"),              //主任
        USER_GDY("user_gdy"),            //归档员
        SEND_FGW("send_fgw"),            //送发改委
        MAIN_USER("MAIN_USER"),          //项目主要负责人
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
     * 月报简报流程参数
     */
    public static enum MonthlyNewsletterFlowParams{
        USER("user"),         //用户申请
        USER_BZ("user_bz"),    //部长
        USER_FGLD("user_fgld"),  //分管领导
        USER_ZR("user_zr"), //主任
        MONTH_USER("month_user");//是否部长发起


        private String value;

        MonthlyNewsletterFlowParams(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    /**
     * 通知公告流程参数
     */
    public static enum AnnountMentFLOWParams{
        USER("user"),         //用户申请
        USER_BZ("user_bz"),    //部长
        USER_FZ("user_fz"),  //副主任
        USER_ZR("user_zr"), //主任
        ANNOUNT_USER("annount_user"); //是否部长发起



        private String value;

        AnnountMentFLOWParams(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    public static String getFLowNameByFlowKey(String flowKey){
        String resultValue = "";
        switch (flowKey){
            case SIGN_FLOW:
                resultValue = "【项目签收流程】";
                break;
            case TOPIC_FLOW:
                resultValue = "【课题研究流程】";
                break;
            case BOOKS_BUY_FLOW:
                resultValue = "【图书采购流程】";
                break;
            case ASSERT_STORAGE_FLOW:
                resultValue = "【资产入库流程】";
                break;
            case PROJECT_STOP_FLOW:
                resultValue = "【项目暂停审批流程】";
                break;
            case FLOW_ARCHIVES:
                resultValue = "【档案借阅流程】";
                break;
            case FLOW_APPRAISE_REPORT:
                resultValue = "【优秀评审报告流程】";
                break;
            case FLOW_SUPP_LETTER:
                resultValue = "【拟补充资料函流程】";
                break;
            case MONTHLY_BULLETIN_FLOW:
                resultValue = "【月报简报流程】";
                break;
            case ANNOUNT_MENT_FLOW:
                resultValue = "【通知公告流程】";
                break;
            case WORK_HIS_FLOW:
                resultValue = "【重写工作方案流程】";
                break;
            default:
                    ;
        }
        return resultValue;
    }

    /***************************   流程回退参数 begin  **************************/
    /**
     * 回退环节
     */
    public static final String FLOW_BACK_NODEKEY = "FLOW_BACK_NODEKEY";

    /**
     * 用户人
     */
    public static final String FLOW_BACK_USER = "FLOW_BACK_USER";
    /***************************   合并发文参数 end  **************************/
}
