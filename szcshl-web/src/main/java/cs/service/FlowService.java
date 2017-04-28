package cs.service;
import java.util.List;

import org.activiti.engine.history.HistoricActivityInstance;

import cs.model.FlowHistoryDto;

public interface FlowService {

	List<FlowHistoryDto> convertHistory(String processInstanceId);
}
