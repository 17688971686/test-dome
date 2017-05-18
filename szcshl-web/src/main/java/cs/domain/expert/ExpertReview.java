package cs.domain.expert;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

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

	//状态
	@Column(columnDefinition="VARCHAR(2)")
	private String state;
	
	//选择类型
	@Column(columnDefinition="VARCHAR(2)")
	private String selectType;
	
	@OneToOne
	@JoinColumn(name="expertId")
	private Expert expert;
		
	@ManyToOne
	@JoinColumn(name="workProgramId")
	private WorkProgram workProgram;

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

	public Expert getExpert() {
		return expert;
	}

	public void setExpert(Expert expert) {
		this.expert = expert;
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
	
	
}
