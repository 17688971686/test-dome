package cs.domain.project;


import cs.domain.DomainBase;
import cs.domain.expert.ExpertSelected;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Formula;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 设置为动态更新，只更新有修改的字段
 * (对应视图 V_RU_PROCESS_TASK)
 * @author ldm
 */
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Table(name = "V_SIGN_DISP_WORK")
public class SignDispaWork extends DomainBase {

    /**
     * 收文ID
     */
    @Id
    
    @Column
    private String projectname;	//项目名称
    
    @Column
    private String builtcompanyName;//建设单位
    
    @Column
    private String reviewstage;	//评审阶段
    
    @Column
    private String ishaveeia;	//是否有环评
    
    @Column
    private String isAssociate;	//是否已关联
    
    @Column
    private String dprojectType;	//项目类型
    
    @Column
    private String mianchargeusername;	//项目负责人
    
    @Column
    private String filenum;	//归档编号
    
    @Column
    private String mOrgName;	//主办部门
    @Column
    
    private String pmaindeptname;	//主管部门(改革前)
    
    @Column
    private String urgencydegree;	//缓急程度
    
  /*  @Column
    private String mOrgNameAfter;	//主管部门(改革后）
*/    
   /* @Column
    private String goodReview;	//优秀评审报告
*/    
    @Column
    private String reviewType;	//评审方式
    
    @Column
    private Date receivedate;	//收文日期
    
    /*@Column
    private String agreedate;	//批复时间
*/    
    @Column
    private String dfilenum;	//文件字号
    
    @Column
    private Date dispatchDate;	//发文日期
    
    @Column
    private Date fileDate;	//归档日期
    
    @Column
    private String dispatchType;	//发文类型
    
    @Column
    private String industryType;	//行业类型
    
    @Column
    private String secrectlevel;	//秘密等级
    
    @Column
    private String reviewdays;	//评审天数
    
    //@Column
    private String surplusdays;	//剩余工作日
    
    @Column
    private String appalyInvestment;	//申报投资
   
    @Column
    private String authorizeValue;	//审定投资
    
    @Column
    private String extraValue;	//核减（增）投资
    
    @Column
    private String extraRate;	//核减率
    
    @Column
    private String approveValue;	//批复金额
    
   // @Column
   // private String approveTime;	//批复来文时间
    
    @Column
    private String isassistproc;	//是否协审
    
    //@Column
    private String daysafterdispatch;	//发文后工作日
    
    @Column
    private String mOrgId;	//部长所属部门ID
    
	public String getProjectname() {
		return projectname;
	}

	
	public String getPmaindeptname() {
		return pmaindeptname;
	}

	public void setPmaindeptname(String pmaindeptname) {
		this.pmaindeptname = pmaindeptname;
	}

	public void setProjectname(String projectname) {
		this.projectname = projectname;
	}

	public String getmOrgId() {
		return mOrgId;
	}

	public void setmOrgId(String mOrgId) {
		this.mOrgId = mOrgId;
	}

	public String getBuiltcompanyName() {
		return builtcompanyName;
	}

	public String getMianchargeusername() {
		return mianchargeusername;
	}


	public void setMianchargeusername(String mianchargeusername) {
		this.mianchargeusername = mianchargeusername;
	}


	public String getReviewType() {
		return reviewType;
	}


	public void setReviewType(String reviewType) {
		this.reviewType = reviewType;
	}


	public void setBuiltcompanyName(String builtcompanyName) {
		this.builtcompanyName = builtcompanyName;
	}

	public String getReviewstage() {
		return reviewstage;
	}

	public void setReviewstage(String reviewstage) {
		this.reviewstage = reviewstage;
	}

	public String getIsAssociate() {
		return isAssociate;
	}

	public void setIsAssociate(String isAssociate) {
		this.isAssociate = isAssociate;
	}

	public String getFilenum() {
		return filenum;
	}

	public void setFilenum(String filenum) {
		this.filenum = filenum;
	}

	public String getUrgencydegree() {
		return urgencydegree;
	}

	public void setUrgencydegree(String urgencydegree) {
		this.urgencydegree = urgencydegree;
	}

	/*public String getGoodReview() {
		return goodReview;
	}

	public void setGoodReview(String goodReview) {
		this.goodReview = goodReview;
	}
*/

	public String getIshaveeia() {
		return ishaveeia;
	}

	public void setIshaveeia(String ishaveeia) {
		this.ishaveeia = ishaveeia;
	}

	public String getDprojectType() {
		return dprojectType;
	}

	public void setDprojectType(String dprojectType) {
		this.dprojectType = dprojectType;
	}


	public String getmOrgName() {
		return mOrgName;
	}


	public void setmOrgName(String mOrgName) {
		this.mOrgName = mOrgName;
	}


	public String getDfilenum() {
		return dfilenum;
	}

	public void setDfilenum(String dfilenum) {
		this.dfilenum = dfilenum;
	}

	public String getReviewdays() {
		return reviewdays;
	}

	public void setReviewdays(String reviewdays) {
		this.reviewdays = reviewdays;
	}

	public String getSurplusdays() {
		return surplusdays;
	}

	public void setSurplusdays(String surplusdays) {
		this.surplusdays = surplusdays;
	}


	public String getAppalyInvestment() {
		return appalyInvestment;
	}

	public void setAppalyInvestment(String appalyInvestment) {
		this.appalyInvestment = appalyInvestment;
	}

	public String getAuthorizeValue() {
		return authorizeValue;
	}

	public void setAuthorizeValue(String authorizeValue) {
		this.authorizeValue = authorizeValue;
	}

	public String getExtraValue() {
		return extraValue;
	}

	public void setExtraValue(String extraValue) {
		this.extraValue = extraValue;
	}

	public String getExtraRate() {
		return extraRate;
	}

	public void setExtraRate(String extraRate) {
		this.extraRate = extraRate;
	}

	public String getApproveValue() {
		return approveValue;
	}

	public void setApproveValue(String approveValue) {
		this.approveValue = approveValue;
	}

	/*public String getApproveTime() {
		return approveTime;
	}

	public void setApproveTime(String approveTime) {
		this.approveTime = approveTime;
	}*/

	public String getIsassistproc() {
		return isassistproc;
	}

	public void setIsassistproc(String isassistproc) {
		this.isassistproc = isassistproc;
	}

	public String getDaysafterdispatch() {
		return daysafterdispatch;
	}

	public void setDaysafterdispatch(String daysafterdispatch) {
		this.daysafterdispatch = daysafterdispatch;
	}

	public String getDispatchType() {
		return dispatchType;
	}

	public void setDispatchType(String dispatchType) {
		this.dispatchType = dispatchType;
	}

	public Date getReceivedate() {
		return receivedate;
	}

	public void setReceivedate(Date receivedate) {
		this.receivedate = receivedate;
	}

	public Date getDispatchDate() {
		return dispatchDate;
	}

	public void setDispatchDate(Date dispatchDate) {
		this.dispatchDate = dispatchDate;
	}

	public Date getFileDate() {
		return fileDate;
	}

	public void setFileDate(Date fileDate) {
		this.fileDate = fileDate;
	}

	public String getIndustryType() {
		return industryType;
	}

	public void setIndustryType(String industryType) {
		this.industryType = industryType;
	}

	public String getSecrectlevel() {
		return secrectlevel;
	}

	public void setSecrectlevel(String secrectlevel) {
		this.secrectlevel = secrectlevel;
	}
    
   
	
}