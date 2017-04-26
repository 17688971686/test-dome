package cs.repository;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

/**
 * hql 封装 (后面再完善)
 * @author ldm
 *
 */
public abstract class RepositoryHelper  {
	
	@Autowired
	protected SessionFactory sessionFactory;

	//这样获取session会自动关闭
	protected Session getSession() {
		return sessionFactory.getCurrentSession();
	}
	
	/**
	 * 根据查询函数与参数列表创建Query对象,后续可进行更多处理,辅助函数.
	 * @param queryString
	 * @param values
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	protected Query createQuery(String queryString, Object... values) {
		Assert.hasText(queryString);
		Query queryObject = getSession().createQuery(queryString);
		if (values != null) {
			for (int i = 0; i < values.length; i++) {
				queryObject.setParameter(i, values[i]);
			}
		}
		return queryObject;
	}
	
	/**
	 * 通过HQL查询对象列表
	 * 
	 * @param hql
	 *            hql语句
	 * @param values
	 *            数量可变的参数
	 */
	@SuppressWarnings("rawtypes")
	protected List findByHql(String hql, Object... values) {
		return createQuery(hql, values).list();
	}

	/**
	 * 通过HQL查询唯一对象
	 */
	protected Object findUniqueByHql(String hql, Object... values) {
		return createQuery(hql, values).uniqueResult();
	}


	
	

	
}
