package cs.service.sys;

import cs.common.ResultMsg;
import cs.domain.sys.SMSLog;
import cs.domain.sys.User;

import java.util.Date;
import java.util.List;

/**
 * Created by ldm on 2018/7/2.
 */
public interface MsgService {

    /**
     * 获取发送短信token
     * @return
     */
    ResultMsg getMsgToken();

    /**
     * 新的获取发送短信token
     * @return
     */
    ResultMsg getMsgTokenNew();
    /**
     * 发送短信
     * @param recvUserList 用户列表
     * @param msgContent   短信内容
     * @param smsLog   消息日志
     */
    void sendMsg(List<User> recvUserList, String msgContent,SMSLog smsLog);

    /**
     * 新的发送短信接口
     * @param recvUserList
     * @param msgContent
     * @param smsLog
     */
    void sendMsgNew(List<User> recvUserList, String msgContent,SMSLog smsLog);
    /**
     * 通过配置值获取通知的用户信息
     */
    List<User> getNoticeUserByConfigKey(String configKey);

}
