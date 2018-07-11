package cs.service.flow;

import cs.common.constants.FlowConstant;
import org.springframework.stereotype.Service;
import cs.common.constants.FlowConstant.SignFlowParams;
import java.util.HashMap;
import java.util.Map;

import static cs.common.constants.FlowConstant.FLOW_BACK_NODEKEY;
import static cs.common.constants.FlowConstant.FLOW_BACK_USER;

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
    public Map<String,Object> backActivitiId(String businessKey, String curActivitiId) {
        Map<String,Object> resultMap = new HashMap<>();
        String backActivitiId = "";
        String dealUserParam = "";
        switch (curActivitiId) {
            //分管领导审批，直接回到签收环节
            case FlowConstant.FLOW_SIGN_FGLD_FB:
                backActivitiId = FlowConstant.FLOW_SIGN_QS;
                dealUserParam = SignFlowParams.USER_QS.getValue();
                break;
            //部长审批环节（不指定，从分管领导回退之后，会有两条记录）
            case FlowConstant.FLOW_SIGN_BMLD_SPW1:
                backActivitiId = FlowConstant.FLOW_SIGN_XMFZR1;
                dealUserParam = SignFlowParams.USER_FZR1.getValue();
                break;
            case FlowConstant.FLOW_SIGN_BMLD_SPW2:
                backActivitiId = FlowConstant.FLOW_SIGN_XMFZR2;
                dealUserParam = SignFlowParams.USER_FZR2.getValue();
                break;
            case FlowConstant.FLOW_SIGN_BMLD_SPW3:
                backActivitiId = FlowConstant.FLOW_SIGN_XMFZR3;
                dealUserParam = SignFlowParams.USER_FZR3.getValue();
                break;
            case FlowConstant.FLOW_SIGN_BMLD_SPW4:
                backActivitiId = FlowConstant.FLOW_SIGN_XMFZR4;
                dealUserParam = SignFlowParams.USER_FZR4.getValue();
                break;
            //部长审批发文，直接回退到发文申请环节
            case FlowConstant.FLOW_SIGN_BMLD_QRFW:
                backActivitiId = FlowConstant.FLOW_SIGN_FW;
                dealUserParam = SignFlowParams.USER_FZR1.getValue();
                break;
            //分管领导审批发文
            case FlowConstant.FLOW_SIGN_FGLD_QRFW:
                backActivitiId = FlowConstant.FLOW_SIGN_BMLD_QRFW;
                dealUserParam = SignFlowParams.USER_BZ1.getValue();
                break;
            //主任审批发文，退回分管副主任
            case FlowConstant.FLOW_SIGN_ZR_QRFW:
                backActivitiId = FlowConstant.FLOW_SIGN_FGLD_QRFW;
                dealUserParam = SignFlowParams.USER_FGLD1.getValue();
                break;
            //第二负责人回退到归档环节
            case FlowConstant.FLOW_SIGN_DSFZR_QRGD:
                backActivitiId = FlowConstant.FLOW_SIGN_GD;
                dealUserParam = SignFlowParams.USER_BZ1.getValue();
                break;
            //最终归档(回退到第一负责人处理)
            case FlowConstant.FLOW_SIGN_QRGD:
                backActivitiId = FlowConstant.FLOW_SIGN_GD;
                dealUserParam = SignFlowParams.USER_BZ1.getValue();
                break;
            default:
                ;
        }
        resultMap.put(FLOW_BACK_NODEKEY,backActivitiId);
        resultMap.put(FLOW_BACK_USER,dealUserParam);
        return resultMap;
    }
}
