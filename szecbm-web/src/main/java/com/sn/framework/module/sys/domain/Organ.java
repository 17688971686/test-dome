package com.sn.framework.module.sys.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sn.framework.core.DomainBase;
import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Description:  机构信息   数据映射实体
 * @Author: tzg
 * @Date: 2017/8/26 16:49
 */
@Entity
@Table(name = "cs_sys_organ")
@Data
public class Organ extends DomainBase {

    private static final long serialVersionUID = -3935935708897224976L;
    /**
     * 机构id
     */
    @Id
    @Column(name = "organ_id", nullable = false, length = 64)
    private String organId;
    /**
     * 机构代码
     */
    @Column(name = "organ_code", nullable = false)
    private String organCode;
    /**
     * 机构名称
     */
    @Column(name = "organ_name", nullable = false)
    private String organName;
    /**
     * 机构简称
     */
    @Column
    private String shortName;
    /**
     * 机构关系链
     */
    @Column(name = "organ_rel", nullable = false, length = 2000)
    private String organRel;
    /**
     * 父机构id
     */
    @Column(name = "parent_id")
    private String parentId;
    /**
     * 机构类型
     */
    @Column(nullable = false, length = 10)
    private String organType;
    /**
     * 机构性质
     */
    @Column
    private String organNature;
    /**
     * 机构所属区域
     */
    @Column
    private String organRegion;
    /**
     * 机构数据类型：0、为系统基础数据，不可删除；1、为用户添加数据，可删除
     */
    @Column(length = 1)
    private int organDataType;
    /**
     * 备注
     */
    @Column(name = "remark")
    private String remark;
    /**
     * 主要领导
     */
    @Column
    private String organLead;
    /**
     * 分管员
     */
    @Column
    private String organManage;
    /**
     * 主要领导电话
     */
    @Column
    private String leadPhone;
    /**
     * 分管员电话
     */
    @Column
    private String managePhone;

    /**
     * 机构人员列表
     */
    @JsonIgnore
    @OneToMany(mappedBy = "organ")
    private List<User> users = new ArrayList<>();
    /**
     * 机构资源列表
     */
    @ManyToMany
    @JoinTable(name = "cs_sys_organ_resources", joinColumns = @JoinColumn(name = "organ_id"),
            inverseJoinColumns = @JoinColumn(name = "res_id"))
    private Set<Resource> resources = new HashSet<>();

    public Organ() {
    }

    public Organ(Organ organ) {
        this.organId = organ.getOrganId();
        this.organName = organ.getOrganName();
        this.organNature = organ.getOrganNature();
    }

    public Organ(String organId, String organName) {
        this.organId = organId;
        this.organName = organName;
    }

    public Organ(String organId, String organName, String parentId) {
        this.organId = organId;
        this.organName = organName;
        this.parentId = parentId;
    }

}