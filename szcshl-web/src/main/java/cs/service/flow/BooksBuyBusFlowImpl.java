package cs.service.flow;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zsl on 2017/9/11.
 */
@Service("booksBuyBusFlowImpl")
public class BooksBuyBusFlowImpl implements IFlow {

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

            default:
        }
        return businessMap;
    }
}
