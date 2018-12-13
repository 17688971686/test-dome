package cs.ahelper.projhelper;

import cs.common.constants.Constant;
import cs.common.constants.FlowConstant;
import cs.common.utils.Validate;
import cs.domain.project.Sign;
import cs.domain.project.WorkProgram;
import cs.domain.sys.OrgDept;
import cs.model.project.Achievement;
import cs.model.project.AchievementDeptDetailDto;
import cs.model.project.AchievementSumDto;
import cs.model.project.SignDto;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by ldm on 2018/3/6 0006.
 */
public class ProjUtil {

    public static boolean checkProjDataValidate(SignDto signDto){
        if(!Validate.isString(signDto.getProjectname()) ||!Validate.isString(signDto.getReviewstage())
                || !Validate.isString(signDto.getProjectcode()) ||!Validate.isString(signDto.getFilecode())){
            return false;
        }
        return true;
    }

    /**
     * 根据会签数量发送短信
     *
     * @param valiables
     * @param signNum
     * @return
     */
    public static boolean checkSignComplete(Map<String, Object> valiables, int signNum) {
        if (!valiables.containsKey("nrOfInstances") || !valiables.containsKey("nrOfCompletedInstances")) {
            return false;
        }
        Integer nrOfInstances = (Integer) valiables.get("nrOfInstances"),
                nrOfCompletedInstances = (Integer) valiables.get("nrOfCompletedInstances");
        return (nrOfInstances - nrOfCompletedInstances) == signNum ? true : false;
    }

    /**
     * 判断是否是主分支
     *
     * @param branchId
     * @return
     */
    public static boolean isMainBranch(String branchId) {
        if (Validate.isString(branchId) && FlowConstant.SignFlowParams.BRANCH_INDEX1.getValue().equals(branchId)) {
            return true;
        }
        return false;
    }

    /**
     * 是否合并评审
     *
     * @param isSigle
     * @return
     */
    public static boolean isMergeReview(String isSigle) {
        if (Validate.isString(isSigle) && Constant.MergeType.REVIEW_MERGE.getValue().equals(isSigle)) {
            return true;
        }
        return false;
    }


    /**
     * 是否合并评审主项目
     * 9表示合并评审主项目，0表示合并评审次项目
     *
     * @param reviewType
     * @return
     */
    public static boolean isMergeRVMainTask(String reviewType) {
        if (Validate.isString(reviewType) && Constant.EnumState.YES.getValue().equals(reviewType)) {
            return true;
        }
        return false;
    }

    /**
     * 是否合并评审次项目
     * 9表示合并评审主项目，0表示合并评审次项目
     *
     * @param reviewType
     * @return
     */
    public static boolean isMergeRVAssistTask(String reviewType) {
        if (Validate.isString(reviewType) && Constant.EnumState.NO.getValue().equals(reviewType)) {
            return true;
        }
        return false;
    }

    /**
     * 是否合并发文
     *
     * @param disWay
     * @return
     */
    public static boolean isMergeDis(String disWay) {
        if (Validate.isString(disWay) && Constant.MergeType.DIS_MERGE.getValue().equals(disWay)) {
            return true;
        }
        return false;
    }

    /**
     * 是否主项目
     *
     * @param mainFlag
     * @return
     */
    public static boolean isMain(String mainFlag) {
        if (Validate.isString(mainFlag) && Constant.EnumState.YES.getValue().equals(mainFlag)) {
            return true;
        }
        return false;
    }

    /**
     * 获取主工作方案
     *
     * @param wpList
     * @return
     */
    public static WorkProgram filterMainWP(List<WorkProgram> wpList) {
        if (!Validate.isList(wpList)) {
            return null;
        }
        WorkProgram workProgram = wpList.stream().filter(item -> FlowConstant.SignFlowParams.BRANCH_INDEX1.getValue().equals(item.getBranchId())).findFirst().get();
        return workProgram;
    }

    /**
     * 拷贝主工作方案的公共属性到分支对象
     *
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

    public static String getReFlowName(String projName,String reworkType) {
        String type = "重做";
        if(Constant.EnumState.NO.getValue().equals(reworkType)){
            type = "新增";
        }
        return "[" + projName + "]"+type+"工作方案";
    }

    /**
     * 过滤出有效的工作方案
     *
     * @param workProgramList
     * @return
     */
    public static List<WorkProgram> filterEnableWP(List<WorkProgram> workProgramList) {
        if (Validate.isList(workProgramList)) {
            return workProgramList.stream().filter(item -> Constant.EnumState.YES.getValue().equals(item.getState())).collect(Collectors.toList());
        }
        return null;
    }
    /**
     * 清空综合部意见
     * @return
     */
    public static Sign resetLeaderOption(Sign sign){
        sign.setLeaderhandlesug("");
        sign.setLeaderDate(null);
        sign.setLeaderId("");
        sign.setLeaderName("");
        return sign;
    }

    /**
     * 清空部长意见
     * @return
     */
    public static Sign resetMinisterOption(Sign sign){
        sign.setMinisterhandlesug("");
        sign.setMinisterDate(null);
        sign.setMinisterId("");
        sign.setMinisterName("");
        return sign;
    }

    /**
     * 清空部门信息
     * @return
     */
    public static Sign resetReviewDept(Sign sign){
        sign.setmOrgId("");
        sign.setmOrgName("");
        sign.setaOrgId("");
        sign.setaOrgName("");
        return sign;
    }
    /**
     * 清空部门信息
     * @return
     */
    public static Sign resetReviewUser(Sign sign){
        sign.setmUserId("");
        sign.setmUserName("");
        sign.setaUserID("");
        sign.setaUserName("");
        return sign;
    }

    /**
     * 部门用户业绩详情信息统计
     * @param countList
     * @param orgDeptList
     * @return
     */
    public static List<Achievement> orgDeptDetail(List<Achievement> countList, List<OrgDept> orgDeptList,Map<String,AchievementSumDto> orgDeptCount) {
        List<Achievement> resultList = new ArrayList<>();
        //初始化已经统计好的
        for(OrgDept orgDept : orgDeptList){
            Achievement achievementDeptDetailDto = new Achievement();
            achievementDeptDetailDto.setOrgId(orgDept.getId());
            achievementDeptDetailDto.setOrgName(orgDept.getName());
            boolean isHave = false;
            Iterator entries = orgDeptCount.entrySet().iterator();
            while (entries.hasNext()) {
                Map.Entry entry = (Map.Entry) entries.next();
                String key = entry.getKey().toString();
                if(key.equals(orgDept.getName())){
                    isHave = true;
                    AchievementSumDto achievementSumDto = (AchievementSumDto) entry.getValue();
                    achievementDeptDetailDto.setAssistDisSum(achievementSumDto.getAssistDisSum());
                    achievementDeptDetailDto.setMainDisSum(achievementSumDto.getMainDisSum());
                }
            }
            if(!isHave){
                achievementDeptDetailDto.setAssistDisSum(0);
                achievementDeptDetailDto.setMainDisSum(0);
            }
            resultList.add(achievementDeptDetailDto);
        }
        //遍历统计list
        if(Validate.isList(countList)){
            Map<String,List<Achievement>> cacheMap = new HashMap<>();
            for(int i=0,l=countList.size();i<l;i++){
                Achievement achievement = countList.get(i);
                countAchievementDetail(cacheMap,achievement);
            }

            //合并List
            for(Achievement achievement :resultList){
                Object value = cacheMap.get(achievement.getOrgId());
                if(Validate.isObject(value)){
                    achievement.setChildList((List<Achievement>) value);
                }
            }
        }
        return resultList;
    }

    private static void countAchievementDetail(Map<String, List<Achievement>> cacheMap, Achievement achievement) {
        List<Achievement> achievementList = cacheMap.get(achievement.getOrgId());
        if(!Validate.isList(achievementList)){
            achievementList = new ArrayList<>();
        }
        achievementList.add(achievement);
        cacheMap.put(achievement.getOrgId(),achievementList);
    }

    /**
     * 部门业绩发文数统计
     * @param countList
     * @param orgDeptList
     * @return
     */
    public static Map<String,AchievementSumDto> countOrgDept(List<Achievement> countList, List<OrgDept> orgDeptList) {
        Map<String,AchievementSumDto> resultMap = new HashMap<>();
        if(Validate.isList(countList)){
            String lastSignId = "",lastOrgId = "";
            Map<String,AchievementSumDto> countOrgDept = new HashMap<>();
            for(int i=0,l=countList.size();i<l;i++){
                Achievement achievement = countList.get(i);
                String signId = achievement.getSignId();
                String orgId = achievement.getOrgId();
                String branchId = achievement.getBranchId();
                //两者有一个不等，则是新的
                if(!lastSignId.equals(signId) || !lastOrgId.equals(orgId)){
                    countAchievementSum(countOrgDept,orgId,branchId);
                    lastSignId = signId;
                    lastOrgId = orgId;
                }
                //最后一个也要进行统计
                if(i == (l-1)){
                    countAchievementSum(countOrgDept,orgId,branchId);
                }
            }
            //替换map的key值
            Iterator entries = countOrgDept.entrySet().iterator();
            while (entries.hasNext()) {
                Map.Entry entry = (Map.Entry) entries.next();
                String key = entry.getKey().toString();
                resultMap.put(getOrgName(key,orgDeptList),(AchievementSumDto) entry.getValue());
            }
        }
        return resultMap;
    }

    private static String getOrgName(String key, List<OrgDept> orgDeptList) {
        for(OrgDept orgDept : orgDeptList){
            if(Validate.isString(key) && key.equals(orgDept.getId())){
                return orgDept.getName();
            }
        }
        return "";
    }

    /**
     * 统计数量
     * @param countOrgDept
     * @param orgId
     * @param branchId
     */
    private static void countAchievementSum(Map<String, AchievementSumDto> countOrgDept, String orgId, String branchId) {
        AchievementSumDto achievementSumDto = countOrgDept.get(orgId);
        if(!Validate.isObject(achievementSumDto)){
            achievementSumDto = new AchievementSumDto();
        }
        if(isMainBranch(branchId)){
            achievementSumDto.setMainDisSum(achievementSumDto.getMainDisSum()+1);
        }else{
            achievementSumDto.setAssistDisSum(achievementSumDto.getAssistDisSum()+1);
        }
        countOrgDept.put(orgId,achievementSumDto);
    }
}
