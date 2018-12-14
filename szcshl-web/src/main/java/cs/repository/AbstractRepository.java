package cs.repository;

import cs.common.HqlBuilder;
import cs.common.utils.Validate;
import cs.repository.odata.ODataObj;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.Query;
import org.owasp.esapi.ESAPI;
import org.owasp.esapi.codecs.Codec;
import org.owasp.esapi.codecs.OracleCodec;
import org.owasp.esapi.codecs.WindowsCodec;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;

public class AbstractRepository<T, ID extends Serializable> implements IRepository<T, ID> {
    protected static Logger logger = Logger.getLogger(AbstractRepository.class);

    private Class<T> persistentClass;
    private Session session;
    @Autowired
    private SessionFactory sessionFactory;

    public Class<T> getPersistentClass() {
        return persistentClass;
    }

    @SuppressWarnings("unchecked")
    public AbstractRepository() {
        this.persistentClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    @Override
    public Criteria getExecutableCriteria() {
        return DetachedCriteria.forClass(this.getPersistentClass()).getExecutableCriteria(getSession());
    }

    public DetachedCriteria getDetachedCriteria() {
        return DetachedCriteria.forClass(this.getPersistentClass());
    }

    public Criteria getCriteriaByDetachedCriteria(DetachedCriteria dc) {
        return dc.getExecutableCriteria(getSession());
    }

    @Override
    public T findById(ID id) {
        logger.debug("findById");
        if (null != id) {
            Session session = getSession();
            if(null != session){
                return session.load(this.getPersistentClass(), id);
            }
        }
        return null;
    }

    /**
     * 用hql方式 根据ID查询
     *
     * @param idPropertyName
     * @param idValue
     * @return
     */
    @Override
    public T findById(String idPropertyName, String idValue) {
        HqlBuilder hqlBuilder = HqlBuilder.create();
        hqlBuilder.append(" from  " + getPersistentClass().getSimpleName());
        hqlBuilder.append(" where " + idPropertyName + " = :id ");
        hqlBuilder.setParam("id", idValue);
        Query<T> q = this.getCurrentSession().createQuery(hqlBuilder.getHqlString(), this.getPersistentClass());
        QueryParamBuild<T> queryParamBuild = new QueryParamBuild(q);
        q = queryParamBuild.buildParams(hqlBuilder).getQuery();
        if (Validate.isObject(q)) {
            List<T> resultList = q.list();
            if (Validate.isList(resultList)) {
                return resultList.get(0);
            }
        }
        return null;
    }


    @Override
    public List<T> findAll() {
        logger.debug("findAll");
        return this.findByCriteria();
    }

    @Override
    @SuppressWarnings({"unchecked", "deprecation"})
    public List<T> findByCriteria(Criterion... criterion) {
        logger.debug("findByCriteria");
        Criteria crit = getExecutableCriteria();
        for (Criterion c : criterion) {
            if (Validate.isObject(c)) {
                crit.add(c);
            }
        }
        return crit.list();
    }

    @Override
    @SuppressWarnings({"unchecked", "deprecation"})
    public List<T> findByOdata(ODataObj oDataObj) {
        logger.debug("findByOdata");
        Criteria crit = getExecutableCriteria();
        crit = oDataObj.buildQuery(crit);
        if (Validate.isObject(crit)) {
            List<T> list = crit.list();
            return list;
        }
        return null;
    }

    @Override
    public T save(T entity) {
        logger.debug("save");
        flush();
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
    public Session getSession() {
        logger.debug("getSession");
        this.session = sessionFactory.getCurrentSession();
        return this.session;
    }

    private Session getCurrentSession() {
        return this.sessionFactory.getCurrentSession();
    }

    @Override
    public void bathUpdate(List<T> t) {
        for (int i = 0, l = t.size(); i < l; i++) {
            T entity = t.get(i);
            save(entity);
            if (i > 0 && i / 50 == 0) {
                flush();
                clear();
            }
        }
    }

    @Override
    public T getById(ID id) {
        return getSession().get(this.getPersistentClass(), id);
    }


    @Override
    public List<T> findByHql(HqlBuilder hqlBuilder) {
        if(!this.checkBuilder(hqlBuilder)){
            return null;
        }
        Query<T> q = this.getCurrentSession().createQuery(hqlBuilder.getHqlString(), this.getPersistentClass());
        QueryParamBuild<T> queryParamBuild = new QueryParamBuild(q);
        q = queryParamBuild.buildParams(hqlBuilder).getQuery();
        if (Validate.isObject(q)) {
            return q.list();
        }
        return null;
    }

    @Override
    public List<T> findBySql(HqlBuilder hqlBuilder) {
        if(!this.checkBuilder(hqlBuilder)){
            return null;
        }
        Query<T> q = this.getCurrentSession().createNativeQuery(hqlBuilder.getHqlString(), this.getPersistentClass());
        QueryParamBuild<T> queryParamBuild = new QueryParamBuild(q);
        q = queryParamBuild.buildParams(hqlBuilder).getQuery();
        if (Validate.isObject(q)) {
            return q.list();
        }
        return null;
    }

    /**
     * 返回Int的sql查询
     *
     * @param sqlBuilder
     * @return
     */
    @Override
    public int returnIntBySql(HqlBuilder sqlBuilder) {
        if(!this.checkBuilder(sqlBuilder)){
            return -1;
        }
        Query q = this.getCurrentSession().createNativeQuery(sqlBuilder.getHqlString());
        QueryParamBuild queryParamBuild = new QueryParamBuild(q);
        q = queryParamBuild.buildParams(sqlBuilder).getQuery();
        int returnValue = -1;
        if (Validate.isObject(q)) {
            returnValue = Integer.parseInt(q.getSingleResult().toString());
        }
        return returnValue;
    }

    @Override
    public int executeHql(HqlBuilder hqlBuilder) {
        if(!this.checkBuilder(hqlBuilder)){
            return -1;
        }
        Query q = this.getCurrentSession().createQuery(hqlBuilder.getHqlString());
        QueryParamBuild queryParamBuild = new QueryParamBuild(q);
        q = queryParamBuild.buildParams(hqlBuilder).getQuery();
        int updateCount = 0;
        if (Validate.isObject(q)) {
            updateCount = q.executeUpdate();
        }
        return updateCount;
    }

    @Override
    public int executeSql(HqlBuilder hqlBuilder) {
        if(!this.checkBuilder(hqlBuilder)){
            return -1;
        }
        Query q = this.getCurrentSession().createNativeQuery(hqlBuilder.getHqlString());
        QueryParamBuild queryParamBuild = new QueryParamBuild(q);
        q = queryParamBuild.buildParams(hqlBuilder).getQuery();
        int updateCount = 0;
        if (Validate.isObject(q)) {
            updateCount = q.executeUpdate();
        }
        return updateCount;
    }

    protected boolean checkBuilder(HqlBuilder hqlBuilder){
        if (!Validate.isObject(hqlBuilder)) {
            return false;
        }
        return true;
    }


    @Override
    public int deleteById(String idPropertyName, String idValue) {
        if(!Validate.isString(idPropertyName) || !Validate.isString(idValue)){
            return -1;
        }
        HqlBuilder hqlBuilder = HqlBuilder.create();
        hqlBuilder.append(" delete from  " + getPersistentClass().getSimpleName() + " ");
        hqlBuilder.bulidPropotyString(" where ", idPropertyName, idValue);
        return executeHql(hqlBuilder);
    }

    /**
     * 根据sql 返回一个数组列表
     *
     * @param sqlBuilder
     * @return
     */
    @Override
    public List<Object[]> getObjectArray(HqlBuilder sqlBuilder) {
        if(!this.checkBuilder(sqlBuilder)){
            return null;
        }
        Query q = this.getCurrentSession().createNativeQuery(sqlBuilder.getHqlString());
        QueryParamBuild queryParamBuild = new QueryParamBuild(q);
        q = queryParamBuild.buildParams(sqlBuilder).getQuery();
        return q.getResultList();
    }

    @Override
    public List<T> findByIds(String idPropertyName, String idValue, String orderStr) {
        HqlBuilder hqlBuilder = HqlBuilder.create();
        hqlBuilder.append(" from  " + getPersistentClass().getSimpleName() + " ");
        hqlBuilder.bulidPropotyString("where", idPropertyName, idValue);
        if (Validate.isString(orderStr)) {
            hqlBuilder.append(" order by " + orderStr);
        }
        return findByHql(hqlBuilder);
    }

    /**
     * 取数据库的当前时间
     *
     * @param format 日期格式
     * @return
     */
    @Override
    public String getDataBaseTime(String format) {
        String dataBaseTime = "";
        String sql = "select to_char(sysdate,'" + format + "') from dual";
        try {
            NativeQuery<String> q = this.getCurrentSession().createNativeQuery(sql);
            dataBaseTime = q.uniqueResult();
        } catch (Exception ex) {
        }
        return dataBaseTime;
    }
}
