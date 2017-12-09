package cs.service.sys;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import cs.common.utils.BeanCopierUtils;
import cs.common.utils.Validate;
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
    public PageModelDto<LogDto> get(ODataObj odataObj) {
        List<Log> logList = logRepo.findByOdata(odataObj);
        List<LogDto> logDtoList = new ArrayList<>();
        if (Validate.isList(logList)) {
            logList.forEach(ll -> {
                LogDto logDto = new LogDto();
                BeanCopierUtils.copyProperties(ll, logDto);
                logDtoList.add(logDto);
            });
        }
        PageModelDto<LogDto> pageModelDto = new PageModelDto<>();
        pageModelDto.setCount(odataObj.getCount());
        pageModelDto.setValue(logDtoList);

        logger.info("查询日志数据");
        return pageModelDto;
    }

    @Override
    public void save(Log log) {
        logRepo.save(log);
    }
}
