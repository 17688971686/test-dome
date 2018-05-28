package cs.model.project;

import com.alibaba.fastjson.annotation.JSONField;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 用于封装前日起往后的5个工作日的调研和会议统计信息
 *
 * @author zsl
 */
public class ProMeetDto{

    @JSONField(format = "yyyy-MM-dd")
    private Date proMeetDate;

    private String proName; //项目名称

    private String rbName;//会议名称

    private String addressName;//会议地点

    private BigDecimal innerSeq;


    public Date getProMeetDate() {
        return proMeetDate;
    }

    public void setProMeetDate(Date proMeetDate) {
        this.proMeetDate = proMeetDate;
    }

    public String getProName() {
        return proName;
    }

    public void setProName(String proName) {
        this.proName = proName;
    }

    public String getRbName() {
        return rbName;
    }

    public void setRbName(String rbName) {
        this.rbName = rbName;
    }

    public String getAddressName() {
        return addressName;
    }

    public void setAddressName(String addressName) {
        this.addressName = addressName;
    }

    public BigDecimal getInnerSeq() {
        return innerSeq;
    }

    public void setInnerSeq(BigDecimal innerSeq) {
        this.innerSeq = innerSeq;
    }
}
