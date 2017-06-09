package cs.service.flow;

import cs.model.PageModelDto;
import cs.model.flow.FlowDto;
import cs.model.flow.FlowHistoryDto;
import cs.model.flow.TaskDto;
import cs.repository.odata.ODataObj;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.impl.task.TaskDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;

import java.util.List;

public interface FlowService {

	List<FlowHistoryDto> convertHistory(String processInstanceId);
	
	void RollBackLastNode(FlowDto flowDto);
	
	void RollBackByActiviti(FlowDto flowDto);
	
	ActivityImpl getActivityImpl(String taskId,String activityId);

	HistoricActivityInstance getHistoricInfoByActivityId(String processInstanceId,String activityId);
	
	void nextTaskDefinition(List<TaskDefinition> taskDefinitionList,ActivityImpl activityImpl,String activityId);
	
	void deployementProcessByName(String path,String sourceName,String flowName);
	
	void deployementProcessByZip(String zipPath,String flowName);
	
	ProcessInstance findProcessInstanceByBusinessKey(String businessKey);
	
	Task findTaskByBusinessKey(String businessKey);

	PageModelDto<TaskDto> queryGTasks(ODataObj odataObj);

    PageModelDto<TaskDto> queryETasks(ODataObj odataObj);

    PageModelDto<TaskDto> queryDoingTasks(ODataObj odataObj);
}
