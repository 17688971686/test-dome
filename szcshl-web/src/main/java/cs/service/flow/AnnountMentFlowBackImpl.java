package cs.service.flow;

import cs.common.constants.FlowConstant;
import cs.domain.sys.Annountment;
import cs.domain.sys.Annountment_;
import cs.repository.repositoryImpl.sys.AnnountmentRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

import static cs.common.constants.FlowConstant.FLOW_BACK_NODEKEY;
import static cs.common.constants.FlowConstant.FLOW_BACK_USER;

/**
 * Created by hjm on 2017/11/28.
 */
@Service("annountmentFlowBackImpl")
public class AnnountMentFlowBackImpl implements IFlowBack {

    @Autowired
    private AnnountmentRepo annountmentRepo;

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
            case FlowConstant.ANNOUNT_BZ:
                backActivitiId = FlowConstant.ANNOUNT_TZ;
                dealUserParam = FlowConstant.AnnountMentFLOWParams.USER.getValue();
                break;
            case FlowConstant.ANNOUNT_FZ:
                Annountment annountment =annountmentRepo.findById(Annountment_.anId.getName(),businessKey);
                if(null == annountment.getDeptMinisterId()){//是否有部门负责人ID
                    backActivitiId = FlowConstant.ANNOUNT_TZ;
                    dealUserParam = FlowConstant.AnnountMentFLOWParams.USER.getValue();
                }else{
                    backActivitiId = FlowConstant.ANNOUNT_BZ;
                    dealUserParam = FlowConstant.AnnountMentFLOWParams.USER_BZ.getValue();
                }

                break;
            case FlowConstant.ANNOUNT_ZR:
                backActivitiId = FlowConstant.ANNOUNT_FZ;
                dealUserParam = FlowConstant.AnnountMentFLOWParams.USER_FZ.getValue();
                break;
           default:
               break;

        }
        resultMap.put(FLOW_BACK_NODEKEY,backActivitiId);
        resultMap.put(FLOW_BACK_USER,dealUserParam);
        return resultMap;
    }
}
