package com.sn.framework.module.sys.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sn.framework.core.DomainBase;
import lombok.Data;

import javax.persistence.*;

/**
 * Created by qbl on 2017/9/9.
 */
@Entity
@Table(name = "cs_sys_dict", uniqueConstraints = {@UniqueConstraint(columnNames = {"parentId", "dictKey"}), @UniqueConstraint(columnNames = {"parentId", "dictName"})})
public class Dict extends DomainBase {

    @Id
    @Column(length = 50)
    private String dictId;
    /**
     * 字典名称
     */
    @Column(length = 100)
    private String dictName;
    /**
     * 父级字典码
     */
    @Column(length = 50)
    private String parentId;
    /**
     * 字典值
     */
    @Column(length = 64, nullable = false)
    private String dictKey;
    /**
     * 字典状态
     */
    @Column(nullable = false, length = 1)
    private int dictState;
    /**
     * 字典类型：0、表示系统默认，不可删除；1、表示用户添加
     */
    @Column(nullable = false, length = 1)
    @JsonIgnore
    private int dictType;
    /**
     * 备注
     */
    @Column
    private String remark;

    public String getDictId() {
        return dictId;
    }

    public void setDictId(String dictId) {
        this.dictId = dictId;
    }

    public String getDictName() {
        return dictName;
    }

    public void setDictName(String dictName) {
        this.dictName = dictName;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getDictKey() {
        return dictKey;
    }

    public void setDictKey(String dictKey) {
        this.dictKey = dictKey;
    }

    public int getDictState() {
        return dictState;
    }

    public void setDictState(int dictState) {
        this.dictState = dictState;
    }

    public int getDictType() {
        return dictType;
    }

    public void setDictType(int dictType) {
        this.dictType = dictType;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}