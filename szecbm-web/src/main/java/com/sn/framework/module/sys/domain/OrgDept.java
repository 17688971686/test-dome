package com.sn.framework.module.sys.domain;

import org.hibernate.annotations.Subselect;

import javax.persistence.*;

/**
 * 部门——小组视图
 * Created by ldm on 2017/8/21.
 */

@Entity
@Subselect("select * from V_ORG_DEPT")
public class OrgDept {

    @Id
    private String id;
    /**
     * 名称
     */
    @Column(name="NAME")
    private String name;
    /**
     * 部长
     */
    @Column(name="DIRECTORID")
    private String directorID;
    @Column(name="DIRECTORNAME")
    private String directorName;
    /**
     * 分管领导
     */
    @Column(name="SLEADERID")
    private String sLeaderID;
    @Column(name="SLEADERNAME")
    private String sLeaderName;
    /**
     * 主任
     */
    @Column(name="MLEADERID")
    private String mLeaderID;
    @Column(name="MLEADERNAME")
    private String mLeaderName;
    /**
     * 类型（org 表示部门，dept表示小组）
     */
    @Column(name="TYPE")
    private String type;
    @Column(name="SORT",columnDefinition = "INTEGER")
    private Integer sort;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDirectorID() {
        return directorID;
    }

    public void setDirectorID(String directorID) {
        this.directorID = directorID;
    }

    public String getDirectorName() {
        return directorName;
    }

    public void setDirectorName(String directorName) {
        this.directorName = directorName;
    }

    public String getsLeaderID() {
        return sLeaderID;
    }

    public void setsLeaderID(String sLeaderID) {
        this.sLeaderID = sLeaderID;
    }

    public String getsLeaderName() {
        return sLeaderName;
    }

    public void setsLeaderName(String sLeaderName) {
        this.sLeaderName = sLeaderName;
    }

    public String getmLeaderID() {
        return mLeaderID;
    }

    public void setmLeaderID(String mLeaderID) {
        this.mLeaderID = mLeaderID;
    }

    public String getmLeaderName() {
        return mLeaderName;
    }

    public void setmLeaderName(String mLeaderName) {
        this.mLeaderName = mLeaderName;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
