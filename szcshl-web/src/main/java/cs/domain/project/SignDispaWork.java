package cs.domain.project;


import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 设置为动态更新，只更新有修改的字段
 * (对应视图 V_SIGN_DISP_WORK)
 *
 * @author ldm
 */
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Table(name = "V_SIGN_DISP_WORK")
public class SignDispaWork {

    /**
     * 收文ID
     */
    @Id
    private String signid;

    /**
     * 是否是优秀评审项目 9： 是    0或其他：否
     */

    @Column
    private String isAppraising;

    /**
     * 项目签收日期
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column
    private Date signdate;

    /**
     * 收文编号
     */
    @Column
    private String signnum;

    /**
     * 项目代码
     */
    @Column
    private String projectcode;

    /**
     * 项目名称
     */
    @Column
    private String projectname;

    /**
     * 评审阶段
     */
    @Column
    private String reviewstage;

    /**
     * 建设单位
     */
    @Column
    private String builtcompanyname;

    /**
     * 投资申报
     */
    @Column
    private String appalyinvestment;

    /**
     * 是否有其他关联
     */
    @Column(columnDefinition = "INTEGER")
    private String isRelated;

    /**
     * 流程实例ID
     */
    @Column
    private String processInstanceId;

    /**
     * 项目进程状态
     */
    private String processState;

    /**
     * 送来日期
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column
    private Date receivedate;

    /**
     * 发文后工作日
     */
    @Column(columnDefinition = "NUMBER")
    private Float daysafterdispatch;

    /**
     * 评审天数
     */
    @Column(columnDefinition = "NUMBER")
    private Float reviewdays;

    /**
     * 秘密等级
     */
    @Column
    private String secrectlevel;

    /**
     * 缓急程度
     */
    @Column
    private String urgencydegree;

    /**
     * 剩余工作日
     */
    @Column(columnDefinition = "NUMBER")
    private Float surplusdays;

    /**
     * 归档编号
     */
    @Column
    private String ffilenum;

    /**
     * 是否有环评
     */
    @Column
    private String ishaveeia;

    /**
     * 项目类别
     */
    @Column
    private String projectType;

    /**
     * 评审方式
     */
    @Column
    private String reviewType;

    /**
     * 行业类别
     */
    @Column
    private String industryType;

    /**
     * 主负责人ID
     */
    @Column
    private String mUserId;

    /**
     * 主负责人名称
     */
    @Column
    private String mUserName;

    /**
     * 负责人ID
     */
    @Column
    private String aUserID;
    /**
     * 负责人名称
     */
    @Column
    private String aUserName;

    /**
     * 中心领导名称
     */
    @Column
    private String leaderName;

    /**
     * 部门负责人
     */
    @Column
    private String ministerName;

    /**
     * 评审部门
     */
    @Column
    private String reviewOrgName;

    /**
     * 发文编号
     */
    @Column
    private String dfilenum;

    /**
     * 核减（增）金额
     */
    @Column(columnDefinition = "NUMBER")
    private BigDecimal extraValue;

    /**
     * 增减（增）率
     */
    @Column(columnDefinition = "NUMBER")
    private BigDecimal extraRate;

    /**
     * 批复金额
     */
    @Column(columnDefinition = "NUMBER")
    private BigDecimal approveValue;

    /**
     * 审定金额
     */
    @Column(columnDefinition = "NUMBER")
    private BigDecimal authorizeValue;

    /**
     * 发文类别
     */
    @Column
    private String dispatchType;

    /**
     * 发文日期
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column
    private Date dispatchDate;

    /**
     * 主办部门名称
     */
    @Column
    private String mOrgName;

    /**
     * 主办部门ID
     */
    @Column
    private String mOrgId;

    /**
     * 归档日期
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column
    private Date fileDate;

    //项目状态
    @Column
    private String signState;

    //是否有登记补充资料:9表示是,0表示否
    @Column
    private String isSupplementary;

    //是否有拟补充资料函:9表示是,0表示否
    @Column
    private String isHaveSuppLetter;

    /**
     * 是否协审
     */
    @Column
    private String isassistproc;


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

    public String getIsassistproc() {
        return isassistproc;
    }

    public void setIsassistproc(String isassistproc) {
        this.isassistproc = isassistproc;
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

    public String getSignid() {
        return signid;
    }

    public void setSignid(String signid) {
        this.signid = signid;
    }

    public Date getSigndate() {
        return signdate;
    }

    public void setSigndate(Date signdate) {
        this.signdate = signdate;
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

    public String getAppalyinvestment() {
        return appalyinvestment;
    }

    public void setAppalyinvestment(String appalyinvestment) {
        this.appalyinvestment = appalyinvestment;
    }

    public String getIsRelated() {
        return isRelated;
    }

    public void setIsRelated(String isRelated) {
        this.isRelated = isRelated;
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

    public String getmUserName() {
        return mUserName;
    }

    public void setmUserName(String mUserName) {
        this.mUserName = mUserName;
    }

    public String getAUserName() {
        return aUserName;
    }

    public void setAUserName(String aUserName) {
        this.aUserName = aUserName;
    }

    public String getReviewOrgName() {
        return reviewOrgName;
    }

    public void setReviewOrgName(String reviewOrgName) {
        this.reviewOrgName = reviewOrgName;
    }

    public String getFfilenum() {
        return ffilenum;
    }

    public void setFfilenum(String ffilenum) {
        this.ffilenum = ffilenum;
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

    public String getIsAppraising() {
        return isAppraising;
    }

    public void setIsAppraising(String isAppraising) {
        this.isAppraising = isAppraising;
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

    public String getmUserId() {
        return mUserId;
    }

    public void setmUserId(String mUserId) {
        this.mUserId = mUserId;
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
}