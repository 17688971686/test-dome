package cs.repository.repositoryImpl.expert;

import org.springframework.stereotype.Service;

import cs.domain.expert.ExpertType;
import cs.repository.AbstractRepository;

@Service
public class ExpertTypeRepoImpl extends AbstractRepository<ExpertType,String> implements ExpertTypeRepo{

}
