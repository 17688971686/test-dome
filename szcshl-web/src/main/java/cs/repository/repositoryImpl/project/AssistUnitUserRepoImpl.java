package cs.repository.repositoryImpl.project;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import cs.domain.project.AssistUnitUser;
import cs.domain.project.AssistUnitUser_;
import cs.repository.AbstractRepository;
import cs.repository.odata.ODataObj;

/**
 * Description: 协审单位用户 数据操作实现类
 * author: ldm
 * Date: 2017-6-9 9:37:54
 */
@Repository
public class AssistUnitUserRepoImpl extends AbstractRepository<AssistUnitUser, String> implements AssistUnitUserRepo {

	@Override
	public List<AssistUnitUser> getUserNotIn(ODataObj odataObj,List<String> assistUnitUserIds) {
		
		Criteria criteria=this.getSession().createCriteria(AssistUnitUser.class);
		
		assistUnitUserIds.forEach(x ->{
			criteria.add(Restrictions.ne(AssistUnitUser_.id.getName(), x));
		});
		
		List<AssistUnitUser> assistUnitUserList=odataObj.buildQuery(criteria).list();
		
		return assistUnitUserList;
	}

	@Override
	public boolean isUserExist(String userName) {
		
		Criteria criteria=this.getSession().createCriteria(AssistUnitUser.class);
		
		criteria.add(Restrictions.eq(AssistUnitUser_.userName.getName(), userName));
		
		List<AssistUnitUser> list=criteria.list();
		
		return !list.isEmpty();
	}
}