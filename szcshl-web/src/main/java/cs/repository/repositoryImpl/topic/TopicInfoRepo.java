package cs.repository.repositoryImpl.topic;

import cs.domain.topic.TopicInfo;
import cs.repository.IRepository;

/**
 * Description: 课题研究 数据操作实现接口
 * author: sjy
 * Date: 2017-9-4 15:04:55
 */
public interface TopicInfoRepo extends IRepository<TopicInfo, String> {

    /**
     * 通过业务id获取课题信息
     * @param businessId
     * @return
     */
    TopicInfo findTopByBusinessId(String businessId);
}
