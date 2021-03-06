package cs.threadtask;

import cs.domain.sys.SMSLog;
import cs.domain.sys.User;
import cs.service.sys.MsgService;

import java.util.List;

/**
 * 发送短信线程
 * @author ldm on 2018/7/3 0004.
 */
public class MsgThread implements Runnable{

    private MsgService msgService;

    private List<User> recvUserList;

    private String  msgContent;

    private SMSLog smsLog;

    public MsgThread(MsgService msgService,List<User> recvUserList,String  msgContent,SMSLog smsLog) {
        this.msgService = msgService;
        this.recvUserList = recvUserList;
        this.msgContent = msgContent;
        this.smsLog = smsLog;
    }

    @Override
    public void run() {
        //调用新的短信发送接口
        boolean newType = msgService.checkMsgType();
        if(newType){
            msgService.sendMsgNew(recvUserList,msgContent,smsLog);
        }else{
            msgService.sendMsg(recvUserList,msgContent,smsLog);
        }
    }
}