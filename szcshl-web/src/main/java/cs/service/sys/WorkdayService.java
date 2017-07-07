package cs.service.sys;

import java.util.List;

import cs.domain.sys.Workday;
import cs.model.PageModelDto;
import cs.model.sys.WorkdayDto;
import cs.repository.odata.ODataObj;

public interface WorkdayService {
	
	PageModelDto<WorkdayDto> getWorkday(ODataObj odataObj);
	
	void createWorkday(WorkdayDto workdayDto);
	
	WorkdayDto getWorkdayById(String id);
	
	void updateWorkday(WorkdayDto workdayDto);
	
	void deleteWorkday(String id);
	
	List<Workday> selectSpecialDays(String status);

}