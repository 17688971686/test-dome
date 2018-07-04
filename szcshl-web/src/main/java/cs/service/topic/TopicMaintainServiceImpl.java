package cs.service.topic;

import cs.common.utils.BeanCopierUtils;
import cs.common.utils.SessionUtil;
import cs.common.utils.Validate;
import cs.domain.sys.User;
import cs.domain.topic.TopicMaintain;
import cs.domain.topic.TopicMaintain_;
import cs.model.topic.TopicMaintainDto;
import cs.repository.repositoryImpl.sys.UserRepo;
import cs.repository.repositoryImpl.topic.TopicMaintainRepo;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Description: 课题维护 业务操作实现类
 * author: zsl
 * Date: 2018-6-22 15:40:48
 */
@Service
public class TopicMaintainServiceImpl implements TopicMaintainService {
    @Autowired
    private TopicMaintainRepo topicMaintainRepo;

    @Autowired
    private UserRepo userRepo;

    /**
     * 查询课题维护详情
     * @return
     */
    @Override
    public List<TopicMaintainDto> findTopicAll(String userId) {
        Criteria criteria = topicMaintainRepo.getExecutableCriteria();
        criteria.add(Restrictions.eq(TopicMaintain_.userId.getName(),userId));
        List<TopicMaintain> topicMaintainList = criteria.list();
        //todo:初始化课题研究信息
        List<TopicMaintainDto> topicMaintainDtoList = new ArrayList<TopicMaintainDto>();
        topicMaintainList.forEach(topicMaintain -> {
            TopicMaintainDto topicMaintainDto = new TopicMaintainDto();
            BeanCopierUtils.copyProperties(topicMaintain,topicMaintainDto);
            topicMaintainDtoList.add(topicMaintainDto);
        });
        return topicMaintainDtoList;
    }
}