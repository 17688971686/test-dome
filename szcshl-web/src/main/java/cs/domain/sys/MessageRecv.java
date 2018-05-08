package cs.domain.sys;

import javax.persistence.*;
import java.util.Date;

/**
 * 消息-人员映射表
 * @author ldm
 *
 */
@Entity
@Table(name = "cs_message_recv")
public class MessageRecv{
	// 主键，这里需要添加@Id标记
    /**
     * 消息ID
     */
	@Id
    @Column(name="recvId",columnDefinition="VARCHAR(64)")
    private String recvId;

    /**
     * 接收对象ID
     */
	@Column(name="userId",columnDefinition="VARCHAR(64)")
	private String userId;
    /**
     * 手机号码
     */
    @Column(columnDefinition="VARCHAR(16)")
    private String phoneNum;

    /**
     * 接收对象名称
     */
	@Column(columnDefinition="VARCHAR(64)")
	private String userName;

    /**
     * 状态值（0：未发送，1：发送未成功，9：已成功发送）
     */
	@Column(columnDefinition="VARCHAR(2)")
	private String status;

    /**
     * 发送次数统计
     */
	@Column(columnDefinition="Integer")
	private Integer sendCount;

    /**
     * 消息发送成功时间
     */
    @Column(columnDefinition="Date")
    private Date successDate;

    /**
     * 上次发送失败时间
     */
    @Column(columnDefinition="Date")
    private Date lastErrorDate;

    @ManyToOne
    @JoinColumn(name="msgId")
    private Message message;

	public MessageRecv(){}

    public String getRecvId() {
        return recvId;
    }

    public void setRecvId(String recvId) {
        this.recvId = recvId;
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

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public Date getSuccessDate() {
        return successDate;
    }

    public void setSuccessDate(Date successDate) {
        this.successDate = successDate;
    }

    public Date getLastErrorDate() {
        return lastErrorDate;
    }

    public void setLastErrorDate(Date lastErrorDate) {
        this.lastErrorDate = lastErrorDate;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }
}
