package cs.model.book;

import cs.model.BaseDto;

import java.util.Date;
import java.util.List;


/**
 * Description: 图书采购流程 页面数据模型
 * author: zsl
 * Date: 2017-9-11 10:25:15
 */
public class BookBuyBusinessDto extends BaseDto {

    private String businessId;
    private String businessName;
    private String applyDept;
    private String operator;
    private String buyChannel;
    private String applyReason;
    private String orgDirectorId;
    private String orgDirector;
    private Date orgDirectorDate;
    private String orgSLeaderId;
    private String orgSLeader;
    private String orgSLeaderHandlesug;
    private Date orgSLeaderDate;
    private String orgMLeaderId;
    private String orgMLeader;
    private String orgMLeaderHandlesug;
    private Date orgMLeaderDate;
    private String filerId;
    private String filer;
    private String filerHandlesug;
    private Date filerDate;
    private List bookBuyList;
    private String processInstanceId;
    private String isFinishFiling;
    private String state;
    private String remark;
    public BookBuyBusinessDto() {
    }

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
    public List getBookBuyList() {
        return bookBuyList;
    }

    public void setBookBuyList(List bookBuyList) {
        this.bookBuyList = bookBuyList;
    }
    public String getProcessInstanceId() {
        return processInstanceId;
    }

    public void setProcessInstanceId(String processInstanceId) {
        this.processInstanceId = processInstanceId;
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
}