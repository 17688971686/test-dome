package cs.domain.expert;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;
/**
 * 宸ヤ綔缁忓巻瀹炰綋绫�
 * @author Administrator
 *
 */

/**
 * @author Administrator
 *
 */
@Entity
@Table(name="cs_workeExpe")
public class WorkExpe implements Serializable {

	private static final long serialVersionUID = 3L;
	@Id
	private String weID; //Id
	@Column(name = "beginTime", nullable = true)
    private String beginTime;//开始时间  
    @Column(name = "endTime", nullable = true, length = 500)
    private String endTime; //结束时间
    @Column(name = "companyName", nullable = true, length = 500)
    private String companyName; //单位名称
    @Column(name = "job", nullable = true, length = 500)
    private String job; //职位
    @Column(name = "create_Time", nullable = true, length = 500)
    private String create_Time; //创建时间
    @ManyToOne
    @JoinColumn(name="expertID")
    private Expert expert;
	public String getWeID() {
		return weID;
	}
	public void setWeID(String weID) {
		this.weID = weID;
	}
    
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")  
	public String getBeginTime() {
		return beginTime;
	}
	public void setBeginTime(String beginTime) {
		this.beginTime = beginTime;
	}
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")  
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
	public Expert getExpert() {
		return expert;
	}
	public void setExpert(Expert expert) {
		this.expert = expert;
	}
	
    
	
}
