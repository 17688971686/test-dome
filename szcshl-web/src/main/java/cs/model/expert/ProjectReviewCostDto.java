package cs.model.expert;

import com.alibaba.fastjson.annotation.JSONField;
import cs.model.financial.FinancialManagerDto;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 项目评审费相关信息
 *
 * @author zsl
 */
public class ProjectReviewCostDto {
    //
    private String projectcode;
    //项目名称
    private String projectname;
    //建设单位
    private String builtcompanyname;
    private String reviewstage;
    //负责人
    private String principal;

    //评审费用
    private BigDecimal totalCost;
    /**
     * 评审费发放日期
     */
    @JSONField(format = "yyyy-MM-dd")
    private Date payDate;

    //申报金额
    private BigDecimal declareValue;

    //审定金额
    private BigDecimal authorizeValue;

    //签收时间
    @JSONField(format = "yyyy-MM-dd")
    private Date signdate;

    private String businessId;

    private String chargeName;
    private BigDecimal charge;
    private String remarke;
    private String beginTime;
    private String endTime;
    private String deptName;
    /**
     * 警示灯
     */
     private String isLightUp;

    /**
     * 流程状态
     */
    private Integer processState;

    public String getIsLightUp() {
        return isLightUp;
    }

    public void setIsLightUp(String isLightUp) {
        this.isLightUp = isLightUp;
    }

    public Integer getProcessState() {
        return processState;
    }

    public void setProcessState(Integer processState) {
        this.processState = processState;
    }

    List<FinancialManagerDto> financialManagerDtoList;

    public String getProjectcode() {
        return projectcode;
    }

    public void setProjectcode(String projectcode) {
        this.projectcode = projectcode;
    }

    public String getProjectname() {
        return projectname;
    }

    public void setProjectname(String projectname) {
        this.projectname = projectname;
    }

    public String getBuiltcompanyname() {
        return builtcompanyname;
    }

    public void setBuiltcompanyname(String builtcompanyname) {
        this.builtcompanyname = builtcompanyname;
    }

    public String getPrincipal() {
        return principal;
    }

    public void setPrincipal(String principal) {
        this.principal = principal;
    }

    public BigDecimal getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(BigDecimal totalCost) {
        this.totalCost = totalCost;
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

    public Date getSigndate() {
        return signdate;
    }

    public void setSigndate(Date signdate) {
        this.signdate = signdate;
    }

    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    public List<FinancialManagerDto> getFinancialManagerDtoList() {
        return financialManagerDtoList;
    }

    public void setFinancialManagerDtoList(List<FinancialManagerDto> financialManagerDtoList) {
        this.financialManagerDtoList = financialManagerDtoList;
    }

    public String getReviewstage() {
        return reviewstage;
    }

    public void setReviewstage(String reviewstage) {
        this.reviewstage = reviewstage;
    }

    public String getChargeName() {
        return chargeName;
    }

    public void setChargeName(String chargeName) {
        this.chargeName = chargeName;
    }

    public BigDecimal getCharge() {
        return charge;
    }

    public void setCharge(BigDecimal charge) {
        this.charge = charge;
    }

    public String getRemarke() {
        return remarke;
    }

    public void setRemarke(String remarke) {
        this.remarke = remarke;
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

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }
}
