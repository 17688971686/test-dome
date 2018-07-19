package cs.domain.expert;

import cs.domain.DomainBase;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by ldm on 2018/7/17 0017.
 */
@MappedSuperclass
public class ExpertReviewBase extends DomainBase {

    @Id
    @GeneratedValue(generator = "epReviewGenerator")
    @GenericGenerator(name = "epReviewGenerator", strategy = "uuid2")
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
     * 当前抽取信息，ALL表示整体抽取，其它的表示抽取条件ID
     */
    @Column(columnDefinition = "VARCHAR(64)")
    private String extractInfo;

    /**
     * 当前抽取未确认的次数，当extractInfo未抽取条件ID时有效
     */
    @Column(columnDefinition = "integer default 0")
    private Integer selectIndex;    //抽取次数

    /**
     * 是否完成整体专家抽取
     * 0表示未完成整体抽取，1表示已完成整体抽取
     */
    @Column(columnDefinition = "integer default 0")
    private Integer finishExtract;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getExpretCount() {
        return expretCount;
    }

    public void setExpretCount(Integer expretCount) {
        this.expretCount = expretCount;
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

    public BigDecimal getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(BigDecimal totalCost) {
        this.totalCost = totalCost;
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

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
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

    public String getExtractInfo() {
        return extractInfo;
    }

    public void setExtractInfo(String extractInfo) {
        this.extractInfo = extractInfo;
    }

    public Integer getSelectIndex() {
        return selectIndex;
    }

    public void setSelectIndex(Integer selectIndex) {
        this.selectIndex = selectIndex;
    }

    public Integer getFinishExtract() {
        return finishExtract;
    }

    public void setFinishExtract(Integer finishExtract) {
        this.finishExtract = finishExtract;
    }
}
