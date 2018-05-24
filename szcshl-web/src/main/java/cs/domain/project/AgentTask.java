package cs.domain.project;

import org.hibernate.annotations.DynamicUpdate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * 任务待办历史记录
 * Created by ldm on 2018/5/21.
 */
@Entity
@Table(name = "cs_agent_his")
@DynamicUpdate(true)
public class AgentTask {

    /**
     * ID
     */
    @Id
    @Column(columnDefinition = "VARCHAR(64)")
    private String agentId;

    /**
     * 流程名称
     */
    @Column(columnDefinition = "VARCHAR(256)")
    private String flowName;
    /**
     * 任务ID
     */
    @Column(columnDefinition = "VARCHAR(64)")
    private String taskId;

    /**
     * 原先用户ID
     */
    @Column(columnDefinition = "VARCHAR(64)")
    private String userId;

    /**
     * 原先用户人姓名
     */
    @Column(columnDefinition = "VARCHAR(64)")
    private String userName;

    /**
     * 代理人用户ID
     */
    @Column(columnDefinition = "VARCHAR(64)")
    private String agentUserId;

    /**
     * 代理人用户姓名
     */
    @Column(columnDefinition = "VARCHAR(64)")
    private String agentUserName;

    /**
     * 任务转接时间
     */
    @Column(columnDefinition = "Date")
    private Date transDate;

    /**
     * 待办环节KEY
     */
    @Column(columnDefinition = "VARCHAR(255)")
    private String nodeKey;

    /**
     * 待办环节NAME
     */
    @Column(columnDefinition = "VARCHAR(255)")
    private String nodeNameValue;

    public String getAgentId() {
        return agentId;
    }

    public void setAgentId(String agentId) {
        this.agentId = agentId;
    }

    public String getFlowName() {
        return flowName;
    }

    public void setFlowName(String flowName) {
        this.flowName = flowName;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAgentUserId() {
        return agentUserId;
    }

    public void setAgentUserId(String agentUserId) {
        this.agentUserId = agentUserId;
    }

    public String getAgentUserName() {
        return agentUserName;
    }

    public void setAgentUserName(String agentUserName) {
        this.agentUserName = agentUserName;
    }

    public Date getTransDate() {
        return transDate;
    }

    public void setTransDate(Date transDate) {
        this.transDate = transDate;
    }

    public String getNodeKey() {
        return nodeKey;
    }

    public void setNodeKey(String nodeKey) {
        this.nodeKey = nodeKey;
    }

    public String getNodeNameValue() {
        return nodeNameValue;
    }

    public void setNodeNameValue(String nodeNameValue) {
        this.nodeNameValue = nodeNameValue;
    }

    public AgentTask(String agentId, String flowName, String taskId, String userId, String userName, String agentUserId, String agentUserName, Date transDate) {
        this.agentId = agentId;
        this.flowName = flowName;
        this.taskId = taskId;
        this.userId = userId;
        this.userName = userName;
        this.agentUserId = agentUserId;
        this.agentUserName = agentUserName;
        this.transDate = transDate;
    }

    public AgentTask() {
    }
}
