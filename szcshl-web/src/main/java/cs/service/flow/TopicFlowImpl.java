package cs.service.flow;

import cs.common.constants.Constant;
import cs.common.constants.FlowConstant;
import cs.common.utils.SessionUtil;
import cs.domain.sys.User;
import cs.domain.topic.TopicInfo;
import cs.domain.topic.TopicInfo_;
import cs.repository.repositoryImpl.flow.FlowPrincipalRepo;
import cs.repository.repositoryImpl.topic.TopicInfoRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ldm on 2017/9/05.
 */
@Service("topicFlowImpl")
public class TopicFlowImpl implements IFlow {
    @Autowired
    private FlowPrincipalRepo flowPrincipalRepo;
    @Autowired
    private TopicInfoRepo topicInfoRepo;
    /**
     * 获取流程参数
     * @param businessKey
     * @param taskDefinitionKey
     * @return
     */
    @Override
    public Map<String, Object> getFlowBusinessMap(String businessKey,String taskDefinitionKey) {
        Map<String, Object> businessMap = new HashMap<>();
        switch (taskDefinitionKey) {
            case FlowConstant.TOPIC_ZRSH_JH:
                TopicInfo topicInfo = topicInfoRepo.findById(TopicInfo_.id.getName(),businessKey);
                //如果送发改委
                if(Constant.EnumState.YES.getValue().equals(topicInfo.getSendFgw())){
                    businessMap.put(FlowConstant.FlowParams.SEND_FGW.getValue(), true);
                }
                break;
            case FlowConstant.TOPIC_GZFA:
            case FlowConstant.TOPIC_CGJD:
            case FlowConstant.TOPIC_KTBG:
            case FlowConstant.TOPIC_KTJT:
                //判断当前用户是不是第一负责人,主要控制专家评分和专家评审费
                User dealUser = flowPrincipalRepo.getFlowMainPrinByBusiId(businessKey);
                if(dealUser != null && SessionUtil.getUserId().equals(dealUser.getId())){
                    businessMap.put(FlowConstant.FlowParams.MAIN_USER.getValue(),true);
                }
                break;
            default:
        }
        return businessMap;
    }
}
