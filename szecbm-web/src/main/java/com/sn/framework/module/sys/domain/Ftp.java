package com.sn.framework.module.sys.domain;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


/**
 * ftp管理表
 */
@Entity
@Table(name = "cs_sys_ftp")
@Data
public class Ftp {
    @Id
    private String ftpId;

    //ip地址
    @Column(columnDefinition = "varchar(30)",unique = true)
    private String ipAddr;

    //端口号
    @Column(columnDefinition = "Integer")
    private Integer port;

    //用户名
    @Column(columnDefinition = "varchar(30)")
    private String userName;

    //密码
    @Column(columnDefinition = "varchar(60)")
    private String pwd;

    /**
     * 根路径
     */
    @Column(columnDefinition = "varchar(100)")
    private String path;

    /**
     * 是否当前在用 9表示是，0表示否
     */
    @Column(columnDefinition = "varchar(2) default 0")
    private String enable;


}
