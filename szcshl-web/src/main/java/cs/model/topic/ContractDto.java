package cs.model.topic;

import cs.model.BaseDto;
import java.math.BigDecimal;



/**
 * Description: 合同 页面数据模型
 * author: zsl
 * Date: 2017-9-4 15:40:48
 */
public class ContractDto extends BaseDto {
    private String contractId;

    private String cooperator;

    private BigDecimal entrustValue;

    private String purchaseType;

    private String contractPerson;

    private String contractTel;

    private TopicInfoDto topicInfoDto;

    private String topicId;

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

    public TopicInfoDto getTopicInfoDto() {
        return topicInfoDto;
    }

    public void setTopicInfoDto(TopicInfoDto topicInfoDto) {
        this.topicInfoDto = topicInfoDto;
    }

    public String getTopicId() {
        return topicId;
    }

    public void setTopicId(String topicId) {
        this.topicId = topicId;
    }
}