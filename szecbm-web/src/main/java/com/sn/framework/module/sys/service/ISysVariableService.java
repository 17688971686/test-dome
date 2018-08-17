package com.sn.framework.module.sys.service;

import com.sn.framework.core.service.ISService;
import com.sn.framework.module.sys.model.SysVariableDto;

/**
 * Description: 系统变量信息  业务操作接口
 *
 * @author: tzg
 * @date: 2018/1/23 20:25
 */
public interface ISysVariableService extends ISService<SysVariableDto> {

    /**
     * 通过编码获取系统变量
     * @param varCode
     * @return
     */
    SysVariableDto findByCode(String varCode);
}
