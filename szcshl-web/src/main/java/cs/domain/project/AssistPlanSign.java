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

    /**
     * 项目ID
     */
    @Column(columnDefinition="VARCHAR(64)")
    private String signId;

    /**
     * 协审费用
     */
    @Column(columnDefinition="NUMBER")
    private BigDecimal assistCost;

    /**
     * 报审概算
     */
    @Column(columnDefinition="NUMBER")
    private BigDecimal estimateCost;

    /**
     * 建安费
     */
    @Column(columnDefinition="NUMBER")
    private BigDecimal jiananCost;

    /**
     * 协审天数
     */
    @Column(columnDefinition="NUMBER")
    private Float assistDays;

    /**
     * 是否主项目（9代表是，0代表否）
     */
    @Column(columnDefinition="VARCHAR(2)")
    private String isMain;
    /**
     * 拆分项目序号
     */
    @Column(columnDefinition="INTEGER")
    private Integer splitNum;

    @ManyToOne
    @JoinColumn(name = "assistUnitId")
    private AssistUnit assistUnit;              //协审单位

    @ManyToOne
    @JoinColumn(name = "planId")
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

    public Integer getSplitNum() {
        return splitNum;
    }

    public void setSplitNum(Integer splitNum) {
        this.splitNum = splitNum;
    }
}
