package cs.service.flow;

import cs.common.FlowConstant;
import org.springframework.stereotype.Service;

/**
 * Created by hjm on 2017/11/2.
 */
@Service("archivesFlowBackImpl")
public class ArchivesFlowBackImpl implements IFlowBack {

    /**
     * 获取回退环节(全部指定，避免报错)
     * @param curActivitiId
     * @return
     */
    @Override
    public String backActivitiId(String businessKey,String curActivitiId) {
        String backActivitiId = "";
        switch (curActivitiId){
            case FlowConstant.FLOW_ARC_BZ_SP:
                backActivitiId = FlowConstant.FLOW_ARC_SQ;
                break;
            case FlowConstant.FLOW_ARC_FGLD_SP:
                backActivitiId = FlowConstant.FLOW_ARC_BZ_SP;
                break;
            case FlowConstant.FLOW_ARC_ZR_SP:
                backActivitiId = FlowConstant.FLOW_ARC_FGLD_SP;
                break;
           default:
               break;

        }
        return backActivitiId;
    }
}
