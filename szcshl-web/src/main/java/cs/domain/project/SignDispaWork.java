package cs.domain.project;


import cs.domain.DomainBase;

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

    @Temporal(TemporalType.TIMESTAMP)
    @Column
    private Date signdate;          //签收日期（收文日期）

    @Column
    private String projectname;    //项目名称*

    @Column
    private String reviewstage;    //评审阶段*

    @Column
    private String builtcompanyname;//建设单位*

    @Column
    private String appalyinvestment;//申报投资

    @Column(columnDefinition = "INTEGER")
    private String isAssociate;     //是否已关联*

    @Column
    private String processInstanceId;   //流程实例ID

    @Temporal(TemporalType.TIMESTAMP)
    @Column
    private Date receivedate;       //送来日期

    @Column(columnDefinition = "NUMBER")
    private Float daysafterdispatch; //发文后工作日


    @Column(columnDefinition = "NUMBER")
    private Float reviewdays; //评审天数

    //秘密登记
    @Column
    private String secrectlevel;

    //缓急程度
    @Column
    private String urgencydegree;

    //剩余工作日
    @Column(columnDefinition = "NUMBER")
    private Float surplusdays;

    //归档编号
    @Column
    private String ffilenum;

    //是否有环评
    @Column
    private String ishaveeia;

    //项目类别
    @Column
    private String projectType;

    //评审方式
    @Column
    private String reviewType;

    //行业类别
    @Column
    private String industryType;

    //主负责人名称
    @Column
    private String mUserName;

    //负责人名称
    @Column
    private String aUserName;

    //评审部门
    @Column
    private String reviewOrgName;

    //发文编号
    @Column
    private String dfilenum;

    //核减（增）金额
    @Column(columnDefinition = "NUMBER")
    private BigDecimal extraValue;

    //增减（增）率
    @Column(columnDefinition = "NUMBER")
    private BigDecimal extraRate;

    //批复金额
    @Column(columnDefinition = "NUMBER")
    private BigDecimal approveValue;

    //审定金额
    @Column(columnDefinition = "NUMBER")
    private BigDecimal authorizeValue;

    //发文类别
    @Column
    private String dispatchType;

    //发文日期
    @Temporal(TemporalType.TIMESTAMP)
    @Column
    private Date dispatchDate;

    //主办部门
    @Column
    private String mOrgName;

    //主办部门ID
    @Column
    private String mOrgId;

    @Temporal(TemporalType.TIMESTAMP)
    @Column
    private Date fileDate;


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

    public String getIsAssociate() {
        return isAssociate;
    }

    public void setIsAssociate(String isAssociate) {
        this.isAssociate = isAssociate;
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

    public String getaUserName() {
        return aUserName;
    }

    public void setaUserName(String aUserName) {
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
}