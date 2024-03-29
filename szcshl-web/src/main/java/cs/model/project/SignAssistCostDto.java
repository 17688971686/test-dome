package cs.model.project;

import com.alibaba.fastjson.annotation.JSONField;
import cs.common.utils.Validate;
import cs.model.financial.FinancialManagerDto;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 协审项目列表
 * Created by ldm on 2017/10/18.
 */
public class SignAssistCostDto {

    /**
     * 项目ID
     */
    private String signId;

    /**
     * 项目名称
     */
    private String projectName;

    /**
     * 协审单位
     */
    private String assistUnit;

    /**
     * 协审登记号
     */
    private String assistPlanNo;

    /**
     * 项目负责人
     */
    private String changeUserName;

    /**
     * 计划协审费用
     */
    private BigDecimal planCost;

    /**
     * 实际协审费用
     */
    private BigDecimal factCost;

    /**
     * 付款日期
     */
    @JSONField(format = "yyyy-MM-dd")
    private Date payDate;

    /**
     * 申报金额（万元）
     */
    private BigDecimal declareValue;

    /**
     * 审定金额（万元）
     */
    private BigDecimal authorizeValue;
    /**
     * 警示灯状态
     */
    private String isLightUp;

    /**
     * 项目流程状态
     */
    private Integer processState;
    /**
     * 费用详情
     */
    private List<FinancialManagerDto> costList;

    //一下字段仅用于查询
    //开始时间段
    private String beginTime;
    //结束时间段
    private String endTime;

    public String getSignId() {
        return signId;
    }

    public void setSignId(String signId) {
        this.signId = signId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getAssistUnit() {
        return assistUnit;
    }

    public void setAssistUnit(String assistUnit) {
        this.assistUnit = assistUnit;
    }

    public String getAssistPlanNo() {
        return assistPlanNo;
    }

    public void setAssistPlanNo(String assistPlanNo) {
        this.assistPlanNo = assistPlanNo;
    }

    public String getChangeUserName() {
        return changeUserName;
    }

    public void setChangeUserName(String changeUserName) {
        this.changeUserName = changeUserName;
    }

    public BigDecimal getPlanCost() {
        return planCost;
    }

    public void setPlanCost(BigDecimal planCost) {
        this.planCost = planCost;
    }

    public BigDecimal getFactCost() {
        return factCost;
    }

    public void setFactCost(BigDecimal factCost) {
        this.factCost = factCost;
    }

    public Date getPayDate() {
        return payDate;
    }

    public void setPayDate(Date payDate) {
        this.payDate = payDate;
    }

    public BigDecimal getDeclareValue() {
        return declareValue;
    }

    public void setDeclareValue(BigDecimal declareValue) {
        this.declareValue = declareValue;
    }

    public BigDecimal getAuthorizeValue() {
        return authorizeValue;
    }

    public void setAuthorizeValue(BigDecimal authorizeValue) {
        this.authorizeValue = authorizeValue;
    }

    public List<FinancialManagerDto> getCostList() {
        return costList;
    }

    public void setCostList(List<FinancialManagerDto> costList) {
        this.costList = costList;
    }

    public String getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(String beginTime) {
        this.beginTime = beginTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getIsLightUp() {
        return isLightUp;
    }

    public void setIsLightUp(String isLightUp) {
        if(Validate.isString(isLightUp)){
            this.isLightUp = isLightUp;
        }
    }

    public Integer getProcessState() {
        return processState;
    }

    public void setProcessState(Integer processState) {
        this.processState = processState;
    }

    public SignAssistCostDto() {
    }
}
