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
    @GenericGenerator(name = "epSelectedGenerator", strategy = "uuid")
    private String id;

    //评分
    @Column(columnDefinition = "NUMBER")
    private Double score;

    //评审费用
    @Column(columnDefinition = "NUMBER")
    private BigDecimal reviewCost;

    //缴税
    @Column(columnDefinition = "NUMBER")
    private BigDecimal reviewTaxes;

    //总费用
    @Column(columnDefinition = "NUMBER")
    private BigDecimal totalCost;

    //评级描述
    @Column(columnDefinition = "VARCHAR(200)")
    private String describes;

    //是否参加
    @Column(columnDefinition = "VARCHAR(2)")
    private String isJoin;

    //抽取类型（随机、自选、境外专家3种）
    @Column(columnDefinition = "VARCHAR(2)")
    private String selectType;

    //抽取评审方案（多对一）
    @ManyToOne
    @JoinColumn(name = "expertReviewId")
    private ExpertReview expertReview;

    //抽取专家关系（多对一）
    @ManyToOne
    @JoinColumn(name = "expertId")
    private Expert expert;

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
}
