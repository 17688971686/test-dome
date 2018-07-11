package cs.ahelper.projhelper;

import cs.common.constants.Constant;
import cs.common.constants.FlowConstant;
import cs.common.utils.Validate;
import org.apache.xmlbeans.impl.xb.xsdschema.NamespaceList;

/**
 * Created by ldm on 2018/3/6 0006.
 */
public class ProjUtil {

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

    /**
     * 是否合并评审
     * @param isSigle
     * @return
     */
    public static boolean isMergeReview(String isSigle){
        if(Validate.isString(isSigle) && Constant.MergeType.REVIEW_MERGE.getValue().equals(isSigle)){
            return true;
        }
        return false;
    }


    /**
     * 是否合并评审主项目
     * 9表示合并评审主项目，0表示合并评审次项目
     * @param reviewType
     * @return
     */
    public static boolean isMergeRVMainTask(String reviewType){
        if(Validate.isString(reviewType) && Constant.EnumState.YES.getValue().equals(reviewType)){
            return true;
        }
        return false;
    }

    /**
     * 是否合并评审次项目
     * 9表示合并评审主项目，0表示合并评审次项目
     * @param reviewType
     * @return
     */
    public static boolean isMergeRVAssistTask(String reviewType){
        if(Validate.isString(reviewType) && Constant.EnumState.NO.getValue().equals(reviewType)){
            return true;
        }
        return false;
    }
    /**
     * 是否合并发文
     * @param disWay
     * @return
     */
    public static boolean isMergeDis(String disWay){
        if(Validate.isString(disWay) && Constant.MergeType.DIS_MERGE.getValue().equals(disWay)){
            return true;
        }
        return false;
    }

    /**
     * 是否主项目
     * @param mainFlag
     * @return
     */
    public static boolean isMain(String mainFlag){
        if(Validate.isString(mainFlag) && Constant.EnumState.YES.getValue().equals(mainFlag)){
            return true;
        }
        return false;
    }
}
