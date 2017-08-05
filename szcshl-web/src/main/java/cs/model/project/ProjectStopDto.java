package cs.model.project;


import com.alibaba.fastjson.annotation.JSONField;
import cs.domain.DomainBase;
import cs.domain.project.Sign;

import java.util.Date;

/**
 * 设置为动态更新，只更新有修改的字段
 *
 * @author ldm
 */
public class ProjectStopDto extends DomainBase {

    private String stopid;

    /**
     * 是否有效（9：是，0：否）
     */
    private String isactive;

    //实际暂停工作日
    private Float pausedays;

    //预计暂停工作日
    private Float expectpausedays;

    //暂停时间
    @JSONField(format = "yyyy-MM-dd")
    private Date pausetime;

    //实际启动时间
    @JSONField(format = "yyyy-MM-dd")
    private Date startTime;

    //预计启动时间
    @JSONField(format = "yyyy-MM-dd")
    private Date expectstartTime;

    //暂停说明
    private String pasedescription;

    private String projectDesc;	//项目描述

    private String signid;

    private Sign sign;

    private String directorIdeaContent;	//部长意见

    private String leaderIdeaContent;	//分管副主任签批

    private String isSupplementMaterial;//是否中心发补充材料函

    private String isPuaseApprove;//是否申报单位要求暂停审核函

    private String directorName;//部长名称

    private String leaderName;//分管副主任名称

    private String material;
    private String approveStatus;//审批环节状态    0：为审批   1：部长审批  9：分管副主任审批

    public String getApproveStatus() {
        return approveStatus;
    }

    public void setApproveStatus(String approveStatus) {
        this.approveStatus = approveStatus;
    }

    public String getLeaderName() {
        return leaderName;
    }

    public void setLeaderName(String leaderName) {
        this.leaderName = leaderName;
    }

    public String getDirectorName() {
        return directorName;
    }

    public void setDirectorName(String directorName) {
        this.directorName = directorName;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
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

    public String getProjectDesc() {
        return projectDesc;
    }

    public void setProjectDesc(String projectDesc) {
        this.projectDesc = projectDesc;
    }

    public Sign getSign() {

        return sign;
    }

    public void setSign(Sign sign) {
        this.sign = sign;
    }

    public String getSignid() {
        return signid;
    }

    public void setSignid(String signid) {
        this.signid = signid;
    }

    public String getLeaderIdeaContent() {
        return leaderIdeaContent;
    }

    public void setLeaderIdeaContent(String leaderIdeaContent) {
        this.leaderIdeaContent = leaderIdeaContent;
    }

    public String getDirectorIdeaContent() {
        return directorIdeaContent;
    }

    public void setDirectorIdeaContent(String directorIdeaContent) {
        this.directorIdeaContent = directorIdeaContent;
    }

    public String getStopid() {
        return stopid;
    }

    public void setStopid(String stopid) {
        this.stopid = stopid;
    }

    public String getIsactive() {
        return isactive;
    }

    public void setIsactive(String isactive) {
        this.isactive = isactive;
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
}