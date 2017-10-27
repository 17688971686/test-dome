package cs.service.flow;

import cs.common.FlowConstant;
import cs.domain.project.AddSuppLetter;
import cs.repository.repositoryImpl.project.AddSuppLetterRepo;
import cs.repository.repositoryImpl.project.SignBranchRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ldm on 2017/8/21.
 */
@Service("suppLetterFlowImpl")
public class SuppLetterFlowImpl implements IFlow {

    @Autowired
    private SignBranchRepo signBranchRepo;
    @Autowired
    private AddSuppLetterRepo addSuppLetterRepo;
    /**
     * 获取流程参数
     * @param businessKey
     * @param taskDefinitionKey
     * @return
     */
    @Override
    public Map<String, Object> getFlowBusinessMap(String businessKey,String taskDefinitionKey) {
        Map<String, Object> businessMap = new HashMap<>();
        switch (taskDefinitionKey) {
            case FlowConstant.FLOW_SPL_BZ_SP:
                AddSuppLetter addSuppLetter = addSuppLetterRepo.findById(businessKey);
                if(signBranchRepo.allAssistCount(addSuppLetter.getBusinessId()) == 0){
                    businessMap.put(FlowConstant.FlowParams.FGLD_FZ.getValue(),true);
                }
                break;
            default:
        }
        return businessMap;
    }
}
