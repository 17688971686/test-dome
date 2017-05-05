package cs.service.sys;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cs.domain.sys.Log;
import cs.model.PageModelDto;
import cs.model.sys.LogDto;
import cs.repository.odata.ODataObj;
import cs.repository.repositoryImpl.sys.LogRepo;

@Service
public class LogServiceImpl implements LogService {
	private static Logger logger = Logger.getLogger(LogServiceImpl.class);
	
	@Autowired
	private LogRepo logRepo;
	/* (non-Javadoc)
	 * @see cs.service.LogService#get(cs.repository.odata.ODataObj)
	 */
	@Override
	@Transactional
	public PageModelDto<LogDto> get(ODataObj odataObj){
		List<Log> logList = logRepo.findByOdata(odataObj);
		List<LogDto> logDtoList = new ArrayList<>();
		for (Log item : logList) {
			LogDto logDto = new LogDto();
			logDto.setId(item.getId());
			logDto.setUserId(item.getUserId());			
			logDto.setCreatedDate(item.getCreatedDate());
			logDto.setLevel(item.getLogLevel());
			logDto.setLogger(item.getLogger());
			logDto.setMessage(item.getMessage());

			logDtoList.add(logDto);
		}
		PageModelDto<LogDto> pageModelDto = new PageModelDto<>();
		pageModelDto.setCount(odataObj.getCount());
		pageModelDto.setValue(logDtoList);

		logger.info("查询日志数据");		
		return pageModelDto;
	}
}
