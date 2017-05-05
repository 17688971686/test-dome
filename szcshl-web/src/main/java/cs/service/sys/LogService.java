package cs.service.sys;

import cs.model.PageModelDto;
import cs.model.sys.LogDto;
import cs.repository.odata.ODataObj;

public interface LogService {

	PageModelDto<LogDto> get(ODataObj odataObj);

}