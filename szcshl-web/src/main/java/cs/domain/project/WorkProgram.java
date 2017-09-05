package cs.domain.project;

import cs.domain.DomainBase;
import cs.domain.meeting.RoomBooking;
import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 工作方案
 *
 * @author ldm
 */
@Entity
@Table(name = "cs_work_program")
@DynamicUpdate(true)
public class WorkProgram extends DomainBase {
    @Id
    private String id;

    //标题
    @Column(columnDefinition = "VARCHAR(64)")
    private String titleName;

    //评审方式
    @Column(columnDefinition = "VARCHAR(20)")
    private String reviewType;
    
    //评审阶段
    @Column(columnDefinition = "VARCHAR(50)")
    private String workreviveStage;

    //是否单个评审
    @Column(columnDefinition = "VARCHAR(20)")
    private String isSigle;


    //项目名称
    @Column(columnDefinition = "VARCHAR(256)")
    private String projectName;

    //来文单位
    @Column(columnDefinition = "VARCHAR(100)")
    private String sendFileUnit;

    //来文单位联系人
    @Column(columnDefinition = "VARCHAR(20)")
    private String sendFileUser;

    //建设单位
    @Column(columnDefinition = "VARCHAR(200)")
    private String buildCompany;

    //编制单位
    @Column(columnDefinition = "VARCHAR(200)")
    private String designCompany;

    //主管部门ID
    @Column(columnDefinition = "VARCHAR(64)")
    private String mainDeptId;
    
    //主管部门名称
    @Column(columnDefinition = "VARCHAR(200)")
    private String mainDeptName;

    //是否有环评
    @Column(columnDefinition = "VARCHAR(2)")
    private String isHaveEIA;

    //项目类别
    @Column(columnDefinition = "VARCHAR(40)")
    private String projectType;

    //小类
    @Column(columnDefinition = "VARCHAR(40)")
    private String projectSubType;

    //行业类别
    @Column(columnDefinition = "VARCHAR(200)")
    private String industryType;

    //联系人
    @Column(columnDefinition = "VARCHAR(32)")
    private String contactPerson;

    //联系人手机
    @Column(columnDefinition = "VARCHAR(12)")
    private String contactPersonPhone;

    //联系人电话
    @Column(columnDefinition = "VARCHAR(12)")
    private String contactPersonTel;

    //联系人传真
    @Column(columnDefinition = "VARCHAR(12)")
    private String contactPersonFax;

    //申报建设规模
    @Column(columnDefinition = "VARCHAR(1024)")
    private String buildSize;

    //申报投资
    @Column(columnDefinition = "NUMBER")
    private BigDecimal appalyInvestment;

    //申报建设内容
    @Column(columnDefinition = "VARCHAR(2048)")
    private String buildContent;

    //项目背景
    @Column(columnDefinition = "VARCHAR(2048)")
    private String projectBackGround;

    //评估部门
    @Column(columnDefinition = "VARCHAR(512)")
    private String reviewOrgName;

    //第一负责人
    @Column(columnDefinition = "VARCHAR(64)")
    private String mianChargeUserName;

    //第二负责人(可以有多个)
    @Column(columnDefinition = "VARCHAR(1024)")
    private String secondChargeUserName;

    //是否有补充函
    @Column(columnDefinition = "VARCHAR(2)")
    private String isHaveSuppLetter;

    //补充资料函发文日期
    @Column(columnDefinition = "DATE")
    private Date suppLetterDate;

    //评审会时间
   /* @Column(columnDefinition = "DATE")
    private Date stageTime;

    //评审时间
    @Column(columnDefinition = "VARCHAR(128)")
    private String workStageTime;

    //会议地点
    @Column(columnDefinition = "VARCHAR(128)")
    private String meetingAddress;

    @Column(columnDefinition = "VARCHAR(128)")
    private String meetingId;*/

    //调研开始时间
    @Column(columnDefinition = "DATE")
    private Date studyBeginTime;

    //调研结束时间
    @Column(columnDefinition = "DATE")
    private Date studyEndTime;

    //专家费用
    @Column(columnDefinition = "NUMBER")
    private BigDecimal expertCost;

    //拟评审重点问题
    @Column(columnDefinition = "VARCHAR(2000)")
    private String mainPoint;

    //部长处理意见
    @Column(columnDefinition = "VARCHAR(2000)")
    private String ministerSuggesttion;

    //部长处理日期
    @Column(columnDefinition = "DATE")
    private Date ministerDate;

    //部长名
    @Column(columnDefinition = "varchar(100)")
    private String ministerName;

    //中心领导处理意见
    @Column(columnDefinition = "VARCHAR(2000)")
    private String leaderSuggesttion;

    //中心领导处理日期
    @Column(columnDefinition = "DATE")
    private Date leaderDate;

    //中心领导名
    @Column(columnDefinition = "varchar(100)")
    private String leaderName;

    //标题日期
    @Column(columnDefinition = "DATE")
    private Date titleDate;
    
    //S 设备清单（进口）
    //项目概况
    @Column(columnDefinition = "varchar(100)")
    private String projectSurvey;
    
    //评审重点
    @Column(columnDefinition = "varchar(100)")
    private String stageEmphasis;
    
    //申报总额
    @Column(columnDefinition = "number")
    private BigDecimal declareCount;

    //是否已经生产会前准备材料
    @Column(columnDefinition = "varchar(2)")
    private String isCreateDoc;
    //E 设备清单（进口）

    //邀请单位及领导
    @Column(columnDefinition = "varchar(1024)")
    private String inviteUnitLeader;

    //流程分支序号
    @Column(columnDefinition = "varchar(2)")
    private String branchId;

    //收文，一对一（只做级联删除）
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "signId")
    private Sign sign;

    /**
     * 专家评审方案ID（这里不做关联了）
     */
    @Column(columnDefinition = "varchar(64)")
    private String expertReviewId;

    //会议预定信息
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "workProgram",orphanRemoval=true)
    private List<RoomBooking> roomBookings;

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

    public String getTitleName() {
        return titleName;
    }

    public void setTitleName(String titleName) {
        this.titleName = titleName;
    }

    public List<RoomBooking> getRoomBookings() {
        return roomBookings;
    }

    public void setRoomBookings(List<RoomBooking> roomBookings) {
        this.roomBookings = roomBookings;
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

    public String getExpertReviewId() {
        return expertReviewId;
    }

    public void setExpertReviewId(String expertReviewId) {
        this.expertReviewId = expertReviewId;
    }
}
