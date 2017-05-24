package cs.repository.repositoryImpl.project;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import cs.domain.project.MergeDispa;
import cs.domain.project.MergeDispa_;
import cs.domain.project.Sign;
import cs.repository.AbstractRepository;

@Repository
public class MergeDispaRepoImpl extends AbstractRepository<MergeDispa, String> implements MergeDispaRepo {
	@Override
	public List<MergeDispa> findLinkSignIdBySignId(String SignId) {
		Criteria criteria = getExecutableCriteria();
		criteria.add(Restrictions.eq(MergeDispa_.signId.getName(), SignId));
		return  criteria.list();		
	}
}
