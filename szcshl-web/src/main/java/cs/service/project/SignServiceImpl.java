package cs.service.project;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import cs.domain.expert.ExpertReview_;
import cs.domain.expert.ExpertSelCondition;
import cs.domain.expert.ExpertSelected;
import cs.domain.flow.RuProcessTask;
import cs.domain.flow.RuProcessTask_;
import cs.domain.meeting.RoomBooking_;
import cs.domain.project.*;
import cs.domain.sys.*;
import cs.model.project.*;
import cs.model.sys.SysConfigDto;
import cs.repository.repositoryImpl.expert.ExpertSelConditionRepo;
import cs.repository.repositoryImpl.expert.ExpertSelectedRepo;
import cs.repository.repositoryImpl.flow.RuProcessTaskRepo;
import cs.repository.repositoryImpl.meeting.RoomBookingRepo;
import cs.repository.repositoryImpl.project.*;
import cs.repository.repositoryImpl.sys.*;
import cs.service.sys.SysConfigService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;

import cs.common.Constant;
import cs.common.Constant.EnumFlowNodeGroupName;
import cs.common.Constant.EnumState;
import cs.common.Constant.MsgCode;
import cs.common.FlowConstant;
import cs.common.HqlBuilder;
import cs.common.ResultMsg;
import cs.common.utils.ActivitiUtil;
import cs.common.utils.BeanCopierUtils;
import cs.common.utils.DateUtils;
import cs.common.utils.SessionUtil;
import cs.common.utils.StringUtil;
import cs.common.utils.Validate;
import cs.domain.expert.ExpertReview;
import cs.domain.external.Dept;
import cs.model.PageModelDto;
import cs.model.expert.ExpertReviewDto;
import cs.model.external.DeptDto;
import cs.model.external.OfficeUserDto;
import cs.model.flow.FlowDto;
import cs.model.sys.OrgDto;
import cs.model.sys.UserDto;
import cs.repository.odata.ODataObj;
import cs.repository.repositoryImpl.expert.ExpertReviewRepo;
import cs.repository.repositoryImpl.external.DeptRepo;
import cs.service.external.OfficeUserService;
import cs.service.flow.FlowService;
import cs.service.rtx.RTXSendMsgPool;

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
    private FlowService flowService;
    @Autowired
    private SignBranchRepo signBranchRepo;
    @Autowired
    private WorkProgramService workProgramService;
    //部门（小组）列表
    @Autowired
    private OrgDeptRepo orgDeptRepo;
    @Autowired
    private ExpertReviewRepo expertReviewRepo;
    @Autowired
    private AddRegisterFileRepo addRegisterFileRepo;
    @Autowired
    private AddSuppLetterRepo addSuppLetterRepo;
    @Autowired
    private SignMergeRepo signMergeRepo;
    @Autowired
    private RoomBookingRepo roomBookingRepo;
    @Autowired
    private SysConfigService sysConfigService;
    @Autowired
    private RuProcessTaskRepo ruProcessTaskRepo;
    @Autowired
    private ExpertSelConditionRepo expertSelConditionRepo;
    @Autowired
    private ExpertSelectedRepo expertSelectedRepo;
    @Autowired
    private AssistPlanRepo assistPlanRepo;
    /**
     * 项目签收保存操作（这里的方法是正式签收）
     *
     * @param signDto
     */
    @Override
    @Transactional
    public ResultMsg createSign(SignDto signDto) {
        if (!Validate.isString(signDto.getFilecode())) {
            return new ResultMsg(false, MsgCode.ERROR.getValue(), "保存失败，没有收文编号！");
        }
        Date now = new Date();
        Sign sign = null;
        //如果收文编号以0000结束，说明委里没有收文编号，这个编号可以有多个
        if (!signDto.getFilecode().endsWith("0000")) {
            signRepo.findByFilecode(signDto.getFilecode());
        }
        //1、根据收文编号获取项目信息
        if (sign == null) {
            sign = new Sign();
            //送来时间
            if (sign.getReceivedate() == null) {
                sign.setReceivedate(now);
            }
            BeanCopierUtils.copyProperties(signDto, sign);
            sign.setSignid(UUID.randomUUID().toString());
            sign.setSignState(EnumState.NORMAL.getValue());
            sign.setSigndate(now);
            sign.setIsLightUp(Constant.signEnumState.NOLIGHT.getValue());
            //2、是否是项目概算流程
            if (Constant.STAGE_BUDGET.equals(sign.getReviewstage()) || Validate.isString(sign.getIschangeEstimate())) {
                sign.setIsassistflow(EnumState.YES.getValue());
            } else {
                sign.setIsassistflow(EnumState.NO.getValue());
            }
            //3、送件人为当前签收人，
            sign.setSendusersign(SessionUtil.getDisplayName());

            //4、默认办理部门（项目建议书、可研为PX，概算为GX，其他为评估）
            if (Constant.STAGE_BUDGET.equals(sign.getReviewstage())) {
                sign.setDealOrgType(Constant.BusinessType.GX.getValue());
                sign.setLeaderhandlesug("请（概算一部 概算二部）组织评审。");
            } else {
                sign.setDealOrgType(Constant.BusinessType.PX.getValue());
                sign.setLeaderhandlesug("请（评估一部 评估二部 评估一部信息化组）组织评审。");
            }

            //5、综合部、分管副主任默认办理信息
            List<User> roleList = userRepo.findUserByRoleName(EnumFlowNodeGroupName.VICE_DIRECTOR.getValue());
            for (User user : roleList) {
                if (sign.getDealOrgType().equals(user.getMngOrgType())) {
                    sign.setLeaderId(user.getId());
                    sign.setLeaderName(user.getDisplayName());
                    sign.setComprehensivehandlesug("请" + (user.getDisplayName()).substring(0, 1) + "主任阅示。");
                    sign.setComprehensiveName("综合部");
                    sign.setComprehensiveDate(new Date());
                    break;
                }
            }
            ///6、创建人信息
            sign.setCreatedDate(now);
            sign.setModifiedDate(now);
            sign.setCreatedBy(SessionUtil.getDisplayName());
            sign.setModifiedBy(SessionUtil.getDisplayName());
        } else {
            BeanCopierUtils.copyPropertiesIgnoreNull(signDto, sign);
            sign.setModifiedDate(now);
            sign.setModifiedBy(SessionUtil.getDisplayName());
        }
        //6、收文编号
        if (!Validate.isString(sign.getSignNum())) {
            int maxSeq = findSignMaxSeqByType(sign.getDealOrgType(), sign.getSigndate());
            sign.setSignSeq(maxSeq + 1);
            String signSeqString = (maxSeq + 1) > 999 ? (maxSeq + 1) + "" : String.format("%03d", Integer.valueOf(maxSeq + 1));
            sign.setSignNum(DateUtils.converToString(new Date(), "yyyy") + sign.getDealOrgType() + signSeqString);
        }
        //7、正式签收
        if (Validate.isString(sign.getIssign()) || !EnumState.YES.getValue().equals(sign.getIssign())) {
            sign.setSigndate(now);
            sign.setIssign(EnumState.YES.getValue());       //正式签收

            Float reviewsDays = getReviewDays(sign.getReviewstage());
            if (reviewsDays > 0) {
                sign.setSurplusdays(Constant.WORK_DAY_15);
                sign.setReviewdays(Constant.WORK_DAY_15);
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
        } else {
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

    @Override
    public SignDto findById(String signid, boolean queryAll) {
        Sign sign = signRepo.findById(signid);
        SignDto signDto = new SignDto();
        BeanCopierUtils.copyProperties(sign, signDto);
        //查询所有的属性
        if (queryAll) {
            if (sign.getWorkProgramList() != null && sign.getWorkProgramList().size() > 0) {
                boolean isMergeReview = false;
                //判断是否是合并评审项目，如果是，则要获取合并评审信息
                if (sign.getWorkProgramList().size() == 1) {
                    WorkProgram workProgram = sign.getWorkProgramList().get(0);
                    // 合并评审
                    if (Constant.MergeType.REVIEW_MERGE.getValue().equals(workProgram.getIsSigle())) {
                        isMergeReview = true;
                        //主项目
                        if (EnumState.YES.getValue().equals(workProgram.getIsMainProject())) {
                            List<WorkProgramDto> workProgramDtoList = new ArrayList<>();
                            WorkProgramDto workProgramDto = new WorkProgramDto();
                            BeanCopierUtils.copyProperties(workProgram, workProgramDto);
                            workProgramRepo.initWPMeetingExp(workProgramDto, workProgram);
                            workProgramDto.setSignId(signid);
                            workProgramDtoList.add(workProgramDto);
                            //还得查找合并评审次项目
                            List<WorkProgramDto> wpDtoList = workProgramService.findMergeWP(signid);
                            if (Validate.isList(wpDtoList)) {
                                workProgramDtoList.addAll(wpDtoList);
                            }
                            signDto.setWorkProgramDtoList(workProgramDtoList);
                            //次项目
                        } else {
                            List<WorkProgramDto> workProgramDtoList = new ArrayList<>(sign.getWorkProgramList().size());
                            //查找主项目信息，并且获取主项目的会议室信息和专家信息
                            WorkProgram wp = workProgramRepo.findMainReviewWP(signid);
                            WorkProgramDto workProgramDto = new WorkProgramDto();
                            BeanCopierUtils.copyProperties(workProgram, workProgramDto);
                            workProgramRepo.initWPMeetingExp(workProgramDto, wp);
                            workProgramDto.setSignId(signid);
                            workProgramDtoList.add(workProgramDto);
                            signDto.setWorkProgramDtoList(workProgramDtoList);
                        }
                    }

                }

                if (!isMergeReview) {
                    List<WorkProgramDto> workProgramDtoList = new ArrayList<>(sign.getWorkProgramList().size());
                    //由于工作方案不是按主次顺便排序，则遍历工作方案，获取主工作方案
                    WorkProgram mainW = new WorkProgram();
               /*     WorkProgram mainW =workProgramRepo.findByPrincipalUser(signid);*/
                    for (int i = 0; i < sign.getWorkProgramList().size(); i++) {
                        WorkProgram workProgram = sign.getWorkProgramList().get(i);
                        if (workProgram != null && (EnumState.PROCESS.getValue()).equals(workProgram.getBranchId())) {
                            BeanCopierUtils.copyProperties(workProgram, mainW);
                            break;
                        }
                    }

                    sign.getWorkProgramList().forEach(workProgram -> {
                        WorkProgramDto workProgramDto = new WorkProgramDto();
                        BeanCopierUtils.copyProperties(workProgram, workProgramDto);
                        workProgramRepo.initWPMeetingExp(workProgramDto, workProgram);
                        workProgramDto.setSignId(signid);
                        //判断如不是主工作方案，则初始化公共部分
                        if (!(EnumState.PROCESS.getValue()).equals(workProgram.getBranchId())) {
                            workProgramDto.setSendFileUnit(mainW.getSendFileUnit()); //来文单位
                            workProgramDto.setSendFileUser(mainW.getSendFileUser());//来文单位联系人
                            workProgramDto.setBuildCompany(mainW.getBuildCompany());//建设单位
                            workProgramDto.setDesignCompany(mainW.getDesignCompany());//编制单位
                            workProgramDto.setMainDeptName(mainW.getMainDeptName());//主管部门名称
                            workProgramDto.setIsHaveEIA(mainW.getIsHaveEIA());//是否有环评
                            workProgramDto.setProjectType(mainW.getProjectType());//项目类别
                            workProgramDto.setProjectSubType(mainW.getProjectSubType());//小类
                            workProgramDto.setIndustryType(mainW.getIndustryType());//行业类别
                            workProgramDto.setContactPerson(mainW.getContactPerson());//联系人
                            workProgramDto.setContactPersonPhone(mainW.getContactPersonPhone());//联系人手机号
                            workProgramDto.setContactPersonTel(mainW.getContactPersonTel());//联系人电话
                            workProgramDto.setContactPersonFax(mainW.getContactPersonFax());//联系人传真
//                            workProgramDto.setBuildSize(mainW.getBuildSize());//建设规模
//                            workProgramDto.setBuildContent(mainW.getBuildContent());//建设内容
//                            workProgramDto.setProjectBackGround(mainW.getProjectBackGround());//项目背景
/*                            workProgramDto.setReviewOrgName(mainW.getReviewOrgName());//评估部门
                            workProgramDto.setMianChargeUserName(mainW.getMianChargeUserName());//第一负责人
                            workProgramDto.setSecondChargeUserName(mainW.getSecondChargeUserName());//第二负责人*/

                        }
                        workProgramDtoList.add(workProgramDto);
                    });
                    signDto.setWorkProgramDtoList(workProgramDtoList);
                }
            }

            if (sign.getDispatchDoc() != null && Validate.isString(sign.getDispatchDoc().getId())) {
                DispatchDocDto dispatchDocDto = new DispatchDocDto();
                BeanCopierUtils.copyProperties(sign.getDispatchDoc(), dispatchDocDto);
                signDto.setDispatchDocDto(dispatchDocDto);
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

            //专家评审方案
            ExpertReview expertReview = expertReviewRepo.findByBusinessId(signid);
            if (Validate.isObject(expertReview)) {
                ExpertReviewDto expertReviewDto = expertReviewRepo.formatReview(expertReview);
                signDto.setExpertReviewDto(expertReviewDto);
            }

            //拟补充资料函
            List<AddSuppLetter> suppLetterList = addSuppLetterRepo.findByIds(AddSuppLetter_.businessId.getName(), signid, null);
            if (Validate.isList(suppLetterList)) {
                List<AddSuppLetterDto> suppLetterDtoList = new ArrayList<>(suppLetterList.size());
                suppLetterList.forEach(al -> {
                    AddSuppLetterDto dto = new AddSuppLetterDto();
                    BeanCopierUtils.copyProperties(al, dto);
                    suppLetterDtoList.add(dto);
                });
                signDto.setSuppLetterDtoList(suppLetterDtoList);
            }

            //拟补充资料
            List<AddRegisterFile> registerFileList = addRegisterFileRepo.findByIds(AddRegisterFile_.businessId.getName(), signid, null);
            if (Validate.isList(registerFileList)) {
                List<AddRegisterFileDto> registerFileDtoList = new ArrayList<>(registerFileList.size());
                registerFileList.forEach(rl -> {
                    AddRegisterFileDto dto = new AddRegisterFileDto();
                    BeanCopierUtils.copyProperties(rl, dto);
                    registerFileDtoList.add(dto);
                });
                signDto.setRegisterFileDtoDtoList(registerFileDtoList);
            }

            //项目关联信息
            if (null != sign.getIsAssociate() && sign.getIsAssociate() == 1) {
                List<SignDto> signDtoList = new ArrayList<>();
                getPreAssociateDto(sign.getAssociateSign(), signDtoList);   //自身还没加上去，在页面处理
                signDto.setAssociateSignDtoList(signDtoList);
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

        try {
            //1、启动流程
            ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(FlowConstant.SIGN_FLOW, signid,
                    ActivitiUtil.setAssigneeValue(FlowConstant.SignFlowParams.USER_ZR.getValue(), SessionUtil.getUserId()));

            //2、设置流程实例名称
            processEngine.getRuntimeService().setProcessInstanceName(processInstance.getId(), sign.getProjectname());

            //3、更改项目状态
            sign.setProcessInstanceId(processInstance.getId());
            sign.setSignState(EnumState.PROCESS.getValue());
            sign.setProcessState(Constant.SignProcessState.IS_START.getValue());
            signRepo.save(sign);

            //4、跳过第一环节（主任审核）
            Task task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).active().singleResult();
            taskService.addComment(task.getId(), processInstance.getId(), "");    //添加处理信息
            taskService.complete(task.getId(), ActivitiUtil.setAssigneeValue(FlowConstant.SignFlowParams.USER_QS.getValue(), SessionUtil.getUserId()));

            //5、跳过第二环节（签收）
            task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).active().singleResult();
            taskService.addComment(task.getId(), processInstance.getId(), "");    //添加处理信息
            taskService.complete(task.getId(), ActivitiUtil.setAssigneeValue(FlowConstant.SignFlowParams.USER_ZHB.getValue(), SessionUtil.getUserId()));

            //6、跳过第三个环节（综合部拟办意见）
            task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).active().singleResult();
            taskService.addComment(task.getId(), processInstance.getId(), sign.getComprehensivehandlesug());    //综合部拟办意见
            //查询是否有待办人员
            User leadUser = userRepo.findById(User_.id.getName(), sign.getLeaderId());
            String assigneeValue = Validate.isString(leadUser.getTakeUserId()) ? leadUser.getTakeUserId() : leadUser.getId();
            taskService.complete(task.getId(), ActivitiUtil.setAssigneeValue(FlowConstant.SignFlowParams.USER_FGLD.getValue(), assigneeValue));
            //放入腾讯通消息缓冲池
            RTXSendMsgPool.getInstance().sendReceiverIdPool(task.getId(), assigneeValue);
            return new ResultMsg(true, MsgCode.OK.getValue(), "操作成功！");
        } catch (Exception e) {
            log.error("发起项目签收流程异常：" + e.getMessage());
            return new ResultMsg(true, MsgCode.OK.getValue(), "操作异常，错误信息已记录，请刷新重试或联系管理员处理！");
        }
    }

    @Override
    @Transactional
    public ResultMsg dealFlow(ProcessInstance processInstance, Task task, FlowDto flowDto) {
        //参数定义
        String signid = processInstance.getBusinessKey(),
                businessId = "",                    //前段传过来的业务ID
                assigneeValue = "",                 //环节处理人
                branchIndex = "";                   //分支序号
        Sign sign = null;                           //收文对象
        WorkProgram wk = null;                      //工作方案
        DispatchDoc dp = null;                      //发文
        FileRecord fileRecord = null;               //归档
        List<User> userList = null;                 //用户列表
        List<SignPrincipal> signPriList = null;     //项目负责人
        User dealUser = null;                       //处理人
        OrgDept orgDept = null;                     //部门和小组
        boolean isNextUser = false;                 //是否是下一环节处理人（主要是处理领导审批，目前主要有三个地方，部长审批工作方案，部长审批发文和分管领导审批发文）
        String nextNodeKey = "";                    //下一环节名称
        //取得之前的环节处理人信息
        Map<String, Object> variables = new HashMap<>();

        //以下是流程环节处理
        switch (task.getTaskDefinitionKey()) {
            //项目签收
            case FlowConstant.FLOW_SIGN_QS:
                task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).active().singleResult();
                taskService.addComment(task.getId(), processInstance.getId(), flowDto.getDealOption());    //添加处理信息
                taskService.complete(task.getId(), ActivitiUtil.setAssigneeValue(FlowConstant.SignFlowParams.USER_ZHB.getValue(), SessionUtil.getUserId()));

                sign = signRepo.findById(Sign_.signid.getName(), signid);
                if (!Validate.isString(sign.getLeaderName())) {
                    return new ResultMsg(false, MsgCode.ERROR.getValue(), "操作失败，请先设置默认办理部门！");
                }
                task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).active().singleResult();
                taskService.addComment(task.getId(), processInstance.getId(), sign.getComprehensivehandlesug());    //综合部拟办意见
                //完成综合部审批，查询是否有代办
                dealUser = userRepo.findById(User_.id.getName(), sign.getLeaderId());
                assigneeValue = Validate.isString(dealUser.getTakeUserId()) ? dealUser.getTakeUserId() : dealUser.getId();
                variables = ActivitiUtil.setAssigneeValue(FlowConstant.SignFlowParams.USER_FGLD.getValue(), assigneeValue);
                break;

            //分管副主任审批
            case FlowConstant.FLOW_SIGN_FGLD_FB:
                if (flowDto.getBusinessMap().get("MAIN_ORG") == null) {
                    return new ResultMsg(false, MsgCode.ERROR.getValue(), "请先选择主办部门！");
                }
                sign = signRepo.findById(Sign_.signid.getName(), signid);
                //流程分支
                List<SignBranch> saveBranchList = new ArrayList<>();
                businessId = flowDto.getBusinessMap().get("MAIN_ORG").toString();   //主办部门ID
                SignBranch signBranch1 = new SignBranch(signid, FlowConstant.SignFlowParams.BRANCH_INDEX1.getValue(), EnumState.YES.getValue(), EnumState.NO.getValue(), EnumState.YES.getValue(), businessId, EnumState.NO.getValue());
                saveBranchList.add(signBranch1);
                //设置流程参数
                variables.put(FlowConstant.SignFlowParams.BRANCH1.getValue(), true);
                orgDept = orgDeptRepo.findOrgDeptById(businessId);
                if (orgDept == null || !Validate.isString(orgDept.getDirectorID())) {
                    return new ResultMsg(false, MsgCode.ERROR.getValue(), "请设置" + orgDept.getName() + "的部门负责人！");
                }
                //查询处理人
                dealUser = userRepo.getCacheUserById(orgDept.getDirectorID());
                assigneeValue = Validate.isString(dealUser.getTakeUserId()) ? dealUser.getTakeUserId() : dealUser.getId();
                variables.put(FlowConstant.SignFlowParams.USER_BZ1.getValue(), assigneeValue);
                /******************  一下处理协办部门 ****************/
                variables.put(FlowConstant.SignFlowParams.BRANCH2.getValue(), false);
                variables.put(FlowConstant.SignFlowParams.BRANCH3.getValue(), false);
                variables.put(FlowConstant.SignFlowParams.BRANCH4.getValue(), false);
                List<String> assistOrgIdList = null;
                //协办流程分支
                if (flowDto.getBusinessMap().get("ASSIST_ORG") != null) {
                    businessId = flowDto.getBusinessMap().get("ASSIST_ORG").toString();
                    assistOrgIdList = StringUtil.getSplit(businessId, ",");
                }
                if (Validate.isList(assistOrgIdList)) {
                    if (assistOrgIdList.size() > 3) {
                        return new ResultMsg(false, MsgCode.ERROR.getValue(), "协办部门最多只能选择3个！");
                    }
                    for (int i = 2, l = (assistOrgIdList.size() + 2); i < l; i++) {
                        businessId = assistOrgIdList.get(i - 2);
                        SignBranch signBranch = new SignBranch(signid, String.valueOf(i), EnumState.YES.getValue(), EnumState.NO.getValue(), EnumState.NO.getValue(), businessId, EnumState.NO.getValue());
                        saveBranchList.add(signBranch);
                        //判断是否有部门负责人
                        orgDept = orgDeptRepo.findOrgDeptById(businessId);
                        if (orgDept == null || !Validate.isString(orgDept.getDirectorID())) {
                            return new ResultMsg(false, MsgCode.ERROR.getValue(), "请设置" + orgDept.getName() + "的部门负责人！");
                        }
                        dealUser = userRepo.getCacheUserById(orgDept.getDirectorID());
                        String userId = Validate.isString(dealUser.getTakeUserId()) ? dealUser.getTakeUserId() : dealUser.getId();
                        assigneeValue += "," + userId;
                        switch (i) {
                            case 2:
                                variables.put(FlowConstant.SignFlowParams.BRANCH2.getValue(), true);
                                variables.put(FlowConstant.SignFlowParams.USER_BZ2.getValue(), userId);
                                break;
                            case 3:
                                variables.put(FlowConstant.SignFlowParams.BRANCH3.getValue(), true);
                                variables.put(FlowConstant.SignFlowParams.USER_BZ3.getValue(), userId);
                                break;
                            case 4:
                                variables.put(FlowConstant.SignFlowParams.BRANCH4.getValue(), true);
                                variables.put(FlowConstant.SignFlowParams.USER_BZ4.getValue(), userId);
                                break;
                            default:
                                ;
                        }
                    }
                }
                signBranchRepo.bathUpdate(saveBranchList);
                //更改项目信息
                sign.setLeaderhandlesug(flowDto.getDealOption());
                sign.setLeaderDate(new Date());
                sign.setLeaderId(SessionUtil.getUserId());
                sign.setLeaderName(SessionUtil.getDisplayName());
                signRepo.save(sign);
                break;
            //部门分办1
            case FlowConstant.FLOW_SIGN_BMFB1:
                branchIndex = FlowConstant.SignFlowParams.BRANCH_INDEX1.getValue();
                //部门分办2
            case FlowConstant.FLOW_SIGN_BMFB2:
                if (!Validate.isString(branchIndex)) {
                    branchIndex = FlowConstant.SignFlowParams.BRANCH_INDEX2.getValue();
                }
                //部门分办3
            case FlowConstant.FLOW_SIGN_BMFB3:
                if (!Validate.isString(branchIndex)) {
                    branchIndex = FlowConstant.SignFlowParams.BRANCH_INDEX3.getValue();
                }
                //部门分办4
            case FlowConstant.FLOW_SIGN_BMFB4:
                if (!Validate.isString(branchIndex)) {
                    branchIndex = FlowConstant.SignFlowParams.BRANCH_INDEX4.getValue();
                }
                sign = signRepo.findById(Sign_.signid.getName(), signid);
                //1、如果是协审项目
                if (EnumState.YES.getValue().equals(sign.getIsassistflow())) {
                    if (flowDto.getBusinessMap().get("PRINCIPAL") == null) {
                        return new ResultMsg(false, MsgCode.ERROR.getValue(), "请选择项目负责人！");
                    }
                    signPriList = JSON.parseArray(flowDto.getBusinessMap().get("PRINCIPAL").toString(), SignPrincipal.class);
                    if (!Validate.isList(signPriList)) {
                        return new ResultMsg(false, MsgCode.ERROR.getValue(), "请选择项目负责人！");
                    }
                    for (int i = 0, l = signPriList.size(); i < l; i++) {
                        SignPrincipal obj = signPriList.get(i);
                        if (i > 0) {
                            assigneeValue += ",";
                        }
                        assigneeValue += obj.getUserId();
                        obj.setSignId(signid);
                        obj.setFlowBranch(branchIndex);
                    }
                    //不是协审项目
                } else {
                    //主流程处理，一定要有第一负责人
                    if (FlowConstant.SignFlowParams.BRANCH_INDEX1.getValue().equals(branchIndex)) {
                        if (flowDto.getBusinessMap().get("M_USER_ID") == null) {
                            return new ResultMsg(false, MsgCode.ERROR.getValue(), "请选择第一负责人！");
                        }
                        businessId = flowDto.getBusinessMap().get("M_USER_ID").toString();
                        //查询是否有代办
                        dealUser = userRepo.getCacheUserById(businessId);
                        assigneeValue = Validate.isString(dealUser.getTakeUserId()) ? dealUser.getTakeUserId() : dealUser.getId();

                        //设置项目负责人
                        signPriList = new ArrayList<>();
                        //String signId, String userId, String flowBranch, String userType, Integer sort, String isMainUser
                        SignPrincipal mainPri = new SignPrincipal(signid, dealUser.getId(), branchIndex, "", null, EnumState.YES.getValue());
                        signPriList.add(mainPri);

                        //更改项目信息
                        sign.setMinisterhandlesug(flowDto.getDealOption());
                        sign.setMinisterDate(new Date());
                        sign.setMinisterId(SessionUtil.getUserId());
                        sign.setMinisterName(SessionUtil.getDisplayName());

                    }
                    //项目负责人
                    if (flowDto.getBusinessMap().get("A_USER_ID") != null) {
                        businessId = flowDto.getBusinessMap().get("A_USER_ID").toString();
                        userList = userRepo.getCacheUserListById(businessId);
                        if (signPriList == null) {
                            signPriList = new ArrayList<>();
                        }
                        for (User user : userList) {
                            if (Validate.isString(assigneeValue)) {
                                assigneeValue += ",";
                            }
                            assigneeValue += user.getId();
                            SignPrincipal secondPri = new SignPrincipal(signid, user.getId(), branchIndex, "", null, EnumState.NO.getValue());
                            signPriList.add(secondPri);
                        }
                    } else {
                        //分支流程必须要选择第二负责人
                        if (!FlowConstant.SignFlowParams.BRANCH_INDEX1.getValue().equals(branchIndex)) {
                            return new ResultMsg(false, MsgCode.ERROR.getValue(), "请选择项目负责人！");
                        }
                    }
                }
                //保存项目负责人
                signPrincipalRepo.bathUpdate(signPriList);

                //查询办理人
                userList = userRepo.getCacheUserListById(assigneeValue);
                assigneeValue = "";
                for (int i = 0, l = userList.size(); i < l; i++) {
                    dealUser = userList.get(i);
                    if (i > 0) {
                        assigneeValue += ",";
                    }
                    assigneeValue += Validate.isString(dealUser.getTakeUserId()) ? dealUser.getTakeUserId() : dealUser.getId();
                }
                //设定下一环节处理人
                if (FlowConstant.SignFlowParams.BRANCH_INDEX1.getValue().equals(branchIndex)) {
                    variables.put(FlowConstant.SignFlowParams.USER_FZR1.getValue(), assigneeValue);
                } else if (FlowConstant.SignFlowParams.BRANCH_INDEX2.getValue().equals(branchIndex)) {
                    variables.put(FlowConstant.SignFlowParams.USER_FZR2.getValue(), assigneeValue);
                } else if (FlowConstant.SignFlowParams.BRANCH_INDEX3.getValue().equals(branchIndex)) {
                    variables.put(FlowConstant.SignFlowParams.USER_FZR3.getValue(), assigneeValue);
                } else if (FlowConstant.SignFlowParams.BRANCH_INDEX4.getValue().equals(branchIndex)) {
                    variables.put(FlowConstant.SignFlowParams.USER_FZR4.getValue(), assigneeValue);
                }
                //完成部门分办，表示正在做工作方案
                sign.setProcessState(Constant.SignProcessState.DO_WP.getValue());
                signRepo.save(sign);
                break;

            //项目负责人办理1
            case FlowConstant.FLOW_SIGN_XMFZR1:
                branchIndex = FlowConstant.SignFlowParams.BRANCH_INDEX1.getValue();
                //主流程要第一负责才能进行下一步操作
                if (!signPrincipalService.isMainPri(SessionUtil.getUserInfo().getId(), signid)) {
                    return new ResultMsg(false, MsgCode.ERROR.getValue(), "您不是第一负责人，不能进行下一步操作！");
                }
                //项目负责人办理2
            case FlowConstant.FLOW_SIGN_XMFZR2:
                if (!Validate.isString(branchIndex)) {
                    branchIndex = FlowConstant.SignFlowParams.BRANCH_INDEX2.getValue();
                }
                //项目负责人办理3
            case FlowConstant.FLOW_SIGN_XMFZR3:
                if (!Validate.isString(branchIndex)) {
                    branchIndex = FlowConstant.SignFlowParams.BRANCH_INDEX3.getValue();
                }
                //项目负责人办理4
            case FlowConstant.FLOW_SIGN_XMFZR4:
                if (!Validate.isString(branchIndex)) {
                    branchIndex = FlowConstant.SignFlowParams.BRANCH_INDEX4.getValue();
                }
                //是否需要做工作方案
                businessId = flowDto.getBusinessMap().get("IS_NEED_WP").toString();
                if (EnumState.YES.getValue().equals(businessId)) {
                    wk = workProgramRepo.findBySignIdAndBranchId(signid, branchIndex);
                    //如果是主项目
                    if (FlowConstant.SignFlowParams.BRANCH_INDEX1.getValue().equals(branchIndex)) {
                        //如果是专家评审会，则一定要选会议室(单个评审或者合并评审主项目要判断，合并评审次项目不用判断)
                        if (Constant.MergeType.REVIEW_MEETING.getValue().equals(wk.getReviewType()) && !roomBookingRepo.isHaveBookMeeting(wk.getId())
                                && (Constant.MergeType.REVIEW_SIGNLE.equals(wk.getIsSigle()) || (Constant.MergeType.REVIEW_MERGE.equals(wk.getIsSigle()) && !EnumState.YES.getValue().equals(wk.getIsMainProject())))) {
                            return new ResultMsg(false, MsgCode.ERROR.getValue(), "您选择的评审方式是【专家评审会】，但是还没有选择会议室，请先预定会议室！");
                        }
                        //如果是合并评审主项目，
                        if (Constant.MergeType.REVIEW_MERGE.getValue().equals(wk.getIsSigle())
                                && EnumState.YES.getValue().equals(wk.getIsMainProject())) {
                            //如果没有合并其他项目，则不准提交
                            if (!signMergeRepo.isHaveMerge(signid, Constant.MergeType.WORK_PROGRAM.getValue())) {
                                return new ResultMsg(false, MsgCode.ERROR.getValue(), "工作方案您选择的是合并评审主项目，您还没有设置关联项目，不能提交到下一步！");
                            }
                            //如果合并评审次项目没提交，不能进行下一步操作
                            if (!signRepo.isMergeSignEndWP(signid)) {
                                return new ResultMsg(false, MsgCode.ERROR.getValue(), "合并评审次项目还没有完成工作方案，不能进行下一步操作！");
                            }
                        }
                    }
                    //如果做工作方案，则要判断该分支工作方案是否完成
                    if (!signBranchRepo.checkFinishWP(signid, branchIndex)) {
                        return new ResultMsg(false, MsgCode.ERROR.getValue(), "您还没有完成工作方案，不能进行下一步操作！");
                    }
                    //查询部门领导
                    orgDept = orgDeptRepo.queryBySignBranchId(signid, branchIndex);
                    if (orgDept == null || !Validate.isString(orgDept.getDirectorID())) {
                        return new ResultMsg(false, MsgCode.ERROR.getValue(), "请设置该分支的部门负责人！");
                    }

                    dealUser = userRepo.getCacheUserById(orgDept.getDirectorID());
                    assigneeValue = Validate.isString(dealUser.getTakeUserId()) ? dealUser.getTakeUserId() : dealUser.getId();
                    //设定下一环节处理人
                    if (FlowConstant.SignFlowParams.BRANCH_INDEX1.getValue().equals(branchIndex)) {
                        variables.put(FlowConstant.SignFlowParams.WORK_PLAN1.getValue(), true);
                        variables.put(FlowConstant.SignFlowParams.USER_BZ1.getValue(), assigneeValue);
                    } else if (FlowConstant.SignFlowParams.BRANCH_INDEX2.getValue().equals(branchIndex)) {
                        variables.put(FlowConstant.SignFlowParams.WORK_PLAN2.getValue(), true);
                        variables.put(FlowConstant.SignFlowParams.USER_BZ2.getValue(), assigneeValue);
                    } else if (FlowConstant.SignFlowParams.BRANCH_INDEX3.getValue().equals(branchIndex)) {
                        variables.put(FlowConstant.SignFlowParams.WORK_PLAN3.getValue(), true);
                        variables.put(FlowConstant.SignFlowParams.USER_BZ3.getValue(), assigneeValue);
                    } else if (FlowConstant.SignFlowParams.BRANCH_INDEX4.getValue().equals(branchIndex)) {
                        variables.put(FlowConstant.SignFlowParams.WORK_PLAN4.getValue(), true);
                        variables.put(FlowConstant.SignFlowParams.USER_BZ4.getValue(), assigneeValue);
                    }

                    //不做工作方案
                } else {
                    dealUser = signPrincipalService.getMainPriUser(signid);
                    if (dealUser == null) {
                        return new ResultMsg(false, MsgCode.ERROR.getValue(), "项目还没分配主负责人，不能进行下一步操作！请联系主办部门进行负责人分配！");
                    }
                    assigneeValue = Validate.isString(dealUser.getTakeUserId()) ? dealUser.getTakeUserId() : dealUser.getId();
                    variables.put(FlowConstant.SignFlowParams.USER_FZR1.getValue(), assigneeValue);

                    //更改状态
                    signBranchRepo.isNeedWP(signid, branchIndex, EnumState.NO.getValue());

                    //注意：1、项目建议书、可研阶段一定要做工作方案；
                    // 2、主分支跳转，则必须要所有协办分支都完成才能跳转。
                    if (FlowConstant.SignFlowParams.BRANCH_INDEX1.getValue().equals(branchIndex)) {
                        if (signBranchRepo.assistFlowFinish(signid)) {
                            return new ResultMsg(false, MsgCode.ERROR.getValue(), "协审分支还没处理完，您不能进行直接发文操作！");
                        }
                        sign = signRepo.findById(Sign_.signid.getName(), signid);
                        if ((Constant.STAGE_SUG.equals(sign.getReviewstage()) ||
                                Constant.STAGE_STUDY.equals(sign.getReviewstage())) &&
                                !signBranchRepo.isHaveWP(signid)) {
                            return new ResultMsg(false, MsgCode.ERROR.getValue(), "【项目建议书，可行性研究报告】必要要做工作方案！");
                        }
                    }
                    signBranchRepo.finishBranch(signid, branchIndex);
                    //不做工作方案，还是需要设定下一环节处理人
                    //设定下一环节处理人
                    if (FlowConstant.SignFlowParams.BRANCH_INDEX1.getValue().equals(branchIndex)) {
                        variables.put(FlowConstant.SignFlowParams.WORK_PLAN1.getValue(), false);
                    } else if (FlowConstant.SignFlowParams.BRANCH_INDEX2.getValue().equals(branchIndex)) {
                        variables.put(FlowConstant.SignFlowParams.WORK_PLAN2.getValue(), false);
                    } else if (FlowConstant.SignFlowParams.BRANCH_INDEX3.getValue().equals(branchIndex)) {
                        variables.put(FlowConstant.SignFlowParams.WORK_PLAN3.getValue(), false);
                    } else if (FlowConstant.SignFlowParams.BRANCH_INDEX4.getValue().equals(branchIndex)) {
                        variables.put(FlowConstant.SignFlowParams.WORK_PLAN4.getValue(), false);
                    }
                }
                //判断是否完成所有工作方案
                if (signBranchRepo.allWPFinish(signid)) {
                    signRepo.updateSignProcessState(signid, Constant.SignProcessState.END_WP.getValue());
                }
                break;

            //部长审批工作方案1
            case FlowConstant.FLOW_SIGN_BMLD_SPW1:
                branchIndex = FlowConstant.SignFlowParams.BRANCH_INDEX1.getValue();
                //部长审批工作方案1
            case FlowConstant.FLOW_SIGN_BMLD_SPW2:
                if (!Validate.isString(branchIndex)) {
                    branchIndex = FlowConstant.SignFlowParams.BRANCH_INDEX2.getValue();
                }
                //部长审批工作方案1
            case FlowConstant.FLOW_SIGN_BMLD_SPW3:
                if (!Validate.isString(branchIndex)) {
                    branchIndex = FlowConstant.SignFlowParams.BRANCH_INDEX3.getValue();
                }
                //部长审批工作方案4
            case FlowConstant.FLOW_SIGN_BMLD_SPW4:
                if (!Validate.isString(branchIndex)) {
                    branchIndex = FlowConstant.SignFlowParams.BRANCH_INDEX4.getValue();
                }
                if (SessionUtil.getUserInfo().getOrg() == null) {
                    return new ResultMsg(false, MsgCode.ERROR.getValue(), "你还没设置所属部门！");
                }
                if (!Validate.isString(SessionUtil.getUserInfo().getOrg().getOrgSLeader())) {
                    return new ResultMsg(false, MsgCode.ERROR.getValue(), "请先设置该部门的分管领导！");
                }
                //更改工作方案信息
                wk = workProgramRepo.findBySignIdAndBranchId(signid, branchIndex);
                wk.setMinisterSuggesttion(flowDto.getDealOption());
                wk.setMinisterDate(new Date());
                wk.setMinisterName(SessionUtil.getDisplayName());

                //如果是主办流程，要判断是否有合并评审方案，有则跟着主项目一起办理
                if (FlowConstant.SignFlowParams.BRANCH_INDEX1.getValue().equals(branchIndex)) {
                    if (Constant.MergeType.REVIEW_MERGE.getValue().equals(wk.getIsSigle()) && EnumState.YES.getValue().equals(wk.getIsMainProject())) {
                        List<SignMerge> mergeList = signMergeRepo.findByIds(SignMerge_.signId.getName(), signid, null);
                        if (Validate.isList(mergeList)) {
                            ResultMsg resultMsg = null;
                            FlowDto flowDto2 = new FlowDto();
                            flowDto2.setDealOption(flowDto.getDealOption());
                            for (SignMerge s : mergeList) {
                                resultMsg = flowService.dealFlowByBusinessKey(s.getMergeId(), FlowConstant.FLOW_SIGN_BMLD_SPW1, flowDto2, processInstance.getProcessDefinitionKey());
                                if (resultMsg.isFlag() || Constant.MsgCode.FLOW_INSTANCE_NULL.getValue().equals(resultMsg.getReCode())
                                        || Constant.MsgCode.FLOW_TASK_NULL.getValue().equals(resultMsg.getReCode())
                                        || Constant.MsgCode.FLOW_ACTIVI_NEQ.getValue().equals(resultMsg.getReCode())
                                        || Constant.MsgCode.FLOW_NOT_MATCH.getValue().equals(resultMsg.getReCode())) {

                                } else {
                                    return resultMsg;
                                }
                            }
                        }
                    }
                }

                workProgramRepo.save(wk);

                //设定下一环节处理人
                dealUser = userRepo.getCacheUserById(SessionUtil.getUserInfo().getOrg().getOrgSLeader());
                assigneeValue = Validate.isString(dealUser.getTakeUserId()) ? dealUser.getTakeUserId() : dealUser.getId();
                //下一环节还是自己
                if (assigneeValue.equals(SessionUtil.getUserId())) {
                    isNextUser = true;
                }
                if (FlowConstant.SignFlowParams.BRANCH_INDEX1.getValue().equals(branchIndex)) {
                    variables.put(FlowConstant.SignFlowParams.USER_FGLD1.getValue(), assigneeValue);
                    nextNodeKey = FlowConstant.FLOW_SIGN_FGLD_SPW1;
                } else if (FlowConstant.SignFlowParams.BRANCH_INDEX2.getValue().equals(branchIndex)) {
                    variables.put(FlowConstant.SignFlowParams.USER_FGLD2.getValue(), assigneeValue);
                    nextNodeKey = FlowConstant.FLOW_SIGN_FGLD_SPW2;
                } else if (FlowConstant.SignFlowParams.BRANCH_INDEX3.getValue().equals(branchIndex)) {
                    variables.put(FlowConstant.SignFlowParams.USER_FGLD3.getValue(), assigneeValue);
                    nextNodeKey = FlowConstant.FLOW_SIGN_FGLD_SPW3;
                } else if (FlowConstant.SignFlowParams.BRANCH_INDEX4.getValue().equals(branchIndex)) {
                    variables.put(FlowConstant.SignFlowParams.USER_FGLD4.getValue(), assigneeValue);
                    nextNodeKey = FlowConstant.FLOW_SIGN_FGLD_SPW4;
                }

                break;

            //分管副主任审批工作方案
            case FlowConstant.FLOW_SIGN_FGLD_SPW1:
                branchIndex = FlowConstant.SignFlowParams.BRANCH_INDEX1.getValue();
            case FlowConstant.FLOW_SIGN_FGLD_SPW2:
                if (!Validate.isString(branchIndex)) {
                    branchIndex = FlowConstant.SignFlowParams.BRANCH_INDEX2.getValue();
                }
            case FlowConstant.FLOW_SIGN_FGLD_SPW3:
                if (!Validate.isString(branchIndex)) {
                    branchIndex = FlowConstant.SignFlowParams.BRANCH_INDEX3.getValue();
                }
            case FlowConstant.FLOW_SIGN_FGLD_SPW4:
                if (!Validate.isString(branchIndex)) {
                    branchIndex = FlowConstant.SignFlowParams.BRANCH_INDEX4.getValue();
                }
                //选择第一负责人作为下一环节处理人
                dealUser = signPrincipalService.getMainPriUser(signid);
                if (dealUser == null) {
                    return new ResultMsg(false, MsgCode.ERROR.getValue(), "项目还没分配主负责人，不能进行下一步操作！请联系主办部门进行负责人分配！");
                }
                assigneeValue = Validate.isString(dealUser.getTakeUserId()) ? dealUser.getTakeUserId() : dealUser.getId();
                variables.put(FlowConstant.SignFlowParams.USER_FZR1.getValue(), assigneeValue);

                //更改工作方案审核信息
                wk = workProgramRepo.findBySignIdAndBranchId(signid, branchIndex);

                //如果是主办流程，要判断是否有合并评审方案，有则跟着主项目一起办理
                if (FlowConstant.SignFlowParams.BRANCH_INDEX1.getValue().equals(branchIndex)) {
                    if (Constant.MergeType.REVIEW_MERGE.getValue().equals(wk.getIsSigle()) && EnumState.YES.getValue().equals(wk.getIsMainProject())) {
                        List<SignMerge> mergeList = signMergeRepo.findByIds(SignMerge_.signId.getName(), signid, null);
                        if (Validate.isList(mergeList)) {
                            ResultMsg resultMsg = null;
                            FlowDto flowDto2 = new FlowDto();
                            flowDto2.setDealOption(flowDto.getDealOption());
                            for (SignMerge s : mergeList) {
                                resultMsg = flowService.dealFlowByBusinessKey(s.getMergeId(), FlowConstant.FLOW_SIGN_FGLD_SPW1, flowDto2, processInstance.getProcessDefinitionKey());
                                if (resultMsg.isFlag() || Constant.MsgCode.FLOW_INSTANCE_NULL.getValue().equals(resultMsg.getReCode())
                                        || Constant.MsgCode.FLOW_TASK_NULL.getValue().equals(resultMsg.getReCode())
                                        || Constant.MsgCode.FLOW_ACTIVI_NEQ.getValue().equals(resultMsg.getReCode())
                                        || Constant.MsgCode.FLOW_NOT_MATCH.getValue().equals(resultMsg.getReCode())) {

                                } else {
                                    return resultMsg;
                                }
                            }
                        }
                    }
                }

                wk.setLeaderSuggesttion(flowDto.getDealOption());
                wk.setLeaderDate(new Date());
                wk.setLeaderName(SessionUtil.getDisplayName());
                workProgramRepo.save(wk);

                //更新评审会时间
                ExpertReview expertReview = expertReviewRepo.findById(ExpertReview_.businessId.getName(), signid);
                //更新专家评审方案评审日期
                if (expertReview != null) {
                    if (Constant.MergeType.REVIEW_MEETING.getValue().equals(wk.getReviewType())) {
                        //获取评审会日期
                        expertReview.setReviewDate(roomBookingRepo.getMeetingDateByBusinessId(wk.getId()));
                    } else {
                        //函评日期
                        expertReview.setReviewDate(wk.getLetterDate());
                    }
                    expertReviewRepo.save(expertReview);
                }
                break;
            //发文申请
            case FlowConstant.FLOW_SIGN_FW:
                //如果有专家评审费，则要先办理专家评审费
                if (expertReviewRepo.isHaveEPReviewCost(signid)) {
                    ExpertReview expertReview2 = expertReviewRepo.findById(ExpertReview_.businessId.getName(), signid);
                    if (expertReview2.getPayDate() == null || expertReview2.getTotalCost() == null) {
                        return new ResultMsg(false, MsgCode.ERROR.getValue(), "您还没完成专家评审费发放，不能进行下一步操作！");
                    }
                }
                //修改第一负责人意见
                businessId = flowDto.getBusinessMap().get("DIS_ID").toString();
                dp = dispatchDocRepo.findById(DispatchDoc_.id.getName(), businessId);
                dp.setMianChargeSuggest(flowDto.getDealOption() + "<br>" + SessionUtil.getDisplayName() + " &nbsp; " + DateUtils.converToString(new Date(), "yyyy年MM月dd日"));
                dp.setSecondChargeSuggest("");
                dispatchDocRepo.save(dp);

                //有项目负责人，则项目负责人审核
                userList = signPrincipalService.getAllSecondPriUser(signid);
                if (Validate.isList(userList)) {
                    variables.put(FlowConstant.SignFlowParams.HAVE_XMFZR.getValue(), true);
                    assigneeValue = buildUser(userList);
                    variables.put(FlowConstant.SignFlowParams.USER_HQ_LIST.getValue(), StringUtil.getSplit(assigneeValue, ","));
                    //没有项目负责人，则主办部长审核
                } else {
                    variables.put(FlowConstant.SignFlowParams.HAVE_XMFZR.getValue(), false);
                    assigneeValue = getMainDirecotr(signid);
                    variables.put(FlowConstant.SignFlowParams.USER_BZ1.getValue(), assigneeValue);
                }
                //完成发文
                signRepo.updateSignProcessState(signid, Constant.SignProcessState.END_DIS.getValue());
                break;
            //项目负责人确认发文（所有人确认通过才通过）
            case FlowConstant.FLOW_SIGN_QRFW:
                if (flowDto.getBusinessMap().get("AGREE") == null || !Validate.isString(flowDto.getBusinessMap().get("AGREE").toString())) {
                    return new ResultMsg(false, MsgCode.ERROR.getValue(), "请选择同意或者不同意！");
                }
                //修改第二负责人意见
                businessId = flowDto.getBusinessMap().get("DIS_ID").toString();
                dp = dispatchDocRepo.findById(DispatchDoc_.id.getName(), businessId);
                String optionString = Validate.isString(dp.getSecondChargeSuggest()) ? (dp.getSecondChargeSuggest() + "<br>") : "";
                dp.setSecondChargeSuggest(optionString + flowDto.getDealOption() + "              " + SessionUtil.getDisplayName() + " 日期：" + DateUtils.converToString(new Date(), "yyyy年MM月dd日"));
                dispatchDocRepo.save(dp);

                //如果同意
                if (EnumState.YES.getValue().equals(flowDto.getBusinessMap().get("AGREE").toString())) {
                    variables.put(FlowConstant.SignFlowParams.XMFZR_SP.getValue(), true);
                    //判断是否有协办部门
                    if (signBranchRepo.allAssistCount(signid) > 0) {
                        variables.put(FlowConstant.SignFlowParams.HAVE_XB.getValue(), true);
                        userList = signBranchRepo.findAssistOrgDirector(signid);
                        assigneeValue = buildUser(userList);
                        variables.put(FlowConstant.SignFlowParams.USER_XBBZ_LIST.getValue(), StringUtil.getSplit(assigneeValue, ","));
                    } else {
                        variables.put(FlowConstant.SignFlowParams.HAVE_XB.getValue(), false);
                        //获取主分支的部门领导
                        assigneeValue = getMainDirecotr(signid);
                        variables.put(FlowConstant.SignFlowParams.USER_BZ1.getValue(), assigneeValue);
                    }
                    flowDto.setDealOption(flowDto.getDealOption() + "【审批结果：通过】");
                    //如果不同意，则流程回到发文申请环节
                } else {
                    variables.put(FlowConstant.SignFlowParams.HAVE_XB.getValue(), null);
                    variables.put(FlowConstant.SignFlowParams.XMFZR_SP.getValue(), false);
                    //选择第一负责人
                    variables = buildMainPriUser(variables, signid, assigneeValue);
                    flowDto.setDealOption(flowDto.getDealOption() + "【审批结果：不通过】");
                }
                break;
            //协办部长审批发文
            case FlowConstant.FLOW_SIGN_BMLD_QRFW_XB:
                if (flowDto.getBusinessMap().get("AGREE") == null || !Validate.isString(flowDto.getBusinessMap().get("AGREE").toString())) {
                    return new ResultMsg(false, MsgCode.ERROR.getValue(), "请选择同意或者不同意！");
                }
                //如果同意，则到主办部长审批
                if (EnumState.YES.getValue().equals(flowDto.getBusinessMap().get("AGREE").toString())) {
                    variables.put(FlowConstant.SignFlowParams.XBBZ_SP.getValue(), true);
                    //获取主分支的部门领导
                    assigneeValue = getMainDirecotr(signid);
                    variables.put(FlowConstant.SignFlowParams.USER_BZ1.getValue(), assigneeValue);
                    flowDto.setDealOption(flowDto.getDealOption() + "【审批结果：通过】");
                    //如果不同意，则回退到发文环节
                } else {
                    variables.put(FlowConstant.SignFlowParams.XBBZ_SP.getValue(), false);
                    variables = buildMainPriUser(variables, signid, assigneeValue);
                    flowDto.setDealOption(flowDto.getDealOption() + "【审批结果：不通过】");
                }
                break;
            //部长审批发文
            case FlowConstant.FLOW_SIGN_BMLD_QRFW:
                //获取所有分管领导信息
                userList = signBranchRepo.findAssistSLeader(signid);
                //排除主办分支的领导
                if (Validate.isList(userList)) {
                    orgDept = orgDeptRepo.queryBySignBranchId(signid, FlowConstant.SignFlowParams.BRANCH_INDEX1.getValue());
                    dealUser = userRepo.getCacheUserById(orgDept.getsLeaderID());
                    for (int n = 0, l = userList.size(); n < l; n++) {
                        if ((userList.get(n)).getId().equals(dealUser.getId())) {
                            userList.remove(n);
                            break;
                        }
                    }
                }
                boolean isHaveTwoSLeader = Validate.isList(userList);
                variables.put(FlowConstant.SignFlowParams.HAVE_XB.getValue(), isHaveTwoSLeader);
                //如果有协办
                if (isHaveTwoSLeader) {
                    assigneeValue = buildUser(userList);
                    variables.put(FlowConstant.SignFlowParams.USER_XBFGLD_LIST.getValue(), StringUtil.getSplit(assigneeValue, ","));
                    //没有协办，则流转给主办分管领导审批
                } else {
                    assigneeValue = getMainSLeader(signid);
                    variables.put(FlowConstant.SignFlowParams.USER_FGLD1.getValue(), assigneeValue);
                    //下一环节还是自己处理
                    if (assigneeValue.equals(SessionUtil.getUserId())) {
                        isNextUser = true;
                        nextNodeKey = FlowConstant.FLOW_SIGN_FGLD_QRFW;
                    }
                }

                //修改发文信息
                businessId = flowDto.getBusinessMap().get("DIS_ID").toString();
                dp = dispatchDocRepo.findById(DispatchDoc_.id.getName(), businessId);
                dp.setMinisterSuggesttion(flowDto.getDealOption());
                dp.setMinisterDate(new Date());
                dp.setMinisterName(SessionUtil.getDisplayName());
                dispatchDocRepo.save(dp);
                break;
            //协办分管领导审批发文
            case FlowConstant.FLOW_SIGN_FGLD_QRFW_XB:
                if (flowDto.getBusinessMap().get("AGREE") == null || !Validate.isString(flowDto.getBusinessMap().get("AGREE").toString())) {
                    return new ResultMsg(false, MsgCode.ERROR.getValue(), "请选择同意或者不同意！");
                }
                //如果同意
                if (EnumState.YES.getValue().equals(flowDto.getBusinessMap().get("AGREE").toString())) {
                    variables.put(FlowConstant.SignFlowParams.XBFZR_SP.getValue(), true);
                    assigneeValue = getMainSLeader(signid);
                    variables.put(FlowConstant.SignFlowParams.USER_FGLD1.getValue(), assigneeValue);
                    //不同意则回退到发文申请环节
                } else {
                    variables.put(FlowConstant.SignFlowParams.XBFZR_SP.getValue(), false);
                    variables = buildMainPriUser(variables, signid, assigneeValue);
                    flowDto.setDealOption(flowDto.getDealOption() + "【审批结果：不通过】");
                }
                break;
            //分管领导审批发文
            case FlowConstant.FLOW_SIGN_FGLD_QRFW:
                userList = userRepo.findUserByRoleName(EnumFlowNodeGroupName.DIRECTOR.getValue());
                if (!Validate.isList(userList)) {
                    return new ResultMsg(false, MsgCode.ERROR.getValue(), "请先设置【" + EnumFlowNodeGroupName.DIRECTOR.getValue() + "】角色用户！");
                }
                dealUser = userList.get(0);
                assigneeValue = Validate.isString(dealUser.getTakeUserId()) ? dealUser.getTakeUserId() : dealUser.getId();
                variables.put(FlowConstant.SignFlowParams.USER_ZR.getValue(), assigneeValue);

                //修改发文信息
                businessId = flowDto.getBusinessMap().get("DIS_ID").toString();
                dp = dispatchDocRepo.findById(DispatchDoc_.id.getName(), businessId);
                dp.setViceDirectorSuggesttion(flowDto.getDealOption());
                dp.setViceDirectorDate(new Date());
                dp.setViceDirectorName(SessionUtil.getDisplayName());
                dispatchDocRepo.save(dp);
                //下一环节还是自己处理
                if (assigneeValue.equals(SessionUtil.getUserId())) {
                    isNextUser = true;
                    nextNodeKey = FlowConstant.FLOW_SIGN_ZR_QRFW;
                }
                break;
            //主任审批发文
            case FlowConstant.FLOW_SIGN_ZR_QRFW:
                businessId = flowDto.getBusinessMap().get("DIS_ID").toString();
                dp = dispatchDocRepo.findById(DispatchDoc_.id.getName(), businessId);
                dp.setDirectorSuggesttion(flowDto.getDealOption());
                dp.setDirectorDate(new Date());
                dp.setDirectorName(SessionUtil.getDisplayName());
                dispatchDocRepo.save(dp);

                //项目负责人生成发文编号
                variables = buildMainPriUser(variables, signid, assigneeValue);
                break;
            //生成发文编号
            case FlowConstant.FLOW_SIGN_FWBH:
                businessId = flowDto.getBusinessMap().get("DIS_ID").toString();
                dp = dispatchDocRepo.findById(DispatchDoc_.id.getName(), businessId);
                if (!Validate.isString(dp.getFileNum())) {
                    return new ResultMsg(false, MsgCode.ERROR.getValue(), "操作失败，该项目还没有发文编号，不能进行下一步操作！");
                }
                sign = signRepo.findById(Sign_.signid.getName(), signid);
                //如果有评审费或者是协审流程，则给财务部办理，没有，则直接到归档环节
                if (expertReviewRepo.isHaveEPReviewCost(signid) || EnumState.YES.getValue().equals(sign.getIsassistflow())) {
                    userList = userRepo.findUserByRoleName(EnumFlowNodeGroupName.FINANCIAL.getValue());
                    if (!Validate.isList(userList)) {
                        return new ResultMsg(false, MsgCode.ERROR.getValue(), "请先设置【" + EnumFlowNodeGroupName.FINANCIAL.getValue() + "】角色用户！");
                    }
                    assigneeValue = buildUser(userList);
                    variables.put(FlowConstant.SignFlowParams.HAVE_ZJPSF.getValue(), true);
                    variables.put(FlowConstant.SignFlowParams.USER_CW.getValue(), assigneeValue);

                    //没有评审费，则直接到归档环节(还是当前人处理)
                } else {
                    variables.put(FlowConstant.SignFlowParams.HAVE_ZJPSF.getValue(), false);
                    assigneeValue = SessionUtil.getUserId();
                    variables.put(FlowConstant.SignFlowParams.USER_FZR1.getValue(), assigneeValue);
                    //更改已发送存档字段值
                    sign.setIsSendFileRecord(EnumState.YES.getValue());
                    signRepo.save(sign);
                }
                break;

            //财务办理
            case FlowConstant.FLOW_SIGN_CWBL:
                sign = signRepo.findById(Sign_.signid.getName(), signid);
                /*if (EnumState.YES.getValue().equals(sign.getIsassistflow())) {
                    //协审费录入状态 9 表示已办理
                    sign.setAssistStatus(EnumState.YES.getValue());
                } else {
                    //评审费录入状态 9 表示已办理
                    sign.setFinanciaStatus(EnumState.YES.getValue());
                }*/
                sign.setIsSendFileRecord(EnumState.YES.getValue());
                signRepo.save(sign);
                variables = buildMainPriUser(variables, signid, assigneeValue);
                break;

            //第一负责人归档
            case FlowConstant.FLOW_SIGN_GD:
                //如果没有完成专家评分，则不可以提交到下一步
                if (!expertReviewRepo.isFinishEPGrade(signid)) {
                    return new ResultMsg(false, MsgCode.ERROR.getValue(), "您还未对专家进行评分,不能提交到下一步操作！");
                }

                if (flowDto.getBusinessMap().get("checkFileUser") != null) {
                    dealUser = JSON.parseObject(flowDto.getBusinessMap().get("checkFileUser").toString(), User.class);
                    variables.put(FlowConstant.SignFlowParams.HAVE_XMFZR.getValue(), true);
                    assigneeValue = Validate.isString(dealUser.getTakeUserId()) ? dealUser.getTakeUserId() : dealUser.getId();
                    variables.put(FlowConstant.SignFlowParams.USER_A.getValue(), assigneeValue);
                } else {
                    variables.put(FlowConstant.SignFlowParams.HAVE_XMFZR.getValue(), false);
                    //如果是回退，则保留之前的审核人
                    sign = signRepo.findById(Sign_.signid.getName(), signid);
                    if (Validate.isString(sign.getSecondPriUser())) {
                        dealUser = userRepo.getCacheUserById(sign.getSecondPriUser());
                        //不是回退，则取第一个
                    } else {
                        userList = userRepo.findUserByRoleName(EnumFlowNodeGroupName.FILER.getValue());
                        if (!Validate.isList(userList)) {
                            return new ResultMsg(false, MsgCode.ERROR.getValue(), "请先设置【" + EnumFlowNodeGroupName.FILER.getValue() + "】角色用户！");
                        }
                        dealUser = userList.get(0);
                    }
                    assigneeValue = Validate.isString(dealUser.getTakeUserId()) ? dealUser.getTakeUserId() : dealUser.getId();
                    variables.put(FlowConstant.SignFlowParams.USER_QRGD.getValue(), assigneeValue);
                }
                signRepo.updateSignProcessState(signid, Constant.SignProcessState.END_FILE.getValue());
                break;

            //第二负责人审批归档
            case FlowConstant.FLOW_SIGN_DSFZR_QRGD:
                userList = userRepo.findUserByRoleName(EnumFlowNodeGroupName.FILER.getValue());
                if (!Validate.isList(userList)) {
                    return new ResultMsg(false, MsgCode.ERROR.getValue(), "请先设置【" + EnumFlowNodeGroupName.FILER.getValue() + "】角色用户！");
                }
                dealUser = userList.get(0);
                assigneeValue = Validate.isString(dealUser.getTakeUserId()) ? dealUser.getTakeUserId() : dealUser.getId();
                variables.put(FlowConstant.SignFlowParams.USER_QRGD.getValue(), assigneeValue);
                //更新项目第二负责人
                sign = signRepo.findById(Sign_.signid.getName(), signid);
                sign.setSecondPriUser(SessionUtil.getUserId());
                signRepo.save(sign);

                //第二负责人确认
                businessId = flowDto.getBusinessMap().get("GD_ID").toString();
                fileRecord = fileRecordRepo.findById(FileRecord_.fileRecordId.getName(), businessId);
                fileRecord.setProjectTwoUser(SessionUtil.getDisplayName());
                //发送存档日期为第二负责人审批意见后的日期，
                fileRecord.setSendStoreDate(new Date());
                fileRecordRepo.save(fileRecord);

                break;
            //确认归档
            case FlowConstant.FLOW_SIGN_QRGD:
                businessId = flowDto.getBusinessMap().get("GD_ID").toString();
                fileRecord = fileRecordRepo.findById(FileRecord_.fileRecordId.getName(), businessId);
                fileRecord.setFileDate(new Date());
                fileRecord.setSignUserid(SessionUtil.getUserId());
                fileRecord.setSignUserName(SessionUtil.getDisplayName());
                //纸质文件接受日期 ：为归档员陈春燕确认的归档日期
                fileRecord.setPageDate(new Date());
                fileRecordRepo.save(fileRecord);

                //更改项目状态
                sign = signRepo.findById(Sign_.signid.getName(), signid);
                sign.setSignState(EnumState.YES.getValue());
                sign.setProcessState(Constant.SignProcessState.FINISH.getValue());
                signRepo.save(sign);
               /* signRepo.updateSignState(signid, EnumState.YES.getValue());
                signRepo.updateSignProcessState(signid, Constant.SignProcessState.FINISH.getValue());*/

                break;
            default:
                ;
        }

        taskService.addComment(task.getId(), processInstance.getId(), flowDto.getDealOption());    //添加处理信息
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

        if (isNextUser == false) {
            //放入腾讯通消息缓冲池
            RTXSendMsgPool.getInstance().sendReceiverIdPool(task.getId(), assigneeValue);
        }

        return new ResultMsg(true, MsgCode.OK.getValue(), "操作成功！");
    }

    /**
     * 获取主分支的分管领导
     *
     * @param signid
     */
    private String getMainSLeader(String signid) {
        OrgDept orgDept = orgDeptRepo.queryBySignBranchId(signid, FlowConstant.SignFlowParams.BRANCH_INDEX1.getValue());
        User dealUser = userRepo.getCacheUserById(orgDept.getsLeaderID());
        return Validate.isString(dealUser.getTakeUserId()) ? dealUser.getTakeUserId() : dealUser.getId();
    }

    /**
     * 获取主分支的部门领导
     *
     * @param signid
     */
    private String getMainDirecotr(String signid) {
        OrgDept orgDept = orgDeptRepo.queryBySignBranchId(signid, FlowConstant.SignFlowParams.BRANCH_INDEX1.getValue());
        User dealUser = userRepo.getCacheUserById(orgDept.getDirectorID());
        return Validate.isString(dealUser.getTakeUserId()) ? dealUser.getTakeUserId() : dealUser.getId();
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

    private Map<String, Object> buildMainPriUser(Map<String, Object> variables, String signid, String assigneeValue) {
        User dealUser = signPrincipalService.getMainPriUser(signid);
        assigneeValue = Validate.isString(dealUser.getTakeUserId()) ? dealUser.getTakeUserId() : dealUser.getId();
        variables.put(FlowConstant.SignFlowParams.USER_FZR1.getValue(), assigneeValue);
        return variables;
    }

    /**
     * 查询项目信息，调概项目
     *
     * @return
     */
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
        sqlBuilder.append(" order by sign." + Sign_.createdDate.getName() + " desc ");

        List<Sign> signList = signRepo.findByHql(sqlBuilder);
        List<SignDto> resultList = new ArrayList<>(signList == null ? 0 : signList.size());
        signList.forEach(s -> {
            SignDto signDto = new SignDto();
            BeanCopierUtils.copyProperties(s, signDto);
            resultList.add(signDto);
        });
        return resultList;
    }

    /**
     * 根据协审计划，查询收文信息
     *
     * @param planId
     * @param isOnlySign 是否只查询项目信息（9代表是，0代表否，默认为否）
     * @return
     */
    @Override
    public Map<String,Object> findByPlanId(String planId,String isOnlySign) {
        Map<String,Object> resultMap = new HashMap<>();
        HqlBuilder sqlBuilder = HqlBuilder.create();
        sqlBuilder.append(" select distinct sign from " + Sign.class.getSimpleName() + " as sign,AssistPlanSign as psign ");
        sqlBuilder.append(" where sign." + Sign_.signid.getName() + " = psign." + AssistPlanSign_.signId.getName());
        sqlBuilder.append(" and psign.assistPlan.id =:planid");
        sqlBuilder.setParam("planid", planId);

        List<Sign> signList = signRepo.findByHql(sqlBuilder);
        List<SignDto> resultList = new ArrayList<>(signList == null ? 0 : signList.size());
        signList.forEach(s -> {
            SignDto signDto = new SignDto();
            BeanCopierUtils.copyProperties(s, signDto);
            resultList.add(signDto);
        });
        resultMap.put("signList",resultList);

        if(EnumState.NO.getValue().equals(isOnlySign)){
            //协审计划信息
            AssistPlan assistPlan = assistPlanRepo.findById(planId);
            AssistPlanDto planDto = new AssistPlanDto();
            BeanCopierUtils.copyProperties(assistPlan,planDto);

            //获取项目信息
            if (Validate.isList(assistPlan.getAssistPlanSignList())) {
                List<AssistPlanSignDto> planSignDtoList = new ArrayList<>(assistPlan.getAssistPlanSignList().size());
                for (AssistPlanSign assistPlanSign : assistPlan.getAssistPlanSignList()) {
                    AssistPlanSignDto planSignDto = new AssistPlanSignDto();
                    BeanCopierUtils.copyProperties(assistPlanSign, planSignDto);
                    planSignDtoList.add(planSignDto);
                }
                planDto.setAssistPlanSignDtoList(planSignDtoList);
            }
            resultMap.put("planDto",planDto);
        }
        return resultMap;
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

    /**
     * 根据项目ID，查找关联项目
     *
     * @param signId
     * @return
     */
    @Override
    public List<SignDto> getAssociateDtos(String signId) {
        List<SignDto> signDtoList = new ArrayList<SignDto>();
        SignDto signDto = new SignDto();
        Sign sign = signRepo.getById(signId);
        if (Validate.isString(sign.getProjectcode())) {
            BeanCopierUtils.copyProperties(sign, signDto);
            signDtoList.add(signDto);
            getPreAssociateDto(sign.getAssociateSign(), signDtoList);
        } else {
            signDto = null;
        }

        return signDtoList;
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
        //发文关联阶段数据
        if (associateSign.getDispatchDoc() != null && Validate.isString(associateSign.getDispatchDoc().getId())) {
            DispatchDocDto dispatchDocDto = new DispatchDocDto();
            BeanCopierUtils.copyProperties(associateSign.getDispatchDoc(), dispatchDocDto);
            associateSignDto.setDispatchDocDto(dispatchDocDto);
        }
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
     * @param associateId 关联到的项目ID,如果这个为空，则为解除关联
     */
    @Override
    @Transactional
    public ResultMsg associate(String signId, String associateId) {
        if (signId.equals(associateId)) {
            return new ResultMsg(false, MsgCode.ERROR.getValue(), "不能关联自身项目");
        }
        Sign sign = signRepo.getById(signId);
        if (sign == null) {
            return new ResultMsg(false, MsgCode.ERROR.getValue(), "项目不存在");
        }
        boolean isLink = Validate.isString(associateId) ? true : false;     //
        //如果associateId为空，解除关联
        if (isLink) {
            Sign associateSign = signRepo.findById(Sign_.signid.getName(), associateId);
            if (associateSign == null) {
                return new ResultMsg(false, MsgCode.ERROR.getValue(), "关联项目不存在");
            }
            sign.setAssociateSign(associateSign);
            sign.setIsAssociate(1);
        } else {
            //找出关联项目
            associateId = sign.getAssociateSign().getSignid();

            sign.setIsAssociate(0);
            sign.setAssociateSign(null);
        }
        signRepo.save(sign);

        //更改对应收文的状态
        dispatchDocRepo.updateIsRelatedState(signId);
        dispatchDocRepo.updateIsRelatedState(associateId);

        return new ResultMsg(true, MsgCode.OK.getValue(), "操作成功！");
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
        List<OrgDept> orgDeptList = orgDeptRepo.findAll();
        return new ResultMsg(true, MsgCode.OK.getValue(), "添加成功", orgDeptList);
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
        //6、收文编号
        if (!Validate.isString(sign.getSignNum())) {
            int maxSeq = findSignMaxSeqByType(sign.getDealOrgType(), sign.getSigndate());
            sign.setSignSeq(maxSeq + 1);
            String signSeqString = (maxSeq + 1) > 999 ? (maxSeq + 1) + "" : String.format("%03d", Integer.valueOf(maxSeq + 1));
            sign.setSignNum(DateUtils.converToString(new Date(), "yyyy") + sign.getDealOrgType() + signSeqString);
        }
        //7、正式签收
        if (Validate.isString(sign.getIssign()) || !EnumState.YES.getValue().equals(sign.getIssign())) {
            sign.setSigndate(new Date());
            sign.setIssign(EnumState.YES.getValue());       //正式签收
            Float reviewsDays = getReviewDays(sign.getReviewstage());
            if (reviewsDays > 0) {
                sign.setSurplusdays(Constant.WORK_DAY_15);
                sign.setReviewdays(Constant.WORK_DAY_15);
            }
        }
        return new ResultMsg(true, MsgCode.OK.getValue(), "操作成功！");
    }

    private Float getReviewDays(String reviewstage) {
        Float resultFloat = 0f;
        if (Validate.isString(reviewstage)) {
            //先查找系统配置对否有评审阶段的评审天数，如有则用系统的，如果没有则用默认值
            String configKey = "";
            switch (reviewstage) {
                case Constant.STAGE_SUG:
                    configKey = Constant.RevireStageKey.KEY_SUG.getValue();
                    break;
                case Constant.STAGE_STUDY:
                    configKey = Constant.RevireStageKey.KEY_STUDY.getValue();
                    break;
                case Constant.STAGE_BUDGET:
                    configKey = Constant.RevireStageKey.KEY_BUDGET.getValue();
                    break;
                case Constant.APPLY_REPORT:
                    configKey = Constant.RevireStageKey.KEY_REPORT.getValue();
                    break;
                case Constant.OTHERS:
                    configKey = Constant.RevireStageKey.KEY_OTHER.getValue();
                    break;
                case Constant.DEVICE_BILL_HOMELAND:
                    configKey = Constant.RevireStageKey.KEY_HOMELAND.getValue();
                    break;
                case Constant.DEVICE_BILL_IMPORT:
                    configKey = Constant.RevireStageKey.KEY_IMPORT.getValue();
                    break;
                case Constant.IMPORT_DEVICE:
                    configKey = Constant.RevireStageKey.KEY_DEVICE.getValue();
                    break;
                default:
                    configKey = "";
            }

            if (Validate.isString(configKey)) {
                SysConfigDto sysConfigDto = sysConfigService.findByKey(configKey);
                if (sysConfigDto != null && sysConfigDto.getConfigValue() != null) {
                    return Float.parseFloat(sysConfigDto.getConfigValue());
                } else {
                    //设定默认值，项目建议书和资金申请报告是12天，其他是15天
                    if ((Constant.STAGE_SUG).equals(reviewstage)
                            || (Constant.APPLY_REPORT).equals(reviewstage)) {
                        return Constant.WORK_DAY_12;
                    } else {
                        return Constant.WORK_DAY_15;
                    }
                }
            }
        }
        return resultFloat;
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
     *
     * @param odataObj
     * @return
     */
    @Override
    public PageModelDto<SignDto> findBySignUser(ODataObj odataObj) {
        PageModelDto<SignDto> pageModelDto = new PageModelDto<SignDto>();
        Criteria criteria = signRepo.getExecutableCriteria();
        criteria = odataObj.buildFilterToCriteria(criteria);
        //1、排除旧项目
        criteria.add(Restrictions.isNull(Sign_.oldProjectId.getName()));
        //2、排除已终止、已完成
        criteria.add(Restrictions.ne(Sign_.signState.getName(), EnumState.YES.getValue()));
        criteria.add(Restrictions.ne(Sign_.signState.getName(), EnumState.FORCE.getValue()));
        //3、已签收，但是未发起流程的项目 或者已发起流程，但是未签收的项目
        StringBuffer sb = new StringBuffer();
        sb.append("( (" + Sign_.issign.getName() + " is null or " + Sign_.issign.getName() + " = '" + EnumState.NO.getValue() + "' ");
        sb.append(" and " + Sign_.processInstanceId.getName() + " = '" + EnumState.YES.getValue() + "') ");
        sb.append("  or ( " + Sign_.issign.getName() + " = '" + EnumState.YES.getValue() + "' and " + Sign_.processInstanceId.getName() + " is null ))");
        criteria.add(Restrictions.sqlRestriction(sb.toString()));

        Integer totalResult = ((Number) criteria.setProjection(Projections.rowCount()).uniqueResult()).intValue();
        criteria.setProjection(null);

        criteria.addOrder(Order.desc(Sign_.createdDate.getName()));
        if (odataObj.getSkip() > 0) {
            criteria.setFirstResult(odataObj.getSkip());
        }
        if (odataObj.getTop() > 0) {
            criteria.setMaxResults(odataObj.getTop());
        }
        pageModelDto.setCount(totalResult);

        List<Sign> signList = criteria.list();
        if (Validate.isList(signList)) {
            List<SignDto> signDtoList = new ArrayList<SignDto>(signList.size());
            signList.forEach(x -> {
                SignDto signDto = new SignDto();
                BeanCopierUtils.copyProperties(x, signDto);
                signDtoList.add(signDto);
            });
            pageModelDto.setValue(signDtoList);
        } else {
            pageModelDto.setValue(null);
        }
        return pageModelDto;
    }

    /**
     * 项目关联，每个项目只能关联一个前一阶段
     * * @param projectName
     *
     * @return
     */
    @Override
    public List<SignDispaWork> findAssociateSign(SignDispaWork signDispaWork) {
        HqlBuilder sqlBuilder = HqlBuilder.create();
        sqlBuilder.append("select s.* from V_SIGN_DISP_WORK s where s." + SignDispaWork_.signid.getName() + " != :signid ");
        //sqlBuilder.append(" and (select count(cas.associate_signid) from CS_ASSOCIATE_SIGN cas where cas.signid = s." + SignDispaWork_.signid.getName() + ")=0 ");
        sqlBuilder.setParam("signid", signDispaWork.getSignid());
        if (Validate.isString(signDispaWork.getProjectname())) {
            sqlBuilder.append(" and s." + SignDispaWork_.projectname.getName() + " like :projectName");
            sqlBuilder.setParam("projectName", "%" + signDispaWork.getProjectname() + "%");
        }
        List<SignDispaWork> signList = signDispaWorkRepo.findBySql(sqlBuilder);
        return signList;
    }

    @Override
    @Transactional
    public ResultMsg reserveAddSign(SignDto signDto) {
        Sign sign = new Sign();
        BeanCopierUtils.copyProperties(signDto, sign);
        sign.setSignState(EnumState.NORMAL.getValue());

        //0 用于区别签收和预签收页面实现送来资料存放位置
        sign.setIspresign(Constant.EnumState.NO.getValue());
        //2、是否是项目概算流程
        if (Constant.STAGE_BUDGET.equals(sign.getReviewstage()) || Validate.isString(sign.getIschangeEstimate())) {
            sign.setIsassistflow(EnumState.YES.getValue());
        } else {
            sign.setIsassistflow(EnumState.NO.getValue());
        }
        Date now = new Date();

        //3、送件人为当前签收人，
        sign.setSendusersign(SessionUtil.getDisplayName());

        //4、默认办理部门（项目建议书、可研为PX，概算为GX，其他为评估）
        if (Constant.STAGE_BUDGET.equals(sign.getReviewstage())) {
            sign.setDealOrgType(Constant.BusinessType.GX.getValue());
            sign.setLeaderhandlesug("请（概算一部 概算二部）组织评审。");
        } else {
            sign.setDealOrgType(Constant.BusinessType.PX.getValue());
            sign.setLeaderhandlesug("请（评估一部 评估二部 评估一部信息化组）组织评审。");
        }

        //5、综合部、分管副主任默认办理信息
        List<User> roleList = userRepo.findUserByRoleName(EnumFlowNodeGroupName.VICE_DIRECTOR.getValue());
        for (User user : roleList) {
            if (sign.getDealOrgType().equals(user.getMngOrgType())) {
                sign.setLeaderId(user.getId());
                sign.setLeaderName(user.getDisplayName());
                sign.setComprehensivehandlesug("请" + (user.getDisplayName()).substring(0, 1) + "主任阅示。");
                sign.setComprehensiveName("综合部");
                sign.setComprehensiveDate(new Date());
                break;
            }
        }
        if (!Validate.isString(sign.getSignNum())) {
            int maxSeq = findSignMaxSeqByType(sign.getDealOrgType(), sign.getSigndate());
            sign.setSignSeq(maxSeq + 1);
            String signSeqString = (maxSeq + 1) > 999 ? (maxSeq + 1) + "" : String.format("%03d", Integer.valueOf(maxSeq + 1));
            sign.setSignNum(DateUtils.converToString(new Date(), "yyyy") + sign.getDealOrgType() + signSeqString);
        }

        //项目预签收，没有签收日期
        //sign.setSigndate(now);
        sign.setSignid(UUID.randomUUID().toString());
        sign.setCreatedDate(now);
        sign.setModifiedDate(now);
        sign.setCreatedBy(SessionUtil.getLoginName());
        sign.setModifiedBy(SessionUtil.getLoginName());
        //默认为不亮灯
        sign.setIsLightUp(Constant.signEnumState.NOLIGHT.getValue());
        signRepo.save(sign);
        return new ResultMsg(true, MsgCode.OK.getValue(), "操作成功！", sign);
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
     *
     * @param signid
     */
    @Override
    @Transactional
    public void deleteReserveSign(String signid) {
        Sign sign = signRepo.findById(Sign_.signid.getName(), signid);
        if (sign != null) {
            signRepo.delete(sign);
            log.info(String.format("删除预签收项目", sign.getProjectname()));
        }
    }

    /**
     * 获取合并评审项目
     *
     * @param signid
     * @return
     */
    @Override
    public List<SignDto> findReviewSign(String signid) {
        List<Sign> signList = signRepo.findReviewSign(signid);
        if (Validate.isList(signList)) {
            List<SignDto> resultList = new ArrayList<>(signList.size());
            signList.forEach(sl -> {
                SignDto signDto = new SignDto();
                BeanCopierUtils.copyProperties(sl, signDto);
                resultList.add(signDto);
            });

            return resultList;
        }
        return null;
    }

    /**
     * 根据项目类型，获取项目最大收文编号
     *
     * @param signType
     * @return
     */
    private int findSignMaxSeqByType(String signType, Date signdate) {
        if (signdate == null) {
            signdate = new Date();
        }
        HqlBuilder sqlBuilder = HqlBuilder.create();
        sqlBuilder.append("select max(" + Sign_.signSeq.getName() + ") from cs_sign ");
        sqlBuilder.append("where " + Sign_.dealOrgType.getName() + " =:signType and (" + Sign_.signdate.getName() + " between ");
        sqlBuilder.append(" to_date(:beginTime,'yyyy-mm-dd hh24:mi:ss') and to_date(:endTime,'yyyy-mm-dd hh24:mi:ss' )) ");
        sqlBuilder.setParam("signType", signType);
        sqlBuilder.setParam("beginTime", DateUtils.converToString(signdate, "yyyy") + "-01-01 00:00:00");
        sqlBuilder.setParam("endTime", DateUtils.converToString(signdate, "yyyy") + "-12-31 23:59:59");
        return dispatchDocRepo.returnIntBySql(sqlBuilder);
    }

    /***********************   以下是对接接口部分  ****************************/
    /**
     * 项目推送
     *
     * @param signDto
     * @return
     */
    @Override
    public ResultMsg pushProject(SignDto signDto) {
        if (signDto == null) {
            return new ResultMsg(false, MsgCode.OBJ_NULL.getValue(), "获取对象为空！");
        }
        if (!Validate.isString(signDto.getFilecode())) {
            return new ResultMsg(false, MsgCode.MAIN_VALUE_NULL.getValue(), "收文编号为空！");
        }
        //1、根据收文编号获取项目信息
        Sign sign = signRepo.findByFilecode(signDto.getFilecode());
        Date now = new Date();
        if (sign == null) {
            sign = new Sign();
            BeanCopierUtils.copyProperties(signDto, sign);

            //是否是项目概算流程
            if (Constant.STAGE_BUDGET.equals(sign.getReviewstage()) || Validate.isString(sign.getIschangeEstimate())) {
                sign.setIsassistflow(EnumState.YES.getValue());
            } else {
                sign.setIsassistflow(EnumState.NO.getValue());
            }
            sign.setSignid(UUID.randomUUID().toString());
            sign.setIsLightUp(Constant.signEnumState.NOLIGHT.getValue());
            sign.setCreatedDate(now);
            sign.setModifiedDate(now);
            sign.setCreatedBy("FGW");
            sign.setModifiedBy("FGW");
        } else {
            BeanCopierUtils.copyPropertiesIgnoreNull(signDto, sign);
        }

        //送来时间
        if (sign.getReceivedate() == null) {
            sign.setReceivedate(now);
        }

        if (!Validate.isString(sign.getIssign()) || EnumState.NO.getValue().equals(sign.getIssign())) {
            //正式签收
            sign.setIssign(EnumState.YES.getValue());
            sign.setSigndate(now);

            if (Validate.isString(sign.getReviewstage())) {
                //先查找系统配置对否有评审阶段的评审天数，如有则用系统的，如果没有则用默认值
                String configKey = "";
                switch (sign.getReviewstage()) {
                    case Constant.STAGE_SUG:
                        configKey = Constant.RevireStageKey.KEY_SUG.getValue();
                        break;
                    case Constant.STAGE_STUDY:
                        configKey = Constant.RevireStageKey.KEY_STUDY.getValue();
                        break;
                    case Constant.STAGE_BUDGET:
                        configKey = Constant.RevireStageKey.KEY_BUDGET.getValue();
                        break;
                    case Constant.APPLY_REPORT:
                        configKey = Constant.RevireStageKey.KEY_REPORT.getValue();
                        break;
                    case Constant.OTHERS:
                        configKey = Constant.RevireStageKey.KEY_OTHER.getValue();
                        break;
                    case Constant.DEVICE_BILL_HOMELAND:
                        configKey = Constant.RevireStageKey.KEY_HOMELAND.getValue();
                        break;
                    case Constant.DEVICE_BILL_IMPORT:
                        configKey = Constant.RevireStageKey.KEY_IMPORT.getValue();
                        break;
                    case Constant.IMPORT_DEVICE:
                        configKey = Constant.RevireStageKey.KEY_DEVICE.getValue();
                        break;
                    default:
                        configKey = "";
                }

                if (Validate.isString(configKey)) {
                    SysConfigDto sysConfigDto = sysConfigService.findByKey(configKey);
                    if (sysConfigDto != null && sysConfigDto.getConfigValue() != null) {
                        sign.setReviewdays(Float.parseFloat(sysConfigDto.getConfigValue()));
                        sign.setSurplusdays(Float.parseFloat(sysConfigDto.getConfigValue()));
                    } else {
                        //设定默认值，项目建议书和资金申请报告是12天，其他是15天
                        if ((Constant.STAGE_SUG).equals(sign.getReviewstage())
                                || (Constant.APPLY_REPORT).equals(sign.getReviewstage())) {
                            sign.setSurplusdays(Constant.WORK_DAY_12);  //剩余评审天数
                            sign.setReviewdays(Constant.WORK_DAY_12);   //评审天数，完成的时候再结算
                        } else {
                            sign.setSurplusdays(Constant.WORK_DAY_15);
                            sign.setReviewdays(Constant.WORK_DAY_15);
                        }
                    }
                }
            }
        }

        //保存
        try {
            signRepo.save(sign);
            return new ResultMsg(true, MsgCode.SUCCESS.getValue(), "推送成功！");
        } catch (Exception e) {
            return new ResultMsg(false, MsgCode.MAIN_VALUE_NULL.getValue(), "保存异常！", e);
        }

    }

    @Override
    public List<SignDispaWork> getStastitacalData(ODataObj oDataObj) {
        Criteria criteria = signDispaWorkRepo.getExecutableCriteria();
        criteria = oDataObj.buildFilterToCriteria(criteria);
        List<SignDispaWork> signDispaWorks = criteria.list();
        return signDispaWorks;
    }

    /**
     * 更新是否已生成模板状态
     *
     * @param signId
     */
    @Override
    @Transactional
    public void updateSignTemplate(String signId) {
        HqlBuilder hqlBuilder = HqlBuilder.create();
        hqlBuilder.append("update cs_sign set " + Sign_.isSignTemplate.getName() + "=:isSignTemplate" + " where signid=:signId");
        hqlBuilder.setParam("isSignTemplate", EnumState.YES.getValue());
        hqlBuilder.setParam("signId", signId);
        signRepo.executeSql(hqlBuilder);
    }

    /**
     * 项目取回列表（分管领导和部门领导）
     * 根据不同的角色，获取不同的列表数据
     *
     * @param odataObj
     * @param isOrgLeader 是否部长
     */
    @Override
    public PageModelDto<RuProcessTask> getBackList(ODataObj odataObj, boolean isOrgLeader) {
        /**
         * 项目流程信息状态
         * (1:已发起，2:正在做工作方案，3:已完成工作方案，4:正在做发文 5:已完成发文 6:已完成发文编号 7:正在归档，8:已完成归档，9:已确认归档)
         */
        PageModelDto<RuProcessTask> pageModelDto = new PageModelDto<RuProcessTask>();
        Criteria criteria = ruProcessTaskRepo.getExecutableCriteria();
        criteria = odataObj.buildFilterToCriteria(criteria);
        //查询正在执行的项目，已经暂停的不能取回，合并评审次项目也不能取回
        criteria.add(Restrictions.eq(RuProcessTask_.taskState.getName(), EnumState.PROCESS.getValue()));
        criteria.add(Restrictions.isNull(RuProcessTask_.reviewType.getName()));
        /*criteria.add(Restrictions.or(
                Restrictions.isNull(RuProcessTask_.reviewType.getName()),
                Restrictions.eq(RuProcessTask_.reviewType.getName(), EnumState.YES.getValue()))
        );*/

        StringBuffer sBuffer = new StringBuffer();
        //部长回退( 只能在项目负责人办理和部长审批环节取回 )
        if (isOrgLeader) {
            criteria.add(Restrictions.or(
                    Restrictions.eq(RuProcessTask_.signprocessState.getName(), Constant.SignProcessState.DO_WP.getValue()),
                    Restrictions.eq(RuProcessTask_.signprocessState.getName(), Constant.SignProcessState.END_WP.getValue()))
            );
            sBuffer.append(" (ASSIGNEE = '" + SessionUtil.getUserId() + "' OR SUBSTR (NODEDEFINEKEY, -1) = ");
            sBuffer.append(" (SELECT BRANCHID FROM CS_SIGN_BRANCH WHERE signid = BUSINESSKEY AND orgid = ");
            sBuffer.append(" (SELECT ID FROM V_ORG_DEPT WHERE DIRECTORID = '" + SessionUtil.getUserId() + "'))) ");
            criteria.add(Restrictions.sqlRestriction(sBuffer.toString()));
            //除去部门分办环节
            criteria.add(Restrictions.not(Restrictions.like(RuProcessTask_.nodeDefineKey.getName(), "%" + FlowConstant.FLOW_SIGN_BMFB1.substring(0, FlowConstant.FLOW_SIGN_BMFB1.length() - 1) + "%")));
            //是副主任，只要没发文，均可取回
        } else {
            sBuffer.append(" ( ( SUBSTR (NODEDEFINEKEY, -1)) = '" + FlowConstant.SignFlowParams.BRANCH_INDEX1.getValue() + "' ");
            sBuffer.append(" OR ( SUBSTR (NODEDEFINEKEY, -1)) = '" + FlowConstant.SignFlowParams.BRANCH_INDEX2.getValue() + "' ");
            sBuffer.append(" OR ( SUBSTR (NODEDEFINEKEY, -1)) = '" + FlowConstant.SignFlowParams.BRANCH_INDEX3.getValue() + "' ");
            sBuffer.append(" OR ( SUBSTR (NODEDEFINEKEY, -1)) = '" + FlowConstant.SignFlowParams.BRANCH_INDEX4.getValue() + "' ) ");
            //合并评审的工作方案，部长没审批时可以回退
            criteria.add(Restrictions.ge(RuProcessTask_.signprocessState.getName(), Constant.SignProcessState.IS_START.getValue()));
            criteria.add(Restrictions.le(RuProcessTask_.signprocessState.getName(), Constant.SignProcessState.END_WP.getValue()));
            criteria.add(Restrictions.sqlRestriction(sBuffer.toString()));
        }

        Integer totalResult = ((Number) criteria.setProjection(Projections.rowCount()).uniqueResult()).intValue();
        criteria.setProjection(null);
        // 处理分页
        if (odataObj.getSkip() > 0) {
            criteria.setFirstResult(odataObj.getSkip());
        }
        if (odataObj.getTop() > 0) {
            criteria.setMaxResults(odataObj.getTop());
        }
        List<RuProcessTask> runProcessList = criteria.list();
        /*//合并评审项目处理
        runProcessList.forEach(rl -> {
            if (Constant.EnumState.YES.getValue().equals(rl.getReviewType())) {
                rl.setReviewSignDtoList(findReviewSign(rl.getBusinessKey()));
            }
        });*/

        pageModelDto.setCount(totalResult);
        pageModelDto.setValue(runProcessList);
        return pageModelDto;
    }

    /**
     * 删除相应的分支信息（没有分支ID，则删除全部分支）
     *
     * @param signId
     * @param branchId
     */
    @Override
    @Transactional
    public void deleteBranchInfo(String signId, String branchId) {
        boolean deleteBranchId = false;
        if (Validate.isString(branchId)) {
            deleteBranchId = true;
        }
        //1、删除分支，如果是分管领导分办，则删除分支；如果是部长，则改变相应的状态即可
        if (deleteBranchId) {
            signBranchRepo.resetBranchState(signId, branchId);
        } else {
            HqlBuilder sqlBuilder1 = HqlBuilder.create();
            sqlBuilder1.append("delete from CS_SIGN_BRANCH where " + SignBranch_.signId.getName() + " =:signId ");
            sqlBuilder1.setParam("signId", signId);
            signBranchRepo.executeSql(sqlBuilder1);
        }

        //2、删除分支负责人
        HqlBuilder sqlBuilder2 = HqlBuilder.create();
        sqlBuilder2.append("delete from CS_SIGN_PRINCIPAL2 where " + SignPrincipal_.signId.getName() + " =:signId ");
        sqlBuilder2.setParam("signId", signId);
        if (deleteBranchId) {
            sqlBuilder2.append(" and " + SignPrincipal_.flowBranch.getName() + " =:branchId ");
            sqlBuilder2.setParam("branchId", branchId);
        }
        signBranchRepo.executeSql(sqlBuilder2);
        //3、删除工作方案、会议室、专家评审方案
        Sign sign = signRepo.findById(signId);
        List<WorkProgram> workProgramList = sign.getWorkProgramList();
        if (Validate.isList(workProgramList)) {
            String deleteWPId = null;             //删除工作方案ID
            for (int i = 0, l = workProgramList.size(); i < l; i++) {
                WorkProgram wp = workProgramList.get(i);
                if (deleteBranchId && branchId.equals(wp.getBranchId())) {
                    deleteWPId = wp.getId();
                    break;
                } else {
                    if (i > 0) {
                        deleteWPId += ",";
                    }
                    deleteWPId += wp.getId();
                }
            }
            //删除会议室信息
            roomBookingRepo.deleteById(RoomBooking_.businessId.getName(), deleteWPId);

            //如果是分管领导回退，则删除整个专家评审方案信息；部长回退，则删除对应的分支信息，并修改评审方案状态
            Criteria criteria = expertReviewRepo.getExecutableCriteria();
            criteria.add(Restrictions.eq(ExpertReview_.businessId.getName(), signId));
            List<ExpertReview> reviewList = criteria.list();
            if (Validate.isList(reviewList)) {
                ExpertReview review = reviewList.get(0);
                if (deleteBranchId) {
                    if (Validate.isList(review.getExpertSelConditionList())) {
                        for (int n = 0, m = review.getExpertSelConditionList().size(); n < m; n++) {
                            ExpertSelCondition ec = review.getExpertSelConditionList().get(n);
                            if (deleteWPId.equals(ec.getBusinessId())) {
                                expertSelConditionRepo.delete(ec);
                            }
                        }
                    }
                    if (Validate.isList(review.getExpertSelectedList())) {
                        for (int n = 0, m = review.getExpertSelectedList().size(); n < m; n++) {
                            ExpertSelected el = review.getExpertSelectedList().get(n);
                            if (deleteWPId.equals(el.getBusinessId())) {
                                expertSelectedRepo.delete(el);
                            }
                        }
                    }
                    review.setState(EnumState.NO.getValue());
                    expertReviewRepo.save(review);
                } else {
                    expertReviewRepo.delete(review);        //删除评审方案，顺便删除抽取专家信息(级联删除)
                }
            }
            //最后删除工作方案信息
            for (int i = 0, l = workProgramList.size(); i < l; i++) {
                WorkProgram wp = workProgramList.get(i);
                if (deleteBranchId && deleteWPId.equals(wp.getId())) {
                    workProgramRepo.delete(wp);
                    break;
                } else {
                    workProgramRepo.delete(wp);
                }
            }
        }

    }


    /**
     * 启动时更新数据状态
     *
     * @param businessKey
     */
    @Override
    public void updateState(String businessKey) {
        Sign sign = signRepo.findById(businessKey);
        //更改项目状态
        sign.setSignState(Constant.EnumState.PROCESS.getValue());
        sign.setIsLightUp(Constant.signEnumState.NOLIGHT.getValue());
        signRepo.save(sign);

    }
}
