package com.sn.framework.module.sys.domain;

import com.sn.framework.core.DomainBase;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Description: 操作日志  数据映射实体
 *
 * @Author: tzg
 * @Date: 2017/9/11 14:01
 */
@Entity
@Table(name = "cs_sys_operator_log")
public class OperatorLog extends DomainBase {

    private static final long serialVersionUID = -2362545749749076325L;

    @Id
    @Column(length = 64)
    private String id;
    /**
     * 客户端IP
     */
    @Column(length = 100)
    private String ipAddress;
    /**
     * 客户端标识
     */
    @Column
    private String clientIdentify;

    @Column
    private String userAgent;
    /**
     * 客户端类型
     */
    @Column
    private String clientType;
    /**
     * 操作链接
     */
    @Column(length = 500)
    private String operateUri;
    /**
     * 操作方法
     */
    @Column
    private String operateMethod;
    /**
     * 操作名
     */
    @Column(length = 100)
    private String operateName;
    /**
     * 操作类型
     */
    @Column(length = 10)
    private String operateType;
    /**
     * 业务类型
     */
    @Column(length = 32)
    private String businessType;
    /**
     * 消息
     */
    @Column(length = 2000)
    private String message;
    /**
     * 操作耗时
     */
    @Column
    private long operateTime;
    /**
     * 旧数据
     */
    @Column(columnDefinition = "CLOB")
    private String oldInfo;

    /**
     * 新数据
     */
    @Column(columnDefinition = "CLOB")
    private String newInfo;

    /**
     * 修改的内容
     */
    @Column(columnDefinition = "CLOB")
    private String updateInfo;

    /**
     * 成功标识
     */
    @Column(length = 2)
    private String sucessFlag;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getClientIdentify() {
        return clientIdentify;
    }

    public void setClientIdentify(String clientIdentify) {
        this.clientIdentify = clientIdentify;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public String getClientType() {
        return clientType;
    }

    public void setClientType(String clientType) {
        this.clientType = clientType;
    }

    public String getOperateUri() {
        return operateUri;
    }

    public void setOperateUri(String operateUri) {
        this.operateUri = operateUri;
    }

    public String getOperateMethod() {
        return operateMethod;
    }

    public void setOperateMethod(String operateMethod) {
        this.operateMethod = operateMethod;
    }

    public String getOperateName() {
        return operateName;
    }

    public void setOperateName(String operateName) {
        this.operateName = operateName;
    }

    public String getOperateType() {
        return operateType;
    }

    public void setOperateType(String operateType) {
        this.operateType = operateType;
    }

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getOperateTime() {
        return operateTime;
    }

    public void setOperateTime(long operateTime) {
        this.operateTime = operateTime;
    }

    public String getOldInfo() {
        return oldInfo;
    }

    public void setOldInfo(String oldInfo) {
        this.oldInfo = oldInfo;
    }

    public String getNewInfo() {
        return newInfo;
    }

    public void setNewInfo(String newInfo) {
        this.newInfo = newInfo;
    }

    public String getUpdateInfo() {
        return updateInfo;
    }

    public void setUpdateInfo(String updateInfo) {
        this.updateInfo = updateInfo;
    }

    public String getSucessFlag() {
        return sucessFlag;
    }

    public void setSucessFlag(String sucessFlag) {
        this.sucessFlag = sucessFlag;
    }
}