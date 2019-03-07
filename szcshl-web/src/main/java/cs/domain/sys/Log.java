package cs.domain.sys;

import org.hibernate.annotations.GenericGenerator;

import java.util.Date;
import javax.persistence.*;

/**
 * @author ldm
 */
@Entity
@Table(name="cs_log")
public class Log {
	@Id
    @GeneratedValue(generator= "logGenerator")
    @GenericGenerator(name= "logGenerator",strategy = "uuid2")
	private String id;

    /**
     * 用户名
     */
    @Column(columnDefinition = "varchar(255)")
	private String userName;

    /**
     * 创建日期
     */
    @Column(columnDefinition = "date NOT NULL")
	private Date createdDate;

    /**
     * 类名
     */
	@Column(columnDefinition = "varchar(255)")
    private String logger;

    /**
     * 方法名
     */
    @Column(columnDefinition = "varchar(128)")
    private String logMethod;
    /**
     * 所属模块
     */
    @Column(columnDefinition = "varchar(255)")
    private String module;
    /**
     * 日记级别(1表示级别高，2表示一般，3表示低)
     */
    @Column(columnDefinition = "varchar(32)")
	private String logLevel;

    /**
     * 返回码
     */
    @Column(columnDefinition = "varchar(32)")
    private String logCode;

    /**
     * 信息
     */
    @Column(columnDefinition = "CLOB")
	private String message;

    /**
     * 业务ID
     */
    @Column(columnDefinition = "varchar(64)")
	private String buninessId;

    /**
     * 业务类型
     */
    @Column(columnDefinition = "varchar(64)")
    private String buninessType;

    /**
     * IP地址
     */
    @Column(columnDefinition = "varchar(64)")
    private String ipAdd;

    /**
     * 浏览器版本
     */
    @Column(columnDefinition = "varchar(256)")
    private String browserInfo;

    /**
     * 参数信息
     */
    @Column(columnDefinition = "CLOB")
    private String paramsInfo;
    /**
     * 结果（9：表示成功。0：表示失败）
     */
    @Column(columnDefinition = "varchar(2)")
    private String result;

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

	public String getLogLevel() {
		return logLevel;
	}

	public void setLogLevel(String logLevel) {
		this.logLevel = logLevel;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBuninessId() {
        return buninessId;
    }

    public void setBuninessId(String buninessId) {
        this.buninessId = buninessId;
    }

    public String getBuninessType() {
        return buninessType;
    }

    public void setBuninessType(String buninessType) {
        this.buninessType = buninessType;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getLogger() {
        return logger;
    }

    public void setLogger(String logger) {
        this.logger = logger;
    }

    public String getLogCode() {
        return logCode;
    }

    public void setLogCode(String logCode) {
        this.logCode = logCode;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getIpAdd() {
        return ipAdd;
    }

    public void setIpAdd(String ipAdd) {
        this.ipAdd = ipAdd;
    }

    public String getBrowserInfo() {
        return browserInfo;
    }

    public void setBrowserInfo(String browserInfo) {
        this.browserInfo = browserInfo;
    }

    public String getLogMethod() {
        return logMethod;
    }

    public void setLogMethod(String logMethod) {
        this.logMethod = logMethod;
    }

    public String getParamsInfo() {
        return paramsInfo;
    }

    public void setParamsInfo(String paramsInfo) {
        this.paramsInfo = paramsInfo;
    }
}
