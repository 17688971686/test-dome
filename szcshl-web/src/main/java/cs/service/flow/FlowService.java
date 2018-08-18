package cs.service.flow;

import cs.common.ResultMsg;
import cs.domain.flow.HiProcessTask;
import cs.domain.flow.RuProcessTask;
import cs.domain.flow.RuTask;
import cs.domain.project.DispatchDoc;
import cs.domain.project.SignDispaWork;
import cs.model.PageModelDto;
import cs.model.flow.*;
import cs.model.project.CommentDto;
import cs.repository.odata.ODataObj;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.impl.task.TaskDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;

import java.util.List;
import java.util.Map;

public interface FlowService {

    /**
     * 流程回退
     * @param flowDto
     * @return
     */
    ResultMsg rollBackLastNode(FlowDto flowDto);

    void nextTaskDefinition(List<Node> nextNodeList, ActivityImpl activityImpl, String activityId);

    void deployementProcessByName(String path, String sourceName, String flowName);

    void deployementProcessByZip(String zipPath, String flowName);

    ProcessInstance findProcessInstanceByBusinessKey(String businessKey);

    PageModelDto<SignDispaWork> queryETasks(ODataObj odataObj);

    /**
     * 在办任务查询
     **/
    List<RuProcessTask> queryRunProcessTasks(ODataObj odataObj, boolean isUserDeal, Integer powerFlag, List<String> mngOrgIdList);

    /**
     * 根据用户ID查询待办项目
     * @param userId
     * @return
     */
    List<RuProcessTask> findRuProcessTaskByUserId(String userId);

    /**
     * 根据用户ID查询待办任务
     * @param userId
     * @return
     */
    List<RuTask> findRuTaskByUserId(String userId);
    /**
     * 获取流程处理记录
     * @param processInstanceId
     * @return
     */
    List<HiProcessTask> getProcessHistory(String processInstanceId);

    /**
     * 查询主页上的待办任务
     * @return
     */
    List<RuProcessTask> queryMyRunProcessTasks(Integer max);

    /**
     * 查询主页上的办结任务
     * @return
     */
    /*List<TaskDto> queryMyEndTasks();*/

    /**
     * 查询主页上的待办任务
     * @return
     */
    List<RuTask> queryMyHomeAgendaTask();


    PageModelDto<RuProcessTask> queryPersonTasks(ODataObj oDataObj);

    /**
     * 20170905 查询个人待办事项（除项目签收流程外）
     **/
    PageModelDto<RuTask> queryMyAgendaTask(ODataObj oDataObj);

    PageModelDto<RuTask> queryAgendaTask(ODataObj odataObj);

    /**
     * 流程处理，只有一个分支的情况
     * @param businessKey       业务ID
     * @param activiName        环节名称
     * @param flowDto           处理信息
     * @param definitionKey     流程定义KEY
     * @return
     */
    ResultMsg dealFlowByBusinessKey(String businessKey,String activiName,FlowDto flowDto,String definitionKey) throws Exception;

    /**
     * 取回流程
     * @param taskId    当前任务ID
     * @param activityId 取回节点ID
     * @param businessKey 删除工作方案的singId
     * @param allBranch  是否删除所有分支
     * @return ResultMsg
     */
    ResultMsg callBackProcess(String taskId, String activityId,String businessKey,boolean allBranch)throws Exception;

    /**
     * 根据流程实例ID查询所有任务集合
     *
     * @param processInstanceId
     * @return
     */
    List<Task> findTaskListByKey(String processInstanceId);

    /**
     * 根据业务ID，暂停流程
     * @param businessKey
     * @return
     */
    ResultMsg stopFlow(String businessKey);

    /**
     * 根据业务ID，激活流程
     * @param businessKey
     * @return
     */
    ResultMsg restartFlow(String businessKey);

    /**
     * 删除流程任务实例
     * @param taskId 任务节点ID
     * @param executionId 流程实例节点ID
     * */
    void deleteTask(String taskId, String executionId);


    /**
     * 获取流程列表
     * @return
     */
    List<Map<String, Object>> getProc();

    /**
     * 根据流程实例，获取经办人ID
     * @param processInstanceId
     * @return
     */
    List<String> findUserIdByProcessInstanceId(String processInstanceId);


    /**
     * 根据流程实例获取办理意见信息
     * @param procInstId
     * @param nodeKeys
     * @return
     */
    List<CommentDto> findCommentByProcInstId(String procInstId,List<String> nodeKeys);

    /**
     * 只要最新的评审意见
     * @param nodeKeys
     * @return
     */
    Map<String,CommentDto> filterComents(List<CommentDto> commentList,List<String> nodeKeys);

    /**
     * 流程转办
     * @param taskId        任务ID
     * @param oldUserId     原先处理人
     * @param newUserId     新处理人
     * @return
     */
    ResultMsg taskTransferAssignee(String taskId,String oldUserId,String newUserId);


    /**
     * 获取重新做工作方案的分支
     * @param signId
     * @return
     */
    List<Map<String,Object>> getBranchInfo(String signId);

    /**
     * 合并发文流程处理
     * @param processInstance
     * @param task
     * @param dp
     * @param nodeName
     * @param flowDto
     * @return
     */
    ResultMsg dealMerDisFlow(ProcessInstance processInstance, Task task, DispatchDoc dp, String nodeName, FlowDto flowDto, boolean isAgentTask) throws Exception;

    /**
     * 办结任务
     * @param odataObj
     * @return
     */
    PageModelDto<TaskDto> queryEndTasks(ODataObj odataObj);
}
