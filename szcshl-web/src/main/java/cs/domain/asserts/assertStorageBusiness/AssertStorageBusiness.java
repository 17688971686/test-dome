package cs.domain.asserts.assertStorageBusiness;

import com.alibaba.fastjson.annotation.JSONField;
import cs.domain.DomainBase;
import cs.domain.asserts.goodsDetail.GoodsDetail;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * Created by zsl on 2017-09-15.
 */
@Entity
@Table(name = "cs_assert_storage_business")
public class AssertStorageBusiness extends DomainBase {
    //业务id
    @Id
    private String businessId;
    //业务流程名字
    @Column(columnDefinition = "varchar(64) ")
    private String businessName;
    //申请部门
    @Column(columnDefinition = "varchar(64) ")
    private String applyDept;
    //经办人
    @Column(columnDefinition = "varchar(64) ")
    private String operator;
    //申请事由
    @Column(columnDefinition = "varchar(30) ")
    private String applyReason;
    //部长ID
    @Column(columnDefinition = "VARCHAR(64)")
    private String orgDirectorId;
    //部门主管（科长/部长）
    @Column(columnDefinition = "varchar(255)")
    private String orgDirector;
    //部长审批日期
    @Column(columnDefinition = "DATE")
    private Date orgDirectorDate;

    //综合部拟办意见
    @Column(columnDefinition = "VARCHAR(255)")
    private String comprehensivehandlesug;

    //综合部部长ID
    @Column(columnDefinition = "VARCHAR(64)")
    private String comprehensiveId;

    //综合部拟办人名称
    @Column(columnDefinition = "VARCHAR(100)")
    private String comprehensiveName;

    //综合部拟办日期
    @Column(columnDefinition = "DATE")
    private Date comprehensiveDate;

    //中心领导审批意见
    @Column(columnDefinition = "VARCHAR(255)")
    private String leaderhandlesug;

    //中心领导ID
    @Column(columnDefinition = "VARCHAR(64)")
    private String leaderId;

    //中心领导名称
    @Column(columnDefinition = "VARCHAR(32)")
    private String leaderName;

    //中心领导审批日期
    @Column(columnDefinition = "DATE")
    private Date leaderDate;
    //物品明细
    @OneToMany(mappedBy = "assertStorageBusiness", fetch = FetchType.LAZY,orphanRemoval=true,cascade = CascadeType.ALL)
    @JSONField(serialize = false)
    private List<GoodsDetail> goodsDetailList;
    /**
     * 是否完成归档（9:是，0：否，默认为0）
     */
    @Column(columnDefinition="VARCHAR(2)")
    private String isFinishFiling;

    /**
     * 流程状态
     */
    @Column(columnDefinition="VARCHAR(2)")
    private String state;

    /**
     * 备注信息
     */
    @Column(columnDefinition="VARCHAR(512)")
    private String remark;

    /**
     * 流程实例ID
     */
    @Column(columnDefinition="VARCHAR(64)")
    private String processInstanceId;


    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public String getApplyDept() {
        return applyDept;
    }

    public void setApplyDept(String applyDept) {
        this.applyDept = applyDept;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getApplyReason() {
        return applyReason;
    }

    public void setApplyReason(String applyReason) {
        this.applyReason = applyReason;
    }

    public String getOrgDirectorId() {
        return orgDirectorId;
    }

    public void setOrgDirectorId(String orgDirectorId) {
        this.orgDirectorId = orgDirectorId;
    }

    public String getOrgDirector() {
        return orgDirector;
    }

    public void setOrgDirector(String orgDirector) {
        this.orgDirector = orgDirector;
    }

    public Date getOrgDirectorDate() {
        return orgDirectorDate;
    }

    public void setOrgDirectorDate(Date orgDirectorDate) {
        this.orgDirectorDate = orgDirectorDate;
    }

    public String getComprehensivehandlesug() {
        return comprehensivehandlesug;
    }

    public void setComprehensivehandlesug(String comprehensivehandlesug) {
        this.comprehensivehandlesug = comprehensivehandlesug;
    }

    public String getComprehensiveId() {
        return comprehensiveId;
    }

    public void setComprehensiveId(String comprehensiveId) {
        this.comprehensiveId = comprehensiveId;
    }

    public String getComprehensiveName() {
        return comprehensiveName;
    }

    public void setComprehensiveName(String comprehensiveName) {
        this.comprehensiveName = comprehensiveName;
    }

    public Date getComprehensiveDate() {
        return comprehensiveDate;
    }

    public void setComprehensiveDate(Date comprehensiveDate) {
        this.comprehensiveDate = comprehensiveDate;
    }

    public String getLeaderhandlesug() {
        return leaderhandlesug;
    }

    public void setLeaderhandlesug(String leaderhandlesug) {
        this.leaderhandlesug = leaderhandlesug;
    }

    public String getLeaderId() {
        return leaderId;
    }

    public void setLeaderId(String leaderId) {
        this.leaderId = leaderId;
    }

    public String getLeaderName() {
        return leaderName;
    }

    public void setLeaderName(String leaderName) {
        this.leaderName = leaderName;
    }

    public Date getLeaderDate() {
        return leaderDate;
    }

    public void setLeaderDate(Date leaderDate) {
        this.leaderDate = leaderDate;
    }

    public List<GoodsDetail> getGoodsDetailList() {
        return goodsDetailList;
    }

    public void setGoodsDetailList(List<GoodsDetail> goodsDetailList) {
        this.goodsDetailList = goodsDetailList;
    }

    public String getIsFinishFiling() {
        return isFinishFiling;
    }

    public void setIsFinishFiling(String isFinishFiling) {
        this.isFinishFiling = isFinishFiling;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getProcessInstanceId() {
        return processInstanceId;
    }

    public void setProcessInstanceId(String processInstanceId) {
        this.processInstanceId = processInstanceId;
    }
}
