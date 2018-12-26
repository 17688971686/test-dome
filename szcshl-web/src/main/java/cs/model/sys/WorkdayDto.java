package cs.model.sys;

import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;

import cs.model.BaseDto;

/**
 * @author mcl
 * 工作日管理Dto
 */
public class WorkdayDto extends BaseDto{
	
	private String id;
	private String month;
	private String datess;
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
	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public String getDatess() {
		return datess;
	}

	public void setDatess(String datess) {
		this.datess = datess;
	}
}
