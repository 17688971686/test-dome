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

import java.util.List;

public interface FlowService {

    ResultMsg rollBackLastNode(FlowDto flowDto);

    void rollBackByActiviti(FlowDto flowDto);

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
}
