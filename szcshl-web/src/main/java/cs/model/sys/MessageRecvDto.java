package cs.model.sys;

import java.util.List;

/**
 * Created by Administrator on 2018/5/8 0008.
 */
public class MessageRecvDto {

    private String msgId;		//消息ID

    private String userId;		//接收对象ID

    private String userName;	//接收对象名称

    private String status;		//状态值（0：未发送，1：发送未成功，9：已成功发送）

    private Integer sendCount;		//发送次数统计

    private List<MessageRecvDto> messageRecvDtoList;	//对应映射表

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getSendCount() {
        return sendCount;
    }

    public void setSendCount(Integer sendCount) {
        this.sendCount = sendCount;
    }

    public List<MessageRecvDto> getMessageRecvDtoList() {
        return messageRecvDtoList;
    }

    public void setMessageRecvDtoList(List<MessageRecvDto> messageRecvDtoList) {
        this.messageRecvDtoList = messageRecvDtoList;
    }
}
