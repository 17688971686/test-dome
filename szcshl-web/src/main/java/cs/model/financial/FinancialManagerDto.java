package cs.model.financial;

import com.alibaba.fastjson.annotation.JSONField;
import cs.model.BaseDto;

import java.math.BigDecimal;
import java.util.Date;


/**
 * Description: 财务管理 页面数据模型
 * author: sjy
 * Date: 2017-8-7 11:49:16
 */
public class FinancialManagerDto extends BaseDto {

    private String id;
    private String chargeName;
    private Integer charge;
	private Integer stageCount;
	@JSONField(format = "yyyy-MM-dd")
	private Date paymentData;
	private String projectName;
	private String assistBuiltcompanyName;
	private BigDecimal assissCost;
	private String signid;
	private String chargeType;//费用类型
    private String remarke;
	private String businessId;

    public FinancialManagerDto() {
    }
   
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    public String getChargeName() {
        return chargeName;
    }

  
    public Integer getCharge() {
		return charge;
	}

	public void setCharge(Integer charge) {
		this.charge = charge;
	}

	public void setChargeName(String chargeName) {
		this.chargeName = chargeName;
	}

	public String getRemarke() {
        return remarke;
    }

    public void setRemarke(String remarke) {
        this.remarke = remarke;
    }

	public Integer getStageCount() {
		return stageCount;
	}

	public void setStageCount(Integer stageCount) {
		this.stageCount = stageCount;
	}

	

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getChargeType() {
		return chargeType;
	}

	public void setChargeType(String chargeType) {
		this.chargeType = chargeType;
	}

	public Date getPaymentData() {
		return paymentData;
	}

	public void setPaymentData(Date paymentData) {
		this.paymentData = paymentData;
	}

	public String getSignid() {
		return signid;
	}

	public void setSignid(String signid) {
		this.signid = signid;
	}

	public String getAssistBuiltcompanyName() {
		return assistBuiltcompanyName;
	}

	public void setAssistBuiltcompanyName(String assistBuiltcompanyName) {
		this.assistBuiltcompanyName = assistBuiltcompanyName;
	}

	public BigDecimal getAssissCost() {
		return assissCost;
	}

	public void setAssissCost(BigDecimal assissCost) {
		this.assissCost = assissCost;
	}

	public String getBusinessId() {
		return businessId;
	}

	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}
}