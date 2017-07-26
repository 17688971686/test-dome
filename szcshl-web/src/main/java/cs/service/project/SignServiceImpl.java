package cs.service.project;

import com.alibaba.fastjson.JSON;
import cs.common.Constant;
import cs.common.Constant.EnumFlowNodeGroupName;
import cs.common.Constant.EnumState;
import cs.common.Constant.MsgCode;
import cs.common.HqlBuilder;
import cs.common.ICurrentUser;
import cs.common.ResultMsg;
import cs.common.utils.*;
import cs.domain.external.Dept;
import cs.domain.project.*;
import cs.domain.sys.*;
import cs.model.PageModelDto;
import cs.model.expert.ExpertSelectedDto;
import cs.model.external.DeptDto;
import cs.model.external.OfficeUserDto;
import cs.model.flow.FlowDto;
import cs.model.project.*;
import cs.model.sys.OrgDto;
import cs.model.sys.UserDto;
import cs.repository.odata.ODataFilterItem;
import cs.repository.odata.ODataObj;
import cs.repository.repositoryImpl.external.DeptRepo;
import cs.repository.repositoryImpl.project.*;
import cs.repository.repositoryImpl.sys.CompanyRepo;
import cs.repository.repositoryImpl.sys.OrgRepo;
import cs.repository.repositoryImpl.sys.SysFileRepo;
import cs.repository.repositoryImpl.sys.UserRepo;
import cs.service.external.OfficeUserService;
import cs.service.flow.FlowService;
import cs.service.sys.UserService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class SignServiceImpl implements SignService {
    private static Logger log = Logger.getLogger(SignServiceImpl.class);

    @Autowired
    private SignRepo signRepo;
    @Autowired
    private ICurrentUser currentUser;
    @Autowired
    private UserService userService;
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

    @Override
    @Transactional
    public void createSign(SignDto signDto) {
        Sign sign = new Sign();
        BeanCopierUtils.copyProperties(signDto, sign);
        sign.setSignState(EnumState.NORMAL.getValue());
        //送件人默认魏俊辉(可以更改)
        sign.setSendusersign(Constant.SEND_SIGN_NAME);
        Date now = new Date();
        //签收时间
        sign.setCreatedDate(now);
        sign.setModifiedDate(now);
        sign.setCreatedBy(currentUser.getLoginName());
        sign.setModifiedBy(currentUser.getLoginName());
        sign.setIsLightUp("0");//默认为不亮灯
        //判断是否为协审
        if ("项目概算".equals(sign.getReviewstage()) || Validate.isString(sign.getIschangeEstimate())) {
            sign.setIsassistflow(EnumState.YES.getValue());
        } else {
            sign.setIsassistflow(EnumState.NO.getValue());
        }
        signRepo.save(sign);
    }

    @Override
    public PageModelDto<SignDto> get(ODataObj odataObj) {
        PageModelDto<SignDto> pageModelDto = new PageModelDto<SignDto>();
        List<Sign> signs = signRepo.findByOdata(odataObj);
        List<SignDto> signDtos = new ArrayList<SignDto>();

        if (signs != null && signs.size() > 0) {
            signs.forEach(x -> {
                SignDto signDto = new SignDto();
                BeanCopierUtils.copyProperties(x, signDto);
                //cannot copy
                signDto.setCreatedDate(x.getCreatedDate());
                signDto.setModifiedDate(x.getModifiedDate());

                signDtos.add(signDto);
            });
        }
        pageModelDto.setCount(odataObj.getCount());
        pageModelDto.setValue(signDtos);
        return pageModelDto;
    }

    @Override
    @Transactional
    public void updateSign(SignDto signDto) {
        Sign sign = signRepo.findById(signDto.getSignid());
        BeanCopierUtils.copyPropertiesIgnoreNull(signDto, sign);

        sign.setModifiedBy(currentUser.getLoginName());
        sign.setModifiedDate(new Date());
        signRepo.save(sign);

        log.info("更新sign 成功！signid=" + signDto.getSignid());
    }

    @Override
    public Map<String, Object> initFillPageData(String signId) {
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
        //编制单位查询
        Criteria criteria = companyRepo.getSession().createCriteria(Company.class);
        criteria.add(Restrictions.eq("coType", "编制单位"));
        List<Company> designcomlist = criteria.list();
        if (designcomlist != null) {
            map.put("designcomlist", designcomlist);
        }
        //建设单位查询
        Criteria c = companyRepo.getSession().createCriteria(Company.class);
        c.add(Restrictions.eq("coType", "建设单位"));
        List<Company> builtcomlist = c.list();
        if (builtcomlist != null) {
            map.put("builtcomlist", builtcomlist);
        }
        //查询系统上传文件
//        List<SysFileDto> sysFiles= fileService.findBySignId(sign.getSignid());
        Criteria file = sysFileRepo.getSession().createCriteria(SysFile.class);
        file.add(Restrictions.eq("businessId", sign.getSignid()));
        List<SysFile> sysFiles = file.list();
        if (sysFiles != null) {
            map.put("sysFiles", sysFiles);
        }
        return map;
    }

    @Override
    public void claimSignFlow(String taskId) {
        taskService.claim(taskId, currentUser.getLoginName());
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

    @Override
    @Transactional
    public void deleteSign(String signid) {
        Sign sign = signRepo.findById(signid);
        sign.setSignState(EnumState.DELETE.getValue());
        signRepo.save(sign);
        log.info(String.format("删除收文, 逻辑删除成功！", sign.getSignid()));
    }

    @Override
    @Transactional
    public void deleteSigns(String[] signids) {
        for (String signid : signids) {
            this.deleteSign(signid);
        }
        log.info("批量删除收文");
    }

    /**
     * 项目暂停
     * @param signid
     */
    @Override
    public ResultMsg stopFlow(String signid,ProjectStopDto projectStopDto) {
        Sign sign = signRepo.findById(Sign_.signid.getName(),signid);
        if (sign == null ) {
            return new ResultMsg(false,MsgCode.ERROR.getValue(),"操作失败，该项目数据已被删除！");
        }else if(Constant.EnumState.STOP.getValue().equals(sign.getSignState())){
            return new ResultMsg(false,MsgCode.ERROR.getValue(),"操作失败，该项目目前还是暂停状态！");
        }
        sign.setSignState(Constant.EnumState.STOP.getValue());

        //暂停流程
        ProcessInstance processInstance = flowService.findProcessInstanceByBusinessKey(signid);
        runtimeService.suspendProcessInstanceById(processInstance.getId());

        Date now = new Date();
		ProjectStop projectStop = new ProjectStop();
		if(projectStopDto != null){
            BeanCopierUtils.copyProperties(projectStopDto,projectStop);
        }
        projectStop.setStopid(UUID.randomUUID().toString());
		projectStop.setPausetime(now);
        projectStop.setCreatedDate(now);
        projectStop.setModifiedDate(now);
		projectStop.setIsactive(EnumState.YES.getValue());
		projectStop.setModifiedBy(currentUser.getLoginName());
		projectStop.setCreatedBy(currentUser.getLoginName());
        projectStop.setSign(sign);
		projectStopRepo.save(projectStop);

        return new ResultMsg(true,MsgCode.OK.getValue(),"操作成功！");
    }

    /**
     * 流程激活
     * @param signid
     * @return
     */
    @Override
    public ResultMsg restartFlow(String signid) {
        Sign sign = signRepo.findById(Sign_.signid.getName(),signid);
        if (sign == null ) {
            return new ResultMsg(false,MsgCode.ERROR.getValue(),"操作失败，该项目数据已被删除！");
        }
        sign.setSignState(EnumState.PROCESS.getValue());

        //激活流程
        ProcessInstance processInstance = flowService.findProcessInstanceByBusinessKey(signid);
        runtimeService.activateProcessInstanceById(processInstance.getId());

		//获取已暂停项目
        ProjectStop projectStop = projectStopRepo.findProjectStop(signid);
		if (projectStop == null) {
            return new ResultMsg(false,MsgCode.ERROR.getValue(),"操作失败，暂停记录已被删除！");
		}
        Date now = new Date();
        projectStop.setStartTime(now);
        long longtime = DateUtils.daysBetween(now, projectStop.getPausetime());
        projectStop.setPausedays((float) longtime);
        projectStop.setIsactive(EnumState.NO.getValue());
        projectStop.setSign(sign);
        projectStopRepo.save(projectStop);

        return new ResultMsg(true,MsgCode.OK.getValue(),"操作成功！");
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
                List<ExpertSelectedDto> expertSelectedDtos = new ArrayList<ExpertSelectedDto>();
                sign.getWorkProgramList().forEach(workProgram -> {
                    WorkProgramDto workProgramDto = new WorkProgramDto();
                    BeanCopierUtils.copyProperties(workProgram, workProgramDto);
                    workProgramDtoList.add(workProgramDto);


                });
                signDto.setWorkProgramDtoList(workProgramDtoList);
                // signDto.setExpertSelectedDtoList(expertSelectedDtos);
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

    @Override
    public void endFlow(String signid) {
        Sign sign = signRepo.findById(signid);
        sign.setSignState(EnumState.FORCE.getValue());
        signRepo.save(sign);
    }

    /************************************** S  新流程项目处理   *********************************************/
    /**
     * 发起流程
     * @param signid
     * @return
     */
    @Override
    @Transactional
    public ResultMsg startNewFlow(String signid) {
        Sign sign = signRepo.findById(Sign_.signid.getName(), signid);
        if(sign == null){
            return new ResultMsg(false,MsgCode.ERROR.getValue(),"发起流程失败，该项目已不存在！");
        }

        if(Validate.isString(sign.getProcessInstanceId())){
            return new ResultMsg(false,MsgCode.ERROR.getValue(),"该项目已发起流程！");
        }

        ProcessInstance processInstance = null;
        //如果是协审流程
        if (EnumState.YES.getValue().equals(sign.getIsassistflow())) {
            processInstance = runtimeService.startProcessInstanceByKey(Constant.EnumFlow.SIGN_XS_FLOW.getValue(), signid);
        } else {
            processInstance = runtimeService.startProcessInstanceByKey(Constant.EnumFlow.FINAL_SIGN.getValue(), signid);
        }
        //设置流程实例名称
        processEngine.getRuntimeService().setProcessInstanceName(processInstance.getId(), sign.getProjectname());

        //跳过第一环节
        Task task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).active().singleResult();
        taskService.addComment(task.getId(), processInstance.getId(), "系统自动处理");    //添加处理信息
        taskService.complete(task.getId(), ActivitiUtil.setAssigneeValue(currentUser.getLoginName()));

        //查找综合部部长
        List<UserDto> userList = userService.findUserByRoleName(EnumFlowNodeGroupName.COMM_DEPT_DIRECTOR.getValue());
        if (userList == null || userList.size() == 0) {
            throw new IllegalArgumentException(String.format("请先设置【%s】角色用户!!!", EnumFlowNodeGroupName.COMM_DEPT_DIRECTOR.getValue()));
        }
        //跳过第二环节
        task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).active().singleResult();
        taskService.addComment(task.getId(), processInstance.getId(), "已经确认签收！");    //添加处理信息
        taskService.complete(task.getId(), ActivitiUtil.setAssigneeValue(userList.get(0).getLoginName()));

        //项目正在运行
        sign.setProcessInstanceId(processInstance.getId());
        sign.setSignState(EnumState.PROCESS.getValue());
        signRepo.save(sign);

        return new ResultMsg(true,MsgCode.OK.getValue(),"操作成功！");
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
            return new ResultMsg(false, MsgCode.ERROR.getValue(),  "该流程已被处理！");
        }
        if(task.isSuspended()){
            return new ResultMsg(false, MsgCode.ERROR.getValue(), "项目已暂停，不能进行操作！");
        }
        //参数定义

        String signid = processInstance.getBusinessKey(), businessId = "", assigneeValue = "";
        Sign sign = null;
        WorkProgram wk = null;
        DispatchDoc dp = null;
        List<UserDto> userList = null;
        List<User> users = null;
        List<SignPrincipal> signPriList = null;     //项目负责人
        User dealUser = null;
        UserDto dealUserDto = null;
        boolean saveSignFlag = false;

        Map<String, Object> variables = processInstance.getProcessVariables();

        //流程处理
        switch (flowDto.getCurNode().getActivitiId()) {
            case Constant.FLOW_ZR_TB:                //填报
                break;

            case Constant.FLOW_QS:                    //签收
                userList = userService.findUserByRoleName(EnumFlowNodeGroupName.COMM_DEPT_DIRECTOR.getValue());
                if (userList == null || userList.size() == 0) {
                    return new ResultMsg(false, MsgCode.ERROR.getValue(), "请先设置【" + EnumFlowNodeGroupName.COMM_DEPT_DIRECTOR.getValue() + "】角色用户！");
                }
                variables.put("user", userList.get(0).getLoginName());

                sign = signRepo.findById(Sign_.signid.getName(), signid);
                sign.setSigndate(new Date());
                sign.setIssign(EnumState.YES.getValue());
                saveSignFlag = true;

                break;

            case Constant.FLOW_ZHB_SP_SW:            //综合部审批
                if (flowDto.getBusinessMap().get("FGLD") == null) {
                    return new ResultMsg(false, MsgCode.ERROR.getValue(), "请选择分管领导！");
                }
                variables.put("user", flowDto.getBusinessMap().get("FGLD").toString());

                sign = signRepo.findById(Sign_.signid.getName(), signid);
                sign.setComprehensivehandlesug(flowDto.getDealOption());
                sign.setComprehensiveDate(new Date());
                sign.setComprehensiveName(currentUser.getLoginName());
                saveSignFlag = true;

                break;
            case Constant.FLOW_FGLD_SP_SW:            //分管副主任
                userList = userService.findUserByRoleName(EnumFlowNodeGroupName.DEPT_LEADER.getValue());
                if (userList == null || userList.size() == 0) {
                    return new ResultMsg(false, "", "请先设置【" + EnumFlowNodeGroupName.DEPT_LEADER.getValue() + "】角色用户！");
                }
                sign = signRepo.findById(Sign_.signid.getName(), signid);
                sign.setLeaderhandlesug(flowDto.getDealOption());
                sign.setLeaderDate(new Date());
                sign.setLeaderName(currentUser.getLoginName());
                //判断是否分为两个部门处理
                if (flowDto.getBusinessMap().get("hostdept") != null) {
                    Org hostOrg = orgRepo.findById(flowDto.getBusinessMap().get("hostdept").toString());
                    dealUserDto = userService.filterOrgDirector(userList, hostOrg);
                    if (dealUserDto == null || !Validate.isString(dealUserDto.getLoginName())) {
                        return new ResultMsg(false, MsgCode.ERROR.getValue(), "请先设置【" + hostOrg.getName() + "】的" + EnumFlowNodeGroupName.DEPT_LEADER.getValue()
                                + "，设置的用户角色必须为【" + EnumFlowNodeGroupName.DEPT_LEADER.getValue() + "】！");
                    }
                    sign.setmOrgId(hostOrg.getId());    //设置主办部门
                    assigneeValue = dealUserDto.getLoginName();
                    variables.put("hostdept", Integer.valueOf(EnumState.YES.getValue()));
                    variables.put("muser", assigneeValue);
                } else {
                    return new ResultMsg(false, MsgCode.ERROR.getValue(), "必须要设置主办部门！");
                }

                if (flowDto.getBusinessMap().get("assistdept") != null) {
                    Org assistOrg = orgRepo.findById(flowDto.getBusinessMap().get("assistdept").toString());
                    dealUserDto = userService.filterOrgDirector(userList, assistOrg);
                    if (dealUserDto == null || !Validate.isString(dealUserDto.getLoginName())) {
                        return new ResultMsg(false, MsgCode.ERROR.getValue(), "请先设置【" + assistOrg.getName() + "】的" + EnumFlowNodeGroupName.DEPT_LEADER.getValue()
                                + "，设置的用户角色必须为【" + EnumFlowNodeGroupName.DEPT_LEADER.getValue() + "】！");
                    }
                    sign.setaOrgId(assistOrg.getId());    //设置协办部门
                    variables.put("assistdept", Integer.valueOf(EnumState.YES.getValue()));
                    variables.put("auser", dealUserDto.getLoginName());
                } else {
                    variables.put("assistdept", Integer.valueOf(EnumState.NO.getValue()));
                }

                saveSignFlag = true;
                break;
            case Constant.FLOW_BM_FB1:                //部门分办-主流程
                if (flowDto.getBusinessMap().get("M_USER_ID") == null) {
                    return new ResultMsg(false, MsgCode.ERROR.getValue(), "请先设置选择主要负责人！");
                }
                dealUser = userRepo.findById(User_.id.getName(),flowDto.getBusinessMap().get("M_USER_ID").toString());

                //设置项目负责人
                signPriList = new ArrayList<>();
                SignPrincipal mainPri = new SignPrincipal();
                mainPri.setSingId(signid);
                mainPri.setIsMainFlow(EnumState.YES.getValue());
                mainPri.setUserId(dealUser.getId());
                mainPri.setIsMainUser(EnumState.YES.getValue());
                signPriList.add(mainPri);

                sign = signRepo.findById(Sign_.signid.getName(), signid);
                sign.setMinisterhandlesug(flowDto.getDealOption());
                sign.setMinisterDate(new Date());
                sign.setMinisterName(currentUser.getLoginName());

                assigneeValue = "";
                assigneeValue = dealUser.getLoginName();

                //第二负责人
                if (flowDto.getBusinessMap().get("A_USER_ID") != null && Validate.isString(flowDto.getBusinessMap().get("A_USER_ID").toString())) {
                    users = userRepo.findByIds(User_.id.getName(), flowDto.getBusinessMap().get("A_USER_ID").toString(), null);
                    for (User user : users) {
                        assigneeValue += "," + user.getLoginName();
                        SignPrincipal secondPri = new SignPrincipal();
                        secondPri.setSingId(signid);
                        secondPri.setIsMainFlow(EnumState.YES.getValue());
                        secondPri.setUserId(user.getId());
                        secondPri.setIsMainUser(EnumState.NO.getValue());
                        signPriList.add(secondPri);
                    }
                }
                variables.put("users", assigneeValue);

                //保存项目负责人
                signPrincipalRepo.bathUpdate(signPriList);
                saveSignFlag = true;
                break;
            case Constant.FLOW_BM_FB2:                //部门分办
                if (flowDto.getBusinessMap().get("M_USER_ID") == null) {
                    return new ResultMsg(false, MsgCode.ERROR.getValue(), "请先设置选择主要负责人！");
                }
                dealUser = userRepo.findById(User_.id.getName(),flowDto.getBusinessMap().get("M_USER_ID").toString());
                //设置项目负责人
                signPriList = new ArrayList<>();
                SignPrincipal sMainPri = new SignPrincipal();
                sMainPri.setSingId(signid);
                sMainPri.setIsMainFlow(EnumState.NO.getValue());
                sMainPri.setUserId(dealUser.getId());
                sMainPri.setIsMainUser(EnumState.YES.getValue());
                signPriList.add(sMainPri);

                assigneeValue = "";
                assigneeValue = dealUser.getLoginName();

                //第二负责人
                if (flowDto.getBusinessMap().get("A_USER_ID") != null && Validate.isString(flowDto.getBusinessMap().get("A_USER_ID").toString())) {
                    users = userRepo.findByIds(User_.id.getName(), flowDto.getBusinessMap().get("A_USER_ID").toString(), null);
                    for (User user : users) {
                        assigneeValue += "," + user.getLoginName();
                        SignPrincipal secondPri = new SignPrincipal();
                        secondPri.setSingId(signid);
                        secondPri.setIsMainFlow(EnumState.NO.getValue());
                        secondPri.setUserId(user.getId());
                        secondPri.setIsMainUser(EnumState.NO.getValue());
                        signPriList.add(secondPri);
                    }
                }
                variables.put("users", assigneeValue);
                //保存项目负责人
                signPrincipalRepo.bathUpdate(signPriList);
                saveSignFlag = true;
                break;
            case Constant.FLOW_XMFZR_SP_GZFA1:        //项目负责人承办-主流程
                if (!signPrincipalService.isMainPri(currentUser.getLoginUser().getId(), signid, EnumState.YES.getValue())) {
                    return new ResultMsg(false, MsgCode.ERROR.getValue(), "您不是第一负责人，不能进行下一步操作！");
                }
                //如果是直接发文，则直接跳转
                if (flowDto.getBusinessMap().get("ZJFW") != null && EnumState.YES.getValue().equals(flowDto.getBusinessMap().get("ZJFW").toString())) {
                    //判断当前任务是否是分办任务，如果是分办任务，则不给跳转
                    String excId = task.getExecutionId();
                    ExecutionEntity execution = (ExecutionEntity) runtimeService.createExecutionQuery().executionId(task.getExecutionId()).singleResult();
                    if (execution.isConcurrent()) {
                        return new ResultMsg(false, MsgCode.ERROR.getValue(), "当前流程为并行分支流程，不能直接跳转发文！");
                    }
                    variables.put("zjfw", EnumState.YES.getValue());
                    //查找项目负责人
                    List<User> dealUserList = signPrincipalService.getSignPriUser(signid, EnumState.YES.getValue());
                    for (int i = 0, l = dealUserList.size(); i < l; i++) {
                        if (i == 0) {
                            assigneeValue = dealUserList.get(i).getLoginName();
                        } else {
                            assigneeValue += "," + dealUserList.get(i).getLoginName();
                        }
                    }
                    variables.put("users", assigneeValue);
                } else {
                    variables.put("zjfw", EnumState.NO.getValue());
                    dealUserDto = userService.getOrgDirector();
                    if (dealUserDto == null) {
                        return new ResultMsg(false, MsgCode.ERROR.getValue(), "请先设置" + EnumFlowNodeGroupName.DEPT_LEADER.getValue() + "，然后重新登录处理即可！");
                    }
                    variables.put("user", dealUserDto.getLoginName());
                }
                break;
            case Constant.FLOW_XMFZR_SP_GZFA2:        //项目负责人承办
                if (!signPrincipalService.isMainPri(currentUser.getLoginUser().getId(), signid, EnumState.NO.getValue())) {
                    return new ResultMsg(false, MsgCode.ERROR.getValue(), "您不是第一负责人，不能进行下一步操作！");
                }
                dealUserDto = userService.getOrgDirector();
                if (dealUserDto == null) {
                    return new ResultMsg(false, MsgCode.ERROR.getValue(), "请先设置" + EnumFlowNodeGroupName.DEPT_LEADER.getValue() + "，然后重新登录处理即可！");
                } else {
                    variables.put("user", dealUserDto.getLoginName());
                }
                break;
            case Constant.FLOW_BZ_SP_GZAN1:            //部长审批-主流程
                //获取分管领导
                dealUserDto = userService.getOrgSLeader();
                if (dealUserDto == null) {
                    return new ResultMsg(false, MsgCode.ERROR.getValue(), "请先设置该部门的【" + EnumFlowNodeGroupName.VICE_DIRECTOR.getValue() + "】角色用户！");
                }
                variables.put("user", dealUserDto.getLoginName());
                //更改工作方案信息
                businessId = flowDto.getBusinessMap().get("M_WP_ID").toString();
                wk = workProgramRepo.findById(WorkProgram_.id.getName(), businessId);
                wk.setMinisterSuggesttion(flowDto.getDealOption());
                wk.setMinisterDate(new Date());
                wk.setMinisterName(currentUser.getLoginName());
                workProgramRepo.save(wk);
                break;
            case Constant.FLOW_BZ_SP_GZAN2:            //部长审批
                //获取分管领导
                dealUserDto = userService.getOrgSLeader();
                if (dealUserDto == null) {
                    return new ResultMsg(false, MsgCode.ERROR.getValue(), "请先设置该部门的【" + EnumFlowNodeGroupName.VICE_DIRECTOR.getValue() + "】角色用户！");
                }
                variables.put("user", dealUserDto.getLoginName());

                //更改工作方案信息
                businessId = flowDto.getBusinessMap().get("A_WP_ID").toString();
                wk = workProgramRepo.findById(WorkProgram_.id.getName(), businessId);
                wk.setMinisterSuggesttion(flowDto.getDealOption());
                wk.setMinisterDate(new Date());
                wk.setMinisterName(currentUser.getLoginName());
                workProgramRepo.save(wk);
                break;
            case Constant.FLOW_FGLD_SP_GZFA1:        //分管副主任审批-主流程
                businessId = flowDto.getBusinessMap().get("M_WP_ID").toString();
                wk = workProgramRepo.findById(WorkProgram_.id.getName(), businessId);
                wk.setLeaderSuggesttion(flowDto.getDealOption());
                wk.setLeaderDate(new Date());
                wk.setLeaderName(currentUser.getLoginName());
                workProgramRepo.save(wk);

                List<User> dealUserList = signPrincipalService.getSignPriUser(signid, EnumState.YES.getValue());
                for (int i = 0, l = dealUserList.size(); i < l; i++) {
                    if (i == 0) {
                        assigneeValue = dealUserList.get(i).getLoginName();
                    } else {
                        assigneeValue += "," + dealUserList.get(i).getLoginName();
                    }
                }
                variables.put("users", assigneeValue);
                break;
            case Constant.FLOW_FGLD_SP_GZFA2:        //分管副主任审批
                businessId = flowDto.getBusinessMap().get("A_WP_ID").toString();
                wk = workProgramRepo.findById(WorkProgram_.id.getName(), businessId);
                wk.setMinisterSuggesttion(flowDto.getDealOption());
                wk.setLeaderDate(new Date());
                wk.setLeaderName(currentUser.getLoginName());
                workProgramRepo.save(wk);

                //查找项目负责人
                List<User> dealUserList2 = signPrincipalService.getSignPriUser(signid, EnumState.YES.getValue());
                for (int i = 0, l = dealUserList2.size(); i < l; i++) {
                    if (i == 0) {
                        assigneeValue = dealUserList2.get(i).getLoginName();
                    } else {
                        assigneeValue += "," + dealUserList2.get(i).getLoginName();
                    }
                }
                variables.put("users", assigneeValue);

                break;
            case Constant.FLOW_FW_SQ:                //发文申请
                sign = signRepo.findById(Sign_.signid.getName(), signid);
                if (!signPrincipalService.isMainPri(currentUser.getLoginUser().getId(), signid, EnumState.YES.getValue())) {
                    return new ResultMsg(false, MsgCode.ERROR.getValue(), "您不是第一负责人，不能进行下一步操作！");
                }
                dealUserDto = userService.getOrgDirector();
                if (dealUserDto == null) {
                    return new ResultMsg(false, MsgCode.ERROR.getValue(), "请先设置该部门的部门领导！");
                }
                variables.put("user", dealUserDto.getLoginName());
                break;
            case Constant.FLOW_BZ_SP_FW:            //部长审批发文
                dealUserDto = userService.getOrgSLeader();
                if (dealUserDto == null) {
                    return new ResultMsg(false, MsgCode.ERROR.getValue(), "请先设置该部门的分管领导！");
                }
                variables.put("user", dealUserDto.getLoginName());
                //修改发文信息
                businessId = flowDto.getBusinessMap().get("DIS_ID").toString();
                dp = dispatchDocRepo.findById(DispatchDoc_.id.getName(), businessId);
                dp.setMinisterSuggesttion(flowDto.getDealOption());
                dp.setMinisterDate(new Date());
                dp.setMinisterName(currentUser.getLoginName());
                dispatchDocRepo.save(dp);
                break;
            case Constant.FLOW_FGLD_SP_FW:            //分管领导审批发文
                userList = userService.findUserByRoleName(EnumFlowNodeGroupName.DIRECTOR.getValue());
                if (userList == null || userList.size() == 0) {
                    return new ResultMsg(false, MsgCode.ERROR.getValue(), "请先设置【" + EnumFlowNodeGroupName.DIRECTOR.getValue() + "】角色用户！");
                }
                variables.put("user", userList.get(0).getLoginName());

                //修改发文信息
                businessId = flowDto.getBusinessMap().get("DIS_ID").toString();
                dp = dispatchDocRepo.findById(DispatchDoc_.id.getName(), businessId);
                dp.setViceDirectorSuggesttion(flowDto.getDealOption());
                dp.setViceDirectorDate(new Date());
                dp.setViceDirectorName(currentUser.getLoginName());
                dispatchDocRepo.save(dp);
                break;
            case Constant.FLOW_ZR_SP_FW:            //主任审批发文
                businessId = flowDto.getBusinessMap().get("DIS_ID").toString();
                dp = dispatchDocRepo.findById(DispatchDoc_.id.getName(), businessId);
                dp.setDirectorSuggesttion(flowDto.getDealOption());
                dp.setDirectorDate(new Date());
                dp.setDirectorName(currentUser.getLoginName());
                dispatchDocRepo.save(dp);

                //第一负责人归档
                User firstUser = signPrincipalService.getMainPriUser(signid, EnumState.YES.getValue());
                variables.put("user", firstUser.getLoginName());
                break;
            case Constant.FLOW_MFZR_GD:                //第一负责人归档
                sign = signRepo.findById(Sign_.signid.getName(), signid);
                //有第二负责人，则跳转到第二负责人审核
                List<User> secondUserList = signPrincipalService.getAllSecondPriUser(processInstance.getBusinessKey(), Constant.EnumState.YES.getValue());
                if (secondUserList != null && secondUserList.size() > 0) {
                    if (flowDto.getBusinessMap().get("SEPRI_ID") != null && Validate.isString(flowDto.getBusinessMap().get("SEPRI_ID").toString())) {
                        for (User user : secondUserList) {
                            if ((flowDto.getBusinessMap().get("SEPRI_ID").toString()).equals(user.getId())) {
                                variables.put("assistuser", Integer.valueOf(EnumState.YES.getValue()));
                                variables.put("user", user.getLoginName());
                                break;
                            }
                        }
                    } else {
                        return new ResultMsg(false, MsgCode.ERROR.getValue(), "请先选择第二负责人审核！");
                    }
                } else {
                    variables.put("assistuser", Integer.valueOf(EnumState.NO.getValue()));
                    userList = userService.findUserByRoleName(EnumFlowNodeGroupName.FILER.getValue());
                    if (userList == null || userList.size() == 0) {
                        return new ResultMsg(false, MsgCode.ERROR.getValue(), "请先设置【" + EnumFlowNodeGroupName.FILER.getValue() + "】角色用户！");
                    }
                    dealUserDto = userList.get(0);
                    variables.put("user", dealUserDto.getLoginName());
                }

                break;
            case Constant.FLOW_AZFR_SP_GD:            //第二负责人审批归档
                userList = userService.findUserByRoleName(EnumFlowNodeGroupName.FILER.getValue());
                if (userList == null || userList.size() == 0) {
                    return new ResultMsg(false, MsgCode.ERROR.getValue(), "请先设置【" + EnumFlowNodeGroupName.FILER.getValue() + "】角色用户！");
                }
                dealUserDto = userList.get(0);
                variables.put("user", dealUserDto.getLoginName());
                //更改第二负责人
                sign = signRepo.findById(Sign_.signid.getName(), signid);
                sign.setSecondPriUser(currentUser.getLoginName());
                saveSignFlag = true;
                break;
            case Constant.FLOW_BMLD_QR_GD:            //确认归档
                sign = signRepo.findById(signid);

                FileRecord fileRecord = sign.getFileRecord();
                fileRecord.setFileDate(new Date());
                fileRecord.setSignUserid(currentUser.getLoginUser().getId());
                fileRecordRepo.save(fileRecord);

                sign.setSignState(EnumState.YES.getValue());    //更改状态
                saveSignFlag = true;
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

    /**
     * 协审流程处理
     *
     * @param processInstance
     * @param flowDto
     * @return
     */
    @Override
    public ResultMsg dealXSFlow(ProcessInstance processInstance, FlowDto flowDto) {
        Task task = null;
        if (Validate.isString(flowDto.getTaskId())) {
            task = taskService.createTaskQuery().taskId(flowDto.getTaskId()).active().singleResult();
        } else {
            task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).active().singleResult();
        }
        if (task == null) {
            return new ResultMsg(false, MsgCode.ERROR.getValue(), "该流程已被处理！");
        }
        if(task.isSuspended()){
            return new ResultMsg(false, MsgCode.ERROR.getValue(), "项目已暂停，不能进行操作！");
        }
        //参数定义

        String signid = ActivitiUtil.getProcessBusinessKey(processInstance.getBusinessKey()), businessId = "", assigneeValue = "";
        Sign sign = null;
        WorkProgram wk = null;
        DispatchDoc dp = null;
        List<UserDto> userList = null;
        UserDto dealUser = null;
        List<SignPrincipal> signPriList = null;        //项目负责人
        boolean saveSignFlag = false;
        Map<String, Object> variables = processInstance.getProcessVariables();

        //流程处理
        switch (flowDto.getCurNode().getActivitiId()) {
            case Constant.FLOW_XS_ZR:                       //主任
                break;
            case Constant.FLOW_XS_XMQS:                     //项目签收
                userList = userService.findUserByRoleName(EnumFlowNodeGroupName.COMM_DEPT_DIRECTOR.getValue());
                if (userList == null || userList.size() == 0) {
                    return new ResultMsg(false, MsgCode.ERROR.getValue(), "请先设置【" + EnumFlowNodeGroupName.COMM_DEPT_DIRECTOR.getValue() + "】角色用户！");
                }
                variables.put("user", userList.get(0).getLoginName());

                sign = signRepo.findById(Sign_.signid.getName(), signid);
                sign.setSigndate(new Date());
                sign.setIssign(EnumState.YES.getValue());
                saveSignFlag = true;
                break;
            case Constant.FLOW_XS_ZHBBL:                    //综合部审批
                if (flowDto.getBusinessMap().get("FGLD") == null) {
                    return new ResultMsg(false, MsgCode.ERROR.getValue(), "请选择分管领导！");
                }
                variables.put("user", flowDto.getBusinessMap().get("FGLD").toString());
                //修改发文信息
                sign = signRepo.findById(Sign_.signid.getName(), signid);
                sign.setComprehensivehandlesug(flowDto.getDealOption());
                sign.setComprehensiveDate(new Date());
                sign.setComprehensiveName(currentUser.getLoginName());
                saveSignFlag = true;
                break;
            case Constant.FLOW_XS_FGLD_SP:                  //分管副主任
                userList = userService.findUserByRoleName(EnumFlowNodeGroupName.DEPT_LEADER.getValue());
                if (userList == null || userList.size() == 0) {
                    return new ResultMsg(false, "", "请先设置【" + EnumFlowNodeGroupName.DEPT_LEADER.getValue() + "】角色用户！");
                }

                //修改发文信息
                sign = signRepo.findById(Sign_.signid.getName(), signid);
                sign.setLeaderhandlesug(flowDto.getDealOption());
                sign.setLeaderDate(new Date());
                sign.setLeaderName(currentUser.getLoginName());

                //判断选择的分办部门
                if (flowDto.getBusinessMap().get("deptid") != null) {
                    Org org = orgRepo.findById(flowDto.getBusinessMap().get("deptid").toString());
                    dealUser = userService.filterOrgDirector(userList, org);
                    if (dealUser == null || !Validate.isString(dealUser.getLoginName())) {
                        return new ResultMsg(false, MsgCode.ERROR.getValue(), "请先设置【" + org.getName() + "】的" + EnumFlowNodeGroupName.DEPT_LEADER.getValue()
                                + "，设置的用户角色必须为【" + EnumFlowNodeGroupName.DEPT_LEADER.getValue() + "】！");
                    }
                    sign.setmOrgId(org.getId());    //设置主办部门
                    variables.put("user", dealUser.getLoginName());
                } else {
                    return new ResultMsg(false, MsgCode.ERROR.getValue(), "必须要设置主办部门！");
                }
                saveSignFlag = true;
                break;
            case Constant.FLOW_XS_BMFB:                     //部门分办
                if (flowDto.getBusinessMap().get("PRINCIPAL") == null) {
                    return new ResultMsg(false, MsgCode.ERROR.getValue(), "请选择项目负责人！");
                }
                List<SignPrincipalDto> principalUserList = JSON.parseArray(flowDto.getBusinessMap().get("PRINCIPAL").toString(), SignPrincipalDto.class);
                if (principalUserList == null || principalUserList.size() == 0) {
                    return new ResultMsg(false, MsgCode.ERROR.getValue(), "请选择项目负责人！");
                }

                sign = signRepo.findById(Sign_.signid.getName(), signid);
                sign.setMinisterhandlesug(flowDto.getDealOption());
                sign.setMinisterDate(new Date());
                sign.setMinisterName(currentUser.getLoginName());

                int totalL = principalUserList.size();

                List<SignPrincipal> saveList = new ArrayList<>(totalL);
                for (int i = 0; i < totalL; i++) {
                    SignPrincipalDto obj = principalUserList.get(i);
                    if (i > 0) {
                        assigneeValue += ",";
                    }
                    assigneeValue += obj.getUserName();

                    SignPrincipal spriUser = new SignPrincipal();
                    BeanCopierUtils.copyProperties(obj, spriUser);
                    spriUser.setSingId(signid);
                    spriUser.setSort(i);
                    spriUser.setIsMainFlow(EnumState.YES.getValue());
                    if (!Validate.isString(spriUser.getIsMainUser())) {
                        spriUser.setIsMainUser(EnumState.NO.getValue());
                    }
                    saveList.add(spriUser);
                }
                signPrincipalRepo.bathUpdate(saveList);

                variables.put("users", assigneeValue);
                saveSignFlag = true;
                break;
            case Constant.FLOW_XS_XMFZR_GZFA:               //项目负责人承办
                if (!signPrincipalService.isMainPri(currentUser.getLoginUser().getId(), signid, EnumState.YES.getValue())) {
                    return new ResultMsg(false, MsgCode.ERROR.getValue(), "您不是项目总负责人，不能进行下一步操作！");
                }
                sign = signRepo.findById(Sign_.signid.getName(), signid);
                //如果选择评审方案，则要走评审方案审批流程
                if (EnumState.YES.getValue().equals(sign.getIsNeedWrokPrograml()) &&
                        (flowDto.getBusinessMap().get("PSFA") != null && EnumState.YES.getValue().equals(flowDto.getBusinessMap().get("PSFA").toString()))) {
                    variables.put("psfa", EnumState.YES.getValue());
                    dealUser = userService.getOrgDirector();
                    if (dealUser == null) {
                        return new ResultMsg(false, MsgCode.ERROR.getValue(), "请先设置" + EnumFlowNodeGroupName.DEPT_LEADER.getValue() + "，然后重新登录处理即可！");
                    }
                    variables.put("user", dealUser.getLoginName());
                    //否则直接发文
                } else {
                    sign.setIsNeedWrokPrograml(EnumState.NO.getValue());
                    saveSignFlag = true;

                    variables.put("psfa", EnumState.NO.getValue());
                    List<User> dealUserList = signPrincipalService.getSignPriUser(signid, null);
                    for (int i = 0, l = dealUserList.size(); i < l; i++) {
                        if (i == 0) {
                            assigneeValue = dealUserList.get(i).getLoginName();
                        } else {
                            assigneeValue += "," + dealUserList.get(i).getLoginName();
                        }
                    }
                    variables.put("users", assigneeValue);
                }
                break;
            case Constant.FLOW_XS_BZSP_GZFA:                //部长审批
                //获取分管领导
                dealUser = userService.getOrgSLeader();
                if (dealUser == null) {
                    return new ResultMsg(false, MsgCode.ERROR.getValue(), "请先设置该部门的【" + EnumFlowNodeGroupName.VICE_DIRECTOR.getValue() + "】角色用户！");
                }
                variables.put("user", dealUser.getLoginName());
                //更改工作方案信息
                businessId = flowDto.getBusinessMap().get("WP_ID").toString();
                wk = workProgramRepo.findById(WorkProgram_.id.getName(), businessId);
                wk.setMinisterSuggesttion(flowDto.getDealOption());
                wk.setMinisterDate(new Date());
                wk.setMinisterName(currentUser.getLoginName());
                workProgramRepo.save(wk);
                break;
            case Constant.FLOW_XS_FGLDSP_GZFA:              //分管副主任审批
                businessId = flowDto.getBusinessMap().get("WP_ID").toString();
                wk = workProgramRepo.findById(WorkProgram_.id.getName(), businessId);
                wk.setLeaderSuggesttion(flowDto.getDealOption());
                wk.setLeaderDate(new Date());
                wk.setLeaderName(currentUser.getLoginName());
                workProgramRepo.save(wk);

                //查找项目负责人
                List<User> dealUserList = signPrincipalService.getSignPriUser(signid, EnumState.YES.getValue());
                for (int i = 0, l = dealUserList.size(); i < l; i++) {
                    if (i == 0) {
                        assigneeValue = dealUserList.get(i).getLoginName();
                    } else {
                        assigneeValue += "," + dealUserList.get(i).getLoginName();
                    }
                }
                variables.put("users", assigneeValue);
                break;
            case Constant.FLOW_XS_FW:                       //发文申请
                sign = signRepo.findById(Sign_.signid.getName(), signid);
                if (!signPrincipalService.isMainPri(currentUser.getLoginUser().getId(), signid, EnumState.YES.getValue())) {
                    return new ResultMsg(false, MsgCode.ERROR.getValue(), "您不是项目总负责人人，不能进行下一步操作！");
                }
                dealUser = userService.getOrgDirector();
                if (dealUser == null) {
                    return new ResultMsg(false, MsgCode.ERROR.getValue(), "请先设置该部门的部门领导！");
                }
                variables.put("user", dealUser.getLoginName());
                break;
            case Constant.FLOW_XS_BZSP_FW:                  //部长审批发文
                dealUser = userService.getOrgSLeader();
                if (dealUser == null) {
                    return new ResultMsg(false, MsgCode.ERROR.getValue(), "请先设置该部门的分管领导！");
                }
                variables.put("user", dealUser.getLoginName());
                //修改发文信息
                businessId = flowDto.getBusinessMap().get("DIS_ID").toString();
                dp = dispatchDocRepo.findById(DispatchDoc_.id.getName(), businessId);
                dp.setMinisterSuggesttion(flowDto.getDealOption());
                dp.setMinisterDate(new Date());
                dp.setMinisterName(currentUser.getLoginName());
                dispatchDocRepo.save(dp);
                break;
            case Constant.FLOW_XS_FGLDSP_FW:                //分管领导审批发文
                userList = userService.findUserByRoleName(EnumFlowNodeGroupName.DIRECTOR.getValue());
                if (userList == null || userList.size() == 0) {
                    return new ResultMsg(false, MsgCode.ERROR.getValue(), "请先设置【" + EnumFlowNodeGroupName.DIRECTOR.getValue() + "】角色用户！");
                }
                variables.put("user", userList.get(0).getLoginName());
                //修改发文信息
                businessId = flowDto.getBusinessMap().get("DIS_ID").toString();
                dp = dispatchDocRepo.findById(DispatchDoc_.id.getName(), businessId);
                dp.setViceDirectorSuggesttion(flowDto.getDealOption());
                dp.setViceDirectorDate(new Date());
                dp.setViceDirectorName(currentUser.getLoginName());
                dispatchDocRepo.save(dp);
                break;
            case Constant.FLOW_XS_ZRSP_FW:                  //主任审批发文
                businessId = flowDto.getBusinessMap().get("DIS_ID").toString();
                dp = dispatchDocRepo.findById(DispatchDoc_.id.getName(), businessId);
                dp.setDirectorSuggesttion(flowDto.getDealOption());
                dp.setDirectorDate(new Date());
                dp.setDirectorName(currentUser.getLoginName());
                dispatchDocRepo.save(dp);

                //第一负责人归档
                User totalUser = signPrincipalService.getMainPriUser(signid, EnumState.YES.getValue());
                variables.put("user", totalUser.getLoginName());
                break;
            case Constant.FLOW_XS_FZR_GD:                   //第一负责人归档
                sign = signRepo.findById(Sign_.signid.getName(), signid);

                //有第二负责人，则跳转到第二负责人审核
                List<User> secondUserList = signPrincipalService.getAllSecondPriUser(processInstance.getBusinessKey(), Constant.EnumState.YES.getValue());
                if (secondUserList != null && secondUserList.size() > 0) {
                    if (flowDto.getBusinessMap().get("SEPRI_ID") != null && Validate.isString(flowDto.getBusinessMap().get("SEPRI_ID").toString())) {
                        for (User user : secondUserList) {
                            if ((flowDto.getBusinessMap().get("SEPRI_ID").toString()).equals(user.getId())) {
                                variables.put("assistuser", Integer.valueOf(EnumState.YES.getValue()));
                                variables.put("user", user.getLoginName());
                                break;
                            }
                        }
                    } else {
                        return new ResultMsg(false, MsgCode.ERROR.getValue(), "请先选择第二负责人审核！");
                    }
                } else {
                    variables.put("assistuser", Integer.valueOf(EnumState.NO.getValue()));
                    userList = userService.findUserByRoleName(EnumFlowNodeGroupName.FILER.getValue());
                    if (userList == null || userList.size() == 0) {
                        return new ResultMsg(false, MsgCode.ERROR.getValue(), "请先设置【" + EnumFlowNodeGroupName.FILER.getValue() + "】角色用户！");
                    }
                    dealUser = userList.get(0);
                    variables.put("user", dealUser.getLoginName());
                }

                break;
            case Constant.FLOW_XS_FZR_SP:                   //第二负责人审批归档
                userList = userService.findUserByRoleName(EnumFlowNodeGroupName.FILER.getValue());
                if (userList == null || userList.size() == 0) {
                    return new ResultMsg(false, MsgCode.ERROR.getValue(), "请先设置【" + EnumFlowNodeGroupName.FILER.getValue() + "】角色用户！");
                }
                dealUser = userList.get(0);
                variables.put("user", dealUser.getLoginName());
                //更改第二负责人
                sign = signRepo.findById(Sign_.signid.getName(), signid);
                sign.setSecondPriUser(currentUser.getLoginName());
                saveSignFlag = true;
                break;
            case Constant.FLOW_XS_QRGD:                     //确认归档
                sign = signRepo.findById(signid);

                FileRecord fileRecord = sign.getFileRecord();
                fileRecord.setFileDate(new Date());
                fileRecord.setSignUserid(currentUser.getLoginUser().getId());
                fileRecordRepo.save(fileRecord);

                sign.setSignState(EnumState.YES.getValue());    //更改状态
                saveSignFlag = true;
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
    public void updateAssistState(String signIds, String status, boolean isSingle) {
        //更新项目状态
        HqlBuilder hqlBuilder = HqlBuilder.create();
        hqlBuilder.append(" update " + Sign.class.getSimpleName() + " set " + Sign_.isassistproc.getName() + " =:isassistproc");
        hqlBuilder.setParam("isassistproc", status);
        if (isSingle) {
            hqlBuilder.append(" where " + Sign_.signid.getName() + " =:signid").setParam("signid", signIds);
        } else {
            List<String> singIdList = StringUtil.getSplit(signIds, ",");
            if (singIdList.size() == 1) {
                hqlBuilder.append(" where " + Sign_.signid.getName() + " =:signid").setParam("signid", singIdList.get(0));
            } else {
                hqlBuilder.append(" where " + Sign_.signid.getName() + " in ( ");
                for (int i = 0, l = singIdList.size(); i < l; i++) {
                    if (i == l - 1) {
                        hqlBuilder.append(" :id" + i).setParam("id" + i, singIdList.get(i));
                    } else {
                        hqlBuilder.append(" :id" + i + ",").setParam("id" + i, singIdList.get(i));
                    }
                }
                hqlBuilder.append(" )");
            }
        }
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
     * @return
     */
    @Override
    @Transactional
    public List<Sign> selectSignNotFinish() {
        HqlBuilder hqlBuilder = HqlBuilder.create();
        hqlBuilder.append("select s from " + Sign.class.getSimpleName() + " s where s." + Sign_.signState.getName() + "=:state ");
        hqlBuilder.setParam("state", EnumState.PROCESS.getValue());
        hqlBuilder.append(" and s."+Sign_.issign.getName()+" =:issign").setParam("issign",EnumState.YES.getValue());
        List<Sign> signList = signRepo.findByHql(hqlBuilder);
        return signList;
    }

    @Override
    @Transactional
    public void bathUpdate(List<Sign> signList) {
        signRepo.bathUpdate(signList);
    }


    //获取部长所在部门的项目信息
    @Override
    public Map<String, Object> initSignList() {

        Map<String, Object> map = new HashMap<>();

        //添加部门
        List<Org> orgsList = new ArrayList<>();
        List<Org> orgList = orgRepo.findAll();
        if (!orgList.isEmpty()) {
            for (Org org : orgList) {
                Org orgs = new Org();
                orgs.setName(org.getName());
                orgs.setId(org.getId());
                orgsList.add(orgs);
            }
        }
        /*//获取当前用户的部门id
        User curruser = userRepo.findById(currentUser.getLoginUser().getId());
        List<Role> roleList = curruser.getRoles();
        //判断角色
        if (!roleList.isEmpty()) {
            String orgId = "";
            for (Role role : roleList) {
                if (role.getRoleName().equals(Constant.EnumFlowNodeGroupName.DIRECTOR.getValue())) {
                    map.put("orgMLeaderName", currentUser.getLoginUser().getLoginName());
                } else if (role.getRoleName().equals(Constant.EnumFlowNodeGroupName.VICE_DIRECTOR.getValue())) {
                    map.put("orgSLeaderName", currentUser.getLoginUser().getLoginName());
                } else {
                    map.put("orgdirectorname", currentUser.getLoginUser().getLoginName());
                }

            }
        }*/
        List<User> userList = new ArrayList<>();
        userList = userRepo.findAll();
        List<User> usersList = new ArrayList<>();
        if (!userList.isEmpty()) {
            for (User user : userList) {
                User users = new User();
                users.setLoginName(user.getLoginName());
                usersList.add(users);
            }
        }
        map.put("usersList", usersList);
        map.put("orgsList", orgsList);
        return map;
    }

    /**
     * 正式签收
     * @param signId
     */
    @Override
    public ResultMsg realSign(String signId) {
        Sign sign = signRepo.findById(Sign_.signid.getName(),signId);
        if(Validate.isString(sign.getIssign()) && EnumState.YES.getValue().equals(sign.getIssign())){
            return new ResultMsg(false,MsgCode.ERROR.getValue(),"操作失败，该项目已经签收完毕！");
        }
        boolean is15Days = (Validate.isString(sign.getReviewstage())&&("可行性研究报告".equals(sign.getReviewstage())|| "项目概算".equals(sign.getReviewstage())));
        HqlBuilder sqlBuilder = HqlBuilder.create();
        sqlBuilder.append(" update cs_sign set "+Sign_.signdate.getName()+" = sysdate,");
        sqlBuilder.append(Sign_.issign.getName()+" =:issign ").setParam("issign",EnumState.YES.getValue());
        sqlBuilder.append(" ,"+Sign_.surplusdays.getName()+" = :surplusdays  ");
        if(is15Days){
            sqlBuilder.setParam("surplusdays",Constant.WORK_DAY_15);
        }else{
            sqlBuilder.setParam("surplusdays",Constant.WORK_DAY_12);
        }
        sqlBuilder.bulidIdString("where",Sign_.signid.getName(),signId);
        signRepo.executeSql(sqlBuilder);

        return new ResultMsg(true,MsgCode.OK.getValue(),"操作成功！");
    }

    @Override
    public PageModelDto<SignDispaWork> getSign(ODataObj odataObj, String skip, String top) {
        PageModelDto<SignDispaWork> pageModelDto = new PageModelDto<SignDispaWork>();
        Criteria criteria = signDispaWorkRepo.getExecutableCriteria();
        // 处理filter
        if (odataObj.getFilter() != null) {
            for (ODataFilterItem oDataFilterItem : odataObj.getFilter()) {
                String field = oDataFilterItem.getField();
                String operator = oDataFilterItem.getOperator();
                Object value = oDataFilterItem.getValue();
                switch (operator) {
                    case "like":
                        criteria.add(Restrictions.like(field, "%" + value + "%"));
                        break;
                    case "eq":
                        criteria.add(Restrictions.eq(field, value));
                        break;
                    case "ne":
                        criteria.add(Restrictions.ne(field, value));
                        break;
                    default:
                        break;
                }
            }
        }

        Disjunction dis = Restrictions.disjunction();
        dis.add(Restrictions.eq(SignDispaWork_.orgdirectorname.getName(), currentUser.getLoginName()));
        dis.add(Restrictions.eq(SignDispaWork_.orgMLeaderName.getName(), currentUser.getLoginName()));
        dis.add(Restrictions.eq(SignDispaWork_.orgSLeaderName.getName(), currentUser.getLoginName()));
        criteria.add(dis);


        Integer totalResult = ((Number) criteria.setProjection(Projections.rowCount()).uniqueResult()).intValue();
        criteria.setProjection(null);
        // 处理分页
        if (Validate.isString(skip)) {
            criteria.setFirstResult(Integer.valueOf(skip));
        }
        if (Validate.isString(top)) {
            criteria.setMaxResults(Integer.valueOf(top));
        }
        List<SignDispaWork> signDispaWork = criteria.list();
        pageModelDto.setCount(totalResult);
        pageModelDto.setValue(signDispaWork);
        return pageModelDto;
    }
    

}
