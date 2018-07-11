package cs.domain.flow;

import com.alibaba.fastjson.annotation.JSONField;
import cs.model.project.SignDto;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

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
     * 环节定义名
     */
    @Column
    private String nodeDefineKey;

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
    @Column
    private String isConcurrent;

    /**
     * 流程状态（1：正在进行，2：停止）
     */
    @Column
    private String processState;

    /**********   以下是业务数据  **********/
    /**
     * 签收人（流程发起人ID）
     */
    @Column
    private String createdBy;

    /**
     * 送件人签名，（流程发起人）
     */
    @Column
    private String sendusersign;

    @Column
    private String projectName;

    @Column
    private String reviewStage;

    @Column
    private Integer signprocessState;

    @Column
    private String lightState;
    
    @Column
    private String ispause;

    @Temporal(TemporalType.TIMESTAMP)
    @Column
    @JSONField(format = "yyyy-MM-dd")
    private Date preSignDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column
    @JSONField(format = "yyyy-MM-dd")
    private Date signDate;

    @Column
    private Float surplusDays;

    @Column
    private String signState;

    /**
     * 评审方式【9表示合并评审主项目，0表示合并评审次项目，空表示单个评审】
     */
    @Column
    private String reviewType;

    //主办部门ID
    @Column
    private String mOrgId;
    @Column
    private String mOrgName;
    //协办部门
    @Column
    private String aOrgId;
    @Column
    private String aOrgName;
    //第一负责人ID
    @Column
    private String mainUserId;
    @Column
    private String mainUserName;
    //其它负责人
    @Column
    private String aUserId;
    @Column
    private String aUserName;
    //所有负责人名称
    @Column
    private String allPriUser;

    /**
     * 是否提前介入
     */
    @Column
    private String isAdvanced;

    /**
     * 是否代办
     */
    @Column
    private String isAgent;
    /**
     * 预发文日期
     */
    @Column(columnDefinition = "DATE")
    @JSONField(format = "yyyy-MM-dd")
    private Date expectdispatchdate;

    /**
     * 是否合并发文(1表示单个发文，2表示合并发文)
     */
    @Column
    private String mergeDis;

    /**
     * 是否合并发文主项目
     */
    @Column
    private String mergeDisMain;

    /**
     * 合并评审项目
     */
    //这是此注解后该属性不会数据持久化
    @Transient
    private List<SignDto> reviewSignDtoList;

    public String getMainUserId() {
        return mainUserId;
    }

    public void setMainUserId(String mainUserId) {
        this.mainUserId = mainUserId;
    }

    public String getIspause() {
		return ispause;
	}

	public void setIspause(String ispause) {
		this.ispause = ispause;
	}

	public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getTaskState() {
        return taskState;
    }

    public String getNodeNameValue() {
        return nodeNameValue;
    }

    public void setNodeNameValue(String nodeNameValue) {
        this.nodeNameValue = nodeNameValue;
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

    public Date getSignDate() {
        return signDate;
    }

    public void setSignDate(Date signDate) {
        this.signDate = signDate;
    }

    public Float getSurplusDays() {
        return surplusDays;
    }

    public void setSurplusDays(Float surplusDays) {
        this.surplusDays = surplusDays;
    }

    public String getSignState() {
        return signState;
    }

    public void setSignState(String signState) {
        this.signState = signState;
    }

    public Date getPreSignDate() {
        return preSignDate;
    }

    public void setPreSignDate(Date preSignDate) {
        this.preSignDate = preSignDate;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getReviewType() {
        return reviewType;
    }

    public void setReviewType(String reviewType) {
        this.reviewType = reviewType;
    }

    public List<SignDto> getReviewSignDtoList() {
        return reviewSignDtoList;
    }

    public void setReviewSignDtoList(List<SignDto> reviewSignDtoList) {
        this.reviewSignDtoList = reviewSignDtoList;
    }

    public String getNodeDefineKey() {
        return nodeDefineKey;
    }

    public void setNodeDefineKey(String nodeDefineKey) {
        this.nodeDefineKey = nodeDefineKey;
    }

    public Integer getSignprocessState() {
        return signprocessState;
    }

    public void setSignprocessState(Integer signprocessState) {
        this.signprocessState = signprocessState;
    }

    public String getmOrgId() {
        return mOrgId;
    }

    public void setmOrgId(String mOrgId) {
        this.mOrgId = mOrgId;
    }

    public String getmOrgName() {
        return mOrgName;
    }

    public void setmOrgName(String mOrgName) {
        this.mOrgName = mOrgName;
    }

    public String getaOrgId() {
        return aOrgId;
    }

    public void setaOrgId(String aOrgId) {
        this.aOrgId = aOrgId;
    }

    public String getaOrgName() {
        return aOrgName;
    }

    public void setaOrgName(String aOrgName) {
        this.aOrgName = aOrgName;
    }

    public String getMainUserName() {
        return mainUserName;
    }

    public void setMainUserName(String mainUserName) {
        this.mainUserName = mainUserName;
    }

    public String getaUserId() {
        return aUserId;
    }

    public void setaUserId(String aUserId) {
        this.aUserId = aUserId;
    }

    public String getaUserName() {
        return aUserName;
    }

    public void setaUserName(String aUserName) {
        this.aUserName = aUserName;
    }

    public String getAllPriUser() {
        return allPriUser;
    }

    public void setAllPriUser(String allPriUser) {
        this.allPriUser = allPriUser;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getSendusersign() {
        return sendusersign;
    }

    public void setSendusersign(String sendusersign) {
        this.sendusersign = sendusersign;
    }

    public Date getExpectdispatchdate() {
        return expectdispatchdate;
    }

    public void setExpectdispatchdate(Date expectdispatchdate) {
        this.expectdispatchdate = expectdispatchdate;
    }

    public String getIsAdvanced() {
        return isAdvanced;
    }

    public void setIsAdvanced(String isAdvanced) {
        this.isAdvanced = isAdvanced;
    }

    public String getIsAgent() {
        return isAgent;
    }

    public void setIsAgent(String isAgent) {
        this.isAgent = isAgent;
    }

    public String getMergeDis() {
        return mergeDis;
    }

    public void setMergeDis(String mergeDis) {
        this.mergeDis = mergeDis;
    }

    public String getMergeDisMain() {
        return mergeDisMain;
    }

    public void setMergeDisMain(String mergeDisMain) {
        this.mergeDisMain = mergeDisMain;
    }
}