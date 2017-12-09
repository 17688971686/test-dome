package cs.service.sys;

import cs.domain.sys.Log;
import cs.model.PageModelDto;
import cs.model.sys.LogDto;
import cs.repository.odata.ODataObj;

public interface LogService {

	PageModelDto<LogDto> get(ODataObj odataObj);

	void save(Log log);
}