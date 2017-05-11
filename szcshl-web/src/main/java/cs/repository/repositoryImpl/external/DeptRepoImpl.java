package cs.repository.repositoryImpl.external;

import cs.domain.organs.Dept;
import cs.repository.AbstractRepository;
import org.springframework.stereotype.Repository;

@Repository
public class DeptRepoImpl extends AbstractRepository<Dept, String> implements DeptRepo {
}