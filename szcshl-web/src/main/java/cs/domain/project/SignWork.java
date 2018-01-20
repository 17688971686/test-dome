package cs.domain.project;

import com.alibaba.fastjson.annotation.JSONField;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by ldm on 2018/1/20.
 */
@Entity
@Table(name = "V_SIGN_WORK")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@IdClass(SignBranchID.class)
public class SignWork {
    @Id
    @Column(name="signId")
    private String signId;
    @Id
    @Column(name="branchId")
    private String branchId;
    @Column
    private String projectName;
    @Column
    private String reviewstage;
    @Temporal(TemporalType.TIMESTAMP)
    @Column
    @JSONField(format = "yyyy-MM-dd")
    private Date signdate;
    @Column
    private String wpid;
    @Column
    private String orgName;
    @Column
    private String userIds;
    @Column
    private String displaynames;
    @Column
    private String businessType;

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
