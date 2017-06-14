package cs.domain.expert;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.*;

import org.hibernate.annotations.DynamicUpdate;

import cs.domain.DomainBase;
import cs.domain.project.WorkProgram;

/**
 * 专家评审表
 * @author Administrator
 *
 */
@Entity
@Table(name="cs_expert_review")
@DynamicUpdate(true)
public class ExpertReview extends DomainBase{

	@Id
	private String id;
	
	//专家个数
	@Column(columnDefinition="Integer")
	private Integer expretCount;
	
	//评分
	@Column(columnDefinition="NUMBER")
	private Double score;
	
	//评审日期
	@Column(columnDefinition="Date")
	private Date reviewDate;
	
	//评审费用
	@Column(columnDefinition="NUMBER")
	private BigDecimal reviewCost;
	
	//评审费发送标题
	@Column(columnDefinition="VARCHAR(128)")
	private String reviewTitle;
	
	//评审费发放日期
	@Column(columnDefinition="Date")
	private Date payDate;
	
	//缴税
	@Column(columnDefinition="NUMBER")
	private BigDecimal reviewTaxes;
	
	//费用合计
	@Column(columnDefinition="NUMBER")
	private BigDecimal totalCost;

	//状态
	@Column(columnDefinition="VARCHAR(2)")
	private String state;
	
	//选择类型
	@Column(columnDefinition="VARCHAR(2)")
	private String selectType;
	

	//是否参加【默认参加(9)】
	@Column(columnDefinition="VARCHAR(2)")
	private String isJoin;
	
	
	//评级描述
	@Column(columnDefinition="VARCHAR(200)")
	private String describes;
	
	//综合评分
	@Column(columnDefinition="VARCHAR(10)")
	private String totalScore;
	
	@ManyToMany
	private List<Expert> expertList;
		
	@ManyToOne
	@JoinColumn(name="workProgramId")
	private WorkProgram workProgram;

	//专家抽取条件
    @ManyToOne
    @JoinColumn(name="expertSelConditionId")
	private ExpertSelCondition epSelCondition;

    
    public BigDecimal getTotalCost() {
		return totalCost;
	}

	public void setTotalCost(BigDecimal totalCost) {
		this.totalCost = totalCost;
	}

	public String getDescribes() {
		return describes;
	}

	public void setDescribes(String describes) {
		this.describes = describes;
	}

	public String getTotalScore() {
		return totalScore;
	}

	public void setTotalScore(String totalScore) {
		this.totalScore = totalScore;
	}

	public String getIsJoin() {
		return isJoin;
	}

	public void setIsJoin(String isJoin) {
		this.isJoin = isJoin;
	}


	public ExpertSelCondition getEpSelCondition() {
        return epSelCondition;
    }

    public void setEpSelCondition(ExpertSelCondition epSelCondition) {
        this.epSelCondition = epSelCondition;
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

	public Date getReviewDate() {
		return reviewDate;
	}

	public void setReviewDate(Date reviewDate) {
		this.reviewDate = reviewDate;
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
	
	public WorkProgram getWorkProgram() {
		return workProgram;
	}

	public void setWorkProgram(WorkProgram workProgram) {
		this.workProgram = workProgram;
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

	public String getSelectType() {
		return selectType;
	}

	public void setSelectType(String selectType) {
		this.selectType = selectType;
	}

	public List<Expert> getExpertList() {
		return expertList;
	}

	public void setExpertList(List<Expert> expertList) {
		this.expertList = expertList;
	}
}
