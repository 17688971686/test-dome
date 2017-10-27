package cs.service.flow;

import cs.common.Constant;
import cs.common.FlowConstant;
import cs.common.utils.BeanCopierUtils;
import cs.common.utils.DateUtils;
import cs.common.utils.SessionUtil;
import cs.common.utils.Validate;
import cs.domain.archives.ArchivesLibrary;
import cs.domain.archives.ArchivesLibrary_;
import cs.domain.project.Sign;
import cs.domain.project.Sign_;
import cs.domain.sys.OrgDept;
import cs.domain.sys.User;
import cs.model.sys.UserDto;
import cs.repository.repositoryImpl.archives.ArchivesLibraryRepo;
import cs.repository.repositoryImpl.expert.ExpertReviewRepo;
import cs.repository.repositoryImpl.project.SignBranchRepo;
import cs.repository.repositoryImpl.project.SignMergeRepo;
import cs.repository.repositoryImpl.project.SignRepo;
import cs.repository.repositoryImpl.sys.OrgDeptRepo;
import cs.repository.repositoryImpl.sys.UserRepo;
import cs.service.project.DispatchDocService;
import cs.service.project.SignPrincipalService;
import cs.service.sys.OrgDeptService;
import cs.service.sys.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by ldm on 2017/8/21.
 */
@Service("archivesFlowImpl")
public class ArchivesFlowImpl implements IFlow {

    @Autowired
    private ArchivesLibraryRepo archivesLibraryRepo;
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
            //审批环节，默认都是通过
            case FlowConstant.FLOW_ARC_BZ_SP:
                ArchivesLibrary archivesLibrary1 = archivesLibraryRepo.findById(ArchivesLibrary_.id.getName(), businessKey);
                if( Constant.EnumState.NO.getValue().equals(archivesLibrary1.getIsSZECUser()) || Constant.EnumState.YES.getValue().equals(archivesLibrary1.getArchivesType()) || Constant.EnumState.YES.getValue().equals(archivesLibrary1.getIsLendOut())){
                    businessMap.put(FlowConstant.FlowParams.FGLD_FZ.getValue(), Constant.EnumState.YES.getValue());
                }
                businessMap.put("AGREE", Constant.EnumState.YES.getValue());
                break;
            case FlowConstant.FLOW_ARC_FGLD_SP:
                ArchivesLibrary archivesLibrary2 = archivesLibraryRepo.findById(ArchivesLibrary_.id.getName(), businessKey);
                if( Constant.EnumState.YES.getValue().equals(archivesLibrary2.getIsLendOut()) || Constant.EnumState.YES.getValue().equals(archivesLibrary2.getArchivesType())){
                    businessMap.put(FlowConstant.FlowParams.ZR_FZ.getValue(), Constant.EnumState.YES.getValue());
                }
                businessMap.put("AGREE", Constant.EnumState.YES.getValue());
                break;
            case FlowConstant.FLOW_ARC_ZR_SP:
                businessMap.put("AGREE", Constant.EnumState.YES.getValue());
                break;
            case FlowConstant.FLOW_ARC_GDY:
                businessMap.put("RETURNDATE", DateUtils.converToString(new Date(),null));
                break;
            default:
        }
        return businessMap;
    }
}