package cs.model.sharing;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;

import cs.domain.sharing.SharingPlatlform;
import cs.domain.sys.Org;
import cs.model.BaseDto;
import cs.model.BaseDto2;
    
    

/**
 * Description: 共享平台 页面数据模型
 * author: sjy
 * Date: 2017-7-11 10:31:02
 */
public class SharingPlatlformDto extends BaseDto {

    private String sharId;
    private String theme;
    private String pubDept;
    private String centerDept;
    private String postResume;
	private String isPublish;//是否发布 0:未发布 ,9:已发布
	private String publishUsername;//发布人
	private Date publishDate;//发布时间 
	private String orgId;
	private List<Org> orgs;
  
    private String remark;
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
	public void setIsPublish(String isPublish) {
		this.isPublish = isPublish;
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