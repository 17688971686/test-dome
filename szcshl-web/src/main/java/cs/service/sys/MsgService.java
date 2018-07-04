package cs.service.sys;

import cs.common.ResultMsg;
import cs.domain.sys.SMSLog;
import cs.domain.sys.User;

import java.util.List;

/**
 * Created by Administrator on 2018/5/31.
 */
public interface MsgService {

    /**
     * 获取发送短信token
     * @return
     */
    ResultMsg getMsgToken();


    /**
     * 判断是否已经已发送过短信.规则是发送过短信就不再发送短信
     * @param projectName
     * @param fileCode
     * @param type
     * */
    String orNotsendSMS(List<User> list, String projectName, String fileCode, String type, String infoType,String xianzhiNumber);

    /**
     * 判断是否已经已发送过短信.规则是发送过短信就不再发送短信
     * @param projectName
     * @param fileCode
     * @param type
     * */
    String querySmsNumber(List<User> list, String projectName, String fileCode, String type, String infoType,String xianzhiNumber);

    /**
     * 发送短信
     * @param recvUserList 用户列表
     * @param msgContent   短信内容
     * @param smsLog   消息日志
     */
    void sendMsg(List<User> recvUserList, String msgContent,SMSLog smsLog);
}
