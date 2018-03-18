package cs.service.expert;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import cs.common.ResultMsg;
import cs.domain.expert.ExpertReview;
import cs.domain.expert.ExpertSelected;
import cs.model.PageModelDto;
import cs.model.expert.ExpertDto;
import cs.model.expert.ExpertNewInfoDto;
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

    /**
     * 根据专家ID和月份，获取对应月份的评审费
     * @param expertIds
     * @param month
     * @return
     */
    List<Map<String,Object>> getExpertReviewCost(String expertIds, String month);

    /**
     * 根据专家评审方案ID和月份获取专家对应月份的评审费
     * @param expertReviewId
     * @param month
     * @return
     */
    List<Object[]> countExpertReviewCost(String expertReviewId, String month);

    void saveExpertReviewCost(ExpertReviewDto[]  expertReviews);

	ResultMsg saveExpertReviewCost(ExpertReviewDto  expertReview);

	/**
	 * 保存最新的专家信息
	 * @param expertNewInfoDto
	 * @return
	 */

	ResultMsg saveExpertNewInfo(ExpertNewInfoDto expertNewInfoDto);

	/**
	 * 查询专家评审费超期发放的信息
	 * @param oDataObj
	 * @return
	 */
	PageModelDto<ExpertReviewDto> findOverTimeReview(ODataObj oDataObj);

	/**
	 * 查询业务的专家信息
	 * @param businessId
	 * @return
	 */
    List<ExpertDto> refleshBusinessEP(String businessId);


	/**
	 * 查询超期未办理专家评审费的方法
	 * @return
	 */
	List<ExpertReview> queryUndealReview();

    /**
     * 级联保存
     * @param expertReview
     */
    void save(ExpertReview expertReview);
}
