package cs.service.expert;

import cs.common.ResultMsg;
import cs.model.PageModelDto;
import cs.model.expert.*;
import cs.model.financial.FinancialManagerDto;
import cs.repository.odata.ODataObj;

import java.util.List;

/**
 * Description: 抽取专家 业务操作接口
 * author: ldm
 * Date: 2017-6-14 14:26:49
 */
public interface ExpertSelectedService {

	void save(ExpertSelectedDto record);

	ResultMsg update(ExpertSelectedDto record);

	ExpertSelectedDto findById(String deptId);

	ResultMsg delete(String conditionId, String id, boolean deleteAll);

	PageModelDto<ExpertSelectedDto> get(ODataObj odataObj);

	ResultMsg expertCostTotal(ExpertCostCountDto expertCostCountDto);

	ResultMsg expertCostDetailTotal(ExpertCostDetailCountDto expertCostDetailCountDto);

	List<ExpertCostDetailCountDto> getExpertCostDetailById(String expertId,String beginTime,String endTime);

	ResultMsg projectReviewCost(ProjectReviewCostDto projectReviewCostDto);

	List<FinancialManagerDto> getFinancialManagerByBusid(String businessId);

	ResultMsg proReviewClassifyCount(ProjectReviewCostDto projectReviewCostDto,int page);

	/**
	 * 项目评审情况统计
	 * @param projectReviewConditionDto
	 * @return
	 */
	ResultMsg proReviewConditionCount(ProReviewConditionDto projectReviewConditionDto);

	/**
	 * 根据业务ID统计已经确认的抽取专家
	 * @param minBusinessId
	 * @return
	 */
	int getSelectEPCount(String minBusinessId);
	/**
	 *专家评审基本情况详细统计
	 * @param expertReviewCondDto
	 * @return
	 */
	ResultMsg expertReviewCondDetailCount(ExpertReviewCondDto expertReviewCondDto);

	/**
	 * 专家评审基本情况综合统计
	 * @param expertReviewConSimpleDto
	 * @return
	 */
	ResultMsg expertReviewConSimpleCount(ExpertReviewConSimpleDto expertReviewConSimpleDto);

	/**
	 * 专家评审情况不规则统计
	 * @param expertReviewConSimpleDto
	 * @return
	 */
	ResultMsg expertReviewConComplicatedCount(ExpertReviewConSimpleDto expertReviewConSimpleDto);

	/**
	 * 查询项目评审费录入的项目信息列表
	 * @return
	 */
	List<ProjectReviewCostDto> findProjectRevireCost(ProjectReviewCostDto projectReviewCost);

	/**
	 * 项目评审情况统计(按类别)
	 * @param projectReviewConditionDto
	 * @return
	 */
	ResultMsg proReviewConditionByTypeCount(ProReviewConditionDto projectReviewConditionDto);

	/**
	 * 项目评审情况统计汇总
	 * @param projectReviewConditionDto
	 * @return
	 */
	ProReviewConditionDto proReviewConditionSum(ProReviewConditionDto projectReviewConditionDto);

	/**
	 * 项目评审情况明细
	 * @param projectReviewConditionDto
	 * @return
	 */
	List<ProReviewConditionDto> proReviewConditionDetail(ProReviewConditionDto projectReviewConditionDto);

	/**
	 * 项目评审情况汇总(按照申报投资金额)
	 * @param beginTime
	 * @param  endTime
	 * @return
	 */
	Integer[]  proReviewCondByDeclare(String beginTime , String endTime);

	/**
	 * 专家评审会次数
	 * @param projectReviewConditionDto
	 * @return
	 */
	Integer proReviewMeetingCount(ProReviewConditionDto projectReviewConditionDto);

	/**
	 * 项目签收次数
	 * @param projectReviewConditionDto
	 * @return
	 */
	Integer proReviewCount(ProReviewConditionDto projectReviewConditionDto);

	/**
	 * 获取提前介入评审情况
	 *
	 * @param projectReviewConditionDto
	 * @return
	 */
	ProReviewConditionDto getAdvancedCon(ProReviewConditionDto projectReviewConditionDto);
}
