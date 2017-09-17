package cs.model.topic;

import com.alibaba.fastjson.annotation.JSONField;
import cs.domain.flow.FlowPrincipal;
import cs.model.BaseDto;
import cs.model.expert.ExpertReviewDto;

import javax.persistence.Column;
import java.util.Date;
import java.util.List;


/**
 * Description: 课题研究 页面数据模型
 * author: ldm
 * Date: 2017-9-4 15:04:55
 */
public class TopicInfoDto extends BaseDto {

    private String id;
    /**
     * 课题名称
     */
    private String topicName;
    /**
     * 课题合作单位
     */
    private String cooperator;
    /**
     * 发改委立项课题（9:是，0：否，默认为0）
     */
    private String fgwlx;
    /**
     * 是否报发改委审批（9:是，0：否，默认为0）
     */
    private String sendFgw;

    /**
     * 申报部门
     */
    private String orgId;

    /**
     * 申报部门名称
     */
    private String orgName;

    /**
     * 是否完成成果鉴定会方案（9:是，0：否，默认为0）
     */
    private String isFinishPlan;

    /**
     * 是否完成归档（9:是，0：否，默认为0）
     */
    private String isFinishFiling;
    /**
     * 状态
     */
    private String state;

    /**
     * 备注信息
     */
    private String remark;

    /**
     * 流程实例
     */
    private String processInstanceId;

    /**
     * 任务ID
     */
    private String taskId;

    /**
     * 课题结题时间
     */
    @JSONField(format = "yyyy-MM-dd")
    private Date endTime;

    /**
     * 课题序号
     */
    private Integer topicSeq;

    /**
     * 课题代码
     */
    private String topicCode;

    /**
     * 工作方案
     */
    private WorkPlanDto workPlanDto;

    /**
     * 归档
     */
    private FilingDto filingDto;

    /**
     * 专家评审方案信息
     */
    private ExpertReviewDto expertReviewDto;

    /*******************  以下参数只是为了方便显示，并不是数据表字段 ********************/
    /**
     * 主要负责人ID
     */
    private String mainPrinUserId;

    /**
     * 项目负责人ID（多个）
     */
    private String prinUserIds;
    /**
     * 流程负责人
     */
    private List<FlowPrincipal> flowPrincipal;

    public TopicInfoDto() {
    }

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


    public WorkPlanDto getWorkPlanDto() {
        return workPlanDto;
    }

    public void setWorkPlanDto(WorkPlanDto workPlanDto) {
        this.workPlanDto = workPlanDto;
    }

    public FilingDto getFilingDto() {
        return filingDto;
    }

    public void setFilingDto(FilingDto filingDto) {
        this.filingDto = filingDto;
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

    public List<FlowPrincipal> getFlowPrincipal() {
        return flowPrincipal;
    }

    public void setFlowPrincipal(List<FlowPrincipal> flowPrincipal) {
        this.flowPrincipal = flowPrincipal;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getMainPrinUserId() {
        return mainPrinUserId;
    }

    public void setMainPrinUserId(String mainPrinUserId) {
        this.mainPrinUserId = mainPrinUserId;
    }

    public String getPrinUserIds() {
        return prinUserIds;
    }

    public void setPrinUserIds(String prinUserIds) {
        this.prinUserIds = prinUserIds;
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

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public ExpertReviewDto getExpertReviewDto() {
        return expertReviewDto;
    }

    public void setExpertReviewDto(ExpertReviewDto expertReviewDto) {
        this.expertReviewDto = expertReviewDto;
    }
}