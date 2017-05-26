package cs.domain.project;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicUpdate;

import cs.domain.DomainBase;
/**
 * 合并发文
 * @author ldm
 *
 */
@Entity
@Table(name = "cs_merge_dispa")
@DynamicUpdate(true)	
public class MergeDispa extends DomainBase{
	@Id
	private String businessId;//业务ID(发文/工作方案）
	@Column(columnDefinition="VARCHAR(1000)")
	private String linkSignId;//关联收文ID(一对多）
	@Column(columnDefinition="VARCHAR(64)")
	private String signId;//收文ID
	@Column(columnDefinition="VARCHAR(20)")
	private String type;//业务类型
	public String getSignId() {
		return signId;
	}
	public void setSignId(String signId) {
		this.signId = signId;
	}
	public String getLinkSignId() {
		return linkSignId;
	}
	public void setLinkSignId(String linkSignId) {
		this.linkSignId = linkSignId;
	}
	public String getBusinessId() {
		return businessId;
	}
	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
}
