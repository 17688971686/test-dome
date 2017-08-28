package cs.domain.financial;


import cs.domain.DomainBase;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;


/**
 * Description: 财务管理 页面数据模型
 * author: sjy
 * Date: 2017-8-7 11:49:16
 */
@Entity
@Table(name ="cs_financial_manager")
public class FinancialManager extends DomainBase {

	@Id
    private String id;
	@Column(columnDefinition = "varchar(100)")
    private String chargeName;
	
	@Column(columnDefinition = "INTEGER")
    private Integer charge;
	
	@Column(columnDefinition = "INTEGER")
	private Integer stageCount;
	
	@Column(columnDefinition="date")
	private Date paymentData;
	
	@Column(columnDefinition = "varchar(100)")
	private String projectName;
	
	@Column(columnDefinition = "varchar(50)")
	private String signid;
	
	@Column(columnDefinition = "varchar(20)")
	private String chargeType;//费用类型
	
	@Column(columnDefinition = "varchar(200)")
    private String remarke;

    public FinancialManager() {
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

    public void setChargeName(String chargeName) {
        this.chargeName = chargeName;
    }
  
    
    public Integer getCharge() {
		return charge;
	}

	public void setCharge(Integer charge) {
		this.charge = charge;
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
	
    

}