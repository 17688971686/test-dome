package cs.domain.project;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicUpdate;

import cs.domain.DomainBase;
import cs.domain.expert.ExpertReview;

/**
 * 工作方案
 * @author ldm
 *
 */
@Entity
@Table(name = "WORK_PROGRAM")
@DynamicUpdate(true)
public class WorkProgram extends DomainBase{

	@Id
	private String id;
	
	//评审方式
	@Column(columnDefinition="VARCHAR(20)")
	private String reviewType;
	
	//是否单个评审
	@Column(columnDefinition="VARCHAR(20)")
	private String isSigle;
	
	//项目名称
	@Column(columnDefinition="VARCHAR(256)")
	private String projectName;
	
	//来文单位
	@Column(columnDefinition="VARCHAR(100)")
	private String sendFileUnit;
	
	//来文单位联系人
	@Column(columnDefinition="VARCHAR(20)")
	private String sendFileUser;
	
	//建设单位
	@Column(columnDefinition="VARCHAR(200)")
	private String buildCompany;
	
	//编制单位
	@Column(columnDefinition="VARCHAR(200)")
	private String designCompany;
	
	//主管部门ID
	@Column(columnDefinition="VARCHAR(64)")
	private String mainDeptId;
	
	//是否有环评
	@Column(columnDefinition="VARCHAR(2)")
	private String isHaveEIA;
	
	//项目类别
	@Column(columnDefinition="VARCHAR(40)")
	private String projectType;
	
	//小类
	@Column(columnDefinition="VARCHAR(40)")
	private String projectSubType;
	
	//行业类别
	@Column(columnDefinition="VARCHAR(40)")
	private String industryType;
	
	//联系人
	@Column(columnDefinition="VARCHAR(10)")
	private String contactPerson;
	
	//联系人手机
	@Column(columnDefinition="VARCHAR(12)")
	private String contactPersonPhone;
	
	//联系人电话
	@Column(columnDefinition="VARCHAR(12)")
	private String contactPersonTel;
	
	//联系人传真
	@Column(columnDefinition="VARCHAR(12)")
	private String contactPersonFax;
	
	//申报建设规模
	@Column(columnDefinition="VARCHAR(1024)")
	private String buildSize;
	
	//申报投资
	@Column(columnDefinition="NUMBER")
	private BigDecimal appalyInvestment;
	
	//申报建设内容
	@Column(columnDefinition="VARCHAR(2048)")
	private String buildContent;
	
	//项目背景
	@Column(columnDefinition="VARCHAR(2048)")
	private String projectBackGround;
	
	//评估部门
	@Column(columnDefinition="VARCHAR(64)")
	private String reviewOrgId;
	
	@Column(columnDefinition="VARCHAR(128)")
	private String reviewOrgName;
	
	//第一负责人
	@Column(columnDefinition="VARCHAR(64)")
	private String mianChargeUserId;
	
	@Column(columnDefinition="VARCHAR(64)")
	private String mianChargeUserName;
	
	//第二负责人
	@Column(columnDefinition="VARCHAR(64)")
	private String secondChargeUserId;
	
	@Column(columnDefinition="VARCHAR(64)")
	private String secondChargeUserName;
	
	//是否有补充函
	@Column(columnDefinition="VARCHAR(2)")
	private String isHaveSuppLetter;
	
	//补充资料函发文日期
	@Column(columnDefinition="DATE")
	private Date suppLetterDate;
	
	//会议地点
	@Column(columnDefinition="VARCHAR(128)")
	private String meetingAddress;
	
	//调研开始时间
	@Column(columnDefinition="DATE")
	private Date studyBeginTime;
	
	//调研结束时间
	@Column(columnDefinition="DATE")
	private Date studyEndTime;
	
	//专家费用
	@Column(columnDefinition="NUMBER")
	private BigDecimal expertCost;
	
	//拟评审重点问题
	@Column(columnDefinition="VARCHAR(2000)")
	private String mainPoint;
	
	//部长处理意见
	@Column(columnDefinition="VARCHAR(2000)")
	private String ministerSuggesttion;
	
	//部长处理日期
	@Column(columnDefinition="DATE")
	private Date ministerDate;
	
	//中心领导处理意见
	@Column(columnDefinition="VARCHAR(2000)")
	private String leaderSuggesttion;
	
	//中心领导处理日期
	@Column(columnDefinition="DATE")
	private Date leaderDate;
	
	//标题日期
	@Column(columnDefinition="DATE")
	private Date titleDate;

	//是否主流程的工作方案
	@Column(columnDefinition="VARCHAR(2)")
	private String isMain;
	
	//收文，一对一
	@ManyToOne
	@JoinColumn(name="signId")
	private Sign sign;
	
	@OneToMany(mappedBy="workProgram")
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

	public Date getSuppLetterDate() {
		return suppLetterDate;
	}

	public void setSuppLetterDate(Date suppLetterDate) {
		this.suppLetterDate = suppLetterDate;
	}

	public String getMeetingAddress() {
		return meetingAddress;
	}

	public void setMeetingAddress(String meetingAddress) {
		this.meetingAddress = meetingAddress;
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

	public Date getTitleDate() {
		return titleDate;
	}

	public void setTitleDate(Date titleDate) {
		this.titleDate = titleDate;
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
		
}
