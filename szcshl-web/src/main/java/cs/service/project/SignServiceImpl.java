package cs.service.project;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;

import cs.common.Constant;
import cs.common.Constant.EnumFlowNodeGroupName;
import cs.common.Constant.EnumState;
import cs.common.Constant.MsgCode;
import cs.common.HqlBuilder;
import cs.common.ResultMsg;
import cs.common.utils.ActivitiUtil;
import cs.common.utils.BeanCopierUtils;
import cs.common.utils.DateUtils;
import cs.common.utils.SessionUtil;
import cs.common.utils.StringUtil;
import cs.common.utils.Validate;
import cs.domain.external.Dept;
import cs.domain.project.AssistPlanSign;
import cs.domain.project.AssistPlanSign_;
import cs.domain.project.DispatchDoc;
import cs.domain.project.DispatchDoc_;
import cs.domain.project.FileRecord;
import cs.domain.project.FileRecord_;
import cs.domain.project.ProjectStop;
import cs.domain.project.Sign;
import cs.domain.project.SignBranch;
import cs.domain.project.SignDispaWork;
import cs.domain.project.SignDispaWork_;
import cs.domain.project.SignMerge;
import cs.domain.project.SignMerge_;
import cs.domain.project.SignPrincipal;
import cs.domain.project.Sign_;
import cs.domain.project.WorkProgram;
import cs.domain.sys.Company;
import cs.domain.sys.Org;
import cs.domain.sys.OrgDept;
import cs.domain.sys.SysFile;
import cs.domain.sys.SysFile_;
import cs.domain.sys.User;
import cs.domain.sys.User_;
import cs.model.PageModelDto;
import cs.model.external.DeptDto;
import cs.model.external.OfficeUserDto;
import cs.model.flow.FlowDto;
import cs.model.project.DispatchDocDto;
import cs.model.project.FileRecordDto;
import cs.model.project.ProjectStopDto;
import cs.model.project.SignDto;
import cs.model.project.WorkProgramDto;
import cs.model.sys.OrgDto;
import cs.model.sys.UserDto;
import cs.repository.odata.ODataObj;
import cs.repository.repositoryImpl.external.DeptRepo;
import cs.repository.repositoryImpl.project.DispatchDocRepo;
import cs.repository.repositoryImpl.project.FileRecordRepo;
import cs.repository.repositoryImpl.project.ProjectStopRepo;
import cs.repository.repositoryImpl.project.SignBranchRepo;
import cs.repository.repositoryImpl.project.SignDispaWorkRepo;
import cs.repository.repositoryImpl.project.SignMergeRepo;
import cs.repository.repositoryImpl.project.SignPrincipalRepo;
import cs.repository.repositoryImpl.project.SignRepo;
import cs.repository.repositoryImpl.project.WorkProgramRepo;
import cs.repository.repositoryImpl.sys.CompanyRepo;
import cs.repository.repositoryImpl.sys.OrgDeptRepo;
import cs.repository.repositoryImpl.sys.OrgRepo;
import cs.repository.repositoryImpl.sys.SysFileRepo;
import cs.repository.repositoryImpl.sys.UserRepo;
import cs.service.external.OfficeUserService;
import cs.service.flow.FlowService;

@Service
public class SignServiceImpl implements SignService {
    private static Logger log = Logger.getLogger(SignServiceImpl.class);
    @Autowired
    private SignRepo signRepo;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private OrgRepo orgRepo;
    @Autowired
    private WorkProgramRepo workProgramRepo;
    @Autowired
    private DispatchDocRepo dispatchDocRepo;
    @Autowired
    private FileRecordRepo fileRecordRepo;
    //flow service
    @Autowired
    private TaskService taskService;
    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private DeptRepo deptRepo;
    @Autowired
    private ProcessEngine processEngine;
    @Autowired
    private OfficeUserService officeUserService;
    @Autowired
    private CompanyRepo companyRepo;
    @Autowired
    private SysFileRepo sysFileRepo;
    @Autowired
    private AssistPlanSignService assistPlanSignService;
    @Autowired
    private SignPrincipalRepo signPrincipalRepo;
    @Autowired
    private SignPrincipalService signPrincipalService;
    @Autowired
    private SignDispaWorkRepo signDispaWorkRepo;
    @Autowired
    private ProjectStopRepo projectStopRepo;
    @Autowired
    private FlowService flowService;
    @Autowired
    private SignBranchRepo signBranchRepo;
    @Autowired
    private SignMergeRepo signMergeRepo;
    @Autowired
    private WorkProgramService workProgramService;
    //部门（小组）列表
    @Autowired
    private OrgDeptRepo orgDeptRepo;

    /**
     * 项目签收保存操作（这里的方法是正式签收）
     *
     * @param signDto
     */
    @Override
    @Transactional
    public ResultMsg createSign(SignDto signDto) {
        if(!Validate.isString(signDto.getFilecode())){
            return new ResultMsg(false,MsgCode.ERROR.getValue(),"保存失败，没有收文编号！");
        }
        Date now = new Date();
        Sign sign = null;
        //如果收文编号以0000结束，说明委里没有收文编号，这个编号可以有多个
        if(!signDto.getFilecode().endsWith("0000")){
            signRepo.findByFilecode(signDto.getFilecode());
        }
        //1、根据收文编号获取项目信息
        if(sign == null){
            sign = new Sign();
            BeanCopierUtils.copyProperties(signDto,sign);
            sign.setSignid(UUID.randomUUID().toString());
            sign.setSignState(EnumState.NORMAL.getValue());
            sign.setSigndate(now);
            sign.setIsLightUp(Constant.signEnumState.NOLIGHT.getValue());
            //2、是否是项目概算流程
            if (Constant.ProjectStage.STAGE_BUDGET.getValue().equals(sign.getReviewstage()) || Validate.isString(sign.getIschangeEstimate())) {
                sign.setIsassistflow(EnumState.YES.getValue());
            } else {
                sign.setIsassistflow(EnumState.NO.getValue());
            }
            //3、送件人为当前签收人，
            sign.setSendusersign(SessionUtil.getDisplayName());

            //4、默认办理部门（项目建议书、可研为PX，概算为GX，其他为评估）
            //综合部、分管副主任默认办理信息
            List<User> roleList = userRepo.findUserByRoleName(EnumFlowNodeGroupName.VICE_DIRECTOR.getValue());
            if(Constant.ProjectStage.STAGE_BUDGET.getValue().equals(sign.getReviewstage())){
                sign.setDealOrgType(Constant.SignBusinessType.GX.getValue());
                sign.setLeaderhandlesug("请（概算一部         概算二部）组织评审。");
            }else{
                sign.setDealOrgType(Constant.SignBusinessType.PX.getValue());
                sign.setLeaderhandlesug("请（评估一部         评估二部         综合部）组织评审。");
            }
            for(User user :roleList ){
                if(sign.getDealOrgType().equals(user.getMngOrgType())){
                    sign.setLeaderId(user.getId());
                    sign.setLeaderName(user.getDisplayName());
                    sign.setComprehensivehandlesug("请"+(user.getDisplayName()).substring(0,1)+"主任阅示。");
                    sign.setComprehensiveName("综合部");
                    sign.setComprehensiveDate(new Date());
                    break;
                }
            }
            //5、创建人信息
            sign.setCreatedDate(now);
            sign.setModifiedDate(now);
            sign.setCreatedBy(SessionUtil.getDisplayName());
            sign.setModifiedBy(SessionUtil.getDisplayName());
        }else{
            BeanCopierUtils.copyPropertiesIgnoreNull(signDto,sign);
            sign.setModifiedDate(now);
            sign.setModifiedBy(SessionUtil.getDisplayName());
        }
        //6、收文编号
        if(!Validate.isString(sign.getSignNum())){
            int maxSeq = findSignMaxSeqByType(sign.getDealOrgType());
            sign.setSignSeq(maxSeq+1);
            String signSeqString = (maxSeq+1)>999?(maxSeq+1)+"":String.format("%03d", Integer.valueOf(maxSeq+1));
            sign.setSignNum(DateUtils.converToString(new Date(),"yyyy")+sign.getDealOrgType()+signSeqString);
        }
        //7、正式签收
        if (Validate.isString(sign.getIssign()) || !EnumState.YES.getValue().equals(sign.getIssign())) {
            sign.setSigndate(now);
            sign.setIssign(EnumState.YES.getValue());       //正式签收
            boolean is15Days = (Validate.isString(sign.getReviewstage()) &&
                    (Constant.ProjectStage.STAGE_STUDY.getValue().equals(sign.getReviewstage())
                            || Constant.ProjectStage.STAGE_BUDGET.getValue().equals(sign.getReviewstage())));
            if (is15Days) {
                sign.setSurplusdays(Constant.WORK_DAY_15);
            } else {
                sign.setSurplusdays(Constant.WORK_DAY_12);
            }
        }

        signRepo.save(sign);
        return new ResultMsg(true, MsgCode.OK.getValue(), "操作成功！", sign);
    }

    @Override
    public PageModelDto<SignDto> get(ODataObj odataObj) {
        PageModelDto<SignDto> pageModelDto = new PageModelDto<SignDto>();
        List<Sign> signList = signRepo.findByOdata(odataObj);
        if (Validate.isList(signList)) {
            List<SignDto> signDtoList = new ArrayList<>(signList.size());
            signList.forEach(x -> {
                SignDto signDto = new SignDto();
                BeanCopierUtils.copyProperties(x, signDto);
                signDtoList.add(signDto);
            });
            pageModelDto.setValue(signDtoList);
        }else{
            pageModelDto.setValue(null);
        }
        pageModelDto.setCount(odataObj.getCount());

        return pageModelDto;
    }

    @Override
    @Transactional
    public void updateSign(SignDto signDto) {
        Sign sign = signRepo.findById(signDto.getSignid());
        BeanCopierUtils.copyPropertiesIgnoreNull(signDto, sign);

        sign.setModifiedBy(SessionUtil.getUserId());
        sign.setModifiedDate(new Date());
        signRepo.save(sign);
    }

    @Override
    public ResultMsg initFillPageData(String signId) {
        Map<String, Object> map = new HashMap<String, Object>();

        //1收文对象
        Sign sign = signRepo.findById(signId);
        SignDto signDto = new SignDto();
        BeanCopierUtils.copyProperties(sign, signDto);
        map.put("sign", signDto);

        //获取办事处所有信息
        List<DeptDto> deptDtolist = new ArrayList<DeptDto>();
        List<Dept> deptlist = deptRepo.findAll();
        if (deptlist != null) {
            deptlist.forEach(o -> {
                DeptDto deptDto = new DeptDto();
                BeanCopierUtils.copyProperties(o, deptDto);
                deptDtolist.add(deptDto);
            });
            map.put("deptlist", deptDtolist);
        }
        //主办事处
        if (Validate.isString(sign.getMaindepetid())) {
            List<OfficeUserDto> officeList = officeUserService.findOfficeUserByDeptId(sign.getMaindepetid());
            map.put("mainOfficeList", officeList);
        }
        //协办事处
        if (Validate.isString(sign.getAssistdeptid())) {
            List<OfficeUserDto> officeList = officeUserService.findOfficeUserByDeptId(sign.getAssistdeptid());
            map.put("assistOfficeList", officeList);
        }
        //单位
        List<Company> companyList = companyRepo.findAll();
        map.put("companyList", companyList);

        //查询系统上传文件
        Criteria file = sysFileRepo.getExecutableCriteria();
        file.add(Restrictions.eq(SysFile_.businessId.getName(), sign.getSignid()));
        List<SysFile> sysFiles = file.list();
        if (sysFiles != null) {
            map.put("sysFiles", sysFiles);
        }
        List<UserDto> leaderList = new ArrayList<>();
        List<User> roleList = userRepo.findUserByRoleName(EnumFlowNodeGroupName.VICE_DIRECTOR.getValue());
        //获取分管领导信息
        if (Validate.isList(roleList)) {
            roleList.forEach(rl -> {
                UserDto ud = new UserDto();
                BeanCopierUtils.copyProperties(rl, ud);
                leaderList.add(ud);
            });
            map.put("leaderList", leaderList);
        }
        return new ResultMsg(true, MsgCode.OK.getValue(), "查询数据成功", map);
    }

    @Override
    @Transactional
    public List<OrgDto> selectSign(ODataObj odataObj) {
        List<Org> org = orgRepo.findByOdata(odataObj);
        List<OrgDto> orgDto = new ArrayList<OrgDto>();
        if (org != null && org.size() > 0) {
            org.forEach(x -> {
                OrgDto orgDtos = new OrgDto();
                BeanCopierUtils.copyProperties(x, orgDtos);
                orgDtos.setCreatedDate(x.getCreatedDate());
                orgDtos.setModifiedDate(x.getModifiedDate());
                orgDto.add(orgDtos);
            });
        }
        return orgDto;
    }

    /**
     * 删除项目信息
     *
     * @param signid
     * @return
     */
    @Override
    @Transactional
    public ResultMsg deleteSign(String signid) {
        Sign sign = signRepo.findById(Sign_.signid.getName(), signid);
        if (Validate.isString(sign.getProcessInstanceId())) {
            return new ResultMsg(false, MsgCode.ERROR.getValue(), "该项目已经发起流程，不能删除！");
        }
        if (sign.getSigndate() != null || EnumState.YES.getValue().equals(sign.getIssign())) {
            return new ResultMsg(false, MsgCode.ERROR.getValue(), "该项目已经正式签收，不能删除！");
        }
        signRepo.deleteById(Sign_.signid.getName(), signid);
        return new ResultMsg(true, MsgCode.OK.getValue(), "删除成功！");
    }

    /**
     * 项目暂停
     *
     * @param signid
     */
    @Override
    public ResultMsg stopFlow(String signid, ProjectStopDto projectStopDto) {
        Sign sign = signRepo.findById(Sign_.signid.getName(), signid);
        if (sign == null) {
            return new ResultMsg(false, MsgCode.ERROR.getValue(), "操作失败，该项目数据已被删除！");
        } else if (Constant.EnumState.STOP.getValue().equals(sign.getSignState())) {
            return new ResultMsg(false, MsgCode.ERROR.getValue(), "操作失败，该项目目前还是暂停状态！");
        }
        sign.setSignState(Constant.EnumState.STOP.getValue());

        //暂停流程
        ProcessInstance processInstance = flowService.findProcessInstanceByBusinessKey(signid);
        runtimeService.suspendProcessInstanceById(processInstance.getId());

        Date now = new Date();
        ProjectStop projectStop = new ProjectStop();
        if (projectStopDto != null) {
            BeanCopierUtils.copyProperties(projectStopDto, projectStop);
        }
        projectStop.setStopid(UUID.randomUUID().toString());
        projectStop.setPausetime(now);
        projectStop.setCreatedDate(now);
        projectStop.setModifiedDate(now);
        projectStop.setIsactive(EnumState.YES.getValue());
        projectStop.setModifiedBy(SessionUtil.getLoginName());
        projectStop.setCreatedBy(SessionUtil.getLoginName());
        projectStop.setSign(sign);
        projectStopRepo.save(projectStop);

        return new ResultMsg(true, MsgCode.OK.getValue(), "操作成功！");
    }

    /**
     * 流程激活
     *
     * @param signid
     * @return
     */
    @Override
    public ResultMsg restartFlow(String signid) {
        Sign sign = signRepo.findById(Sign_.signid.getName(), signid);
        if (sign == null) {
            return new ResultMsg(false, MsgCode.ERROR.getValue(), "操作失败，该项目数据已被删除！");
        }
        sign.setSignState(EnumState.PROCESS.getValue());

        //激活流程
        ProcessInstance processInstance = flowService.findProcessInstanceByBusinessKey(signid);
        runtimeService.activateProcessInstanceById(processInstance.getId());

        //获取已暂停项目
        ProjectStop projectStop = projectStopRepo.findProjectStop(signid);
        if (projectStop == null) {
            return new ResultMsg(false, MsgCode.ERROR.getValue(), "操作失败，暂停记录已被删除！");
        }
        Date now = new Date();
        projectStop.setStartTime(now);
        long longtime = DateUtils.daysBetween(now, projectStop.getPausetime());
        projectStop.setPausedays((float) longtime);
        projectStop.setIsactive(EnumState.NO.getValue());
        projectStop.setSign(sign);
        projectStopRepo.save(projectStop);

        return new ResultMsg(true, MsgCode.OK.getValue(), "操作成功！");
    }

    @Override
    public SignDto findById(String signid, boolean queryAll) {
        Sign sign = signRepo.findById(signid);
        SignDto signDto = new SignDto();
        BeanCopierUtils.copyProperties(sign, signDto);
        //查询所有的属性
        if (queryAll) {
            if (sign.getWorkProgramList() != null && sign.getWorkProgramList().size() > 0) {
                List<WorkProgramDto> workProgramDtoList = new ArrayList<>(sign.getWorkProgramList().size());
                sign.getWorkProgramList().forEach(workProgram -> {
                    WorkProgramDto workProgramDto = new WorkProgramDto();
                    workProgramService.initWorkProgramDto(workProgram, workProgramDto);
                    workProgramDtoList.add(workProgramDto);
                });
                signDto.setWorkProgramDtoList(workProgramDtoList);
            }

            if (sign.getDispatchDoc() != null && Validate.isString(sign.getDispatchDoc().getId())) {
                DispatchDocDto dispatchDocDto = new DispatchDocDto();
                BeanCopierUtils.copyProperties(sign.getDispatchDoc(), dispatchDocDto);
                signDto.setDispatchDocDto(dispatchDocDto);
                //如果评审阶段是可研和概算的，才关联到前一阶段
                String reviewStage = sign.getReviewstage();
                if (reviewStage != null && (reviewStage.equals("可行性研究报告") || reviewStage.equals("项目概算")) && sign.getAssociateSign() != null) {
                    List<Sign> associateSigns = getAssociates(sign.getAssociateSign().getSignid());
                    if (associateSigns != null && associateSigns.size() > 0) {
                        List<DispatchDocDto> associateDispatchDtos = new ArrayList<DispatchDocDto>(associateSigns.size());
                        associateSigns.forEach(associateSign -> {
                            Sign asSign = signRepo.getById(associateSign.getSignid());
                            DispatchDoc associateDispatch = asSign.getDispatchDoc();
                            if (associateDispatch != null && associateDispatch.getId() != null) {
                                //关联发文
                                DispatchDocDto associateDis = new DispatchDocDto();
                                BeanCopierUtils.copyProperties(associateDispatch, associateDis);
                                SignDto copyDto = new SignDto();
                                copyDto.setReviewstage(asSign.getReviewstage());
                                associateDis.setSignDto(copyDto);
                                associateDispatchDtos.add(associateDis);
                            }
                        });
                        if (Validate.isList(associateDispatchDtos)) {
                            dispatchDocDto.setAssociateDispatchs(associateDispatchDtos);
                        }
                    }
                }
            }

            if (sign.getFileRecord() != null && Validate.isString(sign.getFileRecord().getFileRecordId())) {
                FileRecordDto fileRecordDto = new FileRecordDto();
                BeanCopierUtils.copyProperties(sign.getFileRecord(), fileRecordDto);
                signDto.setFileRecordDto(fileRecordDto);
            }
            //如果是协审项目，还要查询项目协审方案信息
            if (EnumState.YES.getValue().equals(sign.getIsassistproc())) {
                signDto.setPlanSignDtoList(assistPlanSignService.findBySignId(sign.getSignid()));
            }
        }
        return signDto;
    }

    /**
     * 删除流程
     *
     * @param signid
     * @return
     */
    @Override
    public ResultMsg endFlow(String signid) {
        if (signRepo.updateSignState(signid, EnumState.FORCE.getValue())) {
            return new ResultMsg(true, MsgCode.OK.getValue(), "操作成功！");
        }
        return new ResultMsg(false, MsgCode.ERROR.getValue(), "操作失败！");
    }

    /************************************** S  新流程项目处理   *********************************************/
    /**
     * 发起流程
     *
     * @param signid
     * @return
     */
    @Override
    @Transactional
    public ResultMsg startNewFlow(String signid) {
        Sign sign = signRepo.findById(Sign_.signid.getName(), signid);
        if (sign == null) {
            return new ResultMsg(false, MsgCode.ERROR.getValue(), "发起流程失败，该项目已不存在！");
        }
        if (Validate.isString(sign.getProcessInstanceId())) {
            return new ResultMsg(false, MsgCode.ERROR.getValue(), "该项目已发起流程！");
        }
        if (!Validate.isString(sign.getLeaderId())) {
            return new ResultMsg(false, MsgCode.ERROR.getValue(), "操作失败，请先设置默认办理部门！");
        }
        //1、启动流程
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(Constant.SIGN_FLOW, signid, ActivitiUtil.setAssigneeValue(Constant.FlowUserName.USER_ZR.getValue(), SessionUtil.getUserId()));

        //2、设置流程实例名称
        processEngine.getRuntimeService().setProcessInstanceName(processInstance.getId(), sign.getProjectname());

        //3、更改项目状态
        sign.setProcessInstanceId(processInstance.getId());
        sign.setSignState(EnumState.PROCESS.getValue());
        sign.setProcessState(Constant.SignProcessState.IS_START.getValue());
        signRepo.save(sign);

        //4、跳过第一环节（主任审核）
        Task task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).active().singleResult();
        taskService.addComment(task.getId(), processInstance.getId(), "系统自动处理");    //添加处理信息
        taskService.complete(task.getId(), ActivitiUtil.setAssigneeValue(Constant.FlowUserName.USER_QS.getValue(), SessionUtil.getUserId()));

        //5、跳过第二环节（签收）
        task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).active().singleResult();
        taskService.addComment(task.getId(), processInstance.getId(), "已经确认签收！");    //添加处理信息
        taskService.complete(task.getId(), ActivitiUtil.setAssigneeValue(Constant.FlowUserName.USER_ZHB.getValue(), SessionUtil.getUserId()));

        //6、跳过第三个环节（综合部拟办意见）
        task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).active().singleResult();
        taskService.addComment(task.getId(), processInstance.getId(), sign.getComprehensivehandlesug());    //综合部拟办意见
        //查询是否有待办人员
        User leadUser = userRepo.findById(User_.id.getName(), sign.getLeaderId());
        taskService.complete(task.getId(), ActivitiUtil.setAssigneeValue(Constant.FlowUserName.USER_FGLD.getValue(),
                Validate.isString(leadUser.getTakeUserId()) ? leadUser.getTakeUserId() : leadUser.getId()));

        return new ResultMsg(true, MsgCode.OK.getValue(), "操作成功！");
    }

    @Override
    @Transactional
    public ResultMsg dealFlow(ProcessInstance processInstance, FlowDto flowDto) {
        Task task = null;
        if (Validate.isString(flowDto.getTaskId())) {
            task = taskService.createTaskQuery().taskId(flowDto.getTaskId()).active().singleResult();
        } else {
            task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).active().singleResult();
        }
        if (task == null) {
            return new ResultMsg(false, MsgCode.ERROR.getValue(), "该流程已被处理！");
        }
        if (task.isSuspended()) {
            return new ResultMsg(false, MsgCode.ERROR.getValue(), "项目已暂停，不能进行操作！");
        }

        //参数定义
        String signid = processInstance.getBusinessKey(), businessId = "", assigneeValue = "", branchIndex = "";
        Sign sign = null;
        WorkProgram wk = null;
        DispatchDoc dp = null;
        FileRecord fileRecord = null;
        List<User> userList = null;
        List<SignPrincipal> signPriList = null;     //项目负责人
        User dealUser = null;
        Org org = null;                             //部门
        OrgDept orgDept = null;                     //部门和小组
        boolean isMainBranch = false;
        boolean saveSignFlag = false;

        Map<String, Object> variables = processInstance.getProcessVariables();

        //以下是流程环节处理
        switch (task.getTaskDefinitionKey()) {
            //签收
            case Constant.FLOW_SIGN_QS:
                sign = signRepo.findById(Sign_.signid.getName(), signid);
                task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).active().singleResult();
                taskService.addComment(task.getId(), processInstance.getId(), flowDto.getDealOption());    //添加处理信息
                taskService.complete(task.getId(), ActivitiUtil.setAssigneeValue(Constant.FlowUserName.USER_ZHB.getValue(), SessionUtil.getUserId()));

                if (!Validate.isString(sign.getLeaderName())) {
                    return new ResultMsg(false, MsgCode.ERROR.getValue(), "操作失败，请先设置默认办理部门！");
                }
                task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).active().singleResult();
                taskService.addComment(task.getId(), processInstance.getId(), sign.getComprehensivehandlesug());    //综合部拟办意见
                //完成综合部审批，查询是否有代办
                dealUser = userRepo.findById(User_.id.getName(), sign.getLeaderId());
                variables = ActivitiUtil.setAssigneeValue(Constant.FlowUserName.USER_FGLD.getValue(),
                        Validate.isString(dealUser.getTakeUserId()) ? dealUser.getTakeUserId() : dealUser.getId());

                break;
            //综合部审批（该环节自动跳过）
            case Constant.FLOW_SIGN_ZHB:
                if (flowDto.getBusinessMap().get("FGLD") == null) {
                    return new ResultMsg(false, MsgCode.ERROR.getValue(), "请选择分管领导！");
                }
                variables.put("user", flowDto.getBusinessMap().get("FGLD").toString());

                sign = signRepo.findById(Sign_.signid.getName(), signid);
                sign.setComprehensivehandlesug(flowDto.getDealOption());
                sign.setComprehensiveDate(new Date());
                sign.setComprehensiveId(SessionUtil.getUserId());
                sign.setComprehensiveName(SessionUtil.getDisplayName());
                saveSignFlag = true;
                //查询是否有代办
                dealUser = userRepo.findById(User_.id.getName(), sign.getLeaderId());
                taskService.complete(task.getId(), ActivitiUtil.setAssigneeValue(Constant.FlowUserName.USER_FGLD.getValue(),
                        Validate.isString(dealUser.getTakeUserId()) ? dealUser.getTakeUserId() : dealUser.getId()));

                break;
            //分管副主任审批
            case Constant.FLOW_SIGN_FGLD_FB:
                if (flowDto.getBusinessMap().get("MAIN_ORG") == null || !Validate.isString(flowDto.getBusinessMap().get("MAIN_ORG").toString())) {
                    return new ResultMsg(false, MsgCode.ERROR.getValue(), "请先选择主办部门！");
                }
                boolean isHaveAssistOrg = false;
                List<String> assistOrgIdList = null;
                //协办流程分支
                if (flowDto.getBusinessMap().get("ASSIST_ORG") != null && Validate.isString(flowDto.getBusinessMap().get("ASSIST_ORG").toString())) {
                    assistOrgIdList = StringUtil.getSplit(flowDto.getBusinessMap().get("ASSIST_ORG").toString(), ",");
                    if (assistOrgIdList.size() > 3) {
                        return new ResultMsg(false, MsgCode.ERROR.getValue(), "协办部门最多只能选择3个！");
                    }
                    isHaveAssistOrg = true;
                }
                //保存分支
                int branchId = 1;
                List<SignBranch> saveBranchList = new ArrayList<>();
                SignBranch signBranch1 = new SignBranch();
                signBranch1.setBranchId(String.valueOf(branchId));
                signBranch1.setSignId(signid);
                signBranch1.setIsNeedWP(EnumState.YES.getValue());
                signBranch1.setIsMainBrabch(EnumState.YES.getValue());
                signBranch1.setOrgId(flowDto.getBusinessMap().get("MAIN_ORG").toString());
                saveBranchList.add(signBranch1);
                //设置流程参数
                variables.put(Constant.SignFlowParams.BRANCH1.getValue(), Integer.valueOf(EnumState.YES.getValue()));
                orgDept = orgDeptRepo.findById(signBranch1.getOrgId());
                if (orgDept == null || !Validate.isString(orgDept.getDirectorID())) {
                    return new ResultMsg(false, MsgCode.ERROR.getValue(), "请设置" + orgDept.getName() + "的部门负责人！");
                }
                //查询是否有代办
                dealUser = userRepo.findById(User_.id.getName(), orgDept.getDirectorID());
                variables.put(Constant.FlowUserName.USER_BZ1.getValue(), Validate.isString(dealUser.getTakeUserId()) ? dealUser.getTakeUserId() : dealUser.getId());
                //默认不走其他分支
                variables.put(Constant.SignFlowParams.BRANCH2.getValue(), Integer.valueOf(EnumState.NO.getValue()));
                variables.put(Constant.SignFlowParams.BRANCH3.getValue(), Integer.valueOf(EnumState.NO.getValue()));
                variables.put(Constant.SignFlowParams.BRANCH4.getValue(), Integer.valueOf(EnumState.NO.getValue()));
                //保存协办部门信息
                if (isHaveAssistOrg) {
                    branchId++;
                    for (String assistOrgId : assistOrgIdList) {
                        SignBranch signBranch = new SignBranch();
                        signBranch.setBranchId(String.valueOf(branchId));
                        signBranch.setSignId(signid);
                        signBranch.setIsNeedWP(EnumState.YES.getValue());
                        signBranch.setIsMainBrabch(EnumState.NO.getValue());
                        signBranch.setOrgId(assistOrgId);
                        saveBranchList.add(signBranch);
                        orgDept = orgDeptRepo.findById(signBranch.getOrgId());
                        if (orgDept == null || !Validate.isString(orgDept.getDirectorID())) {
                            return new ResultMsg(false, MsgCode.ERROR.getValue(), "请设置【" + orgDept.getName() + "】的部门负责人！");
                        }
                        dealUser = userRepo.findById(User_.id.getName(), orgDept.getDirectorID());
                        switch (branchId) {
                            case 2:
                                variables.put(Constant.SignFlowParams.BRANCH2.getValue(), Integer.valueOf(EnumState.YES.getValue()));
                                variables.put(Constant.FlowUserName.USER_BZ2.getValue(), Validate.isString(dealUser.getTakeUserId()) ? dealUser.getTakeUserId() : dealUser.getId());
                                break;
                            case 3:
                                variables.put(Constant.SignFlowParams.BRANCH3.getValue(), Integer.valueOf(EnumState.YES.getValue()));
                                variables.put(Constant.FlowUserName.USER_BZ3.getValue(), Validate.isString(dealUser.getTakeUserId()) ? dealUser.getTakeUserId() : dealUser.getId());
                                break;
                            case 4:
                                variables.put(Constant.SignFlowParams.BRANCH4.getValue(), Integer.valueOf(EnumState.YES.getValue()));
                                variables.put(Constant.FlowUserName.USER_BZ4.getValue(), Validate.isString(dealUser.getTakeUserId()) ? dealUser.getTakeUserId() : dealUser.getId());
                                break;
                            default:
                                ;
                        }
                        branchId++;
                    }
                }
                signBranchRepo.bathUpdate(saveBranchList);
                //更改项目信息
                sign = signRepo.findById(Sign_.signid.getName(), signid);
                sign.setLeaderhandlesug(flowDto.getDealOption());
                sign.setLeaderDate(new Date());
                sign.setLeaderId(SessionUtil.getUserId());
                sign.setLeaderName(SessionUtil.getDisplayName());
                saveSignFlag = true;
                break;
            //部门分办1
            case Constant.FLOW_SIGN_BMFB1:
                branchIndex = Constant.SignFlowParams.BRANCH_INDEX1.getValue();
                isMainBranch = true;
                if (flowDto.getBusinessMap().get("M_USER_ID") == null || !Validate.isString(flowDto.getBusinessMap().get("M_USER_ID").toString())) {
                    return new ResultMsg(false, MsgCode.ERROR.getValue(), "请选择第一负责人！");
                }
                //查询是否有代办
                dealUser = userRepo.findById(User_.id.getName(), flowDto.getBusinessMap().get("M_USER_ID").toString());
                assigneeValue = Validate.isString(dealUser.getTakeUserId()) ? dealUser.getTakeUserId() : dealUser.getId();

                //设置项目负责人
                signPriList = new ArrayList<>();
                SignPrincipal mainPri = new SignPrincipal();
                mainPri.setSignId(signid);
                mainPri.setUserId(dealUser.getId());
                mainPri.setIsMainUser(EnumState.YES.getValue());
                mainPri.setFlowBranch(branchIndex);
                signPriList.add(mainPri);

                sign = signRepo.findById(Sign_.signid.getName(), signid);
                sign.setMinisterhandlesug(flowDto.getDealOption());
                sign.setMinisterDate(new Date());
                sign.setMinisterId(SessionUtil.getUserId());
                sign.setMinisterName(SessionUtil.getDisplayName());
                saveSignFlag = true;
                //部门分办2
            case Constant.FLOW_SIGN_BMFB2:
                if (!Validate.isString(branchIndex)) {
                    branchIndex = Constant.SignFlowParams.BRANCH_INDEX2.getValue();
                }
                //部门分办3
            case Constant.FLOW_SIGN_BMFB3:
                if (!Validate.isString(branchIndex)) {
                    branchIndex = Constant.SignFlowParams.BRANCH_INDEX3.getValue();
                }
                //部门分办4
            case Constant.FLOW_SIGN_BMFB4:
                if (!Validate.isString(branchIndex)) {
                    branchIndex = Constant.SignFlowParams.BRANCH_INDEX4.getValue();
                }
                if (flowDto.getBusinessMap().get("A_USER_ID") != null && Validate.isString(flowDto.getBusinessMap().get("A_USER_ID").toString())) {
                    userList = userRepo.findByIds(User_.id.getName(), flowDto.getBusinessMap().get("A_USER_ID").toString(), null);
                    if (signPriList == null) {
                        signPriList = new ArrayList<>();
                    }
                    for (User user : userList) {
                        //下一环节处理人
                        assigneeValue += "," + (Validate.isString(user.getTakeUserId()) ? user.getTakeUserId() : user.getId());
                        //项目负责人对象
                        SignPrincipal secondPri = new SignPrincipal();
                        secondPri.setSignId(signid);
                        secondPri.setUserId(user.getId());
                        secondPri.setIsMainUser(EnumState.NO.getValue());
                        secondPri.setFlowBranch(branchIndex);
                        signPriList.add(secondPri);
                    }
                } else {
                    //协流程必须要选择第二负责人
                    if (isMainBranch == false) {
                        return new ResultMsg(false, MsgCode.ERROR.getValue(), "请选择项目负责人！");
                    }
                }
                //设定下一环节处理人
                if (Constant.SignFlowParams.BRANCH_INDEX1.getValue().equals(branchIndex)) {
                    variables.put(Constant.FlowUserName.USER_FZR1.getValue(), assigneeValue);
                } else if (Constant.SignFlowParams.BRANCH_INDEX2.getValue().equals(branchIndex)) {
                    variables.put(Constant.FlowUserName.USER_FZR2.getValue(), assigneeValue);
                } else if (Constant.SignFlowParams.BRANCH_INDEX3.getValue().equals(branchIndex)) {
                    variables.put(Constant.FlowUserName.USER_FZR3.getValue(), assigneeValue);
                } else if (Constant.SignFlowParams.BRANCH_INDEX4.getValue().equals(branchIndex)) {
                    variables.put(Constant.FlowUserName.USER_FZR4.getValue(), assigneeValue);
                }
                //保存项目负责人
                signPrincipalRepo.bathUpdate(signPriList);

                break;
            //项目负责人办理1
            case Constant.FLOW_SIGN_XMFZR1:
                branchIndex = Constant.SignFlowParams.BRANCH_INDEX1.getValue();
                isMainBranch = true;
                //主流程要第一负责才能进行下一步操作
                if (!signPrincipalService.isMainPri(SessionUtil.getUserInfo().getId(), signid)) {
                    return new ResultMsg(false, MsgCode.ERROR.getValue(), "您不是第一负责人，不能进行下一步操作！");
                }
                //项目负责人办理2
            case Constant.FLOW_SIGN_XMFZR2:
                if (!Validate.isString(branchIndex)) {
                    branchIndex = Constant.SignFlowParams.BRANCH_INDEX2.getValue();
                }
                //项目负责人办理3
            case Constant.FLOW_SIGN_XMFZR3:
                if (!Validate.isString(branchIndex)) {
                    branchIndex = Constant.SignFlowParams.BRANCH_INDEX3.getValue();
                }
                //项目负责人办理4
            case Constant.FLOW_SIGN_XMFZR4:
                if (!Validate.isString(branchIndex)) {
                    branchIndex = Constant.SignFlowParams.BRANCH_INDEX4.getValue();
                }
                String isNeedWP = flowDto.getBusinessMap().get("IS_NEED_WP").toString();
                if (EnumState.YES.getValue().equals(isNeedWP)) {
                    //如果做工作方案，则要判断该分支工作方案是否完成
                    if (!signBranchRepo.checkFinishWP(signid, branchIndex)) {
                        return new ResultMsg(false, MsgCode.ERROR.getValue(), "您还没有完成工作方案，不能进行下一步操作！");
                    }
                    variables.put(Constant.SignFlowParams.GO_DISPATCH.getValue(), EnumState.NO.getValue());

                    //查询部门领导
                    orgDept = orgDeptRepo.queryBySignBranchId(signid, branchIndex);
                    if (orgDept == null || !Validate.isString(orgDept.getDirectorID())) {
                        return new ResultMsg(false, MsgCode.ERROR.getValue(), "请设置【" + orgDept.getName() + "】的部门负责人！");
                    }
                    dealUser = userRepo.findById(User_.id.getName(), orgDept.getDirectorID());

                    //设定下一环节处理人
                    if (Constant.SignFlowParams.BRANCH_INDEX1.getValue().equals(branchIndex)) {
                        variables.put(Constant.FlowUserName.USER_BZ1.getValue(), Validate.isString(dealUser.getTakeUserId()) ? dealUser.getTakeUserId() : dealUser.getId());
                    } else if (Constant.SignFlowParams.BRANCH_INDEX2.getValue().equals(branchIndex)) {
                        variables.put(Constant.FlowUserName.USER_BZ2.getValue(), Validate.isString(dealUser.getTakeUserId()) ? dealUser.getTakeUserId() : dealUser.getId());
                    } else if (Constant.SignFlowParams.BRANCH_INDEX3.getValue().equals(branchIndex)) {
                        variables.put(Constant.FlowUserName.USER_BZ3.getValue(), Validate.isString(dealUser.getTakeUserId()) ? dealUser.getTakeUserId() : dealUser.getId());
                    } else if (Constant.SignFlowParams.BRANCH_INDEX4.getValue().equals(branchIndex)) {
                        variables.put(Constant.FlowUserName.USER_BZ4.getValue(), Validate.isString(dealUser.getTakeUserId()) ? dealUser.getTakeUserId() : dealUser.getId());
                    }

                } else {
                    //更改状态
                    signBranchRepo.isNeedWP(signid, branchIndex, EnumState.NO.getValue());

                    //注意：1、项目建议书、可研阶段一定要做工作方案；
                    // 2、主分支跳转，则必须要所有协办分支都完成才能跳转。
                    if (isMainBranch) {
                        if(!signBranchRepo.assistFlowFinish(signid)){
                            return new ResultMsg(false, MsgCode.ERROR.getValue(), "协审分支还没处理完，您不能进行直接发文操作！");
                        }
                        sign = signRepo.findById(Sign_.signid.getName(), signid);
                        if((Constant.ProjectStage.STAGE_SUG.getValue().equals(sign.getReviewstage())
                            || Constant.ProjectStage.STAGE_STUDY.getValue().equals(sign.getReviewstage()))
                            && !signBranchRepo.isHaveWP(signid)){
                            return new ResultMsg(false, MsgCode.ERROR.getValue(), "【项目建议书，可行性研究报告】必要要做工作方案！");
                        }
                    }
                    variables.put(Constant.SignFlowParams.GO_DISPATCH.getValue(), EnumState.YES.getValue());
                    signBranchRepo.finishBranch(signid, branchIndex);
                    //不做工作方案，还是需要设定下一环节处理人
                    dealUser = signPrincipalService.getMainPriUser(signid);
                    variables.put(Constant.FlowUserName.USER_FZR1.getValue(), Validate.isString(dealUser.getTakeUserId()) ? dealUser.getTakeUserId() : dealUser.getId());
                }
                //完成所有工作方案
                if (signBranchRepo.allWPFinish(signid)) {
                    signRepo.updateSignProcessState(signid, Constant.SignProcessState.END_WP.getValue());
                }
                break;
            //部长审批工作方案1
            case Constant.FLOW_SIGN_BMLD_SPW1:
                branchIndex = Constant.SignFlowParams.BRANCH_INDEX1.getValue();
                isMainBranch = true;
                //部长审批工作方案1
            case Constant.FLOW_SIGN_BMLD_SPW2:
                if (!Validate.isString(branchIndex)) {
                    branchIndex = Constant.SignFlowParams.BRANCH_INDEX2.getValue();
                }
                //部长审批工作方案1
            case Constant.FLOW_SIGN_BMLD_SPW3:
                if (!Validate.isString(branchIndex)) {
                    branchIndex = Constant.SignFlowParams.BRANCH_INDEX3.getValue();
                }
                //部长审批工作方案4
            case Constant.FLOW_SIGN_BMLD_SPW4:
                if (!Validate.isString(branchIndex)) {
                    branchIndex = Constant.SignFlowParams.BRANCH_INDEX4.getValue();
                }
                if (!Validate.isString(SessionUtil.getUserInfo().getOrg().getOrgSLeader())) {
                    return new ResultMsg(false, MsgCode.ERROR.getValue(), "请先设置该部门的分管领导！");
                }
                //更改工作方案信息
                wk = workProgramRepo.findBySignIdAndBranchId(signid, branchIndex);
                wk.setMinisterSuggesttion(flowDto.getDealOption());
                wk.setMinisterDate(new Date());
                wk.setMinisterName(SessionUtil.getDisplayName());
                workProgramRepo.save(wk);

                //设定下一环节处理人
                dealUser = userRepo.findById(SessionUtil.getUserInfo().getOrg().getOrgSLeader());
                if (Constant.SignFlowParams.BRANCH_INDEX1.getValue().equals(branchIndex)) {
                    variables.put(Constant.FlowUserName.USER_FGLD1.getValue(), Validate.isString(dealUser.getTakeUserId()) ? dealUser.getTakeUserId() : dealUser.getId());
                } else if (Constant.SignFlowParams.BRANCH_INDEX2.getValue().equals(branchIndex)) {
                    variables.put(Constant.FlowUserName.USER_FGLD2.getValue(), Validate.isString(dealUser.getTakeUserId()) ? dealUser.getTakeUserId() : dealUser.getId());
                } else if (Constant.SignFlowParams.BRANCH_INDEX3.getValue().equals(branchIndex)) {
                    variables.put(Constant.FlowUserName.USER_FGLD3.getValue(), Validate.isString(dealUser.getTakeUserId()) ? dealUser.getTakeUserId() : dealUser.getId());
                } else if (Constant.SignFlowParams.BRANCH_INDEX4.getValue().equals(branchIndex)) {
                    variables.put(Constant.FlowUserName.USER_FGLD4.getValue(), Validate.isString(dealUser.getTakeUserId()) ? dealUser.getTakeUserId() : dealUser.getId());
                }
                break;
            //分管副主任审批工作方案
            case Constant.FLOW_SIGN_FGLD_SPW1:
                branchIndex = Constant.SignFlowParams.BRANCH_INDEX1.getValue();
                isMainBranch = true;
            case Constant.FLOW_SIGN_FGLD_SPW2:
                if (!Validate.isString(branchIndex)) {
                    branchIndex = Constant.SignFlowParams.BRANCH_INDEX2.getValue();
                }
            case Constant.FLOW_SIGN_FGLD_SPW3:
                if (!Validate.isString(branchIndex)) {
                    branchIndex = Constant.SignFlowParams.BRANCH_INDEX3.getValue();
                }
            case Constant.FLOW_SIGN_FGLD_SPW4:
                if (!Validate.isString(branchIndex)) {
                    branchIndex = Constant.SignFlowParams.BRANCH_INDEX4.getValue();
                }
                //更改工作方案审核信息
                wk = workProgramRepo.findBySignIdAndBranchId(signid, branchIndex);
                wk.setLeaderSuggesttion(flowDto.getDealOption());
                wk.setLeaderDate(new Date());
                wk.setLeaderName(SessionUtil.getDisplayName());
                workProgramRepo.save(wk);

                //选择第一负责人作为下一环节处理人
                dealUser = signPrincipalService.getMainPriUser(signid);
                variables.put(Constant.FlowUserName.USER_FZR1.getValue(), Validate.isString(dealUser.getTakeUserId()) ? dealUser.getTakeUserId() : dealUser.getId());
                break;
            //发文申请
            case Constant.FLOW_SIGN_FW:
                if (!signPrincipalService.isMainPri(SessionUtil.getUserInfo().getId(), signid)) {
                    return new ResultMsg(false, MsgCode.ERROR.getValue(), "您不是第一负责人，不能进行下一步操作！");
                }
                //有项目负责人，则项目负责人审核
                List<User> prilUserList = signPrincipalService.getAllSecondPriUser(processInstance.getBusinessKey());
                if (Validate.isList(prilUserList)) {
                    variables.put(Constant.SignFlowParams.PRIN_USER.getValue(), EnumState.YES.getValue());
                    List<String> assigneeList = new ArrayList<>(prilUserList.size()); //分配任务的人员
                    prilUserList.forEach(pul -> {
                        assigneeList.add(Validate.isString(pul.getTakeUserId()) ? pul.getTakeUserId() : pul.getId());
                    });
                    variables.put(Constant.FlowUserName.USER_HQ_LIST.getValue(), assigneeList);
                    //没有项目负责人，则部长审核
                } else {
                    variables.put(Constant.SignFlowParams.PRIN_USER.getValue(), EnumState.NO.getValue());
                    orgDept = orgDeptRepo.queryBySignBranchId(signid, Constant.SignFlowParams.BRANCH_INDEX1.getValue());
                    dealUser = userRepo.findById(User_.id.getName(), orgDept.getDirectorID());
                    variables.put(Constant.FlowUserName.USER_BZ1.getValue(), Validate.isString(dealUser.getTakeUserId()) ? dealUser.getTakeUserId() : dealUser.getId());
                }
                //完成发文
                signRepo.updateSignProcessState(signid, Constant.SignProcessState.END_DIS.getValue());
                break;
            //项目负责人确认发文（所有人确认通过才通过）
            case Constant.FLOW_SIGN_QRFW:
                if (flowDto.getBusinessMap().get("AGREE") == null || !Validate.isString(flowDto.getBusinessMap().get("AGREE").toString())) {
                    return new ResultMsg(false, MsgCode.ERROR.getValue(), "请选择同意或者不同意！");
                }
                //如果同意
                if (EnumState.YES.getValue().equals(flowDto.getBusinessMap().get("AGREE").toString())) {
                    variables.put(Constant.SignFlowParams.UNPASS.getValue(), false);
                    //设置部长审核参数
                    orgDept = orgDeptRepo.queryBySignBranchId(signid, Constant.SignFlowParams.BRANCH_INDEX1.getValue());
                    dealUser = userRepo.findById(User_.id.getName(), orgDept.getDirectorID());
                    variables.put(Constant.FlowUserName.USER_BZ1.getValue(), Validate.isString(dealUser.getTakeUserId()) ? dealUser.getTakeUserId() : dealUser.getId());
                    flowDto.setDealOption(flowDto.getDealOption() + "【审批结果：通过】");
                    //如果不同意，则流程回到发文申请环节
                } else {
                    variables.put(Constant.SignFlowParams.UNPASS.getValue(), true);
                    //选择第一负责人
                    dealUser = signPrincipalService.getMainPriUser(signid);
                    variables.put(Constant.FlowUserName.USER_FZR1.getValue(), Validate.isString(dealUser.getTakeUserId()) ? dealUser.getTakeUserId() : dealUser.getId());
                    flowDto.setDealOption(flowDto.getDealOption() + "【审批结果：不通过】");
                }
                break;
            //部长审批发文
            case Constant.FLOW_SIGN_BMLD_QRFW:
                if (!Validate.isString(SessionUtil.getUserInfo().getOrg().getOrgSLeaderName())) {
                    return new ResultMsg(false, MsgCode.ERROR.getValue(), "请先设置该部门的分管领导！");
                }
                dealUser = userRepo.findById(User_.id.getName(), SessionUtil.getUserInfo().getOrg().getOrgSLeader());
                variables.put(Constant.FlowUserName.USER_FGLD1.getValue(), Validate.isString(dealUser.getTakeUserId()) ? dealUser.getTakeUserId() : dealUser.getId());
                //修改发文信息
                businessId = flowDto.getBusinessMap().get("DIS_ID").toString();
                dp = dispatchDocRepo.findById(DispatchDoc_.id.getName(), businessId);
                dp.setMinisterSuggesttion(flowDto.getDealOption());
                dp.setMinisterDate(new Date());
                dp.setMinisterName(SessionUtil.getDisplayName());
                dispatchDocRepo.save(dp);
                break;

            //分管领导审批发文
            case Constant.FLOW_SIGN_FGLD_QRFW:
                userList = userRepo.findUserByRoleName(EnumFlowNodeGroupName.DIRECTOR.getValue());
                if (!Validate.isList(userList)) {
                    return new ResultMsg(false, MsgCode.ERROR.getValue(), "请先设置【" + EnumFlowNodeGroupName.DIRECTOR.getValue() + "】角色用户！");
                }
                dealUser = userList.get(0);
                variables.put(Constant.FlowUserName.USER_ZR.getValue(), Validate.isString(dealUser.getTakeUserId()) ? dealUser.getTakeUserId() : dealUser.getId());

                //修改发文信息
                businessId = flowDto.getBusinessMap().get("DIS_ID").toString();
                dp = dispatchDocRepo.findById(DispatchDoc_.id.getName(), businessId);
                dp.setViceDirectorSuggesttion(flowDto.getDealOption());
                dp.setViceDirectorDate(new Date());
                dp.setViceDirectorName(SessionUtil.getDisplayName());
                dispatchDocRepo.save(dp);
                break;
            //主任审批发文
            case Constant.FLOW_SIGN_ZR_QRFW:
                businessId = flowDto.getBusinessMap().get("DIS_ID").toString();
                dp = dispatchDocRepo.findById(DispatchDoc_.id.getName(), businessId);
                dp.setDirectorSuggesttion(flowDto.getDealOption());
                dp.setDirectorDate(new Date());
                dp.setDirectorName(SessionUtil.getDisplayName());
                dispatchDocRepo.save(dp);

                //项目负责人生成发文编号
                dealUser = signPrincipalService.getMainPriUser(signid);
                variables.put(Constant.FlowUserName.USER_M.getValue(), Validate.isString(dealUser.getTakeUserId()) ? dealUser.getTakeUserId() : dealUser.getId());
                break;
            //生成发文编号
            case Constant.FLOW_SIGN_FWBH:
                businessId = flowDto.getBusinessMap().get("DIS_ID").toString();
                dp = dispatchDocRepo.findById(DispatchDoc_.id.getName(), businessId);
                if (!Validate.isString(dp.getFileNum())) {
                    return new ResultMsg(false, MsgCode.ERROR.getValue(), "操作失败，该项目还没有发文编号，不能进行下一步操作！");
                }
                userList = userRepo.findUserByRoleName(EnumFlowNodeGroupName.FINANCIAL.getValue());
                if (!Validate.isList(userList)) {
                    return new ResultMsg(false, MsgCode.ERROR.getValue(), "请先设置【" + EnumFlowNodeGroupName.FINANCIAL.getValue() + "】角色用户！");
                }
                assigneeValue = "";
                for (int i = 0, l = userList.size(); i < l; i++) {
                    if (i > 0) {
                        assigneeValue += ",";
                    }
                    dealUser = userList.get(i);
                    assigneeValue += Validate.isString(dealUser.getTakeUserId()) ? dealUser.getTakeUserId() : dealUser.getId();
                }
                variables.put(Constant.FlowUserName.USER_CW.getValue(), assigneeValue);
                break;
            //财务办理
            case Constant.FLOW_SIGN_CWBL:
            	sign = signRepo.findById(Sign_.signid.getName(), signid);
            	//财务办理状态9已办理
            	sign.setFinanciaStatus(Constant.EnumState.YES.getValue());
            	
                dealUser = signPrincipalService.getMainPriUser(signid);
                variables.put(Constant.FlowUserName.USER_M.getValue(), Validate.isString(dealUser.getTakeUserId()) ? dealUser.getTakeUserId() : dealUser.getId());
                break;
            //第一负责人归档
            case Constant.FLOW_SIGN_GD:
                if (flowDto.getBusinessMap().get("checkFileUser") != null) {
                    dealUser = JSON.parseObject(flowDto.getBusinessMap().get("checkFileUser").toString(), User.class);
                    variables.put(Constant.SignFlowParams.SECOND_USER.getValue(), EnumState.YES.getValue());
                    variables.put(Constant.FlowUserName.USER_A.getValue(), Validate.isString(dealUser.getTakeUserId()) ? dealUser.getTakeUserId() : dealUser.getId());
                } else {
                    variables.put(Constant.SignFlowParams.SECOND_USER.getValue(), EnumState.NO.getValue());
                    //如果是回退，则保留之前的审核人
                    sign = signRepo.findById(Sign_.signid.getName(), signid);
                    if (Validate.isString(sign.getSecondPriUser())) {
                        dealUser = userRepo.findById(User_.id.getName(), sign.getSecondPriUser());
                        //不是回退，则取第一个
                    } else {
                        userList = userRepo.findUserByRoleName(EnumFlowNodeGroupName.FILER.getValue());
                        if (!Validate.isList(userList)) {
                            return new ResultMsg(false, MsgCode.ERROR.getValue(), "请先设置【" + EnumFlowNodeGroupName.FILER.getValue() + "】角色用户！");
                        }
                        dealUser = userList.get(0);
                    }
                    variables.put(Constant.FlowUserName.USER_QRGD.getValue(), Validate.isString(dealUser.getTakeUserId()) ? dealUser.getTakeUserId() : dealUser.getId());
                }
                signRepo.updateSignProcessState(signid, Constant.SignProcessState.END_FILE.getValue());
                break;
            //第二负责人审批归档
            case Constant.FLOW_SIGN_DSFZR_QRGD:
                userList = userRepo.findUserByRoleName(EnumFlowNodeGroupName.FILER.getValue());
                if (!Validate.isList(userList)) {
                    return new ResultMsg(false, MsgCode.ERROR.getValue(), "请先设置【" + EnumFlowNodeGroupName.FILER.getValue() + "】角色用户！");
                }
                dealUser = userList.get(0);
                variables.put(Constant.FlowUserName.USER_QRGD.getValue(), Validate.isString(dealUser.getTakeUserId()) ? dealUser.getTakeUserId() : dealUser.getId());
                //更新项目第二负责人
                sign = signRepo.findById(Sign_.signid.getName(), signid);
                sign.setSecondPriUser(SessionUtil.getUserId());
                saveSignFlag = true;

                //第二负责人确认
                businessId = flowDto.getBusinessMap().get("GD_ID").toString();
                fileRecord = fileRecordRepo.findById(FileRecord_.fileRecordId.getName(), businessId);
                fileRecord.setProjectTwoUser(SessionUtil.getDisplayName());
                fileRecordRepo.save(fileRecord);
                break;
            //确认归档
            case Constant.FLOW_SIGN_QRGD:
                businessId = flowDto.getBusinessMap().get("GD_ID").toString();
                fileRecord = fileRecordRepo.findById(FileRecord_.fileRecordId.getName(), businessId);
                fileRecord.setFileDate(new Date());
                fileRecord.setSignUserid(SessionUtil.getUserId());
                fileRecord.setSignUserName(SessionUtil.getDisplayName());
                fileRecordRepo.save(fileRecord);

                //更改项目状态
                signRepo.updateSignState(signid, EnumState.YES.getValue());
                signRepo.updateSignProcessState(signid, Constant.SignProcessState.FINISH.getValue());
                break;
            default:
                ;
        }
        if (sign != null && saveSignFlag) {
            signRepo.save(sign);
        }

        taskService.addComment(task.getId(), processInstance.getId(), flowDto.getDealOption());    //添加处理信息
        if (flowDto.isEnd()) {
            taskService.complete(task.getId());
        } else {
            taskService.complete(task.getId(), variables);
        }
        return new ResultMsg(true, MsgCode.OK.getValue(), "操作成功！");
    }


    @Override
    public List<SignDto> findAssistSign() {
        HqlBuilder sqlBuilder = HqlBuilder.create();
        sqlBuilder.append(" select sign from " + Sign.class.getSimpleName() + " sign left join " + AssistPlanSign.class.getSimpleName() +
                " plang on sign." + Sign_.signid.getName() + " = plang." + AssistPlanSign_.signId.getName());
        sqlBuilder.append(" where sign." + Sign_.isassistflow.getName() + " =:isassistflow");
        sqlBuilder.setParam("isassistflow", EnumState.YES.getValue());
        sqlBuilder.append(" and sign." + Sign_.signState.getName() + "  =:signState");
        sqlBuilder.setParam("signState", EnumState.PROCESS.getValue());
        //排除掉已经在选的项目
        sqlBuilder.append(" and plang." + AssistPlanSign_.signId.getName() + " is null ");
        sqlBuilder.append(" order by sign."+Sign_.createdDate.getName()+" desc ");
        List<Sign> signList = signRepo.findByHql(sqlBuilder);

        if(Validate.isList(signList)){
            List<SignDto> resultList = new ArrayList<>(signList.size());
            if (signList != null && signList.size() > 0) {
                signList.forEach(s -> {
                    SignDto signDto = new SignDto();
                    BeanCopierUtils.copyProperties(s, signDto);
                    resultList.add(signDto);
                });
            }
            return resultList;
        }

        return null;
    }

    /**
     * 根据协审计划，查询收文信息
     *
     * @param planId
     * @return
     */
    @Override
    public List<SignDto> findByPlanId(String planId) {
        HqlBuilder sqlBuilder = HqlBuilder.create();

        sqlBuilder.append(" select  distinct sign from " + Sign.class.getSimpleName() + " as sign,AssistPlanSign as psign where sign." + Sign_.signid.getName() + " = psign." + AssistPlanSign_.signId.getName());
        sqlBuilder.append(" and psign.assistPlan.id =:planid");
        sqlBuilder.setParam("planid", planId);

        List<Sign> signList = signRepo.findByHql(sqlBuilder);
        List<SignDto> resultList = new ArrayList<>(signList == null ? 0 : signList.size());
        if (signList != null && signList.size() > 0) {
            signList.forEach(s -> {
                SignDto signDto = new SignDto();
                BeanCopierUtils.copyProperties(s, signDto);
                resultList.add(signDto);
            });
        }
        return resultList;
    }

    /**
     * 更新项目是否 协审状态
     *
     * @param signIds
     * @param status
     */
    @Override
    public void updateAssistState(String signIds, String status) {
        //更新项目状态
        HqlBuilder hqlBuilder = HqlBuilder.create();
        hqlBuilder.append(" update " + Sign.class.getSimpleName() + " set " + Sign_.isassistproc.getName() + " =:isassistproc ");
        hqlBuilder.setParam("isassistproc", status);
        hqlBuilder.bulidPropotyString("where", Sign_.signid.getName(), signIds);
        signRepo.executeHql(hqlBuilder);
    }

    @Override
    public List<SignDto> getAssociateDtos(String signId) {

        List<SignDto> signDtos = new ArrayList<SignDto>();
        SignDto signDto = new SignDto();
        Sign sign = signRepo.getById(signId);
        if (Validate.isString(sign.getProjectcode())) {
            BeanCopierUtils.copyProperties(sign, signDto);
            signDtos.add(signDto);

            getPreAssociateDto(sign.getAssociateSign(), signDtos);
        } else {
            signDto = null;
        }

        return signDtos;
    }


    /**
     * 递归查找项目关联
     */
    private void getPreAssociateDto(Sign associateSign, List<SignDto> signDtos) {

        //递归条件，没有关联项目的时候，停止递归
        if (associateSign == null || !Validate.isString(associateSign.getProjectcode())) {
            return;
        }

        SignDto associateSignDto = new SignDto();
        BeanCopierUtils.copyProperties(associateSign, associateSignDto);
        signDtos.add(associateSignDto);


        getPreAssociateDto(associateSign.getAssociateSign(), signDtos);

    }

    @Override
    public List<Sign> getAssociates(String signId) {
        List<Sign> signs = new ArrayList<Sign>();
        Sign sign = signRepo.getById(signId);
        if (Validate.isString(sign.getProjectcode())) {
            signs.add(sign);
            getPreAssociate(sign.getAssociateSign(), signs);
        }
        return signs;
    }


    /**
     * 递归查找项目关联
     */
    private void getPreAssociate(Sign associateSign, List<Sign> signs) {
        //递归条件，没有关联项目的时候，停止递归
        if (associateSign == null || !Validate.isString(associateSign.getProjectcode())) {
            return;
        }
        signs.add(associateSign);
        getPreAssociate(associateSign.getAssociateSign(), signs);
    }

    /**
     * 项目关联
     *
     * @param signId      项目ID
     * @param associateId 关联到的项目ID
     */
    @Override
    @Transactional
    public void associate(String signId, String associateId) {

        if (signId.equals(associateId)) {
            throw new IllegalArgumentException("不能关联自身项目");
        }
        Sign sign = signRepo.getById(signId);
        if (sign == null) {
            throw new IllegalArgumentException("项目不存在");
        }

        //如果associateId为空，解除关联
        if (!Validate.isString(associateId)) {
            sign.setIsAssociate(0);
            sign.setAssociateSign(null);
        } else {
            Sign associateSign = signRepo.getById(associateId);
            if (associateSign == null) {
                throw new IllegalArgumentException("关联项目不存在");
            }
            sign.setAssociateSign(associateSign);
            sign.setIsAssociate(1);
        }

        signRepo.save(sign);

    }

    /**
     * 查找正在运行并没有结束的收文
     *
     * @return
     */
    @Override
    @Transactional
    public List<Sign> selectSignNotFinish() {
        HqlBuilder hqlBuilder = HqlBuilder.create();
        hqlBuilder.append("select s from " + Sign.class.getSimpleName() + " s where s." + Sign_.signState.getName() + "=:state ");
        hqlBuilder.setParam("state", EnumState.PROCESS.getValue());
        hqlBuilder.append(" and s." + Sign_.issign.getName() + " =:issign").setParam("issign", EnumState.YES.getValue());
        List<Sign> signList = signRepo.findByHql(hqlBuilder);
        return signList;
    }

    @Override
    @Transactional
    public void bathUpdate(List<Sign> signList) {
        signRepo.bathUpdate(signList);
    }


    /**
     * 项目综合查询 条件初始化
     *
     * @return
     */
    @Override
    public ResultMsg initSignList() {
        //添加部门
        List<OrgDto> orgsList = new ArrayList<>();
        List<Org> orgList = orgRepo.findAll();
        if (Validate.isList(orgList)) {
            for (Org org : orgList) {
                OrgDto orgs = new OrgDto();
                orgs.setName(org.getName());
                orgs.setId(org.getId());
                orgsList.add(orgs);
            }
        }
        return new ResultMsg(true, MsgCode.OK.getValue(), "添加成功", orgsList);
    }

    /**
     * 正式签收
     *
     * @param signId
     */
    @Override
    public ResultMsg realSign(String signId) {
        Sign sign = signRepo.findById(Sign_.signid.getName(), signId);
        if (EnumState.YES.getValue().equals(sign.getIssign())) {
            return new ResultMsg(false, MsgCode.ERROR.getValue(), "操作失败，该项目已经签收完毕！");
        }
        boolean is15Days = (Validate.isString(sign.getReviewstage()) && ("可行性研究报告".equals(sign.getReviewstage()) || "项目概算".equals(sign.getReviewstage())));
        HqlBuilder sqlBuilder = HqlBuilder.create();
        sqlBuilder.append(" update cs_sign set " + Sign_.signdate.getName() + " = sysdate,");
        sqlBuilder.append(Sign_.issign.getName() + " =:issign ").setParam("issign", EnumState.YES.getValue());
        sqlBuilder.append(" ," + Sign_.surplusdays.getName() + " = :surplusdays  ");
        if (is15Days) {
            sqlBuilder.setParam("surplusdays", Constant.WORK_DAY_15);
        } else {
            sqlBuilder.setParam("surplusdays", Constant.WORK_DAY_12);
        }
        sqlBuilder.bulidPropotyString("where", Sign_.signid.getName(), signId);
        signRepo.executeSql(sqlBuilder);

        return new ResultMsg(true, MsgCode.OK.getValue(), "操作成功！");
    }

    /**
     * 获取未合并评审的项目信息
     * (已经发起流程，未完成发文的项目信息,排除自身)
     *
     * @param signId
     * @return
     */
    @Override
    public List<SignDto> unMergeWPSign(String signId) {
        HqlBuilder hqlBuilder = HqlBuilder.create();
        hqlBuilder.append(" from " + Sign.class.getSimpleName() + " where " + Sign_.signState.getName() + " =:signState ");
        hqlBuilder.setParam("signState", EnumState.PROCESS.getValue());
        hqlBuilder.append(" and " + Sign_.processState.getName() + " > " + Constant.SignProcessState.IS_START.getValue());
        hqlBuilder.append(" and " + Sign_.processState.getName() + " < " + Constant.SignProcessState.END_WP.getValue());
        hqlBuilder.append(" and " + Sign_.filenum.getName() + " is null  ");
        hqlBuilder.append(" and " + Sign_.signid.getName() + " != :self ").setParam("self", signId);
        hqlBuilder.append(" and " + Sign_.signid.getName() + " not in ( select " + SignMerge_.mergeId.getName() + " from " + SignMerge.class.getSimpleName());
        hqlBuilder.append(" where " + SignMerge_.signId.getName() + " =:signId and " + SignMerge_.mergeType.getName() + " =:mergeType )");
        hqlBuilder.setParam("signId", signId).setParam("mergeType", Constant.MergeType.WORK_PROGRAM.getValue());
        ;
        List<Sign> signList = signRepo.findByHql(hqlBuilder);

        List<SignDto> resultList = signList == null ? null : new ArrayList<>();
        if (Validate.isList(signList)) {
            signList.forEach(sl -> {
                SignDto signDto = new SignDto();
                BeanCopierUtils.copyProperties(sl, signDto);
                resultList.add(signDto);
            });
        }
        return resultList;
    }

    /**
     * 获取已合并评审的项目信息
     *
     * @param signId
     * @return
     */
    @Override
    public List<SignDto> getMergeWPSignBySignId(String signId) {
        HqlBuilder hqlBuilder = HqlBuilder.create();
        hqlBuilder.append(" from " + Sign.class.getSimpleName() + " where " + Sign_.signid.getName());
        hqlBuilder.append(" in ( select " + SignMerge_.mergeId.getName() + " from " + SignMerge.class.getSimpleName());
        hqlBuilder.append(" where " + SignMerge_.signId.getName() + " =:signId and " + SignMerge_.mergeType.getName() + " =:mergeType )");
        hqlBuilder.setParam("signId", signId).setParam("mergeType", Constant.MergeType.WORK_PROGRAM.getValue());
        List<Sign> signList = signRepo.findByHql(hqlBuilder);

        List<SignDto> resultList = signList == null ? null : new ArrayList<>();
        if (Validate.isList(signList)) {
            signList.forEach(sl -> {
                SignDto signDto = new SignDto();
                BeanCopierUtils.copyProperties(sl, signDto);
                resultList.add(signDto);
            });
        }
        return resultList;
    }

    /**
     * 获取待合并发文的项目
     * (已完成工作方案，但是没有生成发文编号的项目)
     *
     * @param signId
     * @return
     */
    @Override
    public List<SignDto> unMergeDISSign(String signId) {
        HqlBuilder hqlBuilder = HqlBuilder.create();
        hqlBuilder.append(" from " + Sign.class.getSimpleName() + " where " + Sign_.signState.getName() + " =:signState ");
        hqlBuilder.setParam("signState", EnumState.PROCESS.getValue());
        hqlBuilder.append(" and " + Sign_.processState.getName() + " > " + Constant.SignProcessState.END_WP.getValue());
        hqlBuilder.append(" and " + Sign_.processState.getName() + " < " + Constant.SignProcessState.END_DIS_NUM.getValue());
        hqlBuilder.append(" and (" + Sign_.filenum.getName() + " is null )");
        hqlBuilder.append(" and " + Sign_.signid.getName() + " != :self ").setParam("self", signId);
        hqlBuilder.append(" and " + Sign_.signid.getName() + " not in ( select " + SignMerge_.mergeId.getName() + " from " + SignMerge.class.getSimpleName());
        hqlBuilder.append(" where " + SignMerge_.signId.getName() + " =:signId and " + SignMerge_.mergeType.getName() + " =:mergeType )");
        hqlBuilder.setParam("signId", signId).setParam("mergeType", Constant.MergeType.DISPATCH.getValue());
        List<Sign> signList = signRepo.findByHql(hqlBuilder);

        List<SignDto> resultList = signList == null ? null : new ArrayList<>();
        if (Validate.isList(signList)) {
            signList.forEach(sl -> {
                SignDto signDto = new SignDto();
                BeanCopierUtils.copyProperties(sl, signDto);
                resultList.add(signDto);
            });
        }
        return resultList;
    }

    /**
     * 获取已选合并发文的项目
     *
     * @param signId
     * @return
     */
    @Override
    public List<SignDto> getMergeDISSignBySignId(String signId) {
        HqlBuilder hqlBuilder = HqlBuilder.create();
        hqlBuilder.append(" from " + Sign.class.getSimpleName() + " where " + Sign_.signid.getName());
        hqlBuilder.append(" in ( select " + SignMerge_.mergeId.getName() + " from " + SignMerge.class.getSimpleName());
        hqlBuilder.append(" where " + SignMerge_.signId.getName() + " =:signId and " + SignMerge_.mergeType.getName() + " =:mergeType )");
        hqlBuilder.setParam("signId", signId).setParam("mergeType", Constant.MergeType.DISPATCH.getValue());
        List<Sign> signList = signRepo.findByHql(hqlBuilder);
        List<SignDto> resultList = signList == null ? null : new ArrayList<>();
        if (Validate.isList(signList)) {
            signList.forEach(sl -> {
                SignDto signDto = new SignDto();
                BeanCopierUtils.copyProperties(sl, signDto);
                resultList.add(signDto);
            });
        }
        return resultList;
    }

    /**
     * 保存合并评审项目
     *
     * @param signId
     * @param mergeIds
     * @param mergeType
     * @return
     */
    @Override
    public ResultMsg mergeSign(String signId, String mergeIds, String mergeType) {
        if (!Validate.isString(signId)) {
            return new ResultMsg(false, MsgCode.ERROR.getValue(), "操作失败，无法获取主项目信息！");
        }
        if (!Validate.isString(mergeIds)) {
            return new ResultMsg(false, MsgCode.ERROR.getValue(), "操作失败，无法获取合并评审项目信息！");
        }
        if (!Constant.MergeType.WORK_PROGRAM.getValue().equals(mergeType) && !Constant.MergeType.DISPATCH.getValue().equals(mergeType)) {
            return new ResultMsg(false, MsgCode.ERROR.getValue(), "操作失败，参数异常，请联系管理员查看！");
        }
        List<String> mergeSignIdList = StringUtil.getSplit(mergeIds, ",");
        List<SignMerge> saveList = new ArrayList<>(mergeSignIdList.size());
        for (String mergeId : mergeSignIdList) {
            SignMerge signMerge = new SignMerge();
            signMerge.setSignId(signId);
            signMerge.setMergeId(mergeId);
            signMerge.setMergeType(mergeType);
            Date now = new Date();
            signMerge.setCreatedBy(SessionUtil.getLoginName());
            signMerge.setModifiedBy(SessionUtil.getLoginName());
            signMerge.setCreatedDate(now);
            signMerge.setModifiedDate(now);
            saveList.add(signMerge);
        }
        signMergeRepo.bathUpdate(saveList);
        return new ResultMsg(true, MsgCode.OK.getValue(), "操作成功！");
    }

    /**
     * 取消合并评审项目
     *
     * @param signId
     * @param cancelIds
     * @param mergeType
     * @return
     */
    @Override
    public ResultMsg cancelMergeSign(String signId, String cancelIds, String mergeType) {
        if (!Validate.isString(signId)) {
            return new ResultMsg(false, MsgCode.ERROR.getValue(), "操作失败，无法获取主项目信息！");
        }
        if (!Constant.MergeType.WORK_PROGRAM.getValue().equals(mergeType) && !Constant.MergeType.DISPATCH.getValue().equals(mergeType)) {
            return new ResultMsg(false, MsgCode.ERROR.getValue(), "操作失败，参数异常，请联系管理员查看！");
        }
        HqlBuilder hqlBuilder = HqlBuilder.create();
        hqlBuilder.append(" delete from " + SignMerge.class.getSimpleName() + " where ");
        hqlBuilder.append(SignMerge_.signId.getName() + " =:signId ").setParam("signId", signId);
        hqlBuilder.append(" and " + SignMerge_.mergeType.getName() + " =:mergeType ").setParam("mergeType", mergeType);
        //如果有解除删除，则解除相应的项目，否则解除所有
        if (Validate.isString(cancelIds)) {
            hqlBuilder.bulidPropotyString("and", SignMerge_.mergeId.getName(), cancelIds);
        }

        signMergeRepo.executeHql(hqlBuilder);

        return new ResultMsg(true, MsgCode.OK.getValue(), "操作成功！");
    }

    /**
     * 更改项目状态
     *
     * @param signId
     * @param state
     * @return
     */
    @Override
    public boolean updateSignState(String signId, String state) {
        return signRepo.updateSignState(signId, state);
    }

    /**
     * 更改项目流程状态
     *
     * @param signId
     * @param processState
     * @return
     */
    @Override
    public boolean updateSignProcessState(String signId, Integer processState) {
        return signRepo.updateSignProcessState(signId, processState);
    }

    /**
     * 签收人获取项目列表
     * @param odataObj
     * @return
     */
    @Override
    public PageModelDto<SignDto> findBySignUser(ODataObj odataObj) {
        PageModelDto<SignDto> pageModelDto = new PageModelDto<SignDto>();
        Criteria criteria = signRepo.getExecutableCriteria();
        criteria = odataObj.buildFilterToCriteria(criteria);
        //1、排除已终止、已完成
        criteria.add(Restrictions.ne(Sign_.signState.getName(), EnumState.YES.getValue()));
        criteria.add(Restrictions.ne(Sign_.signState.getName(), EnumState.FORCE.getValue()));
        //2、已签收，但是未发起流程的项目 或者已发起流程，但是未签收的项目
        StringBuffer sb = new StringBuffer();
        sb.append( "( ("+Sign_.issign.getName()+" is null or "+Sign_.issign.getName()+" = '"+EnumState.NO.getValue()+"' ");
        sb.append(" and "+Sign_.processInstanceId.getName()+" = '"+EnumState.YES.getValue()+"') ");
        sb.append("  or ( "+Sign_.issign.getName()+" = '"+EnumState.YES.getValue()+"' and "+Sign_.processInstanceId.getName()+" is null ))");
        criteria.add(Restrictions.sqlRestriction(sb.toString()));

        Integer totalResult = ((Number) criteria.setProjection(Projections.rowCount()).uniqueResult()).intValue();
        criteria.setProjection(null);

        criteria.addOrder( Order.desc(Sign_.createdDate.getName()));
        if (odataObj.getSkip() > 0) {
            criteria.setFirstResult(odataObj.getSkip());
        }
        if (odataObj.getTop() > 0) {
            criteria.setMaxResults(odataObj.getTop());
        }
        pageModelDto.setCount(totalResult);

        List<Sign> signList = criteria.list();
        if(Validate.isList(signList)){
            List<SignDto> signDtoList = new ArrayList<SignDto>(signList.size());
            signList.forEach(x -> {
                SignDto signDto = new SignDto();
                BeanCopierUtils.copyProperties(x, signDto);
                signDtoList.add(signDto);
            });
            pageModelDto.setValue(signDtoList);
        }else{
            pageModelDto.setValue(null);
        }
        return pageModelDto;
    }

    /**
     * 根据项目名称，查询未关联阶段的项目（查询视图）
     * * @param projectName
     * @return
     */
    @Override
    public List<SignDispaWork> findAssociateSign(SignDispaWork signDispaWork) {
        HqlBuilder hqlBuilder = HqlBuilder.create();
        hqlBuilder.append(" from "+SignDispaWork.class.getSimpleName()+" where "+SignDispaWork_.isAssociate.getName()+" = 0 ");
        hqlBuilder.append(" and "+SignDispaWork_.signid.getName()+" != :signid ");
        hqlBuilder.setParam("signid",signDispaWork.getSignid());
        if(Validate.isString(signDispaWork.getProjectname())){
            hqlBuilder.append(" and "+SignDispaWork_.projectname.getName()+" like :projectName");
            hqlBuilder.setParam("projectName","%"+signDispaWork.getProjectname()+"%");
        }

        List<SignDispaWork> signList = signDispaWorkRepo.findByHql(hqlBuilder);

        return signList;
    }


    @Override
    public PageModelDto<SignDispaWork> getCommQurySign(ODataObj odataObj) {
        PageModelDto<SignDispaWork> pageModelDto = new PageModelDto<SignDispaWork>();
        Criteria criteria = signDispaWorkRepo.getExecutableCriteria();
        criteria = odataObj.buildFilterToCriteria(criteria);

       /* Disjunction dis = Restrictions.disjunction();
        dis.add(Restrictions.eq(SignDispaWork_.orgdirectorname.getName(), SessionUtil.getLoginName()));
        dis.add(Restrictions.eq(SignDispaWork_.orgMLeaderName.getName(), SessionUtil.getLoginName()));
        dis.add(Restrictions.eq(SignDispaWork_.orgSLeaderName.getName(), SessionUtil.getLoginName()));
        criteria.add(dis);*/

        Integer totalResult = ((Number) criteria.setProjection(Projections.rowCount()).uniqueResult()).intValue();
        criteria.setProjection(null);
        if (odataObj.getSkip() > 0) {
            criteria.setFirstResult(odataObj.getSkip());
        }
        if (odataObj.getTop() > 0) {
            criteria.setMaxResults(odataObj.getTop());
        }
        List<SignDispaWork> signDispaWork = criteria.list();
        pageModelDto.setCount(totalResult);
        pageModelDto.setValue(signDispaWork);
        return pageModelDto;
    }

    @Override
    @Transactional
    public void reserveAddSign(SignDto signDto) {
        Sign sign = new Sign();
        BeanCopierUtils.copyProperties(signDto, sign);
        sign.setSignState(EnumState.NORMAL.getValue());
        //送件人默认魏俊辉(可以更改)
        sign.setSendusersign(Constant.SEND_SIGN_NAME);
        //预签收状态为0
        sign.setIspresign(Constant.EnumState.NO.getValue());
        Date now = new Date();
        //签收时间
        sign.setCreatedDate(now);
        sign.setModifiedDate(now);
        sign.setCreatedBy(SessionUtil.getLoginName());
        sign.setModifiedBy(SessionUtil.getLoginName());
        sign.setIsLightUp("0");//默认为不亮灯
        signRepo.save(sign);

    }

    @Override
    public PageModelDto<SignDto> findAllReserve(ODataObj odataObj) {
        PageModelDto<SignDto> pageModelDto = new PageModelDto<SignDto>();
        Criteria criteria = signRepo.getExecutableCriteria();
        criteria = odataObj.buildFilterToCriteria(criteria);
        criteria.add(Restrictions.eq(Sign_.ispresign.getName(), Constant.EnumState.NO.getValue()));
        Integer totalResult = ((Number) criteria.setProjection(Projections.rowCount()).uniqueResult()).intValue();
        pageModelDto.setCount(totalResult);
        criteria.setProjection(null);
        if (odataObj.getSkip() > 0) {
            criteria.setFirstResult(odataObj.getTop());
        }
        if (odataObj.getTop() > 0) {
            criteria.setMaxResults(odataObj.getTop());
        }
        List<Sign> signlist = criteria.list();
        List<SignDto> signDtos = new ArrayList<SignDto>(signlist == null ? 0 : signlist.size());
        if (signlist != null && signlist.size() > 0) {
            signlist.forEach(x -> {
                SignDto signDto = new SignDto();
                BeanCopierUtils.copyProperties(x, signDto);
                signDtos.add(signDto);
            });
        }
        pageModelDto.setValue(signDtos);

        return pageModelDto;
    }

    /**
     * 删除预签收项目
     * @param signid
     */
    @Override
    @Transactional
    public void deleteReserveSign(String signid) {
        Sign sign = signRepo.findById(Sign_.signid.getName(),signid);
        if (sign != null) {
            signRepo.delete(sign);
            log.info(String.format("删除预签收项目", sign.getProjectname()));
        }
    }

    /**
     * 根据项目类型，获取项目最大收文编号
     * @param signType
     * @return
     */
    private int findSignMaxSeqByType(String signType) {
        HqlBuilder sqlBuilder = HqlBuilder.create();
        sqlBuilder.append("select max("+Sign_.signSeq.getName()+") from cs_sign ");
        sqlBuilder.append("where "+Sign_.dealOrgType.getName()+" =:signType and TO_CHAR("+Sign_.signdate.getName()+",'yyyy') = :signdate ");
        sqlBuilder.setParam("signType",signType);
        sqlBuilder.setParam("signdate",DateUtils.converToString(new Date(),"yyyy"));
        return dispatchDocRepo.returnIntBySql(sqlBuilder);
    }

    /***********************   以下是对接接口部分  ****************************/
    /**
     * 项目推送
     * @param signDto
     * @return
     */
    @Override
    public ResultMsg pushProject(SignDto signDto) {
        if(signDto == null){
            return new ResultMsg(false,MsgCode.OBJ_NULL.getValue(),"获取对象为空！");
        }
        if(!Validate.isString(signDto.getFilecode())){
            return new ResultMsg(false,MsgCode.MAIN_VALUE_NULL.getValue(),"收文编号为空！");
        }
        //1、根据收文编号获取项目信息
        Sign sign = signRepo.findByFilecode(signDto.getFilecode());
        Date now = new Date();
        if(sign == null){
            sign = new Sign();
            BeanCopierUtils.copyProperties(signDto,sign);

            //是否是项目概算流程
            if (Constant.ProjectStage.STAGE_BUDGET.getValue().equals(sign.getReviewstage()) || Validate.isString(sign.getIschangeEstimate())) {
                sign.setIsassistflow(EnumState.YES.getValue());
            } else {
                sign.setIsassistflow(EnumState.NO.getValue());
            }
            sign.setIsLightUp(Constant.signEnumState.NOLIGHT.getValue());
            sign.setCreatedDate(now);
            sign.setModifiedDate(now);
            sign.setCreatedBy("委里推送");
            sign.setModifiedBy("委里推送");
        }else{
            BeanCopierUtils.copyPropertiesIgnoreNull(signDto,sign);
        }

        //保存
        try{
            signRepo.save(sign);
            return new ResultMsg(true,MsgCode.SUCCESS.getValue(),"推送成功！");
        }catch (Exception e){
            return new ResultMsg(false,MsgCode.MAIN_VALUE_NULL.getValue(),"保存异常！",e);
        }

    }

}
