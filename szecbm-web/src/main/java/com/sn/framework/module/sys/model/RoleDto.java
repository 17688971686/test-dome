package com.sn.framework.module.sys.model;

import com.sn.framework.module.sys.domain.Role;

/**
 * Description: 角色信息  数据展示类
 * @Author: lyc
 * @Date: 2017/9/1 15:57
 */
public class RoleDto extends Role {
    private static final long serialVersionUID = -5003615900351925311L;

    private Boolean checked;

    public Boolean getChecked() {
        return checked;
    }

    public void setChecked(Boolean checked) {
        this.checked = checked;
    }
}