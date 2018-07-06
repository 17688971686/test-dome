package cs.ahelper.projhelper;

import cs.common.constants.FlowConstant;
import cs.common.utils.Validate;

/**
 * Created by ldm on 2018/3/6 0006.
 */
public class BranchUtil {

    /**
     * 判断是否是主分支
     * @param branchId
     * @return
     */
    public static boolean isMainBranch(String branchId){
        if(Validate.isString(branchId) && FlowConstant.SignFlowParams.BRANCH_INDEX1.getValue().equals(branchId)){
            return true;
        }
        return false;
    }
}
