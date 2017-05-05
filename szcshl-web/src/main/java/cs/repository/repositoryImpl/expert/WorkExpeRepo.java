package cs.repository.repositoryImpl.expert;

import cs.domain.expert.WorkExpe;
import cs.repository.IRepository;

public interface WorkExpeRepo extends IRepository<WorkExpe, String>{
	 WorkExpe findWorkByName(String workName);
}
