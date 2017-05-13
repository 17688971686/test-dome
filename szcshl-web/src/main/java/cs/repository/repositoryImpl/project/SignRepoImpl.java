package cs.repository.repositoryImpl.project;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import cs.domain.expert.Expert;
import cs.domain.expert.Expert_;
import cs.domain.project.Sign;
import cs.repository.AbstractRepository;

@Repository
public class SignRepoImpl  extends AbstractRepository<Sign, String> implements SignRepo {
	public Expert findDispatchBysignId(String expertName) {
		Criteria criteria = this.getSession().createCriteria(Expert.class);
		criteria.add(Restrictions.eq(Expert_.name.getName(), expertName));
		List<Expert> experts = criteria.list();
		if (experts.size() > 0) {
			return experts.get(0);
		} else {
			return null;
		}
	}
}
