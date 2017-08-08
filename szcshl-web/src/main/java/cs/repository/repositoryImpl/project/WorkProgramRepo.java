package cs.repository.repositoryImpl.project;

import cs.domain.project.WorkProgram;
import cs.domain.sys.Org;
import cs.repository.IRepository;

import java.util.List;

public interface WorkProgramRepo extends IRepository<WorkProgram, String>{

    WorkProgram findByPrincipalUser(String signId);
    List<Org> getReviewOrg(String signId);
    WorkProgram findBySignIdAndBranchId(String signId,String branchId);
}
