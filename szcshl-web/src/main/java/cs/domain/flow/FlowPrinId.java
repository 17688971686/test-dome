package cs.domain.flow;

import java.io.Serializable;

/**
 * 流程负责人ID
 * Created by ldm on 2017/9/4 0004.
 */

public class FlowPrinId implements Serializable {

    /**
     * 业务ID
     */
    private String busiId;
    /**
     * 用户ID
     */
    private String userId;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FlowPrinId that = (FlowPrinId) o;

        if (busiId != null ? !busiId.equals(that.busiId) : that.busiId != null) return false;
        return userId != null ? userId.equals(that.userId) : that.userId == null;
    }

    @Override
    public int hashCode() {
        int result = busiId != null ? busiId.hashCode() : 0;
        result = 31 * result + (userId != null ? userId.hashCode() : 0);
        return result;
    }
}
