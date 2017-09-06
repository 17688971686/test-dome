package cs.domain.flow;

import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

/**
 * 流程负责人
 * Created by ldm on 2017/9/4 0004.
 */
@Entity
@Table(name = "cs_flow_principal")
@DynamicUpdate(true)
@IdClass(FlowPrinId.class)
public class FlowPrincipal {

    /**
     * 业务ID
     */
    @Id
    @Column(name="busiId",columnDefinition="VARCHAR(64)")
    private String busiId;

    /**
     * 用户ID
     */
    @Id
    @Column(name="userId",columnDefinition="VARCHAR(64)")
    private String userId;

    /**
     * 是否总负责人
     */
    @Column(columnDefinition="VARCHAR(2)")
    private String isMainUser;


    public String getBusiId() {
        return busiId;
    }

    public void setBusiId(String busiId) {
        this.busiId = busiId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getIsMainUser() {
        return isMainUser;
    }

    public void setIsMainUser(String isMainUser) {
        this.isMainUser = isMainUser;
    }
}
