package cs.model.expert;

import com.alibaba.fastjson.annotation.JSONField;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 专家费统计相关信息
 *
 * @author zsl
 */
public class ExpertCostDetailCountDto {
    //专家id
    private String expertId;
    //专家编号
    private String expertNo;
    //专家姓名
    private String name;
    //月合计评审费
    private BigDecimal reviewcost;
    //月合计交税金额
    private BigDecimal reviewtaxes;
    /**
     * 评审费发送标题
     */
    private String reviewTitle;
    //评审方式
    private String reviewType;
    //评审时间
    @JSONField(format = "yyyy-MM-dd")
    private Date reviewDate;

    private String beginTime;

    private String endTime;

    private String year;
    private String month;

    public String getExpertId() {
        return expertId;
    }

    public void setExpertId(String expertId) {
        this.expertId = expertId;
    }

    public String getExpertNo() {
        return expertNo;
    }

    public void setExpertNo(String expertNo) {
        this.expertNo = expertNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getReviewcost() {
        return reviewcost;
    }

    public void setReviewcost(BigDecimal reviewcost) {
        this.reviewcost = reviewcost;
    }

    public BigDecimal getReviewtaxes() {
        return reviewtaxes;
    }

    public void setReviewtaxes(BigDecimal reviewtaxes) {
        this.reviewtaxes = reviewtaxes;
    }

    public String getReviewTitle() {
        return reviewTitle;
    }

    public void setReviewTitle(String reviewTitle) {
        this.reviewTitle = reviewTitle;
    }

    public String getReviewType() {
        return reviewType;
    }

    public void setReviewType(String reviewType) {
        this.reviewType = reviewType;
    }

    public Date getReviewDate() {
        return reviewDate;
    }

    public void setReviewDate(Date reviewDate) {
        this.reviewDate = reviewDate;
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

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }
}
