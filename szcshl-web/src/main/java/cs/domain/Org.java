package cs.domain;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Formula;

@Entity
@Table(name="cs_org")
public class Org extends DomainBase {
	@Id
	//@SequenceGenerator(name = "generator_increment", sequenceName = "seq_increment" )
	//@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "generator_increment")
	private String id;
	@Column(columnDefinition="varchar(255)")
	private String name;//部门名称
	@Column(columnDefinition="varchar(255)")
	private String remark;//部门简介
	@Column(columnDefinition="varchar(255)")
	private String orgPhone; //电话
	@Column(columnDefinition="varchar(255)")
	private String orgFax; //传真
	@Column(columnDefinition="varchar(255)")
	private String orgAddress; //地址
	@Column(columnDefinition="varchar(255)")
	private String orgFunction; //职能
	//编号
	@Column(columnDefinition="varchar(255)")
	private String orgFirst; //上级部门
	@Column(columnDefinition="varchar(255)")
	private String orgDirector; //部门主管（科长）
	
	@Column(columnDefinition="varchar(255)")
	private String orgAssistant; //部门助理(副科长)
	@Column(columnDefinition="varchar(255)")
	private String orgMLeader; //主管领导（署长）
	@Column(columnDefinition="varchar(255)")
	private String orgSLeader; //分管领导（分管署长）
	@Column(columnDefinition="varchar(255)")
	private String orgContact; //联络人
	@Column(columnDefinition="varchar(255)")
	private String orgCompany; //所属单位
	@Column(columnDefinition="varchar(255)")
	private String orgOrder; //单位序号
	//编号对应的字段
	@Column(columnDefinition="varchar(255)")
	private String orgFirstName; //上级部门名称
	@Column(columnDefinition="varchar(255)")
	private String orgDirectorName; //科长名称
	@Column(columnDefinition="varchar(255)")
	private String orgAssistantName; //副科长名称
	@Column(columnDefinition="varchar(255)")
	private String orgMLeaderName; //署长名称
	@Column(columnDefinition="varchar(255)")
	private String orgSLeaderName;  //分管署长名称

	@Column(columnDefinition="varchar(255)")
	private String orgContactName; //联络人名称
	@Column(columnDefinition="varchar(255)")
	private String orgCompanyName; //所属单位名称
	@Column(columnDefinition="varchar(255)")
    private String  orgDtID;//钉钉ID
    
	@Column(columnDefinition="varchar(255)")
	private String orgIdentity;
	
	@ManyToOne
	@JoinColumn(name="userOrgID")
	private User userOrgs;
	

	public User getUserOrgs() {
		return userOrgs;
	}

	public void setUserOrgs(User userOrgs) {
		this.userOrgs = userOrgs;
	}

	public String getOrgPhone() {
		return orgPhone;
	}

	public void setOrgPhone(String orgPhone) {
		this.orgPhone = orgPhone;
	}

	public String getOrgFax() {
		return orgFax;
	}

	public void setOrgFax(String orgFax) {
		this.orgFax = orgFax;
	}

	public String getOrgAddress() {
		return orgAddress;
	}

	public void setOrgAddress(String orgAddress) {
		this.orgAddress = orgAddress;
	}

	public String getOrgFunction() {
		return orgFunction;
	}

	public void setOrgFunction(String orgFunction) {
		this.orgFunction = orgFunction;
	}

	public String getOrgFirst() {
		return orgFirst;
	}

	public void setOrgFirst(String orgFirst) {
		this.orgFirst = orgFirst;
	}

	public String getOrgDirector() {
		return orgDirector;
	}

	public void setOrgDirector(String orgDirector) {
		this.orgDirector = orgDirector;
	}

	public String getOrgAssistant() {
		return orgAssistant;
	}

	public void setOrgAssistant(String orgAssistant) {
		this.orgAssistant = orgAssistant;
	}

	public String getOrgMLeader() {
		return orgMLeader;
	}

	public void setOrgMLeader(String orgMLeader) {
		this.orgMLeader = orgMLeader;
	}

	public String getOrgSLeader() {
		return orgSLeader;
	}

	public void setOrgSLeader(String orgSLeader) {
		this.orgSLeader = orgSLeader;
	}

	public String getOrgContact() {
		return orgContact;
	}

	public void setOrgContact(String orgContact) {
		this.orgContact = orgContact;
	}

	public String getOrgCompany() {
		return orgCompany;
	}

	public void setOrgCompany(String orgCompany) {
		this.orgCompany = orgCompany;
	}

	public String getOrgOrder() {
		return orgOrder;
	}

	public void setOrgOrder(String orgOrder) {
		this.orgOrder = orgOrder;
	}

	public String getOrgFirstName() {
		return orgFirstName;
	}

	public void setOrgFirstName(String orgFirstName) {
		this.orgFirstName = orgFirstName;
	}
	//科长
	@Formula("(select u.loginName from cs_user u where u.id = orgDirector)")
	public String getOrgDirectorName() {
		return orgDirectorName;
	}

	public void setOrgDirectorName(String orgDirectorName) {
		this.orgDirectorName = orgDirectorName;
	}
	//副科长
	@Formula("(select u.loginName from cs_user u where u.id = orgAssistant)")
	public String getOrgAssistantName() {
		return orgAssistantName;
	}
	//单位名称
	@Formula("(select c.coName from cs_Company c where c.id = orgCompany)")
	public String getOrgCompanyName() {
		return orgCompanyName;
	}

	public void setOrgCompanyName(String orgCompanyName) {
		this.orgCompanyName = orgCompanyName;
	}
	public void setOrgAssistantName(String orgAssistantName) {
		this.orgAssistantName = orgAssistantName;
	}

	public String getOrgMLeaderName() {
		return orgMLeaderName;
	}

	public void setOrgMLeaderName(String orgMLeaderName) {
		this.orgMLeaderName = orgMLeaderName;
	}

	public String getOrgSLeaderName() {
		return orgSLeaderName;
	}

	public void setOrgSLeaderName(String orgSLeaderName) {
		this.orgSLeaderName = orgSLeaderName;
	}

	public String getOrgContactName() {
		return orgContactName;
	}

	public void setOrgContactName(String orgContactName) {
		this.orgContactName = orgContactName;
	}
	

	public String getOrgDtID() {
		return orgDtID;
	}

	public void setOrgDtID(String orgDtID) {
		this.orgDtID = orgDtID;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	/*public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}*/

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getOrgIdentity() {
		return orgIdentity;
	}

	public void setOrgIdentity(String orgIdentity) {
		this.orgIdentity = orgIdentity;
	}
	
	
	
}
