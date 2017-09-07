package cs.repository.repositoryImpl.expert;

import java.util.List;

import cs.common.HqlBuilder;
import cs.domain.expert.Expert_;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;

import cs.domain.expert.ExpertType;
import cs.domain.expert.ExpertType_;
import cs.repository.AbstractRepository;

@Service
public class ExpertTypeRepoImpl extends AbstractRepository<ExpertType,String> implements ExpertTypeRepo{

	@Override
	public int isExpertTypeExist(String expertType , String expertId) {

		HqlBuilder hqlBuilder = HqlBuilder.create();
		hqlBuilder.append("from " + ExpertType.class.getSimpleName() );
		hqlBuilder.append("where "+ ExpertType_.expert.getName()+"."+ Expert_.expertID.getName() + "=:expertId and ");
		hqlBuilder.append(ExpertType_.expertType.getName() + "=:expertType");
		hqlBuilder.setParam("expertId" , expertId);
		hqlBuilder.setParam("expertType" , expertType);
		/*Criteria criteria=this.getSession().createCriteria(ExpertType.class);
		criteria.add(Restrictions.eq(ExpertType_.expertType.getName(), expertType));
		criteria.add(Restrictions.eq(ExpertType_.expert.getName() , expertType));
		List<ExpertType> expertTypes=criteria.list();
		return !expertTypes.isEmpty();*/
		return 0;
	}

}
