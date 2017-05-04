package cs.service;

import java.util.List;

import cs.model.ProjectExpeDto;
import cs.repository.odata.ODataObj;

public interface ProjectExpeService  {
	 List<ProjectExpeDto> getProject(ODataObj odataObj);
	 void createProject(ProjectExpeDto projectExpeDto);
	 void deleteProject(String id);
	 void deleteProject(String[] ids);
	 void updateProject(ProjectExpeDto projectExpeDto);
}
