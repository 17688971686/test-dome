package cs.model.topic;

import com.alibaba.fastjson.annotation.JSONField;
import cs.model.BaseDto;
import cs.model.expert.ExpertDto;
import cs.model.meeting.RoomBookingDto;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


/**
 * Description:  页面数据模型
 * author: ldm
 * Date: 2017-9-4 15:33:03
 */
public class WorkPlanDto extends BaseDto {

    private String id;
    private String topicName;
    private String cooperator;
    private String contactName;
    private String tellPhone;
    private String fax;
    private String topicContent;
    private BigDecimal cost;
    private Date researchDate;
    private String unitAndLeader;
    private String directorOption;
    private String directorName;
    @JSONField(format = "yyyy-MM-dd")
    private Date directorDate;
    private String leaderOption;
    private String leaderName;
    @JSONField(format = "yyyy-MM-dd")
    private Date leaderDate;
    /**
     * 主任批示
     */
    private String mleaderOption;

    /**
     * 主任名称
     */
    private String mleaderName;

    /**
     * 主任批示日期
     */
    @JSONField(format = "yyyy-MM-dd")
    private Date mleaderDate;
    private TopicInfoDto topicInfoDto;
    //课题研究ID
    private String topicId;

    /**
     * 会议室预定列表
     */
    private List<RoomBookingDto> roomDtoList;
    /**
     * 拟聘请专家列表
     */
    private List<ExpertDto> expertDtoList;

    public WorkPlanDto() {
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
    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }
    public String getTellPhone() {
        return tellPhone;
    }

    public void setTellPhone(String tellPhone) {
        this.tellPhone = tellPhone;
    }
    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }
    public String getTopicContent() {
        return topicContent;
    }

    public void setTopicContent(String topicContent) {
        this.topicContent = topicContent;
    }
    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }
    public Date getResearchDate() {
        return researchDate;
    }

    public void setResearchDate(Date researchDate) {
        this.researchDate = researchDate;
    }
    public String getUnitAndLeader() {
        return unitAndLeader;
    }

    public void setUnitAndLeader(String unitAndLeader) {
        this.unitAndLeader = unitAndLeader;
    }
    public String getDirectorOption() {
        return directorOption;
    }

    public void setDirectorOption(String directorOption) {
        this.directorOption = directorOption;
    }
    public String getDirectorName() {
        return directorName;
    }

    public void setDirectorName(String directorName) {
        this.directorName = directorName;
    }
    public Date getDirectorDate() {
        return directorDate;
    }

    public void setDirectorDate(Date directorDate) {
        this.directorDate = directorDate;
    }
    public String getLeaderOption() {
        return leaderOption;
    }

    public void setLeaderOption(String leaderOption) {
        this.leaderOption = leaderOption;
    }
    public String getLeaderName() {
        return leaderName;
    }

    public void setLeaderName(String leaderName) {
        this.leaderName = leaderName;
    }
    public Date getLeaderDate() {
        return leaderDate;
    }

    public void setLeaderDate(Date leaderDate) {
        this.leaderDate = leaderDate;
    }

    public TopicInfoDto getTopicInfoDto() {
        return topicInfoDto;
    }

    public void setTopicInfoDto(TopicInfoDto topicInfoDto) {
        this.topicInfoDto = topicInfoDto;
    }

    public String getTopicId() {
        return topicId;
    }

    public void setTopicId(String topicId) {
        this.topicId = topicId;
    }

    public List<RoomBookingDto> getRoomDtoList() {
        return roomDtoList;
    }

    public void setRoomDtoList(List<RoomBookingDto> roomDtoList) {
        this.roomDtoList = roomDtoList;
    }

    public List<ExpertDto> getExpertDtoList() {
        return expertDtoList;
    }

    public void setExpertDtoList(List<ExpertDto> expertDtoList) {
        this.expertDtoList = expertDtoList;
    }

    public String getMleaderOption() {
        return mleaderOption;
    }

    public void setMleaderOption(String mleaderOption) {
        this.mleaderOption = mleaderOption;
    }

    public String getMleaderName() {
        return mleaderName;
    }

    public void setMleaderName(String mleaderName) {
        this.mleaderName = mleaderName;
    }

    public Date getMleaderDate() {
        return mleaderDate;
    }

    public void setMleaderDate(Date mleaderDate) {
        this.mleaderDate = mleaderDate;
    }
}