package cs.service.sys;

import cs.domain.sys.OrgDept;
import cs.domain.sys.User;

import java.util.List;

public interface OrgDeptService {

    List<OrgDept> queryAll();

    OrgDept queryById(String id);

    OrgDept queryBySignBranchId(String signId,String branchId);

    List<User> queryOrgDeptUser(String id);

    List<User> queryOrgDeptUser(String signId,String branchId);
}