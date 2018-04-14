package cs.domain.flow;

/**
 * Created by ldm
 */

import com.alibaba.fastjson.annotation.JSONField;

import javax.persistence.*;
import java.util.Date;

/**
 * 已办任务处理信息表
 */
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Table(name="V_HI_PROCESS_TASK")
public class HiProcessTask {

    /**
     * 历史节点ID
     */
    @Id
    private String hctId;
    /**
     * 任务ID
     */
    @Column
    private String taskId;
    /**
     * 流程实例ID
     */
    @Column
    private String procInstId;
    /**
     * 流程定义ID
     */
    @Column
    private String procDefId;
    /**
     * 环节名称
     */
    @Column
    private String nodeNameValue;

    /**
     * 环节名称Key
     */
    @Column
    private String nodeKeyValue;
    /**
     * 开始日期
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column
    private Date startTime;
    /**
     * 结束日期
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column
    private Date endTime;
    /**
     * 处理时长
     */
    @Column(columnDefinition = "NUMBER")
    private Long duration;

    /**
     * 处理时长（中文）
     */
    @Column
    private String durationStr;
    /**
     * 签收人
     */
    @Column
    private String assignee;
    /**
     * 用户的显示名
     */
    @Column
    private String displayName;

    /**
     * 待选用户名称（多个用“，”相隔）
     */
    @Column
    private String assigneeList;

    /**
     * 处理信息
     */
    @Column
    private String message;


    public String getHctId() {
        return hctId;
    }

    public void setHctId(String hctId) {
        this.hctId = hctId;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getProcInstId() {
        return procInstId;
    }

    public void setProcInstId(String procInstId) {
        this.procInstId = procInstId;
    }

    public String getProcDefId() {
        return procDefId;
    }

    public void setProcDefId(String procDefId) {
        this.procDefId = procDefId;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    public String getAssignee() {
        return assignee;
    }

    public void setAssignee(String assignee) {
        this.assignee = assignee;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getAssigneeList() {
        return assigneeList;
    }

    public void setAssigneeList(String assigneeList) {
        this.assigneeList = assigneeList;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDurationStr() {
        return durationStr;
    }

    public void setDurationStr(String durationStr) {
        this.durationStr = durationStr;
    }

    public String getNodeNameValue() {
        return nodeNameValue;
    }

    public void setNodeNameValue(String nodeNameValue) {
        this.nodeNameValue = nodeNameValue;
    }

    public String getNodeKeyValue() {
        return nodeKeyValue;
    }

    public void setNodeKeyValue(String nodeKeyValue) {
        this.nodeKeyValue = nodeKeyValue;
    }
}
