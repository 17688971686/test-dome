package cs.service.project;

import java.util.Map;

import cs.common.ResultMsg;
import cs.domain.project.WorkProgram;
import cs.model.project.WorkProgramDto;

public interface WorkProgramService {

    ResultMsg save(WorkProgramDto workProgramDto, Boolean isNeedWorkProgram);

    Map<String,Object> initWorkProgram(String signId);

    ResultMsg deleteBySignId(String signId);

    void delete(String id);

    ResultMsg createMeetingDoc(String signId, String workprogramId);

    WorkProgramDto initWorkProgramById(String workId);

    void initWorkProgramDto(WorkProgram workProgram, WorkProgramDto workProgramDto);

    WorkProgramDto findByPrincipalUser(String signId);
}
