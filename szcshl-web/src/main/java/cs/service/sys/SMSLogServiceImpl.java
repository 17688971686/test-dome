package cs.service.sys;

import cs.common.utils.BeanCopierUtils;
import cs.common.utils.Validate;
import cs.domain.sys.SMSLog;
import cs.model.PageModelDto;
import cs.model.sys.SMSLogDto;
import cs.repository.odata.ODataObj;
import cs.repository.repositoryImpl.sys.SMSLogRepo;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class SMSLogServiceImpl implements SMSLogService {
    private static Logger logger = Logger.getLogger(SMSLogServiceImpl.class);

    @Autowired
    private SMSLogRepo smslogRepo;

    @Override
    @Transactional
    public PageModelDto<SMSLogDto> get(ODataObj odataObj) {
        List<SMSLog> logList = smslogRepo.findByOdata(odataObj);
        List<SMSLogDto> smslogDtoList = new ArrayList<>();
        if (Validate.isList(logList)) {
            logList.forEach(ll -> {
                SMSLogDto smsLogDto = new SMSLogDto();
                BeanCopierUtils.copyProperties(ll, smsLogDto);
                smslogDtoList.add(smsLogDto);
            });
        }
        PageModelDto<SMSLogDto> pageModelDto = new PageModelDto<>();
        pageModelDto.setCount(odataObj.getCount());
        pageModelDto.setValue(smslogDtoList);

        logger.info("查询日志数据");
        return pageModelDto;
    }

    @Override
    @Transactional
    public void save(SMSLog smsLog) {
        smslogRepo.save(smsLog);
    }
}