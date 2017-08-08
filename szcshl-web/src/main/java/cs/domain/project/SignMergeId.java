package cs.domain.project;

/**
 * Created by ldm on 2017/8/6.
 */

import java.io.Serializable;

/**
 * 项目合并ID
 */
public class SignMergeId implements Serializable {

    private String signId;
    private String mergeId;
    private String mergeType;

    public String getSignId() {
        return signId;
    }

    public void setSignId(String signId) {
        this.signId = signId;
    }

    public String getMergeId() {
        return mergeId;
    }

    public void setMergeId(String mergeId) {
        this.mergeId = mergeId;
    }

    public String getMergeType() {
        return mergeType;
    }

    public void setMergeType(String mergeType) {
        this.mergeType = mergeType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SignMergeId that = (SignMergeId) o;

        if (signId != null ? !signId.equals(that.signId) : that.signId != null) return false;
        if (mergeId != null ? !mergeId.equals(that.mergeId) : that.mergeId != null) return false;
        return mergeType != null ? mergeType.equals(that.mergeType) : that.mergeType == null;
    }

    @Override
    public int hashCode() {
        int result = signId != null ? signId.hashCode() : 0;
        result = 31 * result + (mergeId != null ? mergeId.hashCode() : 0);
        result = 31 * result + (mergeType != null ? mergeType.hashCode() : 0);
        return result;
    }

    public SignMergeId(){}
}
