package cs.repository.repositoryImpl.topic;

import cs.domain.topic.Filing;
import cs.repository.AbstractRepository;
import org.springframework.stereotype.Repository;

/**
 * Description: 课题归档 数据操作实现类
 * author: ldm
 * Date: 2017-9-4 15:40:48
 */
@Repository
public class FilingRepoImpl extends AbstractRepository<Filing, String> implements FilingRepo {
}