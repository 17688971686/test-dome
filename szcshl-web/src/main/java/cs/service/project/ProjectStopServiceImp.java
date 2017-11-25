package cs.service.project;

import cs.common.Constant;
import cs.common.FlowConstant;
import cs.common.HqlBuilder;
import cs.common.ResultMsg;
import cs.common.utils.*;
import cs.domain.flow.RuProcessTask;
import cs.domain.flow.RuProcessTask_;
import cs.domain.project.*;
import cs.domain.sys.Role;
import cs.domain.sys.User;
import cs.domain.topic.TopicInfo_;
import cs.model.PageModelDto;
import cs.model.flow.FlowDto;
import cs.model.project.ProjectStopDto;
import cs.model.project.SignDto;
import cs.quartz.unit.QuartzUnit;
import cs.repository.odata.ODataObj;
import cs.repository.repositoryImpl.flow.RuProcessTaskRepo;
import cs.repository.repositoryImpl.project.ProjectStopRepo;
import cs.repository.repositoryImpl.project.SignDispaWorkRepo;
import cs.repository.repositoryImpl.project.SignRepo;
import cs.repository.repositoryImpl.sys.UserRepo;
import cs.service.flow.FlowService;
import cs.service.rtx.RTXSendMsgPool;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.hibernate.Criteria;
import org.hibernate.criterion.*;
import org.hibernate.hql.internal.ast.HqlParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static cs.common.Constant.SUPER_USER;

@Service
public class ProjectStopServiceImp implements ProjectStopService {
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
    @Autowired
    private SignService signService;

    //flow service
    @Autowired
    private TaskService taskService;
    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private FlowService flowService;


    @Override
    @Transactional
    public List<ProjectStop> findProjectStopBySign(String signId) {

        HqlBuilder hqlBuilder = HqlBuilder.create();

        hqlBuilder.append("select ps from " + ProjectStop.class.getSimpleName() + " ps where ps." + ProjectStop_.sign.getName() + "." + Sign_.signid.getName() + "=:signId");
        hqlBuilder.setParam("signId", signId);
        List<ProjectStop> psList = projectStopRepo.findByHql(hqlBuilder);
        return psList;
    }

    /**
     * 根据项目ID，初始化项目暂停信息
     * @param signId
     * @return
     */
    @Override
    public SignDispaWork findSignBySignId(String signId) {
        return signDispaWorkRepo.findById("signid",signId);
    }

    /*@Override
    public int countUsedWorkday(String signId) {
        HqlBuilder sqlBuilder = HqlBuilder.create();
        sqlBuilder.append("select signdate from cs_sign where signid =:signId");
        sqlBuilder.setParam("signId", signId);
        List<Object[]> signList = signRepo.getObjectArray(sqlBuilder);
        int count = 0;
        if (Validate.isList(signList)) {
            Date signDate = (Date) signList.get(0)[0];
            QuartzUnit unit = new QuartzUnit();
            count = unit.countWorkday(signDate);
        }
        return count;
    }*/

    /**
     * 保存项目暂停信息
     * @param projectStopDto
     * @return
     */
    @Override
    @Transactional
    public ResultMsg savePauseProject(ProjectStopDto projectStopDto) {
        ProjectStop projectStop = new ProjectStop();
        Sign sign = new Sign();
        sign.setSignid(projectStopDto.getSignid());
        projectStop.setSign(sign);
        Date now = new Date();


        if(Validate.isString(projectStopDto.getStopid())){//判断是否是更新
            projectStop = projectStopRepo.findById(projectStopDto.getStopid());
            BeanCopierUtils.copyProperties(projectStopDto, projectStop);
        }else{
            BeanCopierUtils.copyProperties(projectStopDto, projectStop);
            projectStop.setCreatedBy(SessionUtil.getDisplayName());
            projectStop.setCreatedDate(now);
            projectStop.setModifiedBy(SessionUtil.getDisplayName());
            projectStop.setModifiedDate(now);
            projectStop.setStopid(UUID.randomUUID().toString());
            //设置默认值
            projectStop.setIsactive(Constant.EnumState.NO.getValue());
            projectStop.setApproveStatus(Constant.EnumState.NO.getValue());//默认处于：未处理环节

            //1、启动流程
            ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(FlowConstant.PROJECT_STOP_FLOW, projectStop.getStopid(),
                    ActivitiUtil.setAssigneeValue(FlowConstant.SignFlowParams.USER.getValue(), SessionUtil.getUserId()));

            //2、设置流程实例名称
            processEngine.getRuntimeService().setProcessInstanceName(processInstance.getId(), projectStopDto.getProcessName());
            //4、跳过第一环节（主任审核）
            Task task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).active().singleResult();
            taskService.addComment(task.getId(), processInstance.getId(), "");    //添加处理信息
            String userId = SessionUtil.getUserInfo().getOrg().getOrgDirector() == null?SessionUtil.getUserId():SessionUtil.getUserInfo().getOrg().getOrgDirector();
            taskService.complete(task.getId(), ActivitiUtil.setAssigneeValue(FlowConstant.SignFlowParams.USER_BZ1.getValue(), userId));

            //设置流程实例ID
            projectStop.setProcessInstanceId(processInstance.getId());
            projectStopRepo.save(projectStop);
        }
        return new ResultMsg(true, Constant.MsgCode.OK.getValue(),"操作成功！");
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
     * @param stopId
     * @return
     */
    @Override
    public ProjectStopDto getProjectStopByStopId(String stopId) {
        ProjectStop projectStop = projectStopRepo.findById(stopId);
        ProjectStopDto projectStopDto = new ProjectStopDto();
        BeanCopierUtils.copyProperties(projectStop, projectStopDto);
        if(projectStop.getSign() != null ){
            projectStopDto.setSignDispaWork(signDispaWorkRepo.findById("signid",projectStop.getSign().getSignid()));
        }
        return projectStopDto;
    }

   /* @Override
    @Transactional
    public void updateProjectStop(ProjectStopDto projectStopDto) {

        HqlBuilder sqlBuilder = HqlBuilder.create();
        sqlBuilder.append("update " + ProjectStop.class.getSimpleName() + " set ");
        //部长审核
        if (SessionUtil.hashRole(Constant.EnumFlowNodeGroupName.DEPT_LEADER.getValue())
                || SessionUtil.hashRole(Constant.EnumFlowNodeGroupName.COMM_DEPT_DIRECTOR.getValue())) {
            sqlBuilder.append(ProjectStop_.directorIdeaContent.getName() + "=:directorIdeaContent , " + ProjectStop_.approveStatus.getName() + "=:approveStatus");
            sqlBuilder.setParam("directorIdeaContent", projectStopDto.getDirectorIdeaContent());
            sqlBuilder.setParam("approveStatus", Constant.EnumState.PROCESS.getValue());
        }
        //分管副主任办理
        else if (SessionUtil.hashRole(Constant.EnumFlowNodeGroupName.VICE_DIRECTOR.getValue())) {
            sqlBuilder.append(ProjectStop_.leaderIdeaContent.getName() + "=:leaderIdeaContent ," + ProjectStop_.approveStatus.getName() + "=:approveStatus ," + ProjectStop_.isactive.getName() + "=:isactive");
            sqlBuilder.setParam("leaderIdeaContent", projectStopDto.getLeaderIdeaContent());
            sqlBuilder.setParam("approveStatus", Constant.EnumState.YES.getValue());
            sqlBuilder.setParam("isactive", Constant.EnumState.YES.getValue());

            //修改项目状态
            HqlBuilder hql = HqlBuilder.create();
            hql.append("update " + Sign.class.getSimpleName() + " set " + Sign_.signState.getName() + " =:signState ," + Sign_.isProjectState.getName() + " =:isProjectState ," + Sign_.isLightUp.getName() + "=:isLightUp");
            hql.append(" where " + Sign_.signid.getName() + " =:signId");
            hql.setParam("isLightUp", Constant.signEnumState.PAUSE.getValue());
            hql.setParam("signState", Constant.EnumState.STOP.getValue());
            hql.setParam("isProjectState", Constant.EnumState.YES.getValue());
            hql.setParam("signId", projectStopDto.getSign().getSignid());
            signRepo.executeHql(hql);

            //暂停流程
            ProcessInstance processInstance = flowService.findProcessInstanceByBusinessKey(projectStopDto.getSign().getSignid());
            runtimeService.suspendProcessInstanceById(processInstance.getId());
        }
        sqlBuilder.append(" where " + ProjectStop_.stopid.getName() + "=:stopId");
        sqlBuilder.setParam("stopId", projectStopDto.getStopid());
        projectStopRepo.executeHql(sqlBuilder);

    }*/


    /**
     * 查询正在执行的暂停项目
     * @return
     */
    @Override
    public List<ProjectStop> findPauseProjectSuccess() {
        Criteria criteria = projectStopRepo.getExecutableCriteria();
        criteria.add(Restrictions.eq(ProjectStop_.isactive.getName(), Constant.EnumState.YES.getValue()));
        criteria.add(Restrictions.eq(ProjectStop_.isOverTime.getName(), Constant.EnumState.PROCESS.getValue()));
        return criteria.list();
    }

    @Override
    public void updateProjectStopStatus(List<ProjectStop> projectStopList) {
        projectStopRepo.bathUpdate(projectStopList);
    }

    /**
     * 项目暂停申请流程
     * @param processInstance
     * @param task
     * @param flowDto
     * @return
     */
    @Override
    @Transactional
    public ResultMsg dealFlow(ProcessInstance processInstance, Task task, FlowDto flowDto) {
        String businessId = processInstance.getBusinessKey(),
               assigneeValue = "";                             //流程处理人
        Map<String,Object> variables = null;                   //流程参数
        User dealUser = null;                                  //用户
        ProjectStop projectStop = null;                        //项目暂停对象
        boolean isNextUser = false;                 //是否是下一环节处理人（主要是处理领导审批，部长审批）
        String nextNodeKey = "";                    //下一环节名称
        //环节处理人设定
        switch (task.getTaskDefinitionKey()) {
            //项目负责人填报
            case FlowConstant.FLOW_STOP_FZR:
                dealUser = userRepo.getCacheUserById(SessionUtil.getUserInfo().getOrg().getOrgDirector());
                variables = ActivitiUtil.setAssigneeValue(FlowConstant.SignFlowParams.USER_FGLD1.getValue(),
                        Validate.isString(dealUser.getTakeUserId()) ? dealUser.getTakeUserId() : dealUser.getId());
                break;
            //部长审批
            case FlowConstant.FLOW_STOP_BZ_SP:
                projectStop = projectStopRepo.findById(ProjectStop_.stopid.getName(),businessId);
                projectStop.setDirectorId(SessionUtil.getUserId());
                projectStop.setDirectorName(SessionUtil.getDisplayName());
                projectStop.setDirectorIdeaContent(flowDto.getDealOption());
                projectStop.setDirectorDate(new Date());
                projectStop.setApproveStatus(Constant.EnumState.PROCESS.getValue());
                projectStopRepo.save(projectStop);
                dealUser = userRepo.getCacheUserById(SessionUtil.getUserInfo().getOrg().getOrgSLeader());
                assigneeValue= Validate.isString(dealUser.getTakeUserId()) ? dealUser.getTakeUserId() : dealUser.getId();
                variables = ActivitiUtil.setAssigneeValue(FlowConstant.SignFlowParams.USER_FGLD1.getValue(), assigneeValue);
                //下一环节还是自己处理
                if(assigneeValue.equals(SessionUtil.getUserId())){
                    isNextUser = true;
                    nextNodeKey = FlowConstant.FLOW_STOP_FGLD_SP;
                }
                break;
            //分管领导审批
            case FlowConstant.FLOW_STOP_FGLD_SP:
  /*              if (flowDto.getBusinessMap().get("AGREE") == null || !Validate.isString(flowDto.getBusinessMap().get("AGREE").toString())) {
                    return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), "请选择同意或者不同意！");
                }*/
                String isactive = flowDto.getBusinessMap().get("AGREE").toString();
                isactive = Constant.EnumState.YES.getValue().equals(isactive)?Constant.EnumState.YES.getValue():Constant.EnumState.NO.getValue();
                projectStop = projectStopRepo.findById(businessId);
                //审批通过，暂停还未开始执行，由定时器去启动
                //暂停时间，如果没有，就按审批通过算起。有就按暂停日期算起
                if(null == projectStop.getPausetime() || DateUtils.daysBetween(new Date(),projectStop.getPausetime()) == 0){
                    projectStop.setPausetime(new Date());
                    projectStop.setIsOverTime(Constant.EnumState.PROCESS.getValue());
                    Sign sign = projectStop.getSign();
                    //如果领导同意，则将流程暂停
                    if(Constant.EnumState.YES.getValue().equals(isactive) ||isactive==null){
                        ResultMsg stopResult = flowService.stopFlow(projectStop.getSign().getSignid());
                        if(!stopResult.isFlag()){
                            return stopResult;
                        }
                    }
                    //更改项目状态
                    sign.setSignState(Constant.EnumState.STOP.getValue());
                    sign.setIsLightUp(Constant.signEnumState.PAUSE.getValue());
                    signRepo.save(sign);
                }else{
                    projectStop.setIsOverTime(Constant.EnumState.NO.getValue());
                }
                projectStop.setLeaderId(SessionUtil.getUserId());
                projectStop.setLeaderName(SessionUtil.getDisplayName());
                projectStop.setLeaderIdeaContent(flowDto.getDealOption());
                projectStop.setLeaderDate(new Date());
                projectStop.setApproveStatus(Constant.EnumState.YES.getValue());
                projectStop.setIsactive(isactive);

                projectStopRepo.save(projectStop);
                break;
            default:
                break;
        }
        taskService.addComment(task.getId(), processInstance.getId(),(flowDto == null)?"":flowDto.getDealOption());    //添加处理信息
        if (flowDto.isEnd()) {
            taskService.complete(task.getId());
        } else {
            taskService.complete(task.getId(), variables);
            //如果下一环节还是自己
            if(isNextUser){
                List<Task> nextTaskList = taskService.createTaskQuery().processInstanceId(processInstance.getId()).taskAssignee(assigneeValue).list();
                for(Task t:nextTaskList){
                    if(nextNodeKey.equals(t.getTaskDefinitionKey())){
                        ResultMsg returnMsg = dealFlow(processInstance,t,flowDto);
                        if(returnMsg.isFlag() == false){
                            return returnMsg;
                        }
                        break;
                    }
                }
            }
        }
        //放入腾讯通消息缓冲池
        RTXSendMsgPool.getInstance().sendReceiverIdPool(task.getId(),assigneeValue);
        return new ResultMsg(true, Constant.MsgCode.OK.getValue(), "操作成功！");
    }

    /**
     * 通过收文ID获取审批通过的项目暂停信息
     * @param signId
     * @return
     */
    @Override
    public List<ProjectStopDto> getStopList(String signId) {
        return projectStopRepo.getStopList(signId);
    }

}
