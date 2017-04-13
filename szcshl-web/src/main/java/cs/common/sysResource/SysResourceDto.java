package cs.common.sysResource;

import java.util.ArrayList;
import java.util.List;

public class SysResourceDto {
	private String id;
	private boolean checked;
	private List<SysResourceDto> children=new ArrayList<>();
	private String name;	
	private String path;
	private String method;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public boolean isChecked() {
		return checked;
	}
	public void setChecked(boolean checked) {
		this.checked = checked;
	}
	public List<SysResourceDto> getChildren() {
		return children;
	}
	public void setChildren(List<SysResourceDto> children) {
		this.children = children;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}
	
}
