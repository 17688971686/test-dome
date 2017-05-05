package cs.domain.sys;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Resource {	
	@Column(columnDefinition="varchar(255)")
	private String name;
	@Column(columnDefinition="varchar(255)")
	private String path;	
	@Column(columnDefinition="varchar(255)")
	private String method;

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
