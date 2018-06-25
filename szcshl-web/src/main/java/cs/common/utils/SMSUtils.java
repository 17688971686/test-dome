package cs.common.utils;


import cs.common.constants.Constant;
import cs.domain.sys.SMSLog;
import cs.domain.sys.User;
import cs.model.sys.SysConfigDto;
import cs.service.sys.SMSContent;
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

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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

    private static Map<String, String> smsMap = new ConcurrentHashMap<>();
    /**
     * 异步发送短信
     * @param
     * @return
     */
    public static void seekSMSThread(SMSContent smsContent, List<User> receiverList, String projectName, String filecode, String type, String infoType, String seekContent, SMSLogService smsLogService) {
            ExecutorService threadPool = Executors.newCachedThreadPool();
            //线程池提交一个异步任务
            Future<HashMap<String, String>> future = threadPool.submit(new Callable<HashMap<String, String>>() {
                @Override
                public HashMap<String, String> call() throws Exception {
//                    异步任务 不需要直接反应结果，通过日志记录发信状况信息
                    logger.info("进入发送短信异步:  "+smsContent);
                    synchronized(projectName){
                        boolean  boo = seekSMS(smsContent,receiverList, projectName, filecode, type, infoType,seekContent, smsLogService);
                        logger.info("seekSMS: 返回调用结果. " + boo);
                        return
                                new HashMap<String, String>() {
                                    {
                                        this.put(""+projectName, String.valueOf(boo));
                                    }
                                };
                    }
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
    /**
     * 将限制短信次数放到短信发送之前
     */
    public  static boolean seekSMS(SMSContent smsContent,List<User> receiverList,String projectName,String filecode,String type,String infoType,String seekContent,SMSLogService smsLogService) {
        //如果是待办不短信次数
        if("待办".equals(infoType)){
            return sureSendSMS(smsContent,receiverList,projectName,filecode,type,infoType,seekContent,smsLogService);
        }

        //获取打开限制次数
        if("打开限制次数".equals(smsContent.orNotsendSMS(receiverList,projectName,filecode,type,infoType,"打开限制次数"))){
            //限制次数开始
            if(smsContent.querySmsNumber(receiverList,projectName,filecode,type,infoType,null)== null){
                logger.info("seekSMS:进入限制次数判断" );
                return sureSendSMS(smsContent,receiverList,projectName,filecode,type,infoType,seekContent,smsLogService);

            }
        }
        if("关闭限制次数".equals(smsContent.orNotsendSMS(receiverList,projectName,filecode,type,infoType,"关闭限制次数"))){
            logger.info("seekSMS:进入关闭限制次数判断" );
            return sureSendSMS(smsContent,receiverList,projectName,filecode,type,infoType,seekContent,smsLogService);
        }
        return  false;
    }
    public static boolean sureSendSMS(SMSContent smsContent,List<User> receiverList,String projectName,String filecode,String type,String infoType,String seekContent,SMSLogService smsLogService){
        //组装接收短信用户信息
        User user = null;
        String resultCode="";//phone = "",userName = "",
        //获取电话号码
        packList(receiverList);
        //验证短信内容
        TOKEN = getHttpSMS(userName,phone,projectName,filecode,seekContent,smsLogService);
        if (TOKEN ==null) {
            logger.info("sureSendSMS: TOKEN为空" );
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
                String[] resCode = content.split("resultCode");
                resCode = resCode[1].split("resultMessage");
                resCode = resCode[0].split("\"");
                resultCode = resCode[2];
                if ("0000000".equals(resultCode)) {
                    logger.info("sureSendSMS: 成功: "+resultCode );
                    insertLog(userName,phone,projectName,filecode,resultCode, type, infoType,seekContent,smsLogService,true);
                    return true;
                }else{
                    logger.info("sureSendSMS: 失败: "+resultCode );
                    insertLog(userName,phone,projectName,filecode,resultCode, type, infoType,seekContent,smsLogService,false);
                    return false;
                }
            }
        } catch (Exception e) {
            seekContent="\n 通信异常";
            logger.info("sureSendSMS: 通信异常: " );
            insertLog(userName,phone,projectName,filecode,resultCode, "custom_type", "通信异常",seekContent,smsLogService,false);
            return  false;
        } finally {
            try {
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
    public static String phone ="";
    public static String userName ="";
    public static void packList(List<User> receiverList){
        phone = "";
        userName ="";
        User user = null;
       if (receiverList.size() > 1) {
            for (int i = 0, l = receiverList.size(); i < l; i++) {
                user = receiverList.get(i);
                if (StringUtil.isNotEmpty(user.getUserMPhone())) {
                    if (i == receiverList.size() - 1) {
                        phone += user.getUserMPhone().trim();
                        userName += user.getDisplayName().trim();
                    } else {
                        phone += user.getUserMPhone().trim() + ",";
                        userName += user.getDisplayName().trim() + ",";
                    }
                }
            }
        }else {
           phone =null;
           userName =null;
       }
    }

    public static boolean isOrTimeout() {
        Date date = new Date();
        long time = date.getTime() - GET_TOKEN_TIME;
        if (time < hourOne && time > 0) {
            return true;
        }
        return false;
    }

    public static void insertLog( String userName,String smsUserPhone,String projectName,String filecode,String resultCode,String type,String infoType,String seekContent, SMSLogService smsLogService,boolean success) {
        //写入短信日志表
        if (smsLogService != null) {
            //查看当前信息是否存在
            SMSLog smsLog = smsLogService.querySMSLog(userName,smsUserPhone,projectName, filecode, resultCode, type, infoType, seekContent,smsLogService,success);
            if (smsLog != null){
                smsLog.setModifiedDate(new Date());
                List<SMSLog> list = new ArrayList<>();
                list.add(smsLog);
                smsLogService.update(list);
            }else{
                smsLog = new SMSLog();
                smsLog.setObject(userName,smsUserPhone,projectName, filecode, resultCode, type, infoType, seekContent,  success);
                smsLogService.save(smsLog);
            }
        }
    }
    /**
     * 获取token服务
     */
    public static String getHttpSMS(String userName,String phone,String projectName,String filecode,String seekContent,SMSLogService smsLogService) {
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
            logger.info("getHttpSMS 获取token异常. " + e.getMessage()+" tempCode："+tempCode );
        }
        insertLog(userName,phone,projectName,filecode,tempCode, "custom_type", "获取短信平台Token异常",seekContent,smsLogService,false);
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
            logger.info("封装 SMSUtils URL异常"+e.getMessage());
        }
        return url;
    }

    /**
     * 判断日期是周不是周末
     * 默认周六、日为休息日
     * 其他通过工作管理获取判断
     * @param date
     * @return 0-星期日
     */
    public static boolean getWeek(WorkdayService workdayService, Date date, SysConfigService sysConfigService) {
        boolean boo =  workdayService.isRepeat(new Date());
        //如果不存在将默认采取周末机制
        if (boo){
            boo =  workdayService.isRepeat(new Date());
            if (boo){
                return calculateTime(sysConfigService,date);
            }else {
                return false;
            }
        }
        Calendar calendarTemp = Calendar.getInstance();
        try {
            calendarTemp.setTime(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        int i = calendarTemp.get(Calendar.DAY_OF_WEEK);
        int value=i-1;//0-星期日
        if (value==6||value==0) {//周六，周日
            return false;
        }
        return calculateTime(sysConfigService,date);
    }
    public static boolean calculateTime(SysConfigService sysConfigService,Date date){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date nowTime = simpleDateFormat.parse(new SimpleDateFormat("yyyy-MM-dd").format(date));
            //如果工作日时间是8点到晚上12点; 在配置系统中配置时间
            //WORK_START_TIME  WORK_END_TIME
            List<SysConfigDto>  sysConfigDtoList = sysConfigService.findListBykey("WORK_START_TIME");
            String startTime =  workDayStartTime(sysConfigService);
            String endTime = workDayEndTime(sysConfigService);
            long times = nowTime.getTime()+(Integer.valueOf(startTime) * 60 * 60 * 1000);
            long times1 = nowTime.getTime()+(Integer.valueOf(endTime) * 60 * 60 * 1000);
            if ( date.getTime()>times && date.getTime()<times1){
                return true;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }


    public static String workDayStartTime(SysConfigService sysConfigService){
        //如果工作日时间是8点到晚上12点; 在配置系统中配置时间
        List<SysConfigDto>  sysConfigDtoList = sysConfigService.findListBykey("WORK_START_TIME");
        String startTime = "";
        for(SysConfigDto sysConfigDto: sysConfigDtoList){
            startTime = sysConfigDto.getConfigValue();
        }
        return  startTime;
    }
    public static  String workDayEndTime(SysConfigService sysConfigService){
        //如果工作日时间是8点到晚上12点; 在配置系统中配置时间
        List<SysConfigDto>  sysConfigDtoList = sysConfigService.findListBykey("WORK_END_TIME");
        String endTime = "";
        for(SysConfigDto sysConfigDto: sysConfigDtoList){
            endTime = sysConfigDto.getConfigValue();
        }
        return  endTime;
    }
    public static void main(String[] args) {

        String d = "{\"data\":{\"accessToken\":\"ED920981FE92F35EB04ACC30CE8840326DE0209A0EFD7E6BCE8F5135C61E9E0DD7B0F42E63075E37\",\"expiredValue\":\"7200\"},\"resultCode\":\"0000000\",\"resultMessage\":\"成功\"}";

//      getHttpSMS();
        List<User> list = new ArrayList<>();
        User user = new User();
        user.setDisplayName("郭冬冬");
        ;
        user.setUserMPhone("13640950289");
        list.add(user);

        User user3 = new User();
        user3.setDisplayName("开发者");
        ;
        user3.setUserMPhone("18038078167");
        list.add(user3);
//
//        for (int i =0 ;i<10;i++){
        //此测试需要自己在获取 接收短信用户
//             SMSUtils.seekSMSThread(null,null,"发文失败",list, "测试项目", "A001","发文成功","发文成功", "\n"+"发文失败。"+"\n"+"项目名称:A1001DFD1 "+"\n"+"  【评审中心项目管理系统】", null);
//   SMSContent smsContent,SysConfigService sysConfigService,String sysConfiType, List<User> receiverList, String projectName, String filecode, String type, String infoType, String seekContent, SMSLogService smsLogService

//            System.out.println("boo=   ");
//        }

    }
}
