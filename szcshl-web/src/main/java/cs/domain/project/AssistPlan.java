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
     * 计划名称(时间+序号命名  yyyymmdd01)
     */
    @Column(columnDefinition="VARCHAR(64)")
    private String planName;

    /**
     * 报审时间
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column
    private Date reportTime;

    /**
     * 报批时间
     */
    @Column(columnDefinition="VARCHAR(128)")
    private String approvalTime;
    
    /**
     * 抽签时间
     */
    @Column(columnDefinition="VARCHAR(128)")
    private String drawTime;

    /**
     * 抽签方式: 1 :表示轮空   0：表示全部抽中
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
    @Column(columnDefinition = "VARCHAR(1000)")
    private String ministerOpinion;

    /**
     * 分管副主任意见
     */
    @Column(columnDefinition = "VARCHAR(1000)")
    private String viceDirectorOpinion;

    /**
     * 主任意见
     */
    @Column(columnDefinition = "VARCHAR(1000)")
    private String directorOpinion;

    /**
     * 计划状态
     */
    @Column(columnDefinition = "VARCHAR(2)")
    private String planState;

    /**
     * 是否已抽签,9是已抽签
     *
     * */
    @Column(columnDefinition = "VARCHAR(2)")
    private String isDrawed;

    /**
     * 协审类型
     */
    @Column(columnDefinition="VARCHAR(32)")
    private String assistType;

    /**
     * 拆分项目个数（只有独立项目才有才分，默认是1）
     */
    @Column(columnDefinition="INTEGER")
    private Integer splitNum;

    /**
     * 参加抽签单位
     */
    @ManyToMany(fetch = FetchType.LAZY)
    private List<AssistUnit> assistUnitList;

    /**
     * 协审项目信息
     */
    @OneToMany(mappedBy = "assistPlan",fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.ALL)
    private List<AssistPlanSign> assistPlanSignList;

    public String getPlanState() {
        return planState;
    }

    public void setPlanState(String planState) {
        this.planState = planState;
    }

    public String getIsDrawed() {
        return isDrawed;
    }

    public void setIsDrawed(String isDrawed) {
        this.isDrawed = isDrawed;
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

    public String getAssistType() {
        return assistType;
    }

    public void setAssistType(String assistType) {
        this.assistType = assistType;
    }

    public Integer getSplitNum() {
        return splitNum;
    }

    public void setSplitNum(Integer splitNum) {
        this.splitNum = splitNum;
    }
}
