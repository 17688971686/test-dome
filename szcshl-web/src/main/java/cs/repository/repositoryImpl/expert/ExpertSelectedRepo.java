package cs.repository.repositoryImpl.expert;

import cs.common.ResultMsg;
import cs.domain.expert.ExpertSelected;
import cs.model.expert.ExpertReviewConSimpleDto;
import cs.model.expert.ExpertReviewCondDto;
import cs.repository.IRepository;

/**
 * Description: 抽取专家 数据操作实现接口
 * author: ldm
 * Date: 2017-6-14 14:26:49
 */
public interface ExpertSelectedRepo extends IRepository<ExpertSelected, String> {
    /**
     * 根据大类，小类和专家类别确认已经抽取的专家
     * @param reviewId
     * @param maJorBig
     * @param maJorSmall
     * @param expeRttype
     * @return
     */
    int findConfirmSeletedEP(String reviewId,String maJorBig,String maJorSmall,String expeRttype);

    /**
     *专家评审基本情况详细统计
     * @param expertReviewCondDto
     * @return
     */
    ResultMsg expertReviewCondDetailCount(ExpertReviewCondDto expertReviewCondDto);

    ResultMsg expertReviewConSimpleCount(ExpertReviewConSimpleDto expertReviewConSimpleDto);
}
