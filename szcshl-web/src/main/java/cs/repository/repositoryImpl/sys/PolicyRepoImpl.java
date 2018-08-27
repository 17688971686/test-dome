package cs.repository.repositoryImpl.sys;
import cs.domain.sys.Policy;
import cs.repository.AbstractRepository;
import org.springframework.stereotype.Repository;

@Repository
public class PolicyRepoImpl extends AbstractRepository<Policy, String> implements PolicyRepo {

}
