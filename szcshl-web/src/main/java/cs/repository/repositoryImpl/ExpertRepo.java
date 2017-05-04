package cs.repository.repositoryImpl;

import cs.domain.Expert;
import cs.domain.User;
import cs.repository.IRepository;

public interface ExpertRepo extends IRepository<Expert, String> {
	Expert findExpertByName(String expertName);
}
