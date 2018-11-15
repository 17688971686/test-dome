package com.sn.framework.module.project.domain;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.sn.framework.core.DomainBase;
import lombok.Data;
import javax.persistence.*;
import java.util.Date;


/**
 *
 *
 * @author zsl
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

    /**
     * 主办部门ID
     */
    @Column(columnDefinition = "VARCHAR(64)")
    private String mainOrgId;

    /**
     * 主办部门名称
     */
    @Column(columnDefinition = "VARCHAR(32)")
    private String mainOrgName;

    /**
     * 协办部门ID
     */
    @Column(columnDefinition = "VARCHAR(512)")
    private String assistOrgId;

    /**
     * 协办部门名称
     */
    @Column(columnDefinition = "VARCHAR(256)")
    private String assistOrgName;

    //项目负责人
    @Column(columnDefinition = "VARCHAR(64)")
    private String mainUser;

    @Column(columnDefinition = "VARCHAR(64)")
    private String mainUserName;

    //其他项目负责人
    @Column(columnDefinition = "VARCHAR(1024)")
    private String assistUser;

    @Column(columnDefinition = "VARCHAR(1024)")
    private String assistUserName;

    //发文日期
    @JsonFormat(pattern = "yyyy-mm-dd")
    @Column(columnDefinition = "Date")
    @Temporal(TemporalType.DATE)
    private Date dispatchDate;

    //发文号
    @Column(columnDefinition = "VARCHAR(64)")
    private String fileNum;

    //存档日期
    @JsonFormat(pattern = "yyyy-mm-dd")
    @Column(columnDefinition = "Date")
    @Temporal(TemporalType.DATE)
    private Date fileDate;

    //存档号
    @Column(columnDefinition="VARCHAR(32)")
    private String fileNo;

    //备注
    @Column(columnDefinition="VARCHAR(500)")
    private String remark;

    //状态：1.正常 2.作废
    @Column(columnDefinition="VARCHAR(1)")
    private String status;

    //是否调概
    @Column(columnDefinition = "VARCHAR(2)")
    private String ischangeEstimate;

    //是否提前介入
    @Column(columnDefinition = "VARCHAR(2)")
    private String isAdvanced;

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

    public String getMainOrgId() {
        return mainOrgId;
    }

    public void setMainOrgId(String mainOrgId) {
        this.mainOrgId = mainOrgId;
    }

    public String getMainOrgName() {
        return mainOrgName;
    }

    public void setMainOrgName(String mainOrgName) {
        this.mainOrgName = mainOrgName;
    }

    public String getAssistOrgId() {
        return assistOrgId;
    }

    public void setAssistOrgId(String assistOrgId) {
        this.assistOrgId = assistOrgId;
    }

    public String getAssistOrgName() {
        return assistOrgName;
    }

    public void setAssistOrgName(String assistOrgName) {
        this.assistOrgName = assistOrgName;
    }

    public String getFileNum() {
        return fileNum;
    }

    public void setFileNum(String fileNum) {
        this.fileNum = fileNum;
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

    public String getAssistUser() {
        return assistUser;
    }

    public void setAssistUser(String assistUser) {
        this.assistUser = assistUser;
    }

    public Date getDispatchDate() {
        return dispatchDate;
    }

    public void setDispatchDate(Date dispatchDate) {
        this.dispatchDate = dispatchDate;
    }

    public Date getFileDate() {
        return fileDate;
    }

    public void setFileDate(Date fileDate) {
        this.fileDate = fileDate;
    }

    public String getIschangeEstimate() {
        return ischangeEstimate;
    }

    public void setIschangeEstimate(String ischangeEstimate) {
        this.ischangeEstimate = ischangeEstimate;
    }

    public String getIsAdvanced() {
        return isAdvanced;
    }

    public void setIsAdvanced(String isAdvanced) {
        this.isAdvanced = isAdvanced;
    }

    public String getMainUserName() {
        return mainUserName;
    }

    public void setMainUserName(String mainUserName) {
        this.mainUserName = mainUserName;
    }

    public String getAssistUserName() {
        return assistUserName;
    }

    public void setAssistUserName(String assistUserName) {
        this.assistUserName = assistUserName;
    }
}