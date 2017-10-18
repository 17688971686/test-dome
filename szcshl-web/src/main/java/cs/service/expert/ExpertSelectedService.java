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

	void update(ExpertSelectedDto record);

	ExpertSelectedDto findById(String deptId);

	ResultMsg delete(String conditionId, String id, boolean deleteAll);

	PageModelDto<ExpertSelectedDto> get(ODataObj odataObj);

	ResultMsg expertCostTotal(ExpertCostCountDto expertCostCountDto);

	ResultMsg expertCostDetailTotal(ExpertCostDetailCountDto expertCostDetailCountDto);

	List<ExpertCostDetailCountDto> getExpertCostDetailById(String expertId,String beginTime,String endTime);

	ResultMsg projectReviewCost(ProjectReviewCostDto projectReviewCostDto);

	List<FinancialManagerDto> getFinancialManagerByBusid(String businessId);

	ResultMsg proReviewClassifyCount(ProjectReviewCostDto projectReviewCostDto);

	/**
	 * 项目评审情况统计
	 * @param projectReviewConditionDto
	 * @return
	 */
	ResultMsg proReviewConditionCount(ProReviewConditionDto projectReviewConditionDto);

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

}
