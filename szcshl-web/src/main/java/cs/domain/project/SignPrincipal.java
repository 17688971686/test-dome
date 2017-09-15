package cs.domain.project;

import org.hibernate.annotations.DynamicUpdate;
import javax.persistence.*;

/**
 * 项目-负责人中间表（主要用于查询用）
 */
@Entity
@Table(name = "cs_sign_principal2")
@DynamicUpdate(true)
@IdClass(SignPrincipalID.class)
public class SignPrincipal {

    /**
     *收文ID
     */
    @Id
    @Column(name="signId",columnDefinition="VARCHAR(64)")
    private String signId;

    /**
     * 用户ID
     */
    @Id
    @Column(name="userId",columnDefinition="VARCHAR(64)")
    private String userId;

    /**
     *流程分支（1,2,3,4）最多四个流程
     */
    @Column(columnDefinition="VARCHAR(2)")
    private String flowBranch;

    /**
     * 负责人类型(土建、安装)
     */
    @Column(columnDefinition="VARCHAR(64)")
    private String userType;

    /**
     * 排序（用于当有三个负责人的时候，获取第二个负责人）
     */
    @Column(columnDefinition="INTEGER")
    private Integer sort;

    /**
     * 是否总负责人（第一负责人）
     */
    @Column(columnDefinition="VARCHAR(2)")
    private String isMainUser;  //是否总负责人

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getSignId() {
        return signId;
    }

    public void setSignId(String signId) {
        this.signId = signId;
    }

    public String getFlowBranch() {
        return flowBranch;
    }

    public void setFlowBranch(String flowBranch) {
        this.flowBranch = flowBranch;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public String getIsMainUser() {
        return isMainUser;
    }

    public void setIsMainUser(String isMainUser) {
        this.isMainUser = isMainUser;
    }

    public SignPrincipal(String signId, String userId, String flowBranch, String userType, Integer sort, String isMainUser) {
        this.signId = signId;
        this.userId = userId;
        this.flowBranch = flowBranch;
        this.userType = userType;
        this.sort = sort;
        this.isMainUser = isMainUser;
    }

    public SignPrincipal() {
    }
}
