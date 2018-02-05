package cs.domain.expert;

import cs.domain.DomainBase;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 专家评审方案
 *
 * @author Administrator
 */
@Entity
@Table(name = "cs_expert_review")
@DynamicUpdate(true)
public class ExpertReview extends DomainBase {

    @Id
    @GeneratedValue(generator = "epReviewGenerator")
    @GenericGenerator(name = "epReviewGenerator", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;

    /**
     * 专家个数
     */
    @Column(columnDefinition = "Integer")
    private Integer expretCount;

    /**
     * 评审日期
     */
    @Column(columnDefinition = "Date")
    private Date reviewDate;

    /**
     * 评审费发送标题
     */
    @Column(columnDefinition = "VARCHAR(256)")
    private String reviewTitle;

    /**
     * 评审费发放日期
     */
    @Column(columnDefinition = "Date")
    private Date payDate;

    /**
     * 费用合计
     */
    @Column(columnDefinition = "NUMBER")
    private BigDecimal totalCost;

    /**
     * 评审费用
     */
    @Column(columnDefinition = "NUMBER")
    private BigDecimal reviewCost;

    /**
     * 税费
     */
    @Column(columnDefinition = "NUMBER")
    private BigDecimal reviewTaxes;

    /**
     * 状态(9表示已完成评审费发放)
     */
    @Column(columnDefinition = "VARCHAR(2)")
    private String state;

    /**
     * 业务ID（项目ID，或者课题研究ID，唯一）
     */
    @Column(columnDefinition = "VARCHAR(64)",unique = true)
    private String businessId;

    /**
     * 业务类型（SIGN 表示收文，TOPIC表示课题研究）
     */
    @Column(columnDefinition = "VARCHAR(16)")
    private String businessType;

    /**
     * 抽取条件（一对多）
     */
    @OneToMany(mappedBy = "expertReview", fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.ALL)
    private List<ExpertSelCondition> expertSelConditionList;

    /**
     * 抽取的专家信息（一对多）
     */
    @OneToMany(mappedBy = "expertReview", fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.ALL)
    private List<ExpertSelected> expertSelectedList;


    public BigDecimal getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(BigDecimal totalCost) {
        this.totalCost = totalCost;
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

    public void setReviewDate(Date reviewDate) {
        this.reviewDate = reviewDate;
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

    public List<ExpertSelCondition> getExpertSelConditionList() {
        return expertSelConditionList;
    }

    public void setExpertSelConditionList(List<ExpertSelCondition> expertSelConditionList) {
        this.expertSelConditionList = expertSelConditionList;
    }

    public List<ExpertSelected> getExpertSelectedList() {
        return expertSelectedList;
    }

    public void setExpertSelectedList(List<ExpertSelected> expertSelectedList) {
        this.expertSelectedList = expertSelectedList;
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

    public ExpertReview() {

    }
}
