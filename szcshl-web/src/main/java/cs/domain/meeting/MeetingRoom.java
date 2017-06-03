package cs.domain.meeting;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import cs.domain.DomainBase;

/**
 * 会议室表
 * @author wyl
 * 2016年9月12日
 */
@Entity
@Table(name = "cs_meeting_room")
public class MeetingRoom extends DomainBase{

	@Id	
	private String id;
	@Column(columnDefinition="varchar(255)")
	private String num;//编号
	@Column(columnDefinition="varchar(255) ")
	private String mrName;//会议名称
	@Column(columnDefinition="varchar(255) ")
	private String addr;//会议地址
	@Column(columnDefinition="varchar(255) ")
	private String mrType;//会议室类型，比如多媒体、普通等，应从字典表获取数据
	@Column(columnDefinition="varchar(255) ")
	private String mrStatus;//会议室状态、1、正常、2、暂停使用
	@Column(columnDefinition="varchar(255) ")
	private String capacity;//会议室容量，单位人
	@Column(columnDefinition="varchar(255) ")
	private String userName;//会议室负责人
	@Column(columnDefinition="varchar(255) ")
	private String userPhone;//负责人联系电话
	@Column(columnDefinition="varchar(255) ")
	private String remark;//备注
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getNum() {
		return num;
	}
	public void setNum(String num) {
		this.num = num;
	}
	public String getMrName() {
		return mrName;
	}
	public void setMrName(String mrName) {
		this.mrName = mrName;
	}
	public String getAddr() {
		return addr;
	}
	public void setAddr(String addr) {
		this.addr = addr;
	}
	public String getMrType() {
		return mrType;
	}
	public void setMrType(String mrType) {
		this.mrType = mrType;
	}
	public String getMrStatus() {
		return mrStatus;
	}
	public void setMrStatus(String mrStatus) {
		this.mrStatus = mrStatus;
	}
	public String getCapacity() {
		return capacity;
	}
	public void setCapacity(String capacity) {
		this.capacity = capacity;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserPhone() {
		return userPhone;
	}
	public void setUserPhone(String userPhone) {
		this.userPhone = userPhone;
	}

	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
}
