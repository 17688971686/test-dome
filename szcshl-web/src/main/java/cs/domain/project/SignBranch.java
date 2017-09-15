package cs.domain.project;

import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

/**
 * Created by Administrator on 2017/8/1 0001.
 */
@Entity
@Table(name = "cs_sign_branch")
@DynamicUpdate(true)
@IdClass(SignBranchID.class)
public class SignBranch {
    // 主键，这里需要添加@Id标记
    @Id
    @Column(name="signId",columnDefinition="VARCHAR(64)")
    private String signId;                  //项目ID
    // 主键，这里需要添加@Id标记
    @Id
    @Column(name="branchId",columnDefinition="VARCHAR(64)")
    private String branchId;                //分支序号
    /**
     * 是否需要工作方案(默认需要)
     */
    @Column(columnDefinition="VARCHAR(2) default '9' ")
    private String isNeedWP;
    /**
     * 是否完成工作方案
     */
    @Column(columnDefinition="VARCHAR(2)")
    private String isEndWP;
    /**
     * 是否主分支
     */
    @Column(columnDefinition="VARCHAR(2)")
    private String isMainBrabch;
    /**
     * 分支办理部门
     */
    @Column(columnDefinition="VARCHAR(64)")
    private String orgId;

    /**
     * 是否已经完成
     */
    @Column(columnDefinition="VARCHAR(2)")
    private String isFinished;

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

    public String getIsNeedWP() {
        return isNeedWP;
    }

    public void setIsNeedWP(String isNeedWP) {
        this.isNeedWP = isNeedWP;
    }

    public String getIsEndWP() {
        return isEndWP;
    }

    public void setIsEndWP(String isEndWP) {
        this.isEndWP = isEndWP;
    }

    public String getIsMainBrabch() {
        return isMainBrabch;
    }

    public void setIsMainBrabch(String isMainBrabch) {
        this.isMainBrabch = isMainBrabch;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getIsFinished() {
        return isFinished;
    }

    public void setIsFinished(String isFinished) {
        this.isFinished = isFinished;
    }

    public SignBranch(String signId, String branchId, String isNeedWP, String isEndWP, String isMainBrabch, String orgId, String isFinished) {
        this.signId = signId;
        this.branchId = branchId;
        this.isNeedWP = isNeedWP;
        this.isEndWP = isEndWP;
        this.isMainBrabch = isMainBrabch;
        this.orgId = orgId;
        this.isFinished = isFinished;
    }

    public SignBranch() {
    }
}
