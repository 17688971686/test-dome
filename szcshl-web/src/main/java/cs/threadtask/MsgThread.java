package cs.threadtask;

import cs.domain.sys.SMSLog;
import cs.domain.sys.User;
import cs.service.sys.MsgService;

import java.util.List;

/**
 * 发送短信线程
 * Created by ldm on 2018/7/3 0004.
 */
public class MsgThread implements Runnable{

    private MsgService msgService;

    private List<User> recvUserList;

    private String  msgContent;

    private SMSLog smsLog;

    private boolean newType;

    public MsgThread(MsgService msgService,List<User> recvUserList,String  msgContent,SMSLog smsLog,boolean newType) {
        this.msgService = msgService;
        this.recvUserList = recvUserList;
        this.msgContent = msgContent;
        this.smsLog = smsLog;
        this.newType = newType;
    }

    @Override
    public void run() {
        if(newType){
            msgService.sendMsgNew(recvUserList,msgContent,smsLog);
        }else{
            msgService.sendMsg(recvUserList,msgContent,smsLog);
        }
    }
}