package com.sn.framework.core.web;

/**
 * Description: 客户端类型
 *
 * @author: tzg
 * @date: 2018/2/8 11:19
 */
public enum ClientType {
    WECHAT("微信端"),
    QQ("QQ 浏览器"),
    CHROME("谷歌浏览器"),
    IE("IE 浏览器"),
    FIREFOX("火狐浏览器"),
    SAFARI("Safari 浏览器"),
    OPERA("Opera 浏览器"),
    OTHER("其他"),
    UNKNOWN("未知"),
    ;

    private final String typeName;

    ClientType(String typeName) {
        this.typeName = typeName;
    }

    public String getTypeName() {
        return typeName;
    }
}