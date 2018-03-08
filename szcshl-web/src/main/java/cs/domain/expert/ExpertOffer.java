package cs.domain.expert;

import cs.domain.DomainBase;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * 专家聘书
 */
@Entity
@Table(name = "cs_expert_offer")
@DynamicUpdate(true)
public class ExpertOffer extends DomainBase {

    @Id
    @GeneratedValue(generator= "OfferGenerator")
    @GenericGenerator(name= "OfferGenerator",strategy = "org.hibernate.id.UUIDGenerator")
    private String id;

    /**
     * 是否发证(9:是，0：否)
     */
    @Column(columnDefinition="VARCHAR(2)")
    private String isSendCcie;

    /**
     * 发证日期
     */
    @Temporal(TemporalType.DATE)
    @Column
    private Date sendCcieDate;

    /**
     * 有效期
     */
    @Column(columnDefinition="VARCHAR(10)")
    private String period;

    /**
     * 有效期至
     */
    @Temporal(TemporalType.DATE)
    @Column
    private Date periodDate;

    /**
     * 是否作废(9:是，0：否)
     */
    @Column(columnDefinition="VARCHAR(2)")
    private String isInvalid;

    /**
     * 备注信息
     */
    @Column(columnDefinition="VARCHAR(512)")
    private String descInfo;

    /**
     * 序号
     */
    @Column(columnDefinition = "INTEGER")
    private Integer seqNum;

    //抽取专家关系（多对一）
    @ManyToOne
    @JoinColumn(name = "expertId")
    private Expert expert;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIsSendCcie() {
        return isSendCcie;
    }

    public void setIsSendCcie(String isSendCcie) {
        this.isSendCcie = isSendCcie;
    }

    public Date getSendCcieDate() {
        return sendCcieDate;
    }

    public void setSendCcieDate(Date sendCcieDate) {
        this.sendCcieDate = sendCcieDate;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public Date getPeriodDate() {
        return periodDate;
    }

    public void setPeriodDate(Date periodDate) {
        this.periodDate = periodDate;
    }

    public String getIsInvalid() {
        return isInvalid;
    }

    public void setIsInvalid(String isInvalid) {
        this.isInvalid = isInvalid;
    }

    public String getDescInfo() {
        return descInfo;
    }

    public void setDescInfo(String descInfo) {
        this.descInfo = descInfo;
    }

    public Expert getExpert() {
        return expert;
    }

    public void setExpert(Expert expert) {
        this.expert = expert;
    }

    public Integer getSeqNum() {
        return seqNum;
    }

    public void setSeqNum(Integer seqNum) {
        this.seqNum = seqNum;
    }
}
