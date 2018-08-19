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
     * 部长
     */
    @Column(length = 64)
    private String organLead;

    /**
     * 部长ID
     */
    @Column(length = 64)
    private String organLeadId;
    /**
     * 分管领导
     */
    @Column(length = 64)
    private String organManage;

    /**
     * 分管领导ID
     */
    @Column(length = 64)
    private String organManageId;

    /**
     * 主任
     */
    @Column(length = 64)
    private String orgMLeader;

    /**
     * 主任ID
     */
    @Column(length = 64)
    private String orgMLeaderId;
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

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getOrganId() {
        return organId;
    }

    public void setOrganId(String organId) {
        this.organId = organId;
    }

    public String getOrganCode() {
        return organCode;
    }

    public void setOrganCode(String organCode) {
        this.organCode = organCode;
    }

    public String getOrganName() {
        return organName;
    }

    public void setOrganName(String organName) {
        this.organName = organName;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getOrganRel() {
        return organRel;
    }

    public void setOrganRel(String organRel) {
        this.organRel = organRel;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getOrganType() {
        return organType;
    }

    public void setOrganType(String organType) {
        this.organType = organType;
    }

    public String getOrganNature() {
        return organNature;
    }

    public void setOrganNature(String organNature) {
        this.organNature = organNature;
    }

    public String getOrganRegion() {
        return organRegion;
    }

    public void setOrganRegion(String organRegion) {
        this.organRegion = organRegion;
    }

    public int getOrganDataType() {
        return organDataType;
    }

    public void setOrganDataType(int organDataType) {
        this.organDataType = organDataType;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getOrganLead() {
        return organLead;
    }

    public void setOrganLead(String organLead) {
        this.organLead = organLead;
    }

    public String getOrganLeadId() {
        return organLeadId;
    }

    public void setOrganLeadId(String organLeadId) {
        this.organLeadId = organLeadId;
    }

    public String getOrganManage() {
        return organManage;
    }

    public void setOrganManage(String organManage) {
        this.organManage = organManage;
    }

    public String getOrganManageId() {
        return organManageId;
    }

    public void setOrganManageId(String organManageId) {
        this.organManageId = organManageId;
    }

    public String getOrgMLeader() {
        return orgMLeader;
    }

    public void setOrgMLeader(String orgMLeader) {
        this.orgMLeader = orgMLeader;
    }

    public String getOrgMLeaderId() {
        return orgMLeaderId;
    }

    public void setOrgMLeaderId(String orgMLeaderId) {
        this.orgMLeaderId = orgMLeaderId;
    }

    public String getLeadPhone() {
        return leadPhone;
    }

    public void setLeadPhone(String leadPhone) {
        this.leadPhone = leadPhone;
    }

    public String getManagePhone() {
        return managePhone;
    }

    public void setManagePhone(String managePhone) {
        this.managePhone = managePhone;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public Set<Resource> getResources() {
        return resources;
    }

    public void setResources(Set<Resource> resources) {
        this.resources = resources;
    }
}