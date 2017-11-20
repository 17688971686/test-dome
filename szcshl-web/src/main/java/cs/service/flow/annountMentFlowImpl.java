package cs.service.flow;

import cs.common.Constant;
import cs.common.FlowConstant;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by hjm on 2017/11/20.
 */
@Service("annountMentFlowImpl")
public class annountMentFlowImpl implements IFlow {

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
            case FlowConstant.ANNOUNT_ZR:
                businessMap.put("AGREE", Constant.EnumState.YES.getValue());
                break;
            default:
        }
        return businessMap;
    }
}
