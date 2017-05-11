package cs.repository.repositoryImpl.expert;

import java.util.List;

import cs.domain.expert.Expert;
import cs.repository.IRepository;

public interface ExpertRepo extends IRepository<Expert, String> {
	
	Expert findExpertByName(String expertName);
	List<Expert> findAllRepeat();
}
