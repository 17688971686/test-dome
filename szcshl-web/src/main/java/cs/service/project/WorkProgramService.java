package cs.service.project;

import cs.model.project.WorkProgramDto;

public interface WorkProgramService {

	void save(WorkProgramDto workProgramDto) throws Exception ;

	WorkProgramDto initWorkBySignId(String signId,String isMain);

}
