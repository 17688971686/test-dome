package cs.service.flow;

import cs.common.ResultMsg;
import cs.domain.flow.HiProcessTask;
import cs.domain.flow.RuProcessTask;
import cs.domain.flow.RuTask;
import cs.model.PageModelDto;
import cs.model.flow.FlowDto;
import cs.model.flow.FlowHistoryDto;
import cs.model.flow.Node;
import cs.model.flow.TaskDto;
import cs.repository.odata.ODataObj;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.impl.task.TaskDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;

import java.util.List;

public interface FlowService {

    ResultMsg rollBackLastNode(FlowDto flowDto);

    void nextTaskDefinition(List<Node> nextNodeList, ActivityImpl activityImpl, String activityId);

    void deployementProcessByName(String path, String sourceName, String flowName);

    void deployementProcessByZip(String zipPath, String flowName);

    ProcessInstance findProcessInstanceByBusinessKey(String businessKey);

    PageModelDto<TaskDto> queryETasks(ODataObj odataObj);

    /**
     * 20170706 项目签收流程接口
     **/
    PageModelDto<RuProcessTask> queryRunProcessTasks(ODataObj odataObj, boolean isUserDeal);

    List<HiProcessTask> getProcessHistory(String processInstanceId);

    List<RuProcessTask> queryMyRunProcessTasks();

    List<TaskDto> queryMyEndTasks();

    PageModelDto<RuProcessTask> queryPersonTasks(ODataObj oDataObj);

    /**
     * 20170905 查询个人待办事项（除项目签收流程外）
     **/
    PageModelDto<RuTask> queryMyAgendaTask(ODataObj oDataObj);

    PageModelDto<RuTask> queryAgendaTask(ODataObj odataObj);

    /**
     * 流程处理，只有一个分支的情况
     * @param businessKey       业务ID
     * @param activiName        环节名称
     * @param flowDto           处理信息
     * @param definitionKey     流程定义KEY
     * @return
     */
    ResultMsg dealFlowByBusinessKey(String businessKey,String activiName,FlowDto flowDto,String definitionKey);

    /**
     * 取回流程
     * @param taskId    当前任务ID
     * @param activityId 取回节点ID
     * @return ResultMsg
     */
    ResultMsg callBackProcess(String taskId, String activityId)throws Exception;

    /**
     * 根据流程实例ID和任务key值查询所有同级任务集合
     *
     * @param processInstanceId
     * @param key
     * @return
     */
    List<Task> findTaskListByKey(String processInstanceId, String key);

    /**
     * 根据业务ID，暂停流程
     * @param businessKey
     * @return
     */
    ResultMsg stopFlow(String businessKey);

    /**
     * 根据业务ID，激活流程
     * @param businessKey
     * @return
     */
    ResultMsg restartFlow(String businessKey);
}
