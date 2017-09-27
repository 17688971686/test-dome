package cs.repository.repositoryImpl.expert;

import cs.domain.expert.ExpertType;
import cs.repository.IRepository;

public interface ExpertTypeRepo extends IRepository<ExpertType, String>{

	boolean checkExpertTypeExist(String maJorBig,String maJorSmall,String expertType,String expertId);
}
