package cs.model.project;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;

import cs.domain.expert.ExpertReview;
import cs.domain.project.Sign;
import cs.model.BaseDto;

/**	
 * 工作方案
 * @author ldm
 *
 */
public class WorkProgramDto extends BaseDto{

	private String id;
	//签收ID
	private String signId;
	//评审方式
	private String reviewType;
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
	private String appalyInvestment;
	
	//申报建设内容
	private String buildContent;
	
	//项目背景
	private String projectBackGround;
	
	//评估部门
	private String reviewOrgId;
	private String reviewOrgName;
	
	//第一负责人
	private String mianChargeUserId;
	
	private String mianChargeUserName;
	
	//第二负责人
	private String secondChargeUserId;
	
	private String secondChargeUserName;
	
	//是否有补充函
	private String isHaveSuppLetter;
	
	//补充资料函发文日期
	@JSONField(format = "yyyy-MM-dd")
	private Date suppLetterDate;
	//评审会时间
	@JSONField(format = "yyyy-MM-dd")
	private Date stageTime;

	//会议地点
	private String meetingAddress;
	
	//调研开始时间
	@JSONField(format = "yyyy-MM-dd")
	private Date studyBeginTime;
	
	//调研结束时间
	@JSONField(format = "yyyy-MM-dd")
	private Date studyEndTime;
	
	//专家费用
	private BigDecimal expertCost;
	
	//拟评审重点问题
	private String mainPoint;
	
	//部长处理意见
	private String ministerSuggesttion;
	
	@JSONField(format = "yyyy-MM-dd")
	private Date ministerDate;
	
	//中心领导处理意见
	private String leaderSuggesttion;
	
	@JSONField(format = "yyyy-MM-dd")
	private Date leaderDate;
	
	//标题日期
	private Date titleDate;
	
	//收文对象
	private Sign sign;
	
	//是否主流程
	private String isMain;
	
	//评审专家
	private List<ExpertReview> expertReviews;
	
	public Sign getSign() {
		return sign;
	}

	public void setSign(Sign sign) {
		this.sign = sign;
	}

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

	public String getAppalyInvestment() {
		return appalyInvestment;
	}

	public void setAppalyInvestment(String appalyInvestment) {
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
	
	public String getReviewOrgId() {
		return reviewOrgId;
	}

	public void setReviewOrgId(String reviewOrgId) {
		this.reviewOrgId = reviewOrgId;
	}

	public String getReviewOrgName() {
		return reviewOrgName;
	}

	public void setReviewOrgName(String reviewOrgName) {
		this.reviewOrgName = reviewOrgName;
	}

	public String getMianChargeUserId() {
		return mianChargeUserId;
	}

	public void setMianChargeUserId(String mianChargeUserId) {
		this.mianChargeUserId = mianChargeUserId;
	}

	public String getMianChargeUserName() {
		return mianChargeUserName;
	}

	public void setMianChargeUserName(String mianChargeUserName) {
		this.mianChargeUserName = mianChargeUserName;
	}

	public String getSecondChargeUserId() {
		return secondChargeUserId;
	}

	public void setSecondChargeUserId(String secondChargeUserId) {
		this.secondChargeUserId = secondChargeUserId;
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

	public String getMeetingAddress() {
		return meetingAddress;
	}

	public void setMeetingAddress(String meetingAddress) {
		this.meetingAddress = meetingAddress;
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

	public String getIsMain() {
		return isMain;
	}

	public void setIsMain(String isMain) {
		this.isMain = isMain;
	}

	public List<ExpertReview> getExpertReviews() {
		return expertReviews;
	}

	public void setExpertReviews(List<ExpertReview> expertReviews) {
		this.expertReviews = expertReviews;
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

	public Date getStageTime() {
		return stageTime;
	}

	public void setStageTime(Date stageTime) {
		this.stageTime = stageTime;
	}

		
}
