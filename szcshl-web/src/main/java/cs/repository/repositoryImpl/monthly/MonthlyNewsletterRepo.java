package cs.repository.repositoryImpl.monthly;

import cs.common.ResultMsg;
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
    @Deprecated
    String findYear(String businessId);


    /**
     * 更改月报简报类型状态
     * @param id
     * @param value
     * @return
     */
    ResultMsg updateMonthlyType(String id, String value);
}
