package cs.repository.repositoryImpl.sys;

import java.util.Date;
import java.util.List;

import cs.domain.sys.Workday;
import cs.repository.IRepository;

public interface WorkdayRepo extends IRepository<Workday, String>{
	
	boolean isExist(Date days);

    /**
     * 查询从当前日期开始，往前两年内的记录
     * @return
     */
    List<Workday> findWorkDay(String beginTime,String endTime);

    /**
     * 通过时间段获取
     * @param startDate
     * @return
     */
    List<Workday> getBetweenTimeDay(Date startDate   , Date endDate);
}
