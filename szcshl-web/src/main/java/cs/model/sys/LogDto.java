package cs.model.sys;

import java.util.Date;

public class LogDto {
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
	 * 类名
	 */
	private String logger;

    /**
     * 模块
     */
    private String module;
	/**
	 * 日记级别
	 */
	private String logLevel;

	/**
	 * 返回码
	 */
	private String logCode;

	/**
	 * 信息
	 */
	private String message;

	/**
	 * 业务ID
	 */
	private String buninessId;

	/**
	 * 业务类型
	 */
	private String buninessType;

	/**
	 * IP地址
	 */
	private String ipAdd;

	/**
	 * 浏览器版本
	 */
	private String browserInfo;

    /**
     * 方法名
     */
    private String logMethod;

    /**
     * 参数信息
     */
    private String paramsInfo;
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

	public String getLogger() {
		return logger;
	}

	public void setLogger(String logger) {
		this.logger = logger;
	}

	public String getLogLevel() {
		return logLevel;
	}

	public void setLogLevel(String logLevel) {
		this.logLevel = logLevel;
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
