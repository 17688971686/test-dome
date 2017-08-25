package cs.repository.repositoryImpl.sys;

import cs.domain.sys.OrgDept;
import cs.domain.sys.User;
import cs.repository.IRepository;

import java.util.List;

public interface OrgDeptRepo extends IRepository<OrgDept, String>{

    OrgDept queryBySignBranchId(String signId,String branchId);

    List<User> queryOrgDeptUser(String id);

    List<User> queryOrgDeptUser(String signId,String branchId);
}
