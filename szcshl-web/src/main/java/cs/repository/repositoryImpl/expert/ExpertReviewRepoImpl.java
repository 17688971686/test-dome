package cs.repository.repositoryImpl.expert;

import cs.domain.expert.ExpertReview;
import cs.domain.expert.ExpertReview_;
import cs.domain.project.WorkProgram_;
import cs.repository.AbstractRepository;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.criterion.Restrictions;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.stereotype.Repository;

/**
 * Description: 专家评审 数据操作实现类
 * author: ldm
 * Date: 2017-5-17 14:02:25
 */
@Repository
public class ExpertReviewRepoImpl extends AbstractRepository<ExpertReview, String> implements ExpertReviewRepo {

    /**
     * 根据工作方案ID查找评审方案信息
     * @param workProgramId
     * @return
     */
    @Override
    public ExpertReview findByWPId(String workProgramId) {
        Criteria criteria = getExecutableCriteria();
        criteria.add(Restrictions.sqlRestriction(ExpertReview_.id.getName()+" = (select "+ WorkProgram_.expertReviewId.getName()+" from cs_work_program where "+ WorkProgram_.id.getName()+" = ? )",workProgramId, StandardBasicTypes.STRING));
        return (ExpertReview) criteria.uniqueResult();
    }
}