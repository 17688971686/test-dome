package cs.service.project;

import java.util.List;
import java.util.Map;

import cs.common.ResultMsg;
import cs.domain.project.WorkProgram;
import cs.model.project.WorkProgramDto;

public interface WorkProgramService {

    ResultMsg save(WorkProgramDto workProgramDto, Boolean isNeedWorkProgram);

    Map<String,Object> initWorkProgram(String signId);

    /**
     * 根据当前负责人，删除工作方案信息
     * @param signId
     * @return
     */
    ResultMsg deleteBySignId(String signId);

    void delete(String id);

    /**
     * 生成会前准备材料
     * @param signId
     * @return
     */
    ResultMsg createMeetingDoc(String signId);

    /**
     * 根据主键查询工作方案信息
     * @param workId
     * @return
     */
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

    /**
     * 专家评审方式修改
     * @param signId            项目ID
     * @param workprogramId     工作方案ID
     * @param reviewType        新的评审方式
     * @return
     */
    ResultMsg updateReviewType(String signId, String workprogramId, String reviewType);
}
