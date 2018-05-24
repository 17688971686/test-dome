package cs.service.flow;

import cs.common.constants.FlowConstant;
import org.springframework.stereotype.Service;

/**
 * Created by hjm on 2017/11/2.
 */
@Service("appraiseFlowBackImpl")
public class AppraiseFlowBackImpl implements IFlowBack {

    /**
     * 获取回退环节(全部指定，避免报错)
     * @param curActivitiId
     * @return
     */
    @Override
    public String backActivitiId(String businessKey,String curActivitiId) {
        String backActivitiId = "";
        switch (curActivitiId){
            case FlowConstant.FLOW_ARP_BZ_SP:
                backActivitiId = FlowConstant.FLOW_ARP_FZR;
                break;
            case FlowConstant.FLOW_ARP_ZHB_SP:
                backActivitiId = FlowConstant.FLOW_ARP_BZ_SP;
                break;
           default:
               break;

        }
        return backActivitiId;
    }
}
