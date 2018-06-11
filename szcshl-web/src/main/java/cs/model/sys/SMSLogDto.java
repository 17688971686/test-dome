package cs.model.sys;

import javax.persistence.Column;
import javax.persistence.Id;
import java.util.Date;

public class SMSLogDto {
	private String id;

	/**
	 * 用户名
	 */
	private String userName;

	/**
	 * 创建日期
	 */
	private Date createdDate;




	/**
	 * 短信提心类型: 收文:MSS_SYS_USER_TYPE  发文: SMS_SYS_USER_POST_FAILURE
	 */
	private String smsLogType;

	/**
	 * 返回码
	 */
	private String logCode;

	/**
	 * 信息内容
	 */
	private String message;

	/**
	 * 业务ID
	 */
	private String buninessId;

	/**
	 *
	 */
	private String fileCode;

	/**
	 */
	private String projectName;
	/**
	 * IP地址
	 */
	private String ipAdd;

	/**
	 * 结果（9：表示成功。0：表示失败）
	 */
	private String result;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getSmsLogType() {
		return smsLogType;
	}

	public void setSmsLogType(String smsLogType) {
		this.smsLogType = smsLogType;
	}

	public String getLogCode() {
		return logCode;
	}

	public void setLogCode(String logCode) {
		this.logCode = logCode;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getBuninessId() {
		return buninessId;
	}

	public void setBuninessId(String buninessId) {
		this.buninessId = buninessId;
	}

	public String getFileCode() {
		return fileCode;
	}

	public void setFileCode(String fileCode) {
		this.fileCode = fileCode;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getIpAdd() {
		return ipAdd;
	}

	public void setIpAdd(String ipAdd) {
		this.ipAdd = ipAdd;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}
}
