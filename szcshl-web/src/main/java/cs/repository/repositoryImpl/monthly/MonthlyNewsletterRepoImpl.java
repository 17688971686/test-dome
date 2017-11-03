package cs.repository.repositoryImpl.monthly;

import cs.domain.monthly.MonthlyNewsletter;
import cs.domain.monthly.MonthlyNewsletter_;
import cs.repository.AbstractRepository;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Description: 月报简报 数据操作实现类
 * author: ldm
 * Date: 2017-9-8 11:23:41
 */
@Repository
public class MonthlyNewsletterRepoImpl extends AbstractRepository<MonthlyNewsletter, String> implements MonthlyNewsletterRepo {
    /**
     * 查询年度
     * @param businessId
     * @return
     */
    @Deprecated
    @Override
    public  String findYear(String businessId){
        Criteria criteria = getExecutableCriteria();
        criteria.add(Restrictions.eq(MonthlyNewsletter_.id.getName(),businessId));
        List<MonthlyNewsletter> monthlyNewsletterList=criteria.list();
        String year=monthlyNewsletterList.get(0).getReportMultiyear();
        return year;
    }
}