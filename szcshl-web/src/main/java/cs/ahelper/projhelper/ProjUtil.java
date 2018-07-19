package cs.ahelper.projhelper;

import cs.common.constants.Constant;
import cs.common.constants.FlowConstant;
import cs.common.utils.Validate;
import cs.domain.project.WorkProgram;

import java.util.List;

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

    /**
     * 获取主工作方案
     * @param wpList
     * @return
     */
    public static WorkProgram filterMainWP(List<WorkProgram> wpList){
        if(!Validate.isList(wpList)){
            return null;
        }
        WorkProgram workProgram = wpList.stream().filter(item -> FlowConstant.SignFlowParams.BRANCH_INDEX1.getValue().equals(item.getBranchId())).findFirst().get();
        return workProgram;
    }

    /**
     * 拷贝主工作方案的公共属性到分支对象
     * @param mainWP
     * @param wp
     */
    public static void copyMainWPProps(WorkProgram mainWP, WorkProgram wp) {
        //申报金额appalyInvestment
        wp.setAppalyInvestment(mainWP.getAppalyInvestment());
        //来文单位sendFileUnit
        wp.setSendFileUnit(mainWP.getSendFileUnit());
        //来文联系人sendFileUser
        wp.setSendFileUser(mainWP.getSendFileUser());
        //建设单位buildCompany
        wp.setBuildCompany(mainWP.getBuildCompany());
        //编制单位designCompany
        wp.setDesignCompany(mainWP.getDesignCompany());
        //主管部门mainDeptName
        wp.setMainDeptName(mainWP.getMainDeptName());
        //是否有环评isHaveEIA
        wp.setIsHaveEIA(mainWP.getIsHaveEIA());
        //项目类别projectType
        wp.setProjectType(mainWP.getProjectType());
        //项目类别（小类）projectSubType
        wp.setProjectSubType(mainWP.getProjectSubType());
        //行业类别industryType
        wp.setIndustryType(mainWP.getIndustryType());
        //联系人contactPerson
        wp.setContactPerson(mainWP.getContactPerson());
        //联系人手机contactPersonPhone
        wp.setContactPersonPhone(mainWP.getContactPersonPhone());
        //联系人电话contactPersonTel
        wp.setContactPersonTel(mainWP.getContactPersonTel());
        //联系人传真contactPersonFax
        wp.setContactPersonFax(mainWP.getContactPersonFax());
        //评估部门reviewOrgName
        wp.setReviewOrgName(mainWP.getReviewOrgName());
        //第一负责人mianChargeUserName
        wp.setMianChargeUserName(mainWP.getMianChargeUserName());
        //第二负责人secondChargeUserName
        wp.setSecondChargeUserName(mainWP.getSecondChargeUserName());
    }
}
