package cs.model.project;

import java.math.BigDecimal;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.SerializerFeature;

import cs.common.utils.Validate;
import cs.domain.project.AssistPlan;
import cs.domain.project.AssistUnit;
import cs.domain.project.Sign;
import cs.xss.XssShieldUtil;

/**
 * Description: 协审项目 页面数据模型
 * author: ldm
 * Date: 2017-6-6 14:50:37
 */
public class AssistPlanSignDto {

    private String id;
    /**
     * 协审计划ID
     */
    private String planId;
    /**
     * 项目ID
     */
    private String signId;
    /**
     * 拆分项目序号（拆分项目的时候才有）
     */
    private Integer splitNum;
    /**
     * 项目名称
     */
    private String projectName;
    /**
     * 协审费用
     */
    private BigDecimal assistCost;
    /**
     * 协审天数
     */
    private Float assistDays;
    /**
     * 是否主项目（合并项目的时候才有）
     */
    private String isMain;
    /**
     * 报审概算
     */
    private BigDecimal estimateCost;
    /**
     * 建安费
     */
    private BigDecimal jiananCost;

    private String userName;
    /**
     * 多个协审单位拼接后的字段
     */
    private String unitNameStr;

    public String getUnitNameStr() {
        return unitNameStr;
    }

    public void setUnitNameStr(String unitNameStr) {
        this.unitNameStr = XssShieldUtil.stripXss(unitNameStr);
    }

    @JSONField(serialzeFeatures = SerializerFeature.DisableCircularReferenceDetect)
    private AssistUnit assistUnit;

    @JSONField(serialzeFeatures = SerializerFeature.DisableCircularReferenceDetect)
    private AssistPlan assistPlan;

    public AssistPlanSignDto() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPlanId() {
        return planId;
    }

    public void setPlanId(String planId) {
        this.planId = XssShieldUtil.stripXss(planId);
    }

    public String getSignId() {
        return signId;
    }

    public void setSignId(String signId) {
        this.signId = XssShieldUtil.stripXss(signId);
    }

    public BigDecimal getAssistCost() {
        return assistCost;
    }

    public void setAssistCost(BigDecimal assistCost) {
        this.assistCost = assistCost;
    }

    public Float getAssistDays() {
        return assistDays;
    }

    public void setAssistDays(Float assistDays) {
        this.assistDays = assistDays;
    }

    public String getIsMain() {
        return isMain;
    }

    public void setIsMain(String isMain) {
        this.isMain = isMain;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public BigDecimal getEstimateCost() {
        return estimateCost;
    }

    public void setEstimateCost(BigDecimal estimateCost) {
        this.estimateCost = estimateCost;
    }

    public BigDecimal getJiananCost() {
        return jiananCost;
    }

    public void setJiananCost(BigDecimal jiananCost) {
        this.jiananCost = jiananCost;
    }

    public AssistUnit getAssistUnit() {
        return assistUnit;
    }

    public void setAssistUnit(AssistUnit assistUnit) {
        this.assistUnit = assistUnit;
    }

    public AssistPlan getAssistPlan() {
        return assistPlan;
    }

    public void setAssistPlan(AssistPlan assistPlan) {
        this.assistPlan = assistPlan;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = XssShieldUtil.stripXss(userName);
    }

    public Integer getSplitNum() {
        return splitNum;
    }

    public void setSplitNum(Integer splitNum) {
        this.splitNum = splitNum;
    }
}