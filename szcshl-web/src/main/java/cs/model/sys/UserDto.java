package cs.model.sys;

import cs.model.BaseDto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UserDto extends BaseDto {

    /**
     * 用户ID
     */
    private String id;

    /**
     * 员工代码
     */
    private String userNo;

    /**
     * 职务（01中心主任、02中心副主任、03部长、04工程师）
     */
    private String position;

    /**
     * 职称
     */
    private String jobTitle;

    /**
     * 工作岗位
     */
    private String job;

    /**
     * 登录名
     */
    private String loginName;

    /**
     * 登录名
     */
    private String password;

    /**
     * 显示名
     */
    private String displayName;

    /**
     * 性别
     */
    private String userSex;

    /**
     * 联系电话
     */
    private String userPhone;

    /**
     * 联系电话
     */
    private String userMPhone; //联系手机

    /**
     * 电子邮件
     */
    private String email;

    /**
     * 在职情况(t表示在职，f表示不在)
     */
    private String jobState;

    /**
     * 是否停用
     */
    private String useState;

    /**
     * 是否需要更改密码
     */
    private String pwdState;

    /**
     * 登录IP
     */
    private String userIP;

    /**
     * 最后登录时间
     */
    private Date lastLogin;

    /**
     * 加密盐
     */
    private String userSalt;

    /**
     * 钉钉ID
     */
    private String userDtID;

    /**
     * 备注信息
     */
    private String remark;

    /**
     * 登录次数
     */
    private int loginFailCount;

    /**
     * 最后登录成功日期
     */
    private Date lastLoginDate;

    /**
     * 是否参与考勤（默认值为0，1为不参加考勤）
     */
    private String isAttendance;

    /**
     * 是否参与统计（默认值为0，1为不参加考勤）
     */
    private String isCount;

    /**
     * 是否上传发改委（默认值为0，1为已提交，2为已上传）
     */
    private String isUploadFGW;

    /**
     * 用户排序
     */
    private Integer userSort;

    private String orgId;

    //角色
    private List<RoleDto> roleDtoList = new ArrayList<>();

    private OrgDto orgDto;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserNo() {
        return userNo;
    }

    public void setUserNo(String userNo) {
        this.userNo = userNo;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
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

    public String getUserDtID() {
        return userDtID;
    }

    public void setUserDtID(String userDtID) {
        this.userDtID = userDtID;
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

    public String getIsAttendance() {
        return isAttendance;
    }

    public void setIsAttendance(String isAttendance) {
        this.isAttendance = isAttendance;
    }

    public String getIsCount() {
        return isCount;
    }

    public void setIsCount(String isCount) {
        this.isCount = isCount;
    }

    public String getIsUploadFGW() {
        return isUploadFGW;
    }

    public void setIsUploadFGW(String isUploadFGW) {
        this.isUploadFGW = isUploadFGW;
    }

    public Integer getUserSort() {
        return userSort;
    }

    public void setUserSort(Integer userSort) {
        this.userSort = userSort;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }


    public List<RoleDto> getRoleDtoList() {
		return roleDtoList;
	}

	public void setRoleDtoList(List<RoleDto> roleDtoList) {
		this.roleDtoList = roleDtoList;
	}

	public OrgDto getOrgDto() {
        return orgDto;
    }

    public void setOrgDto(OrgDto orgDto) {
        this.orgDto = orgDto;
    }
}
