package cs.domain.expert;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import cs.domain.DomainBase;

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
@Table(name="cs_worke_expe")
public class WorkExpe extends DomainBase {

	@Id
	private String weID; //Id
	
	@Column(name = "beginTime", nullable = true)
    private Date beginTime;//开始时间  
	
    @Column(name = "endTime", nullable = true)
    private Date endTime; //结束时间
    
    @Column(name = "companyName", nullable = true, length = 128)
    private String companyName; //单位名称
    
    @Column(name = "job", nullable = true, length = 128)
    private String job; //职位
    
    @ManyToOne
    @JoinColumn(name="expertID")
    private Expert expert;
    
	public Date getBeginTime() {
		return beginTime;
	}
	public Date getEndTime() {
		return endTime;
	}	
	public void setBeginTime(Date beginTime) {
		this.beginTime = beginTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
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
