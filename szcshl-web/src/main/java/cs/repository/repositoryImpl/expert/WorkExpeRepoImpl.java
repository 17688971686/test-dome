package cs.repository.repositoryImpl.expert;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import cs.domain.expert.WorkExpe;
import cs.domain.expert.WorkExpe_;
import cs.repository.AbstractRepository;
@Repository
public class WorkExpeRepoImpl extends AbstractRepository<WorkExpe, String> implements WorkExpeRepo {
	@Override
	public WorkExpe findWorkByName(String workName) {
		Criteria criteria = this.getSession().createCriteria(WorkExpe.class);
		criteria.add(Restrictions.eq(WorkExpe_.companyName.getName(), workName));
		List<WorkExpe> works = criteria.list();
		if (works.size() > 0) {
			return works.get(0);
		} else {
			return null;
		}
	}
}
