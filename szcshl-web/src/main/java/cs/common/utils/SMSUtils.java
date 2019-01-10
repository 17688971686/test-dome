package cs.common.utils;


import com.google.common.util.concurrent.ThreadFactoryBuilder;
import cs.common.constants.Constant;
import cs.common.constants.FlowConstant;
import cs.domain.sys.SMSLog;
import cs.domain.sys.User;
import cs.service.sys.MsgService;
import cs.threadtask.MsgThread;
import org.apache.log4j.Logger;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.*;

/**
 * Created by ldm on 2018/5/29.
 * 短信工具类
 */
public class SMSUtils {
    private static Logger logger = Logger.getLogger(SMSUtils.class);
    /**
     * 单条发送短信地址（旧地址）
     */
    public static final String SM_URL = "http://172.18.225.30:8080/jxh/open/serApi.do";

    /**
     * 单条发送短信地址（新地址）
     */
    public static final String SM_ONE_URL = "http://172.18.225.31:9016/N/SmsApi/SendSmsToUser";

    /**
     * 多条发送地址（新地址）
     */
    public static final String SM_MORE_URL = "http://172.18.225.31:9016/N/SmsApi/SendSmsToGroupUser";

    /**
     * 获取token地址
     */
    public static final String GET_TOKEN_URL = "http://172.18.225.30:8080/jxh/open/getAccessToken";

    /**
     * 获取token地址(新地址)
     */
    public static final String GET_TOKEN_URL_NEW = "http://172.18.225.31:9016/N/BasicApi/GetAccessToken";
    public static final String GET_TOKEN_APPID_NEW = "31ad58a4247d40b2940fc6a416b5d1ad";
    public static final String GET_TOKEN_APPSECRET_NEW = "f3435e901865420b937f2dc2161e3520";

    /**
     * 单条短信的sercode
     */
    public static final String ONE_SERCODE = "dzzwdxptdf";
    /**
     * 多条短信的sercode
     */
    public static final String MANY_SERCODE = "dzzwdxptqf";
    /**
     * 单条发送密钥
     */
    public static final String apiSecret_one = "dc5bd5fa7dad814200c732948b369928";
    /**
     * 多条发送密钥
     */
    public static final String apiSecret_many = "d948e206924748b2d58396ead55108fc";

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
     * 发送手机短信
     * @param msgService
     * @param recvUserList
     * @param msgContent
     * @param smsLog
     */
    public static void sendMsg(MsgService msgService, List<User> recvUserList, String msgContent, SMSLog smsLog) {
        //手动创建线程池
        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder().setNameFormat("thread-sendsms-runner-%d").build();
        ExecutorService threadPool = new ThreadPoolExecutor(1,5,0L, TimeUnit.MILLISECONDS,new LinkedBlockingQueue<Runnable>(),namedThreadFactory);
        threadPool.execute(new MsgThread(msgService,recvUserList,msgContent,smsLog));
        threadPool.shutdown();
    }

    /**
     * token实现返回码
     */
    public static final String TOKEN_UNVALIABLE_CODE = "0190007";

    /**
     * 验证短信发送方式
     * @param msgTypeDto
     * @return
     */
    public static boolean checkMsgType(SysConfigDto msgTypeDto) {
        if(Validate.isObject(msgTypeDto) && String.valueOf(SEND_MSG_TYPE.OLD.ordinal()).equals(msgTypeDto.getConfigValue())){
            return false;
        }
        return true;
    }

    /**
     * 短信发送方式
     */
    public enum SEND_MSG_TYPE {
        //预留
        UNDEFIND,
        //旧方式
        OLD,
        //新方式
        NEW
    }

    /**
     * 短信发送返回码
     */
    public enum RESULT_CODE {
        //失败
        ERROR,
        //成功
        SUCCESS
    }

    /**
     * 返回参数名称
     */
    public enum MSG_PARAMS{
        //返回信息
        resultMsg,
        //返回码
        resultCode,
        //返回数据
        resultData,
        //返回token
        accessToken,
        //token信息
        tokenMsg,
        //电话号码
        Phone,
        //短信内容
        SmsContent,
        //密钥
        authorSecret,
        /**
         * 认证平台，预先申请的appid
         */
        appid,
        /**
         * 认证平台，预先申请的服务密钥
         */
        appsecret
    }

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
            case "0200001":
                codeMsg = "发送失败";
                break;
            default:
                ;
        }
        return codeMsg;
    }

    public static String buildSendMsgContent(String flowTaskDefindKey,String msgType, String procInstName,boolean isSucess) {
        StringBuffer stringBuffer = new StringBuffer();
        Constant.MsgType typeEnum = Constant.MsgType.valueOf(msgType);
        switch (typeEnum) {
            case task_type:
                stringBuffer.append("\n 您收到一条待办任务。\n 任务名称:" + procInstName + ",请及时处理").append("\n");
                break;
            case project_type:
                stringBuffer.append("\n 您收到一条待办项目。\n 项目名称:" + procInstName + ",请及时处理").append("\n");
                if(Validate.isString(flowTaskDefindKey)){
                    switch (flowTaskDefindKey){
                        //部长分办环节，短信内容后面加上“请到综合部领取项目资料”；
                        case FlowConstant.FLOW_SIGN_BMFB1:
                        case FlowConstant.FLOW_SIGN_BMFB2:
                        case FlowConstant.FLOW_SIGN_BMFB3:
                        case FlowConstant.FLOW_SIGN_BMFB4:
                            stringBuffer.append("请到综合部领取项目资料").append("\n");
                            break;
                            default:
                                ;
                    }
                }
                break;
            case incoming_type: //委里推送项目
                stringBuffer.append("\n 您收到一条信息(委里推送项目提示): \n"+procInstName);
                if(isSucess){
                    stringBuffer.append(" 推送成功！");
                }else{
                    stringBuffer.append(" 推送失败！");
                }
                stringBuffer.append("\n");
                break;
            case sendfgw_type:
                stringBuffer.append("\n 您收到一条信息(项目回传委里提示)：\n"+procInstName).append("\n");
                break;
            default:
                ;
        }
        stringBuffer.append(COMPANY_SIGN);
        return stringBuffer.toString();
    }


    /**
     * 获取返回码
     * @param httpResult
     * @return
     */
    public static String analysisResult(String httpResult) {
        if(Validate.isString(httpResult)){
            JSONObject json = new JSONObject(httpResult);
            if(Validate.isObject(json) ){
                JSONObject jo = json.getJSONObject("data");
                String resultCode = jo.getString("code");
                return resultCode;
            }
        }
        return "";
    }

    /**
     * 重置token信息
     */
    public static void resetTokenInfo(String token,long newTime,long expireValue) {
        setTOKEN(token);
        setGetTokenTime(newTime);
        setTokenExpireValue(expireValue);
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

    public static boolean isSendTime() {
        Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        if(hour > 7 && hour < 20){
            return true;
        }else{
            return false;
        }
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
