package cs.service.flow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cs.domain.project.WorkProgram;
import cs.domain.sys.OrgDept;
import cs.repository.repositoryImpl.expert.ExpertReviewRepo;
import cs.repository.repositoryImpl.project.*;
import cs.repository.repositoryImpl.sys.OrgDeptRepo;
import cs.repository.repositoryImpl.sys.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cs.common.constants.Constant;
import cs.common.constants.FlowConstant;
import cs.common.utils.BeanCopierUtils;
import cs.common.utils.SessionUtil;
import cs.common.utils.Validate;
import cs.domain.sys.User;
import cs.model.sys.UserDto;
import cs.service.project.SignPrincipalService;
import cs.service.sys.OrgDeptService;
import cs.service.sys.UserService;

/**
 * Created by ldm on 2017/8/21.
 */
@Service("signFlowImpl")
public class SignFlowImpl implements IFlow {
    @Autowired
    private UserService userService;
    @Autowired
    private SignPrincipalService signPrincipalService;
    @Autowired
    private SignMergeRepo signMergeRepo;
    @Autowired
    private OrgDeptService orgDeptService;
    @Autowired
    private WorkProgramRepo workProgramRepo;
    @Autowired
    private SignRepo signRepo;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private OrgDeptRepo orgDeptRepo;
    @Autowired
    private ExpertReviewRepo expertReviewRepo;
    @Autowired
    private AssistUnitRepo assistUnitRepo;
    @Autowired
    private SignBranchRepo signBranchRepo;
    /**
     * 获取流程参数
     * @param businessKey
     * @param taskDefinitionKey
     * @return
     */
    @Override
    public Map<String, Object> getFlowBusinessMap(String businessKey,String taskDefinitionKey) {
        Map<String, Object> businessMap = new HashMap<>();
        List<User> userList = null;     //用户列表
        String branchIndex = "";        //分支序号

        switch (taskDefinitionKey) {
            //综合部拟办
            case FlowConstant.FLOW_SIGN_ZHB:
                businessMap.put("viceDirectors", userService.findUserByRoleName(Constant.EnumFlowNodeGroupName.VICE_DIRECTOR.getValue()));
                break;
            //分管领导审核(所有部门和小组)
            case FlowConstant.FLOW_SIGN_FGLD_FB:
                businessMap.put("orgs", orgDeptService.queryAll());
                break;
            //部门分办（选择项目负责人）
            case FlowConstant.FLOW_SIGN_BMFB1:
                branchIndex =  FlowConstant.SignFlowParams.BRANCH_INDEX1.getValue();
            case FlowConstant.FLOW_SIGN_BMFB2:
                if(!Validate.isString(branchIndex)){
                    branchIndex =  FlowConstant.SignFlowParams.BRANCH_INDEX2.getValue();
                }
            case FlowConstant.FLOW_SIGN_BMFB3:
                if(!Validate.isString(branchIndex)){
                    branchIndex =  FlowConstant.SignFlowParams.BRANCH_INDEX3.getValue();
                }
            case FlowConstant.FLOW_SIGN_BMFB4:
                if(!Validate.isString(branchIndex)){
                    branchIndex =  FlowConstant.SignFlowParams.BRANCH_INDEX4.getValue();
                }
                List<UserDto> resultList = new ArrayList<>();
                userList = orgDeptService.queryOrgDeptUser(businessKey,branchIndex);
                //排除项目负责人（这里是用户本身）
                for (int i=0,l=userList.size();i<l; i++) {
                    User user = userList.get(i);
                    if(!user.getId().equals(SessionUtil.getUserInfo().getId())){
                        UserDto uDto = new UserDto();
                        uDto.setId(user.getId());
                        uDto.setUseState(user.getUseState());
                        uDto.setLoginName(user.getLoginName());
                        uDto.setDisplayName(user.getDisplayName());
                        resultList.add(uDto);
                    }
                }
                businessMap.put("users", resultList);
                break;
            //项目负责人办理
            case FlowConstant.FLOW_SIGN_XMFZR1:
                branchIndex =  FlowConstant.SignFlowParams.BRANCH_INDEX1.getValue();
            case FlowConstant.FLOW_SIGN_XMFZR2:
                if(!Validate.isString(branchIndex)){
                    branchIndex =  FlowConstant.SignFlowParams.BRANCH_INDEX2.getValue();
                }
            case FlowConstant.FLOW_SIGN_XMFZR3:
                if(!Validate.isString(branchIndex)){
                    branchIndex =  FlowConstant.SignFlowParams.BRANCH_INDEX3.getValue();
                }
            case FlowConstant.FLOW_SIGN_XMFZR4:
                if(!Validate.isString(branchIndex)){
                    branchIndex =  FlowConstant.SignFlowParams.BRANCH_INDEX4.getValue();
                }
                String isNeedWP =  Constant.EnumState.YES.getValue();

                WorkProgram wp = workProgramRepo.findBySignIdAndBranchId(businessKey,branchIndex, false);
                boolean isFinishWP = false;
                if(Validate.isObject(wp)){
                    if(Validate.isString(wp.getBaseInfo()) && Constant.EnumState.YES.getValue().equals(wp.getBaseInfo())){
                        isFinishWP = false;
                        isNeedWP =  Constant.EnumState.NO.getValue();
                    }else{
                        isFinishWP = true;
                    }
                }
                businessMap.put("isNeedWP", isNeedWP);
                //能查询出工作方案，代表已经完成工作填写
                businessMap.put("isFinishWP", isFinishWP);
                break;
            //发文申请
            case FlowConstant.FLOW_SIGN_FW:
                userList = signPrincipalService.getAllSecondPriUser(businessKey);
                if(Validate.isList(userList)){
                    List<UserDto> userDtoList = new ArrayList<>(userList.size());
                    userList.forEach(pul ->{
                        UserDto userDto = new UserDto();
                        BeanCopierUtils.copyProperties(pul,userDto);
                        userDtoList.add(userDto);
                    });
                    businessMap.put("prilUserList", userDtoList);
                }
                break;
            //项目负责人确认
            case FlowConstant.FLOW_SIGN_QRFW:
                //判断是否有协办部门
                if(signBranchRepo.allAssistCount(businessKey) > 0){
                    businessMap.put("hasAssistDept", true);
                }
                break;
            //部长审批发文
            case FlowConstant.FLOW_SIGN_BMLD_QRFW :
                //获取所有分管领导信息
                userList = signBranchRepo.findAssistSLeader(businessKey);
                //排除主办分支的领导
                if(Validate.isList(userList)){
                    OrgDept orgDept = orgDeptRepo.queryBySignBranchId(businessKey, FlowConstant.SignFlowParams.BRANCH_INDEX1.getValue());
                    User dealUser = userRepo.getCacheUserById(orgDept.getsLeaderID());
                    for(int n=0,l=userList.size();n<l;n++){
                        if((userList.get(n)).getId().equals(dealUser.getId())){
                            userList.remove(n);
                            break;
                        }
                    }
                }
                boolean isHaveTwoSLeader = Validate.isList(userList);       //有两个分管领导
                if(isHaveTwoSLeader){
                    businessMap.put(FlowConstant.SignFlowParams.HAVE_XB.getValue(), isHaveTwoSLeader);
                }
                break;
            //生成发文编号，
            case FlowConstant.FLOW_SIGN_FWBH:
                //如果是合并发文次项目，则不生成发文编号
                boolean isMerge =signMergeRepo.checkIsMerege(businessKey, Constant.MergeType.DISPATCH.getValue());
                businessMap.put("needDISNum", !isMerge);
                //如果有评审费或者是协审流程(有协审单位才算)，则给财务部办理，没有，则直接到归档环节
                boolean isGotoCW = expertReviewRepo.isHaveEPReviewCost(businessKey) ||
                        (signRepo.checkAssistSign(businessKey) && assistUnitRepo.checkAssistUnitBySignId(businessKey));
                if (isGotoCW){
                    businessMap.put(FlowConstant.SignFlowParams.HAVE_ZJPSF.getValue(), true);
                }else{
                    /*//发文环节也可以归档
                    businessMap.put("isGotoGD", true);
                    //如果有其它负责人，则还跳转
                    userList = signPrincipalService.getAllSecondPriUser(businessKey);
                    if(Validate.isList(userList)){
                        businessMap.put("checkFileUser", userList.get(0));
                    }*/
                }
                break;
            //项目归档
            case FlowConstant.FLOW_SIGN_GD:
                //项目负责人获取第一个作为处理人
                userList = signPrincipalService.getAllSecondPriUser(businessKey);
                if(Validate.isList(userList)){
                    businessMap.put("checkFileUser", userList.get(0));
                }
                break;
            default:
                ;
        }
        return businessMap;
    }
}
