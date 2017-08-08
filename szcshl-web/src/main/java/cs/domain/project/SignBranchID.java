package cs.domain.project;

import java.io.Serializable;

/**
 * 项目分支联合主键（项目ID + 分支序号，是唯一的）
 * 1. 主键类必须实现序列化接口（implements Serializable）；
 * 2. 主键类必须有默认的public无参数的构造方法；
 * 3. 主键类必须覆盖equals和hashCode方法。
 * Created by ldm on 2017/8/1 0001.
 */
public class SignBranchID implements Serializable {
    private String signId;
    private String branchId;

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

    public SignBranchID() {

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SignBranchID that = (SignBranchID) o;

        if (signId != null ? !signId.equals(that.signId) : that.signId != null) return false;
        return branchId != null ? branchId.equals(that.branchId) : that.branchId == null;
    }

    @Override
    public int hashCode() {
        int result = signId != null ? signId.hashCode() : 0;
        result = 31 * result + (branchId != null ? branchId.hashCode() : 0);
        return result;
    }
}
