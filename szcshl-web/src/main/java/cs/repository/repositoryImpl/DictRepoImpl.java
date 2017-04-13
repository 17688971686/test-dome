package cs.repository.repositoryImpl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import cs.domain.Dict;
import cs.domain.Dict_;
import cs.repository.AbstractRepository;



@Repository
public class DictRepoImpl extends AbstractRepository<Dict, String> implements DictRepo{

	@Override
	public Dict findByCodeOrName(String code, String dictGroupName) {
		Criteria criteria = this.getSession().createCriteria(Dict.class);
		//criteria.add(Restrictions.eq(DictGroup_.dictCode.getName(), code));
		//criteria.add(Restrictions.eq(DictGroup_.dictGroupName.getName(), name));
		//criteria.add(Restrictions.or(Restrictions.eq(Dict_.dictCode.getName(), code),Restrictions.eq(DictGroup_.dictGroupName.getName(), dictGroupName)));
		List<Dict> dictGroups = criteria.list();
		if (dictGroups.size() > 0) {
			return dictGroups.get(0);
		} else {
			return null;
		}
		
	}

	@Override
	public Dict findByName(String dictGroupName) {
		Criteria criteria = this.getSession().createCriteria(Dict.class);
		//criteria.add(Restrictions.eq(DictGroup_.dictGroupName.getName(), dictGroupName));
		List<Dict> dictGroups = criteria.list();
		if (dictGroups.size() > 0) {
			return dictGroups.get(0);
		} else {
			return null;
		}
	}

	@Override
	public Dict findByCode(String dictCode) {
		Criteria criteria = this.getSession().createCriteria(Dict.class);
		criteria.add(Restrictions.eq(Dict_.dictCode.getName(), dictCode));
		List<Dict> dictGroups = criteria.list();
		if (dictGroups.size() > 0) {
			return dictGroups.get(0);
		} else {
			return null;
		}
	}

	@Override
	public Dict findByCodeKeyAndName(String dictCode, String dictKey, String dictName) {
		Criteria criteria = this.getSession().createCriteria(Dict.class);
		criteria.add(Restrictions.eq(Dict_.dictCode.getName(), dictCode));
		criteria.add(Restrictions.or(Restrictions.eq(Dict_.dictKey.getName(), dictKey),Restrictions.eq(Dict_.dictName.getName(), dictName)));
		//criteria.add(Restrictions.eq(Dict_.dictKey.getName(), dictKey));
		//criteria.add(Restrictions.eq(Dict_.dictName.getName(), dictName));
		List<Dict> dictGroups = criteria.list();
		if (dictGroups.size() > 0) {
			return dictGroups.get(0);
		} else {
			return null;
		}
	}

}
