package cs.model.expert;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.Date;

/**
 * 专家评审基本情况业务统计
 *
 * @author zsl
 */
public class ExpertReviewCondBusDto {
    /**
     * 会议日期
     */
    @JSONField(format = "yyyy-MM-dd")
    private Date reviewDate;

    private  String projectName;

    private  String reviewStage;

    private  String signId;

    private String beginTime;

    private  String endTime;

    private String isLetterRw;


    public Date getReviewDate() {
        return reviewDate;
    }

    public void setReviewDate(Date reviewDate) {
        this.reviewDate = reviewDate;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getReviewStage() {
        return reviewStage;
    }

    public void setReviewStage(String reviewStage) {
        this.reviewStage = reviewStage;
    }

    public String getSignId() {
        return signId;
    }

    public void setSignId(String signId) {
        this.signId = signId;
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

    public String getIsLetterRw() {
        return isLetterRw;
    }

    public void setIsLetterRw(String isLetterRw) {
        this.isLetterRw = isLetterRw;
    }
}
