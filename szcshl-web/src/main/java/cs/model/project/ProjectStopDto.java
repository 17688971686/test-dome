package cs.model.project;


import com.alibaba.fastjson.annotation.JSONField;
import cs.domain.DomainBase;
import cs.domain.project.SignDispaWork;

import java.util.Date;

/**
 * 设置为动态更新，只更新有修改的字段
 *
 * @author ldm
 */
public class ProjectStopDto extends DomainBase {

    /**
     * 暂停ID
     */
    private String stopid;

    /**
     * 实际暂停工作日
     */
    private Float pausedays;

    /**
     * 预计暂停工作日
     */
    private Float expectpausedays;

    /**
     * @Column(columnDefinition = "DATE")
     */
    private Date pausetime;

    /**
     * 实际启动时间
     */
    private Date startTime;

    /**
     * 预计启动时间
     */
    private Date expectstartTime;

    /**
     * 暂停说明
     */
    private String pasedescription;


    /**
     * 项目描述
     */
    private String projectDesc;

    /**
     * 部长ID
     */
    private String directorId;

    /**
     * 部长名称
     */
    private String directorName;

    /**
     * 分管副主任ID
     */
    private String leaderId;

    /**
     * 分管副主任名称
     */
    private String leaderName;

    /**
     * 部长意见
     */
    private String directorIdeaContent;

    /**
     * 分管副主任签批
     */
    private String leaderIdeaContent;

    /**
     * 分管副主任审批日期
     */
    @JSONField(format = "yyyy-MM-dd")
    private Date leaderDate;

    /**
     * 部长审批意见
     */
    @JSONField(format = "yyyy-MM-dd")
    private Date directorDate;

    /**
     * 审批环节状态
     * 0：已发起审批
     * 1：部长已审批
     * 9：分管副主任已审批
     */
    private String approveStatus;

    /**
     * 是否中心发补充材料函   0：否  9： 是
     */
    private String isSupplementMaterial;

    /**
     * 是否申报单位要求暂停审核函  0 ：否  9： 是
     */
    private String isPuaseApprove;

    /**
     * 审批结果是否通过（9：通过，0，不通过）
     */
    private String isactive;
    /**
     * 是否已经执行（9表示已经执行，1:表示正在执行）
     */
    private String isOverTime;
    /**
     * 流程实例ID
     */
    private String processInstanceId;

    /**
     * 项目ID
     */
    private String signid;

    /**
     * 已用工作日
     */
    private Float userDays;

    /**
     * 流程名称
     */
    private String processName;

    /**
     * 项目收文对象
     */
    private SignDto signDto;

    private SignDispaWork signDispaWork;

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

    public Float getExpectpausedays() {
        return expectpausedays;
    }

    public void setExpectpausedays(Float expectpausedays) {
        this.expectpausedays = expectpausedays;
    }

    public Date getPausetime() {
        return pausetime;
    }

    public void setPausetime(Date pausetime) {
        this.pausetime = pausetime;
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

    public String getPasedescription() {
        return pasedescription;
    }

    public void setPasedescription(String pasedescription) {
        this.pasedescription = pasedescription;
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

    public String getDirectorName() {
        return directorName;
    }

    public void setDirectorName(String directorName) {
        this.directorName = directorName;
    }

    public String getLeaderId() {
        return leaderId;
    }

    public void setLeaderId(String leaderId) {
        this.leaderId = leaderId;
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

    public String getLeaderIdeaContent() {
        return leaderIdeaContent;
    }

    public void setLeaderIdeaContent(String leaderIdeaContent) {
        this.leaderIdeaContent = leaderIdeaContent;
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

    public String getIsactive() {
        return isactive;
    }

    public void setIsactive(String isactive) {
        this.isactive = isactive;
    }

    public String getProcessInstanceId() {
        return processInstanceId;
    }

    public void setProcessInstanceId(String processInstanceId) {
        this.processInstanceId = processInstanceId;
    }

    public String getSignid() {
        return signid;
    }

    public void setSignid(String signid) {
        this.signid = signid;
    }

    public String getProcessName() {
        return processName;
    }

    public void setProcessName(String processName) {
        this.processName = processName;
    }

    public Float getUserDays() {
        return userDays;
    }

    public void setUserDays(Float userDays) {
        this.userDays = userDays;
    }

    public SignDto getSignDto() {
        return signDto;
    }

    public void setSignDto(SignDto signDto) {
        this.signDto = signDto;
    }

    public SignDispaWork getSignDispaWork() {
        return signDispaWork;
    }

    public void setSignDispaWork(SignDispaWork signDispaWork) {
        this.signDispaWork = signDispaWork;
    }

    public String getIsOverTime() {
        return isOverTime;
    }

    public void setIsOverTime(String isOverTime) {
        this.isOverTime = isOverTime;
    }
}