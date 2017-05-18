package cs.domain.external;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Formula;

import cs.domain.DomainBase;

/**
 * 办事处人员管理
 * @author sjy
 *
 */

@Entity
@Table(name="cs_OfficeUser")
public class OfficeUser extends DomainBase{

	@Id
//	@SequenceGenerator(name = "generator_increment", sequenceName = "seq_increment" )
//	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "generator_increment")
	private String officeID;
	
	//办事处联系人
	@Column(columnDefinition="varchar(80) ")
	private String officeUserName;
	//所在办事处
	@Formula("(select d.deptName from dept d where d.deptId = departId)")
	private String deptName;
	
	//办事处联系人电话
	@Column(columnDefinition="varchar(100) ")
	private String officePhone;
	
	//办事处联系人邮件
	@Column(columnDefinition="varchar(150) ")
	private String officeEmail;
	
	//描述
	@Column(columnDefinition="varchar(200) ")
	private String officeDesc;
	
	@ManyToOne
	@JoinColumn(name="departId")
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
