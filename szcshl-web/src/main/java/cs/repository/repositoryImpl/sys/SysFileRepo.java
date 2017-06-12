package cs.repository.repositoryImpl.sys;

import java.util.List;

import cs.domain.sys.SysFile;
import cs.repository.IRepository;

public interface SysFileRepo extends IRepository<SysFile, String> {

	List<SysFile> findBySignId(String signid);

}
