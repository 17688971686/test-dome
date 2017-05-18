package cs.repository.repositoryImpl.sys;

import java.util.List;

import cs.domain.sys.Org;
import cs.repository.IRepository;

public interface OrgRepo extends IRepository<Org, String> {

	List<Org> findUserChargeOrg();

}
