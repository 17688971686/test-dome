package cs.repository.repositoryImpl.project;

import cs.domain.project.SignBranch;
import cs.domain.sys.OrgDept;
import cs.domain.sys.User;
import cs.repository.IRepository;

import java.util.List;

public interface SignBranchRepo extends IRepository<SignBranch, String> {

    void finishWP(String signId,String branchId);

    boolean checkFinishWP(String signId,String branchId);

    void finishBranch(String signId,String branchId);

    void isNeedWP(String signId,String branchId,String state);

    boolean checkIsNeedWP(String signId,String branchId);

    boolean assistFlowFinish(String signId);

    int allAssistCount(String signId);

    int finishAssistCount(String signId);

    boolean allBranchFinish(String signId);

    boolean allWPFinish(String signid);

    boolean isHaveWP(String signId);

    List<User> findAssistOrgDirector(String signId);

    List<User> findAssistSLeader(String signId);

    List<OrgDept> getOrgDeptBySignId(String signId);
}
