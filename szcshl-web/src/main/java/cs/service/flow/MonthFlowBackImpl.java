package cs.service.flow;

import cs.common.constants.FlowConstant;
import cs.domain.project.AddSuppLetter;
import cs.domain.project.AddSuppLetter_;
import cs.repository.repositoryImpl.project.AddSuppLetterRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

import static cs.common.constants.FlowConstant.FLOW_BACK_NODEKEY;
import static cs.common.constants.FlowConstant.FLOW_BACK_USER;

/**
 * Created by hjm on 2017/11/28.
 */
@Service("monthFlowBackImpl")
public class MonthFlowBackImpl implements IFlowBack {

    @Autowired
    private AddSuppLetterRepo addSuppLetterRepo;

    /**
     * 获取回退环节(全部指定，避免报错)
     * @param curActivitiId
     * @return
     */
    @Override
    public Map<String,Object> backActivitiId(String businessKey, String curActivitiId) {
        //根据businessKey查出数据
        AddSuppLetter addSuppLetter = addSuppLetterRepo.findById(AddSuppLetter_.id.getName(), businessKey);
        Map<String,Object> resultMap = new HashMap<>();
        String backActivitiId = "";
        String dealUserParam = "";

        switch (curActivitiId){
            case FlowConstant.MONTH_BZ:
                backActivitiId = FlowConstant.MONTH_YB;
                dealUserParam = FlowConstant.FlowParams.USER.getValue();
                break;
            case FlowConstant.MONTH_FG:
                if(addSuppLetter.getDeptMinisterId()==null){//是否有部门负责人ID
                    backActivitiId = FlowConstant.MONTH_YB;
                    dealUserParam = FlowConstant.FlowParams.USER.getValue();
                }else{
                    backActivitiId = FlowConstant.MONTH_BZ;
                    dealUserParam = FlowConstant.FlowParams.USER_BZ.getValue();
                }
                break;
            case FlowConstant.MONTH_ZR:
                backActivitiId = FlowConstant.MONTH_FG;
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
