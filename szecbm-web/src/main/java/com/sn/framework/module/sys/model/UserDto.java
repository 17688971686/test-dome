package com.sn.framework.module.sys.model;


import com.sn.framework.module.sys.domain.User;

/**
 * 用户视图数据
 * Created by lqs on 2017/7/19.
 */
public class UserDto extends User {

    private String verifyPassword;  // 确认密码

    public String getVerifyPassword() {
        return verifyPassword;
    }

    public void setVerifyPassword(String verifyPassword) {
        this.verifyPassword = verifyPassword;
    }

}
