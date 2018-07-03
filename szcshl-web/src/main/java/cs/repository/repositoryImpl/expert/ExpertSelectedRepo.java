package cs.repository.repositoryImpl.expert;

import cs.common.ResultMsg;
import cs.domain.expert.ExpertSelected;
import cs.domain.sys.OrgDept;
import cs.model.expert.*;
import cs.repository.IRepository;

import java.util.List;
import java.util.Map;

/**
 * Description: 抽取专家 数据操作实现接口
 * author: ldm
 * Date: 2017-6-14 14:26:49
 */
public interface ExpertSelectedRepo extends IRepository<ExpertSelected, String> {
    /**
     * 根据大类，小类和专家类别确认已经抽取的专家
     *
     * @param reviewId
     * @param maJorBig
     * @param maJorSmall
     * @param expeRttype
     * @return
     */
    int findConfirmSeletedEP(String reviewId, String maJorBig, String maJorSmall, String expeRttype, Integer compositeScore, Integer compositeScortEnd);

    /**
     * 根据业务ID统计已经确认的抽取专家
     *
     * @param businessId
     * @return
     */
    int getSelectEPCount(String businessId);

    /**
     * 专家抽取统计
     *
     * @param expertSelectHis
     * @param isScore         是否评分统计
     * @return
     */
    List<Object[]> getSelectHis(ExpertSelectHis expertSelectHis, boolean isScore);

    /**
     * 专家评审基本情况详细统计
     *
     * @param expertReviewCondDto
     * @return
     */
    ResultMsg expertReviewCondDetailCount(ExpertReviewCondDto expertReviewCondDto);

    /**
     * 专家评审基本情况详细综合
     *
     * @param expertReviewConSimpleDto
     * @return
     */
    ResultMsg expertReviewConSimpleCount(ExpertReviewConSimpleDto expertReviewConSimpleDto);

    /**
     * 专家评审基本情况不规则
     *
     * @param expertReviewConSimpleDto
     * @return
     */
    ResultMsg expertReviewConComplicatedCount(ExpertReviewConSimpleDto expertReviewConSimpleDto);

    /**
     * 项目评审情况统计(按类别)
     *
     * @param projectReviewConditionDto
     * @return
     */
    ResultMsg proReviewConditionByTypeCount(ProReviewConditionDto projectReviewConditionDto);

    /**
     * 项目评审情况汇总
     *
     * @param projectReviewConditionDto
     * @return
     */
    ProReviewConditionDto proReviewConditionSum(ProReviewConditionDto projectReviewConditionDto);

    /**
     * 获取提前介入评审情况
     *
     * @param projectReviewConditionDto
     * @return
     */
    ProReviewConditionDto getAdvancedCon(ProReviewConditionDto projectReviewConditionDto);
    /**
     * 项目评审情况明细
     *
     * @param projectReviewConditionDto
     * @return
     */
    List<ProReviewConditionDto> proReviewConditionDetail(ProReviewConditionDto projectReviewConditionDto);

    /**
     * 获取退文项目详细情况
     * @param projectReviewConditionDto
     * @return
     */
     List<ProReviewConditionDto> getBackDispatchInfo(ProReviewConditionDto projectReviewConditionDto);

    /**
     * 项目退文汇总
     * @param projectReviewConditionDto
     * @return
     */
     ProReviewConditionDto getBackDispatchSum(ProReviewConditionDto projectReviewConditionDto);

    /**
     * 项目评审情况汇总(按照申报投资金额)
     *
     * @param beginTime
     * @param endTime
     * @return
     */
    Integer[] proReviewCondByDeclare(String beginTime, String endTime);

    /**
     * 专家评审会次数
     *
     * @param projectReviewConditionDto
     * @return
     */
    Integer proReviewMeetingCount(ProReviewConditionDto projectReviewConditionDto);

    /**
     * 项目评审次数
     *
     * @param projectReviewConditionDto
     * @return
     */
    Integer proReviewCount(ProReviewConditionDto projectReviewConditionDto);


    /**
     * 更改抽取专家的属性值
     * @param businessId
     * @param propertyname
     * @param value
     */
    void updateExpertSelectState(String businessId, String propertyname, String value);

    /**
     * 业绩汇总
     * @param achievementSumDto
     * @return
     */
    List<AchievementSumDto> findAchievementSum(AchievementSumDto achievementSumDto,Map<String,Object> levelMap,List<OrgDept> orgDeptList);

    /**
     * 业绩明细
     * @param achievementSumDto
     * @return
     */
    List<AchievementDetailDto> findAchievementDetail(AchievementSumDto achievementSumDto,Map<String,Object> levelMap,List<OrgDept> orgDeptList);

    List<ExpertSelectedDto> findByBusinessId(String businessID);

    /**
     * 查询部门业绩明细
     * @param achievementSumDto
     * @param levelMap
     * @return
     */
    List<AchievementDeptDetailDto> findDeptAchievementDetail(AchievementDeptDetailDto achievementSumDto,Map<String,Object> levelMap);

}
