package cs.domain.project;


import cs.domain.DomainBase;
import cs.domain.expert.ExpertSelected;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Formula;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 设置为动态更新，只更新有修改的字段
 *
 * @author ldm
 */
@Entity
@Table(name = "cs_projectStop")
@DynamicUpdate(true)
public class ProjectStop extends DomainBase {

    /**
     *暂停ID
     */
    @Id
    private String stopid;
    

    //是否暂停(2:暂停，1：运行)
    @Column(columnDefinition = "VARCHAR(1)")
    private String ispause;

    //实际暂停工作日
    @Column(columnDefinition = "NUMBER")
    private Float pausedays;
    
    //预计暂停工作日
    @Column(columnDefinition = "NUMBER")
    private Float expectpausedays;
  
    //暂停时间
    @Column(columnDefinition = "DATE")
    private Date pausetime;
    
    //实际启动时间
    @Column(columnDefinition = "DATE")
    private Date startTime;
    
    //预计启动时间
    @Column(columnDefinition = "DATE")
    private Date expectstartTime;
    
    //暂停说明
    @Column(columnDefinition = "VARCHAR(2048)")
    private String pasedescription;
    
    @ManyToOne
    @JoinColumn(name="signid")
    private Sign sign;

    
	public Float getExpectpausedays() {
		return expectpausedays;
	}

	public void setExpectpausedays(Float expectpausedays) {
		this.expectpausedays = expectpausedays;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getExpectstartTime() {
		return expectstartTime;
	}

	public void setExpectstartTime(Date expectstartTime) {
		this.expectstartTime = expectstartTime;
	}

	public Sign getSign() {
		return sign;
	}

	public void setSign(Sign sign) {
		this.sign = sign;
	}

	public String getStopid() {
		return stopid;
	}

	public void setStopid(String stopid) {
		this.stopid = stopid;
	}

	public String getIspause() {
		return ispause;
	}

	public void setIspause(String ispause) {
		this.ispause = ispause;
	}

	public Float getPausedays() {
		return pausedays;
	}

	public void setPausedays(Float pausedays) {
		this.pausedays = pausedays;
	}

	public Date getPausetime() {
		return pausetime;
	}

	public void setPausetime(Date pausetime) {
		this.pausetime = pausetime;
	}

	public String getPasedescription() {
		return pasedescription;
	}

	public void setPasedescription(String pasedescription) {
		this.pasedescription = pasedescription;
	}
    
}