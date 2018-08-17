package cs.service.flow;

import cs.common.constants.FlowConstant;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

import static cs.common.constants.FlowConstant.FLOW_BACK_NODEKEY;
import static cs.common.constants.FlowConstant.FLOW_BACK_USER;

/**
 * Created by ldm on 2017/9/11.
 */
@Service("topicFlowBackImpl")
public class TopicFlowBackImpl implements IFlowBack {

    /**
     * 获取回退环节(全部指定，避免报错)
     * @param curActivitiId
     * @return
     */
    @Override
    public Map<String,Object> backActivitiId(String businessKey, String curActivitiId) {
        Map<String,Object> resultMap = new HashMap<>();
        String backActivitiId = "";
        String dealUserParam = "";
        switch (curActivitiId){
            case FlowConstant.TOPIC_BZSH_JH:
                backActivitiId = FlowConstant.TOPIC_JHTC;
                dealUserParam = FlowConstant.FlowParams.USER.getValue();
                break;
            case FlowConstant.TOPIC_FGLD_JH:
                backActivitiId = FlowConstant.TOPIC_BZSH_JH;
                dealUserParam = FlowConstant.FlowParams.USER_BZ.getValue();
                break;
            case FlowConstant.TOPIC_ZRSH_JH:
                backActivitiId = FlowConstant.TOPIC_FGLD_JH;
                dealUserParam = FlowConstant.FlowParams.USER_FGLD.getValue();
                break;
            case FlowConstant.TOPIC_KTFZR:
                backActivitiId = FlowConstant.TOPIC_ZRSH_JH;
                dealUserParam = FlowConstant.FlowParams.USER_ZR.getValue();
                break;
            case FlowConstant.TOPIC_BZSH_FA:
                backActivitiId = FlowConstant.TOPIC_KTFZR;
                dealUserParam = FlowConstant.FlowParams.USER.getValue();
                break;
            case FlowConstant.TOPIC_FGLD_FA:
                backActivitiId = FlowConstant.TOPIC_BZSH_FA;
                dealUserParam = FlowConstant.FlowParams.USER_BZ.getValue();
                break;
            case FlowConstant.TOPIC_ZRSH_FA:
                backActivitiId = FlowConstant.TOPIC_FGLD_FA;
                dealUserParam = FlowConstant.FlowParams.USER_FGLD.getValue();
                break;

            case FlowConstant.TOPIC_ZLGD:
                backActivitiId = FlowConstant.TOPIC_KTFZR_QR;
                dealUserParam = FlowConstant.FlowParams.USER.getValue();
                break;
           default:
               break;
        }
        resultMap.put(FLOW_BACK_NODEKEY,backActivitiId);
        resultMap.put(FLOW_BACK_USER,dealUserParam);
        return resultMap;
    }
}
