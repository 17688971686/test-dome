package cs.repository.repositoryImpl.sys;

import org.springframework.stereotype.Repository;

import cs.domain.sys.Annountment;
import cs.repository.AbstractRepository;

@Repository
public class AnnountmentRepoImpl extends AbstractRepository<Annountment, String> implements AnnountmentRepo{

}
