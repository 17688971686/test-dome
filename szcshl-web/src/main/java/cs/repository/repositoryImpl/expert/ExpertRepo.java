package cs.repository.repositoryImpl.expert;

import java.util.List;

import cs.domain.expert.Expert;
import cs.repository.IRepository;

public interface ExpertRepo extends IRepository<Expert, String> {

    boolean checkIsHaveIdCard(String idCard,String expertId);

    List<Expert> findExpertByIdCard(String idCard);

    List<Expert> findAllRepeat();

    List<Expert> findByBusinessId(String businessId);
}
