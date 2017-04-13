package cs.repository.repositoryImpl;

import org.springframework.stereotype.Repository;

import cs.domain.Log;
import cs.repository.AbstractRepository;

@Repository
public class LogRepoImpl extends AbstractRepository<Log, Long> implements LogRepo {

	
}
