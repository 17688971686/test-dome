package cs.service.rtx;

import cs.common.constants.Constant;
import cs.common.ResultMsg;
import cs.common.utils.*;
import cs.domain.sys.User;
import cs.model.sys.SysConfigDto;
import cs.repository.repositoryImpl.sys.UserRepo;
import cs.service.sys.LogService;
import cs.service.sys.SysConfigService;
import org.activiti.engine.runtime.ProcessInstance;
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

import static cs.common.constants.Constant.RevireStageKey.RTX_ENABLED;
import static cs.common.constants.Constant.RevireStageKey.SMS_SYS_TYPE;


@Service
public class RTXService {
    private final static String RTX_GETSESSION = "/GetSession.cgi?";
    private final static String RTX_GETSTATUS = "/getstatus.php?";

    @Autowired
    private UserRepo userRepo;
    @Autowired
    private SysConfigService sysConfigService;

    @Autowired
    private LogService logService;
    /**
     * 获取腾讯通的sessionKey
     *
     * @param url       腾讯通URL
     * @param loginUser 登录用户
     * @return
     */
    public String getSessionKey(String url, String loginUser) {
        String strSessionKey = "";
        if (!Validate.isString(url)) {
            PropertyUtil propertyUtil = new PropertyUtil(Constant.businessPropertiesName);
            url = propertyUtil.readProperty("RTX_URL");
        }
        url += RTX_GETSESSION;
        try {
            url += "receiver=" + URLEncoder.encode(loginUser, "GBK");
            java.net.URL loginUrl = new URL(url);
            HttpURLConnection httpConnection = (HttpURLConnection) loginUrl.openConnection();
            BufferedReader reader = new BufferedReader(new InputStreamReader(httpConnection.getInputStream()));
            strSessionKey = reader.readLine();
        } catch (Exception e) {
            System.out.println("获取腾讯通sessionKey异常：" + e);
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
        if (!Validate.isString(url)) {
            PropertyUtil propertyUtil = new PropertyUtil(Constant.businessPropertiesName);
            url = propertyUtil.readProperty("RTX_URL");
        }
        url += RTX_GETSTATUS;
        try {
            url += "username=" + URLEncoder.encode(loginUser, "GBK");
            java.net.URL loginUrl = new URL(url);
            HttpURLConnection httpConnection = (HttpURLConnection) loginUrl.openConnection();
            BufferedReader reader = new BufferedReader(new InputStreamReader(httpConnection.getInputStream()));
            userState = reader.readLine();
        } catch (Exception e) {
            System.out.println("获取用户在线状态异常：" + e);
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
    public boolean dealPoolRTXMsg(String taskId, ResultMsg resultMsg,ProcessInstance processInstance) {
        String receiverIds = RTXSendMsgPool.getInstance().getReceiver(taskId).toString();
        List<User> receiverList = userRepo.getCacheUserListById(receiverIds);
        String sendContent = "你有一条新的待办事项要处理【"+processInstance.getName()+"】";
        //短息开关
        if(rtxSMSEnabled()){
            //发送短息
            SMSUtils.seekSMSThread(receiverList, sendContent,logService);
        }
        //如果使用腾讯通，并处理成功！
        if (rtxEnabled() && resultMsg.isFlag() && RTXSendMsgPool.getInstance().getReceiver(taskId) != null) {
            RTXUtils.sendRTXThread(taskId,receiverList,sendContent,logService);
        } else {
            RTXSendMsgPool.getInstance().removeCache(taskId);
        }
        return true;
    }

    /**
     * 判断是否使用腾讯通
     * @return
     */
    public boolean rtxEnabled() {
        SysConfigDto sysConfigDto = sysConfigService.findByKey(RTX_ENABLED.getValue());
        if(sysConfigDto != null && Constant.EnumState.YES.getValue().equals(sysConfigDto.getConfigValue())) {
            return true;
        }else{
            return false;
        }
    }
    /**
     * 判断是否使用短息同志
     * @return
     */
    public boolean rtxSMSEnabled() {
        SysConfigDto sysConfigDto = sysConfigService.findByKey(SMS_SYS_TYPE.getValue());
        if(sysConfigDto != null && Constant.EnumState.NO.getValue().equals(sysConfigDto.getConfigValue())) {
            return true;
        }else{
            return false;
        }
    }
}
