package cs.domain.book;

import com.alibaba.fastjson.annotation.JSONField;
import cs.domain.DomainBase;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * Created by zsl on 2017-09-04.
 */
@Entity
@Table(name = "cs_books_buy_business")
public class BookBuyBusiness extends DomainBase {
    //业务id
    @Id
    private String businessId;
    //业务流程名字
    @Column(columnDefinition = "varchar(64) ")
    private String businessName;
    //申请部门
    @Column(columnDefinition = "varchar(64) ")
    private String applyDept;
    //申请日期
    @Column(columnDefinition = "DATE")
    private Date applyDate;
    //经办人
    @Column(columnDefinition = "varchar(64) ")
    private String operator;
    //购买渠道
    @Column(columnDefinition = "varchar(30) ")
    private String buyChannel;
    //申请事由及发放范围
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
    //分管领导（副主任）ID
    @Column(columnDefinition = "VARCHAR(64)")
    private String orgSLeaderId;
    //分管领导（副主任）
    @Column(columnDefinition = "varchar(255)")
    private String orgSLeader;
    //分管副主任处理意见
    @Column(columnDefinition = "varchar(255)")
    private String orgSLeaderHandlesug;
    //分管副主任审批日期
    @Column(columnDefinition = "DATE")
    private Date orgSLeaderDate;
    //主任ID
    @Column(columnDefinition = "VARCHAR(64)")
    private String orgMLeaderId;
    //主任
    @Column(columnDefinition = "varchar(255)")
    private String orgMLeader; //主任
    //主任处理意见
    @Column(columnDefinition = "varchar(255)")
    private String orgMLeaderHandlesug;
    //主任审批日期
    @Column(columnDefinition = "DATE")
    private Date orgMLeaderDate;
    //档案员ID
    @Column(columnDefinition = "VARCHAR(64)")
    private String filerId;
    //档案员
    @Column(columnDefinition = "varchar(255)")
    private String filer;
    //档案员处理意见
    @Column(columnDefinition = "varchar(255)")
    private String filerHandlesug;
    //档案员审批日期
    @Column(columnDefinition = "DATE")
    private Date filerDate;
    //图书
    @OneToMany(mappedBy = "bookBuyBusiness", fetch = FetchType.LAZY,orphanRemoval=true,cascade = CascadeType.ALL)
    @JSONField(serialize = false)
    private List<BookBuy> bookBuyList;
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

    public String getBuyChannel() {
        return buyChannel;
    }

    public void setBuyChannel(String buyChannel) {
        this.buyChannel = buyChannel;
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

    public String getOrgSLeaderId() {
        return orgSLeaderId;
    }

    public void setOrgSLeaderId(String orgSLeaderId) {
        this.orgSLeaderId = orgSLeaderId;
    }

    public String getOrgSLeader() {
        return orgSLeader;
    }

    public void setOrgSLeader(String orgSLeader) {
        this.orgSLeader = orgSLeader;
    }

    public String getOrgSLeaderHandlesug() {
        return orgSLeaderHandlesug;
    }

    public void setOrgSLeaderHandlesug(String orgSLeaderHandlesug) {
        this.orgSLeaderHandlesug = orgSLeaderHandlesug;
    }

    public Date getOrgSLeaderDate() {
        return orgSLeaderDate;
    }

    public void setOrgSLeaderDate(Date orgSLeaderDate) {
        this.orgSLeaderDate = orgSLeaderDate;
    }

    public String getOrgMLeaderId() {
        return orgMLeaderId;
    }

    public void setOrgMLeaderId(String orgMLeaderId) {
        this.orgMLeaderId = orgMLeaderId;
    }

    public String getOrgMLeader() {
        return orgMLeader;
    }

    public void setOrgMLeader(String orgMLeader) {
        this.orgMLeader = orgMLeader;
    }

    public String getOrgMLeaderHandlesug() {
        return orgMLeaderHandlesug;
    }

    public void setOrgMLeaderHandlesug(String orgMLeaderHandlesug) {
        this.orgMLeaderHandlesug = orgMLeaderHandlesug;
    }

    public Date getOrgMLeaderDate() {
        return orgMLeaderDate;
    }

    public void setOrgMLeaderDate(Date orgMLeaderDate) {
        this.orgMLeaderDate = orgMLeaderDate;
    }

    public String getFilerId() {
        return filerId;
    }

    public void setFilerId(String filerId) {
        this.filerId = filerId;
    }

    public String getFiler() {
        return filer;
    }

    public void setFiler(String filer) {
        this.filer = filer;
    }

    public String getFilerHandlesug() {
        return filerHandlesug;
    }

    public void setFilerHandlesug(String filerHandlesug) {
        this.filerHandlesug = filerHandlesug;
    }

    public Date getFilerDate() {
        return filerDate;
    }

    public void setFilerDate(Date filerDate) {
        this.filerDate = filerDate;
    }

    public List<BookBuy> getBookBuyList() {
        return bookBuyList;
    }

    public void setBookBuyList(List<BookBuy> bookBuyList) {
        this.bookBuyList = bookBuyList;
    }

    public String getProcessInstanceId() {
        return processInstanceId;
    }

    public void setProcessInstanceId(String processInstanceId) {
        this.processInstanceId = processInstanceId;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
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

    public Date getApplyDate() {
        return applyDate;
    }

    public void setApplyDate(Date applyDate) {
        this.applyDate = applyDate;
    }
}
