package cs.repository.repositoryImpl.sys;

import cs.domain.sys.SMSLog;
import cs.domain.sys.SysConfig;
import cs.model.sys.SMSLogDto;
import cs.repository.IRepository;

import java.util.List;

/**
 * Description: 系统参数 数据操作实现接口
 * author: ldm
 * Date: 2017-6-13 14:28:16
 */
public interface SMSLogRepo extends IRepository<SMSLog, String> {
    boolean isSMSlogExist(String smsLogType , String projectName,String smsUserPhone,
    String smsUserName,String fileCode);


}
