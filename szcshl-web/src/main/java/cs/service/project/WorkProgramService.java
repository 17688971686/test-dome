package cs.service.project;

import java.util.List;
import java.util.Map;

import cs.common.ResultMsg;
import cs.model.project.SignDto;
import cs.model.project.WorkProgramDto;

public interface WorkProgramService {

    ResultMsg save(WorkProgramDto workProgramDto, Boolean isNeedWorkProgram);

    WorkProgramDto initWorkProgram(String signId, String workProgramId);

    List<WorkProgramDto> waitSeleWP(String mainBusinessId);

    List<WorkProgramDto> getSeleWPByMainId(String mainBusinessId);

    void mergeWork(String mainBusinessId, String signId,String businessId,String linkSignId);

    void deleteMergeWork(String mainBusinessId, String businessId);

    void deleteBySignId(String signId);

    void delete(String id);

    ResultMsg createMeetingDoc(String signId, String workprogramId);

    WorkProgramDto initWorkProgramById(String workId);

}
