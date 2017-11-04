package cs.service.project;

import java.util.List;
import java.util.Map;

import cs.common.ResultMsg;
import cs.domain.project.WorkProgram;
import cs.model.project.WorkProgramDto;

public interface WorkProgramService {

    ResultMsg save(WorkProgramDto workProgramDto, Boolean isNeedWorkProgram);

    Map<String,Object> initWorkProgram(String signId);

    ResultMsg deleteBySignId(String signId);

    void delete(String id);

    /**
     * 生成会前准备材料
     * @param signId
     * @return
     */
    ResultMsg createMeetingDoc(String signId);

    WorkProgramDto initWorkProgramById(String workId);

    /**
     * 通过项目负责人获取项目信息
     * @param signId
     * @return
     */
    WorkProgramDto findByPrincipalUser(String signId);

    /**
     * 根据合并评审主项目ID，获取合并评审次项目的工作方案信息
     * @param signid
     * @return
     */
    List<WorkProgramDto> findMergeWP(String signid);
}
