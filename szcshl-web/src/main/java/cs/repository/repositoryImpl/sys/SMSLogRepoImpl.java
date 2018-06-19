package cs.repository.repositoryImpl.sys;

import cs.domain.sys.SMSLog;
import cs.model.sys.SMSLogDto;
import cs.repository.AbstractRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class SMSLogRepoImpl extends AbstractRepository<SMSLog, String> implements SMSLogRepo {

    @Override
    public boolean isSMSlogExist(String smsLogType, String projectName, String smsUserPhone, String smsUserName, String fileCode) {
        return false;
    }


}
