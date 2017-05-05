package cs.repository.repositoryImpl.expert;

import cs.domain.expert.Expert;
import cs.domain.sys.User;
import cs.repository.IRepository;

public interface ExpertRepo extends IRepository<Expert, String> {
	Expert findExpertByName(String expertName);
}
