package cs.domain.sys;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.*;

import cs.domain.DomainBase;

@Entity
@Table(name="cs_user")
public class User extends DomainBase {
	@Id	
	private String id;
	
	@Column(columnDefinition="varchar(255) NOT NULL")
	private String loginName;//登录名
	
	@Column(columnDefinition="varchar(255) NOT NULL")
	private String password;
	
	@Column(columnDefinition="varchar(255) ")
	private String displayName;//显示名
	
	@Column(columnDefinition="varchar(255) ")
	private String userSex; //性别
	
	@Column(columnDefinition="varchar(255) ")
    private String userPhone; //联系电话
	
	@Column(columnDefinition="varchar(255) ")
    private String userMPhone; //联系手机
	
	@Column(columnDefinition="varchar(255) ")
    private String email; //电子邮件
	
	@Column(columnDefinition="varchar(255) ")
    private String jobState; //在职情况
	
	@Column(columnDefinition="varchar(255) ")
    private String useState; //是否停用
	
	@Column(columnDefinition="varchar(255) ")
    private String pwdState; //是否需要更改密码
	
	@Column(columnDefinition="varchar(255) ")
    private String userIP; //登录IP
	
	@Column(columnDefinition="date")
    private Date lastLogin; //最后登录时间
	
	@Column(columnDefinition="varchar(255) ")
	private String userSalt; //加密盐
	
	@Column(columnDefinition="varchar(255) ")
	private Long userOrder; //用户序号
	
	@Column(columnDefinition="varchar(255) ")
	private String userDtID;//钉钉ID
	
	@Column(columnDefinition="varchar(255) ")
	private String remark;
	
	@Column(columnDefinition="integer")
	private int loginFailCount;
	
	@Column(columnDefinition="date")
	private Date lastLoginDate;
	
	//用户排序
	@Column(columnDefinition="INTEGER")
	private Integer userSort;
	

	@ManyToMany(fetch = FetchType.LAZY)
	private List<Role> roles=new ArrayList<>();
	
	@ManyToOne
	@JoinColumn(name="orgID")
	private Org org;
	

	public Org getOrg() {
		return org;
	}
	public void setOrg(Org org) {
		this.org = org;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getLoginName() {
		return loginName;
	}
	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getDisplayName() {
		return displayName;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	public List<Role> getRoles() {
		return roles;
	}
	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	
	
	public int getLoginFailCount() {
		return loginFailCount;
	}
	public void setLoginFailCount(int loginFailCount) {
		this.loginFailCount = loginFailCount;
	}
	public Date getLastLoginDate() {
		return lastLoginDate;
	}
	
	public void setLastLoginDate(Date lastLoginDate) {
		this.lastLoginDate = lastLoginDate;
	}
	public String getUserSex() {
		return userSex;
	}
	public void setUserSex(String userSex) {
		this.userSex = userSex;
	}
	public String getUserPhone() {
		return userPhone;
	}
	public void setUserPhone(String userPhone) {
		this.userPhone = userPhone;
	}
	public String getUserMPhone() {
		return userMPhone;
	}
	public void setUserMPhone(String userMPhone) {
		this.userMPhone = userMPhone;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getJobState() {
		return jobState;
	}
	public void setJobState(String jobState) {
		this.jobState = jobState;
	}
	public String getUseState() {
		return useState;
	}
	public void setUseState(String useState) {
		this.useState = useState;
	}
	public String getPwdState() {
		return pwdState;
	}
	public void setPwdState(String pwdState) {
		this.pwdState = pwdState;
	}
	public String getUserIP() {
		return userIP;
	}
	public void setUserIP(String userIP) {
		this.userIP = userIP;
	}	
	
	public Date getLastLogin() {
		return lastLogin;
	}
	public void setLastLogin(Date lastLogin) {
		this.lastLogin = lastLogin;
	}
	public String getUserSalt() {
		return userSalt;
	}
	public void setUserSalt(String userSalt) {
		this.userSalt = userSalt;
	}
	public Long getUserOrder() {
		return userOrder;
	}
	public void setUserOrder(Long userOrder) {
		this.userOrder = userOrder;
	}
	public String getUserDtID() {
		return userDtID;
	}
	public void setUserDtID(String userDtID) {
		this.userDtID = userDtID;
	}
	public Integer getUserSort() {
		return userSort;
	}
	public void setUserSort(Integer userSort) {
		this.userSort = userSort;
	}
	
	
	
}
