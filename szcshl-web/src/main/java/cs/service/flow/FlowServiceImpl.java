package cs.service.flow;

import java.util.ArrayList;
import java.util.List;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.task.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cs.common.utils.ActivitiUtil;
import cs.common.utils.Validate;
import cs.model.flow.FlowHistoryDto;

@Service
public class FlowServiceImpl implements FlowService{

	@Autowired
	private TaskService taskService;
	@Autowired
	private ProcessEngine processEngine;
	
	@Override
	public List<FlowHistoryDto> convertHistory(String processInstanceId) {
		List<HistoricActivityInstance> list = processEngine.getHistoryService().
	    		createHistoricActivityInstanceQuery().processInstanceId(processInstanceId).list();  
		List<Comment> cmlist = taskService.getProcessInstanceComments(processInstanceId);
		if(list != null){			 						
			List<FlowHistoryDto> reultList = new ArrayList<FlowHistoryDto>(list.size());
			list.forEach(h ->{
				FlowHistoryDto fh = new FlowHistoryDto();
				fh.setTaskId(h.getTaskId());
				fh.setActivityId(h.getActivityId());
				fh.setActivityName(h.getActivityName());
				fh.setDurationInMillis(h.getDurationInMillis());
				fh.setDuration(ActivitiUtil.formatTime(h.getDurationInMillis()));
				fh.setStartTime(h.getStartTime());
				fh.setEndTime(h.getEndTime());
				fh.setAssignee(h.getAssignee());
				fh.setProcessInstanceId(h.getProcessInstanceId());	
				fh.setMessage(getTaskMessage(cmlist,h.getTaskId()));
				reultList.add(fh);
			});
			return reultList;
		}else{
			return null;
		}
		
	}

	private String getTaskMessage(List<Comment> list, String taskId) {		
		StringBuffer message = new StringBuffer() ;
		if(list != null && Validate.isString(taskId)){			
			list.forEach(cl ->{			
				if(taskId.equals(cl.getTaskId())){
					message.append(cl.getFullMessage());
					return ;
				}				
			});		
		}	
		return message.toString();
	}	
	
	public HistoricActivityInstance getHistoricInfoByActivityId(String processInstanceId,String activityId){
		List<HistoricActivityInstance> listH = processEngine.getHistoryService().
	    		createHistoricActivityInstanceQuery().processInstanceId(processInstanceId).desc().list(); 		
		HistoricActivityInstance resultObj = null;
		if(listH != null){	
			for(int i=0,l=listH.size();i<l;i++){
				HistoricActivityInstance h = listH.get(i);
				if(activityId.equals(h.getActivityId())){
					resultObj = h;
					break;
				}
			}			
		}
		return resultObj;
	}
}
