package cs.model.expert;

import com.alibaba.fastjson.annotation.JSONField;
import cs.model.BaseDto;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Description: 专家评审 页面数据模型
 * author: ldm
 * Date: 2017-5-17 14:02:25
 */
public class ExpertReviewDto extends BaseDto {

    /**
     * id
     */
    private String id;
    /**
     * 专家个数
     */
    private Integer expretCount;
    /**
     * 会议日期
     */
    @JSONField(format = "yyyy-MM-dd")
    private Date reviewDate;
    /**
     * 评审费发送标题
     */
    private String reviewTitle;
    /**
     * 评审费发放日期
     */
    @JSONField(format = "yyyy-MM-dd")
    private Date payDate;
    /**
     * 状态(主要用于前端控制显示，seleted:已经进行专家抽取)
     */
    private String state;
    /**
     * 费用合计
     */
    private BigDecimal totalCost;
    /**
     * 评审费用
     */
    private BigDecimal reviewCost;
    /**
     * 缴税费用
     */
    private BigDecimal reviewTaxes;
    /**
     * 业务ID
     */
    private String businessId;
    /**
     * 业务类型（SIGN 表示收文，TOPIC表示课题研究）
     */
    private String businessType;
    /**
     * 抽取条件
     */
    private List<ExpertSelConditionDto> expertSelConditionDtoList;

    /**
     * 抽取结果
     */
    private List<ExpertSelectedDto> expertSelectedDtoList;

    public ExpertReviewDto() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getReviewDate() {
        return reviewDate;
    }

    public String getReviewTitle() {
        return reviewTitle;
    }

    public void setReviewTitle(String reviewTitle) {
        this.reviewTitle = reviewTitle;
    }

    public Date getPayDate() {
        return payDate;
    }

    public void setPayDate(Date payDate) {
        this.payDate = payDate;
    }

    public Integer getExpretCount() {
        return expretCount;
    }

    public void setExpretCount(Integer expretCount) {
        this.expretCount = expretCount;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public BigDecimal getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(BigDecimal totalCost) {
        this.totalCost = totalCost;
    }

    public void setReviewDate(Date reviewDate) {
        this.reviewDate = reviewDate;
    }

    public List<ExpertSelConditionDto> getExpertSelConditionDtoList() {
        return expertSelConditionDtoList;
    }

    public void setExpertSelConditionDtoList(List<ExpertSelConditionDto> expertSelConditionDtoList) {
        this.expertSelConditionDtoList = expertSelConditionDtoList;
    }

    public List<ExpertSelectedDto> getExpertSelectedDtoList() {
        return expertSelectedDtoList;
    }

    public void setExpertSelectedDtoList(List<ExpertSelectedDto> expertSelectedDtoList) {
        this.expertSelectedDtoList = expertSelectedDtoList;
    }

    public BigDecimal getReviewCost() {
        return reviewCost;
    }

    public void setReviewCost(BigDecimal reviewCost) {
        this.reviewCost = reviewCost;
    }

    public BigDecimal getReviewTaxes() {
        return reviewTaxes;
    }

    public void setReviewTaxes(BigDecimal reviewTaxes) {
        this.reviewTaxes = reviewTaxes;
    }

    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }
}