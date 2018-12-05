package cs.listener;

import cs.common.constants.FlowConstant;
import org.activiti.engine.delegate.DelegateExecution;
import org.springframework.stereotype.Component;

/**
 * 发文会签监听类
 * Created by ldm on 2017/8/7.
 */
@Component
public class ConfirmDisMulitiTask {

    /**
     * 是否所有环节都完成
     *
     * @param execution
     * @return
     */
    public boolean completeTask(DelegateExecution execution) {
        Integer nrOfInstances = (Integer) execution.getVariable("nrOfInstances"),
                nrOfCompletedInstances = (Integer) execution.getVariable("nrOfCompletedInstances");

        Boolean pass = (Boolean) execution.getVariable(FlowConstant.SignFlowParams.XMFZR_SP.getValue());
        //只要有人不同意，则回退
        if (pass != null && pass == false) {
            return true;
        }
        return (nrOfCompletedInstances == nrOfInstances) ? true : false;
    }

    /**
     * 协办部长审批
     *
     * @param execution
     * @return
     */
    public boolean directorAudit(DelegateExecution execution) {
        Integer nrOfInstances = (Integer) execution.getVariable("nrOfInstances"),
                nrOfCompletedInstances = (Integer) execution.getVariable("nrOfCompletedInstances");

        Boolean pass = (Boolean) execution.getVariable(FlowConstant.SignFlowParams.XBBZ_SP.getValue());
        //只要有人不同意，则回退
        if (pass != null && pass == false) {
            return true;
        }
        return (nrOfCompletedInstances == nrOfInstances) ? true : false;
    }

    /**
     * 协办副主任审批
     *
     * @param execution
     * @return
     */
    public boolean sLeaderAudit(DelegateExecution execution) {
        Integer nrOfInstances = (Integer) execution.getVariable("nrOfInstances"),
                nrOfCompletedInstances = (Integer) execution.getVariable("nrOfCompletedInstances");

        Boolean pass = (Boolean) execution.getVariable(FlowConstant.SignFlowParams.XBFZR_SP.getValue());
        //只要有人不同意，则回退
        if (pass != null && pass == false) {
            return true;
        }
        return (nrOfCompletedInstances == nrOfInstances) ? true : false;
    }

    /**
     * 领导会签，只要每个领导会签即可
     *
     * @param execution
     * @return
     */
    public boolean leadConSign(DelegateExecution execution) {
        Integer nrOfInstances = (Integer) execution.getVariable("nrOfInstances"),
                nrOfCompletedInstances = (Integer) execution.getVariable("nrOfCompletedInstances");

        return (nrOfCompletedInstances == nrOfInstances) ? true : false;
    }
}
