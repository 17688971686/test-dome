package cs.model.expert;

import java.math.BigDecimal;

/**
 * 项目评审费分类汇总
 *
 * @author zsl
 */
public class ProReviewClassifyCountDto {
    private String chargeName;
    private BigDecimal totalCharge;

    public String getChargeName() {
        return chargeName;
    }

    public void setChargeName(String chargeName) {
        this.chargeName = chargeName;
    }

    public BigDecimal getTotalCharge() {
        return totalCharge;
    }

    public void setTotalCharge(BigDecimal totalCharge) {
        this.totalCharge = totalCharge;
    }
}
