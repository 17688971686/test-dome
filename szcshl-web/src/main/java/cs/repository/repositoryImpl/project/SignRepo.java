package cs.repository.repositoryImpl.project;

import cs.domain.project.Sign;
import cs.repository.IRepository;

public interface SignRepo extends IRepository<Sign, String> {

    boolean updateSignState(String signId,String state);

    boolean updateSignProcessState(String signId,Integer processState);

    Sign findByFilecode(String filecode);
}
