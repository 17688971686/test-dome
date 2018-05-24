package cs.service.flow;

import cs.common.constants.FlowConstant;
import cs.domain.sys.Annountment;
import cs.domain.sys.Annountment_;
import cs.repository.repositoryImpl.sys.AnnountmentRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public String backActivitiId(String businessKey,String curActivitiId) {
        //根据businessKey查出数据
        Annountment annountment =annountmentRepo.findById(Annountment_.anId.getName(),businessKey);
        String backActivitiId = "";
        switch (curActivitiId){
            case FlowConstant.ANNOUNT_BZ:
                backActivitiId = FlowConstant.ANNOUNT_TZ;
                break;
            case FlowConstant.ANNOUNT_FZ:
                if(annountment.getDeptMinisterId()==null){//是否有部门负责人ID
                    backActivitiId = FlowConstant.ANNOUNT_TZ;
                }else{
                    backActivitiId = FlowConstant.ANNOUNT_BZ;
                }

                break;
            case FlowConstant.ANNOUNT_ZR:
                backActivitiId = FlowConstant.ANNOUNT_FZ;
                break;
           default:
               break;

        }
        return backActivitiId;
    }
}
