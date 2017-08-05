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
     *暂停ID
     */
    @Id
    private String stopid;

    /**
     * 是否有效（9：是，0：否）
     */
    @Column(columnDefinition = "VARCHAR(1)")
    private String isactive;

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
    private String projectDesc;	//项目描述

	@Column(columnDefinition = "VARCHAR(20)")
	private String directorName;//部长名称

	@Column(columnDefinition = "VARCHAR(20)")
	private String leaderName;//分管副主任名称

    @Column(columnDefinition = "VARCHAR(1000)")
    private String directorIdeaContent;	//部长意见

	@Column(columnDefinition = "VARCHAR(1000)")
	private String leaderIdeaContent;	//分管副主任签批


	@Column(columnDefinition = "VARCHAR(2)")
	private String approveStatus;//审批环节状态    0：未审批   1：部长审批  9：分管副主任审批

	@Column(columnDefinition = "VARCHAR(2)")
	private String isSupplementMaterial;//是否中心发补充材料函   0：否  9： 是

	@Column(columnDefinition = "VARCHAR(2)")
	private String isPuaseApprove;//是否申报单位要求暂停审核函  0 ：否  9： 是
    
    @ManyToOne
    @JoinColumn(name="signid")
    private Sign sign;


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
}