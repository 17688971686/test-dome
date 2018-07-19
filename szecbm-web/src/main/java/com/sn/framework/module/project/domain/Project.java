package com.sn.framework.module.project.domain;
import com.sn.framework.core.DomainBase;
import lombok.Data;
import javax.persistence.*;
import java.util.Date;


/**
 * 设置为动态更新，只更新有修改的字段
 *
 * @author ldm
 */
@Entity
@Table(name = "cs_project")
@Data
public class Project extends DomainBase {

    /**
     * 收文ID
     */
    @Id
    private String id;

    /**
     * 委内收文编号
     */
    @Column(columnDefinition = "VARCHAR(32)")
    private String fileCode;


    //项目名称
    @Column(columnDefinition = "VARCHAR(200)")
        private String projectName;


    //评审阶段
    @Column(columnDefinition = "VARCHAR(64)")
    private String reviewStage;

    //项目单位
    @Column(columnDefinition = "VARCHAR(150)")
    private String proUnit;

    //评审部门
    @Column(columnDefinition = "VARCHAR(1)")
    private String reviewDept;

    //项目负责人
    @Column(columnDefinition = "VARCHAR(64)")
    private String mainUser;

    //发文日期
    @Column
    private String dispatchDate;

    //发文号
    @Column(columnDefinition = "VARCHAR(64)")
    private String fileNum;

    //存档日期
    @Column
    private String fileDate;

    //存档号
    @Column(columnDefinition="VARCHAR(32)")
    private String fileNo;

    //备注
    @Column(columnDefinition="VARCHAR(200)")
    private String remark;

    //状态：1.正常 2.作废
    @Column(columnDefinition="VARCHAR(1)")
    private String status;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFileCode() {
        return fileCode;
    }

    public void setFileCode(String fileCode) {
        this.fileCode = fileCode;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getReviewStage() {
        return reviewStage;
    }

    public void setReviewStage(String reviewStage) {
        this.reviewStage = reviewStage;
    }

    public String getProUnit() {
        return proUnit;
    }

    public void setProUnit(String proUnit) {
        this.proUnit = proUnit;
    }

    public String getReviewDept() {
        return reviewDept;
    }

    public void setReviewDept(String reviewDept) {
        this.reviewDept = reviewDept;
    }

    public String getDispatchDate() {
        return dispatchDate;
    }

    public void setDispatchDate(String dispatchDate) {
        this.dispatchDate = dispatchDate;
    }

    public String getFileNum() {
        return fileNum;
    }

    public void setFileNum(String fileNum) {
        this.fileNum = fileNum;
    }

    public String getFileDate() {
        return fileDate;
    }

    public void setFileDate(String fileDate) {
        this.fileDate = fileDate;
    }

    public String getFileNo() {
        return fileNo;
    }

    public void setFileNo(String fileNo) {
        this.fileNo = fileNo;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getMainUser() {
        return mainUser;
    }

    public void setMainUser(String mainUser) {
        this.mainUser = mainUser;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}