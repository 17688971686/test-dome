package cs.repository.repositoryImpl.history;

import cs.common.utils.Validate;
import cs.domain.expert.ExpertReview;
import cs.domain.expert.ExpertReview_;
import cs.domain.history.ExpertReviewHis;
import cs.domain.history.ExpertReviewHis_;
import cs.repository.AbstractRepository;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ExpertReviewHisRepoImpl extends AbstractRepository<ExpertReviewHis,String> implements ExpertReviewHisRepo {

    @Override
    public ExpertReviewHis findByBusinessId(String businessId) {
        Criteria criteria = getExecutableCriteria();
        criteria.add(Restrictions.eq(ExpertReviewHis_.businessId.getName(), businessId));
        List<ExpertReviewHis> list = criteria.list();
        if (Validate.isList(list)) {
            return list.get(0);
        } else {
            return null;
        }
    }
}
