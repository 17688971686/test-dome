package cs.domain.sys;

import cs.domain.DomainBase;

import javax.persistence.*;
import java.util.List;

/**
 * 消息提醒实体类
 * @author ldm
 *
 */
@Entity
@Table(name = "cs_message")
public class Message extends DomainBase {
	private static final long serialVersionUID = 1L;
    /**
     * id
     */
    @Id
	private String id;
    /**
     * 标题
     */
    @Column(columnDefinition = "VARCHAR(255)")
	private String title;
    /**
     * 内容
     */
    @Column(columnDefinition = "VARCHAR(4000)")
	private String content;
    /**
     * 发送人名称
     */
    @Column(columnDefinition = "VARCHAR(32)")
	private String createUserName;
    /**
     * 短信类型
     */
    @Column(columnDefinition = "VARCHAR(16)")
	private String messageType;
    /**
     * 短信级别	(0:普通，9：重要)
     */
    @Column(columnDefinition = "VARCHAR(2)")
	private String messageLevel;

    /**
     * 状态	(0:还可修改，9：不可修改)
     */
    @Column(columnDefinition = "VARCHAR(2)")
    private String status;
    /**
     * 短信接收人
     */
    @OneToMany(mappedBy="message",cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<MessageRecv> messageRecvList;

	//消息类型
	public enum MsgType {
        WARING, 		//警告
        OVERTIME, 		//超时
        SUPERVISION,	//督办
        SYSTEM,			//系统消息
        NORMAL,			//普通
    }

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

    public String getCreateUserName() {
        return createUserName;
    }

    public void setCreateUserName(String createUserName) {
        this.createUserName = createUserName;
    }

    public List<MessageRecv> getMessageRecvList() {
        return messageRecvList;
    }

    public void setMessageRecvList(List<MessageRecv> messageRecvList) {
        this.messageRecvList = messageRecvList;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
