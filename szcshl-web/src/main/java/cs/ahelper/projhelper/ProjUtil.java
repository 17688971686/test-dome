package cs.ahelper.projhelper;

import com.google.common.collect.Lists;
import cs.common.constants.Constant;
import cs.common.constants.FlowConstant;
import cs.common.constants.SysConstants;
import cs.common.utils.Arith;
import cs.common.utils.SessionUtil;
import cs.common.utils.Validate;
import cs.domain.project.Sign;
import cs.domain.project.WorkProgram;
import cs.domain.sys.OrgDept;
import cs.domain.sys.SysDept;
import cs.domain.sys.User;
import cs.model.project.Achievement;
import cs.model.project.AchievementSumDto;
import cs.model.project.SignDto;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by ldm on 2018/3/6 0006.
 */
public class ProjUtil {

    /**
     * 验证项目名称，评审阶段、项目编码，委里收文编号等字段信息
     * @param signDto
     * @return
     */
    public static boolean checkProjDataValidate(SignDto signDto) {
        if (!Validate.isString(signDto.getProjectname()) || !Validate.isString(signDto.getReviewstage())
                || !Validate.isString(signDto.getProjectcode()) || !Validate.isString(signDto.getFilecode())) {
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

    public static String getReFlowName(String projName, String reworkType) {
        String type = "重做";
        if (Constant.EnumState.NO.getValue().equals(reworkType)) {
            type = "新增";
        }
        return "[" + projName + "]" + type + "工作方案";
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
     *
     * @return
     */
    public static Sign resetLeaderOption(Sign sign) {
        sign.setLeaderhandlesug("");
        sign.setLeaderDate(null);
        sign.setLeaderId("");
        sign.setLeaderName("");
        return sign;
    }

    /**
     * 清空部长意见
     *
     * @return
     */
    public static Sign resetMinisterOption(Sign sign) {
        sign.setMinisterhandlesug("");
        sign.setMinisterDate(null);
        sign.setMinisterId("");
        sign.setMinisterName("");
        return sign;
    }

    /**
     * 清空部门信息
     *
     * @return
     */
    public static Sign resetReviewDept(Sign sign) {
        sign.setmOrgId("");
        sign.setmOrgName("");
        sign.setaOrgId("");
        sign.setaOrgName("");
        return sign;
    }

    /**
     * 清空部门信息
     *
     * @return
     */
    public static Sign resetReviewUser(Sign sign) {
        sign.setmUserId("");
        sign.setmUserName("");
        sign.setaUserID("");
        sign.setaUserName("");
        return sign;
    }

    /**
     * 部门用户业绩详情信息统计
     * 规则：排除部长人员的统计信息
     *
     * @param countList
     * @param orgDeptList
     * @return
     */
    public static List<Achievement> orgDeptDetail(List<Achievement> countList, List<OrgDept> orgDeptList, Map<String, AchievementSumDto> orgDeptCount, List<SysDept> deptList) {
        List<Achievement> resultList = new ArrayList<>();
        //初始化已经统计好的
        for (OrgDept orgDept : orgDeptList) {
            Achievement achievementDeptDetailDto = new Achievement();
            achievementDeptDetailDto.setOrgId(orgDept.getId());
            achievementDeptDetailDto.setOrgName(orgDept.getName());
            //区分部长和组长
            if (Validate.isString(orgDept.getType()) && "org".equals(orgDept.getType().toLowerCase())) {
                achievementDeptDetailDto.setLevel(Constant.SYS_USER_LEVEL.BZ.getValue());
            } else {
                achievementDeptDetailDto.setLevel(Constant.SYS_USER_LEVEL.ZZ.getValue());
            }
            boolean isHave = false;
            Iterator entries = orgDeptCount.entrySet().iterator();
            while (entries.hasNext()) {
                Map.Entry entry = (Map.Entry) entries.next();
                String key = entry.getKey().toString();
                if (key.equals(orgDept.getName())) {
                    isHave = true;
                    AchievementSumDto achievementSumDto = (AchievementSumDto) entry.getValue();
                    achievementDeptDetailDto.setAssistDisSum(achievementSumDto.getAssistDisSum());
                    achievementDeptDetailDto.setMainDisSum(achievementSumDto.getMainDisSum());
                }
            }
            if (!isHave) {
                achievementDeptDetailDto.setAssistDisSum(0);
                achievementDeptDetailDto.setMainDisSum(0);
            }
            resultList.add(achievementDeptDetailDto);
        }
        //遍历统计list
        if (Validate.isList(countList)) {
            Map<String, List<Achievement>> cacheMap = new HashMap<>();
            for (int i = 0, l = countList.size(); i < l; i++) {
                Achievement achievement = countList.get(i);
                //如果是部长、副主任、主任，则不计入统计
                if (!deptDirector(achievement.getUserId(), orgDeptList)) {
                    countAchievementDetail(cacheMap, achievement, deptList);
                }
            }
            //合并List
            for (Achievement achievement : resultList) {
                Object value = cacheMap.get(achievement.getOrgId());
                if (Validate.isObject(value)) {
                    achievement.setChildList((List<Achievement>) value);
                }
            }
        }
        return resultList;
    }

    /**
     * 判断用户是否组长、部长、副主任、主任
     *
     * @param userId
     * @param orgDeptList
     * @return
     */
    private static boolean deptDirector(String userId, List<OrgDept> orgDeptList) {
        for (OrgDept orgDept : orgDeptList) {
            if (userId.equals(orgDept.getDirectorID())
                    || userId.equals(orgDept.getsLeaderID())
                    || userId.equals(orgDept.getmLeaderID())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 部门人员统计，部门归属的优先级：组别》用户所在部门》项目评审部门
     * （原因，旧数据属于组别的，项目要归属于部门，而且就的评审项目，所有人员都是同一个部门，
     * 要确保人员评审项目的数目对得上，部门数据对不上也无所谓）
     *
     * @param cacheMap
     * @param achievement
     * @param deptList
     */
    private static void countAchievementDetail(Map<String, List<Achievement>> cacheMap, Achievement achievement, List<SysDept> deptList) {
        //项目评审部门ID，用户所在部门ID，用户所在组别ID,最终所属的部门或者组别ID
        String orgId = achievement.getOrgId(), userOrgId = achievement.getUserOrgId(), deptId = achievement.getDeptIds(), orgDeptId;
        //如果当前用户是属于组别,则替换组别信息
        if (Validate.isString(deptId)) {
            orgDeptId = changeDeptId(orgId, deptList);
            //如果找不到对应的组别,则默认第一个（特殊情况，但是也要考虑）
            if (orgDeptId.equals(orgId)) {
                orgDeptId = deptId.split(SysConstants.SEPARATE_COMMA)[0];
            }
        } else {
            orgDeptId = userOrgId;
        }
        //从缓存中取出对应的用户办理详情信息
        List<Achievement> achievementList = cacheMap.get(orgDeptId);
        //如果已经初始化，则判断人员
        boolean isHaveUser = false;
        if (Validate.isList(achievementList)) {
            for (Achievement ach : achievementList) {
                if (ach.getUserId().equals(achievement.getUserId())) {
                    //判断新的评审项目信息是否是主办人
                    if (Constant.EnumState.YES.getValue().equals(achievement.getIsMainUser())) {
                        ach.setMainDisSum(ach.getMainDisSum() + 1);
                    } else {
                        ach.setAssistDisSum(ach.getAssistDisSum() + 1);
                    }
                    isHaveUser = true;
                }
            }
        } else {
            //如果没有初始化，则创建一个新的
            achievementList = new ArrayList<>();
        }
        if (!isHaveUser) {
            Achievement newAchievement = new Achievement();
            if (Constant.EnumState.YES.getValue().equals(achievement.getIsMainUser())) {
                newAchievement.setMainDisSum(1);
            } else {
                newAchievement.setAssistDisSum(1);
            }
            newAchievement.setUserId(achievement.getUserId());
            newAchievement.setOrgId(orgDeptId);
            newAchievement.setUserName(achievement.getUserName());
            achievementList.add(newAchievement);
        }
        cacheMap.put(orgDeptId, achievementList);
    }

    /**
     * 部门业绩发文数统计
     *
     * @param countList
     * @param orgDeptList
     * @return
     */
    public static Map<String, AchievementSumDto> countOrgDept(List<Achievement> countList, List<OrgDept> orgDeptList) {
        Map<String, AchievementSumDto> resultMap = new HashMap<>();
        if (Validate.isList(countList)) {
            //缓存信息
            String lastSignId = countList.get(0).getSignId(), lastOrgId = countList.get(0).getOrgId();
            int lastIndex = 0;
            Map<String, AchievementSumDto> countOrgDept = new HashMap<>();
            for (int i = 0, l = countList.size(); i < l; i++) {
                Achievement achievement = countList.get(i);
                String signId = achievement.getSignId();
                String orgId = achievement.getOrgId();
                //两者有一个不等，则是新的;
                if (!lastSignId.equals(signId) || !lastOrgId.equals(orgId)) {
                    Achievement mainData = countList.get(lastIndex);
                    countAchievementSum(countOrgDept, lastOrgId, mainData);
                    lastSignId = signId;
                    lastOrgId = orgId;
                    lastIndex = i;
                }
                //最后一个也要进行统计
                if (i == (l - 1)) {
                    countAchievementSum(countOrgDept, orgId, achievement);
                }
            }
            //替换map的key值
            Iterator entries = countOrgDept.entrySet().iterator();
            while (entries.hasNext()) {
                Map.Entry entry = (Map.Entry) entries.next();
                String key = entry.getKey().toString();
                resultMap.put(getOrgName(key, orgDeptList), (AchievementSumDto) entry.getValue());
            }
        }
        return resultMap;
    }

    private static String getOrgName(String key, List<OrgDept> orgDeptList) {
        for (OrgDept orgDept : orgDeptList) {
            if (Validate.isString(key) && key.equals(orgDept.getId())) {
                return orgDept.getName();
            }
        }
        return "";
    }

    /**
     * 统计部门数量
     * 规则：旧数据，如果第一负责人是信息化组的人，则项目归为信息化组；新数据则按分办部门统计；排除部长等人的统计
     *
     * @param countOrgDept
     * @param orgId
     * @param achievement
     */
    private static void countAchievementSum(Map<String, AchievementSumDto> countOrgDept, String orgId, Achievement achievement) {
        String branchId = achievement.getBranchId();
        AchievementSumDto achievementSumDto = countOrgDept.get(orgId);
        if (!Validate.isObject(achievementSumDto)) {
            achievementSumDto = new AchievementSumDto();
        }
        if (isMainBranch(branchId)) {
            achievementSumDto.setMainDisSum(achievementSumDto.getMainDisSum() + 1);
        } else {
            achievementSumDto.setAssistDisSum(achievementSumDto.getAssistDisSum() + 1);
        }
        countOrgDept.put(orgId, achievementSumDto);
    }

    /**
     * 替换组别信息
     * 规则，把迁移数据，第一负责人是组别人员的项目，项目归为对应的组
     *
     * @param countList
     * @param deptList
     * @return
     */
    public static List<Achievement> changeOrgId(List<Achievement> countList, List<SysDept> deptList, boolean useOrgData, boolean useDeptData) {
        if (Validate.isList(countList)) {
            String newOrdId = "", udateSignId = "";
            int totalCount = countList.size();
            for (int i = 0; i < totalCount; i++) {
                Achievement achievement = countList.get(i);
                String singId = achievement.getSignId();
                //只要项目名称相同，则把项目所属部门ID替换
                if (udateSignId.equals(singId)) {
                    achievement.setOrgId(newOrdId);
                } else {
                    //1、确定是否是旧数据和是否是组别的人
                    boolean isOldData = Validate.isString(achievement.getOldId());
                    boolean isDeptUser = Validate.isString(achievement.getDeptIds());
                    boolean isMainUser = Constant.EnumState.YES.getValue().equals(achievement.getIsMainUser());
                    if (isOldData && isDeptUser && isMainUser) {
                        udateSignId = singId;
                        //如果第一负责人是组别人员，则把该项目归为对应的组别
                        newOrdId = changeDeptId(achievement.getOrgId(), deptList);
                        achievement.setOrgId(newOrdId);
                    }
                }
            }
            //如果使用部门数据，则要排除归属组别的数据
            if (useOrgData) {
                List<Achievement> orgData = new ArrayList<>();
                String filterSignId = "";
                for (int i = 0; i < totalCount; i++) {
                    Achievement item = countList.get(i);
                    if (filterSignId.equals(item.getSignId())) {
                        continue;
                    }
                    //如果第一负责人是组别人员，要排除这个项目
                    if (Validate.isString(item.getOldId()) && Validate.isString(item.getDeptIds()) && item.getDeptIds().contains(item.getOrgId())) {
                        filterSignId = item.getSignId();
                    } else {
                        orgData.add(item);
                        filterSignId = "";
                    }
                }
                return orgData;
            }
            //如果使用组别的数据，则要包含部门的数据
            if (useDeptData) {
                return countList.stream().filter(item -> (!Validate.isString(item.getOldId())
                        || (Validate.isString(item.getOldId()) && Validate.isString(item.getDeptIds()) && item.getDeptIds().contains(item.getOrgId())))).collect(Collectors.toList());
            }
        }
        return countList;
    }

    private static String changeDeptId(String orgId, List<SysDept> deptList) {
        //确认第一负责人是哪个组别的人,替换部门组别信息
        for (SysDept sysDept1 : deptList) {
            if (orgId.equals(sysDept1.getOrgId())) {
                return sysDept1.getId();
            }
        }
        return orgId;
    }

    /**
     * 部长业绩统计
     *
     * @param countList
     * @param orgDeptList
     * @return
     */
    public static AchievementSumDto sumOrgDeptAchievement(List<Achievement> countList, List<OrgDept> orgDeptList) {
        AchievementSumDto achievementSumDto = new AchievementSumDto();
        achievementSumDto.init();
        if (Validate.isList(orgDeptList)) {
            OrgDept orgDept = orgDeptList.get(0);
            //项目评审部门ID
            String orgId = orgDept.getId();
            achievementSumDto.setDeptIds(orgId);
            achievementSumDto.setDeptNames(orgDept.getName());
            //是否存在统计结果
            if (Validate.isList(countList)) {
                String lastSignId = countList.get(0).getSignId();
                //上一个数据的下标
                int lastIndex = 0, sunCount = 0;
                for (int i = 0, l = countList.size(); i < l; i++) {
                    Achievement achievement = countList.get(i);
                    //用户所在部门ID，用户所在组别ID
                    String userOrgId = achievement.getUserOrgId(), deptIds = achievement.getDeptIds();
                    if (orgId.equals(userOrgId) || (Validate.isString(deptIds) && deptIds.contains(orgId))) {
                        //如果项目名称不同，进行统计
                        if (!lastSignId.equals(achievement.getSignId())) {
                            sumAchievementInfo(achievementSumDto, countList.get(lastIndex), true);
                            lastSignId = achievement.getSignId();
                            lastIndex = i;
                        }
                        //如果是最后一位，也要进行统计
                        if (i == (l - 1)) {
                            sumAchievementInfo(achievementSumDto, achievement, true);
                        }
                    }
                }
            }
            //计算核增核减总额和核增核减率
            countAchievementExtra(achievementSumDto);
        }
        return achievementSumDto;
    }

    private static void countAchievementExtra(AchievementSumDto achievementSumDto) {
        //1、主办
        //核增核减总额
        BigDecimal mainExtravalue = Arith.safeSubtract(false, achievementSumDto.getMainAuthorizevalueSum(), achievementSumDto.getMainDeclarevalueSum());
        achievementSumDto.setMainExtravalueSum(mainExtravalue.abs());
        //核增核减率
        BigDecimal mainExtraRate = BigDecimal.ZERO;
        if (achievementSumDto.getMainDeclarevalueSum().compareTo(BigDecimal.ZERO) == 1) {
            mainExtraRate = achievementSumDto.getMainExtravalueSum().divide(achievementSumDto.getMainDeclarevalueSum(), 4, BigDecimal.ROUND_HALF_UP);
            mainExtraRate = Arith.safeMultiply(mainExtraRate, 100);
        }
        achievementSumDto.setMainExtraRateSum(mainExtraRate);
        //协办
        //核增核减总额
        BigDecimal assistExtravalue = Arith.safeSubtract(false, achievementSumDto.getAssistAuthorizevalueSum(), achievementSumDto.getAssistDeclarevalueSum());
        achievementSumDto.setAssistExtravalueSum(assistExtravalue.abs());
        //核增核减率
        BigDecimal assistExtraRate = BigDecimal.ZERO;
        if (achievementSumDto.getAssistDeclarevalueSum().compareTo(BigDecimal.ZERO) == 1) {
            assistExtraRate = achievementSumDto.getAssistExtravalueSum().divide(achievementSumDto.getAssistDeclarevalueSum(), 4, BigDecimal.ROUND_HALF_UP);
            assistExtraRate = Arith.safeMultiply(assistExtraRate, 100);
        }
        achievementSumDto.setAssistExtraRateSum(assistExtraRate);
    }

    /**
     * 统计部门业绩的评审数，评审金额等信息
     *
     * @param achievementSumDto
     * @param achievement
     */
    private static void sumAchievementInfo(AchievementSumDto achievementSumDto, Achievement achievement, boolean isDept) {
        boolean isMain = false;
        //如果是部门，主分支就是主办
        if (isDept && isMainBranch(achievement.getBranchId())) {
            isMain = true;
            //如果是人员，第一负责人才是主办
        } else if (Constant.EnumState.YES.getValue().equals(achievement.getIsMainUser())) {
            isMain = true;
        }
        if (isMain) {
            //主办发文
            achievementSumDto.setMainDisSum(achievementSumDto.getMainDisSum() + 1);
            achievementSumDto.setMainAuthorizevalueSum(Arith.safeAdd(achievementSumDto.getMainAuthorizevalueSum(), achievement.getAuthorizeValue()));
            achievementSumDto.setMainDeclarevalueSum(Arith.safeAdd(achievementSumDto.getMainDeclarevalueSum(), achievement.getDeclareValue()));
            //核增核减总金额
            //achievementSumDto.setMainExtravalueSum(Arith.safeAdd(achievementSumDto.getMainExtravalueSum(),achievement.getExtraValue()));
        } else {
            //协办发文
            achievementSumDto.setAssistDisSum(achievementSumDto.getAssistDisSum() + 1);
            achievementSumDto.setAssistAuthorizevalueSum(Arith.safeAdd(achievementSumDto.getAssistAuthorizevalueSum(), achievement.getAuthorizeValue()));
            achievementSumDto.setAssistDeclarevalueSum(Arith.safeAdd(achievementSumDto.getAssistDeclarevalueSum(), achievement.getDeclareValue()));
            //achievementSumDto.setAssistExtravalueSum(Arith.safeAdd(achievementSumDto.getAssistExtravalueSum(),achievement.getExtraValue()));
        }
        //如果是部门，还要统计主办协办项目数据
        if (isDept) {
            //如果是部门主办
            boolean isMainDept = isMainBranch(achievement.getBranchId());
            List<Achievement> chileLit = null;
            if (isMainDept) {
                chileLit = achievementSumDto.getMainChildList();
            } else {
                chileLit = achievementSumDto.getAssistChildList();
            }
            //判断是否已经有该项目
            checkDeptAchievement(chileLit, achievement);
            if (isMainDept) {
                achievementSumDto.setMainChildList(chileLit);
            } else {
                achievementSumDto.setAssistChildList(chileLit);
            }
        }
    }

    private static void checkDeptAchievement(List<Achievement> chileLit, Achievement achievement) {
        if (null == chileLit) {
            chileLit = Lists.newArrayList();
            chileLit.add(achievement);
        } else {
            boolean isHave = false;
            for (Achievement chile : chileLit) {
                if (chile.getSignId().equals(achievement.getSignId())) {
                    isHave = true;
                    break;
                }
            }
            //如果之前项目没有，则添加到列表中
            if (!isHave) {
                chileLit.add(achievement);
            }
        }
    }

    /**
     * 普通用户业绩统计信息
     *
     * @param countList
     * @return
     */
    public static AchievementSumDto sumUserAchievement(List<Achievement> countList) {
        AchievementSumDto achievementSumDto = new AchievementSumDto();
        achievementSumDto.init();
        achievementSumDto.setUserId(SessionUtil.getUserId());
        if (Validate.isList(countList)) {
            for (int i = 0, l = countList.size(); i < l; i++) {
                Achievement achievement = countList.get(i);
                sumAchievementInfo(achievementSumDto, achievement, false);
                //统计子集信息
                if (Constant.EnumState.YES.getValue().equals(achievement.getIsMainUser())) {
                    List<Achievement> mainChileLit = achievementSumDto.getMainChildList();
                    mainChileLit.add(achievement);
                    achievementSumDto.setMainChildList(mainChileLit);
                } else {
                    List<Achievement> assistChileLit = achievementSumDto.getAssistChildList();
                    assistChileLit.add(achievement);
                    achievementSumDto.setAssistChildList(assistChileLit);
                }
            }
            //计算核增核减率
            countAchievementExtra(achievementSumDto);
        }

        return achievementSumDto;
    }

    public static String[] getQuarterMonth(String quarter) {
        String smonth = "1", emonth = "12";
        switch (quarter) {
            case "1":
                smonth = "1";
                emonth = "3";
                break;
            case "2":
                smonth = "4";
                emonth = "6";
                break;
            case "3":
                smonth = "7";
                emonth = "9";
                break;
            case "4":
                smonth = "10";
                emonth = "12";
                break;
            default:
                ;
        }
        return new String[]{smonth, emonth};
    }

    /**
     * 根据年份和季度，查出对应的时间信息
     *
     * @param year
     * @param quarter
     * @return
     */
    public static String[] getQueryTime(String year, String quarter) {
        String beginTime = year + "-01-01 00:00:00", endTime = year + "-12-31 23:59:59";
        if (Validate.isString(year) && Validate.isString(quarter)) {
            switch (quarter) {
                case "1":
                    beginTime = year + "-01-01 00:00:00";
                    endTime = year + "-03-31 23:59:59";
                    break;
                case "2":
                    beginTime = year + "-04-01 00:00:00";
                    endTime = year + "-06-30 23:59:59";
                    break;
                case "3":
                    beginTime = year + "-07-01 00:00:00";
                    endTime = year + "-09-30 23:59:59";
                    break;
                case "4":
                    beginTime = year + "-10-01 00:00:00";
                    endTime = year + "-12-31 23:59:59";
                    break;
                default:
                    ;
            }
        }
        return new String[]{beginTime, endTime};
    }

    /**
     * 校验用户是否是任务用户
     *
     * @param user
     * @param userId
     * @return
     */
    public static boolean userIsTaskUser(User user, String userId) {
        if (null == user) {
            return false;
        }
        return userId.equals(user.getId()) || userId.equals(user.getTakeUserId());

    }

    /**
     * 如果收文编号以0000结束，说明委里没有收文编号，这个编号可以有多个
     * 之前委里收文编号年份后面+4位数，现在是5位数
     * @param filecode 项目收文编码，不能为空
     * @return
     */
    public static boolean isSelfProj(String filecode) {
        return filecode.endsWith("00000") || filecode.endsWith("0000");
    }
}
