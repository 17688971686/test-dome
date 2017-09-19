package cs.service.flow;

import cs.common.FlowConstant;
import cs.common.utils.SessionUtil;
import cs.domain.sys.User;
import cs.repository.repositoryImpl.flow.FlowPrincipalRepo;
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
