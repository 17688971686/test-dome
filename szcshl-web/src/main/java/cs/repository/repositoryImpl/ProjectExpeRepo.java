package cs.repository.repositoryImpl;

import cs.domain.ProjectExpe;
import cs.repository.IRepository;

public interface ProjectExpeRepo extends IRepository<ProjectExpe, String>{
	ProjectExpe findProjectByName(String projectName);
}
