package cs.domain.topic;

import cs.domain.DomainBase;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * 课题研究基本信息
 * Created by ldm on 2017/9/4 0004.
 */
@Entity
@Table(name="cs_topic_info")
@DynamicUpdate(true)
public class TopicInfo extends DomainBase {

    @Id
    @GeneratedValue(generator= "topicInfoGenerator")
    @GenericGenerator(name= "topicInfoGenerator",strategy = "uuid")
    private String id;

    /**
     * 课题名称
     */
    @Column(columnDefinition="VARCHAR(128)")
    private String topicName;

    /**
     * 课题合作单位
     */
    @Column(columnDefinition="VARCHAR(256)")
    private String cooperator;

    /**
     * 发改委立项课题（9:是，0：否，默认为0）
     */
    @Column(columnDefinition="VARCHAR(2)")
    private String fgwlx;

    /**
     * 状态
     */
    @Column(columnDefinition="VARCHAR(2)")
    private String state;

    /**
     * 课题负责人IDs
     */
    @Column(columnDefinition="VARCHAR(1024)")
    private String prinIds;

    /**
     * 课题负责人名称
     */
    @Column(columnDefinition="VARCHAR(256)")
    private String prinNames;

    /**
     * 工作方案
     */
    @OneToOne(mappedBy = "topicInfo", fetch = FetchType.LAZY,orphanRemoval=true)
    private WorkPlan workPlan;

    /**
     * 归档
     */
    @OneToOne(mappedBy = "topicInfo", fetch = FetchType.LAZY,orphanRemoval=true)
    private Filing filing;

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

    public String getCooperator() {
        return cooperator;
    }

    public void setCooperator(String cooperator) {
        this.cooperator = cooperator;
    }

    public WorkPlan getWorkPlan() {
        return workPlan;
    }

    public void setWorkPlan(WorkPlan workPlan) {
        this.workPlan = workPlan;
    }

    public Filing getFiling() {
        return filing;
    }

    public void setFiling(Filing filing) {
        this.filing = filing;
    }

    public String getFgwlx() {
        return fgwlx;
    }

    public void setFgwlx(String fgwlx) {
        this.fgwlx = fgwlx;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPrinIds() {
        return prinIds;
    }

    public void setPrinIds(String prinIds) {
        this.prinIds = prinIds;
    }

    public String getPrinNames() {
        return prinNames;
    }

    public void setPrinNames(String prinNames) {
        this.prinNames = prinNames;
    }
}