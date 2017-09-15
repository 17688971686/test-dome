package cs.domain.project;

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "cs_as_plansign")
@DynamicUpdate(true)
public class AssistPlanSign {
    @Id
    @GeneratedValue(generator= "plansignGenerator")
    @GenericGenerator(name= "plansignGenerator",strategy = "uuid")
    private String id;

    @Column(columnDefinition="VARCHAR(64)")
    private String signId;                      //项目ID

    @Column(columnDefinition="VARCHAR(128)")
    private String projectName;                 //项目名称

    @Column(columnDefinition="VARCHAR(32)")
    private String assistType;                  //协审类型

    @Column(columnDefinition="VARCHAR(64)")
    private String mainSignId;                  //主项目ID

    @Column(columnDefinition="NUMBER")
    private BigDecimal assistCost;              //协审费用
    
    @Column(columnDefinition="NUMBER")
    private BigDecimal estimateCost;			//报审概算
    
    @Column(columnDefinition="NUMBER")
    private BigDecimal jiananCost;				//建安费

    @Column(columnDefinition="NUMBER")
    private Float assistDays;                   //协审天数

    @Column(columnDefinition="VARCHAR(2)")
    private String isMain;                      //是否主项目

    @Column(columnDefinition="INTEGER")
    private Integer splitNum;                   //拆分项目个数（只有独立项目才有才分，默认是1）

    @ManyToOne
    @JoinColumn(name = "assistUnitId")
//    @JSONField(serialzeFeatures = SerializerFeature.DisableCircularReferenceDetect)
    private AssistUnit assistUnit;              //协审单位

    @ManyToOne
    @JoinColumn(name = "planId")
//    @JSONField(serialzeFeatures = SerializerFeature.DisableCircularReferenceDetect)
    private AssistPlan assistPlan;              //协审计划

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getMainSignId() {
        return mainSignId;
    }

    public void setMainSignId(String mainSignId) {
        this.mainSignId = mainSignId;
    }

    public String getIsMain() {
        return isMain;
    }

    public void setIsMain(String isMain) {
        this.isMain = isMain;
    }

    public AssistUnit getAssistUnit() {
        return assistUnit;
    }

    public void setAssistUnit(AssistUnit assistUnit) {
        this.assistUnit = assistUnit;
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

    public AssistPlan getAssistPlan() {
        return assistPlan;
    }

    public void setAssistPlan(AssistPlan assistPlan) {
        this.assistPlan = assistPlan;
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
    
    
}
