package cs.repository.repositoryImpl.sys;

import cs.domain.sys.SMSLog;
import cs.domain.sys.SMSLog_;
import cs.model.sys.SMSLogDto;
import cs.repository.AbstractRepository;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

@Repository
public class SMSLogRepoImpl extends AbstractRepository<SMSLog, String> implements SMSLogRepo {

    @Override
    public boolean isSMSlogExist(String smsLogType, String projectName, String smsUserPhone, String smsUserName, String fileCode,String infoType) {
        WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
        SessionFactory factory=wac.getBean(SessionFactory.class);
        Session session=factory.openSession();
        Criteria criteria = session.createCriteria(this.getPersistentClass()); //getExecutableCriteria();
        criteria.add(Restrictions.eq(SMSLog_.smsLogType.getName(), smsLogType));
        criteria.add(Restrictions.eq(SMSLog_.smsUserPhone.getName(), smsUserPhone));
        criteria.add(Restrictions.eq(SMSLog_.userName.getName(), smsUserName));
        if (("待办").equals(infoType)){//暂时没开通待办: 代码限制
            criteria.add(Restrictions.eq(SMSLog_.projectName.getName(), projectName));
            criteria.add(Restrictions.eq(SMSLog_.smsLogType.getName(), smsLogType));
            criteria.add(Restrictions.eq(SMSLog_.smsUserPhone.getName(), smsUserPhone));
            criteria.add(Restrictions.eq(SMSLog_.userName.getName(), smsUserName));
        }else {
            criteria.add(Restrictions.eq(SMSLog_.fileCode.getName(), fileCode));

        }
        List<SMSLog> smsLogList = criteria.list();
        if (smsLogList.size()>0){
            return false;
        }
        return true;
    }


}
