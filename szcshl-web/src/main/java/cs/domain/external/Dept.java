package cs.domain.external;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Formula;

import cs.domain.DomainBase;

@Entity
@Table(name = "DEPT")
@DynamicUpdate(true)
public class Dept extends DomainBase{

	@Id
	private String deptId;
	
	@Column(columnDefinition="VARCHAR(128)")
	private String deptName;
	
	//办事处联系人
	@Column(columnDefinition="VARCHAR(128)")
	@Formula("(select o.officeUserName from cs_OfficeUser o where o.officeID = deptOfficeId)")
	private String deptUserName;
	
	//办事处人员ID
	@Column(columnDefinition="varchar(255)")
	private String deptOfficeId;
	
	@Column(columnDefinition="VARCHAR(256)")
	private String address;
	
	@Column(columnDefinition="VARCHAR(16)")
	private String deptType;
	
	@Column(columnDefinition="VARCHAR(2)")
	private String status;
	
	@OneToMany(mappedBy="dept")
	private List<OfficeUser> offices;

	public String getDeptId() {
		return deptId;
	}

	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getDeptType() {
		return deptType;
	}

	public void setDeptType(String deptType) {
		this.deptType = deptType;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getDeptUserName() {
		return deptUserName;
	}

	public void setDeptUserName(String deptUserName) {
		this.deptUserName = deptUserName;
	}

	public Dept() {
		super();
	}

	public List<OfficeUser> getOffices() {
		return offices;
	}

	public void setOffices(List<OfficeUser> offices) {
		this.offices = offices;
	}

	public String getDeptOfficeId() {
		return deptOfficeId;
	}

	public void setDeptOfficeId(String deptOfficeId) {
		this.deptOfficeId = deptOfficeId;
	}	
	
	
	
}
