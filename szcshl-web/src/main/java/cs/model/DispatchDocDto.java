package cs.model;

import java.util.Date;

public class DispatchDocDto extends BaseDto{
	//主键
	private String id;	
	//发文方式
	private String dispatchWay;
	//项目类别
	private String projectType;
	//发文类别
	private String dispatchType;
	
	private String fileTitle;
	
	private String orgId;
	
	private String userId;
	
	private Date draftDate;
	
	private String secretLevel;
	
	private String yearPlan;
	
	private String urgentLevel;
	
	private Date dispatchDate;
	
	private String fileNum;
	
	private String dispatchScope;
	
	private String printCount;
	
	private String desc;
	
	private String reviewAbstract;
	
	private String ministerSuggesttion;
	
	private Date ministerDate;
	
	private String viceDirectorSuggesttion;
	
	private Date viceDirectorDate;
	
	private String directorSuggesttion;
	
	private Date directorDate;

	//签收ID
	private String signId;
		
	public String getSignId() {
		return signId;
	}

	public void setSignId(String signId) {
		this.signId = signId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDispatchWay() {
		return dispatchWay;
	}

	public void setDispatchWay(String dispatchWay) {
		this.dispatchWay = dispatchWay;
	}

	public String getProjectType() {
		return projectType;
	}

	public void setProjectType(String projectType) {
		this.projectType = projectType;
	}

	public String getDispatchType() {
		return dispatchType;
	}

	public void setDispatchType(String dispatchType) {
		this.dispatchType = dispatchType;
	}

	public String getFileTitle() {
		return fileTitle;
	}

	public void setFileTitle(String fileTitle) {
		this.fileTitle = fileTitle;
	}

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Date getDraftDate() {
		return draftDate;
	}

	public void setDraftDate(Date draftDate) {
		this.draftDate = draftDate;
	}

	public String getSecretLevel() {
		return secretLevel;
	}

	public void setSecretLevel(String secretLevel) {
		this.secretLevel = secretLevel;
	}

	public String getYearPlan() {
		return yearPlan;
	}

	public void setYearPlan(String yearPlan) {
		this.yearPlan = yearPlan;
	}

	public String getUrgentLevel() {
		return urgentLevel;
	}

	public void setUrgentLevel(String urgentLevel) {
		this.urgentLevel = urgentLevel;
	}

	public Date getDispatchDate() {
		return dispatchDate;
	}

	public void setDispatchDate(Date dispatchDate) {
		this.dispatchDate = dispatchDate;
	}

	public String getFileNum() {
		return fileNum;
	}

	public void setFileNum(String fileNum) {
		this.fileNum = fileNum;
	}

	public String getDispatchScope() {
		return dispatchScope;
	}

	public void setDispatchScope(String dispatchScope) {
		this.dispatchScope = dispatchScope;
	}

	public String getPrintCount() {
		return printCount;
	}

	public void setPrintCount(String printCount) {
		this.printCount = printCount;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getReviewAbstract() {
		return reviewAbstract;
	}

	public void setReviewAbstract(String reviewAbstract) {
		this.reviewAbstract = reviewAbstract;
	}

	public String getMinisterSuggesttion() {
		return ministerSuggesttion;
	}

	public void setMinisterSuggesttion(String ministerSuggesttion) {
		this.ministerSuggesttion = ministerSuggesttion;
	}

	public Date getMinisterDate() {
		return ministerDate;
	}

	public void setMinisterDate(Date ministerDate) {
		this.ministerDate = ministerDate;
	}

	public String getViceDirectorSuggesttion() {
		return viceDirectorSuggesttion;
	}

	public void setViceDirectorSuggesttion(String viceDirectorSuggesttion) {
		this.viceDirectorSuggesttion = viceDirectorSuggesttion;
	}

	public Date getViceDirectorDate() {
		return viceDirectorDate;
	}

	public void setViceDirectorDate(Date viceDirectorDate) {
		this.viceDirectorDate = viceDirectorDate;
	}

	public String getDirectorSuggesttion() {
		return directorSuggesttion;
	}

	public void setDirectorSuggesttion(String directorSuggesttion) {
		this.directorSuggesttion = directorSuggesttion;
	}

	public Date getDirectorDate() {
		return directorDate;
	}

	public void setDirectorDate(Date directorDate) {
		this.directorDate = directorDate;
	}
			
}
