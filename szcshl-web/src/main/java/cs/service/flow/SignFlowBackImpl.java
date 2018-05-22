package cs.service.flow;

import cs.common.constants.FlowConstant;
import org.springframework.stereotype.Service;

/**
 * Created by ldm on 2017/8/21.
 */
@Service("signFlowBackImpl")
public class SignFlowBackImpl implements IFlowBack {

    /**
     * 获取回退环节
     *
     * @param curActivitiId
     * @return
     */
    @Override
    public String backActivitiId(String businessKey, String curActivitiId) {
        String backActivitiId = "";
        switch (curActivitiId) {
            //分管领导审批，直接回到签收环节
            case FlowConstant.FLOW_SIGN_FGLD_FB:
                backActivitiId = FlowConstant.FLOW_SIGN_QS;
                break;
            //部长审批环节（不指定，从分管领导回退之后，会有两条记录）
            case FlowConstant.FLOW_SIGN_BMLD_SPW1:
                backActivitiId = FlowConstant.FLOW_SIGN_XMFZR1;
                break;
            case FlowConstant.FLOW_SIGN_BMLD_SPW2:
                backActivitiId = FlowConstant.FLOW_SIGN_XMFZR2;
                break;
            case FlowConstant.FLOW_SIGN_BMLD_SPW3:
                backActivitiId = FlowConstant.FLOW_SIGN_XMFZR3;
                break;
            case FlowConstant.FLOW_SIGN_BMLD_SPW4:
                backActivitiId = FlowConstant.FLOW_SIGN_XMFZR4;
                break;
            //部长审批发文，直接回退到发文申请环节
            case FlowConstant.FLOW_SIGN_BMLD_QRFW:
                backActivitiId = FlowConstant.FLOW_SIGN_FW;
                break;
            //分管领导审批发文
            case FlowConstant.FLOW_SIGN_FGLD_QRFW:
                backActivitiId = FlowConstant.FLOW_SIGN_BMLD_QRFW;
                break;
            //第二负责人回退到归档环节
            case FlowConstant.FLOW_SIGN_DSFZR_QRGD:
                backActivitiId = FlowConstant.FLOW_SIGN_GD;
                break;
            //最终归档(回退到第一负责人处理)
            case FlowConstant.FLOW_SIGN_QRGD:
                backActivitiId = FlowConstant.FLOW_SIGN_GD;
                break;
            default:
                ;
        }
        return backActivitiId;
    }
}
