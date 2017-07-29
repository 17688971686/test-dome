package cs.repository.repositoryImpl.sys;

import java.util.List;

import cs.common.utils.SessionUtil;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import cs.domain.sys.Org;
import cs.domain.sys.Org_;
import cs.repository.AbstractRepository;

@Repository
public class OrgRepoImpl extends AbstractRepository<Org, String> implements OrgRepo {
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Org> findUserChargeOrg() {
		Criteria criteria = getExecutableCriteria();
		criteria.add(Restrictions.eq(Org_.orgSLeader.getName(), SessionUtil.getUserInfo().getId()));
		return criteria.list();
	}
	
}
