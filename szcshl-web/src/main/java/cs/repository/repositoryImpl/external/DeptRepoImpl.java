package cs.repository.repositoryImpl.external;

import cs.domain.external.Dept;
import cs.repository.AbstractRepository;
import org.springframework.stereotype.Repository;

@Repository
public class DeptRepoImpl extends AbstractRepository<Dept, String> implements DeptRepo {
}