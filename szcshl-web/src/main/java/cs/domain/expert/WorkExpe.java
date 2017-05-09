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
 *工作经历
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
    private Date beginTime;//开始时间  
    @Column(name = "endTime", nullable = true)
    private Date endTime; //结束时间
    @Column(name = "companyName", nullable = true, length = 500)
    private String companyName; //单位名称
    @Column(name = "job", nullable = true, length = 500)
    private String job; //职位
    @Column(name = "create_Time", nullable = true)
    private Date create_Time; //创建时间
    @ManyToOne
    @JoinColumn(name="expertID")
    private Expert expert;
    
	public Date getBeginTime() {
		return beginTime;
	}
	public Date getEndTime() {
		return endTime;
	}
	public Date getCreate_Time() {
		return create_Time;
	}
	public void setBeginTime(Date beginTime) {
		this.beginTime = beginTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	public void setCreate_Time(Date create_Time) {
		this.create_Time = create_Time;
	}
	public String getWeID() {
		return weID;
	}
	public void setWeID(String weID) {
		this.weID = weID;
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
	public Expert getExpert() {
		return expert;
	}
	public void setExpert(Expert expert) {
		this.expert = expert;
	}
	
    
	
}
