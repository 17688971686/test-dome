package cs.model.project;

import java.math.BigDecimal;

/**
 * Description: 协审项目 页面数据模型
 * author: ldm
 * Date: 2017-6-6 14:50:37
 */
public class AssistPlanSignDto {

    private String id;
    private String planId;                      //计划ID
    private String signId;                      //项目ID
    private String projectName;                 //项目名称
    private String assistType;                  //协审类型
    private String mainSignId;                  //主项目ID
    private BigDecimal assistCost;              //协审费用
    private Float assistDays;                   //协审天数
    private String isMain;                      //是否主项目
    private Integer splitNum;                   //拆分项目序号

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
        this.planId = planId;
    }

    public String getSignId() {
        return signId;
    }

    public void setSignId(String signId) {
        this.signId = signId;
    }

    public String getAssistType() {
        return assistType;
    }

    public void setAssistType(String assistType) {
        this.assistType = assistType;
    }

    public String getMainSignId() {
        return mainSignId;
    }

    public void setMainSignId(String mainSignId) {
        this.mainSignId = mainSignId;
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

    public Integer getSplitNum() {
        return splitNum;
    }

    public void setSplitNum(Integer splitNum) {
        this.splitNum = splitNum;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }
}