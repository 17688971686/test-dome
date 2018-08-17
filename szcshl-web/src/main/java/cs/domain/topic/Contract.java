package cs.domain.topic;

import cs.domain.DomainBase;
import org.hibernate.annotations.DynamicUpdate;
import javax.persistence.*;
import java.math.BigDecimal;


/**
 * 合同基本信息
 * Created by ldm on 2017/9/4 0004.
 */
@Entity
@Table(name="cs_topic_contract")
@DynamicUpdate(true)
public class Contract extends DomainBase {

    @Id
    private String contractId;

    /**
     * 合作单位
     */
    @Column(columnDefinition="VARCHAR(256)")
    private String cooperator;

    /**
     *  委托金额
     */
    @Column(columnDefinition = "NUMBER")
    private BigDecimal entrustValue;

    /**
     * 采购方式
     */
    @Column(columnDefinition="VARCHAR(1)")
    private String purchaseType;

    /**
     *    联系人
     */

    @Column(columnDefinition = "VARCHAR(32)")
    private String contractPerson;


    /**
     *   联系电话
     */

    @Column(columnDefinition = "VARCHAR(128)")
    private String contractTel;

    /**
     * 课题信息
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "topicId")
    private TopicInfo topicInfo;

    public String getContractId() {
        return contractId;
    }

    public void setContractId(String contractId) {
        this.contractId = contractId;
    }

    public String getCooperator() {
        return cooperator;
    }

    public void setCooperator(String cooperator) {
        this.cooperator = cooperator;
    }

    public BigDecimal getEntrustValue() {
        return entrustValue;
    }

    public void setEntrustValue(BigDecimal entrustValue) {
        this.entrustValue = entrustValue;
    }

    public String getPurchaseType() {
        return purchaseType;
    }

    public void setPurchaseType(String purchaseType) {
        this.purchaseType = purchaseType;
    }

    public String getContractPerson() {
        return contractPerson;
    }

    public void setContractPerson(String contractPerson) {
        this.contractPerson = contractPerson;
    }

    public String getContractTel() {
        return contractTel;
    }

    public void setContractTel(String contractTel) {
        this.contractTel = contractTel;
    }

    public TopicInfo getTopicInfo() {
        return topicInfo;
    }

    public void setTopicInfo(TopicInfo topicInfo) {
        this.topicInfo = topicInfo;
    }
}
