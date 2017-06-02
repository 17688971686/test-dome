package cs.service.project;

import java.util.List;

import cs.model.project.SignDto;
import cs.model.project.WorkProgramDto;

public interface WorkProgramService {

	void save(WorkProgramDto workProgramDto) throws Exception ;

	WorkProgramDto initWorkBySignId(String signId,String isMain);

	List<SignDto> waitProjects(SignDto signDto);

	List<SignDto> selectedProject(String[] ids);

	void mergeAddWork(String signId, String linkSignId);

}
