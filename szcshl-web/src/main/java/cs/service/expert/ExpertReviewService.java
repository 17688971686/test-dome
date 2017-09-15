package cs.service.expert;

import java.util.List;
import java.util.Map;

import cs.common.ResultMsg;
import cs.domain.expert.ExpertReview;
import cs.domain.expert.ExpertSelected;
import cs.model.PageModelDto;
import cs.model.expert.ExpertReviewDto;
import cs.model.expert.ExpertSelectedDto;
import cs.repository.odata.ODataObj;

/**
 * Description: 专家评审 业务操作接口
 * author: ldm
 * Date: 2017-5-17 14:02:25
 */
public interface ExpertReviewService {
    
    PageModelDto<ExpertReviewDto> get(ODataObj odataObj);

	void update(ExpertReviewDto record);

	ExpertReviewDto findById(String deptId);

	void delete(String id);

	ExpertReviewDto initBybusinessId(String businessId,String minBusinessId);

	ResultMsg save(String businessId,String minBusinessId,String businessType,String reviewId, String expertIds, String selectType);

	void updateExpertState(String minBusinessId,String businessType,String expertSelId,String state,boolean isConfirm);

    List<Map<String,Object>> getExpertReviewCost(String expertIds, String month);

    void saveExpertReviewCost(ExpertReviewDto[]  expertReviews);

	ResultMsg saveExpertReviewCost(ExpertReviewDto  expertReview);
}
