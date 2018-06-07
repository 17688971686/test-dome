package cs.common.utils;


import cs.common.constants.Constant;
import cs.domain.sys.Log;
import cs.domain.sys.User;
import cs.service.sys.LogService;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2018/5/29.
 * 短信工具类
 */
public class SMSUtils {
    private static String GET_URL = "http://172.18.225.30:8080/jxh/open/getAccessToken";

    private static String USER_AGENT = "Mozilla/5.0";

    //加密之后账号/密码
    private static String USER_NAME = "334532f2e95b80bf8fe83683a8a0234f";

    private static String USER_PASS = "334532f2e95b80bf8fe83683a8a0234f";

    //获取token的时间long
    private static long GET_TOKEN_TIME = 0L;

    private static String TOKEN = null;

    private static long hourOne = 1 * 60 * 60 * 1000;

    private static long hourTwo = 2 * 60 * 60 * 1000;

    private static Logger logger = Logger.getLogger(SMSUtils.class);

    private static String SM_URL_ONE = "http://172.18.225.30:8080/jxh/open/serApi.do?serCode=dzzwdxptdf";

    private static String SM_URL_MANY = "http://172.18.225.30:8080/jxh/open/serApi.do?serCode=dzzwdxptqf";


    private static String apiSecret_one = "dc5bd5fa7dad814200c732948b369928";

    private static String apiSecret_many = "d948e206924748b2d58396ead55108fc";

    private static String mobile = "mobile";

    private static String content = "content";

    private static Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
    //http://172.18.225.30:8080/jxh/open/serApi.do?serCode=dzzwdxptdf&accessToken=xxx&apiSecret=xxx&mobile=xxx&content=xxx  发动短息样例URL
    /**
     * 异步发送短信
     *
     * @param
     * @return
     */
    public static void seekSMSThread(List<User> receiverList, String seekContent, LogService logService) {
        ExecutorService threadPool = Executors.newCachedThreadPool();
        //线程池提交一个异步任务
        Future<HashMap<String, String>> future = threadPool.submit(new Callable<HashMap<String, String>>() {
            @Override
            public HashMap<String, String> call() throws Exception {
                //异步任务 不需要直接反应结果，通过日志记录发信状况信息
                seekSMS(receiverList, seekContent, logService);
                return
                        new HashMap<String, String>() {
                            {
                                this.put("success", "成功获取future异步任务结果");
                            }
                        };
            }
        });
        //关闭线程池
        if (!threadPool.isShutdown()) {
            threadPool.shutdown();
        }
    }

    public static boolean isInteger(String str) {
        return pattern.matcher(str).matches();
    }

    //发送短信
    public static boolean seekSMS(List<User> receiverList, String seekContent, LogService logService) {
        User user = null;
        String phone = "";
        String userName = "";
        if (receiverList.size() == 1) {
            user = receiverList.get(0);
            phone = user.getUserMPhone();
            userName = user.getDisplayName();
            if (!isInteger(phone)) {
                logger.info("seekSMS: 发送的手机号码不是数字类型. " + user.getUserMPhone());
                //记录不手机号码不是数字的用户
                insertLog(user.getDisplayName(), "10001", user.getDisplayName() + "的手机号码不是数字类型", logService,false);
            }
        } else if (receiverList.size() > 1) {
            for (int i = 0, l = receiverList.size(); i < l; i++) {
                user = receiverList.get(i);
                if (StringUtil.isNotEmpty(user.getUserMPhone())) {
                    if (!isInteger(user.getUserMPhone())) {
                        logger.info("seekSMS: 发送的手机号码不是数字类型. " + user.getUserMPhone());
                        //记录不手机号码不是数字的用户
                        insertLog(user.getDisplayName(), "10001", user.getDisplayName() + "的手机号码不是数字类型", logService,false);
                        break;
                    }
                    if (i == receiverList.size() - 1) {
                        phone += user.getUserMPhone();
                        userName += user.getDisplayName();
                    } else {
                        phone += user.getUserMPhone() + ",";
                        userName += user.getDisplayName() + ",";
                    }
                } else {
                    //记录不手机号码为空的用户
                    logger.info("seekSMS: 发送的手机号码不为空 ");
                    insertLog(user.getDisplayName(), "10002", user.getDisplayName() + "的手机号码不为空", logService,false);
                }
            }
        }
        if (StringUtil.isEmpty(phone)) {
            insertLog(userName, "10011", userName + " 的手机号码为空", logService,false);
        }
        //验证短信内容
        TOKEN = getHttpSMS(logService);

        if (TOKEN ==null) {
            insertLog(userName, "10015", userName + ": 获取Token为空 ", logService,false);
            return false;
        }
        CloseableHttpClient httpClient = HttpClients.createDefault();
        Map<String, String> params = new HashMap<>();
        params.put("accessToken", TOKEN);
        params.put("mobile", phone);
        params.put("content", seekContent);
        HttpGet httpGet = null;
        if (receiverList.size() == 1) {
            params.put("apiSecret", apiSecret_one);
            httpGet = new HttpGet(doGetParameter(params, SM_URL_ONE));
        } else {
            params.put("apiSecret", apiSecret_many);
            httpGet = new HttpGet(doGetParameter(params, SM_URL_MANY));
        }
        // 创建http GET请求
        CloseableHttpResponse response = null;
        try {
            // 执行请求
            response = httpClient.execute(httpGet);
            // 判断返回状态是否为200
            if (response.getStatusLine().getStatusCode() == 200) {
                String content = EntityUtils.toString(response.getEntity(), "UTF-8");
                insertLog(userName, "" + response.getStatusLine().getStatusCode(), userName + ": 发送短息成功", logService,true);
                logger.info("seekSMS: 发送成功.手机: " + phone + " 内容: " + seekContent);
                return true;
            }

        } catch (Exception e) {
            logger.info("seekSMS 发送短息服务器异常。" + e.getMessage());
            insertLog(userName, "10010", userName + ": 发送短息异常: " + e.getMessage(), logService,false);
        } finally {
            try {
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public static boolean isOrTimeout() {
        Date date = new Date();
        long time = date.getTime() - GET_TOKEN_TIME;
        if (time < hourOne && time > 0) {
            return true;
        }
        return false;
    }

    public static void insertLog(String userName, String reCode, String msg, LogService logService,boolean success) {
//        LogService logService = SpringContextUtil.getBean("logService");
        if (logService != null) {
            Log log = new Log();
            log.setCreatedDate(new Date());
            log.setUserName(userName);
            log.setLogCode(reCode);
            log.setMessage(msg);
            if(success){
                log.setResult(Constant.EnumState.YES.getValue());
            }else{
                log.setResult(Constant.EnumState.NO.getValue());
            }
            log.setBuninessType("SMS_TYPE");
            logService.save(log);
        }
    }


    /**
     * 获取token服务
     */
    public static String getHttpSMS(LogService logService) {
        //是否超时
        if (isOrTimeout()) {
            return TOKEN;
        }
        CloseableHttpClient httpClient = HttpClients.createDefault();
        Map<String, String> params = new HashMap<>();
        params.put("account", USER_NAME);
        params.put("password", USER_PASS);
        // 创建http GET请求
        HttpGet httpGet = new HttpGet(doGetParameter(params, GET_URL));
        CloseableHttpResponse response = null;
        String tempCode ="";
        try {
            // 执行请求
            response = httpClient.execute(httpGet);
            // 判断返回状态是否为200
            if (response.getStatusLine().getStatusCode() == 200) {
                String content = EntityUtils.toString(response.getEntity(), "UTF-8");
                String[] resCode = content.split("resultCode");
                resCode = resCode[1].split("resultMessage");
                resCode = resCode[0].split("\"");
                tempCode = resCode[2];
                if (content.contains("0000000")) {
                    String[] st = content.split("accessToken");
                    st = st[1].split("expiredValue");
                    st = st[0].split("\"");
                    TOKEN = st[2];
                    logger.info("TOKEN. " + TOKEN);
                    GET_TOKEN_TIME = new Date().getTime();
                    return TOKEN;
                }
            }
            httpClient.close();
        } catch (Exception e) {
            logger.info("getHttpSMS 获取token异常. " + e.getMessage());
            insertLog("", "10004", "获取短信Token失败: " + e.getMessage(), logService,false);
        } finally {

        }
        insertLog("", tempCode, "获取短信平台数据异常请看返回code", logService,false);
        return null;
    }


    public static String doGetParameter(Map<String, String> params, String url) {
        URIBuilder uriBuilder = null;
        try {
            uriBuilder = new URIBuilder(url);
            if (params != null) {
                for (String key : params.keySet()) {
                    uriBuilder.setParameter(key, params.get(key));
                }
            }
            url = uriBuilder.build().toString();
        } catch (Exception e) {
            logger.info("封装 SMSUtils URL异常");
        }
        return url;
    }

    public static void main(String[] args) {

        String d = "{\"data\":{\"accessToken\":\"ED920981FE92F35EB04ACC30CE8840326DE0209A0EFD7E6BCE8F5135C61E9E0DD7B0F42E63075E37\",\"expiredValue\":\"7200\"},\"resultCode\":\"0000000\",\"resultMessage\":\"成功\"}";

//        getHttpSMS();
//    SMSUtils.seekSMSThread(getListUser("发文失败"),"发文失败,发送短信。项目名称: "+sign.getFilecode()+"\n"+"  【评审中心项目管理系统】",  logService);
        List<User> list = new ArrayList<>();
        User user = new User();
        user.setDisplayName("道花");
        ;
        user.setUserMPhone("18577186194");
        list.add(user);

        User user3 = new User();
        user3.setDisplayName("道花2");
        ;
        user3.setUserMPhone("18785027849");
        list.add(user3);
        User user2 = new User();
        user2.setDisplayName("苍井空");
        ;
        user2.setUserMPhone("18038078167");
        list.add(user2);
//        User user5 = new User();
//        user5.setDisplayName("大海");
//        ;
//        user5.setUserMPhone("15999654019");
//        list.add(user5);
//        seekSMS(list," 创势科技项目2 ",null);
        seekSMSThread(list, "\n"+"发文失败。"+"\n"+"项目名称:A1001DFD1 "+"\n"+"  【评审中心项目管理系统】", null);

    }
}
