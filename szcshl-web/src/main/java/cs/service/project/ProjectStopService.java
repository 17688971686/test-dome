package cs.service.project;

import java.util.List;

import cs.domain.project.ProjectStop;

public interface ProjectStopService {
	
	/*void addProjectStop(String signid,String taskid);
	void projectStart(String signid,String taskid);*/
	
	List<ProjectStop> findProjectStopBySign(String signId);
}
