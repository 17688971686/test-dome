package cs.model.project;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 业绩统计信息
 * Created by ldm on 2018/12/13 0013.
 */
public class Achievement {

    /**
     * 项目ID
     */
    private String signId;
    /**
     * 项目名称
     */
    private String projName;
    /**
     * 发文号
     */
    private String fileNum;
    /**
     * 发文日期
     */
    private Date dispatchDate;
    /**
     * 报审金额
     */
    private BigDecimal declareValue;
    /**
     * 审核后金额
     */
    private BigDecimal authorizeValue;
    /**
     * 核增核减金额
     */
    private BigDecimal extraValue;
    /**
     * 核增核减率
     */
    private BigDecimal extraRate;
    /**
     * 部门ID
     */
    private String orgId;

    /**
     * 部门名称
     */
    private String orgName;
    /**
     * 用户ID
     */
    private String userId;
    /**
     * 用户名称
     */
    private String userName;
    /**
     * 是否主办人员
     */
    private String isMainUser;

    /**
     * 分支
     */
    private String branchId;

    /**
     * 主办发文
     */
    private int mainDisSum;
    /**
     * 协办出文
     */
    private int assistDisSum;

    /**
     * 用户所在部门
     */
    private String userOrgId;

    /**
     * 用户所在组别
     */
    private String deptIds;
    /**
     * 主任、副主任子集（如一级是部门，子集则为用户）
     */
    List<Achievement> childList;

    public List<Achievement> getChildList() {
        return childList;
    }

    public void setChildList(List<Achievement> childList) {
        this.childList = childList;
    }

    public String getSignId() {
        return signId;
    }

    public void setSignId(String signId) {
        this.signId = signId;
    }

    public String getProjName() {
        return projName;
    }

    public void setProjName(String projName) {
        this.projName = projName;
    }

    public String getFileNum() {
        return fileNum;
    }

    public void setFileNum(String fileNum) {
        this.fileNum = fileNum;
    }

    public Date getDispatchDate() {
        return dispatchDate;
    }

    public void setDispatchDate(Date dispatchDate) {
        this.dispatchDate = dispatchDate;
    }

    public BigDecimal getDeclareValue() {
        return declareValue;
    }

    public void setDeclareValue(BigDecimal declareValue) {
        this.declareValue = declareValue;
    }

    public BigDecimal getAuthorizeValue() {
        return authorizeValue;
    }

    public void setAuthorizeValue(BigDecimal authorizeValue) {
        this.authorizeValue = authorizeValue;
    }

    public BigDecimal getExtraValue() {
        return extraValue;
    }

    public void setExtraValue(BigDecimal extraValue) {
        this.extraValue = extraValue;
    }

    public BigDecimal getExtraRate() {
        return extraRate;
    }

    public void setExtraRate(BigDecimal extraRate) {
        this.extraRate = extraRate;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
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

    public String getBranchId() {
        return branchId;
    }

    public void setBranchId(String branchId) {
        this.branchId = branchId;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public int getMainDisSum() {
        return mainDisSum;
    }

    public void setMainDisSum(int mainDisSum) {
        this.mainDisSum = mainDisSum;
    }

    public int getAssistDisSum() {
        return assistDisSum;
    }

    public void setAssistDisSum(int assistDisSum) {
        this.assistDisSum = assistDisSum;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserOrgId() {
        return userOrgId;
    }

    public void setUserOrgId(String userOrgId) {
        this.userOrgId = userOrgId;
    }

    public String getDeptIds() {
        return deptIds;
    }

    public void setDeptIds(String deptIds) {
        this.deptIds = deptIds;
    }
}
