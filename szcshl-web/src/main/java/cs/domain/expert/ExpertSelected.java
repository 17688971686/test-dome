package cs.domain.expert;

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * 抽取的专家信息
 */
@Entity
@Table(name = "cs_expert_selected")
@DynamicUpdate(true)
public class ExpertSelected {

    @Id
    @GeneratedValue(generator = "epSelectedGenerator")
    @GenericGenerator(name = "epSelectedGenerator", strategy = "uuid2")
    private String id;

    //评分
    @Column(columnDefinition = "NUMBER")
    private Double score;

    /**
     * 综合评分(开始分值)
     */
    @Column(columnDefinition = "NUMBER")
    private Integer compositeScore;

    /**
     * 综合评分(结束分值)
     */
    @Column(columnDefinition = "NUMBER")
    private Integer compositeScoreEnd;

    //评审费用
    @Column(columnDefinition = "NUMBER default 1000")
    private BigDecimal reviewCost;

    //缴税
    @Column(columnDefinition = "NUMBER")
    private BigDecimal reviewTaxes;

    //总费用
    @Column(columnDefinition = "NUMBER")
    private BigDecimal totalCost;

    //评级描述
    @Column(columnDefinition = "VARCHAR(256)")
    private String describes;

    //是否参加
    @Column(columnDefinition = "VARCHAR(2)")
    private String isJoin;

    //抽取类型（随机、自选、境外专家3种）
    @Column(columnDefinition = "VARCHAR(2)")
    private String selectType;

    //突出专业(大类)
    @Column(columnDefinition = "varchar(128) ")
    private String maJorBig;

    //突出专业(小类)
    @Column(columnDefinition = "varchar(128) ")
    private String maJorSmall;

    //专家类别
    @Column(columnDefinition = "varchar(30) ")
    private String expeRttype;

    //抽取结果确认（已经确认的才能成为这次抽取的专家）
    @Column(columnDefinition = "VARCHAR(2)")
    private String isConfrim;

    //抽取次数
    @Column(columnDefinition = "INTEGER default 1")
    private Integer selectIndex;

    /**
     * 业务ID，如项目签收工作方案ID，课题工作方案ID等
     */
    @Column(columnDefinition = "VARCHAR(64)")
    private String businessId;

    /**
     * 是否函评
     */
    @Column(columnDefinition = "VARCHAR(2)")
    private String isLetterRw;

    //抽取评审方案（多对一）
    @ManyToOne
    @JoinColumn(name = "expertReviewId")
    private ExpertReview expertReview;

    //抽取专家关系（多对一）
    @ManyToOne
    @JoinColumn(name = "expertId")
    private Expert expert;

    /**
     * 抽取条件ID，这里不做关联，只是保存
     */
    @Column(columnDefinition = "VARCHAR(64)")
    private String conditionId;

    /**
     * 专家评审费是否拆分打印
     */
    @Column(columnDefinition = "VARCHAR(2)")
    private String isSplit;

    /**
     * 拆分的第一张表的费用
     */
    @Column(columnDefinition = "NUMBER" )
    private BigDecimal oneCost;

    /**
     * 排序号
     */
    @Column(columnDefinition="INTEGER")
    private Integer expertSeq;

    /**
     * 创建人
     */
    @Column(columnDefinition="VARCHAR(64)")
    private String createBy;

    public String getIsSplit() {
        return isSplit;
    }

    public void setIsSplit(String isSplit) {
        this.isSplit = isSplit;
    }

    public BigDecimal getOneCost() {
        return oneCost;
    }

    public void setOneCost(BigDecimal oneCost) {
        this.oneCost = oneCost;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
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

    public String getDescribes() {
        return describes;
    }

    public void setDescribes(String describes) {
        this.describes = describes;
    }

    public String getIsJoin() {
        return isJoin;
    }

    public void setIsJoin(String isJoin) {
        this.isJoin = isJoin;
    }

    public Expert getExpert() {
        return expert;
    }

    public void setExpert(Expert expert) {
        this.expert = expert;
    }

    public String getSelectType() {
        return selectType;
    }

    public void setSelectType(String selectType) {
        this.selectType = selectType;
    }

    public ExpertReview getExpertReview() {
        return expertReview;
    }

    public void setExpertReview(ExpertReview expertReview) {
        this.expertReview = expertReview;
    }

    public BigDecimal getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(BigDecimal totalCost) {
        this.totalCost = totalCost;
    }

    public String getIsConfrim() {
        return isConfrim;
    }

    public void setIsConfrim(String isConfrim) {
        this.isConfrim = isConfrim;
    }

    public Integer getSelectIndex() {
        return selectIndex;
    }

    public void setSelectIndex(Integer selectIndex) {
        this.selectIndex = selectIndex;
    }

    public String getMaJorBig() {
        return maJorBig;
    }

    public void setMaJorBig(String maJorBig) {
        this.maJorBig = maJorBig;
    }

    public String getMaJorSmall() {
        return maJorSmall;
    }

    public void setMaJorSmall(String maJorSmall) {
        this.maJorSmall = maJorSmall;
    }

    public String getExpeRttype() {
        return expeRttype;
    }

    public void setExpeRttype(String expeRttype) {
        this.expeRttype = expeRttype;
    }

    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    public String getIsLetterRw() {
        return isLetterRw;
    }

    public void setIsLetterRw(String isLetterRw) {
        this.isLetterRw = isLetterRw;
    }

    public Integer getCompositeScore() {
        return compositeScore;
    }

    public void setCompositeScore(Integer compositeScore) {
        this.compositeScore = compositeScore;
    }

    public Integer getCompositeScoreEnd() {
        return compositeScoreEnd;
    }

    public void setCompositeScoreEnd(Integer compositeScoreEnd) {
        this.compositeScoreEnd = compositeScoreEnd;
    }

    public String getConditionId() {
        return conditionId;
    }

    public void setConditionId(String conditionId) {
        this.conditionId = conditionId;
    }

    public Integer getExpertSeq() {
        return expertSeq;
    }

    public void setExpertSeq(Integer expertSeq) {
        this.expertSeq = expertSeq;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }
}
