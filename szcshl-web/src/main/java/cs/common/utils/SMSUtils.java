package cs.common.utils;


import freemarker.ext.beans.HashAdapter;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import com.alibaba.fastjson.JSON;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2018/5/29.
 * 短信工具类
 */
public class SMSUtils {
    private static String GET_URL = "http://172.18.225.30:8080/jxh/open/getAccessToken";

    private static String USER_AGENT = "Mozilla/5.0";

    //加密之后账号/密码
    private static String USER_NAME="334532f2e95b80bf8fe83683a8a0234f";

    private static String USER_PASS="334532f2e95b80bf8fe83683a8a0234f";

    //获取token的时间long
    private static  long GET_TOKEN_TIME =0L;

    private static String TOKEN = null;

    private static long hourOne = 1 * 60 * 60 * 1000;

    private static long hourTwo = 2 * 60 * 60 * 1000;

    private static Logger logger = Logger.getLogger(SMSUtils.class);

    private static  String SM_URL ="http://172.18.225.30:8080/jxh/open/serApi.do?serCode=dzzwdxptdf";

    private static String apiSecret = "dc5bd5fa7dad814200c732948b369928";

    private static  String   mobile = "mobile";

    private static  String   content = "content";

    //http://172.18.225.30:8080/jxh/open/serApi.do?serCode=dzzwdxptdf&accessToken=xxx&apiSecret=xxx&mobile=xxx&content=xxx  发动短息样例URL


    public static boolean isInteger(String str) {
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
        return pattern.matcher(str).matches();
    }

    //发送短信
    public static boolean seekSMS(String mobileNum,String seekContent){
        if (!isInteger(mobileNum)){
            logger.info("seekSMS: 发送的手机号码不是数字类型. "+mobileNum);
            return false;
        }
        CloseableHttpClient httpClient = HttpClients.createDefault();
        Map<String, String> params = new HashMap<>();
        //验证短信内容

        getHttpSMS();
        if (TOKEN == null){
            return false;
        }
        params.put("accessToken",TOKEN);
        params.put("apiSecret",apiSecret);
        params.put("mobile",mobileNum);
        params.put("content",seekContent);

        // 创建http GET请求
        HttpGet httpGet = new HttpGet(doGetParameter(params,SM_URL));
//            httpGet.setConfig(requestConfig);//设置请求参数
        CloseableHttpResponse response = null;
        try {
            // 执行请求
            response = httpClient.execute(httpGet);
            // 判断返回状态是否为200
            if (response.getStatusLine().getStatusCode() == 200) {
                logger.info("seekSMS: 发送成功.手机: "+mobileNum+" 内容: "+seekContent);
                return true;
            }
            httpClient.close();
        } catch (Exception e) {
            logger.info("seekSMS 发送短息服务器异常。"+e.getMessage());
        } finally {

        }
        logger.info("seekSMS 发送短息服务器异常。");
        return false;
    }

    public static boolean isOrTimeout(){
        Date date = new Date();
        long time =  date.getTime()-GET_TOKEN_TIME;
        if (time>hourOne && time<hourTwo ){
            return true;
        }
        return false;
    }



    /**获取token服务*/
    public static String getHttpSMS() {
        //是否超时
        if(isOrTimeout()){
            return TOKEN;
        }
        CloseableHttpClient httpClient = HttpClients.createDefault();
        Map<String, String> params = new HashMap<>();
        params.put("account",USER_NAME);
        params.put("password",USER_PASS);
        // 创建http GET请求
        HttpGet httpGet = new HttpGet(doGetParameter(params,GET_URL));
        CloseableHttpResponse response = null;
        try {
            // 执行请求
            response = httpClient.execute(httpGet);
            // 判断返回状态是否为200
            if (response.getStatusLine().getStatusCode() == 200) {
                String content = EntityUtils.toString(response.getEntity(), "UTF-8");
                Map mapType = JSON.parseObject(content,Map.class);
                TOKEN = (String)mapType.get("accessToken");
                GET_TOKEN_TIME = new Date().getTime();
                System.out.println("content:  " + TOKEN);
            }
            httpClient.close();
        } catch (Exception e) {
            logger.info("getHttpSMS 获取token异常. "+e.getMessage());
        } finally {

        }
        logger.info("getHttpSMS 获取token异常 ");
        return null;
    }


    public static String doGetParameter(Map<String, String> params,String url) {
        URIBuilder uriBuilder =null;
        try{
            uriBuilder = new URIBuilder(url);
            if(params != null){
                for(String key : params.keySet()){
                    uriBuilder.setParameter(key, params.get(key));
                }
            }
            url =  uriBuilder.build().toString();
        }catch (Exception e){
            logger.info("封装 SMSUtils URL异常");
        }
        return url;
    }

    public static void main(String[] args) {
//        getHttpSMS();
        long hourOne = 1 * 60 * 60 * 1000;
        long hourTwo = 2 * 60 * 60 * 1000;
//        isOrTimeout();
        System.out.println(isInteger("1111122334455"));
    }
}
