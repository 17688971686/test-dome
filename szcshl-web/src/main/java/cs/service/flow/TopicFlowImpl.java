package cs.service.flow;

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
        return businessMap;
    }
}
