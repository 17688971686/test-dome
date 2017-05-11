package cs.model.expert;

import cs.model.BaseDto;



/**
 * 项目经验
 * @author Administrator
 *
 */

public class ProjectExpeDto extends BaseDto {

	private String peID; //Id
    private String projectName; //项目名称
    private String projectType; //项目类型
    private String projectbeginTime; //项目开始时间
    private String projectendTime; //项目结束时间
    private String expertID;//专家编号
    
	public String getProjectType() {
		return projectType;
	}
	public void setProjectType(String projectType) {
		this.projectType = projectType;
	}
	public String getProjectbeginTime() {
		return projectbeginTime;
	}

	public void setProjectbeginTime(String projectbeginTime) {
		this.projectbeginTime = projectbeginTime;
	}
	public String getProjectendTime() {
		return projectendTime;
	}
	public void setProjectendTime(String projectendTime) {
		this.projectendTime = projectendTime;
	}	
	public String getPeID() {
		return peID;
	}
	public void setPeID(String peID) {
		this.peID = peID;
	}
	public String getExpertID() {
		return expertID;
	}
	public void setExpertID(String expertID) {
		this.expertID = expertID;
	}
	public String getProjectName() {
		return projectName;
	}
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
}
