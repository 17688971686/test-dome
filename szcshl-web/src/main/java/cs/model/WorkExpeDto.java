package cs.model;

import java.io.Serializable;
/**
 * 宸ヤ綔缁忓巻瀹炰綋绫�
 * @author Administrator
 *
 */


/**
 * @author Administrator
 *
 */
public class WorkExpeDto implements Serializable {

	private String weID; //Id
    private String beginTime;//开始时间  
    private String endTime; //结束时间
    private String companyName; //单位名称
    private String job; //职位
    private String create_Time; //创建时间
    private String  expertID; //专家编号
    private ExpertDto expertDto;

	public ExpertDto getExpertDto() {
		return expertDto;
	}
	public void setExpertDto(ExpertDto expertDto) {
		this.expertDto = expertDto;
	}
	public String getBeginTime() {
		return beginTime;
	}
	public void setBeginTime(String beginTime) {
		this.beginTime = beginTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getJob() {
		return job;
	}
	public void setJob(String job) {
		this.job = job;
	}
	public String getCreate_Time() {
		return create_Time;
	}
	public void setCreate_Time(String create_Time) {
		this.create_Time = create_Time;
	}
	public String getWeID() {
		return weID;
	}
	public void setWeID(String weID) {
		this.weID = weID;
	}
	public String getExpertID() {
		return expertID;
	}
	public void setExpertID(String expertID) {
		this.expertID = expertID;
	}
    
    
	
}
