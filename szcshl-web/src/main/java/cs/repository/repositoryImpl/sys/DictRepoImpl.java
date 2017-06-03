package cs.repository.repositoryImpl.sys;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import cs.domain.sys.Dict;
import cs.domain.sys.Dict_;
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
		List<Dict> dictGroups = criteria.list();
		if (dictGroups.size() > 0) {
			return dictGroups.get(0);
		} else {
			return null;
		}
	}

	@Override
	public Dict findByCode(String dictCode,String excludeId) {
		Criteria criteria = this.getSession().createCriteria(Dict.class);
		criteria.add(Restrictions.eq(Dict_.dictCode.getName(), dictCode));
		if(excludeId != null&&!excludeId.isEmpty()){
			criteria.add(Restrictions.not(Restrictions.eq(Dict_.dictId.getName(), excludeId)));
		}
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

	/**
	 * 通过字典编码，查找出该字典编码的子级字典，如果dictCode为空，则查询第一级字典
	 * */
	@Override
	public List<Dict> findDictItemByCode(String dictCode) {
		Criteria criteria = this.getSession().createCriteria(Dict.class);
		if(dictCode == null||dictCode.isEmpty()){
			
		}else{
			criteria.add(Restrictions.eq(Dict_.dictCode.getName(), dictCode));
		}
		
		
		return criteria.list();
	}

	@Override
	public Dict findByKeyAndNameAtSomeLevel(String dictKey, String dictName, String parentId,String excludeId) {
		Criteria criteria = this.getSession().createCriteria(Dict.class);	
		criteria.add(Restrictions.eqOrIsNull(Dict_.parentId.getName(), parentId));
		criteria.add(Restrictions.or(Restrictions.eq(Dict_.dictKey.getName(), dictKey),Restrictions.eq(Dict_.dictName.getName(), dictName)));
		if(excludeId != null&&!excludeId.isEmpty()){
			criteria.add(Restrictions.not(Restrictions.eq(Dict_.dictId.getName(), excludeId)));
		}
		List<Dict> dictGroups = criteria.list();
		if (dictGroups.size() > 0) {
			return dictGroups.get(0);
		} else {
			return null;
		}
	}

	@Override
	public List<Dict> findByPdictId(String parentId) {
		Criteria criteria = this.getSession().createCriteria(Dict.class);	
		criteria.add(Restrictions.eqOrIsNull(Dict_.parentId.getName(), parentId));	
//		criteria.addOrder((Order.asc(Dict_.dictSort.getName())));
		return criteria.list();
		
	}

}
