package cs.service;

import cs.model.LogDto;
import cs.model.PageModelDto;
import cs.repository.odata.ODataObj;

public interface LogService {

	PageModelDto<LogDto> get(ODataObj odataObj);

}