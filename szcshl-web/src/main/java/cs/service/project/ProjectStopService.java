package cs.service.project;

import cs.model.project.ProjectStopDto;

public interface ProjectStopService {
	
	void addProjectStop(String signid,String taskid);
	void projectStart(String signid,String taskid);
}
