package cs.model.expert;

import java.math.BigDecimal;
import java.util.List;

/**
 * 专家费统计相关信息
 *
 * @author zsl
 */
public class ExpertCostCountDto  {
    //专家id
    private String expertId;
    //专家编号
    private String expertNo;
    //专家姓名
    private String name;
    //身份证号
    private String idCard;
    //手机号码
    private String userPhone;
    //月合计评审费
    private BigDecimal reviewcost;
    //月合计交税金额
    private BigDecimal reviewtaxes;
    //月合计金额
    private BigDecimal monthTotal;
    //年合计评审费
    private BigDecimal yreviewcost;
    //年合计交税金额
    private BigDecimal yreviewtaxes;

    private String beginTime;

    List<ExpertCostDetailCountDto> expertCostDetailCountDtoList;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public BigDecimal getReviewcost() {
        return reviewcost;
    }

    public void setReviewcost(BigDecimal reviewcost) {
        this.reviewcost = reviewcost;
    }

    public BigDecimal getReviewtaxes() {
        return reviewtaxes;
    }

    public void setReviewtaxes(BigDecimal reviewtaxes) {
        this.reviewtaxes = reviewtaxes;
    }

    public BigDecimal getYreviewcost() {
        return yreviewcost;
    }

    public void setYreviewcost(BigDecimal yreviewcost) {
        this.yreviewcost = yreviewcost;
    }

    public BigDecimal getYreviewtaxes() {
        return yreviewtaxes;
    }

    public void setYreviewtaxes(BigDecimal yreviewtaxes) {
        this.yreviewtaxes = yreviewtaxes;
    }

    public String getExpertNo() {
        return expertNo;
    }

    public void setExpertNo(String expertNo) {
        this.expertNo = expertNo;
    }

    public List<ExpertCostDetailCountDto> getExpertCostDetailCountDtoList() {
        return expertCostDetailCountDtoList;
    }

    public void setExpertCostDetailCountDtoList(List<ExpertCostDetailCountDto> expertCostDetailCountDtoList) {
        this.expertCostDetailCountDtoList = expertCostDetailCountDtoList;
    }

    public String getExpertId() {
        return expertId;
    }

    public void setExpertId(String expertId) {
        this.expertId = expertId;
    }

    public BigDecimal getMonthTotal() {
        return monthTotal;
    }

    public void setMonthTotal(BigDecimal monthTotal) {
        this.monthTotal = monthTotal;
    }

    public String getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(String beginTime) {
        this.beginTime = beginTime;
    }
}
