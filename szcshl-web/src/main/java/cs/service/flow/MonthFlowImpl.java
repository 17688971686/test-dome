package cs.service.flow;

import cs.common.Constant;
import cs.common.FlowConstant;
import cs.domain.project.AddSuppLetter;
import cs.domain.project.AddSuppLetter_;
import cs.domain.sys.Annountment;
import cs.domain.sys.Annountment_;
import cs.repository.repositoryImpl.project.AddSuppLetterRepo;
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
@Service("monthFlowImpl")
public class MonthFlowImpl implements IFlow {

    @Autowired
    private AddSuppLetterRepo addSuppLetterRepo;
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
            case FlowConstant.MONTH_YB:
                AddSuppLetter addSuppLetter = addSuppLetterRepo.findById(AddSuppLetter_.id.getName(), businessKey);
                Set role=userRepo.getUserRole(addSuppLetter.getCreatedBy());//取到用户角色,根据ID
                boolean isUser=false;
                for (Object str : role) {//循环判断是否是部门负责人
                    if(str.equals(Constant.EnumFlowNodeGroupName.DEPT_LEADER.getValue())){
                        isUser=true;
                    }
                }
                if(isUser){//是否有部门负责人ID
                    businessMap.put(FlowConstant.MonthlyNewsletterFlowParams.MONTH_USER.getValue(), Constant.EnumState.STOP.getValue());
                }
                break;
            case FlowConstant.ANNOUNT_ZR:
                businessMap.put("AGREE", Constant.EnumState.YES.getValue());
                break;
            default:
        }
        return businessMap;
    }
}
