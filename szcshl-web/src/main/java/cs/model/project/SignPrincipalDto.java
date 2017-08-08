package cs.model.project;

public class SignPrincipalDto {
    private String id;
    private String userId;
    private String singId;
    private String flowBranch;
    private String userType;
    private Integer sort;
    private String isMainUser;
    private String userName;

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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
