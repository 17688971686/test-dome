package cs.model.project;

import cs.domain.project.AssistUnit;
import cs.model.BaseDto;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


/**
 * Description: 协审方案 页面数据模型
 * author: ldm
 * Date: 2017-6-6 14:48:31
 */
public class AssistPlanDto extends BaseDto {

    private String id;
    private String planName;
    private Date reportTime;
    private String drawTime;
    private String drawType;
    private List assistUnitList;
    private String ministerOpinion;
    private String viceDirectorOpinion;
    private String directorOpinion;
    private String planState;
    private BigDecimal totalCost;
    private AssistUnit assistUnit;

    //以下参数为传递参数用
    private String signId;          //收文ID
    private boolean isSignle;       //是否单个项目
    private Integer splitNum;       //拆分个数
    private String assistType;      //协审类型
    private String projectName;     //项目名称

    public AssistPlanDto() {
    }
   
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    public String getPlanName() {
        return planName;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }
    public Date getReportTime() {
        return reportTime;
    }

    public void setReportTime(Date reportTime) {
        this.reportTime = reportTime;
    }
    public String getDrawTime() {
        return drawTime;
    }

    public void setDrawTime(String drawTime) {
        this.drawTime = drawTime;
    }
    public String getDrawType() {
        return drawType;
    }

    public void setDrawType(String drawType) {
        this.drawType = drawType;
    }
    public List getAssistUnitList() {
        return assistUnitList;
    }

    public void setAssistUnitList(List assistUnitList) {
        this.assistUnitList = assistUnitList;
    }

    public String getMinisterOpinion() {
        return ministerOpinion;
    }

    public void setMinisterOpinion(String ministerOpinion) {
        this.ministerOpinion = ministerOpinion;
    }

    public String getViceDirectorOpinion() {
        return viceDirectorOpinion;
    }

    public void setViceDirectorOpinion(String viceDirectorOpinion) {
        this.viceDirectorOpinion = viceDirectorOpinion;
    }

    public String getDirectorOpinion() {
        return directorOpinion;
    }

    public void setDirectorOpinion(String directorOpinion) {
        this.directorOpinion = directorOpinion;
    }

    public String getPlanState() {
        return planState;
    }

    public void setPlanState(String planState) {
        this.planState = planState;
    }

    public BigDecimal getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(BigDecimal totalCost) {
        this.totalCost = totalCost;
    }

    public AssistUnit getAssistUnit() {
        return assistUnit;
    }

    public void setAssistUnit(AssistUnit assistUnit) {
        this.assistUnit = assistUnit;
    }

    public String getSignId() {
        return signId;
    }

    public void setSignId(String signId) {
        this.signId = signId;
    }

    public boolean isSignle() {
        return isSignle;
    }

    public void setSignle(boolean signle) {
        isSignle = signle;
    }

    public Integer getSplitNum() {
        return splitNum;
    }

    public void setSplitNum(Integer splitNum) {
        this.splitNum = splitNum;
    }

    public String getAssistType() {
        return assistType;
    }

    public void setAssistType(String assistType) {
        this.assistType = assistType;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }
}