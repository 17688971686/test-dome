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
import org.hibernate.type.Type;
import org.owasp.esapi.ESAPI;
import org.owasp.esapi.codecs.Codec;
import org.owasp.esapi.codecs.OracleCodec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Map;

public class AbstractRepository<T, ID extends Serializable> implements IRepository<T, ID> {
    protected static Logger logger = Logger.getLogger(AbstractRepository.class);

    private Class<T> persistentClass;
    private Session session;
    @Autowired
    private SessionFactory sessionFactory;
    @Autowired
    private JdbcTemplate jdbcTemplate;

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
            if (null != session) {
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
        this.buildParams(q, hqlBuilder);
        List<T> resultList = q.list();
        if (Validate.isList(resultList)) {
            return resultList.get(0);
        }
        return null;
    }

    private void buildParams(Query<?> query, HqlBuilder hqlBuilder) {
        if (Validate.isObject(query)) {
            List<String> params = hqlBuilder.getParams();
            List<Object> values = hqlBuilder.getValues();
            List<Type> types = hqlBuilder.getTypes();
            Codec oracleCodec = new OracleCodec();
            if (Validate.isList(params)) {
                for (int i = 0, l = params.size(); i < l; i++) {
                    String paramName = params.get(i);
                    Object value = values.get(i);
                    if (Validate.isObject(value)) {
                        if (value instanceof String) {
                            value = ESAPI.encoder().encodeForSQL(oracleCodec, value.toString());
                        }
                    }
                    if (types.get(i) == null) {
                        query.setParameter(paramName, value);
                    } else {
                        query.setParameter(paramName, value, types.get(i));
                    }
                }
            }
        }
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
            return crit.list();
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
        Query<T> q = this.getCurrentSession().createQuery(hqlBuilder.getHqlString(), this.getPersistentClass());
        this.buildParams(q, hqlBuilder);
        return q.list();
    }

    @Override
    public List<T> findBySql(HqlBuilder hqlBuilder) {
        Query<T> q = this.getCurrentSession().createNativeQuery(hqlBuilder.getHqlString(), this.getPersistentClass());
        this.buildParams(q, hqlBuilder);
        return q.list();
    }

    /**
     * 返回Int的sql查询
     *
     * @param sqlBuilder
     * @return
     */
    @Override
    public int returnIntBySql(HqlBuilder sqlBuilder) {
        Query q = this.getCurrentSession().createNativeQuery(sqlBuilder.getHqlString());
        this.buildParams(q, sqlBuilder);
        Object value = q.getSingleResult();
        int returnValue = -1;
        if (Validate.isObject(value)) {
            returnValue = Integer.parseInt(value.toString());
        }
        return returnValue;
    }

    @Override
    public int executeHql(HqlBuilder hqlBuilder) {
        Query<? extends Integer> q = this.getCurrentSession().createQuery(hqlBuilder.getHqlString());
        this.buildParams(q, hqlBuilder);
        return q.executeUpdate();
    }

    @Override
    public int executeSql(HqlBuilder hqlBuilder) {
        NativeQuery<? extends Integer> q = this.getCurrentSession().createNativeQuery(hqlBuilder.getHqlString());
        this.buildParams(q, hqlBuilder);
        return q.executeUpdate();
    }


    @Override
    public int deleteById(String idPropertyName, String idValue) {
        if (!Validate.isString(idPropertyName) || !Validate.isString(idValue)) {
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
        Query q = this.getCurrentSession().createNativeQuery(sqlBuilder.getHqlString());
        this.buildParams(q, sqlBuilder);
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

    @Override
    public List<Map<String, Object>> findByJdbc(HqlBuilder queryBuilder) {
        if (Validate.isEmpty(queryBuilder.getJdbcValue())) {
            return jdbcTemplate.queryForList(queryBuilder.getHqlString());
        }
        return jdbcTemplate.queryForList(queryBuilder.getHqlString(), queryBuilder.getJdbcValue());
    }
}
