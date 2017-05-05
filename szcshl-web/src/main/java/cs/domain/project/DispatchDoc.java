package cs.domain.project;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicUpdate;

import cs.domain.DomainBase;

/**
 * 发文
 * @author ldm
 *
 */
@Entity
@Table(name = "DISPATCH_DOC")
@DynamicUpdate(true)
public class DispatchDoc extends DomainBase{

	//主键
	@Id
	private String id;	
	
	//发文方式
	@Column(columnDefinition="VARCHAR(20)")
	private String dispatchWay;
	
	//项目类别
	@Column(columnDefinition="VARCHAR(2)")
	private String projectType;
	
	//发文类别
	@Column(columnDefinition="VARCHAR(20)")
	private String dispatchType;
	
	//文件标题
	@Column(columnDefinition="VARCHAR(256)")
	private String fileTitle;
	
	//范文部门
	@Column(columnDefinition="VARCHAR(64)")
	private String orgId;
	
	//拟稿人
	@Column(columnDefinition="VARCHAR(64)")
	private String userId;
	
	//拟稿日期
	@Column(columnDefinition="DATE")
	private Date draftDate;
	
	//密码等级
	@Column(columnDefinition="VARCHAR(64)")
	private String secretLevel;
	
	//年度计划
	@Column(columnDefinition="VARCHAR(64)")
	private String yearPlan;
	
	//紧急等级
	@Column(columnDefinition="VARCHAR(64)")
	private String urgentLevel;
	
	//发文日期
	@Column(columnDefinition="DATE")
	private Date dispatchDate;
	
	//文件字号
	@Column(columnDefinition="VARCHAR(64)")
	private String fileNum;
	
	//发文范围
	@Column(columnDefinition="VARCHAR(512)")
	private String dispatchScope;
	
	//打印份数
	@Column(columnDefinition="INTEGER")
	private Integer printCount;
	
	//相关说明
	@Column(columnDefinition="VARCHAR(2000)")
	private String description;
	
	//评审意见摘要
	@Column(columnDefinition="VARCHAR(2000)")
	private String reviewAbstract;
	
	//部长核稿意见
	@Column(columnDefinition="VARCHAR(2000)")
	private String ministerSuggesttion;
	
	//部长核稿日期
	@Column(columnDefinition="DATE")
	private Date ministerDate;
	
	//分管副局长核稿意见
	@Column(columnDefinition="VARCHAR(2000)")
	private String viceDirectorSuggesttion;
	
	//分管副局长核稿日期
	@Column(columnDefinition="DATE")
	private Date viceDirectorDate;
	
	//局长核稿意见
	@Column(columnDefinition="VARCHAR(2000)")
	private String directorSuggesttion;
	
	//局长核稿日期
	@Column(columnDefinition="DATE")
	private Date directorDate;

	//收文，一对一
	@OneToOne
	@JoinColumn(name="signId")
	private Sign sign;
		
	public Sign getSign() {
		return sign;
	}

	public void setSign(Sign sign) {
		this.sign = sign;
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

	public Integer getPrintCount() {
		return printCount;
	}

	public void setPrintCount(Integer printCount) {
		this.printCount = printCount;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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
