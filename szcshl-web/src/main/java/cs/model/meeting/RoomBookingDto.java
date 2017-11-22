package cs.model.meeting;

import com.alibaba.fastjson.annotation.JSONField;
import cs.common.utils.DateUtils;
import cs.domain.project.WorkProgram;
import cs.model.BaseDto;
import org.hibernate.annotations.Formula;

import java.util.Date;

public class RoomBookingDto extends BaseDto {

    private String id;
    /**
     * 会议室编号
     */
    private String mrID;
    /**
     * 会议名称
     */
    private String rbName;
    /**
     * 会议地点(停用)
     */
    private String addressName;
    /**
     * 预定人
     */
    private String dueToPeople;
    /**
     * 会议主持人(停用)
     */
    private String host;

    @JSONField(format = "yyyy-MM-dd")
    private Date rbDay;//会议日期

    private String rbDate;//会议日期显示星期

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date beginTime;//会议开始时间

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;//结束时间
    /**
     * 会议预定状态，1,已预定，0未预定(停用)
     */
    private String rbStatus;
    /**
     * 会议类型(停用)
     */
    private String rbType;//会议类型
    /**
     * 主要内容
     */
    private String content;
    /**
     * 备注
     */
    private String remark;

    private String stageOrgName;//评审部门
    private String stageProject;//评审项目

    /**
     * 业务ID，如项目工作方案ID，课题研究工作方案ID等
     */
    private String businessId;
    /**
     * 业务类型（为了方便初始化【SIGN:表示项目，TOPIC:表示课题研究】）
     */
    private String businessType;

    /*****  以下字段只是辅助作用，并不作为数据库存储字段 ***/
    private String beginTimeStr;
    private String endTimeStr;
    private String bookId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMrID() {
        return mrID;
    }

    public void setMrID(String mrID) {
        this.mrID = mrID;
    }

    public String getRbName() {
        return rbName;
    }

    public void setRbName(String rbName) {
        this.rbName = rbName;
    }

    public String getAddressName() {
        return addressName;
    }

    public void setAddressName(String addressName) {
        this.addressName = addressName;
    }

    public String getDueToPeople() {
        return dueToPeople;
    }

    public void setDueToPeople(String dueToPeople) {
        this.dueToPeople = dueToPeople;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Date getRbDay() {
        return rbDay;
    }

    public void setRbDay(Date rbDay) {
        this.rbDay = rbDay;
    }

    public Date getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(Date beginTime) {
        this.beginTime = beginTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getContent() {
        return content;
    }

    public String getRbStatus() {
        return rbStatus;
    }

    public void setRbStatus(String rbStatus) {
        this.rbStatus = rbStatus;
    }

    public String getRbType() {
        return rbType;
    }

    public void setRbType(String rbType) {
        this.rbType = rbType;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getStageOrgName() {
        return stageOrgName;
    }

    public void setStageOrgName(String stageOrgName) {
        this.stageOrgName = stageOrgName;
    }

    public String getRbDate() {
        return rbDate;
    }

    public void setRbDate(String rbDate) {
        this.rbDate = rbDate;
    }

    public String getStageProject() {
        return stageProject;
    }

    public void setStageProject(String stageProject) {
        this.stageProject = stageProject;
    }

    public String getBeginTimeStr() {
        return beginTimeStr;
    }

    public void setBeginTimeStr(String beginTimeStr) {
        this.beginTimeStr = beginTimeStr;
    }

    public String getEndTimeStr() {
        return endTimeStr;
    }

    public void setEndTimeStr(String endTimeStr) {
        this.endTimeStr = endTimeStr;
    }

    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }
}
