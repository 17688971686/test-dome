package cs.model.project;

import cs.model.BaseDto;

public class MergeDispaDto extends BaseDto {
	private String signId;
	private String linkSignId;
	private String businessId;
	private String type;
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
