package cs.model.meeting;

import com.alibaba.fastjson.annotation.JSONField;
import cs.common.utils.DateUtils;
import cs.domain.project.WorkProgram;
import cs.model.BaseDto;
import org.hibernate.annotations.Formula;

import java.util.Date;

public class RoomBookingDto extends BaseDto{

	private String id;
	private String mrID;//会议室编号
	private String rbName;//会议名称
	private String addressName;//会议地点
	private String dueToPeople;//预定人
	private String host;//会议主持人
	@JSONField(format = "yyyy-MM-dd")
	private Date rbDay;//会议日期
	private String rbDate;//会议日期显示星期
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	private Date  beginTime;//会议开始时间
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	private Date endTime;//结束时间
	private String rbStatus;//会议预定状态，1,已预定，0未预定
	private String rbType;//会议类型
	private String content;//主要内容
	private String remark;//备注
	private String workProgramId;//工作方案Id
	private String stageOrg;//评审部门
	private String stageProject;//评审项目
	//开始结束时间段字符串
	private String beginTimeStr;
	private String endTimeStr;

	private WorkProgram workProgram;

	public WorkProgram getWorkProgram() {
		return workProgram;
	}

	public void setWorkProgram(WorkProgram workProgram) {
		this.workProgram = workProgram;
	}

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
	@Formula("(select m.addr from cs_meetingRoom m where m.id = mrID)")
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
	public String getWorkProgramId() {
		return workProgramId;
	}
	public void setWorkProgramId(String workProgramId) {
		this.workProgramId = workProgramId;
	}
	public String getStageOrg() {
		return stageOrg;
	}
	public void setStageOrg(String stageOrg) {
		this.stageOrg = stageOrg;
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
		return DateUtils.converToString(this.beginTime,"HH:mm");
	}

	public void setBeginTimeStr(String beginTimeStr) {
		this.beginTimeStr = beginTimeStr;
	}

	public String getEndTimeStr() {
		return DateUtils.converToString(this.endTime,"HH:mm");
	}

	public void setEndTimeStr(String endTimeStr) {
		this.endTimeStr = endTimeStr;
	}
}
