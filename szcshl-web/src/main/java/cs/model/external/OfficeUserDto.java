package cs.model.external;

import javax.persistence.Column;

import cs.domain.external.Dept;
import cs.model.BaseDto;

public class OfficeUserDto extends BaseDto {

	private String officeID;
	
	//办事处联系人
	private String officeUserName;

	//所在办事处
	private String deptName;
	
	//办事处联系人电话
	private String officePhone;
	
	//办事处联系人邮件
	private String officeEmail;
	
	//描述
	private String officeDesc;
	
	
	private String departId;	//关联办事处ID
	
	private Dept dept;
	
	public String getOfficeID() {
		return officeID;
	}

	public void setOfficeID(String officeID) {
		this.officeID = officeID;
	}

	public String getOfficeUserName() {
		return officeUserName;
	}

	public void setOfficeUserName(String officeUserName) {
		this.officeUserName = officeUserName;
	}

	public String getOfficePhone() {
		return officePhone;
	}

	public void setOfficePhone(String officePhone) {
		this.officePhone = officePhone;
	}

	public String getOfficeEmail() {
		return officeEmail;
	}

	public void setOfficeEmail(String officeEmail) {
		this.officeEmail = officeEmail;
	}

	public String getOfficeDesc() {
		return officeDesc;
	}

	public void setOfficeDesc(String officeDesc) {
		this.officeDesc = officeDesc;
	}

	

	public String getDepartId() {
		return departId;
	}

	public void setDepartId(String departId) {
		this.departId = departId;
	}

	public Dept getDept() {
		return dept;
	}

	public void setDept(Dept dept) {
		this.dept = dept;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	
	
	
}
