package cs.model.project;

/**
 * Created by ldm on 2017/8/2 0002.
 */
public class SignBranchDto {

    private String signId;                  //项目ID
    private String branchId;                //分支序号
    private String isNeedWP;                //是否需要工作方案
    private String isEndWP;                 //是否完成工作方案
    private String isMainBrabch;            //是否主分支

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
}
