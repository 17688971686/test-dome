package cs.model.project;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;

import com.alibaba.fastjson.annotation.JSONField;

import cs.model.BaseDto;

public class DispatchDocDto extends BaseDto {
    private String id;

    //发文方式
    private String dispatchWay;

    //项目类别
    private String projectType;

    //发文类别
    private String dispatchType;

    //文件标题
    private String fileTitle;
    
    //评审方式
    private String dispatchStage;
    
    //发文部门
    private String orgId;

    //拟稿人
    private String userId;

    //拟稿日期
    @JSONField(format = "yyyy-MM-dd")
    private Date draftDate;

    //密码等级
    private String secretLevel;

    //年度计划
    private String yearPlan;

    //紧急等级
    private String urgentLevel;

    //发文日期
    @JSONField(format = "yyyy-MM-dd")
    private Date dispatchDate;

    //文件字号
    private String fileNum;

    //文件序号
    private Integer fileSeq;

    //发文范围
    private String dispatchScope;

    //打印份数
    private Integer printCount;

    //相关说明
    private String description;

    //评审意见摘要
    private String reviewAbstract;

    //部长核稿意见
    private String ministerSuggesttion;

    //部长核稿日期
    @JSONField(format = "yyyy-MM-dd")
    private Date ministerDate;

    private String ministerName;//部长名称

    //分管副局长核稿意见
    private String viceDirectorSuggesttion;

    //分管副局长核稿日期
    @JSONField(format = "yyyy-MM-dd")
    private Date viceDirectorDate;

    private String viceDirectorName;//分管副局长名称


    //局长核稿意见
    private String directorSuggesttion;

    //局长核稿日期
    @JSONField(format = "yyyy-MM-dd")
    private Date directorDate;

    private String directorName;//局长名称

    //申报金额
    private BigDecimal declareValue;

    //审定金额
    private BigDecimal authorizeValue;

    //核减（增）金额
    private BigDecimal extraValue;

    //增减（增）率
    private BigDecimal extraRate;

    //批复金额
    private BigDecimal approveValue;

    //备注
    private String remark;

    //项目建设必要性
    private String projectBuildNecess;

    //审定建设规模及内容
    private String buildSizeContent;

    //投资匡算及资金来源
    private String fundTotalOrigin;

    //下一阶段工作要求
    private String nextWorkPlan;

    //编号对应的字段
    //拟稿人名称
    private String userName;

    //发文部门名称
    private String orgName;

    //校对人名称
    private String proofreadName;

    //签收ID
    private String signId;

    //是否与其他表关联
    private String isRelated;

    //是否是主项目
    private String isMainProject;

    //收文对象
    private SignDto signDto;

    public SignDto getSignDto() {
        return signDto;
    }

    //第一负责人意见
    private String mianChargeSuggest;

    //第二负责人意见
    private String secondChargeSuggest;

    //关联阶段的发文申报信息
    private List<DispatchDocDto> associateDispatchs;

    public List<DispatchDocDto> getAssociateDispatchs() {
        return associateDispatchs;
    }

    public void setAssociateDispatchs(List<DispatchDocDto> associateDispatchs) {
        this.associateDispatchs = associateDispatchs;
    }

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

    public void setSignDto(SignDto signDto) {
        this.signDto = signDto;
    }

    public Date getDraftDate() {
        return draftDate;
    }

    public void setDraftDate(Date draftDate) {
        this.draftDate = draftDate;
    }

    public Date getDispatchDate() {
        return dispatchDate;
    }

    public void setDispatchDate(Date dispatchDate) {
        this.dispatchDate = dispatchDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public void setPrintCount(Integer printCount) {
        this.printCount = printCount;
    }

    public Integer getPrintCount() {
        return printCount;
    }

    public String getSignId() {
        return signId;
    }

    public void setSignId(String signId) {
        this.signId = signId;
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

    public String getIsRelated() {
        return isRelated;
    }

    public void setIsRelated(String isRelated) {
        this.isRelated = isRelated;
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

    public Integer getFileSeq() {
        return fileSeq;
    }

    public void setFileSeq(Integer fileSeq) {
        this.fileSeq = fileSeq;
    }

	public String getDispatchStage() {
		return dispatchStage;
	}

	public void setDispatchStage(String dispatchStage) {
		this.dispatchStage = dispatchStage;
	}
    
}
