package cs.service.flow;

import cs.common.constants.Constant;
import cs.common.constants.FlowConstant;
import cs.domain.sys.Annountment;
import cs.domain.sys.Annountment_;
import cs.repository.repositoryImpl.sys.AnnountmentRepo;
import cs.repository.repositoryImpl.sys.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by hjm on 2017/11/20.
 */
@Service("annountMentFlowImpl")
public class AnnountMentFlowImpl implements IFlow {

    @Autowired
    private AnnountmentRepo annountmentRepo;
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
            case FlowConstant.ANNOUNT_TZ:
                Annountment annountment =annountmentRepo.findById(Annountment_.anId.getName(),businessKey);
                Set role=userRepo.getUserRole(annountment.getCreatedBy());//取到用户角色,根据ID
                boolean isUser=false;
                for (Object str : role) {//循环判断是否是部门负责人
                    if(str.equals(Constant.EnumFlowNodeGroupName.DEPT_LEADER.getValue())){
                        isUser=true;
                    }
                }
                if(isUser){//是否有部门负责人ID
                    businessMap.put(FlowConstant.AnnountMentFLOWParams.ANNOUNT_USER.getValue(), Constant.EnumState.STOP.getValue());
                }
                break;
            case FlowConstant.ANNOUNT_ZR:
                businessMap.put(FlowConstant.SignFlowParams.AGREE.getValue(), Constant.EnumState.YES.getValue());
                break;
            default:
        }
        return businessMap;
    }
}
