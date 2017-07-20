package cs.service.project;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.transaction.Transactional;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cs.common.Constant;
import cs.common.ICurrentUser;
import cs.common.utils.DateUtils;
import cs.common.utils.Validate;
import cs.domain.project.ProjectStop;
import cs.domain.project.Sign;
import cs.domain.project.Sign_;
import cs.repository.repositoryImpl.project.ProjectStopRepo;
import cs.repository.repositoryImpl.project.SignRepo;
import cs.service.flow.FlowService;

@Service
public class ProjectStopServiceImp implements ProjectStopService {

	@Autowired
	private ProjectStopRepo projectStopRepo;

	@Autowired
	private SignRepo signRepo;
	
	@Autowired
	private SignService signService;
	
	@Autowired
	private FlowService flowService;
	
	@Autowired
	private RuntimeService runtimeService;

	@Autowired
	private ICurrentUser currentUser;
	
	@Autowired
	private TaskService taskService;

	@Transactional
	@Override
	public void addProjectStop(String signid,String taskid) {
		if (Validate.isString(signid)) {
		   //ProcessInstance processInstance = flowService.findProcessInstanceByBusinessKey(signid);
		   //runtimeService.suspendProcessInstanceById(processInstance.getId());
          // if (processInstance.getProcessDefinitionKey().equals(Constant.EnumFlow.FINAL_SIGN.getValue())) {
              signService.stopFlow(signid);
              /*Task task = taskService.createTaskQuery().taskId(taskid).active().singleResult();
              task.set*/
              
           //}
			
		}
	}

	@Transactional
	@Override
	public void projectStart(String signid,String taskid) {
		if (Validate.isString(signid)) {
			 //ProcessInstance processInstance = flowService.findProcessInstanceByBusinessKey(signid);
			   //if (processInstance.getProcessDefinitionKey().equals(Constant.EnumFlow.FINAL_SIGN.getValue())) {
		            signService.restartFlow(signid);
		        //}
			
		}
	}

}
