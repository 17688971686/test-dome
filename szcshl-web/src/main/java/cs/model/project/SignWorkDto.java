package cs.model.project;

import java.util.Date;
import java.util.List;

/**
 * Created by shenning on 2018/1/20.
 */
public class SignWorkDto {

    private String signId;

    private String branchId;

    private String projectName;

    private String reviewstage;

    private Date signdate;

    private String wpid;

    private String orgName;

    private String userIds;

    private String displaynames;

    private String businessType;
    //多个分支的时候使用
    private List<SignWorkDto> signWorkDtoList;

    public String getSignId() {
        return signId;
    }

    public void setSignId(String signId) {
        this.signId = signId;
    }

    public String getBranchId() {
        return branchId;
    }

    public void setBranchId(String branchId) {
        this.branchId = branchId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getReviewstage() {
        return reviewstage;
    }

    public void setReviewstage(String reviewstage) {
        this.reviewstage = reviewstage;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getUserIds() {
        return userIds;
    }

    public void setUserIds(String userIds) {
        this.userIds = userIds;
    }

    public String getDisplaynames() {
        return displaynames;
    }

    public void setDisplaynames(String displaynames) {
        this.displaynames = displaynames;
    }

    public List<SignWorkDto> getSignWorkDtoList() {
        return signWorkDtoList;
    }

    public void setSignWorkDtoList(List<SignWorkDto> signWorkDtoList) {
        this.signWorkDtoList = signWorkDtoList;
    }

    public Date getSigndate() {
        return signdate;
    }

    public void setSigndate(Date signdate) {
        this.signdate = signdate;
    }

    public String getWpid() {
        return wpid;
    }

    public void setWpid(String wpid) {
        this.wpid = wpid;
    }

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }
}
