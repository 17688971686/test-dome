package cs.domain.expert;

import java.io.Serializable;

/**
 * Created by shenning on 2018/1/19.
 */
public class ExpertSignId implements Serializable {

    private String signid;
    private String expertId;

    public String getSignid() {
        return signid;
    }

    public void setSignid(String signid) {
        this.signid = signid;
    }

    public String getExpertId() {
        return expertId;
    }

    public void setExpertId(String expertId) {
        this.expertId = expertId;
    }

    public ExpertSignId() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ExpertSignId that = (ExpertSignId) o;

        if (signid != null ? !signid.equals(that.signid) : that.signid != null) {
            return false;
        }
        return expertId != null ? expertId.equals(that.expertId) : that.expertId == null;
    }

    @Override
    public int hashCode() {
        int result = signid != null ? signid.hashCode() : 0;
        result = 31 * result + (expertId != null ? expertId.hashCode() : 0);
        return result;
    }
}
