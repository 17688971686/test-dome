package cs.repository.repositoryImpl.expert;


import cs.common.Constant;
import cs.common.HqlBuilder;
import cs.domain.expert.ExpertReview;
import cs.domain.expert.ExpertReview_;
import cs.domain.expert.ExpertSelected_;
import cs.repository.IRepository;

import java.util.Date;

/**
 * Description: 专家评审 数据操作实现接口
 * author: ldm
 * Date: 2017-5-17 14:02:25
 */
public interface ExpertReviewRepo extends IRepository<ExpertReview, String> {

    void updateReviewDate(String businessId, String businessType, Date rbDate);

    ExpertReview findByBusinessId(String businessId);

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
}
