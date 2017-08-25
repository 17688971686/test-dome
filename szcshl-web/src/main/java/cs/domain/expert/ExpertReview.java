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
    @GenericGenerator(name = "epReviewGenerator", strategy = "uuid")
    private String id;

    //专家个数
    @Column(columnDefinition = "Integer")
    private Integer expretCount;

    //评审日期
    @Column(columnDefinition = "Date")
    private Date reviewDate;

    //评审费发送标题
    @Column(columnDefinition = "VARCHAR(128)")
    private String reviewTitle;

    //评审费发放日期
    @Column(columnDefinition = "Date")
    private Date payDate;

    //费用合计
    @Column(columnDefinition = "NUMBER")
    private BigDecimal totalCost;

    //评审费用
    @Column(columnDefinition = "NUMBER")
    private BigDecimal reviewCost;

    //税费
    @Column(columnDefinition = "NUMBER")
    private BigDecimal reviewTaxes;

    //抽取结果是否已经确认
    @Column(columnDefinition = "varchar(2) ")
    private String isComfireResult;

    //专家抽取次数
    @Column(columnDefinition = "Integer default 0")
    private Integer selCount;

    //状态
    @Column(columnDefinition = "VARCHAR(2)")
    private String state;

    /**
     * 抽取条件（一对多）
     */
    @OneToMany(mappedBy = "expertReview",fetch = FetchType.LAZY,orphanRemoval=true,cascade = CascadeType.ALL)
    private List<ExpertSelCondition> expertSelConditionList;

    /**
     * 抽取的专家信息（一对多）
     */
    @OneToMany(mappedBy = "expertReview",fetch = FetchType.LAZY,orphanRemoval=true,cascade = CascadeType.ALL)
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

    public String getIsComfireResult() {
        return isComfireResult;
    }

    public void setIsComfireResult(String isComfireResult) {
        this.isComfireResult = isComfireResult;
    }

    public Integer getSelCount() {
        return selCount;
    }

    public void setSelCount(Integer selCount) {
        this.selCount = selCount;
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

    public ExpertReview(){

    }
}
