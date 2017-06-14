package cs.domain.project;


import cs.domain.DomainBase;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "cs_as_plan")
@DynamicUpdate(true)
public class AssistPlan extends DomainBase {
    /**
     * 主键
     */
    @Id
    private String id;

    /**
     * 计划名称
     */
    @Column(columnDefinition="VARCHAR(64)")
    private String planName;

    /**
     * 报审时间
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column
    private Date reportTime;
    
    //报批时间
    @Column(columnDefinition="VARCHAR(20)")
    private String approvalTime;

    /**
     * 抽签时间
     */
    @Column(columnDefinition="VARCHAR(20)")
    private String drawTime;

    /**
     * 抽签方式
     */
    @Column(columnDefinition="VARCHAR(2)")
    private String drawType;

    /**
     * 协审总费用
     */
    @Column(columnDefinition="NUMBER")
    private BigDecimal totalCost;

    /**
     * 部长审核意见
     */
    private String ministerOpinion;

    /**
     * 分管副主任意见
     */
    private String viceDirectorOpinion;

    /**
     * 主任意见
     */
    private String directorOpinion;

    /**
     * 计划状态
     */
    private String planState;

    /**
     * 参加抽签单位
     */
    @ManyToMany
    private List<AssistUnit> assistUnitList;

    /**
     * 协审项目信息
     */
    @OneToMany(mappedBy = "assistPlan")
    private List<AssistPlanSign> assistPlanSignList;

    public String getPlanState() {
        return planState;
    }

    public void setPlanState(String planState) {
        this.planState = planState;
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

    public List<AssistUnit> getAssistUnitList() {
        return assistUnitList;
    }

    public void setAssistUnitList(List<AssistUnit> assistUnitList) {
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

    public BigDecimal getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(BigDecimal totalCost) {
        this.totalCost = totalCost;
    }

    public List<AssistPlanSign> getAssistPlanSignList() {
        return assistPlanSignList;
    }

    public void setAssistPlanSignList(List<AssistPlanSign> assistPlanSignList) {
        this.assistPlanSignList = assistPlanSignList;
    }

	public String getApprovalTime() {
		return approvalTime;
	}

	public void setApprovalTime(String approvalTime) {
		this.approvalTime = approvalTime;
	}
    
    
}