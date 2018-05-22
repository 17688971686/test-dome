package cs.service.flow;

import cs.common.constants.FlowConstant;
import cs.domain.project.AddSuppLetter;
import cs.domain.project.AddSuppLetter_;
import cs.repository.repositoryImpl.project.AddSuppLetterRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public String backActivitiId(String businessKey,String curActivitiId) {
        //根据businessKey查出数据
        AddSuppLetter addSuppLetter = addSuppLetterRepo.findById(AddSuppLetter_.id.getName(), businessKey);
        String backActivitiId = "";
        switch (curActivitiId){
            case FlowConstant.MONTH_BZ:
                backActivitiId = FlowConstant.MONTH_YB;
                break;
            case FlowConstant.MONTH_FG:
                if(addSuppLetter.getDeptMinisterId()==null){//是否有部门负责人ID
                    backActivitiId = FlowConstant.MONTH_YB;
                }else{
                    backActivitiId = FlowConstant.MONTH_BZ;
                }

                break;
            case FlowConstant.MONTH_ZR:
                backActivitiId = FlowConstant.MONTH_FG;
                break;
           default:
               break;

        }
        return backActivitiId;
    }
}
