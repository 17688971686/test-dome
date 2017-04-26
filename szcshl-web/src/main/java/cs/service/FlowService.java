package cs.service;
import org.activiti.engine.history.HistoricActivityInstance;

public interface FlowService {

	HistoricActivityInstance getCurInstanceInfo(String proccessInstanceId);
}
