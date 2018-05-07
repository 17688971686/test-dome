package cs.service.sys;

import java.util.Date;
import java.util.List;

import cs.common.ResultMsg;
import cs.domain.sys.Workday;
import cs.model.PageModelDto;
import cs.model.sys.WorkdayDto;
import cs.repository.odata.ODataObj;

public interface WorkdayService {
	
	PageModelDto<WorkdayDto> getWorkday(ODataObj odataObj);

	ResultMsg createWorkday(WorkdayDto workdayDto);

	/**
	 * 判断工作日期是否有重复
	 * @param dates
	 * @return
	 */
	Boolean isRepeat(Date dates);
	
	WorkdayDto getWorkdayById(String id);
	
	void updateWorkday(WorkdayDto workdayDto);
	
	void deleteWorkday(String id);
	
	List<Workday> selectSpecialDays(String status);

    /**
     * 计算从当前日期开始，一年内的调休记录
     * @return
     */
	List<Workday> findWorkDayByNow();

	/**
	 * 通过时间段获取
	 * @param startDate
	 * @return
	 */
	List<Workday> getBetweenTimeDay(Date startDate   , Date endDate);
}
