package cs.repository.repositoryImpl.expert;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;

import cs.domain.expert.ExpertType;
import cs.domain.expert.ExpertType_;
import cs.repository.AbstractRepository;

@Service
public class ExpertTypeRepoImpl extends AbstractRepository<ExpertType,String> implements ExpertTypeRepo{

	@Override
	public boolean isExpertTypeExist(String expertType) {
		
		Criteria criteria=this.getSession().createCriteria(ExpertType.class);
		criteria.add(Restrictions.eq(ExpertType_.expertType.getName(), expertType));
		List<ExpertType> expertTypes=criteria.list();
		return !expertTypes.isEmpty();
	}

}
