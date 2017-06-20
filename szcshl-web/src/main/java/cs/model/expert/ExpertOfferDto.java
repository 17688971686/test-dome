package cs.model.expert;

import com.alibaba.fastjson.annotation.JSONField;
import cs.model.BaseDto;

import java.util.Date;


/**
 * Description: 专家聘书 页面数据模型
 * author: ldm
 * Date: 2017-6-19 19:13:35
 */
public class ExpertOfferDto extends BaseDto {

    private String id;
    private String isSendCcie;
    @JSONField(format = "yyyy-MM-dd")
    private Date sendCcieDate;
    private String period;
    @JSONField(format = "yyyy-MM-dd")
    private Date periodDate;
    private String isInvalid;
    private String descInfo;
    private ExpertDto expertDto;
    private String expertId;        //专家ID

    public ExpertOfferDto() {
    }

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

    public ExpertDto getExpertDto() {
        return expertDto;
    }

    public void setExpertDto(ExpertDto expertDto) {
        this.expertDto = expertDto;
    }

    public String getExpertId() {
        return expertId;
    }

    public void setExpertId(String expertId) {
        this.expertId = expertId;
    }
}