package cs.repository;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;

import cs.common.HqlBuilder;
import cs.repository.odata.ODataObj;


public interface IRepository<T, ID> {
	public T findById(ID id);
	
	public T getById(ID id);
	
	public List<T> findAll();
	
	public List<T> findByCriteria(Criterion ... criterion);
	
	public List<T> findByOdata(ODataObj oDataObj);

	public T save(T entity);
	
	public void bathUpdate(List<T> t);

	public void delete(T entity);

	public void flush();

	public void clear();

	public void setSession(Session session);
	
	Criteria  getExecutableCriteria();
	
	public Session getSession();
	
	List<T> findByHql(HqlBuilder hqlBuilder);
	
	public int executeHql(String hql);
	
	public int executeHql(HqlBuilder hqlBuilder);

	public int executeSql(String sql);

}
