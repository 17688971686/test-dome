package cs.domain.sharing;


import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import cs.domain.DomainBase;
import cs.domain.sys.Org;
import cs.domain.sys.User;


@Entity
@Table(name = "cs_sharing_Platform")
public class SharingPlatlform extends DomainBase{

	@Id
	private String sharId;
	  
	@Column(columnDefinition="VARCHAR(64)")
	private String theme;
	
	@Column(columnDefinition="VARCHAR(64)")
	private String pubDept;
	 
	@Column(columnDefinition="VARCHAR(128)")
	private String centerDept;
	
	@Column(columnDefinition="VARCHAR(128)")
	private String postResume;
	 
	@Column(columnDefinition="VARCHAR(2)")
	private String isPublish;//是否发布 0:未发布 ,9:已发布
	
	@Column(columnDefinition="VARCHAR(30)")
	private String publishUsername;//发布人
	
	@Column(columnDefinition="DATE")
	private Date publishDate;//发布时间
	
	@Column(columnDefinition="VARCHAR(128)")
	private String orgId;
	
	@OneToMany(mappedBy = "sharing",fetch = FetchType.LAZY)
	private List<Org> orgs;
	
	@Column(columnDefinition="VARCHAR(255)")
	private String remark;
	
	@Column(columnDefinition="VARCHAR(255)")
	private String content;
	
	public String getSharId() {
		return sharId;
	}
	public void setSharId(String sharId) {
		this.sharId = sharId;
	}
	public String getTheme() {
		return theme;
	}
	public void setTheme(String theme) {
		this.theme = theme;
	}
	public String getPubDept() {
		return pubDept;
	}
	public void setPubDept(String pubDept) {
		this.pubDept = pubDept;
	}

	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getCenterDept() {
		return centerDept;
	}
	public void setCenterDept(String centerDept) {
		this.centerDept = centerDept;
	}
	public String getPostResume() {
		return postResume;
	}
	public void setPostResume(String postResume) {
		this.postResume = postResume;
	}
	public String getIsPublish() {
		return isPublish;
	}
	public void setIsPublish(String string) {
		this.isPublish = string;
	}
	public String getPublishUsername() {
		return publishUsername;
	}
	public void setPublishUsername(String publishUsername) {
		this.publishUsername = publishUsername;
	}
	public Date getPublishDate() {
		return publishDate;
	}
	public void setPublishDate(Date publishDate) {
		this.publishDate = publishDate;
	}
	public List<Org> getOrgs() {
		return orgs;
	}
	public void setOrgs(List<Org> orgs) {
		this.orgs = orgs;
	}
	public String getOrgId() {
		return orgId;
	}
	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	
	
	
	
	
}
