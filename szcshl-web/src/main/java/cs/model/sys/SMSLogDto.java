package cs.model.sys;

import javax.persistence.Column;
import javax.persistence.Id;
import java.util.Date;

public class SMSLogDto {
	private String id;
	private String userName;

	private String smsUserName;


	private String smsUserPhone;


	private String manyOrOne;


	private Date createdDate;



	private String resultCode;

	/**
	 * 短信提心类型:
	 * 收文:MSS_SYS_USER_TYPE  发文: SMS_SYS_USER_POST_FAILURE
	 */
	private String smsLogType;
	//类型显示名称
	private String smsLogTypeName;
	/**
	 * 返回码
	 */
	private String logCode;

	/**
	 * 信息内容
	 */
	private String message;
	/**
	 * 自定义信息内容
	 */
	private String customMessage;
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
	 * 暂时界面使用:结果（9：表示成功。0：表示失败）
	 */
	private String result;
	private String createdBy;

	public String getSmsLogTypeName() {
		return smsLogTypeName;
	}

	public void setSmsLogTypeName(String smsLogTypeName) {
		this.smsLogTypeName = smsLogTypeName;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

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

	public String getSmsUserName() {
		return smsUserName;
	}

	public void setSmsUserName(String smsUserName) {
		this.smsUserName = smsUserName;
	}

	public String getSmsUserPhone() {
		return smsUserPhone;
	}

	public void setSmsUserPhone(String smsUserPhone) {
		this.smsUserPhone = smsUserPhone;
	}

	public String getManyOrOne() {
		return manyOrOne;
	}

	public void setManyOrOne(String manyOrOne) {
		this.manyOrOne = manyOrOne;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getResultCode() {
		return resultCode;
	}

	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
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

	public String getCustomMessage() {
		return customMessage;
	}

	public void setCustomMessage(String customMessage) {
		this.customMessage = customMessage;
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
