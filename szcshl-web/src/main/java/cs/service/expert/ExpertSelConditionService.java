package cs.service.expert;

import cs.common.ResultMsg;
import cs.model.PageModelDto;
import cs.model.expert.ExpertSelConditionDto;
import cs.repository.odata.ODataObj;

import java.util.List;
import java.util.Map;


/**
 * Description: 专家抽取条件 业务操作接口
 * author: ldm
 * Date: 2017-5-23 19:49:58
 */
public interface ExpertSelConditionService {
    
    PageModelDto<ExpertSelConditionDto> get(ODataObj odataObj);

	void save(ExpertSelConditionDto record);

	void update(ExpertSelConditionDto record);

	ExpertSelConditionDto findById(String deptId);

	ResultMsg delete(String ids);

	ResultMsg saveConditionList(String businessId,String minBusinessId,String businessType,String reviewId,ExpertSelConditionDto[] recordList);

	/**
	 * 统计专家抽取设定人数
	 * @param minBusinessId
	 * @return
	 */
	int getExtractEPCount(String minBusinessId);
}
