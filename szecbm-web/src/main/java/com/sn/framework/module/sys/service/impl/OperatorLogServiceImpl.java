package com.sn.framework.module.sys.service.impl;

import com.sn.framework.core.service.impl.SServiceImpl;
import com.sn.framework.module.sys.domain.OperatorLog;
import com.sn.framework.module.sys.model.OperatorLogDto;
import com.sn.framework.module.sys.repo.IOperatorLogRepo;
import com.sn.framework.module.sys.service.IOperatorLogService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Description: 系统操作日志  业务操作实现类
 *
 * @Author: tzg
 * @Date: 2017/9/11 17:05
 */
@Service
public class OperatorLogServiceImpl extends SServiceImpl<IOperatorLogRepo, OperatorLog, OperatorLogDto> implements IOperatorLogService {


    @Override
    public void update(OperatorLogDto dto, String... ignoreProperties) {
//        super.update(dto, ignoreProperties);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByDays(Integer days) {
        //获取当前日期
        Calendar cal = Calendar.getInstance();
        //days之前的日期
        cal.add(Calendar.DAY_OF_MONTH, -days);
        Date date = cal.getTime();
        int n = baseRepo.deleteByDays(date);
        if (n == 0) {
            logger.warn("没有可删除的日志数据");
        }
    }

}