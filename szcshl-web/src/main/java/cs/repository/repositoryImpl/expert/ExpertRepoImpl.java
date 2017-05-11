package cs.repository.repositoryImpl.expert;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;

import cs.domain.expert.Expert;
import cs.domain.expert.Expert_;
import cs.repository.AbstractRepository;
@Service
public class ExpertRepoImpl extends AbstractRepository<Expert,String> implements ExpertRepo {
	@Override
	public Expert findExpertByName(String expertName) {
		Criteria criteria = this.getSession().createCriteria(Expert.class);
		criteria.add(Restrictions.eq(Expert_.name.getName(), expertName));
		List<Expert> experts = criteria.list();
		if (experts.size() > 0) {
			return experts.get(0);
		} else {
			return null;
		}
	}

	@Override
	public List<Expert> findAllRepeat() {
		Criteria criteria = getExecutableCriteria();
		criteria.add(Restrictions.sqlRestriction(" name IN (SELECT name FROM CS_EXPERT GROUP BY  name  HAVING COUNT (name) > 1)"));
		return criteria.list();
	}
}
