package cs.service.project;

import cs.common.ResultMsg;
import cs.domain.project.SignDispaWork;
import cs.domain.sys.OrgDept;
import cs.domain.sys.SysDept;
import cs.model.PageModelDto;
import cs.model.project.Achievement;
import cs.model.project.AchievementSumDto;
import cs.model.project.SignDispaWorkDto;
import cs.repository.odata.ODataObj;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface SignDispaWorkService {

    /**
     * 查询个人经办项目
     * @param odataObj
     * @param isMianUser
     * @return
     */
    PageModelDto<SignDispaWork> findMyDoProject(ODataObj odataObj,boolean isMianUser);

    /**
     * 项目综合查询
     * @param odataObj
     * @return
     */
    PageModelDto<SignDispaWorkDto> getCommQurySign(ODataObj odataObj);

    /**
     * 查询待合并评审的项目
     * @param signId
     * @return
     */
    List<SignDispaWork> unMergeWPSign(String signId);

    /**
     * 查询已经合并评审的项目
     * @param signId
     * @return
     */
    List<SignDispaWork> getMergeWPSignBySignId(String signId);

    /**
     * 选择待合并的项目
     * @param signId
     * @return
     */
    List<SignDispaWork> unMergeDISSign(String signId);

    List<SignDispaWork> getMergeDISSignBySignId(String signId);

    /**
     * 保存合并项目
     * @param signId
     * @param mergeIds
     * @param mergeType 1代表合并评审，2代表合并发文
     * @return
     */
    ResultMsg mergeSign(String signId, String mergeIds, String mergeType);

    /**
     * 解除合并
     * @param signId
     * @param cancelIds
     * @param mergeType
     * @return
     */
    ResultMsg cancelMergeSign(String signId, String cancelIds, String mergeType);

    /**
     * 删除已经合并的工作方案
     * @param signId
     * @param mergeType
     * @return
     */
    ResultMsg deleteAllMerge(String signId,String mergeType,String businessId);

    List<SignDispaWork> getSignDispaWork(String filters);

    //查询评审费发放超期的项目信息  （停用）
    PageModelDto<SignDispaWork> findOverSignDispaWork();

    /**
     * 通过时间段 获取项目信息（按评审阶段分组），用于项目查询统计分析
     * @param startTime
     * @param endTime
     * @return
     */
    ResultMsg findByTime(String startTime , String endTime);

    /**
     * 通过评审阶段，项目类别，统计项目信息
     * @param startTime
     * @param endTime
     * @return
     */
    ResultMsg findByTypeAndReview(String startTime , String endTime);

    /**
     * 通过条件查询统计
     * @param queryData
     * @param page
     * @return
     */
    List<SignDispaWork> queryStatistics(String  queryData , int page);

    /**
     * 通过业务id，判断当前用户是否有权限查看项目详情----用于秘密项目
     * @param signId
     * @return
     */
    ResultMsg findSecretProPermission(String signId);

    /**
     * 超级管理员修改收文日期，重新计算剩余工作日
     * @param oldSignDate
     * @param signDate
     * @return
     */
    ResultMsg countWeekDays(Date oldSignDate , Date signDate);

    /**
     * 业绩统计汇总
     * @param achievementSumDto
     * @return
     */
    Map<String,Object> countAchievementSum(AchievementSumDto achievementSumDto);

    /**
     * 业绩查询结果统计
     * @param resultMap
     * @param level
     * @param countList
     * @param orgDeptList
     */
    void countAchievementDetail(Map<String, Object> resultMap, int level, List<Achievement> countList, List<OrgDept> orgDeptList, List<SysDept> deptList);
}
