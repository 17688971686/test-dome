package cs.service.flow;

import cs.common.constants.FlowConstant;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

import static cs.common.constants.FlowConstant.*;

/**
 * Created by Administrator on 2018/7/19 0019.
 */
@Service("workHisFlowBackImpl")
public class WorkHisFlowBackImpl implements IFlowBack {
    Map<String,Object> resultMap = new HashMap<>();
    String backActivitiId = "";
    String dealUserParam = "";

    @Override
    public Map<String, Object> backActivitiId(String businessKey, String taskDefinitionKey) {
        switch (taskDefinitionKey){
            case WPHIS_BMLD_SPW:
                backActivitiId = FlowConstant.WPHIS_XMFZR;
                dealUserParam = FlowConstant.FlowParams.USERS.getValue();
                break;
            case WPHIS_FGLD_SPW:
                backActivitiId = FlowConstant.WPHIS_BMLD_SPW;
                dealUserParam = FlowConstant.FlowParams.USER_BZ.getValue();
                break;

        }
        resultMap.put(FLOW_BACK_NODEKEY,backActivitiId);
        resultMap.put(FLOW_BACK_USER,dealUserParam);
        return resultMap;
    }
}
