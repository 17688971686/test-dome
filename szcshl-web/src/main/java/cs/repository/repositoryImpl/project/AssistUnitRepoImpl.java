package cs.repository.repositoryImpl.project;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import cs.domain.project.AssistUnit;
import cs.domain.project.AssistUnit_;
import cs.repository.AbstractRepository;

/**
 * Description: 协审单位 数据操作实现类
 * author: ldm
 * Date: 2017-6-6 14:49:58
 */
@Repository
public class AssistUnitRepoImpl extends AbstractRepository<AssistUnit, String> implements AssistUnitRepo {

	@Override
	public int getUnitSortMax() {
		Query query=this.getSession().createQuery("select max(a.unitSort) from AssistUnit a");
		int max=0;
		if(query.uniqueResult()!=null){

			max=(int) query.uniqueResult();
		}
		return max;

	}

	@Override
	public boolean isUnitExist(String unitName) {
		
		Criteria criteria=this.getSession().createCriteria(AssistUnit.class);
		
		criteria.add(Restrictions.eq(AssistUnit_.unitName.getName(), unitName));
		
		List<AssistUnit> assistUnitList=criteria.list();
		return !assistUnitList.isEmpty();
	}

}