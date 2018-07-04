package cs.common.utils;


import cs.common.constants.Constant;
import cs.domain.sys.SMSLog;
import cs.domain.sys.User;
import cs.model.sys.SysConfigDto;
import cs.service.sys.SMSLogService;
import cs.service.sys.SysConfigService;
import cs.service.sys.WorkdayService;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.*;

/**
 * Created by Administrator on 2018/5/29.
 * 短信工具类
 */
public class SMSUtils {
    private static Logger logger = Logger.getLogger(SMSUtils.class);
    /**
     * 单条发送短信地址
     */
    public static final String SM_URL_ONE = "http://172.18.225.30:8080/jxh/open/serApi.do?serCode=dzzwdxptdf";
    /**
     * 多条发送短信地址
     */
    public static final String SM_URL_MANY = "http://172.18.225.30:8080/jxh/open/serApi.do?serCode=dzzwdxptqf";
    /**
     * 单条发送密钥
     */
    public static final String apiSecret_one = "dc5bd5fa7dad814200c732948b369928";
    /**
     * 多条发送密钥
     */
    public static final String apiSecret_many = "d948e206924748b2d58396ead55108fc";

    /**
     * 获取token地址
     */
    public static final String GET_TOKEN_URL = "http://172.18.225.30:8080/jxh/open/getAccessToken";

    /**
     * MD5加密之后的账号密码，（但龙）
     */
    public static final String USER_NAME = "334532f2e95b80bf8fe83683a8a0234f";

    public static final String USER_PASS = "334532f2e95b80bf8fe83683a8a0234f";

    /**
     * 最新获取token时间
     */
    private static long GET_TOKEN_TIME = 0L;

    /**
     * token存活时长（秒）
     */
    private static long TOKEN_EXPIRE_VALUE = 0L;


    private static String TOKEN = null;

    public static final String COMPANY_SIGN = "【评审中心项目管理系统】";

    /**
     * 根据消息码，返回特定的内容
     *
     * @param code
     * @return
     */
    public static String getMsgInfoByCode(String code) {
        String codeMsg = "";
        switch (code) {
            case "0000000":
                codeMsg = "成功";
                break;
            case "0190001":
                codeMsg = "系统异常";
                break;
            case "0190002":
                codeMsg = "缺少参数";
                break;
            case "0190003":
                codeMsg = "服务不存在";
                break;
            case "0190004":
                codeMsg = "服务已停用";
                break;
            case "0190005":
                codeMsg = "账号不存在或者密码错误";
                break;
            case "0190006":
                codeMsg = "账号已停用";
                break;
            case "0190007":
                codeMsg = "accessToken 无效";
                break;
            case "0190008":
                codeMsg = "当前IP被拒绝";
                break;
            case "0190009":
                codeMsg = "当前账号被拒绝访问";
                break;
            case "0190010":
                codeMsg = "账户没有订阅该服务";
                break;
            case "0190020":
                codeMsg = "缺少部分请求参数";
                break;
            case "0190023":
                codeMsg = "请求参数不合法";
                break;
            case "0190024":
                codeMsg = "获取accessToken的次数已达到上限";
                break;
        }

        return codeMsg;
    }

    public static String buildSendMsgContent(String msgType, String procInstName) {
        StringBuffer stringBuffer = new StringBuffer("\n 您收到一条待办");
        Constant.MsgType typeEnum = Constant.MsgType.valueOf(msgType);
        switch (typeEnum) {
            case task_type:
                stringBuffer.append("任务。\n 任务名称:" + procInstName + ",请及时处理").append("\n");
                break;
            case project_type:
                stringBuffer.append("项目。\n 项目名称:" + procInstName + ",请及时处理").append("\n");
                break;
            default:
                ;
        }
        stringBuffer.append(COMPANY_SIGN);
        return stringBuffer.toString();
    }

    /**
     * 封装短信发送失败内容
     *
     * @param projectName
     * @param fileCode
     * @param type
     */
    public String seekSMSSuccee(String projectName, String fileCode, String type) {
        StringBuffer stringBuffer = new StringBuffer("\n 您收到一条信息:");
        if (type.contains("收文")) {
            stringBuffer.append(type + "。\n 项目名称:" + projectName + "(" + fileCode + ")").append("\n");
        }
        if (type.contains("发文")) {
            stringBuffer.append(type + "。\n 项目名称:" + projectName + "(" + fileCode + ")").append("\n");
        }
        stringBuffer.append(COMPANY_SIGN);
        return stringBuffer.toString();
    }

    /**
     * 判断token是否已到期
     * @return
     */
    public static boolean isTokenTimeout() {
        if (GET_TOKEN_TIME == 0) {
            return true;
        }
        long time = (new Date()).getTime() - GET_TOKEN_TIME;
        //提前5分钟获取新token
        if (time > 0 && time < (TOKEN_EXPIRE_VALUE - 300) * 1000) {
            return false;
        }
        return true;
    }

    public static long getGetTokenTime() {
        return GET_TOKEN_TIME;
    }

    public static void setGetTokenTime(long getTokenTime) {
        GET_TOKEN_TIME = getTokenTime;
    }

    public static long getTokenExpireValue() {
        return TOKEN_EXPIRE_VALUE;
    }

    public static void setTokenExpireValue(long tokenExpireValue) {
        TOKEN_EXPIRE_VALUE = tokenExpireValue;
    }

    public static String getTOKEN() {
        return TOKEN;
    }

    public static void setTOKEN(String TOKEN) {
        SMSUtils.TOKEN = TOKEN;
    }
}
