package cs.service.flow;

import cs.common.constants.FlowConstant;
import cs.domain.archives.ArchivesLibrary;
import cs.domain.archives.ArchivesLibrary_;
import cs.repository.repositoryImpl.archives.ArchivesLibraryRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public String backActivitiId(String businessKey,String curActivitiId) {
        //根据businessKey查出数据
        ArchivesLibrary archivesLibrary =archivesLibraryRepo.findById(ArchivesLibrary_.id.getName(),businessKey);
        String backActivitiId = "";
        switch (curActivitiId){
            case FlowConstant.FLOW_ARC_BZ_SP:
                backActivitiId = FlowConstant.FLOW_ARC_SQ;
                break;
            case FlowConstant.FLOW_ARC_FGLD_SP:
                if(archivesLibrary.getDeptMinisterId()==null){
                    backActivitiId = FlowConstant.FLOW_ARC_SQ;
                }else{
                    backActivitiId = FlowConstant.FLOW_ARC_BZ_SP;
                }

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
