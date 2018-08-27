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

    public String getResId() {
        return resId;
    }

    public void setResId(String resId) {
        this.resId = resId;
    }

    public String getResName() {
        return resName;
    }

    public void setResName(String resName) {
        this.resName = resName;
    }

    public Integer getResType() {
        return resType;
    }

    public void setResType(Integer resType) {
        this.resType = resType;
    }

    public String getResUri() {
        return resUri;
    }

    public void setResUri(String resUri) {
        this.resUri = resUri;
    }

    public String getResIcon() {
        return resIcon;
    }

    public void setResIcon(String resIcon) {
        this.resIcon = resIcon;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getDataType() {
        return dataType;
    }

    public void setDataType(int dataType) {
        this.dataType = dataType;
    }

    public String getPermCode() {
        return permCode;
    }

    public void setPermCode(String permCode) {
        this.permCode = permCode;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public List<Organ> getOrgans() {
        return organs;
    }

    public void setOrgans(List<Organ> organs) {
        this.organs = organs;
    }
}