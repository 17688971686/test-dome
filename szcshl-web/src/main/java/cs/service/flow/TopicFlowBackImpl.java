package cs.service.flow;

import cs.common.constants.FlowConstant;
import org.springframework.stereotype.Service;

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
    public String backActivitiId(String businessKey,String curActivitiId) {
        String backActivitiId = "";
        switch (curActivitiId){
            case FlowConstant.TOPIC_BZSH_JH:
                backActivitiId = FlowConstant.TOPIC_JHTC;
                break;
            case FlowConstant.TOPIC_FGLD_JH:
                backActivitiId = FlowConstant.TOPIC_BZSH_JH;
                break;
            case FlowConstant.TOPIC_ZRSH_JH:
                backActivitiId = FlowConstant.TOPIC_FGLD_JH;
                break;
            case FlowConstant.TOPIC_KTFZR:
                backActivitiId = FlowConstant.TOPIC_ZRSH_JH;
                break;
            case FlowConstant.TOPIC_BZSH_FA:
                backActivitiId = FlowConstant.TOPIC_KTFZR;
                break;
            case FlowConstant.TOPIC_FGLD_FA:
                backActivitiId = FlowConstant.TOPIC_BZSH_FA;
                break;
            case FlowConstant.TOPIC_ZRSH_FA:
                backActivitiId = FlowConstant.TOPIC_FGLD_FA;
                break;

            case FlowConstant.TOPIC_ZLGD:
                backActivitiId = FlowConstant.TOPIC_KTFZR_QR;
                break;
           default:
               break;
        }
        return backActivitiId;
    }
}
