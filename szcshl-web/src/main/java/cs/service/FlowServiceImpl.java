package cs.service;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.history.HistoricActivityInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FlowServiceImpl implements FlowService{

	@Autowired
	private ProcessEngine processEngine;
	
	//获取当前处理节点的信息
	@Override
	public HistoricActivityInstance getCurInstanceInfo(String proccessInstanceId) {
		List<HistoricActivityInstance> list = processEngine.getHistoryService().
				createHistoricActivityInstanceQuery().processInstanceId(proccessInstanceId).unfinished().list();
		if(list != null && list.size() > 0){
			Collections.sort(list,new Comparator<HistoricActivityInstance>() {
		        @Override
		        public int compare(HistoricActivityInstance compare,HistoricActivityInstance bycompare) {
		        	//开始时间迟的在前
		            if(bycompare.getStartTime().getTime() > compare.getStartTime().getTime()){
		               return 1;
		            }
		            if(bycompare.getStartTime().getTime() < compare.getStartTime().getTime()){
		              return -1;
		            }
		           return 0;
		       }});
			
			return list.get(0);
		}else{
			return null;
		}
		
	}


}
