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
@Data
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
     * 消息
     */
    @Column(length = 2000)
    private String message;
    /**
     * 操作耗时
     */
    @Column
    private long operateTime;

}