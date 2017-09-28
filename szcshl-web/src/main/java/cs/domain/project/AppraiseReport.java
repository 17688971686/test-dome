package cs.domain.project;

import cs.domain.DomainBase;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.Date;

/**
 * Description: 评优评审报告
 * Author: mcl
 * Date: 2017/9/26 11:32
 */

@Entity
@Table(name = "cs_appraise_report")
@DynamicUpdate(true)
public class AppraiseReport extends DomainBase{

    @Id
    private String id ;

    /**
     * 项目名称
     */
    @Column(columnDefinition = "VARCHAR(255)")
    private String projectName;

    /**
     * 申请原因
     */
    @Column(columnDefinition = "VARCHAR(1000)")
    private String appraiseCause;


    /**
     * 评审报告名称
     */
    @Column(columnDefinition = "VARCHAR(255)")
    private String appraiseReportName;

    /**
     * 申请人
     */
    @Column(columnDefinition = "VARCHAR(255)")
    private String proposerName;

    /**
     * 申请时间
     */
    @Column(columnDefinition = "DATE")
    private Date proposerTime;

    /**
     * 部长名称
     */
    @Column(columnDefinition = "VARCHAR(255)")
    private String ministerName ;


    /**
     * 综合部处理人名称
     */
    @Column(columnDefinition = "VARCHAR(255)")
    private String generalConductorName;


    /**
     * 部长意见
     */
    @Column(columnDefinition = "VARCHAR(1000)")
    private String ministerOpinion;

    /**
     * 综合部处理人意见
     */
    @Column(columnDefinition = "VARCHAR(1000)")
    private String generalConductorOpinion;

    /**
     * 审批环节  0 ： 未审批   1：部长审批   9：综合部审批 ，
     */
    @Column(columnDefinition = "VARCHAR(2)")
    private String approveStatus;


    /**
     * 收文ID
     */
    @Column(columnDefinition = "VARCHAR(255)")
    private String signId ;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAppraiseCause() {
        return appraiseCause;
    }

    public void setAppraiseCause(String appraiseCause) {
        this.appraiseCause = appraiseCause;
    }

    public String getMinisterName() {
        return ministerName;
    }

    public void setMinisterName(String ministerName) {
        this.ministerName = ministerName;
    }

    public String getGeneralConductorName() {
        return generalConductorName;
    }

    public void setGeneralConductorName(String generalConductorName) {
        this.generalConductorName = generalConductorName;
    }

    public String getMinisterOpinion() {
        return ministerOpinion;
    }

    public void setMinisterOpinion(String ministerOpinion) {
        this.ministerOpinion = ministerOpinion;
    }

    public String getGeneralConductorOpinion() {
        return generalConductorOpinion;
    }

    public void setGeneralConductorOpinion(String generalConductorOpinion) {
        this.generalConductorOpinion = generalConductorOpinion;
    }

    public String getSignId() {
        return signId;
    }

    public void setSignId(String signId) {
        this.signId = signId;
    }

    public String getAppraiseReportName() {
        return appraiseReportName;
    }

    public void setAppraiseReportName(String appraiseReportName) {
        this.appraiseReportName = appraiseReportName;
    }

    public String getProposerName() {
        return proposerName;
    }

    public void setProposerName(String proposerName) {
        this.proposerName = proposerName;
    }

    public Date getProposerTime() {
        return proposerTime;
    }

    public void setProposerTime(Date proposerTime) {
        this.proposerTime = proposerTime;
    }

    public String getApproveStatus() {
        return approveStatus;
    }

    public void setApproveStatus(String approveStatus) {
        this.approveStatus = approveStatus;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }
}