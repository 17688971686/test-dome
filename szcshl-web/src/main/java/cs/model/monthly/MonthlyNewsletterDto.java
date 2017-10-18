package cs.model.monthly;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Id;

import com.alibaba.fastjson.annotation.JSONField;

import cs.model.BaseDto;

public class MonthlyNewsletterDto extends BaseDto{

	private String id;

	//月报简报名称
	private String monthlyNewsletterName;

	//添加时间
	 @JSONField(format = "yyyy-MM-dd")
	private Date addTime;

	//编制人
	private String authorizedUser;

	//编制时间
	 @JSONField(format = "yyyy-MM-dd")
	private Date authorizedTime;

	//开始日期
    @JSONField(format = "yyyy-MM-dd")
	private Date startDay;

	//截止日期
    @JSONField(format = "yyyy-MM-dd")
	private Date endDay;

	//已评审项目数
	private Integer stageMunber;

	//申报总投资
	private BigDecimal declarationSum;

	//核审总投资
	private BigDecimal assessorSum;
	//报告年度
	private String  reportMultiyear;
	
	//报告月份
	private String  theMonths;
	
	//开始年度
	private String startMoultiyear;
	
	//开始月份
	private String staerTheMonths;

	//结束年度
	private String endMoultiyear;

	//结束月份
	private String endTheMonths;
	
	//月报简报类型:1:表示月报简报历史数据,2:表示月报简报年度
	private String monthlyType;
	
	//业务ID
	private String businessId;
	
	//备注
	private String remark;

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
	

	public String getBusinessId() {
		return businessId;
	}

	public void setBusinessId(String businessId) {
		this.businessId = businessId;
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
	
	
	
	
	
}
