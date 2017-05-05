package cs.model.meeting;

import cs.model.BaseDto;

public class MeetingRoomDto extends BaseDto{

	private String id;
	private String num;//编号
	private String mrName;//会议名称
	private String addr;//会议地址
	private String mrType;//会议室类型，比如多媒体、普通等，应从字典表获取数据
	private String mrStatus;//会议室状态、1、正常、2、暂停使用
	private String capacity;//会议室容量，单位人
	private String userName;//会议室负责人
	private String userPhone;//负责人联系电话
	private String createDate;//创建时间
	private String remark;//备注
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	
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
	public String getCreateDate() {
		return createDate;
	}
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	
}
