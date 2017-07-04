package cs.model.sys;

import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;

import cs.model.BaseDto;

public class WorkdayDto extends BaseDto{
	
	private String id;
	
	@JSONField(format = "yyyy-MM-dd")
	private Date dates;
	private String status;
	private String remark;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Date getDate() {
		return dates;
	}
	public void setDate(Date dates) {
		this.dates = dates;
	}
	public Date getDates() {
		return dates;
	}
	public void setDates(Date dates) {
		this.dates = dates;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	

}
