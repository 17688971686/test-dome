package cs.repository.repositoryImpl.external;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import cs.domain.external.Dept_;
import cs.domain.external.OfficeUser;
import cs.domain.external.OfficeUser_;
import cs.repository.AbstractRepository;
import cs.repository.odata.ODataObj;

@Repository
public class OfficeUserRepoImpl extends AbstractRepository<OfficeUser, String> implements OfficeUserRepo {

	@Override
	public List<OfficeUser> getOfficeNotIn(List<String> officeIds, ODataObj odataObj) {
		Criteria citer = this.getSession().createCriteria(OfficeUser.class);
		officeIds.forEach(x->{
			citer.add(Restrictions.ne(OfficeUser_.officeID.getName(), x));
		});
		List<OfficeUser> lsit= odataObj.buildQuery(citer).list();
		return lsit;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<OfficeUser> findOfficeUserByDeptId(String deptId) {
		Criteria criteria = getExecutableCriteria();
		List<OfficeUser> officelist=criteria.createAlias(OfficeUser_.dept.getName(), OfficeUser_.dept.getName())
				.add(Restrictions.eq(OfficeUser_.dept.getName()+"."+Dept_.deptName.getName(), deptId)).list();
		return officelist;
	}

}
