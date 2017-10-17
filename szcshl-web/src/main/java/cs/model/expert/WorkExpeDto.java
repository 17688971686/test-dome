package cs.model.expert;

import com.alibaba.fastjson.annotation.JSONField;
import cs.model.BaseDto;

import java.util.Date;


/**
 * @author Administrator
 */
public class WorkExpeDto extends BaseDto {

    private String weID; //Id
    @JSONField(format = "yyyy-MM-dd")
    private Date beginTime;//开始时间  
    @JSONField(format = "yyyy-MM-dd")
    private Date endTime; //结束时间
    private String companyName; //单位名称
    private String workJob; //职位
    private String expertID; //专家编号
    private Integer seqNum;    //工作简历
    private ExpertDto expertDto;

    public ExpertDto getExpertDto() {
        return expertDto;
    }

    public void setExpertDto(ExpertDto expertDto) {
        this.expertDto = expertDto;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
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

    public Date getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(Date beginTime) {
        this.beginTime = beginTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getWorkJob() {
        return workJob;
    }

    public void setWorkJob(String workJob) {
        this.workJob = workJob;
    }

    public Integer getSeqNum() {
        return seqNum;
    }

    public void setSeqNum(Integer seqNum) {
        this.seqNum = seqNum;
    }
}
