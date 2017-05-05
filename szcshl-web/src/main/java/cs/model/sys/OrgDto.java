package cs.model.sys;

import java.util.List;

import javax.persistence.Column;

import org.hibernate.annotations.Formula;

import cs.model.BaseDto;

public class OrgDto extends BaseDto {
	private String id;
	private String name;//部门名称
	private String remark;//部门简介
	private String orgPhone; //电话
	private String orgFax; //传真
	private String orgAddress; //地址
	private String orgFunction; //职能
	
	private String orgFirst; //上级部门
	private String orgDirector; //部门主管（科长）
	private String orgAssistant; //部门助理(副科长)
	private String orgMLeader; //主管领导（署长）
	private String orgSLeader; //分管领导（分管署长）
	private String orgContact; //联络人
	private String orgCompany; //所属单位
	private String orgOrder; //单位序号
	
	private String orgFirstName; //上级部门名称
	private String orgDirectorName; //科长名称
	private String orgAssistantName; //副科长名称
	private String orgMLeaderName; //署长名称
	private String orgSLeaderName;  //分管署长名称

	private String orgContactName; //联络人名称
	private String orgCompanyName; //所属单位名称
    private String orgDtID;//钉钉ID
    
	private String orgIdentity;
	
	private List<UserDto> userDtos;
		
	public List<UserDto> getUserDtos() {
		return userDtos;
	}
	public void setUserDtos(List<UserDto> userDtos) {
		this.userDtos = userDtos;
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
	
	public String getOrgDirectorName() {
		return orgDirectorName;
	}
	public void setOrgDirectorName(String orgDirectorName) {
		this.orgDirectorName = orgDirectorName;
	}
	
	public String getOrgAssistantName() {
		return orgAssistantName;
	}
	public void setOrgAssistantName(String orgAssistantName) {
		this.orgAssistantName = orgAssistantName;
	}
	
	public String getOrgCompanyName() {
		return orgCompanyName;
	}
	public void setOrgCompanyName(String orgCompanyName) {
		this.orgCompanyName = orgCompanyName;
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
