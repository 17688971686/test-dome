package cs.service.expert;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import cs.common.ResultMsg;
import cs.domain.expert.ExpertReview;
import cs.domain.expert.ExpertSelected;
import cs.model.PageModelDto;
import cs.model.expert.*;
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
	/**
	 * 校验是否发送评审费
	 * @param signId
	 * @return
	 */
	boolean checkReviewCost(String signId);

	ResultMsg save(String businessId,String minBusinessId,String businessType,String reviewId, String expertIds, String selectType);

	/**
	 * 更改专家是否参加状态
	 * @param reviewId
	 * @param minBusinessId
	 * @param businessType
	 * @param expertSelId
	 * @param state
	 */
    ResultMsg updateJoinState(String reviewId,String minBusinessId,String businessType,String expertSelId,String state);

	/**
     * 更改专家抽取是否确认字段
     * @param minBusinessId
     * @param businessType
     * @param expertSelId
     * @param state
     */
    void updateConfirmState(String reviewId,String minBusinessId, String businessType, String expertSelId, String state);
    /**
     * 根据专家ID和月份，获取对应月份的评审费
     * @param expertIds
     * @param month
     * @return
     */
    List<Object[]> getExpertReviewCost(String reviewId,String expertIds, String month);

	//List<Map<String,Object>> getExpertInfo();


    /**
     * 根据专家评审方案ID和月份获取专家对应月份的评审费
     * @param expertReviewId
     * @param month
     * @return
     */
   /* List<Object[]> countExpertReviewCost(String expertReviewId, String month);*/

    //void saveExpertReviewCost(ExpertReviewDto[]  expertReviews);

	/**
	 * 保存专家评审费用
	 * @param expertReview
	 * @param isCountTaxes 是否计税
	 * @return
	 */
	ResultMsg saveExpertReviewCost(ExpertReviewDto  expertReview,boolean isCountTaxes);

	/**
	 * 计算此次专家评审费用
	 * @param expertReview
	 * @return
	 */
	List<Map<String,Object>> countReviewExpense(ExpertReview expertReview);
	/**
	 * 保存最新的专家信息
	 * @param expertSelectedDtos
	 * @return
	 */

	ResultMsg saveExpertNewInfo(ExpertSelectedDto[] expertSelectedDtos);

	/**
	 * 删除新专家信息
	 * @param minBusinessId
	 */
	void deleteExpertNewInfo(String minBusinessId);

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

	List<ExpertNewInfoDto> getExpertInfo(String businessId);

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

	/**
	 * 保存专家评审费发放打印方案
	 * @param expertSelectedDto
	 */
	void saveSplit(ExpertSelectedDto expertSelectedDto);


    /**
     * 并不是按计算公式算，只是单纯计算评审费，税费，和总费用
     * @param expertReview
     * @return
     */
    List<Map<String,Object>> countTotalExpense(ExpertReview expertReview);
}
