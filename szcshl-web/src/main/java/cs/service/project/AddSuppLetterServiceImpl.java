package cs.service.project;

import cs.common.constants.Constant;
import cs.common.constants.Constant.EnumState;
import cs.common.constants.FlowConstant;
import cs.common.HqlBuilder;
import cs.common.ResultMsg;
import cs.common.utils.*;
import cs.domain.project.*;
import cs.domain.sys.OrgDept;
import cs.domain.sys.User;
import cs.model.PageModelDto;
import cs.model.flow.FlowDto;
import cs.model.project.AddSuppLetterDto;
import cs.repository.odata.ODataObj;
import cs.repository.repositoryImpl.project.*;
import cs.repository.repositoryImpl.sys.OrgDeptRepo;
import cs.repository.repositoryImpl.sys.UserRepo;
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

import static cs.common.constants.SysConstants.SEPARATE_COMMA;
import static cs.common.constants.SysConstants.SUPER_ACCOUNT;


/**
 * Description: 项目资料补充函 业务操作实现类
 * author: ldm
 * Date: 2017-8-1 18:05:57
 */
@Service
public class AddSuppLetterServiceImpl implements AddSuppLetterService {
    private static Logger logger = Logger.getLogger(AddSuppLetterServiceImpl.class);

    @Autowired
    private AddSuppLetterRepo addSuppLetterRepo;
    @Autowired
    private SignRepo signRepo;
    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private ProcessEngine processEngine;
    @Autowired
    private TaskService taskService;
    @Autowired
    private OrgDeptRepo orgDeptRepo;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private SignBranchRepo signBranchRepo;
    @Autowired
    private WorkProgramRepo workProgramRepo;
    @Autowired
    private AgentTaskService agentTaskService;
    @Autowired
    private UserService userService;
    /**
     * 保存补充资料函
     */
    @Override
    @Transactional
    public ResultMsg saveSupp(AddSuppLetterDto addSuppLetterDto) {
        if (Validate.isString(addSuppLetterDto.getBusinessId())) {
            AddSuppLetter addSuppLetter = new AddSuppLetter();
            Date now = new Date();
            if (Validate.isString(addSuppLetterDto.getId())) {
                addSuppLetter = addSuppLetterRepo.findById(addSuppLetterDto.getId());
                BeanCopierUtils.copyPropertiesIgnoreNull(addSuppLetterDto, addSuppLetter);
            } else {
                BeanCopierUtils.copyProperties(addSuppLetterDto, addSuppLetter);
                addSuppLetter.setCreatedBy(SessionUtil.getUserId());
                addSuppLetter.setCreatedDate(now);
            }
            addSuppLetter.setModifiedDate(now);
            addSuppLetter.setModifiedBy(SessionUtil.getDisplayName());
            addSuppLetterRepo.save(addSuppLetter);

            BeanCopierUtils.copyProperties(addSuppLetter, addSuppLetterDto);
            return new ResultMsg(true, Constant.MsgCode.OK.getValue(), "操作成功！", addSuppLetterDto);
        } else {
            return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), "操作失败，获取项目信息失败，请联系相关人员处理！");
        }
    }

    /**
     * 根据ID查询
     *
     * @param id
     * @return
     */
    @Override
    public AddSuppLetterDto findById(String id) {
        AddSuppLetter addSuppLetter = addSuppLetterRepo.findById(id);
        AddSuppLetterDto addSuppLetterDto = new AddSuppLetterDto();
        BeanCopierUtils.copyProperties(addSuppLetter, addSuppLetterDto);
        return addSuppLetterDto;
    }


    /**
     * 新增初始化
     *
     * @param businessId
     * @param businessType
     * @return
     */
    @Override
    public AddSuppLetterDto initSuppLetter(String businessId, String businessType) {
        AddSuppLetterDto suppletterDto = new AddSuppLetterDto();
        //新增
        if (Constant.BusinessType.SIGN.getValue().equals(businessType)) {
            Sign sign = signRepo.findById(Sign_.signid.getName(), businessId);
            OrgDept orgDept = orgDeptRepo.queryBySignBranchId(businessId, FlowConstant.SignFlowParams.BRANCH_INDEX1.getValue());
            if (orgDept != null) {
                suppletterDto.setOrgName(orgDept.getName());
            }
            suppletterDto.setUserName(SessionUtil.getDisplayName());
            suppletterDto.setSuppLetterTime(new Date());
            suppletterDto.setBusinessId(businessId);
            suppletterDto.setBusinessType(businessType);

            suppletterDto.setTitle("《" + sign.getProjectname() + sign.getReviewstage() + "》");
            suppletterDto.setSecretLevel(sign.getSecrectlevel());
            suppletterDto.setMergencyLevel(sign.getUrgencydegree());
        }
        return suppletterDto;
    }

    /**
     * 根据业务ID查询拟补充资料函
     *
     * @param businessId
     * @return
     */
    @Override
    public List<AddSuppLetterDto> initSuppList(String businessId) {
        HqlBuilder hql = HqlBuilder.create();
        hql.append(" from " + AddSuppLetter.class.getSimpleName() + " where " + AddSuppLetter_.businessId.getName() + " = :businessId ");
        hql.setParam("businessId", businessId);
        List<AddSuppLetter> suppletterlist = addSuppLetterRepo.findByHql(hql);
        List<AddSuppLetterDto> addSuppLetterDtos = new ArrayList<AddSuppLetterDto>();
        if (Validate.isList(suppletterlist)) {
            suppletterlist.forEach(a -> {
                AddSuppLetterDto addDto = new AddSuppLetterDto();
                BeanCopierUtils.copyProperties(a, addDto);
                addSuppLetterDtos.add(addDto);
            });
        }

        return addSuppLetterDtos;
    }

    /**
     * 根据业务ID判断是否有补充资料函
     *
     * @param businessId
     * @return
     */
    @Override
    public boolean isHaveSuppLetter(String businessId) {
        return addSuppLetterRepo.isHaveSuppLetter(businessId);
    }

    /**
     * 保存月报（中心）文件稿纸
     */
    @Override
    @Transactional
    public ResultMsg saveMonthlyMultiyear(AddSuppLetterDto record) {
        Date now = new Date();
        AddSuppLetter addSuppLetter = new AddSuppLetter();
        if (Validate.isString(record.getId())) {
            addSuppLetter = addSuppLetterRepo.findById(record.getId());
            BeanCopierUtils.copyPropertiesIgnoreNull(record, addSuppLetter);
        } else {
            BeanCopierUtils.copyProperties(record, addSuppLetter);
            addSuppLetter.setOrgName(SessionUtil.getUserInfo().getOrg() == null ? "" : SessionUtil.getUserInfo().getOrg().getName());
            addSuppLetter.setUserName(SessionUtil.getUserInfo().getDisplayName());
            addSuppLetter.setCreatedBy(SessionUtil.getUserInfo().getId());
            addSuppLetter.setModifiedBy(SessionUtil.getUserInfo().getId());
            addSuppLetter.setModifiedDate(now);
            addSuppLetter.setCreatedDate(now);
        }
        if(!Validate.isObject(addSuppLetter.getSuppLetterTime())){
            addSuppLetter.setSuppLetterTime(now);
        }
        //设置年份和月份
        addSuppLetter.setFileYear(DateUtils.getYear(addSuppLetter.getSuppLetterTime()));
        addSuppLetterRepo.save(addSuppLetter);
        return new ResultMsg(true, Constant.MsgCode.OK.getValue(), "操作成功！", addSuppLetter);
    }

    /**
     * 获取月报简报
     */
    @Override
    public PageModelDto<AddSuppLetterDto> monthlyMultiyearListData(ODataObj odataObj) {
        PageModelDto<AddSuppLetterDto> pageModelDto = new PageModelDto<AddSuppLetterDto>();
        List<AddSuppLetter> resultList = addSuppLetterRepo.findByOdata(odataObj);
        List<AddSuppLetterDto> resultDtoList = new ArrayList<AddSuppLetterDto>(resultList==null?0:resultList.size());
        if (Validate.isList(resultList)) {
            resultList.forEach(x -> {
                AddSuppLetterDto modelDto = new AddSuppLetterDto();
                BeanCopierUtils.copyProperties(x, modelDto);
                resultDtoList.add(modelDto);
            });
        }
        pageModelDto.setValue(resultDtoList);
        pageModelDto.setCount(odataObj.getCount());

        return pageModelDto;
    }

    /**
     * 初始化（中心）文件稿纸
     */
    @Override
    public AddSuppLetterDto initMonthlyMutilyear() {
        AddSuppLetterDto suppletterDto = new AddSuppLetterDto();
        suppletterDto.setOrgName(SessionUtil.getUserInfo().getOrg() == null ? "" : SessionUtil.getUserInfo().getOrg().getName());
        suppletterDto.setUserName(SessionUtil.getDisplayName());
        suppletterDto.setSuppLetterTime(new Date());
        return suppletterDto;
    }

    /**
     * 删除年度（中心）月报简报记录
     */
    @Override
    public void delete(String id) {
       /* this.delete(id);*/
       AddSuppLetter addSuppLetter=addSuppLetterRepo.findById(id);
       if(addSuppLetter !=null){
           addSuppLetterRepo.delete(addSuppLetter);
       }

    }

    @Override
    public void deletes(String[] ids) {
        for (String id : ids) {
           this.delete(id);
        }
    }

    /**
     * 获取拟补充资料函查询列表
     */
    @Override
    public PageModelDto<AddSuppLetterDto> addsuppListData(ODataObj odataObj) {
        PageModelDto<AddSuppLetterDto> pageModelDto = new PageModelDto<AddSuppLetterDto>();
        List<AddSuppLetter> allist = addSuppLetterRepo.findByOdata(odataObj);
        List<AddSuppLetterDto> alDtos = new ArrayList<AddSuppLetterDto>(allist == null ? 0 : allist.size());
        if (Validate.isList(allist)) {
            allist.forEach(x -> {
                AddSuppLetterDto alDto = new AddSuppLetterDto();
                BeanCopierUtils.copyProperties(x, alDto);
                alDtos.add(alDto);
            });
        }
        pageModelDto.setValue(alDtos);
        pageModelDto.setCount(odataObj.getCount());
        return pageModelDto;
    }

    /**
     * 获取拟补充资料函审批处理列表
     */
    @Override
    public PageModelDto<AddSuppLetterDto> addSuppApproveList(ODataObj oDataObj) {
        PageModelDto<AddSuppLetterDto> pageModelDto = new PageModelDto<>();
        Criteria criteria = addSuppLetterRepo.getExecutableCriteria();
        criteria = oDataObj.buildFilterToCriteria(criteria);
        Boolean falg = false;
        //部长审批
        if (SessionUtil.hashRole(Constant.EnumFlowNodeGroupName.DEPT_LEADER.getValue())) {
            falg = true;
            criteria.add(Restrictions.eq(AddSuppLetter_.deptMinisterName.getName(), SessionUtil.getDisplayName()));
            criteria.add(Restrictions.eq(AddSuppLetter_.appoveStatus.getName(), Constant.EnumState.NO.getValue()));
        }
        //分管领导
        else if (SessionUtil.hashRole(Constant.EnumFlowNodeGroupName.VICE_DIRECTOR.getValue())) {
            criteria.add(Restrictions.eq(AddSuppLetter_.deptSLeaderName.getName(), SessionUtil.getDisplayName()));
            criteria.add(Restrictions.eq(AddSuppLetter_.appoveStatus.getName(), Constant.EnumState.PROCESS.getValue()));
            falg = true;
        }
        //主任
        else if (SessionUtil.hashRole(Constant.EnumFlowNodeGroupName.DIRECTOR.getValue())) {
            criteria.add(Restrictions.eq(AddSuppLetter_.deptDirectorName.getName(), SessionUtil.getDisplayName()));
            criteria.add(Restrictions.eq(AddSuppLetter_.appoveStatus.getName(), Constant.EnumState.STOP.getValue()));
            falg = true;
        }
        if (falg) {
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
            List<AddSuppLetter> addSuppList = criteria.list();
            List<AddSuppLetterDto> addSuppDtoList = new ArrayList<>();
            for (AddSuppLetter archivesLibrary : addSuppList) {
                AddSuppLetterDto addDto = new AddSuppLetterDto();
                BeanCopierUtils.copyProperties(archivesLibrary, addDto);
                addSuppDtoList.add(addDto);
            }

            pageModelDto.setValue(addSuppDtoList);
        }
        return pageModelDto;
    }

    /**
     * 发起拟补充资料函流程
     *
     * @param id
     * @return
     */
    @Override
    public ResultMsg startSignSupperFlow(String id) {
        AddSuppLetter addSuppLetter = addSuppLetterRepo.findById(id);
        if (addSuppLetter == null) {
            return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), "发起流程失败，该记录已不存在！");
        }
        if (Validate.isString(addSuppLetter.getProcessInstanceId())) {
            return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), "该记录已发起流程了，不能重复发起流程！");
        }
        //判断项目的主办部门
        OrgDept orgDept = orgDeptRepo.queryBySignBranchId(addSuppLetter.getBusinessId(), FlowConstant.SignFlowParams.BRANCH_INDEX1.getValue());
        if (orgDept == null) {
            return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), "主办部门已被删除，请联系管理员进行处理！");
        }
        if (!Validate.isString(orgDept.getDirectorID())) {
            return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), "【" + orgDept.getName() + "】的部长未设置，请先设置！");
        }
        //1、启动流程
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(FlowConstant.FLOW_SUPP_LETTER, id,
                ActivitiUtil.setAssigneeValue(FlowConstant.FlowParams.USER.getValue(), SessionUtil.getUserId()));

        //2、设置流程实例名称
        processEngine.getRuntimeService().setProcessInstanceName(processInstance.getId(), addSuppLetter.getTitle());

        //3、更改项目状态
        addSuppLetter.setProcessInstanceId(processInstance.getId());
        addSuppLetter.setFileType(EnumState.PROCESS.getValue());            //表示是拟补充资料函类型
        addSuppLetter.setAppoveStatus(Constant.EnumState.NO.getValue());    //0表示到部长审核环节
        addSuppLetterRepo.save(addSuppLetter);

        //4、跳过第一环节（填报）
        Task task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).active().singleResult();
        taskService.addComment(task.getId(), processInstance.getId(), "");    //添加处理信息

        List<AgentTask> agentTaskList = new ArrayList<>();
        String assigneeValue = userService.getTaskDealId(orgDept.getDirectorID(), agentTaskList, FlowConstant.FLOW_SPL_BZ_SP);
        taskService.complete(task.getId(), ActivitiUtil.setAssigneeValue(FlowConstant.FlowParams.USER_BZ.getValue(), assigneeValue));

        //如果是代办，还要更新环节名称和任务ID
        if (Validate.isList(agentTaskList)) {
            agentTaskService.updateAgentInfo(agentTaskList,processInstance.getId(),processInstance.getName());
        }

        //放入腾讯通消息缓冲池
        RTXSendMsgPool.getInstance().sendReceiverIdPool(task.getId(), assigneeValue);

        return new ResultMsg(true, Constant.MsgCode.OK.getValue(), "操作成功！");
    }

    /**
     * 拟补充资料函流程处理
     *
     * @param processInstance
     * @param task
     * @param flowDto
     * @return
     */
    @Override
    @Transactional
    public ResultMsg dealSignSupperFlow(ProcessInstance processInstance, Task task, FlowDto flowDto) {
        String businessId = processInstance.getBusinessKey(),
                assigneeValue = "",                            //流程处理人
                nextNodeKey = "",                              //下一环节名称
                curUserId = SessionUtil.getUserId();            //当前用户ID
        Map<String, Object> variables = new HashMap<>();       //流程参数
        User dealUser = null;                                  //用户
        List<User> dealUserList = null;                        //用户列表
        boolean isNextUser = false,                 //是否是下一环节处理人（主要是处理领导审批，部长审批）
                isAgentTask = agentTaskService.isAgentTask(task.getId(),curUserId); //是否代办
        List<AgentTask> agentTaskList = new ArrayList<>();
        AddSuppLetter addSuppLetter = addSuppLetterRepo.findById(AddSuppLetter_.id.getName(), businessId);
        //环节处理人设定
        switch (task.getTaskDefinitionKey()) {
            //项目负责人填报
            case FlowConstant.FLOW_SPL_FZR:
                OrgDept orgDept = orgDeptRepo.queryBySignBranchId(addSuppLetter.getBusinessId(), FlowConstant.SignFlowParams.BRANCH_INDEX1.getValue());
                if (orgDept == null) {
                    return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), "主办部门已被删除，请联系管理员进行处理！");
                }
                if (!Validate.isString(orgDept.getDirectorID())) {
                    return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), "【" + orgDept.getName() + "】的部长未设置，请先设置！");
                }
                assigneeValue = userService.getTaskDealId(orgDept.getDirectorID(), agentTaskList, FlowConstant.FLOW_SPL_BZ_SP);
                variables = ActivitiUtil.setAssigneeValue(FlowConstant.FlowParams.USER_BZ.getValue(), assigneeValue);
                addSuppLetter.setAppoveStatus(Constant.EnumState.NO.getValue());
                addSuppLetterRepo.save(addSuppLetter);
                break;
            //部长审批
            case FlowConstant.FLOW_SPL_BZ_SP:
                //没有分支，则直接跳转到分管领导环节
                if (signBranchRepo.allAssistCount(addSuppLetter.getBusinessId()) == 0) {
                    dealUser = signBranchRepo.findMainSLeader(addSuppLetter.getBusinessId());
                    assigneeValue = userService.getTaskDealId(dealUser, agentTaskList, FlowConstant.FLOW_SPL_FGLD_SP);
                    variables.put(FlowConstant.FlowParams.USER_FGLD.getValue(), assigneeValue);  //设置分管领导
                    variables.put(FlowConstant.FlowParams.FGLD_FZ.getValue(), true);             //设置分支条件
                    //2表示到分管领导会签
                    addSuppLetter.setAppoveStatus(EnumState.STOP.getValue());
                    //下一环节还是自己处理
                    if(assigneeValue.equals(SessionUtil.getUserId())){
                        isNextUser = true;
                        nextNodeKey = FlowConstant.FLOW_SPL_FGLD_SP;
                    }
                //有分支，则跳转到领导会签环节
                } else {
                    dealUserList = signBranchRepo.findAssistOrgDirector(addSuppLetter.getBusinessId());
                    for (int i = 0, l = dealUserList.size(); i < l; i++) {
                        String userId = userService.getTaskDealId(dealUserList.get(i).getId(), agentTaskList,FlowConstant.FLOW_SPL_LD_HQ);
                        assigneeValue = StringUtil.joinString(assigneeValue, SEPARATE_COMMA, userId);
                    }
                    variables.put(FlowConstant.SignFlowParams.USER_HQ_LIST.getValue(), StringUtil.getSplit(assigneeValue, SEPARATE_COMMA));
                    variables.put(FlowConstant.FlowParams.FGLD_FZ.getValue(), false);            //设置分支条件
                    //1表示到领导会签
                    addSuppLetter.setAppoveStatus(EnumState.PROCESS.getValue());
                }
                if(isAgentTask){
                    addSuppLetter.setDeptMinisterId(agentTaskService.getUserId(task.getId(),curUserId));
                }else{
                    addSuppLetter.setDeptMinisterId(curUserId);
                }
                addSuppLetter.setDeptMinisterName(ActivitiUtil.getSignName(SessionUtil.getDisplayName(),isAgentTask));
                addSuppLetter.setDeptMinisterDate(new Date());
                addSuppLetter.setDeptMinisterIdeaContent(flowDto.getDealOption());

                addSuppLetterRepo.save(addSuppLetter);
                break;
            //领导会签
            case FlowConstant.FLOW_SPL_LD_HQ:
                dealUser = signBranchRepo.findMainSLeader(addSuppLetter.getBusinessId());
                assigneeValue = userService.getTaskDealId(dealUser, agentTaskList,FlowConstant.FLOW_SPL_FGLD_SP);
                variables.put(FlowConstant.FlowParams.USER_FGLD.getValue(), assigneeValue);

                String signString = "";
                //旧的会签记录
                String oldMsg = addSuppLetter.getLeaderSignIdeaContent();
                if (Validate.isString(oldMsg) && !"null".equals(oldMsg)) {
                    signString += oldMsg+"<br>";
                }
                signString += flowDto.getDealOption()+" " + SessionUtil.getDisplayName() + " " + DateUtils.converToString(new Date(), null);
                addSuppLetter.setLeaderSignIdeaContent(signString);
                //2表示到分管领导会签
                addSuppLetterRepo.save(addSuppLetter);
                break;

            //分管领导审批
            case FlowConstant.FLOW_SPL_FGLD_SP:
                //如果没有生成文件字号或者生成错的文件字号，则重新生成
                if (!Validate.isString(addSuppLetter.getFilenum()) || !addSuppLetter.getFilenum().contains(Constant.ADDSUPPER_PREFIX)) {
                   initFileNum(addSuppLetter);
                }
                if(!Validate.isString(addSuppLetter.getFilenum())){
                    new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), "无法生成文件字号，请联系管理员查看！");
                }
                if(isAgentTask){
                    addSuppLetter.setDeptSLeaderId(agentTaskService.getUserId(task.getId(),curUserId));
                }else{
                    addSuppLetter.setDeptSLeaderId(curUserId);
                }
                addSuppLetter.setDeptSLeaderName(ActivitiUtil.getSignName(SessionUtil.getDisplayName(),isAgentTask));
                addSuppLetter.setDeptSleaderDate(new Date());
                addSuppLetter.setDeptSLeaderIdeaContent(flowDto.getDealOption());
                addSuppLetter.setAppoveStatus(EnumState.YES.getValue());
                addSuppLetterRepo.save(addSuppLetter);

                //如果是项目，则更新项目补充资料函状态
                if(Validate.isString(addSuppLetter.getBusinessType()) && Constant.BusinessType.SIGN.getValue().equals(addSuppLetter.getBusinessType())){
                    signRepo.updateSuppLetterState(addSuppLetter.getBusinessId(),EnumState.YES.getValue(),addSuppLetter.getDisapDate());
                    workProgramRepo.updateSuppLetterState(addSuppLetter.getBusinessId(),EnumState.YES.getValue(),addSuppLetter.getDisapDate());
                }
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
            if(isNextUser){
                List<Task> nextTaskList = taskService.createTaskQuery().processInstanceId(processInstance.getId()).taskAssignee(assigneeValue).list();
                for(Task t:nextTaskList){
                    if(nextNodeKey.equals(t.getTaskDefinitionKey())){
                        ResultMsg returnMsg = dealSignSupperFlow(processInstance,t,flowDto);
                        if(returnMsg.isFlag() == false){
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

    @Override
    public void initFileNum(AddSuppLetter addSuppLetter) {
        //补充资料函的发文日期
        if(!Validate.isObject(addSuppLetter.getDisapDate())){
            addSuppLetter.setDisapDate(new Date());
        }
        String yearName = DateUtils.converToString(addSuppLetter.getDisapDate(),DateUtils.DATE_YEAR);
        int maxSeq = addSuppLetterRepo.findCurMaxSeq(yearName)+1;
        //String fileNum = maxSeq > 999 ? maxSeq + "" : String.format("%03d", maxSeq);
        String fileNumValue =  Constant.ADDSUPPER_PREFIX+"["+yearName+"]"+maxSeq;
        addSuppLetter.setFilenum(fileNumValue);
        addSuppLetter.setFileSeq(maxSeq);
    }

    /**
     * 检查是否还有正在审批的拟补充资料函
     * @param signId
     * @param fileType
     * @return
     */
    @Override
    public ResultMsg checkIsApprove(String signId, String fileType) {
        return addSuppLetterRepo.checkIsApprove(signId, fileType);
    }

    @Override
    public ResultMsg endFlow(String businessKey) {
        AddSuppLetter addSuppLetter = addSuppLetterRepo.findById(AddSuppLetter_.id.getName(),businessKey);
        if(Validate.isObject(addSuppLetter)){
            if(!SessionUtil.getUserId().equals(addSuppLetter.getCreatedBy()) && !SUPER_ACCOUNT.equals(SessionUtil.getLoginName())){
                return ResultMsg.error("您无权进行删除流程操作！");
            }
            addSuppLetter.setAppoveStatus(Constant.EnumState.FORCE.getValue());
            addSuppLetterRepo.save(addSuppLetter);
        }
        return ResultMsg.ok("操作成功！");
    }

    @Override
    public void updateSuppLetterState(String businessId, String businessType, Date disapDate) {
        //如果是项目，则更新项目补充资料函状态
        if(Validate.isString(businessType) && Constant.BusinessType.SIGN.getValue().equals(businessType)){
            signRepo.updateSuppLetterState(businessId,EnumState.YES.getValue(),disapDate);
            workProgramRepo.updateSuppLetterState(businessId,EnumState.YES.getValue(),disapDate);
        }
    }
}