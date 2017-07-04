package cs.repository.repositoryImpl.sys;

import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import cs.domain.sys.Workday;
import cs.domain.sys.Workday_;
import cs.repository.AbstractRepository;

@Repository
public class WorkdayRepoImpl extends AbstractRepository<Workday, String> implements WorkdayRepo{

	@Override
	public boolean isExist(Date days) {
		Criteria criteria=this.getSession().createCriteria(Workday.class);
		criteria.add(Restrictions.eq(Workday_.dates.getName(), days));
		List<Workday> workdayList=criteria.list();
		return !workdayList.isEmpty();
	}

}
