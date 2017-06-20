package cs.repository.repositoryImpl.sys;

import cs.domain.sys.Quartz;
import cs.repository.AbstractRepository;
import org.springframework.stereotype.Repository;

/**
 * Description: 定时器配置 数据操作实现类
 * author: ldm
 * Date: 2017-6-20 10:47:42
 */
@Repository
public class QuartzRepoImpl extends AbstractRepository<Quartz, String> implements QuartzRepo {
}