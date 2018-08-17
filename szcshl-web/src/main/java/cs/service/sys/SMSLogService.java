package cs.service.sys;

import cs.common.ResultMsg;
import cs.domain.sys.SMSLog;
import cs.model.PageModelDto;
import cs.model.sys.SMSLogDto;
import cs.repository.odata.ODataObj;

import java.util.List;

public interface SMSLogService {

	PageModelDto<SMSLogDto> get(ODataObj odataObj);

	void save(SMSLog smsLog);

	void update(List<SMSLog> smsLogList);
	/*
	* 发送短信不收次数限制,通过短信类型区分是手动发送 manual_type
	* */
	ResultMsg sendSMSContent(SMSLogDto smsLogDto);

	void deleteSMSLog(String id);

	/**
	 * 判断短信日志表心思是否存在
	 * @param type
	 * */
	SMSLog querySMSLog(String userName,String smsUserPhone,String projectName,String filecode,String resultCode,String type,String infoType,String seekContent, SMSLogService smsLogService,boolean success);

}