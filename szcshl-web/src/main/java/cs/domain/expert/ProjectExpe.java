package cs.domain.expert;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

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
	@Column(name = "projectbeginTime", nullable = false)
    private Date projectbeginTime; //项目开始时间
	@Column(name = "projectendTime", nullable = false)
    private Date projectendTime; //项目结束时间
	@Column(name = "createTime", nullable = false)
    private Date createTime; //创建日期
	@ManyToOne
	  @JoinColumn(name="expertID")
	private Expert expert;
	
	public Date getProjectbeginTime() {
		return projectbeginTime;
	}
	public Date getProjectendTime() {
		return projectendTime;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setProjectbeginTime(Date projectbeginTime) {
		this.projectbeginTime = projectbeginTime;
	}
	public void setProjectendTime(Date projectendTime) {
		this.projectendTime = projectendTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
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
	
}
