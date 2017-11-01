package cs.repository.repositoryImpl.monthly;

import cs.domain.monthly.MonthlyNewsletter;
import cs.repository.IRepository;

/**
 * Description: 月报简报 数据操作实现接口
 * author: ldm
 * Date: 2017-9-8 11:23:41
 */
public interface MonthlyNewsletterRepo extends IRepository<MonthlyNewsletter, String> {
    /**
     * 根据业务ID查询年份。
     * @param businessId
     * @return
     */
    String findYear(String businessId);


}
