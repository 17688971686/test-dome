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
	public List<Expert> findExpertByIdCard(String idCard) {
		Criteria criteria = getExecutableCriteria();
		criteria.add(Restrictions.eq(Expert_.idCard.getName(), idCard));
		return  criteria.list();		
	}

	@Override
	public List<Expert> findAllRepeat() {
		Criteria criteria = getExecutableCriteria();
		criteria.add(Restrictions.sqlRestriction(" name IN (SELECT name FROM CS_EXPERT GROUP BY  name  HAVING COUNT (name) > 1)"));
		return criteria.list();
	}
}
