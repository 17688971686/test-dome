package cs.service.project;

import cs.common.Constant;
import cs.common.Constant.EnumState;
import cs.common.FlowConstant;
import cs.common.HqlBuilder;
import cs.common.ResultMsg;
import cs.common.utils.*;
import cs.domain.monthly.MonthlyNewsletter;
import cs.domain.monthly.MonthlyNewsletter_;
import cs.domain.project.*;
import cs.domain.sys.OrgDept;
import cs.domain.sys.User;
import cs.model.PageModelDto;
import cs.model.flow.FlowDto;
import cs.model.monthly.MonthlyNewsletterDto;
import cs.model.project.AddSuppLetterDto;
import cs.repository.odata.ODataFilterItem;
import cs.repository.odata.ODataObj;
import cs.repository.odata.ODataObjFilterStrategy;
import cs.repository.repositoryImpl.monthly.MonthlyNewsletterRepo;
import cs.repository.repositoryImpl.project.AddSuppLetterRepo;
import cs.repository.repositoryImpl.project.SignBranchRepo;
import cs.repository.repositoryImpl.project.SignRepo;
import cs.repository.repositoryImpl.project.WorkProgramRepo;
import cs.repository.repositoryImpl.sys.OrgDeptRepo;
import cs.repository.repositoryImpl.sys.UserRepo;
import cs.service.rtx.RTXSendMsgPool;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;


/**
 * Description: 项目资料补充函 业务操作实现类
 * author: ldm
 * Date: 2017-8-1 18:05:57
 */
@Service
public class AddSuppLetterServiceImpl implements AddSuppLetterService {

    @Autowired
    private AddSuppLetterRepo addSuppLetterRepo;
    @Autowired
    private SignRepo signRepo;
    @Autowired
    private WorkProgramRepo workProgramRepo;
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
    private MonthlyNewsletterRepo monthlyNewsletterRepo;

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
     * 获取最大拟稿编号
     *
     * @param dispaDate
     * @return
     */
    private int findCurMaxSeq(Date dispaDate) {
        HqlBuilder sqlBuilder = HqlBuilder.create();
        sqlBuilder.append("select max(" + AddSuppLetter_.fileSeq.getName() + ") from cs_add_suppLetter where " + AddSuppLetter_.disapDate.getName() + " between ");
        sqlBuilder.append(" to_date(:beginTime,'yyyy-mm-dd hh24:mi:ss') and to_date(:endTime,'yyyy-mm-dd hh24:mi:ss' )");
        sqlBuilder.setParam("beginTime", DateUtils.converToString(dispaDate, "yyyy") + "-01-01 00:00:00");
        sqlBuilder.setParam("endTime", DateUtils.converToString(dispaDate, "yyyy") + "-12-31 23:59:59");
        return addSuppLetterRepo.returnIntBySql(sqlBuilder);
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
     * 生成文件字号
     */
    @Override
    @Transactional
    public ResultMsg fileNum(String id) {
        AddSuppLetter addSuppLetter = addSuppLetterRepo.findById(AddSuppLetter_.id.getName(), id);
        if (Validate.isString(addSuppLetter.getFilenum())) {
            return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), "该补充资料函已经生成过发文字号，不能重复生成！");
        }
        //获取拟稿最大编号
        int curYearMaxSeq = findCurMaxSeq(addSuppLetter.getSuppLetterTime());
        String filenum = Constant.DISPATCH_PREFIX + "[" + DateUtils.converToString(addSuppLetter.getSuppLetterTime(), "yyyy") + "]" + (curYearMaxSeq + 1);
        addSuppLetter.setFilenum(filenum);
        addSuppLetter.setFileSeq((curYearMaxSeq + 1));
        addSuppLetterRepo.save(addSuppLetter);

        //如果是收文，则要更新对应的资料信息(如果生成了文件字号，工作方案的是否补充资料函则显示为是，并且显示最新的日期。如果没有，则显示为否)
        if (Constant.BusinessType.SIGN.getValue().equals(addSuppLetter.getBusinessType())) {
            Date now = new Date();
            Sign sign = signRepo.findById(Sign_.signid.getName(), addSuppLetter.getBusinessId());
            if (!Validate.isString(sign.getIsHaveSuppLetter()) || Constant.EnumState.NO.getValue().equals(sign.getIsHaveSuppLetter())) {
                sign.setIsHaveSuppLetter(Constant.EnumState.YES.getValue());
                sign.setSuppLetterDate(now);
                signRepo.save(sign);
            }
            List<WorkProgram> wpList = workProgramRepo.findByIds(Sign_.signid.getName(), addSuppLetter.getBusinessId(), null);
            if (Validate.isList(wpList)) {
                List<WorkProgram> saveList = new ArrayList<>();
                for (WorkProgram wp : wpList) {
                    if (!Validate.isString(wp.getIsHaveSuppLetter()) || Constant.EnumState.NO.getValue().equals(wp.getIsHaveSuppLetter())) {
                        wp.setIsHaveSuppLetter(Constant.EnumState.YES.getValue());
                        wp.setSuppLetterDate(now);
                        saveList.add(wp);
                    }
                }
                workProgramRepo.bathUpdate(saveList);
            }
        }
        return new ResultMsg(true, Constant.MsgCode.OK.getValue(), "操作成功！", addSuppLetter);
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
        addSuppLetterRepo.save(addSuppLetter);
        return new ResultMsg(true, Constant.MsgCode.OK.getValue(), "操作成功！", addSuppLetter);
    }

    /**
     * 获取月报简报
     */
    @Override
    public PageModelDto<AddSuppLetterDto> monthlyMultiyearListData(ODataObj odataObj) {
        Criteria criteria = addSuppLetterRepo.getExecutableCriteria();
        if (Validate.isList(odataObj.getFilter())) {
            Object value;
            for (ODataFilterItem item : odataObj.getFilter()) {
                value = item.getValue();
                if (null == value) {
                    continue;
                }
                //如果是月报简报年度列表，则不分页
                if("monthLetterYearName".equals(item.getField())){
                    //不统计分页
                    odataObj.setCount(false);
                    criteria.add(Restrictions.gt(AddSuppLetter_.createdDate.getName(),DateUtils.converToDate(value.toString()+"-01-01 00:00:00","yyyy-MM-dd HH:mm:ss")));
                    criteria.add(Restrictions.lt(AddSuppLetter_.createdDate.getName(),DateUtils.converToDate(value.toString()+"-12-31 24:00:00","yyyy-MM-dd HH:mm:ss")));
                    continue;
                }
                criteria.add(ODataObjFilterStrategy.getStrategy(item.getOperator()).getCriterion(item.getField(),value));
            }
        }
        //如果统计分页
        if(odataObj.isCount()){
            Integer totalResult = ((Number) criteria.setProjection(Projections.rowCount()).uniqueResult()).intValue();
            criteria.setProjection(null);
            odataObj.setCount(totalResult);
            if (odataObj.getTop() != 0) {
                criteria.setFirstResult(odataObj.getSkip());
                criteria.setMaxResults(odataObj.getTop());
            }
        }
        List<AddSuppLetter> suppLetterList = criteria.list();
        List<AddSuppLetterDto> suppLetterDtoList = new ArrayList<AddSuppLetterDto>();
        if (Validate.isList(suppLetterList)) {
            suppLetterList.forEach(x -> {
                AddSuppLetterDto addDto = new AddSuppLetterDto();
                BeanCopierUtils.copyProperties(x, addDto);
                suppLetterDtoList.add(addDto);
            });
        }

        PageModelDto<AddSuppLetterDto> pageModelDto = new PageModelDto<AddSuppLetterDto>();
        pageModelDto.setValue(suppLetterDtoList);
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
        this.delete(id);
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

        Criteria criteria = addSuppLetterRepo.getExecutableCriteria();
        criteria = odataObj.buildFilterToCriteria(criteria);
        //文件类型，1表示拟补充资料函
        criteria.add(Restrictions.eq(AddSuppLetter_.fileType.getName(), EnumState.PROCESS.getValue()));
        criteria.add(Restrictions.eq(AddSuppLetter_.appoveStatus.getName(), EnumState.YES.getValue()));

        Integer totalResult = ((Number) criteria.setProjection(Projections.rowCount()).uniqueResult()).intValue();
        pageModelDto.setCount(totalResult);
        criteria.setProjection(null);
        if (odataObj.getSkip() > 0) {
            criteria.setFirstResult(odataObj.getTop());
        }
        if (odataObj.getTop() > 0) {
            criteria.setMaxResults(odataObj.getTop());
        }
        List<AddSuppLetter> allist = criteria.list();
        List<AddSuppLetterDto> alDtos = new ArrayList<AddSuppLetterDto>(allist == null ? 0 : allist.size());

        if (allist != null && allist.size() > 0) {
            allist.forEach(x -> {
                AddSuppLetterDto alDto = new AddSuppLetterDto();
                BeanCopierUtils.copyProperties(x, alDto);
                alDtos.add(alDto);
            });
        }
        pageModelDto.setValue(alDtos);

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

        User leadUser = userRepo.getCacheUserById(orgDept.getDirectorID());
        String assigneeValue = Validate.isString(leadUser.getTakeUserId()) ? leadUser.getTakeUserId() : leadUser.getId();
        taskService.complete(task.getId(), ActivitiUtil.setAssigneeValue(FlowConstant.FlowParams.USER_BZ.getValue(), assigneeValue));

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
        ResultMsg rturnReuslt = null;
        String businessId = processInstance.getBusinessKey(),
                assigneeValue = "";                            //流程处理人
        Map<String, Object> variables = new HashMap<>();       //流程参数
        User dealUser = null;                                  //用户
        List<User> dealUserList = null;                        //用户列表
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
                dealUser = userRepo.getCacheUserById(orgDept.getDirectorID());
                assigneeValue = Validate.isString(dealUser.getTakeUserId()) ? dealUser.getTakeUserId() : dealUser.getId();

                addSuppLetter.setAppoveStatus(Constant.EnumState.NO.getValue());
                addSuppLetterRepo.save(addSuppLetter);
                break;
            //部长审批
            case FlowConstant.FLOW_SPL_BZ_SP:
                //没有分支，则直接跳转到分管领导环节
                if (signBranchRepo.allAssistCount(addSuppLetter.getBusinessId()) == 0) {
                    dealUser = signBranchRepo.findMainSLeader(addSuppLetter.getBusinessId());
                    assigneeValue = Validate.isString(dealUser.getTakeUserId()) ? dealUser.getTakeUserId() : dealUser.getId();
                    variables.put(FlowConstant.FlowParams.USER_FGLD.getValue(), assigneeValue);  //设置分管领导
                    variables.put(FlowConstant.FlowParams.FGLD_FZ.getValue(), true);             //设置分支条件
                    //2表示到分管领导会签
                    addSuppLetter.setAppoveStatus(EnumState.STOP.getValue());

                    //有分支，则跳转到领导会签环节
                } else {
                    dealUserList = signBranchRepo.findAssistOrgDirector(addSuppLetter.getBusinessId());
                    assigneeValue = buildUser(dealUserList);
                    variables.put(FlowConstant.SignFlowParams.USER_HQ_LIST.getValue(), StringUtil.getSplit(assigneeValue, ","));
                    variables.put(FlowConstant.FlowParams.FGLD_FZ.getValue(), false);            //设置分支条件
                    //1表示到领导会签
                    addSuppLetter.setAppoveStatus(EnumState.PROCESS.getValue());
                }
                addSuppLetter.setDeptMinisterId(SessionUtil.getUserId());
                addSuppLetter.setDeptMinisterName(SessionUtil.getDisplayName());
                addSuppLetter.setDeptMinisterDate(new Date());
                addSuppLetter.setDeptMinisterIdeaContent(flowDto.getDealOption());

                addSuppLetterRepo.save(addSuppLetter);
                break;
            //领导会签
            case FlowConstant.FLOW_SPL_LD_HQ:
                dealUser = signBranchRepo.findMainSLeader(addSuppLetter.getBusinessId());
                assigneeValue = Validate.isString(dealUser.getTakeUserId()) ? dealUser.getTakeUserId() : dealUser.getId();
                variables.put(FlowConstant.FlowParams.USER_FGLD.getValue(), assigneeValue);

                String signString = "";
                //旧的会签记录
                String oldMsg = addSuppLetter.getLeaderSignIdeaContent();
                if (Validate.isString(oldMsg) && !"null".equals(oldMsg)) {
                    signString += oldMsg+"<br>";
                }
                signString += flowDto.getDealOption() + "<br>" + SessionUtil.getDisplayName() + "  " + DateUtils.converToString(new Date(), null);
                addSuppLetter.setLeaderSignIdeaContent(signString);
                //2表示到分管领导会签
                addSuppLetterRepo.save(addSuppLetter);
                break;

            //分管领导审批
            case FlowConstant.FLOW_SPL_FGLD_SP:
                //生成发文字号失败，则
                   rturnReuslt = fileNum(addSuppLetter.getId());
                if (!rturnReuslt.isFlag()) {
                    return rturnReuslt;
                }
                addSuppLetter.setDeptSLeaderId(SessionUtil.getUserId());
                addSuppLetter.setDeptSLeaderName(SessionUtil.getDisplayName());
                addSuppLetter.setDeptSleaderDate(new Date());
                addSuppLetter.setDeptSLeaderIdeaContent(flowDto.getDealOption());
                addSuppLetter.setAppoveStatus(EnumState.YES.getValue());
                addSuppLetterRepo.save(addSuppLetter);

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

    private String buildUser(List<User> userList) {
        StringBuffer assigneeValue = new StringBuffer();
        for (int i = 0, l = userList.size(); i < l; i++) {
            if (i > 0) {
                assigneeValue.append(",");
            }
            assigneeValue.append(Validate.isString(userList.get(i).getTakeUserId()) ? userList.get(i).getTakeUserId() : userList.get(i).getId());
        }
        return assigneeValue.toString();
    }
}