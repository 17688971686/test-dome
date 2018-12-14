package cs.repository;

import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;

import cs.common.HqlBuilder;
import cs.repository.odata.ODataObj;


public interface IRepository<T, ID> {
    T findById(ID id);

    T findById(String idPropertyName, String idValue);

    T getById(ID id);

    List<T> findAll();

    List<T> findByCriteria(Criterion... criterion);

    List<T> findByOdata(ODataObj oDataObj);

    T save(T entity);

    void bathUpdate(List<T> t);

    void delete(T entity);

    void flush();

    void clear();

    void setSession(Session session);

    Criteria getExecutableCriteria();

    Session getSession();

    List<T> findByHql(HqlBuilder hqlBuilder);

    List<T> findByIds(String idPropertyName, String idValue, String orderStr);

    int executeHql(HqlBuilder hqlBuilder);

    int executeSql(HqlBuilder hqlBuilder);

    List<T> findBySql(HqlBuilder hqlBuilder);

    int returnIntBySql(HqlBuilder sqlBuilder);

    int deleteById(String idPropertyName, String idValue);

    List<Object[]> getObjectArray(HqlBuilder hqlBuilder);

    String getDataBaseTime(String format);

    /**
     * 根据JDBC查询
     * @param queryBuilder
     * @return
     */
    List<Map<String, Object>> findByJdbc(HqlBuilder queryBuilder);
}
