package cs.service.flow;

import cs.common.ResultMsg;
import cs.domain.flow.HiProcessTask;
import cs.domain.flow.RuProcessTask;
import cs.model.PageModelDto;
import cs.model.flow.FlowDto;
import cs.model.flow.FlowHistoryDto;
import cs.model.flow.TaskDto;
import cs.repository.odata.ODataObj;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.impl.task.TaskDefinition;
import org.activiti.engine.runtime.ProcessInstance;

import java.util.List;

public interface FlowService {

    List<FlowHistoryDto> convertHistory(String processInstanceId);

    ResultMsg rollBackLastNode(FlowDto flowDto);

    void rollBackByActiviti(FlowDto flowDto);

    ActivityImpl getActivityImpl(String taskId, String activityId);

    HistoricActivityInstance getHistoricInfoByActivityId(String processInstanceId, String activityId);

    void nextTaskDefinition(List<TaskDefinition> taskDefinitionList, ActivityImpl activityImpl, String activityId);

    void deployementProcessByName(String path, String sourceName, String flowName);

    void deployementProcessByZip(String zipPath, String flowName);

    ProcessInstance findProcessInstanceByBusinessKey(String businessKey);


    //PageModelDto<TaskDto> queryGTasks(ODataObj odataObj);

    //PageModelDto<TaskDto> queryDoingTasks(ODataObj odataObj);

    PageModelDto<TaskDto> queryETasks(ODataObj odataObj);

    /**
     * 20170706 新增
     **/
    PageModelDto<RuProcessTask> queryRunProcessTasks(ODataObj odataObj, String skip, String top, boolean isUserDeal);

    List<HiProcessTask> getProcessHistory(String processInstanceId);
}
