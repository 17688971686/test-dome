package cs.repository.repositoryImpl;

import cs.domain.MyTest;
import cs.repository.AbstractRepository;
import org.springframework.stereotype.Repository;

/**
 * Description: my test 数据操作实现类
 * User: Administrator
 * Date: 2017/5/4 18:27
 */
@Repository
public class MyTestRepoImpl extends AbstractRepository<MyTest, String> implements MyTestRepo {
}