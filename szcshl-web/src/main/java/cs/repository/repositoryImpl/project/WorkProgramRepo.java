package cs.repository.repositoryImpl.project;

import cs.domain.project.WorkProgram;
import cs.domain.sys.Org;
import cs.model.project.WorkProgramDto;
import cs.repository.IRepository;

import java.util.List;

public interface WorkProgramRepo extends IRepository<WorkProgram, String> {

    WorkProgram findByPrincipalUser(String signId);

    WorkProgram findBySignIdAndBranchId(String signId, String branchId);

    void initExpertCost(String id);

    void updateWPReivewType(String signId,String isSigle, String isMain, String mergeIds);

    /**
     * 根据项目ID获取合并评审主工作方案信息
     * @param signId
     * @return
     */
    WorkProgram findMainReviewWP(String signId);

    /**
     * 初始化工作方案的会议室信息和专家信息
     * @param workProgramDto
     * @param wp
     */
    void initWPMeetingExp(WorkProgramDto workProgramDto, WorkProgram wp);

    /**
     * 根据合并评审主项目ID，获取合并评审次项目的工作方案信息
     * @param signid
     * @return
     */
    List<WorkProgram> findMergeWP(String signid);
}
