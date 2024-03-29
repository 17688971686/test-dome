package cs.repository.repositoryImpl.project;

import cs.domain.project.WorkProgram;
import cs.model.project.ProMeetDto;
import cs.model.project.WorkProgramDto;
import cs.repository.IRepository;

import java.util.Date;
import java.util.List;

public interface WorkProgramRepo extends IRepository<WorkProgram, String> {

    WorkProgram findByPrincipalUser(String signId);

    /**
     * 根据项目ID和分支查询工作方案信息
     * @param signId        项目ID
     * @param branchId      分支
     * @param isBaseInfo    是否项目基本信息
     * @return
     */
    WorkProgram findBySignIdAndBranchId(String signId, String branchId, boolean isBaseInfo);

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

    /**
     * 获取工作方案调研及会议信息
     * @return
     */
    List<ProMeetDto> findAmProMeetInfo();

    /**
     * 获取工作方案调研及会议信息
     * @return
     */
    List<ProMeetDto> findPmProMeetInfo();

    /**
     * 删除工作方案及会议和专家评审方案等信息
     * @param signId
     */
    void removeWPCascade(String signId,String brandId);

    /**
     * 更新工作方案状态
     * @param signId
     * @param brandIds
     */
    void updateWPState(String signId, String brandIds,String state);

    /**
     * 更新工作方案状态
     * @param excludeId 排除的工作方案ID
     * @param branchId 分支
     * @param value 要修改的状态值
     * @param signid 项目ID
     */
    void updateBrandWPState(String excludeId, String branchId, String value,String signid);
}
