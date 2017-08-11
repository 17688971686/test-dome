package cs.repository.repositoryImpl.financial;

import cs.domain.financial.FinancialManager;
import cs.repository.AbstractRepository;
import org.springframework.stereotype.Repository;

/**
 * Description: 财务管理 数据操作实现类
 * author: sjy
 * Date: 2017-8-7 11:32:03
 */
@Repository
public class FinancialManagerRepoImpl extends AbstractRepository<FinancialManager, String> implements FinancialManagerRepo {
}