package cs.service.project;

import com.alibaba.fastjson.JSON;
import cs.ahelper.projhelper.DisUtil;
import cs.ahelper.projhelper.ProjUtil;
import cs.ahelper.projhelper.WorkPGUtil;
import cs.common.HqlBuilder;
import cs.common.RandomGUID;
import cs.common.ResultMsg;
import cs.common.constants.Constant;
import cs.common.constants.FlowConstant;
import cs.common.constants.ProjectConstant;
import cs.common.constants.SysConstants;
import cs.common.utils.*;
import cs.domain.expert.*;
import cs.domain.external.Dept;
import cs.domain.flow.RuProcessTask;
import cs.domain.flow.RuProcessTask_;
import cs.domain.meeting.RoomBooking_;
import cs.domain.project.*;
import cs.domain.sys.*;
import cs.model.PageModelDto;
import cs.model.expert.ExpertReviewDto;
import cs.model.external.DeptDto;
import cs.model.external.OfficeUserDto;
import cs.model.flow.FlowDto;
import cs.model.project.*;
import cs.model.sys.OrgDto;
import cs.model.sys.SysConfigDto;
import cs.model.sys.SysFileDto;
import cs.model.sys.UserDto;
import cs.quartz.unit.DispathUnit;
import cs.quartz.unit.QuartzUnit;
import cs.repository.odata.ODataObj;
import cs.repository.repositoryImpl.expert.ExpertRepo;
import cs.repository.repositoryImpl.expert.ExpertReviewRepo;
import cs.repository.repositoryImpl.expert.ExpertSelConditionRepo;
import cs.repository.repositoryImpl.expert.ExpertSelectedRepo;
import cs.repository.repositoryImpl.external.DeptRepo;
import cs.repository.repositoryImpl.flow.RuProcessTaskRepo;
import cs.repository.repositoryImpl.meeting.RoomBookingRepo;
import cs.repository.repositoryImpl.project.*;
import cs.repository.repositoryImpl.sys.*;
import cs.service.expert.ExpertReviewService;
import cs.service.external.OfficeUserService;
import cs.service.flow.FlowService;
import cs.service.rtx.RTXSendMsgPool;
import cs.service.sys.*;
import cs.sql.*;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static cs.common.constants.Constant.*;
import static cs.common.constants.FlowConstant.*;
import static cs.common.constants.IgnoreProps.PUSH_SIGN_IGNORE_PROPS;
import static cs.common.constants.ProjectConstant.CAUTION_LIGHT_ENUM.NO_LIGHT;
import static cs.common.constants.SysConstants.SEPARATE_COMMA;

/**
 * @author ldm
 */
@Service
public class SignServiceImpl implements SignService {
    private static Logger log = Logger.getLogger(SignServiceImpl.class);
    @Autowired
    private SignRepo signRepo;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private UserService userService;
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
    private ExpertReviewService expertReviewService;
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
    @Autowired
    private WorkdayService workdayService;
    @Autowired
    private ProjectStopService projectStopService;
    @Autowired
    private ExpertRepo expertRepo;
    @Autowired
    private UnitScoreRepo unitScoreRepo;
    @Autowired
    private CompanyService companyService;
    @Autowired
    private UnitScoreService unitScoreService;
    @Autowired
    private AgentTaskService agentTaskService;
    @Autowired
    private AssistUnitRepo assistUnitRepo;
    @Autowired
    private ProjectStopRepo projectStopRepo;
    @Autowired
    private OrgDeptService orgDeptService;
    /**
     * 项目签收保存操作（这里的方法是正式签收）
     *
     * @param signDto
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultMsg createSign(SignDto signDto) {
        if (!ProjUtil.checkProjDataValidate(signDto)) {
            return ResultMsg.error("项目代码、评审阶段、项目名称和收文编号为空或者有特殊字符，保存失败！");
        }
        Date now = new Date();
        Sign sign = null;
        //是否是签收员操作，部分是由委里传过来的
        boolean isSignUser = Validate.isString(SessionUtil.getUserId());
        //如果不是评审中心自己的项目，还根据收文编号获取项目信息，以判断项目是否已存在
        boolean isSelfProj = ProjUtil.isSelfProj(signDto.getFilecode());
        if (!isSelfProj) {
            sign = signRepo.findByFilecode(signDto.getFilecode(), Constant.EnumState.DELETE.getValue());
        }
        //如果是新项目，则初始化收文信息
        if (!Validate.isObject(sign)) {
            sign = initNewSignInfo(signDto, false, now, isSignUser);
        } else {
            if (!signDto.getProjectname().equals(sign.getProjectname())) {
                updateProjectNameCascade(sign, signDto.getProjectname());
            }
            //如果之前已经有送件人签名，则不能覆盖（因为委里过来的值不是评审中心要的值）
            BeanCopierUtils.copyPropertiesIgnoreProps(signDto, sign, PUSH_SIGN_IGNORE_PROPS);
        }
        sign.setModifiedDate(now);
        sign.setModifiedBy(isSignUser ? SessionUtil.getDisplayName() : SysConstants.SUPER_ACCOUNT);

        //如果单位是手动添加时就添加到数据库
        if (Validate.isString(sign.getBuiltcompanyName())) {
            companyService.createSignCompany(sign.getBuiltcompanyName(), DEFAULT_BUILD_COMPNAME, isSignUser);
        }
        //添加单位评分(没有编制单位时也添加编制单位)
        if (Validate.isString(sign.getDesigncompanyName())) {
            unitScoreService.decide(sign.getDesigncompanyName(), sign.getSignid(), isSignUser);
        }
        //7、正式签收
        if (!Validate.isString(sign.getIssign()) || !EnumState.YES.getValue().equals(sign.getIssign())) {
            formalProject(sign);
        }
        //如果是自己的项目,则不用回传给委里(2表示不用回传给委里)
        if (isSelfProj) {
            sign.setIsSendFGW(EnumState.STOP.getValue());
        }
        signRepo.save(sign);
        return new ResultMsg(true, MsgCode.OK.getValue(), "操作成功！", sign);
    }

    /**
     * 正式签收项目的一些数据信息
     * @param sign
     */
    private void formalProject(Sign sign) {
        sign.setSigndate(new Date());
        //预签收状态也要改
        sign.setIspresign(Constant.EnumState.YES.getValue());
        //正式签收
        sign.setIssign(EnumState.YES.getValue());
        if (!Validate.isString(sign.getSignNum())) {
            //签收序号
            initSignNum(sign);
        }
        //评审天数
        Float reviewsDays = getReviewDays(sign.getReviewstage());
        if (reviewsDays > 0f) {
            sign.setSurplusdays(reviewsDays);
            sign.setTotalReviewdays(reviewsDays);
            sign.setReviewdays(0f);
            //计算预发文日期
            //1、先获取从签收日期后的30天之间的工作日情况
            List<Workday> workdayList = workdayService.getBetweenTimeDay(sign.getSigndate(), DateUtils.addDay(sign.getSigndate(), 30));
            //预发文时间，是剩余最后一个工作日的时间
            int totalDays = (new Float(reviewsDays)).intValue();
            Date expectDispatchDate = DispathUnit.dispathDate(workdayList, sign.getSigndate(), totalDays);
            sign.setExpectdispatchdate(expectDispatchDate);
        }
    }

    /**
     * 项目预签收
     *
     * @param signDto
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultMsg reserveAddSign(SignDto signDto) {
        if (!ProjUtil.checkProjDataValidate(signDto)) {
            return ResultMsg.error( "项目代码、评审阶段、项目名称和收文编号为空或者有特殊字符，保存失败！");
        }
        Sign sign = null;
        Date now = new Date();
        //是否是签收员操作
        boolean isSignUser = Validate.isString(SessionUtil.getUserId());
        /**
         * 如果收文编号以0000结束，说明委里没有收文编号，这个编号可以有多个
         * 之前委里收文编号年份后面+4位数，现在是5位数
         */
        boolean isSelfProj = ProjUtil.isSelfProj(signDto.getFilecode());
        if (!isSelfProj) {
            sign = signRepo.findByFilecode(signDto.getFilecode(), signDto.getSignState());
        }
        //如果之前已经有送件人签名，则不能覆盖（因为委里过来的值不是评审中心要的值）
        if (Validate.isObject(sign) && Validate.isString(sign.getSignid())) {
            String signName = sign.getSendusersign();
            BeanCopierUtils.copyPropertiesIgnoreNull(signDto, sign);
            sign.setSendusersign(signName);
        } else {
            sign = initNewSignInfo(signDto, true, now, isSignUser);
        }
        sign.setModifiedDate(now);
        sign.setModifiedBy(isSignUser ? SessionUtil.getDisplayName() : SysConstants.SUPER_ACCOUNT);

        //如果是自己的项目,则不用回传给委里(2表示不用回传给委里)
        if (isSelfProj) {
            sign.setIsSendFGW(EnumState.STOP.getValue());
        }
        signRepo.save(sign);
        return new ResultMsg(true, MsgCode.OK.getValue(), "操作成功！", sign);
    }

    /**
     * 初始化项目信息
     *
     * @param signDto
     * @param isPreSign 是否预签收项目
     * @return
     */
    private Sign initNewSignInfo(SignDto signDto, boolean isPreSign, Date now, boolean isSignUser) {
        Sign sign = new Sign();
        BeanCopierUtils.copyProperties(signDto, sign);
        sign.setSignid((new RandomGUID()).valueAfterMD5);
        sign.setSignState(EnumState.NORMAL.getValue());
        //2、是否是项目概算流程
        if (ProjectConstant.REVIEW_STATE_ENUM.STAGEBUDGET.getZhCode().equals(sign.getReviewstage())
                || Validate.isString(sign.getIschangeEstimate())) {
            sign.setIsassistflow(EnumState.YES.getValue());
        } else {
            sign.setIsassistflow(EnumState.NO.getValue());
        }
        //3、送件人为当前签收人，
        sign.setSendusersign(SessionUtil.getDisplayName());
        //初始化办理部门信息
        initSignDeptInfo(sign);
        sign.setCreatedDate(now);
        sign.setCreatedBy(isSignUser ? SessionUtil.getLoginName() : SysConstants.SUPER_ACCOUNT);
        //默认为不亮灯
        sign.setIsLightUp(NO_LIGHT.getCodeValue());
        //0 用于区别签收和预签收页面实现送来资料存放位置
        if (isPreSign) {
            //预签收日期
            sign.setPresignDate(now);
            sign.setIspresign(Constant.EnumState.NO.getValue());
        } else {
            //签收日期
            sign.setSigndate(now);
            sign.setReceivedate(now);
        }
        return sign;
    }

    @Override
    public PageModelDto<SignDto> get(ODataObj odataObj) {
        PageModelDto<SignDto> pageModelDto = new PageModelDto<>();
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
    @Transactional(rollbackFor = Exception.class)
    public ResultMsg updateSign(SignDto signDto) {
        if (!ProjUtil.checkProjDataValidate(signDto)) {
            return ResultMsg.error("项目代码、评审阶段、项目名称和收文编号为空或者有特殊字符，保存失败！");
        }

        boolean isSignUser = Validate.isString(SessionUtil.getUserId());
        Sign sign = signRepo.findById(signDto.getSignid());
        if (!signDto.getProjectname().equals(sign.getProjectname())) {
            updateProjectNameCascade(sign, signDto.getProjectname());
        }
        BeanCopierUtils.copyProperties(signDto, sign);
        String curUserName = SessionUtil.getDisplayName();
        Date nowDate = new Date();
        //添加单位评分
        if (Validate.isString(sign.getDesigncompanyName())) {
            Company company = companyRepo.findCompany(sign.getDesigncompanyName());
            UnitScore unitScore = unitScoreRepo.findUnitScore(sign.getSignid());
            if (unitScore != null) {
                unitScore.setCompany(company);
                unitScoreRepo.save(unitScore);
            } else {
                UnitScore unitScores = new UnitScore();
                unitScores.setSignid(sign.getSignid());
                unitScores.setCompany(company);
                unitScores.setId((new RandomGUID()).valueAfterMD5);
                unitScores.setCreatedBy(curUserName);
                unitScores.setModifiedBy(curUserName);
                unitScores.setCreatedDate(nowDate);
                unitScores.setModifiedDate(nowDate);
                unitScoreRepo.save(unitScores);
            }
        }

        //如果单位是手动添加时就添加到数据库
        //建设单位
        if (Validate.isString(signDto.getBuiltcompanyName())) {
            companyService.createSignCompany(signDto.getBuiltcompanyName(), DEFAULT_BUILD_COMPNAME, isSignUser);
        }
        /*//编制单位
        if (Validate.isString(signDto.getDesigncompanyName())) {
            companyService.createSignCompany(signDto.getDesigncompanyName(), DEFAULT_DESIGN_COMPNAME, isSignUser);
            companyService.createSignCompany(signDto.getBuiltcompanyName(), DEFAULT_BUILD_COMPNAME, isSignUser);
        }*/
        //编制单位
        if (Validate.isString(signDto.getDesigncompanyName())) {
            companyService.createSignCompany(signDto.getDesigncompanyName(), DEFAULT_DESIGN_COMPNAME, isSignUser);
        }
        sign.setModifiedBy(SessionUtil.getUserId());
        sign.setModifiedDate(nowDate);
        signRepo.save(sign);

        return ResultMsg.ok("操作成功!");
    }

    @Override
    public ResultMsg initFillPageData(String signId) {
        Map<String, Object> map = new HashMap<String, Object>();

        //1收文对象
        Sign sign = signRepo.findById(signId);
        SignDto signDto = new SignDto();
        BeanCopierUtils.copyProperties(sign, signDto);
        if (Validate.isEmpty(signDto.getSendusersign())) {
            signDto.setSendusersign(SessionUtil.getLoginName());
        }
        map.put("sign", signDto);

        //获取办事处所有信息
        List<DeptDto> deptDtoList = new ArrayList<DeptDto>();
        List<Dept> deptList = deptRepo.findAll();
        if (Validate.isList(deptList)) {
            deptList.forEach(o -> {
                DeptDto deptDto = new DeptDto();
                BeanCopierUtils.copyProperties(o, deptDto);
                deptDtoList.add(deptDto);
            });
            map.put("deptlist", deptDtoList);
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
        map.put("companyList", Validate.isList(companyList) ? companyList : new ArrayList<>());

        //查询系统上传文件
        Criteria file = sysFileRepo.getExecutableCriteria();
        file.add(Restrictions.eq(SysFile_.businessId.getName(), sign.getSignid()));
        List<SysFile> sysFiles = file.list();
        if (Validate.isList(sysFiles)) {
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
        //补充资料
        List<AddRegisterFile> registerFileList = addRegisterFileRepo.findByIds(AddRegisterFile_.businessId.getName(), sign.getSignid(), null);
        if (Validate.isList(registerFileList)) {
            List<AddRegisterFileDto> registerFileDtoList = new ArrayList<>(registerFileList.size());
            registerFileList.forEach(rl -> {
                AddRegisterFileDto dto = new AddRegisterFileDto();
                BeanCopierUtils.copyProperties(rl, dto);
                registerFileDtoList.add(dto);
            });
            map.put("registerFileDtoDtoList", registerFileDtoList);
        }
        //部门分组问题
        map.put("orgNameGroupMap",orgDeptService.groubOrgNameByType("   "));

        return new ResultMsg(true, MsgCode.OK.getValue(), "查询数据成功", map);
    }

    @Override
    public List<OrgDto> selectSign(ODataObj odataObj) {
        List<Org> orgList = orgRepo.findByOdata(odataObj);
        List<OrgDto> orgDtoList = new ArrayList<OrgDto>();
        if (Validate.isList(orgList)) {
            orgList.forEach(x -> {
                OrgDto orgDto = new OrgDto();
                BeanCopierUtils.copyProperties(x, orgDto);
                orgDtoList.add(orgDto);
            });
        }
        return orgDtoList;
    }

    /**
     * 删除项目信息
     *
     * @param signid
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultMsg deleteSign(String signid) {
/*        Sign sign = signRepo.findById(Sign_.signid.getName(), signid);
        if (Validate.isString(sign.getProcessInstanceId())) {
            return new ResultMsg(false, MsgCode.ERROR.getValue(), "该项目已经发起流程，不能删除！");
        }
        if (sign.getSigndate() != null || EnumState.YES.getValue().equals(sign.getIssign())) {
            return new ResultMsg(false, MsgCode.ERROR.getValue(), "该项目已经正式签收，不能删除！");
        }
        signRepo.deleteById(Sign_.signid.getName(), signid);*/
        if (signRepo.updateSignState(signid, Sign_.signState.getName(), EnumState.DELETE.getValue())) {
            return ResultMsg.ok("删除成功！");
        } else {
            return ResultMsg.error("删除失败！");
        }
    }

    @Override
    public SignDto findById(String signid, boolean queryAll) {
        Sign sign = signRepo.findById( Sign_.signid.getName() , signid);
        SignDto signDto = new SignDto();
        if (!Validate.isObject(sign) || !Validate.isString(sign.getSignid())) {
            return null;
        }
        BeanCopierUtils.copyProperties(sign, signDto);
        //查询所有的属性
        if (queryAll) {
            List<WorkProgram> workProgramList = ProjUtil.filterEnableWP(sign.getWorkProgramList());
            if (Validate.isList(workProgramList)) {
                boolean isMergeReview = false;
                int totalL = workProgramList.size();
                WorkProgram workProgram = null;
                //判断是否是合并评审项目，如果是，则要获取合并评审信息
                if (totalL == 1) {
                    workProgram = workProgramList.get(0);
                    if (!EnumState.YES.getValue().equals(workProgram.getBaseInfo())) {
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
                                List<WorkProgramDto> workProgramDtoList = new ArrayList<>(totalL);
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
                }

                if (!isMergeReview) {
                    List<WorkProgramDto> workProgramDtoList = new ArrayList<>();
                    //由于工作方案不是按主次顺便排序，则遍历工作方案，获取主工作方案
                    WorkProgram mainW = new WorkProgram();
                    if (totalL > 1) {
                        //遍历第一遍，先找出主分支工作方案
                        for (int i = 0; i < totalL; i++) {
                            WorkProgram wp = workProgramList.get(i);
                            if (FlowConstant.SignFlowParams.BRANCH_INDEX1.getValue().equals(wp.getBranchId())) {
                                mainW = wp;
                                break;
                            }
                        }
                    }

                    for (int i = 0; i < totalL; i++) {
                        workProgram = workProgramList.get(i);
                        //判断是否是项目基本信息
                        if (EnumState.YES.getValue().equals(workProgram.getBaseInfo())) {
                            ProjBaseInfoDto projBaseInfoDto = new ProjBaseInfoDto();
                            BeanCopierUtils.copyProperties(workProgram, projBaseInfoDto);
                            signDto.setProjBaseInfoDto(projBaseInfoDto);
                        } else {
                            WorkProgramDto workProgramDto = new WorkProgramDto();
                            BeanCopierUtils.copyProperties(workProgram, workProgramDto);
                            workProgramRepo.initWPMeetingExp(workProgramDto, workProgram);
                            workProgramDto.setSignId(signid);
                            if (!FlowConstant.SignFlowParams.BRANCH_INDEX1.getValue().equals(workProgram.getBranchId())) {
                                WorkProgramDto mainWPDto = new WorkProgramDto();
                                //如果已经填报了主工作方案，则从主工作方案中获取
                                if (Validate.isString(mainW.getId())) {
                                    BeanCopierUtils.copyProperties(mainW, mainWPDto);
                                    //否则从项目中初始化
                                } else {
                                    workProgramService.copySignCommonInfo(mainWPDto, sign);
                                }
                                workProgramDto.setMainWorkProgramDto(mainWPDto);
                            }
                            workProgramDtoList.add(workProgramDto);
                        }
                    }
                    if (Validate.isList(workProgramDtoList)) {
                        signDto.setWorkProgramDtoList(workProgramDtoList);
                    }
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
                //补充资料
                List<AddRegisterFile> registerFileList = addRegisterFileRepo.findByIds(AddRegisterFile_.businessId.getName(), sign.getFileRecord().getFileRecordId(), null);
                if (Validate.isList(registerFileList)) {
                    List<AddRegisterFileDto> registerFileDtoList = new ArrayList<>(registerFileList.size());
                    registerFileList.forEach(rl -> {
                        AddRegisterFileDto dto = new AddRegisterFileDto();
                        BeanCopierUtils.copyProperties(rl, dto);
                        registerFileDtoList.add(dto);
                    });
                    fileRecordDto.setRegisterFileDto(registerFileDtoList);
                }

                signDto.setFileRecordDto(fileRecordDto);
            }

            //如果是协审项目，还要查询项目协审方案信息
            if (  EnumState.YES.getValue().equals(sign.getIsassistproc())) {
                List<AssistPlanSignDto> planSignDtoList = assistPlanSignService.findBySignId(sign.getSignid());
                //设置项目名称之类的信息
                planSignDtoList.forEach(ps -> {
                    if (!Validate.isString(ps.getProjectName())) {
                        String newProjectName = signDto.getProjectname();
                        if (null != ps.getSplitNum() && ps.getSplitNum() > 1) {
                            newProjectName += "（" + ps.getSplitNum() + "）";
                        }
                        ps.setProjectName(newProjectName);
                    }
                });
                signDto.setPlanSignDtoList(planSignDtoList);
            }

            //专家评审方案
            ExpertReview expertReview = expertReviewRepo.findByBusinessId(signid);
            if (Validate.isObject(expertReview)) {
                ExpertReviewDto expertReviewDto = expertReviewRepo.formatReview(expertReview);
                signDto.setExpertReviewDto(expertReviewDto);
            }
            //单位评分（新系统才有）
            if (!Validate.isString(sign.getOldProjectId())) {
                if (Validate.isString(sign.getDesigncompanyName())) {
                    //查找单位评分列表
                    UnitScore unitScore = unitScoreRepo.findUnitScore(signid);
                    if (Validate.isObject(unitScore) && Validate.isString(unitScore.getCompany())) {
                        UnitScoreDto unitScoreDto = new UnitScoreDto();
                        BeanCopierUtils.copyProperties(unitScore, unitScoreDto);
                        signDto.setUnitScoreDto(unitScoreDto);
                    } else {
                        //添加
                        unitScoreService.decide(sign.getDesigncompanyName(), signid, Validate.isString(SessionUtil.getUserId()));
                        UnitScore unitScores = unitScoreRepo.findUnitScore(signid);
                        UnitScoreDto unitScoreDto = new UnitScoreDto();
                        BeanCopierUtils.copyProperties(unitScores, unitScoreDto);
                        signDto.setUnitScoreDto(unitScoreDto);
                    }
                }
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

            //补充资料
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
                //自身还没加上去，在页面处理
                getPreAssociateDto(sign.getAssociateSign(), signDtoList);
                signDto.setAssociateSignDtoList(signDtoList);
            }

            //项目暂停信息
            List<ProjectStop> projectStopList = projectStopRepo.findByIds(ProjectStop_.sign.getName() + "." + Sign_.signid.getName(), signid, null);
            if (Validate.isList(projectStopList)) {
                List<ProjectStopDto> projectStopDtoList = new ArrayList<>(projectStopList.size());
                for (ProjectStop p : projectStopList) {
                    ProjectStopDto projectStopDto = new ProjectStopDto();
                    BeanCopierUtils.copyProperties(p, projectStopDto);
                    projectStopDtoList.add(projectStopDto);
                }
                signDto.setProjectStopDtos(projectStopDtoList);
            }
        }
        signDto.setCurDate(DateUtils.converToString(new Date(), "yyyy-MM-dd"));
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
        if (signRepo.updateSignState(signid, Sign_.signState.getName(), EnumState.FORCE.getValue())) {
            return ResultMsg.ok("操作成功！");
        }
        return ResultMsg.error("操作失败！");
    }

    /************************************** S  新流程项目处理   *********************************************/
    /**
     * 发起流程
     *
     * @param signid
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultMsg startNewFlow(String signid) {
        Sign sign = signRepo.findById(Sign_.signid.getName(), signid);
        if (sign == null) {
            return ResultMsg.error("发起流程失败，该项目已不存在！");
        }
        if (Validate.isString(sign.getProcessInstanceId())) {
            return ResultMsg.error( "该项目已发起流程！");
        }
        if (!Validate.isString(sign.getLeaderId())) {
            return ResultMsg.error("操作失败，请先设置默认办理部门！");
        }
        //项目建议书、可研、概算要验证必填字段(建设单位(builtcompanyName)、编制单位(designcompanyName)、主办处室(maindeptName)、缓急程度(urgencydegree)、秘密等级(secrectlevel))
        if (ProjectConstant.REVIEW_STATE_ENUM.STAGESUG.getZhCode().equals(sign.getReviewstage())
                ||  ProjectConstant.REVIEW_STATE_ENUM.STAGESTUDY.getZhCode().equals(sign.getReviewstage())
                || ProjectConstant.REVIEW_STATE_ENUM.STAGEBUDGET.getZhCode().equals(sign.getReviewstage())) {
            if (!Validate.isString(sign.getBuiltcompanyName()) || !Validate.isString(sign.getDesigncompanyName())
                    || !Validate.isString(sign.getMaindeptName()) || !Validate.isString(sign.getUrgencydegree())
                    || !Validate.isString(sign.getSecrectlevel())) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(sign.getBuiltcompanyName() == null ? DEFAULT_BUILD_COMPNAME + "," : "");
                stringBuilder.append(sign.getDesigncompanyName() == null ? DEFAULT_DESIGN_COMPNAME + "," : "");
                stringBuilder.append(sign.getMaindeptName() == null ? "主办处室," : "");
                stringBuilder.append(sign.getUrgencydegree() == null ? "缓急程度," : "");
                stringBuilder.append(sign.getSecrectlevel() == null ? "秘密等级," : "");
                String resultStr = stringBuilder.toString();
                return ResultMsg.error(resultStr.substring(0, resultStr.length() - 1) + "不能为空");
            }
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
            //送件人,默认为流程发起人
            sign.setSendusersign(SessionUtil.getDisplayName());
            sign.setCreatedBy(SessionUtil.getUserId());
            signRepo.save(sign);

            //4、跳过第一环节（主任审核）
            Task task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).active().singleResult();
            //添加处理信息
            taskService.addComment(task.getId(), processInstance.getId(), "");
            taskService.complete(task.getId(), ActivitiUtil.setAssigneeValue(FlowConstant.SignFlowParams.USER_QS.getValue(), SessionUtil.getUserId()));

            //5、跳过第二环节（签收）
            task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).active().singleResult();
            //添加处理信息
            taskService.addComment(task.getId(), processInstance.getId(), "");
            taskService.complete(task.getId(), ActivitiUtil.setAssigneeValue(FlowConstant.SignFlowParams.USER_ZHB.getValue(), SessionUtil.getUserId()));

            //6、跳过第三个环节（综合部拟办意见）
            task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).active().singleResult();
            //综合部拟办意见
            taskService.addComment(task.getId(), processInstance.getId(), sign.getComprehensivehandlesug());
            //查询是否有待办人员
            List<AgentTask> agentTaskList = new ArrayList<>();
            String assigneeValue = userService.getTaskDealId(sign.getLeaderId(), agentTaskList, FlowConstant.FLOW_SIGN_FGLD_FB);
            taskService.complete(task.getId(), ActivitiUtil.setAssigneeValue(FlowConstant.SignFlowParams.USER_FGLD.getValue(), assigneeValue));
            //保存代办
            if (Validate.isList(agentTaskList)) {
                agentTaskService.updateAgentInfo(agentTaskList, processInstance.getId(), processInstance.getName());
            }
            //放入腾讯通消息缓冲池
            RTXSendMsgPool.getInstance().sendReceiverIdPool(task.getId(), assigneeValue);
            //发送短信消息
            return new ResultMsg(true, MsgCode.OK.getValue(), task.getId(), "操作成功！", sign.getProjectname());
        } catch (Exception e) {
            log.error("发起项目签收流程异常：" + e.getMessage());
            return ResultMsg.error( "操作异常，错误信息已记录，请刷新重试或联系管理员处理！");
        }
    }

    @Override
    @Transactional
    public ResultMsg dealFlow(ProcessInstance processInstance, Task task, FlowDto flowDto) throws Exception {
        //参数定义
        String signid = processInstance.getBusinessKey(),
                businessId = "",                        //前段传过来的业务ID
                businessValue = "",                     //业务值
                assigneeValue = "",                     //环节处理人
                branchIndex = "",                       //分支序号
                nextNodeKey = "",                       //下一环节名称
                curUserId = SessionUtil.getUserId();    //当前用户ID
        Sign sign = null;                           //收文对象
        WorkProgram wk = null;                      //工作方案
        DispatchDoc dp = null;                      //发文
        FileRecord fileRecord = null;               //归档
        List<User> userList = null;                 //用户列表
        List<SignPrincipal> signPriList = null;     //项目负责人
        User dealUser = null;                       //处理人
        OrgDept orgDept = null;                     //部门和小组
        boolean isNextUser = false,                 //是否是下一环节处理人（主要是处理领导审批，目前主要有三个地方，部长审批工作方案，部长审批发文和分管领导审批发文）
                isAgentTask = agentTaskService.isAgentTask(task.getId(), curUserId),
                isMergeDisTask = false,             //是否合并发文任务
                isCompeleteSign = true;             //是否完成所有会签
        List<AgentTask> agentTaskList = new ArrayList<>();
        //取得之前的环节处理人信息
        Map<String, Object> variables = new HashMap<>();
        ResultMsg returnResult = null;
        DisUtil disUtil = null;
        WorkPGUtil workPGUtil = null;

        //以下是流程环节处理
        switch (task.getTaskDefinitionKey()) {
            //项目签收
            case FlowConstant.FLOW_SIGN_QS:
                task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).active().singleResult();
                //添加处理信息
                taskService.addComment(task.getId(), processInstance.getId(), flowDto.getDealOption());
                taskService.complete(task.getId(), ActivitiUtil.setAssigneeValue(FlowConstant.SignFlowParams.USER_ZHB.getValue(), SessionUtil.getUserId()));

                sign = signRepo.findById(Sign_.signid.getName(), signid);
                if (!Validate.isString(sign.getLeaderName())) {
                    return ResultMsg.error( "操作失败，请先设置默认办理部门！");
                }
                task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).active().singleResult();
             /*   taskService.addComment(task.getId(), processInstance.getId(), sign.getComprehensivehandlesug());    //综合部拟办意见*/
                //获取处理人
                assigneeValue = userService.getTaskDealId(sign.getLeaderId(), agentTaskList, FlowConstant.FLOW_SIGN_FGLD_FB);
                variables = ActivitiUtil.setAssigneeValue(FlowConstant.SignFlowParams.USER_FGLD.getValue(), assigneeValue);
                break;

            //分管副主任审批
            case FlowConstant.FLOW_SIGN_FGLD_FB:
                if (flowDto.getBusinessMap().get(SignFlowParams.MAIN_ORG.getValue()) == null) {
                    return ResultMsg.error("请先选择主办部门！");
                }
                sign = signRepo.findById(Sign_.signid.getName(), signid);
                //流程分支
                List<SignBranch> saveBranchList = new ArrayList<>();
                //主办部门ID
                businessId = flowDto.getBusinessMap().get(SignFlowParams.MAIN_ORG.getValue()).toString();
                SignBranch signBranch1 = new SignBranch(signid, FlowConstant.SignFlowParams.BRANCH_INDEX1.getValue(), EnumState.YES.getValue(), EnumState.NO.getValue(), EnumState.YES.getValue(), businessId, EnumState.NO.getValue());
                saveBranchList.add(signBranch1);
                //设置流程参数
                variables.put(FlowConstant.SignFlowParams.BRANCH1.getValue(), true);
                orgDept = orgDeptRepo.findOrgDeptById(businessId);
                if (orgDept == null || !Validate.isString(orgDept.getDirectorID())) {
                    return ResultMsg.error( "请设置" + orgDept.getName() + "的部门负责人！");
                }
                int branchCount = 1;
                //主办部门信息
                sign.setmOrgId(businessId);
                sign.setmOrgName(orgDept.getName());
                //获取处理人
                assigneeValue = userService.getTaskDealId(orgDept.getDirectorID(), agentTaskList, FlowConstant.FLOW_SIGN_BMFB1);
                variables.put(FlowConstant.SignFlowParams.USER_BZ1.getValue(), assigneeValue);
                /******************  一下处理协办部门 ****************/
                variables.put(FlowConstant.SignFlowParams.BRANCH2.getValue(), false);
                variables.put(FlowConstant.SignFlowParams.BRANCH3.getValue(), false);
                variables.put(FlowConstant.SignFlowParams.BRANCH4.getValue(), false);
                List<String> assistOrgIdList = null;
                //协办流程分支
                if (flowDto.getBusinessMap().get(SignFlowParams.ASSIST_ORG.getValue()) != null) {
                    businessId = flowDto.getBusinessMap().get(SignFlowParams.ASSIST_ORG.getValue()).toString();
                    assistOrgIdList = StringUtil.getSplit(businessId, SysConstants.SEPARATE_COMMA);
                }

                if (Validate.isList(assistOrgIdList)) {
                    if (assistOrgIdList.size() > 3) {
                        return ResultMsg.error( "协办部门最多只能选择3个！");
                    }
                    String aOrgId = "", aOrgName = "";
                    for (int i = 2, l = (assistOrgIdList.size() + 2); i < l; i++) {
                        businessId = assistOrgIdList.get(i - 2);
                        SignBranch signBranch = new SignBranch(signid, String.valueOf(i), EnumState.YES.getValue(), EnumState.NO.getValue(), EnumState.NO.getValue(), businessId, EnumState.NO.getValue());
                        saveBranchList.add(signBranch);
                        //判断是否有部门负责人
                        orgDept = orgDeptRepo.findOrgDeptById(businessId);
                        if (orgDept == null || !Validate.isString(orgDept.getDirectorID())) {
                            return ResultMsg.error( "请设置" + orgDept.getName() + "的部门负责人！");
                        }
                        aOrgId = StringUtil.joinString(aOrgId, SysConstants.SEPARATE_COMMA, businessId);
                        aOrgName = StringUtil.joinString(aOrgName, SysConstants.SEPARATE_COMMA, orgDept.getName());

                        nextNodeKey = FlowConstant.FLOW_SIGN_BMFB2;
                        switch (i) {
                            case 3:
                                nextNodeKey = FlowConstant.FLOW_SIGN_BMFB3;
                                break;
                            case 4:
                                nextNodeKey = FlowConstant.FLOW_SIGN_BMFB4;
                                break;
                            default:
                                ;
                        }
                        String userId = userService.getTaskDealId(orgDept.getDirectorID(), agentTaskList, nextNodeKey);
                        assigneeValue += SEPARATE_COMMA + userId;
                        //设置部门分支领导信息
                        ActivitiUtil.setFlowBrandLead(variables, i, userId);
                        branchCount++;
                    }
                    //协办部门信息
                    sign.setaOrgId(aOrgId);
                    sign.setaOrgName(aOrgName);
                }
                signBranchRepo.bathUpdate(saveBranchList);
                //更改项目信息
                sign.setLeaderhandlesug(flowDto.getDealOption());
                sign.setLeaderDate(new Date());
                if (isAgentTask) {
                    sign.setLeaderId(agentTaskService.getUserId(task.getId(), curUserId));
                } else {
                    sign.setLeaderId(curUserId);
                }
                sign.setLeaderName(ActivitiUtil.getSignName(SessionUtil.getDisplayName(), isAgentTask));
                //清空部长审核意见
                sign.setMinisterhandlesug("");
                sign.setMinisterDate(null);
                sign.setMinisterId("");
                sign.setMinisterName("");
                //记录总分支数
                sign.setBranchCount(branchCount);
                signRepo.save(sign);

                break;
            //部门分办1
            case FlowConstant.FLOW_SIGN_BMFB1:
                branchIndex = FlowConstant.SignFlowParams.BRANCH_INDEX1.getValue();
                nextNodeKey = FlowConstant.FLOW_SIGN_XMFZR1;
                //部门分办2
            case FlowConstant.FLOW_SIGN_BMFB2:
                if (!Validate.isString(branchIndex)) {
                    branchIndex = FlowConstant.SignFlowParams.BRANCH_INDEX2.getValue();
                    nextNodeKey = FlowConstant.FLOW_SIGN_XMFZR2;
                }
                //部门分办3
            case FlowConstant.FLOW_SIGN_BMFB3:
                if (!Validate.isString(branchIndex)) {
                    branchIndex = FlowConstant.SignFlowParams.BRANCH_INDEX3.getValue();
                    nextNodeKey = FlowConstant.FLOW_SIGN_XMFZR3;
                }
                //部门分办4
            case FlowConstant.FLOW_SIGN_BMFB4:
                if (!Validate.isString(branchIndex)) {
                    branchIndex = FlowConstant.SignFlowParams.BRANCH_INDEX4.getValue();
                    nextNodeKey = FlowConstant.FLOW_SIGN_XMFZR4;
                }
                sign = signRepo.findById(Sign_.signid.getName(), signid);
                //1、如果是协审项目
                if (EnumState.YES.getValue().equals(sign.getIsassistflow())) {
                    if (flowDto.getBusinessMap().get(SignFlowParams.PRINCIPAL.getValue()) == null) {
                        return ResultMsg.error( "请选择项目负责人！");
                    }
                    signPriList = JSON.parseArray(flowDto.getBusinessMap().get(SignFlowParams.PRINCIPAL.getValue()).toString(), SignPrincipal.class);
                    if (!Validate.isList(signPriList)) {
                        return ResultMsg.error( "请选择项目负责人！");
                    }
                    String mUserId = sign.getmUserId() == null ? "" : sign.getmUserId(),
                            mUserName = sign.getmUserName() == null ? "" : sign.getmUserName(),
                            aUserId = sign.getaUserID() == null ? "" : sign.getaUserID(),
                            aUserName = sign.getaUserName() == null ? "" : sign.getaUserName();
                    for (int i = 0, l = signPriList.size(); i < l; i++) {
                        SignPrincipal obj = signPriList.get(i);
                        dealUser = userRepo.getCacheUserById(obj.getUserId());
                        String userId = userService.getTaskDealId(dealUser, agentTaskList, nextNodeKey);
                        assigneeValue = StringUtil.joinString(assigneeValue, SEPARATE_COMMA, userId);
                        obj.setSignId(signid);
                        obj.setFlowBranch(branchIndex);
                        //设置负责人信息
                        if (EnumState.YES.getValue().equals(obj.getIsMainUser())) {
                            mUserId = obj.getUserId();
                            mUserName = dealUser.getDisplayName();
                        } else {
                            aUserId = StringUtil.joinString(aUserId, SEPARATE_COMMA, obj.getUserId());
                            aUserName = StringUtil.joinString(aUserName, SEPARATE_COMMA, dealUser.getDisplayName());
                        }
                    }
                    //设置主办人员信息
                    sign.setmUserId(mUserId);
                    sign.setmUserName(mUserName);
                    sign.setaUserID(aUserId);
                    sign.setaUserName(aUserName);
                    //不是协审项目
                } else {
                    //主流程处理，一定要有第一负责人
                    if (FlowConstant.SignFlowParams.BRANCH_INDEX1.getValue().equals(branchIndex)) {
                        if (flowDto.getBusinessMap().get(SignFlowParams.M_USER_ID.getValue()) == null) {
                            return ResultMsg.error("请选择第一负责人！");
                        }
                        businessValue = flowDto.getBusinessMap().get(SignFlowParams.M_USER_ID.getValue()).toString();
                        //查询是否有代办
                        dealUser = userRepo.getCacheUserById(businessValue);
                        assigneeValue = userService.getTaskDealId(dealUser, agentTaskList, nextNodeKey);
                        //设置主办人员信息
                        sign.setmUserId(businessValue);
                        sign.setmUserName(dealUser.getDisplayName());

                        //设置项目负责人
                        signPriList = new ArrayList<>();
                        SignPrincipal mainPri = new SignPrincipal(signid, businessValue, branchIndex, "", null, EnumState.YES.getValue());
                        signPriList.add(mainPri);
                    }
                    //项目负责人
                    if (flowDto.getBusinessMap().get(SignFlowParams.A_USER_ID.getValue()) != null) {
                        businessValue = flowDto.getBusinessMap().get(SignFlowParams.A_USER_ID.getValue()).toString();
                        userList = userRepo.getCacheUserListById(businessValue);
                        if (signPriList == null) {
                            signPriList = new ArrayList<>();
                        }
                        String aUserId = sign.getaUserID() == null ? "" : sign.getaUserID(),
                                aUserName = sign.getaUserName() == null ? "" : sign.getaUserName();
                        for (User user : userList) {
                            aUserId = StringUtil.joinString(aUserId, SysConstants.SEPARATE_COMMA, user.getId());
                            aUserName = StringUtil.joinString(aUserName, SysConstants.SEPARATE_COMMA, user.getDisplayName());
                            assigneeValue = StringUtil.joinString(assigneeValue, SysConstants.SEPARATE_COMMA, user.getId());

                            SignPrincipal secondPri = new SignPrincipal(signid, user.getId(), branchIndex, "", null, EnumState.NO.getValue());
                            signPriList.add(secondPri);
                        }
                        //设置协办人员信息
                        sign.setaUserID(aUserId);
                        sign.setaUserName(aUserName);
                    } else {
                        //分支流程必须要选择第二负责人
                        if (!FlowConstant.SignFlowParams.BRANCH_INDEX1.getValue().equals(branchIndex)) {
                            return ResultMsg.error( "请选择项目负责人！");
                        }
                    }
                }
                //保存项目负责人
                signPrincipalRepo.bathUpdate(signPriList);
                ActivitiUtil.setFlowPriUser(variables, branchIndex, assigneeValue);
                //保存处理意见，单个分支按之前的格式，跟多分支的时候，拼接下
                if (sign.getBranchCount() == 1) {
                    sign.setMinisterhandlesug(flowDto.getDealOption());
                } else {
                    String optionString = Validate.isString(sign.getMinisterhandlesug()) ? (sign.getMinisterhandlesug() + "<br>") : "";
                    sign.setMinisterhandlesug(optionString + flowDto.getDealOption() + " 签名：" + ActivitiUtil.getSignName(SessionUtil.getDisplayName(), isAgentTask) + "  日期：" + DateUtils.converToString(new Date(), "yyyy年MM月dd日"));
                }
                //主办部门意见也保存下
                if (branchIndex.equals(FlowConstant.SignFlowParams.BRANCH_INDEX1.getValue())) {
                    sign.setMinisterDate(new Date());
                    if (isAgentTask) {
                        sign.setMinisterId(agentTaskService.getUserId(task.getId(), curUserId));
                    } else {
                        sign.setMinisterId(curUserId);
                    }
                    sign.setMinisterName(ActivitiUtil.getSignName(SessionUtil.getDisplayName(), isAgentTask));
                }
                //完成部门分办，表示正在做工作方案
                sign.setProcessState(Constant.SignProcessState.DO_WP.getValue());
                //更新工作方案中的项目负责人
                WorkProgram mainwk2 = workProgramRepo.findBySignIdAndBranchId(signid, FlowConstant.SignFlowParams.BRANCH_INDEX1.getValue(), false);
                if (Validate.isObject(mainwk2)) {
                    mainwk2.setSecondChargeUserName(sign.getaUserName());
                    mainwk2.setSign(sign);
                    workProgramRepo.save(mainwk2);
                }
                signRepo.save(sign);
                break;

            //项目负责人办理1
            case FlowConstant.FLOW_SIGN_XMFZR1:
                branchIndex = FlowConstant.SignFlowParams.BRANCH_INDEX1.getValue();
                nextNodeKey = FlowConstant.FLOW_SIGN_BMLD_SPW1;
                //项目负责人办理2
            case FlowConstant.FLOW_SIGN_XMFZR2:
                if (!Validate.isString(branchIndex)) {
                    branchIndex = FlowConstant.SignFlowParams.BRANCH_INDEX2.getValue();
                    nextNodeKey = FlowConstant.FLOW_SIGN_BMLD_SPW2;
                }
                //项目负责人办理3
            case FlowConstant.FLOW_SIGN_XMFZR3:
                if (!Validate.isString(branchIndex)) {
                    branchIndex = FlowConstant.SignFlowParams.BRANCH_INDEX3.getValue();
                    nextNodeKey = FlowConstant.FLOW_SIGN_BMLD_SPW3;
                }
                //项目负责人办理4
            case FlowConstant.FLOW_SIGN_XMFZR4:
                if (!Validate.isString(branchIndex)) {
                    branchIndex = FlowConstant.SignFlowParams.BRANCH_INDEX4.getValue();
                    nextNodeKey = FlowConstant.FLOW_SIGN_BMLD_SPW4;
                }
                sign = signRepo.findById(Sign_.signid.getName(), signid);
                String mianBranch = FlowConstant.SignFlowParams.BRANCH_INDEX1.getValue();
                boolean isMainBranch = mianBranch.equals(branchIndex);
                //主流程要第一负责才能进行下一步操作
                if (isMainBranch) {
                    User mainUser = userRepo.getCacheUserById(sign.getmUserId());
                    if (!curUserId.equals(mainUser.getId()) && !curUserId.equals(mainUser.getTakeUserId())) {
                        return ResultMsg.error("您不是第一负责人，不能进行下一步操作！");
                    }
                }
                //是否需要做工作方案
                businessValue = flowDto.getBusinessMap().get(SignFlowParams.IS_NEED_WP.getValue()).toString();
                if (EnumState.YES.getValue().equals(businessValue)) {
                    //查找该分支做的工作方案
                    wk = workProgramRepo.findBySignIdAndBranchId(signid, branchIndex, false);
                    //如果做工作方案，则要判断该分支工作方案是否完成
                    if (!Validate.isObject(wk) || !Validate.isString(wk.getId())) {
                        return ResultMsg.error( "您还没有完成工作方案，不能进行下一步操作！");
                    }
                    //判断是否预定了会议室和选择了专家（只对主分支判断，因为协办分支以主分支为准，当然也可以自己选，但是不是强制要求）
                    if (isMainBranch) {
                        //是否专家评审
                        boolean isExpertReview = Constant.MergeType.REVIEW_MEETING.getValue().equals(wk.getReviewType()) || Constant.MergeType.REVIEW_LEETER.getValue().equals(wk.getReviewType());
                        //是否单个评审
                        boolean isSigle = Constant.MergeType.REVIEW_SIGNLE.getValue().equals(wk.getIsSigle());
                        //是否合并评审主项目
                        boolean isMergeMain = (Constant.MergeType.REVIEW_MERGE.getValue().equals(wk.getIsSigle()) && EnumState.YES.getValue().equals(wk.getIsMainProject()));
                        //单个评审或者合并评审主项目；如果是专家评审会，则要选择专家和会议室
                        boolean needCheck = isExpertReview && (isSigle || isMergeMain);
                        if (needCheck) {
                            if (expertRepo.countByBusinessId(wk.getId()) == 0) {
                                return ResultMsg.error( "您选择的评审方式是【" + wk.getReviewType() + "】，但是还没有选择专家，请先选择专家！");
                            }
                            if (Constant.MergeType.REVIEW_MEETING.getValue().equals(wk.getReviewType()) && !roomBookingRepo.isHaveBookMeeting(wk.getId())) {
                                return ResultMsg.error("您选择的评审方式是【" + wk.getReviewType() + "】，但是还没有选择会议室，请先预定会议室！");
                            }
                        }
                        //如果没有合并其他项目，则不准提交
                        if (isMergeMain && !signMergeRepo.isHaveMerge(signid, Constant.MergeType.WORK_PROGRAM.getValue())) {
                            return ResultMsg.error( "工作方案您选择的是合并评审主项目，您还没有设置关联项目，不能提交到下一步！");
                        }
                        //如果合并评审次项目没提交，不能进行下一步操作
                        if (!signRepo.isMergeSignEndWP(signid)) {
                            return ResultMsg.error("合并评审次项目还未提交审批，主项目不能提交审批！");
                        }
                    } else {
                        WorkProgram mainwk = workProgramRepo.findBySignIdAndBranchId(signid, mianBranch, false);
                        if (!Validate.isObject(mainwk)) {
                            return ResultMsg.error( "主流程还没进行工作方案信息填写，不能提交下一步操作！");
                        }
                    }

                    //查询部门领导
                    orgDept = orgDeptRepo.queryBySignBranchId(signid, branchIndex);
                    if (orgDept == null || !Validate.isString(orgDept.getDirectorID())) {
                        return ResultMsg.error("请设置该分支的部门负责人！");
                    }
                    assigneeValue = userService.getTaskDealId(orgDept.getDirectorID(), agentTaskList, nextNodeKey);
                    //设定是否做工作方案桉树
                    ActivitiUtil.setWorkParam(variables, branchIndex, true);
                    //更改预定会议室状态
                    roomBookingRepo.updateStateByBusinessId(wk.getId(), EnumState.PROCESS.getValue());
                    //完成分支工作方案
                    signBranchRepo.finishWP(signid, wk.getBranchId());

                } else {
                    //不做工作方案
                    //1、如果是主分支，则要填写项目基本信息
                    if (isMainBranch) {
                        wk = workProgramRepo.findBySignIdAndBranchId(signid, branchIndex, true);
                        if (!Validate.isObject(wk)) {
                            return ResultMsg.error("请先填写项目基本信息！");
                        }
                    }
                    dealUser = signPrincipalService.getMainPriUser(signid);
                    if (dealUser == null) {
                        return ResultMsg.error( "项目还没分配主负责人，不能进行下一步操作！请联系主办部门进行负责人分配！");
                    }
                    assigneeValue = userService.getTaskDealId(dealUser, agentTaskList, FLOW_SIGN_FW);
                    variables.put(FlowConstant.SignFlowParams.USER_FZR1.getValue(), assigneeValue);
                    //更改状态
                    signBranchRepo.isNeedWP(signid, branchIndex, EnumState.NO.getValue());
                    //注意：1、项目建议书、可研阶段一定要做工作方案；
                    // 2、主分支跳转，则必须要所有协办分支都完成才能跳转。
                    if (FlowConstant.SignFlowParams.BRANCH_INDEX1.getValue().equals(branchIndex)) {
                        if ((signBranchRepo.countBranch(signid) > 1) && signBranchRepo.assistFlowFinish(signid)) {
                            return ResultMsg.error("协办分支还没处理完，不能直接进行发文操作！");
                        }
                        sign = signRepo.findById(Sign_.signid.getName(), signid);
                        boolean needCheck = (ProjectConstant.REVIEW_STATE_ENUM.STAGESUG.getZhCode()).equals(sign.getReviewstage())
                                || (ProjectConstant.REVIEW_STATE_ENUM.STAGESTUDY.getZhCode()).equals(sign.getReviewstage());
                        if (needCheck && !signBranchRepo.isHaveWP(signid)) {
                            return ResultMsg.error("【项目建议书】和【可行性研究报告】阶段必须要做工作方案！");
                        }
                    }
                    signBranchRepo.updateFinishState(signid, branchIndex, EnumState.YES.getValue());
                    //设定是否做工作方案参数
                    ActivitiUtil.setWorkParam(variables, branchIndex, false);
                }
                //判断是否完成所有工作方案
                if (signBranchRepo.allWPFinish(signid)) {
                    signRepo.updateSignProcessState(signid, Constant.SignProcessState.END_WP.getValue());
                }
                break;

            //部长审批工作方案1
            case FlowConstant.FLOW_SIGN_BMLD_SPW1:
                branchIndex = FlowConstant.SignFlowParams.BRANCH_INDEX1.getValue();
                nextNodeKey = FlowConstant.FLOW_SIGN_FGLD_SPW1;
                //部长审批工作方案2
            case FlowConstant.FLOW_SIGN_BMLD_SPW2:
                if (!Validate.isString(branchIndex)) {
                    branchIndex = FlowConstant.SignFlowParams.BRANCH_INDEX2.getValue();
                    nextNodeKey = FlowConstant.FLOW_SIGN_FGLD_SPW2;
                }
                //部长审批工作方案3
            case FlowConstant.FLOW_SIGN_BMLD_SPW3:
                if (!Validate.isString(branchIndex)) {
                    branchIndex = FlowConstant.SignFlowParams.BRANCH_INDEX3.getValue();
                    nextNodeKey = FlowConstant.FLOW_SIGN_FGLD_SPW3;
                }
                //部长审批工作方案4
            case FlowConstant.FLOW_SIGN_BMLD_SPW4:
                if (!Validate.isString(branchIndex)) {
                    branchIndex = FlowConstant.SignFlowParams.BRANCH_INDEX4.getValue();
                    nextNodeKey = FlowConstant.FLOW_SIGN_FGLD_SPW4;
                }
                if (SessionUtil.getUserInfo().getOrg() == null) {
                    return ResultMsg.error( "你还没设置所属部门！");
                }
                if (!Validate.isString(SessionUtil.getUserInfo().getOrg().getOrgSLeader())) {
                    return ResultMsg.error("请先设置该部门的分管副主任！");
                }
                //更改工作方案信息
                wk = workProgramRepo.findBySignIdAndBranchId(signid, branchIndex, false);
                workPGUtil = WorkPGUtil.create(wk);
                workPGUtil.setMinisterOption(flowDto.getDealOption(), new Date(), ActivitiUtil.getSignName(SessionUtil.getDisplayName(), isAgentTask));
                //如果是主办流程，要判断是否有合并评审方案，有则跟着主项目一起办理
                if (ProjUtil.isMainBranch(branchIndex)) {
                    if (workPGUtil.isMergeWP() && workPGUtil.isMainWP()) {
                        List<SignMerge> mergeList = signMergeRepo.findByType(signid, MergeType.WORK_PROGRAM.getValue());
                        if (Validate.isList(mergeList)) {
                            ResultMsg resultMsg = null;
                            FlowDto flowDto2 = new FlowDto(flowDto.getDealOption());
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

                //设定下一环节处理人【主分支哪个领导安排部门工作方案则由他审批，次分支则按按照部门所在领导审批】
                if (FlowConstant.SignFlowParams.BRANCH_INDEX1.getValue().equals(branchIndex)) {
                    sign = signRepo.findById(Sign_.signid.getName(), signid);
                    assigneeValue = userService.getTaskDealId(sign.getLeaderId(), agentTaskList, nextNodeKey);
                    variables.put(FlowConstant.SignFlowParams.USER_FGLD1.getValue(), assigneeValue);
                    nextNodeKey = FlowConstant.FLOW_SIGN_FGLD_SPW1;
                } else if (FlowConstant.SignFlowParams.BRANCH_INDEX2.getValue().equals(branchIndex)) {
                    assigneeValue = userService.getTaskDealId(SessionUtil.getUserInfo().getOrg().getOrgSLeader(), agentTaskList, nextNodeKey);
                    variables.put(FlowConstant.SignFlowParams.USER_FGLD2.getValue(), assigneeValue);
                    nextNodeKey = FlowConstant.FLOW_SIGN_FGLD_SPW2;
                } else if (FlowConstant.SignFlowParams.BRANCH_INDEX3.getValue().equals(branchIndex)) {
                    assigneeValue = userService.getTaskDealId(SessionUtil.getUserInfo().getOrg().getOrgSLeader(), agentTaskList, nextNodeKey);
                    variables.put(FlowConstant.SignFlowParams.USER_FGLD3.getValue(), assigneeValue);
                    nextNodeKey = FlowConstant.FLOW_SIGN_FGLD_SPW3;
                } else if (FlowConstant.SignFlowParams.BRANCH_INDEX4.getValue().equals(branchIndex)) {
                    assigneeValue = userService.getTaskDealId(SessionUtil.getUserInfo().getOrg().getOrgSLeader(), agentTaskList, nextNodeKey);
                    variables.put(FlowConstant.SignFlowParams.USER_FGLD4.getValue(), assigneeValue);
                    nextNodeKey = FlowConstant.FLOW_SIGN_FGLD_SPW4;
                }
                //下一环节还是自己
                if (assigneeValue.equals(SessionUtil.getUserId())) {
                    isNextUser = true;
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
                assigneeValue = userService.getTaskDealId(dealUser, agentTaskList, FLOW_SIGN_FW);
                variables.put(FlowConstant.SignFlowParams.USER_FZR1.getValue(), assigneeValue);

                //更改工作方案审核信息
                wk = workProgramRepo.findBySignIdAndBranchId(signid, branchIndex, false);
                workPGUtil = WorkPGUtil.create(wk);

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
                                boolean isPass = resultMsg.isFlag()
                                        || Constant.MsgCode.FLOW_INSTANCE_NULL.getValue().equals(resultMsg.getReCode())
                                        || Constant.MsgCode.FLOW_TASK_NULL.getValue().equals(resultMsg.getReCode())
                                        || Constant.MsgCode.FLOW_ACTIVI_NEQ.getValue().equals(resultMsg.getReCode())
                                        || Constant.MsgCode.FLOW_NOT_MATCH.getValue().equals(resultMsg.getReCode());
                                if (!isPass) {
                                    return resultMsg;
                                }
                            }
                        }
                    }
                }

                workPGUtil.setLeaderOption(flowDto.getDealOption(), new Date(), ActivitiUtil.getSignName(SessionUtil.getDisplayName(), isAgentTask));
                workProgramRepo.save(wk);
                //完成分支的工作方案
                signBranchRepo.updateFinishState(signid, branchIndex, EnumState.YES.getValue());
                //更改预定会议室状态
                roomBookingRepo.updateStateByBusinessId(wk.getId(), EnumState.YES.getValue());
                //更新评审会时间
                ExpertReview expertReview = expertReviewRepo.findById(ExpertReview_.businessId.getName(), signid);
                if (expertReview != null) {
                    //以主工作方案为准，工作方案不做工作方案，则任选一个
                    if (FlowConstant.SignFlowParams.BRANCH_INDEX1.getValue().equals(branchIndex)
                            || expertReview.getReviewDate() == null) {
                        //如果是专家评审会，获取评审会日期
                        if (Constant.MergeType.REVIEW_MEETING.getValue().equals(wk.getReviewType())) {
                            expertReview.setReviewDate(roomBookingRepo.getMeetingDateByBusinessId(wk.getId()));
                            //如果是专家函评，取函评日期并修改专家默认评审方式为函评
                        } else {
                            expertReview.setReviewDate(wk.getLetterDate());
                            expertSelectedRepo.updateExpertSelectState(wk.getId(), ExpertSelected_.isLetterRw.getName(), EnumState.YES.getValue());
                        }
                        expertReviewRepo.save(expertReview);
                    }
                }
                break;
            //发文申请
            case FLOW_SIGN_FW:
                if (!expertReviewRepo.isFinishEPGrade(signid)) {
                    return ResultMsg.error( "您还未对专家进行评分,不能提交到下一步操作！");
                }
                businessId = flowDto.getBusinessMap().get(SignFlowParams.DIS_ID.getValue()).toString();
                dp = dispatchDocRepo.findById(DispatchDoc_.id.getName(), businessId);
                disUtil = DisUtil.create(dp);
                //是否是合并发文项目
                if (disUtil.isMergeDis()) {
                    if (disUtil.isMainProj()) {
                        isMergeDisTask = true;
                    } else {
                        return ResultMsg.error( "合并发文的项目，只能由主项目进行操作！");
                    }
                }
                if (isMergeDisTask) {
                    //合并发文主项目，另起一个方法处理
                    returnResult = flowService.dealMerDisFlow(processInstance, task, dp, FLOW_SIGN_FW, flowDto, isAgentTask);
                } else {
                    //不是合并发文的项目，还是按原计划处理
                    if (!expertReviewService.checkReviewCost(signid)) {
                        return ResultMsg.error("您还没完成专家评审费发放，不能进行下一步操作！");
                    }
                    if (!checkFileUpload(signid)) {
                        return ResultMsg.error( "您还没上传[评审意见]或者[审核意见]附件信息！");
                    }
                    //有项目负责人，则项目负责人审核
                    userList = signPrincipalService.getAllSecondPriUser(signid);
                    if (Validate.isList(userList)) {
                        variables.put(FlowConstant.SignFlowParams.HAVE_XMFZR.getValue(), true);
                        for (int i = 0, l = userList.size(); i < l; i++) {
                            String userId = userService.getTaskDealId(userList.get(i).getId(), agentTaskList, FLOW_SIGN_QRFW);
                            assigneeValue = StringUtil.joinString(assigneeValue, SEPARATE_COMMA, userId);
                        }
                        variables.put(FlowConstant.SignFlowParams.USER_HQ_LIST.getValue(), StringUtil.getSplit(assigneeValue, ","));
                        //没有项目负责人，则主办部长审核
                    } else {
                        variables.put(FlowConstant.SignFlowParams.HAVE_XMFZR.getValue(), false);
                        assigneeValue = getMainDirecotr(signid, agentTaskList, FLOW_SIGN_BMLD_QRFW);
                        variables.put(FlowConstant.SignFlowParams.USER_BZ1.getValue(), assigneeValue);
                    }

                    //修改第一负责人意见
                    dp.setMianChargeSuggest(flowDto.getDealOption() + "  签名：" + ActivitiUtil.getSignName(SessionUtil.getDisplayName(), isAgentTask) + "  日期：" + DateUtils.converToString(new Date(), "yyyy年MM月dd日"));
                    DisUtil.create(dp).resetDisReviewOption(dp);
                    dispatchDocRepo.save(dp);
                    //完成发文
                    signRepo.updateSignProcessState(signid, Constant.SignProcessState.END_DIS.getValue());
                }

                break;
            //项目负责人确认发文（所有人确认通过才通过）
            case FLOW_SIGN_QRFW:
                if (flowDto.getBusinessMap().get(SignFlowParams.AGREE.getValue()) == null || !Validate.isString(flowDto.getBusinessMap().get(SignFlowParams.AGREE.getValue()).toString())) {
                    return ResultMsg.error( "请先勾选对应的审批结果！");
                }
                businessId = flowDto.getBusinessMap().get(SignFlowParams.DIS_ID.getValue()).toString();
                dp = dispatchDocRepo.findById(DispatchDoc_.id.getName(), businessId);
                disUtil = DisUtil.create(dp);
                //是否是合并发文项目
                if (disUtil.isMergeDis()) {
                    if (disUtil.isMainProj()) {
                        isMergeDisTask = true;
                    } else {
                        return ResultMsg.error( "合并发文的项目，只能由主项目进行操作！");
                    }
                }
                if (isMergeDisTask) {
                    //合并发文主项目，另起一个方法处理
                    returnResult = flowService.dealMerDisFlow(processInstance, task, dp, FLOW_SIGN_QRFW, flowDto, isAgentTask);
                } else {
                    //修改第二负责人意见
                    String optionString = Validate.isString(dp.getSecondChargeSuggest()) ? (dp.getSecondChargeSuggest() + "<br>") : "";
                    dp.setSecondChargeSuggest(optionString + flowDto.getDealOption() + " 签名：" + ActivitiUtil.getSignName(SessionUtil.getDisplayName(), isAgentTask) + " 日期：" + DateUtils.converToString(new Date(), "yyyy年MM月dd日"));
                    dispatchDocRepo.save(dp);
                    //如果同意
                    if (EnumState.YES.getValue().equals(flowDto.getBusinessMap().get(SignFlowParams.AGREE.getValue()).toString())) {
                        variables.put(FlowConstant.SignFlowParams.XMFZR_SP.getValue(), true);
                        //判断是否有协办部门
                        if (signBranchRepo.allAssistCount(signid) > 0) {
                            variables.put(FlowConstant.SignFlowParams.HAVE_XB.getValue(), true);
                            userList = signBranchRepo.findAssistOrgDirector(signid);
                            for (int i = 0, l = userList.size(); i < l; i++) {
                                String userId = userService.getTaskDealId(userList.get(i).getId(), agentTaskList, FlowConstant.FLOW_SIGN_BMLD_QRFW_XB);
                                assigneeValue = StringUtil.joinString(assigneeValue, SEPARATE_COMMA, userId);
                            }
                            variables.put(FlowConstant.SignFlowParams.USER_XBBZ_LIST.getValue(), StringUtil.getSplit(assigneeValue, SEPARATE_COMMA));
                        } else {
                            variables.put(FlowConstant.SignFlowParams.HAVE_XB.getValue(), false);
                            //获取主分支的部门领导
                            assigneeValue = getMainDirecotr(signid, agentTaskList, FLOW_SIGN_BMLD_QRFW);
                            variables.put(FlowConstant.SignFlowParams.USER_BZ1.getValue(), assigneeValue);
                        }
                        flowDto.setDealOption(flowDto.getDealOption() + "【审批结果：核稿无误】");

                        isCompeleteSign = ProjUtil.checkSignComplete(taskService.getVariables(task.getId()), 1);
                        //如果不同意，则流程回到发文申请环节
                    } else {
                        variables.put(FlowConstant.SignFlowParams.HAVE_XB.getValue(), null);
                        variables.put(FlowConstant.SignFlowParams.XMFZR_SP.getValue(), false);
                        //获取第一负责人
                        assigneeValue = getMainPriUserId(signid, agentTaskList, FlowConstant.FLOW_SIGN_FW);
                        variables.put(FlowConstant.SignFlowParams.USER_FZR1.getValue(), assigneeValue);
                        flowDto.setDealOption(flowDto.getDealOption() + "【审批结果：核稿有误】");
                    }
                    break;
                }
                //协办部长审批发文
            case FlowConstant.FLOW_SIGN_BMLD_QRFW_XB:
                if (flowDto.getBusinessMap().get(SignFlowParams.AGREE.getValue()) == null || !Validate.isString(flowDto.getBusinessMap().get(SignFlowParams.AGREE.getValue()).toString())) {
                    return ResultMsg.error( "请先勾选对应的审批结果！");
                }
                String dirDealOption = flowDto.getDealOption();
                //如果同意，则到主办部长审批
                if (EnumState.YES.getValue().equals(flowDto.getBusinessMap().get("AGREE").toString())) {
                    variables.put(FlowConstant.SignFlowParams.XBBZ_SP.getValue(), true);
                    //获取主分支的部门领导
                    assigneeValue = getMainDirecotr(signid, agentTaskList, FLOW_SIGN_BMLD_QRFW);
                    variables.put(FlowConstant.SignFlowParams.USER_BZ1.getValue(), assigneeValue);
                    flowDto.setDealOption(flowDto.getDealOption() + "【审批结果：核稿无误】");
                    isCompeleteSign = ProjUtil.checkSignComplete(taskService.getVariables(task.getId()), 1);
                    //如果不同意，则回退到发文环节
                } else {
                    variables.put(FlowConstant.SignFlowParams.XBBZ_SP.getValue(), false);
                    //获取第一负责人
                    assigneeValue = getMainPriUserId(signid, agentTaskList, FlowConstant.FLOW_SIGN_FW);
                    variables.put(FlowConstant.SignFlowParams.USER_FZR1.getValue(), assigneeValue);
                    flowDto.setDealOption(flowDto.getDealOption() + "【审批结果：核稿有误】");
                }
                //协办部门的意见也要保存
                businessId = flowDto.getBusinessMap().get("DIS_ID").toString();
                dp = dispatchDocRepo.findById(DispatchDoc_.id.getName(), businessId);
                String optionString2 = Validate.isString(dp.getMinisterSuggesttion()) ? (dp.getMinisterSuggesttion() + "<br>") : "";
                dp.setMinisterSuggesttion(optionString2 + dirDealOption + " 签名：" + ActivitiUtil.getSignName(SessionUtil.getDisplayName(), isAgentTask) + " 日期：" + DateUtils.converToString(new Date(), DateUtils.DATE_PATTERN));

                dispatchDocRepo.save(dp);
                break;
            //部长审批发文
            case FLOW_SIGN_BMLD_QRFW:
                businessId = flowDto.getBusinessMap().get(SignFlowParams.DIS_ID.getValue()).toString();
                dp = dispatchDocRepo.findById(DispatchDoc_.id.getName(), businessId);
                disUtil = DisUtil.create(dp);
                //是否是合并发文项目
                if (disUtil.isMergeDis()) {
                    if (disUtil.isMainProj()) {
                        isMergeDisTask = true;
                    } else {
                        return ResultMsg.error( "合并发文的项目，只能由主项目进行操作！");
                    }
                }
                if (isMergeDisTask) {
                    //合并发文主项目，另起一个方法处理
                    returnResult = flowService.dealMerDisFlow(processInstance, task, dp, FLOW_SIGN_BMLD_QRFW, flowDto, isAgentTask);
                } else {
                    //获取主办分管领导
                    sign = signRepo.findById(Sign_.signid.getName(), signid);
                    User mainLead = userRepo.getCacheUserById(sign.getLeaderId());
                    /*不用协办分管领导审批，只要主办分管领导审批（2018-09-30）*/
                    assigneeValue = Validate.isString(mainLead.getTakeUserId()) ? mainLead.getTakeUserId() : mainLead.getId();
                    variables.put(FlowConstant.SignFlowParams.USER_FGLD1.getValue(), assigneeValue);
                    variables.put(FlowConstant.SignFlowParams.HAVE_XB.getValue(), false);
                    //下一环节还是自己处理
                    if (assigneeValue.equals(curUserId)) {
                        isNextUser = true;
                        nextNodeKey = FlowConstant.FLOW_SIGN_FGLD_QRFW;
                    }
                    //获取所有分管领导信息
                    /*不用协办分管领导审批，只要主办分管领导审批（2018-09-30）*/
                    /*userList = signBranchRepo.findAssistSLeader(signid);
                    //排除主办分支的领导
                    if (Validate.isList(userList)) {
                        for (int n = 0, l = userList.size(); n < l; n++) {
                            if ((userList.get(n)).getId().equals(mainLead.getId())) {
                                userList.remove(n);
                                break;
                            }
                        }
                    }
                    boolean isHaveTwoSLeader = Validate.isList(userList);
                    variables.put(FlowConstant.SignFlowParams.HAVE_XB.getValue(), isHaveTwoSLeader);
                    //如果有协办
                    if (isHaveTwoSLeader) {
                        //如果只有两个分管领导的情况，当另外一个是兼职主任时，自动跳过协审部长审批环节
                        if (userList.size() == 1) {
                            dealUser = userList.get(0);
                            Set<String> rolesName = userRepo.getUserRoles(dealUser.getLoginName());
                            //如果是主任角色，自动处理协办分管主任环节信息
                            boolean isDirector = false;
                            flowDto.getBusinessMap().put("isDirector", false);
                            for (Object str : rolesName) {//循环判断是否是部门负责人
                                if (str.equals(Constant.EnumFlowNodeGroupName.DIRECTOR.getValue())) {
                                    isDirector = true;
                                    break;
                                }
                            }
                            if (isDirector) {
                                isNextUser = true;
                                nextNodeKey = FlowConstant.FLOW_SIGN_FGLD_QRFW_XB;
                                flowDto.getBusinessMap().put("AGREE", EMPTY_STRING); //暂留空
                                flowDto.getBusinessMap().put("isDirector", true);
                            }
                        }
                        for (int i = 0, l = userList.size(); i < l; i++) {
                            String userId = userService.getTaskDealId(userList.get(i).getId(), agentTaskList,FlowConstant.FLOW_SIGN_FGLD_QRFW_XB);
                            assigneeValue = StringUtil.joinString(assigneeValue, SEPARATE_COMMA, userId);
                        }
                        variables.put(FlowConstant.SignFlowParams.USER_XBFGLD_LIST.getValue(), StringUtil.getSplit(assigneeValue, SEPARATE_COMMA));

                        //没有协办，则流转给主办分管领导审批
                    } else {
                        assigneeValue = Validate.isString(mainLead.getTakeUserId()) ? mainLead.getTakeUserId() : mainLead.getId();
                        variables.put(FlowConstant.SignFlowParams.USER_FGLD1.getValue(), assigneeValue);
                        //下一环节还是自己处理
                        if (assigneeValue.equals(curUserId)) {
                            isNextUser = true;
                            nextNodeKey = FlowConstant.FLOW_SIGN_FGLD_QRFW;
                        }
                    }*/

                    //修改发文信息
                    if (dp.getBranchCount() == 1) {
                        dp.setMinisterSuggesttion(flowDto.getDealOption());
                    } else {
                        String optionString3 = Validate.isString(dp.getMinisterSuggesttion()) ? (dp.getMinisterSuggesttion() + "<br>") : "";
                        dp.setMinisterSuggesttion(optionString3 + flowDto.getDealOption() + " 签名：" + ActivitiUtil.getSignName(SessionUtil.getDisplayName(), isAgentTask) + "   日期：" + DateUtils.converToString(new Date(), DateUtils.DATE_PATTERN));
                    }
                    //发文日期也要保存下
                    dp.setMinisterDate(new Date());
                    dp.setMinisterName(ActivitiUtil.getSignName(SessionUtil.getDisplayName(), isAgentTask));
                    dispatchDocRepo.save(dp);
                }
                break;
            //协办分管领导审批发文（去掉该环节 2018-09-30）
            case FlowConstant.FLOW_SIGN_FGLD_QRFW_XB:
                Boolean isDirector = (Boolean) flowDto.getBusinessMap().get("isDirector");
                if (null != isDirector && isDirector) {
                } else {
                    if (flowDto.getBusinessMap().get(SignFlowParams.AGREE.getValue()) == null || !Validate.isString(flowDto.getBusinessMap().get(SignFlowParams.AGREE.getValue()).toString())) {
                        return ResultMsg.error( "请先勾选对应的审批结果！");
                    }
                }
                //如果同意
                String agreeString = flowDto.getBusinessMap().get(SignFlowParams.AGREE.getValue()).toString();
                if (EnumState.YES.getValue().equals(agreeString) || EMPTY_STRING.equals(agreeString)) {
                    variables.put(FlowConstant.SignFlowParams.XBFZR_SP.getValue(), true);
                    //获取主办分管领导
                    sign = signRepo.findById(Sign_.signid.getName(), signid);
                    assigneeValue = userService.getTaskDealId(sign.getLeaderId(), agentTaskList, FlowConstant.FLOW_SIGN_FGLD_QRFW);
                    variables.put(FlowConstant.SignFlowParams.USER_FGLD1.getValue(), assigneeValue);
                    if (EMPTY_STRING.equals(agreeString)) {
                        flowDto.setDealOption(EMPTY_STRING);
                    }
                    //不同意则回退到发文申请环节
                } else {
                    variables.put(FlowConstant.SignFlowParams.XBFZR_SP.getValue(), false);
                    //获取第一负责人
                    assigneeValue = getMainPriUserId(signid, agentTaskList, FlowConstant.FLOW_SIGN_FW);
                    variables.put(FlowConstant.SignFlowParams.USER_FZR1.getValue(), assigneeValue);
                    flowDto.setDealOption(flowDto.getDealOption() + "【审批结果：核稿有误】");
                }
                //修改发文信息
                businessId = flowDto.getBusinessMap().get("DIS_ID").toString();
                dp = dispatchDocRepo.findById(DispatchDoc_.id.getName(), businessId);
                String vdSugMin = Validate.isString(dp.getViceDirectorSuggesttion()) ? (dp.getViceDirectorSuggesttion() + "<br>") : "";
                dp.setViceDirectorSuggesttion(vdSugMin + flowDto.getDealOption() + " 签名：" + ActivitiUtil.getSignName(SessionUtil.getDisplayName(), isAgentTask) + "   日期：" + DateUtils.converToString(new Date(), DateUtils.DATE_PATTERN));
                dp.setMoreLeader(1);
                dispatchDocRepo.save(dp);
                break;
            //主办分管主任审批发文
            case FlowConstant.FLOW_SIGN_FGLD_QRFW:
                userList = userRepo.findUserByRoleName(EnumFlowNodeGroupName.DIRECTOR.getValue());
                if (!Validate.isList(userList)) {
                    return ResultMsg.error("请先设置【" + EnumFlowNodeGroupName.DIRECTOR.getValue() + "】角色用户！");
                }
                businessId = flowDto.getBusinessMap().get(SignFlowParams.DIS_ID.getValue()).toString();
                dp = dispatchDocRepo.findById(DispatchDoc_.id.getName(), businessId);
                disUtil = DisUtil.create(dp);
                //是否是合并发文项目
                if (disUtil.isMergeDis()) {
                    if (disUtil.isMainProj()) {
                        isMergeDisTask = true;
                    } else {
                        return ResultMsg.error("合并发文的项目，只能由主项目进行操作！");
                    }
                }
                if (isMergeDisTask) {
                    //合并发文主项目，另起一个方法处理
                    returnResult = flowService.dealMerDisFlow(processInstance, task, dp, FLOW_SIGN_FGLD_QRFW, flowDto, isAgentTask);
                } else {
                    //获取主任角色用户
                    dealUser = userList.get(0);
                    assigneeValue = userService.getTaskDealId(dealUser, agentTaskList, FLOW_SIGN_ZR_QRFW);
                    variables.put(FlowConstant.SignFlowParams.USER_ZR.getValue(), assigneeValue);

                    //修改发文信息
                    if (dp.getMoreLeader() == 1) {
                        String vdSug = Validate.isString(dp.getViceDirectorSuggesttion()) ? (dp.getViceDirectorSuggesttion() + "<br>") : "";
                        dp.setViceDirectorSuggesttion(vdSug + flowDto.getDealOption() + "  签名：" + ActivitiUtil.getSignName(SessionUtil.getDisplayName(), isAgentTask) + "   日期：" + DateUtils.converToString(new Date(), "yyyy年MM月dd日"));
                    } else {
                        dp.setViceDirectorSuggesttion(flowDto.getDealOption());
                    }
                    dp.setViceDirectorDate(new Date());
                    dp.setViceDirectorName(ActivitiUtil.getSignName(SessionUtil.getDisplayName(), isAgentTask));
                    dispatchDocRepo.save(dp);
                    //下一环节还是自己处理
                    if (assigneeValue.equals(SessionUtil.getUserId())) {
                        isNextUser = true;
                        nextNodeKey = FLOW_SIGN_ZR_QRFW;
                    }
                }
                break;
            //主任审批发文
            case FLOW_SIGN_ZR_QRFW:
                businessId = flowDto.getBusinessMap().get("DIS_ID").toString();
                dp = dispatchDocRepo.findById(DispatchDoc_.id.getName(), businessId);
                disUtil = DisUtil.create(dp);
                //是否是合并发文项目
                if (disUtil.isMergeDis()) {
                    if (disUtil.isMainProj()) {
                        isMergeDisTask = true;
                    } else {
                        return ResultMsg.error( "合并发文的项目，只能由主项目进行操作！");
                    }
                }
                if (isMergeDisTask) {
                    //合并发文主项目，另起一个方法处理
                    returnResult = flowService.dealMerDisFlow(processInstance, task, dp, FLOW_SIGN_ZR_QRFW, flowDto, isAgentTask);
                } else {
                    dp.setDirectorSuggesttion(flowDto.getDealOption());
                    dp.setDirectorDate(new Date());
                    dp.setDirectorName(ActivitiUtil.getSignName(SessionUtil.getDisplayName(), isAgentTask));
                    dispatchDocRepo.save(dp);

                    //获取第一负责人
                    assigneeValue = getMainPriUserId(signid, agentTaskList, FlowConstant.FLOW_SIGN_FWBH);
                    variables.put(FlowConstant.SignFlowParams.USER_FZR1.getValue(), assigneeValue);
                }

                break;
            //生成发文编号
            case FlowConstant.FLOW_SIGN_FWBH:
                businessId = flowDto.getBusinessMap().get("DIS_ID").toString();
                dp = dispatchDocRepo.findById(DispatchDoc_.id.getName(), businessId);
                if (!Validate.isString(dp.getFileNum())) {
                    return ResultMsg.error( "操作失败，该项目还没有发文编号，不能进行下一步操作！");
                }
                //专家评审方案,判断专家的一些信息是否完整
                expertReview = expertReviewRepo.findByBusinessId(signid);
                if (expertReview != null && Validate.isList(expertReview.getExpertSelectedList())) {
                    Boolean isDisplay = false;
                    String prompt = "";
                    List<ExpertSelected> expertSelecteds = expertReview.getExpertSelectedList();
                    for (int i = 0; i < expertSelecteds.size(); i++) {
                        if (Constant.EnumState.YES.getValue().equals(expertSelecteds.get(i).getIsConfrim()) &&
                                Constant.EnumState.YES.getValue().equals(expertSelecteds.get(i).getIsJoin())) {
                            //银行账户和身份证号不能为空
                            if (!Validate.isString(expertSelecteds.get(i).getExpert().getBankAccount()) ||
                                    !Validate.isString(expertSelecteds.get(i).getExpert().getIdCard())) {
                                if (prompt.length() > 0 && i + 1 <= expertSelecteds.size()) {//第一位和最后一位不用加
                                    prompt += "、";
                                }
                                //对提示信息的拼接
                                prompt += expertSelecteds.get(i).getExpert().getName();
                                isDisplay = true;
                            }
                        }
                    }
                    if (isDisplay) {
                        prompt += "专家的身份证号和银行卡号不完整,不能进行下一步操作,请去完善专家信息！";
                        return ResultMsg.error( prompt);
                    }
                }
                //如果有评审费或者是协审流程(有协审单位才算)，则给财务部办理，没有，则直接到归档环节
                boolean isGotoCW = expertReviewRepo.isHaveEPReviewCost(signid) ||
                        (signRepo.checkAssistSign(signid) && assistUnitRepo.checkAssistUnitBySignId(signid));
                if (isGotoCW) {
                    userList = userRepo.findUserByRoleName(EnumFlowNodeGroupName.FINANCIAL.getValue());
                    if (!Validate.isList(userList)) {
                        return ResultMsg.error("请先设置【" + EnumFlowNodeGroupName.FINANCIAL.getValue() + "】角色用户！");
                    }
                    for (int i = 0, l = userList.size(); i < l; i++) {
                        String userId = userService.getTaskDealId(userList.get(i).getId(), agentTaskList, FlowConstant.FLOW_SIGN_CWBL);
                        assigneeValue = StringUtil.joinString(assigneeValue, SEPARATE_COMMA, userId);
                    }
                    variables.put(FlowConstant.SignFlowParams.HAVE_ZJPSF.getValue(), true);
                    variables.put(FlowConstant.SignFlowParams.USER_CW.getValue(), assigneeValue);

                    signRepo.updateSignProcessState(signid, Constant.SignProcessState.SEND_CW.getValue());
                    //没有评审费，则直接到归档环节(还是当前人处理)
                } else {
                    variables.put(FlowConstant.SignFlowParams.HAVE_ZJPSF.getValue(), false);
                    //获取第一负责人
                    assigneeValue = getMainPriUserId(signid, agentTaskList, FlowConstant.FLOW_SIGN_GD);
                    variables.put(FlowConstant.SignFlowParams.USER_FZR1.getValue(), assigneeValue);

                    signRepo.updateSignProcessState(signid, Constant.SignProcessState.SEND_FILE.getValue());
                }
                break;

            //财务办理
            case FlowConstant.FLOW_SIGN_CWBL:
                sign = signRepo.findById(Sign_.signid.getName(), signid);
                sign.setIsSendFileRecord(EnumState.YES.getValue());
                //确认归档环节，才是已发送存档
                //sign.setProcessState(Constant.SignProcessState.SEND_FILE.getValue());
                signRepo.save(sign);
                //获取第一负责人
                assigneeValue = getMainPriUserId(signid, agentTaskList, FlowConstant.FLOW_SIGN_GD);
                variables.put(FlowConstant.SignFlowParams.USER_FZR1.getValue(), assigneeValue);
                break;

            //第一负责人归档
            case FlowConstant.FLOW_SIGN_GD:
                //如果没有完成专家评分，则不可以提交到下一步（移到发文申请环节，没有评分不能进行下一步操作2018-12-28）
                if (!expertReviewRepo.isFinishEPGrade(signid)) {
                    return ResultMsg.error( "您还未对专家进行评分,不能提交到下一步操作！");
                }
                //如果没有完成单位评分，则不可以提交下一步
                UnitScore unitScore = unitScoreRepo.findUnitScore(signid);
                if (unitScore == null || unitScore.getScore() == null) {
                    return ResultMsg.error( "您还未对单位进行评分,不能提交到下一步操作！");
                }

                //如果没有完成归档信息，则不可以提交下一步
                if (!fileRecordRepo.isFileRecord(signid)) {
                    return ResultMsg.error("您还没完成归档操作，不能进行下一步操作！");
                }

                //如果有第二负责人审核
                if (flowDto.getBusinessMap().get("checkFileUser") != null) {
                    dealUser = JSON.parseObject(flowDto.getBusinessMap().get("checkFileUser").toString(), User.class);
                    variables.put(FlowConstant.SignFlowParams.HAVE_XMFZR.getValue(), true);
                    assigneeValue = userService.getTaskDealId(dealUser, agentTaskList, FlowConstant.FLOW_SIGN_DSFZR_QRGD);
                    variables.put(FlowConstant.SignFlowParams.USER_A.getValue(), assigneeValue);
                    //没有第二负责人审核
                } else {
                    variables.put(FlowConstant.SignFlowParams.HAVE_XMFZR.getValue(), false);
                    //如果是回退，则保留之前的审核人
                    sign = signRepo.findById(Sign_.signid.getName(), signid);
                    sign.setIsSendFileRecord(EnumState.YES.getValue());
                    if (Validate.isString(sign.getSecondPriUser())) {
                        dealUser = userRepo.getCacheUserById(sign.getSecondPriUser());
                    } else {
                        userList = userRepo.findUserByRoleName(EnumFlowNodeGroupName.FILER.getValue());
                        if (!Validate.isList(userList)) {
                            return ResultMsg.error( "请先设置【" + EnumFlowNodeGroupName.FILER.getValue() + "】角色用户！");
                        }
                        dealUser = userList.get(0);
                    }
                    assigneeValue = userService.getTaskDealId(dealUser, agentTaskList, FlowConstant.FLOW_SIGN_QRGD);
                    variables.put(FlowConstant.SignFlowParams.USER_QRGD.getValue(), assigneeValue);

                    //更新项目状态（已发送归档）
                    signRepo.updateSignProcessState(signid, Constant.SignProcessState.SEND_FILE.getValue());
                }
                break;

            //第二负责人审批归档
            case FlowConstant.FLOW_SIGN_DSFZR_QRGD:
                userList = userRepo.findUserByRoleName(EnumFlowNodeGroupName.FILER.getValue());
                if (!Validate.isList(userList)) {
                    return ResultMsg.error("请先设置【" + EnumFlowNodeGroupName.FILER.getValue() + "】角色用户！");
                }
                dealUser = userList.get(0);
                assigneeValue = userService.getTaskDealId(dealUser, agentTaskList, FlowConstant.FLOW_SIGN_QRGD);
                variables.put(FlowConstant.SignFlowParams.USER_QRGD.getValue(), assigneeValue);
                //更新项目第二负责人
                sign = signRepo.findById(Sign_.signid.getName(), signid);
                sign.setIsSendFileRecord(EnumState.YES.getValue());
                if (isAgentTask) {
                    sign.setSecondPriUser(agentTaskService.getUserId(task.getId(), curUserId));
                } else {
                    sign.setSecondPriUser(curUserId);
                }
                signRepo.save(sign);

                //第二负责人确认
                businessId = flowDto.getBusinessMap().get("GD_ID").toString();
                fileRecord = fileRecordRepo.findById(FileRecord_.fileRecordId.getName(), businessId);
                fileRecord.setProjectTwoUser(ActivitiUtil.getSignName(SessionUtil.getDisplayName(), isAgentTask));
                //发送存档日期为第二负责人审批意见后的日期，
                fileRecord.setSendStoreDate(new Date());
                fileRecordRepo.save(fileRecord);
                //改项目状态（已发送归档）
                signRepo.updateSignProcessState(signid, Constant.SignProcessState.SEND_FILE.getValue());
                break;
            //确认归档
            case FlowConstant.FLOW_SIGN_QRGD:
                businessId = flowDto.getBusinessMap().get("GD_ID").toString();
                fileRecord = fileRecordRepo.findById(FileRecord_.fileRecordId.getName(), businessId);
                sign = signRepo.findById(Sign_.signid.getName(), signid);
                fileRecord.setFileDate(new Date());
                if (isAgentTask) {
                    fileRecord.setSignUserid(agentTaskService.getUserId(task.getId(), curUserId));
                } else {
                    fileRecord.setSignUserid(curUserId);
                }
                fileRecord.setSignUserName(ActivitiUtil.getSignName(SessionUtil.getDisplayName(), isAgentTask));
                //纸质文件接受日期 ：为归档员陈春燕确认的归档日期
                //设置归档编号
                if (!Validate.isString(fileRecord.getFileNo())) {
                    String seqType = ProjUtil.getFileRecordTypeByStage(sign.getReviewstage());
                    String yearName = DateUtils.converToString(fileRecord.getFileDate(), DateUtils.DATE_YEAR);
                    int maxSeq = fileRecordRepo.getMaxSeq(yearName, seqType) + 1;
                    //如果是0，则改为1
                    if (maxSeq < 1) {
                        maxSeq = 1;
                    }
                    String fileNum = maxSeq > 999 ? maxSeq + "" : String.format("%03d", maxSeq);
                    //归档编号=发文年份+档案类型+存档年份+存档顺序号
                    fileNum = DateUtils.converToString(sign.getDispatchdate(), DateUtils.DATE_YEAR) + seqType
                            + DateUtils.converToString(fileRecord.getFileDate(), "yy") + fileNum;
                    //设置本次的发文序号
                    fileRecord.setFileSeq(maxSeq);
                    fileRecord.setFileNo(fileNum);
                }
                fileRecord.setPageDate(new Date());
                fileRecordRepo.save(fileRecord);

                //收文这边也要更新归档编号
                sign.setFilenum(fileRecord.getFileNo());
                //更改项目状态
                sign.setSignState(EnumState.YES.getValue());
                sign.setProcessState(Constant.SignProcessState.FINISH.getValue());
                signRepo.save(sign);
                break;
            default:
                ;
        }

        if (isMergeDisTask) {
            //如果是合并发文项目，直接返回处理结果
            return returnResult;
        } else {
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
                            return dealFlow(processInstance, t, flowDto);
                        }
                    }
                }
            }

            if (isNextUser == false && isCompeleteSign) {
                //放入腾讯通消息缓冲池
                RTXSendMsgPool.getInstance().sendReceiverIdPool(task.getId(), assigneeValue);
            }
            //如果是代办，还要更新环节名称和任务ID
            if (Validate.isList(agentTaskList)) {
                agentTaskService.updateAgentInfo(agentTaskList, processInstance.getId(), processInstance.getName());
            }
            //当下一个处理人还是自己的时候，任务ID是已经改变了的，所以这里要返回任务ID
            return new ResultMsg(true, MsgCode.OK.getValue(), task.getId(), "操作成功！", null);
        }
    }

    /**
     * 验证是否已经上传附件
     *
     * @param signid
     * @return
     */
    @Override
    public boolean checkFileUpload(String signid) {
        boolean isUploadMainFile = false;
        List<SysFile> fileList = sysFileRepo.findByMainId(signid);
        for (SysFile sysFile : fileList) {
            String fileShowName = sysFile.getShowName();
            if (fileShowName.contains("评审意见") || fileShowName.contains("审核意见")) {
                isUploadMainFile = true;
            }
        }
        return isUploadMainFile;
    }


    /**
     * 获取主分支的部门领导
     *
     * @param signid
     */
    @Override
    public String getMainDirecotr(String signid, List<AgentTask> agentTaskList, String nodeKey) {
        OrgDept orgDept = orgDeptRepo.queryBySignBranchId(signid, FlowConstant.SignFlowParams.BRANCH_INDEX1.getValue());
        return userService.getTaskDealId(orgDept.getDirectorID(), agentTaskList, nodeKey);
    }

    @Override
    public String getMainPriUserId(String signid, List<AgentTask> agentTaskList, String nodeKey) {
        User dealUser = signPrincipalService.getMainPriUser(signid);
        return userService.getTaskDealId(dealUser, agentTaskList, nodeKey);
    }

    @Override
    public void updateProjectNameCascade(Sign sign, String newProjectName) {
        //如果还未完成流程，则要修改流程名称
        if (Validate.isString(sign.getProcessInstanceId())) {
            signRepo.executeSql(ProjSql.updateRunFlowName(sign.getProcessInstanceId(), newProjectName));
            signRepo.executeSql(ProjSql.updateHisFlowName(sign.getProcessInstanceId(), newProjectName));
        }
        //评审费发送表标题
        signRepo.executeSql(ReviewSql.updateReviewTitleName(sign.getSignid(), newProjectName));
        //工作方案标题
        signRepo.executeSql(WorkSql.updateWPProjName(sign.getSignid(), newProjectName));
        //工作方案留痕标题
        signRepo.executeSql(WorkSql.updateWPHisProjName(sign.getSignid(), newProjectName));
        //更新重做工作方案流程标题
        signRepo.executeSql(WorkSql.updateRunFlowName(sign.getSignid(), newProjectName));
        signRepo.executeSql(WorkSql.updateHisFlowName(sign.getSignid(), newProjectName));
        //存档项目名称
        signRepo.executeSql(FileRecordSql.updateProjNameSql(sign.getSignid(), newProjectName));
        //会议名称修改
        signRepo.executeSql(MeettingSql.updateProjNameSql(sign.getSignid(), newProjectName));
    }

    /**
     * 查询项目信息，调概项目
     *
     * @return
     */
    @Override
    public List<SignDto> findAssistSign() {
        HqlBuilder sqlBuilder = HqlBuilder.create();
        sqlBuilder.append(" select sign from " + Sign.class.getSimpleName() + " sign ");
        sqlBuilder.append(" left join " + AssistPlanSign.class.getSimpleName() + " plang on sign." + Sign_.signid.getName() + " = plang." + AssistPlanSign_.signId.getName());
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
    public Map<String, Object> findByPlanId(String planId, String isOnlySign) {
        Map<String, Object> resultMap = new HashMap<>();
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
        resultMap.put("signList", resultList);

        if (EnumState.NO.getValue().equals(isOnlySign)) {
            //协审计划信息
            AssistPlan assistPlan = assistPlanRepo.findById(planId);
            AssistPlanDto planDto = new AssistPlanDto();
            BeanCopierUtils.copyProperties(assistPlan, planDto);

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
            resultMap.put("planDto", planDto);
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
        ResultMsg result = new ResultMsg(false, MsgCode.ERROR.getValue(), "");
        if (signId.equals(associateId)) {
            result.setReMsg("不能关联自身项目");
            return result;
        }
        Sign sign = signRepo.getById(signId);
        if (sign == null) {
            result.setReMsg("项目不存在");
            return result;
        }
        boolean isLink = Validate.isString(associateId) ? true : false;     //
        //如果associateId为空，解除关联
        if (isLink) {
            Sign associateSign = signRepo.findById(Sign_.signid.getName(), associateId);
            if (associateSign == null) {
                result.setReMsg("关联项目不存在");
                return result;
            }
            sign.setAssociateSign(associateSign);
            sign.setIsAssociate(1);
            //找出关联的项目
            List<Sign> associateSigns = getAssociates(sign.getAssociateSign().getSignid());
            if (associateSigns != null && associateSigns.size() > 0) {
                List<DispatchDocDto> associateDispatchDtos = new ArrayList<DispatchDocDto>(associateSigns.size());
                associateSigns.forEach(associateSign2 -> {
                    Sign asSign = signRepo.getById(associateSign2.getSignid());
                    DispatchDoc associateDispatch2 = asSign.getDispatchDoc();
                    if (associateDispatch2 != null && associateDispatch2.getId() != null) {
                        //关联发文
                        DispatchDocDto associateDis = new DispatchDocDto();
                        BeanCopierUtils.copyProperties(associateDispatch2, associateDis);
                        SignDto signDto = new SignDto();
                        signDto.setReviewstage(asSign.getReviewstage());
                        associateDis.setSignDto(signDto);
                        associateDispatchDtos.add(associateDis);
                    }
                });
                result.setReObj(associateDispatchDtos);
            }
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

        result.setFlag(true);
        result.setReCode(MsgCode.OK.getValue());
        result.setReMsg("操作成功");
        return result;
    }

    /**
     * 查询正式签收的项目，并未归档的项目
     *
     * @return
     */
    @Override
    public List<Sign> selectSignNotFinish() {
        Criteria criteria = signRepo.getExecutableCriteria();
        criteria.add(Restrictions.eq(Sign_.issign.getName(), EnumState.YES.getValue()));
        criteria.add(Restrictions.isNotNull(Sign_.signdate.getName()));
        criteria.add(Restrictions.ne(Sign_.signState.getName(), EnumState.DELETE.getValue()));
        criteria.add(Restrictions.ne(Sign_.signState.getName(), EnumState.STOP.getValue()));
        criteria.add(Restrictions.ne(Sign_.signState.getName(), EnumState.YES.getValue()));
        Disjunction dis = Restrictions.disjunction();
        dis.add(Restrictions.isNull(Sign_.processState.getName()));
        dis.add(Restrictions.ne(Sign_.processState.getName(), Constant.SignProcessState.FINISH.getValue()));
        criteria.add(dis);
        List<Sign> signList = criteria.list();
        //项目暂停信息
        for (Sign sign : signList) {
            if (Validate.isList(sign.getProjectStopList())) {
                List<ProjectStop> newList = new ArrayList();
                for (ProjectStop projectStop : sign.getProjectStopList()) {
                    ProjectStop newStop = new ProjectStop();
                    BeanCopierUtils.copyProperties(projectStop, newStop);
                    newList.add(newStop);
                }
                sign.setProjectStopList(newList);
            }
        }

        return signList;
    }

    @Override
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
        List<OrgDept> orgDeptList = orgDeptRepo.findAllByCache();
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
        formalProject(sign);

        return new ResultMsg(true, MsgCode.OK.getValue(), "操作成功！");
    }

    @Override
    public Float getReviewDays(String reviewstage) {
        Float resultFloat = 0f;
        if (Validate.isString(reviewstage)) {
            ProjectConstant.REVIEW_STATE_ENUM reviewStateEnum = ProjectConstant.REVIEW_STATE_ENUM.getByZhCode(reviewstage);
            if (Validate.isObject(reviewStateEnum)) {
                SysConfigDto sysConfigDto = sysConfigService.findByKey(reviewStateEnum.getEnCode());
                if (null != sysConfigDto && null != sysConfigDto.getConfigValue()) {
                    return Float.parseFloat(sysConfigDto.getConfigValue());
                } else {
                    //设定默认值，项目建议书和资金申请报告是12天，其他是15天
                    if ((ProjectConstant.REVIEW_STATE_ENUM.STAGESUG.getZhCode()).equals(reviewstage)
                            || (ProjectConstant.REVIEW_STATE_ENUM.STAGEREPORT.getZhCode()).equals(reviewstage)) {
                        return ProjectConstant.WORK_DAY_12;
                    } else {
                        return ProjectConstant.WORK_DAY_15;
                    }
                }
            }
        }
        return resultFloat;
    }

    /**
     * 修改项目状态
     *
     * @param signId
     * @param stateProperty 状态属性
     * @param stateValue    值
     * @return
     */
    @Override
    public boolean updateSignState(String signId, String stateProperty, String stateValue) {
        return signRepo.updateSignState(signId, stateProperty, stateValue);
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
     * 获取签收项目（正式签收未发起流程或者已经发起流程正式签收的项目,排除预签收）
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
        Disjunction dis = Restrictions.disjunction();
        dis.add(Restrictions.isNull(Sign_.signState.getName()));
        dis.add(Restrictions.sqlRestriction(" " + Sign_.signState.getName() + " != '" + EnumState.YES.getValue() + "' and " + Sign_.signState.getName() + " != '" + EnumState.FORCE.getValue() + "' and " + Sign_.signState.getName() + " != '" + EnumState.DELETE.getValue() + "' "));
        criteria.add(dis);

        //3、已签收，但是未发起流程的项目
        criteria.add(Restrictions.eq(Sign_.issign.getName(), EnumState.YES.getValue()));
        criteria.add(Restrictions.isNull(Sign_.processInstanceId.getName()));
        int totalResult = Integer.parseInt(criteria.setProjection(Projections.rowCount()).uniqueResult().toString());
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
     * 不用设置阶段关联限制，
     * 查询已经生成发文编号的项目，如果已经被关联，则排除
     * * @param projectName
     *
     * @return
     */
    @Override
    public List<SignDispaWork> findAssociateSign(SignDispaWork signDispaWork) {
        HqlBuilder sqlBuilder = HqlBuilder.create();
        sqlBuilder.append("select s.* from SIGN_DISP_WORK s where s." + SignDispaWork_.signid.getName() + " != :signid ");
        sqlBuilder.setParam("signid", signDispaWork.getSignid());
        //只能是生成发文编号后的项目
        sqlBuilder.append(" and s." + SignDispaWork_.processState.getName() + " >= :processState ");
        sqlBuilder.setParam("processState", Constant.SignProcessState.END_DIS_NUM.getValue());
        //排除已经进行了关联的项目
        sqlBuilder.append(" and s.signid not in( select ASSOCIATE_SIGNID from CS_ASSOCIATE_SIGN) ");
        //根据阶段查询对应关联阶段项目
        ProjectConstant.REVIEW_STATE_ENUM reviewStateEnum = ProjectConstant.REVIEW_STATE_ENUM.getByZhCode(signDispaWork.getReviewstage());
        if(Validate.isObject(reviewStateEnum)){
            switch (reviewStateEnum){
                /**
                 * 项目建议书 或资金申请
                 */
                case STAGESUG:
                case STAGEREPORT:
                    sqlBuilder.append(" and s." + SignDispaWork_.reviewstage.getName() + "=:reviewStage ");
                    sqlBuilder.setParam("reviewStage", signDispaWork.getReviewstage());
                    break;
                /**
                 * 可研
                 */
                case STAGESTUDY:
                    sqlBuilder.append(" and s." + SignDispaWork_.reviewstage.getName() + " in('" + ProjectConstant.REVIEW_STATE_ENUM.STAGESTUDY.getZhCode() + "','" + ProjectConstant.REVIEW_STATE_ENUM.STAGESUG.getZhCode() + "') ");
                    break;
                /**
                 * 概算
                 */
                case STAGEBUDGET:
                    sqlBuilder.append(" and s." + SignDispaWork_.reviewstage.getName() + " in('" + ProjectConstant.REVIEW_STATE_ENUM.STAGESTUDY.getZhCode() + "','" + ProjectConstant.REVIEW_STATE_ENUM.STAGESUG.getZhCode() + "','" + ProjectConstant.REVIEW_STATE_ENUM.STAGEBUDGET.getZhCode() + "') ");
                    break;
                default:
                    ;
            }
        }

        if (Validate.isString(signDispaWork.getProjectname())) {
            sqlBuilder.append(" and s." + SignDispaWork_.projectname.getName() + " like :projectName");
            sqlBuilder.setParam("projectName", "%" + signDispaWork.getProjectname() + "%");
        }
        return  signDispaWorkRepo.findBySql(sqlBuilder);
    }

    /**
     * 项目发文的关联项目，返回gird列表
     *
     * @param signid      项目id
     * @param reviewstage 项目阶段
     * @param projectname 项目名称
     * @param skip        页码
     * @param size        页数
     * @return
     */

    @Override
    @Transactional
    public PageModelDto<SignDispaWork> findAssociateSignList(String signid, String reviewstage, String projectname, String mUserName, String skip, String size) {
        //解决乱码问题
        try {
            if (Validate.isString(reviewstage) && reviewstage.equals(new String(reviewstage.getBytes("iso8859-1"), "iso8859-1"))) {
                reviewstage = new String(reviewstage.getBytes("iso8859-1"), "utf-8");
            }
            if (Validate.isString(projectname) && projectname.equals(new String(projectname.getBytes("iso8859-1"), "iso8859-1"))) {
                projectname = new String(projectname.getBytes("iso8859-1"), "utf-8");
            }
            if (Validate.isString(mUserName) && mUserName.equals(new String(mUserName.getBytes("iso8859-1"), "iso8859-1"))) {
                mUserName = new String(mUserName.getBytes("iso8859-1"), "utf-8");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        PageModelDto<SignDispaWork> pageModelDto = new PageModelDto<SignDispaWork>();
        HqlBuilder sqlBuilder = HqlBuilder.create();   //基础语句
        HqlBuilder sqlBuilders = HqlBuilder.create(); //返回list的语句
        HqlBuilder sqlBuilder1 = HqlBuilder.create();//返回总页数的语句

        sqlBuilder.append("  from SIGN_DISP_WORK s where s." + SignDispaWork_.signid.getName() + " != :signid ");
        sqlBuilder1.setParam("signid", signid);
        sqlBuilders.setParam("signid", signid);
        //只能是生成发文编号后的项目
        sqlBuilder.append(" and s." + SignDispaWork_.processState.getName() + " >= " + Constant.SignProcessState.END_DIS_NUM.getValue() + " ");
        //排除已经进行了关联的项目
        /*sqlBuilder.append(" and s.signid not in( select ASSOCIATE_SIGNID from CS_ASSOCIATE_SIGN) ");*/
        ProjectConstant.REVIEW_STATE_ENUM reviewStateEnum = ProjectConstant.REVIEW_STATE_ENUM.getByZhCode(reviewstage);
        if(Validate.isObject(reviewStateEnum)){
            switch (reviewStateEnum) {
                /**
                 * 项目建议书 或 资金申请 或 登记赋码
                 */
                case STAGESUG:
                case REGISTERCODE:
                case STAGEREPORT:
                    sqlBuilder.append(" and s." + SignDispaWork_.reviewstage.getName() + "=:reviewStage ");
                    sqlBuilders.setParam("reviewStage", reviewstage);
                    sqlBuilder1.setParam("reviewStage", reviewstage);
                    break;
                /**
                 * 可研
                 */
                case STAGESTUDY:
                    sqlBuilder.append(" and s." + SignDispaWork_.reviewstage.getName() + " in('" + ProjectConstant.REVIEW_STATE_ENUM.STAGESTUDY.getZhCode() + "','" + ProjectConstant.REVIEW_STATE_ENUM.STAGESUG.getZhCode() + "') ");
                    break;
                /**
                 * 概算
                 */
                case STAGEBUDGET:
                    sqlBuilder.append(" and s." + SignDispaWork_.reviewstage.getName() + " in('" + ProjectConstant.REVIEW_STATE_ENUM.STAGESTUDY.getZhCode() + "','" + ProjectConstant.REVIEW_STATE_ENUM.STAGESUG.getZhCode() + "','" + ProjectConstant.REVIEW_STATE_ENUM.STAGEBUDGET.getZhCode() +"') ");
                    break;
                default:
                    ;
            }
        }

        if (Validate.isString(projectname)) {
            sqlBuilder.append(" and s." + SignDispaWork_.projectname.getName() + " like :projectname ");
            sqlBuilder1.setParam("projectname", "%" + projectname + "%");
            sqlBuilders.setParam("projectname", "%" + projectname + "%");
        }
        if (Validate.isString(mUserName)) {
            sqlBuilder.append(" and s." + SignDispaWork_.mUserName.getName() + " like :mUserName ");
            sqlBuilder1.setParam("mUserName", "%" + mUserName + "%");
            sqlBuilders.setParam("mUserName", "%" + mUserName + "%");
        }
        sqlBuilder.append(" order by s." + SignDispaWork_.signdate.getName() + " desc ");
        //返回总页数
        sqlBuilder1.append(" select count(*) ");
        sqlBuilder1.append(sqlBuilder.getHqlString());
        int total = signDispaWorkRepo.returnIntBySql(sqlBuilder1);
        //返回list
        sqlBuilders.append(" select * from ( select s.*,rownum as num ");
        sqlBuilders.append(sqlBuilder.getHqlString());
        sqlBuilders.append(" ) where num between :skip and :size ");
        sqlBuilders.setParam("skip", skip).setParam("size", size);
        List<SignDispaWork> signList = signDispaWorkRepo.findBySql(sqlBuilders);
        if (!Validate.isList(signList)) {
            signList = new ArrayList<>();
        }
        pageModelDto.setCount(total);
        pageModelDto.setValue(signList);
        return pageModelDto;
    }

    @Override
    public UnitScoreDto findSignUnitScore(String signId) {
        Sign sign = signRepo.findById(Sign_.signid.getName(), signId);
        UnitScoreDto unitScoreDto = new UnitScoreDto();
        //查找单位评分列表
        UnitScore unitScore = unitScoreRepo.findUnitScore(signId);
        if (Validate.isObject(unitScore) && Validate.isString(unitScore.getId())) {
            unitScoreService.decide(sign.getDesigncompanyName(), signId, Validate.isString(SessionUtil.getUserId()));
            unitScore = unitScoreRepo.findUnitScore(signId);
            BeanCopierUtils.copyProperties(unitScore, unitScoreDto);
            return unitScoreDto;
        } else {
            return null;
        }

    }

    @Override
    public void initSignDeptInfo(Sign sign) {
        //4、默认办理部门（项目建议书、可研为PX，概算为GX，其他为评估）
        if (!Validate.isString(sign.getDealOrgType())) {
            Map<String,String> orgNameGroupMap = orgDeptService.groubOrgNameByType(" ");
            String orgType = ProjectConstant.BUSINESS_TYPE.PX.toString();
            if (ProjectConstant.REVIEW_STATE_ENUM.STAGEBUDGET.getZhCode().equals(sign.getReviewstage())) {
                orgType = ProjectConstant.BUSINESS_TYPE.GX.toString();
            }
            sign.setDealOrgType(orgType);
            sign.setLeaderhandlesug("请（"+ orgNameGroupMap.get(orgType)+"）组织评审。");
        }

        if (!Validate.isString(sign.getLeaderId())) {
            //5、综合部、分管副主任默认办理信息
            List<User> roleList = userRepo.findUserByRoleName(EnumFlowNodeGroupName.VICE_DIRECTOR.getValue());
            for (User user : roleList) {
                if (sign.getDealOrgType().equals(user.getMngOrgType())) {
                    sign.setLeaderId(user.getId());
                    sign.setLeaderName(user.getDisplayName());
                    sign.setComprehensivehandlesug("请" + user.getDisplayName() + "同志阅示。");
                    sign.setComprehensiveName("综合部");
                    sign.setComprehensiveDate(new Date());
                    break;
                }
            }
        }
    }

    /**
     * 通过signId查询平均评审天数和工作日
     *
     * @param signIds
     * @return
     */
    @Override
    public ResultMsg findAVGDayId(String signIds) {
        return signRepo.findAVGDayId(signIds);
    }


    /**
     * 保存项目维护中的添加评审部门
     *
     * @param signId
     * @param orgIds
     * @return
     */
    @Override
    public ResultMsg addAOrg(String signId, String orgIds) {
        return signRepo.addAOrg(signId, orgIds);
    }

    /**
     * 移除项目维护中所添加的评审部门
     *
     * @param signId
     * @param orgIds
     * @return
     */
    @Override
    public ResultMsg deleteAOg(String signId, String orgIds) {
        return signRepo.deleteAOrg(signId, orgIds);
    }

    /**
     * 保存项目维护中的添加负责人
     *
     * @param signId
     * @param userId
     * @return
     */
    @Override
    public ResultMsg addSecondUser(String signId, String userId) {
        return signRepo.addSecondUser(signId, userId);
    }

    /**
     * 删除项目维护中添加的负责人
     *
     * @param signId
     * @param userId
     * @return
     */
    @Override
    public ResultMsg deleteSecondUser(String signId, String userId) {
        return signRepo.deleteSecondUser(signId, userId);
    }

    /**
     * 保存是否能多选专家
     *
     * @param signId
     * @param isMoreExpert
     * @return
     */
    @Override
    public ResultMsg saveMoreExpert(String signId, String isMoreExpert) {
        return signRepo.saveMoreExpert(signId, isMoreExpert);
    }


    /**
     * 查询项目预签收信息
     *
     * @param odataObj
     * @return
     */
    @Override
    public PageModelDto<SignDto> findAllReserve(ODataObj odataObj) {
        PageModelDto<SignDto> pageModelDto = new PageModelDto<SignDto>();
        List<Sign> signlist = signRepo.findByOdata(odataObj);
        List<SignDto> signDtos = new ArrayList<SignDto>();
        if (Validate.isList(signlist)) {
            signlist.forEach(x -> {
                SignDto signDto = new SignDto();
                BeanCopierUtils.copyProperties(x, signDto);
                signDtos.add(signDto);
            });
        }
        pageModelDto.setCount(odataObj.getCount());
        pageModelDto.setValue(signDtos);

        return pageModelDto;
    }

    /**
     * 删除预签收项目（暂时不用）
     *
     * @param signid
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteReserveSign(String signid) {
        Sign sign = signRepo.findById(Sign_.signid.getName(), signid);
        if (sign != null) {
            signRepo.delete(sign);
            log.info(String.format("删除预签收项目{0}", sign.getProjectname()));
        }
    }

    /**
     * 获取合并评审次项目
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
     * 获取合并评审主项目
     *
     * @param signid
     * @return
     */
    @Override
    public List<SignDto> findMainReviewSign(String signid) {
        List<Sign> signList = signRepo.findMainReviewSign(signid);
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
     * @param sign
     * @return
     */
    @Override
    public void initSignNum(Sign sign) {
        if (sign.getSigndate() == null) {
            sign.setSigndate(new Date());
        }
        if (!Validate.isString(sign.getDealOrgType())) {
            sign.setDealOrgType(SIGN_PX_PREFIX);
        }
        String orgType = sign.getDealOrgType();
        String yearName = DateUtils.converToString(sign.getSigndate(), DateUtils.DATE_YEAR);
        int maxSeq = signRepo.getMaxSignSeq(yearName, orgType) + 1;
        if (maxSeq < 1) {
            maxSeq = 1;
        }
        String fileNum = maxSeq > 999 ? maxSeq + "" : String.format("%03d", maxSeq);
        sign.setSignNum(yearName + orgType + fileNum);
        sign.setSignSeq(maxSeq);
    }

    /**
     * 更新是否已生成模板状态
     *
     * @param signId
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
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
            //是副主任，只要没发文，均可取回(只能取自己分管的项目)
        } else {
            sBuffer.append(" (SELECT count(cs.signid) FROM cs_sign cs WHERE cs.signid = {alias}.businessKey and cs.leaderId = '" + SessionUtil.getUserId() + "') > 0 ");
            sBuffer.append(" AND ( ( SUBSTR (NODEDEFINEKEY, -1)) = '" + FlowConstant.SignFlowParams.BRANCH_INDEX1.getValue() + "' ");
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
     * 项目取回列表（分管领导和部门领导）
     * 根据不同的角色，获取不同的列表数据
     *
     * @param odataObj
     * @param isOrgLeader 是否部长
     */
    @Override
    public PageModelDto<RuProcessTask> getBackAppList(ODataObj odataObj, boolean isOrgLeader, User user) {
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
            sBuffer.append(" (SELECT ID FROM V_ORG_DEPT WHERE DIRECTORID = '" + user.getId() + "'))) ");
            criteria.add(Restrictions.sqlRestriction(sBuffer.toString()));
            //除去部门分办环节
            criteria.add(Restrictions.not(Restrictions.like(RuProcessTask_.nodeDefineKey.getName(), "%" + FlowConstant.FLOW_SIGN_BMFB1.substring(0, FlowConstant.FLOW_SIGN_BMFB1.length() - 1) + "%")));
            //是副主任，只要没发文，均可取回(只能取自己分管的项目)
        } else {
            sBuffer.append(" (SELECT count(cs.signid) FROM cs_sign cs WHERE cs.signid = {alias}.businessKey and cs.leaderId = '" + user.getId() + "') > 0 ");
            sBuffer.append(" AND ( ( SUBSTR (NODEDEFINEKEY, -1)) = '" + FlowConstant.SignFlowParams.BRANCH_INDEX1.getValue() + "' ");
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
        pageModelDto.setValue(Validate.isList(runProcessList) ? runProcessList : new ArrayList<>());
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
        //是否需要要删除所有分支
        boolean deleteBranchId = false;
        if (Validate.isString(branchId)) {
            deleteBranchId = true;
        }
        Sign sign = signRepo.findById(signId);
        //1、删除分支，如果是部长，则改变相应的状态即可
        if (deleteBranchId) {
            //如果是分管领导分办，则删除分支；
            signBranchRepo.resetBranchState(signId, branchId);
            //重新初始化负责人信息
            signRepo.initAOrgAndUser(sign, branchId);
        } else {
            HqlBuilder sqlBuilder1 = HqlBuilder.create();
            sqlBuilder1.append("delete from CS_SIGN_BRANCH where " + SignBranch_.signId.getName() + " =:signId ");
            sqlBuilder1.setParam("signId", signId);
            signBranchRepo.executeSql(sqlBuilder1);

            sign = ProjUtil.resetLeaderOption(sign);
            sign = ProjUtil.resetMinisterOption(sign);
            sign = ProjUtil.resetReviewDept(sign);
            sign = ProjUtil.resetReviewUser(sign);

            signRepo.save(sign);
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
        List<WorkProgram> workProgramList = ProjUtil.filterEnableWP(sign.getWorkProgramList());
        if (Validate.isList(workProgramList)) {
            List<WorkProgram> deleteWPList = new ArrayList<>();
            String deleteWPId = null;             //删除工作方案ID
            for (int i = 0, l = workProgramList.size(); i < l; i++) {
                WorkProgram wp = workProgramList.get(i);
                if (deleteBranchId) {
                    if (branchId.equals(wp.getBranchId())) {
                        deleteWPId = wp.getId();
                        deleteWPList.add(wp);
                        break;
                    }
                } else {
                    if (i > 0) {
                        deleteWPId += ",";
                    }
                    deleteWPId += wp.getId();
                    deleteWPList.add(wp);
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
            for (WorkProgram wp : deleteWPList) {
                sign.getWorkProgramList().remove(wp);
                sign.setProcessState(Constant.SignProcessState.IS_START.getValue());
                signRepo.save(sign);
                wp.setSign(null);
                //先解除关联，再删除
                workProgramRepo.save(wp);
                workProgramRepo.delete(wp);
            }
        }
    }

    /**
     * 启动时更新数据状态
     *
     * @param businessKey
     */
    @Override
    public ResultMsg activateFlow(String businessKey) {
        boolean isLoadWorkDay = false;
        Sign sign = signRepo.findById(businessKey);
        List<ProjectStop> stopList = sign.getProjectStopList();
        //如果有项目暂停列表，则要更新对应的状态信息
        if (Validate.isList(stopList)) {
            List<Workday> workdayList = null;
            List<ProjectStop> updateList = null;
            for (int i = 0, l = stopList.size(); i < l; i++) {
                ProjectStop pl = stopList.get(i);
                //审批通过，又没执行完的（正常情况下，只有一条记录）
                if (EnumState.YES.getValue().equals(pl.getIsactive()) && !EnumState.YES.getValue().equals(pl.getIsOverTime())) {
                    if (!isLoadWorkDay) {
                        workdayList = workdayService.findWorkDayByNow();
                        updateList = new ArrayList<>();
                        isLoadWorkDay = true;
                    }
                    int countDay = QuartzUnit.countWorkday(workdayList, (pl.getPausetime() == null ? pl.getCreatedDate() : pl.getPausetime()));
                    pl.setIsOverTime(Constant.EnumState.YES.getValue());
                    //实际启动日期
                    pl.setStartTime(new Date());
                    //实际暂停天数
                    pl.setPausedays(Float.parseFloat(String.valueOf(countDay)));

                    updateList.add(pl);
                }
            }
            if (Validate.isList(updateList)) {
                projectStopService.updateProjectStopStatus(updateList);
            }
        }
        //更改项目状态
        sign.setSignState(Constant.EnumState.PROCESS.getValue());
        sign.setIsLightUp(NO_LIGHT.getCodeValue());
        signRepo.save(sign);

        return new ResultMsg(true, MsgCode.OK.getValue(), "操作成功！");
    }

    /**
     * 保存项目信息，这个在定时器中用到，请不要删除
     *
     * @param sign
     */
    @Override
    @Transactional
    public void save(Sign sign) {
        signRepo.save(sign);
    }

    /**
     * 获取没有发送给发改委的项目信息
     * 这里要加上事务，否则无法获取工作方案和发文的信息
     *
     * @return
     */
    @Override
    @Transactional
    public List<SignDto> findUnSendFGWList() {
        List<SignDto> listSignDto = new ArrayList<>();
        List<Sign> listSign = signRepo.findUnSendFGWList();
        if (Validate.isList(listSign)) {
            for (int i = 0, l = listSign.size(); i < l; i++) {
                Sign sign = listSign.get(i);
                if (EnumState.STOP.getValue().equals(sign.getIsSendFGW()) || sign.getFilecode().endsWith("0000")) {
                    continue;
                } else {
                    SignDto signDto = new SignDto();
                    BeanCopierUtils.copyProperties(sign, signDto);
                    List<WorkProgram> workProgramList = ProjUtil.filterEnableWP(sign.getWorkProgramList());
                    //只获取主工作方案
                    if (Validate.isList(workProgramList)) {
                        List<WorkProgramDto> workProgramDtoList = new ArrayList<>();
                        WorkProgram mainWP = workProgramList.stream().filter(item -> FlowConstant.SignFlowParams.BRANCH_INDEX1.getValue().equals(item.getBranchId())).findFirst().get();
                        if (Validate.isObject(mainWP)) {
                            if (EnumState.YES.getValue().equals(mainWP.getBaseInfo())) {

                                //不是项目基本信息的才是项目工作方案
                            } else {
                                WorkProgramDto workProgramDto = new WorkProgramDto();
                                BeanCopierUtils.copyProperties(mainWP, workProgramDto);
                                workProgramDtoList.add(workProgramDto);
                                signDto.setWorkProgramDtoList(workProgramDtoList);
                            }
                        }
                    }
                    if (sign.getDispatchDoc() != null && Validate.isString(sign.getDispatchDoc().getId())) {
                        DispatchDocDto dispatchDocDto = new DispatchDocDto();
                        BeanCopierUtils.copyProperties(sign.getDispatchDoc(), dispatchDocDto);
                        signDto.setDispatchDocDto(dispatchDocDto);
                    }

                    //封装附件
                    List<SysFileDto> sysFileDtoList = new ArrayList<>();
                    List<SysFile> fileList = sysFileRepo.queryFileList(sign.getSignid(), "评审报告");
                    if (Validate.isList(fileList)) {
                        for (SysFile sysFile : fileList) {
                            SysFileDto sysFileDto = new SysFileDto();
                            BeanCopierUtils.copyProperties(sysFile, sysFileDto);
                            sysFileDtoList.add(sysFileDto);
                        }
                    }
                    signDto.setSysFileDtoList(sysFileDtoList);
                    listSignDto.add(signDto);
                }
            }
        }
        return listSignDto;
    }

    /**
     * 恢复项目
     *
     * @param signId
     * @param stateProperty
     * @param stateValue
     * @return
     */
    @Override
    public ResultMsg editSignState(String signId, String stateProperty, String stateValue) {
        if (signRepo.updateSignState(signId, stateProperty, stateValue)) {
            return new ResultMsg(true, MsgCode.OK.getValue(), "操作成功！");
        }
        return new ResultMsg(false, MsgCode.ERROR.getValue(), "操作失败！");
    }

    /**
     * 获取项目签收列表数量
     *
     * @return
     */
    @Override
    public Integer findSignCount() {
        Criteria criteria = signRepo.getExecutableCriteria();
        //1、排除旧项目
        criteria.add(Restrictions.isNull(Sign_.oldProjectId.getName()));
        //2、排除已终止、已完成
        Disjunction dis = Restrictions.disjunction();
        dis.add(Restrictions.isNull(Sign_.signState.getName()));
        dis.add(Restrictions.sqlRestriction(" " + Sign_.signState.getName() + " != '" + EnumState.YES.getValue() + "' and " + Sign_.signState.getName() + " != '" + EnumState.FORCE.getValue() + "' and " + Sign_.signState.getName() + " != '" + EnumState.DELETE.getValue() + "' "));
        criteria.add(dis);

        //3、已签收，但是未发起流程的项目
        criteria.add(Restrictions.eq(Sign_.issign.getName(), EnumState.YES.getValue()));
        criteria.add(Restrictions.isNull(Sign_.processInstanceId.getName()));
        Integer totalResult = ((Number) criteria.setProjection(Projections.rowCount()).uniqueResult()).intValue();

        return totalResult;
    }

    /**
     * 获取项目预签收列表数量
     *
     * @return
     */
    @Override
    public Integer findReservesSignCount() {
        Criteria criteria = signRepo.getExecutableCriteria();
        //1、排除旧项目
        criteria.add(Restrictions.isNull(Sign_.oldProjectId.getName()));
        //2、排除已终止、已完成、已删除
        Disjunction dis = Restrictions.disjunction();
        dis.add(Restrictions.isNull(Sign_.signState.getName()));
        dis.add(Restrictions.sqlRestriction(" " + Sign_.signState.getName() + " != '" + EnumState.YES.getValue() + "' and " + Sign_.signState.getName() + " != '" + EnumState.FORCE.getValue() + "' and " + Sign_.signState.getName() + " != '" + EnumState.DELETE.getValue() + "' "));
        criteria.add(dis);

        //3、预签收
        StringBuffer sb = new StringBuffer();
        sb.append(" " + Sign_.ispresign.getName() + " is not null and " + Sign_.ispresign.getName() + " = '" + EnumState.NO.getValue() + "' ");
        criteria.add(Restrictions.sqlRestriction(sb.toString()));

        Integer totalResult = ((Number) criteria.setProjection(Projections.rowCount()).uniqueResult()).intValue();
        return totalResult;
    }

    /**
     * 统计项目平均天数，未办结的按当前日期算，已办结的按办结日期算
     *
     * @param signIds
     * @return
     */
    @Override
    public ResultMsg sumExistDays(String signIds) {
        ResultMsg resultMsg;
        try {
            int totalDays = signRepo.sumExistDays(signIds);
            resultMsg = new ResultMsg(true, MsgCode.OK.getValue(), "统计成功！", totalDays);
        } catch (Exception e) {
            resultMsg = new ResultMsg(false, MsgCode.ERROR.getValue(), "统计错误！");
        }
        return resultMsg;
    }

    /**
     * 通过收文id查询 评审天数、剩余工作日、收文日期、送来日期、评审总天数等
     *
     * @param signId
     * @return
     */
    @Override
    public SignDto findReviewDayBySignId(String signId) {
        SignDto signDto = signRepo.findReviewDayBySignId(signId);
        if (!Validate.isObject(signDto)) {
            signDto = new SignDto();
        }
        return signDto;
    }


    /**
     * 保存评审工作日维护的信息
     *
     * @param signDto
     * @return
     */
    @Override
    public ResultMsg saveReview(SignDto signDto) {
        return signRepo.saveReview(signDto);
    }

    @Override
    public SignDto findSignByFileCode(SignDto signDto) {
        return signRepo.findSignByFileCode(signDto);
    }

    @Override
    public ResultMsg updateSendFGWState(String signId, String state) {
        HqlBuilder hqlBuilder = HqlBuilder.create();
        hqlBuilder.append(" update " + Sign.class.getSimpleName() + " set " + Sign_.isSendFGW.getName() + " =:state ");
        hqlBuilder.setParam("state", Validate.isString(state) ? state : EnumState.STOP.getValue());
        hqlBuilder.append(" where " + Sign_.signid.getName() + " = :signid ");
        hqlBuilder.setParam("signid", signId);
        int updateCount = signRepo.executeHql(hqlBuilder);
        if (updateCount > 0) {
            return ResultMsg.ok("操作成功！");
        } else {
            return ResultMsg.error( "操作失败！");
        }
    }
}
