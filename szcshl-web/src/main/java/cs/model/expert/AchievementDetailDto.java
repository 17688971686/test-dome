package cs.model.expert;
import com.alibaba.fastjson.annotation.JSONField;

import java.math.BigDecimal;
import java.util.Date;


/**
 * 业绩明细
 *
 * @author zsl
 */
public class AchievementDetailDto {
    //发文日期
    @JSONField(format = "yyyy-MM-dd")
    private Date dispatchDate;
    //项目名称
    private String projectName;
    //发文编号
    private String fileNum;
    //申报金额
    private BigDecimal declareValue;
    //审定金额
    private BigDecimal authorizeValue;
    //核减（增）金额
    private BigDecimal extraValue;
    //增减（增）率
    private BigDecimal extraRate;

    private String ismainuser;
    public Date getDispatchDate() {
        return dispatchDate;
    }

    public void setDispatchDate(Date dispatchDate) {
        this.dispatchDate = dispatchDate;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getFileNum() {
        return fileNum;
    }

    public void setFileNum(String fileNum) {
        this.fileNum = fileNum;
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

    public String getIsmainuser() {
        return ismainuser;
    }

    public void setIsmainuser(String ismainuser) {
        this.ismainuser = ismainuser;
    }
}
