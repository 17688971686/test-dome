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
@Data
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
}