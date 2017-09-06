package cs.model.topic;

import cs.domain.flow.FlowPrincipal;
import cs.model.BaseDto;

import javax.persistence.Column;
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
     * 工作方案
     */
    private WorkPlanDto workPlanDto;

    /**
     * 归档
     */
    private FilingDto filingDto;

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
}