package cs.model.sys;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.ManyToMany;

import cs.domain.sys.Org;
import cs.model.BaseDto;

public class UserDto extends BaseDto {
	private String id;
	private String loginName;
	private String password;
	private String displayName;
	private String remark;
	
	private String userSex; //性别
    private String userPhone; //联系电话
    private String userMPhone; //联系手机
    private String email; //电子邮件
    private String jobState; //在职情况
    private String useState; //是否停用
    private String pwdState; //是否需要更改密码
    private String userIP; //登录IP
    private String lastLogin; //最后登录时间
    private String userCreateTime; //创建时间
	private String userSalt; //加密盐
	private Long userOrder; //用户序号
	
	private String orgUserId;
	
	private int loginFailCount;
	@Column(columnDefinition="date")
	private Date lastLoginDate;
	//角色
	private List<RoleDto> roles=new ArrayList<>();
	
	private OrgDto orgDto;
	
	
	public OrgDto getOrgDto() {
		return orgDto;
	}


	public String getOrgUserId() {
		return orgUserId;
	}


	public void setOrgUserId(String orgUserId) {
		this.orgUserId = orgUserId;
	}


	public void setOrgDto(OrgDto orgDto) {
		this.orgDto = orgDto;
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
	public List<RoleDto> getRoles() {
		return roles;
	}
	public void setRoles(List<RoleDto> roles) {
		this.roles = roles;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
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
	public String getLastLogin() {
		return lastLogin;
	}
	public void setLastLogin(String lastLogin) {
		this.lastLogin = lastLogin;
	}
	public String getUserCreateTime() {
		return userCreateTime;
	}
	public void setUserCreateTime(String userCreateTime) {
		this.userCreateTime = userCreateTime;
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
	
	
	
	
}
