package cs.service.project;

import cs.model.project.ProjectStopDto;

public interface ProjectStopService {
	
	void addProjectStop(String signid);
	void projectStart(String signid);
}
