package cs.model.sys;

import javax.persistence.Column;
import javax.persistence.Id;

import cs.model.BaseDto;

public class CompanyDto extends BaseDto {

	private String id; //单位ID
	private String coName; //单位名称
	private String coPhone; //电话
	private String coFax; //传真
	private String coPC; //邮编
	private String coAddress; //地址
	private String coSite; //网站
	private String coSynopsis; //单位简介
	private String coDept; //直属部门
	private String coDeptName;//直属部门名称
	private String coType;//单位类型
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
	public String getCoType() {
		return coType;
	}
	public void setCoType(String coType) {
		this.coType = coType;
	}
	
	
}
