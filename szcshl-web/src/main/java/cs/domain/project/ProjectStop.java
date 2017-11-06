package cs.domain.project;


import cs.domain.DomainBase;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.Date;

/**
 * 设置为动态更新，只更新有修改的字段
 *
 * @author ldm
 */
@Entity
@Table(name = "cs_projectStop")
@DynamicUpdate(true)
public class ProjectStop extends DomainBase {

    /**
     * 暂停ID
     */
    @Id
    private String stopid;

    //实际暂停工作日
    @Column(columnDefinition = "NUMBER")
    private Float pausedays;

    //预计暂停工作日
    @Column(columnDefinition = "NUMBER")
    private Float expectpausedays;

    //暂停时间
    @Column(columnDefinition = "DATE")
    private Date pausetime;

    //实际启动时间
    @Column(columnDefinition = "DATE")
    private Date startTime;

    //预计启动时间
    @Column(columnDefinition = "DATE")
    private Date expectstartTime;

    //暂停说明
    @Column(columnDefinition = "VARCHAR(2048)")
    private String pasedescription;

    @Column(columnDefinition = "VARCHAR(2000)")
    private String projectDesc;    //项目描述

    /**
     * 部长ID
     */
    @Column(columnDefinition = "VARCHAR(64)")
    private String directorId;

    /**
     * 部长名称
     */
    @Column(columnDefinition = "VARCHAR(32)")
    private String directorName;

    /**
     * 分管副主任ID
     */
    @Column(columnDefinition = "VARCHAR(64)")
    private String leaderId;

    /**
     * 分管副主任名称
     */
    @Column(columnDefinition = "VARCHAR(32)")
    private String leaderName;

    /**
     * 部长意见
     */
    @Column(columnDefinition = "VARCHAR(1000)")
    private String directorIdeaContent;

    /**
     * 分管副主任签批
     */
    @Column(columnDefinition = "VARCHAR(1000)")
    private String leaderIdeaContent;

    /**
     * 分管副主任审批日期
     */
    @Column(columnDefinition = "DATE")
    private Date leaderDate;

    /**
     * 部长审批意见
     */
    @Column(columnDefinition = "DATE")
    private Date directorDate;


    /**
     * 已用工作日
     */
    @Column(columnDefinition = "NUMBER")
    private Float userDays;
    /**
     * 审批环节状态
     * 0：已发起审批
     * 1：部长已审批
     * 9：分管副主任已审批
     */
    @Column(columnDefinition = "VARCHAR(2)")
    private String approveStatus;

    /**
     * 是否中心发补充材料函   0：否  9： 是
     */
    @Column(columnDefinition = "VARCHAR(2)")
    private String isSupplementMaterial;

    /**
     * 是否申报单位要求暂停审核函  0 ：否  9： 是
     */
    @Column(columnDefinition = "VARCHAR(2)")
    private String isPuaseApprove;

    /**
     * 审批结果是否通过（9：通过，0，不通过）
     */
    @Column(columnDefinition = "VARCHAR(2)")
    private String isactive;

    /**
     * 是否已经执行（9表示已经执行，1:表示正在执行）
     */
    @Column(columnDefinition = "VARCHAR(2)")
    private String isOverTime;
    /**
     * 流程实例ID
     */
    @Column(columnDefinition = "VARCHAR(64)")
    private String processInstanceId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "signid")
    private Sign sign;

    public Date getLeaderDate() {
        return leaderDate;
    }

    public void setLeaderDate(Date leaderDate) {
        this.leaderDate = leaderDate;
    }

    public Date getDirectorDate() {
        return directorDate;
    }

    public void setDirectorDate(Date directorDate) {
        this.directorDate = directorDate;
    }

    public String getDirectorName() {
        return directorName;
    }

    public void setDirectorName(String directorName) {
        this.directorName = directorName;
    }

    public String getLeaderName() {
        return leaderName;
    }

    public void setLeaderName(String leaderName) {
        this.leaderName = leaderName;
    }

    public String getDirectorIdeaContent() {
        return directorIdeaContent;
    }

    public void setDirectorIdeaContent(String directorIdeaContent) {
        this.directorIdeaContent = directorIdeaContent;
    }

    public String getApproveStatus() {
        return approveStatus;
    }

    public void setApproveStatus(String approveStatus) {
        this.approveStatus = approveStatus;
    }

    public String getIsSupplementMaterial() {
        return isSupplementMaterial;
    }

    public void setIsSupplementMaterial(String isSupplementMaterial) {
        this.isSupplementMaterial = isSupplementMaterial;
    }

    public String getIsPuaseApprove() {
        return isPuaseApprove;
    }

    public void setIsPuaseApprove(String isPuaseApprove) {
        this.isPuaseApprove = isPuaseApprove;
    }

    public Float getExpectpausedays() {
        return expectpausedays;
    }

    public void setExpectpausedays(Float expectpausedays) {
        this.expectpausedays = expectpausedays;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getExpectstartTime() {
        return expectstartTime;
    }

    public void setExpectstartTime(Date expectstartTime) {
        this.expectstartTime = expectstartTime;
    }

    public Sign getSign() {
        return sign;
    }

    public void setSign(Sign sign) {
        this.sign = sign;
    }

    public String getStopid() {
        return stopid;
    }

    public void setStopid(String stopid) {
        this.stopid = stopid;
    }


    public Float getPausedays() {
        return pausedays;
    }

    public void setPausedays(Float pausedays) {
        this.pausedays = pausedays;
    }

    public Date getPausetime() {
        return pausetime;
    }

    public void setPausetime(Date pausetime) {
        this.pausetime = pausetime;
    }

    public String getPasedescription() {
        return pasedescription;
    }

    public void setPasedescription(String pasedescription) {
        this.pasedescription = pasedescription;
    }

    public String getIsactive() {
        return isactive;
    }

    public void setIsactive(String isactive) {
        this.isactive = isactive;
    }

    public String getLeaderIdeaContent() {
        return leaderIdeaContent;
    }

    public void setLeaderIdeaContent(String leaderIdeaContent) {
        this.leaderIdeaContent = leaderIdeaContent;
    }

    public String getProjectDesc() {
        return projectDesc;
    }

    public void setProjectDesc(String projectDesc) {
        this.projectDesc = projectDesc;
    }

    public String getDirectorId() {
        return directorId;
    }

    public void setDirectorId(String directorId) {
        this.directorId = directorId;
    }

    public String getLeaderId() {
        return leaderId;
    }

    public void setLeaderId(String leaderId) {
        this.leaderId = leaderId;
    }

    public String getProcessInstanceId() {
        return processInstanceId;
    }

    public void setProcessInstanceId(String processInstanceId) {
        this.processInstanceId = processInstanceId;
    }

    public Float getUserDays() {
        return userDays;
    }

    public void setUserDays(Float userDays) {
        this.userDays = userDays;
    }

    public String getIsOverTime() {
        return isOverTime;
    }

    public void setIsOverTime(String isOverTime) {
        this.isOverTime = isOverTime;
    }
}