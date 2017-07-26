package cs.domain.sharing;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import cs.domain.DomainBase;
import cs.model.BaseDto;

@Entity
@Table(name = "cs_Sharing_Privilege")
public class SharingPrivilege extends DomainBase {

	@Id
	private String id;
	private String businessId;//主题ID
	private String businessType;//业务类型,部门,用户
	private String remark;//备注
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getBusinessId() {
		return businessId;
	}
	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}
	public String getBusinessType() {
		return businessType;
	}
	public void setBusinessType(String businessType) {
		this.businessType = businessType;
	}
	
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	
	
}
