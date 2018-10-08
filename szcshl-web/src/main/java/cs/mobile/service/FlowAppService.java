package cs.mobile.service;

import cs.common.ResultMsg;
import cs.domain.project.AgentTask;
import cs.domain.sys.User;
import cs.model.flow.FlowDto;
import cs.model.sys.UserDto;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;

import java.util.List;

/**
 * Created by Administrator on 2018/2/28.
 */
public interface FlowAppService {

    /**
     * 项目流程处理
     * @param processInstance
     * @param task
     * @param flowDto
     * @param userDto
     * @return
     */
    ResultMsg dealFlow(ProcessInstance processInstance, Task task, FlowDto flowDto, UserDto userDto) throws Exception;

    String getMainDirecotr(String signid, List<AgentTask> agentTaskList, String nodeKey);

    String getMainPriUserId(String signid, List<AgentTask> agentTaskList,String nodeKey);

    /**
     * 图书流程处理
     */
    ResultMsg bookDealFlow(ProcessInstance processInstance, Task task, FlowDto flowDto, UserDto userDto);

    /**
     * 月报流程处理
     */
    ResultMsg monthlyDealFlow(ProcessInstance processInstance, Task task, FlowDto flowDto, UserDto userDto);

    /**
     * 通知公告流程处理
     */
    ResultMsg annountDealFlow(ProcessInstance processInstance, Task task, FlowDto flowDto, UserDto userDto);

    /**
     * 拟补充资料函流程处理
     */
    ResultMsg suppletterDealFlow(ProcessInstance processInstance, Task task, FlowDto flowDto, UserDto userDto);

    /**
     * 取回流程
     * @param taskId    当前任务ID
     * @param activityId 取回节点ID
     * @param businessKey 删除工作方案的singId
     * @param allBranch  是否删除所有分支
     * @return ResultMsg
     */
    ResultMsg callBackProcess(String taskId, String activityId,String businessKey,boolean allBranch,UserDto userDto)throws Exception;

    /**
     * 根据流程实例ID查询所有任务集合
     *
     * @param processInstanceId
     * @return
     */
    List<Task> findTaskListByKey(String processInstanceId);


    /**
     * 删除流程任务实例
     * @param taskId 任务节点ID
     * @param executionId 流程实例节点ID
     * */
    void deleteTask(String taskId, String executionId);
}
