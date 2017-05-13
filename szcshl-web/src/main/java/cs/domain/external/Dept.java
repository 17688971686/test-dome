package cs.domain.external;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicUpdate;

import cs.domain.DomainBase;

@Entity
@Table(name = "DEPT")
@DynamicUpdate(true)
public class Dept extends DomainBase{

	@Id
	private String deptId;
	
	@Column(columnDefinition="VARCHAR(128)")
	private String deptName;
	
	@Column(columnDefinition="VARCHAR(256)")
	private String address;
	
	@Column(columnDefinition="VARCHAR(16)")
	private String deptType;
	
	@Column(columnDefinition="VARCHAR(2)")
	private String status;

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

	public Dept() {
		super();
	}	
	
}
