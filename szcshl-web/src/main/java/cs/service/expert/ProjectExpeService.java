package cs.service.expert;

import java.util.List;

import cs.common.ResultMsg;
import cs.model.expert.ProjectExpeDto;
import cs.repository.odata.ODataObj;

public interface ProjectExpeService  {
	 List<ProjectExpeDto> getProject(ODataObj odataObj);
	 void deleteProject(String id);
	 void updateProject(ProjectExpeDto projectExpeDto);
    ResultMsg saveProject(ProjectExpeDto project);
}
