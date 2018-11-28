package cs.service.rtx;

import cs.common.ResultMsg;
import cs.common.constants.Constant;
import cs.common.utils.*;
import cs.domain.sys.SMSLog;
import cs.domain.sys.User;
import cs.model.sys.SysConfigDto;
import cs.repository.repositoryImpl.sys.UserRepo;
import cs.service.restService.SignRestService;
import cs.service.sys.LogService;
import cs.service.sys.MsgService;
import cs.service.sys.SMSLogService;
import cs.service.sys.SysConfigService;
import cs.threadtask.MsgThread;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static cs.common.constants.Constant.RevireStageKey.RTX_ENABLED;
import static cs.common.constants.Constant.RevireStageKey.SMS_SYS_TYPE;


@Service
public class RTXService {
    private static Logger logger = Logger.getLogger(RTXService.class);
    private final static String RTX_GETSESSION = "/GetSession.cgi?";
    private final static String RTX_GETSTATUS = "/getstatus.php?";

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private SysConfigService sysConfigService;

    @Autowired
    private LogService logService;

    @Autowired
    private MsgService msgService;


    /**
     * 获取腾讯通的sessionKey
     *
     * @param url       腾讯通URL
     * @param loginUser 登录用户
     * @return
     */
    public String getSessionKey(String url, String loginUser) {
        String strSessionKey = "";
        BufferedReader reader = null;
        if (!Validate.isString(url)) {
            PropertyUtil propertyUtil = new PropertyUtil(Constant.businessPropertiesName);
            url = propertyUtil.readProperty("RTX_URL");
        }
        url += RTX_GETSESSION;
        try {
            url += "receiver=" + URLEncoder.encode(loginUser, "GBK");
            java.net.URL loginUrl = new URL(url);
            HttpURLConnection httpConnection = (HttpURLConnection) loginUrl.openConnection();
            reader = new BufferedReader(new InputStreamReader(httpConnection.getInputStream()));
            strSessionKey = reader.readLine();
        } catch (Exception e) {
            System.out.println("获取腾讯通sessionKey异常：" + e);
        }finally {
            if(Validate.isObject(reader)){
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return strSessionKey;
    }

    /**
     * 获取用户在线状态仅支持GET
     *
     * @param url       腾讯通URL
     * @param loginUser 登录用户
     * @return String       0不在线 1在线 2离线 3异常
     * @example http://localhost:8012/getstatus.php?username=XXXX
     */
    public String queryUserState(String url, String loginUser) {
        String userState = "0";
        BufferedReader reader = null;
        if (!Validate.isString(url)) {
            PropertyUtil propertyUtil = new PropertyUtil(Constant.businessPropertiesName);
            url = propertyUtil.readProperty("RTX_URL");
        }
        url += RTX_GETSTATUS;
        try {
            url += "username=" + URLEncoder.encode(loginUser, "GBK");
            java.net.URL loginUrl = new URL(url);
            HttpURLConnection httpConnection = (HttpURLConnection) loginUrl.openConnection();
            reader = new BufferedReader(new InputStreamReader(httpConnection.getInputStream()));
            userState = reader.readLine();
        } catch (Exception e) {
            System.out.println("获取用户在线状态异常：" + e);
            userState = "3";
        }finally {
            if(Validate.isObject(reader)){
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return userState;
    }

    /**
     * 发送消息缓冲池
     *
     * @param taskId
     * @param resultMsg
     * @return
     */
    public boolean dealPoolRTXMsg(String taskId, ResultMsg resultMsg, String procInstName, String msgType) {
        Object obj = RTXSendMsgPool.getInstance().getReceiver(taskId);
        if (Validate.isObject(obj)) {
            String receiverIds = obj.toString();
            List<User> receiverList = userRepo.getCacheUserListById(receiverIds);
            String msgContent = SMSUtils.buildSendMsgContent(msgType,procInstName,true);
            //短息开关
            if (rtxSMSEnabled() && resultMsg.isFlag()) {
                if (Validate.isList(receiverList)) {
                    SMSLog smsLog = new SMSLog();
                    smsLog.setSmsLogType(msgType);
                    smsLog.setProjectName(procInstName);
                    smsLog.setBuninessId(taskId);

                    ExecutorService threadPool = Executors.newSingleThreadExecutor();
                    threadPool.execute(new MsgThread(msgService,receiverList,msgContent,smsLog));
                    threadPool.shutdown();
                }
            }
            //如果使用腾讯通，并处理成功！
            if (rtxEnabled() && resultMsg.isFlag() && RTXSendMsgPool.getInstance().getReceiver(taskId) != null) {
                RTXUtils.sendRTXThread(taskId, receiverList, msgContent, logService);
            } else {
                RTXSendMsgPool.getInstance().removeCache(taskId);
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * 判断是否使用腾讯通
     *
     * @return
     */
    public boolean rtxEnabled() {
        SysConfigDto sysConfigDto = sysConfigService.findByKey(RTX_ENABLED.getValue());
        if (sysConfigDto != null && Constant.EnumState.YES.getValue().equals(sysConfigDto.getConfigValue())) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 判断是否使用短息同志
     *
     * @return
     */
    public boolean rtxSMSEnabled() {
        SysConfigDto sysConfigDto = sysConfigService.findByKey(SMS_SYS_TYPE.getValue());
        if (sysConfigDto != null && Constant.EnumState.NO.getValue().equals(sysConfigDto.getConfigValue())) {
            return true;
        } else {
            return false;
        }
    }

    public static void main(String[] args) {
       /* List<User> receiverList = new ArrayList<>();
        User user = new User();
        user.setUserMPhone("18038078167");
        user.setDisplayName("2131");
        receiverList.add(user);
        for (int i = 0; i < 10; i++) {

            SMSUtils.seekSMSThreadToDo(null, receiverList, "测试项目", "", "task_type", "待办", "ceshi", null);
        }*/
    }

}
