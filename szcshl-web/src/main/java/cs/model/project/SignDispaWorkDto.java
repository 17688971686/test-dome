package cs.model.project;

import com.alibaba.fastjson.annotation.JSONField;

import javax.persistence.Column;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 归档后视图
 */
public class SignDispaWorkDto {
    /**
     * 收文ID
     */
    private String signid;

    /**
     * 签收人（流程发起人）
     */
    private String createdBy;

    /**
     * 送件人签名，（流程发起人）
     */
    private String sendusersign;
    /**
     * 是否是优秀评审项目 9： 是    0或其他：否
     */

    private String isAppraising;

    /**
     * 是否确认签收(9:是，其他 否)
     */
    private String issign;

    /**
     * 委内收文编号
     */
    private String filecode;

    /**
     * 项目签收日期
     */
    @Temporal(TemporalType.TIMESTAMP)
    @JSONField(format = "yyyy-MM-dd")
    private Date signdate;

    /**
     * 评审中心收文编号(年份+收文类型+序号[序号保留3位数])
     */
    private String signnum;

    /**
     * 项目代码
     */
    private String projectcode;

    /**
     * 项目名称
     */
    private String projectname;

    /**
     * 评审阶段
     */
    private String reviewstage;

    /**
     * 建设单位
     */
    private String builtcompanyname;

    /**
     * 投资申报
     */
    private BigDecimal appalyInvestment;

    /**
     * 是否有其他关联
     */
    private String isRelated;

    /**
     * 流程实例ID
     */
    private String processInstanceId;

    /**
     * 项目进程状态
     */
    private String processState;

    /**
     * 收文日期
     */
    @Temporal(TemporalType.TIMESTAMP)
    @JSONField(format = "yyyy-MM-dd")
    private Date receivedate;

    /**
     * 发文后工作日
     */
    private Float daysafterdispatch;

    /**
     * 评审天数
     */
    private Float reviewdays;

    /**
     * 秘密等级
     */
    private String secrectlevel;

    /**
     * 缓急程度
     */
    private String urgencydegree;

    /**
     * 剩余工作日
     */
    private Float surplusdays;

    /**
     * 归档编号
     */
    private String ffilenum;

    /**
     * 是否有环评
     */
    private String ishaveeia;

    /**
     * 项目类别
     */
    private String projectType;

    /**
     * 项目类别(小类)
     */
    private String projectSubType;


    /**
     * 评审方式
     */
    private String reviewType;

    /**
     * 行业类别
     */
    private String industryType;

    /**
     * 主负责人ID
     */
    private String mUserId;

    /**
     * 主负责人名称
     */
    private String mUserName;

    /**
     * 负责人ID
     */
    private String aUserID;

    /**
     * 负责人名称
     */
    private String aUserName;

    /**
     * 所有负责人
     */
    private String allPriUser;

    /**
     * 中心领导名称
     */
    private String leaderName;

    /**
     * 部门负责人
     */
    private String ministerName;

    /**
     * 评审部门
     */
    private String reviewOrgName;

    /**
     * 发文编号
     */
    private String dfilenum;

    /**
     * 核减（增）金额
     */
    private BigDecimal extraValue;

    /**
     * 增减（增）率
     */
    private BigDecimal extraRate;

    /**
     * 批复金额
     */
    private BigDecimal approveValue;

    /**
     * 审定金额
     */
    private BigDecimal authorizeValue;

    /**
     * 发文类别
     */
    private String dispatchType;

    /**
     * 发文日期
     */
    @Temporal(TemporalType.TIMESTAMP)
    @JSONField(format = "yyyy-MM-dd")
    private Date dispatchDate;

    /**
     * 主办部门名称
     */
    private String mOrgName;

    /**
     * 主办部门ID
     */
    private String mOrgId;

    /**
     * 协办部门ID
     */
    private String aOrgId;

    /**
     * 协办部门名称
     */
    private String aOrgName;
    /**
     * 归档日期
     */
    @Temporal(TemporalType.TIMESTAMP)
    @JSONField(format = "yyyy-MM-dd")
    private Date fileDate;

    //项目状态
    private String signState;

    //是否有登记补充资料:9表示是,0表示否
    private String isSupplementary;

    //是否有拟补充资料函:9表示是,0表示否
    private String isHaveSuppLetter;

    /**
     * 是否协审
     */
    private String isassistproc;

    /**
     * 警示灯状态
     */
    private String lightState;

    /**
     * 是否已经发送发改委
     */
    private String isSendFGW;

    /**
     * 项目是否暂停
     */
    private String isProjectStop;

    /**
     * 旧项目ID
     */
    private Integer oldProjectId;

    /**
     * 是否项目预签收
     */
    private String ispresign;

    public String getSignid() {
        return signid;
    }

    public void setSignid(String signid) {
        this.signid = signid;
    }

    public String getIsAppraising() {
        return isAppraising;
    }

    public void setIsAppraising(String isAppraising) {
        this.isAppraising = isAppraising;
    }

    public String getIssign() {
        return issign;
    }

    public void setIssign(String issign) {
        this.issign = issign;
    }

    public String getFilecode() {
        return filecode;
    }

    public void setFilecode(String filecode) {
        this.filecode = filecode;
    }

    public Date getSigndate() {
        return signdate;
    }

    public void setSigndate(Date signdate) {
        this.signdate = signdate;
    }

    public String getSignnum() {
        return signnum;
    }

    public void setSignnum(String signnum) {
        this.signnum = signnum;
    }

    public String getProjectcode() {
        return projectcode;
    }

    public void setProjectcode(String projectcode) {
        this.projectcode = projectcode;
    }

    public String getProjectname() {
        return projectname;
    }

    public void setProjectname(String projectname) {
        this.projectname = projectname;
    }

    public String getReviewstage() {
        return reviewstage;
    }

    public void setReviewstage(String reviewstage) {
        this.reviewstage = reviewstage;
    }

    public String getBuiltcompanyname() {
        return builtcompanyname;
    }

    public void setBuiltcompanyname(String builtcompanyname) {
        this.builtcompanyname = builtcompanyname;
    }

    public BigDecimal getAppalyInvestment() {
        return appalyInvestment;
    }

    public void setAppalyInvestment(BigDecimal appalyInvestment) {
        this.appalyInvestment = appalyInvestment;
    }

    public String getIsRelated() {
        return isRelated;
    }

    public void setIsRelated(String isRelated) {
        this.isRelated = isRelated;
    }

    public String getProcessInstanceId() {
        return processInstanceId;
    }

    public void setProcessInstanceId(String processInstanceId) {
        this.processInstanceId = processInstanceId;
    }

    public String getProcessState() {
        return processState;
    }

    public void setProcessState(String processState) {
        this.processState = processState;
    }

    public Date getReceivedate() {
        return receivedate;
    }

    public void setReceivedate(Date receivedate) {
        this.receivedate = receivedate;
    }

    public Float getDaysafterdispatch() {
        return daysafterdispatch;
    }

    public void setDaysafterdispatch(Float daysafterdispatch) {
        this.daysafterdispatch = daysafterdispatch;
    }

    public Float getReviewdays() {
        return reviewdays;
    }

    public void setReviewdays(Float reviewdays) {
        this.reviewdays = reviewdays;
    }

    public String getSecrectlevel() {
        return secrectlevel;
    }

    public void setSecrectlevel(String secrectlevel) {
        this.secrectlevel = secrectlevel;
    }

    public String getUrgencydegree() {
        return urgencydegree;
    }

    public void setUrgencydegree(String urgencydegree) {
        this.urgencydegree = urgencydegree;
    }

    public Float getSurplusdays() {
        return surplusdays;
    }

    public void setSurplusdays(Float surplusdays) {
        this.surplusdays = surplusdays;
    }

    public String getFfilenum() {
        return ffilenum;
    }

    public void setFfilenum(String ffilenum) {
        this.ffilenum = ffilenum;
    }

    public String getIshaveeia() {
        return ishaveeia;
    }

    public void setIshaveeia(String ishaveeia) {
        this.ishaveeia = ishaveeia;
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

    public String getReviewType() {
        return reviewType;
    }

    public void setReviewType(String reviewType) {
        this.reviewType = reviewType;
    }

    public String getIndustryType() {
        return industryType;
    }

    public void setIndustryType(String industryType) {
        this.industryType = industryType;
    }

    public String getmUserId() {
        return mUserId;
    }

    public void setmUserId(String mUserId) {
        this.mUserId = mUserId;
    }

    public String getmUserName() {
        return mUserName;
    }

    public void setmUserName(String mUserName) {
        this.mUserName = mUserName;
    }

    public String getaUserID() {
        return aUserID;
    }

    public void setaUserID(String aUserID) {
        this.aUserID = aUserID;
    }

    public String getaUserName() {
        return aUserName;
    }

    public void setaUserName(String aUserName) {
        this.aUserName = aUserName;
    }

    public String getAllPriUser() {
        return allPriUser;
    }

    public void setAllPriUser(String allPriUser) {
        this.allPriUser = allPriUser;
    }

    public String getLeaderName() {
        return leaderName;
    }

    public void setLeaderName(String leaderName) {
        this.leaderName = leaderName;
    }

    public String getMinisterName() {
        return ministerName;
    }

    public void setMinisterName(String ministerName) {
        this.ministerName = ministerName;
    }

    public String getReviewOrgName() {
        return reviewOrgName;
    }

    public void setReviewOrgName(String reviewOrgName) {
        this.reviewOrgName = reviewOrgName;
    }

    public String getDfilenum() {
        return dfilenum;
    }

    public void setDfilenum(String dfilenum) {
        this.dfilenum = dfilenum;
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

    public BigDecimal getAuthorizeValue() {
        return authorizeValue;
    }

    public void setAuthorizeValue(BigDecimal authorizeValue) {
        this.authorizeValue = authorizeValue;
    }

    public String getDispatchType() {
        return dispatchType;
    }

    public void setDispatchType(String dispatchType) {
        this.dispatchType = dispatchType;
    }

    public Date getDispatchDate() {
        return dispatchDate;
    }

    public void setDispatchDate(Date dispatchDate) {
        this.dispatchDate = dispatchDate;
    }

    public String getmOrgName() {
        return mOrgName;
    }

    public void setmOrgName(String mOrgName) {
        this.mOrgName = mOrgName;
    }

    public String getmOrgId() {
        return mOrgId;
    }

    public void setmOrgId(String mOrgId) {
        this.mOrgId = mOrgId;
    }

    public Date getFileDate() {
        return fileDate;
    }

    public void setFileDate(Date fileDate) {
        this.fileDate = fileDate;
    }

    public String getSignState() {
        return signState;
    }

    public void setSignState(String signState) {
        this.signState = signState;
    }

    public String getIsSupplementary() {
        return isSupplementary;
    }

    public void setIsSupplementary(String isSupplementary) {
        this.isSupplementary = isSupplementary;
    }

    public String getIsHaveSuppLetter() {
        return isHaveSuppLetter;
    }

    public void setIsHaveSuppLetter(String isHaveSuppLetter) {
        this.isHaveSuppLetter = isHaveSuppLetter;
    }

    public String getIsassistproc() {
        return isassistproc;
    }

    public void setIsassistproc(String isassistproc) {
        this.isassistproc = isassistproc;
    }

    public String getLightState() {
        return lightState;
    }

    public void setLightState(String lightState) {
        this.lightState = lightState;
    }

    public String getIsSendFGW() {
        return isSendFGW;
    }

    public void setIsSendFGW(String isSendFGW) {
        this.isSendFGW = isSendFGW;
    }

    public String getIsProjectStop() {
        return isProjectStop;
    }

    public void setIsProjectStop(String isProjectStop) {
        this.isProjectStop = isProjectStop;
    }

    public Integer getOldProjectId() {
        return oldProjectId;
    }

    public void setOldProjectId(Integer oldProjectId) {
        this.oldProjectId = oldProjectId;
    }

    public String getIspresign() {
        return ispresign;
    }

    public void setIspresign(String ispresign) {
        this.ispresign = ispresign;
    }

    public String getaOrgId() {
        return aOrgId;
    }

    public void setaOrgId(String aOrgId) {
        this.aOrgId = aOrgId;
    }

    public String getaOrgName() {
        return aOrgName;
    }

    public void setaOrgName(String aOrgName) {
        this.aOrgName = aOrgName;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getSendusersign() {
        return sendusersign;
    }

    public void setSendusersign(String sendusersign) {
        this.sendusersign = sendusersign;
    }
}
