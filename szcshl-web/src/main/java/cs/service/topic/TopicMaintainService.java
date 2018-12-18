package cs.service.topic;

import cs.model.topic.TopicMaintainDto;

import java.util.List;

/**
 * Description: 课题维护 业务操作接口
 * author: zsl
 * Date: 2018-6-22 15:40:48
 */
public interface TopicMaintainService {

    List<TopicMaintainDto> findTopicAll(String userId,String yearName,String quarter);
}
