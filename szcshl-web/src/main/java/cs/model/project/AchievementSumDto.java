package cs.model.project;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


/**
 * 业绩统计
 *
 * @author zsl
 */
public class AchievementSumDto {
    /**
     * 主办发文
     */
    private int mainDisSum;
    /**
     * 协办出文
     */
    private int assistDisSum;
    /**
     * 主办申报金额
     */
    private BigDecimal mainDeclarevalueSum;
    /**
     * 申报金额
     */
    private BigDecimal assistDeclarevalueSum;
    /**
     * 主办审定金额
     */
    private BigDecimal mainAuthorizevalueSum;
    /**
     * 审定金额
     */
    private BigDecimal assistAuthorizevalueSum;
    /**
     * 主办累计净核减投资
     */
    private BigDecimal mainExtravalueSum;
    /**
     * 累计净核减投资
     */
    private BigDecimal assistExtravalueSum;
    /**
     * 主办核减率
     */
    private BigDecimal mainExtraRateSum;
    /**
     * 核减率
     */
    private BigDecimal assistExtraRateSum;
    /**
     * 部门ID
     */
    private String deptIds;
    /**
     * 部门名称
     */
    private String deptNames;
    /**
     * （部长）主办人评审项目一览表
     */
    private List<Achievement> mainChildList;

    /**
     * （部长） 协办人评审项目一览表
     */
    private List<Achievement> assistChildList;

    private String year;

    private String quarter;

    private String userId;

    private String isMainPro;

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

    public BigDecimal getMainDeclarevalueSum() {
        return mainDeclarevalueSum;
    }

    public void setMainDeclarevalueSum(BigDecimal mainDeclarevalueSum) {
        this.mainDeclarevalueSum = mainDeclarevalueSum;
    }

    public BigDecimal getAssistDeclarevalueSum() {
        return assistDeclarevalueSum;
    }

    public void setAssistDeclarevalueSum(BigDecimal assistDeclarevalueSum) {
        this.assistDeclarevalueSum = assistDeclarevalueSum;
    }

    public BigDecimal getMainAuthorizevalueSum() {
        return mainAuthorizevalueSum;
    }

    public void setMainAuthorizevalueSum(BigDecimal mainAuthorizevalueSum) {
        this.mainAuthorizevalueSum = mainAuthorizevalueSum;
    }

    public BigDecimal getAssistAuthorizevalueSum() {
        return assistAuthorizevalueSum;
    }

    public void setAssistAuthorizevalueSum(BigDecimal assistAuthorizevalueSum) {
        this.assistAuthorizevalueSum = assistAuthorizevalueSum;
    }

    public BigDecimal getMainExtravalueSum() {
        return mainExtravalueSum;
    }

    public void setMainExtravalueSum(BigDecimal mainExtravalueSum) {
        this.mainExtravalueSum = mainExtravalueSum;
    }

    public BigDecimal getAssistExtravalueSum() {
        return assistExtravalueSum;
    }

    public void setAssistExtravalueSum(BigDecimal assistExtravalueSum) {
        this.assistExtravalueSum = assistExtravalueSum;
    }

    public BigDecimal getMainExtraRateSum() {
        return mainExtraRateSum;
    }

    public void setMainExtraRateSum(BigDecimal mainExtraRateSum) {
        this.mainExtraRateSum = mainExtraRateSum;
    }

    public BigDecimal getAssistExtraRateSum() {
        return assistExtraRateSum;
    }

    public void setAssistExtraRateSum(BigDecimal assistExtraRateSum) {
        this.assistExtraRateSum = assistExtraRateSum;
    }

    public String getDeptIds() {
        return deptIds;
    }

    public void setDeptIds(String deptIds) {
        this.deptIds = deptIds;
    }

    public String getDeptNames() {
        return deptNames;
    }

    public void setDeptNames(String deptNames) {
        this.deptNames = deptNames;
    }

    public List<Achievement> getMainChildList() {
        return mainChildList;
    }

    public void setMainChildList(List<Achievement> mainChildList) {
        this.mainChildList = mainChildList;
    }

    public List<Achievement> getAssistChildList() {
        return assistChildList;
    }

    public void setAssistChildList(List<Achievement> assistChildList) {
        this.assistChildList = assistChildList;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getQuarter() {
        return quarter;
    }

    public void setQuarter(String quarter) {
        this.quarter = quarter;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getIsMainPro() {
        return isMainPro;
    }

    public void setIsMainPro(String isMainPro) {
        this.isMainPro = isMainPro;
    }

    public void init() {
        this.mainDisSum = 0;
        this.assistDisSum = 0;
        this.mainAuthorizevalueSum = BigDecimal.ZERO;
        this.assistAuthorizevalueSum = BigDecimal.ZERO;
        this.mainDeclarevalueSum = BigDecimal.ZERO;
        this.assistDeclarevalueSum = BigDecimal.ZERO;
        this.mainExtravalueSum = BigDecimal.ZERO;
        this.assistExtravalueSum = BigDecimal.ZERO;
        this.mainExtraRateSum = BigDecimal.ZERO;
        this.assistExtraRateSum = BigDecimal.ZERO;
        this.mainChildList = new ArrayList<>();
        this.assistChildList = new ArrayList<>();
    }
}
