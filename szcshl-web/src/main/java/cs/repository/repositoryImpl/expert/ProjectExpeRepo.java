package cs.repository.repositoryImpl.expert;

import cs.domain.expert.ProjectExpe;
import cs.repository.IRepository;

public interface ProjectExpeRepo extends IRepository<ProjectExpe, String>{
	ProjectExpe findProjectByName(String projectName);
}
