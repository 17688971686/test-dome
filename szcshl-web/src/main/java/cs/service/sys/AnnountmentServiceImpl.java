package cs.service.sys;

import cs.common.constants.Constant;
import cs.common.constants.FlowConstant;
import cs.common.HqlBuilder;
import cs.common.ResultMsg;
import cs.common.utils.ActivitiUtil;
import cs.common.utils.BeanCopierUtils;
import cs.common.utils.SessionUtil;
import cs.common.utils.Validate;
import cs.domain.monthly.MonthlyNewsletter;
import cs.domain.monthly.MonthlyNewsletter_;
import cs.domain.project.AgentTask;
import cs.domain.sys.*;
import cs.model.PageModelDto;
import cs.model.flow.FlowDto;
import cs.model.sys.AnnountmentDto;
import cs.repository.odata.ODataObj;
import cs.repository.repositoryImpl.sys.AnnountmentRepo;
import cs.repository.repositoryImpl.sys.OrgRepo;
import cs.repository.repositoryImpl.sys.UserRepo;
import cs.service.project.AgentTaskService;
import cs.service.rtx.RTXSendMsgPool;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static cs.common.constants.SysConstants.SUPER_ACCOUNT;

@Service
public class AnnountmentServiceImpl implements AnnountmentService {
    private static Logger logger = Logger.getLogger(AnnountmentServiceImpl.class);

    @Autowired
    private AnnountmentRepo annountmentRepo;
    @Autowired
    private OrgRepo orgRepo;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private ProcessEngine processEngine;
    @Autowired
    private TaskService taskService;
    @Autowired
    private AgentTaskService agentTaskService;
    @Autowired
    private UserService userService;
    /* 
     * 获取个人发布的通知公告
     */
    @Override
    public PageModelDto<AnnountmentDto> findByCurUser(ODataObj odataobj) {
        PageModelDto<AnnountmentDto> pageModelDto = new PageModelDto<>();
        Criteria criteria = annountmentRepo.getExecutableCriteria();
        criteria = odataobj.buildFilterToCriteria(criteria);
        //创建人为当前用户,
        criteria.add(Restrictions.eq(Annountment_.createdBy.getName(), SessionUtil.getUserId()));
        //统计总数
        Integer totalResult = ((Number) criteria.setProjection(Projections.rowCount()).uniqueResult()).intValue();
        pageModelDto.setCount(totalResult);
        //处理分页
        criteria.setProjection(null);
        if (odataobj.getSkip() > 0) {
            criteria.setFirstResult(odataobj.getSkip());
        }
        if (odataobj.getTop() > 0) {
            criteria.setMaxResults(odataobj.getTop());
        }

        //处理orderby
        if (Validate.isString(odataobj.getOrderby())) {
            if (odataobj.isOrderbyDesc()) {
                criteria.addOrder(Property.forName(odataobj.getOrderby()).desc());
            } else {
                criteria.addOrder(Property.forName(odataobj.getOrderby()).asc());
            }
        }

        List<Annountment> annList = criteria.list();
        List<AnnountmentDto> annountmentDtoList = new ArrayList<>();
        for (Annountment annountment : annList) {
            AnnountmentDto annDto = new AnnountmentDto();
            BeanCopierUtils.copyProperties(annountment, annDto);
            annountmentDtoList.add(annDto);
        }

        pageModelDto.setValue(annountmentDtoList);

        return pageModelDto;
    }


    /*
     * 获取所有已经发布的通知公告
     */
    @Override
    public PageModelDto<AnnountmentDto> findByIssue(ODataObj odataobj) {
        PageModelDto<AnnountmentDto> pageModelDto = new PageModelDto<>();
        Criteria criteria = annountmentRepo.getExecutableCriteria();
        criteria.add(Restrictions.like(Annountment_.issue.getName(), Constant.EnumState.YES.getValue()));
        criteria = odataobj.buildFilterToCriteria(criteria);

        //统计总数
        Integer totalResult = ((Number) criteria.setProjection(Projections.rowCount()).uniqueResult()).intValue();
        pageModelDto.setCount(totalResult);
        //处理分页
        criteria.setProjection(null);
        if (odataobj.getSkip() > 0) {
            criteria.setFirstResult(odataobj.getSkip());
        }
        if (odataobj.getTop() > 0) {
            criteria.setMaxResults(odataobj.getTop());
        }

        //处理orderby
        if (Validate.isString(odataobj.getOrderby())) {
            if (odataobj.isOrderbyDesc()) {
                criteria.addOrder(Property.forName(odataobj.getOrderby()).desc());
            } else {
                criteria.addOrder(Property.forName(odataobj.getOrderby()).asc());
            }
        }
        List<Annountment> annountmentList = criteria.list();

        List<AnnountmentDto> annountmentDtoList = new ArrayList<>();
        for (Annountment annountment : annountmentList) {
            AnnountmentDto annountmentDto = new AnnountmentDto();
            BeanCopierUtils.copyProperties(annountment, annountmentDto);
            annountmentDtoList.add(annountmentDto);
        }

        pageModelDto.setValue(annountmentDtoList);
        return pageModelDto;
    }


    @Override
    @Transactional
    public ResultMsg createAnnountment(AnnountmentDto annountmentDto) {
        try {
            Annountment annountment = new Annountment();
            BeanCopierUtils.copyProperties(annountmentDto, annountment);
            Date now = new Date();
            //默认不排序和不置顶
            if (!Validate.isString(annountment.getIssue())) {
                annountment.setIssue(Constant.EnumState.NO.getValue());
            }
            if (!Validate.isObject(annountment.getIsStick())) {
                annountment.setIsStick(Integer.valueOf(Constant.EnumState.NO.getValue()));
            } else {
                if (Constant.EnumState.YES.getValue().equals(annountment.getIsStick())) {
                    annountment.setIssueDate(now);
                }
            }
            if (!Validate.isString(annountment.getAnId())) {
                annountment.setAnId(null);
            }
            //已发布的要加上发布人和发布时间
            if (Constant.EnumState.YES.getValue().equals(annountment.getIssue())) {
                annountment.setIssueDate(now);
            }
            //发布人取显示名
            annountment.setIssueUser(SessionUtil.getDisplayName());
            //基本信息
            annountment.setCreatedBy(SessionUtil.getUserId());
            annountment.setCreatedDate(now);
            annountment.setModifiedBy(SessionUtil.getDisplayName());
            annountment.setModifiedDate(now);
            annountmentRepo.save(annountment);
            BeanCopierUtils.copyProperties(annountment, annountmentDto);

            return new ResultMsg(true, Constant.MsgCode.OK.getValue(), "保存成功！", annountmentDto);
        } catch (Exception e) {
            logger.info("保存通知公告异常：" + e.getMessage());
            return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), "保存异常，错误信息已记录，请联系管理员处理！");
        }
    }

    @Override
    @Transactional
    public String findAnOrg() {
        String loginName = SessionUtil.getLoginName();
        HqlBuilder hqlBuilder = HqlBuilder.create();
        hqlBuilder.append("select o from " + Org.class.getSimpleName() + " o where o." + Org_.id.getName() + "=(");
        hqlBuilder.append("select u." + User_.org.getName() + "." + Org_.id.getName() + " from " + User.class.getSimpleName() + " u where u." + User_.loginName.getName() + "=:loginName)");
        hqlBuilder.setParam("loginName", loginName);
        List<Org> orgList = orgRepo.findByHql(hqlBuilder);
        if (orgList != null && orgList.size() > 0) {
            return orgList.get(0).getName();
        } else {
            return null;
        }

    }

    @Override
    @Transactional
    public AnnountmentDto findAnnountmentById(String anId) {
        Annountment annountment = annountmentRepo.findById(Annountment_.anId.getName(), anId);
        AnnountmentDto annountmentDto = new AnnountmentDto();
        BeanCopierUtils.copyProperties(annountment, annountmentDto);
        return annountmentDto;
    }

    @Override
    @Transactional
    public void updateAnnountment(AnnountmentDto annountmentDto) {
        Annountment annountment = annountmentRepo.findById(Annountment_.anId.getName(), annountmentDto.getAnId());
        if (annountment != null) {
            Date now = new Date();
            BeanCopierUtils.copyPropertiesIgnoreNull(annountmentDto, annountment);
            annountment.setModifiedBy(SessionUtil.getDisplayName());
            annountment.setModifiedDate(now);

            //已发布的要加上发布人和发布时间
            if (Constant.EnumState.YES.getValue().equals(annountment.getIssue())) {
                annountment.setIssueDate(now);
                annountment.setIssueUser(SessionUtil.getLoginName());
            }
        }
        annountmentRepo.save(annountment);
    }

    /**
     * 经过审批的通知公告，不能修改和删除！不用审批的通知公告，若已发布，不能删除
     *
     * @param id
     * @return
     */
    @Override
    @Transactional
    public ResultMsg deleteAnnountment(String id) {
        ResultMsg resultMsg = new ResultMsg(true, Constant.MsgCode.OK.getValue(), "删除成功！");
        List<Annountment> deleteList = annountmentRepo.findByIds(Annountment_.anId.getName(), id, null);
        if (Validate.isList(deleteList)) {
            for (int i = 0, l = deleteList.size(); i < l; i++) {
                Annountment dl = deleteList.get(i);
                if (Validate.isString(dl.getProcessInstanceId()) || Constant.EnumState.YES.getValue().equals(dl.getIssue())) {
                    resultMsg = new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), "操作失败，审批的通知公告和已发布的通知公告不能删除！");
                    break;
                }
            }
        }
        if (resultMsg.isFlag()) {
            annountmentRepo.deleteById(Annountment_.anId.getName(), id);
        }
        return resultMsg;
    }

    /**
     * 获取主页上的通知公告数据，最多6条数据
     *
     * @return
     */
    @Override
    @Transactional
    public List<AnnountmentDto> getHomePageAnnountment() {
       /* HqlBuilder hqlBuilder = HqlBuilder.create();
        hqlBuilder.append("select a from " + Annountment.class.getSimpleName() + " a order by " + Annountment_.isStick.getName() + " desc," + Annountment_.issueDate.getName() + " desc");
        List<Annountment> annList = annountmentRepo.findByHql(hqlBuilder);*/

        //通过Criteria查询
        Criteria criteria = annountmentRepo.getExecutableCriteria();
        criteria.add(Restrictions.eq(Annountment_.issue.getName(), Constant.EnumState.YES.getValue()));
        criteria.addOrder(Order.desc(Annountment_.isStick.getName())).addOrder(Order.desc(Annountment_.issueDate.getName()));
        criteria.setMaxResults(6);
        List<Annountment> annList = criteria.list();

        List<AnnountmentDto> annDtoList = new ArrayList<>();
        for (Annountment ann : annList) {
            AnnountmentDto annDto = new AnnountmentDto();
            BeanCopierUtils.copyProperties(ann, annDto);
            annDtoList.add(annDto);
        }
        return annDtoList;
    }

    /**
     * 上一篇
     *
     * @param id
     * @return
     */
    @Override
    @Transactional
    public AnnountmentDto postAritle(String id) {
        HqlBuilder hqlBuilder = HqlBuilder.create();
        hqlBuilder.append("with res as(select cs_annountment.*,row_number() over(order by isstick desc,issueDate desc)t from cs_annountment)");
        hqlBuilder.append(" select res.* from res where t=(");
        hqlBuilder.append("select res.t-1 from res where res.anid=:id)");
        hqlBuilder.append(" and res.issue=:issue");
        hqlBuilder.setParam("id", id);
        hqlBuilder.setParam("issue", Constant.EnumState.YES.getValue());
        List<Annountment> annList = annountmentRepo.findBySql(hqlBuilder);
        AnnountmentDto annDto = new AnnountmentDto();
        if (annList.size() > 0) {
            BeanCopierUtils.copyProperties(annList.get(0), annDto);
        }
        return annDto;
    }

    /**
     * 下一篇
     *
     * @param id
     * @return
     */
    @Override
    @Transactional
    public AnnountmentDto nextArticle(String id) {
        HqlBuilder hqlBuilder = HqlBuilder.create();
        hqlBuilder.append("with res as(select cs_annountment.*,row_number() over(order by isstick desc,issueDate desc)t from cs_annountment)");
        hqlBuilder.append(" select res.* from res where t=(");
        hqlBuilder.append("select res.t+1 from res where res.anid=:id)");
        hqlBuilder.append(" and res.issue=:issue");
        hqlBuilder.setParam("id", id);
        hqlBuilder.setParam("issue", Constant.EnumState.YES.getValue());
        List<Annountment> annList = annountmentRepo.findBySql(hqlBuilder);
        AnnountmentDto annDto = new AnnountmentDto();
        if (annList.size() > 0) {
            BeanCopierUtils.copyProperties(annList.get(0), annDto);
        }
        return annDto;
    }

    /**
     * 更改通知公告的发布状态
     *
     * @param ids
     * @param issueState
     */
    @Override
    @Transactional
    public ResultMsg updateIssueState(String ids, String issueState) {
        //是否发布,如果是发布，还要更新发布日期
        boolean isaPublish = (Constant.EnumState.YES.getValue().equals(issueState)) ? true : false;
        HqlBuilder hqlBuilder = HqlBuilder.create();
        hqlBuilder.append(" update "+Annountment.class.getSimpleName()+" set "+Annountment_.issue.getName()+" =:issue ");
        hqlBuilder.setParam("issue",issueState);
        if(isaPublish){
            hqlBuilder.append(","+Annountment_.issueDate.getName()+" =sysdate ");
            hqlBuilder.append(","+Annountment_.issueUser.getName()+" = "+Annountment_.modifiedBy.getName());
        }
        annountmentRepo.executeHql(hqlBuilder);

        return new ResultMsg(true, Constant.MsgCode.OK.getValue(),"更新成功！");
    }


    /**
     * 通知公告发起流程
     *
     * @param id
     * @return
     */
    @Override
    public ResultMsg startFlow(String id) {
        Annountment annountment = annountmentRepo.findById(Annountment_.anId.getName(), id);
        String assigneeValue;
        Map<String, Object> variables = new HashMap<>();       //流程参数
        if (annountment == null) {
            return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), "发起流程失败，该项目已不存在！");
        }
        if (Validate.isString(annountment.getProcessInstanceId())) {
            return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), "该项目已发起流程！");
        }
        if (SessionUtil.getUserInfo().getOrg() == null || !Validate.isString(SessionUtil.getUserInfo().getOrg().getOrgDirector())) {
            return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), "您所在部门还没设置部长，请联系管理员进行设置！");
        }

        //查询部门领导
        User user = userRepo.findOrgDirector(annountment.getCreatedBy());
        if (!Validate.isObject(user) || !Validate.isString(user.getId())) {
            return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), "【" + user.getOrg().getName() + "】的部长未设置，请先设置！");
        }
        //1、启动流程
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(FlowConstant.ANNOUNT_MENT_FLOW, id,
                ActivitiUtil.setAssigneeValue(FlowConstant.AnnountMentFLOWParams.USER.getValue(), SessionUtil.getUserId()));

        //2、设置流程实例名称
        processEngine.getRuntimeService().setProcessInstanceName(processInstance.getId(), annountment.getAnTitle());

        //3、更改项目状态
        annountment.setProcessInstanceId(processInstance.getId());
        annountment.setAppoveStatus(Constant.EnumState.NO.getValue());
        annountmentRepo.save(annountment);

        //4、跳过第一环节（填报）
        Task task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).active().singleResult();
        taskService.addComment(task.getId(), processInstance.getId(), "");    //添加处理信息

        List<AgentTask> agentTaskList = new ArrayList<>();
        if (SessionUtil.hashRole(Constant.EnumFlowNodeGroupName.DEPT_LEADER.getValue())) {
            assigneeValue = userService.getTaskDealId(SessionUtil.getUserInfo().getOrg().getOrgSLeader(), agentTaskList, FlowConstant.ANNOUNT_FZ);
            variables.put(FlowConstant.AnnountMentFLOWParams.ANNOUNT_USER.getValue(), true);
            taskService.complete(task.getId(), variables);
        } else {
            String userId = SessionUtil.getUserInfo().getOrg().getOrgDirector() == null ? SessionUtil.getUserId() : SessionUtil.getUserInfo().getOrg().getOrgDirector();
            assigneeValue = userService.getTaskDealId(userId, agentTaskList, FlowConstant.ANNOUNT_BZ);
            variables = ActivitiUtil.setAssigneeValue(FlowConstant.AnnountMentFLOWParams.USER_BZ.getValue(), assigneeValue);
            variables.put(FlowConstant.AnnountMentFLOWParams.ANNOUNT_USER.getValue(), false);
            taskService.complete(task.getId(), variables);
        }

        //如果是代办，还要更新环节名称和任务ID
        if (Validate.isList(agentTaskList)) {
            agentTaskService.updateAgentInfo(agentTaskList,processInstance.getId(),processInstance.getName());
        }
        //放入腾讯通消息缓冲池
        RTXSendMsgPool.getInstance().sendReceiverIdPool(task.getId(), assigneeValue);
        return new ResultMsg(true, Constant.MsgCode.OK.getValue(), "操作成功！");
    }

    /**
     * 通知公告流程处理
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
                assigneeValue = "",                             //流程处理人
                nextNodeKey = "",                               //下一环节名称
                curUserId = SessionUtil.getUserId();           //当前用户ID
        List<User> userList = null;                             //用户列表
        User dealUser = null;
        Map<String, Object> variables = new HashMap<>();        //流程参数
        boolean isNextUser = false,                             //是否是下一环节处理人（主要是处理领导审批，部长审批）
                isAgentTask = agentTaskService.isAgentTask(task.getId(),curUserId); //是否代办
        Annountment annountment = annountmentRepo.findById(Annountment_.anId.getName(), businessId);
        List<AgentTask> agentTaskList = new ArrayList<>();

        //环节处理人设定
        switch (task.getTaskDefinitionKey()) {
            //项目负责人填报
            case FlowConstant.ANNOUNT_TZ:
                flowDto.setDealOption("");//默认意见为空
                if (SessionUtil.hashRole(Constant.EnumFlowNodeGroupName.DEPT_LEADER.getValue())) {//判断是否是部长
                    assigneeValue = userService.getTaskDealId(SessionUtil.getUserInfo().getOrg().getOrgSLeader(), agentTaskList, FlowConstant.ANNOUNT_FZ);
                    variables = ActivitiUtil.setAssigneeValue(FlowConstant.AnnountMentFLOWParams.ANNOUNT_USER.getValue(), assigneeValue);
                    variables.put(FlowConstant.AnnountMentFLOWParams.ANNOUNT_USER.getValue(), true);
                } else {
                    String userId = SessionUtil.getUserInfo().getOrg().getOrgDirector() == null ? SessionUtil.getUserId() : SessionUtil.getUserInfo().getOrg().getOrgDirector();
                    assigneeValue = userService.getTaskDealId(userId, agentTaskList, FlowConstant.ANNOUNT_BZ);
                    variables = ActivitiUtil.setAssigneeValue(FlowConstant.AnnountMentFLOWParams.ANNOUNT_USER.getValue(), assigneeValue);
                    variables.put(FlowConstant.AnnountMentFLOWParams.ANNOUNT_USER.getValue(), false);
                }
                break;
            //部长审批
            case FlowConstant.ANNOUNT_BZ:
                //获取创建人的部长
                dealUser = userRepo.getCacheUserById(annountment.getCreatedBy());
                if(!Validate.isString(dealUser.getOrg().getOrgSLeader())){
                    return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(),"您还没设置该部门的分管副主任，请先设置再提交流程！");
                }
                assigneeValue = userService.getTaskDealId(dealUser.getOrg().getOrgSLeader(), agentTaskList, FlowConstant.ANNOUNT_FZ);
                variables = ActivitiUtil.setAssigneeValue(FlowConstant.AnnountMentFLOWParams.USER_FZ.getValue(), assigneeValue);

                if(isAgentTask){
                    annountment.setDeptMinisterId(agentTaskService.getUserId(task.getId(),curUserId));
                }else{
                    annountment.setDeptMinisterId(curUserId);
                }
                annountment.setDeptMinisterName(ActivitiUtil.getSignName(SessionUtil.getDisplayName(),isAgentTask));

                annountment.setDeptMinisterDate(new Date());
                annountment.setDeptMinisterIdeaContent(flowDto.getDealOption());
                annountment.setAppoveStatus(Constant.EnumState.PROCESS.getValue());
                annountmentRepo.save(annountment);

                //下一环节还是自己处理
                if (assigneeValue.equals(SessionUtil.getUserId())) {
                    isNextUser = true;
                    nextNodeKey = FlowConstant.ANNOUNT_FZ;
                }
                break;
            //副主任审批
            case FlowConstant.ANNOUNT_FZ:
                dealUser = userRepo.getCacheUserById(annountment.getCreatedBy());
                if(!Validate.isString(dealUser.getOrg().getOrgMLeader())){
                    return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(),"您还没设置该部门的主任，请先设置再提交流程！");
                }
                assigneeValue = userService.getTaskDealId(dealUser.getOrg().getOrgMLeader(), agentTaskList, FlowConstant.ANNOUNT_FZ);
                variables = ActivitiUtil.setAssigneeValue(FlowConstant.AnnountMentFLOWParams.USER_ZR.getValue(), assigneeValue);

                if(isAgentTask){
                    annountment.setDeptSLeaderId(agentTaskService.getUserId(task.getId(),curUserId));
                }else{
                    annountment.setDeptSLeaderId(curUserId);
                }
                annountment.setDeptSLeaderName(ActivitiUtil.getSignName(SessionUtil.getDisplayName(),isAgentTask));

                annountment.setDeptSleaderDate(new Date());
                annountment.setDeptSLeaderIdeaContent(flowDto.getDealOption());
                annountment.setAppoveStatus(Constant.EnumState.STOP.getValue());
                annountmentRepo.save(annountment);
                //查询部门主任领导

                //下一环节还是自己处理
                if (assigneeValue.equals(SessionUtil.getUserId())) {
                    isNextUser = true;
                    nextNodeKey = FlowConstant.ANNOUNT_ZR;
                    flowDto.getBusinessMap().put("AGREE", Constant.EnumState.YES.getValue());
                }
                break;

            //主任审批
            case FlowConstant.ANNOUNT_ZR:
                boolean isPass = Validate.isObject(flowDto.getBusinessMap().get("AGREE")) && Constant.EnumState.YES.getValue().equals(flowDto.getBusinessMap().get("AGREE").toString());
                if(isPass){
                    annountment.setIssue(Constant.EnumState.YES.getValue());
                    annountment.setIssueDate(new Date());
                    //发布人为最后修改人
                    annountment.setIssueUser(annountment.getModifiedBy());
                }else{
                    annountment.setIssue(Constant.EnumState.NO.getValue());
                }
                if(isAgentTask){
                    annountment.setDeptDirectorId(agentTaskService.getUserId(task.getId(),curUserId));
                }else{
                    annountment.setDeptDirectorId(curUserId);
                }
                annountment.setDeptDirectorName(ActivitiUtil.getSignName(SessionUtil.getDisplayName(),isAgentTask));

                annountment.setDeptDirectorDate(new Date());
                annountment.setDeptDirectorIdeaContent(flowDto.getDealOption());
                annountment.setAppoveStatus(Constant.EnumState.YES.getValue());
                annountmentRepo.save(annountment);

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
                        ResultMsg returnMsg = dealSignSupperFlow(processInstance, t, flowDto);
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
            agentTaskService.updateAgentInfo(agentTaskList,processInstance.getId(),processInstance.getName());
        }
        //放入腾讯通消息缓冲池
        RTXSendMsgPool.getInstance().sendReceiverIdPool(task.getId(), assigneeValue);
        return new ResultMsg(true, Constant.MsgCode.OK.getValue(), "操作成功！");
    }

    @Override
    public ResultMsg endFlow(String businessKey) {
        Annountment annountment = annountmentRepo.findById(Annountment_.anId.getName(),businessKey);
        if(Validate.isObject(annountment)){
            if(!SessionUtil.getUserId().equals(annountment.getCreatedBy()) && !SUPER_ACCOUNT.equals(SessionUtil.getLoginName())){
                return ResultMsg.error("您无权进行删除流程操作！");
            }
            annountment.setAppoveStatus(Constant.EnumState.DELETE.getValue());
            annountmentRepo.save(annountment);
        }
        return ResultMsg.ok("操作成功！");
    }


}
