package cs.service.flow;

import cs.common.constants.Constant;
import cs.common.constants.FlowConstant;
import cs.common.utils.DateUtils;
import cs.domain.archives.ArchivesLibrary;
import cs.domain.archives.ArchivesLibrary_;
import cs.repository.repositoryImpl.archives.ArchivesLibraryRepo;
import cs.repository.repositoryImpl.sys.UserRepo;
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
    @Autowired
    private UserRepo userRepo;
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
            //填报环节
            case FlowConstant.FLOW_ARC_SQ:
                ArchivesLibrary archivesLibrary = archivesLibraryRepo.findById(ArchivesLibrary_.id.getName(), businessKey);
                Set role=userRepo.getUserRoles(archivesLibrary.getCreatedBy());//取到用户角色
                boolean isUser=false;
                for (Object str : role) {//循环判断是否是部门负责人
                    if(str.equals(Constant.EnumFlowNodeGroupName.DEPT_LEADER.getValue())){
                        isUser=true;
                    }
                }
                if(isUser){//是否是部门负责人
                    businessMap.put(FlowConstant.FlowParams.FGLD_FZ.getValue(), Constant.EnumState.STOP.getValue());
                }else{
                    businessMap.put(FlowConstant.FlowParams.BZ_FZ.getValue(), Constant.EnumState.PROCESS.getValue());
                }
                break;

            //审批环节，默认都是通过
            case FlowConstant.FLOW_ARC_BZ_SP:
                businessMap.put(FlowConstant.SignFlowParams.AGREE.getValue(), Constant.EnumState.YES.getValue());
                break;
            case FlowConstant.FLOW_ARC_FGLD_SP:
                ArchivesLibrary archivesLibrary2 = archivesLibraryRepo.findById(ArchivesLibrary_.id.getName(), businessKey);
                if( Constant.EnumState.YES.getValue().equals(archivesLibrary2.getIsLendOut()) ){
                    businessMap.put(FlowConstant.FlowParams.ZR_FZ.getValue(), Constant.EnumState.YES.getValue());
                }else if( Constant.EnumState.NO.getValue().equals(archivesLibrary2.getArchivesType())){
                    businessMap.put(FlowConstant.FlowParams.USER_GDY.getValue(), Constant.EnumState.YES.getValue());
                }
                businessMap.put(FlowConstant.SignFlowParams.AGREE.getValue(), Constant.EnumState.YES.getValue());
                break;
            case FlowConstant.FLOW_ARC_ZR_SP:
                businessMap.put(FlowConstant.SignFlowParams.AGREE.getValue(), Constant.EnumState.YES.getValue());
                break;
            case FlowConstant.FLOW_ARC_GDY:
                businessMap.put("RETURNDATE", DateUtils.converToString(new Date(),null));
                break;
            default:
        }
        return businessMap;
    }
}
