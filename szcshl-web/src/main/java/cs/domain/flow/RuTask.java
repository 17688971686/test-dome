package cs.domain.flow;

import com.alibaba.fastjson.annotation.JSONField;

import javax.persistence.*;
import java.util.Date;

/**
 * 系统待办任务（除项目签收流程外）
 * Created by ldm on 2017/9/5 0005.
 * (对应视图 V_RU_TASK)
 */
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Table(name="V_RU_TASK")
public class RuTask {
    /**
     * 任务ID
     */
    @Id
    private String taskId;
    /**
     * 环节KEY
     */
    @Column
    private String nodeKey;

    /**
     * 环节名称
     */
    @Column
    private String nodeNameValue;

    /**
     * 任务状态
     */
    @Column
    private String taskState;

    /**
     * 创建日期
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column
    @JSONField(format = "yyyy-MM-dd")
    private Date createTime;

    /**
     * 指定人
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
     * 流程定义名称
     */
    @Column
    private String processName;

    /**
     * 流程定义key
     */
    @Column
    private String processKey;

    /**
     * 流程实例ID
     */
    @Column
    private String instanceId;

    /**
     * 流程实例名称
     */
    @Column
    private String instanceName;

    /**
     * 业务ID
     */
    @Column
    private String businessKey;

    /**
     * 任务存活状态
     */
    @Column
    private String isActive;

    /**
     * 任务是否并行
     */
    @Column
    private String isConcurrent;
    /**
     * 流程状态（1：正在进行，2：停止）
     */
    @Column
    private String processState;


    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getNodeNameValue() {
        return nodeNameValue;
    }

    public void setNodeNameValue(String nodeNameValue) {
        this.nodeNameValue = nodeNameValue;
    }

    public String getTaskState() {
        return taskState;
    }

    public void setTaskState(String taskState) {
        this.taskState = taskState;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
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

    public String getProcessName() {
        return processName;
    }

    public void setProcessName(String processName) {
        this.processName = processName;
    }

    public String getProcessKey() {
        return processKey;
    }

    public void setProcessKey(String processKey) {
        this.processKey = processKey;
    }

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    public String getInstanceName() {
        return instanceName;
    }

    public void setInstanceName(String instanceName) {
        this.instanceName = instanceName;
    }

    public String getBusinessKey() {
        return businessKey;
    }

    public void setBusinessKey(String businessKey) {
        this.businessKey = businessKey;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

    public String getIsConcurrent() {
        return isConcurrent;
    }

    public void setIsConcurrent(String isConcurrent) {
        this.isConcurrent = isConcurrent;
    }

    public String getProcessState() {
        return processState;
    }

    public String getNodeKey() {
        return nodeKey;
    }

    public void setNodeKey(String nodeKey) {
        this.nodeKey = nodeKey;
    }

    public void setProcessState(String processState) {
        this.processState = processState;
    }
}
