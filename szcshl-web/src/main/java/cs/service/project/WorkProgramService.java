package cs.service.project;

import java.util.List;
import java.util.Map;

import cs.model.project.SignDto;
import cs.model.project.WorkProgramDto;

public interface WorkProgramService {

	void save(WorkProgramDto workProgramDto,Boolean isNeedWorkProgram) throws Exception ;

	WorkProgramDto initWorkBySignId(String signId,String isMain);

	List<SignDto> waitProjects(SignDto signDto);

	List<SignDto> selectedProject(String[] ids);

	void mergeAddWork(String signId, String linkSignId);

	Map<String, Object> getInitSeleSignByIds(String bussnessId);

	Map<String, Object> getInitRelateData(String signId);


    void deleteBySignId(String signId);
}
