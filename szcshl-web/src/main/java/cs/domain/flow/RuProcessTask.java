package cs.domain.flow;

import javax.persistence.*;
import java.util.Date;

/**
 * 在办任务列表 on 2017/7/6 0006.
 * (对应视图 V_RU_PROCESS_TASK)
 */
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Table(name="V_RU_PROCESS_TASK")
public class RuProcessTask {

    /**
     * 任务ID
     */
    @Id
    private String taskId;

    /**
     * 环节名称
     */
    @Column
    private String nodeName;

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
    private Date createTime;

    /**
     * 指定人
     */
    @Column
    private String assignee;

    /**
     * 待选用户名称（多个用“，”相隔）
     */
    @Column
    private String userName;

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
    private String processInstanceId;

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
    private String isConcurrent;
    /**
     * 流程状态（1：正在进行，2：停止）
     */
    @Column
    private String processState;

    /**********   以下是业务数据  **********/
    @Column
    private String projectName;

    @Column
    private String reviewStage;

    @Column
    private String lightState;


    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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

    public String getProcessInstanceId() {
        return processInstanceId;
    }

    public void setProcessInstanceId(String processInstanceId) {
        this.processInstanceId = processInstanceId;
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

    public String getProcessState() {
        return processState;
    }

    public void setProcessState(String processState) {
        this.processState = processState;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getReviewStage() {
        return reviewStage;
    }

    public void setReviewStage(String reviewStage) {
        this.reviewStage = reviewStage;
    }

    public String getLightState() {
        return lightState;
    }

    public void setLightState(String lightState) {
        this.lightState = lightState;
    }

    public String getIsConcurrent() {
        return isConcurrent;
    }

    public void setIsConcurrent(String isConcurrent) {
        this.isConcurrent = isConcurrent;
    }
}