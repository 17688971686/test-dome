package cs.domain.project;

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * 项目-负责人中间表（主要用于查询用）
 */
@Entity
@Table(name = "cs_sign_principal")
@DynamicUpdate(true)
public class SignPrincipal {

    @Id
    @GeneratedValue(generator= "plansignGenerator")
    @GenericGenerator(name= "plansignGenerator",strategy = "uuid")
    private String id;

    /**
     * 用户ID
     */
    @Column(columnDefinition="VARCHAR(64)")
    private String userId;

    /**
     *收文ID
     */
    @Column(columnDefinition="VARCHAR(64)")
    private String singId;

    /**
     *是否主流程
     */
    @Column(columnDefinition="VARCHAR(2)")
    private String isMainFlow;

    /**
     * 负责人类型
     */
    @Column(columnDefinition="VARCHAR(64)")
    private String userType;

    /**
     * 排序（用于当有三个负责人的时候，获取第二个负责人）
     */
    @Column(columnDefinition="INTEGER")
    private Integer sort;

    /**
     * 是否总负责人
     */
    @Column(columnDefinition="VARCHAR(2)")
    private String isMainUser;  //是否总负责人

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getSingId() {
        return singId;
    }

    public void setSingId(String singId) {
        this.singId = singId;
    }

    public String getIsMainFlow() {
        return isMainFlow;
    }

    public void setIsMainFlow(String isMainFlow) {
        this.isMainFlow = isMainFlow;
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
}
