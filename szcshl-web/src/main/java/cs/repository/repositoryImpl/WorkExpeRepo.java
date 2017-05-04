package cs.repository.repositoryImpl;

import cs.domain.WorkExpe;
import cs.repository.IRepository;

public interface WorkExpeRepo extends IRepository<WorkExpe, String>{
	 WorkExpe findWorkByName(String workName);
}
