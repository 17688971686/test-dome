package cs.repository.repositoryImpl;

import java.io.Serializable;

import org.springframework.stereotype.Repository;

import cs.domain.Company;
import cs.repository.AbstractRepository;

@Repository
public class CompanyRepoImpl extends AbstractRepository<Company, String> implements CompanyRepo {

}
