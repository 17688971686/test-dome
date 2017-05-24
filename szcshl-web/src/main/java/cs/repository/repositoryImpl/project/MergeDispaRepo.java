package cs.repository.repositoryImpl.project;

import java.util.List;

import cs.domain.project.MergeDispa;
import cs.repository.IRepository;

public interface MergeDispaRepo extends IRepository<MergeDispa, String> {
	List<MergeDispa> findLinkSignIdBySignId(String SignId);
}
