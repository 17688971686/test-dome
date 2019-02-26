package cs.common.constants;

/**
 * Description: 系统常量
 * Author: ldm
 * Date: 2018/04/18
 */
public final class SysConstants {
    /**
     * 分隔符：逗号
     */
    public final static String SEPARATE_COMMA = ",";

    /**
     * 分隔符: 分号
     */
    public final static String SEPARATE_COLON = ";";

    /**
     * 日期链接符
     */
    public final static String DATE_COLON = "-";

    /**
     * 等号
     */
    public final static String EQ_COLON = "=";

    /**
     * 默认
     */
    public static final String DEFAULT = "default";
    /**
     * UTF-8编码
     */
    public static final String UTF8 = "UTF-8";
    /**
     * GBK编码
     */
    public static final String GBK = "GBK";
    /**
     * ISO88591编码
     */
    public static final String  ISO88591 = "ISO-8859-1";
    /**
     * cookie中的JSESSIONID名称
     */
    public static final String JSESSION_COOKIE = "JSESSIONID";
    /**
     * url中的jsessionid名称
     */
    public static final String JSESSION_URL = "jsessionid";
    /**
     * HTTP POST请求
     */
    public static final String POST = "POST";
    /**
     * HTTP GET请求
     */
    public static final String GET = "GET";

    /**
     * 默认系统管理员账号
     */
    public static final String SUPER_ACCOUNT = "admin";


    /**
     * 系统默认密码
     */
    public static final String DEFAULT_PASSWORD = "1";


    /**
     * 默认忽略不用拷贝的属性
     */
    public static final String[] defaultIgnore = new String[]{"createdDate","createdBy","modifiedDate","modifiedBy"};

    /**
     * 系统读取属性文件的bean名称
     */
    public final static String SYS_BUSI_PROP_BEAN = "businessProperties";
    /**
     * 系统参数配置key值
     */
    public enum SYS_CONFIG_ENUM {
        /**
         * 上一个抽签单位的最大序号
         */
        LAST_UNIT_MAXSORT,
        /**
         * 要校验的文件名
         */
        CHECKFILE,
        /**
         * 在用的文件服务器IP地址（多个的时候可以选择）
         */
        FTPIP,
        /**
         * 文件服务器根目录
         */
        FTPROOT,
        /**
         * 是否使用腾讯通账号
         */
        RTX_ENABLED,
        /**
         * 回传给委里的接口配置
         */
        RETURN_FGW_URL,
        /**
         * 预签收接口配置
         */
        FGW_PRE_PROJECT_IFS,
        /**
         * 系统参数: 短息开关0:打开，1:关闭
         */
        SMS_SYS_TYPE,
        /**
         * 委里项目推送短信通知人配置
         */
        SMS_SING_NOTICE_USER,
        /**
         * 回传委里失败推送的短信通知人配置
         */
        SMS_SENDFGW_FAIL_USER,
        /**
         * 缓存地址
         */
        FILE_UPLOAD_PATH,
        /**
         * 系统发送短信方式（1：旧方式，2：新方式）
         */
        MSG_TYPE,
        /**
         * 系统访问地址
         */
        LOCAL_URL;
    }

    /**
     * V_ORG_DEPT 视图中， type字段标识说明
     */
    public enum ORGDEPT_TYPE_ENUM{
        /**
         * org表示部门
         */
        org,
        /**
         * dept表示组别
         */
        dept
    }
}