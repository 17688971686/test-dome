package cs.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

/**
 * 项目经验
 * @author Administrator
 *
 */

@Entity
@Table(name="cs_projectExpe")
public class ProjectExpe implements Serializable {

	private static final long serialVersionUID = 3L;
	@Id
	private String peID; //Id
	@Column(name = "projectName", nullable = false, length = 100)
    private String projectName; //项目名称
	@Column(name = " projectType", nullable = false, length = 100)
    private String projectType; //项目类型
	@Column(name = "projectbeginTime", nullable = false, length = 100)
    private String projectbeginTime; //项目开始时间
	@Column(name = "projectendTime", nullable = false, length = 100)
    private String projectendTime; //项目结束时间
	@Column(name = " createTime", nullable = false, length = 100)
    private String createTime; //创建日期
	@ManyToOne
	  @JoinColumn(name="expertID")
	private Expert expert;
	public Expert getExpert() {
		return expert;
	}
	public void setExpert(Expert expert) {
		this.expert = expert;
	}
	public String getProjectName() {
		return projectName;
	}
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	public String getPeID() {
		return peID;
	}
	public void setPeID(String peID) {
		this.peID = peID;
	}
	
	public String getProjectType() {
		return projectType;
	}
	public void setProjectType(String projectType) {
		this.projectType = projectType;
	}
	//@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")  
	public String getProjectbeginTime() {
		return projectbeginTime;
	}

	public void setProjectbeginTime(String projectbeginTime) {
		this.projectbeginTime = projectbeginTime;
	}
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")  
	public String getProjectendTime() {
		return projectendTime;
	}
	public void setProjectendTime(String projectendTime) {
		this.projectendTime = projectendTime;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	
	
}
