package cs.model.sys;

import java.util.ArrayList;
import java.util.List;

import cs.model.BaseDto;

public class RoleDto extends BaseDto {
	private String id;
	private String roleName;
	private String remark;
	private List<ResourceDto> resources=new ArrayList<>();
	
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
	
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public List<ResourceDto> getResources() {
		return resources;
	}
	public void setResources(List<ResourceDto> resources) {
		this.resources = resources;
	}
}
