package cs.repository.repositoryImpl.monthly;

import cs.domain.monthly.MonthlyNewsletter;
import cs.repository.AbstractRepository;
import org.springframework.stereotype.Repository;

/**
 * Description: 月报简报 数据操作实现类
 * author: ldm
 * Date: 2017-9-8 11:23:41
 */
@Repository
public class MonthlyNewsletterRepoImpl extends AbstractRepository<MonthlyNewsletter, String> implements MonthlyNewsletterRepo {
}