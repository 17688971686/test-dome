package cs.repository.repositoryImpl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import cs.domain.ProjectExpe;
import cs.repository.AbstractRepository;
@Repository
public class ProjectExpeRepoImpl extends AbstractRepository<ProjectExpe, String> implements ProjectExpeRepo {
	@Override
	public ProjectExpe findProjectByName(String projectName) {
		Criteria criteria = this.getSession().createCriteria(ProjectExpe.class);
		criteria.add(Restrictions.eq("projectName", projectName));
		List<ProjectExpe> projects = criteria.list();
		if (projects.size() > 0) {
			return projects.get(0);
		} else {
			return null;
		}
	}
}
