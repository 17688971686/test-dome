package cs.service.reviewProjectAppraise;

import cs.common.Constant;
import cs.common.FlowConstant;
import cs.common.HqlBuilder;
import cs.common.ResultMsg;
import cs.common.utils.ActivitiUtil;
import cs.common.utils.BeanCopierUtils;
import cs.common.utils.SessionUtil;
import cs.common.utils.Validate;
import cs.domain.project.*;
import cs.domain.sys.OrgDept;
import cs.domain.sys.User;
import cs.model.PageModelDto;
import cs.model.flow.FlowDto;
import cs.model.project.AppraiseReportDto;
import cs.repository.odata.ODataObj;
import cs.repository.repositoryImpl.project.SignDispaWorkRepo;
import cs.repository.repositoryImpl.project.SignRepo;
import cs.repository.repositoryImpl.reviewProjectAppraise.AppraiseRepo;
import cs.repository.repositoryImpl.sys.OrgDeptRepo;
import cs.repository.repositoryImpl.sys.UserRepo;
import cs.service.rtx.RTXSendMsgPool;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * Description: 优秀评审项目
 * Author: mcl
 * Date: 2017/9/23 16:10
 */
@Service
public class AppraiseServiceImpl implements AppraiseService {

    @Autowired
    private SignDispaWorkRepo signDispaWorkRepo;

    @Autowired
    private AppraiseRepo appraiseRepo;

    @Autowired
    private SignRepo signRepo;
    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private ProcessEngine processEngine;
    @Autowired
    private TaskService taskService;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private OrgDeptRepo orgDeptRepo;


    /**
     * 查询优秀评审报告列表
     *
     * @param oDataObj
     * @return
     */
    @Override
    public PageModelDto<SignDispaWork> findAppraisingProject(ODataObj oDataObj) {
        PageModelDto<SignDispaWork> pageModelDto = new PageModelDto<>();
        Criteria criteria = signDispaWorkRepo.getExecutableCriteria();
        criteria = oDataObj.buildFilterToCriteria(criteria);
        criteria.add(Restrictions.eq(SignDispaWork_.isAppraising.getName(), Constant.EnumState.YES.getValue()));

        //统计总数
        Integer totalResult = ((Number) criteria.setProjection(Projections.rowCount()).uniqueResult()).intValue();
        pageModelDto.setCount(totalResult);
        //处理分页
        criteria.setProjection(null);
        if (oDataObj.getSkip() > 0) {
            criteria.setFirstResult(oDataObj.getSkip());
        }
        if (oDataObj.getTop() > 0) {
            criteria.setMaxResults(oDataObj.getTop());
        }

        //处理orderby
        if (Validate.isString(oDataObj.getOrderby())) {
            if (oDataObj.isOrderbyDesc()) {
                criteria.addOrder(Property.forName(oDataObj.getOrderby()).desc());
            } else {
                criteria.addOrder(Property.forName(oDataObj.getOrderby()).asc());
            }
        }

        List<SignDispaWork> signDispaWorkList = criteria.list();

        pageModelDto.setValue(signDispaWorkList);


        return pageModelDto;
    }

    @Override
    @Transactional
    public void updateIsAppraising(String signId) {
        HqlBuilder hqlBuilder = HqlBuilder.create();
        hqlBuilder.append("update " + Sign.class.getSimpleName() + " set " + Sign_.isAppraising.getName() + "=:isAppraising " + " where " + Sign_.signid.getName() + "=:signId");
        hqlBuilder.setParam("isAppraising", Constant.EnumState.YES.getValue());
        hqlBuilder.setParam("signId", signId);
        signRepo.executeHql(hqlBuilder);

    }


    /**
     * 保存申请信息
     *
     * @param appraiseReportDto
     */
    @Override
    @Transactional
    public ResultMsg saveApply(AppraiseReportDto appraiseReportDto) {
        Sign sign = signRepo.findById(Sign_.signid.getName(), appraiseReportDto.getSignId());
        AppraiseReport appraiseReport = new AppraiseReport();
        Date now = new Date();
        if (Validate.isString(appraiseReportDto.getId())) {
            appraiseReport = appraiseRepo.findById(appraiseReportDto.getId());
            BeanCopierUtils.copyPropertiesIgnoreNull(appraiseReportDto, appraiseReport);
        } else {
            if (Validate.isString(sign.getIsAppraising()) && (Constant.EnumState.YES.getValue().equals(sign.getIsAppraising())
                    || Constant.EnumState.NO.getValue().equals(sign.getIsAppraising())) || Constant.EnumState.PROCESS.getValue().equals(sign.getIsAppraising())) {
                return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), "该项目已经申报过优秀评审报告评优，不能重复申请！");
            }
            BeanCopierUtils.copyProperties(appraiseReportDto, appraiseReport);
            appraiseReport.setId(UUID.randomUUID().toString());
            appraiseReport.setCreatedBy(SessionUtil.getDisplayName());
            appraiseReport.setCreatedDate(now);
            if (!Validate.isString(appraiseReport.getProposerName())) {
                appraiseReport.setProposerName(SessionUtil.getDisplayName());
            }
            if (appraiseReport.getProposerTime() == null) {
                appraiseReport.setProposerTime(now);
            }
        }

        appraiseReport.setModifiedBy(SessionUtil.getDisplayName());
        appraiseReport.setModifiedDate(now);
        appraiseRepo.save(appraiseReport);

        //修改收文 状态
        sign.setIsAppraising(Constant.EnumState.PROCESS.getValue());
        signRepo.save(sign);
       /* HqlBuilder hql = HqlBuilder.create();
        hql.append("update " +Sign.class.getSimpleName() + " set " + Sign_.isAppraising.getName() + "=:isAppraising");
        hql.append(" where " + Sign_.signid.getName() + "=:signId");
        hql.setParam("signId" , appraiseReportDto.getSignId());
        hql.setParam("isAppraising" , Constant.EnumState.PROCESS.getValue());//修改收文为：审核中
        signRepo.executeHql(hql);*/

        //复制返回值
        BeanCopierUtils.copyProperties(appraiseReport, appraiseReportDto);

        return new ResultMsg(true, Constant.MsgCode.OK.getValue(), "操作成功！", appraiseReportDto);
    }

    /**
     * 查询优秀评审报告审批列表
     *
     * @param oDataObj
     * @return
     */
    @Override
    public PageModelDto<AppraiseReportDto> getAppraiseReport(ODataObj oDataObj) {
        PageModelDto<AppraiseReportDto> pageModelDto = new PageModelDto<>();
        Criteria criteria = appraiseRepo.getExecutableCriteria();
        criteria = oDataObj.buildFilterToCriteria(criteria);
       /*
        if(SessionUtil.hashRole(Constant.EnumFlowNodeGroupName.DEPT_LEADER.getValue())
                || SessionUtil.hashRole(Constant.EnumFlowNodeGroupName.COMM_DEPT_DIRECTOR.getValue())){//部长
            criteria.add(Restrictions.eq(AppraiseReport_.ministerName.getName() , SessionUtil.getDisplayName()));
            criteria.add(Restrictions.eq(AppraiseReport_.approveStatus.getName() , Constant.EnumState.NO.getValue()));
            b=true;
        }else if(Constant.GENERALCONDUTOR.equals(SessionUtil.getDisplayName())){//综合部
            criteria.add(Restrictions.eq(AppraiseReport_.generalConductorName.getName() , SessionUtil.getDisplayName()));
            criteria.add(Restrictions.eq(AppraiseReport_.approveStatus.getName() , Constant.EnumState.PROCESS.getValue()));
            b=true;
        }*/
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
        List<AppraiseReport> appraiseRepoList = criteria.list();
        List<AppraiseReportDto> appraiseReportDtoList = new ArrayList<>();
        for (AppraiseReport appraiseReport : appraiseRepoList) {
            AppraiseReportDto appraiseReportDto = new AppraiseReportDto();
            BeanCopierUtils.copyProperties(appraiseReport, appraiseReportDto);
            appraiseReportDtoList.add(appraiseReportDto);
        }
        pageModelDto.setValue(appraiseReportDtoList);
        return pageModelDto;
    }

    /**
     * 通过id获取优秀评审报告的信息
     *
     * @param id
     * @return
     */
    @Override
    public AppraiseReportDto getAppraiseById(String id) {
        AppraiseReport appraiseReport = appraiseRepo.findById(AppraiseReport_.id.getName(), id);
        AppraiseReportDto appraiseReportDto = new AppraiseReportDto();
        BeanCopierUtils.copyProperties(appraiseReport, appraiseReportDto);
        return appraiseReportDto;
    }

    /**
     * 通过项目ID，初始化优秀评审报告
     *
     * @param signId
     * @return
     */
    @Override
    public AppraiseReportDto initBySignId(String signId) {
        AppraiseReportDto resultDto = new AppraiseReportDto();
        Sign sign = signRepo.findById(Sign_.signid.getName(), signId);
        resultDto.setProposerName(SessionUtil.getDisplayName());
        resultDto.setSignId(sign.getSignid());
        resultDto.setProposerTime(new Date());
        resultDto.setProjectName(sign.getProjectname());

        return resultDto;
    }

    /**
     * 发起流程
     *
     * @param id
     * @return
     */
    @Override
    public ResultMsg startFlow(String id) {
        AppraiseReport appraiseReport = appraiseRepo.findById(AppraiseReport_.id.getName(), id);
        if (appraiseReport == null) {
            return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), "发起流程失败，该项目已不存在！");
        }
        if (Validate.isString(appraiseReport.getProcessInstanceId())) {
            return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), "该项目已发起流程！");
        }
        //判断项目的主办部门
        OrgDept orgDept = orgDeptRepo.queryBySignBranchId(appraiseReport.getSignId(), FlowConstant.SignFlowParams.BRANCH_INDEX1.getValue());
        if (orgDept == null) {
            return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), "主办部门已被删除，请联系管理员进行处理！");
        }
        if (!Validate.isString(orgDept.getDirectorID())) {
            return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), "【" + orgDept.getName() + "】的部长未设置，请先设置！");
        }

        //1、启动流程
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(FlowConstant.FLOW_APPRAISE_REPORT, id,
                ActivitiUtil.setAssigneeValue(FlowConstant.FlowParams.USER.getValue(), SessionUtil.getUserId()));

        //2、设置流程实例名称
        processEngine.getRuntimeService().setProcessInstanceName(processInstance.getId(), appraiseReport.getAppraiseReportName());

        //3、更改项目状态
        appraiseReport.setProcessInstanceId(processInstance.getId());
        appraiseReport.setApproveStatus(Constant.EnumState.NO.getValue());
        appraiseRepo.save(appraiseReport);

        //4、跳过第一环节（填报）
        Task task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).active().singleResult();
        taskService.addComment(task.getId(), processInstance.getId(), "");    //添加处理信息

        //优秀评审报告申请部长和项目签收流程环节一致
        //查询部门领导

        User leadUser = userRepo.getCacheUserById(orgDept.getDirectorID());
        String assigneeValue = Validate.isString(leadUser.getTakeUserId()) ? leadUser.getTakeUserId() : leadUser.getId();
        taskService.complete(task.getId(), ActivitiUtil.setAssigneeValue(FlowConstant.FlowParams.USER_BZ.getValue(), assigneeValue));

        //放入腾讯通消息缓冲池
        RTXSendMsgPool.getInstance().sendReceiverIdPool(task.getId(), assigneeValue);
        return new ResultMsg(true, Constant.MsgCode.OK.getValue(), "操作成功！");
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
    public ResultMsg dealFlow(ProcessInstance processInstance, Task task, FlowDto flowDto) {
        String businessId = processInstance.getBusinessKey(),
                assigneeValue = "";                            //流程处理人
        Map<String, Object> variables = new HashMap<>();        //流程参数
        User dealUser = null;                                  //用户
        List<User> dealUserList = null;                        //用户列表
        AppraiseReport appraiseReport = null;                //档案借阅管理

        //环节处理人设定
        switch (task.getTaskDefinitionKey()) {
            //项目负责人填报
            case FlowConstant.FLOW_ARP_FZR:
                appraiseReport = appraiseRepo.findById(AppraiseReport_.id.getName(), businessId);
                OrgDept orgDept = orgDeptRepo.queryBySignBranchId(appraiseReport.getSignId(), FlowConstant.SignFlowParams.BRANCH_INDEX1.getValue());
                if (orgDept == null) {
                    return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), "主办部门已被删除，请联系管理员进行处理！");
                }
                if (!Validate.isString(orgDept.getDirectorID())) {
                    return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), "【" + orgDept.getName() + "】的部长未设置，请先设置！");
                }
                User leadUser = userRepo.getCacheUserById(orgDept.getDirectorID());
                assigneeValue = Validate.isString(leadUser.getTakeUserId()) ? leadUser.getTakeUserId() : leadUser.getId();
                break;
            //部长审批
            case FlowConstant.FLOW_ARP_BZ_SP:
                if (flowDto.getBusinessMap().get("AGREE") == null || !Validate.isString(flowDto.getBusinessMap().get("AGREE").toString())) {
                    return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), "请选择同意或者不同意！");
                }
                appraiseReport = appraiseRepo.findById(AppraiseReport_.id.getName(), businessId);
                //同意
                if (Constant.EnumState.YES.getValue().equals(flowDto.getBusinessMap().get("AGREE").toString())) {
                    appraiseReport.setMinisterId(SessionUtil.getUserId());
                    appraiseReport.setMinisterName(SessionUtil.getDisplayName());
                    appraiseReport.setMinisterOpinion(flowDto.getDealOption());
                    appraiseReport.setMinisterDate(new Date());

                    //获取综合部档案评审员用户
                    dealUserList = userRepo.findUserByRoleName(Constant.EnumFlowNodeGroupName.APPRAISE_REVIEWER.getValue());
                    if (!Validate.isList(dealUserList)) {
                        return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), "请先设置【" + Constant.EnumFlowNodeGroupName.APPRAISE_REVIEWER.getValue() + "】角色用户！");
                    }
                    dealUser = dealUserList.get(0);
                    assigneeValue = Validate.isString(dealUser.getTakeUserId()) ? dealUser.getTakeUserId() : dealUser.getId();
                    variables.put(FlowConstant.FlowParams.USER_ZHB.getValue(), assigneeValue);
                    variables.put(FlowConstant.FlowParams.ISAGREE.getValue(), true);
                    //如果不同意，则直接结束
                } else {
                    Sign sign = signRepo.findById(Sign_.signid.getName(), appraiseReport.getSignId());
                    sign.setIsAppraising(Constant.EnumState.NO.getValue());
                    signRepo.save(sign);
                    appraiseReport.setApproveStatus(Constant.EnumState.NO.getValue());
                    variables.put(FlowConstant.FlowParams.ISAGREE.getValue(), false);
                }
                appraiseRepo.save(appraiseReport);
                break;
            //综合部负责人审批
            case FlowConstant.FLOW_ARP_ZHB_SP:
                if (flowDto.getBusinessMap().get("AGREE") == null || !Validate.isString(flowDto.getBusinessMap().get("AGREE").toString())) {
                    return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), "请选择同意或者不同意！");
                }
                appraiseReport = appraiseRepo.findById(AppraiseReport_.id.getName(), businessId);
                Sign sign = signRepo.findById(Sign_.signid.getName(), appraiseReport.getSignId());
                if (Constant.EnumState.YES.getValue().equals(flowDto.getBusinessMap().get("AGREE").toString())) {
                    sign.setIsAppraising(Constant.EnumState.YES.getValue());
                    appraiseReport.setApproveStatus(Constant.EnumState.YES.getValue());
                } else {
                    appraiseReport.setApproveStatus(Constant.EnumState.NO.getValue());
                    sign.setIsAppraising(Constant.EnumState.NO.getValue());
                }
                signRepo.save(sign);
                appraiseRepo.save(appraiseReport);
                break;
            default:
                break;
        }
        taskService.addComment(task.getId(), processInstance.getId(), (flowDto == null) ? "" : flowDto.getDealOption());    //添加处理信息
        if (flowDto.isEnd()) {
            taskService.complete(task.getId());
        } else {
            taskService.complete(task.getId(), variables);
        }
        //放入腾讯通消息缓冲池
        RTXSendMsgPool.getInstance().sendReceiverIdPool(task.getId(), assigneeValue);
        return new ResultMsg(true, Constant.MsgCode.OK.getValue(), "操作成功！");
    }
}