package cs.repository.repositoryImpl.sys;

import java.util.Date;

import cs.domain.sys.Workday;
import cs.repository.IRepository;

public interface WorkdayRepo extends IRepository<Workday, String>{
	
	boolean isExist(Date days);

}
