package cs.domain.financial;


import cs.domain.DomainBase;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import java.math.BigDecimal;
import java.util.Date;


/**
 * Description: 财务管理 页面数据模型
 * author: sjy
 * Date: 2017-8-7 11:49:16
 */
@Entity
@Table(name = "cs_financial_manager")
public class FinancialManager extends DomainBase {

    @Id
    private String id;

    //费用名称
    @Column(columnDefinition = "varchar(100)")
    private String chargeName;

    //费用
    @Column(columnDefinition = "NUMBER")
    private BigDecimal charge;

    //费用总和
    @Column(columnDefinition = "NUMBER")
    private BigDecimal stageCount;

    //付款时间
    @Column(columnDefinition = "date")
    private Date paymentData;

    //业务ID
    @Column(columnDefinition = "varchar(50)")
    private String businessId;

    //费用类型：协审费用录入8,评审费用录入9
    @Column(columnDefinition = "varchar(20)")
    private String chargeType;

    //备注
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

    public String getRemarke() {
        return remarke;
    }

    public void setRemarke(String remarke) {
        this.remarke = remarke;
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

    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
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
}