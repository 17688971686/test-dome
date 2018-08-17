package com.sn.framework.module.sys.repo.impl;

import com.sn.framework.core.repo.impl.AbstractRepository;
import com.sn.framework.module.sys.domain.OperatorLog;
import com.sn.framework.module.sys.repo.IOperatorLogRepo;
import com.sn.framework.odata.OdataFilter;
import com.sn.framework.odata.impl.jpa.OdataJPADelete;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import java.util.Date;
import java.util.List;

/**
 * Description: 系统操作日志  数据操作实现类
 * Author: tzg
 * Date: 2017/9/11 14:15
 */
@Repository
public class OperatorLogRepoImpl extends AbstractRepository<OperatorLog, String> implements IOperatorLogRepo {

    @Override
    public int deleteByDays(Date date){
        OdataJPADelete delete = new OdataJPADelete();
        delete.addFilter(new OdataFilter("createdDate", OdataFilter.Operate.LT, date));
        return delete.criteriaQuery(entityManager, OperatorLog.class).executeUpdate();
    }

}