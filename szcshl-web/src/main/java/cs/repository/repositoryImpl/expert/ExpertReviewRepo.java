package cs.repository.repositoryImpl.expert;


import cs.domain.expert.ExpertReview;
import cs.model.expert.ExpertReviewDto;
import cs.model.expert.ExpertSelectedDto;
import cs.repository.IRepository;

import java.util.Date;
import java.util.List;

/**
 * Description: 专家评审 数据操作实现接口
 * author: ldm
 * Date: 2017-5-17 14:02:25
 */
public interface ExpertReviewRepo extends IRepository<ExpertReview, String> {

    void updateReviewDate(String businessId, String businessType, Date rbDate);

    /**
     * 根据业务ID查询评审方案信息
     * @param businessId
     * @return
     */
    ExpertReview findByBusinessId(String businessId);

    /**
     * 根据domain，初始化dto
     * @param expertReview
     * @return
     */
    ExpertReviewDto formatReview(ExpertReview expertReview);

    /**
     * 根据业务ID判断是否有专家评审费
     * 是否有专家评审费
     * @param businessId
     * @return
     */
    boolean isHaveEPReviewCost(String businessId);

    /**
     * 根据业务ID判断是否完成专家评分
     * 是否有专家评审费
     * @param businessId
     * @return
     */
    boolean isFinishEPGrade(String businessId);

    void initReviewTitle(ExpertReview expertReview, String businessId, String businessType);

    /**
     * 查询专家评审费超期发放的信息(停用)
     * @param  businessType
     * @return
     */
    @Deprecated
    List<ExpertReview> findReviewOverTime(String businessType);

    /**
     * 判断评审方式是否为空（即没有专家抽取条件，也没有抽取专家）
     * @param businessId
     * @return
     */
    boolean isReviewIsEmpty(String businessId);

    /**
     * 根据专家评审方案和月份，统计专家的收入
     * @param expertReviewId
     * @param month
     * @return
     */
    //List<Object[]> countExpertReviewCost(String expertReviewId, String month);

    /**
     * 查询未处理的评审方案
     * @return
     */
    List queryUndealReview();

    /**
     * 保存专家评审费发放打印方案
     * @param expertSelectedDto
     */
    void saveSplit(ExpertSelectedDto expertSelectedDto);

    /**
     * 根据业务ID删除
     * @param signId
     */
    void deleteByBusinessId(String signId);

}
