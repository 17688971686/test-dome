package cs.repository.repositoryImpl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import cs.domain.Expert;
import cs.domain.ProjectExpe;
import cs.domain.User;
import cs.domain.User_;
import cs.domain.WorkExpe;
import cs.repository.AbstractRepository;
@Repository
public class WorkExpeRepoImpl extends AbstractRepository<WorkExpe, String> implements WorkExpeRepo {
	@Override
	public WorkExpe findWorkByName(String workName) {
		Criteria criteria = this.getSession().createCriteria(WorkExpe.class);
		criteria.add(Restrictions.eq("companyName", workName));
		List<WorkExpe> works = criteria.list();
		if (works.size() > 0) {
			return works.get(0);
		} else {
			return null;
		}
	}
}
