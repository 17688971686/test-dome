package cs.repository.repositoryImpl.project;

import org.springframework.stereotype.Repository;

import cs.domain.project.Sign;
import cs.repository.AbstractRepository;

@Repository
public class SignRepoImpl  extends AbstractRepository<Sign, String> implements SignRepo {

}
