package cs.domain.project;

import java.io.Serializable;

/**
 * 项目负责人主键
 * Created by ldm on 2017/8/3.
 */
public class SignPrincipalID implements Serializable {

    private String signId;
    private String userId;

    public String getSignId() {
        return signId;
    }

    public void setSignId(String signId) {
        this.signId = signId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public SignPrincipalID(){

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        SignPrincipalID that = (SignPrincipalID) o;

        if (signId != null ? !signId.equals(that.signId) : that.signId != null) {
            return false;
        }
        return userId != null ? userId.equals(that.userId) : that.userId == null;
    }

    @Override
    public int hashCode() {
        int result = signId != null ? signId.hashCode() : 0;
        result = 31 * result + (userId != null ? userId.hashCode() : 0);
        return result;
    }
}
