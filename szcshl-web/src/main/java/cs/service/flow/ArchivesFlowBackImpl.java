package cs.service.flow;

import cs.common.constants.FlowConstant;
import cs.domain.archives.ArchivesLibrary;
import cs.domain.archives.ArchivesLibrary_;
import cs.repository.repositoryImpl.archives.ArchivesLibraryRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

import static cs.common.constants.FlowConstant.FLOW_BACK_NODEKEY;
import static cs.common.constants.FlowConstant.FLOW_BACK_USER;

/**
 * Created by hjm on 2017/11/2.
 */
@Service("archivesFlowBackImpl")
public class ArchivesFlowBackImpl implements IFlowBack {

    @Autowired
    private ArchivesLibraryRepo archivesLibraryRepo;

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
            case FlowConstant.FLOW_ARC_BZ_SP:
                backActivitiId = FlowConstant.FLOW_ARC_SQ;
                dealUserParam = FlowConstant.FlowParams.USER.getValue();
                break;
            case FlowConstant.FLOW_ARC_FGLD_SP:
                ArchivesLibrary archivesLibrary =archivesLibraryRepo.findById(ArchivesLibrary_.id.getName(),businessKey);
                if(null == archivesLibrary.getDeptMinisterId()){
                    backActivitiId = FlowConstant.FLOW_ARC_SQ;
                    dealUserParam = FlowConstant.FlowParams.USER.getValue();
                }else{
                    backActivitiId = FlowConstant.FLOW_ARC_BZ_SP;
                    dealUserParam = FlowConstant.FlowParams.USER_BZ.getValue();
                }

                break;
            case FlowConstant.FLOW_ARC_ZR_SP:
                backActivitiId = FlowConstant.FLOW_ARC_FGLD_SP;
                dealUserParam = FlowConstant.FlowParams.USER_FGLD.getValue();
                break;
           default:
               break;

        }
        resultMap.put(FLOW_BACK_NODEKEY,backActivitiId);
        resultMap.put(FLOW_BACK_USER,dealUserParam);
        return resultMap;
    }
}
