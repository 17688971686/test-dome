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
     * 日期链接副
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
}