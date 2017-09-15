package cs.model.asserts.assertStorageBusiness;

import cs.model.BaseDto;

import java.util.Date;
import java.util.List;


/**
 * Description: 固定资产申购流程 页面数据模型
 * author: zsl
 * Date: 2017-9-15 14:45:23
 */
public class AssertStorageBusinessDto extends BaseDto {

    private String businessId;
    private String businessName;
    private String applyDept;
    private String operator;
    private String applyReason;
    private String orgDirectorId;
    private String orgDirector;
    private Date orgDirectorDate;
    private String comprehensivehandlesug;
    private String comprehensiveId;
    private String comprehensiveName;
    private Date comprehensiveDate;
    private String leaderhandlesug;
    private String leaderId;
    private String leaderName;
    private Date leaderDate;
    private List goodsDetailList;
    private String isFinishFiling;
    private String state;
    private String remark;
    private String processInstanceId;

    public AssertStorageBusinessDto() {
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
    public List getGoodsDetailList() {
        return goodsDetailList;
    }

    public void setGoodsDetailList(List goodsDetailList) {
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