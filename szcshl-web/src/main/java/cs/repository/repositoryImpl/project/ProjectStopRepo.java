package cs.repository.repositoryImpl.project;

import java.util.List;

import cs.domain.project.ProjectStop;
import cs.repository.IRepository;

public interface ProjectStopRepo extends IRepository<ProjectStop, String> {
	List<ProjectStop> getProjectStop(String signid,String ispause);

}
