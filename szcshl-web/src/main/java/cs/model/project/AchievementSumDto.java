package cs.model.project;
import java.math.BigDecimal;


/**
 * 业绩统计
 *
 * @author zsl
 */
public class AchievementSumDto {
    //主办发文
    private int mainDisSum;
    //协办出文
    private int assistDisSum;
    //申报金额
    private BigDecimal declarevalueSum;
    //审定金额
    private BigDecimal authorizevalueSum;
    //累计净核减投资
    private BigDecimal extravalueSum;
    //核减率
    private BigDecimal extraRateSum;

    private String year;

    private String quarter;

    private String userId;

    private String deptIds;

    private String deptNames;

    public BigDecimal getDeclarevalueSum() {
        return declarevalueSum;
    }

    public void setDeclarevalueSum(BigDecimal declarevalueSum) {
        this.declarevalueSum = declarevalueSum;
    }

    public BigDecimal getAuthorizevalueSum() {
        return authorizevalueSum;
    }

    public void setAuthorizevalueSum(BigDecimal authorizevalueSum) {
        this.authorizevalueSum = authorizevalueSum;
    }

    public BigDecimal getExtravalueSum() {
        return extravalueSum;
    }

    public void setExtravalueSum(BigDecimal extravalueSum) {
        this.extravalueSum = extravalueSum;
    }

    public BigDecimal getExtraRateSum() {
        return extraRateSum;
    }

    public void setExtraRateSum(BigDecimal extraRateSum) {
        this.extraRateSum = extraRateSum;
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
}
