package cs.repository.repositoryImpl.sys;

import java.util.Date;
import java.util.List;

import cs.common.utils.DateUtils;
import cs.common.utils.Validate;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
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
		return Validate.isList(workdayList);
	}

	/**
	 * 计算从当前日期开始，一年内的调休记录
	 * @return
	 */
	@Override
	public List<Workday> findWorkDay(String beginTime,String endTime) {
        Criteria criteria = getExecutableCriteria();
        if(!Validate.isString(endTime)){
            criteria.add(Restrictions.le(Workday_.dates.getName(),new Date()));
        }else{
            criteria.add(Restrictions.le(Workday_.dates.getName(),DateUtils.converToDate(endTime,DateUtils.DATE_PATTERN)));
        }
        if(Validate.isString(beginTime)){
            criteria.add(Restrictions.ge(Workday_.dates.getName(),DateUtils.converToDate(beginTime,DateUtils.DATE_PATTERN)));
        }
        //按添加的日期排序
        criteria.addOrder(Order.asc(Workday_.dates.getName()));
		return criteria.list();
	}

}
