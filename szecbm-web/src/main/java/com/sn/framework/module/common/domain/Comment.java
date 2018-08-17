package com.sn.framework.module.common.domain;

import com.sn.framework.core.DomainBase;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 处理意见表
 * Created by ldm on 2018/4/23.
 */
@Entity
@Table(name = "sys_com_comment")
@Data
public class Comment extends DomainBase {
    @Id
    @Column(columnDefinition = "varchar(32)")
    private String commentId;

    /**
     * 签收人姓名
     */
    @Column(columnDefinition = "varchar(64)")
    private String createName;

    /**
     * 是否代理签字，1表示是，0表示否
     */
    @Column(columnDefinition = "Integer default 0")
    private Integer agent;

    /**
     * 是否单个签字，1表示是，0表示否
     */
    @Column(columnDefinition = "Integer default 1")
    private Integer single = 1;

    /**
     * 实际签收人ID，有代理的时候才显示'
     */
    @Column(columnDefinition = "varchar(32)")
    private String signUserId;

    /**
     * 实际签收人姓名，有代理的时候才显示
     */
    @Column(columnDefinition = "varchar(64)")
    private String signUserName;

    /**
     * 业务主键
     */
    @Column(columnDefinition = "varchar(64)")
    private String businessKey;

    /**
     * 环节名称
     */
    @Column(columnDefinition = "varchar(32)")
    private String nodeKey;

    /**
     * 处理意见
     */
    @Column(columnDefinition = "varchar(4000)")
    private String suggestion;

}
