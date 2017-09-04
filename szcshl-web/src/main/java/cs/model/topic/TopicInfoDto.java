package cs.model.topic;

import cs.model.BaseDto;

import javax.persistence.Column;


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
     * 课题负责人IDs
     */
    private String prinIds;

    /**
     * 课题负责人名称
     */
    private String prinNames;
    private WorkPlanDto workPlanDto;
    private FilingDto filingDto;

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