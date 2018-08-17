package com.sn.framework.module.sys.model;

import com.sn.framework.module.sys.domain.SysVariable;

/**
 * Description: 系统变量信息   数据展示实体
 *
 * @author: tzg
 * @date: 2018/1/23 20:26
 */
public class SysVariableDto extends SysVariable {

    public String getTypeName() {
        return getVarType().getTypeName();
    }

}