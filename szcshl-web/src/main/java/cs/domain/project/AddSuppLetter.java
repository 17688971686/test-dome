package cs.domain.project;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicUpdate;

import com.alibaba.fastjson.annotation.JSONField;

import cs.domain.DomainBase;
/*
 * 添加项目资料补充函
 */
@Entity
@Table(name = "cs_add_suppLetter")
@DynamicUpdate(true)
public class AddSuppLetter extends DomainBase {
	//拟稿编号
	@Id
	private String id;
	
   //拟稿部门
	@Column(columnDefinition="VARCHAR(64)")
	private String orgName;
	
	//拟稿人
	@Column(columnDefinition = "VARCHAR(64)")	
	private String userName;
	
	//拟稿时间
	@Column(columnDefinition = "Date")
	private Date suppLetterTime;
	
	//发文日期
	@Column(columnDefinition = "Date")
	private Date disapDate;
	
	//秘密等级
	@Column(columnDefinition = "VARCHAR(32)")
	private String secretLevel;
	
	//缓急程度
	@Column(columnDefinition = "VARCHAR(10)")
	private String mergencyLevel;
	
	//文件字号
	@Column(columnDefinition ="VARCHAR(32)")
	private String filenum;
	
	//用户登录id
	@Column(columnDefinition ="VARCHAR(100)")
	private String userId;
	
	//文件标题
	@Column(columnDefinition = "VARCHAR(200)")
	private String title;
	
	//发行范围
	@Column(columnDefinition = "VARCHAR(225)")
	private String dispaRange;
	
	//核稿意见
	@Column(columnDefinition = "VARCHAR(225)")
	private String suppleterSuggest;
	
	//会签意见
	@Column(columnDefinition = "VARCHAR(225)")
	private String meetingSuggest;
	
	//领导意见
	@Column(columnDefinition = "VARCHAR(225)")
	private String leaderSuggest;
	
	//打印份数
	@Column(columnDefinition = "VARCHAR(10)")
	private String printnum;
	
	//收文id
	@Column(columnDefinition = "VARCHAR(64)")
	private String signid;
	
	//业务ID
	@Column(columnDefinition = "VARCHAR(64)")
	private String businessId;
	
	//业务类型
	@Column(columnDefinition = "VARCHAR(64)")
	private String businessIdType;
	
	//文字序号
    @Column(columnDefinition = "INTEGER")
	private Integer fileSeq;
	
    
	public Integer getFileSeq() {
		return fileSeq;
	}

	public void setFileSeq(Integer fileSeq) {
		this.fileSeq = fileSeq;
	}

	public String getSignid() {
		return signid;
	}

	public void setSignid(String signid) {
		this.signid = signid;
	}

	public String getMeetingSuggest() {
		return meetingSuggest;
	}

	public void setMeetingSuggest(String meetingSuggest) {
		this.meetingSuggest = meetingSuggest;
	}

	public String getLeaderSuggest() {
		return leaderSuggest;
	}

	public void setLeaderSuggest(String leaderSuggest) {
		this.leaderSuggest = leaderSuggest;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Date getSuppLetterTime() {
		return suppLetterTime;
	}

	public void setSuppLetterTime(Date suppLetterTime) {
		this.suppLetterTime = suppLetterTime;
	}

	public String getSecretLevel() {
		return secretLevel;
	}

	public void setSecretLevel(String secretLevel) {
		this.secretLevel = secretLevel;
	}

	public String getMergencyLevel() {
		return mergencyLevel;
	}

	public void setMergencyLevel(String mergencyLevel) {
		this.mergencyLevel = mergencyLevel;
	}

	public String getFilenum() {
		return filenum;
	}

	public void setFilenum(String filenum) {
		this.filenum = filenum;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDispaRange() {
		return dispaRange;
	}

	public void setDispaRange(String dispaRange) {
		this.dispaRange = dispaRange;
	}

	public String getSuppleterSuggest() {
		return suppleterSuggest;
	}

	public void setSuppleterSuggest(String suppleterSuggest) {
		this.suppleterSuggest = suppleterSuggest;
	}

	public String getPrintnum() {
		return printnum;
	}

	public void setPrintnum(String printnum) {
		this.printnum = printnum;
	}

	public Date getDisapDate() {
		return disapDate;
	}

	public void setDisapDate(Date disapDate) {
		this.disapDate = disapDate;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getBusinessId() {
		return businessId;
	}

	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}

	public String getBusinessIdType() {
		return businessIdType;
	}

	public void setBusinessIdType(String businessIdType) {
		this.businessIdType = businessIdType;
	}
	
	
	
}
