package cs.model.expert;

import com.alibaba.fastjson.annotation.JSONField;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 项目评审情况统计
 *
 * @author zsl
 */
public class ProReviewConditionDto {

    private BigDecimal proCount;
    private String reviewStage;
    //申报金额
    private BigDecimal declareValue;
    //审定金额
    private BigDecimal authorizeValue;
    //累计净核减投资
    private BigDecimal ljhj;
    //核减率
    private BigDecimal hjl;

    private String beginTime;
    private String endTime;

    //工程类型
    private String projectType;
    //工程类型项目数
    private BigDecimal projectTypeCount;

    private String projectName;//项目名称

    private String isadvanced;//是否提前介入



    /**
     * 签收日期
     */
    @JSONField(format = "yyyy-MM-dd")
    private Date signDate;
    /***
     * 以下字段为生成月报简报使用
     */
    //申报金额
    private String declareStr;
    //审定金额
    private String authorizeStr;
    //累计净核减投资
    private String ljhjStr;
    //核减率
    private String hjlStr;

    public BigDecimal getProCount() {
        return proCount;
    }

    public void setProCount(BigDecimal proCount) {
        this.proCount = proCount;
    }

    public String getReviewStage() {
        return reviewStage;
    }

    public void setReviewStage(String reviewStage) {
        this.reviewStage = reviewStage;
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

    public BigDecimal getLjhj() {
        return ljhj;
    }

    public void setLjhj(BigDecimal ljhj) {
        this.ljhj = ljhj;
    }

    public BigDecimal getHjl() {
        return hjl;
    }

    public void setHjl(BigDecimal hjl) {
        this.hjl = hjl;
    }

    public Date getSignDate() {
        return signDate;
    }

    public void setSignDate(Date signDate) {
        this.signDate = signDate;
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

    public String getProjectType() {
        return projectType;
    }

    public void setProjectType(String projectType) {
        this.projectType = projectType;
    }

    public BigDecimal getProjectTypeCount() {
        return projectTypeCount;
    }

    public void setProjectTypeCount(BigDecimal projectTypeCount) {
        this.projectTypeCount = projectTypeCount;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getDeclareStr() {
        return declareStr;
    }

    public void setDeclareStr(String declareStr) {
        this.declareStr = declareStr;
    }

    public String getAuthorizeStr() {
        return authorizeStr;
    }

    public void setAuthorizeStr(String authorizeStr) {
        this.authorizeStr = authorizeStr;
    }

    public String getLjhjStr() {
        return ljhjStr;
    }

    public void setLjhjStr(String ljhjStr) {
        this.ljhjStr = ljhjStr;
    }

    public String getHjlStr() {
        return hjlStr;
    }

    public void setHjlStr(String hjlStr) {
        this.hjlStr = hjlStr;
    }

    public String getIsadvanced() {
        return isadvanced;
    }

    public void setIsadvanced(String isadvanced) {
        this.isadvanced = isadvanced;
    }
}
