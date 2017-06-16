package cs.model.expert;

import com.alibaba.fastjson.annotation.JSONField;
import cs.domain.expert.ExpertSelCondition;
import cs.domain.expert.ExpertSelected;
import cs.model.BaseDto;
import cs.model.project.WorkProgramDto;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.OneToMany;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Description: 专家评审 页面数据模型
 * author: ldm
 * Date: 2017-5-17 14:02:25
 */
public class ExpertReviewDto extends BaseDto {

    private String id;
    private Integer expretCount;
    private Double score;
    @JSONField(format = "yyyy-MM-dd")
    private Date reviewDate;
    private String reviewTitle;
    private String selectType;
    @JSONField(format = "yyyy-MM-dd")
    private Date payDate;
    private String state;
    private BigDecimal totalCost;
    //是否已经抽取专家
    private String isSelete;
    //抽取结果是否已经确认
    private String isComfireResult;
    //专家抽取次数
    private Integer selCount;

    //工作方案【与工作方案一对多关系（合并评审）】
    private List<WorkProgramDto> workProgramDtoList;
    //抽取条件【与工作方案一对多关系（合并评审）】
    private List<ExpertSelConditionDto> expertSelConditionDtoList;
    //抽取的专家信息（一对多）
    private List<ExpertSelectedDto> expertSelectedDtoList;

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

    public BigDecimal getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(BigDecimal totalCost) {
        this.totalCost = totalCost;
    }

    public void setReviewDate(Date reviewDate) {
        this.reviewDate = reviewDate;
    }

    public List<WorkProgramDto> getWorkProgramDtoList() {
        return workProgramDtoList;
    }

    public void setWorkProgramDtoList(List<WorkProgramDto> workProgramDtoList) {
        this.workProgramDtoList = workProgramDtoList;
    }

    public List<ExpertSelConditionDto> getExpertSelConditionDtoList() {
        return expertSelConditionDtoList;
    }

    public void setExpertSelConditionDtoList(List<ExpertSelConditionDto> expertSelConditionDtoList) {
        this.expertSelConditionDtoList = expertSelConditionDtoList;
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

    public List<ExpertSelectedDto> getExpertSelectedDtoList() {
        return expertSelectedDtoList;
    }

    public void setExpertSelectedDtoList(List<ExpertSelectedDto> expertSelectedDtoList) {
        this.expertSelectedDtoList = expertSelectedDtoList;
    }
}