package cs.model.project;

import com.alibaba.fastjson.annotation.JSONField;
import cs.common.utils.DateUtils;
import cs.domain.meeting.RoomBooking;
import cs.model.BaseDto;
import cs.model.expert.ExpertDto;
import cs.model.expert.ExpertSelectedDto;
import cs.model.meeting.RoomBookingDto;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;

/**
 * 工作方案
 *
 * @author ldm
 */
public class WorkProgramDto extends BaseDto {

    private String id;
    /**
     * 申报总投资
     */
    private BigDecimal totalInvestment;

    //签收ID
    private String signId;
    //标题
    private String titleName;
    //是否主项目
    private String isMainProject;
    //评审方式
    private String reviewType;
    //评审阶段
    private String workreviveStage;

    //是否单个评审
    private String isSigle;
    //项目名称
    private String projectName;
    //来文单位
    private String sendFileUnit;
    //来文单位联系人
    private String sendFileUser;
    //建设单位
    private String buildCompany;
    //编制单位
    private String designCompany;
    //主管部门ID
    private String mainDeptId;
    //主管部门名称
    private String mainDeptName;
    //是否有环评
    private String isHaveEIA;
    //项目类别
    private String projectType;
    //小类
    private String projectSubType;
    //行业类别
    private String industryType;
    //联系人
    private String contactPerson;
    //联系人手机
    private String contactPersonPhone;
    //联系人电话
    private String contactPersonTel;
    //联系人传真
    private String contactPersonFax;
    //申报建设规模
    private String buildSize;
    //申报投资
    private BigDecimal appalyInvestment;
    //报审概算
    private BigDecimal declaration;
    //申报建设内容
    private String buildContent;

    //项目背景
    private String projectBackGround;

    //评估部门
    private String reviewOrgName;

    //第一负责人
    private String mianChargeUserName;

    //第二负责人
    private String secondChargeUserName;

    //是否有补充函
    private String isHaveSuppLetter;

    //补充资料函发文日期
    @JSONField(format = "yyyy-MM-dd")
    private Date suppLetterDate;

    /**
     * 调研时间段（AM:表示上午，PM:表示下午，DAY:表示全天）
     */
    private String studyQuantum;
    
    //调研日期
    @JSONField(format = "yyyy-MM-dd")
    private Date studyAllDay;

    //调研开始时间
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date studyBeginTime;

    //调研结束时间
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date studyEndTime;
    
    //函评会日期
    @JSONField(format = "yyyy-MM-dd")
    private Date letterDate;
    
    //字符串日期格式
    private String studyBeginTimeStr;
    private String studyEndTimeStr;
    
    //专家费用
    private BigDecimal expertCost;

    //拟评审重点问题
    private String mainPoint;

    //部长处理意见
    private String ministerSuggesttion;

    private String ministerName;//部长名字

    @JSONField(format = "yyyy-MM-dd")
    private Date ministerDate;

    //中心领导处理意见
    private String leaderSuggesttion;
    private String leaderName;//中心领导名字

    @JSONField(format = "yyyy-MM-dd")
    private Date leaderDate;

    //标题日期
    @JSONField(format = "yyyy-MM-dd")
    private Date titleDate;

    //收文对象
    private SignDto signDto;

    //S 设备清单（进口）
    //项目概况
    private String projectSurvey;

    //评审重点
    private String stageEmphasis;

    //申报总额
    private BigDecimal declareCount;

    //是否已经生成会前准备材料
    private String isCreateDoc;

    //E 设备清单（进口）

    //流程分支序号
    private String branchId;

    //邀请单位及领导
    private String inviteUnitLeader;

    /**
     * 项目基本信息
     */
    private String baseInfo;

    private List<RoomBookingDto> roomBookingDtos;

    //拟聘请专家
    private List<ExpertDto> expertDtoList;

    private List<ExpertSelectedDto> expertSelectedDtoList;


    /**
     * 主工作方案DTO
     */
    private WorkProgramDto mainWorkProgramDto;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSignId() {
        return signId;
    }

    public void setSignId(String signId) {
        this.signId = signId;
    }

    public String getReviewType() {
        return reviewType;
    }

    public void setReviewType(String reviewType) {
        this.reviewType = reviewType;
    }

    public String getIsSigle() {
        return isSigle;
    }

    public void setIsSigle(String isSigle) {
        this.isSigle = isSigle;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getSendFileUnit() {
        return sendFileUnit;
    }

    public void setSendFileUnit(String sendFileUnit) {
        this.sendFileUnit = sendFileUnit;
    }

    public String getSendFileUser() {
        return sendFileUser;
    }

    public void setSendFileUser(String sendFileUser) {
        this.sendFileUser = sendFileUser;
    }

    public String getBuildCompany() {
        return buildCompany;
    }

    public void setBuildCompany(String buildCompany) {
        this.buildCompany = buildCompany;
    }

    public String getDesignCompany() {
        return designCompany;
    }

    public void setDesignCompany(String designCompany) {
        this.designCompany = designCompany;
    }

    public String getMainDeptId() {
        return mainDeptId;
    }

    public void setMainDeptId(String mainDeptId) {
        this.mainDeptId = mainDeptId;
    }

    public String getIsHaveEIA() {
        return isHaveEIA;
    }

    public void setIsHaveEIA(String isHaveEIA) {
        this.isHaveEIA = isHaveEIA;
    }

    public String getProjectType() {
        return projectType;
    }

    public void setProjectType(String projectType) {
        this.projectType = projectType;
    }

    public String getProjectSubType() {
        return projectSubType;
    }

    public void setProjectSubType(String projectSubType) {
        this.projectSubType = projectSubType;
    }

    public String getIndustryType() {
        return industryType;
    }

    public void setIndustryType(String industryType) {
        this.industryType = industryType;
    }

    public String getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    public String getContactPersonPhone() {
        return contactPersonPhone;
    }

    public void setContactPersonPhone(String contactPersonPhone) {
        this.contactPersonPhone = contactPersonPhone;
    }

    public String getContactPersonTel() {
        return contactPersonTel;
    }

    public void setContactPersonTel(String contactPersonTel) {
        this.contactPersonTel = contactPersonTel;
    }

    public String getContactPersonFax() {
        return contactPersonFax;
    }

    public void setContactPersonFax(String contactPersonFax) {
        this.contactPersonFax = contactPersonFax;
    }

    public String getBuildSize() {
        return buildSize;
    }

    public void setBuildSize(String buildSize) {
        this.buildSize = buildSize;
    }

    public BigDecimal getAppalyInvestment() {
        return appalyInvestment;
    }

    public void setAppalyInvestment(BigDecimal appalyInvestment) {
        this.appalyInvestment = appalyInvestment;
    }

    public String getBuildContent() {
        return buildContent;
    }

    public void setBuildContent(String buildContent) {
        this.buildContent = buildContent;
    }

    public String getProjectBackGround() {
        return projectBackGround;
    }

    public void setProjectBackGround(String projectBackGround) {
        this.projectBackGround = projectBackGround;
    }

    public String getReviewOrgName() {
        return reviewOrgName;
    }

    public void setReviewOrgName(String reviewOrgName) {
        this.reviewOrgName = reviewOrgName;
    }

    public String getMianChargeUserName() {
        return mianChargeUserName;
    }

    public void setMianChargeUserName(String mianChargeUserName) {
        this.mianChargeUserName = mianChargeUserName;
    }

    public String getSecondChargeUserName() {
        return secondChargeUserName;
    }

    public void setSecondChargeUserName(String secondChargeUserName) {
        this.secondChargeUserName = secondChargeUserName;
    }

    public String getIsHaveSuppLetter() {
        return isHaveSuppLetter;
    }

    public void setIsHaveSuppLetter(String isHaveSuppLetter) {
        this.isHaveSuppLetter = isHaveSuppLetter;
    }

    public BigDecimal getExpertCost() {
        return expertCost;
    }

    public void setExpertCost(BigDecimal expertCost) {
        this.expertCost = expertCost;
    }

    public String getMainPoint() {
        return mainPoint;
    }

    public void setMainPoint(String mainPoint) {
        this.mainPoint = mainPoint;
    }

    public String getMinisterSuggesttion() {
        return ministerSuggesttion;
    }

    public void setMinisterSuggesttion(String ministerSuggesttion) {
        this.ministerSuggesttion = ministerSuggesttion;
    }

    public String getLeaderSuggesttion() {
        return leaderSuggesttion;
    }

    public void setLeaderSuggesttion(String leaderSuggesttion) {
        this.leaderSuggesttion = leaderSuggesttion;
    }

    public Date getMinisterDate() {
        return ministerDate;
    }

    public void setMinisterDate(Date ministerDate) {
        this.ministerDate = ministerDate;
    }

    public Date getLeaderDate() {
        return leaderDate;
    }

    public void setLeaderDate(Date leaderDate) {
        this.leaderDate = leaderDate;
    }

    public Date getSuppLetterDate() {
        return suppLetterDate;
    }

    public void setSuppLetterDate(Date suppLetterDate) {
        this.suppLetterDate = suppLetterDate;
    }

    public Date getStudyBeginTime() {
        return studyBeginTime;
    }

    public void setStudyBeginTime(Date studyBeginTime) {
        this.studyBeginTime = studyBeginTime;
    }

    public Date getStudyEndTime() {
        return studyEndTime;
    }

    public void setStudyEndTime(Date studyEndTime) {
        this.studyEndTime = studyEndTime;
    }

    public Date getTitleDate() {
        return titleDate;
    }

    public void setTitleDate(Date titleDate) {
        this.titleDate = titleDate;
    }

    public SignDto getSignDto() {
        return signDto;
    }

    public void setSignDto(SignDto signDto) {
        this.signDto = signDto;
    }

    public String getTitleName() {
        return titleName;
    }

    public void setTitleName(String titleName) {
        this.titleName = titleName;
    }

    public String getMinisterName() {
        return ministerName;
    }

    public void setMinisterName(String ministerName) {
        this.ministerName = ministerName;
    }

    public String getLeaderName() {
        return leaderName;
    }

    public void setLeaderName(String leaderName) {
        this.leaderName = leaderName;
    }

    public String getIsMainProject() {
        return isMainProject;
    }

    public void setIsMainProject(String isMainProject) {
        this.isMainProject = isMainProject;
    }

    public String getProjectSurvey() {
        return projectSurvey;
    }

    public void setProjectSurvey(String projectSurvey) {
        this.projectSurvey = projectSurvey;
    }

    public String getStageEmphasis() {
        return stageEmphasis;
    }

    public void setStageEmphasis(String stageEmphasis) {
        this.stageEmphasis = stageEmphasis;
    }

    public BigDecimal getDeclareCount() {
        return declareCount;
    }

    public void setDeclareCount(BigDecimal declareCount) {
        this.declareCount = declareCount;
    }

    public String getIsCreateDoc() {
        return isCreateDoc;
    }

    public void setIsCreateDoc(String isCreateDoc) {
        this.isCreateDoc = isCreateDoc;
    }

    public String getWorkreviveStage() {
        return workreviveStage;
    }

    public void setWorkreviveStage(String workreviveStage) {
        this.workreviveStage = workreviveStage;
    }

    public String getMainDeptName() {
        return mainDeptName;
    }

    public void setMainDeptName(String mainDeptName) {
        this.mainDeptName = mainDeptName;
    }

    public String getBranchId() {
        return branchId;
    }

    public void setBranchId(String branchId) {
        this.branchId = branchId;
    }

    public String getInviteUnitLeader() {
        return inviteUnitLeader;
    }

    public void setInviteUnitLeader(String inviteUnitLeader) {
        this.inviteUnitLeader = inviteUnitLeader;
    }

    public String getStudyQuantum() {
        return studyQuantum;
    }

    public void setStudyQuantum(String studyQuantum) {
        this.studyQuantum = studyQuantum;
    }

	public BigDecimal getDeclaration() {
		return declaration;
	}

	public void setDeclaration(BigDecimal declaration) {
		this.declaration = declaration;
	}

	public Date getStudyAllDay() {
		return studyAllDay;
	}

	public void setStudyAllDay(Date studyAllDay) {
		this.studyAllDay = studyAllDay;
	}

	public String getStudyBeginTimeStr() {
		 return DateUtils.converToString(this.studyBeginTime, "HH:mm");
	}

	public void setStudyBeginTimeStr(String studyBeginTimeStr) {
		this.studyBeginTimeStr = studyBeginTimeStr;
	}

	public String getStudyEndTimeStr() {
		 return DateUtils.converToString(this.studyEndTime, "HH:mm");
	}

	public void setStudyEndTimeStr(String studyEndTimeStr) {
		this.studyEndTimeStr = studyEndTimeStr;
	}

    public List<RoomBookingDto> getRoomBookingDtos() {
        return roomBookingDtos;
    }

    public void setRoomBookingDtos(List<RoomBookingDto> roomBookingDtos) {
        this.roomBookingDtos = roomBookingDtos;
    }

	public Date getLetterDate() {
		return letterDate;
	}

	public void setLetterDate(Date letterDate) {
		this.letterDate = letterDate;
	}

    public void setTotalInvestment(BigDecimal totalInvestment){
        this.totalInvestment = totalInvestment;
    }

    public BigDecimal getTotalInvestment(){
        return totalInvestment;
    }

    public String getBaseInfo() {
        return baseInfo;
    }

    public void setBaseInfo(String baseInfo) {
        this.baseInfo = baseInfo;
    }

    public WorkProgramDto getMainWorkProgramDto() {
        return mainWorkProgramDto;
    }

    public void setMainWorkProgramDto(WorkProgramDto mainWorkProgramDto) {
        this.mainWorkProgramDto = mainWorkProgramDto;
    }

    public List<ExpertSelectedDto> getExpertSelectedDtoList() {
        return expertSelectedDtoList;
    }

    public void setExpertSelectedDtoList(List<ExpertSelectedDto> expertSelectedDtoList) {
        this.expertSelectedDtoList = expertSelectedDtoList;
    }

    public List<ExpertDto> getExpertDtoList() {
        return expertDtoList;
    }

    public void setExpertDtoList(List<ExpertDto> expertDtoList) {
        this.expertDtoList = expertDtoList;
    }
}
