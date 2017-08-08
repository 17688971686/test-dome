package cs.repository.repositoryImpl.expert;

import java.util.List;

import cs.common.HqlBuilder;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;

import cs.domain.expert.Expert;
import cs.domain.expert.Expert_;
import cs.repository.AbstractRepository;

@Service
public class ExpertRepoImpl extends AbstractRepository<Expert, String> implements ExpertRepo {
    @Override
    public List<Expert> findExpertByIdCard(String idCard) {
        Criteria criteria = getExecutableCriteria();
        criteria.add(Restrictions.eq(Expert_.idCard.getName(), idCard));
        return criteria.list();
    }

    /**
     * 查询所有重名的专家
     * @return
     */
    @Override
    public List<Expert> findAllRepeat() {
        Criteria criteria = getExecutableCriteria();
        criteria.add(Restrictions.sqlRestriction(" name IN (SELECT name FROM CS_EXPERT GROUP BY  name  HAVING COUNT (name) > 1)"));
        criteria.addOrder(Order.desc(Expert_.name.getName()));

        return criteria.list();
    }

    /**
     * 根据工作方案ID 查询对应的拟聘请专家
     * @param wpId
     * @return
     */
    @Override
    public List<Expert> findByWorkProgramId(String wpId) {
        HqlBuilder sqlBuilder = HqlBuilder.create();
        sqlBuilder.append(" select e.* from cs_expert e where e.expertID in (select expertID from cs_expert_selected where expertReviewId = ");
        sqlBuilder.append(" (select expertReviewId from cs_work_program where id =:wpId ) ) ");
        sqlBuilder.setParam("wpId",wpId);

        return findBySql(sqlBuilder);
    }
}
