package cs.service.flow;

import cs.common.Constant;
import cs.common.utils.BeanCopierUtils;
import cs.common.utils.SessionUtil;
import cs.common.utils.Validate;
import cs.domain.sys.User;
import cs.model.sys.UserDto;
import cs.repository.repositoryImpl.project.SignBranchRepo;
import cs.repository.repositoryImpl.project.SignMergeRepo;
import cs.service.project.SignPrincipalService;
import cs.service.sys.OrgDeptService;
import cs.service.sys.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private SignBranchRepo signBranchRepo;
    @Autowired
    private SignMergeRepo signMergeRepo;
    //部门（小组）列表
    @Autowired
    private OrgDeptService orgDeptService;

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
            case Constant.FLOW_SIGN_ZHB:
                businessMap.put("viceDirectors", userService.findUserByRoleName(Constant.EnumFlowNodeGroupName.VICE_DIRECTOR.getValue()));
                break;
            //分管领导审核(所有部门和小组)
            case Constant.FLOW_SIGN_FGLD_FB:
                businessMap.put("orgs", orgDeptService.queryAll());
                break;
            //部门分办（选择项目负责人）
            case Constant.FLOW_SIGN_BMFB1:
                branchIndex =  Constant.SignFlowParams.BRANCH_INDEX1.getValue();
            case Constant.FLOW_SIGN_BMFB2:
                if(!Validate.isString(branchIndex)){
                    branchIndex =  Constant.SignFlowParams.BRANCH_INDEX2.getValue();
                }
            case Constant.FLOW_SIGN_BMFB3:
                if(!Validate.isString(branchIndex)){
                    branchIndex =  Constant.SignFlowParams.BRANCH_INDEX3.getValue();
                }
            case Constant.FLOW_SIGN_BMFB4:
                if(!Validate.isString(branchIndex)){
                    branchIndex =  Constant.SignFlowParams.BRANCH_INDEX4.getValue();
                }
                List<UserDto> resultList = new ArrayList<>();
                userList = orgDeptService.queryOrgDeptUser(businessKey,branchIndex);
                //排除项目负责人（这里是用户本身）
                for (int i=0,l=userList.size();i<l; i++) {
                    User user = userList.get(i);
                    if(!user.getId().equals(SessionUtil.getUserInfo().getId())){
                        UserDto uDto = new UserDto();
                        uDto.setId(user.getId());
                        uDto.setLoginName(user.getLoginName());
                        uDto.setDisplayName(user.getDisplayName());
                        resultList.add(uDto);
                    }
                }
                businessMap.put("users", resultList);
                break;
            //项目负责人办理
            case Constant.FLOW_SIGN_XMFZR1:
                branchIndex =  Constant.SignFlowParams.BRANCH_INDEX1.getValue();
            case Constant.FLOW_SIGN_XMFZR2:
                if(!Validate.isString(branchIndex)){
                    branchIndex =  Constant.SignFlowParams.BRANCH_INDEX2.getValue();
                }
            case Constant.FLOW_SIGN_XMFZR3:
                if(!Validate.isString(branchIndex)){
                    branchIndex =  Constant.SignFlowParams.BRANCH_INDEX3.getValue();
                }
            case Constant.FLOW_SIGN_XMFZR4:
                if(!Validate.isString(branchIndex)){
                    branchIndex =  Constant.SignFlowParams.BRANCH_INDEX4.getValue();
                }
                businessMap.put("isFinishWP", signBranchRepo.checkFinishWP(businessKey,branchIndex));
                break;
            //发文申请
            case Constant.FLOW_SIGN_FW:
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
            //生成发文编号，如果是合并发文次项目，则不需要关联
            case Constant.FLOW_SIGN_FWBH:
                boolean isMerge =signMergeRepo.checkIsMerege(businessKey, Constant.MergeType.DISPATCH.getValue());
                businessMap.put("needDISNum", !isMerge);
                break;
            //项目归档
            case Constant.FLOW_SIGN_GD:
                //项目负责人获取第一个作为处理人
                userList = signPrincipalService.getAllSecondPriUser(businessKey);
                if(Validate.isList(userList)){
                    businessMap.put("checkFileUser", userList.get(0));
                }
                break;
            default:
        }
        return businessMap;
    }
}
