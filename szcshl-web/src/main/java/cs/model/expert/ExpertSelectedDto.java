package cs.model.expert;

import cs.model.project.SignDto;

import java.math.BigDecimal;

public class ExpertSelectedDto {

    private String id;
    private Double score;
    private BigDecimal reviewCost;
    private BigDecimal reviewTaxes;
    private String selectType;
    private String describes;
    private String isJoin;
    private ExpertSelConditionDto expertSelConditionDto;
    private ExpertDto expertDto;

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

    public String getSelectType() {
        return selectType;
    }

    public void setSelectType(String selectType) {
        this.selectType = selectType;
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

    public ExpertSelConditionDto getExpertSelConditionDto() {
        return expertSelConditionDto;
    }

    public void setExpertSelConditionDto(ExpertSelConditionDto expertSelConditionDto) {
        this.expertSelConditionDto = expertSelConditionDto;
    }

    public ExpertDto getExpertDto() {
        return expertDto;
    }

    public void setExpertDto(ExpertDto expertDto) {
        this.expertDto = expertDto;
    }
}
