package cs.service.project;

import cs.common.constants.Constant;
import cs.common.constants.FlowConstant;
import cs.common.HqlBuilder;
import cs.common.ResultMsg;
import cs.common.utils.*;
import cs.domain.project.*;
import cs.domain.sys.User;
import cs.model.PageModelDto;
import cs.model.flow.FlowDto;
import cs.model.project.ProjectStopDto;
import cs.repository.odata.ODataObj;
import cs.repository.repositoryImpl.project.ProjectStopRepo;
import cs.repository.repositoryImpl.project.SignDispaWorkRepo;
import cs.repository.repositoryImpl.project.SignRepo;
import cs.repository.repositoryImpl.sys.UserRepo;
import cs.service.flow.FlowService;
import cs.service.rtx.RTXSendMsgPool;
import cs.service.sys.UserService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @author ldm
 */
@Service
public class ProjectStopServiceImpl implements ProjectStopService {
    private static Logger logger = Logger.getLogger(ProjectStopServiceImpl.class);
    @Autowired
    private ProjectStopRepo projectStopRepo;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private SignRepo signRepo;
    @Autowired
    private ProcessEngine processEngine;
    @Autowired
    private SignDispaWorkRepo signDispaWorkRepo;

    //flow service
    @Autowired
    private TaskService taskService;
    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private FlowService flowService;
    @Autowired
    private AgentTaskService agentTaskService;
    @Autowired
    private UserService userService;

    @Override
    @Transactional
    public List<ProjectStopDto> findProjectStopBySign(String signId) {
        HqlBuilder hqlBuilder = HqlBuilder.create();
        hqlBuilder.append("select ps from " + ProjectStop.class.getSimpleName() + " ps where ps." + ProjectStop_.sign.getName() + "." + Sign_.signid.getName() + "=:signId");
       hqlBuilder.append(" and " + ProjectStop_.approveStatus.getName() +"=:approveStatus ");
        hqlBuilder.append("  order by  createdDate desc");
        hqlBuilder.setParam("signId", signId);
        hqlBuilder.setParam("approveStatus" , Constant.EnumState.NO.getValue());
        List<ProjectStop> psList = projectStopRepo.findByHql(hqlBuilder);
        List<ProjectStopDto> projectStopDtoList = new ArrayList<>();
        for (ProjectStop pt : psList) {
            ProjectStopDto projectStopDto = new ProjectStopDto();
            BeanCopierUtils.copyPropertiesIgnoreNull(pt, projectStopDto);
            projectStopDtoList.add(projectStopDto);
        }
        return projectStopDtoList;
    }

    /**
     * 根据项目ID，初始化项目暂停信息
     *
     * @param signId
     * @return
     */
    @Override
    public SignDispaWork findSignBySignId(String signId) {
        return signDispaWorkRepo.findById("signid", signId);
    }

    /**
     * 保存发起项目暂停信息
     *
     * @param projectStopDto
     * @return
     */
    @Override
    @Transactional
    public ResultMsg savePauseProject(ProjectStopDto projectStopDto) {
        try {
            ProjectStop projectStop = new ProjectStop();
            Sign sign = signRepo.findById(projectStopDto.getSignid());
            //1、判断项目当前的状态
            if (Constant.EnumState.STOP.getValue().equals(sign.getSignState())) {
                return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), "操作失败，项目目前已是暂停状态！");
            }
            boolean isHaveApproveing = false;
            //2、判断项目目前是否有正在申请的暂停信息
            List<ProjectStop> stopList = projectStopRepo.findProjectStop(sign.getSignid(), null, null);
            for (ProjectStop st : stopList) {
                if (Validate.isString(st.getProcessInstanceId()) && !Validate.isString(st.getLeaderId())) {
                    isHaveApproveing = true;
                } else if (Constant.EnumState.YES.getValue().equals(st.getIsactive()) && Constant.EnumState.PROCESS.getValue().equals(st.getIsOverTime())) {
                    isHaveApproveing = true;
                }
                if (isHaveApproveing) {
                    break;
                }
            }
            if (isHaveApproveing) {
                return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), "操作失败，该项目已有暂停记录在处理！");
            }

            BeanCopierUtils.copyProperties(projectStopDto, projectStop);
            if (!Validate.isObject(projectStop.getExpectpausedays())) {
                return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), "操作失败，没填写预计暂停天数！");
            }
            if (Validate.isString(projectStopDto.getStopid())) {
                projectStop = projectStopRepo.findById(projectStopDto.getStopid());
                BeanCopierUtils.copyPropertiesIgnoreNull(projectStopDto, projectStop);
                projectStopRepo.save(projectStop);
            }else{
                Date now = new Date();
                projectStop.setCreatedBy(SessionUtil.getDisplayName());
                projectStop.setCreatedDate(now);
                projectStop.setModifiedBy(SessionUtil.getDisplayName());
                projectStop.setModifiedDate(now);
                projectStop.setStopid(UUID.randomUUID().toString());
                //设置默认值
                projectStop.setIsactive(Constant.EnumState.NO.getValue());
                projectStop.setApproveStatus(Constant.EnumState.NO.getValue());//默认处于：未处理环节
            }


            //1、启动流程
            ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(FlowConstant.PROJECT_STOP_FLOW, projectStop.getStopid(),
                    ActivitiUtil.setAssigneeValue(FlowConstant.SignFlowParams.USER.getValue(), SessionUtil.getUserId()));

            //2、设置流程实例名称
            processEngine.getRuntimeService().setProcessInstanceName(processInstance.getId(), projectStopDto.getProcessName());
            //4、跳过第一环节
            Task task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).active().singleResult();
            taskService.addComment(task.getId(), processInstance.getId(), "");    //添加处理信息

            String userId = SessionUtil.getUserInfo().getOrg().getOrgDirector() == null ? SessionUtil.getUserId() : SessionUtil.getUserInfo().getOrg().getOrgDirector();
            List<AgentTask> agentTaskList = new ArrayList<>();
            String assigneeValue = userService.getTaskDealId(userId, agentTaskList, FlowConstant.FLOW_STOP_BZ_SP);
            taskService.complete(task.getId(), ActivitiUtil.setAssigneeValue(FlowConstant.SignFlowParams.USER_BZ1.getValue(), assigneeValue));
            //保存代办
            if(Validate.isList(agentTaskList)){
                agentTaskService.updateAgentInfo(agentTaskList, processInstance.getId(), processInstance.getName());
            }
            //设置流程实例ID
            projectStop.setProcessInstanceId(processInstance.getId());
            projectStop.setSign(sign);
            projectStopRepo.save(projectStop);

            return new ResultMsg(true, Constant.MsgCode.OK.getValue(), "操作成功！");
        } catch (Exception e) {
            return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), "保存失败，错误信息已记录，请联系管理员处理！");
        }

    }

    /**
     * 保存项目暂停信息
     *
     * @param projectStopDto
     * @return
     */
    @Override
    @Transactional
    public ResultMsg saveProjectStop(ProjectStopDto projectStopDto) {
        ProjectStop projectStop = new ProjectStop();
        //判断是否是更新(这步基本没用到)
        if (Validate.isString(projectStopDto.getStopid())) {
            projectStop = projectStopRepo.findById(projectStopDto.getStopid());
            BeanCopierUtils.copyPropertiesIgnoreNull(projectStopDto, projectStop);
            projectStopRepo.save(projectStop);
        } else {
            Sign sign = signRepo.findById(projectStopDto.getSignid());
            BeanCopierUtils.copyProperties(projectStopDto, projectStop);
            if (!Validate.isObject(projectStop.getExpectpausedays())) {
                return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), "操作失败，没填写预计暂停天数！");
            }
            Date now = new Date();
            projectStop.setCreatedBy(SessionUtil.getDisplayName());
            projectStop.setCreatedDate(now);
            projectStop.setModifiedBy(SessionUtil.getDisplayName());
            projectStop.setModifiedDate(now);
            projectStop.setStopid(UUID.randomUUID().toString());
            //设置默认值
            projectStop.setIsactive(Constant.EnumState.NO.getValue());
            projectStop.setApproveStatus(Constant.EnumState.NO.getValue());//默认处于：未处理环节
            projectStop.setSign(sign);
            projectStopRepo.save(projectStop);
        }
        return new ResultMsg(true, Constant.MsgCode.OK.getValue(), "操作成功！", projectStop);

    }


    @Override
    public PageModelDto<ProjectStopDto> findProjectStopByStopId(ODataObj oDataObj) {
        PageModelDto<ProjectStopDto> pageModelDto = new PageModelDto<>();
        Criteria criteria = projectStopRepo.getExecutableCriteria();
        criteria = oDataObj.buildFilterToCriteria(criteria);
        Boolean b = false;
        //部长审核
        if (SessionUtil.hashRole(Constant.EnumFlowNodeGroupName.DEPT_LEADER.getValue())) {
            b = true;
            criteria.add(Restrictions.eq(ProjectStop_.directorName.getName(), SessionUtil.getLoginName()));
            criteria.add(Restrictions.eq(ProjectStop_.approveStatus.getName(), Constant.EnumState.NO.getValue()));
        }
        //分管副主任办理
        else if (SessionUtil.hashRole(Constant.EnumFlowNodeGroupName.VICE_DIRECTOR.getValue())) {
            criteria.add(Restrictions.eq(ProjectStop_.leaderName.getName(), SessionUtil.getLoginName()));
            criteria.add(Restrictions.eq(ProjectStop_.approveStatus.getName(), Constant.EnumState.PROCESS.getValue()));
            b = true;
        }
        if (b) {
            Integer totalResult = ((Number) criteria.setProjection(Projections.rowCount()).uniqueResult()).intValue();

            pageModelDto.setCount(totalResult);

            criteria.setProjection(null);
            if (oDataObj.getSkip() > 0) {
                criteria.setFirstResult(oDataObj.getSkip());
            }
            if (oDataObj.getTop() > 0) {
                criteria.setMaxResults(oDataObj.getTop());
            }

            if (Validate.isString(oDataObj.getOrderby())) {
                if (oDataObj.isOrderbyDesc()) {
                    criteria.addOrder(Property.forName(oDataObj.getOrderby()).desc());
                } else {
                    criteria.addOrder(Property.forName(oDataObj.getOrderby()).asc());
                }
            }
            List<ProjectStop> projectStopList = criteria.list();
            List<ProjectStopDto> projectStopDtoList = new ArrayList<>();
            for (ProjectStop projectStop : projectStopList) {
                ProjectStopDto projectStopDto = new ProjectStopDto();
                BeanCopierUtils.copyProperties(projectStop, projectStopDto);
                projectStopDtoList.add(projectStopDto);
            }

            pageModelDto.setValue(projectStopDtoList);
        }
        return pageModelDto;
    }

    /**
     * 根据ID获取项目暂停信息
     *
     * @param stopId
     * @return
     */
    @Override
    public ProjectStopDto getProjectStopByStopId(String stopId) {
        ProjectStop projectStop = projectStopRepo.findById(stopId);
        ProjectStopDto projectStopDto = new ProjectStopDto();
        BeanCopierUtils.copyProperties(projectStop, projectStopDto);
        if (projectStop.getSign() != null) {
            projectStopDto.setSignDispaWork(signDispaWorkRepo.findById("signid", projectStop.getSign().getSignid()));
        }
        return projectStopDto;
    }

    /**
     * 查询正在执行的暂停项目(要转成dto，否则定时器那边会报错)
     *
     * @return
     */
    @Override
    public List<ProjectStop> selectPauseProject() {
        Criteria criteria = projectStopRepo.getExecutableCriteria();
        criteria.add(Restrictions.eq(ProjectStop_.isactive.getName(), Constant.EnumState.YES.getValue()));
        criteria.add(Restrictions.eq(ProjectStop_.isOverTime.getName(), Constant.EnumState.PROCESS.getValue()));
        /* List<ProjectStop> projectStopList = criteria.list();
        List<ProjectStopDto> projectStopDtoList = new ArrayList<>();
        if(Validate.isList(projectStopList)){
            projectStopList.forEach(ps ->{
                ProjectStopDto projectStopDto = new ProjectStopDto();
                BeanCopierUtils.copyProperties(ps,projectStopDto);
                projectStopDtoList.add(projectStopDto);
            });
        }*/
        return criteria.list();
    }

    @Override
    public void updateProjectStopStatus(List<ProjectStop> projectStopList) {
       /* List<ProjectStop> projectStopList = new ArrayList<>();
        projectStopDtoList.forEach( psd->{
            ProjectStop projectStop = new ProjectStop();
            BeanCopierUtils.copyProperties(psd,projectStop);
            projectStopList.add(projectStop);
        });*/
        projectStopRepo.bathUpdate(projectStopList);
    }

    /**
     * 项目暂停申请流程
     *
     * @param processInstance
     * @param task
     * @param flowDto
     * @return
     */
    @Override
    @Transactional
    public ResultMsg dealFlow(ProcessInstance processInstance, Task task, FlowDto flowDto) {
        String businessId = processInstance.getBusinessKey(),
                assigneeValue = "",                             //流程处理人
                nextNodeKey = "",                               //下一环节名称
                curUserId = SessionUtil.getUserId();            //当前用户ID
        Map<String, Object> variables = null;                   //流程参数
        User dealUser = null;                                   //用户
        ProjectStop projectStop = null;                         //项目暂停对象
        boolean isNextUser = false,                             //是否是下一环节处理人（主要是处理领导审批，部长审批）
                isAgentTask = agentTaskService.isAgentTask(task.getId(),curUserId); //是否代办
        List<AgentTask> agentTaskList = new ArrayList<>();
        //环节处理人设定
        switch (task.getTaskDefinitionKey()) {
            //项目负责人填报
            case FlowConstant.FLOW_STOP_FZR:
                String directorId = SessionUtil.getUserInfo().getOrg().getOrgDirector();
                assigneeValue = userService.getTaskDealId(directorId, agentTaskList, FlowConstant.FLOW_STOP_BZ_SP);
                variables = ActivitiUtil.setAssigneeValue(FlowConstant.SignFlowParams.USER_FGLD1.getValue(), assigneeValue);
                break;
            //部长审批
            case FlowConstant.FLOW_STOP_BZ_SP:
                projectStop = projectStopRepo.findById(ProjectStop_.stopid.getName(), businessId);
                if(isAgentTask){
                    projectStop.setDirectorId(agentTaskService.getUserId(task.getId(),curUserId));
                }else{
                    projectStop.setDirectorId(curUserId);
                }
                projectStop.setDirectorName(ActivitiUtil.getSignName(SessionUtil.getDisplayName(),isAgentTask));
                projectStop.setDirectorIdeaContent(flowDto.getDealOption());
                projectStop.setDirectorDate(new Date());
                projectStop.setApproveStatus(Constant.EnumState.PROCESS.getValue());
                projectStopRepo.save(projectStop);

                assigneeValue = userService.getTaskDealId(SessionUtil.getUserInfo().getOrg().getOrgSLeader(), agentTaskList, FlowConstant.FLOW_STOP_FGLD_SP);
                variables = ActivitiUtil.setAssigneeValue(FlowConstant.SignFlowParams.USER_FGLD1.getValue(), assigneeValue);
                //下一环节还是自己处理
                if (assigneeValue.equals(SessionUtil.getUserId())) {
                    isNextUser = true;
                    nextNodeKey = FlowConstant.FLOW_STOP_FGLD_SP;
                }
                break;
            //分管领导审批
            case FlowConstant.FLOW_STOP_FGLD_SP:
                if (flowDto.getBusinessMap().get("AGREE") == null || !Validate.isString(flowDto.getBusinessMap().get("AGREE").toString())) {
                    return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), "请选择同意或者不同意！");
                }
                String isactive = flowDto.getBusinessMap().get("AGREE").toString();
                boolean isPass = Constant.EnumState.YES.getValue().equals(isactive);
                projectStop = projectStopRepo.findById(businessId);

                //审批通过，暂停还未开始执行，由定时器去启动
                if(isPass){
                    //暂停时间，如果没有，就按审批通过算起。有就按暂停日期算起
                    if (null == projectStop.getPausetime() || !(projectStop.getPausetime()).after(new Date())) {
                        projectStop.setPausetime(new Date());
                        projectStop.setIsOverTime(Constant.EnumState.PROCESS.getValue());
                        Sign sign = projectStop.getSign();
                        //流程不暂停，还是可以继续往下走
                        /*ResultMsg stopResult = flowService.stopFlow(sign.getSignid());
                        if (!stopResult.isFlag()) {
                            return stopResult;
                        }*/
                        //更改项目状态
                        sign.setSignState(Constant.EnumState.STOP.getValue());
                        sign.setIsLightUp(Constant.signEnumState.PAUSE.getValue());
                        sign.setIsProjectState(Constant.EnumState.YES.getValue());
                        signRepo.save(sign);
                    }else{
                        projectStop.setIsOverTime(Constant.EnumState.NO.getValue());
                    }
                }
                if(isAgentTask){
                    projectStop.setLeaderId(agentTaskService.getUserId(task.getId(),curUserId));
                }else{
                    projectStop.setLeaderId(curUserId);
                }
                projectStop.setLeaderName(ActivitiUtil.getSignName(SessionUtil.getDisplayName(),isAgentTask));
                projectStop.setLeaderIdeaContent(flowDto.getDealOption());
                projectStop.setLeaderDate(new Date());
                projectStop.setApproveStatus(Constant.EnumState.YES.getValue());
                projectStop.setIsactive(isactive);

                projectStopRepo.save(projectStop);
                break;
            default:
                break;
        }
        taskService.addComment(task.getId(), processInstance.getId(), (flowDto == null) ? "" : flowDto.getDealOption());    //添加处理信息
        if (flowDto.isEnd()) {
            taskService.complete(task.getId());
        } else {
            taskService.complete(task.getId(), variables);
            //如果下一环节还是自己
            if (isNextUser) {
                List<Task> nextTaskList = taskService.createTaskQuery().processInstanceId(processInstance.getId()).taskAssignee(assigneeValue).list();
                for (Task t : nextTaskList) {
                    if (nextNodeKey.equals(t.getTaskDefinitionKey())) {
                        ResultMsg returnMsg = dealFlow(processInstance, t, flowDto);
                        if (returnMsg.isFlag() == false) {
                            return returnMsg;
                        }
                        break;
                    }
                }
            }
        }
        //如果是代办，还要更新环节名称和任务ID
        if (Validate.isList(agentTaskList)) {
            agentTaskService.updateAgentInfo(agentTaskList, processInstance.getId(), processInstance.getName());
        }
        //放入腾讯通消息缓冲池
        RTXSendMsgPool.getInstance().sendReceiverIdPool(task.getId(), assigneeValue);
        return new ResultMsg(true, Constant.MsgCode.OK.getValue(), "操作成功！");
    }

    /**
     * 通过收文ID获取审批通过的项目暂停信息
     *
     * @param signId
     * @return
     */
    @Override
    public List<ProjectStopDto> getStopList(String signId) {
        List<ProjectStop> projectStopList = projectStopRepo.getStopList(signId);
        List<ProjectStopDto> projectStopDtoList = new ArrayList<>();
        if(Validate.isList(projectStopList)){
            for(ProjectStop projectStop : projectStopList){
                ProjectStopDto projectStopDto = new ProjectStopDto();
                BeanCopierUtils.copyProperties(projectStop , projectStopDto);
                projectStopDtoList.add(projectStopDto);
            }
        }
        return projectStopDtoList;
    }

    /**
     * 根据项目暂停ID，获取项目信息
     *
     * @param stopid
     * @return
     */
    @Override
    @Transactional
    public Sign findSignByStopId(String stopid) {
        HqlBuilder hqlBuilder = HqlBuilder.create();
        hqlBuilder.append(" from " + Sign.class.getSimpleName() + " s where s." + Sign_.signid.getName() + " = ");
        hqlBuilder.append(" (select ps." + ProjectStop_.sign.getName() + "." + Sign_.signid.getName() + " from " + ProjectStop.class.getSimpleName() + " ps where ps." + ProjectStop_.stopid.getName() + " =:stopid ) ");
        hqlBuilder.setParam("stopid", stopid);
        List<Sign> signList = signRepo.findByHql(hqlBuilder);
        if (Validate.isList(signList)) {
            return signList.get(0);
        }
        return null;
    }

}
