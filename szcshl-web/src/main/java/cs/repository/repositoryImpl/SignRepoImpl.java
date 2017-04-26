package cs.repository.repositoryImpl;

import org.springframework.stereotype.Repository;

import cs.domain.Sign;
import cs.repository.AbstractRepository;

@Repository
public class SignRepoImpl  extends AbstractRepository<Sign, String> implements SignRepo {

}
