package cs.repository.repositoryImpl.project;

import cs.domain.project.SignBranch;
import cs.domain.sys.OrgDept;
import cs.domain.sys.User;
import cs.repository.IRepository;

import java.util.List;

public interface SignBranchRepo extends IRepository<SignBranch, String> {

    /**
     * 完成分支工作方案
     * @param signId
     * @param branchId
     */
    void finishWP(String signId,String branchId);

    /**
     * 验证是否完成了工作方案
     * @param signId
     * @param branchId
     * @return
     */
    boolean checkFinishWP(String signId,String branchId);

    /**
     * 更新分支流程状态
     * @param signId
     * @param branchId
     */
    void updateFinishState(String signId,String branchId,String state);
    /**
     * 分支是否需要工作方案
     * @param signId
     * @param branchId
     * @param state
     */
    void isNeedWP(String signId,String branchId,String state);

    /**
     * 校验是否需要工作方案
     * @param signId
     * @param branchId
     * @return
     */
    boolean checkIsNeedWP(String signId,String branchId);

    /**
     * 协审分支是否全部完成
     * @param signId
     * @return
     */
    boolean assistFlowFinish(String signId);

    /**
     * 所有协审分支（除了主分支）
     * @param signId
     * @return
     */
    int allAssistCount(String signId);

    int finishAssistCount(String signId);

    boolean allBranchFinish(String signId);

    /**
     * 是否已完成所有的工作方案
     * @param signId
     * @return
     */
    boolean allWPFinish(String signId);

    /**
     * 判断分支是否有工作方案
     * @param signId
     * @return
     */
    boolean isHaveWP(String signId);

    /**
     * 查询分支的部门领导
     * @param signId
     * @return
     */
    List<User> findAssistOrgDirector(String signId);

    User findMainOrgDirector(String signId);

    /**
     * 查询分支的分管副主任
     * @param signId
     * @return
     */
    List<User> findAssistSLeader(String signId);

    User findMainSLeader(String signId);

    /**
     * 根据项目ID和分支ID查询部门信息
     * @param signId
     * @param branchId
     * @return
     */
    List<OrgDept> getOrgDeptBySignId(String signId,String branchId);

    /**
     * 根据部门ID和项目ID，查询所在分支信息
     * @param signId
     * @param orgId
     * @return
     */
    SignBranch findByOrgDirector(String signId,String orgId);

    /**
     * 重置分支状态
     * @param signId
     * @param branchId
     */
    void resetBranchState(String signId,String branchId);

    /**
     * 统计分办部门个数
     * @param signid
     * @return
     */
    int countBranch(String signid);

    /**
     * 获取项目分办部门的名称
     * @param signid
     * @return
     */
    String getOrgDeptNameBySignId(String signid,String branchId);

    /**
     * 统计需要做工作方案的分支
     * @param signid
     * @return
     */
    int countNeedWP(String signid);

    /**
     * 根据项目ID和分支ID查询分支信息
     * @param signId
     * @param branchId
     * @return
     */
    SignBranch findBySignIdAndBranchId(String signId,String branchId);
}
