package cs.repository.repositoryImpl.history;

import cs.domain.history.WorkProgramHis;
import cs.model.history.WorkProgramHisDto;
import cs.repository.IRepository;

import java.util.List;

public interface WorkProgramHisRepo extends IRepository<WorkProgramHis, String> {

    /**
     * 初始化会议室信息和抽取专家信息
     * @param workProgramHisDto
     * @param workProgramHis
     */
    void initWPMeetingExp(WorkProgramHisDto workProgramHisDto, WorkProgramHis workProgramHis);

    /**
     * 根据项目ID和分支查询历史记录信息
     * @param signid
     * @param branchId
     * @return
     */
    List<WorkProgramHis> findBySignAndBranch(String signid, String branchId);
}
