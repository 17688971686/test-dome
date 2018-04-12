package cs.model.expert;
import java.math.BigDecimal;


/**
 * 业绩统计
 *
 * @author zsl
 */
public class AchievementSumDto {
    //发文数
    private Integer disSum;
    //申报金额
    private BigDecimal declarevalueSum;
    //审定金额
    private BigDecimal authorizevalueSum;
    //累计净核减投资
    private BigDecimal extravalueSum;
    //核减率
    private BigDecimal extraRateSum;

    private String beginTime;

    private String endTime;

    private String ismainuser;

    private String year;

    private String quarter;


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

    public String getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(String beginTime) {
        this.beginTime = beginTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public Integer getDisSum() {
        return disSum;
    }

    public void setDisSum(Integer disSum) {
        this.disSum = disSum;
    }

    public String getIsmainuser() {
        return ismainuser;
    }

    public void setIsmainuser(String ismainuser) {
        this.ismainuser = ismainuser;
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
}
