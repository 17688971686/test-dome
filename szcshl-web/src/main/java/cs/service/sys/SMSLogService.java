package cs.service.sys;

import cs.domain.sys.SMSLog;
import cs.model.PageModelDto;
import cs.model.sys.SMSLogDto;
import cs.repository.odata.ODataObj;

public interface SMSLogService {

	PageModelDto<SMSLogDto> get(ODataObj odataObj);

	void save(SMSLog smsLog);
}