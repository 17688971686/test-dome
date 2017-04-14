package cs.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * 单位实体类
 * @author wyl
 * 2016年9月12日
 */
@Entity
@Table(name = "cs_Company")
public class Company extends DomainBase{

	@Id
	//@SequenceGenerator(name = "generator_increment", sequenceName = "seq_increment" )
	//@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "generator_increment")
	private String id; //单位ID
	@Column(columnDefinition="varchar(255)")
	private String coName; //单位名称
	@Column(columnDefinition="varchar(255)")
	private String coPhone; //电话
	@Column(columnDefinition="varchar(255)")
	private String coFax; //传真
	@Column(columnDefinition="varchar(255)")
	private String coPC; //邮编
	@Column(columnDefinition="varchar(255)")
	private String coAddress; //地址
	@Column(columnDefinition="varchar(255)")
	private String coSite; //网站
	@Column(columnDefinition="varchar(255)")
	private String coSynopsis; //单位简介
	@Column(columnDefinition="varchar(255)")
	private String coDept; //直属部门
	@Column(columnDefinition="varchar(255)")
	private String coDeptName;//直属部门名称
	
//	private String coFirst; //上级单位
//	private Set<Department> department = new HashSet<Department>();
//	private Set<Company> coSelf = new HashSet<Company>();
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getCoName() {
		return coName;
	}
	public void setCoName(String coName) {
		this.coName = coName;
	}
	public String getCoPhone() {
		return coPhone;
	}
	public void setCoPhone(String coPhone) {
		this.coPhone = coPhone;
	}
	public String getCoFax() {
		return coFax;
	}
	public void setCoFax(String coFax) {
		this.coFax = coFax;
	}
	public String getCoPC() {
		return coPC;
	}
	public void setCoPC(String coPC) {
		this.coPC = coPC;
	}
	public String getCoAddress() {
		return coAddress;
	}
	public void setCoAddress(String coAddress) {
		this.coAddress = coAddress;
	}
	public String getCoSite() {
		return coSite;
	}
	public void setCoSite(String coSite) {
		this.coSite = coSite;
	}
	public String getCoSynopsis() {
		return coSynopsis;
	}
	public void setCoSynopsis(String coSynopsis) {
		this.coSynopsis = coSynopsis;
	}
	public String getCoDept() {
		return coDept;
	}
	public void setCoDept(String coDept) {
		this.coDept = coDept;
	}
	public String getCoDeptName() {
		return coDeptName;
	}
	public void setCoDeptName(String coDeptName) {
		this.coDeptName = coDeptName;
	}
	
	
}
