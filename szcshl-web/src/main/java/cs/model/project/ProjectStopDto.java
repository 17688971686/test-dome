package cs.model.project;


import com.alibaba.fastjson.annotation.JSONField;
import cs.domain.DomainBase;

import java.util.Date;

/**
 * 设置为动态更新，只更新有修改的字段
 *
 * @author ldm
 */
public class ProjectStopDto extends DomainBase {

    private String stopid;

    /**
     * 是否有效（9：是，0：否）
     */
    private String isactive;

    //实际暂停工作日
    private Float pausedays;

    //预计暂停工作日
    private Float expectpausedays;

    //暂停时间
    @JSONField(format = "yyyy-MM-dd")
    private Date pausetime;

    //实际启动时间
    @JSONField(format = "yyyy-MM-dd")
    private Date startTime;

    //预计启动时间
    @JSONField(format = "yyyy-MM-dd")
    private Date expectstartTime;

    //暂停说明
    private String pasedescription;

    public String getStopid() {
        return stopid;
    }

    public void setStopid(String stopid) {
        this.stopid = stopid;
    }

    public String getIsactive() {
        return isactive;
    }

    public void setIsactive(String isactive) {
        this.isactive = isactive;
    }

    public Float getPausedays() {
        return pausedays;
    }

    public void setPausedays(Float pausedays) {
        this.pausedays = pausedays;
    }

    public Float getExpectpausedays() {
        return expectpausedays;
    }

    public void setExpectpausedays(Float expectpausedays) {
        this.expectpausedays = expectpausedays;
    }

    public Date getPausetime() {
        return pausetime;
    }

    public void setPausetime(Date pausetime) {
        this.pausetime = pausetime;
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

    public String getPasedescription() {
        return pasedescription;
    }

    public void setPasedescription(String pasedescription) {
        this.pasedescription = pasedescription;
    }
}