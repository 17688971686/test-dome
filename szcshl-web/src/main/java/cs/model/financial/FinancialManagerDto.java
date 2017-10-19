package cs.model.financial;

import com.alibaba.fastjson.annotation.JSONField;
import cs.model.BaseDto;

import javax.persistence.Id;
import java.math.BigDecimal;
import java.util.Date;


/**
 * Description: 财务管理 页面数据模型
 * author: sjy
 * Date: 2017-8-7 11:49:16
 */
public class FinancialManagerDto extends BaseDto {

	@Id
	private String id;

	//费用名称
	private String chargeName;

	//费用
	private BigDecimal charge;

	//费用总和
	private BigDecimal stageCount;

	//付款时间
	@JSONField(format = "yyyy-MM-dd")
	private Date paymentData;

	//业务ID
	private String businessId;

	//费用类型：协审费用录入8,评审费用录入9
	private String chargeType;

	//备注
	private String remarke;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getChargeName() {
        return chargeName;
    }

    public void setChargeName(String chargeName) {
        this.chargeName = chargeName;
    }

    public BigDecimal getCharge() {
        return charge;
    }

    public void setCharge(BigDecimal charge) {
        this.charge = charge;
    }

    public BigDecimal getStageCount() {
        return stageCount;
    }

    public void setStageCount(BigDecimal stageCount) {
        this.stageCount = stageCount;
    }

    public Date getPaymentData() {
        return paymentData;
    }

    public void setPaymentData(Date paymentData) {
        this.paymentData = paymentData;
    }

    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    public String getChargeType() {
        return chargeType;
    }

    public void setChargeType(String chargeType) {
        this.chargeType = chargeType;
    }

    public String getRemarke() {
        return remarke;
    }

    public void setRemarke(String remarke) {
        this.remarke = remarke;
    }
}