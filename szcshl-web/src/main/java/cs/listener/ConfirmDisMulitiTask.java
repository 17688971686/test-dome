package cs.listener;

import cs.common.Constant;
import org.activiti.engine.delegate.DelegateExecution;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

/**
 * 发文会签监听类
 * Created by shenning on 2017/8/7.
 */
@Component
public class ConfirmDisMulitiTask {

    /**
     * 是否所有环节都完成
     * @param execution
     * @return
     */
    public boolean completeTask(DelegateExecution execution) {
        Integer nrOfInstances = (Integer) execution.getVariable("nrOfInstances"),
                nrOfCompletedInstances = (Integer) execution.getVariable("nrOfCompletedInstances");

        Boolean unPass = (Boolean) execution.getVariable(Constant.SignFlowParams.UNPASS.getValue());
        //只要有人不同意，则回退
        if(unPass != null && unPass){
            return true;
        }
        //System.out.println("总的会签任务数量：" + execution.getVariable("nrOfInstances") + "当前获取的会签任务数量：" + execution.getVariable("nrOfActiveInstances") + " - " + "已经完成的会签任务数量：" + execution.getVariable("nrOfCompletedInstances"));
        //System.out.println("I am invoked.");
        return (nrOfCompletedInstances==nrOfInstances)?true:false;
    }
}
