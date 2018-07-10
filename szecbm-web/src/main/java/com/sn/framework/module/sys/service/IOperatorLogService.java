package com.sn.framework.module.sys.service;

import com.sn.framework.core.service.ISService;
import com.sn.framework.module.sys.model.OperatorLogDto;

/**
 * Description: 系统操作日志  业务操作接口
 * @Author: tzg
 * @Date: 2017/9/11 17:04
 */
public interface IOperatorLogService extends ISService<OperatorLogDto> {
    void deleteByDays(Integer days);

}
