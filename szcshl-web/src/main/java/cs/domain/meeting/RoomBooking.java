package cs.domain.meeting;

import javax.persistence.Table;

import org.hibernate.annotations.Formula;

import cs.domain.DomainBase;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
/**
 * 会议预定表
 * @author sjy
 * 2017年4月14日
 */
@Entity
@Table(name="cs_roomBooking")
public class RoomBooking extends DomainBase{

	@Id
	private String id;
	@Column(columnDefinition="varchar(255)")
	private String mrID;//会议室编号
	@Column(columnDefinition="varchar(255)")
	private String rbName;//会议名称
	@Formula("(select m.addr from cs_meetingRoom m where m.id = mrID)")
	@Column(columnDefinition="varchar(255)")
	private String addressName;//会议地点
	
	@Column(columnDefinition="varchar(255)")
	private String dueToPeople;//预定人
	@Column(columnDefinition="varchar(255)")
	private String host;//会议主持人
	
	@Column(columnDefinition="date")
	private Date rbDay;//会议日期
	@Column(columnDefinition="date")
	private Date  beginTime;//会议开始时间
	@Column(columnDefinition="date")
	private Date endTime;//结束时间
	
	@Column(columnDefinition="varchar(255)")
	private String rbStatus;//会议预定状态，1,已预定，0未预定
	@Column(columnDefinition="varchar(255)")
	private String rbType;//会议类型
	
	@Column(columnDefinition="varchar(255)")
	private String content;//主要内容
	@Column(columnDefinition="varchar(255)")
	private String remark;//备注
	//工作方案Id
	@Column(columnDefinition="varchar(100)")
	private String workProgramId;
	
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
	
	public Date getRbDay() {
		return rbDay;
	}
	public void setRbDay(Date rbDay) {
		this.rbDay = rbDay;
	}
	public Date getBeginTime() {
		return beginTime;
	}
	public void setBeginTime(Date beginTime) {
		this.beginTime = beginTime;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
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
	public String getContent() {
		return content;
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
	public String getWorkProgramId() {
		return workProgramId;
	}
	public void setWorkProgramId(String workProgramId) {
		this.workProgramId = workProgramId;
	}
	
	
	
}
