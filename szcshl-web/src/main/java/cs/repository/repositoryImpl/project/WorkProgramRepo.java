package cs.repository.repositoryImpl.project;

import cs.domain.project.WorkProgram;
import cs.domain.sys.Org;
import cs.model.project.WorkProgramDto;
import cs.repository.IRepository;

import java.util.Date;
import java.util.List;

public interface WorkProgramRepo extends IRepository<WorkProgram, String> {

    WorkProgram findByPrincipalUser(String signId);

    WorkProgram findBySignIdAndBranchId(String signId, String branchId);

    void initExpertCost(String id);

    /**
     * 把所有被合并的项目改为合并评审次项目，同时评审方式也要跟主项目一致
     * @param signId
     * @param isSigle
     * @param isMain
     * @param mergeIds
     */
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

    /**
     * 根据ID判断是否是想验证的评审方式
     * @param reviewType
     * @param workProgramId
     * @return
     */
    boolean checkReviewType(String reviewType, String workProgramId);

    /**
     * 拟补充资料函状态
     * @param businessId
     * @param value
     * @param disapDate
     */
    void updateSuppLetterState(String businessId, String value, Date disapDate);

    /**
     * 通过业务ID判断是不是主分支
     * @param signId
     * @return
     */
    Boolean mainBranch(String signId);
}
