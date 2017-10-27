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
     * 完成分支流程
     * @param signId
     * @param branchId
     */
    void finishBranch(String signId,String branchId);
    /**
     * 分支是否需要工作方案
     * @param signId
     * @param branchId
     * @param state
     */
    void isNeedWP(String signId,String branchId,String state);

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

    List<OrgDept> getOrgDeptBySignId(String signId);
}
