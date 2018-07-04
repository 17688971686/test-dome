package cs.model.topic;

import com.alibaba.fastjson.annotation.JSONField;
import cs.model.BaseDto;
import javax.persistence.Column;
import java.util.Date;


/**
 * Description: 课题研究 页面数据模型
 * author: zsl
 * Date: 2018-6-22 15:04:55
 */
public class TopicMaintainDto extends BaseDto {

    private String id;

    /**
     * 课题名称
     */
    private String topicName;

    /**
     * 业务类型
     */
    private String businessType;

    /**
     * 课题结题时间
     */
    @JSONField(format = "yyyy-MM-dd")
    private Date endTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

}