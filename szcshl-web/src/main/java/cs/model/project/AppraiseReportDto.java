package cs.model.project;

import com.alibaba.fastjson.annotation.JSONField;
import cs.domain.project.Sign;
import cs.model.BaseDto;

import java.util.Date;

/**
 * Description: 优秀评审报告
 * Author: mcl
 * Date: 2017/9/27 10:07
 */
public class AppraiseReportDto extends BaseDto{

    private String id ;

    /**
     * 申请原因
     */
    private String appraiseCause;


    /**
     * 项目名称
     */
    private String projectName;


    /**
     * 评审报告名称
     */
    private String appraiseReportName;

    /**
     * 申请人
     */
    private String proposerName;

    /**
     * 申请时间
     */
    @JSONField(format = "yyyy-MM-dd")
    private Date proposerTime;

    /**
     * 部长名称
     */
    private String ministerName ;


    /**
     * 综合部处理人名称
     */
    private String generalConductorName;


    /**
     * 部长意见
     */
    private String ministerOpinion;

    /**
     * 综合部处理人意见
     */
    private String generalConductorOpinion;


    /**
     * 审批环节  0 ： 未审批   1：部长审批   9：综合部审批
     */
    private String approveStatus;


    private String signId;

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