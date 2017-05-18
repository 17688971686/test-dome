package cs.model.expert;

import java.math.BigDecimal;
import java.util.Date;

import cs.domain.expert.Expert;
import cs.domain.project.WorkProgram;
import cs.model.BaseDto;
import cs.model.project.WorkProgramDto;

/**
 * Description: 专家评审 页面数据模型
 * author: ldm
 * Date: 2017-5-17 14:02:25
 */
public class ExpertReviewDto extends BaseDto {

    private String id;
    private Integer expretCount;
    private Double score;
    private Date reviewDate;
    private BigDecimal reviewCost;
    private BigDecimal reviewTaxes;
    private String reviewTitle;
    private String selectType;
	private Date payDate;
	private String state;
	
	private String expertId;
	private String workProgramId;
	
    private ExpertDto expertDto;
    private WorkProgramDto workProgramDto;

    public ExpertReviewDto() {
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

	public ExpertDto getExpertDto() {
		return expertDto;
	}

	public void setExpertDto(ExpertDto expertDto) {
		this.expertDto = expertDto;
	}

	public WorkProgramDto getWorkProgramDto() {
		return workProgramDto;
	}

	public void setWorkProgramDto(WorkProgramDto workProgramDto) {
		this.workProgramDto = workProgramDto;
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

	public String getExpertId() {
		return expertId;
	}

	public void setExpertId(String expertId) {
		this.expertId = expertId;
	}

	public String getWorkProgramId() {
		return workProgramId;
	}

	public void setWorkProgramId(String workProgramId) {
		this.workProgramId = workProgramId;
	}  	
	
}