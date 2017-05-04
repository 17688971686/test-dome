package cs.repository.repositoryImpl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;

import cs.domain.Expert;
import cs.domain.User;
import cs.domain.User_;
import cs.repository.AbstractRepository;
@Service
public class ExpertRepoImpl extends AbstractRepository<Expert,String> implements ExpertRepo {
	@Override
	public Expert findExpertByName(String expertName) {
		Criteria criteria = this.getSession().createCriteria(Expert.class);
		criteria.add(Restrictions.eq("name", expertName));
		List<Expert> experts = criteria.list();
		if (experts.size() > 0) {
			return experts.get(0);
		} else {
			return null;
		}
	}
}
