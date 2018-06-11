package cs.domain.sys;

import cs.common.constants.Constant;
import cs.service.sys.SMSLogService;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name="cs_sms_log")
public class SMSLog {
    @Id
    @Column(columnDefinition = "VARCHAR(64)")
	private String id;

    /**
     * 用户名
     */
    @Column(columnDefinition = "varchar(64)")
	private String userName;
    /**
     * 短信用户名
     */
    @Column(columnDefinition = "varchar(64)")
    private String smsUserName;

    /**
     * 发送短信手机号码
     */
    @Column(columnDefinition = "varchar(255)")
    private String smsUserPhone;

    /**
     * 是:单个发送还是多个发送
     */
    @Column(columnDefinition = "varchar(64)")
    private String manyOrOne;

    /**
     * 创建日期
     */
    @Column(columnDefinition = "date NOT NULL")
	private Date createdDate;


    /**
     * 短信结果编码
     */
    @Column(columnDefinition = "varchar(10)")
    private String resultCode;

    /**
     * 短信提心类型:
     * 收文:MSS_SYS_USER_TYPE  发文: SMS_SYS_USER_POST_FAILURE
     */
    @Column(columnDefinition = "varchar(64)")
	private String smsLogType;

    /**
     * 返回码
     */
    @Column(columnDefinition = "varchar(32)")
    private String logCode;

    /**
     * 信息内容
     */
    @Column(columnDefinition = "varchar(255)")
	private String message;
    /**
     * 自定义信息内容
     */
    @Column(columnDefinition = "varchar(255)")
    private String customMessage;
    /**
     * 业务ID
     */
    @Column(columnDefinition = "varchar(64)")
	private String buninessId;
    /**
     *
     */
    @Column(columnDefinition = "varchar(64)")
    private String fileCode;

    /**
     */
    @Column(columnDefinition = "varchar(64)")
    private String projectName;
    /**
     * IP地址
     */
    @Column(columnDefinition = "varchar(64)")
    private String ipAdd;

    /**
     * 暂时界面使用:结果（9：表示成功。0：表示失败）
     */
    @Column(columnDefinition = "varchar(2)")
    private String result;


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



    public  void setObject(String userName,String smsUserPhone,String projectName, String filecode, String resultCode, String type, String infoType, String seekContent, boolean success){
        this.setId(UUID.randomUUID().toString());
        this.setCreatedDate(new Date());
        this.setProjectName(projectName);
        //fileCode 编码
        this.setFileCode(filecode);
        //返回结果:可以是自定义code
        this.setResultCode(resultCode);
        this.setSmsLogType(type);
        //自定义内容
        this.setCustomMessage(infoType);
        //短信内容
        this.setMessage(seekContent);
        this.setUserName(userName);
        this.setSmsUserPhone(smsUserPhone);
        if(success){
            this.setResult(Constant.EnumState.YES.getValue());
        }else{
            this.setResult(Constant.EnumState.NO.getValue());
        }
    }
}
