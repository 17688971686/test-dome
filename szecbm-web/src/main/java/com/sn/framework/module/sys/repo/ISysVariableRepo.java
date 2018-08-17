package com.sn.framework.module.sys.repo;

import com.sn.framework.core.repo.IRepository;
import com.sn.framework.module.sys.domain.SysVariable;

/**
 * Description: 系统变量信息  数据操作接口
 *
 * @author: tzg
 * @date: 2018/1/23 20:23
 */
public interface ISysVariableRepo extends IRepository<SysVariable, String> {

    SysVariable findByCode(String varCode);
}
