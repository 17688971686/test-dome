package cs.service.flow;
import java.util.List;

import org.activiti.engine.history.HistoricActivityInstance;

import cs.model.flow.FlowHistoryDto;

public interface FlowService {

	List<FlowHistoryDto> convertHistory(String processInstanceId);
	HistoricActivityInstance getHistoricInfoByActivityId(String processInstanceId,String activityId);
}
