package cs.domain.expert;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.*;

import org.hibernate.annotations.DynamicUpdate;

import cs.domain.DomainBase;
import cs.domain.project.WorkProgram;
import org.hibernate.annotations.GenericGenerator;

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

    //是否已经抽取专家
    @Column(columnDefinition = "varchar(2) ")
    private String isSelete;

    //抽取结果是否已经确认
    @Column(columnDefinition = "varchar(2) ")
    private String isComfireResult;

    //专家抽取次数
    @Column(columnDefinition = "Integer")
    private Integer selCount;

    //状态
    @Column(columnDefinition = "VARCHAR(2)")
    private String state;

    /**
     * 工作方案【与工作方案一对多关系（合并评审）】
     */
    @OneToMany(mappedBy = "expertReview")
    private List<WorkProgram> workProgramList;

    /**
     * 抽取条件（一对多）
     */
    @OneToMany(mappedBy = "expertReview")
    private List<ExpertSelCondition> expertSelConditionList;

    /**
     * 抽取的专家信息（一对多）
     */
    @OneToMany(mappedBy = "expertReview",cascade=CascadeType.REMOVE)
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

    public List<WorkProgram> getWorkProgramList() {
        return workProgramList;
    }

    public void setWorkProgramList(List<WorkProgram> workProgramList) {
        this.workProgramList = workProgramList;
    }

    public List<ExpertSelCondition> getExpertSelConditionList() {
        return expertSelConditionList;
    }

    public void setExpertSelConditionList(List<ExpertSelCondition> expertSelConditionList) {
        this.expertSelConditionList = expertSelConditionList;
    }

    public String getIsSelete() {
        return isSelete;
    }

    public void setIsSelete(String isSelete) {
        this.isSelete = isSelete;
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
}
