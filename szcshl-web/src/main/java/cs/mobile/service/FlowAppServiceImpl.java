package cs.mobile.service;

import com.alibaba.fastjson.JSON;
import cs.common.Constant;
import cs.common.FlowConstant;
import cs.common.ResultMsg;
import cs.common.utils.*;
import cs.domain.expert.ExpertReview;
import cs.domain.expert.ExpertReview_;
import cs.domain.expert.ExpertSelected_;
import cs.domain.project.*;
import cs.domain.sys.OrgDept;
import cs.domain.sys.User;
import cs.domain.sys.User_;
import cs.model.flow.FlowDto;
import cs.model.sys.UserDto;
import cs.repository.repositoryImpl.expert.ExpertRepo;
import cs.repository.repositoryImpl.expert.ExpertReviewRepo;
import cs.repository.repositoryImpl.expert.ExpertSelConditionRepo;
import cs.repository.repositoryImpl.expert.ExpertSelectedRepo;
import cs.repository.repositoryImpl.external.DeptRepo;
import cs.repository.repositoryImpl.flow.RuProcessTaskRepo;
import cs.repository.repositoryImpl.flow.RuTaskRepo;
import cs.repository.repositoryImpl.meeting.RoomBookingRepo;
import cs.repository.repositoryImpl.project.*;
import cs.repository.repositoryImpl.sys.*;
import cs.service.external.OfficeUserService;
import cs.service.flow.FlowService;
import cs.service.project.*;
import cs.service.rtx.RTXSendMsgPool;
import cs.service.sys.SysConfigService;
import cs.service.sys.WorkdayService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

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



    @Override
    @Transactional
    public ResultMsg dealFlow(ProcessInstance processInstance, Task task, FlowDto flowDto,UserDto userDto) {
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
                taskService.complete(task.getId(), ActivitiUtil.setAssigneeValue(FlowConstant.SignFlowParams.USER_ZHB.getValue(), userDto.getId()));

                sign = signRepo.findById(Sign_.signid.getName(), signid);
                if (!Validate.isString(sign.getLeaderName())) {
                    return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), "操作失败，请先设置默认办理部门！");
                }
                task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).active().singleResult();
             /*   taskService.addComment(task.getId(), processInstance.getId(), sign.getComprehensivehandlesug());    //综合部拟办意见*/
                //完成综合部审批，查询是否有代办
                dealUser = userRepo.findById(User_.id.getName(), sign.getLeaderId());
                assigneeValue = Validate.isString(dealUser.getTakeUserId()) ? dealUser.getTakeUserId() : dealUser.getId();
                variables = ActivitiUtil.setAssigneeValue(FlowConstant.SignFlowParams.USER_FGLD.getValue(), assigneeValue);
                break;

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
                        return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), "协办部门最多只能选择3个！");
                    }
                    for (int i = 2, l = (assistOrgIdList.size() + 2); i < l; i++) {
                        businessId = assistOrgIdList.get(i - 2);
                        SignBranch signBranch = new SignBranch(signid, String.valueOf(i), Constant.EnumState.YES.getValue(), Constant.EnumState.NO.getValue(), Constant.EnumState.NO.getValue(), businessId, Constant.EnumState.NO.getValue());
                        saveBranchList.add(signBranch);
                        //判断是否有部门负责人
                        orgDept = orgDeptRepo.findOrgDeptById(businessId);
                        if (orgDept == null || !Validate.isString(orgDept.getDirectorID())) {
                            return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), "请设置" + orgDept.getName() + "的部门负责人！");
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
                sign.setLeaderId(userDto.getId());
                sign.setLeaderName(userDto.getDisplayName());
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
                if (Constant.EnumState.YES.getValue().equals(sign.getIsassistflow())) {
                    if (flowDto.getBusinessMap().get("PRINCIPAL") == null) {
                        return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), "请选择项目负责人！");
                    }
                    signPriList = JSON.parseArray(flowDto.getBusinessMap().get("PRINCIPAL").toString(), SignPrincipal.class);
                    if (!Validate.isList(signPriList)) {
                        return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), "请选择项目负责人！");
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
                            return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), "请选择第一负责人！");
                        }
                        businessId = flowDto.getBusinessMap().get("M_USER_ID").toString();
                        //查询是否有代办
                        dealUser = userRepo.getCacheUserById(businessId);
                        assigneeValue = Validate.isString(dealUser.getTakeUserId()) ? dealUser.getTakeUserId() : dealUser.getId();

                        //设置项目负责人
                        signPriList = new ArrayList<>();
                        //String signId, String userId, String flowBranch, String userType, Integer sort, String isMainUser
                        SignPrincipal mainPri = new SignPrincipal(signid, dealUser.getId(), branchIndex, "", null, Constant.EnumState.YES.getValue());
                        signPriList.add(mainPri);

                        //更改项目信息
                        sign.setMinisterhandlesug(flowDto.getDealOption());
                        sign.setMinisterDate(new Date());
                        sign.setMinisterId(userDto.getId());
                        sign.setMinisterName(userDto.getDisplayName());

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
                            SignPrincipal secondPri = new SignPrincipal(signid, user.getId(), branchIndex, "", null, Constant.EnumState.NO.getValue());
                            signPriList.add(secondPri);
                        }
                    } else {
                        //分支流程必须要选择第二负责人
                        if (!FlowConstant.SignFlowParams.BRANCH_INDEX1.getValue().equals(branchIndex)) {
                            return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), "请选择项目负责人！");
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
                if (!signPrincipalService.isMainPri(userDto.getId(), signid)) {
                    return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), "您不是第一负责人，不能进行下一步操作！");
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
                if (Constant.EnumState.YES.getValue().equals(businessId)) {
                    wk = workProgramRepo.findBySignIdAndBranchId(signid, branchIndex);
                    //如果做工作方案，则要判断该分支工作方案是否完成
                    if (!Validate.isObject(wk) || !Validate.isString(wk.getId())) {
                        return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), "您还没有完成工作方案，不能进行下一步操作！");
                    }

                    //是否专家评审
                    boolean isExpertReview = Constant.MergeType.REVIEW_MEETING.getValue().equals(wk.getReviewType()) || Constant.MergeType.REVIEW_LEETER.getValue().equals(wk.getReviewType());
                    //是否单个评审
                    boolean isSigle = Constant.MergeType.REVIEW_SIGNLE.getValue().equals(wk.getIsSigle());
                    //是否合并评审主项目
                    boolean isMergeMain = (Constant.MergeType.REVIEW_MERGE.getValue().equals(wk.getIsSigle()) && Constant.EnumState.YES.getValue().equals(wk.getIsMainProject()));
                    //是否主分支
                    boolean isMainBranch = FlowConstant.SignFlowParams.BRANCH_INDEX1.getValue().equals(branchIndex);

                    //判断是否预定了会议室和选择了专家（只对主分支判断，因为协办分支以主分支为准，当然也可以自己选，但是不是强制要求）
                    if (isMainBranch) {
                        //单个评审或者合并评审主项目；如果是专家评审会，则要选择专家和会议室
                        boolean needCheck = isExpertReview && (isSigle || isMergeMain);
                        if (needCheck) {
                            if (expertRepo.countByBusinessId(wk.getId()) == 0) {
                                return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), "您选择的评审方式是【" + wk.getReviewType() + "】，但是还没有选择专家，请先选择专家！");
                            }
                            if (Constant.MergeType.REVIEW_MEETING.getValue().equals(wk.getReviewType()) && !roomBookingRepo.isHaveBookMeeting(wk.getId())) {
                                return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), "您选择的评审方式是【" + wk.getReviewType() + "】，但是还没有选择会议室，请先预定会议室！");
                            }
                        }
                        //如果没有合并其他项目，则不准提交
                        if (isMergeMain && !signMergeRepo.isHaveMerge(signid, Constant.MergeType.WORK_PROGRAM.getValue())) {
                            return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), "工作方案您选择的是合并评审主项目，您还没有设置关联项目，不能提交到下一步！");
                        }
                        //如果合并评审次项目没提交，不能进行下一步操作
                        if (!signRepo.isMergeSignEndWP(signid)) {
                            return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), "合并评审次项目还未提交审批，主项目不能提交审批！");
                        }
                    }

                    //查询部门领导
                    orgDept = orgDeptRepo.queryBySignBranchId(signid, branchIndex);
                    if (orgDept == null || !Validate.isString(orgDept.getDirectorID())) {
                        return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), "请设置该分支的部门负责人！");
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
                    //更改预定会议室状态
                    roomBookingRepo.updateStateByBusinessId(wk.getId(), Constant.EnumState.PROCESS.getValue());
                    //完成分支工作方案
                    signBranchRepo.finishWP(signid, wk.getBranchId());
                    //不做工作方案
                } else {
                    dealUser = signPrincipalService.getMainPriUser(signid);
                    if (dealUser == null) {
                        return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), "项目还没分配主负责人，不能进行下一步操作！请联系主办部门进行负责人分配！");
                    }
                    assigneeValue = Validate.isString(dealUser.getTakeUserId()) ? dealUser.getTakeUserId() : dealUser.getId();
                    variables.put(FlowConstant.SignFlowParams.USER_FZR1.getValue(), assigneeValue);

                    //更改状态
                    signBranchRepo.isNeedWP(signid, branchIndex, Constant.EnumState.NO.getValue());

                    //注意：1、项目建议书、可研阶段一定要做工作方案；
                    // 2、主分支跳转，则必须要所有协办分支都完成才能跳转。
                    if (FlowConstant.SignFlowParams.BRANCH_INDEX1.getValue().equals(branchIndex)) {
                        if ((signBranchRepo.countBranch(signid) > 1) && signBranchRepo.assistFlowFinish(signid)) {
                            return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), "协办分支还没处理完，不能直接进行发文操作！");
                        }
                        sign = signRepo.findById(Sign_.signid.getName(), signid);
                        if ((Constant.STAGE_SUG.equals(sign.getReviewstage()) || Constant.STAGE_STUDY.equals(sign.getReviewstage()))
                                && !signBranchRepo.isHaveWP(signid)) {
                            return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), "【项目建议书】和【可行性研究报告】阶段必须要做工作方案！");
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
                if (userDto.getOrgDto() == null) {
                    return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), "你还没设置所属部门！");
                }
                if (!Validate.isString(userDto.getOrgDto().getOrgSLeader())) {
                    return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), "请先设置该部门的分管领导！");
                }
                //更改工作方案信息
                wk = workProgramRepo.findBySignIdAndBranchId(signid, branchIndex);
                wk.setMinisterSuggesttion(flowDto.getDealOption());
                wk.setMinisterDate(new Date());
                wk.setMinisterName(userDto.getDisplayName());

                //如果是主办流程，要判断是否有合并评审方案，有则跟着主项目一起办理
                if (FlowConstant.SignFlowParams.BRANCH_INDEX1.getValue().equals(branchIndex)) {
                    if (Constant.MergeType.REVIEW_MERGE.getValue().equals(wk.getIsSigle()) && Constant.EnumState.YES.getValue().equals(wk.getIsMainProject())) {
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
                dealUser = userRepo.getCacheUserById(userDto.getOrgDto().getOrgSLeader());
                assigneeValue = Validate.isString(dealUser.getTakeUserId()) ? dealUser.getTakeUserId() : dealUser.getId();
                //下一环节还是自己
                if (assigneeValue.equals(userDto.getId())) {
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
                    return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), "项目还没分配主负责人，不能进行下一步操作！请联系主办部门进行负责人分配！");
                }
                assigneeValue = Validate.isString(dealUser.getTakeUserId()) ? dealUser.getTakeUserId() : dealUser.getId();
                variables.put(FlowConstant.SignFlowParams.USER_FZR1.getValue(), assigneeValue);

                //更改工作方案审核信息
                wk = workProgramRepo.findBySignIdAndBranchId(signid, branchIndex);

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
                wk.setLeaderName(userDto.getDisplayName());
                workProgramRepo.save(wk);
                //完成分支的工作方案
                signBranchRepo.finishBranch(signid, branchIndex);
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
            //发文申请
            case FlowConstant.FLOW_SIGN_FW:
                //如果有专家评审费，则要先办理专家评审费
                if (expertReviewRepo.isHaveEPReviewCost(signid)) {
                    ExpertReview expertReview2 = expertReviewRepo.findById(ExpertReview_.businessId.getName(), signid);
                    if (expertReview2.getPayDate() == null || expertReview2.getTotalCost() == null) {
                        return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), "您还没完成专家评审费发放，不能进行下一步操作！");
                    }
                }
                //修改第一负责人意见
                businessId = flowDto.getBusinessMap().get("DIS_ID").toString();
                dp = dispatchDocRepo.findById(DispatchDoc_.id.getName(), businessId);
                dp.setMianChargeSuggest(flowDto.getDealOption() + "<br>" + userDto.getDisplayName() + " &nbsp; " + DateUtils.converToString(new Date(), "yyyy年MM月dd日"));
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
                    return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), "请选择同意或者不同意！");
                }
                //修改第二负责人意见
                businessId = flowDto.getBusinessMap().get("DIS_ID").toString();
                dp = dispatchDocRepo.findById(DispatchDoc_.id.getName(), businessId);
                String optionString = Validate.isString(dp.getSecondChargeSuggest()) ? (dp.getSecondChargeSuggest() + "<br>") : "";
                dp.setSecondChargeSuggest(optionString + flowDto.getDealOption() + "              " + userDto.getDisplayName() + " 日期：" + DateUtils.converToString(new Date(), "yyyy年MM月dd日"));
                dispatchDocRepo.save(dp);

                //如果同意
                if (Constant.EnumState.YES.getValue().equals(flowDto.getBusinessMap().get("AGREE").toString())) {
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
                    return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), "请选择同意或者不同意！");
                }
                //如果同意，则到主办部长审批
                if (Constant.EnumState.YES.getValue().equals(flowDto.getBusinessMap().get("AGREE").toString())) {
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
                    if (assigneeValue.equals(userDto.getId())) {
                        isNextUser = true;
                        nextNodeKey = FlowConstant.FLOW_SIGN_FGLD_QRFW;
                    }
                }

                //修改发文信息
                businessId = flowDto.getBusinessMap().get("DIS_ID").toString();
                dp = dispatchDocRepo.findById(DispatchDoc_.id.getName(), businessId);
                dp.setMinisterSuggesttion(flowDto.getDealOption());
                dp.setMinisterDate(new Date());
                dp.setMinisterName(userDto.getDisplayName());
                dispatchDocRepo.save(dp);
                break;
            //协办分管领导审批发文
            case FlowConstant.FLOW_SIGN_FGLD_QRFW_XB:
                if (flowDto.getBusinessMap().get("AGREE") == null || !Validate.isString(flowDto.getBusinessMap().get("AGREE").toString())) {
                    return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), "请选择同意或者不同意！");
                }
                //如果同意
                if (Constant.EnumState.YES.getValue().equals(flowDto.getBusinessMap().get("AGREE").toString())) {
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
                userList = userRepo.findUserByRoleName(Constant.EnumFlowNodeGroupName.DIRECTOR.getValue());
                if (!Validate.isList(userList)) {
                    return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), "请先设置【" + Constant.EnumFlowNodeGroupName.DIRECTOR.getValue() + "】角色用户！");
                }
                dealUser = userList.get(0);
                assigneeValue = Validate.isString(dealUser.getTakeUserId()) ? dealUser.getTakeUserId() : dealUser.getId();
                variables.put(FlowConstant.SignFlowParams.USER_ZR.getValue(), assigneeValue);

                //修改发文信息
                businessId = flowDto.getBusinessMap().get("DIS_ID").toString();
                dp = dispatchDocRepo.findById(DispatchDoc_.id.getName(), businessId);
                dp.setViceDirectorSuggesttion(flowDto.getDealOption());
                dp.setViceDirectorDate(new Date());
                dp.setViceDirectorName(userDto.getDisplayName());
                dispatchDocRepo.save(dp);
                //下一环节还是自己处理
                if (assigneeValue.equals(userDto.getId())) {
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
                dp.setDirectorName(userDto.getDisplayName());
                dispatchDocRepo.save(dp);

                //项目负责人生成发文编号
                variables = buildMainPriUser(variables, signid, assigneeValue);
                break;
            //生成发文编号
            case FlowConstant.FLOW_SIGN_FWBH:
                businessId = flowDto.getBusinessMap().get("DIS_ID").toString();
                dp = dispatchDocRepo.findById(DispatchDoc_.id.getName(), businessId);
                if (!Validate.isString(dp.getFileNum())) {
                    return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), "操作失败，该项目还没有发文编号，不能进行下一步操作！");
                }
                sign = signRepo.findById(Sign_.signid.getName(), signid);
                //如果有评审费或者是协审流程，则给财务部办理，没有，则直接到归档环节
                if (expertReviewRepo.isHaveEPReviewCost(signid) || Constant.EnumState.YES.getValue().equals(sign.getIsassistflow())) {
                    userList = userRepo.findUserByRoleName(Constant.EnumFlowNodeGroupName.FINANCIAL.getValue());
                    if (!Validate.isList(userList)) {
                        return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), "请先设置【" + Constant.EnumFlowNodeGroupName.FINANCIAL.getValue() + "】角色用户！");
                    }
                    assigneeValue = buildUser(userList);
                    variables.put(FlowConstant.SignFlowParams.HAVE_ZJPSF.getValue(), true);
                    variables.put(FlowConstant.SignFlowParams.USER_CW.getValue(), assigneeValue);

                    //没有评审费，则直接到归档环节(还是当前人处理)
                } else {
                    variables.put(FlowConstant.SignFlowParams.HAVE_ZJPSF.getValue(), false);
                    assigneeValue = userDto.getId();
                    variables.put(FlowConstant.SignFlowParams.USER_FZR1.getValue(), assigneeValue);
                    //更改已发送存档字段值
                    sign.setIsSendFileRecord(Constant.EnumState.YES.getValue());
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
                sign.setIsSendFileRecord(Constant.EnumState.YES.getValue());
                signRepo.save(sign);
                variables = buildMainPriUser(variables, signid, assigneeValue);
                break;

            //第一负责人归档
            case FlowConstant.FLOW_SIGN_GD:
                //如果没有完成专家评分，则不可以提交到下一步
                if (!expertReviewRepo.isFinishEPGrade(signid)) {
                    return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), "您还未对专家进行评分,不能提交到下一步操作！");
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
                        userList = userRepo.findUserByRoleName(Constant.EnumFlowNodeGroupName.FILER.getValue());
                        if (!Validate.isList(userList)) {
                            return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), "请先设置【" + Constant.EnumFlowNodeGroupName.FILER.getValue() + "】角色用户！");
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
                userList = userRepo.findUserByRoleName(Constant.EnumFlowNodeGroupName.FILER.getValue());
                if (!Validate.isList(userList)) {
                    return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), "请先设置【" + Constant.EnumFlowNodeGroupName.FILER.getValue() + "】角色用户！");
                }
                dealUser = userList.get(0);
                assigneeValue = Validate.isString(dealUser.getTakeUserId()) ? dealUser.getTakeUserId() : dealUser.getId();
                variables.put(FlowConstant.SignFlowParams.USER_QRGD.getValue(), assigneeValue);
                //更新项目第二负责人
                sign = signRepo.findById(Sign_.signid.getName(), signid);
                sign.setSecondPriUser(userDto.getId());
                signRepo.save(sign);

                //第二负责人确认
                businessId = flowDto.getBusinessMap().get("GD_ID").toString();
                fileRecord = fileRecordRepo.findById(FileRecord_.fileRecordId.getName(), businessId);
                fileRecord.setProjectTwoUser(userDto.getDisplayName());
                //发送存档日期为第二负责人审批意见后的日期，
                fileRecord.setSendStoreDate(new Date());
                fileRecordRepo.save(fileRecord);

                break;
            //确认归档
            case FlowConstant.FLOW_SIGN_QRGD:
                businessId = flowDto.getBusinessMap().get("GD_ID").toString();
                fileRecord = fileRecordRepo.findById(FileRecord_.fileRecordId.getName(), businessId);
                sign = signRepo.findById(Sign_.signid.getName(), signid);
                fileRecord.setFileDate(new Date());
                fileRecord.setSignUserid(userDto.getId());
                fileRecord.setSignUserName(userDto.getDisplayName());
                //纸质文件接受日期 ：为归档员陈春燕确认的归档日期
                //设置归档编号
                if (!Validate.isString(fileRecord.getFileNo())) {
                    String fileNumValue = "";
                    int maxSeq = fileRecordService.findCurMaxSeq(fileRecord.getFileDate());
                    maxSeq = maxSeq + 1;
                    if (maxSeq < 1000) {
                        fileNumValue = String.format("%03d", maxSeq);
                    } else {
                        fileNumValue = String.valueOf(maxSeq);
                    }
                    //设置本次的发文序号
                    fileRecord.setFileSeq(maxSeq);
                    //归档编号=发文年份+档案类型+存档年份+存档顺序号
                    fileNumValue = DateUtils.converToString(sign.getExpectdispatchdate(), "yyyy") + ProjectUtils.getFileRecordTypeByStage(sign.getReviewstage())
                            + DateUtils.converToString(fileRecord.getFileDate(), "yy") + fileNumValue;
                    fileRecord.setFileNo(fileNumValue);
                }
                fileRecord.setPageDate(new Date());
                fileRecordRepo.save(fileRecord);

                //收文这边也要更新归档编号
                sign.setFilenum(fileRecord.getFileNo());
                //更改项目状态
                sign.setSignState(Constant.EnumState.YES.getValue());
                sign.setProcessState(Constant.SignProcessState.FINISH.getValue());
                signRepo.save(sign);
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
                        ResultMsg returnMsg = dealFlow(processInstance, t, flowDto,userDto);
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

        return new ResultMsg(true, Constant.MsgCode.OK.getValue(), "操作成功！");
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
     * 获取主分支的分管领导
     *
     * @param signid
     */
    private String getMainSLeader(String signid) {
        OrgDept orgDept = orgDeptRepo.queryBySignBranchId(signid, FlowConstant.SignFlowParams.BRANCH_INDEX1.getValue());
        User dealUser = userRepo.getCacheUserById(orgDept.getsLeaderID());
        return Validate.isString(dealUser.getTakeUserId()) ? dealUser.getTakeUserId() : dealUser.getId();
    }
}