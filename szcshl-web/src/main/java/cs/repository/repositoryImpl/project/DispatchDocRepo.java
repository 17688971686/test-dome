package cs.repository.repositoryImpl.project;

import java.util.List;

import cs.domain.project.DispatchDoc;
import cs.repository.IRepository;

public interface DispatchDocRepo extends IRepository<DispatchDoc,String>{
	List<DispatchDoc> findDispatchBySignId(String signId);
}
