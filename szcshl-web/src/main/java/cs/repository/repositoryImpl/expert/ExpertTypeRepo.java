package cs.repository.repositoryImpl.expert;

import cs.domain.expert.ExpertType;
import cs.repository.IRepository;

public interface ExpertTypeRepo extends IRepository<ExpertType, String>{
	
	int isExpertTypeExist(String expertType , String expertId);

}
