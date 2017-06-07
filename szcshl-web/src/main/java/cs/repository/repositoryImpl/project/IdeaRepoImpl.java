package cs.repository.repositoryImpl.project;

import org.springframework.stereotype.Repository;

import cs.domain.project.Idea;
import cs.repository.AbstractRepository;

@Repository
public class IdeaRepoImpl extends AbstractRepository<Idea, String> implements IdeaRepo{

}
