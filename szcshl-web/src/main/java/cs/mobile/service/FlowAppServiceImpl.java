package cs.mobile.service;

import com.alibaba.fastjson.JSON;
import cs.ahelper.projhelper.DisUtil;
import cs.ahelper.projhelper.ProjUtil;
import cs.ahelper.projhelper.WorkPGUtil;
import cs.common.constants.Constant;
import cs.common.constants.FlowConstant;
import cs.common.ResultMsg;
import cs.common.constants.SysConstants;
import cs.common.utils.*;
import cs.domain.book.BookBuy;
import cs.domain.book.BookBuyBusiness;
import cs.domain.book.BookBuyBusiness_;
import cs.domain.expert.ExpertReview;
import cs.domain.expert.ExpertReview_;
import cs.domain.expert.ExpertSelected_;
import cs.domain.project.*;
import cs.domain.sys.*;
import cs.model.flow.FlowDto;
import cs.model.sys.RoleDto;
import cs.model.sys.UserDto;
import cs.repository.repositoryImpl.book.BookBuyBusinessRepo;
import cs.repository.repositoryImpl.expert.ExpertRepo;
import cs.repository.repositoryImpl.expert.ExpertReviewRepo;
import cs.repository.repositoryImpl.expert.ExpertSelectedRepo;
import cs.repository.repositoryImpl.meeting.RoomBookingRepo;
import cs.repository.repositoryImpl.project.*;
import cs.repository.repositoryImpl.sys.*;
import cs.service.flow.FlowService;
import cs.service.project.*;
import cs.service.rtx.RTXSendMsgPool;
import cs.service.sys.UserService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.impl.pvm.process.ProcessDefinitionImpl;
import org.activiti.engine.impl.pvm.process.TransitionImpl;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.NativeQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static cs.common.constants.Constant.EMPTY_STRING;
import static cs.common.constants.FlowConstant.*;
import static cs.common.constants.FlowConstant.FLOW_SIGN_ZR_QRFW;
import static cs.common.constants.SysConstants.SEPARATE_COMMA;

/**
 * Created by Administrator on 2018/2/28.
 */
@Service
public class FlowAppServiceImpl implements FlowAppService {
    private static Logger log = Logger.getLogger(SignServiceImpl.class);
    @Autowired
    private SignRepo signRepo;
    @Autowired
    private UserRepo userRepo;
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
    private FileRecordService fileRecordService;
    @Autowired
    private SignPrincipalRepo signPrincipalRepo;
    @Autowired
    private SignPrincipalService signPrincipalService;
    @Autowired
    private FlowService flowService;
    @Autowired
    private SignBranchRepo signBranchRepo;
    //部门（小组）列表
    @Autowired
    private OrgDeptRepo orgDeptRepo;
    @Autowired
    private ExpertReviewRepo expertReviewRepo;
    @Autowired
    private SignMergeRepo signMergeRepo;
    @Autowired
    private RoomBookingRepo roomBookingRepo;
    @Autowired
    private ExpertSelectedRepo expertSelectedRepo;
    @Autowired
    private ExpertRepo expertRepo;
    @Autowired
    private BookBuyBusinessRepo bookBuyBusinessRepo;
    @Autowired
    private AddSuppLetterRepo  addSuppLetterRepo;
    @Autowired
    private AnnountmentRepo annountmentRepo;
    @Autowired
    private  AddSuppLetterService addSuppLetterService;

    @Autowired
    private AgentTaskService agentTaskService;

    @Autowired
    private UserService userService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private SessionFactory sessionFactory;

    @Autowired
    private RepositoryService repositoryService;

    @Override
    @Transactional
    public ResultMsg dealFlow(ProcessInstance processInstance, Task task, FlowDto flowDto, UserDto userDto) throws Exception {
        //参数定义
        String signid = processInstance.getBusinessKey(),
                businessId = "",                        //前段传过来的业务ID
                businessValue = "",                     //业务值
                assigneeValue = "",                     //环节处理人
                branchIndex = "",                       //分支序号
                nextNodeKey = "",                       //下一环节名称
                curUserId = userDto.getId();    //当前用户ID
        Sign sign = null;                           //收文对象
        WorkProgram wk = null;                      //工作方案
        DispatchDoc dp = null;                      //发文
        FileRecord fileRecord = null;               //归档
        List<User> userList = null;                 //用户列表
        List<SignPrincipal> signPriList = null;     //项目负责人
        List<SignPrincipal> signPriListNew = null;     //项目负责人
        User dealUser = null;                       //处理人
        OrgDept orgDept = null;                     //部门和小组
        ResultMsg returnResult = null;
        DisUtil disUtil = null;
        WorkPGUtil workPGUtil = null;
        boolean isNextUser = false,                 //是否是下一环节处理人（主要是处理领导审批，目前主要有三个地方，部长审批工作方案，部长审批发文和分管领导审批发文）
                isAgentTask = agentTaskService.isAgentTask(task.getId(),curUserId),
                isMergeDisTask = false,             //是否合并发文任务
                isCompeleteSign = true;             //是否完成所有会签
        List<AgentTask> agentTaskList = new ArrayList<>();
        //取得之前的环节处理人信息
        Map<String, Object> variables = new HashMap<>();

        //以下是流程环节处理
        switch (task.getTaskDefinitionKey()) {
            //分管副主任审批
            case FlowConstant.FLOW_SIGN_FGLD_FB:
                if (flowDto.getBusinessMap().get("MAIN_ORG") == null) {
                    return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), "请先选择主办部门！");
                }
                sign = signRepo.findById(Sign_.signid.getName(), signid);
                //流程分支
                List<SignBranch> saveBranchList = new ArrayList<>();
                businessId = flowDto.getBusinessMap().get("MAIN_ORG").toString();   //主办部门ID
                SignBranch signBranch1 = new SignBranch(signid, FlowConstant.SignFlowParams.BRANCH_INDEX1.getValue(), Constant.EnumState.YES.getValue(), Constant.EnumState.NO.getValue(), Constant.EnumState.YES.getValue(), businessId, Constant.EnumState.NO.getValue());
                saveBranchList.add(signBranch1);
                //设置流程参数
                variables.put(FlowConstant.SignFlowParams.BRANCH1.getValue(), true);
                orgDept = orgDeptRepo.findOrgDeptById(businessId);
                if (orgDept == null || !Validate.isString(orgDept.getDirectorID())) {
                    return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), "请设置" + orgDept.getName() + "的部门负责人！");
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
                if (flowDto.getBusinessMap().get("ASSIST_ORG") != null) {
                    businessId = flowDto.getBusinessMap().get("ASSIST_ORG").toString();
                    assistOrgIdList = StringUtil.getSplit(businessId, SysConstants.SEPARATE_COMMA);
                }

                if (Validate.isList(assistOrgIdList)) {
                    if (assistOrgIdList.size() > 3) {
                        return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), "协办部门最多只能选择3个！");
                    }
                    String aOrgId = "", aOrgName = "";
                    for (int i = 2, l = (assistOrgIdList.size() + 2); i < l; i++) {
                        businessId = assistOrgIdList.get(i - 2);
                        SignBranch signBranch = new SignBranch(signid, String.valueOf(i), Constant.EnumState.YES.getValue(), Constant.EnumState.NO.getValue(), Constant.EnumState.NO.getValue(), businessId, Constant.EnumState.NO.getValue());
                        saveBranchList.add(signBranch);
                        //判断是否有部门负责人
                        orgDept = orgDeptRepo.findOrgDeptById(businessId);
                        if (orgDept == null || !Validate.isString(orgDept.getDirectorID())) {
                            return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), "请设置" + orgDept.getName() + "的部门负责人！");
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
                if(isAgentTask){
                    sign.setLeaderId(agentTaskService.getUserId(task.getId(),curUserId));
                }else{
                    sign.setLeaderId(curUserId);
                }
                sign.setLeaderName(ActivitiUtil.getSignName(userDto.getDisplayName(),isAgentTask));
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
                if (Constant.EnumState.YES.getValue().equals(sign.getIsassistflow())) {
                    if (flowDto.getBusinessMap().get("PRINCIPAL") == null) {
                        return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), "请选择项目负责人！");
                    }
                    signPriList = JSON.parseArray(flowDto.getBusinessMap().get("PRINCIPAL").toString(), SignPrincipal.class);
                    signPriListNew = new ArrayList<SignPrincipal>() ;
                    if (!Validate.isList(signPriList)) {
                        return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), "请选择项目负责人！");
                    }
                    String mUserId = sign.getmUserId() == null ? "" : sign.getmUserId(),
                            mUserName = sign.getmUserName() == null ? "" : sign.getmUserName(),
                            aUserId = sign.getaUserID() == null ? "" : sign.getaUserID(),
                            aUserName = sign.getaUserName() == null ? "" : sign.getaUserName();
                    for (int i = 0, l = signPriList.size(); i < l; i++) {
                        SignPrincipal obj = signPriList.get(i);
                        dealUser = userRepo.getCacheUserById(obj.getUserId());
                        String userId = userService.getTaskDealId(dealUser, agentTaskList,nextNodeKey);
                        assigneeValue = StringUtil.joinString(assigneeValue, SysConstants.SEPARATE_COMMA, userId);
                        obj.setSignId(signid);
                        obj.setFlowBranch(branchIndex);
                       // obj.setUserType(new String(obj.getUserType().getBytes("ISO-8859-1"),"UTF-8"));
                        signPriListNew.add(obj);
                        //设置负责人信息
                        if (Constant.EnumState.YES.getValue().equals(obj.getIsMainUser())) {
                            mUserId = obj.getUserId();
                            mUserName = dealUser.getDisplayName();
                        } else {
                            aUserId = StringUtil.joinString(aUserId, SysConstants.SEPARATE_COMMA, obj.getUserId());
                            aUserName = StringUtil.joinString(aUserName, SysConstants.SEPARATE_COMMA, dealUser.getDisplayName());
                        }
                    }
                    signPriList.clear();
                    signPriList = signPriListNew;
                    //设置主办人员信息
                    sign.setmUserId(mUserId);
                    sign.setmUserName(mUserName);
                    sign.setaUserID(aUserId);
                    sign.setaUserName(aUserName);
                    //不是协审项目
                } else {
                    //主流程处理，一定要有第一负责人
                    if (FlowConstant.SignFlowParams.BRANCH_INDEX1.getValue().equals(branchIndex)) {
                        if (flowDto.getBusinessMap().get("M_USER_ID") == null) {
                            return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), "请选择第一负责人！");
                        }
                        businessValue = flowDto.getBusinessMap().get("M_USER_ID").toString();
                        //查询是否有代办
                        dealUser = userRepo.getCacheUserById(businessValue);
                        assigneeValue = userService.getTaskDealId(dealUser, agentTaskList,nextNodeKey);
                        //设置主办人员信息
                        sign.setmUserId(businessValue);
                        sign.setmUserName(dealUser.getDisplayName());

                        //设置项目负责人
                        signPriList = new ArrayList<>();
                        SignPrincipal mainPri = new SignPrincipal(signid, businessValue, branchIndex, "", null, Constant.EnumState.YES.getValue());
                        signPriList.add(mainPri);
                    }
                    //项目负责人
                    if (flowDto.getBusinessMap().get("A_USER_ID") != null) {
                        businessValue = flowDto.getBusinessMap().get("A_USER_ID").toString();
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

                            SignPrincipal secondPri = new SignPrincipal(signid, user.getId(), branchIndex, "", null, Constant.EnumState.NO.getValue());
                            signPriList.add(secondPri);
                        }
                        //设置协办人员信息
                        sign.setaUserID(aUserId);
                        sign.setaUserName(aUserName);
                    } else {
                        //分支流程必须要选择第二负责人
                        if (!FlowConstant.SignFlowParams.BRANCH_INDEX1.getValue().equals(branchIndex)) {
                            return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), "请选择项目负责人！");
                        }
                    }
                }
                //保存项目负责人
                signPrincipalRepo.bathUpdate(signPriList);
                ActivitiUtil.setFlowPriUser(variables, branchIndex, assigneeValue);
                //保存处理意见，单个分支按之前的格式，跟多分支的时候，拼接下
                if (null != sign.getBranchCount() &&  sign.getBranchCount() == 1) {
                    sign.setMinisterhandlesug(flowDto.getDealOption());
                } else {
                    String optionString = Validate.isString(sign.getMinisterhandlesug()) ? (sign.getMinisterhandlesug() + "<br>") : "";
                    sign.setMinisterhandlesug(optionString + flowDto.getDealOption() + " 签名：" + ActivitiUtil.getSignName(userDto.getDisplayName(),isAgentTask) + "  日期：" + DateUtils.converToString(new Date(), "yyyy年MM月dd日"));
                }
                //主办部门意见也保存下
                if (branchIndex.equals(FlowConstant.SignFlowParams.BRANCH_INDEX1.getValue())) {
                    sign.setMinisterDate(new Date());
                    if(isAgentTask){
                        sign.setMinisterId(agentTaskService.getUserId(task.getId(),curUserId));
                    }else{
                        sign.setMinisterId(curUserId);
                    }
                    sign.setMinisterName(ActivitiUtil.getSignName(userDto.getDisplayName(),isAgentTask));
                }
                //完成部门分办，表示正在做工作方案
                sign.setProcessState(Constant.SignProcessState.DO_WP.getValue());
                //更新工作方案中的项目负责人
                WorkProgram mainwk2 = workProgramRepo.findBySignIdAndBranchId(signid, FlowConstant.SignFlowParams.BRANCH_INDEX1.getValue(), false);
                if(Validate.isObject(mainwk2)){
                    mainwk2.setSecondChargeUserName(sign.getaUserName());
                    mainwk2.setSign(sign);
                    workProgramRepo.save(mainwk2);
                }
                signRepo.save(sign);
                break;

            //部长审批工作方案1
            case FlowConstant.FLOW_SIGN_BMLD_SPW1:
                branchIndex = FlowConstant.SignFlowParams.BRANCH_INDEX1.getValue();
                nextNodeKey = FlowConstant.FLOW_SIGN_FGLD_SPW1;
                //部长审批工作方案1
            case FlowConstant.FLOW_SIGN_BMLD_SPW2:
                if (!Validate.isString(branchIndex)) {
                    branchIndex = FlowConstant.SignFlowParams.BRANCH_INDEX2.getValue();
                    nextNodeKey = FlowConstant.FLOW_SIGN_FGLD_SPW2;
                }
                //部长审批工作方案1
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
          /*      if (SessionUtil.getUserInfo().getOrg() == null) {
                    return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), "你还没设置所属部门！");
                }
                if (!Validate.isString(SessionUtil.getUserInfo().getOrg().getOrgSLeader())) {
                    return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), "请先设置该部门的分管副主任！");
                }*/
                //更改工作方案信息
                wk = workProgramRepo.findBySignIdAndBranchId(signid, branchIndex, false);
                workPGUtil = WorkPGUtil.create(wk);
                workPGUtil.setMinisterOption(flowDto.getDealOption(),new Date(),ActivitiUtil.getSignName(userDto.getDisplayName(),isAgentTask));
                //如果是主办流程，要判断是否有合并评审方案，有则跟着主项目一起办理
                if (ProjUtil.isMainBranch(branchIndex)) {
                    if (workPGUtil.isMergeWP() && workPGUtil.isMainWP()) {
                        List<SignMerge> mergeList = signMergeRepo.findByType(signid, Constant.MergeType.WORK_PROGRAM.getValue());
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
                    assigneeValue = userService.getTaskDealId(sign.getLeaderId(), agentTaskList,nextNodeKey);
                    variables.put(FlowConstant.SignFlowParams.USER_FGLD1.getValue(), assigneeValue);
                    nextNodeKey = FlowConstant.FLOW_SIGN_FGLD_SPW1;
                } else if (FlowConstant.SignFlowParams.BRANCH_INDEX2.getValue().equals(branchIndex)) {

                    assigneeValue = userService.getTaskDealId(userDto.getOrgDto().getOrgSLeader(), agentTaskList,nextNodeKey);
                    variables.put(FlowConstant.SignFlowParams.USER_FGLD2.getValue(), assigneeValue);
                    nextNodeKey = FlowConstant.FLOW_SIGN_FGLD_SPW2;
                } else if (FlowConstant.SignFlowParams.BRANCH_INDEX3.getValue().equals(branchIndex)) {
                    assigneeValue = userService.getTaskDealId(userDto.getOrgDto().getOrgSLeader(), agentTaskList,nextNodeKey);
                    variables.put(FlowConstant.SignFlowParams.USER_FGLD3.getValue(), assigneeValue);
                    nextNodeKey = FlowConstant.FLOW_SIGN_FGLD_SPW3;
                } else if (FlowConstant.SignFlowParams.BRANCH_INDEX4.getValue().equals(branchIndex)) {
                    assigneeValue = userService.getTaskDealId(userDto.getOrgDto().getOrgSLeader(), agentTaskList,nextNodeKey);
                    variables.put(FlowConstant.SignFlowParams.USER_FGLD4.getValue(), assigneeValue);
                    nextNodeKey = FlowConstant.FLOW_SIGN_FGLD_SPW4;
                }
                //下一环节还是自己
                if (assigneeValue.equals(curUserId)) {
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
                    return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), "项目还没分配主负责人，不能进行下一步操作！请联系主办部门进行负责人分配！");
                }
                assigneeValue = userService.getTaskDealId(dealUser, agentTaskList, FLOW_SIGN_FW);
                variables.put(FlowConstant.SignFlowParams.USER_FZR1.getValue(), assigneeValue);

                //更改工作方案审核信息
                wk = workProgramRepo.findBySignIdAndBranchId(signid, branchIndex, false);
                workPGUtil = WorkPGUtil.create(wk);

                //如果是主办流程，要判断是否有合并评审方案，有则跟着主项目一起办理
                if (FlowConstant.SignFlowParams.BRANCH_INDEX1.getValue().equals(branchIndex)) {
                    if (Constant.MergeType.REVIEW_MERGE.getValue().equals(wk.getIsSigle()) && Constant.EnumState.YES.getValue().equals(wk.getIsMainProject())) {
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

                workPGUtil.setLeaderOption(flowDto.getDealOption(),new Date(),ActivitiUtil.getSignName(userDto.getDisplayName(),isAgentTask));
                workProgramRepo.save(wk);
                //完成分支的工作方案
                signBranchRepo.updateFinishState(signid, branchIndex, Constant.EnumState.YES.getValue());
                //更改预定会议室状态
                roomBookingRepo.updateStateByBusinessId(wk.getId(), Constant.EnumState.YES.getValue());
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
                            expertSelectedRepo.updateExpertSelectState(wk.getId(), ExpertSelected_.isLetterRw.getName(), Constant.EnumState.YES.getValue());
                        }
                        expertReviewRepo.save(expertReview);
                    }
                }
                break;
            //协办部长审批发文
            case FlowConstant.FLOW_SIGN_BMLD_QRFW_XB:
                if (flowDto.getBusinessMap().get("AGREE") == null || !Validate.isString(flowDto.getBusinessMap().get("AGREE").toString())) {
                    return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), "请先勾选对应的审批结果！");
                }
                String dirDealOption = flowDto.getDealOption();
                //如果同意，则到主办部长审批
                if (Constant.EnumState.YES.getValue().equals(flowDto.getBusinessMap().get("AGREE").toString())) {
                    variables.put(FlowConstant.SignFlowParams.XBBZ_SP.getValue(), true);
                    //获取主分支的部门领导
                    assigneeValue = getMainDirecotr(signid, agentTaskList, FlowConstant.FLOW_SIGN_BMLD_QRFW);
                    variables.put(FlowConstant.SignFlowParams.USER_BZ1.getValue(), assigneeValue);
                    flowDto.setDealOption(flowDto.getDealOption() + "【审批结果：核稿无误】");
                    isCompeleteSign = ProjUtil.checkSignComplete(taskService.getVariables(task.getId()),1);
                    //如果不同意，则回退到发文环节
                } else {
                    variables.put(FlowConstant.SignFlowParams.XBBZ_SP.getValue(), false);
                    //获取第一负责人
                    assigneeValue = getMainPriUserId(signid, agentTaskList,FlowConstant.FLOW_SIGN_FW);
                    variables.put(FlowConstant.SignFlowParams.USER_FZR1.getValue(), assigneeValue);
                    flowDto.setDealOption(flowDto.getDealOption() + "【审批结果：核稿有误】");
                }
                //协办部门的意见也要保存
                businessId = flowDto.getBusinessMap().get("DIS_ID").toString();
                dp = dispatchDocRepo.findById(DispatchDoc_.id.getName(), businessId);
                String optionString2 = Validate.isString(dp.getMinisterSuggesttion()) ? (dp.getMinisterSuggesttion() + "<br>") : "";
                dp.setMinisterSuggesttion(optionString2 + dirDealOption + " 签名：" + ActivitiUtil.getSignName(userDto.getDisplayName(),isAgentTask) + " 日期：" + DateUtils.converToString(new Date(), DateUtils.DATE_PATTERN));

                dispatchDocRepo.save(dp);
                break;
            //部长审批发文
            case FLOW_SIGN_BMLD_QRFW:
                businessId = flowDto.getBusinessMap().get("DIS_ID").toString();
                dp = dispatchDocRepo.findById(DispatchDoc_.id.getName(), businessId);
                disUtil = DisUtil.create(dp);
                //是否是合并发文项目
                if(disUtil.isMergeDis()){
                    if(disUtil.isMainProj()){
                        isMergeDisTask = true;
                    }else{
                        return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(),"合并发文的项目，只能由主项目进行操作！");
                    }
                }
                if(isMergeDisTask){
                    //合并发文主项目，另起一个方法处理
                    returnResult = flowService.dealMerDisFlow(processInstance,task,dp,FLOW_SIGN_BMLD_QRFW,flowDto,isAgentTask);
                }else{
                    //获取主办分管领导
                    sign = signRepo.findById(Sign_.signid.getName(), signid);
                    User mainLead = userRepo.getCacheUserById(sign.getLeaderId());
                    //获取所有分管领导信息
                    userList = signBranchRepo.findAssistSLeader(signid);
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
                    }

                    //修改发文信息
                    if (dp.getBranchCount() == 1) {
                        dp.setMinisterSuggesttion(flowDto.getDealOption());
                    } else {
                        String optionString3 = Validate.isString(dp.getMinisterSuggesttion()) ? (dp.getMinisterSuggesttion() + "<br>") : "";
                        dp.setMinisterSuggesttion(optionString3 + flowDto.getDealOption() + " 签名：" + ActivitiUtil.getSignName(userDto.getDisplayName(),isAgentTask) + "   日期：" + DateUtils.converToString(new Date(), DateUtils.DATE_PATTERN));
                    }
                    //发文日期也要保存下
                    dp.setMinisterDate(new Date());
                    dp.setMinisterName(ActivitiUtil.getSignName(userDto.getDisplayName(),isAgentTask));
                    dispatchDocRepo.save(dp);
                }
                break;
            //协办分管领导审批发文
            case FlowConstant.FLOW_SIGN_FGLD_QRFW_XB:
                Boolean isDirector = (Boolean) flowDto.getBusinessMap().get("isDirector");
                if (null != isDirector && isDirector) {
                } else {
                    if (flowDto.getBusinessMap().get("AGREE") == null || !Validate.isString(flowDto.getBusinessMap().get("AGREE").toString())) {
                        return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), "请先勾选对应的审批结果！");
                    }
                }
                //如果同意
                String agreeString = flowDto.getBusinessMap().get("AGREE").toString();
                if (Constant.EnumState.YES.getValue().equals(agreeString) || EMPTY_STRING.equals(agreeString)) {
                    variables.put(FlowConstant.SignFlowParams.XBFZR_SP.getValue(), true);
                    //获取主办分管领导
                    sign = signRepo.findById(Sign_.signid.getName(), signid);
                    assigneeValue = userService.getTaskDealId(sign.getLeaderId(), agentTaskList,FlowConstant.FLOW_SIGN_FGLD_QRFW);
                    variables.put(FlowConstant.SignFlowParams.USER_FGLD1.getValue(), assigneeValue);
                    if (EMPTY_STRING.equals(agreeString)) {
                        flowDto.setDealOption(EMPTY_STRING);
                    }
                    //不同意则回退到发文申请环节
                } else {
                    variables.put(FlowConstant.SignFlowParams.XBFZR_SP.getValue(), false);
                    //获取第一负责人
                    assigneeValue = getMainPriUserId(signid, agentTaskList,FlowConstant.FLOW_SIGN_FW);
                    variables.put(FlowConstant.SignFlowParams.USER_FZR1.getValue(), assigneeValue);
                    flowDto.setDealOption(flowDto.getDealOption() + "【审批结果：核稿有误】");
                }
                //修改发文信息
                businessId = flowDto.getBusinessMap().get("DIS_ID").toString();
                dp = dispatchDocRepo.findById(DispatchDoc_.id.getName(), businessId);
                String vdSugMin = Validate.isString(dp.getViceDirectorSuggesttion()) ? (dp.getViceDirectorSuggesttion() + "<br>") : "";
                dp.setViceDirectorSuggesttion(vdSugMin + flowDto.getDealOption() + " 签名：" + ActivitiUtil.getSignName(userDto.getDisplayName(),isAgentTask) + "   日期：" + DateUtils.converToString(new Date(), DateUtils.DATE_PATTERN));
                dp.setMoreLeader(1);
                dispatchDocRepo.save(dp);
                break;
            //主办分管主任审批发文
            case FlowConstant.FLOW_SIGN_FGLD_QRFW:
                userList = userRepo.findUserByRoleName(Constant.EnumFlowNodeGroupName.DIRECTOR.getValue());
                if (!Validate.isList(userList)) {
                    return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), "请先设置【" + Constant.EnumFlowNodeGroupName.DIRECTOR.getValue() + "】角色用户！");
                }
                businessId = flowDto.getBusinessMap().get("DIS_ID").toString();
                dp = dispatchDocRepo.findById(DispatchDoc_.id.getName(), businessId);
                disUtil = DisUtil.create(dp);
                //是否是合并发文项目
                if(disUtil.isMergeDis()){
                    if(disUtil.isMainProj()){
                        isMergeDisTask = true;
                    }else{
                        return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(),"合并发文的项目，只能由主项目进行操作！");
                    }
                }
                if(isMergeDisTask){
                    //合并发文主项目，另起一个方法处理
                    returnResult = flowService.dealMerDisFlow(processInstance,task,dp,FLOW_SIGN_FGLD_QRFW,flowDto,isAgentTask);
                }else{
                    dealUser = userList.get(0);
                    assigneeValue = userService.getTaskDealId(dealUser, agentTaskList, FLOW_SIGN_ZR_QRFW);
                    variables.put(FlowConstant.SignFlowParams.USER_ZR.getValue(), assigneeValue);

                    //修改发文信息
                    if (dp.getMoreLeader() == 1) {
                        String vdSug = Validate.isString(dp.getViceDirectorSuggesttion()) ? (dp.getViceDirectorSuggesttion() + "<br>") : "";
                        dp.setViceDirectorSuggesttion(vdSug + flowDto.getDealOption() + "  签名：" + ActivitiUtil.getSignName(userDto.getDisplayName(),isAgentTask) + "   日期：" + DateUtils.converToString(new Date(), "yyyy年MM月dd日"));
                    } else {
                        dp.setViceDirectorSuggesttion(flowDto.getDealOption());
                    }
                    dp.setViceDirectorDate(new Date());
                    dp.setViceDirectorName(ActivitiUtil.getSignName(userDto.getDisplayName(),isAgentTask));
                    dispatchDocRepo.save(dp);
                    //下一环节还是自己处理
                    if (assigneeValue.equals(userDto.getId())) {
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
                if(disUtil.isMergeDis()){
                    if(disUtil.isMainProj()){
                        isMergeDisTask = true;
                    }else{
                        return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(),"合并发文的项目，只能由主项目进行操作！");
                    }
                }
                if(isMergeDisTask){
                    //合并发文主项目，另起一个方法处理
                    returnResult = flowService.dealMerDisFlow(processInstance,task,dp,FLOW_SIGN_ZR_QRFW,flowDto,isAgentTask);
                }else{
                    dp.setDirectorSuggesttion(flowDto.getDealOption());
                    dp.setDirectorDate(new Date());
                    dp.setDirectorName(ActivitiUtil.getSignName(userDto.getDisplayName(),isAgentTask));
                    dispatchDocRepo.save(dp);

                    //获取第一负责人
                    assigneeValue = getMainPriUserId(signid, agentTaskList,FlowConstant.FLOW_SIGN_FWBH);
                    variables.put(FlowConstant.SignFlowParams.USER_FZR1.getValue(), assigneeValue);
                }

                break;

            default:
                ;
        }

        if(isMergeDisTask){
            //如果是合并发文项目，直接返回处理结果
            return returnResult;
        }else{
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
                            return dealFlow(processInstance, t, flowDto,userDto);
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
                agentTaskService.updateAgentInfo(agentTaskList,processInstance.getId(),processInstance.getName());
            }

            return new ResultMsg(true, Constant.MsgCode.OK.getValue(), "操作成功！");
        }
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

    @Override
    public String getMainDirecotr(String signid, List<AgentTask> agentTaskList,String nodeKey) {
        OrgDept orgDept = orgDeptRepo.queryBySignBranchId(signid, FlowConstant.SignFlowParams.BRANCH_INDEX1.getValue());
        return userService.getTaskDealId(orgDept.getDirectorID(), agentTaskList,nodeKey);
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
     * 图书流程处理
     * @param processInstance
     * @param flowDto
     * @return ResultMsg
     */
    @Override
    @Transactional
    public ResultMsg bookDealFlow(ProcessInstance processInstance, Task task, FlowDto flowDto, UserDto userDto) {
        String businessKey = processInstance.getBusinessKey(), businessId = "", assigneeValue = "", branchIndex = "";
        BookBuyBusiness bookBuyBusiness = null;
        List<User> dealUserList = null;
        User dealUser = null;
        Org org = null;
        boolean isNextUser = false;                 //是否是下一环节处理人（主要是处理领导审批，部长审批）
        String nextNodeKey = "";                    //下一环节名称
        Map<String, Object> variables = processInstance.getProcessVariables();
        boolean saveFlag = false;
        //以下是流程环节处理
        switch (task.getTaskDefinitionKey()) {
            case FlowConstant.BOOK_BZSP:
                bookBuyBusiness = bookBuyBusinessRepo.findById(BookBuyBusiness_.businessId.getName(), businessKey);
                bookBuyBusiness.setOrgDirectorId(userDto.getOrgDto().getOrgDirector());
                bookBuyBusiness.setOrgDirector(userDto.getOrgDto().getOrgDirectorName());
                bookBuyBusiness.setOrgDirectorDate(new Date());
                bookBuyBusiness.setApplyReason(flowDto.getDealOption());
                bookBuyBusinessRepo.save(bookBuyBusiness);
                task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).active().singleResult();
                assigneeValue = userDto.getOrgDto().getOrgSLeader();
                variables = ActivitiUtil.setAssigneeValue(FlowConstant.BooksBuyFlowParams.USER_FGLD.getValue(), assigneeValue);
                //下一环节还是自己处理
                if (assigneeValue.equals(userDto.getId())) {
                    isNextUser = true;
                    nextNodeKey = FlowConstant.BOOK_FGFZRSP;
                }
                break;
            case FlowConstant.BOOK_FGFZRSP:
                bookBuyBusiness = bookBuyBusinessRepo.findById(BookBuyBusiness_.businessId.getName(), businessKey);
                bookBuyBusiness.setOrgSLeaderId(userDto.getId());
                bookBuyBusiness.setOrgSLeader(userDto.getLoginName());
                bookBuyBusiness.setOrgSLeaderDate(new Date());
                bookBuyBusiness.setOrgSLeaderHandlesug(flowDto.getDealOption());
                bookBuyBusinessRepo.save(bookBuyBusiness);
                dealUserList = userRepo.findUserByRoleName(Constant.EnumFlowNodeGroupName.DIRECTOR.getValue());
                if (!Validate.isList(dealUserList)) {
                    return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), "请先设置【" + Constant.EnumFlowNodeGroupName.DIRECTOR.getValue() + "】角色用户！");
                }
                dealUser = dealUserList.get(0);
                task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).active().singleResult();
                assigneeValue = Validate.isString(dealUser.getTakeUserId()) ? dealUser.getTakeUserId() : dealUser.getId();
                variables = ActivitiUtil.setAssigneeValue(FlowConstant.BooksBuyFlowParams.USER_ZR.getValue(), assigneeValue);
                //下一环节还是自己处理
                if (assigneeValue.equals(userDto.getId())) {
                    isNextUser = true;
                    nextNodeKey = FlowConstant.BOOK_ZXZRSP;
                }
                break;
            case FlowConstant.BOOK_ZXZRSP:
                bookBuyBusiness = bookBuyBusinessRepo.findById(BookBuyBusiness_.businessId.getName(), businessKey);
                bookBuyBusiness.setOrgMLeader(userDto.getLoginName());
                bookBuyBusiness.setOrgMLeaderId(userDto.getId());
                bookBuyBusiness.setOrgMLeaderDate(new Date());
                bookBuyBusiness.setOrgMLeaderHandlesug(flowDto.getDealOption());
                bookBuyBusinessRepo.save(bookBuyBusiness);
                task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).active().singleResult();
                dealUserList = userRepo.findUserByRoleName(Constant.EnumFlowNodeGroupName.FILER.getValue());
                if (!Validate.isList(dealUserList)) {
                    return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), "请先设置【" + Constant.EnumFlowNodeGroupName.FILER.getValue() + "】角色用户！");
                }
                dealUser = dealUserList.get(0);
                assigneeValue = Validate.isString(dealUser.getTakeUserId()) ? dealUser.getTakeUserId() : dealUser.getId();
                variables = ActivitiUtil.setAssigneeValue(FlowConstant.BooksBuyFlowParams.USER_DAY.getValue(), assigneeValue);
                //下一环节还是自己处理
                if (assigneeValue.equals(userDto.getId())) {
                    isNextUser = true;
                    nextNodeKey = FlowConstant.BOOK_YSRK;
                }
                break;
            case FlowConstant.BOOK_YSRK:
                bookBuyBusiness = bookBuyBusinessRepo.findById(BookBuyBusiness_.businessId.getName(), businessKey);
                bookBuyBusiness.setFilerId(userDto.getId());
                bookBuyBusiness.setFiler(userDto.getLoginName());
                bookBuyBusiness.setFilerDate(new Date());
                bookBuyBusiness.setFilerHandlesug(flowDto.getDealOption());
                List<BookBuy> booksList = bookBuyBusiness.getBookBuyList();
                flowEndUpdateBooks(booksList);
                //bookBuyBusiness.getBookBuyList().clear();
                bookBuyBusiness.setBookBuyList(booksList);
                bookBuyBusinessRepo.save(bookBuyBusiness);
                task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).active().singleResult();
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
                        return bookDealFlow(processInstance, t, flowDto,userDto);
                    }
                }
            }
        }
        //放入腾讯通消息缓冲池
        RTXSendMsgPool.getInstance().sendReceiverIdPool(task.getId(), assigneeValue);
        return new ResultMsg(true, Constant.MsgCode.OK.getValue(), "操作成功！");
    }

    /**
     * 流程结束后更新图书库存数量、入库时间
     * @param booksList
     */
    private  void flowEndUpdateBooks(List<BookBuy> booksList){
        for(int i=0;i< booksList.size();i++){
            if(null !=booksList.get(i).getBookNumber()){
                booksList.get(i).setStoreConfirm(booksList.get(i).getBookNumber());
            }
            booksList.get(i).setStoreTime(new Date());
        }
    }


    /**
     * 月报简报流程处理
     *
     * @param processInstance
     * @param task
     * @param flowDto
     * @return
     */
    @Override
    @Transactional
    public ResultMsg monthlyDealFlow(ProcessInstance processInstance, Task task, FlowDto flowDto,UserDto userDto) {
        String businessId = processInstance.getBusinessKey(),
                assigneeValue = "";                            //流程处理人
        Map<String, Object> variables = new HashMap<>();       //流程参数
        User dealUser = null;                                  //用户
        List<User> dealUserList = null;                        //用户列表
        boolean isNextUser = false;                 //是否是下一环节处理人（主要是处理领导审批，部长审批）
        String nextNodeKey = "";                    //下一环节名称
        AddSuppLetter addSuppLetter = addSuppLetterRepo.findById(businessId);
        task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).active().singleResult();
        //环节处理人设定
        switch (task.getTaskDefinitionKey()) {
            //项目负责人填报
            case FlowConstant.MONTH_YB:
                flowDto.setDealOption("");//默认意见为空
                for (RoleDto r:userDto.getRoleDtoList() ){
                    if (r.getRoleName().equals(Constant.EnumFlowNodeGroupName.DEPT_LEADER.getValue())){
                        assigneeValue=userDto.getOrgDto().getOrgSLeader();
                        variables=ActivitiUtil.setAssigneeValue(FlowConstant.MonthlyNewsletterFlowParams.USER_FGLD.getValue(), assigneeValue);
                        variables.put(FlowConstant.MonthlyNewsletterFlowParams.MONTH_USER.getValue(), true);
                        break;
                    }else{
                        variables = ActivitiUtil.setAssigneeValue(FlowConstant.MonthlyNewsletterFlowParams.USER_BZ.getValue(), SessionUtil.getUserInfo().getOrg().getOrgDirector());
                        variables.put(FlowConstant.MonthlyNewsletterFlowParams.MONTH_USER.getValue(), false);
                    }
                }


                break;
            //部长审批
            case FlowConstant.MONTH_BZ:
                addSuppLetter.setDeptMinisterId(userDto.getId());
                addSuppLetter.setDeptMinisterName(userDto.getDisplayName());
                addSuppLetter.setDeptMinisterDate(new Date());
                addSuppLetter.setDeptMinisterIdeaContent(flowDto.getDealOption());
                addSuppLetter.setAppoveStatus(Constant.EnumState.PROCESS.getValue());
                addSuppLetterRepo.save(addSuppLetter);
                assigneeValue=userDto.getOrgDto().getOrgSLeader();
                variables = ActivitiUtil.setAssigneeValue(FlowConstant.MonthlyNewsletterFlowParams.USER_FGLD.getValue(), assigneeValue);
                //下一环节还是自己处理
                if(assigneeValue.equals(userDto.getId())){
                    isNextUser = true;
                    nextNodeKey = FlowConstant.MONTH_FG;
                }
                break;
            //分管领导审批
            case FlowConstant.MONTH_FG:
                addSuppLetter.setDeptSLeaderId(userDto.getId());
                addSuppLetter.setDeptSLeaderName(userDto.getDisplayName());
                addSuppLetter.setDeptSleaderDate(new Date());
                addSuppLetter.setDeptSLeaderIdeaContent(flowDto.getDealOption());
                addSuppLetter.setAppoveStatus(Constant.EnumState.STOP.getValue());
                addSuppLetterRepo.save(addSuppLetter);
                //查询部门领导
                dealUserList = userRepo.findUserByRoleName(Constant.EnumFlowNodeGroupName.DIRECTOR.getValue());
                if (!Validate.isList(dealUserList)) {
                    return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), "请先设置【" + Constant.EnumFlowNodeGroupName.DIRECTOR.getValue() + "】角色用户！");
                }
                assigneeValue=dealUserList.get(0).getId();
                variables = ActivitiUtil.setAssigneeValue(FlowConstant.MonthlyNewsletterFlowParams.USER_ZR.getValue(), assigneeValue);
                //下一环节还是自己处理
                if(assigneeValue.equals(userDto.getId())){
                    isNextUser = true;
                    nextNodeKey = FlowConstant.MONTH_ZR;
                }
                break;

            //主任审批
            case FlowConstant.MONTH_ZR:
                addSuppLetter.setDeptDirectorId(userDto.getId());
                addSuppLetter.setDeptDirectorName(userDto.getDisplayName());
                addSuppLetter.setDeptDirectorDate(new Date());
                addSuppLetter.setDeptDirectorIdeaContent(flowDto.getDealOption());
                addSuppLetter.setAppoveStatus(Constant.EnumState.YES.getValue());
                //判断生成序号
                int curYearMaxSeq = addSuppLetterRepo.findybMaxSeq(addSuppLetter.getFileType())+1;
                /*if(curYearMaxSeq < 1000){
                    seq = String.format("%03d", Integer.valueOf(curYearMaxSeq+1));
                }else{
                    seq = (curYearMaxSeq+1)+"";
                }*/
                addSuppLetter.setMonthlySeq(curYearMaxSeq);

                //查询年份
                String year = DateUtils.converToString(addSuppLetter.getSuppLetterTime(),"yyyy");
                //生成存档编号,年份+类型+存档年份+存档序号
                String fileNumber = year + Constant.FILE_RECORD_KEY.YD.getValue() + DateUtils.converToString(addSuppLetter.getSuppLetterTime(), "yy") + curYearMaxSeq;
                addSuppLetter.setFileCode(fileNumber);
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
            //如果下一环节还是自己
            if(isNextUser){
                List<Task> nextTaskList = taskService.createTaskQuery().processInstanceId(processInstance.getId()).taskAssignee(assigneeValue).list();
                for(Task t:nextTaskList){
                    if(nextNodeKey.equals(t.getTaskDefinitionKey())){
                        return monthlyDealFlow(processInstance,t,flowDto,userDto);
                    }
                }
            }
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
    public ResultMsg annountDealFlow(ProcessInstance processInstance, Task task, FlowDto flowDto, UserDto userDto) {
        String businessId = processInstance.getBusinessKey(),
                assigneeValue = "";                            //流程处理人
        List<User> userList = null;                 //用户列表
        User dealUser = null;
        Map<String, Object> variables = new HashMap<>();       //流程参数
        boolean isNextUser = false;                 //是否是下一环节处理人（主要是处理领导审批，部长审批）
        String nextNodeKey = "";                    //下一环节名称
        Annountment annountment = annountmentRepo.findById(Annountment_.anId.getName(), businessId);
        task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).active().singleResult();
        //环节处理人设定
        switch (task.getTaskDefinitionKey()) {
            //项目负责人填报
            case FlowConstant.ANNOUNT_TZ:
                for (RoleDto r:userDto.getRoleDtoList() ) {
                    if (r.getRoleName().equals(Constant.EnumFlowNodeGroupName.DEPT_LEADER.getValue())) {//判断是否是部长
                        dealUser = userRepo.getCacheUserById(userDto.getOrgDto().getOrgSLeader());
                        assigneeValue = Validate.isString(dealUser.getTakeUserId()) ? dealUser.getTakeUserId() : dealUser.getId();
                        variables = ActivitiUtil.setAssigneeValue(FlowConstant.AnnountMentFLOWParams.ANNOUNT_USER.getValue(), assigneeValue);
                        variables.put(FlowConstant.AnnountMentFLOWParams.ANNOUNT_USER.getValue(), true);
                        break;
                    } else {
                        String userId = userDto.getOrgDto().getOrgDirector() == null ? userDto.getId() : userDto.getOrgDto().getOrgDirector();
                        User leadUser = userRepo.getCacheUserById(userId);
                        assigneeValue = Validate.isString(leadUser.getTakeUserId()) ? leadUser.getTakeUserId() : leadUser.getId();
                        variables = ActivitiUtil.setAssigneeValue(FlowConstant.AnnountMentFLOWParams.ANNOUNT_USER.getValue(), assigneeValue);
                        variables.put(FlowConstant.AnnountMentFLOWParams.ANNOUNT_USER.getValue(), false);
                    }
                }
                break;
            //部长审批
            case FlowConstant.ANNOUNT_BZ:
                annountment.setDeptMinisterId(userDto.getId());
                annountment.setDeptMinisterName(userDto.getDisplayName());
                annountment.setDeptMinisterDate(new Date());
                annountment.setDeptMinisterIdeaContent(flowDto.getDealOption());
                annountment.setAppoveStatus(Constant.EnumState.PROCESS.getValue());
                annountmentRepo.save(annountment);
                assigneeValue = userDto.getOrgDto().getOrgSLeader();//下一环节的处理人
                variables = ActivitiUtil.setAssigneeValue(FlowConstant.AnnountMentFLOWParams.USER_FZ.getValue(), assigneeValue);
                //下一环节还是自己处理
                if (assigneeValue.equals(userDto.getId())) {
                    isNextUser = true;
                    nextNodeKey = FlowConstant.ANNOUNT_FZ;
                }
                break;
            //副主任审批
            case FlowConstant.ANNOUNT_FZ:
                annountment.setDeptSLeaderId(userDto.getId());
                annountment.setDeptSLeaderName(userDto.getDisplayName());
                annountment.setDeptSleaderDate(new Date());
                annountment.setDeptSLeaderIdeaContent(flowDto.getDealOption());
                annountment.setAppoveStatus(Constant.EnumState.STOP.getValue());
                annountmentRepo.save(annountment);
                //查询部门主任领导

                userList = userRepo.findUserByRoleName(Constant.EnumFlowNodeGroupName.DIRECTOR.getValue());
                if (!Validate.isList(userList)) {
                    return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), "请先设置【" + Constant.EnumFlowNodeGroupName.DIRECTOR.getValue() + "】角色用户！");
                }
                assigneeValue = userList.get(0).getId();//下一环节处理人
                variables = ActivitiUtil.setAssigneeValue(FlowConstant.AnnountMentFLOWParams.USER_ZR.getValue(), assigneeValue);
                //下一环节还是自己处理
                if (assigneeValue.equals(userDto.getId())) {
                    isNextUser = true;
                    nextNodeKey = FlowConstant.ANNOUNT_ZR;
                }
                break;

            //主任审批
            case FlowConstant.ANNOUNT_ZR:
                annountment.setDeptDirectorId(userDto.getId());
                annountment.setDeptDirectorName(userDto.getDisplayName());
                annountment.setDeptDirectorDate(new Date());
                annountment.setDeptDirectorIdeaContent(flowDto.getDealOption());
                annountment.setAppoveStatus(Constant.EnumState.YES.getValue());
                annountmentRepo.save(annountment);
                if (flowDto.getBusinessMap().get("AGREE") != null) {
                    if (Constant.EnumState.YES.getValue().equals(flowDto.getBusinessMap().get("AGREE"))) {//当主任同意时就发布通知公告
                        updateIssueState(annountment.getAnId(), Constant.EnumState.YES.getValue(),userDto);
                    }
                } else {
                    updateIssueState(annountment.getAnId(), Constant.EnumState.YES.getValue(),userDto);
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
            if (isNextUser) {
                List<Task> nextTaskList = taskService.createTaskQuery().processInstanceId(processInstance.getId()).taskAssignee(assigneeValue).list();
                for (Task t : nextTaskList) {
                    if (nextNodeKey.equals(t.getTaskDefinitionKey())) {
                        return annountDealFlow(processInstance, t, flowDto,userDto);
                    }
                }
            }
        }
        //放入腾讯通消息缓冲池
        RTXSendMsgPool.getInstance().sendReceiverIdPool(task.getId(), assigneeValue);
        return new ResultMsg(true, Constant.MsgCode.OK.getValue(), "操作成功！");
    }

    /**
     * 更改通知公告的发布状态（如果是已审批的，不能取消发布）
     *
     * @param ids
     * @param issueState
     */
    @Transactional
    public ResultMsg updateIssueState(String ids, String issueState,UserDto userDto) {
        ResultMsg resultMsg = new ResultMsg(true, Constant.MsgCode.OK.getValue(), "操作成功！");
        List<Annountment> bathAnnountment = new ArrayList<>();
        //是否取消发布
        boolean isCancelPublish = (Constant.EnumState.NO.getValue().equals(issueState)) ? true : false;
        List<Annountment> updateList = annountmentRepo.findByIds(Annountment_.anId.getName(), ids, null);
        if (Validate.isList(updateList)) {
            Date now = new Date();
            for (int i = 0, l = updateList.size(); i < l; i++) {
                Annountment annountment = updateList.get(i);
                if (Validate.isString(annountment.getProcessInstanceId())) {
                    resultMsg = new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), "操作失败，审批的通知公告，不能对其进行修改和删除操作！");
                    break;
                }
                //如果是发布
                if (!isCancelPublish) {
                    if (!Validate.isString(annountment.getIssueUser())) {
                        annountment.setIssueUser(userDto.getDisplayName());
                    }
                    if (!Validate.isObject(annountment.getIssueDate())) {
                        annountment.setIssueDate(now);
                    }
                }
                annountment.setModifiedBy(userDto.getId());
                annountment.setModifiedDate(now);
                annountment.setIssue(issueState);

                bathAnnountment.add(annountment);
            }
        }
        if (resultMsg.isFlag() && Validate.isList(bathAnnountment)) {
            annountmentRepo.bathUpdate(bathAnnountment);
        }

        return resultMsg;
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
    public ResultMsg suppletterDealFlow(ProcessInstance processInstance, Task task, FlowDto flowDto,UserDto userDto) {
        String businessId = processInstance.getBusinessKey(),
                assigneeValue = "";                            //流程处理人
        Map<String, Object> variables = new HashMap<>();       //流程参数
        User dealUser = null;                                  //用户
        List<User> dealUserList = null;                        //用户列表
        boolean isNextUser = false;                 //是否是下一环节处理人（主要是处理领导审批，部长审批）
        String nextNodeKey = "";                    //下一环节名称
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
                    addSuppLetter.setAppoveStatus(Constant.EnumState.STOP.getValue());
                    //下一环节还是自己处理
                    if(assigneeValue.equals(userDto.getId())){
                        isNextUser = true;
                        nextNodeKey = FlowConstant.FLOW_SPL_FGLD_SP;
                    }
                    //有分支，则跳转到领导会签环节
                } else {
                    dealUserList = signBranchRepo.findAssistOrgDirector(addSuppLetter.getBusinessId());
                    assigneeValue = buildUser(dealUserList);
                    variables.put(FlowConstant.SignFlowParams.USER_HQ_LIST.getValue(), StringUtil.getSplit(assigneeValue, ","));
                    variables.put(FlowConstant.FlowParams.FGLD_FZ.getValue(), false);            //设置分支条件
                    //1表示到领导会签
                    addSuppLetter.setAppoveStatus(Constant.EnumState.PROCESS.getValue());
                }
                addSuppLetter.setDeptMinisterId(userDto.getId());
                addSuppLetter.setDeptMinisterName(userDto.getDisplayName());
                addSuppLetter.setDeptMinisterDate(new Date());
                addSuppLetter.setDeptMinisterIdeaContent(flowDto.getDealOption());

                addSuppLetterRepo.save(addSuppLetter);
                break;
            //领导会签
            case FlowConstant.FLOW_SPL_LD_HQ:
                dealUser = signBranchRepo.findMainSLeader(addSuppLetter.getBusinessId());
                assigneeValue = Validate.isString(dealUser.getTakeUserId()) ? dealUser.getTakeUserId() : dealUser.getId();
                variables.put(FlowConstant.FlowParams.USER_FGLD.getValue(), assigneeValue);

                //下一环节还是自己处理
                if(assigneeValue.equals(userDto.getId())){
                    isNextUser = true;
                    nextNodeKey = FlowConstant.FLOW_SPL_FGLD_SP;
                }
                String signString = "";
                //旧的会签记录
                String oldMsg = addSuppLetter.getLeaderSignIdeaContent();
                if (Validate.isString(oldMsg) && !"null".equals(oldMsg)) {
                    signString += oldMsg+"<br>";
                }
                signString += "<span style='float: left'>"+flowDto.getDealOption()+"</span>" + "<br>" + userDto.getDisplayName() + "<br>" + DateUtils.converToString(new Date(), null);
                addSuppLetter.setLeaderSignIdeaContent(signString);
                //2表示到分管领导会签
                addSuppLetterRepo.save(addSuppLetter);
                break;

            //分管领导审批
            case FlowConstant.FLOW_SPL_FGLD_SP:
                //如果没有生成文件字号或者生成错的文件字号，则重新生成
                if (!Validate.isString(addSuppLetter.getFilenum()) || !addSuppLetter.getFilenum().contains(Constant.ADDSUPPER_PREFIX)) {
                    addSuppLetterService.initFileNum(addSuppLetter);
                }
                if(!Validate.isString(addSuppLetter.getFilenum())){
                    return ResultMsg.error("无法生成文件字号，请联系管理员查看！");
                }
                addSuppLetter.setDeptSLeaderId(userDto.getId());
                addSuppLetter.setDeptSLeaderName(userDto.getDisplayName());
                addSuppLetter.setDeptSleaderDate(new Date());
                addSuppLetter.setDeptSLeaderIdeaContent(flowDto.getDealOption());
                addSuppLetter.setAppoveStatus(Constant.EnumState.YES.getValue());
                addSuppLetterRepo.save(addSuppLetter);

                //更新项目和工作方案是否有拟补充资料函字段信息
                //updateSuppLetterState(addSuppLetter.getBusinessId(),addSuppLetter.getBusinessType(),addSuppLetter.getDisapDate());
                //如果是项目，则更新项目补充资料函状态
                if(Validate.isString(addSuppLetter.getBusinessType()) && Constant.BusinessType.SIGN.getValue().equals(addSuppLetter.getBusinessType())){
                    signRepo.updateSuppLetterState(addSuppLetter.getBusinessId(), Constant.EnumState.YES.getValue(),addSuppLetter.getDisapDate());
                    workProgramRepo.updateSuppLetterState(addSuppLetter.getBusinessId(), Constant.EnumState.YES.getValue(),addSuppLetter.getDisapDate());
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
                        return suppletterDealFlow(processInstance,t,flowDto,userDto);
                    }
                }
            }
        }
        //放入腾讯通消息缓冲池
        RTXSendMsgPool.getInstance().sendReceiverIdPool(task.getId(), assigneeValue);
        return new ResultMsg(true, Constant.MsgCode.OK.getValue(), "操作成功！");
    }

    @Override
    public String getMainPriUserId(String signid, List<AgentTask> agentTaskList,String nodeKey) {
        User dealUser = signPrincipalService.getMainPriUser(signid);
        return userService.getTaskDealId(dealUser, agentTaskList,nodeKey);
    }


    /**
     * 取回流程
     *
     * @param taskId      当前任务ID
     * @param activityId  取回节点ID
     * @param businessKey 删除工作方案的singId
     * @return ResultMsg
     */
    @Override
    public ResultMsg callBackProcess(String taskId, String activityId, String businessKey, boolean allBranch,UserDto userDto) throws Exception {
        if (!Validate.isString(activityId)) {
            throw new Exception("目标节点ID为空！");
        }
        ProcessInstance ProcessInstance = findProcessInstanceByTaskId(taskId);
        taskService.addComment(taskId, ProcessInstance.getId(), "【" + userDto.getDisplayName() + "】重新分办");    //添加处理信息
        if (allBranch) {
            // 如果是删除所有分支，查找所有并行任务节点，同时取回
            List<Task> taskList = findTaskListByKey(ProcessInstance.getId());
            for (Task task : taskList) {
                if (task.getId().equals(taskId)) {
                    //取回项目流程
                    commitProcess(task.getId(), null, activityId);
                } else {
                    //删除流程实例
                    deleteTask(task.getId(), task.getExecutionId());
                }
            }
        } else {
            //如果只是取回当前任务
            Task task = taskService.createTaskQuery().taskId(taskId).active().singleResult();
            if (task != null) {
                commitProcess(task.getId(), null, activityId);
            } else {
                return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), "操作失败，该任务已提交！请重新刷新再试!");
            }
        }
        return new ResultMsg(true, Constant.MsgCode.OK.getValue(), "操作成功！");
    }




    /**
     * 根据任务ID获取对应的流程实例
     *
     * @param taskId 任务ID
     * @return
     * @throws Exception
     */
    private ProcessInstance findProcessInstanceByTaskId(String taskId)
            throws Exception {
        // 找到流程实例
        ProcessInstance processInstance = runtimeService
                .createProcessInstanceQuery().processInstanceId(findTaskById(taskId).getProcessInstanceId()).singleResult();
        if (processInstance == null) {
            throw new Exception("流程实例未找到!");
        }
        return processInstance;
    }

    /**
     * 根据流程实例ID查询所有任务集合
     *
     * @param processInstanceId
     * @return
     */
    @Override
    public List<Task> findTaskListByKey(String processInstanceId) {
        return taskService.createTaskQuery().processInstanceId(processInstanceId).list();
    }


    /**
     * @param taskId     当前任务ID
     * @param variables  流程变量
     * @param activityId 流程转向执行任务节点ID<br>
     *                   此参数为空，默认为提交操作
     * @throws Exception
     */
    private void commitProcess(String taskId, Map<String, Object> variables, String activityId) throws Exception {
        if (variables == null) {
            variables = new HashMap<String, Object>();
        }
        // 跳转节点为空，默认提交操作
        if (!Validate.isString(activityId)) {
            taskService.complete(taskId, variables);
        } else {// 流程转向操作
            turnTransition(taskId, activityId, variables);
        }
    }

    /**
     * 删除流程任务实例
     *
     * @param taskId      任务节点ID
     * @param executionId 流程实例节点ID
     */
    @Override
    public void deleteTask(String taskId, String executionId) {
        //直接执行SQL
        //DELETE FROM act_ru_identitylink WHERE TASK_ID_='135068'(act_ru_task主键)
        //流程实例,通过EXECUTION_ID_字段和act_ru_execution关联
        //DELETE FROM act_ru_task WHERE ID_='135068'
        //任务节点表
        //DELETE FROM act_ru_execution WHERE ID_='135065'
        Session session = sessionFactory.getCurrentSession();
        StringBuffer stringBuffer = new StringBuffer();
        NativeQuery nativeQuery = null;

        stringBuffer.append("DELETE FROM act_ru_identitylink WHERE TASK_ID_=:taskId");
        nativeQuery = session.createNativeQuery(stringBuffer.toString());
        nativeQuery.setParameter("taskId", taskId).executeUpdate();

        stringBuffer.setLength(0);
        stringBuffer.append("DELETE FROM act_ru_t" +
                "" +
                "" +
                "" +
                "ask WHERE ID_=:taskId");
        nativeQuery = session.createNativeQuery(stringBuffer.toString());
        nativeQuery.setParameter("taskId", taskId).executeUpdate();

        stringBuffer.setLength(0);
        stringBuffer.append("DELETE FROM act_ru_execution WHERE ID_=:executionId");
        nativeQuery = session.createNativeQuery(stringBuffer.toString());
        nativeQuery.setParameter("executionId", executionId).executeUpdate();

    }


    /**
     * 根据任务ID获得任务实例
     *
     * @param taskId 任务ID
     * @return
     * @throws Exception
     */
    private TaskEntity findTaskById(String taskId) throws Exception {
        TaskEntity task = (TaskEntity) taskService.createTaskQuery().taskId(taskId).singleResult();
        if (task == null) {
            throw new Exception("任务实例未找到!");
        }
        return task;
    }


    /**
     * 流程转向操作
     *
     * @param taskId     当前任务ID
     * @param activityId 目标节点任务ID
     * @param variables  流程变量
     * @throws Exception
     */
    private void turnTransition(String taskId, String activityId,
                                Map<String, Object> variables) throws Exception {
        // 当前节点
        ActivityImpl currActivity = findActivitiImpl(taskId, null);
        // 清空当前流向
        List<PvmTransition> oriPvmTransitionList = clearTransition(currActivity);

        // 创建新流向
        TransitionImpl newTransition = currActivity.createOutgoingTransition();
        // 目标节点
        ActivityImpl pointActivity = findActivitiImpl(taskId, activityId);
        // 设置新流向的目标节点
        newTransition.setDestination(pointActivity);

        // 执行转向任务
        taskService.complete(taskId, variables);
        // 删除目标节点新流入
        pointActivity.getIncomingTransitions().remove(newTransition);

        // 还原以前流向
        restoreTransition(currActivity, oriPvmTransitionList);
    }


    /**
     * 根据任务ID和节点ID获取活动节点 <br>
     *
     * @param taskId     任务ID
     * @param activityId 活动节点ID <br>
     *                   如果为null或""，则默认查询当前活动节点 <br>
     *                   如果为"end"，则查询结束节点 <br>
     * @return
     * @throws Exception
     */
    private ActivityImpl findActivitiImpl(String taskId, String activityId)
            throws Exception {
        // 取得流程定义
        ProcessDefinitionEntity processDefinition = findProcessDefinitionEntityByTaskId(taskId);

        // 获取当前活动节点ID
        if (!Validate.isString(activityId)) {
            activityId = findTaskById(taskId).getTaskDefinitionKey();
        }

        // 根据流程定义，获取该流程实例的结束节点
        if (activityId.toUpperCase().equals("END")) {
            for (ActivityImpl activityImpl : processDefinition.getActivities()) {
                List<PvmTransition> pvmTransitionList = activityImpl
                        .getOutgoingTransitions();
                if (pvmTransitionList.isEmpty()) {
                    return activityImpl;
                }
            }
        }

        // 根据节点ID，获取对应的活动节点
        ActivityImpl activityImpl = ((ProcessDefinitionImpl) processDefinition).findActivity(activityId);

        return activityImpl;
    }


    /**
     * 清空指定活动节点流向
     *
     * @param activityImpl 活动节点
     * @return 节点流向集合
     */
    private List<PvmTransition> clearTransition(ActivityImpl activityImpl) {
        // 存储当前节点所有流向临时变量
        List<PvmTransition> oriPvmTransitionList = new ArrayList<PvmTransition>();
        // 获取当前节点所有流向，存储到临时变量，然后清空
        List<PvmTransition> pvmTransitionList = activityImpl
                .getOutgoingTransitions();
        for (PvmTransition pvmTransition : pvmTransitionList) {
            oriPvmTransitionList.add(pvmTransition);
        }
        pvmTransitionList.clear();

        return oriPvmTransitionList;
    }


    /**
     * 还原指定活动节点流向
     *
     * @param activityImpl         活动节点
     * @param oriPvmTransitionList 原有节点流向集合
     */
    private void restoreTransition(ActivityImpl activityImpl,
                                   List<PvmTransition> oriPvmTransitionList) {
        // 清空现有流向
        List<PvmTransition> pvmTransitionList = activityImpl
                .getOutgoingTransitions();
        pvmTransitionList.clear();
        // 还原以前流向
        for (PvmTransition pvmTransition : oriPvmTransitionList) {
            pvmTransitionList.add(pvmTransition);
        }
    }

    /**
     * 根据任务ID获取流程定义
     *
     * @param taskId 任务ID
     * @return
     * @throws Exception
     */
    private ProcessDefinitionEntity findProcessDefinitionEntityByTaskId(String taskId) throws Exception {
        // 取得流程定义
        ProcessDefinitionEntity processDefinition = (ProcessDefinitionEntity) ((RepositoryServiceImpl) repositoryService)
                .getDeployedProcessDefinition(findTaskById(taskId).getProcessDefinitionId());

        if (processDefinition == null) {
            throw new Exception("流程定义未找到!");
        }

        return processDefinition;
    }

}