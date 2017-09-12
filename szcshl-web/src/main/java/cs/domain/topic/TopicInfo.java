package cs.domain.topic;

import com.alibaba.fastjson.annotation.JSONField;
import cs.domain.DomainBase;
import org.hibernate.annotations.DynamicUpdate;
import javax.persistence.*;
import java.util.Date;

/**
 * 课题研究基本信息
 * Created by ldm on 2017/9/4 0004.
 */
@Entity
@Table(name="cs_topic_info")
@DynamicUpdate(true)
public class TopicInfo extends DomainBase {

    @Id
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
     * 是否报发改委审批（9:是，0：否，默认为0）
     */
    @Column(columnDefinition="VARCHAR(2)")
    private String sendFgw;

    /**
     * 申报部门
     */
    @Column(columnDefinition="VARCHAR(64)")
    private String orgId;

    /**
     * 申报部门名称
     */
    @Column(columnDefinition="VARCHAR(255)")
    private String orgName;

    /**
     * 是否完成成果鉴定会方案（9:是，0：否，默认为0）
     */
    @Column(columnDefinition="VARCHAR(2)")
    private String isFinishPlan;

    /**
     * 是否完成归档（9:是，0：否，默认为0）
     */
    @Column(columnDefinition="VARCHAR(2)")
    private String isFinishFiling;

    /**
     * 流程状态
     */
    @Column(columnDefinition="VARCHAR(2)")
    private String state;

    /**
     * 流程实例ID
     */
    @Column(columnDefinition="VARCHAR(64)")
    private String processInstanceId;

    /**
     * 备注信息
     */
    @Column(columnDefinition="VARCHAR(512)")
    private String remark;

    /**
     * 课题结题时间
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column
    @JSONField(format = "yyyy-MM-dd")
    private Date endTime;

    /**
     * 课题序号
     */
    @Column(columnDefinition="INTEGER")
    private Integer topicSeq;

    /**
     * 课题代码(课题代码2017KT001，归档编号2016KD17001)
     */
    @Column(columnDefinition="VARCHAR(16)")
    private String topicCode;
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

    public String getProcessInstanceId() {
        return processInstanceId;
    }

    public void setProcessInstanceId(String processInstanceId) {
        this.processInstanceId = processInstanceId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getSendFgw() {
        return sendFgw;
    }

    public void setSendFgw(String sendFgw) {
        this.sendFgw = sendFgw;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getIsFinishPlan() {
        return isFinishPlan;
    }

    public void setIsFinishPlan(String isFinishPlan) {
        this.isFinishPlan = isFinishPlan;
    }

    public String getIsFinishFiling() {
        return isFinishFiling;
    }

    public void setIsFinishFiling(String isFinishFiling) {
        this.isFinishFiling = isFinishFiling;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Integer getTopicSeq() {
        return topicSeq;
    }

    public void setTopicSeq(Integer topicSeq) {
        this.topicSeq = topicSeq;
    }

    public String getTopicCode() {
        return topicCode;
    }

    public void setTopicCode(String topicCode) {
        this.topicCode = topicCode;
    }
}
