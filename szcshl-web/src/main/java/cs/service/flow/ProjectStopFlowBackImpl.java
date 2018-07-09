package cs.service.flow;

import cs.common.constants.FlowConstant;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

import static cs.common.constants.FlowConstant.FLOW_BACK_NODEKEY;
import static cs.common.constants.FlowConstant.FLOW_BACK_USER;

/**
 * Created by hjm on 2017/12/13.
 */
@Service("projectStopFlowBackImpl")
public class ProjectStopFlowBackImpl implements IFlowBack {

    /**
     * 获取回退环节(全部指定，避免报错)
     *
     * @param curActivitiId
     * @return
     */
    @Override
    public Map<String, Object> backActivitiId(String businessKey, String curActivitiId) {
        Map<String, Object> resultMap = new HashMap<>();
        String backActivitiId = "";
        String dealUserParam = "";
        switch (curActivitiId) {
            case FlowConstant.FLOW_STOP_BZ_SP:
                backActivitiId = FlowConstant.FLOW_STOP_FZR;
                dealUserParam = FlowConstant.SignFlowParams.USER.getValue();
                break;
            case FlowConstant.FLOW_STOP_FGLD_SP:
                backActivitiId = FlowConstant.FLOW_STOP_BZ_SP;
                dealUserParam =  FlowConstant.SignFlowParams.USER_BZ1.getValue();
                break;
            default:
                break;
        }
        resultMap.put(FLOW_BACK_NODEKEY, backActivitiId);
        resultMap.put(FLOW_BACK_USER, dealUserParam);
        return resultMap;
    }
}
