package cs.service.expert;

import java.util.List;
import java.util.Map;

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

	void save(ExpertReviewDto record) throws Exception;

	void update(ExpertReviewDto record);

	ExpertReviewDto findById(String deptId);

	void delete(String id);

	ExpertReviewDto initByWorkProgramId(String workProgramId);

	void save(String reviewId,String expertIds, String selectType,boolean isDraw);

	void updateExpertState(String reviewId,String expertIds,String state,boolean isConfirm);
	
	Map<String,Object> getReviewList(String orgName,String year,String quarter);
	
	 void expertMark(ExpertReviewDto expertReviewDto );
	
	void savePayment(ExpertReviewDto expertReviewDto)throws Exception;

	void affirmAutoExpert(String reviewId, String state);

    PageModelDto<ExpertSelectedDto> getSelectExpert(String signId);

	PageModelDto<ExpertReviewDto> getBySignId(String signId);

    List<Map<String,Object>> getExpertReviewCost(String expertIds, String month);

    void saveExpertReviewCost(ExpertReviewDto[]  expertReviews);
}
