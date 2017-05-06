package cs.domain.project;


import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Formula;

import cs.domain.DomainBase;
import cs.domain.sys.Org;

/**
 * 设置为动态更新，只更新有修改的字段
 * @author ldm
 *
 */
@Entity
@Table(name = "SIGN")
@DynamicUpdate(true)	
public class Sign extends DomainBase{

	/**
	 * 收文ID
	 */
	@Id
	private String signid;
	
	//委内收文编号
	@Column(columnDefinition="VARCHAR(30)")
	private String filecode;
	
	//项目代码
	@Column(columnDefinition="VARCHAR(20)")
	private String projectcode;
	
	//评审阶段
	@Column(columnDefinition="VARCHAR(64)")
	private String reviewstage;
	
	//是否项目预签收
	@Column(columnDefinition="VARCHAR(1)")
	private String ispresign;
	
	//项目名称
	@Column(columnDefinition="VARCHAR(200)")
	private String projectname;
	
	//是否登记完毕
	@Column(columnDefinition="VARCHAR(2)")
	private String isregisteredcompleted;
	
	//主办处室ID
	@Column(columnDefinition="VARCHAR(64)")
	private String maindepetid;
	//主办事处名称
	@Formula("(select o.name from cs_org o where o.id = maindepetid)")
	private String maindeptName;

	//主办处室联系人ID
	@Column(columnDefinition="VARCHAR(64)")
	private String maindepetcontactuserid;
	
	//主办事处联系人	
	@Formula("(select u.loginName from cs_user u where u.id = maindepetcontactuserid)")
	private String mainDeptUserName;
		
	//协办处室ID
	@Column(columnDefinition="VARCHAR(64)")
	private String assistdeptid;
	
	//协办事处名称
	@Formula("(select o.name from cs_org o where o.id = assistdeptid)")
	private String assistdeptName;
	
	//协办处室联系人ID
	@Column(columnDefinition="VARCHAR(64)")
	private String assistdeptcontactuserid;
	
	//协办事处联系人
	@Formula("(select u.loginName from cs_user u where u.id = assistdeptcontactuserid)")
	private String assistDeptUserName;
	
	//编制单位ID
	@Column(columnDefinition="VARCHAR(64)")
	private String designcompanyid;
	
	//建设单位ID
	@Column(columnDefinition="VARCHAR(64)")
	private String builtcompanyid;
	
	//缓急程度
	@Column(columnDefinition="VARCHAR(16)")
	private String urgencydegree;
	
	//年度计划类别
	@Column(columnDefinition="VARCHAR(16)")
	private String yearplantype;
	
	//秘密登记
	@Column(columnDefinition="VARCHAR(16)")
	private String secrectlevel;
	
	//综合部拟办意见
	@Column(columnDefinition="VARCHAR(255)")
	private String comprehensivehandlesug;
	
	//中心领导审批意见
	@Column(columnDefinition="VARCHAR(255)")
	private String leaderhandlesug;
	
	//部长处理意见
	@Column(columnDefinition="VARCHAR(255)")
	private String ministerhandlesug;
	
	//送件人签名
	@Column(columnDefinition="VARCHAR(16)")
	private String sendusersign;
	
	//是否确认签收
	@Column(columnDefinition="VARCHAR(2)")
	private String issign;
	
	//是否有评审费用
	@Column(columnDefinition="VARCHAR(2)")
	private String ishasreviewcost;
	
	//项目签收时间
	@Column(columnDefinition="DATE")
	private Date signdate;
	
	//剩余工作日
	@Column(columnDefinition="NUMBER")
	private BigDecimal surplusdays;
	
	//预计发文时间
	@Column(columnDefinition="DATE")
	private Date expectdispatchdate;
	
	//送来时间
	@Column(columnDefinition="DATE")
	private Date receivedate;
	
	//发文后工作日
	@Column(columnDefinition="NUMBER")
	private BigDecimal daysafterdispatch;
	
	//评审天数
	@Column(columnDefinition="NUMBER")
	private BigDecimal reviewdays;
	
	//是否协审
	@Column(columnDefinition="VARCHAR(2)")
	private String isassistproc;
	
	//项目主负责人
	@Column(columnDefinition="VARCHAR(64)")
	private String mainchargeuserid;
	
	//评审部门
	@Column(columnDefinition="VARCHAR(64)")
	private String reviewdeptid;
	
	//归档编号
	@Column(columnDefinition="VARCHAR(100)")
	private String filenum;
	
	//文件字号
	@Column(columnDefinition="VARCHAR(100)")
	private String docnum;
	
	//是否暂停
	@Column(columnDefinition="VARCHAR(1)")
	private String ispause;
	
	//暂停工作日
	@Column(columnDefinition="NUMBER")
	private BigDecimal pausedays;
	
	//暂停时间
	@Column(columnDefinition="DATE")
	private Date pausetime;
	
	//暂停说明
	@Column(columnDefinition="VARCHAR(2048)")
	private String pasedescription;
	
	//是否完成评审方案
	@Column(columnDefinition="VARCHAR(2)")
	private String isreviewcompleted;

	//建议书项目处理表份数	 
	@Column(columnDefinition="INTEGER")
	private Integer sugProDealCount;
	
	//建议书项目处理表是否有原件
	@Column(columnDefinition="VARCHAR(2)")
	private String sugProDealOriginal;
	
	//建议书项目处理表是否有复印件
	@Column(columnDefinition="VARCHAR(2)")
	private String sugProDealCopy;
	
	//建议书文件处理表份数
	@Column(columnDefinition="INTEGER")
	private Integer sugFileDealCount;
	
	//建议书文件处理表是否有原件
	@Column(columnDefinition="VARCHAR(2)")
	private String sugFileDealOriginal;
	
	//建议书文件处理表是否有复印件
	@Column(columnDefinition="VARCHAR(2)")
	private String sugFileDealCopy;
	
	//建议书项目单位申报表份数
	@Column(columnDefinition="INTEGER")
	private Integer sugOrgApplyCount;
	
	//建议书项目单位申报表是否有原件
	@Column(columnDefinition="VARCHAR(2)")
	private String sugOrgApplyOriginal;
	
	//建议书项目单位申报表是否有复印件
	@Column(columnDefinition="VARCHAR(2)")
	private String sugOrgApplyCopy;
	
	//建议书项目单位请示报告份数
	@Column(columnDefinition="INTEGER")
	private Integer sugOrgReqCount;
	
	//建议书项目单位请示报告是否有原件
	@Column(columnDefinition="VARCHAR(2)")
	private String sugOrgReqOriginal;
	
	//建议书项目单位请示报告是否有复印件
	@Column(columnDefinition="VARCHAR(2)")
	private String sugOrgReqCopy;
	
	//项目建议书份数
	@Column(columnDefinition="INTEGER")
	private Integer sugProAdviseCount;
	
	//项目建议书是否有原件
	@Column(columnDefinition="VARCHAR(2)")
	private String sugProAdviseOriginal;
		
	//项目建议书是否有复印件
	@Column(columnDefinition="VARCHAR(2)")
	private String sugProAdviseCopy;
	
	//项目建议书电子文档份数
	@Column(columnDefinition="INTEGER")
	private Integer proSugEledocCount;
	
	//项目建议书电子文档是否有原件
	@Column(columnDefinition="VARCHAR(2)")
	private String proSugEledocOriginal;
	
	//项目建议书电子文档是否有复印件
	@Column(columnDefinition="VARCHAR(2)")
	private String proSugEledocCopy;
	
	//建议书相关会议纪要份数
	@Column(columnDefinition="INTEGER")
	private Integer sugMeetCount;
	
	//建议书相关会议纪要是否有原件
	@Column(columnDefinition="VARCHAR(2)")
	private String sugMeetOriginal;
	
	//建议书相关会议纪要是否有复印件
	@Column(columnDefinition="VARCHAR(2)")
	private String sugMeetCopy;
	
	//可研项目处理表份数
	@Column(columnDefinition="INTEGER")
	private Integer studyProDealCount;
	
	//可研项目处理表是否有原件
	@Column(columnDefinition="VARCHAR(2)")
	private String studyPealOriginal;
	
	//可研项目处理表是否有复印件
	@Column(columnDefinition="VARCHAR(2)")
	private String studyProDealCopy;
	
	//可研文件处理表份数
	@Column(columnDefinition="INTEGER")
	private Integer studyFileDealCount;
	
	//可研文件处理表是否有原件
	@Column(columnDefinition="VARCHAR(2)")
	private String studyFileDealOriginal;
	
	//可研文件处理表是否有复印件
	@Column(columnDefinition="VARCHAR(2)")
	private String studyFileDealCopy;
	
	//可研项目单位申报表份数
	@Column(columnDefinition="INTEGER")
	private Integer studyOrgApplyCount;
	
	//可研项目单位申报表是否有原件
	@Column(columnDefinition="VARCHAR(2)")
	private String studyOrgApplyOriginal;
	
	//可研项目单位申报表是否有复印件
	@Column(columnDefinition="VARCHAR(2)")
	private String studyOrgApplyCopy;
	
	//可研项目单位请示报告份数
	@Column(columnDefinition="INTEGER")
	private Integer studyOrgReqCount;
	
	//可研项目单位请示报告是否有原件
	@Column(columnDefinition="VARCHAR(2)")
	private String studyOrgReqOriginal;
	
	//可研项目单位请示报告是否有复印件
	@Column(columnDefinition="VARCHAR(2)")
	private String studyOrgReqCopy;
	
	//可研项目建议书批复
	@Column(columnDefinition="Integer")
	private Integer studyProSugCount;
	
	//可研项目建议书批复是否有原件
	@Column(columnDefinition="VARCHAR(2)")
	private String studyProSugOriginal;
	
	//可研项目建议书批复是否有复印件
	@Column(columnDefinition="VARCHAR(2)")
	private String studyProSugCopy;
	
	//可研相关会议纪要份数
	@Column(columnDefinition="INTEGER")
	private Integer studyMeetCount;
	
	//可研相关会议是否有原件
	@Column(columnDefinition="VARCHAR(2)")
	private String studyMeetOriginal;
	
	//可研相关会议是否有复印件
	@Column(columnDefinition="VARCHAR(2)")
	private String studyMeetCopy;
	
	//可研环保批复文件份数
	@Column(columnDefinition="INTEGER")
	private Integer envproReplyCount;
	
	//可研环保批复文件是否有原件
	@Column(columnDefinition="VARCHAR(2)")
	private String envproReplyOriginal;
	
	//可研环保批复文件是否有复印件
	@Column(columnDefinition="VARCHAR(2)")
	private String envproReplyCopy;
	
	//可研规划选址批文份数
	@Column(columnDefinition="INTEGER")
	private Integer planAddrCount;
	
	//可研规划选址批文是否有原件
	@Column(columnDefinition="VARCHAR(2)")
	private String planAddrOriginal;
	
	//可研规划选址批文是否有复印件
	@Column(columnDefinition="VARCHAR(2)")
	private String planAddrCopy;
	
	//可研报告份数
	@Column(columnDefinition="INTEGER")
	private Integer reportCount;
	
	//可研报告是否有原件
	@Column(columnDefinition="VARCHAR(2)")
	private String reportOrigin;
	
	//可研报告是否有复印件
	@Column(columnDefinition="VARCHAR(2)")
	private String reportCopy;
	
	//可研报告电子文档份数
	@Column(columnDefinition="INTEGER")
	private Integer eledocCount;
	
	//可研报告电子文档是否有原件
	@Column(columnDefinition="VARCHAR(2)")
	private String eledocOriginal;
	
	//可研报告电子文档是否有复印件
	@Column(columnDefinition="VARCHAR(2)")
	private String eledocCopy;
	
	//可研节能报告份数
	@Column(columnDefinition="INTEGER")
	private Integer energyCount;
	
	//可研节能报告是否有原件
	@Column(columnDefinition="VARCHAR(2)")
	private String energyOriginal;
	
	//可研节能报告是否有复印件
	@Column(columnDefinition="VARCHAR(2)")
	private String energyCopy;

	//是否已发起流程
	@Column(columnDefinition="VARCHAR(2)")
	private String folwState;

	//收文状态
	@Column(columnDefinition="VARCHAR(2)")
	private String signState;
	
	//收文是否完成
	private String isDispatchCompleted;
	
	//收文与部门关联：一对一关系
	@OneToOne
	@JoinColumn(name ="orgSignId")
	private Org orgSignId;
	
	//工作方案
	@OneToOne(mappedBy="sign")  
	private WorkProgram workProgram;
	
	//发文
	@OneToOne(mappedBy="sign")  
	private DispatchDoc dispatchDoc;
	
	public Sign() {
		super();
	}

	public String getSignid() {
		return signid;
	}

	public void setSignid(String signid) {
		this.signid = signid;
	}

	public String getFilecode() {
		return filecode;
	}

	public void setFilecode(String filecode) {
		this.filecode = filecode;
	}

	public String getProjectcode() {
		return projectcode;
	}

	public void setProjectcode(String projectcode) {
		this.projectcode = projectcode;
	}

	public String getReviewstage() {
		return reviewstage;
	}

	public void setReviewstage(String reviewstage) {
		this.reviewstage = reviewstage;
	}

	public String getIspresign() {
		return ispresign;
	}

	public void setIspresign(String ispresign) {
		this.ispresign = ispresign;
	}

	public String getProjectname() {
		return projectname;
	}

	public void setProjectname(String projectname) {
		this.projectname = projectname;
	}

	public String getIsregisteredcompleted() {
		return isregisteredcompleted;
	}

	public void setIsregisteredcompleted(String isregisteredcompleted) {
		this.isregisteredcompleted = isregisteredcompleted;
	}

	public String getMaindepetid() {
		return maindepetid;
	}

	public void setMaindepetid(String maindepetid) {
		this.maindepetid = maindepetid;
	}

	public String getMaindepetcontactuserid() {
		return maindepetcontactuserid;
	}

	public void setMaindepetcontactuserid(String maindepetcontactuserid) {
		this.maindepetcontactuserid = maindepetcontactuserid;
	}

	public String getAssistdeptid() {
		return assistdeptid;
	}

	public void setAssistdeptid(String assistdeptid) {
		this.assistdeptid = assistdeptid;
	}

	public String getAssistdeptcontactuserid() {
		return assistdeptcontactuserid;
	}

	public void setAssistdeptcontactuserid(String assistdeptcontactuserid) {
		this.assistdeptcontactuserid = assistdeptcontactuserid;
	}

	public String getDesigncompanyid() {
		return designcompanyid;
	}

	public void setDesigncompanyid(String designcompanyid) {
		this.designcompanyid = designcompanyid;
	}

	public String getBuiltcompanyid() {
		return builtcompanyid;
	}

	public void setBuiltcompanyid(String builtcompanyid) {
		this.builtcompanyid = builtcompanyid;
	}

	public String getUrgencydegree() {
		return urgencydegree;
	}

	public void setUrgencydegree(String urgencydegree) {
		this.urgencydegree = urgencydegree;
	}

	public String getYearplantype() {
		return yearplantype;
	}

	public void setYearplantype(String yearplantype) {
		this.yearplantype = yearplantype;
	}

	public String getSecrectlevel() {
		return secrectlevel;
	}

	public void setSecrectlevel(String secrectlevel) {
		this.secrectlevel = secrectlevel;
	}

	public String getComprehensivehandlesug() {
		return comprehensivehandlesug;
	}

	public void setComprehensivehandlesug(String comprehensivehandlesug) {
		this.comprehensivehandlesug = comprehensivehandlesug;
	}

	public String getLeaderhandlesug() {
		return leaderhandlesug;
	}

	public void setLeaderhandlesug(String leaderhandlesug) {
		this.leaderhandlesug = leaderhandlesug;
	}

	public String getMinisterhandlesug() {
		return ministerhandlesug;
	}

	public void setMinisterhandlesug(String ministerhandlesug) {
		this.ministerhandlesug = ministerhandlesug;
	}

	public String getSendusersign() {
		return sendusersign;
	}

	public void setSendusersign(String sendusersign) {
		this.sendusersign = sendusersign;
	}

	public String getIssign() {
		return issign;
	}

	public void setIssign(String issign) {
		this.issign = issign;
	}

	public String getIshasreviewcost() {
		return ishasreviewcost;
	}

	public void setIshasreviewcost(String ishasreviewcost) {
		this.ishasreviewcost = ishasreviewcost;
	}

	public Date getSigndate() {
		return signdate;
	}

	public void setSigndate(Date signdate) {
		this.signdate = signdate;
	}

	public BigDecimal getSurplusdays() {
		return surplusdays;
	}

	public void setSurplusdays(BigDecimal surplusdays) {
		this.surplusdays = surplusdays;
	}

	public Date getExpectdispatchdate() {
		return expectdispatchdate;
	}

	public void setExpectdispatchdate(Date expectdispatchdate) {
		this.expectdispatchdate = expectdispatchdate;
	}

	public Date getReceivedate() {
		return receivedate;
	}

	public void setReceivedate(Date receivedate) {
		this.receivedate = receivedate;
	}

	public BigDecimal getDaysafterdispatch() {
		return daysafterdispatch;
	}

	public void setDaysafterdispatch(BigDecimal daysafterdispatch) {
		this.daysafterdispatch = daysafterdispatch;
	}

	public BigDecimal getReviewdays() {
		return reviewdays;
	}

	public void setReviewdays(BigDecimal reviewdays) {
		this.reviewdays = reviewdays;
	}

	public String getIsassistproc() {
		return isassistproc;
	}

	public void setIsassistproc(String isassistproc) {
		this.isassistproc = isassistproc;
	}

	public String getMainchargeuserid() {
		return mainchargeuserid;
	}

	public void setMainchargeuserid(String mainchargeuserid) {
		this.mainchargeuserid = mainchargeuserid;
	}

	public String getReviewdeptid() {
		return reviewdeptid;
	}

	public void setReviewdeptid(String reviewdeptid) {
		this.reviewdeptid = reviewdeptid;
	}

	public String getFilenum() {
		return filenum;
	}

	public void setFilenum(String filenum) {
		this.filenum = filenum;
	}

	public String getDocnum() {
		return docnum;
	}

	public void setDocnum(String docnum) {
		this.docnum = docnum;
	}

	public String getIspause() {
		return ispause;
	}

	public void setIspause(String ispause) {
		this.ispause = ispause;
	}

	public BigDecimal getPausedays() {
		return pausedays;
	}

	public void setPausedays(BigDecimal pausedays) {
		this.pausedays = pausedays;
	}

	public Date getPausetime() {
		return pausetime;
	}

	public void setPausetime(Date pausetime) {
		this.pausetime = pausetime;
	}

	public String getPasedescription() {
		return pasedescription;
	}

	public void setPasedescription(String pasedescription) {
		this.pasedescription = pasedescription;
	}

	public Integer getSugProDealCount() {
		return sugProDealCount;
	}

	public void setSugProDealCount(Integer sugProDealCount) {
		this.sugProDealCount = sugProDealCount;
	}

	public String getSugProDealOriginal() {
		return sugProDealOriginal;
	}

	public void setSugProDealOriginal(String sugProDealOriginal) {
		this.sugProDealOriginal = sugProDealOriginal;
	}

	public String getSugProDealCopy() {
		return sugProDealCopy;
	}

	public void setSugProDealCopy(String sugProDealCopy) {
		this.sugProDealCopy = sugProDealCopy;
	}

	public Integer getSugFileDealCount() {
		return sugFileDealCount;
	}

	public void setSugFileDealCount(Integer sugFileDealCount) {
		this.sugFileDealCount = sugFileDealCount;
	}

	public String getSugFileDealOriginal() {
		return sugFileDealOriginal;
	}

	public void setSugFileDealOriginal(String sugFileDealOriginal) {
		this.sugFileDealOriginal = sugFileDealOriginal;
	}

	public String getSugFileDealCopy() {
		return sugFileDealCopy;
	}

	public void setSugFileDealCopy(String sugFileDealCopy) {
		this.sugFileDealCopy = sugFileDealCopy;
	}

	public Integer getSugOrgApplyCount() {
		return sugOrgApplyCount;
	}

	public void setSugOrgApplyCount(Integer sugOrgApplyCount) {
		this.sugOrgApplyCount = sugOrgApplyCount;
	}

	public String getSugOrgApplyOriginal() {
		return sugOrgApplyOriginal;
	}

	public void setSugOrgApplyOriginal(String sugOrgApplyOriginal) {
		this.sugOrgApplyOriginal = sugOrgApplyOriginal;
	}

	public String getSugOrgApplyCopy() {
		return sugOrgApplyCopy;
	}

	public void setSugOrgApplyCopy(String sugOrgApplyCopy) {
		this.sugOrgApplyCopy = sugOrgApplyCopy;
	}

	public Integer getSugOrgReqCount() {
		return sugOrgReqCount;
	}

	public void setSugOrgReqCount(Integer sugOrgReqCount) {
		this.sugOrgReqCount = sugOrgReqCount;
	}

	public String getSugOrgReqOriginal() {
		return sugOrgReqOriginal;
	}

	public void setSugOrgReqOriginal(String sugOrgReqOriginal) {
		this.sugOrgReqOriginal = sugOrgReqOriginal;
	}

	public String getSugOrgReqCopy() {
		return sugOrgReqCopy;
	}

	public void setSugOrgReqCopy(String sugOrgReqCopy) {
		this.sugOrgReqCopy = sugOrgReqCopy;
	}

	public Integer getSugProAdviseCount() {
		return sugProAdviseCount;
	}

	public void setSugProAdviseCount(Integer sugProAdviseCount) {
		this.sugProAdviseCount = sugProAdviseCount;
	}

	public String getSugProAdviseOriginal() {
		return sugProAdviseOriginal;
	}

	public void setSugProAdviseOriginal(String sugProAdviseOriginal) {
		this.sugProAdviseOriginal = sugProAdviseOriginal;
	}

	public String getSugProAdviseCopy() {
		return sugProAdviseCopy;
	}

	public void setSugProAdviseCopy(String sugProAdviseCopy) {
		this.sugProAdviseCopy = sugProAdviseCopy;
	}

	public Integer getProSugEledocCount() {
		return proSugEledocCount;
	}

	public void setProSugEledocCount(Integer proSugEledocCount) {
		this.proSugEledocCount = proSugEledocCount;
	}

	public String getProSugEledocOriginal() {
		return proSugEledocOriginal;
	}

	public void setProSugEledocOriginal(String proSugEledocOriginal) {
		this.proSugEledocOriginal = proSugEledocOriginal;
	}

	public String getProSugEledocCopy() {
		return proSugEledocCopy;
	}

	public void setProSugEledocCopy(String proSugEledocCopy) {
		this.proSugEledocCopy = proSugEledocCopy;
	}

	public Integer getSugMeetCount() {
		return sugMeetCount;
	}

	public void setSugMeetCount(Integer sugMeetCount) {
		this.sugMeetCount = sugMeetCount;
	}

	public String getSugMeetOriginal() {
		return sugMeetOriginal;
	}

	public void setSugMeetOriginal(String sugMeetOriginal) {
		this.sugMeetOriginal = sugMeetOriginal;
	}

	public String getSugMeetCopy() {
		return sugMeetCopy;
	}

	public void setSugMeetCopy(String sugMeetCopy) {
		this.sugMeetCopy = sugMeetCopy;
	}

	public Integer getStudyProDealCount() {
		return studyProDealCount;
	}

	public void setStudyProDealCount(Integer studyProDealCount) {
		this.studyProDealCount = studyProDealCount;
	}

	public String getStudyPealOriginal() {
		return studyPealOriginal;
	}

	public void setStudyPealOriginal(String studyPealOriginal) {
		this.studyPealOriginal = studyPealOriginal;
	}

	public String getStudyProDealCopy() {
		return studyProDealCopy;
	}

	public void setStudyProDealCopy(String studyProDealCopy) {
		this.studyProDealCopy = studyProDealCopy;
	}

	public Integer getStudyFileDealCount() {
		return studyFileDealCount;
	}

	public void setStudyFileDealCount(Integer studyFileDealCount) {
		this.studyFileDealCount = studyFileDealCount;
	}

	public String getStudyFileDealOriginal() {
		return studyFileDealOriginal;
	}

	public void setStudyFileDealOriginal(String studyFileDealOriginal) {
		this.studyFileDealOriginal = studyFileDealOriginal;
	}

	public String getStudyFileDealCopy() {
		return studyFileDealCopy;
	}

	public void setStudyFileDealCopy(String studyFileDealCopy) {
		this.studyFileDealCopy = studyFileDealCopy;
	}

	public Integer getStudyOrgApplyCount() {
		return studyOrgApplyCount;
	}

	public void setStudyOrgApplyCount(Integer studyOrgApplyCount) {
		this.studyOrgApplyCount = studyOrgApplyCount;
	}

	public String getStudyOrgApplyOriginal() {
		return studyOrgApplyOriginal;
	}

	public void setStudyOrgApplyOriginal(String studyOrgApplyOriginal) {
		this.studyOrgApplyOriginal = studyOrgApplyOriginal;
	}

	public String getStudyOrgApplyCopy() {
		return studyOrgApplyCopy;
	}

	public void setStudyOrgApplyCopy(String studyOrgApplyCopy) {
		this.studyOrgApplyCopy = studyOrgApplyCopy;
	}

	public Integer getStudyOrgReqCount() {
		return studyOrgReqCount;
	}

	public void setStudyOrgReqCount(Integer studyOrgReqCount) {
		this.studyOrgReqCount = studyOrgReqCount;
	}

	public String getStudyOrgReqOriginal() {
		return studyOrgReqOriginal;
	}

	public void setStudyOrgReqOriginal(String studyOrgReqOriginal) {
		this.studyOrgReqOriginal = studyOrgReqOriginal;
	}

	public String getStudyOrgReqCopy() {
		return studyOrgReqCopy;
	}

	public void setStudyOrgReqCopy(String studyOrgReqCopy) {
		this.studyOrgReqCopy = studyOrgReqCopy;
	}

	public Integer getStudyProSugCount() {
		return studyProSugCount;
	}

	public void setStudyProSugCount(Integer studyProSugCount) {
		this.studyProSugCount = studyProSugCount;
	}

	public String getStudyProSugOriginal() {
		return studyProSugOriginal;
	}

	public void setStudyProSugOriginal(String studyProSugOriginal) {
		this.studyProSugOriginal = studyProSugOriginal;
	}

	public String getStudyProSugCopy() {
		return studyProSugCopy;
	}

	public void setStudyProSugCopy(String studyProSugCopy) {
		this.studyProSugCopy = studyProSugCopy;
	}

	public Integer getStudyMeetCount() {
		return studyMeetCount;
	}

	public void setStudyMeetCount(Integer studyMeetCount) {
		this.studyMeetCount = studyMeetCount;
	}

	public String getStudyMeetOriginal() {
		return studyMeetOriginal;
	}

	public void setStudyMeetOriginal(String studyMeetOriginal) {
		this.studyMeetOriginal = studyMeetOriginal;
	}

	public String getStudyMeetCopy() {
		return studyMeetCopy;
	}

	public void setStudyMeetCopy(String studyMeetCopy) {
		this.studyMeetCopy = studyMeetCopy;
	}

	public Integer getEnvproReplyCount() {
		return envproReplyCount;
	}

	public void setEnvproReplyCount(Integer envproReplyCount) {
		this.envproReplyCount = envproReplyCount;
	}

	public String getEnvproReplyOriginal() {
		return envproReplyOriginal;
	}

	public void setEnvproReplyOriginal(String envproReplyOriginal) {
		this.envproReplyOriginal = envproReplyOriginal;
	}

	public String getEnvproReplyCopy() {
		return envproReplyCopy;
	}

	public void setEnvproReplyCopy(String envproReplyCopy) {
		this.envproReplyCopy = envproReplyCopy;
	}

	public Integer getPlanAddrCount() {
		return planAddrCount;
	}

	public void setPlanAddrCount(Integer planAddrCount) {
		this.planAddrCount = planAddrCount;
	}

	public String getPlanAddrOriginal() {
		return planAddrOriginal;
	}

	public void setPlanAddrOriginal(String planAddrOriginal) {
		this.planAddrOriginal = planAddrOriginal;
	}

	public String getPlanAddrCopy() {
		return planAddrCopy;
	}

	public void setPlanAddrCopy(String planAddrCopy) {
		this.planAddrCopy = planAddrCopy;
	}

	public Integer getReportCount() {
		return reportCount;
	}

	public void setReportCount(Integer reportCount) {
		this.reportCount = reportCount;
	}

	public String getReportOrigin() {
		return reportOrigin;
	}

	public void setReportOrigin(String reportOrigin) {
		this.reportOrigin = reportOrigin;
	}

	public String getReportCopy() {
		return reportCopy;
	}

	public void setReportCopy(String reportCopy) {
		this.reportCopy = reportCopy;
	}

	public Integer getEledocCount() {
		return eledocCount;
	}

	public void setEledocCount(Integer eledocCount) {
		this.eledocCount = eledocCount;
	}

	public String getEledocOriginal() {
		return eledocOriginal;
	}

	public void setEledocOriginal(String eledocOriginal) {
		this.eledocOriginal = eledocOriginal;
	}

	public String getEledocCopy() {
		return eledocCopy;
	}

	public void setEledocCopy(String eledocCopy) {
		this.eledocCopy = eledocCopy;
	}

	public Integer getEnergyCount() {
		return energyCount;
	}

	public void setEnergyCount(Integer energyCount) {
		this.energyCount = energyCount;
	}

	public String getEnergyOriginal() {
		return energyOriginal;
	}

	public void setEnergyOriginal(String energyOriginal) {
		this.energyOriginal = energyOriginal;
	}

	public String getEnergyCopy() {
		return energyCopy;
	}

	public void setEnergyCopy(String energyCopy) {
		this.energyCopy = energyCopy;
	}

	public String getIsreviewcompleted() {
		return isreviewcompleted;
	}

	public void setIsreviewcompleted(String isreviewcompleted) {
		this.isreviewcompleted = isreviewcompleted;
	}	
	
	public String getFolwState() {
		return folwState;
	}

	public void setFolwState(String folwState) {
		this.folwState = folwState;
	}
	
	public String getSignState() {
		return signState;
	}

	public void setSignState(String signState) {
		this.signState = signState;
	}

	public WorkProgram getWorkProgram() {
		return workProgram;
	}

	public void setWorkProgram(WorkProgram workProgram) {
		this.workProgram = workProgram;
	}

	public String getMaindeptName() {
		return maindeptName;
	}

	public void setMaindeptName(String maindeptName) {
		this.maindeptName = maindeptName;
	}

	public String getMainDeptUserName() {
		return mainDeptUserName;
	}

	public void setMainDeptUserName(String mainDeptUserName) {
		this.mainDeptUserName = mainDeptUserName;
	}


	public String getAssistdeptName() {
		return assistdeptName;
	}

	public void setAssistdeptName(String assistdeptName) {
		this.assistdeptName = assistdeptName;
	}

	public String getAssistDeptUserName() {
		return assistDeptUserName;
	}

	public void setAssistDeptUserName(String assistDeptUserName) {
		this.assistDeptUserName = assistDeptUserName;
	}

	
	public DispatchDoc getDispatchDoc() {
		return dispatchDoc;
	}

	public void setDispatchDoc(DispatchDoc dispatchDoc) {
		this.dispatchDoc = dispatchDoc;
	}
	
	public String getIsDispatchCompleted() {
		return isDispatchCompleted;
	}

	public void setIsDispatchCompleted(String isDispatchCompleted) {
		this.isDispatchCompleted = isDispatchCompleted;
	}
}