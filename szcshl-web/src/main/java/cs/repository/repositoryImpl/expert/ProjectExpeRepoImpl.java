package cs.repository.repositoryImpl.expert;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import cs.domain.expert.ProjectExpe;
import cs.domain.expert.ProjectExpe_;
import cs.repository.AbstractRepository;
@Repository
public class ProjectExpeRepoImpl extends AbstractRepository<ProjectExpe, String> implements ProjectExpeRepo {
	@Override
	public ProjectExpe findProjectByName(String projectName) {
		Criteria criteria = getExecutableCriteria();
		criteria.add(Restrictions.eq(ProjectExpe_.projectName.getName(), projectName));
		List<ProjectExpe> projects = criteria.list();
		if (projects.size() > 0) {
			return projects.get(0);
		} else {
			return null;
		}
	}
}
