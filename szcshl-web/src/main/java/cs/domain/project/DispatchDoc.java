
package cs.domain.project;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Formula;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.SerializerFeature;

import cs.domain.DomainBase;

/**
 * 发文
 * @author ldm
 *
 */
@Entity
@Table(name = "cs_dispatch_doc")
@DynamicUpdate(true)
public class DispatchDoc extends DomainBase{

	//主键
	@Id
	private String id;	
	
	//发文方式
	@Column(columnDefinition="VARCHAR(20)")
	private String dispatchWay;
	
	//项目类别
	@Column(columnDefinition="VARCHAR(2)")
	private String projectType;
	
	//发文类别
	@Column(columnDefinition="VARCHAR(20)")
	private String dispatchType;
	
	//文件标题
	@Column(columnDefinition="VARCHAR(256)")
	private String fileTitle;
	
	//发文部门
	@Column(columnDefinition="VARCHAR(64)")
	private String orgId;
	
	//拟稿人
	@Column(columnDefinition="VARCHAR(64)")
	private String userId;
	
	//拟稿日期
	@Column(columnDefinition="DATE")
	private Date draftDate;
	
	//密码等级
	@Column(columnDefinition="VARCHAR(64)")
	private String secretLevel;
	
	//年度计划
	@Column(columnDefinition="VARCHAR(64)")
	private String yearPlan;
	
	//紧急等级
	@Column(columnDefinition="VARCHAR(64)")
	private String urgentLevel;
	
	//发文日期
	@Column(columnDefinition="DATE")
	private Date dispatchDate;
	
	//文件字号
	@Column(columnDefinition="VARCHAR(64)")
	private String fileNum;
	
	//发文范围
	@Column(columnDefinition="VARCHAR(512)")
	private String dispatchScope;
	//打印份数
	@Column(columnDefinition="INTEGER")
	private Integer printCount;
	
	//相关说明
	@Column(columnDefinition="VARCHAR(2000)")
	private String description;
	
	//评审意见摘要
	@Column(columnDefinition="VARCHAR(2000)")
	private String reviewAbstract;
	
	//部长核稿意见
	@Column(columnDefinition="VARCHAR(2000)")
	private String ministerSuggesttion;
	
	//部长核稿日期
	@Column(columnDefinition="DATE")
	private Date ministerDate;
	
	@Column(columnDefinition="VARCHAR(100)")
	private String ministerName; //部长名称
	
	//分管副局长核稿意见
	@Column(columnDefinition="VARCHAR(2000)")
	private String viceDirectorSuggesttion;
	
	@Column(columnDefinition="VARCHAR(100)")
	private String viceDirectorName;//分管副局长名称
	
	//分管副局长核稿日期
	@Column(columnDefinition="DATE")
	private Date viceDirectorDate;
	
	//局长核稿意见
	@Column(columnDefinition="VARCHAR(2000)")
	private String directorSuggesttion;
	
	//局长核稿日期
	@Column(columnDefinition="DATE")
	private Date directorDate;
	
	@Column(columnDefinition="VARCHAR(100)")
	private String directorName;//局长名称
	
	//主任签发
	@Column(columnDefinition="VARCHAR(2000)")
	private String DirectorIssue;

	//申报金额
	@Column(columnDefinition="NUMBER")
	private BigDecimal declareValue;
	
	//审定金额
	@Column(columnDefinition="NUMBER")
	private BigDecimal authorizeValue;
	
	//核减（增）金额
	@Column(columnDefinition="NUMBER")
	private BigDecimal extraValue;
	
	//增减（增）率
	@Column(columnDefinition="NUMBER")
	private BigDecimal extraRate;
	
	//批复金额
	@Column(columnDefinition="NUMBER")
	private BigDecimal approveValue;
	
	//备注
	@Column(columnDefinition="VARCHAR(2000)")
	private String remark;
	
	//项目建设必要性
	@Column(columnDefinition="VARCHAR(2000)")
	private String projectBuildNecess;
	
	//审定建设规模及内容
	@Column(columnDefinition="VARCHAR(2000)")
	private String buildSizeContent;
	
	//投资匡算及资金来源
	@Column(columnDefinition="VARCHAR(2000)")
	private String fundTotalOrigin;
	
	//下一阶段工作要求
	@Column(columnDefinition="VARCHAR(2000)")
	private String nextWorkPlan;
	
	//是否有其他关联
	@Column(columnDefinition="VARCHAR(30)")
	private String isRelated;
	
	//第一负责人意见
	@Column(columnDefinition="VARCHAR(2000)")
	private String mianChargeSuggest;
	
	//第二负责人意见
	@Column(columnDefinition="VARCHAR(2000)")
	private String secondChargeSuggest;
	
	//收文，一对一
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="signId",unique = true)
	private Sign sign;
	
	//编号对应的字段
	//拟稿人名称
	@Column(columnDefinition="VARCHAR(225)")
	@Formula("(select u.loginName from cs_user u where u.id = userId)")
	private String userName;
	
	//发文部门名称
	@Column(columnDefinition="VARCHAR(225)")
	@Formula("(select o.name from cs_org o where o.id = orgId)")
	private String orgName;
	
	//校对人名称
	@Column(columnDefinition="VARCHAR(225)")
	private String proofreadName;
	
	//是否为主项目
	private String isMainProject;//(3：主项目，4：次项目)
	
	
	public String getMianChargeSuggest() {
		return mianChargeSuggest;
	}

	public void setMianChargeSuggest(String mianChargeSuggest) {
		this.mianChargeSuggest = mianChargeSuggest;
	}

	public String getSecondChargeSuggest() {
		return secondChargeSuggest;
	}

	public void setSecondChargeSuggest(String secondChargeSuggest) {
		this.secondChargeSuggest = secondChargeSuggest;
	}

	public String getIsRelated() {
		return isRelated;
	}

	public void setIsRelated(String isRelated) {
		this.isRelated = isRelated;
	}

	public String getDirectorIssue() {
		return DirectorIssue;
	}

	public void setDirectorIssue(String directorIssue) {
		DirectorIssue = directorIssue;
	}

	public BigDecimal getDeclareValue() {
		return declareValue;
	}

	public void setDeclareValue(BigDecimal declareValue) {
		this.declareValue = declareValue;
	}

	public BigDecimal getAuthorizeValue() {
		return authorizeValue;
	}

	public void setAuthorizeValue(BigDecimal authorizeValue) {
		this.authorizeValue = authorizeValue;
	}

	public BigDecimal getExtraValue() {
		return extraValue;
	}

	public void setExtraValue(BigDecimal extraValue) {
		this.extraValue = extraValue;
	}

	public BigDecimal getExtraRate() {
		return extraRate;
	}

	public void setExtraRate(BigDecimal extraRate) {
		this.extraRate = extraRate;
	}

	public BigDecimal getApproveValue() {
		return approveValue;
	}

	public void setApproveValue(BigDecimal approveValue) {
		this.approveValue = approveValue;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getProjectBuildNecess() {
		return projectBuildNecess;
	}

	public void setProjectBuildNecess(String projectBuildNecess) {
		this.projectBuildNecess = projectBuildNecess;
	}

	public String getBuildSizeContent() {
		return buildSizeContent;
	}

	public void setBuildSizeContent(String buildSizeContent) {
		this.buildSizeContent = buildSizeContent;
	}

	public String getFundTotalOrigin() {
		return fundTotalOrigin;
	}

	public void setFundTotalOrigin(String fundTotalOrigin) {
		this.fundTotalOrigin = fundTotalOrigin;
	}

	public String getNextWorkPlan() {
		return nextWorkPlan;
	}

	public void setNextWorkPlan(String nextWorkPlan) {
		this.nextWorkPlan = nextWorkPlan;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public String getProofreadName() {
		return proofreadName;
	}

	public void setProofreadName(String proofreadName) {
		this.proofreadName = proofreadName;
	}

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

	public String getDispatchWay() {
		return dispatchWay;
	}

	public void setDispatchWay(String dispatchWay) {
		this.dispatchWay = dispatchWay;
	}

	public String getProjectType() {
		return projectType;
	}

	public void setProjectType(String projectType) {
		this.projectType = projectType;
	}

	public String getDispatchType() {
		return dispatchType;
	}

	public void setDispatchType(String dispatchType) {
		this.dispatchType = dispatchType;
	}

	public String getFileTitle() {
		return fileTitle;
	}

	public void setFileTitle(String fileTitle) {
		this.fileTitle = fileTitle;
	}

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Date getDraftDate() {
		return draftDate;
	}

	public void setDraftDate(Date draftDate) {
		this.draftDate = draftDate;
	}

	public String getSecretLevel() {
		return secretLevel;
	}

	public void setSecretLevel(String secretLevel) {
		this.secretLevel = secretLevel;
	}

	public String getYearPlan() {
		return yearPlan;
	}

	public void setYearPlan(String yearPlan) {
		this.yearPlan = yearPlan;
	}

	public String getUrgentLevel() {
		return urgentLevel;
	}

	public void setUrgentLevel(String urgentLevel) {
		this.urgentLevel = urgentLevel;
	}

	public Date getDispatchDate() {
		return dispatchDate;
	}

	public void setDispatchDate(Date dispatchDate) {
		this.dispatchDate = dispatchDate;
	}

	public String getFileNum() {
		return fileNum;
	}

	public void setFileNum(String fileNum) {
		this.fileNum = fileNum;
	}

	public String getDispatchScope() {
		return dispatchScope;
	}

	public void setDispatchScope(String dispatchScope) {
		this.dispatchScope = dispatchScope;
	}

	public Integer getPrintCount() {
		return printCount;
	}

	public void setPrintCount(Integer printCount) {
		this.printCount = printCount;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getReviewAbstract() {
		return reviewAbstract;
	}

	public void setReviewAbstract(String reviewAbstract) {
		this.reviewAbstract = reviewAbstract;
	}

	public String getMinisterSuggesttion() {
		return ministerSuggesttion;
	}

	public void setMinisterSuggesttion(String ministerSuggesttion) {
		this.ministerSuggesttion = ministerSuggesttion;
	}

	public Date getMinisterDate() {
		return ministerDate;
	}

	public void setMinisterDate(Date ministerDate) {
		this.ministerDate = ministerDate;
	}

	public String getViceDirectorSuggesttion() {
		return viceDirectorSuggesttion;
	}

	public void setViceDirectorSuggesttion(String viceDirectorSuggesttion) {
		this.viceDirectorSuggesttion = viceDirectorSuggesttion;
	}

	public Date getViceDirectorDate() {
		return viceDirectorDate;
	}

	public void setViceDirectorDate(Date viceDirectorDate) {
		this.viceDirectorDate = viceDirectorDate;
	}

	public String getDirectorSuggesttion() {
		return directorSuggesttion;
	}

	public void setDirectorSuggesttion(String directorSuggesttion) {
		this.directorSuggesttion = directorSuggesttion;
	}

	public Date getDirectorDate() {
		return directorDate;
	}

	public void setDirectorDate(Date directorDate) {
		this.directorDate = directorDate;
	}

	public String getIsMainProject() {
		return isMainProject;
	}

	public void setIsMainProject(String isMainProject) {
		this.isMainProject = isMainProject;
	}

	public String getMinisterName() {
		return ministerName;
	}

	public void setMinisterName(String ministerName) {
		this.ministerName = ministerName;
	}

	public String getViceDirectorName() {
		return viceDirectorName;
	}

	public void setViceDirectorName(String viceDirectorName) {
		this.viceDirectorName = viceDirectorName;
	}

	public String getDirectorName() {
		return directorName;
	}

	public void setDirectorName(String directorName) {
		this.directorName = directorName;
	}
	
	
			
}

