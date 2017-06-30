package cs.domain.sys;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

import cs.domain.DomainBase;



@Entity
@Table(name = "cs_role")
public class Role extends DomainBase {
	@Id
	private String id;
	@Column(columnDefinition="varchar(255) NOT NULL")
	
	private String roleName;
	@Column(columnDefinition="varchar(255)")
	private String remark;
	
	@ElementCollection
	@CollectionTable(name="cs_resource",joinColumns=@JoinColumn(name="roleId"))
	private List<Resource> resources=new ArrayList<>();
	
	@ManyToMany(mappedBy="roles",fetch = FetchType.LAZY)
	private List<User> users =new ArrayList<>();
	
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public List<Resource> getResources() {
		return resources;
	}

	public void setResources(List<Resource> resources) {
		this.resources = resources;
	}

	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	

	
}
