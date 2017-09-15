package cs.service.rtx;

import cs.common.Constant;
import cs.common.ResultMsg;
import cs.common.utils.PropertyUtil;
import cs.common.utils.Validate;
import cs.domain.sys.User;
import cs.repository.repositoryImpl.sys.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;


@Service
public class RTXService {
    private final static String MSG_TITLE = "【评审中心项目管理信息系统】消息提醒";
    private final static String RTX_MSG_NOTICE = "/sendnotify.cgi?";
    private final static String RTX_GETSESSION = "/GetSession.cgi?";
    private final static String RTX_GETSTATUS = "/getstatus.php?";

    @Autowired
    private UserRepo userRepo;
    /**
     * 获取腾讯通的sessionKey
     * @param url           腾讯通URL
     * @param loginUser     登录用户
     * @return
     */
    public String getSessionKey(String url,String loginUser){
        String strSessionKey = "";
        if(!Validate.isString(url)){
            PropertyUtil propertyUtil = new PropertyUtil(Constant.businessPropertiesName);
            url = propertyUtil.readProperty("RTX_URL");
        }
        url += RTX_GETSESSION;
        try{
            url += "receiver=" + URLEncoder.encode(loginUser, "GBK");
            java.net.URL loginUrl = new URL(url);
            HttpURLConnection httpConnection = (HttpURLConnection)loginUrl.openConnection();
            BufferedReader reader = new BufferedReader(new InputStreamReader(httpConnection.getInputStream()));
            strSessionKey=reader.readLine();
        }catch(Exception e){
            System.out.println("获取腾讯通sessionKey异常："+e);
        }
        return strSessionKey;
    }

    /**
     * 获取用户在线状态仅支持GET
     * @param url           腾讯通URL
     * @param loginUser     登录用户
     * @return String       0不在线 1在线 2离线 3异常
     * @example http://localhost:8012/getstatus.php?username=XXXX
     */
    public String queryUserState(String url,String loginUser){
        String userState = "0";
        if(!Validate.isString(url)){
            PropertyUtil propertyUtil = new PropertyUtil(Constant.businessPropertiesName);
            url = propertyUtil.readProperty("RTX_URL");
        }
        url += RTX_GETSTATUS;
        try{
            url += "username=" + URLEncoder.encode(loginUser, "GBK");
            java.net.URL loginUrl = new URL(url);
            HttpURLConnection httpConnection = (HttpURLConnection)loginUrl.openConnection();
            BufferedReader reader = new BufferedReader(new InputStreamReader(httpConnection.getInputStream()));
            userState=reader.readLine();
        }catch(Exception e){
            System.out.println("获取用户在线状态异常："+e);
            userState = "3";
        }
        return userState;
    }

    /**
     * 发送消息缓冲池
     * @param taskId
     * @param resultMsg
     * @return
     */
    public boolean dealPoolRTXMsg(String taskId, ResultMsg resultMsg){
        if(resultMsg.isFlag() && RTXSendMsgPool.getInstance().getReceiver(taskId) != null){
            String receiverIds = RTXSendMsgPool.getInstance().getReceiver(taskId).toString();
            List<User> receiverList = userRepo.getCacheUserListById(receiverIds);
            if(Validate.isList(receiverList)){
                String rtxNames = "";
                for(User u : receiverList){
                    if(Validate.isString(rtxNames)){
                        rtxNames += ",";
                    }
                    rtxNames += u.getLoginName();
                }
                if(Validate.isString(rtxNames)){
                    sendRTXMsg(null,"您有待办任务待处理！",rtxNames);
                    RTXSendMsgPool.getInstance().removeCache(taskId);
                    return true;
                }
            }
            return false;
        }else{
            RTXSendMsgPool.getInstance().removeCache(taskId);
        }
        return true;
    }

    /**
     * 腾讯通发送消息
     * @param url           服务端地址
     * @param sendMsg       消息
     * @param receiver      接收人（多个用,号隔开）
     * @return
     */
    public  String sendRTXMsg(String url, String sendMsg,String receiver) {
        if(!Validate.isString(url)){
            PropertyUtil propertyUtil = new PropertyUtil(Constant.businessPropertiesName);
            url = propertyUtil.readProperty("RTX_URL");
        }
        url += RTX_MSG_NOTICE;
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            String param = "msg=" + URLEncoder.encode(sendMsg, "GBK")
                    +"&receiver=" + URLEncoder.encode(receiver, "GBK")
                    +"&title=" + URLEncoder.encode(MSG_TITLE, "GBK");
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            HttpURLConnection conn = (HttpURLConnection) realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // 发送请求参数
            out.print(param);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送 POST 请求出现异常！"+e);
            return "false";
        }
        //使用finally块来关闭输出流、输入流
        finally{
            try{
                if(out!=null){
                    out.close();
                }
                if(in!=null){
                    in.close();
                }
            }
            catch(IOException ex){
                return "false";
            }
        }
        return result;
    }
}
