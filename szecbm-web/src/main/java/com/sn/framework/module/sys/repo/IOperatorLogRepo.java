package com.sn.framework.module.sys.repo;

import com.sn.framework.core.repo.IRepository;
import com.sn.framework.module.sys.domain.OperatorLog;

import java.util.Date;
import java.util.List;

/**
 * Description: 系统操作日志  数据操作接口
 * Author: tzg
 * Date: 2017/9/11 14:14
 */
public interface IOperatorLogRepo extends IRepository<OperatorLog, String> {

    /**
     * 删除某个时间之前的数据
     * @param date
     * @return
     */
    int deleteByDays(Date date);
}