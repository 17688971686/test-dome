package cs.model.meeting;


import java.util.Date;

import javax.persistence.Column;

import org.hibernate.annotations.Formula;
import org.springframework.format.annotation.DateTimeFormat;

import cs.model.BaseDto;

public class RoomBookingDto extends BaseDto{

	private String id;
	private String mrID;//会议室编号
	private String rbName;//会议名称
	private String addressName;//会议地点
	private String dueToPeople;//预定人
	private String host;//会议主持人
	private String rbDay;//会议日期
	private String  beginTime;//会议开始时间
	private String endTime;//结束时间
	private String rbStatus;//会议预定状态，1,已预定，0未预定
	private String rbType;//会议类型
	private String content;//主要内容
	private String remark;//备注
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getMrID() {
		return mrID;
	}
	public void setMrID(String mrID) {
		this.mrID = mrID;
	}
	public String getRbName() {
		return rbName;
	}
	public void setRbName(String rbName) {
		this.rbName = rbName;
	}
	@Formula("(select m.addr from cs_meetingRoom m where m.id = mrID)")
	public String getAddressName() {
		return addressName;
	}
	public void setAddressName(String addressName) {
		this.addressName = addressName;
	}
	public String getDueToPeople() {
		return dueToPeople;
	}
	public void setDueToPeople(String dueToPeople) {
		this.dueToPeople = dueToPeople;
	}
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public String getRbDay() {
		return rbDay;
	}
	public void setRbDay(String rbDay) {
		this.rbDay = rbDay;
	}
	
	public String getBeginTime() {
		return beginTime;
	}
	public void setBeginTime(String beginTime) {
		this.beginTime = beginTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public String getContent() {
		return content;
	}
	public String getRbStatus() {
		return rbStatus;
	}
	public void setRbStatus(String rbStatus) {
		this.rbStatus = rbStatus;
	}
	public String getRbType() {
		return rbType;
	}
	public void setRbType(String rbType) {
		this.rbType = rbType;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	
}
