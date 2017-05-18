package cs.repository.repositoryImpl.external;

import java.util.List;

import cs.domain.external.OfficeUser;
import cs.repository.IRepository;
import cs.repository.odata.ODataObj;

public interface OfficeUserRepo extends IRepository<OfficeUser, String>{

	List<OfficeUser> getOfficeNotIn(List<String> officeIds, ODataObj odataObj);

	List<OfficeUser> findOfficeUserByDeptId(String deptId);

}
