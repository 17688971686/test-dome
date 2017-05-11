package cs.repository.repositoryImpl.project;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import cs.domain.project.DispatchDoc;
import cs.domain.project.DispatchDoc_;
import cs.domain.project.Sign_;
import cs.domain.sys.Org_;
import cs.domain.sys.User;
import cs.domain.sys.User_;
import cs.repository.AbstractRepository;

@Repository
public class DispatchDocRepoImpl extends AbstractRepository<DispatchDoc, String> implements DispatchDocRepo {
	@SuppressWarnings("unchecked")
	@Override
	public List<DispatchDoc> findDispatchBySignId(String signId) {
		Criteria criteria = getExecutableCriteria();		 
		List<DispatchDoc> list = criteria.createAlias(DispatchDoc_.sign.getName(),DispatchDoc_.sign.getName())
        .add(Restrictions.eq(DispatchDoc_.sign.getName()+"."+Sign_.signid.getName(),signId)).list();
		return list;
	}
}
