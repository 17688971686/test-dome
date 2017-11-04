package cs.repository.repositoryImpl.topic;

import cs.domain.topic.TopicInfo;
import cs.domain.topic.TopicInfo_;
import cs.repository.AbstractRepository;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Description: 课题研究 数据操作实现类
 * author: sjy
 * Date: 2017-9-4 15:04:55
 */
@Repository
public class TopicInfoRepoImpl extends AbstractRepository<TopicInfo, String> implements TopicInfoRepo {

    /**
     * 通过业务id获取课题信息
     * @param businessId
     * @return
     */
    @Override
    public TopicInfo findTopByBusinessId(String businessId) {
        Criteria criteria = getExecutableCriteria();
        criteria.add(Restrictions.eq(TopicInfo_.id.getName() , businessId));
        List<TopicInfo> topicInfoList = criteria.list();
        if(topicInfoList!=null && topicInfoList.size()>0){
            return topicInfoList.get(0);
        }else{
            return null;
        }
    }
}