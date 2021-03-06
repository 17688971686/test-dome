package cs.service.flow;

import cs.common.constants.Constant;
import cs.common.constants.FlowConstant;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ldm on 2017/8/21.
 */
@Service("appraiseFlowImpl")
public class AppraiseFlowImpl implements IFlow {

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
            case FlowConstant.FLOW_ARP_BZ_SP:
            case FlowConstant.FLOW_ARP_ZHB_SP:
                businessMap.put(FlowConstant.SignFlowParams.AGREE.getValue(), Constant.EnumState.YES.getValue());
                break;
            default:
        }
        return businessMap;
    }
}
