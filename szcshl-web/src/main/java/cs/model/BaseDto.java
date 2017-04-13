package cs.model;

import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;

import cs.common.Util;

public class BaseDto {
	
	private String createdDate;
	private String createdBy;
	
	private String modifiedDate;
	private String modifiedBy;
	public String getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = Util.formatDate(createdDate);
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public String getModifiedDate() {
		return modifiedDate;
	}
	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = Util.formatDate(modifiedDate);
	}
	public String getModifiedBy() {
		return modifiedBy;
	}
	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}
}
