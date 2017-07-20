package cs.model.project;


import cs.domain.DomainBase;


import javax.persistence.*;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.Date;

/**
 * 设置为动态更新，只更新有修改的字段
 *
 * @author ldm
 */
public class ProjectStopDto extends DomainBase {

    /**
     *暂停ID
     */
    @Id
    private String stopid;
    

    //是否暂停
    private String ispause;

    //实际暂停工作日
    private Float pausedays;
    
    //预计暂停工作日
    @Column(columnDefinition = "NUMBER")
    private Float expectpausedays;

    //暂停时间
    @JSONField(format = "yyyy-MM-dd")
    private Date pausetime;
    
    //实际启动时间
    @JSONField(format = "yyyy-MM-dd")
    private Date startTime;
    
    //预计启动时间
    @Column(columnDefinition = "DATE")
    private Date expectstartTime;
    //暂停说明
    private String pasedescription;
    
    private SignDto signDto;
    

	public Float getExpectpausedays() {
		return expectpausedays;
	}

	public void setExpectpausedays(Float expectpausedays) {
		this.expectpausedays = expectpausedays;
	}

	public Date getExpectstartTime() {
		return expectstartTime;
	}

	public void setExpectstartTime(Date expectstartTime) {
		this.expectstartTime = expectstartTime;
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

	public SignDto getSignDto() {
		return signDto;
	}

	public void setSignDto(SignDto signDto) {
		this.signDto = signDto;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

    
}