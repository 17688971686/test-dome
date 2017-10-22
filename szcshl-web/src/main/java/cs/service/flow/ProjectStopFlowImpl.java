package cs.service.flow;

import cs.common.Constant;
import cs.common.FlowConstant;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ldm on 2017/8/21.
 */
@Service("projectStopFlowImpl")
public class ProjectStopFlowImpl implements IFlow {

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
            //分管领导审批，默认同意
            case FlowConstant.FLOW_STOP_FGLD_SP:
                businessMap.put("AGREE", Constant.EnumState.YES.getValue());
                break;

            default:
        }
        return businessMap;
    }
}
