package cs.repository.repositoryImpl.history;

import cs.domain.history.ExpertReviewHis;
import cs.repository.IRepository;

public interface ExpertReviewHisRepo extends IRepository<ExpertReviewHis, String> {

    /**
     * 根据工作方案查询评审方案信息
     * @param signId
     * @return
     */
    ExpertReviewHis findByBusinessId(String signId);
}
