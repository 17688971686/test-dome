package cs.service.flow;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipInputStream;

import cs.common.Constant;
import cs.domain.sys.User;
import org.activiti.engine.HistoryService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricProcessInstanceQuery;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.bpmn.behavior.UserTaskActivityBehavior;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.PvmActivity;
import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.impl.pvm.process.ProcessDefinitionImpl;
import org.activiti.engine.impl.pvm.process.TransitionImpl;
import org.activiti.engine.impl.task.TaskDefinition;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Comment;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cs.common.ICurrentUser;
import cs.common.cache.CacheFactory;
import cs.common.cache.DefaultCacheFactory;
import cs.common.cache.ICache;
import cs.common.utils.ActivitiUtil;
import cs.common.utils.Validate;
import cs.model.PageModelDto;
import cs.model.flow.FlowDto;
import cs.model.flow.FlowHistoryDto;
import cs.model.flow.TaskDto;
import cs.repository.odata.ODataObj;

@Service
public class FlowServiceImpl implements FlowService {
    private static Logger log = Logger.getLogger(FlowServiceImpl.class);
    @Autowired
    private TaskService taskService;
    @Autowired
    private ProcessEngine processEngine;
    @Autowired
    private HistoryService historyService;
    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    private ICurrentUser currentUser;

    @Override
    public List<FlowHistoryDto> convertHistory(String processInstanceId) {
        List<HistoricActivityInstance> list = processEngine.getHistoryService().createHistoricActivityInstanceQuery()
                .processInstanceId(processInstanceId).list();

        List<Comment> cmlist = taskService.getProcessInstanceComments(processInstanceId);

        if (list != null) {
            List<FlowHistoryDto> reultList = new ArrayList<FlowHistoryDto>(list.size());
            list.forEach(h -> {
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
                fh.setMessage(getTaskMessage(cmlist, h.getTaskId()));
                reultList.add(fh);
            });
            return reultList;
        } else {
            return null;
        }

    }

    private String getTaskMessage(List<Comment> list, String taskId) {
        StringBuffer message = new StringBuffer();
        if (list != null && Validate.isString(taskId)) {
            list.forEach(cl -> {
                if (taskId.equals(cl.getTaskId())) {
                    message.append(cl.getFullMessage());
                    return;
                }
            });
        }
        return message.toString();
    }

    @Override
    @Transactional
    public void rollBackLastNode(FlowDto flowDto) {
        // 取得当前任务.当前任务节点
        HistoricTaskInstance currTask = historyService.createHistoricTaskInstanceQuery().taskId(flowDto.getTaskId()).singleResult();
        // 取得流程实例，流程实例
        ProcessInstance instance = runtimeService.createProcessInstanceQuery()
                .processInstanceId(currTask.getProcessInstanceId()).singleResult();
        if (instance == null) {
            //流程已结束
            return;
        }

        // 取得流程定义
        ProcessDefinitionEntity definition = (ProcessDefinitionEntity) ((RepositoryServiceImpl) repositoryService)
                .getDeployedProcessDefinition(currTask.getProcessDefinitionId());
        if (definition == null) {
            //流程定义未找到
            return;
        }

        // 取得上一步活动
        ActivityImpl currActivity = ((ProcessDefinitionImpl) definition).findActivity(currTask.getTaskDefinitionKey());

        //也就是节点间的连线
        List<PvmTransition> nextTransitionList = currActivity.getIncomingTransitions();
        // 清除当前活动的出口
        List<PvmTransition> oriPvmTransitionList = new ArrayList<PvmTransition>();

        //新建一个节点连线关系集合       
        List<PvmTransition> pvmTransitionList = currActivity.getOutgoingTransitions();
        for (PvmTransition pvmTransition : pvmTransitionList) {
            oriPvmTransitionList.add(pvmTransition);
        }
        pvmTransitionList.clear();

        // 建立新出口
        List<TransitionImpl> newTransitions = new ArrayList<TransitionImpl>();
        for (PvmTransition nextTransition : nextTransitionList) {
            PvmActivity nextActivity = nextTransition.getSource();
            ActivityImpl nextActivityImpl = ((ProcessDefinitionImpl) definition).findActivity(nextActivity.getId());
            TransitionImpl newTransition = currActivity.createOutgoingTransition();
            newTransition.setDestination(nextActivityImpl);
            newTransitions.add(newTransition);
        }

        // 完成任务
        List<Task> tasks = taskService.createTaskQuery().processInstanceId(instance.getId())
                .taskDefinitionKey(currTask.getTaskDefinitionKey()).list();

        for (Task task : tasks) {
            taskService.addComment(task.getId(), instance.getId(), flowDto.getDealOption());    //添加处理信息
            taskService.complete(task.getId(), ActivitiUtil.flowArguments(null, "张一帆", "", false));
            historyService.deleteHistoricTaskInstance(task.getId());
        }

        // 恢复方向
        for (TransitionImpl transitionImpl : newTransitions) {
            currActivity.getOutgoingTransitions().remove(transitionImpl);
        }
        for (PvmTransition pvmTransition : oriPvmTransitionList) {
            pvmTransitionList.add(pvmTransition);
        }
    }

    /**
     *
     * @param taskId    任务ID
     * @param activityId 任务环节ID 而不是实例环节ID
     * @return
     */
    @Override
    public ActivityImpl getActivityImpl(String taskId, String activityId) {
        HistoricTaskInstance currTask = historyService.createHistoricTaskInstanceQuery().taskId(taskId).singleResult();
        ProcessDefinitionEntity definition = (ProcessDefinitionEntity) ((RepositoryServiceImpl) repositoryService)
                .getDeployedProcessDefinition(currTask.getProcessDefinitionId());
        if (definition == null) {
            return null;
        }

        return definition.findActivity(activityId);
    }

    public HistoricActivityInstance getHistoricInfoByActivityId(String processInstanceId, String activityId) {
        List<HistoricActivityInstance> listH = processEngine.getHistoryService().
                createHistoricActivityInstanceQuery().processInstanceId(processInstanceId).desc().list();
        HistoricActivityInstance resultObj = null;
        if (listH != null) {
            for (int i = 0, l = listH.size(); i < l; i++) {
                HistoricActivityInstance h = listH.get(i);
                if (activityId.equals(h.getActivityId())) {
                    resultObj = h;
                    break;
                }
            }
        }
        return resultObj;
    }

    @Override
    public void rollBackByActiviti(FlowDto flowDto) {
        // 取得当前任务.当前任务节点
        HistoricTaskInstance currTask = historyService.createHistoricTaskInstanceQuery()
                .taskId(flowDto.getTaskId()).singleResult();
        // 取得流程实例，流程实例
        ProcessInstance instance = runtimeService.createProcessInstanceQuery()
                .processInstanceId(currTask.getProcessInstanceId()).singleResult();
        if (instance == null) {
            log.error("流程回退异常，流程已经结束了！");
            return;
        }

        // 取得流程定义
        ProcessDefinitionEntity definition = (ProcessDefinitionEntity) ((RepositoryServiceImpl) repositoryService)
                .getDeployedProcessDefinition(currTask.getProcessDefinitionId());
        if (definition == null) {
            log.error("流程回退异常，流程定义未找到");
            return;
        }
        //取得当前活动节点
        ActivityImpl currActivity = ((ProcessDefinitionImpl) definition).findActivity(currTask.getTaskDefinitionKey());

        //也就是节点间的连线
        //获取来源节点的关系
        List<PvmTransition> nextTransitionList = currActivity.getIncomingTransitions();
        // 清除当前活动的出口
        List<PvmTransition> oriPvmTransitionList = new ArrayList<PvmTransition>();
        //新建一个节点连线关系集合
        //获取出口节点的关系
        List<PvmTransition> pvmTransitionList = currActivity.getOutgoingTransitions();
        //
        for (PvmTransition pvmTransition : pvmTransitionList) {
            oriPvmTransitionList.add(pvmTransition);
        }
        pvmTransitionList.clear();

        // 建立新出口
        List<TransitionImpl> newTransitions = new ArrayList<TransitionImpl>();
        for (PvmTransition nextTransition : nextTransitionList) {
            ActivityImpl nextActivityImpl = ((ProcessDefinitionImpl) definition).findActivity(flowDto.getRollBackActiviti());
            TransitionImpl newTransition = currActivity.createOutgoingTransition();
            newTransition.setDestination(nextActivityImpl);
            newTransitions.add(newTransition);
        }

        // 完成任务
        List<Task> tasks = taskService.createTaskQuery().processInstanceId(instance.getId())
                .taskDefinitionKey(currTask.getTaskDefinitionKey()).list();
        for (Task task : tasks) {
            taskService.addComment(task.getId(), instance.getId(), flowDto.getDealOption());    //添加处理信息
            taskService.complete(task.getId(), ActivitiUtil.flowArguments(null, flowDto.getBackNodeDealUser(), "", false));
            historyService.deleteHistoricTaskInstance(task.getId());
        }
        // 恢复方向
        for (TransitionImpl transitionImpl : newTransitions) {
            currActivity.getOutgoingTransitions().remove(transitionImpl);
        }
        for (PvmTransition pvmTransition : oriPvmTransitionList) {
            pvmTransitionList.add(pvmTransition);
        }
    }

    @Override
    public void nextTaskDefinition(List<TaskDefinition> taskDefinitionList, ActivityImpl activityImpl, String activityId) {
        PvmActivity ac = null;
        //如果遍历节点为用户任务并且节点不是当前节点信息   
        if ("userTask".equals(activityImpl.getProperty("type")) && !activityId.equals(activityImpl.getId())) {
            //获取该节点下一个节点信息   
            TaskDefinition taskDefinition = ((UserTaskActivityBehavior) activityImpl.getActivityBehavior()).getTaskDefinition();
            taskDefinitionList.add(taskDefinition);
        } else {
            //获取节点所有流向线路信息
            List<PvmTransition> outTransitions = activityImpl.getOutgoingTransitions();
            List<PvmTransition> outTransitionsTemp = null;
            if (outTransitions != null) {
                for (PvmTransition tr : outTransitions) {
                    ac = tr.getDestination(); //获取线路的终点节点
                    //如果流向线路为排他网关
                    if ("exclusiveGateway".equals(ac.getProperty("type")) || "inclusiveGateway".equals(ac.getProperty("type"))) {
                        outTransitionsTemp = ac.getOutgoingTransitions();
                        if (outTransitionsTemp.size() == 1) {
                            nextTaskDefinition(taskDefinitionList, (ActivityImpl) outTransitionsTemp.get(0).getDestination(), activityId);
                        } else if (outTransitionsTemp.size() > 1) {
                            for (PvmTransition tr1 : outTransitionsTemp) {
                                nextTaskDefinition(taskDefinitionList, (ActivityImpl) tr1.getDestination(), activityId);
                            }
                        }
                    } else if ("userTask".equals(ac.getProperty("type"))) {
                        TaskDefinition taskDefinition = ((UserTaskActivityBehavior) ((ActivityImpl) ac).getActivityBehavior()).getTaskDefinition();
                        taskDefinitionList.add(taskDefinition);
                    }
                }
            }
        }
    }

    @Override
    public void deployementProcessByName(String path, String sourceName, String flowName) {
        String bpmnName = sourceName + ".bpmn", pngName = sourceName + ".png";
        if (path != null && path.trim().length() > 0) {
            bpmnName = path + File.separatorChar + bpmnName;
            pngName = path + File.separatorChar + pngName;
        }

        InputStream inputStreamBpmn = this.getClass().getResourceAsStream(path + File.pathSeparator + bpmnName);
        InputStream inputStreampng = this.getClass().getResourceAsStream(path + File.pathSeparator + pngName);
        //部署流程定义
        Deployment deployment = processEngine.getRepositoryService()
                .createDeployment()//创建部署对象
                .name(flowName)
                .addInputStream(bpmnName, inputStreamBpmn)//部署加载资源文件
                .addInputStream(pngName, inputStreampng)
                .deploy();

        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().deploymentId(deployment.getId()).singleResult();
        log.info("流程部署成功,流程名称-" + processDefinition.getName() + ",流程ID-" + processDefinition.getId());
    }

    @Override
    public void deployementProcessByZip(String zipPath, String flowName) {
        InputStream in = this.getClass().getClassLoader().getResourceAsStream(zipPath);
        ZipInputStream zipIn = new ZipInputStream(in);
        Deployment deployment = repositoryService.createDeployment().addZipInputStream(zipIn).name(flowName).deploy();
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().deploymentId(deployment.getId()).singleResult();
        log.info("流程部署成功,流程名称-" + processDefinition.getName() + ",流程ID-" + processDefinition.getId());
    }

    @Override
    public ProcessInstance findProcessInstanceByBusinessKey(String businessKey) {
        return runtimeService.createProcessInstanceQuery().processInstanceBusinessKey(businessKey).singleResult();
    }

    @Override
    public Task findTaskByBusinessKey(String businessKey) {
        return taskService.createTaskQuery().processInstanceBusinessKey(businessKey).singleResult();
    }

    /**
     * 查询个人待办任务
     */
    @Override
    public PageModelDto<TaskDto> queryGTasks(ODataObj odataObj) {
        CacheFactory cacheFactory = new DefaultCacheFactory();
        ICache cache = cacheFactory.getCache();

        PageModelDto<TaskDto> pageModelDto = new PageModelDto<TaskDto>();

        TaskQuery taskQuery = taskService.createTaskQuery();
        taskQuery.taskCandidateOrAssigned(currentUser.getLoginUser().getLoginName());

        int total = Integer.valueOf(String.valueOf(taskQuery.count()));
        List<Task> tasks = taskQuery.orderByTaskCreateTime().desc().listPage(odataObj.getSkip(), odataObj.getTop());
        if (tasks != null && tasks.size() > 0) {
            List<TaskDto> list = new ArrayList<TaskDto>(tasks.size());
            tasks.forEach(t -> {
                ProcessInstance pi = (ProcessInstance) cache.get(t.getProcessInstanceId());
                if (pi == null || !Validate.isString(pi.getId())) {
                    pi = runtimeService.createProcessInstanceQuery().processInstanceId(t.getProcessInstanceId()).singleResult();
                    cache.put(t.getProcessInstanceId(), pi);
                }
                TaskDto taskDto = new TaskDto();
                taskDto.setBusinessKey(pi.getBusinessKey());
                taskDto.setBusinessName(pi.getName());
                taskDto.setFlowKey(pi.getProcessDefinitionKey());
                taskDto.setFlowName(pi.getProcessDefinitionName());
                taskDto.setTaskId(t.getId());
                taskDto.setTaskName(t.getName());
                taskDto.setFormKey(t.getFormKey());
                taskDto.setParentTaskId(t.getParentTaskId());
                taskDto.setProcessInstanceId(t.getProcessInstanceId());
                taskDto.setProcessDefinitionId(t.getProcessDefinitionId());
                taskDto.setProcessVariables(t.getProcessVariables());
                taskDto.setCreateDate(t.getCreateTime());
                taskDto.setSuspended(t.isSuspended());
                taskDto.setTaskDefinitionKey(t.getTaskDefinitionKey());
                taskDto.setAssignee(t.getAssignee());
                list.add(taskDto);
            });
            pageModelDto.setValue(list);
        }
        pageModelDto.setCount(total);

        return pageModelDto;
    }

    @Override
    public PageModelDto<TaskDto> queryETasks(ODataObj odataObj) {
        PageModelDto<TaskDto> pageModelDto = new PageModelDto<TaskDto>();

        HistoryService historyService = processEngine.getHistoryService();
        HistoricProcessInstanceQuery hq = historyService.createHistoricProcessInstanceQuery().finished();

        int total = Integer.valueOf(String.valueOf(hq.count()));
        pageModelDto.setCount(total);

        List<HistoricProcessInstance> historicList = hq.orderByProcessInstanceEndTime().desc().listPage(odataObj.getSkip(), odataObj.getTop());
        if (historicList != null && historicList.size() > 0) {
            List<TaskDto> list = new ArrayList<TaskDto>(historicList.size());
            historicList.forEach(h -> {
                TaskDto taskDto = new TaskDto();
                taskDto.setBusinessKey(h.getBusinessKey());
                taskDto.setBusinessName(h.getName());
                taskDto.setProcessInstanceId(h.getId());
                taskDto.setProcessVariables(h.getProcessVariables());
                taskDto.setProcessDefinitionId(h.getProcessDefinitionId());
                taskDto.setCreateDate(h.getStartTime());
                taskDto.setEndDate(h.getEndTime());
                taskDto.setDurationInMillis(h.getDurationInMillis());
                taskDto.setDurationTime(ActivitiUtil.formatTime(h.getDurationInMillis()));

                list.add(taskDto);
            });
            pageModelDto.setValue(list);
        }

        return pageModelDto;
    }

    @Override
    public PageModelDto<TaskDto> queryDoingTasks(ODataObj odataObj) {
        CacheFactory cacheFactory = new DefaultCacheFactory();
        ICache cache = cacheFactory.getCache();

        PageModelDto<TaskDto> pageModelDto = new PageModelDto<TaskDto>();

        TaskQuery taskQuery = taskService.createTaskQuery();
        int total = Integer.valueOf(String.valueOf(taskQuery.count()));
        List<Task> tasks = taskQuery.orderByTaskCreateTime().desc().listPage(odataObj.getSkip(), odataObj.getTop());
        if (tasks != null && tasks.size() > 0) {
            List<TaskDto> list = new ArrayList<TaskDto>(tasks.size());
            tasks.forEach(t -> {
                ProcessInstance pi = (ProcessInstance) cache.get(t.getProcessInstanceId());
                if (pi == null || !Validate.isString(pi.getId())) {
                    pi = runtimeService.createProcessInstanceQuery().processInstanceId(t.getProcessInstanceId()).singleResult();
                    cache.put(t.getProcessInstanceId(), pi);
                }
                TaskDto taskDto = new TaskDto();
                taskDto.setBusinessKey(pi.getBusinessKey());
                taskDto.setBusinessName(pi.getName());
                taskDto.setFlowKey(pi.getProcessDefinitionKey());
                taskDto.setFlowName(pi.getProcessDefinitionName());
                taskDto.setTaskId(t.getId());
                taskDto.setTaskName(t.getName());
                taskDto.setFormKey(t.getFormKey());
                taskDto.setParentTaskId(t.getParentTaskId());
                taskDto.setProcessInstanceId(t.getProcessInstanceId());
                taskDto.setProcessDefinitionId(t.getProcessDefinitionId());
                taskDto.setProcessVariables(t.getProcessVariables());
                taskDto.setCreateDate(t.getCreateTime());
                taskDto.setSuspended(t.isSuspended());
                taskDto.setTaskDefinitionKey(t.getTaskDefinitionKey());
                taskDto.setAssignee(t.getAssignee());
                list.add(taskDto);
            });
            pageModelDto.setValue(list);
        }
        pageModelDto.setCount(total);

        return pageModelDto;
    }
}
