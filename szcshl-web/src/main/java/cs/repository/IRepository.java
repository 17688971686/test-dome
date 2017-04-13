package cs.repository;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.criterion.Criterion;

import cs.repository.odata.ODataObj;

public interface IRepository<T, ID> {
	public T findById(ID id);

	public List<T> findAll();
	public List<T> findByCriteria(Criterion ... criterion);
	public List<T> findByOdata(ODataObj oDataObj);

	public T save(T entity);

	public void delete(T entity);

	public void flush();

	public void clear();

	public void setSession(Session session);
	public Session getSession();
}
