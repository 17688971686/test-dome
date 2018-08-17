package cs.domain.topic;

import com.alibaba.fastjson.annotation.JSONField;
import cs.domain.DomainBase;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * 课题信息维护
 * Created by zsl on 2018/6/22 .
 */
@Entity
@Table(name="cs_topic_maintain")
@DynamicUpdate(true)
public class TopicMaintain extends DomainBase {

    @Id
    private String id;

    /**
     * 课题名称
     */
    @Column(columnDefinition="VARCHAR(128)")
    private String topicName;

    /**
     * 业务类型
     */
    @Column(columnDefinition="VARCHAR(1)")
    private String businessType;

    /**
     * 课题结题时间
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column
    @JSONField(format = "yyyy-MM-dd")
    private Date endTime;

    /**
     * 用户Id
     */
    @Column(columnDefinition = "varchar(64)")
    private String userId;

    private String topicId;

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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTopicId() {
        return topicId;
    }

    public void setTopicId(String topicId) {
        this.topicId = topicId;
    }
}
