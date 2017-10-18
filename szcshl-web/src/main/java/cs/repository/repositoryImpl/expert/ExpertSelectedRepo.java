package cs.repository.repositoryImpl.expert;

import cs.common.ResultMsg;
import cs.domain.expert.ExpertSelected;
import cs.model.expert.ExpertReviewConSimpleDto;
import cs.model.expert.ExpertReviewCondDto;
import cs.model.expert.ExpertSelectHis;
import cs.repository.IRepository;

import java.util.List;

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
     * 根据业务ID统计已经确认的抽取专家
     * @param businessId
     * @return
     */
    int getSelectEPCount(String businessId);

    /**
     * 专家抽取统计
     * @param expertSelectHis
     * @return
     */
    List<Object[]> getSelectHis(ExpertSelectHis expertSelectHis);

    /**
     *专家评审基本情况详细统计
     * @param expertReviewCondDto
     * @return
     */
    ResultMsg expertReviewCondDetailCount(ExpertReviewCondDto expertReviewCondDto);

    /**
     * 专家评审基本情况详细综合
     * @param expertReviewConSimpleDto
     * @return
     */
    ResultMsg expertReviewConSimpleCount(ExpertReviewConSimpleDto expertReviewConSimpleDto);

    /**
     * 专家评审基本情况不规则
     * @param expertReviewConSimpleDto
     * @return
     */
    ResultMsg expertReviewConComplicatedCount(ExpertReviewConSimpleDto expertReviewConSimpleDto);
}
