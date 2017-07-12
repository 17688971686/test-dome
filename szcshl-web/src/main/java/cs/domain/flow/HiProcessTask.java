package cs.domain.flow;

/**
 * Created by ldm
 */

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
    private String nodeName;
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
     * 待办人
     */
    @Column
    private String userName;
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

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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
}
