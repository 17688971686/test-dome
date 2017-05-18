package cs.repository.repositoryImpl.expert;

import org.springframework.stereotype.Repository;

import cs.domain.expert.ExpertReview;
import cs.repository.AbstractRepository;

/**
 * Description: 专家评审 数据操作实现类
 * author: ldm
 * Date: 2017-5-17 14:02:25
 */
@Repository
public class ExpertReviewRepoImpl extends AbstractRepository<ExpertReview, String> implements ExpertReviewRepo {
}