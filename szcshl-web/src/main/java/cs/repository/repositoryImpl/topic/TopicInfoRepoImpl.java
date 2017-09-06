package cs.repository.repositoryImpl.topic;

import cs.domain.topic.TopicInfo;
import cs.repository.AbstractRepository;
import org.springframework.stereotype.Repository;

/**
 * Description: 课题研究 数据操作实现类
 * author: sjy
 * Date: 2017-9-4 15:04:55
 */
@Repository
public class TopicInfoRepoImpl extends AbstractRepository<TopicInfo, String> implements TopicInfoRepo {
}