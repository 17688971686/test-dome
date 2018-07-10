package com.sn.framework.module.sys.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sn.framework.core.DomainBase;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * 用户信息 数据映射实体
 * @author tzg
 */
@Entity
@Table(name = "cs_sys_user")
public class User extends DomainBase {

    private static final long serialVersionUID = 8890972428818069739L;

    /**
     * 用户ID
     */
    @Id
    @Column(length = 64)
    private String userId;
    /**
     * 用户名
     */
    @Column(nullable = false, unique = true)
    private String username;
    /**
     * 用户密码
     */
    @Column(nullable = false)
    private String password;
    /**
     * 密码盐
     */
    @JsonIgnore
    @Column
    private String userSalt;
    /**
     * 修改密码的时间，如果为空，则为默认密码
     */
    @JsonIgnore
    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private Date changePasswordDate;
    /**
     * 显示名
     */
    @Column(nullable = false)
    private String displayName;
    /**
     * 用户备注
     */
    @Column
    private String remark;
    /**
     * 用户状态：1、启用，0、禁用
     */
    @Column(length = 2, nullable = false)
    private String useState;
    /**
     * 用户登录校验错误次数
     */
    @Column
    private int loginFailCount;
    /**
     * 用户最后登录时间
     */
    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastLoginDate;
    /**
     * 手机号码
     */
    @Column(length = 20)
    private String mobileNumber;
    /**
     * 隐藏手机号码
     */
    @Column
    private Integer hideMobile;
    /**
     * 联系号码
     */
    @Column(length = 20)
    private String phoneNumber;
    /**
     * 邮件地址
     */
    @Column(length = 100)
    private String email;

    /**
     * 用户数据类型：0、为系统基础数据，不可删除；1、为用户添加数据，可删除
     */
    @JsonIgnore
    @Column
    private int userDataType;

    /**
     * 所属机构
     */
    @ManyToOne
    @JoinColumn(name = "organId")
    private Organ organ;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "cs_sys_user_role", joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserSalt() {
        return userSalt;
    }

    public void setUserSalt(String userSalt) {
        this.userSalt = userSalt;
    }

    public Date getChangePasswordDate() {
        return changePasswordDate;
    }

    public void setChangePasswordDate(Date changePasswordDate) {
        this.changePasswordDate = changePasswordDate;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public Integer getHideMobile() {
        return hideMobile;
    }

    public void setHideMobile(Integer hideMobile) {
        this.hideMobile = hideMobile;
    }

    public int getUserDataType() {
        return userDataType;
    }

    public void setUserDataType(int userDataType) {
        this.userDataType = userDataType;
    }

    public Organ getOrgan() {
        return organ;
    }

    public void setOrgan(Organ organ) {
        this.organ = organ;
    }

    public String getUseState() {
        return useState;
    }

    public void setUseState(String useState) {
        this.useState = useState;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }
}
