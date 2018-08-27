package cs.service.history;

import cs.domain.history.WorkProgramHis;
import cs.domain.project.WorkProgram;
import cs.model.history.WorkProgramHisDto;
import cs.model.project.WorkProgramDto;

import java.util.List;

public interface WorkProgramHisService{

    /**
     * 工作方案留痕
     * @param workProgram
     * @return
     */
    boolean copyWorkProgram(WorkProgram workProgram,String signId,String newWorkProgramId) throws RuntimeException;

    /**
     * 根据项目ID和分支，查询历史信息
     * @param signid
     * @param branchId
     * @return
     */
    List<WorkProgramHisDto> findBySignAndBranch(String signid, String branchId);


    /**
     * 根据ID查询历史记录信息
     * @param wpId
     * @return
     */
    WorkProgramHis findById(String wpId);

    /**
     * 历史记录Dto之间转换
     * @param workProgramDto
     * @param his
     */
    void initWPMeetingExp(WorkProgramDto workProgramDto, WorkProgramHis his);
}
