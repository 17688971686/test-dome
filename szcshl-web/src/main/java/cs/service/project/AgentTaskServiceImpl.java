package cs.service.project;

import cs.common.RandomGUID;
import cs.common.utils.BeanCopierUtils;
import cs.common.utils.Validate;
import cs.domain.project.AgentTask;
import cs.domain.project.Sign;
import cs.domain.sys.User;
import cs.model.PageModelDto;
import cs.model.project.SignDto;
import cs.repository.odata.ODataObj;
import cs.repository.repositoryImpl.project.AgentTaskRepo;
import cs.repository.repositoryImpl.sys.UserRepo;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by ldm on 2018/5/21.
 */
@Service
public class AgentTaskServiceImpl implements AgentTaskService{
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private AgentTaskRepo agentTaskRepo;
    @Autowired
    private TaskService taskService;

    @Override
    public AgentTask initNew(User user,String nodeKey) {
        User agentUser = userRepo.getCacheUserById(user.getTakeUserId());
        AgentTask agentTask = new AgentTask();
        agentTask.setAgentId((new RandomGUID()).valueAfterMD5);
        agentTask.setAgentUserId(agentUser.getId());
        agentTask.setAgentUserName(agentUser.getDisplayName());
        agentTask.setUserId(user.getId());
        agentTask.setUserName(user.getDisplayName());
        agentTask.setNodeKey(nodeKey);
        return agentTask;
    }

    @Override
    public void updateAgentInfo(List<AgentTask> agentTaskList,String processInstandId,String processName) {
        String nodeKey = agentTaskList.get(0).getNodeKey(),nodeName="",taskId="";
        List<Task> taskList = taskService.createTaskQuery().processInstanceId(processInstandId).active().list();
        for(Task task1 : taskList){
            if(nodeKey.equals(task1.getTaskDefinitionKey())){
                nodeName = task1.getName();
                taskId = task1.getId();
            }
        }
        Date now = new Date();
        for(AgentTask agentTask : agentTaskList){
            agentTask.setNodeNameValue(nodeName);
            agentTask.setTaskId(taskId);
            agentTask.setFlowName(processName);
            agentTask.setTransDate(now);
        }
        agentTaskRepo.bathUpdate(agentTaskList);
    }

    @Override
    public PageModelDto<AgentTask> get(ODataObj odataObj) {
        PageModelDto<AgentTask> pageModelDto = new PageModelDto<AgentTask>();
        List<AgentTask> taskList = agentTaskRepo.findByOdata(odataObj);
        if (Validate.isList(taskList)) {
            pageModelDto.setValue(taskList);
        } else {
            pageModelDto.setValue(null);
        }
        pageModelDto.setCount(odataObj.getCount());
        return pageModelDto;
    }
}
