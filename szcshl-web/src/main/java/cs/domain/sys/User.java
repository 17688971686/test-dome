package cs.domain.sys;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.*;

import cs.domain.DomainBase;

@Entity
@Table(name = "cs_user")
public class User extends DomainBase {
    @Id
    @Column(columnDefinition = "varchar(64)")
    private String id;

    /**
     * 员工代码
     */
    @Column(columnDefinition = "varchar(4)")
    private String userNo;

    /**
     * 职务（01中心主任、02中心副主任、03部长、04工程师）
     */
    @Column(columnDefinition = "varchar(20)")
    private String position;

    /**
     * 职称
     */
    @Column(columnDefinition = "varchar(20)")
    private String  jobTitle;

    /**
     * 工作岗位
     */
    @Column(columnDefinition = "varchar(50)")
    private String job;
    /**
     * 登录名
     */
    @Column(columnDefinition = "varchar(64) NOT NULL")
    private String loginName;

    /**
     * 腾讯通账号
     */
    @Column(columnDefinition = "varchar(64)")
    private String rtxName;
    /**
     * 密码
     */
    @Column(columnDefinition = "varchar(64) NOT NULL")
    private String password;

    /**
     * 显示名
     */
    @Column(columnDefinition = "varchar(64) ")
    private String displayName;

    /**
     * 性别
     */
    @Column(columnDefinition = "varchar(2) ")
    private String userSex;

    /**
     * 联系电话
     */
    @Column(columnDefinition = "varchar(20) ")
    private String userPhone;

    /**
     * 联系手机
     */
    @Column(columnDefinition = "varchar(20) ")
    private String userMPhone;

    /**
     * 电子邮件
     */
    @Column(columnDefinition = "varchar(64) ")
    private String email;

    /**
     * 在职情况(t表示在职，f表示不在)
     */
    @Column(columnDefinition = "varchar(2) ")
    private String jobState;

    /**
     * 是否停用
     */
    @Column(columnDefinition = "varchar(2) ")
    private String useState;

    /**
     * 是否需要更改密码
     */
    @Column(columnDefinition = "varchar(255) ")
    private String pwdState;

    /**
     * 登录IP
     */
    @Column(columnDefinition = "varchar(64) ")
    private String userIP;

    /**
     * 最后登录时间
     */
    @Column(columnDefinition = "date")
    private Date lastLogin;

    /**
     * 加密盐
     */
    @Column(columnDefinition = "varchar(255) ")
    private String userSalt;

    /**
     * 钉钉ID
     */
    @Column(columnDefinition = "varchar(64) ")
    private String userDtID;

    /**
     * 备注信息
     */
    @Column(columnDefinition = "varchar(255) ")
    private String remark;

    /**
     * 登录次数
     */
    @Column(columnDefinition = "integer")
    private int loginFailCount;

    /**
     * 最后登录成功日期
     */
    @Column(columnDefinition = "date")
    private Date lastLoginDate;

    /**
     * 是否参与考勤（默认值为0，1为不参加考勤）
     */
    @Column(columnDefinition = "varchar(2) ")
    private String isAttendance;

    /**
     * 是否参与统计（默认值为0，1为不参加考勤）
     */
    @Column(columnDefinition = "varchar(2) ")
    private String isCount;

    /**
     * 是否上传发改委（默认值为0，1为已提交，2为已上传）
     */
    @Column(columnDefinition = "varchar(2) ")
    private String isUploadFGW;

    /**
     * 用户排序
     */
    @Column(columnDefinition = "INTEGER")
    private Integer userSort;

    /**
     * 代办用户ID
     */
    @Column(columnDefinition = "varchar(64)")
    private String takeUserId;

    /**
     * 分管部门类型（PX:评估，GX:概算）
     */
    @Column(columnDefinition = "varchar(8)")
    private String mngOrgType;

    @ManyToMany(fetch = FetchType.LAZY)
    private List<Role> roles = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "orgID")
    private Org org;

    /**
     * 组（内部部门）
     */
    @ManyToMany(mappedBy="userList",fetch = FetchType.LAZY)
    private List<SysDept> sysDeptList = new ArrayList<>();

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

    public String getTakeUserId() {
        return takeUserId;
    }

    public void setTakeUserId(String takeUserId) {
        this.takeUserId = takeUserId;
    }

    public String getMngOrgType() {
        return mngOrgType;
    }

    public void setMngOrgType(String mngOrgType) {
        this.mngOrgType = mngOrgType;
    }

    public List<SysDept> getSysDeptList() {
        return sysDeptList;
    }

    public void setSysDeptList(List<SysDept> sysDeptList) {
        this.sysDeptList = sysDeptList;
    }

    public String getRtxName() {
        return rtxName;
    }

    public void setRtxName(String rtxName) {
        this.rtxName = rtxName;
    }
}
