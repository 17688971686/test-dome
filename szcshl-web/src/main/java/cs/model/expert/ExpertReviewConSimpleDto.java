package cs.model.expert;

import java.math.BigDecimal;

/**
 * 专家评审基本情况综合统计
 *
 * @author zsl
 */
public class ExpertReviewConSimpleDto {
    //专家ID
    private String expertID;
    //专家姓名
    private String name;
    //评审次数
    private BigDecimal reviewNum;//评审次数
    //函授次数
    private BigDecimal letterRwNum;
    //总参会次数
    private BigDecimal totalNum;
    //工作单位
    private String comPany;

    public String getExpertID() {
        return expertID;
    }

    public void setExpertID(String expertID) {
        this.expertID = expertID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getReviewNum() {
        return reviewNum;
    }

    public void setReviewNum(BigDecimal reviewNum) {
        this.reviewNum = reviewNum;
    }

    public BigDecimal getLetterRwNum() {
        return letterRwNum;
    }

    public void setLetterRwNum(BigDecimal letterRwNum) {
        this.letterRwNum = letterRwNum;
    }

    public BigDecimal getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(BigDecimal totalNum) {
        this.totalNum = totalNum;
    }

    public String getComPany() {
        return comPany;
    }

    public void setComPany(String comPany) {
        this.comPany = comPany;
    }
}
