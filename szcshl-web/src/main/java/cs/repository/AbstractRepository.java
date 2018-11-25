package cs.repository;

import cs.common.HqlBuilder;
import cs.common.utils.StringUtil;
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
import org.hibernate.type.Type;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

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
        return getSession().load(this.getPersistentClass(), id);

    }

    @Override
    public T findByIdGet(ID id) {
        logger.debug("findByIdGet");
        return getSession().get(this.getPersistentClass(), id);

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
        Query<T> q = this.getCurrentSession().createQuery(hqlBuilder.getHqlString(), getPersistentClass());
        List<T> resultList = setParamsToQuery2(q, hqlBuilder).list();
        if(Validate.isList(resultList)){
            return resultList.get(0);
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
            crit.add(c);
        }
        return crit.list();

    }

    @Override
    @SuppressWarnings({"unchecked", "deprecation"})
    public List<T> findByOdata(ODataObj oDataObj) {
        logger.debug("findByOdata");
        Criteria crit = getExecutableCriteria();
        List<T> list = oDataObj.buildQuery(crit).list();
        return list;
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
        Query<T> q = this.getCurrentSession().createQuery(hqlBuilder.getHqlString(), getPersistentClass());
        return setParamsToQuery2(q, hqlBuilder).list();
    }


    @Override
    public int executeHql(String hql) {
        Query<?> q = this.getCurrentSession().createQuery(hql);
        return q.executeUpdate();
    }

    @Override
    public int executeSql(String sql) {
        NativeQuery<T> q = this.getCurrentSession().createNativeQuery(sql, this.getPersistentClass());
        return q.executeUpdate();
    }

    @Override
    public List<T> findBySql(HqlBuilder hqlBuilder) {
        NativeQuery<T> q = this.getCurrentSession().createNativeQuery(hqlBuilder.getHqlString(), this.getPersistentClass());
        return setParamsToQuery2(q, hqlBuilder).list();
    }

    /**
     * 返回Int的sql查询
     *
     * @param sqlBuilder
     * @return
     */
    @Override
    public int returnIntBySql(HqlBuilder sqlBuilder) {
        NativeQuery<?> q = this.getCurrentSession().createNativeQuery(sqlBuilder.getHqlString());
        Object returnValue = setParamsToQuery(q, sqlBuilder).getSingleResult();
        return returnValue == null ? 0 : Integer.valueOf(returnValue.toString());
    }

    @Override
    public int executeHql(HqlBuilder hqlBuilder) {
        Query<?> q = setParamsToQuery(this.getCurrentSession().createQuery(hqlBuilder.getHqlString()), hqlBuilder);
        return q.executeUpdate();
    }

    @Override
    public int executeSql(HqlBuilder hqlBuilder) {
        NativeQuery<T> q = this.getCurrentSession().createNativeQuery(hqlBuilder.getHqlString(), this.getPersistentClass());
        return setParamsToQuery2(q, hqlBuilder).executeUpdate();
    }

    protected Query<?> setParamsToQuery(Query<?> query, HqlBuilder hqlBuilder) {
        List<String> params = hqlBuilder.getParams();
        List<Object> values = hqlBuilder.getValues();
        List<Type> types = hqlBuilder.getTypes();
        if (Validate.isList(params)) {
            for (int i = 0, l = params.size();i<l; i++) {
                Object value =  values.get(i);
                if( value instanceof String ) {
                    if (Validate.isString(value)) {
                        value = StringUtil.sqlInjectionFilter(value.toString());
                    }
                }
                if (types.get(i) == null) {
                    query.setParameter(params.get(i), value);
                } else {
                    query.setParameter(params.get(i), value, types.get(i));
                }
            }
        }
        return query;
    }

    protected Query<T> setParamsToQuery2(Query<T> query, HqlBuilder hqlBuilder) {
        List<String> params = hqlBuilder.getParams();
        List<Object> values = hqlBuilder.getValues();
        List<Type> types = hqlBuilder.getTypes();
        if (Validate.isList(params)) {
            for (int i = 0, l = params.size();i<l; i++) {
                Object value =  values.get(i);
                if( value instanceof String ) {
                    if (Validate.isString(value)) {
                        value = StringUtil.sqlInjectionFilter(value.toString());
                    }
                }
                if (types.get(i) == null) {
                    query.setParameter(params.get(i), value);
                } else {
                    query.setParameter(params.get(i), value, types.get(i));
                }
            }
        }
        return query;
    }

    @Override
    public int deleteById(String idPropertyName, String idValue) {
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
        NativeQuery<Object[]> q = this.getCurrentSession().createNativeQuery(sqlBuilder.getHqlString());
        List<String> params = sqlBuilder.getParams();
        List<Object> values = sqlBuilder.getValues();
        List<Type> types = sqlBuilder.getTypes();
        if (params != null) {
            for (int i = 0; i < params.size(); i++) {
                Object value =  values.get(i);
                if( value instanceof String ) {
                    if (Validate.isString(value)) {
                        value = StringUtil.sqlInjectionFilter(value.toString());
                    }
                }
                if (types.get(i) == null) {
                    q.setParameter(params.get(i), value);
                } else {
                    q.setParameter(params.get(i), value, types.get(i));
                }
            }
        }
        return q.getResultList();
    }

    @Override
    public List<Map<String, Object>> getMapListBySql(HqlBuilder sqlBuilder) {
        NativeQuery<Map<String, Object>> q = this.getCurrentSession().createNativeQuery(sqlBuilder.getHqlString());
        List<String> params = sqlBuilder.getParams();
        List<Object> values = sqlBuilder.getValues();
        List<Type> types = sqlBuilder.getTypes();
        if (params != null) {
            for (int i = 0; i < params.size(); i++) {
                Object value =  values.get(i);
                if( value instanceof String ) {
                    if (Validate.isString(value)) {
                        value = StringUtil.sqlInjectionFilter(value.toString());
                    }
                }
                if (types.get(i) == null) {
                    q.setParameter(params.get(i), value);
                } else {
                    q.setParameter(params.get(i), value, types.get(i));
                }
            }
        }
        return q.list();
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
