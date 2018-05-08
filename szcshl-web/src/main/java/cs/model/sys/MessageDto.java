package cs.model.sys;

import java.util.List;

/**
 * Created by Administrator on 2018/5/8 0008.
 */
public class MessageDto {

    private String id;
    /**
     * 标题
     */
    private String title;
    /**
     * 内容
     */
    private String content;
    /**
     * 发送人名称
     */
    private String createUserName;
    /**
     * 短信类型
     */
    private String messageType;
    /**
     * 短信级别	(0:普通，9：重要)
     */
    private String messageLevel;

    /**
     * 状态	(0:还可修改，9：不可修改)
     */
    private String status;

    private List<MessageRecvDto> messageRecvDtoList;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreateUserName() {
        return createUserName;
    }

    public void setCreateUserName(String createUserName) {
        this.createUserName = createUserName;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public String getMessageLevel() {
        return messageLevel;
    }

    public void setMessageLevel(String messageLevel) {
        this.messageLevel = messageLevel;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<MessageRecvDto> getMessageRecvDtoList() {
        return messageRecvDtoList;
    }

    public void setMessageRecvDtoList(List<MessageRecvDto> messageRecvDtoList) {
        this.messageRecvDtoList = messageRecvDtoList;
    }
}
