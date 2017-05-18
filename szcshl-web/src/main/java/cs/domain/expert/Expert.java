package cs.domain.expert;


import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import cs.domain.DomainBase;
/**
 * 专家信息
 * @author Administrator
 *
 */
@Entity
@Table(name="cs_expert")
public class Expert extends DomainBase {
	@Id
	private String expertID;//专家ID
	
	@Column(columnDefinition="varchar(5)")
	private String expertNo;//专家编号
	
	@Column(columnDefinition="varchar(32) NOT NULL")
	private String name;//专家姓名
	
	@Column(columnDefinition="varchar(5)")
	private String sex;//性别
	
	@Column(columnDefinition="date")
	private Date birthDay;//出生日期
	
	@Column(columnDefinition="varchar(50)")
	private String idCard;//身份证号
	
	@Column(columnDefinition="varchar(128) ")
	private String qualifiCations;//最高学历
	
	@Column(columnDefinition="varchar(128) ")
	private String acaDemy;//毕业院校
	
	@Column(columnDefinition="varchar(255) ")
	private String degRee;//最高学位
	
	@Column(columnDefinition="varchar(50) ")
	private String userPhone;//手机号码
	
	@Column(columnDefinition="date")
	private Date graduateDate;//毕业时间
	
	@Column(columnDefinition="varchar(128) ")
	private String comPany;//工作单位
	
	@Column(columnDefinition="varchar(100) ")
	private String job;//现任职位
	
	@Column(columnDefinition="varchar(255) ")
	private String post;//职称
	
	@Column(columnDefinition="varchar(50) ")
	private String phone;//办公电话
	
	@Column(columnDefinition="varchar(50) ")
	private String fax;//传真
	
	@Column(columnDefinition="varchar(50) ")
	private String email;//电子邮箱
	
	@Column(columnDefinition="varchar(225) ")
	private String addRess;//通讯地址
	
	@Column(columnDefinition="varchar(50) ")
	private String zipCode;//邮编
		
	@Column(columnDefinition="varchar(64) ")
	private String majorStudy;	//所学专业
	
	@Column(columnDefinition="varchar(64) ")
	private String majorWork;	//从事专业
	
	@Column(columnDefinition="varchar(128) ")
	private String maJorBig;//突出专业(大类)
	
	@Column(columnDefinition="varchar(128) ")
	private String maJorSmall;//突出专业(小类)
	
	@Column(columnDefinition="varchar(30) ")
	private String expeRttype;//专家类别
	
	@Column(columnDefinition="varchar(30) ")
	private String procoSttype;//工程造价类
	
	@Column(columnDefinition="varchar(30) ")
	private String proteChtype;//项目类型
	
	@Column(columnDefinition="varchar(255) ")
	private String remark;//备注
	
	@Column(columnDefinition="varchar(256) ")
	private String openingBank;//开户行
	
	@Column(columnDefinition="varchar(32) ")
	private String bankAccount;	//银行账号
	
	@Column(columnDefinition="varchar(30) ")
	private String state;	//专家范围(审核中1,正式专家2,备选专家3,已停用4 ，已删除5)
	
	@OneToMany(mappedBy="expert")
	private List<WorkExpe> work;
	
	@OneToMany(mappedBy="expert")
	private List<ProjectExpe> project;
	
	public Date getBirthDay() {
		return birthDay;
	}
	public void setBirthDay(Date birthDay) {
		this.birthDay = birthDay;
	}	
	public Date getGraduateDate() {
		return graduateDate;
	}
	public void setGraduateDate(Date graduateDate) {
		this.graduateDate = graduateDate;
	}
	public List<ProjectExpe> getProject() {
		return project;
	}
	public void setProject(List<ProjectExpe> project) {
		this.project = project;
	}
	public List<WorkExpe> getWork() {
		return work;
	}
	public void setWork(List<WorkExpe> work) {
		this.work = work;
	}
	public String getExpertID() {
		return expertID;
	}
	public void setExpertID(String expertID) {
		this.expertID = expertID;
	}
	public String getIdCard() {
		return idCard;
	}
	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public void setQualifiCations(String qualifiCations) {
		this.qualifiCations = qualifiCations;
	}
	public String getAcaDemy() {
		return acaDemy;
	}
	public void setAcaDemy(String acaDemy) {
		this.acaDemy = acaDemy;
	}
	public String getDegRee() {
		return degRee;
	}
	public void setDegRee(String degRee) {
		this.degRee = degRee;
	}
	public String getUserPhone() {
		return userPhone;
	}
	public void setUserPhone(String userPhone) {
		this.userPhone = userPhone;
	}
	public String getComPany() {
		return comPany;
	}
	public void setComPany(String comPany) {
		this.comPany = comPany;
	}
	public String getJob() {
		return job;
	}
	public void setJob(String job) {
		this.job = job;
	}
	
	public String getPost() {
		return post;
	}
	public void setPost(String post) {
		this.post = post;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getFax() {
		return fax;
	}
	public void setFax(String fax) {
		this.fax = fax;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getAddRess() {
		return addRess;
	}
	public void setAddRess(String addRess) {
		this.addRess = addRess;
	}
	public String getZipCode() {
		return zipCode;
	}
	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}	
	public String getMaJorBig() {
		return maJorBig;
	}
	public void setMaJorBig(String maJorBig) {
		this.maJorBig = maJorBig;
	}
	public String getMaJorSmall() {
		return maJorSmall;
	}
	public void setMaJorSmall(String maJorSmall) {
		this.maJorSmall = maJorSmall;
	}
	public String getQualifiCations() {
		return qualifiCations;
	}
	public String getExpeRttype() {
		return expeRttype;
	}
	public void setExpeRttype(String expeRttype) {
		this.expeRttype = expeRttype;
	}
	public String getProcoSttype() {
		return procoSttype;
	}
	public void setProcoSttype(String procoSttype) {
		this.procoSttype = procoSttype;
	}
	public String getProteChtype() {
		return proteChtype;
	}
	public void setProteChtype(String proteChtype) {
		this.proteChtype = proteChtype;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getMajorStudy() {
		return majorStudy;
	}
	public void setMajorStudy(String majorStudy) {
		this.majorStudy = majorStudy;
	}
	public String getMajorWork() {
		return majorWork;
	}
	public void setMajorWork(String majorWork) {
		this.majorWork = majorWork;
	}
	public String getOpeningBank() {
		return openingBank;
	}
	public void setOpeningBank(String openingBank) {
		this.openingBank = openingBank;
	}
	public String getBankAccount() {
		return bankAccount;
	}
	public void setBankAccount(String bankAccount) {
		this.bankAccount = bankAccount;
	}
	public String getExpertNo() {
		return expertNo;
	}
	public void setExpertNo(String expertNo) {
		this.expertNo = expertNo;
	}
	
}
