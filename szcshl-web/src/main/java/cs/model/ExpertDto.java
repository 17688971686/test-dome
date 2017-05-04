package cs.model;



import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;

/**
 * 专家
 * @author Administrator
 *
 */
public class ExpertDto extends BaseDto {
	private String expertID;//专家ID
	private String name;//专家姓名
	private String sex;//性别
	private String birthDay;//出生日期
	private String idCard;//身份证号
	private String qualifiCations;//最高学历
	private String acaDemy;//毕业院校
	private String degRee;//最高学位
	private String userPhone;//手机号码
	private String createDate;//创建日期
	private String comPany;//工作单位
	private String job;//现任职位
	private String title;//职称
	private String phone;//办公电话
	private String fax;//传真
	private String email;//电子邮箱
	private String addRess;//通讯地址
	private String zipCode;//邮编
	private String maJor;//所学专业
	private String expeRttype;//专家类别
	private String procoSttype;//工程造价类
	private String proteChtype;//项目类型
	private String remark;//备注
	private String state;//专家范围(审核中1,正式专家2,备选专家3,已停用4 ，已删除5)
	private List<WorkExpeDto> work=new ArrayList<>();
	private List<ProjectExpeDto> project=new ArrayList<>();
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
	public String getBirthDay() {
		return birthDay;
	}
	public void setBirthDay(String birthDay) {
		this.birthDay = birthDay;
	}
	public String getQualifiCations() {
		return qualifiCations;
	}
	public void setQualifiCations(String qualifiCations) {
		this.qualifiCations = qualifiCations;
	}
	@Column(name = "acaDemy", nullable = false, length = 200)
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
	public String getCreateDate() {
		return createDate;
	}
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
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
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
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
	public String getMaJor() {
		return maJor;
	}
	public void setMaJor(String maJor) {
		this.maJor = maJor;
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
	public String getExpertID() {
		return expertID;
	}
	public void setExpertID(String expertID) {
		this.expertID = expertID;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public List<WorkExpeDto> getWork() {
		return work;
	}
	public void setWork(List<WorkExpeDto> work) {
		this.work = work;
	}
	public List<ProjectExpeDto> getProject() {
		return project;
	}
	public void setProject(List<ProjectExpeDto> project) {
		this.project = project;
	}


}
