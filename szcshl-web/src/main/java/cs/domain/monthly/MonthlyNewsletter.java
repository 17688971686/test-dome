package cs.domain.monthly;

import cs.common.RandomGUID;
import cs.common.constants.Constant;
import cs.common.utils.SessionUtil;
import cs.domain.DomainBase;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

/**
 * 
 * @author sjy
 * 月报简报管理
 * 2017年09月08日
 */
@Entity
@Table(name ="cs_monthly_newsletter")
public class MonthlyNewsletter extends DomainBase { 
	
	@Id
	private String id;
	
	//月报简报名称
	@Column(columnDefinition="varchar(255)")
	private String monthlyNewsletterName;
	
	//添加时间
	@Column(columnDefinition="date")
	private Date addTime;
	
	//编制人
	@Column(columnDefinition="varchar(20)")
	private String authorizedUser;
	
	//编制时间
	@Column(columnDefinition="date")
	private Date authorizedTime;
	
	//开始日期
	@Column(columnDefinition="date")
	private Date startDay;
	
	//截止日期
	@Column(columnDefinition="date")
	private Date endDay;
	
	//已评审项目数
	@Column(columnDefinition = "INTEGER")
	private Integer stageMunber;
	
	//申报总投资
	@Column(columnDefinition = "NUMBER")
	 private BigDecimal declarationSum;
	
	//核审总投资
	 @Column(columnDefinition = "NUMBER")
	 private BigDecimal assessorSum;
	 
	//报告年度
	@Column(columnDefinition="varchar(20)")
	private String  reportMultiyear;
	
	//报告月份
	@Column(columnDefinition="varchar(20)")
	private String  theMonths;
	
	//开始年度
	@Column(columnDefinition="varchar(20)")
	private String startMoultiyear;
	
	//开始月份
	@Column(columnDefinition="varchar(20)")
	private String staerTheMonths;

	//结束年度
	@Column(columnDefinition="varchar(20)")
	private String endMoultiyear;

	//结束月份
	@Column(columnDefinition="varchar(20)")
	private String endTheMonths;
	
	
	/**
	 * 月报简报类型:
	 * 1.表示月报简报管理列表数据,
	 * 2.表示删除月报简报列表
	 * 5:表示月报简报历史数据,
	 * 7:表示删除月报简历史数据(5和7暂时没用)
	 */
	@Column(columnDefinition="varchar(4)")
	private String monthlyType;
	
	/**
	 * 业务ID
	 */
	@Column(columnDefinition="varchar(200)")
	private String businessId;
	
	//备注
	@Column(columnDefinition="varchar(255)")
	private String remark;

    //数据迁移数据新加的字段
	@Column(columnDefinition="int ")
	private Integer bgId;

	public MonthlyNewsletter() {
	}

	public MonthlyNewsletter(Date date,String monthlyType, String displayName) {
        super();
		this.setId((new RandomGUID()).valueAfterMD5);
		this.setCreatedBy(displayName);
		this.setModifiedBy(displayName);
		this.setAddTime(date);
		this.setAuthorizedUser(displayName);
		this.setAuthorizedTime(date);
		this.setMonthlyType(monthlyType);
		this.setCreatedDate(date);
		this.setModifiedDate(date);
    }


    public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}


	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getMonthlyNewsletterName() {
		return monthlyNewsletterName;
	}

	public void setMonthlyNewsletterName(String monthlyNewsletterName) {
		this.monthlyNewsletterName = monthlyNewsletterName;
	}

	public Date getAddTime() {
		return addTime;
	}

	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}

	public String getAuthorizedUser() {
		return authorizedUser;
	}

	public void setAuthorizedUser(String authorizedUser) {
		this.authorizedUser = authorizedUser;
	}

	public Date getAuthorizedTime() {
		return authorizedTime;
	}

	public void setAuthorizedTime(Date authorizedTime) {
		this.authorizedTime = authorizedTime;
	}

	public Date getStartDay() {
		return startDay;
	}

	public void setStartDay(Date startDay) {
		this.startDay = startDay;
	}
	


	public String getReportMultiyear() {
		return reportMultiyear;
	}

	public void setReportMultiyear(String reportMultiyear) {
		this.reportMultiyear = reportMultiyear;
	}

	public String getTheMonths() {
		return theMonths;
	}

	public void setTheMonths(String theMonths) {
		this.theMonths = theMonths;
	}

	public String getStartMoultiyear() {
		return startMoultiyear;
	}

	public void setStartMoultiyear(String startMoultiyear) {
		this.startMoultiyear = startMoultiyear;
	}

	public String getStaerTheMonths() {
		return staerTheMonths;
	}

	public void setStaerTheMonths(String staerTheMonths) {
		this.staerTheMonths = staerTheMonths;
	}

	public String getEndMoultiyear() {
		return endMoultiyear;
	}

	public void setEndMoultiyear(String endMoultiyear) {
		this.endMoultiyear = endMoultiyear;
	}

	public String getEndTheMonths() {
		return endTheMonths;
	}

	public void setEndTheMonths(String endTheMonths) {
		this.endTheMonths = endTheMonths;
	}

	public Date getEndDay() {
		return endDay;
	}

	public void setEndDay(Date endDay) {
		this.endDay = endDay;
	}

	public Integer getStageMunber() {
		return stageMunber;
	}

	public void setStageMunber(Integer stageMunber) {
		this.stageMunber = stageMunber;
	}

	public BigDecimal getDeclarationSum() {
		return declarationSum;
	}

	public void setDeclarationSum(BigDecimal declarationSum) {
		this.declarationSum = declarationSum;
	}

	public BigDecimal getAssessorSum() {
		return assessorSum;
	}

	public void setAssessorSum(BigDecimal assessorSum) {
		this.assessorSum = assessorSum;
	}

	public String getMonthlyType() {
		return monthlyType;
	}

	public void setMonthlyType(String monthlyType) {
		this.monthlyType = monthlyType;
	}

	public String getBusinessId() {
		return businessId;
	}

	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}

    public Integer getBgId() {
        return bgId;
    }

    public void setBgId(Integer bgId) {
        this.bgId = bgId;
    }
}
