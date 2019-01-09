package cs.common.utils;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import cs.common.sysprop.BusinessProperties;
import cs.domain.sys.User;
import cs.service.rtx.RTXSendMsgPool;
import cs.service.sys.LogService;
import cs.spring.SpringContextUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.*;

/**
 * 腾讯通
 * Created by ldm on 2018/5/30.
 */
public class RTXUtils {
    private final static String MSG_TITLE = "【评审中心项目管理信息系统】消息提醒";
    private final static String RTX_MSG_NOTICE = "/sendnotify.cgi?";

    /**
     * 异步发送腾讯通消息
     *
     * @param
     * @return
     */
    public static void sendRTXThread(String taskId, List<User> receiverList, String seekContent, LogService logService) {
        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder().setNameFormat("thread-rtxmsg-runner-%d").build();
        ExecutorService threadPool = new ThreadPoolExecutor(1,5,0L, TimeUnit.MILLISECONDS,new LinkedBlockingQueue<Runnable>(),namedThreadFactory);
        //线程池提交一个异步任务
        Future<HashMap<String, String>> future = threadPool.submit(new Callable<HashMap<String, String>>() {
            @Override
            public HashMap<String, String> call() throws Exception {
                //异步任务 不需要直接反应结果，通过日志记录发信状况信息
                sendRTXSMS(taskId, receiverList, seekContent);
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

    //发送腾讯通消息
    public static boolean sendRTXSMS(String taskId, List<User> receiverList, String seekContent) {
        User u = null;
        if (Validate.isList(receiverList)) {
            String rtxNames = "";
            for (int i = 0, l = receiverList.size(); i < l; i++) {
                u = receiverList.get(i);
                if (i > 0) {
                    rtxNames += ",";
                }
                rtxNames += Validate.isString(u.getRtxName()) ? u.getRtxName() : u.getLoginName();
            }
            if (Validate.isString(rtxNames)) {
                //正式启动再去掉注释
                sendRTXMsg(null, seekContent, rtxNames);
                RTXSendMsgPool.getInstance().removeCache(taskId);
                return true;
            }
        }
        return false;
    }

    /**
     * 腾讯通发送消息
     *
     * @param url      服务端地址
     * @param sendMsg  消息
     * @param receiver 接收人（多个用,号隔开）
     * @return
     */
    public static String sendRTXMsg(String url, String sendMsg, String receiver) {
        if (!Validate.isString(url)) {
            BusinessProperties businessProperties = SpringContextUtil.getBean("businessProperties");
            url = businessProperties.getRtxUrl();
        }
        url += RTX_MSG_NOTICE;
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "false";
        try {
            String param = "msg=" + URLEncoder.encode(sendMsg, "GBK")
                    + "&receiver=" + URLEncoder.encode(receiver, "GBK")
                    + "&title=" + URLEncoder.encode(MSG_TITLE, "GBK");
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
            result = "";
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送 POST 请求出现异常！" + e);
        }
        //使用finally块来关闭输出流、输入流
        finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {

            }
        }
        return result;
    }
}
