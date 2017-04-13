package cs.repository;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.springframework.beans.factory.annotation.Autowired;

import cs.repository.odata.ODataObj;

public class AbstractRepository<T,ID extends Serializable> implements IRepository<T, ID> {
	protected static Logger logger = Logger.getLogger(AbstractRepository.class);
	private Class<T> persistentClass;
	private Session session;	
	@Autowired
	private SessionFactory sessionFactory;
	
	public Class<T> getPersistentClass(){
		return persistentClass;
	}
	
	@SuppressWarnings("unchecked")
	public  AbstractRepository() {
		this.persistentClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
	}
	
	@Override
	public T findById(ID id) {
		logger.debug("findById");		
		return getSession().load(this.getPersistentClass(), id);		
		
	}
	
	
	
	@Override
	public List<T> findAll() {
		logger.debug("findAll");
		return this.findByCriteria();
	}
	@Override
	@SuppressWarnings({ "unchecked", "deprecation" })
	public List<T> findByCriteria(Criterion ... criterion){
		logger.debug("findByCriteria");
		Criteria crit = this.getSession().createCriteria(this.getPersistentClass());
		
		for(Criterion c: criterion){
			crit.add(c);
		}
		return crit.list();
		
	}
	@Override
	@SuppressWarnings({ "unchecked", "deprecation" })
	public List<T> findByOdata(ODataObj oDataObj){
		logger.debug("findByOdata");		
		Criteria crit = this.getSession().createCriteria(this.getPersistentClass());
		List<T> list=oDataObj.buildQuery(crit).list();	
		return list;
	}
	@Override
	public T save(T entity) {
		logger.debug("save");
		
		this.getSession().saveOrUpdate(entity);
		return entity;
	}

	@Override
	public void delete(T entity) {
		logger.debug("delete");
		
		this.getSession().delete(entity);
	}

	@Override
	public void flush() {
		logger.debug("flush");
		
		this.getSession().flush();
	}

	@Override
	public void clear() {
		logger.debug("clear");
		
		this.getSession().clear();
	}
	
	@Override
	public void setSession(Session session) {
		logger.debug("setSession");
		
		this.session = session;
		
	}
	
	@Override
	public Session getSession(){
		logger.debug("getSession");		
		this.session = sessionFactory.getCurrentSession();
		return this.session;
	}

}
