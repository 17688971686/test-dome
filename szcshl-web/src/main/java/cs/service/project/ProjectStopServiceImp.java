package cs.service.project;

import org.springframework.stereotype.Service;

@Service
public class ProjectStopServiceImp implements ProjectStopService {

	/*@Autowired
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
           //if (processInstance.getProcessDefinitionKey().equals(Constant.EnumFlow.FINAL_SIGN.getValue())) {
              signService.stopFlow(signid);
              *//*Task task = taskService.createTaskQuery().taskId(taskid).active().singleResult();
              task.set*//*
              
           //}
			
		}
	}

	@Transactional
	@Override
	public void projectStart(String signid,String taskid) {
		if (Validate.isString(signid)) {
				//ProcessInstance processInstance = flowService.findProcessInstanceByBusinessKey(signid);
				//runtimeService.suspendProcessInstanceById(processInstance.getId());
			   //if (processInstance.getProcessDefinitionKey().equals(Constant.EnumFlow.FINAL_SIGN.getValue())) {
		            signService.restartFlow(signid);
		        //}
			
		}
	}*/

}
