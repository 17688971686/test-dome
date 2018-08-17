package com.sn.framework.core.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sn.framework.core.common.JacksonUtils;

import java.io.Serializable;

/**
 * Description: 根据 user agent string 来判断出客户端的浏览器以及平台等信息
 *
 * @author: tzg
 * @date: 2018/2/8 10:38
 */
public class UserAgent implements Serializable {

    private static final long serialVersionUID = -650512421122530076L;

    /**
     * 浏览器类型
     */
    private ClientType clientType;
    /**
     * 浏览器版本
     */
    private String clientVersion;
    /**
     * 平台类型
     */
    private String platformType;
    /**
     * 平台系列
     */
    private String platformSeries;
    /**
     * 平台版本
     */
    private String platformVersion;

    public UserAgent() {
        clientType = ClientType.UNKNOWN;
    }

    public UserAgent(ClientType clientType, String clientVersion,
                     String platformType, String platformSeries, String platformVersion) {
        this.clientType = clientType;
        this.clientVersion = clientVersion;
        this.platformType = platformType;
        this.platformSeries = platformSeries;
        this.platformVersion = platformVersion;
    }

    public ClientType getClientType() {
        return clientType;
    }

    public void setClientType(ClientType clientType) {
        this.clientType = clientType;
    }

    public String getClientVersion() {
        return clientVersion;
    }

    public void setClientVersion(String clientVersion) {
        this.clientVersion = clientVersion;
    }

    public String getPlatformType() {
        return platformType;
    }

    public void setPlatformType(String platformType) {
        this.platformType = platformType;
    }

    public String getPlatformSeries() {
        return platformSeries;
    }

    public void setPlatformSeries(String platformSeries) {
        this.platformSeries = platformSeries;
    }

    public String getPlatformVersion() {
        return platformVersion;
    }

    public void setPlatformVersion(String platformVersion) {
        this.platformVersion = platformVersion;
    }

    @Override
    public String toString() {
        try {
            return JacksonUtils.objectToString(this);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "";
        }
    }
}