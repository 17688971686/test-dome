package cs.service.archives;

import java.util.*;

import javax.transaction.Transactional;

import cs.common.constants.FlowConstant;
import cs.common.utils.*;
import cs.domain.project.AgentTask;
import cs.domain.project.ProjectStop;
import cs.domain.project.ProjectStop_;
import cs.domain.sys.User;
import cs.model.flow.FlowDto;
import cs.repository.repositoryImpl.sys.UserRepo;
import cs.service.project.AgentTaskService;
import cs.service.rtx.RTXSendMsgPool;
import cs.service.sys.UserService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cs.common.constants.Constant;
import cs.common.constants.Constant.EnumState;
import cs.common.constants.Constant.MsgCode;
import cs.common.ResultMsg;
import cs.domain.archives.ArchivesLibrary;
import cs.domain.archives.ArchivesLibrary_;
import cs.model.PageModelDto;
import cs.model.archives.ArchivesLibraryDto;
import cs.repository.odata.ODataObj;
import cs.repository.repositoryImpl.archives.ArchivesLibraryRepo;

import static cs.common.constants.SysConstants.SUPER_ACCOUNT;

/**
 * Description: 档案借阅管理 业务操作实现类
 * author: sjy
 * Date: 2017-9-12 17:34:30
 */
@Service
public class ArchivesLibraryServiceImpl implements ArchivesLibraryService {

    @Autowired
    private ArchivesLibraryRepo archivesLibraryRepo;
    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private ProcessEngine processEngine;
    @Autowired
    private TaskService taskService;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private AgentTaskService agentTaskService;
    @Autowired
    private UserService userService;
    @Autowired
    private RepositoryService repositoryService;

    @Override
    public PageModelDto<ArchivesLibraryDto> get(ODataObj odataObj) {
        PageModelDto<ArchivesLibraryDto> pageModelDto = new PageModelDto<ArchivesLibraryDto>();
        List<ArchivesLibrary> resultList = archivesLibraryRepo.findByOdata(odataObj);
        List<ArchivesLibraryDto> resultDtoList = new ArrayList<ArchivesLibraryDto>(resultList.size());

        if (resultList != null && resultList.size() > 0) {
            resultList.forEach(x -> {
                ArchivesLibraryDto modelDto = new ArchivesLibraryDto();
                BeanCopierUtils.copyProperties(x, modelDto);
                resultDtoList.add(modelDto);
            });
        }
        pageModelDto.setCount(odataObj.getCount());
        pageModelDto.setValue(resultDtoList);
        return pageModelDto;
    }

    /**
     * 创建（中心）借阅保存
     */
    @Override
    @Transactional
    public ResultMsg save(ArchivesLibraryDto record) {
        ArchivesLibrary domain = new ArchivesLibrary();
        Date now = new Date();

        if (Validate.isString(record.getId())) {
            domain = archivesLibraryRepo.findById(record.getId());
            BeanCopierUtils.copyPropertiesIgnoreNull(record, domain);
        } else {
            BeanCopierUtils.copyProperties(record, domain);
            domain.setId(UUID.randomUUID().toString());
            domain.setCreatedBy(SessionUtil.getUserId());
            domain.setCreatedDate(now);
        }

        domain.setModifiedBy(SessionUtil.getDisplayName());
        domain.setModifiedDate(now);
        archivesLibraryRepo.save(domain);

        BeanCopierUtils.copyProperties(domain, record);
        return new ResultMsg(true, MsgCode.OK.getValue(), "操作成功！", record);
    }

    /**
     * 创建（市）借阅记录
     *//*
    @Override
    @Transactional
    public ResultMsg saveCity(ArchivesLibraryDto record) {
        ArchivesLibrary domain = new ArchivesLibrary();
        BeanCopierUtils.copyProperties(record, domain);
        Date now = new Date();
        domain.setId(UUID.randomUUID().toString());
        domain.setCreatedBy(SessionUtil.getDisplayName());
        domain.setModifiedBy(SessionUtil.getDisplayName());
        domain.setCreatedDate(now);
        domain.setModifiedDate(now);
        //部长名称
        if (SessionUtil.getUserInfo().getOrg() != null && SessionUtil.getUserInfo().getOrg().getOrgDirectorName() != null) {
            domain.setDeptMinister(SessionUtil.getUserInfo().getOrg().getOrgDirectorName());
        } else {
            domain.setDeptMinister(SessionUtil.getDisplayName());
        }
        //分管副主任
        if (SessionUtil.getUserInfo().getOrg() != null && SessionUtil.getUserInfo().getOrg().getOrgSLeaderName() != null) {
            domain.setDeptSLeader(SessionUtil.getUserInfo().getOrg().getOrgSLeaderName());
        } else {
            domain.setDeptSLeader(SessionUtil.getDisplayName());
        }
        //主任
        if (SessionUtil.getUserInfo().getOrg() != null && SessionUtil.getUserInfo().getOrg().getOrgMLeaderName() != null) {
            domain.setDeptDirector(SessionUtil.getUserInfo().getOrg().getOrgMLeaderName());
        } else {
            domain.setDeptDirector(SessionUtil.getDisplayName());
        }
        //隐藏表单状态
        domain.setHideStatus(Constant.EnumState.NO.getValue());
        //默认未处理状态0
        domain.setArchivesStatus(Constant.EnumState.NO.getValue());
        archivesLibraryRepo.save(domain);
        return new ResultMsg(true, MsgCode.OK.getValue(), "操作成功！", domain);
    }
*/
    @Override
    @Transactional
    public void update(ArchivesLibraryDto record) {
        ArchivesLibrary domain = archivesLibraryRepo.findById(record.getId());
        BeanCopierUtils.copyPropertiesIgnoreNull(record, domain);
        domain.setModifiedBy(SessionUtil.getDisplayName());
        domain.setModifiedDate(new Date());

        archivesLibraryRepo.save(domain);
    }

    /**
     * 通过主键查询
     *
     * @param id
     * @return
     */
    @Override
    public ArchivesLibraryDto findById(String id) {
        ArchivesLibraryDto modelDto = new ArchivesLibraryDto();
        if (Validate.isString(id)) {
            ArchivesLibrary domain = archivesLibraryRepo.findById(id);
            BeanCopierUtils.copyProperties(domain, modelDto);
        }
        return modelDto;
    }

    @Override
    @Transactional
    public void delete(String id) {

    }


    /**
     * 发起流程
     *
     * @param id
     * @return
     */
    @Override
    public ResultMsg startFlow(String id) {
        ArchivesLibrary archivesLibrary = archivesLibraryRepo.findById(id);
        String assigneeValue;
        Map<String, Object> variables = new HashMap<>();       //流程参数
        User dealUser = null;                                  //用户
        List<AgentTask> agentTaskList = new ArrayList<>();
        if (archivesLibrary == null) {
            return new ResultMsg(false, MsgCode.ERROR.getValue(), "发起流程失败，该项目已不存在！");
        }
        if (Validate.isString(archivesLibrary.getProcessInstanceId())) {
            return new ResultMsg(false, MsgCode.ERROR.getValue(), "该项目已发起流程！");
        }
        if (SessionUtil.getUserInfo().getOrg() == null || !Validate.isString(SessionUtil.getUserInfo().getOrg().getOrgDirector())) {
            return new ResultMsg(false, MsgCode.ERROR.getValue(), "您所在部门还没设置部长，请联系管理员进行设置！");
        }
        //1、启动流程
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(FlowConstant.FLOW_ARCHIVES, id,
                ActivitiUtil.setAssigneeValue(FlowConstant.FlowParams.USER.getValue(), SessionUtil.getUserId()));

        //2、设置流程实例名称
        processEngine.getRuntimeService().setProcessInstanceName(processInstance.getId(), archivesLibrary.getReadProjectName() == null ? "《" + archivesLibrary.getReadArchivesCode() + "》档案借阅" : "《" + archivesLibrary.getReadProjectName() + "》档案借阅");

        //3、更改项目状态
        archivesLibrary.setProcessInstanceId(processInstance.getId());
        archivesLibrary.setArchivesStatus(Constant.EnumState.NO.getValue());
        archivesLibraryRepo.save(archivesLibrary);

        //4、跳过第一环节（填报）
        Task task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).active().singleResult();
        taskService.addComment(task.getId(), processInstance.getId(), "");    //添加处理信息
        //判断是否是部长发起
        if (SessionUtil.hashRole(Constant.EnumFlowNodeGroupName.DEPT_LEADER.getValue())) {
            assigneeValue = userService.getTaskDealId(SessionUtil.getUserInfo().getOrg().getOrgSLeader(), agentTaskList, FlowConstant.FLOW_ARC_FGLD_SP);
            variables = ActivitiUtil.setAssigneeValue(FlowConstant.MonthlyNewsletterFlowParams.USER_FGLD.getValue(), assigneeValue);
            variables.put(FlowConstant.FlowParams.TB_FZ.getValue(), true);
            taskService.complete(task.getId(), variables);
        } else {
            String userId = SessionUtil.getUserInfo().getOrg().getOrgDirector() == null ? SessionUtil.getUserId() : SessionUtil.getUserInfo().getOrg().getOrgDirector();
            assigneeValue = userService.getTaskDealId(userId, agentTaskList, FlowConstant.FLOW_ARC_BZ_SP);
            variables = ActivitiUtil.setAssigneeValue(FlowConstant.FlowParams.USER_BZ.getValue(), assigneeValue);
            variables.put(FlowConstant.FlowParams.TB_FZ.getValue(), false);
            taskService.complete(task.getId(), variables);
        }
        //如果是代办，还要更新环节名称和任务ID
        if (Validate.isList(agentTaskList)) {
            agentTaskService.updateAgentInfo(agentTaskList, processInstance.getId(), processInstance.getName());
        }
        //放入腾讯通消息缓冲池
        RTXSendMsgPool.getInstance().sendReceiverIdPool(task.getId(), assigneeValue);

        ProcessDefinitionEntity processDefinitionEntity = (ProcessDefinitionEntity) repositoryService.getProcessDefinition(processInstance.getProcessDefinitionId());

        return new ResultMsg(true, MsgCode.OK.getValue(), task.getId(),"操作成功！",  processDefinitionEntity.getName());
    }

    /**
     * 流程处理
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
        Map<String, Object> variables = new HashMap();          //流程参数
        User dealUser = null;                                  //用户
        List<User> dealUserList = null;                        //用户列表
        ArchivesLibrary archivesLibrary = null;                //档案借阅管理
        boolean isNextUser = false,                            //是否是下一环节处理人（主要是处理领导审批，部长审批）
                isAgentTask = agentTaskService.isAgentTask(task.getId(),curUserId);
        List<AgentTask> agentTaskList = new ArrayList<>();

        //环节处理人设定
        switch (task.getTaskDefinitionKey()) {
            //项目负责人填报
            case FlowConstant.FLOW_ARC_SQ:
                //判断是否是部长
                if (SessionUtil.hashRole(Constant.EnumFlowNodeGroupName.DEPT_LEADER.getValue())) {
                    assigneeValue = userService.getTaskDealId(SessionUtil.getUserInfo().getOrg().getOrgSLeader(), agentTaskList, FlowConstant.FLOW_ARC_FGLD_SP);
                    variables = ActivitiUtil.setAssigneeValue(FlowConstant.MonthlyNewsletterFlowParams.USER_FGLD.getValue(), assigneeValue);
                    variables.put(FlowConstant.FlowParams.TB_FZ.getValue(), true);
                } else {
                    String userId = SessionUtil.getUserInfo().getOrg().getOrgDirector() == null ? SessionUtil.getUserId() : SessionUtil.getUserInfo().getOrg().getOrgDirector();
                    assigneeValue = userService.getTaskDealId(userId, agentTaskList, FlowConstant.FLOW_ARC_BZ_SP);
                    variables = ActivitiUtil.setAssigneeValue(FlowConstant.FlowParams.USER_BZ.getValue(), assigneeValue);
                    variables.put(FlowConstant.FlowParams.TB_FZ.getValue(), false);
                }
                break;
            //部长审批
            case FlowConstant.FLOW_ARC_BZ_SP:
                if (flowDto.getBusinessMap().get("AGREE") == null || !Validate.isString(flowDto.getBusinessMap().get("AGREE").toString())) {
                    return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), "请选择同意或者不同意！");
                }
                archivesLibrary = archivesLibraryRepo.findById(ArchivesLibrary_.id.getName(), businessId);
                //同意
                if (EnumState.YES.getValue().equals(flowDto.getBusinessMap().get("AGREE").toString())) {
                    archivesLibrary.setIsAgree(EnumState.YES.getValue());
                    if(isAgentTask){
                        archivesLibrary.setDeptMinisterId(agentTaskService.getUserId(task.getId(),curUserId));
                    }else{
                        archivesLibrary.setDeptMinisterId(curUserId);
                    }
                    archivesLibrary.setDeptMinister(ActivitiUtil.getSignName(SessionUtil.getDisplayName(),isAgentTask));
                    archivesLibrary.setDeptMinisterIdeaContent(flowDto.getDealOption());
                    archivesLibrary.setDeptMinisterDate(new Date());
                    archivesLibrary.setArchivesStatus(EnumState.PROCESS.getValue());   //1表示到分管领导审批

                    assigneeValue = userService.getTaskDealId(SessionUtil.getUserInfo().getOrg().getOrgSLeader(), agentTaskList, FlowConstant.FLOW_ARC_FGLD_SP);
                    variables = ActivitiUtil.setAssigneeValue(FlowConstant.FlowParams.USER_FGLD.getValue(), assigneeValue);
                    variables.put(FlowConstant.FlowParams.BZ_FZ.getValue(), true);
                    //下一环节还是自己处理
                    if (assigneeValue.equals(SessionUtil.getUserId())) {
                        isNextUser = true;
                        nextNodeKey = FlowConstant.FLOW_ARC_FGLD_SP;
                    }
                    //不同意，则直接结束
                } else {
                    variables.put(FlowConstant.FlowParams.BZ_FZ.getValue(), false);
                    archivesLibrary.setIsAgree(EnumState.NO.getValue());
                }
                archivesLibraryRepo.save(archivesLibrary);
                break;
            //分管领导审批
            case FlowConstant.FLOW_ARC_FGLD_SP:
                if (flowDto.getBusinessMap().get("AGREE") == null || !Validate.isString(flowDto.getBusinessMap().get("AGREE").toString())) {
                    return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), "请选择同意或者不同意！");
                }
                archivesLibrary = archivesLibraryRepo.findById(ArchivesLibrary_.id.getName(), businessId);
                //同意
                if (EnumState.YES.getValue().equals(flowDto.getBusinessMap().get("AGREE").toString()) || flowDto.getBusinessMap().get("AGREE") == null) {
                    archivesLibrary.setIsAgree(EnumState.YES.getValue());
                    if(isAgentTask){
                        archivesLibrary.setDeptSLeaderId(agentTaskService.getUserId(task.getId(),curUserId));
                    }else{
                        archivesLibrary.setDeptSLeaderId(curUserId);
                    }
                    archivesLibrary.setDeptSLeader(ActivitiUtil.getSignName(SessionUtil.getDisplayName(),isAgentTask));
                    archivesLibrary.setDeptSLeaderIdeaContent(flowDto.getDealOption());
                    archivesLibrary.setDeptSleaderDate(new Date());
                    archivesLibrary.setArchivesStatus(Constant.EnumState.PROCESS.getValue());   //1表示部长已经审批
                    //如果是外借档案，则由主任审批
                    if (EnumState.YES.getValue().equals(archivesLibrary.getIsLendOut())) {
                        archivesLibrary.setArchivesStatus(EnumState.STOP.getValue());   //2表示到主任审批
                        dealUserList = userRepo.findUserByRoleName(Constant.EnumFlowNodeGroupName.DIRECTOR.getValue());
                        if (!Validate.isList(dealUserList)) {
                            return new ResultMsg(false, MsgCode.ERROR.getValue(), "请先设置【" + Constant.EnumFlowNodeGroupName.DIRECTOR.getValue() + "】角色用户！");
                        }
                        dealUser = dealUserList.get(0);
                        assigneeValue = userService.getTaskDealId(dealUser, agentTaskList, FlowConstant.FLOW_ARC_ZR_SP);
                        variables.put(FlowConstant.FlowParams.USER_ZR.getValue(), assigneeValue);
                        variables.put(FlowConstant.FlowParams.FGLD_FZ.getValue(), EnumState.YES.getValue());
                        //下一环节还是自己处理
                        if (assigneeValue.equals(SessionUtil.getUserId())) {
                            isNextUser = true;
                            nextNodeKey = FlowConstant.FLOW_ARC_ZR_SP;
                        }
                    } else {//借阅评审中心文档,需要经过归档员
                        if (EnumState.NO.getValue().equals(archivesLibrary.getArchivesType())) {
                            archivesLibrary.setArchivesStatus(EnumState.NORMAL.getValue()); //5表示到归档员处理
                            dealUserList = userRepo.findUserByRoleName(Constant.EnumFlowNodeGroupName.FILER.getValue());
                            if (!Validate.isList(dealUserList)) {
                                return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), "请先设置【" + Constant.EnumFlowNodeGroupName.FILER.getValue() + "】角色用户！");
                            }
                            dealUser = dealUserList.get(0);
                            assigneeValue = userService.getTaskDealId(dealUser, agentTaskList, FlowConstant.FLOW_ARC_GDY);
                            variables = ActivitiUtil.setAssigneeValue(FlowConstant.FlowParams.USER_GDY.getValue(), assigneeValue);
                            variables.put(FlowConstant.FlowParams.FGLD_FZ.getValue(), EnumState.PROCESS.getValue());
                        } else {
                            variables.put(FlowConstant.FlowParams.FGLD_FZ.getValue(), EnumState.NO.getValue());
                            archivesLibrary.setIsAgree(EnumState.YES.getValue());
                        }
                    }
                    //不同意，则直接结束
                } else {
                    variables.put(FlowConstant.FlowParams.FGLD_FZ.getValue(), EnumState.NO.getValue());
                    archivesLibrary.setIsAgree(EnumState.NO.getValue());

                }
                archivesLibraryRepo.save(archivesLibrary);
                break;
            //主任审批
            case FlowConstant.FLOW_ARC_ZR_SP:
                if (flowDto.getBusinessMap().get("AGREE") == null || !Validate.isString(flowDto.getBusinessMap().get("AGREE").toString())) {
                    return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), "请选择同意或者不同意！");
                }
                archivesLibrary = archivesLibraryRepo.findById(ArchivesLibrary_.id.getName(), businessId);
                if(isAgentTask){
                    archivesLibrary.setDeptDirectorId(agentTaskService.getUserId(task.getId(),curUserId));
                }else{
                    archivesLibrary.setDeptDirectorId(curUserId);
                }
                archivesLibrary.setDeptDirector(ActivitiUtil.getSignName(SessionUtil.getDisplayName(),isAgentTask));
                archivesLibrary.setDeptDirectorIdeaContent(flowDto.getDealOption());
                archivesLibrary.setDeptDirectorDate(new Date());
                archivesLibrary.setArchivesStatus(Constant.EnumState.YES.getValue());   //9表示已结束
                //同意或者不同意
                if (EnumState.YES.getValue().equals(flowDto.getBusinessMap().get("AGREE").toString())) {
                    archivesLibrary.setIsAgree(EnumState.YES.getValue());
                    archivesLibrary.setArchivesStatus(EnumState.NORMAL.getValue()); //5表示到归档员处理
                    dealUserList = userRepo.findUserByRoleName(Constant.EnumFlowNodeGroupName.FILER.getValue());
                    if (!Validate.isList(dealUserList)) {
                        return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), "请先设置【" + Constant.EnumFlowNodeGroupName.FILER.getValue() + "】角色用户！");
                    }
                    dealUser = dealUserList.get(0);
                    assigneeValue = userService.getTaskDealId(dealUser, agentTaskList, FlowConstant.FLOW_ARC_GDY);
                    variables = ActivitiUtil.setAssigneeValue(FlowConstant.FlowParams.USER_GDY.getValue(), assigneeValue);
                    variables.put(FlowConstant.FlowParams.ZR_FZ.getValue(), true);
                } else {
                    archivesLibrary.setIsAgree(EnumState.NO.getValue());
                    variables.put(FlowConstant.FlowParams.ZR_FZ.getValue(), false);
                }
                archivesLibraryRepo.save(archivesLibrary);
                break;
            //归档员
            case FlowConstant.FLOW_ARC_GDY:
                archivesLibrary = archivesLibraryRepo.findById(ArchivesLibrary_.id.getName(), businessId);
                if (flowDto.getBusinessMap().get("RETURNDATE") == null || !Validate.isString(flowDto.getBusinessMap().get("RETURNDATE").toString())) {
                    archivesLibrary.setResotoreDate(new Date());
                } else {
                    archivesLibrary.setResotoreDate(DateUtils.converToDate(flowDto.getBusinessMap().get("RETURNDATE").toString(), null));
                }
                archivesLibrary.setArchivesUserName(SessionUtil.getDisplayName());
                archivesLibraryRepo.save(archivesLibrary);
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
                        return dealFlow(processInstance, t, flowDto);
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
        //当下一个处理人还是自己的时候，任务ID是已经改变了的，所以这里要返回任务ID
        return new ResultMsg(true, Constant.MsgCode.OK.getValue(), task.getId(),"操作成功！",null);
    }

    @Override
    public ResultMsg endFlow(String businessKey) {
        ArchivesLibrary archivesLibrary = archivesLibraryRepo.findById(ArchivesLibrary_.id.getName(),businessKey);
        if(Validate.isObject(archivesLibrary)){
            if(!SessionUtil.getUserId().equals(archivesLibrary.getCreatedBy()) && !SUPER_ACCOUNT.equals(SessionUtil.getLoginName())){
                return ResultMsg.error("您无权进行删除流程操作！");
            }
            archivesLibrary.setArchivesStatus(EnumState.FORCE.getValue());
            archivesLibraryRepo.save(archivesLibrary);
        }
        return ResultMsg.ok("操作成功！");
    }


}