package com.sn.framework.module.sys.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sn.framework.core.DomainBase;
import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Description: 系统菜单  数据映射实体
 * @Author: tzg
 * @Date: 2017/9/13 19:02
 */
@Entity
@Table(name = "cs_sys_resource")
@Data
public class Resource extends DomainBase {

    @Id
    @Column(length = 64)
    private String resId;
    /**
     * 资源名称
     */
    @Column(length = 100, nullable = false)
    private String resName;
    /**
     * 资源类型：0、模块；1、菜单；2、按钮
     */
    @Column(nullable = false)
    private Integer resType;
    /**
     * 资源链接
     */
    @Column
    private String resUri;
    /**
     * 图标
     */
    @Column(length = 50)
    private String resIcon;
    /**
     * 打开方式（_blank、_self、_parent、_top）
     */
    @Column(length = 50)
    private String target;
    /**
     * 资源状态：0、禁用；1、可用
     */
    @Column(nullable = false)
    private String status;
    /**
     * 数据类型：0、为系统基础数据，不可删除；1、为用户添加数据，可删除
     */
    @Column(length = 1)
    private int dataType;
    /**
     * 权限码
     */
    @Column
    private String permCode;
    @Column
    private String remark;
    @Column
    private String parentId;

    @JsonIgnore
    @ManyToMany(mappedBy = "resources")
    private List<Role> roles = new ArrayList<>();
    @JsonIgnore
    @ManyToMany(mappedBy = "resources")
    private List<Organ> organs = new ArrayList<>();

}