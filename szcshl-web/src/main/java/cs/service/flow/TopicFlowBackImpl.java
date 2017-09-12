package cs.service.flow;

import cs.common.FlowConstant;
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

            case FlowConstant.TOPIC_BZSH_FA:
                backActivitiId = FlowConstant.TOPIC_GZFA;
                break;
            case FlowConstant.TOPIC_FGLD_FA:
                backActivitiId = FlowConstant.TOPIC_BZSH_FA;
                break;
            case FlowConstant.TOPIC_ZRSH_FA:
                backActivitiId = FlowConstant.TOPIC_FGLD_FA;
                break;

            case FlowConstant.TOPIC_BZSH_BG:
                backActivitiId = FlowConstant.TOPIC_KTBG;
                break;
            case FlowConstant.TOPIC_FGLD_BG:
                backActivitiId = FlowConstant.TOPIC_BZSH_BG;
                break;
            case FlowConstant.TOPIC_ZRSH_BG:
                backActivitiId = FlowConstant.TOPIC_FGLD_BG;
                break;

            case FlowConstant.TOPIC_BZSH_JT:
                backActivitiId = FlowConstant.TOPIC_KTJT;
                break;
            case FlowConstant.TOPIC_FGLD_JT:
                backActivitiId = FlowConstant.TOPIC_BZSH_JT;
                break;
            case FlowConstant.TOPIC_ZRSH_JT:
                backActivitiId = FlowConstant.TOPIC_FGLD_JT;
                break;

            case FlowConstant.TOPIC_BZSH_GD:
                backActivitiId = FlowConstant.TOPIC_ZLGD;
                break;
            case FlowConstant.TOPIC_GDY_QR:
                backActivitiId = FlowConstant.TOPIC_BZSH_GD;
                break;
           default:
               break;

        }
        return backActivitiId;
    }
}
